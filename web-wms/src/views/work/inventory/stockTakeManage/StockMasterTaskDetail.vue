<template>
  <div class="stockMasterTaskDetail">
    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回</el-button>
      <!--      <el-button v-if="asnMaster.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao" @click="showUpdateModal"> 修改</el-button>-->
      <!--      <el-button v-if="asnMaster.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng" @click="isCommit()"> 提交</el-button>-->
      <!--      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="isDelete(asnMaster.asnMasterId)"> 删除</el-button>-->
      <!--      <el-button v-if="!isFile && asnMaster.insertType === 0" size="small" type="warning" round icon="iconfont icon-qc" @click="showQcModal"> 质检</el-button>-->
      <el-button v-if="(asnMaster.state === 0 || asnMaster.state === 2) && formReview.caseDetailId !== null" size="small" type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核</el-button>
    </div>

    <el-dialog title="审核" :visible.sync="dialogReviewVisible" :before-close="handleClose3" width="500px">
      <el-form :model="formReview" :rules="rulesReview" ref="formReview">
        <el-form-item label="审核原因" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="formReview.memo" placeholder="请输入审核原因"></el-input>
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
          <el-tab-pane label="盘点单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>盘点单号:</span>
                  <span>&nbsp;{{stockTakeMaster.billNo}}</span>
                </li>
                <li>
                  <span>单据类型:</span>
                  <span v-for="(item,index) in stockTaskMasterType" :key="index"
                        v-if="stockTakeMaster.type === item.value">{{item.label}}</span>
                </li>
                <li>
                  <span>盘点状态:</span>
                  <span v-for="(item,index) in stockTaskMasterState" :key="index"
                        v-if="stockTakeMaster.state === item.value">{{item.label}}</span>
                </li>
                <li>
                  <span>优先级:</span>
                  <span v-for="(item,index) in stockTaskMasterOrder" :key="index"
                        v-if="stockTakeMaster.priority === item.value">{{item.label}}</span>
                </li>
                <li>
                  <span>创建人:</span> 
                  <span>&nbsp;{{stockTakeMaster.createUserName}}</span>
                </li>
                <li>
                  <span>仓库名称:</span> 
                  <span>&nbsp;{{stockTakeMaster.wareName}}</span>
                </li>
                
                <li>
                  <span>货主:</span>
                  <span>&nbsp;{{stockTakeMaster.organizationName}}</span>
                </li>
                <li>
                  <span>货区:</span>
                  <span>&nbsp;{{stockTakeMaster.fromAreaCode}}</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>货架区:</span>
                  <span>&nbsp;{{stockTakeMaster.fromShelfCode}}</span>
                </li>
                <li>
                  <span>货位:</span>
                  <span>&nbsp;{{stockTakeMaster.fromCellCode}}</span>
                </li>
                <li>
                  <span>是否随机生成:</span>
                  <span v-if="stockTakeMaster.ifRandom === 0">否</span>
                  <span v-else-if="stockTakeMaster.ifRandom === 1">是</span>
                </li>
                <li v-if="stockTakeMaster.ifRandom === 1">
                  <span>随机数量:</span>
                  <span>&nbsp;{{stockTakeMaster.random}}</span>
                </li>
                <li>
                  <span>SKU:</span>
                  <span>&nbsp;{{stockTakeMaster.itemCode}}</span>
                </li>
                <li>
                  <span>SKU名称:</span>
                  <span>&nbsp;{{stockTakeMaster.itemName}}</span>
                </li>
                <li>
                  <span>盘点开始时间:</span>
                  <span>&nbsp;{{stockTakeMaster.dynamicFromTime}}</span>
                </li>
                <li>
                  <span>盘点结束时间:</span>
                  <span>&nbsp;{{stockTakeMaster.dynamicToTime}}</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>产品:</span>
                  <span>&nbsp;{{stockTakeMaster.itemKeywords}}</span>
                </li>
                <li>
                  <span>追踪号:</span>
                  <span>&nbsp;{{stockTakeMaster.boxCode}}</span>
                </li>
                <!-- <li>
                  <span>至货区:</span>
                  <span>&nbsp;{{stockTakeMaster.toAreaCode}}</span>
                </li>
                <li>
                  <span>至货架区:</span>
                  <span>&nbsp;{{stockTakeMaster.toShelfCode}}</span>
                </li>
                <li>
                  <span>至货位:</span>
                  <span>&nbsp;{{stockTakeMaster.toCellCode}}</span>
                </li> -->
                <li>
                  <span>udfHs1:</span>
                  <span>&nbsp;{{stockTakeMaster.udfHs1}}</span>
                </li>
                <li>
                  <span>udfHs2:</span>
                  <span>&nbsp;{{stockTakeMaster.udfHs2}}</span>
                </li>
                <li>
                  <span>udfHs3:</span>
                  <span>&nbsp;{{stockTakeMaster.udfHs3}}</span>
                </li>
              </ul>
            </div>
          </el-tab-pane>
          <el-tab-pane label="审核进程" name="second">
            <ReviewStepBox :masterData="stockTakeMaster.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程" name="third">
            <BillRecordBox :masterData="stockTakeMaster.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

        <!--<div class="billMasterBox clearFloat">
          <div class="billData leftFloat clearFloat">
            <div class="title">
              <span>盘点单信息</span>
            </div>
            <ul class="leftFloat">
              <li>
                <span>盘点单号:</span>
                <span>&nbsp;{{stockTakeMaster.billNo}}</span>
              </li>
              <li>
                <span>单据类型:</span>
                <span v-for="item in stockTaskMasterType"
                      v-if="stockTakeMaster.type === item.value">{{item.label}}</span>
              </li>
              <li>
                <span>盘点状态:</span>
                <span v-for="item in stockTaskMasterState"
                      v-if="stockTakeMaster.state === item.value">{{item.label}}</span>
              </li>
              <li>
                <span>优先级:</span>
                <span v-for="item in stockTaskMasterOrder"
                      v-if="stockTakeMaster.order === item.value">{{item.label}}</span>
              </li>
              <li>
                <span>创建人:</span>&nbsp;
                <span>&nbsp;{{stockTakeMaster.createUserName}}</span>
              </li>
              <li>
                <span>仓库名称:</span>&nbsp;
                <span>&nbsp;{{stockTakeMaster.wareName}}</span>
              </li>
              <li>
                <span>仓库编码:</span>&nbsp;
                <span>&nbsp;{{stockTakeMaster.wareCode}}</span>
              </li>
              <li>
                <span>货主:</span>
                <span>&nbsp;{{stockTakeMaster.organizationName}}</span>
              </li>
              <li>
                <span>从货区:</span>
                <span>&nbsp;{{stockTakeMaster.fromAreaCode}}</span>
              </li>
              <li>
                <span>从货架区:</span>
                <span>&nbsp;{{stockTakeMaster.fromShelfCode}}</span>
              </li>
              <li>
                <span>从货位:</span>
                <span>&nbsp;{{stockTakeMaster.fromCellCode}}</span>
              </li>
              <li>
                <span>是否随机生成:</span>
                <span v-if="stockTakeMaster.ifRandom === 0">否</span>
                <span v-else-if="stockTakeMaster.ifRandom === 1">是</span>
              </li>
              <li v-if="stockTakeMaster.ifRandom === 1">
                <span>随机数量:</span>
                <span>&nbsp;{{stockTakeMaster.random}}</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span>SKU:</span>
                <span>&nbsp;{{stockTakeMaster.itemCode}}</span>
              </li>
              <li>
                <span>SKU名称:</span>
                <span>&nbsp;{{stockTakeMaster.itemName}}</span>
              </li>
              <li>
                <span>盘点开始时间:</span>
                <span>&nbsp;{{stockTakeMaster.dynamicFromTime}}</span>
              </li>
              <li>
                <span>盘点结束时间:</span>
                <span>&nbsp;{{stockTakeMaster.dynamicToTime}}</span>
              </li>
              <li>
                <span>产品:</span>
                <span>&nbsp;{{stockTakeMaster.itemKeywords}}</span>
              </li>
              <li>
                <span>追踪号:</span>
                <span>&nbsp;{{stockTakeMaster.boxCode}}</span>
              </li>
              <li>
                <span>至货区:</span>
                <span>&nbsp;{{stockTakeMaster.toAreaCode}}</span>
              </li>
              <li>
                <span>至货架区:</span>
                <span>&nbsp;{{stockTakeMaster.toShelfCode}}</span>
              </li>
              <li>
                <span>至货位:</span>
                <span>&nbsp;{{stockTakeMaster.toCellCode}}</span>
              </li>
              <li>
                <span>udfHs1:</span>
                <span>&nbsp;{{stockTakeMaster.udfHs1}}</span>
              </li>
              <li>
                <span>udfHs2:</span>
                <span>&nbsp;{{stockTakeMaster.udfHs2}}</span>
              </li>
              <li>
                <span>udfHs3:</span>
                <span>&nbsp;{{stockTakeMaster.udfHs3}}</span>
              </li>
            </ul>
          </div>
          <ReviewStepBox :masterData="asnMaster.billNo"></ReviewStepBox>
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
        :data="stockTakeDetails"
        style="width: 100%">
        <!--        <el-table-column-->
        <!--          type="selection"-->
        <!--          width="55">-->
        <!--        </el-table-column>-->
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <!-- <el-table-column
          fixed="left"
          show-overflow-tooltip
          prop="billNo"
          label="盘点单号"
          width="180">
        </el-table-column> -->
        <el-table-column
          show-overflow-tooltip
          prop="type"
          label="类型"
          width="100">
          <template slot-scope="scope">
            <span v-for="(item,index) in stockTaskMasterType" :key="index" v-if="scope.row.type === item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="taskQuantity"
          label="状态"
          width="80">
          <template slot-scope="scope">
            <span :style="'color:'+item.color" v-for="(item,index) in stockTaskDetailState" :key="index" v-if="scope.row.state === item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="初盘"
          width="300">
           <el-table-column
          show-overflow-tooltip
          prop="differQuantity"
          label="盘盈(+)/盘亏(-)"
          width="80">
           <template slot-scope="scope" v-if="scope.row.differQuantity !== null">
            <span style="color:#409EFF" v-if="scope.row.differQuantity>=0"><span  >{{scope.row.differQuantity}}</span></span>
            <span style="color:rgba(255,3,7,0.87)" v-else-if="scope.row.differQuantity<0"><span  >{{scope.row.differQuantity}}</span></span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="实盘数量"
          width="100">
          <template scope="scope">
            <span style="font-weight:bold">{{scope.row.actualQuantity}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskQuantity"
                label="快照数量"
                width="120">
        </el-table-column>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="复盘"
          width="300">
           <el-table-column
          show-overflow-tooltip
          prop="differQuantity2"
          label="盘盈(+)/盘亏(-)"
          width="80">
           <template slot-scope="scope" v-if="scope.row.differQuantity2 !== null">
            <span style="color:#409EFF" v-if="scope.row.differQuantity2>=0"><span  >{{scope.row.differQuantity2}}</span></span>
            <span style="color:rgba(255,3,7,0.87)" v-else-if="scope.row.differQuantity2<0"><span  >{{scope.row.differQuantity2}}</span></span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity2"
          label="实盘数量"
          width="100">
          <template scope="scope">
            <span style="font-weight:bold">{{scope.row.actualQuantity2}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskQuantity2"
                label="快照数量"
                width="120">
        </el-table-column>
        </el-table-column>
       
        <el-table-column
          show-overflow-tooltip
          prop="quantity"
          label="当前库存"
          width="100">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="taskUserName"
          label="盘点人"
          width="100">
        </el-table-column>
         <el-table-column
          show-overflow-tooltip
          prop="cellCode"
          label="货位"
          width="120">
        </el-table-column>

        <el-table-column
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="120">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="120">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="createTime"
          label="任务创建时间"
          width="120">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="taskTime"
          label="任务时间"
          width="120">
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'


  export default {
    name: 'stockMasterTaskDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
        isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        activeName: 'first',

        searchData: {
          stockTakeMasterId: this.$route.params.stockTakeMasterId,
        },
        stockTakeMaster: {},
        stockTakeDetails: [],


        searchDataItem: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: null,
          organizationId: null,
        },

        asnMaster: {},
        asnDetails: [],
        form: {
          asnMaster: {},
          asnDetails: [],
        },
        asnUdfHs: [],
        asnUdfDs: [],
        asnTypes: [],
        ids: [],
        items: [],
        suppliers: [],
        organizations: [],
        platforms: [],
        carriers: [],
        multipleSelection: [],
        qcUdfHs: [],
        qcUdfDs: [],

        innerVisibleItem: false,
        dialogNewVisible: false,
        dialogQcVisible: false,
        dialogReviewVisible: false,

        rules: {
          supplierId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          buyType: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
        },

        formLabelWidth: '120px',

        formQc: {
          billSource: 0,
          insertType: -2,
          qcMaster: {},
          qcDetails: [],
        },
        rulesQc: {
          asnBillNo: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
          supplierId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          memo: [
            {required: true, message: '请输入备注', trigger: 'blur'}
          ],
        },
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
      ...mapState([
        "abcLevels",
        "stockTaskMasterType",
        "stockTaskMasterState",
        "stockTaskMasterOrder",
        "stockTaskMasterType",
        "stockTaskDetailState",
      ]),
      supplierId() {
        return this.form.asnMaster.supplierId
      },
      isFile() {
        if (this.$route.params.fileUserId) {
          return true
        } else {
          return false
        }
      }
    },
    watch: {
      supplierId(val) {
        for (let i = 0; i < this.suppliers.length; i++) {
          if (this.suppliers[i].supplierId === val) {
            this.form.asnMaster.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      handleClose3(done) {
        this.dialogReviewVisible = false;
      },
      getStockTakeMasterData() {
        IOT.getServerData("/stock/take/masters/list", 'get', {stockTakeMasterId: this.searchData.stockTakeMasterId}, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows[0];
            if (PF.isNull(list.dynamicFromTime)) {
              list.dynamicFromTime = list.dynamicFromTime.substr(5, 14);
            }
            if (PF.isNull(list.dynamicToTime)) {
              list.dynamicToTime = list.dynamicToTime.substr(5, 14);
            }
            this.stockTakeMaster = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      getStockTakeDetailData() {
        IOT.getServerData("/stock/take/tasks/list", 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.stockTakeDetails = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getItemData()
      },
      clearSearchSelectItem() {
        this.searchDataItem.keyWords = '';
        this.searchDataItem.wareId = null;
        this.searchDataItem.organizationId = null;
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
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
            IOT.getServerData('/asn/masters/review', 'get', formReview, (ret) => {
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
      this.getStockTakeMasterData();
      this.getStockTakeDetailData();

    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .asnBillDetail {
  }
</style>
