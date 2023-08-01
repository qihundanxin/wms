<template>
  <div class="qcMaster">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getQcBillData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>质检单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="质检单号" clearable></el-input>
        </div>
        <div>
          <span>质检状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择质检状态" clearable>
            <el-option
              v-for="item in reviewStates"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>质检类型</span>
          <el-select v-model="searchData.qcType" size="mini" filterable placeholder="请选择质检类型" clearable>
            <el-option
              v-for="item in qcTypes"
              :key="item.dataDictDetailId"
              :label="item.dictValue"
              :value="item.dataDictDetailId">
            </el-option>
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
          <span>入库单号</span>
          <el-input v-model="searchData.asnBillNo" size="mini" placeholder="入库单号" clearable></el-input>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker
                  v-model="searchData.createTimeArr"
                  size="mini"
                  type="daterange"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" title="创建人/自定义字段/备注" placeholder="创建人/自定义字段/备注" clearable></el-input>
        </div>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary"    icon="iconfont icon-xinzeng" @click="$router.push({name: 'createQc'})"> 新增</el-button>
      <el-button class="rightFloat" size="small" type="info" plain    icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button>
  </div>

    <div class="tableBox">
      <el-table
      v-loading="loading.masterTable"
        highlight-current-row
        :data="qcBills"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column
          type="selection"
          width="30">
        </el-table-column>
        <el-table-column
          fixed="left"
          type="index"
          width="40">
        </el-table-column>
        <el-table-column
          fixed="left"
          sortable
          show-overflow-tooltip
          prop="billNo"
          label="质检单号"
          width="120">
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
          width="120">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: {billNo: scope.row.asnBillNo}})">{{scope.row.asnBillNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="billSource"
                label="单据来源"
                width="90">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.billSource === 0" type="info">入库单生成</el-tag>
            <el-tag v-else-if="scope.row.billSource === 1">库存生成</el-tag>
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
        <!--<el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="质检状态"
          width="110">
          <template slot-scope="scope">
            <span v-if="scope.row.state === 1">部分质检</span>
            <span v-else-if="scope.row.state === 0">全部质检</span>
          </template>
        </el-table-column>-->
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qcType"
          label="质检类型"
          width="110">
          <template slot-scope="scope">
            <span v-for="(item,index) in qcTypes" :key="index" v-if="item.dataDictDetailId === scope.row.qcType">{{item.dictValue}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
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
          prop="createTime"
          label="创建时间"
          width="110">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,11)}}
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="createUserName"
          label="创建人"
          width="120">
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
         class="pagination-bottom"
        style="text-align: right;"
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
    name: 'QcMaster',
    data(){
      return {
        loading:{
            masterTable:true,
        },
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          state: null,
          organizationId: null,
          supplierId: null,
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          billNo:'',
          asnBillNo:'',
          qcType:''
        },

        qcBills: [],

        formLabelWidth: '120px',

        multipleSelection: [],
        organizations: [],
        suppliers: [],
        qcUdfHs: [],
        qcTypes: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
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
        this.searchData.state = null;
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
        this.searchData.billNo = '';
        this.searchData.asnBillNo = '';
        this.searchData.qcType = '';
        this.searchData.createTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
      },
      getQcBillData(){
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        if(!PF.isNull(searchData.billNo)){
          delete searchData.billNo
        }
        if(!PF.isNull(searchData.asnBillNo)){
          delete searchData.asnBillNo
        }

        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/qc/masters/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.qcBills = ret.rows;
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
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      commitFile(){
        IOT.showOverlay('保存中，请稍等...');
        let list = PF.JSON(this.multipleSelection);
        let billNos = [];
        for(let i=0;i<list.length;i++){
          billNos.push(list[i].billNo)
        }
        IOT.getServerData('/qc/masters/file','post',{billNos: billNos},(ret) => {
          IOT.hideOverlay();
          this.multipleSelection = [];
          if (ret.code === 200) {
            IOT.tips('归档成功！', 'success');
            this.getQcBillData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getQcBillData();
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
  .qcMaster{

  }
</style>
