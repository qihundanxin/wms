<template>
  <div class="saleBill">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="客户" clearable></el-input>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <!-- <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
            <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
            </el-option>
          </el-select>
        </div> -->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng" @click="$router.push({name: 'createSaleBill'})"> 新增</el-button>
<!--      <el-button class="rightFloat" size="small" type="info" plain round icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button>-->
    </div>

    <div class="tableBox">
      <el-table
          highlight-current-row
          :data="saleBills"
          style="width: 100%">
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            sortable
            show-overflow-tooltip
            prop="billNo"
            label="单号"
            width="120">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'saleBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="insertType"
            label="保存类型"
            width="100">
          <template slot-scope="scope">
            <el-tag v-show="scope.row.insertType === 0">单据</el-tag>
            <el-tag v-show="scope.row.insertType === -2" type="info">草稿</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="saleState"
            label="销售状态"
            width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.saleState === 0">初始化</span>
            <span v-else-if="scope.row.saleState === 2">部分发货</span>
            <span v-else-if="scope.row.saleState === 3">全部发货</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="state"
            label="审核状态"
            width="100">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.state === item.value" :type="item.type">{{item.label}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="organizationName"
            label="货主"
            width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="customerName"
            label="客户"
            width="100">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{scope.row.customerName}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="expectTime"
            label="预期到货时间"
            width="160">
          <template slot-scope="scope">
            <span>{{scope.row.expectTimeStr}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            v-for="(item,index) in saleUdfHs"
            :key="index"
            :prop="item.paramCode"
            :label="item.value"
            width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="memo"
            label="备注"
            width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="createTime"
            label="创建时间"
            width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.createTime">{{scope.row.createTime.substr(0,16)}}</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="createUserName"
            label="创建人"
            width="180">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{scope.row.createUserName}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          style="text-align: right;"
          class="pagination-bottom"
          @current-change="handleCurrentChange"
          :current-page.sync="searchData.pageNum"
          :page-size="searchData.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";
  import DateUtils from "@/utils/DateUtils";

  export default{
    name: 'saleBill',
    data(){
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          // insertType: -2,
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          supplierId: null,
        },

        saleBills: [],

        formLabelWidth: '120px',

        saleUdfHs: [],
        organizations: [],
        suppliers: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates"
      ])
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getSaleData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getSaleData();
      },
      getSaleData(){
        IOT.getServerData('/sale/masters/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            ret.rows.map(row => row.expectTimeStr = DateUtils.formatDay(row.expectTime));
            this.saleBills = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getSaleData();
      PF.getSysParamUdfData('saleUdfHs', [], (data, udfs) => {
        this.saleUdfHs = udfs;
      });
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .saleBill{

  }
</style>
