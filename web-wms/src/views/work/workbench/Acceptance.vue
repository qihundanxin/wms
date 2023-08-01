<!--<style type="text/css">
  #guapi{
    border:1px solid gray;
    box-shadow: 2px 4px 6px gray;
    border-radius: 10px;
  }
  .guapi{
    border:1px solid gray;
    margin:20px;
    border-radius: 10px;
  }
  #inside{
    margin:20px
  }
  #billin{
    font-size:30px;
    color:#4499FF
  }
  .div-inline1{
    display:inline-block;
    margin:30px;
    width:1000px
  }
  .div-inline{
    display:inline-block;
    margin:30px;
  }
  .div-inline2{
    display:inline-block;
    margin:10px;
  }
  #inline-font{
    margin-right:5px;
    font-size:30px;
  }
  #inline-font3{
    font-size:20px;
  }
  .element_container{
    width: 200px;
    display: inline-block;
  }
  .buttonFont{
    font-size:40px;
    width: 66px;
    height: 35px;
    vertical-align:middle;
    text-align: center;
    color:white;
  }
  #buttonStyle{
    height:80px;
    width:200px;
    background:#1E90FF;
    border-radius:15px;
    text-align:center;
    display:block;
    margin:0 auto;
  }
  .buttonFont1{
    font-size:35px;
    width: 55px;
    height: 28px;
    vertical-align:middle;
    text-align: center;
    color:white;
  }
  .buttonFont1{
    font-size:50px;
    width: 55px;
    height: 28px;
    vertical-align:middle;
    text-align: center;
    color:white;
  }
  #buttonStyle1{
    height:60px;
    width:140px;
    background:#999;
    border-radius:5px;
    text-align:center;
    display:block;
    margin:0 auto;
  }
  #buttonStyle3{
    height:60px;
    width:170px;
    background:#999;
    border-radius:5px;
    text-align:center;
    display:block;
    margin:0 auto;
  }
  #buttonStyle2{
    height:200px;
    width:300px;
    background:#D3D3D3;
    border-radius:5px;
    text-align:center;
    display:block;
    margin:0 auto;
  }
  .div-inline-two:{
    margin-top:20px;
  }
  .div-inline-three:{
    margin-top:20px;
  }
  .el-button&#45;&#45;primary {
    color: #FFF;
    background-color: #409EFF;
    border-color: #409EFF00;
  }
  div.popContainer{
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0,0,0,0.3);
    text-align: center;
  }
  .div-inline1 .el_switch *{
    font-size: 28px;
  }
  .clearfloat:after {
    display: block;
    clear: both;
    content: "";
    visibility: hidden;
    height: 0
  }
  .clearfloat {
    zoom: 1
  }
</style>-->
<template>

  <div class="acceptance">
    <div id="inside">
      <div><span id="billin">验收操作台</span></div>
      <hr/>
      <div class="clearFloat">
        <div class="div-inline1 leftFloat" >
          <!--<div class="headerBtns">
            <el-button size="small" type="success" round  @click="clearInput()"> 清空</el-button>
          </div>-->

          <div class="bigFormBox2 acceptanceDiv" id="top">
            <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
              <el-form-item label="编码" prop="code" :label-width="formLabelWidth" style="font-size: 20px;">
                <el-input v-model="code" placeholder="扫码枪扫描来料的二维码" size="small" clearable style="width: 90%;"></el-input>
              </el-form-item>
              <el-form-item label="数量" prop="toQuantity" :label-width="formLabelWidth">
                <el-input-number v-model="form.toQuantity" :min="0" label="可以手动输入/可以累加" size="small" style="width: 90%;"></el-input-number>
              </el-form-item>
              <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
                <el-input v-model="form.toBoxCode" placeholder="请输入追踪号" size="small" clearable style="width: 90%;"></el-input>
              </el-form-item>
              <el-form-item label="收货货位" prop="toCellCode" :label-width="formLabelWidth">
                <el-input v-model="form.toCellCode" placeholder="请输入收货货位" size="small" clearable style="width: 90%;"></el-input>
              </el-form-item>
              <el-form-item label="成本单价" prop="costPrice" :label-width="formLabelWidth">
                <el-input-number v-model="form.costPrice" :min="0" label="请输入成本单价" size="small" style="width: 90%;"></el-input-number>
              </el-form-item>
              <el-form-item label="销售单价" prop="salePrice" :label-width="formLabelWidth">
                <el-input-number v-model="form.salePrice" :min="0" label="请输入销售单价" size="small" style="width: 90%;"></el-input-number>
              </el-form-item>
              <el-form-item label="质检状态" prop="qcState" :label-width="formLabelWidth">
                <el-select v-model="form.qcState" filterable placeholder="请选择质检状态" size="small" style="width: 90%;">
                  <el-option
                    v-for="item in qcStates"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>

          <!--<div>
            <div>
              <div class="div-inline2">
                <span id="inline-font" style="margin-left: 60px;">编码:</span>
                <div class="element_container" style="width:300px">
