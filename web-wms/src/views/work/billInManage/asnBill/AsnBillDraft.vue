<template>
  <div class="asnBillDraft">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
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
        :data="asnBills"
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
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="requestBuyBillNo"
          label="采购单号"
          width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.requestBuyBillNo === null">无</span>
            <span v-else class="textBtn" @click="$router.push({name: 'buyBillInDetail',params: {billNo: scope.row.requestBuyBillNo}})">{{scope.row.requestBuyBillNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="asnType"
          label="入库单类型"
          width="120">
          <template slot-scope="scope">
            <span v-for="(item,index) in asnTypes" :key="index" v-if="scope.row.asnType === item.dataDictDetailId">{{item.dictValue}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="billSource"
          label="单据来源"
            width="100">
          <template slot-scope="scope">
              <el-tag v-if="scope.row.billSource === 0" type="info">采购单</el-tag>
              <el-tag v-else-if="scope.row.billSource === 1">新建</el-tag>
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
          prop="supplierName"
          label="供应商"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.supplierId, 'supplier')">{{scope.row.supplierName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="platformCode"
          label="月台编码"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="carrierName"
          label="承运方"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="loadingPlace"
          label="发货地址"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="deliveryPlace"
          label="交货地址"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="trafficDescribe"
          label="运输描述"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          v-for="(item,index) in asnUdfHs"
          :key="index"
          :prop="item.paramCode"
          :label="item.value"
          width="180">
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
          prop="expectTime"
          label="预期到货时间"
          width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,14)}}
          </template>
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
        :current-page.sync="searchData.pageNum"
        :page-sizes="[15, 30, 50, 100]"
        :page-size="searchData.pageSize"
        layout="sizes,total, prev, pager, next"
        :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'AsnBillDraft',
    data(){
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          insertType: -2,
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          supplierId: null,
        },

        asnBills: [],

        formLabelWidth: '120px',

        asnUdfHs: [],
        organizations: [],
        suppliers: [],
        asnTypes: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates"
      ])
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getAsnData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getAsnData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getAsnData();
      },
      getAsnData(){
        IOT.getServerData('/asn/masters/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.asnBills = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getAsnData();
      PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
        this.asnUdfHs = udfs;
      });
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getDataDictUdfData('asnType', (rows) => {
        this.asnTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .asnBillDraft{

  }
</style>
