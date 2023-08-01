<template>
  <div class="pickTask">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPickTaskData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
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

    <el-dialog title="拣货确认" :visible.sync="dialogNewVisible" :before-close="handleClose" width="1000px" style="margin-top: -10vh;" >
      <div class="upperTacticBox">
        <el-form class="searchBar" :model="form" :rules="rules" ref="form">
        <el-form-item label="仓库"  :label-width="formLabelWidth">
          <el-input v-model="form.wareName" disabled="true" ></el-input>
        </el-form-item>
        <el-form-item label="发运单号"  :label-width="formLabelWidth">
          <el-input v-model="form.billNo" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="货主" :label-width="formLabelWidth">
          <el-input v-model="form.organizationName" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="货区编码" :label-width="formLabelWidth">
          <el-input v-model="form.areaCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="货架编码" :label-width="formLabelWidth">
          <el-input v-model="form.shelfCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="货位编码" :label-width="formLabelWidth">
          <el-input v-model="form.cellCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="SKU" :label-width="formLabelWidth">
          <el-input v-model="form.itemCode" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="SKU名称" :label-width="formLabelWidth">
          <el-input v-model="form.itemName" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="批次" :label-width="formLabelWidth">
          <el-input v-model="form.batchName" disabled="true"></el-input>
        </el-form-item>
          <el-form-item label="追踪号" :label-width="formLabelWidth">
            <el-input v-model="form.boxCode" disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="单位" :label-width="formLabelWidth">
            <el-input v-model="form.packDescribe" disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="拣货数量" :label-width="formLabelWidth">
            <el-input v-model="form.pickQuantity" disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="目标货位" prop="toCellCode" :label-width="formLabelWidth">
            <el-select v-model="form.toCellCode" filterable placeholder="请选择目标货位" size="small"
                       style="width: 70%;">
              <el-option
                      v-for="item in cells"
                      :key="item.cellCode"
                      :label="item.cellCode"
                      :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目标追踪号" prop="trackingNum" :label-width="formLabelWidth">
            <el-input v-model="form.trackingNum" placeholder="请输入目标追踪号" ></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.memo" placeholder="请输入备注" ></el-input>
          </el-form-item>
      </el-form>
        <div style="float: left;width: 100%;"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="pickTask('form')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        @selection-change="handleSelectionChange"
        :data="pickTasks"
        style="width: 100%">
        <el-table-column
                type="selection"
                width="55">
        </el-table-column>
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                label="装车数量"
                width="180">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.quantity" controls-position="right" :min="0" :max="scope.row.pickQuantity - scope.row.reviewQuantity" label="请输入数量" size="small" style="width: 90%;"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="pickQuantity"
                label="拣货数量"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="reviewQuantity"
                label="已复核数量"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="packDescribe"
                label="单位"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="billNo"
                label="发运单号"
                width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="areaCode"
            label="货区编码"
            width="100">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="areaName"
          label="货区名称"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfCode"
          label="货架编码"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfName"
          label="货架名称"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="cellCode"
          label="货位编码"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="cellName"
          label="货位名称"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="itemName"
                label="SKU名称"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="batchName"
                label="批次名称"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="boxCode"
                label="追踪号"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="lpn"
                label="lpn"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="state"
                label="状态"
                width="180">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in pickTaskType" :key="index" v-if="scope.row.state === item.value">{{item.label}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <div class="rightFloat">
        <el-button @click="commitSelect" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'ChoosePickTask',
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
        },
        cellUserTypes:{0: 0, 1: 4},
        rules: {
          toCellCode: [{required: true, message: '请输入目标货位', trigger: 'blur'}],
          trackingNum: [{required: true, message: '请输入追踪号', trigger: 'blur'}],
          memo: [{required: true, message: '请输入备注', trigger: 'blur'}],
        },
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
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      pickTask(formName) {
        let that = this;
        let param = {
          pickTaskId:this.form.pickTaskId,
          toCellCode:this.form.toCellCode,
          trackingNum:this.form.trackingNum,
          memo:this.form.memo,
        }
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
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
            this.resetForm('form')
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
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
        this.form.pickQuantity = rowData.pickQuantity;
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getPickTaskData()
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
        let that = this
        IOT.getServerData('/pick/tasks/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            that.pickTasks = []
            let list = ret.rows;
            for (let pickTask of list) {
              let rowData = $.extend(pickTask, {quantity: 0})
              that.pickTasks.push(rowData)
            }

            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
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
        IOT.getServerData('/cell/infos/list','get',{cellUseTypes:this.cellUserTypes,wareId:this.searchData.wareId},(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      commitSelect() {
        
        IOT.showOverlay('保存中，请稍等...')
        let pickTasks = PF.JSON(this.multipleSelection)
        let loadDetails = []
        for(let pickTask of pickTasks) {
          let loadDetail = {
            pickTaskId: pickTask.pickTaskId,
            pickQuantity: pickTask.pickQuantity,
            quantity: pickTask.quantity,
            packDetailId: pickTask.packDetailId,
            packDescribe: pickTask.packDescribe
          }
          loadDetails.push(loadDetail)
        }
        IOT.getServerData('/load/details/addFromPickTask','post',{loadDetails: loadDetails, loadMasterId: this.$route.params.loadMasterId},(ret) => {
          IOT.hideOverlay();
          this.multipleSelection = [];
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success');
            this.$router.push('LoadMaster')
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getPickTaskData();
      this.getCellData();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .buyBillInDraft{

  }
</style>