&lt;!&ndash;                  <el-input placeholder="扫码枪扫描来料的二维码" clearable id="itemCode" size="small" v-model="code" @change="acceptance()"/>&ndash;&gt;
                  <el-input placeholder="扫码枪扫描来料的二维码" clearable id="itemCode" size="small" v-model="code"/>
                </div>

              </div>
              <div class="div-inline2" >
                <span id="inline-font" style="margin-left: 60px;">数量:</span>
                <div class="element_container" style="width:300px">
                  <el-input placeholder="可以手动输入/可以累加" size="small" v-model="form.toQuantity"/>
                </div>
              </div>
            </div>
            <div >
              <div class="div-inline2">
                <span id="inline-font" style="margin-left: 30px;">追踪号:</span>
                <div class="element_container" style="width:300px">
                  <el-input placeholder="请输入追踪号" id="toBoxCode" clearable size="small" v-model="form.toBoxCode"/>
                </div>
              </div>
              <div class="div-inline2">
                <span id="inline-font">收货货位:</span>
                <div class="element_container" style="width:300px">
                  <el-input placeholder="请输入收货货位" clearable size="small" v-model="form.toCellCode" />
                </div>
              </div>
            </div>
            <div >
              <div class="div-inline2">
                <span id="inline-font">成本单价:</span>
                <div class="element_container" style="width:300px">
                  <el-input placeholder="请输入成本单价" clearable size="small" v-model="form.costPrice" />
                </div>
              </div>
              <div class="div-inline2">
                <span id="inline-font">销售单价:</span>
                <div class="element_container" style="width:300px">
                  <el-input placeholder="请输入销售单价" clearable size="small" v-model="form.salePrice"/>
                </div>
              </div>
            </div>
            <div >
              <span id="inline-font" style="margin-left: 10px;">检验状态:</span>
              <div class="element_container" style="width:300px">
                <el-select v-model="form.qcState" filterable placeholder="请选择合格状态" style="width: 70%;">
                  <el-option
                          v-for="item in qcStates"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </div>
          </div>-->

          <div class="billMasterBox acceptanceDiv clearFloat">
            <div class="billData leftFloat clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>货主:</span>
                  <span :title="theInput.organizationName">&nbsp;{{theInput.organizationName}}</span>
                </li>
                <li>
                  <span>供应商编码:</span>
                  <span :title="theInput.supplierCode">&nbsp;{{theInput.supplierCode}}</span>
                </li>
                <li>
                  <span>类型:</span>
                  <span :title="theInput.billType">&nbsp;{{theInput.billType}}</span>
                </li>
                <li>
                  <span>SKU名称:</span>
                  <span :title="theInput.itemName">&nbsp;{{theInput.itemName}}</span>
                </li>
              </ul>
              <ul class="rightFloat">
                <li>
                  <span>已接收数量:</span>
                  <span :title="theInput.acceptQuantity">&nbsp;{{theInput.acceptQuantity}}</span>
                </li>
                <li>
                  <span>单号:</span>
                  <span :title="theInput.asnBillNo">&nbsp;{{theInput.asnBillNo}}</span>
                </li>
                <li>
                  <span>入库单号:</span>
                  <span :title="theInput.asnDetailNo">&nbsp;{{theInput.asnDetailNo}}</span>
                </li>
                <li>
                  <span>请购单号:</span>
                  <span :title="theInput.requestDetailDetailNo">&nbsp;{{theInput.requestDetailDetailNo}}</span>
                </li>
              </ul>
            </div>
          </div>

          <div class="bigFormBox2 acceptanceDiv" style="border: 1px solid gray;border-radius: 10px;padding: 10px 0;margin: 20px;">
            <h2 style="text-align: center;margin-bottom: 20px;">批次策略</h2>
            <el-form class="clearFloat" :model="form" :rules="rules" ref="form">

              <el-form-item v-for="(item,index) in batchTactics.batchTacticDetails" :key="index" :label="item.detailName" :prop="item.detailCode"
                            :label-width="formLabelWidth">
                <el-select v-if="item.detailCode === 'qcState'" v-model="form[item.detailCode]" filterable size="small"
                           :placeholder="'请选择'+item.detailName" style="width: 90%;">
                  <el-option
                    v-for="item2 in qcStates"
                    :key="item2.value"
                    :label="item2.label"
                    :value="item2.value">
                  </el-option>
                </el-select>

                <el-input v-else-if="item.format === 0" v-model="form[item.detailCode]" :placeholder="'请输入'+item.detailName"
                          :disabled="item.detailCode === 'detailNo' || item.detailCode === 'supplierCode'" size="small"
                          style="width: 90%;"></el-input>
                <el-input-number v-else-if="item.format === 1" v-model="form[item.detailCode]"
                                 :min="0" label="请输入" size="small" style="width: 90%;"></el-input-number>
                <el-date-picker v-else-if="item.format === 2" v-model="form[item.detailCode]" value-format="yyyy-MM-dd"
                                type="date" :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-date-picker>
                <el-time-picker v-else-if="item.format === 3" v-model="form[item.detailCode]" value-format="HH:mm:ss"
                                :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-time-picker>
                <el-select v-else-if="item.format === 4" v-model="form[item.detailCode]" filterable
                           :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;">
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


          <p>testCode: {{testCode}}</p>
          <p>code2: {{code2}}</p>
          <p>form: {{form}}</p>

          <!--<div class="guapi clearfloat" style="padding:20px">
            <div  style="width:40%;float: left;">
              <div style="margin-top:10px;position: relative;left: 90px;">
                <span id="inline-font">货主:</span>
                <span id="inline-font3">{{theInput.organizationName}}</span>
              </div>
              <div  style="margin-top:10px" >
                <span id="inline-font">供应商编码:</span>
                <span id="inline-font3">{{theInput.supplierCode}}</span>
              </div>
              <div style="margin-top:10px;position: relative;left: 78px;">
                <span id="inline-font">类型:</span>
                <span id="inline-font3">{{theInput.billType}}</span>
              </div>
              <div class="clearfloat" style="margin-top:10px;position: relative;left: 30px;">
                <div style="float: left;width：37%">
                  <span  id="inline-font">SKU名称:</span>
                </div>
                <div style="float: right;width:63%">
                  <span id="inline-font3">{{theInput.itemName}}</span>
                </div>
              </div>
            </div>
            <div  style="width:40%;margin-left:5%;float:right;">
              <div style="margin-top:10px;position: relative;left: 70px;">
                <span id="inline-font">已接收数量:</span>
                <span id="inline-font3">{{theInput.acceptQuantity}}</span>
              </div>
              <div style="margin-top:10px;position: relative;left: 40px;">
                <span id="inline-font">单号:</span>
                <span id="inline-font3">{{theInput.asnBillNo}}</span>
              </div>
              <div style="margin-top:10px;position: relative;left: 10px;">
                <span id="inline-font">入库单号:</span>
                <span id="inline-font3">{{theInput.asnDetailNo}}</span>
              </div>
              <div style="margin-top:10px;position: relative;left: -20px;">
                <span id="inline-font">请购单号:</span>
                <span id="inline-font3">{{theInput.requestDetailDetailNo}}</span>
              </div>
            </div>
          </div>-->
          <!--<div>
            <div style="text-align: center;">
              <el-button type="primary" style="display: inline-block;height:80px;width:200px;background:#1E90FF;
							border-radius:15px;text-align:center;display:block;margin:0 auto;" size="small" id="billInVerify"  @click="billInWare()">
                <span style="font-size:35px;width: 66px;height: 35px;vertical-align:middle;text-align: center;color:white;">入库</span>
              </el-button>
              <el-button type="primary" size="small" style="height:80px;width:200px;background:#1E90FF;border-radius:15px;
						text-align:center;display:block;margin:0 auto;" id="initializeButton"  @click="billInWare()" display="none">
                <span style="font-size:35px;width: 66px;height: 35px;vertical-align:middle;text-align: center;color:white;">初始化入库</span>
              </el-button>
            </div>
          </div>-->
        </div>

        <div class="div-inline-right rightFloat">
          <div class="inventoryListBox">
            <h2 style="text-align: center;margin-bottom: 20px;">库存余量</h2>
            <div class="tableBox rowHeight">
              <el-table
                highlight-current-row
                :data="inventoryBalances"
                style="width: 100%">
                <el-table-column
                  fixed="left"
                  type="index"
                  width="50">
                </el-table-column>
                <el-table-column
                  fixed="left"
                  sortable
                  prop="itemName"
                  label="SKU名称"
                  width="150">
                  <template slot-scope="scope">
                    <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="quantity2"
                  label="数量"
                  width="150">
                  <template slot-scope="scope">
                    {{scope.row.quantity2}}
                  </template>
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="packDescribe"
                  label="单位"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="itemCode"
                  label="SKU"
                  width="150">
                  <template slot-scope="scope">
                    <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
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
                  prop="batchName"
                  label="批次"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="allotQuantity"
                  label="分配数量"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="freezeQuantity"
                  label="冻结数量"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="cellCode"
                  label="货位"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="boxCode"
                  label="追踪号"
                  width="150">
                </el-table-column>
                <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="lpn"
                  label="LPN码"
                  width="150">
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
          </div>
        </div>


        <!--<div class="div-inline">
          <div class="div-inline-three" style="margin-top:20px;position: relative;">
            <el-button type="primary" size="small" id="buttonStyle2" class="buttonFont1" style="white-space:normal;">{{warningMessage}}</el-button>
          </div>
        </div>-->
      </div>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
        <el-button type="success" @click="clearInput()">清 空</el-button>
      </div>
      <div class="rightFloat">
        <el-button @click="commitForm(form)" type="primary">验 收</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import pako from 'pako';
  import {mapState} from "vuex";

  export default{
    name: 'acceptance',
    data(){
      let validateItemCode = (rule, value, callback) => {
        this.validator(rule, value, callback, 'item_info', 'item_code', this.form.itemCode, 0);
      };
      let validateCellCode = (rule, value, callback) => {
        this.validator(rule, value, callback, 'cell_info', 'cell_code', this.form.toCellCode, 0);
      };
      return {
        state:'',
        theInput:{
          wareId:'',
          wareName:'',
          organizationName:'',
          itemName:'',
          toPackDetailId:'',
          toPackDescribe:'',
          expectQuantity:'',
          toQuantity:'',
          toBoxCode:'',
          toCellCode:'',
          billType:'',
          acceptQuantity:'',
          asnBillNo:'',
          asnDetailNo:'',
          requestDetailDetailNo:'',
          itemCode:'',
          organizationId:'',
          productDate:'',
          exDate:'',
          supplierCode:'',
          supplierBatch:'',
          qcState:'',
          costPrice:'',
          salePrice:'',
          detailNo:'',
        },

        testCode: '{ "detailNo": "ASN-55-1", "requestDetailDetailNo": null, "itemCode": "IM191122-77415-648", "printQuantity": 5, "quantity": 1, "batchNo": "qwe", "time1": "qwe", "time2": "qwe" }',
        code:'',
        code2:'',

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
        },

        batchTactics: {},
        asnDetail: {},
        inventoryBalances: [],

        form: {
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
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '请选择单位', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '请输入SKU', trigger: 'blur'},
            {validator: validateItemCode, trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '请输入货位编码', trigger: 'blur'},
            {validator: validateCellCode, trigger: 'blur'}
          ],
          toBoxCode: [
            {required: true, message: '请输入追踪号', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            {type: 'number', message: '数量必须为数字值', trigger: ['blur', 'change']}
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
        formLabelWidth: '120px',

      }
    },
    watch: {
      code(val,oval){
        if(val.length > 0){
          this.code2 = JSON.parse(val);
          this.code = '';
          this.changeCode();
        }
      }
    },
    computed: {
      ...mapState([
        "reviewStates",
        "pickTaskType",
        "createSource",
        "qcStates",
      ]),
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
    },
    methods: {
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryBalanceData()
      },
      changeCode(){
        let code2 = PF.JSON(this.code2);
        this.form.toQuantity = code2.quantity;
        this.code2 = code2;

        this.getAsnDetailData(code2.detailNo);
      },
      getAsnDetailData(detailNo){
        IOT.getServerData('/asn/details/list','get',{detailNo: detailNo},(ret) => {
          if (ret.code === 200) {
            let list = ret.rows[0];
            list.asnBillNo = list.billNo;
            list.asnDetailNo = list.detailNo;
            this.asnDetail = list;

            this.theInput.organizationName = list.organizationName;
            this.theInput.supplierCode = list.supplierCode;
            this.theInput.billType = list.billType;
            this.theInput.itemName = list.itemName;
            this.theInput.acceptQuantity = list.acceptQuantity;
            this.theInput.asnBillNo = list.asnBillNo;
            this.theInput.asnDetailNo = list.asnDetailNo;
            this.theInput.requestDetailDetailNo = list.requestDetailDetailNo;

            this.getBatchTacticData(list.organizationId, list.itemCode);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getBatchTacticData(organizationId,itemCode) {
        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: organizationId,
          itemCode: itemCode
        }, (ret) => {
          if (ret.code === 200) {
            let list = PF.JSON(ret.rows[0])
            let detailDetails = list.batchTacticDetailDetails;
            let testDetailDetails = [];
            list.batchTacticDetails[12].values = [];
            list.batchTacticDetails[13].values = [];
            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let i = 0; i < batchTacticDetails.length; i++) {
              if (batchTacticDetails[i].state === 1) {
                testBatchTacticDetails.push(batchTacticDetails[i])
                if (batchTacticDetails[i].isRequired === 1) {
                  this.rules[batchTacticDetails[i].detailCode][0].required = true;
                }
                if (batchTacticDetails[i].detailCode === 'inDate') {
                  this.form[batchTacticDetails[i].detailCode] = PF.getNowFormatDate();
                }
                if (batchTacticDetails[i].detailCode === 'detailNo') {
                  this.form.detailNo = this.theInput.asnDetailNo;
                }else if (batchTacticDetails[i].detailCode === 'supplierBatch') {
                  this.form.supplierBatch = this.code2.batchNo;
                }else if (batchTacticDetails[i].detailCode === 'supplierCode') {
                  this.form.supplierCode = this.theInput.supplierCode;
                }
              }
            }

            list.batchTacticDetailDetails = testDetailDetails;
            list.batchTacticDetails = testBatchTacticDetails;
            // this.form.detailNo = this.theInput.asnDetailNo;
            // this.form.supplierBatch = this.theInput.batchNo;
            // this.form.supplierCode = this.theInput.supplierCode;
            this.batchTactics = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitForm(formName){
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = {};
            form = PF.JSON(this.form);
            for(let key in form){
              if(this.asnDetail[key]){
                form[key] = this.asnDetail[key]
              }
            }
            
            IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: [form]}, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('验收成功！', 'success');
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form');
            this.batchTactics = {};
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      validator(rule, value, callback, tableName, fieldName, fieldValue, type, idValue) {
        let form = {
          tableName: tableName, // 表名
          fieldName: fieldName, // 字段名
          fieldValue: fieldValue, // 字段值
          type: type, // 0-新增 1-更新
          idValue: idValue, // type为0时不传，type为1是必传，id值
        };

        IOT.getServerData('/validator', 'post', form, (ret) => {
          if (ret.code === 100001 || ret.code === 100002 || ret.code === 100004) {
            callback();
          } else if (ret.code === 100003) {
            callback(new Error('不存在'));
          } else {
            PF.publicMessage(ret.message || '服务器请求失败，稍后再试！', 'error');
            callback();
          }
        });
      },
      getInventoryBalanceData() {
        let searchData = PF.JSON(this.searchData);
        searchData.pageSize = searchData.pageSize * 5;
        IOT.getServerData('/inventorys/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].quantity2 = list[i].quantity;
              list[i].packDetailId2 = list[i].packDetailId;
            }
            this.inventoryBalances = list;
            this.searchData.total = ret.total / 5;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      clearInput(){
        let that = this;
        that.code = '',
        that.state = '',
        that.theInput = {
          wareId:'',
          wareName:'',
          organizationName:'',
          itemName:'',
          toPackDetailId:'',
          toPackDescribe:'',
          expectQuantity:'',
          toQuantity:'',
          toBoxCode:'',
          toCellCode:'',
          billType:'',
          acceptQuantity:'',
          asnBillNo:'',
          asnDetailNo:'',
          requestDetailDetailNo:'',
          itemCode:'',
          organizationId:'',
          productDate:'',
          exDate:'',
          supplierCode:'',
          supplierBatch:'',
          qcState:'',
          costPrice:'',
          salePrice:'',
          detailNo:'',
        }
      },

      acceptance(){
        let that = this;
        IOT.showOverlay('处理中，请稍等...');
        let test1 = {
          batchNo: "123",
          detailNo: "ASN-49-1",
          itemCode: "IM191211-41423-229",
          printQuantity: "1",
          requestDetailDetailNo: "CJ-67-1",
          time1: "2019-11-22",
          time2: "2020-11-22"
        }
        let test2 = pako.deflate(JSON.stringify(test1), { to: 'string' });

        let test3 = pako;
        let test4 = pako.inflate(test2, { to: 'string' });
        let test5 = JSON.parse(pako.inflate(test2, { to: 'string' }));
        if(that.theInput.toBoxCode === ''){
          that.code = '',
          IOT.tips('请填写追踪号', 'error');
          return false;
        }
        if(that.theInput.toCellCode === ''){
          that.code = '',
          IOT.tips('请填写收货货位', 'error');
          return false;
        }
        if(that.theInput.costPrice === ''){
          that.code = '',
          IOT.tips('请填写成本单价', 'error');
          return false;
        }
        if(that.theInput.salePrice === ''){
          that.code = '',
          IOT.tips('请填写销售单价', 'error');
          return false;
        }
        if(that.theInput.state === ''){
          that.code = '',
          IOT.tips('请选择状态', 'error');
          return false;
        }


        if(test5.detailNo !== ''){
            IOT.getServerData('/asn/details/findByDetailNo', 'get', {detailNo: test5.detailNo}, (ret) => {
              if (ret.code === 200) {
                
                let detail = ret.rows[0];
                that.code = '';
                that.theInput = {
                  wareId:detail.wareId,
                  wareName:detail.wareName,
                  organizationName:detail.organizationName,
                  itemName:detail.itemName,
                  toPackDetailId:detail.packDetailId,
                  toPackDescribe:detail.packDescribe,
                  expectQuantity:detail.expectQuantity,
                  billType:detail.billType,
                  acceptQuantity:detail.acceptQuantity,
                  asnBillNo:detail.billNo,
                  asnDetailNo:detail.detailNo,
                  requestDetailDetailNo:detail.requestDetailDetailNo,
                  itemCode:detail.itemCode,
                  productDate:test5.time1,
                  exDate:test5.time2,
                  supplierCode:detail.supplierCode,
                  supplierBatch:test5.batchNo,
                  qcState:that.state,
                  detailNo:detail.detailNo,
                }
                //验收，没有传参数
                IOT.getServerData('/accept/records/accept', 'post', {}, (ret) => {
                  IOT.hideOverlay();
                  if (ret.code === 200) {
                    IOT.tips('处理成功！', 'success', 1000, () => {
                      that.state = '';
                      that.theInput = {
                        toBoxCode: '',
                        toCellCode: '',
                        qcState: '',
                        costPrice: '',
                        salePrice: '',
                      }
                    });
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });

        }
      },
    },
    created() {
      this.getInventoryBalanceData();
      $('#itemCode').focus();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .acceptance{
    padding-bottom: 100px;

    #billin{
      font-size:30px;
      color:#4499FF;
    }
    .div-inline1{
      margin-top: 20px;
      width: 65%;
    }
    .div-inline-right{
      width: 35%;
      .inventoryListBox{
        border: 1px solid gray;
        border-radius: 10px;
        margin-top: 20px;
        padding: 10px;
      }
    }
  }
</style>
