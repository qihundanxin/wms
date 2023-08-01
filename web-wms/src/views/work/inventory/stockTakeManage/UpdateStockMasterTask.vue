<template>
  <div class="createStockMasterTask">

    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">修改盘点信息</p>
          <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
              <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
                  <el-select v-model="form.wareId" disabled filterable placeholder="请选择仓库" size="small"
                             style="width: 100%;">
                      <el-option
                              v-for="item in wares"
                              :key="item.wareId"
                              :label="item.wareName"
                              :value="item.wareId">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
                  <el-select v-model="form.type" filterable placeholder="请选择类型" size="small"
                             style="width: 100%;">
                      <el-option
                              v-for="item in stockTaskMasterType"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="优先级" prop="priority" :label-width="formLabelWidth">
                  <el-select v-model="form.priority" filterable placeholder="请选择优先级" size="small"
                             style="width: 100%;">
                      <el-option
                              v-for="item in stockTaskMasterOrder"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                  <el-select v-model="form.organizationId" filterable placeholder="请选择货主" size="small"
                             style="width: 100%;">
                      <el-option
                              v-for="item in organizations"
                              :key="item.organizationId"
                              :label="item.organizationName"
                              :value="item.organizationId">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
                  <el-input
                          :disabled="form.organizationId === null || form.organizationId === undefined || form.organizationId === ''"
                          :title="form.itemCode" v-model="form.itemCode" placeholder="请输入SKU" style="width: 80%;"></el-input>
                  <el-button @click="showSelectItemModal" icon="iconfont icon-jia" type="text"
                             style="width: 10%;"></el-button>
              </el-form-item>
              <div style="float:left;width:100%;"></div>
              <el-form-item label="是否随机" prop="checkWare" :label-width="formLabelWidth"
                            class="checkItemBoxHeight">
                  <el-checkbox v-model="form.ifRandom" :true-label="1" :false-label="0">随机盘点</el-checkbox>
              </el-form-item>
              <el-form-item label="任务数" :label-width="formLabelWidth">
                  <el-input v-model="form.randomNum" placeholder="请输入任务数" size="small"></el-input>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left">from</el-divider>
              </div>
              <el-form-item label="从货区" prop="fromAreaCode" :label-width="formLabelWidth">
                  <el-select v-model="form.fromAreaCode" filterable placeholder="请选择货位" size="small"
                             style="width: 100%;" @change="getShelf(1)">
                      <el-option
                              v-for="item in areas"
                              :key="item.areaCode"
                              :label="item.areaName"
                              :value="item.areaCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="从货架区" prop="fromShelfCode" :label-width="formLabelWidth">
                  <el-select v-model="form.fromShelfCode" filterable placeholder="请选择货架" size="small"
                             style="width: 100%;" @change="getCell(1)"
                  >
                      <el-option
                              v-for="item in shelfs"
                              :key="item.shelfCode"
                              :label="item.shelfName"
                              :value="item.shelfCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="从货位" prop="fromCellCode" :label-width="formLabelWidth">
                  <el-select v-model="form.fromCellCode" filterable placeholder="请选择货位" size="small"
                             style="width: 100%;"
                  >
                      <el-option
                              v-for="item in cells"
                              :key="item.cellCode"
                              :label="item.cellName"
                              :value="item.cellCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left">to</el-divider>
              </div>
              <el-form-item label="至货区" prop="toAreaCode" :label-width="formLabelWidth">
                  <el-select v-model="form.toAreaCode" filterable placeholder="请选择货位" size="small"
                             style="width: 100%;" @change="getShelf(2)">
                      <el-option
                              v-for="item in areastwo"
                              :key="item.areaCode"
                              :label="item.areaName"
                              :value="item.areaCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="至货架区" prop="toShelfCode" :label-width="formLabelWidth">
                  <el-select v-model="form.toShelfCode" filterable placeholder="请选择货架" size="small"
                             style="width: 100%;" @change="getCell(2)"
                  >
                      <el-option
                              v-for="item in shelfstwo"
                              :key="item.shelfCode"
                              :label="item.shelfName"
                              :value="item.shelfCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="至货位" prop="toCellCode" :label-width="formLabelWidth">
                  <el-select v-model="form.toCellCode" filterable placeholder="请选择货位" size="small"
                             style="width: 100%;"
                  >
                      <el-option
                              v-for="item in cellstwo"
                              :key="item.cellCode"
                              :label="item.cellName"
                              :value="item.cellCode">
                      </el-option>
                  </el-select>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left">盘点开始时间</el-divider>
              </div>
              <el-form-item label="开始时间" prop="dynamicFromTime" :label-width="formLabelWidth">
                  <el-date-picker
                          v-model="form.dynamicFromTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          size="small"
                          placeholder="请选择预期到货日期">
                  </el-date-picker>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left">自定义字段</el-divider>
              </div>
              <el-form-item label="自定义1" :label-width="formLabelWidth">
                  <el-input v-model="form.udfHs1" placeholder="请输入自定义1" size="small"></el-input>
              </el-form-item>
              <el-form-item label="自定义2" :label-width="formLabelWidth">
                  <el-input v-model="form.udfHs2" placeholder="请输入自定义2" size="small"></el-input>
              </el-form-item>
              <el-form-item label="自定义3" :label-width="formLabelWidth">
                  <el-input v-model="form.udfHs3" placeholder="请输入自定义3" size="small"></el-input>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left">模糊查询条件</el-divider>
              </div>
              <el-form-item label="产品" prop="itemKeywords" :label-width="formLabelWidth">
                  <el-input v-model="form.itemKeywords" placeholder="请输入SKU" size="small"></el-input>
              </el-form-item>
              <el-form-item label="追踪号" prop="boxCode" :label-width="formLabelWidth">
                  <el-input v-model="form.boxCode" placeholder="请输入追踪号" size="small"></el-input>
              </el-form-item>
              <div style="float: left;width: 100%;">
                  <el-divider content-position="left"></el-divider>
              </div>
          </el-form>
      </div>
