<template>
  <div class="qcBillDetail">

    <div class="headerBtns">
      <el-button size="small" type="primary"    icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回</el-button>
      <el-button v-if="qcMaster.insertType === -2" size="small" type="success"    icon="iconfont icon-xinzeng"
                 @click="isCommit()"> 提交
      </el-button>
      <el-button size="small" type="danger"    icon="iconfont icon-shanchu" @click="isDelete(qcMaster.qcMasterId)">
        删除
      </el-button>
      <el-button v-if="qcMaster.insertType === 0" size="small" type="success"    icon="iconfont icon-qc"
                 @click="showQcModal"
                 :disabled="multipleSelection.length <= 0 || multipleSelection.length !== 1 || multipleSelection[0].checkQuantity > 0">
        质检
      </el-button>
      <el-button v-if="qcMaster.insertType === 0" size="small" type="primary"    icon="iconfont icon-tongbu"
                 @click="showSynchronizeModal"
                 :disabled="multipleSelection.length <= 0 || multipleSelection.length !== 1 || multipleSelection[0].checkQuantity <= 0">
        同步
      </el-button>
    </div>

    <el-dialog title="质检" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">

      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="formQc"
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
            prop="detailNo"
            label="单号"
            width="120">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="120">
            <template slot-scope="scope">
              <span class="textBtn"
                    @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
            </template>
          </el-table-column>
          <!--<el-table-column
            show-overflow-tooltip
            prop="checkQuantity"
            label="已检数量"
            width="150">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.checkQuantity" controls-position="right" :min="0" size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>-->
          <el-table-column
            show-overflow-tooltip
            prop="realQuantity"
            label="实检数量"
            width="150">
            <template slot-scope="scope">
              <input type="number" class="my_input small" v-model.number="scope.row.realQuantity" placeholder="实检数量"
                     style="width: 80%;">
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="qualifiedQuantity"
            label="合格数量"
            width="150">
            <template slot-scope="scope">
              <input type="number" class="my_input small" v-model.number="scope.row.qualifiedQuantity"
                     placeholder="合格数量" style="width: 80%;">
            </template>
          </el-table-column>
          <!--<el-table-column
            show-overflow-tooltip
            prop="fiedQuantity"
            label="不合格数量"
            width="150">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.fiedQuantity" controls-position="right" :min="0" size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>-->
          <el-table-column
            show-overflow-tooltip
            prop="expectQuantity"
            label="预约数量"
            width="120">
            <template slot-scope="scope">
              <span>{{ Math.floor(scope.row.expectQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="qcQuantity"
            label="质检数量"
            width="120">
            <template slot-scope="scope">
              <span>{{ Math.floor(scope.row.qcQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="120">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            v-for="(item,index) in qcUdfDs"
            :key="index"
            :prop="item.paramCode"
            :label="item.value"
            width="120">
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="commitFormQc">确 认</el-button>
      </div>
    </el-dialog>
    <el-dialog title="同步" :visible.sync="dialogSynchronizeVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -10vh;">

      <div class="headerBtns">
        <span>该条详情状态为&nbsp;</span>
        <el-tag v-for="(qcState,index) in qcStates" :key="index" v-if="synchronizeData.qcState === qcState.value" :type="qcState.type">
          {{qcState.label}}
        </el-tag>
      </div>

      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="synchronizes"
          max-height="360"
          style="width: 100%">
          <el-table-column
            fixed="left"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="150">
            <template slot-scope="scope">
              <span class="textBtn"
                    @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="quantity"
            label="数量"
            width="150">
            <template slot-scope="scope">
              <span>{{ scope.row.quantity / scope.row.transRatio }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="state"
            label="状态"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.state === 0">待质检</span>
              <span v-else-if="scope.row.state === 1">合格</span>
              <span v-else-if="scope.row.state === 2">不合格</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="boxCode"
            label="追踪号"
            width="150">
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose2">取 消</el-button>
        <el-button type="primary" @click="commitSynchronize">同 步</el-button>
      </div>
    </el-dialog>

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearchMaster?'auto':''}">


        <el-tabs v-model="activeName" @tab-click="handleClick" style="width: 100%;">
          <el-tab-pane label="质检单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                  <span>&nbsp;{{qcMaster.billNo}}</span>
                </li>
                <li>
                  <span>单据来源:</span>
                  <span v-if="qcMaster.billSource === 0">入库单</span>
                  <span v-else-if="qcMaster.billSource === 1">新建</span>
                </li>
                <li v-if="qcMaster.billSource === 0">
                  <span>入库单号:</span>
                  <span :title="qcMaster.requestBuyBillNo">&nbsp;
                  <span class="textBtn"
                        @click="$router.push({name: 'asnBillDetail',params: {billNo: qcMaster.asnBillNo}})">{{qcMaster.asnBillNo}}</span>
                </span>
                </li>
                <li v-if="qcMaster.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="qcMaster.state === -2">草稿</span>
                  <span v-else-if="qcMaster.state === 2">待审核</span>
                  <span v-else-if="qcMaster.state === 1">审核通过</span>
                  <span v-else-if="qcMaster.state === 0">审核中</span>
                  <span v-else-if="qcMaster.state === -1">审核退回</span>
                </li>
                <li>
                  <span>保存类型:</span>&nbsp;
                  <span v-if="qcMaster.insertType === 0">单据</span>
                  <span v-else-if="qcMaster.insertType === -2">草稿</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="qcMaster.wareName">&nbsp;
                  <span class="textBtn"
                        @click="$parent.showPublicModal(qcMaster.wareId, 'ware')">{{qcMaster.wareName}}</span>
                </span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="qcMaster.organizationName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.organizationId, 'organization')">{{qcMaster.organizationName}}</span>
                </span>
                </li>
                <li>
                  <span>供应商:</span>
                  <span :title="qcMaster.supplierName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.supplierId, 'supplier')">{{qcMaster.supplierName}}</span>
                </span>
                </li>
                <li>
                  <span>创建人:</span>
                  <span :title="qcMaster.createUserName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.userId, 'account')">{{qcMaster.createUserName}}</span>
                </span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span>&nbsp;{{qcMaster.createTime}}</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>备注:</span>
                  <span>&nbsp;{{qcMaster.memo}}</span>
                </li>
                <li v-for="(item,index) in qcUdfHs" :key="index">
                  <span>{{item.value}}:</span>
                  <span :title="qcMaster[item.paramCode]">&nbsp;{{qcMaster[item.paramCode]}}</span>
                </li>
              </ul>
            </div>
          </el-tab-pane>
          <el-tab-pane v-if="qcMaster.insertType === 0" label="审核进程" name="second">
            <ReviewStepBox :masterData="qcMaster.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程" name="third">
            <BillRecordBox :masterData="qcMaster.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

        <!--<div class="billMasterBox clearFloat">
          <div class="billData leftFloat clearFloat">
            <div class="title">
              <span>QC单据信息</span>
            </div>
            <ul class="leftFloat">
              <li>
                <span>单号:</span>
                <span>&nbsp;{{qcMaster.billNo}}</span>
              </li>
              <li>
                <span>单据来源:</span>
                <span v-if="qcMaster.billSource === 0">入库单</span>
                <span v-else-if="qcMaster.billSource === 1">新建</span>
              </li>
              <li v-if="qcMaster.billSource === 0">
                <span>入库单号:</span>
                <span :title="qcMaster.requestBuyBillNo">&nbsp;
                  <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: {billNo: qcMaster.asnBillNo}})">{{qcMaster.asnBillNo}}</span>
                </span>
              </li>
              <li v-if="qcMaster.insertType === 0">
                <span>审核状态:</span>&nbsp;
                <span v-if="qcMaster.state === -2">草稿</span>
                <span v-else-if="qcMaster.state === 2">待审核</span>
                <span v-else-if="qcMaster.state === 1">审核通过</span>
                <span v-else-if="qcMaster.state === 0">审核中</span>
                <span v-else-if="qcMaster.state === -1">审核退回</span>
              </li>
              <li>
                <span>备注:</span>
                <span>&nbsp;{{qcMaster.memo}}</span>
              </li>
              <li v-for="item in qcUdfHs">
                <span>{{item.value}}:</span>
                <span :title="qcMaster[item.paramCode]">&nbsp;{{qcMaster[item.paramCode]}}</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span>保存类型:</span>&nbsp;
                <span v-if="qcMaster.insertType === 0">单据</span>
                <span v-else-if="qcMaster.insertType === -2">草稿</span>
              </li>
              <li>
                <span>仓库:</span>
                <span :title="qcMaster.wareName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.wareId, 'ware')">{{qcMaster.wareName}}</span>
                </span>
              </li>
              <li>
                <span>货主:</span>
                <span :title="qcMaster.organizationName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.organizationId, 'organization')">{{qcMaster.organizationName}}</span>
                </span>
              </li>
              <li>
                <span>供应商:</span>
                <span :title="qcMaster.supplierName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.supplierId, 'supplier')">{{qcMaster.supplierName}}</span>
                </span>
              </li>
              <li>
                <span>创建人:</span>
                <span :title="qcMaster.createUserName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(qcMaster.userId, 'account')">{{qcMaster.createUserName}}</span>
                </span>
              </li>
              <li>
                <span>创建时间:</span>
                <span>&nbsp;{{qcMaster.createTime}}</span>
              </li>
            </ul>
          </div>
          <ReviewStepBox :masterData="qcMaster.billNo"></ReviewStepBox>
        </div>-->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        @selection-change="handleSelectionChange"
        :data="qcDetails"
        style="width: 100%">
        <el-table-column
          v-if="qcMaster.insertType === 0"
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
          width="150">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="expectQuantity"
          label="预约数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.expectQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="qcQuantity"
          label="质检数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.qcQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="120">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="checkQuantity"
          label="已检数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.checkQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="realQuantity"
          label="实检数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.realQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="qualifiedQuantity"
          label="合格数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.qualifiedQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="fiedQuantity"
          label="不合格数量"
          width="120">
          <template slot-scope="scope">
            <span>{{ Math.floor(scope.row.fiedQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="one"
          label="合格率"
          width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.one">{{scope.row.one}}%</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="two"
          label="不合格率"
          width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.two">{{scope.row.two}}%</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="minPassRate"
          label="最小合格率"
          width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.minPassRate">{{scope.row.minPassRate}}%</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          v-for="(item,index) in qcUdfDs"
          :key="index"
          :prop="item.paramCode"
          :label="item.value"
          width="150">
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'

  export default {
    name: 'QcBillDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
        isMoreSearch: false,
        isMoreSearchMaster: true,
        activeName: 'first',

        searchData: {
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

        qcMaster: {},
        qcDetails: [],
        qcUdfHs: [],
        qcUdfDs: [],
        multipleSelection: [],
        formQc: [],
        synchronizeData: {},
        synchronizes: [],

        innerVisibleItem: false,
        dialogNewVisible: false,
        dialogQcVisible: false,
        dialogSynchronizeVisible: false,

        formLabelWidth: '120px',

      }
    },
    computed: {
      ...mapState([
        "qcStates",
      ])
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getItemData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getQcMasterData() {
        IOT.getServerData('/qc/masters/list', 'get', {billNo: this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            this.qcMaster = ret.rows[0];
            PF.getSysParamUdfData('qcUdfHs', [], (data, udfs) => {
              this.qcUdfHs = udfs;
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getQcDetailData() {
        IOT.getServerData('/qc/details/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.qcDetails = ret.rows;
            PF.getSysParamUdfData('qcUdfDs', [], (data, udfs) => {
              this.qcUdfDs = udfs;
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleClose(done) {
        this.formQc = [];
        this.dialogNewVisible = false;
      },
      handleClose2(done) {
        this.dialogSynchronizeVisible = false;
      },
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteQcBill(id)
          })
          .catch(_ => {
          });
      },
      deleteQcBill(id) {
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/qc/masters/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.go(-1);
            IOT.tips('删除成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
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
          billSource: this.qcMaster.billSource,
          insertType: 0,
          qcMaster: PF.JSON(this.qcMaster),
          qcDetails: PF.JSON(this.qcDetails)
        };
        if (form.qcDetails.length <= 0) {
          IOT.tips('未选择物料,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.qcDetails.length; i++) {
          if (form.qcDetails[i].expectQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
        }
        form.qcDetailList = PF.JSON(form.qcDetails);
        delete form.qcDetails;
        IOT.getServerData('/qc/masters/insert', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'qcBill'});
            IOT.tips('保存成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showQcModal() {
        let formQc = PF.JSON(this.multipleSelection);
        for (let i = 0; i < formQc.length; i++) {
          formQc[i].wareId = PF.getLocal('wareId', 'number');
          formQc[i].wareName = PF.getLocal('wareName');
          formQc[i].billType = this.qcMaster.billSource;
          formQc[i].qcBillNo = this.qcMaster.billNo;
          formQc[i].qcDetailNo = formQc[i].detailNo;
          formQc[i].realQuantity = formQc[i].expectQuantity;
          formQc[i].qualifiedQuantity = null;
          // formQc[i].fiedQuantity = 0;
        }
        this.formQc = formQc;
        this.dialogNewVisible = true;
      },
      commitFormQc() {
        IOT.showOverlay('保存中，请稍等...');
        let form = this.formQc;
        for (let i = 0; i < form.length; i++) {
          if (!PF.isNull(form[i].realQuantity) && form[i].realQuantity <= 0) {
            IOT.tips('实检数量需大于零！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          if (!PF.isNull(form[i].qualifiedQuantity) && form[i].qualifiedQuantity <= 0) {
            IOT.tips('合格数量需大于零！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          if (form[i].realQuantity > form[i].expectQuantity || form[i].qualifiedQuantity > form[i].expectQuantity) {
            IOT.tips('输入数量不得大于预约数量！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form[i].realQuantity = form[i].realQuantity * form[i].transRatio;
          form[i].qualifiedQuantity = form[i].qualifiedQuantity * form[i].transRatio;
        }
        IOT.getServerData('/inventorys/qcList', 'post', {qcData: form}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'qcBill'});
            IOT.tips('保存成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.handleClose();
      },
      showSynchronizeModal() {
        let detailNo = this.multipleSelection[0].detailNo;
        IOT.getServerData('/inventorys/getInventory', 'get', {detailNo: detailNo}, (ret) => {
          if (ret.code === 200) {
            this.synchronizes = ret.rows;

            let obj = PF.JSON(this.multipleSelection[0]);
            if (!PF.isNull(obj.one) || obj.one === 0) {
              obj.qcState = 0
            } else if (obj.one >= obj.minPassRate) {
              obj.qcState = 1
            } else if (obj.one < obj.minPassRate) {
              obj.qcState = 2
            }
            this.synchronizeData = obj;
            this.dialogSynchronizeVisible = true;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitSynchronize() {
        IOT.showOverlay('保存中，请稍等...');
        let arr = PF.JSON(this.synchronizes);
        let inventoryIds = [];
        for (let i = 0; i < arr.length; i++) {
          inventoryIds.push(arr[i].inventoryId);
          // inventoryIds[i] = arr[i].inventoryId;
        }
        let qcState = this.synchronizeData.qcState;
        
        IOT.getServerData('/inventorys/synchronization', 'post', {
          inventoryIds: inventoryIds,
          qcState: qcState
        }, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success');
            this.dialogSynchronizeVisible = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }

    },
    created() {
      this.getQcMasterData();
      this.getQcDetailData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .qcBillDetail {
  }
</style>
