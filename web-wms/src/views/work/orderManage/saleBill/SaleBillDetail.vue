<template>
  <div class="saleBillDetail">

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回</el-button>
      <el-button v-if="saleMaster.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao" @click="showUpdateModal"> 修改</el-button>
      <el-button v-if="saleMaster.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng" @click="isCommit()"> 提交</el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="isDelete(saleMaster.saleMasterId)"> 删除</el-button>
      <el-button v-if="(saleMaster.state === 0 || saleMaster.state === 2) && formReview.caseDetailId !== null" size="small" type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核</el-button>
    </div>

    <el-dialog title="修改销售单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <!--master-->
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form.saleMaster" :rules="rules" ref="form.saleMaster">
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option
                      v-for="item in suppliers"
                      :key="item.supplierId"
                      :label="item.supplierName"
                      :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="销售单类型" prop="saleType" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.saleType" filterable placeholder="请选择销售单类型" style="width: 70%;">
              <el-option
                      v-for="item in saleTypes"
                      :key="item.dataDictDetailId"
                      :label="item.dictValue"
                      :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.saleMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in saleUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.saleMaster[item.paramCode]" :placeholder="'请输入'+item.value"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div class="headerBtns">
        <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectItemModal"> 选择物料
        </el-button>
      </div>
      <div class="tableBox">
        <el-table
                highlight-current-row
                :data="form.saleDetails"
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
                  width="180">
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="itemCode"
                  label="SKU"
                  width="180">
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="expectQuantity"
                  label="销售数量"
                  width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.expectQuantity" controls-position="right" :min="0" label="请输入数量"
                               size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="packDescribe"
                  label="单位"
                  width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" filterable
                         placeholder="请选择单位" size="small">
                <el-option
                        v-for="item in form.saleDetails[scope.$index].packDetails"
                        :key="item.packDetailId1"
                        :label="item.packDescribe1"
                        :value="item.packDetailId1">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  v-for="(item,index) in saleUdfDs"
                  :key="index"
                  :prop="item.paramCode"
                  :label="item.value"
                  width="180">
            <template slot-scope="scope">
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.label" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
          <el-table-column
                  fixed="right"
                  label="操作"
                  width="60">
            <template slot-scope="scope">
              <el-button @click="deleteDetail(scope.$index)" :disabled="form.saleDetails.length === 1"
                         icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog width="800px" title="选择物料" :visible.sync="innerVisibleItem" append-to-body style="margin-top: -10vh;">

        <div class="publicSearch body">
          <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
            <el-button-group>
              <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
              <el-button @click="clearSearchSelectItem" size="mini" icon="el-icon-refresh">重置</el-button>
            </el-button-group>
            <div>
              <span>关键字搜索</span>
              <el-input v-model="searchDataItem.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
            </div>
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
                    label="操作"
                    width="60">
              <template slot-scope="scope">
                <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                           size="small"></el-button>
              </template>
            </el-table-column>
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
                    label="物料种类"
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
                    prop="upperTacticName"
                    label="上架策略"
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
                    label="包装名称"
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
                    prop="abcLevel"
                    label="产品循环级别"
                    width="150">
              <template slot-scope="scope">
                <span v-for="(item,index) in abcLevels" :key="index" v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
              </template>
            </el-table-column>
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="weight"
                    label="重量"
                    width="180">
            </el-table-column>
          </el-table>
          <el-pagination
                  style="text-align: right;"
                  @current-change="handleCurrentChange2"
                  :current-page.sync="searchDataItem.pageNum"
                  :page-size="searchDataItem.pageSize"
                  layout="total, prev, pager, next, jumper"
                  :total="searchDataItem.total">
          </el-pagination>
        </div>

      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm">修 改</el-button>
      </div>
    </el-dialog>
    <el-dialog title="审核" :visible.sync="dialogReviewVisible" :before-close="handleClose3" width="500px">
      <el-form :model="formReview" :rules="rulesReview" ref="formReview">
        <el-form-item label="审核意见" prop="memo" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="formReview.memo" placeholder="请输入审核意见"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="leftFloat" @click="dialogReviewVisible = false">取 消</el-button>
        <el-button type="danger" @click="commitReview('formReview',-1)">退 回</el-button>
        <el-button type="success" @click="commitReview('formReview',1)">通 过</el-button>
      </div>
    </el-dialog>

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearchMaster?'auto':''}">

        <el-tabs v-model="activeName" @tab-click="handleClick" style="width: 100%;">
          <el-tab-pane label="销售单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
