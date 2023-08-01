<template>
  <div class="inventoryAdjust">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>调整单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="调整单号" clearable></el-input>
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
          <span>审核状态</span>
          <el-select v-model="searchData.caseState" size="mini" filterable placeholder="请选择审核状态" clearable>
            <el-option
                    v-for="item in reviewStates"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>调整原因</span>
          <el-select v-model="searchData.reason" size="mini" filterable placeholder="请选择调整原因" clearable>
            <el-option
                    v-for="item in reasons"
                    :key="item.reason"
                    :label="item.reason"
                    :value="item.reason">
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
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="原因描述/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增库存调整单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="80%" style="margin-top: -10vh;">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.wareId" disabled filterable placeholder="请选择仓库"
            >
              <el-option
                      :key="form.wareId"
                      :label="form.wareName"
                      :value="form.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.organizationId" @change="changeOrganizationId" filterable placeholder="请选择货主"
            >
              <el-option
                      v-for="item in organizations"
                      :key="item.organizationId"
                      :label="item.organizationName"
                      :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="调整原因" prop="reason" :label-width="formLabelWidth">
            <el-select v-model="form.reason" filterable allow-create placeholder="请选择调整或输入调整原因">
              <el-option
                      v-for="item in reasons"
                      :key="item.reason"
                      :label="item.reason"
                      :value="item.reason">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="原因描述" prop="reasonDescription" :label-width="formLabelWidth">
            <el-input v-model="form.reasonDescription" placeholder="请输入原因描述" ></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          
          
        </el-form>
        <div class="headerBtns">
          <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectInventoryModal">选择库存</el-button>
        </div>
        <div class="  noHeight">
          <el-table
                  highlight-current-row
                  :data="form.adjustDetails"
                  max-height="360"
                  style="width: 100%">
            <el-table-column
                    fixed="left"
                    type="index"
                    width="50">
            </el-table-column>
            <el-table-column
                    fixed="left"
                      show-overflow-tooltip
                    prop="itemName"
                    label="SKU名称"
                    width="150">
            </el-table-column>
            <el-table-column
                    show-overflow-tooltip
                    label="调整数量"
                    width="180">
              <template slot-scope="scope">
                <input type="number" class="my_input small" 
                :min="scope.row.allotQuantity+scope.row.freezeQuantity"
             
                 v-model.number="scope.row.adjustQuantity"  placeholder="调整数量" style="width: 80%;">
                <!--                <el-input-number v-model="form.adjustDetails[scope.$index].freezeQuantity" controls-position="right" :min="0" :max="scope.row.quantity - scope.row.nowFreezeQuantity" style="width: 90%;"></el-input-number>-->
              </template>
            </el-table-column>
            <!--<el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="nowFreezeQuantity"
                    label="已冻结数量"
                    width="120">
            </el-table-column>-->
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="quantity"
                    label="数量"
                    width="120">
            </el-table-column>
             <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="allotQuantity"
                    label="分配数量"
                    width="100">
            </el-table-column>
             <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="freezeQuantity"
                    label="已冻结数量"
                    width="120">
            </el-table-column> 
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="packDescribe"
                    label="单位"
                    width="100">
            </el-table-column>
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="batchName"
                    label="批次"
                    width="150">
            </el-table-column>
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="itemCode"
                    label="SKU"
                    width="150">
            </el-table-column>
           
           <!-- <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="wareName"
                    label="仓库"
                    width="180">
            </el-table-column>-->
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="cellCode"
                    label="货位"
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
                    label="LPN码"
                    width="180">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="60">
              <template slot-scope="scope">
                <el-button @click="deleteDetail(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <el-dialog width="70%" title="选择库存" :visible.sync="innerVisibleInventory" append-to-body style="margin-top: -10vh;">

          <div class="publicSearch body">
            <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
              <el-button-group>
                <el-button @click="getInventoryData" size="mini" icon="el-icon-search">查询</el-button>
                <el-button @click="clearSearchSelectInventory" size="mini" icon="el-icon-refresh">重置</el-button>
              </el-button-group>
              <div>
                <span>关键字搜索</span>
                <el-input v-model="searchSelectInventory.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
              </div>
              <div>
                <span>货位</span>
                <el-input v-model="searchSelectInventory.eqCellCode" size="mini" placeholder="请输入货位" clearable></el-input>
              </div>
              <div>
                <span>批次</span>
                <el-input v-model="searchSelectInventory.eqBatchName" size="mini" placeholder="请输入批次" clearable></el-input>
              </div>
              <!--              <div>-->
              <!--                <span>仓库</span>-->
              <!--                <el-select v-model="searchSelectInventory.wareId" size="mini" filterable placeholder="请选择仓库" clearable>-->
              <!--                  <el-option-->
              <!--                    v-for="item in wares"-->
              <!--                    :key="item.wareId"-->
              <!--                    :label="item.wareName"-->
              <!--                    :value="item.wareId">-->
              <!--                  </el-option>-->
              <!--                </el-select>-->
              <!--              </div>-->
              <!--              <div>-->
              <!--                <span>货主</span>-->
              <!--                <el-select v-model="searchSelectInventory.organizationId" size="mini" filterable placeholder="请选择货主" clearable>-->
              <!--                  <el-option-->
              <!--                    v-for="item in organizations"-->
              <!--                    :key="item.organizationId"-->
              <!--                    :label="item.organizationName"-->
              <!--                    :value="item.organizationId">-->
              <!--                  </el-option>-->
              <!--                </el-select>-->
              <!--              </div>-->
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch2 = !isMoreSearch2"></span>
          </div>

          <div   >
            <el-table
                    max-height="500"
                    :data="inventorys"
                    @selection-change="handleSelectionChange"
                    @sort-change="sortChange"
                    style="width: 100%;">
              <el-table-column
                      fixed="left"
                      type="selection"
                      width="55">
              </el-table-column>
              <el-table-column
                      fixed="left"
                      show-overflow-tooltip
                      prop="itemName"
                      label="SKU名称"
                      width="180">
                       <template slot-scope="scope">
                        <span>{{scope.row.itemName}}</span>
                    </template>
                   
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="itemCode"
                      label="SKU"
                      width="180">
              </el-table-column>
              <!--<el-table-column
                      show-overflow-tooltip
                      prop="organizationName"
                      label="货主"
                      width="180">
              </el-table-column>-->
              <el-table-column
                      sortable="custom"
                      show-overflow-tooltip
                      prop="batchName"
                      label="批次"
                      width="180">
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="packDescribe"
                      label="单位"
                      width="180">
              </el-table-column>
              <el-table-column
                      sortable="custom"
                      show-overflow-tooltip
                      prop="quantity"
                      label="数量"
                      width="180">
              </el-table-column>
              <el-table-column
                      sortable="custom"
                      show-overflow-tooltip
                      prop="allotQuantity"
                      label="分配数量"
                      width="180">
              </el-table-column>
              <el-table-column
                      sortable="custom"
                      show-overflow-tooltip
                      prop="freezeQuantity"
                      label="冻结数量"
                      width="180">
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="wareName"
                      label="仓库"
                      width="180">
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="cellCode"
                      label="货位"
                      width="180">
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="boxCode"
                      label="追踪号"
                      width="180">
              </el-table-column>
              <el-table-column
                      show-overflow-tooltip
                      prop="lpn"
                      label="LPN码"
                      width="180">
              </el-table-column>
            </el-table>
            <el-pagination
                    style="text-align: right;"
                    @current-change="handleCurrentChange2"
                    :current-page.sync="searchSelectInventory.pageNum"
                    :page-size="searchSelectInventory.pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="searchSelectInventory.total">
            </el-pagination>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button @click="innerVisibleInventory = false">取 消</el-button>
            <el-button type="primary" @click="clickSelectInventory" :disabled="isFreezeBtn">确 定</el-button>
          </div>

        </el-dialog>
      </div>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button class="leftFloat" type="primary" plain @click="commitForm('form',-2)">保存为草稿</el-button> -->
        <el-button @click="resetForm('form'),dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form',0)">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              :data="inventoryAdjusts"
              style="width: 100%">
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                fixed="left"
                sortable
                prop="billNo"
                label="单号"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'inventoryAdjustDetail',params: scope.row})">{{scope.row.billNo}}</span>
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
                prop="caseState"
                label="审核状态"
                width="100">
          <template slot-scope="scope">
            <div v-show="scope.row.insertType === 0">
              <el-tag v-show="scope.row.caseState === -1" type="danger">不通过</el-tag>
              <el-tag v-show="scope.row.caseState === 0">流转中</el-tag>
              <el-tag v-show="scope.row.caseState === 1" type="success">通过</el-tag>
              <el-tag v-show="scope.row.caseState === 2" type="info">初始化</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="type"
                label="类型"
                width="180">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in inventoryAdjustType" :key="index" v-if="scope.row.type === item.value">{{item.label}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="reason"
                label="调整原因"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="reasonDescription"
                label="原因描述"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="备注">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="userName"
                label="创建人"
                width="150">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.userId, 'account')" type="text">{{scope.row.userName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="createTime"
                label="创建时间"
                width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(0,16)}}
          </template>
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
  import { runInThisContext } from "vm";
