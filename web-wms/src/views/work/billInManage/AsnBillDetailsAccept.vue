<template>
  <div class="asnBillDetailsAccept">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>入库单/明细单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="入库单/明细单号" clearable></el-input>
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
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" clearable>
            <el-option
              v-for="item in suppliers"
              :key="item.supplierId"
              :label="item.supplierName"
              :value="item.supplierId">
            </el-option>
          </el-select>
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
          <span>关键字搜索</span>
          <el-input style="width:165px" v-model="searchData.keyWords" size="mini" placeholder="追踪号/收货货位/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <!-- <el-button size="mini" type="success"    icon="iconfont icon-xinzeng" @click="commitAcceptList('acceptList')" :disabled="multipleSelection.length <= 0"> 批量验收
      </el-button> -->
    </div>

    <el-dialog :title="isAccept ? '单条验收' : '修改批次策略'" :visible.sync="dialogBatchVisible" :before-close="handleClose" width="900px" style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form v-if="isAccept" class="clearFloat" :model="formAccept" :rules="rules" ref="formAccept">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <el-input :title="formAccept.wareName" v-model="formAccept.wareName" disabled style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-input :title="formAccept.organizationName" v-model="formAccept.organizationName" disabled style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input :title="formAccept.itemName" v-model="formAccept.itemName" disabled style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <el-input :title="formAccept.itemCode" v-model="formAccept.itemCode" disabled style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="预约数量" prop="expectQuantity" :label-width="formLabelWidth">
            <span>{{formAccept.expectQuantity / formAccept.transRatio}}</span>
          </el-form-item>
          <el-form-item label="已验收数量" prop="acceptQuantity" :label-width="formLabelWidth">
            <span>{{formAccept.acceptQuantity / formAccept.transRatio}}</span>
          </el-form-item>
          <el-form-item label="单位" prop="toPackDetailId" :label-width="formLabelWidth">
            <el-select v-model="formAccept.packDetailId" @change="changePackDetailId(0, 'one')" filterable placeholder="请选择单位" style="width: 90%;">
              <el-option
                v-for="item in formAccept.packDetails"
                :key="item.packDetailId1"
                :label="item.packDescribe1"
                :value="item.packDetailId1">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数量" prop="toQuantity" :label-width="formLabelWidth">
            <el-input-number v-model="formAccept.toQuantity" controls-position="right" :min="0"
                             :max="formAccept.toQuantityMax " label="请输入数量"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="货位编码" prop="toCellCode" :label-width="formLabelWidth">
            <el-input :title="formAccept.toCellCode" v-model="formAccept.toCellCode" placeholder="请输入编码" style="width: 80%;"></el-input>
            <el-button @click="showSelectCellModal(null)" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </el-form-item>
          <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
            <el-input v-model="formAccept.toBoxCode" placeholder="请输入追踪号" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="formAccept.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">批次策略</el-divider>
          </div>
        </el-form>
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <div v-if="batchTactics.batchTacticDetails === undefined
          || batchTactics.batchTacticDetails === null
          || batchTactics.batchTacticDetails === []"
               style="float: left;width: 100%;line-height: 60px;color: #909399;text-align: center;">无可用属性
          </div>
          <el-form-item v-for="(item,index) in batchTactics.batchTacticDetails" :key="index" :label="item.detailName" :prop="item.detailCode"
                        :label-width="formLabelWidth">
            <el-select v-if="item.detailCode === 'qcState'" v-model="form[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in qcStates"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>

            <el-input :title="form[item.detailCode]" v-else-if="item.format === 0" v-model="form[item.detailCode]"
                      :disabled="item.detailCode === 'detailNo' || item.detailCode === 'supplierCode'" :placeholder="'请输入'+item.detailName"
                      style="width: 90%;"></el-input>
            <input v-else-if="item.format === 1" type="number" class="my_input" v-model.number="form[item.detailCode]" style="width: 80%;">
