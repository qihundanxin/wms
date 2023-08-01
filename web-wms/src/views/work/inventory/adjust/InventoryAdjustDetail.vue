<template>
  <div class="freezeDetail">

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.push({name: 'inventoryAdjust'})">返回
      </el-button>
      <el-button v-if="inventoryAdjust.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao"
                 @click="showUpdateModal">修改
      </el-button>
      <el-button v-if="inventoryAdjust.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng"
                 @click="isCommit()">提交
      </el-button>
      <el-button v-if="inventoryAdjust.caseState !== 1" size="small" type="danger" round icon="iconfont icon-shanchu"
                 @click="isDelete(inventoryAdjust.inventoryAdjustId)">删除
      </el-button>
      <!-- <el-button v-if="inventoryAdjust.caseState === 1" size="small" type="success" round icon="iconfont icon-tijiao"
                 @click="releaseAll(inventoryAdjust.billNo)">调整库存
      </el-button> -->
      <el-button v-if="(inventoryAdjust.caseState === 0 || inventoryAdjust.caseState === 2) && formReview.caseDetailId !== null" size="small" type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核</el-button>
    </div>

    <el-dialog title="修改库存调整单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <div class="headerBtns">
        <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectInventoryModal">
          选择库存
        </el-button>
      </div>
      <div class="tableBox">
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
            sortable
            prop="itemName"
            label="SKU名称"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="调整数量"
            width="180">
            <template slot-scope="scope">
              <el-input-number v-model="form.adjustDetails[scope.$index].adjustQuantity" controls-position="right"
                               style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="quantity"
            label="库存数量"
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
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="wareName"
            label="仓库"
            width="180">
          </el-table-column>
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
              <el-button @click="deleteDetail(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain
                         size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog width="800px" title="选择库存" :visible.sync="innerVisibleInventory" append-to-body
                 style="margin-top: -10vh;">

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
              <span>仓库</span>
              <el-select v-model="searchSelectInventory.wareId" size="mini" filterable placeholder="请选择仓库" clearable>
                <el-option
                  v-for="item in wares"
                  :key="item.wareId"
                  :label="item.wareName"
                  :value="item.wareId">
                </el-option>
              </el-select>
            </div>
            <div>
              <span>货主</span>
              <el-select v-model="searchSelectInventory.organizationId" size="mini" filterable placeholder="请选择货主"
                         clearable>
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
                :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"
                @click="isMoreSearch2 = !isMoreSearch2"></span>
        </div>

        <div class="tableBox">
          <el-table
            max-height="460"
            :data="inventorys"
            @selection-change="handleSelectionChange"
            style="width: 100%;">
            <el-table-column
              fixed="left"
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              fixed="left"
              sortable
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
              prop="organizationName"
              label="货主"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="batchName"
              label="批次"
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
              prop="quantity"
              label="数量"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="allotQuantity"
              label="分配数量"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="freezeQuantity"
              label="冻结数量"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="wareName"
              label="仓库"
              width="180">
            </el-table-column>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm">修 改</el-button>
      </div>
    </el-dialog>
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
          <el-tab-pane label="库存调整单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                  <span>&nbsp;{{inventoryAdjust.billNo}}</span>
                </li>
                <li v-if="inventoryAdjust.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="inventoryAdjust.caseState === -1">不通过</span>
                  <span v-else-if="inventoryAdjust.caseState === 0">流转中</span>
                  <span v-else-if="inventoryAdjust.caseState === 1">通过</span>
                  <span v-else-if="inventoryAdjust.caseState === 2">初始化</span>
                </li>
                <li>
                  <span>保存类型:</span>&nbsp;
                  <span v-if="inventoryAdjust.insertType === 0">单据</span>
                  <span v-else-if="inventoryAdjust.insertType === -2">草稿</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>调整原因:</span>
                  <span>&nbsp;{{inventoryAdjust.reasonDescription}}</span>
                </li>
                <li>
                  <span>仓库:</span>
                  <span>&nbsp;{{inventoryAdjust.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span>&nbsp;{{inventoryAdjust.organizationName}}</span>
                </li>
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>备注:</span>
                  <span>&nbsp;{{inventoryAdjust.memo}}</span>
                </li>
                <li>
                  <span>创建人:</span>
                  <span>&nbsp;{{inventoryAdjust.userName}}</span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span>&nbsp;{{inventoryAdjust.createTime}}</span>
                </li>
                
              </ul>
            </div>
            <el-button v-if="isBatch" type="success" size="small" round plain icon="iconfont icon-tijiao"
                      @click="adjustAffirm(2,inventoryAdjust.billNo)" title="" style="float: right;position:relative;top:-35px;left:-10px;">批量库存调整</el-button>
          </el-tab-pane>
          <el-tab-pane v-if="inventoryAdjust.insertType === 0" label="审核进程" name="reviewStep">
            <ReviewStepBox :masterData="inventoryAdjust.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程"    name="billRecord">
            <BillRecordBox :masterData="inventoryAdjust.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="adjustDetails"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          fixed="left"
          sortable
          prop="detailNo"
          label="单号"
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
          prop="itemCode"
          label="SKU"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          v-if="inventoryAdjust.insertType === 0"
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-tag v-show="scope.row.state === 1" type="danger">未调整</el-tag>
            <el-tag v-show="scope.row.state === 2" type="success">已调整</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="batchName"
          label="批次"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="differQuantity"
          label="差异数量"
          width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="adjustQuantity"
                label="调整数量"
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
          prop="boxCode"
          label="追踪号"
          width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="lpn"
                label="追踪号"
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
          prop="memo"
          label="备注">
        </el-table-column>
        <el-table-column
          v-if="inventoryAdjust.insertType === 0"
          fixed="right"
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 1" :disabled="inventoryAdjust.caseState !== 1" size="small" type="success" circle plain icon="iconfont icon-tijiao"
                       @click="adjustAffirm(1,scope.row.adjustDetailId)" title="">确认调整</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
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
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'


  export default {
    name: 'FreezeDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
        isBatch:false,
        isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        activeName: 'first',

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          billNo: this.$route.params.billNo,
        },
        searchSelectInventory: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: null,
          organizationId: null,
        },

        inventoryAdjust: {},
        adjustDetails: [],
        form: {
          inventoryAdjust: {},
          adjustDetails: [],
        },

        wares: [],
        organizations: [],
        inventorys: [],
        multipleSelection: [],
        selectIds: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        innerVisibleInventory: false,
        dialogReviewVisible: false,

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
      isFreezeBtn() {
        if (this.multipleSelection.length <= 0) {
          return true
        }
        let organizationId = this.multipleSelection[0].organizationId;
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (organizationId !== this.multipleSelection[i].organizationId) {
            return true
          }
        }
        return false
      }
    },
    methods: {

      handleClick(tab, event) {
        console.log(tab, event);
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryAdjustDetailData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryAdjustDetailData()
      },
      handleCurrentChange2(val) {
        this.searchSelectInventory.pageNum = val;
        this.getInventoryData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      clearSearchSelectInventory() {
        this.searchSelectInventory.keyWords = '';
        this.searchSelectInventory.wareId = null;
        this.searchSelectInventory.organizationId = null;
      },
      getInventoryAdjustData() {
        IOT.getServerData('/inventory/adjusts/list', 'get', {billNo: this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows[0];
            list.createTime = list.createTime.substr(0, 19);
            this.inventoryAdjust = list;
            this.getInventoryAdjustDetailData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getInventoryAdjustDetailData() {
        this.adjustDetails = [];
        IOT.getServerData('/inventory/adjust/details/findList', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.adjustDetails = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
            if(this.inventoryAdjust.allowBatch==1){
              for(var i=0;i<this.adjustDetails.length;i++){
                 if(this.adjustDetails[i].state==1){
                  this.isBatch =true;
                  break;
                 }else{
                  this.isBatch = false;
                 }
              }
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getWareData() {
        IOT.getServerData('/ware/infos/findSome', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getInventoryData() {
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
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteInventoryAdjust(id)
          })
          .catch(_ => {
          });
      },
      deleteInventoryAdjust(id) {
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/inventory/adjusts/deleteDetailAndMaster', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'inventoryAdjustDraft'});
            IOT.tips('删除成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      adjustAffirm(value,item) {
        let data;
        if(value==1){
          data = {
            adjustDetailId: item
          }
        }else{
          data = {
            billNo: item
          }
        }
        this.$confirm('确认调整？')
                .then(_ => {
                  IOT.showOverlay('保存中，请稍等...');
                  IOT.getServerData('/inventory/adjust/details/adjustAffirm', 'post', data, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                      IOT.tips('调整成功！', 'success');
                      this.getInventoryAdjustData();
                    } else {
                      IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                  });
                })
                .catch(_ => {
                });

      },
      releaseAll(billNo) {
        this.$confirm('确认全部调整调整？')
                .then(_ => {
                  IOT.showOverlay('保存中，请稍等...');
                  IOT.getServerData('/inventory/adjust/details/adjustAffirm', 'post', {billNo: billNo}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                      IOT.tips('保存成功！', 'success');
                      this.getInventoryAdjustData();
                    } else {
                      IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                  });
                })
                .catch(_ => {
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
          inventoryAdjust: this.inventoryAdjust,
          adjustDetails: this.adjustDetails,
          insertType: 0
        };
        if (form.adjustDetails.length <= 0) {
          IOT.tips('未选择库存,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.adjustDetails.length; i++) {
          if (form.adjustDetails[i].adjustQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
        }
        IOT.getServerData('/inventory/adjusts/commitDraft', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'inventoryAdjustDraft'});
            IOT.tips('保存成功！', 'success');
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
      showSelectInventoryModal() {
        this.selectIds = [];
        for (let i = 0; i < this.form.adjustDetails.length; i++) {
          this.selectIds.push(this.form.adjustDetails[i].inventoryId)
        }
        this.getInventoryData();
        this.innerVisibleInventory = true
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clickSelectInventory() {
        let list = PF.JSON(this.multipleSelection);
        this.form.inventoryAdjust.wareId = list[0].wareId;
        this.form.inventoryAdjust.wareName = list[0].wareName;
        this.form.inventoryAdjust.organizationId = list[0].organizationId;
        this.form.inventoryAdjust.organizationName = list[0].organizationName;

        let isRepeat = false;
        for (let i = 0; i < list.length; i++) {
          list[i].nowFreezeQuantity = list[i].freezeQuantity;
          list[i].freezeQuantity = 0;
          if (this.selectIds.indexOf(list[i].inventoryId) === -1 && list[i].organizationId === this.inventoryAdjust.organizationId) {
            this.form.adjustDetails.push(list[i]);
          } else {
            isRepeat = true
          }
        }
        if (isRepeat) {
          IOT.tips('不能重复添加或添加非此货主！', 'error');
        }
        this.innerVisibleInventory = false;
      },
      deleteDetail(index) {
        this.form.adjustDetails.splice(index, 1)
      },
      showUpdateModal() {
        this.form = {
          inventoryAdjust: PF.JSON(this.inventoryAdjust),
          adjustDetails: PF.JSON(this.adjustDetails),
          insertType: -2
        };
        this.dialogNewVisible = true;
      },
      updateForm() {
        let that = this;
        let form = this.form;
        if (form.adjustDetails.length <= 0) {
          IOT.tips('未选择库存,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.adjustDetails.length; i++) {
          if (form.adjustDetails[i].adjustQuantity === 0 || form.adjustDetails[i].adjustQuantity === null
                  || form.adjustDetails[i].adjustQuantity === '' || form.adjustDetails[i].adjustQuantity === undefined) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
        }
        IOT.getServerData('/inventory/adjusts/updateInventoryAdjust', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getInventoryAdjustDetailData();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.dialogNewVisible = false
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
            IOT.getServerData('/freeze/masters/review', 'get', formReview, (ret) => {
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
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    mounted() {
      this.getInventoryAdjustData();
    }
  }
</script>

<style scoped lang="less">
  .freezeDetail {
  }
</style>
