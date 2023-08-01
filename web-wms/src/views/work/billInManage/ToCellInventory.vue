<template>
  <div class="toCellInventory">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getInventoryBalanceData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="货位/追踪号/LPN号" clearable></el-input>
        </div>
        <div>
          <span>SKU名称</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
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
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <!-- <el-button size="small" type="primary"   icon="iconfont icon-xinzeng" @click="generateInCellTask">生成上架任务</el-button> -->
    </div>

    <el-dialog title="上架" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <div class="billMasterBox" style="padding: 0;margin-bottom: 20px;">
        <div class="billData clearFloat" style="width: 100%;">
          <ul class="leftFloat">
            <li>
              <span>SKU名称:</span>
              <span>&nbsp;{{form.row.itemName}}</span>
            </li>
            <li>
              <span>SKU:</span>
              <span>&nbsp;{{form.row.itemCode}}</span>
            </li>
            <li>
              <span>货主:</span>
              <span>&nbsp;{{form.row.organizationName}}</span>
            </li>
            <li>
              <span>货位:</span>
              <span>&nbsp;{{form.row.cellCode}}</span>
            </li>
            <li>
              <span>追踪号:</span>
              <span>&nbsp;{{form.row.boxCode}}</span>
            </li>
            <li>
              <span>LPN码:</span>
              <span>&nbsp;{{form.row.lpn}}</span>
            </li>
          </ul>
          <ul class="rightFloat">
            <li>
              <span>批次:</span>
              <span>&nbsp;{{form.row.batchName}}</span>
            </li>
            <li>
              <span>单位:</span>
              <span>&nbsp;{{form.row.packDescribe}}</span>
            </li>
            <li>
              <span>库存数量:</span>
              <span>&nbsp;{{ Math.round(form.row.quantity / form.row.transRatio * 10000) / 10000 }}</span>
            </li>
            <li>
              <span>分配数量:</span>
              <span>&nbsp;{{ Math.round(form.row.allotQuantity / form.row.transRatio * 10000) / 10000 }}</span>
            </li>
            <li>
              <span>冻结数量:</span>
              <span>&nbsp;{{ Math.round(form.row.freezeQuantity / form.row.transRatio * 10000) / 10000 }}</span>
            </li>
            <li>
              <span>可上架数量:</span>
              <span>&nbsp;{{ Math.round(form.row.quantityMax / form.row.transRatio * 10000) / 10000 }}</span>
            </li>
          </ul>
        </div>
      </div>
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item v-show="isFindUpperCell" label="推荐货位" :label-width="formLabelWidth">
            <el-input v-model="form.findUpperCell" disabled></el-input>
          </el-form-item>
          <!--<el-form-item label="目标货位" prop="toCellCode" :label-width="formLabelWidth">
            <el-select v-model="form.toCellCode" filterable placeholder="请输入目标货位">
              <el-option
                      v-for="item in cells"
                      :key="item.cellCode"
                      :label="item.cellName"
                      :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>-->
          <el-form-item label="目标货位" prop="toCellCode" :label-width="formLabelWidth">
            <el-input :title="form.toCellCode" v-model="form.toCellCode" placeholder="请输入编码"
                      style="width: 60%;"></el-input>
            <el-button @click="showSelectCellModal" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </el-form-item>
          <el-form-item label="目标追踪号" prop="toBoxCode" :label-width="formLabelWidth">
            <el-input v-model="form.toBoxCode" placeholder="请输入目标追踪号"></el-input>
          </el-form-item>
          <el-form-item label="数量" prop="toQuantity" :label-width="formLabelWidth">
            <input type="number" class="my_input" v-model.number="form.toQuantity" :max="form.row.quantity" placeholder="请输入数量" style="width: 60%;">
            <!--            <el-input-number v-model="form.toQuantity" controls-position="right" :min="0" :max="form.row.quantity" style="width: 70%;"></el-input-number>-->
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button style="margin-right: 550px;" type="primary" @click="showSerialNo()"
                   :disabled="serialNo1.length<=0"
        >序列号({{serialNo1.length}})</el-button>
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
      <el-dialog width="800px" title="选择货位" :visible.sync="innerVisibleCell" append-to-body style="margin-top: -10vh;">

        <div class="publicSearch body">
          <div class="searchBar" :style="{height: isMoreSearch3?'auto':''}">
            <el-button-group>
              <el-button @click="getCellData" size="mini" icon="el-icon-search">查询</el-button>
              <el-button @click="clearSearchSelectCell" size="mini" icon="el-icon-refresh">重置</el-button>
            </el-button-group>
            <div>
              <span>关键字搜索</span>
              <el-input v-model="searchSelectCell.keyWords" size="mini" placeholder="名称" clearable></el-input>
            </div>
            <div>
              <span>货区</span>
              <el-select v-model="searchSelectCell.areaId" size="mini" filterable placeholder="请选择货区"
                         style="width: 70%;">
                <el-option
                    v-for="item in areas"
                    :key="item.areaId"
                    :label="item.areaName"
                    :value="item.areaId">
                </el-option>
              </el-select>
            </div>
          </div>
          <span class="moreSearchBtn"
                :class="isMoreSearch3?'el-icon-caret-top':'el-icon-caret-bottom'"
                @click="isMoreSearch3 = !isMoreSearch3"></span>
        </div>

        <div class="tableBox">
          <el-table
              highlight-current-row
              :data="cells"
              style="width: 100%">
            <el-table-column
                fixed="left"
                type="index"
                width="50">
            </el-table-column>
            <el-table-column
                fixed="left"
                sortable
                prop="cellName"
                label="名称"
                width="180">
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="cellCode"
                label="编码"
                width="180">
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="shelfName"
                label="所属货位组"
                width="180">
            </el-table-column>
         
          
          
           
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="length"
                label="长"
                width="180">
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="wide"
                label="宽"
                width="180">
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="high"
                label="高"
                width="180">
            </el-table-column>
            
         
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="xCoordinate"
                label="X坐标"
                width="180">
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="yCoordinate"
                label="Y坐标"
                width="180">
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作"
                width="110">
              <template slot-scope="scope">
                <el-button @click="selectCell(scope.row.cellCode)" icon="iconfont icon-jia1" type="primary" circle plain
                           size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              style="text-align: right;"
              @current-change="handleCurrentChange3"
              :current-page.sync="searchSelectCell.pageNum"
              :page-size="searchSelectCell.pageSize"
              layout="total, prev, pager, next, jumper"
              :total="searchSelectCell.total">
          </el-pagination>
        </div>

      </el-dialog>
    </el-dialog>
    <!-- 序列号 -->
    <el-dialog title="序列号" :visible.sync="serialNo" :before-close="handleCloseSerialNo" width="900px"
               style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="扫描输入框"  :label-width="formLabelWidth">
            <el-input v-model="scanner" id="scanner" @change="addSerialNo()" placeholder="请扫描序列号" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="affirmSerialNo()" :disabled="select">确 定</el-button>
          </el-form-item>
          <el-form-item label="是否自动提交">
            <el-switch
                    v-model="select"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-text="是"
                    inactive-text="否"
                    :active-value="true"
                    :inactive-value="false"
            >
            </el-switch>
          </el-form-item>
          <!-- 根据数量生成输入框 -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">序列号</el-divider>
          </div>

          <!--          <el-form-item v-for="(item,index) in serialNo1" :label="'序列号'+index"  :label-width="formLabelWidth">-->
          <!--            <el-input v-model="item.serialNo" v-bind:id="'serialNoId'+item.serialNoId" placeholder="请扫描" style="width: 80%;" disabled></el-input>-->
          <!--          </el-form-item>-->
          <el-tag
                  v-for="(item,i) in serialNo1"
                  :key="i"
                  :style="i===0?'':'margin-left: 10px;;margin-top:10px;'"
                  v-bind:id="'serialNoId'+item.serialNoId">
            {{item.serialNo}}
          </el-tag>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloseSerialNo()">取 消</el-button>
        <el-button type="primary" @click="serialNo = false">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
      v-loading="loading.masterTable"
              highlight-current-row
              :data="inventoryBalances"
              @selection-change="handleSelectionChange"
              style="width: 100%">
        <el-table-column
                fixed="left"
                type="selection"
                 min-width="50">
        </el-table-column>
        <el-table-column
                fixed="left"
                show-overflow-tooltip
                prop="inventoryId"
                label="库存ID"
                 min-width="100">
           
        </el-table-column>
        <el-table-column
                fixed="left"
                show-overflow-tooltip
                prop="itemName"
                label="SKU名称"
                 min-width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column

                show-overflow-tooltip
                prop="itemCode"
                label="SKU"
                 min-width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>

        <el-table-column

                show-overflow-tooltip
                prop="organizationName"
                label="货主"
                 min-width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
      
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="batchName"
                label="批次"
                 min-width="130">
        </el-table-column>
        <el-table-column
           
          show-overflow-tooltip
          prop="quantity"
          label="数量"
           min-width="140">
         
        </el-table-column>
        
        <el-table-column
           
          show-overflow-tooltip
          prop="allotQuantity"
          label="分配数量"
           min-width="140">
        <template slot-scope="scope">
            <span style="color:black;font-weight:900;">{{  scope.row.allotQuantity   }} </span>
            <span v-if="scope.row.allotQuantity>0" style="font-size:20% ;color:red ">爆</span>
          </template>  
        
            
        </el-table-column>
        
     
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="cellCode"
                label="货位"
                 min-width="150">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="boxCode"
                label="追踪号"
                 min-width="150">
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

  export default {
    name: 'ToCellInventory',
    data() {
      let validateQuantity = (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('数量不得为零'));
          callback();
        } else if (value > 0) {
          callback();
        } else {
          callback(new Error('请输入正确格式'));
        }
      };
      let validateCellCode = (rule, value, callback) => {
        this.validator(rule, value, callback, 'cell_info', 'cell_code', this.form.toCellCode, 0);
      };
      return {
        loading:{
            masterTable:true,
        },
        isMoreSearch: false,
        isMoreSearch3: false,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          cellUseType: 0,
          itemName:'',
          itemCode:''
        },
        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseTypes: { 0: 2},
        },

        inventoryBalances: [],
        organizations: [],
        areas: [],
        cells: [],
        multipleSelection: [],

        serialNo:false,
        serialNo1:[],
        scanner:'',
        select:true,

        isFindUpperCell: false,
        dialogNewVisible: false,
        innerVisibleCell: false,

        formLabelWidth: '120px',

        form: {
          row: {},
          findUpperCell: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: null,
          serialNos:[],
        },
        rules: {
          toCellCode: [
            {required: true, message: '请选择目标货位', trigger: 'blur'},
             
          ],
          toBoxCode: [
            {required: true, message: '请选择目标追踪号', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            {validator: validateQuantity, trigger: 'blur'}
          ],
        },

      }
    },
    computed: {
      ...mapState([
        "cellUseTypes",
        "cellAbcLevels",
        "cellPickTypes",
      ])
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryBalanceData();
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
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryBalanceData()
      },
      handleCurrentChange3(val) {
        this.searchSelectCell.pageNum = val;
        this.getCellData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
        this.searchData.itemName = null;
        this.searchData.itemCode = null;
      },
      clearSearchSelectCell() {
        this.searchSelectCell.keyWords = '';
      },
       changePackDetailId(index){
      
        let detail = PF.JSON(this.inventoryBalances[index]);
        for (let i = 0; i < detail.packDetails.length; i++) {
          if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
            this.inventoryBalances[index].packDescribe = detail.packDetails[i].packDescribe1;
            this.inventoryBalances[index].transRatio = detail.packDetails[i].transRatio1;
          }
        }
      },
      getInventoryBalanceData() {
         let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
       
        IOT.getServerData('/inventorys/CellUseTypeList', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.inventoryBalances = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData(){
        IOT.getServerData('/cell/infos/list','get', this.searchSelectCell,(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
            this.searchSelectCell.total = ret.total;
            this.searchSelectCell.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAreaData() {
        IOT.getServerData('/area/infos/list', 'get', {wareId: this.searchSelectCell.wareId}, (ret) => {
          if (ret.code === 200) {
            this.areas = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showNewModal(row) {
        this.findUpperCell(row);
        row.quantityMax = PF.subtraction( row.quantity, row.allotQuantity + row.freezeQuantity );
        this.form.toQuantity = row.quantityMax;
        this.form.row = row;
        IOT.getServerData('/serial/nos/list', 'get', {type:1,inventoryId:row.inventoryId}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.serialNo1 = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.dialogNewVisible = true;
      },
      showSelectCellModal() {
        if (!PF.isNull(this.form.row.wareId)) {
          IOT.tips('未选择仓库！', 'error');
          return false;
        }
        this.searchSelectCell.wareId = this.form.row.wareId;
        this.getCellData();
        this.getAreaData();
        this.innerVisibleCell = true;
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form');
        this.dialogNewVisible = false;
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let a = Math.ceil(that.form.toQuantity);
            
            if(that.form.serialNos.length <= 0 && this.serialNo1.length > 0){
              this.$confirm('当前上架库存未录入序列号,会影响后续序列号追踪,是否继续？')
                      .then(_ => {
                        this.commitUpperTask(formName);
                      })
                      .catch(_ => {
                        IOT.hideOverlay();
                      });
            }else if(that.form.serialNos.length > 0 && that.serialNo1.length > 0 && that.form.serialNos.length < a){
              this.$confirm('当前上架库存录入序列号与数量不一致,会影响后续序列号追踪,是否继续？')
                      .then(_ => {
                        this.commitUpperTask(formName);
                      })
                      .catch(_ => {
                        IOT.hideOverlay();
                      });
            }else {
              this.commitUpperTask(formName);
            }
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      commitUpperTask(formName){
        let that = this;
        let form = PF.JSON(this.form);
        for (let key in form.row) {
          form[key] = form.row[key]
        }
        // 主单位数量
        form.toQuantity = form.toQuantity * form.row.transRatio;
        if(form.toQuantity > form.row.quantityMax){
          IOT.tips('输入数量不得大于可上架数量！', 'error', 1000 , () => {
            IOT.hideOverlay();
          });
          return false
        }
        delete form.row;
        form.fromBoxCode = form.boxCode;
        form.fromCellCode = form.cellCode;
        IOT.getServerData('/upper/tasks/upperTask', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getInventoryBalanceData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.resetForm(formName);
        this.dialogNewVisible = false
      },
      findUpperCell(row) {
        IOT.getServerData('/upper/tasks/findUpperCell', 'get', row, (ret) => {
          if (ret.code === 200) {
            this.isFindUpperCell = true;
            this.form.findUpperCell = ret.data;
            this.form.toCellCode = ret.data;
          } else if (ret.code === 122) {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'warning');
            this.isFindUpperCell = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      },
      generateInCellTask() {
        IOT.showOverlay('生成中....')
        IOT.getServerData('/upper/tasks/generateInCellTask', 'post', this.multipleSelection, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.isFindUpperCell = true;
            this.form.findUpperCell = ret.data;
            this.form.toCellCode = ret.data;
          } else if (ret.code === 122) {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'warning');
            this.isFindUpperCell = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      selectCell(cellCode) {
        this.form.toCellCode = cellCode;
        this.innerVisibleCell = false;
      },
    },
    created() {
      this.getInventoryBalanceData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .toCellInventory {

  }
</style>