<!--      <span class="moreSearchBtn"-->
<!--            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"-->
<!--            @click="isMoreSearch = !isMoreSearch"></span>-->
    </div>
      <el-dialog width="800px" title="选择商品" :visible.sync="innerVisibleItem" append-to-body style="margin-top: -10vh;">

          <div class="publicSearch body">
              <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
                  <el-button-group>
                      <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                      <el-button @click="clearSearchSelectItem" size="mini" icon="el-icon-refresh">重置</el-button>
                  </el-button-group>
                  <div>
                      <span>关键字搜索</span>
                      <el-input v-model="searchSelectItem.keyWords" size="mini" placeholder="名称/规格/型号/包装/单价等"
                                clearable></el-input>
                  </div>
                  <div>
                      <span>商品种类</span>
                      <el-cascader
                              size="mini"
                              placeholder="请选择商品种类"
                              v-model="searchSelectItem.itemTypeId"
                              :options="itemTypeTree"
                              filterable></el-cascader>
                  </div>
                  <div>
                      <span>供应商</span>
                      <el-select v-model="searchSelectItem.supplierId" size="mini" filterable placeholder="请选择供应商"
                                 style="width: 70%;">
                          <el-option
                                  v-for="item in suppliers"
                                  :key="item.supplierId"
                                  :label="item.supplierName"
                                  :value="item.supplierId">
                          </el-option>
                      </el-select>
                  </div>
                  <!--<div>
                    <span>货主</span>
                    <el-select v-model="searchSelectItem.organizationId" size="mini" filterable placeholder="请选择货主" style="width: 70%;">
                      <el-option
                        v-for="item in organizations"
                        :key="item.organizationId"
                        :label="item.organizationName"
                        :value="item.organizationId">
                      </el-option>
                    </el-select>
                  </div>-->
              </div>
              <span class="moreSearchBtn"
                    :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"
                    @click="isMoreSearch2 = !isMoreSearch2"></span>
          </div>

          <div class="tableBox">
              <el-table
                      max-height="460"
                      :data="items"
                      style="width: 100%;">
                  <el-table-column
                          fixed="left"
                          type="index"
                          width="50">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          fixed="left"
                          prop="itemName"
                          label="SKU名称"
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
                          prop="itemTypeName"
                          label="商品种类"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="supplierName"
                          label="供应商"
                          width="180">
                  </el-table-column>
                  <!--<el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="organizationName"
                          label="货主"
                          width="180">
                  </el-table-column>-->
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="batchTacticName"
                          label="批次策略"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="defaultPrice"
                          label="默认单价"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="spec"
                          label="规格"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="model"
                          label="型号"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="packName"
                          label="包装编码"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="upperLimit"
                          label="库存上限"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="floorLimit"
                          label="库存下限"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          sortable
                          show-overflow-tooltip
                          prop="weight"
                          label="重量"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          fixed="right"
                          label="添加"
                          width="50">
                      <template slot-scope="scope">
                          <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                                     size="small"></el-button>
                      </template>
                  </el-table-column>
              </el-table>
              <el-pagination
                      style="text-align: right;"
                      @current-change="handleCurrentChange2"
                      :current-page.sync="searchSelectItem.pageNum"
                      :page-size="searchSelectItem.pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="searchSelectItem.total">
              </el-pagination>
          </div>

      </el-dialog>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.push({name: 'stockTakeMaster'})">返 回</el-button>
      </div>
      <div class="rightFloat">
        <el-button @click="commitForm('form')" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'createStockMasterTask',
    data() {
      return {
        formLabelWidth: '120px',
        wares: [],
        organizations: [],
          areas: [],
          areastwo: [],
          shelfs:[],
          shelfstwo:[],
          cells:[],
          cellstwo:[],
          suppliers: [],

        form: {
          stockTakeMasterId:this.$route.params.stockTakeMasterId,
          type:this.$route.params.type,
          state:0,
          priority:this.$route.params.priority,
          wareId:PF.getLocal('wareId', 'number'),
          wareName:'',
          wareCode:'',
          organizationId:this.$route.params.organizationId,
          organizationName:'',
          itemCode:this.$route.params.itemCode,
            itemName:this.$route.params.itemName,
          fromAreaCode:this.$route.params.fromAreaCode,
          toAreaCode:this.$route.params.toAreaCode,
          dynamicFromTime:this.$route.params.dynamicFromTime,
          fromShelfCode:this.$route.params.fromShelfCode,
          toShelfCode:this.$route.params.toShelfCode,
          itemKeywords:this.$route.params.itemKeywords,
          fromCellCode:this.$route.params.fromCellCode,
          toCellCode:this.$route.params.toCellCode,
          boxCode:this.$route.params.boxCode,
          udfHs1:this.$route.params.udfHs1,
          udfHs2:this.$route.params.udfHs2,
          udfHs3:this.$route.params.udfHs3,
          ifRandom:this.$route.params.ifRandom,
          randomNum:this.$route.params.randomNum,
        },

        isMoreSearch: true,
        isMoreSearch2: true,

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,

        },
          searchSelectItem: {
              pageNum: 1,
              pageSize: 5,
              total: null,
              keyWords: '',
              itemTypeId: '',
              supplierId: '',
              organizationId: '',
          },
          innerVisibleItem: false,
          items:[],
          itemTypeTree:[],
          packDetails:[],
          batchTactics:[],
        rules: {
          wareId: [{required: true, message: '请选择仓库', trigger: 'blur'}],
          type: [{required: true, message: '请选择类型', trigger: 'blur'}],
          priority: [{required: true, message: '请选择优先级', trigger: 'blur'}],
          organizationId: [{required: true, message: '请选择货主', trigger: 'blur'}],
          itemCode: [{required: true, message: '请输入SKU', trigger: 'blur'}],
          fromAreaCode: [{required: true, message: '请选择货区', trigger: 'blur'}],
          toAreaCode: [{required: true, message: '请选择货区', trigger: 'blur'}],
          dynamicFromTime: [{required: true, message: '请选择时间', trigger: 'blur'}],
          fromShelfCode: [{required: true, message: '请输入从货架区编码', trigger: 'blur'}],
          toShelfCode: [{required: true, message: '请输入至货架区编码', trigger: 'blur'}],
          itemKeywords: [{required: true, message: '请输入SKU', trigger: 'blur'}],
          fromCellCode: [{required: true, message: '请输入从货位', trigger: 'blur'}],
          toCellCode: [{required: true, message: '请输入至货位', trigger: 'blur'}],
          boxCode: [{required: true, message: '请输入追踪号', trigger: 'blur'}],
        }
      }
    },
    computed: {
      ...mapState([
        "stockTaskMasterType",
        "stockTaskMasterOrder"
      ]),
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
    },
    watch: {
    },
    methods: {
      getWareData(){
        IOT.getServerData('/ware/infos/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      getAreaData() {
        IOT.getServerData('/area/infos/list', 'get', {wareId:this.form.wareId}, (ret) => {
          if (ret.code === 200) {
            this.areas = ret.rows;
            this.areastwo = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
        showSelectItemModal() {
            if (!PF.isNull(this.form.organizationId)) {
                IOT.tips('未选择货主！', 'error');
                return false;
            }
            this.searchSelectItem.organizationId = this.form.organizationId;
            this.getItemData();
            this.getItemTypeData();
            this.innerVisibleItem = true;
        },
        getItemData() {
            let searchSelectItem = JSON.parse(JSON.stringify(this.searchSelectItem))
            searchSelectItem.itemTypeId = searchSelectItem.itemTypeId[searchSelectItem.itemTypeId.length - 1]
            IOT.getServerData('/item/infos/list', 'get', searchSelectItem, (ret) => {
                if (ret.code === 200) {
                    this.items = ret.rows;
                    this.searchSelectItem.total = ret.total;
                    this.searchSelectItem.pageNum = ret.pageNumber
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        getItemTypeData() {
            IOT.getServerData('/item/types/list', 'get', {}, (ret) => {
                if (ret.code === 200) {
                    let list = ret.rows;
                    for (let i = 0; i < list.length; i++) {
                        list[i].value = list[i].itemTypeId;
                        list[i].id = list[i].itemTypeId;
                        list[i].label = list[i].itemTypeName;
                    }
                    // this.itemTypes = list;
                    this.itemTypeTree = PF.toTree(list);
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        clearSearchSelectItem() {
            this.searchSelectItem.keyWords = '';
            this.searchSelectItem.itemTypeId = '';
            this.searchSelectItem.supplierId = '';
            this.searchSelectItem.organizationId = '';
        },
        handleCurrentChange2(val) {
            this.searchSelectItem.pageNum = val;
            this.getItemData()
        },
        selectItem(row) {
          this.form.itemName = row.itemName;
            this.form.itemCode = row.itemCode;
            this.getPackDetailData(row.packCode);
            this.getBatchTacticData(row.batchTacticCode);
            this.innerVisibleItem = false;
        },
        getPackDetailData(packCode) {
            IOT.getServerData('/pack/details/list', 'get', {packCode: packCode}, (ret) => {
                if (ret.code === 200) {
                    this.packDetails = ret.rows;
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        getBatchTacticData(batchTacticCode) {
            if(batchTacticCode==null || batchTacticCode==''){
                batchTacticCode = this.batchTacticCode;
            }
            IOT.getServerData('/batch/tactics/findByBatchTacticCode', 'get', {
                batchTacticCode:batchTacticCode
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
                        }
                    }

                    list.batchTacticDetailDetails = testDetailDetails;
                    list.batchTacticDetails = testBatchTacticDetails;
                    this.batchTactics = list;
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },


        getShelf(row){
            let that = this;
            let areaCode = '';
            if(row === 1){
                areaCode = that.form.fromAreaCode;

            }else{
                areaCode = that.form.toAreaCode;

            }
            IOT.getServerData('/shelf/infos/list', 'get', {areaCode:areaCode}, (ret) => {
                if (ret.code === 200) {
                    if(row === 1){
                        that.shelfs = ret.rows;
                    }else{
                        that.shelfstwo = ret.rows;
                    }
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },


        getCell(row){
            let that = this;
            let shelfCode = '';
            if(row === 1) {
                shelfCode = that.form.fromShelfCode;

            }else{
                shelfCode = that.form.toShelfCode;

            }
            IOT.getServerData('/cell/infos/list', 'get', {shelfCode:shelfCode}, (ret) => {
                if (ret.code === 200) {
                    if(row === 1)
                        that.cells = ret.rows;
                    else{
                        that.cellstwo = ret.rows;
                    }
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
      commitForm(formName) {
        IOT.showOverlay('保存中，请稍等...');
          this.$refs[formName].validate((valid) => {
              if (valid) {
                  let form = PF.JSON(this.form);
                  for (let i = 0; i < this.wares.length; i++) {
                      if (this.wares[i].wareId === form.wareId) {
                          form.wareName = this.wares[i].wareName;
                          form.wareCode = this.wares[i].wareCode;
                          break
                      }
                  }
                  for (let i = 0; i < this.organizations.length; i++) {
                      if (this.organizations[i].organizationId === form.organizationId) {
                          form.organizationName = this.organizations[i].organizationName;
                          break
                      }
                  }
                  if (form.ifRandom == 1 && !PF.isNull(form.randomNum)) {
                      IOT.tips('勾选随机盘点后请填写随机生成的任务数！', 'error', 1000, () => {
                          IOT.hideOverlay();
                      });
                      return false
                  }

                  IOT.getServerData('/stock/take/masters/update', 'post', form, (ret) => {
                      IOT.hideOverlay();
                      if (ret.code === 200) {
                          IOT.tips('保存成功！', 'success');
                          this.$router.push({name: 'stockTakeMaster'})
                      } else {
                          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                      }
                  });
              }else{
                  IOT.hideOverlay();
                  console.error('error submit!!');
                  return false;
              }
          });
      },

      clearForm:function(){
        this.form={
          type:1,
                  state:0,
                  priority:1,
                  wareId:PF.getLocal('wareId', 'number'),
                  wareName:'',
                  wareCode:'',
                  organizationId:'',
                  organizationName:'',
                  itemCode:'',
                  fromAreaCode:'',
                  toAreaCode:'',
                  dynamicFromTime:'',
                  fromShelfCode:'',
                  toShelfCode:'',
                  itemKeywords:'',
                  fromCellCode:'',
                  toCellCode:'',
                  boxCode:'',
                  udfHs1:'',
                  udfHs2:'',
                  udfHs3:'',
                  ifRandom:0,
                  randomNum:'',
        }
      }
    },
    created() {
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      this.getWareData();
      this.getAreaData();
      this.getShelf(1);
      this.getShelf(2);
      this.getCell(1);
      this.getCell(2);
        PF.getSuppliers((rows) => {
            this.suppliers = rows;
        });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .createBuyBillIn {
    padding-bottom: 100px;
  }
</style>
