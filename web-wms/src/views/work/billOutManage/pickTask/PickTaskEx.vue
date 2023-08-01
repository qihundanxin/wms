<template>
  <div class="pickTask">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <!--<div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="仓库名称/货主名称" clearable></el-input>
        </div>-->
          <div>
              <span>发运单号</span>
              <el-input v-model="searchData.billNo" size="mini" placeholder="请输入发运单号" clearable></el-input>
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
              <span>SKU</span>
              <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入SKU" clearable></el-input>
          </div>
          <div>
              <span>SKU名称</span>
              <el-input v-model="searchData.itemName" size="mini" placeholder="请输入SKU名称" clearable></el-input>
          </div>
          <div>
              <span>单据来源</span>
              <el-select v-model="searchData.source" size="mini" filterable placeholder="请选择状态" clearable>
                  <el-option
                          v-for="item in createSource"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
              </el-select>
          </div>
          <div>
            <span>拣货状态</span>
             <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择来源" clearable>
              <el-option
                v-for="item in pickTaskType"
               :key="item.value"
               :label="item.label"
               :value="item.value">
             </el-option>
            </el-select>
          </div>
          <div>
              <span>货区编码/名称</span>
              <el-input style="width:160px" v-model="searchData.areaCode" size="mini" placeholder="请输入货区编码/名称" clearable></el-input>
          </div>
          <div>
              <span>货架编码/名称</span>





              
              <el-input style="width:160px" v-model="searchData.shelfCode" size="mini" placeholder="请输入货架编码/名称" clearable></el-input>
          </div>
          <div>
              <span>货位编码/名称</span>
              <el-input style="width:160px" v-model="searchData.cellCode" size="mini" placeholder="请输入货位编码/名称" clearable></el-input>
          </div>
          <div>
              <span>关键字搜索</span>
              <el-input v-model="searchData.keyWords" size="mini" placeholder="箱号/LPN" clearable></el-input>
          </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
       
    </div>

    <el-dialog title="异常处理" :visible.sync="dialogNewVisible" :before-close="handleClose" width="1000px" style="margin-top: -10vh;" >
      <div class="upperTacticBox">
        <el-form class="searchBar" :model="form" :rules="rules" ref="form">
       
        
        <el-form-item label="货位编码" :label-width="formLabelWidth">
          <el-input v-model="form.cellCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="SKU" :label-width="formLabelWidth">
          <el-input v-model="form.itemCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="SKU名称" :label-width="formLabelWidth">
          <el-input v-model="form.itemName" disabled="true"></el-input>
        </el-form-item>
       
        <el-form-item label="账面数量" :label-width="formLabelWidth">
          <el-input v-model="form.pickQuantity" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="锁定数量" :label-width="formLabelWidth">
          <el-input v-model="form.pickQuantity" disabled="true"></el-input>
        </el-form-item>
          <el-form-item label="实际数量" :label-width="formLabelWidth">
          <el-input v-model="form.commitQuantity"  ></el-input>
        </el-form-item>
           
          
      </el-form>
        <div style="float: left;width: 100%;"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="pickTaskExs('form')">确 定</el-button>
      </div>
    </el-dialog>

  


    <!-- :span-method="objectSpanMethod"  -->
    <div class="tableBox">
      <el-table
         
        v-loading="loading.masterTable"
        highlight-current-row
        
        @selection-change="handleSelectionChange"
        :data="pickTasks"
        style="width: 100%">
       
       
        
         
         <el-table-column
          
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          min-width="100">
          <template slot-scope="scope">
              <span>{{ scope.row.itemCode }}</span>
              <br/> 
              <span>{{ scope.row.itemName }}</span>
          </template>
        </el-table-column>
        
        <!-- <el-table-column
          
          show-overflow-tooltip
          prop="pickTaskId"
          label="pickTaskId"
          min-width="40">
        </el-table-column> -->
        <el-table-column
          
          show-overflow-tooltip
          prop="totalQuantity"
          label="需求数"
          min-width="70">
          <template slot-scope="scope">
            <span>{{ scope.row.totalQuantity  }}</span>
          </template>
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="pickQuantity"
          label="实际拣货数"
          min-width="70">
          <template slot-scope="scope">
            <span>{{ scope.row.pickQuantity   }}</span>
          </template>
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="exQuantity"
          label="缺货数"
          min-width="70">
          <template slot-scope="scope">
            <span>{{ scope.row.exQuantity  }}</span>
          </template>
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="freezeQuantity"
          label="库存冻结数"
          min-width="70">
          <template slot-scope="scope">
            <span>{{ scope.row.freezeQuantity  }}</span>
          </template>
        </el-table-column>
    
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="cellCode"
          label="货位"
          min-width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.cellCode }}</span>
           
          </template>

        </el-table-column>

        <el-table-column
          sortable
          show-overflow-tooltip
          prop="stae"
          label="状态"
          min-width="100">
          <template slot-scope="scope">
            <span style="color:#E6A23C" v-if="scope.row.state==0">待处理</span>
            <span  style="color:#67C23A" v-if="scope.row.state==1">已处理</span>
           
          </template>

        </el-table-column>
        
    
        
       
        <el-table-column
          fixed="right"
          label="操作"
          min-width="120">
          <template slot-scope="scope">
            <el-button  @click="pickTaskAffirm(scope.row)"   type="primary"  
                       size="small" :disabled="scope.row.state === 2">&nbsp;异常处理</el-button>
          </template>
        </el-table-column>

        
       
      </el-table>
      <el-pagination
        class="pagination-bottom"
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        @size-change="handleSizeChange"
        :page-sizes="[15, 30, 50, 100]"
        layout="sizes,total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'pickTask',
    data(){
      return {
        isMoreSearch: false,
        wareId: PF.getLocal('wareId', 'number'),
        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          insertType: -2,
          wareId: PF.getLocal('wareId', 'number'),
          billNo:'',
          organizationId: '',
            itemCode:'',
            itemName:'',
            state:'',
            source:'',
            areaCode:'',
            shelfCode:'',
            cellCode:'',
        },

        loading:{
          masterTable:true,
        },

        rows: [],
        spans: [],
        pickTasks: [],

        cancelButton:false,
        dialogNewVisible:false,
        formLabelWidth: '100px',

        poUdfHs: [],
        buyBillInTypes: [],
        organizations: [],
        suppliers: [],
        multipleSelection:[],
        cells:'',
        form:{
          pickTaskId:'',
          wareName:'',
          billNo:'',
          organizationName:'',
          areaCode:'',
          shelfCode:'',
          cellCode:'',
          itemCode:'',
          itemName:'',
          batchName:'',
          boxCode:'',
          packDescribe:'',
          pickQuantity:'',
          toCellCode:'',
          trackingNum:'',
          memo:'',
          serialNos:[],
        },
        rules: {
          toCellCode: [{required: true, message: '请输入目标货位', trigger: 'blur'}],
          // trackingNum: [{required: true, message: '请输入追踪号', trigger: 'blur'}],
        },

        sysJob:{},

        serialNo:false,
        serialNo1:[],
        scanner:'',
        select:true,
        cellUserTypes:{0: -1, 1: 3},
      }
    },
    computed: {
      ...mapState([
        "reviewStates",
        "pickTaskType",
        "createSource",
      ])
    },
    methods: {


      handleClose(done) {
        this.dialogNewVisible = false;
      },
      addSerialNo(){
        if(this.select){
          this.changeSerialNos();
          let pFocus = document.getElementById("scanner");
          pFocus.focus();
        }
      },
      affirmSerialNo(){
        this.changeSerialNos();
        let pFocus = document.getElementById("scanner");
        pFocus.focus();
      },
     
      handleCloseSerialNo(){
        this.serialNo = false;
        this.scanner = '';
      },
      showSerialNo(){
        this.serialNo = true;
        let pFocus = document.getElementById("scanner");
        pFocus.focus();
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      
      pickTaskExs(formName){
        let param = {
          id:this.form.pickTaskExId,
          
          commitQuantity:this.form.commitQuantity
        }
        IOT.getServerData('/pick/task/exs/commit', 'get', param, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              this.getPickTaskData();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.resetForm(formName)
        this.dialogNewVisible = false
      },
      pickTaskAffirm(row){
       
        let rowData = JSON.parse(JSON.stringify(row));
        this.form=rowData;
          this.dialogNewVisible = true;
      },
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getPickTaskData()
      },
        handleSizeChange(val){
            this.searchData.pageSize = val;
            this.getPickTaskData();
        },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clearSearchData(){
          this.searchData.keyWords = '';
          this.searchData.billNo = '';
          this.searchData.organizationId = '';
          this.searchData.itemCode = '';
          this.searchData.itemName = '';
          this.searchData.state = '';
          this.searchData.source = '';
          this.searchData.areaCode = '';
          this.searchData.shelfCode = '';
          this.searchData.cellCode = '';
      },
      setPageNumFlush(){
          this.searchData.pageNum =1;
          this.getPickTaskData();
      },
      getPickTaskData(){
        let that = this;
        that.loading.masterTable = true;
      //  this.searchData.state=0;
        IOT.getServerData('/pick/task/exs/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            let billNos = [];
            let spans = [];
            let rows = [];
            for (let i = 0; i < list.length; i++) {
              let index = billNos.indexOf(list[i].billNo);
              if (index === -1) {
                billNos.push(list[i].billNo);
                rows.push(i);
                spans.push({rowspan: 1, colspan: 1});
              } else if (index >= 0) {
                spans[index].rowspan++
              }
            }
            this.rows = rows;
            this.spans = spans;
            this.pickTasks = list;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
            that.loading.masterTable = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitCancel(){
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        let form = []
        form = PF.JSON(this.multipleSelection);
        let ids = [];
        for (let i = 0; i < form.length; i++) {
          if (form[i].state === 3) {
            IOT.tips('请选择未取消的拣货任务！', 'error', 1000, () => {s
              IOT.hideOverlay();
            });
            return false
          }
          ids.push(form[i].pickTaskId);
        }
        IOT.getServerData('/pick/tasks/cancelPickTask', 'post', {ids: ids}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('取消成功！', 'success', 1000, () => {
              that.getPickTaskData();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData(){
        IOT.getServerData('/cell/infos/findByCellUserTypes','get',{cellUseTypes:this.cellUserTypes,wareId:this.searchData.wareId},(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getStatus(){
        IOT.getServerData('/monitor/job/list','get',{methodName:"autoCreatePickTask",methodParams:this.searchData.wareId+''},(ret) => {
          if (ret.code === 200) {
            this.sysJob = ret.rows[0];
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      editSysJob(){
        IOT.getServerData('/monitor/job/changeStatus','post',this.sysJob,(ret) => {
          if (ret.code === 200) {
            // IOT.tips('修改成功！', 'success', 1000, () => {
            // });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        let index = this.rows.indexOf(rowIndex);
        if (column.property === 'billNo') {
          if (index >= 0) {
            return this.spans[index];
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
    },
    created() {
      
      this.getPickTaskData();
      this.getCellData();
      //this.getStatus();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
      /*PF.getSysParamUdfData('poUdfHs', [], (data, udfs) => {
        this.poUdfHs = udfs;
      });
      PF.getDataDictUdfData('buyBillInType', (rows) => {
        this.buyBillInTypes = rows;
      });

      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });*/
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .buyBillInDraft{

  }
</style>