import {mapState} from "vuex";

  export default{
    name: 'inventoryAdjust',
    data(){
      return {
        isMoreSearch: false,
        isMoreSearch2: false,
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
          billNo:'',
          caseState:'',
          organizationId:null,
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          reason:'',
        },
        reasons:[],
        searchSelectInventory:{
          pageNum: 1,
          pageSize: 25,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          eqCellCode:'',
          eqBatchName:'',
          cellUseTypeNotIn:[],
        //  cellUseType: 2,
          sort:null
        },

        wares: [],
        organizations: [],
        inventoryAdjusts: [],
        inventorys: [],
        multipleSelection: [],
        selectIds: [],


        dialogNewVisible: false,
        dialogUpdateVisible: false,
        innerVisibleInventory: false,

        formLabelWidth: '120px',

        form: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          wareCode:'',
          reason: '',
          memo: '',
          type:2,
          organizationId: null,
          organizationName: '',
          reasonDescription:'',
          adjustDetails: []
        },
        rules:{
          reason: [
            { required: false, message: '请选择或输入调整原因', trigger: 'blur' }
          ],
          reasonDescription:[{required:false,message:'请输入原因描述',trigger:'blur'}],
          memo: [
            { required: false, message: '请输入备注', trigger: 'blur' }
          ],
          organizationId: [
            { required: true, message: '请选择货主', trigger: 'blur' }
          ],
        },
        isClickSelectInventory:false

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
              "inventoryAdjustType",
      ]),
      isFreezeBtn(){
        if(this.multipleSelection.length <= 0 || this.isClickSelectInventory==true){
          return true
        }
        let organizationId = this.multipleSelection[0].organizationId;
        for(let i=0;i<this.multipleSelection.length;i++){
          if(organizationId !== this.multipleSelection[i].organizationId){
            return true
          }
        }
        return false
      }
    },
    methods: {
      sortChange(column){
        if(column.order=="ascending"){
          this.searchSelectInventory.sort=column.prop+"ASC";
        }else{
          this.searchSelectInventory.sort=column.prop+"DESC"
        }
        console.log(this.searchSelectInventory.sort);
        this.getInventoryData();
      },
      changeOrganizationId(val){
        if(!PF.isNull(this.oldOrganizationId)){
          this.oldOrganizationId = val;
        }else{
          this.$confirm('修改货主已选物料将清空？')
                  .then(_ => {
                    this.oldOrganizationId = val;
                    this.form.adjustDetails = [];
                  })
                  .catch(_ => {
                    this.form.organizationId = this.oldOrganizationId;
                  });
        }
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryAdjustData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryAdjustData()
      },
      handleCurrentChange2(val) {
        this.searchSelectInventory.pageNum = val;
        this.getInventoryData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.createTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
        this.searchData.billNo = '';
        this.searchData.caseState = '';
        this.searchData.organizationId = null;
        this.searchData.reason = '';
      },
      clearSearchSelectInventory(){
        this.searchSelectInventory.keyWords = '';
        this.searchSelectInventory.eqCellCode = '';
        this.searchSelectInventory.eqBatchName = '';
        this.searchSelectInventory.sort =null;
      },
      getReasonData(){
        IOT.getServerData('/reasons/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.reasons = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getInventoryAdjustData();
      },
      getInventoryAdjustData(){
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        IOT.getServerData('/inventory/adjusts/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.inventoryAdjusts = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getWareData(){
        IOT.getServerData('/ware/infos/findSome','get',{},(ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getInventoryData(){
        this.searchSelectInventory.cellUseTypeNotEq =3; //集货位
        IOT.getServerData('/inventorys/list', 'get', this.searchSelectInventory, (ret) => {
          if (ret.code === 200) {
            this.inventorys = ret.rows;
            this.searchSelectInventory.total = ret.total;
            this.searchSelectInventory.pageNum = ret.pageNumber

          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.form.adjustDetails = [];
        this.oldOrganizationId=null;
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form');
        this.dialogNewVisible = false;
      },
      showSelectInventoryModal(){
        this.isClickSelectInventory = false;
        if(!PF.isNull(this.form.wareId)){
          IOT.tips('未获取到仓库！', 'error');
          return false
        }else if(!PF.isNull(this.form.organizationId)){
          IOT.tips('请先选择货主！', 'error');
          return false
        }
        this.searchSelectInventory.wareId = this.form.wareId;
        this.searchSelectInventory.organizationId = this.form.organizationId;
        this.selectIds = [];
        for(let i=0;i<this.form.adjustDetails.length;i++){
          this.selectIds.push(this.form.adjustDetails[i].inventoryId)
        }
        this.getInventoryData();
        this.innerVisibleInventory = true
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clickSelectInventory(){
        this.isClickSelectInventory = true;
        let list = PF.JSON(this.multipleSelection);
        this.form.wareId = list[0].wareId; 
        this.form.wareName = list[0].wareName;
        this.form.organizationId = list[0].organizationId;
        this.form.organizationName = list[0].organizationName;

        let isRepeat = false;
        for(let i=0;i<list.length;i++){
          if(list[i].cellUseType==-1){
            IOT.tips('包含垃圾货位，不可进行库存调整', 'error');
            this.isClickSelectInventory = false;
            return false;
          }
          list[i].nowFreezeQuantity = list[i].freezeQuantity;
          
          list[i].canQuantity =  list[i].quantity - list[i].freezeQuantity- list[i].allotQuantity;
          if(this.form.adjustDetails.length > 0){
            if(this.selectIds.indexOf(list[i].inventoryId) === -1 && list[i].organizationId === this.form.adjustDetails[0].organizationId){
              this.form.adjustDetails.push(list[i]);
            }else{
              isRepeat = true
            }
          }else{
            if(this.selectIds.indexOf(list[i].inventoryId) === -1){
              this.form.adjustDetails.push(list[i]);
            }else{
              isRepeat = true
            }
          }
        }
        if(isRepeat){
          IOT.tips('不能重复添加或添加非此货主！', 'error');
        }
        this.innerVisibleInventory = false;
        setTimeout(()=>{
          this.isClickSelectInventory = false;
        },1000)
      },
      deleteDetail(index){
        this.form.adjustDetails.splice(index,1)
      },
      commitForm(formName,insertType) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = this.form;
            console.log(form);
            if(form.adjustDetails.length <= 0){
              IOT.tips('未选择库存,请重新选择！', 'error', 1000 , () => {
                IOT.hideOverlay();
              });
              return false
            }
            for(let i=0;i<form.adjustDetails.length;i++){
              if(form.adjustDetails[i].adjustQuantity==undefined ||form.adjustDetails[i].adjustQuantity==null ){
                IOT.tips('请输入数量！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              if( form.adjustDetails[i].adjustQuantity<(form.adjustDetails[i].freezeQuantity+form.adjustDetails[i].allotQuantity)

               ){
                IOT.tips('数量不能小于冻结数量与已分配数量之和！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
            for (let i = 0; i < this.wares.length; i++) {
              if (this.wares[i].wareId === form.wareId) {
                form.wareName = this.wares[i].wareName;
                form.wareCode = this.wares[i].wareCode;
                break;
              }
            }
            for(let i=0;i<this.organizations.length;i++){
              if(this.organizations[i].organizationId === form.organizationId){
                this.form.organizationName = this.organizations[i].organizationName;
                break;
              }
            }
            form.insertType = insertType;
            
            IOT.getServerData('/inventory/adjusts/createInventoryAdjust','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getInventoryAdjustData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
    },
    created() {
      this.getInventoryAdjustData();
      this.getReasonData();
      this.getWareData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .freeze{
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
</style>