<!--            <el-input-number v-else-if="item.format === 1" v-model="form[item.detailCode]" controls-position="right" :min="0" label="请输入" style="width: 90%;"></el-input-number>-->
            <el-date-picker v-else-if="item.format === 2" v-model="form[item.detailCode]" value-format="yyyy-MM-dd"
                            type="date" :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-date-picker>
            <el-time-picker v-else-if="item.format === 3" v-model="form[item.detailCode]" value-format="HH:mm:ss"
                            :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-time-picker>
            <el-select v-else-if="item.format === 4" v-model="form[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in item.values"
                :key="item2.batchTacticDetailDetailValue"
                :label="item2.batchTacticDetailDetailValue"
                :value="item2.batchTacticDetailDetailValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button v-if="!isAccept" type="primary" @click="commitBatchTactic('form')">确 定</el-button>
        <el-button v-if="isAccept" type="primary" @click="commitAcceptList('accept')">验 收</el-button>
      </div>
    </el-dialog>
    <el-dialog title="查看批次策略" :visible.sync="dialogLookBatchVisible" :before-close="handleCloseLook" width="500px" style="margin-top: -10vh;">
      <ul class="publicModalUl">
        <li v-for="(item,index) in lookBatchForm" :key="index">
          <div>
            <span v-if="item.detailName">{{item.detailName}}</span>
            <span v-else>-</span>
            <i></i>
          </div>
          <div v-if="item.detailCode === 'qcState'">
            <span v-for="(qcState,index) in qcStates" :key="index" v-if="item.value === qcState.value">{{qcState.label}}</span>
          </div>
          <div v-else>
            <span v-if="item.value">{{item.value}}</span>
            <span v-else>-</span>
          </div>
        </li>
      </ul>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogLookBatchVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog width="800px" title="选择货位" :visible.sync="innerVisibleCell" style="margin-top: -10vh;">

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


      
         
    <div class="tableBox none" >
      <el-table
      v-loading="loading.masterTable"
        highlight-current-row
        :data="asnDetails"
        :span-method="objectSpanMethod"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <!-- <el-table-column
          type="selection"
          width="30">
        </el-table-column> -->
        <el-table-column
          fixed="left"
          type="index"
          width="40">
        </el-table-column>
         <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/SKU名称"
          width="600">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8"> 
                <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                   <el-image 
                      style="width: 80px; height: 80px"
                      :src="scope.row.imgUrl" 
                      :preview-src-list="[scope.row.imgUrl]">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="16">
                <div style="display: inline-block;margin-left:5px; height:86px">
                    <span class="textBtn" style="color:red" @click="$parent.showPublicModal(scope.row.itemCode, 'item')" 
                        >{{scope.row.itemCode}}</span>
                        <br>
                    <span    >{{scope.row.itemName}}</span>
                  
                     <span>入库单号: </span>
            <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
            <br>
            
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
           
           
              </div>
              </el-col>
              
            </el-row>
            
            
          </template>
        </el-table-column>
       
        
         
        <el-table-column
          show-overflow-tooltip
          prop="expectQuantity"
          label="验收数/预约数"
          width="100">
          <template slot-scope="scope">
            <span>{{scope.row.acceptQuantity  }}/{{scope.row.expectQuantity  }}</span>
          </template>
        </el-table-column>
       
        <!-- 
          <el-table-column
          show-overflow-tooltip
          :render-header="renderHeader"
          prop="toQuantity"
          label="验收数量"
          width="180">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.toQuantity" controls-position="right" :min="0"
                             :max="scope.row.toQuantityMax " label="请输入数量" size="small"
                             style="width: 90%;"></el-input-number>
          </template>
        </el-table-column>
        -->
        <el-table-column
          show-overflow-tooltip
          :render-header="renderHeader"
          prop="packDescribe"
          label="单位"
          width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index, 'list')" filterable placeholder="请选择单位" size="small">
              <el-option
                v-for="item in scope.row.packDetails"
                :key="item.packDetailId1"
                :label="item.packDescribe1"
                :value="item.packDetailId1">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          :render-header="renderHeader"
          label="批次"
          width="130">
          <template slot-scope="scope">
            <el-button @click="lookBatchModal(scope.row)" :disabled="scope.row.isCommitBatch"
                       icon="iconfont icon-chakan" type="info" circle plain size="small"></el-button>
            <el-button @click="showBatchModal(scope.row,scope.$index,scope.row.isCommitBatch)" icon="iconfont icon-bianji" type="primary" circle
                       plain size="small"></el-button>
          </template>
        </el-table-column>
        <!--
        <el-table-column
                show-overflow-tooltip
                :render-header="renderHeader"
                prop="toCellCode"
                label="收货货位"
                width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.toCellCode" placeholder="请输入收货货位" size="small" clearable style="width: 90%"></el-input>
            <el-button @click="showSelectCellModal(scope.$index)" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="toBoxCode"
          label="追踪号"
          width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.toBoxCode" placeholder="请输入追踪号" size="small" clearable></el-input>
          </template>
        </el-table-column> 
       -->
        <el-table-column
          fixed="right"
          label="操作"
          width="60">
          <template slot-scope="scope">
            <el-button @click="showAcceptModal(scope.row,scope.$index)" icon="iconfont icon-shouhuo" type="success" circle plain
                       size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
       class="pagination-bottom"
        style="text-align: right;"
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

  export default {
    name: 'asnBillDetailsAccept',
    data() {
      return {
        wareId:PF.getLocal('wareId', 'number'),
        loading:{
            masterTable:true,
        },
        isMoreSearch: false,
        isMoreSearch3: false,

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal("wareName"),
          // asnTypes: {0: 0, 1: 2},
          organizationId: null,
          supplierId: null,
          itemCode:'',
          itemName:'',
          isAccept:1,
          billNo:''
        },
        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseType: 0,
        },

        asnDetails: [],
        asnUdfHsArr: [],
        asnUdfDsArr: [],
        rows: [],
        spans: [],
        multipleSelection: [],
        batchTactics: [],
        acceptExcess: null,
        tacticIndex: null,
        organizations: [],
        suppliers: [],
        lookBatchForm: {},
        cells: [],
        areas: [],
        selectCellIndex: null,

        isAccept: true,
        dialogBatchVisible: false,
        dialogLookBatchVisible: false,
        innerVisibleCell: false,

        formLabelWidth: '120px',
        form: {
          'productDate': '',
          'exDate': '',
          'inDate': '',
          'supplierCode': '',
          'supplierBatch': '',
          'qcState': '',
          'costPrice': null,
          'salePrice': '',
          'detailNo': '',
          'memo1': '',
          'memo2': '',
          'memo3': '',
          'batchAttribute1': '',
          'batchAttribute2': '',
        },
        rules: {
          wareId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          'productDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'exDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'inDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierCode': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierBatch': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'qcState': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'costPrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'salePrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'detailNo': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo3': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
        formAccept: {
          wareId: null,
          wareName: '',
          organizationId: null,
          organizationName: '',
          toPackDetailId: null,
          toPackDescribe: '',
          itemCode: '',
          itemName: '',
          packCode: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: 0,
          billType: 3,
        },

      }
    },
    computed: {
      ...mapState([
        "cellUseTypes",
        "cellAbcLevels",
        "cellPickTypes",
        "qcStates",
      ])
    },
    methods: {

      
      renderHeader(cerateElement, { column }) {
        return cerateElement("div", [
          cerateElement("span", {
            domProps: {
              innerHTML: "*"
            },
            style: {
              width:"10px",
              height:"5px",
              display:"inline-block",
              vertical_align:"middle",
              text_align:"center",
              color:"#f00",
            }
          }),
          cerateElement("span", column.label),
        ]);
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getAsnBillDetailData()
      },
      handleCurrentChange3(val) {
        this.searchSelectCell.pageNum = val;
        this.getCellData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
        this.searchData.itemCode = '';
        this.searchData.itemName = '';
        this.searchData.billNo = '';
      },
      clearSearchSelectCell() {
        this.searchSelectCell.keyWords = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getAsnBillDetailData();
      },
      getAsnBillDetailData() {
         let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
        console.log(searchData);
        searchData.pageSize = searchData.pageSize * 5;
        IOT.getServerData('/asn/details/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
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

              // list[i].expectQuantity = list[i].expectQuantity / list[i].transRatio;
              // list[i].acceptQuantity = list[i].acceptQuantity / list[i].transRatio;

              let expectQuantity1 = list[i].expectQuantity / list[i].transRatio;
              let acceptQuantity1 = list[i].acceptQuantity / list[i].transRatio;
              list[i].toQuantityMax = PF.subtraction( expectQuantity1, acceptQuantity1 );
              list[i].toQuantity = list[i].toQuantityMax;
              list[i].toTransRatio = null;
              list[i].isCommitBatch = true;
              list[i].billType = 2;
              list[i].toBoxCode = '';
              list[i].toCellCode = '';
              list[i].isSetBatch = false;


              list[i].oldToQuantity = list[i].toQuantity;
              list[i].oldPackDetailId = list[i].packDetailId;


            }
            this.rows = rows;
            this.spans = spans;
            this.asnDetails = list;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getBatchTacticData(organizationId, itemCode, detailNo, type, row) {

        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: organizationId,
          itemCode: itemCode
        }, (ret) => {

          if (ret.code === 200) {
            let list2 = PF.JSON(ret.rows[0]);
            let detailDetails = list2.batchTacticDetailDetails;
            let testDetailDetails = [];
            list2.batchTacticDetails[12].values = [];
            list2.batchTacticDetails[13].values = [];

            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list2.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list2.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list2.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let j = 0; j < batchTacticDetails.length; j++) {
              if (batchTacticDetails[j].state === 1) {
                if (type === 'commit' && row.isCommitBatch === true) {
                  this.form[batchTacticDetails[j].detailCode] = '';
                }else if(type === 'commit' && row.isCommitBatch === false){
                  this.form[batchTacticDetails[j].detailCode] = row[batchTacticDetails[j].detailCode];
                }
                testBatchTacticDetails.push(batchTacticDetails[j]);
                if (batchTacticDetails[j].isRequired === 1) {
                  this.rules[batchTacticDetails[j].detailCode][0].required = true;
                }
                if (batchTacticDetails[j].detailCode === 'supplierCode') {
                  this.form[batchTacticDetails[j].detailCode] = this.asnDetails[this.tacticIndex].supplierCode;
                }else if (batchTacticDetails[j].detailCode === 'inDate') {
                  this.form[batchTacticDetails[j].detailCode] = PF.getNowFormatDate();
                }
              }
            }

            list2.batchTacticDetailDetails = testDetailDetails;
            list2.batchTacticDetails = testBatchTacticDetails;
            if (type === 'commit') {
              this.form.detailNo = detailNo;
            }
            if (type === 'look') {
              let lookBatchForm = [];
              for (let i = 0; i < list2.batchTacticDetails.length; i++) {
                for (let key in row) {
                  if (list2.batchTacticDetails[i].detailCode === key) {
                    lookBatchForm.push({
                      detailCode: key,
                      detailName: list2.batchTacticDetails[i].detailName,
                      value: row[key]
                    })
                  }
                }
              }
              this.lookBatchForm = lookBatchForm;
            }
            this.batchTactics = list2;
            // list[i].batchTactics = list;
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
      getSysParamAsnBillData() {
        IOT.getServerData('/sys/params/findByModuleCode', 'get', {moduleCode: "asnBill"}, (ret) => {
          if (ret.code === 200) {
            let sysParams = ret.rows;
            for (let i in sysParams) {
              if (sysParams[i].paramCode === 'asnUdfHs1' || sysParams[i].paramCode === 'asnUdfHs2' || sysParams[i].paramCode === 'asnUdfHs3') {
                this.asnUdfHsArr.push({code: sysParams[i].paramCode, label: sysParams[i].value});
              }
              if (sysParams[i].paramCode === 'asnUdfDs1' || sysParams[i].paramCode === 'asnUdfDs2' || sysParams[i].paramCode === 'asnUdfDs3') {
                this.asnUdfDsArr.push({code: sysParams[i].paramCode, label: sysParams[i].value});
              }
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      
      getCellData() {
        IOT.getServerData('/cell/infos/list', 'get', this.searchSelectCell, (ret) => {
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
        IOT.getServerData('/area/infos/list', 'get', {wareId:this.wareId}, (ret) => {
          if (ret.code === 200) {
            
            this.areas = ret.rows;
             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
   
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      checkBatchTactic(list){
        let isNext = true;
        list = PF.JSON(list);
        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: list.organizationId,
          itemCode: list.itemCode
        }, (ret) => {
          if (ret.code === 200) {
            let row = ret.rows[0];
            let details = row.batchTacticDetails;
            for(let i=0;i<details.length;i++){
              if(details[i].state === 1 && details[i].isRequired === 1 && details[i].detailCode !== "supplierCode" && !PF.isNull(list[details[i].detailCode])){
                IOT.tips('此单' + list.detailNo + '有必输批次！', 'error', 1000, () => {
                  IOT.hideOverlay();
                });
                isNext = false;
                break;
              }
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },false,false);
        return isNext
      },
      commitAcceptList(type) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        let form = [];
        if(type === 'acceptList'){
          form = PF.JSON(this.multipleSelection);
        }else if(type === 'accept'){
          form.push(PF.JSON(this.formAccept));
        }
        for (let i = 0; i < form.length; i++) {
          if (form[i].toQuantity === 0) {
            IOT.tips('数量不得为零！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          if(form[i].toCellCode === ''){
            IOT.tips('货位不得为空！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          if(type === 'acceptList' && !form[i].isSetBatch){
            let isNext = this.checkBatchTactic(form[i]);
            if(!isNext){
              return false
            }
          }

          form[i].asnDetailNo = form[i].detailNo;
          form[i].toPackDetailId = form[i].packDetailId;
          form[i].toPackDescribe = form[i].packDescribe;
          form[i].asnBillNo = form[i].billNo;

          // 主单位数量
          form[i].expectQuantity = form[i].expectQuantity * form[i].transRatio;
          form[i].toTransRatio = form[i].transRatio;
        }
        IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: form}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('验收成功！', 'success', 1000, () => {
              that.getAsnBillDetailData();
              that.resetForm('formAccept');
              that.handleClose();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form');
        if(this.isAccept){
          this.resetForm('formAccept');
        }
        this.dialogBatchVisible = false;
      },
      handleCloseLook(done) {
        this.dialogLookBatchVisible = false;
      },
      lookBatchModal(row) {
        this.getBatchTacticData(row.organizationId, row.itemCode, row.detailNo, 'look', row);
        this.dialogLookBatchVisible = true;
      },
      showBatchModal(row, index, isCommitBatch) {
        this.tacticIndex = index;
        this.getBatchTacticData(row.organizationId, row.itemCode, row.detailNo, 'commit', row);
        this.isAccept = false;
        this.dialogBatchVisible = true;
      },
      showAcceptModal(row,index) {
        this.tacticIndex = index;
        this.formAccept = row;
        this.getBatchTacticData(row.organizationId, row.itemCode, row.detailNo, 'commit', row);
        this.isAccept = true;
        this.dialogBatchVisible = true;
      },
      commitBatchTactic(formName) {
        console.log(this.form)
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = this.form;
            for (let key in form) {
              this.asnDetails[this.tacticIndex][key] = form[key];
            }
            this.asnDetails[this.tacticIndex].isCommitBatch = false;
            this.asnDetails[this.tacticIndex].isSetBatch = true;
            this.batchTactics = {};
            this.handleClose();
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      changePackDetailId(index, type){
        if(type === 'list'){
          let detail = PF.JSON(this.asnDetails[index]);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
              this.asnDetails[index].transRatio = detail.packDetails[i].transRatio1;
            }
          }
          let expectQuantity1 = this.asnDetails[index].expectQuantity / this.asnDetails[index].transRatio;
          let acceptQuantity1 = this.asnDetails[index].acceptQuantity / this.asnDetails[index].transRatio;
          this.asnDetails[index].toQuantityMax = PF.subtraction( expectQuantity1, acceptQuantity1 );
          this.asnDetails[index].toQuantity = this.asnDetails[index].toQuantityMax;
        }else if(type === 'one'){
          let detail = PF.JSON(this.formAccept);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.formAccept.packDescribe = detail.packDetails[i].packDescribe1;
              this.formAccept.transRatio = detail.packDetails[i].transRatio1;
            }
          }
          let expectQuantity1 = this.formAccept.expectQuantity / this.formAccept.transRatio;
          let acceptQuantity1 = this.formAccept.acceptQuantity / this.formAccept.transRatio;
          this.formAccept.toQuantityMax = PF.subtraction( expectQuantity1, acceptQuantity1 );
          this.formAccept.toQuantity = this.formAccept.toQuantityMax;
        }


        /*let oldTransRatio = 0;
        let nowTransRatio = 0;
        let detail = PF.JSON(this.asnDetails[index]);
        for(let i=0;i<detail.packDetails.length;i++){
          if(detail.packDetailId === detail.oldPackDetailId){
            nowTransRatio = oldTransRatio = 1;
          }else if(detail.packDetails[i].packDetailId1 === detail.packDetailId){
            this.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
            nowTransRatio = detail.packDetails[i].transRatio
          }else if(detail.packDetails[i].packDetailId1 === detail.oldPackDetailId){
            oldTransRatio = detail.packDetails[i].transRatio
          }
        }
        this.asnDetails[index].toQuantityMax = (detail.oldToQuantity / oldTransRatio) * nowTransRatio;
        this.asnDetails[index].toQuantity = 0;*/
      },
      showSelectCellModal(index) {
        this.selectCellIndex = index;
        if(PF.isNull(this.selectCellIndex)){
          if (!PF.isNull(this.asnDetails[this.selectCellIndex].wareId)) {
            IOT.tips('未选择仓库！', 'error');
            return false;
          }
          this.searchSelectCell.wareId = this.asnDetails[this.selectCellIndex].wareId;
        }else{
          if (!PF.isNull(this.formAccept.wareId)) {
            IOT.tips('未选择仓库！', 'error');
            return false;
          }
          this.searchSelectCell.wareId = this.formAccept.wareId;
        }
        
        this.getCellData(); 
        this.innerVisibleCell = true;
      },
      selectCell(cellCode) {
        if(PF.isNull(this.selectCellIndex)){
          this.asnDetails[this.selectCellIndex].toCellCode = cellCode;
          this.innerVisibleCell = false;
        }else{
          this.formAccept.toCellCode = cellCode;
          this.innerVisibleCell = false;
        }
      },

    },
    created() {
      PF.getSysParamUdfData('acceptExcess', [], (data, udfs) => {
        this.acceptExcess = 1 + (Number(udfs[0].value) / 100);
      });
      this.getAsnBillDetailData();
      this.getSysParamAsnBillData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      this.getAreaData();
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .asnBillDetailsAccept {
  }

</style>