<!--                  <span :title="saleMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{saleMaster.mergeNo}}</span>-->
                  <span :title="saleMaster.billNo">&nbsp;{{saleMaster.billNo}}</span>
                </li>
                <li>
                  <span>单据来源:</span>
<!--                  <span v-if="billNoType === 'HB'">合并</span>-->
                  <span v-if="saleMaster.billSource === 1">新建</span>
                  <span v-else-if="saleMaster.billSource === 2">合并</span>
                </li>
                <li>
                  <span>销售单类型:</span>&nbsp;
                  <span :title="saleMaster.dictValue" v-for="(item,index) in saleTypes" :key="index"
                        v-if="saleMaster.saleType === item.dataDictDetailId">{{item.dictValue}}</span>
                </li>
                <li v-if="saleMaster.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="saleMaster.state === -2">草稿</span>
                  <span v-else-if="saleMaster.state === 2">待审核</span>
                  <span v-else-if="saleMaster.state === 1">审核通过</span>
                  <span v-else-if="saleMaster.state === 0">审核中</span>
                  <span v-else-if="saleMaster.state === -1">审核退回</span>
                </li>
                <li>
                  <span>保存类型:</span>&nbsp;
                  <span v-if="saleMaster.insertType === 0">单据</span>
                  <span v-else-if="saleMaster.insertType === -2">草稿</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="saleMaster.wareName" class="textBtn"
                        @click="$parent.showPublicModal(saleMaster.wareId, 'ware')">&nbsp;{{saleMaster.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="saleMaster.organizationName" class="textBtn"
                        @click="$parent.showPublicModal(saleMaster.organizationId, 'organization')">&nbsp;{{saleMaster.organizationName}}</span>
                </li>
                <li>
                  <span>客户:</span>
                  <span :title="saleMaster.customerName" class="textBtn"
                        @click="$parent.showPublicModal(saleMaster.customerId, 'customer')">&nbsp;{{saleMaster.customerName}}</span>
                </li>
                <li>
                  <span>创建人:</span>
                  <span :title="saleMaster.createUserName" class="textBtn"
                        @click="$parent.showPublicModal(saleMaster.createUserId, 'account')">&nbsp;{{saleMaster.createUserName}}</span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span :title="saleMaster.createTime">&nbsp;{{saleMaster.createTime}}</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>备注:</span>
                  <span :title="saleMaster.memo">&nbsp;{{saleMaster.memo}}</span>
                </li>
                <li v-for="(item,index) in saleUdfHs" :key="index">
                  <span>{{item.value}}:</span>
                  <span :title="saleMaster[item.paramCode]">&nbsp;{{saleMaster[item.paramCode]}}</span>
                </li>
              </ul>
            </div>
          </el-tab-pane>
          <el-tab-pane v-if="saleMaster.insertType === 0" label="审核进程" name="second">
            <ReviewStepBox :masterData="saleMaster.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程" name="third">
            <BillRecordBox :masterData="saleMaster.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
    </div>

    <div class="tableBox">
      <el-table
              v-loading="loading.detailTable"
              highlight-current-row
              :data="saleDetails"
              @selection-change="handleSelectionChange"
              style="width: 100%">
        <el-table-column
                v-if="saleMaster.insertType === 0"
                type="selection"
                width="55">
        </el-table-column>
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="detailNo"
            label="单号"
            width="180">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="itemCode"
            label="SKU"
            width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="状态"
            width="180">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state === 1" type="info">部分发货</el-tag>
            <el-tag v-else-if="scope.row.state === 2">全部发货</el-tag>
            <el-tag v-else-if="scope.row.state === 3">部分拣货</el-tag>
            <el-tag v-else-if="scope.row.state === 4">全部拣货</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="bomSplit"
            label="拆分BOM"
            width="100">
          <template slot-scope="scope">
            <span>{{scope.row.bomSplit === 1 ? '需要拆分' : '不拆分'}}</span>
          </template>
        </el-table-column>

        <el-table-column
            show-overflow-tooltip
            prop="expectQuantity"
            label="订货数量"
            width="180">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.expectQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="180">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            v-for="(item,index) in saleUdfDs"
            :key="index"
            :prop="item.paramCode"
            :label="item.value"
            width="180">
        </el-table-column>
      </el-table>
      <!--<el-pagination
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>-->
    </div>

  </div>
</template>

<script>
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'

  export default {
    name: 'SaleBillDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
        loading:{
          detailTable:true,

        },
        isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        activeName: 'first',

        searchData: {
          // pageNum: 1,
          // pageSize: 10,
          // total: null,
          billNo: this.$route.params.billNo,
        },
        searchDataItem: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: null,
          organizationId: null,
        },

        saleMaster: {},
        saleDetails: [],
        form: {
          saleMaster: {},
          saleDetails: [],
        },
        saleUdfHs: [],
        saleUdfDs: [],
        ids: [],
        items: [],
        saleTypes: [],
        suppliers: [],
        multipleSelection: [],
        platforms: [],
        carriers: [],

        innerVisibleItem: false,
        dialogNewVisible: false,
        dialogReviewVisible: false,

        rules: {
          supplierId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          saleType: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
          asnType: [
            {required: true, message: '请选择入库单类型', trigger: 'blur'}
          ],
          expectTime: [
            {required: true, message: '请选择预约到货时间', trigger: 'blur'}
          ],
        },

        formLabelWidth: '120px',

        formReview: {
          billNo: this.$route.params.billNo,
          reviewResult: null,
          caseDetailId: null,
          memo: '',
        },
        rulesReview: {
          memo: [
            {required: true, message: '请填写审核原因', trigger: 'blur'}
          ],
        },

      }
    },
    computed: {
      supplierId() {
        return this.form.saleMaster.supplierId
      },
    },
    watch: {
      supplierId(val) {
        for (let i = 0; i < this.suppliers.length; i++) {
          if (this.suppliers[i].supplierId === val) {
            this.form.saleMaster.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      /*handleCurrentChange(val) {
        this.pageNum = val;
        this.getSaleDetailData()
      },*/
      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getItemData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      clearSearchSelectItem() {
        this.searchDataItem.keyWords = '';
        this.searchDataItem.wareId = null;
        this.searchDataItem.organizationId = null;
      },
      getSaleMasterData() {

        IOT.getServerData('/sale/masters/list', 'get', {billNo: this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            this.saleMaster = ret.rows[0];

          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSaleDetailData() {
        var that = this;
        that.loading.detailTable = true;
        IOT.getServerData('/sale/details/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            /*for(let i=0;i<list.length;i++){
              list[i].expectQuantity = list[i].expectQuantity / list[i].transRatio;
              list[i].acceptQuantity = list[i].acceptQuantity / list[i].transRatio;
              list[i].asnQuantity = list[i].asnQuantity / list[i].transRatio;
            }*/
            this.saleDetails = list;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            var that = this;
            that.loading.detailTable = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getItemData() {
        IOT.getServerData('/item/infos/list', 'get', this.searchDataItem, (ret) => {
          if (ret.code === 200) {
            this.items = ret.rows;
            this.searchDataItem.total = ret.total;
            this.searchDataItem.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPlatformData() {
        IOT.getServerData('/platforms/list', 'get', {wareId: this.saleMaster.wareId}, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for (let i = 0; i < list.length; i++) {
              list[i].label = list[i].platformCode
            }
            this.platforms = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCarrierData() {
        IOT.getServerData('/carriers/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleClose(done) {
        this.dialogNewVisible = false;
      },
      handleClose3(done) {
        this.dialogReviewVisible = false;
      },
      isDelete(id) {
        this.$confirm('确认删除？')
                .then(_ => {
                  this.deleteSale(id)
                })
                .catch(_ => {
                });
      },
      deleteSale(id) {
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/sale/masters/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'saleBill'});
            IOT.tips('删除成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showUpdateModal() {
        this.form = {
          saleMaster: PF.JSON(this.saleMaster),
          saleDetails: PF.JSON(this.saleDetails),
          insertType: -2
        };
        PF.getSuppliers((rows) => {
          this.suppliers = rows;
        });
        this.dialogNewVisible = true;
      },
      showSelectItemModal() {
        this.ids = [];
        for (let i = 0; i < this.form.saleDetails.length; i++) {
          this.ids.push(this.form.saleDetails[i].itemCode)
        }
        this.searchDataItem.wareId = this.saleMaster.wareId;
        this.searchDataItem.organizationId = this.form.saleMaster.organizationId;
        this.getItemData();
        this.innerVisibleItem = true
      },
      selectItem(row) {
        if (this.ids.indexOf(row.itemCode) !== -1) {
          IOT.tips('相同物料不能重复添加！', 'error');
          this.innerVisibleItem = false;
          return false
        }
        this.ids.push(row.itemCode);
        this.form.saleDetails.push(row);
        this.form.saleDetails.expectQuantity = 0;
        this.innerVisibleItem = false;
      },
      deleteDetail(index) {
        this.form.saleDetails.splice(index, 1)
      },
      updateForm() {
        let that = this;
        let form = this.form;
        if (form.saleDetails.length <= 0) {
          IOT.tips('未选择物料,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        delete form.saleMaster.createTime;
        for (let i = 0; i < form.saleDetails.length; i++) {
          if (form.saleDetails[i].expectQuantity === 0 || !PF.isNull(form.saleDetails[i].expectQuantity)) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.saleDetails[i].expectQuantity = form.saleDetails[i].expectQuantity * form.saleDetails[i].transRatio;
        }
        IOT.getServerData('/sale/master/update', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getSaleMasterData();
              that.getSaleDetailData();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.dialogNewVisible = false
      },
      isCommit() {
        this.$confirm('确认提交？')
                .then(_ => {
                  this.commitForm()
                })
                .catch(_ => {
                });
      },
      commitForm() {
        IOT.showOverlay('保存中，请稍等...');
        let form = {
          saleMaster: this.saleMaster,
          saleDetails: this.saleDetails,
          insertType: 0
        };
        delete form.saleMaster.createTime;
        if (form.saleDetails.length <= 0) {
          IOT.tips('未选择物料,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.saleDetails.length; i++) {
          if (form.saleDetails[i].expectQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.saleDetails[i].expectQuantity = form.saleDetails[i].expectQuantity * form.saleDetails[i].transRatio;
        }
        IOT.getServerData('/sale/master/insert', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'saleBill'});
            IOT.tips('保存成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      changePackDetailId(index, type) {
        if (type) {

        } else {
          let detail = PF.JSON(this.form.saleDetails[index]);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.form.saleDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
              this.form.saleDetails[index].transRatio = detail.packDetails[i].transRatio1;
            }
          }
        }
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitReview(formName,reviewResult){
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let formReview = PF.JSON(this.formReview);
            formReview.reviewResult = reviewResult;
            IOT.getServerData('/sale/master/review', 'get', formReview, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('审核成功！', 'success');
                this.getCaseNowList();
                this.dialogReviewVisible = false;
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogReviewVisible = false;
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      getCaseNowList() {
        IOT.getServerData('/case/details/nowList', 'get', {billNo: this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            if(ret.rows.length > 0){
              this.formReview.caseDetailId = ret.rows[0].caseDetailId;
            }else{
              this.formReview.caseDetailId = null;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

    },
    created() {
      this.getCaseNowList();
      PF.getSysParamUdfData('saleUdfHs', [], (data, udfs) => {
        this.saleUdfHs = udfs;
      });
      PF.getSysParamUdfData('saleUdfDs', [], (data, udfs) => {
        this.saleUdfDs = udfs;
      });
      this.getSaleMasterData();
      this.getSaleDetailData();
      PF.getDataDictUdfData('saleType', (rows) => {
        this.saleTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .saleBillDetail {
  }
</style>
