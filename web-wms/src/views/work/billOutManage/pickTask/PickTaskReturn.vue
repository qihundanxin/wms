<template>
  <div class="pickTask">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPickTaskData" size="mini" icon="el-icon-search">查询</el-button>
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
              <el-input v-model="searchData.areaCode" size="mini" placeholder="请输入货架编码/名称" clearable></el-input>
          </div>
          <div>
              <span>货架编码/名称</span>
              <el-input v-model="searchData.shelfCode" size="mini" placeholder="请输入货区编码/名称" clearable></el-input>
          </div>
          <div>
              <span>货位编码/名称</span>
              <el-input v-model="searchData.cellCode" size="mini" placeholder="请输入货位编码/名称" clearable></el-input>
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
      <!-- <el-button size="small" type="success" round  @click="commitCancel()" :disabled="multipleSelection.length <= 0"> 批量取消
      </el-button> -->
      <!-- <div class="rightFloat" v-if="sysJob.status !== null || sysJob.status !== undefined || sysJob.status !== ''">
        <span>自动生成拣货任务&nbsp;:&nbsp;</span>
        <el-switch
                v-model="sysJob.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="是"
                inactive-text="否"
                active-value="0"
                inactive-value="1"
                @change="editSysJob"
        >
        </el-switch>
      </div> -->
    </div>

    <el-dialog title="上架" :visible.sync="dialogNewVisible" :before-close="handleClose" width="1000px" style="margin-top: -10vh;" >
      <div class="upperTacticBox">
        <el-form class="searchBar" :model="form" :rules="rules" ref="form">
        
    
        <el-form-item label="SKU" :label-width="formLabelWidth">
          <el-input v-model="form.itemCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="SKU名称" :label-width="formLabelWidth">
          <el-input v-model="form.itemName" disabled="true"></el-input>
        </el-form-item>
         
          <el-form-item label="数量" :label-width="formLabelWidth">
            <el-input v-model="form.quantity" disabled="true"></el-input>
          </el-form-item>
              <el-form-item label="货位编码" :label-width="formLabelWidth">
          <el-input v-model="form.commitCellCode"  ></el-input>
        </el-form-item>
         
          
      </el-form>
        <div style="float: left;width: 100%;"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="pickTask('form')">确 定</el-button>
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
                type="selection"
                width="50">
        </el-table-column>
       
      
         <el-table-column
          
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          min-width="150">
          <template slot-scope="scope">
              <span>{{ scope.row.itemCode }}</span>
              <br/> 
              <span>{{ scope.row.itemName }}</span>
          </template>
        </el-table-column>
       
    
        
       
        <el-table-column
          
          show-overflow-tooltip
          prop="quantity"
          label="数量"
          min-width="70">
          <template slot-scope="scope">
            <span>{{ scope.row.quantity }}</span>
          </template>
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="boxCode"
          label="boxCode"
          min-width="70">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="fromInventoryId"
          label="fromInventoryId"
          min-width="70">
          
        </el-table-column>
  
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="toCellCode"
          label="推荐货位"
          min-width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.toCellCode }}</span> 
          </template> 
        </el-table-column>
      
        
       
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="状态"
          min-width="80">
          <template slot-scope="scope">
               <span  v-if="scope.row.state==0" style="color:#E6A23C">待上架</span> 
               <span  v-if="scope.row.state==1"  style="color:#67C23A"  >已完成</span> 
          </template>
        </el-table-column>

        <el-table-column
          sortable
          show-overflow-tooltip
          prop="type"
          label="类型"
          min-width="80">
          <template slot-scope="scope">
            
            <span  v-if="scope.row.type==1" style="color:#909399">配货异常</span> 
            <span  v-if="scope.row.type==2"  style="color:#F56C6C"  >订单修改（未发运）</span> 
            <span  v-if="scope.row.type==3"  style="color:#F56C6C"  >订单修改（已发运）</span>  
          </template>
        </el-table-column>
 

        
         
        <!-- <el-table-column
          fixed="right"
          label="操作"
          min-width="120">
          <template slot-scope="scope">
            <el-button  @click="pickTaskAffirm(scope.row)"   type="primary"  
                       size="small" :disabled="scope.row.state === 2">&nbsp;拣货确认</el-button>
          </template>
        </el-table-column> -->
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
      changeSerialNos(){
        for(let i=0;i<this.serialNo1.length;i++){
          if(this.scanner === this.serialNo1[i].serialNo){
            if(this.form.serialNos.length>0){
              for(let j=0;j<this.form.serialNos.length;j++){
               if(this.scanner === this.form.serialNos[j]){
                 IOT.tips('当前序列号已录入!', 'error', 1000, () => {
                   this.scanner = '';
                    let pFocus = document.getElementById("scanner");
                    pFocus.focus();
                  });
                 return false
               }
              }
            }
            this.form.serialNos.push(this.serialNo1[i].serialNo);
            this.scanner = '';
            let pFocus = document.getElementById("scanner");
            pFocus.focus();
            document.getElementById("serialNoId"+this.serialNo1[i].serialNoId+"").style.color = "red";
            break;
          }else if(i === this.serialNo1.length-1){
            IOT.tips('当前货物无此序列号!', 'error', 1000, () => {
              this.scanner = '';
              let pFocus = document.getElementById("scanner");
              pFocus.focus();
            });
            return false;
          }
        }
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
      pickTask(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let a = Math.ceil(that.form.pickQuantity);
            if(that.form.serialNos.length <= 0 && this.serialNo1.length > 0){
              this.$confirm('当前拣货未录入序列号,会影响后续序列号追踪,是否继续？')
                      .then(_ => {
                        this.pickTaskAddirms(formName);
                      })
                      .catch(_ => {
                        IOT.hideOverlay();
                      });
            }else if(that.form.serialNos.length > 0 && that.serialNo1.length > 0 && that.form.serialNos.length < a){
              this.$confirm('当前拣货录入序列号与数量不一致,会影响后续序列号追踪,是否继续？')
                      .then(_ => {
                        this.pickTaskAddirms(formName);
                      })
                      .catch(_ => {
                        IOT.hideOverlay();
                      });
            }else{
              this.pickTaskAddirms(formName);
            }
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      pickTaskAddirms(formName){
        let param = {
          pickTaskId:this.form.pickTaskId,
          toCellCode:this.form.toCellCode,
          trackingNum:this.form.trackingNum,
          memo:this.form.memo,
          serialNos:this.form.serialNos,
          wareId:this.wareId
        }
        IOT.getServerData('/pick/tasks/pickTaskAffirm', 'post', param, (ret) => {
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
        this.dialogNewVisible = true;
        let rowData = JSON.parse(JSON.stringify(row));
        this.form.pickTaskId = rowData.pickTaskId;
        this.form.wareName = rowData.wareName;
        this.form.billNo = rowData.billNo;
        this.form.organizationName = rowData.organizationName;
        this.form.areaCode = rowData.areaCode;
        this.form.shelfCode = rowData.shelfCode;
        this.form.cellCode = rowData.cellCode;
        this.form.itemCode = rowData.itemCode;
        this.form.itemName = rowData.itemName;
        this.form.batchName = rowData.batchName;
        this.form.boxCode = rowData.boxCode;
        this.form.packDescribe = rowData.packDescribe;
        this.form.quantity = rowData.quantity;
        IOT.getServerData('/serial/nos/list', 'get', {type:2,inventoryId:row.inventoryId}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.serialNo1 = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
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
      getPickTaskData(){
        let that = this;
        that.loading.masterTable = true;
      //  this.searchData.state=0;
      
        IOT.getServerData('/pick/task/returns/findList','get',this.searchData,(ret) => {
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
