<template>
  <div class="shelf">

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
          <span>货区</span>
          <el-select v-model="searchData.areaId" size="mini" filterable placeholder="请选择货区" clearable>
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
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
      </el-button>
    </div>

    <el-dialog title="新增货位组" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               :close-on-click-modal="false">
      <div class="bigFormBox3 clearFloat">
        <el-form :model="form" :rules="rules" ref="form">
          <!--<el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="wareId" @change="getAreaData" filterable placeholder="请选择仓库" style="width: 90%;">
              <el-option
                v-for="item in wares"
                :key="item.wareId"
                :label="item.wareName"
                :value="item.wareId">
              </el-option>
            </el-select>
          </el-form-item>-->
          <el-form-item label="货区" prop="areaId" :label-width="formLabelWidth">
            <el-select v-model="form.areaId" filterable placeholder="请选择货区" style="width: 90%;">
              <el-option
                v-for="item in areas"
                :key="item.areaId"
                :label="item.areaName"
                :value="item.areaId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="编码" prop="shelfCode" :label-width="formLabelWidth">
            <el-input v-model="form.shelfCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="shelfName" :label-width="formLabelWidth">
            <el-input v-model="form.shelfName" placeholder="请输入名称" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="shelfDescribe" :label-width="formLabelWidth">
            <el-input v-model="form.shelfDescribe" placeholder="请输入描述" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
            <el-select v-model="form.type" 
            @change="form.cellUseType  =form.type"
            filterable placeholder="请选择类型" style="width: 90%;">
              <el-option
                v-for="item in shelfTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否创建货位" prop="isCreateCell" :label-width="formLabelWidth">
            <el-switch
              v-model="form.isCreateCell"
              :active-value="true"
              :inactive-value="false">
            </el-switch>
          </el-form-item>
          <div v-if="form.isCreateCell && form.type==4">
            <div style="float: left;width: 100%;">
              <el-divider content-position="left">货位信息</el-divider>
            </div>
            <el-form-item label="行" prop="shelfRow" :label-width="formLabelWidth">
              <el-input-number v-model="form.shelfRow" controls-position="right" :min="0" label="请输入行"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="列" prop="shelfColumn" :label-width="formLabelWidth">
              <el-input-number v-model="form.shelfColumn" controls-position="right" :min="0" label="请输入列"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
             <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
              <el-select disabled v-model="form.cellUseType"   filterable placeholder="请选择货位使用类型" style="width: 90%;">
                <el-option
                  v-for="item in cellUseTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
             
          </div>
          <div v-if="form.isCreateCell  && form.type!=4">
            <div style="float: left;width: 100%;">
              <el-divider content-position="left">货位信息</el-divider>
            </div>
            <el-form-item label="行" prop="shelfRow" :label-width="formLabelWidth">
              <el-input-number v-model="form.shelfRow" controls-position="right" :min="0" label="请输入长"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="列" prop="shelfColumn" :label-width="formLabelWidth">
              <el-input-number v-model="form.shelfColumn" controls-position="right" :min="0" label="请输入长"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <br/>
            <!-- <el-form-item label="上架顺序" prop="groundOrder" :label-width="formLabelWidth">
              <el-input-number v-model="form.groundOrder" :min="0" label="请输入上架顺序" style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="拣货顺序" prop="pickOrder" :label-width="formLabelWidth">
              <el-input-number v-model="form.pickOrder" :min="0" label="请输入拣货顺序" style="width: 90%;"></el-input-number>
            </el-form-item>
             <br/> -->
            <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
              <el-select v-model="form.cellUseType" filterable placeholder="请选择货位使用类型" style="width: 90%;">
                <el-option
                  v-for="item in cellUseTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="周转需求" prop="cellAbcLevel" :label-width="formLabelWidth">
              <el-select v-model="form.cellAbcLevel" filterable placeholder="请选择周转需求" style="width: 90%;">
                <el-option
                  v-for="item in cellAbcLevels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="拣货方式" prop="cellPickType" :label-width="formLabelWidth">
              <el-select v-model="form.cellPickType" filterable placeholder="请选择拣货方式" style="width: 90%;">
                <el-option
                  v-for="item in cellPickTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="货位属性" prop="cellAttribute" :label-width="formLabelWidth">
              <el-input v-model="form.cellAttribute" placeholder="请输入货位属性" style="width: 90%;"></el-input>
            </el-form-item>
            <el-form-item label="货位处理" prop="cellHandle" :label-width="formLabelWidth">
              <el-input v-model="form.cellHandle" placeholder="请输入货位处理" style="width: 90%;"></el-input>
            </el-form-item> -->
            <!-- <div style="float: left;width: 100%;">
              <el-divider content-position="left">尺寸</el-divider>
            </div>
            <el-form-item label="长(m)" prop="length" :label-width="formLabelWidth">
              <el-input-number v-model="form.length" controls-position="right" :min="0" label="请输入长"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="宽(m)" prop="wide" :label-width="formLabelWidth">
              <el-input-number v-model="form.wide" controls-position="right" :min="0" label="请输入宽"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="高(m)" prop="high" :label-width="formLabelWidth">
              <el-input-number v-model="form.high" controls-position="right" :min="0" label="请输入高"
                               style="width: 90%;"></el-input-number>
            </el-form-item> -->
            <!-- <div style="float: left;width: 100%;">
              <el-divider content-position="left">限制</el-divider>
            </div>
            <el-form-item label="体积限制(m³)" prop="volumeLimit" :label-width="formLabelWidth">
              <el-input-number v-model="form.volumeLimit" controls-position="right" :precision="2" :min="0"
                               :max="formVolumeLimitMax" label="请输入体积限制" style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="重量限制(kg)" prop="weightLimit" :label-width="formLabelWidth">
              <el-input-number v-model="form.weightLimit" controls-position="right" :precision="2" :min="0"
                               label="请输入重量限制" style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="箱数限制" prop="boxLimit" :label-width="formLabelWidth">
              <el-input-number v-model="form.boxLimit" controls-position="right" :precision="2" :min="0" label="请输入箱数限制"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="数量限制" prop="quantityLimit" :label-width="formLabelWidth">
              <el-input-number v-model="form.quantityLimit" controls-position="right" :precision="2" :min="0"
                               label="请输入数量限制" style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="托盘限制" prop="palletLimit" :label-width="formLabelWidth">
              <el-input-number v-model="form.palletLimit" controls-position="right" :precision="2" :min="0"
                               label="请输入托盘限制" style="width: 90%;"></el-input-number>
            </el-form-item>
            <div style="float: left;width: 100%;">
              <el-divider content-position="left">坐标</el-divider>
            </div> -->
            <!-- <el-form-item label="X坐标" prop="xCoordinate" :label-width="formLabelWidth">
              <el-input-number v-model="form.xCoordinate" controls-position="right" :min="0" label="请输入X坐标"
                               style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="Y坐标" prop="yCoordinate" :label-width="formLabelWidth">
              <el-input-number v-model="form.yCoordinate" controls-position="right" :min="0" label="请输入Y坐标"
                               style="width: 90%;"></el-input-number>
            </el-form-item> -->
          </div>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');clearForm();dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改货位组" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px"
               :close-on-click-modal="false">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <!--<el-form-item label="仓库" :label-width="formLabelWidth">
          <el-select v-model="wareId" @change="getAreaData" filterable placeholder="请选择仓库" style="width: 90%;">
            <el-option
              v-for="item in wares"
              :key="item.wareId"
              :label="item.wareName"
              :value="item.wareId">
            </el-option>
          </el-select>
        </el-form-item>-->
        <el-form-item label="货区" prop="areaId" :label-width="formLabelWidth">
          <el-select v-model="rowData.areaId" filterable placeholder="请选择货区" style="width: 90%;">
            <el-option
              v-for="item in areas"
              :key="item.areaId"
              :label="item.areaName"
              :value="item.areaId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="shelfCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.shelfCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="shelfName" :label-width="formLabelWidth">
          <el-input v-model="rowData.shelfName" placeholder="请输入名称" maxlength="20" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="shelfDescribe" :label-width="formLabelWidth">
          <el-input v-model="rowData.shelfDescribe" placeholder="请输入描述" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
          <el-select v-model="rowData.type" filterable placeholder="请选择类型" style="width: 90%;">
            <el-option
              v-for="item in shelfTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');clearForm();dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="shelfs"
        style="width: 100%">
       
        <el-table-column
          fixed="left"
          sortable
          prop="shelfName"
          label="名称"
          >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfCode"
          label="编码"
         >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
        >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="areaName"
          label="货区"
         >
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfDescribe"
          label="描述">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="type"
          label="类型"
         >
          <template slot-scope="scope">
            <span v-for="(item,index) in shelfTypes" :key="index" v-if="item.value === scope.row.type">{{item.label}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfRow"
          label="行"
          width="80">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="shelfColumn"
          label="列"
          width="80">
        </el-table-column> -->
<!--          <el-table-column-->
<!--          show-overflow-tooltip-->
<!--          fixed="right"-->
<!--          prop="state"-->
<!--          label="状态"-->
<!--          width="80">-->
<!--          <template slot-scope="scope">-->
<!--            <el-switch-->
<!--              @change="changeState(scope.row)"-->
<!--               active-color="#13ce66"-->
<!--              inactive-color="#ff4949"-->
<!--              active-value="1q"-->
<!--              inactive-value="0q"  -->
<!--               v-model="scope.row.state + 'q'">-->
<!--            </el-switch>-->
<!--          </template>-->
<!--        </el-table-column>-->
        
        <el-table-column
          fixed="right"
          label="操作"
          width="150">
          <template slot-scope="scope">
             
            <el-button class="textButton" @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"   
                       size="small"></el-button>
            <el-button     @click="isDelete(scope.row.shelfInfoId)" icon="iconfont icon-shanchu" type="text"   class="textButton tableBtn danger"
                       size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination-bottom"
              style="text-align: right;"
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

  export default {
    name: 'Shelf',
    data() {
      let validateShelfCode = (rule, value, callback) => {
        this.validatorShelf(rule, value, callback, 'shelfCode', this.searchData.wareId,this.shelfInfoId);
      };
      let validateShelfCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'shelf_info', 'shelf_code', this.rowData.shelfCode, 1, this.rowData.shelfInfoId);
      };
      let validateShelfName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'shelf_info', 'shelf_name', this.form.shelfName, 0);
      };
      let validateShelfName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'shelf_info', 'shelf_name', this.rowData.shelfName, 1, this.rowData.shelfInfoId);
      };

      let validateCellCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'cell_info', 'cell_code', this.form.cellCode, 0);
      };
      let validateCellName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'cell_info', 'cell_name', this.form.cellName, 0);
      };
      let validateNo0 = (rule, value, callback) => {
        if (value === 0) {
          callback(new Error('不能为 0'));
        } else {
          callback();
        }
      };
      return {
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          areaId: null
        },

        shelfs: [],
        wares: [],
        areas: [],
        wareId: null,
        shelfInfoId: null,

        dialogNewVisible: false,
        dialogUpdateVisible: false,


        form: {
          areaId: null,
          shelfCode: '',
          shelfName: '',
          shelfDescribe: '',
          type: null,
          state: '0',
          // isCreateCell: false,
          shelfRow: 0,
          shelfColumn: 0,

          groundOrder: null,
          pickOrder: null,
          cellUseType: null,
          cellAttribute: '',
          cellHandle: '',
          length: null,
          wide: null,
          high: null,
          cellAbcLevel: '',
          volumeLimit: null,
          weightLimit: null,
          boxLimit: null,
          quantityLimit: null,
          palletLimit: null,
          xCoordinate: null,
          yCoordinate: null,
          cellPickType: '',
          wareId: null
        },
        rules: {
          areaId: [
            {required: true, message: '请选择货区', trigger: 'blur'}
          ],
          shelfCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateShelfCode, trigger: 'blur'}
          ],
          shelfName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            // {validator: validateShelfName, trigger: 'blur'}
          ],
          shelfDescribe: [
            {required: true, message: '请输入货位组描述', trigger: 'blur'}
          ],
          type: [
            {required: true, message: '请选择类型', trigger: 'blur'}
          ],
          shelfRow: [
            {required: true, message: '请输入行', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          shelfColumn: [
            {required: true, message: '请输入列', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],

          shelfInfoId: [
            {required: true, message: '请选择货位组', trigger: 'blur'}
          ],
          cellCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateCellCode, trigger: 'blur'}
          ],
          cellName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateCellName, trigger: 'blur'}
          ],
          groundOrder: [
            {required: true, message: '请输入上架顺序', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          pickOrder: [
            {required: true, message: '请输入拣货顺序', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          cellUseType: [
            {required: true, message: '请选择货位使用类型', trigger: 'blur'}
          ],
          cellAttribute: [
            {required: true, message: '请输入货位属性', trigger: 'blur'}
          ],
          cellHandle: [
            {required: true, message: '请输入货位处理', trigger: 'blur'}
          ],
          length: [
            {required: true, message: '请输入长', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          wide: [
            {required: true, message: '请输入宽', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          high: [
            {required: true, message: '请输入高', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          cellAbcLevel: [
            {required: true, message: '请选择周转需求', trigger: 'blur'}
          ],
          volumeLimit: [
            {required: true, message: '请输入体积限制', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          weightLimit: [
            {required: true, message: '请输入重量限制', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          boxLimit: [
            {required: true, message: '请输入箱数限制', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          quantityLimit: [
            {required: true, message: '请输入数量限制', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          palletLimit: [
            {required: true, message: '请输入托盘限制', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          xCoordinate: [
            {required: true, message: '请输入X坐标', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          yCoordinate: [
            {required: true, message: '请输入Y坐标', trigger: 'blur'},
            {validator: validateNo0, trigger: 'blur'}
          ],
          cellPickType: [
            {required: true, message: '请选择拣货方式', trigger: 'blur'}
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          areaId: null,
          shelfCode: '',
          shelfName: '',
          shelfDescribe: '',
          type: null,
          state: 0,
        },
        rules2: {
          areaId: [
            {required: true, message: '请选择货区', trigger: 'blur'}
          ],
          shelfCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateShelfCode, trigger: 'blur'}
            // {validator: validateShelfCode2, trigger: 'blur'}
          ],
          shelfName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            // {validator: validateShelfName2, trigger: 'blur'}
          ],
          shelfDescribe: [
            {required: false, message: '请输入货位组描述', trigger: 'blur'}
          ],
          type: [
            {required: true, message: '请选择类型', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      ...mapState([
        "cellUseTypes",
        "cellAbcLevels",
        "cellPickTypes",
        "shelfTypes",
      ]),
      formVolumeLimitMax() {
        if (this.form.length > 0 && this.form.wide > 0 && this.form.high > 0) {
          let volume = this.form.length * this.form.wide * this.form.high;
          return volume
        } else {
          return 0
        }
      },
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getShelfData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getShelfData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        // this.searchData.wareId = null;
        this.searchData.type = null;
        this.searchData.areaId = null;
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getShelfData();
      },
      getShelfData() {
        IOT.getServerData('/shelf/infos/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
             
            this.shelfs = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      /*getWareData() {
        IOT.getServerData('/ware/infos/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
            this.wareId = this.wares[0].wareId;
            this.getAreaData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },*/
      getAreaData() {
        IOT.getServerData('/area/infos/list', 'get', {wareId: PF.getLocal('wareId', 'number')}, (ret) => {
          if (ret.code === 200) {
            this.areas = ret.rows;
            this.form.areaId = null;
            this.rowData.areaId = null;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      clearForm() {
        // this.wareId = this.wares[0].wareId;
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.form.wareId = this.searchData.wareId;
            let form = PF.JSON(this.form);
            let url = '';
           
              url = '/shelf/infos/autoInsert';
            

            IOT.getServerData(url, 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getShelfData()
                });
                this.resetForm('form');
                this.clearForm();
                this.dialogNewVisible = false
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });

          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.rowData.wareId = this.searchData.wareId;
            IOT.getServerData('/shelf/infos/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getShelfData()
                });
                this.resetForm('rowData');
                this.clearForm();
                this.shelfInfoId = null;
                this.dialogUpdateVisible = false
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row) {
        this.rowData = JSON.parse(JSON.stringify(row));
        this.shelfInfoId = this.rowData.shelfInfoId;
        this.dialogUpdateVisible = true
      },
      resetForm(formName) { // 重置
        if (formName === 'form') {
          this.form.boxReplenish = false;
          this.form.scatteredReplenish = false;
        } else if (formName === 'rowData') {
          this.rowData.boxReplenish = false;
          this.rowData.scatteredReplenish = false;
        }
        this.$refs[formName].resetFields();
        this.shelfInfoId = null;
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form');
        this.clearForm();
        this.dialogNewVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData');
        this.clearForm();
        this.dialogUpdateVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(() => {
            this.deleteShelf(id)
          })
          .catch(() => {
          });
      },
      deleteShelf(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/shelf/infos/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getShelfData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeState(row) {
        
         if(row.state==1){
           row.state=0;
         }
         else if(row.state==0){
           row.state=1;
         }
        IOT.getServerData('/shelf/infos/update'  , 'post', {shelfInfoId: row.shelfInfoId, state: row.state}, (ret) => {
          if (ret.code === 200) {
            if(row.state === 1){
              IOT.tips('已启用！', 'success');
            }else if(row.state === 0){
              IOT.tips('已禁用！', 'error');
               
            }
            this.getShelfData()
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            this.getShelfData()
          }
        });
         
      },
      validatorShelf(rule, value, callback, fieldName, wareId, shelfInfoId){
        let form = {
          [fieldName]: value, // 字段名
          wareId,
          shelfInfoId,
        };
        IOT.getServerData("/shelf/infos/validator", "post", form, (ret) => {
          if (
                  ret.code === 100001 ||
                  ret.code === 100002 ||
                  ret.code === 100004
          ) {
            callback(new Error(ret.message || "已重复"));
          } else if (ret.code === 100003) {
            callback();
          } else {
            PF.publicMessage(
                    ret.message || "服务器请求失败，稍后再试！",
                    "error"
            );
            callback();
          }
        });
      },
    },
    created() {
      this.getShelfData();
      // this.getWareData();
      this.getAreaData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .shelf {

  }
</style>
