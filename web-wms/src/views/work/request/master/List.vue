<template>
  <div class="asnBill">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getMasterData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
         
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
          <span>收货状态</span>
          <el-select v-model="searchData.asnState" size="mini" filterable placeholder="请选择收货状态" clearable>
            <el-option
              v-for="item in asnStates"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
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
          <span>预计到货时间</span>
          <el-date-picker
            v-model="searchData.expectTimeArr"
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
          <el-input v-model="searchData.keyWords" size="mini" placeholder="发货地址/交货地址/运输描述/自定义字段/创建人/备注" clearable></el-input>
        </div>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
       <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="$router.push({name: 'requestMasterCreate'})"> 新增</el-button>
      <el-button size="small" type="success" round icon="iconfont icon-bianji" @click="showUpdateModal(multipleSelection[0])" :disabled="multipleSelection.length === 0 || multipleSelection.length > 1">修改</el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="deleteTimeTask(multipleSelection)" :disabled="multipleSelection.length === 0">删除</el-button>
      
       <el-button class="small" size="small" type="info" plain round icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button>
    </div>

    <!-- <div class="headerBtns">
     
      <el-button class="rightFloat" size="small" type="info" plain round icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button>
    </div> -->
    <!-- <div class="tableBox">
      <el-row>
        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>

        <el-col :span="4">
          <div class="box"  >

          </div>
        </el-col>
      </el-row>
       
    </div> -->
 
   <div class="tableBox">
       <el-table
          v-loading="loading.masterTable"
          :data="asnBills"
          highlight-current-row
          @selection-change="handleSelectionChange"
          style="width: 100%">
      
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
      
         
        <el-table-column
         
          sortable
          show-overflow-tooltip
          prop="requestMasterId"
          label="单号"
        >
          
        </el-table-column>
         
         <el-table-column
          sortable
          show-overflow-tooltip
          prop="workOrderNo"
          label="工单"
        >
        </el-table-column>
       
         <el-table-column
          sortable
          show-overflow-tooltip
          prop="requestDeptName"
          label="领料部门"
          width="100"
         >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="requestUserName"
          label="领料人"
         >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="requestDate"
          label="领料时间"
          width="100"
          >
        </el-table-column>
        
         <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="状态"
          >
        </el-table-column>

      <el-table-column
          sortable
          show-overflow-tooltip
          prop="purpose"
          label="purpose"
          width="110"
           >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="way"
          label="方式"
          >
          <template slot-scope="scope">
            <span v-if="scope.row.way==1">仓库送料</span>
             <span v-if="scope.row.way==2">部门领料</span>
               </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
           >
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
         <el-table-column
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          >
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column>

          <el-table-column
          sortable
          show-overflow-tooltip
          prop="createTime"
          label="制单时间"
          width="100"
          >
           <template slot-scope="scope">
            {{scope.row.createTime.substr(2,14)}}
          </template>
          </el-table-column>

          <el-table-column
          sortable
          show-overflow-tooltip
          prop="createUserName"
          label="制单人"
          >
          
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="memo"
          label="memo"
          >
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
    name: 'AsnBill',
    data(){
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,
        },
        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          state: null,
          organizationId: null,
          supplierId: null,
          carrierId: null,
          asnState: null,
          billNo:'',
          billSource:'',
          asnType:'',
          platformCode:'',
          orderNo:'',
          createTimeArr:[],
          expectTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          startExpectTime:'',
          endExpectTime:'',
        },

        asnBills: [],
        platforms: [],
        carriers: [],

        formLabelWidth: '120px',

        asnUdfHs: [],
        organizations: [],
        suppliers: [],
        multipleSelection: [],
        asnTypes: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
        "asnStates",
        "qcStates",
      ])
    },
    methods: {

        handleSizeChange(val){
            this.searchData.pageSize = val;
            this.getMasterData();
        },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getMasterData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.state = null;
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
        this.searchData.carrierId = null;
        this.searchData.asnState = null;
        this.searchData.billNo = '';
        this.searchData.billSource = '';
        this.searchData.asnType = '';
        this.searchData.platformCode = '';
        this.searchData.orderNo = '';
        this.searchData.createTimeArr = [];
        this.searchData.expectTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
        this.searchData.startExpectTime = '';
        this.searchData.endExpectTime = '';
      },
      getMasterData(){
        var that = this;
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        if (searchData.expectTimeArr !=null && searchData.expectTimeArr.length > 0) {
          searchData.startExpectTime = searchData.expectTimeArr[0];
          searchData.endExpectTime = searchData.expectTimeArr[1];
        }
        that.loading.masterTable = true;
        IOT.getServerData('/request/masters/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            that.asnBills = ret.rows;
            
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPlatformData(){
        IOT.getServerData('/platforms/findByWareId','get',{wareId: PF.getLocal('wareId', 'number')},(ret) => {
          if (ret.code === 200) {
            this.platforms = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCarrierData(){
        IOT.getServerData('/carriers/allList','get',{},(ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
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
        IOT.getServerData('/asn/masters/file','post',{billNos: billNos},(ret) => {
          IOT.hideOverlay();
          this.multipleSelection = [];
          if (ret.code === 200) {
            IOT.tips('归档成功！', 'success');
            this.getMasterData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getMasterData();
      this.getPlatformData();
      this.getCarrierData();
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
      PF.getCarriers((rows) => {
        this.carriers = rows;
      })
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .asnBill{
    .box{
      box-shadow: 0 2px 4px #c1deff, 0 0 6px #c1deff;
      width: 90%;
      height: 300px;
      background-color: white;
      margin: 10px 5px 10px 5px ;
      border-radius: 10px;
    }
  }
</style>
