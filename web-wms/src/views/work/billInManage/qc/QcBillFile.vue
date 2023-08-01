<template>
  <div class="qcBillFile">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getBuyBillInData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div>
        <div>
          <span>单据来源</span>
          <el-select v-model="searchData.billSource" size="mini" filterable placeholder="请选择单据来源" clearable>
            <el-option :key="0" label="入库单" :value="0"></el-option>
            <el-option :key="1" label="新建" :value="1"></el-option>
          </el-select>
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
        <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
            <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
            </el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="buyBillIns"
        style="width: 100%">
        </el-table-column>
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
          label="质检单号"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'qcBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="left"
          sortable
          show-overflow-tooltip
          prop="asnBillNo"
          label="入库单号"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: {billNo: scope.row.asnBillNo}})">{{scope.row.asnBillNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="质检状态"
          width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.state === 1">部分质检</span>
            <span v-else-if="scope.row.state === 0">全部质检</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qcType"
          label="质检类型"
          width="180">
          <template slot-scope="scope">
            <span v-for="(item,index) in qcTypes" :key="index" v-if="item.dataDictDetailId === scope.row.qcType">{{item.dictValue}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="createTime"
          label="创建时间"
          width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,14)}}
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
        <el-table-column
          sortable
          show-overflow-tooltip
          v-for="(item,index) in qcUdfHs"
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
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'QcBillFile',
    data(){
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          billSource: null,
          organizationId: null,
          supplierId: null,
        },

        buyBillIns: [],

        formLabelWidth: '120px',

        qcUdfHs: [],
        organizations: [],
        suppliers: [],
        qcTypes: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
        "asnStates"
      ])
    },
    methods: {

       handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getQcBillData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getQcBillData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.billSource = null;
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
      },
      getBuyBillInData(){
        IOT.getServerData('/qc/master/files/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.buyBillIns = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            PF.getSysParamUdfData('qcUdfHs', [], (data, udfs) => {
              this.qcUdfHs = udfs;
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getBuyBillInData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getDataDictUdfData('qcType', (rows) => {
        this.qcTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .qcBillFile{

  }
</style>
