<template>
  <div class="inventoryBalance">

    <div class="publicSearch">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>SKU名称</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>货位</span>
          <el-input v-model="searchData.cellCode" size="mini" placeholder="货位" clearable></el-input>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>批次状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择批次状态" clearable>
            <el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="批次/追踪号/LPN号" clearable></el-input>
        </div>
        <div>
          <span>移入货位时间</span>
          <el-date-picker v-model="createTimeArr" size="mini" type="daterange" value-format="yyyy-MM-dd"
            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>货位使用类型</span>
          <el-select class="cellUseType" v-model="searchData.cellUseType" size="mini" filterable placeholder="请选择货位使用类型" clearable>
            <el-option
                v-for="item in cellUseTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="danger" round icon="iconfont icon-user-jinyong" @click="showFreezeModal"
        :disabled="isFreezeBtn">冻结库存
      </el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="recovery"
        :disabled="isRecoveryBtn">放入回收站
      </el-button>
      <el-button size="small" type="success" round :icon="'iconfont '+(exportListLoading?'icon-zhongzhi':'icon-xiazai')"
        @click="exportList" :disabled="exportListLoading">{{exportListLoading?'导出中，请耐心等待':'导出'}}
      </el-button>
    </div>

    <el-dialog title="新增冻结单" :visible.sync="dialogFreezeVisible" :before-close="handleClose" width="900px"
      style="margin-top: -10vh;">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form.freezeMaster" :rules="rules" ref="form.freezeMaster">
          <el-form-item label="冻结原因" prop="freezeDescribe" :label-width="formLabelWidth">
            <el-select v-model="form.freezeMaster.freezeDescribe" filterable placeholder="请选择冻结原因">
              <el-option v-for="item in freezeReasons" :key="item.dictKey" :label="item.dictValue"
                :value="item.dictKey">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.freezeMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
        </el-form>
        <div class="tableBox noHeight">
          <el-table highlight-current-row :data="form.freezeDetails" max-height="360" style="width: 100%">
            <el-table-column fixed="left" type="index" width="50">
            </el-table-column>
            <el-table-column fixed="left" sortable show-overflow-tooltip prop="itemName" label="SKU名称">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="quantity" label="数量">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.quantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="nowFreezeQuantity" label="已冻结数量">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.nowFreezeQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip label="冻结数量">
              <template slot-scope="scope">
                <input type="number" class="my_input small"
                  v-model.number="form.freezeDetails[scope.$index].freezeQuantity"
                  :max="scope.row.quantity - scope.row.nowFreezeQuantity" placeholder="冻结数量" style="width: 80%;">
                <!--                <el-input-number v-model="form.freezeDetails[scope.$index].freezeQuantity" controls-position="right" :min="0" :max="scope.row.quantity - scope.row.nowFreezeQuantity" style="width: 90%;"></el-input-number>-->
              </template>
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="packDescribe" label="单位">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="batchName" label="批次">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="allotQuantity" label="分配数量">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.allotQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="wareName" label="仓库">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="cellCode" label="货位">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="boxCode" label="追踪号">
            </el-table-column>
            <el-table-column sortable show-overflow-tooltip prop="lpn" label="LPN码">
            </el-table-column>
            <el-table-column fixed="right" label="操作">
              <template slot-scope="scope">
                <el-button @click="deleteDetail(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain
                  size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button class="leftFloat" type="primary" plain @click="commitForm('form.freezeMaster',-2)">保存为草稿</el-button> -->
        <el-button @click="resetForm('form.freezeMaster'); dialogFreezeVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.freezeMaster', 0)">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox rowHeight">
      <el-table v-loading="tableLoading" highlight-current-row :data="inventoryBalances"
        @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column fixed="left" type="selection" width="55">
        </el-table-column>
        <el-table-column fixed="left" prop="inventoryId" label="库存ID" width="65" show-overflow-tooltip>
        </el-table-column>
        <el-table-column fixed="left" prop="itemName" label="SKU名称" show-overflow-tooltip>
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
              scope.row.itemName
            }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemCode" label="SKU">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
              scope.row.itemCode
            }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column show-overflow-tooltip prop="upc" label="UPC">
        </el-table-column>-->
        <el-table-column how-overflow-tooltip label="UPC" prop="upcs">
          <template slot-scope="scope">
            <span  :key="upc.upc" v-for="upc in scope.row.upcs">
              {{ upc.upc }}
            </span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="cellCode" label="货位编码">
        </el-table-column>
        <el-table-column
              show-overflow-tooltip
              prop="cellUseType"
              label="货位类型"
              min-width="80">
              <template slot-scope="scope">
                <span v-for="(item,index) in cellUseTypes" :key="index"
                      v-if="item.value === scope.row.cellUseType">{{ item.label }}</span>
              </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="quantity" label="数量">
          <template slot-scope="scope">
            <span style="color:black;font-weight:900;">{{
              Math.floor(scope.row.quantity / scope.row.transRatio * 10000) / 10000
            }} </span>
            <span style="font-size:20% ">（{{ scope.row.packDescribe }}）</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="allotQuantity" label="分配数量">
          <template slot-scope="scope">
            <span style="color:black;font-weight:900;">{{
              Math.floor(scope.row.allotQuantity / scope.row.transRatio * 10000) / 10000
            }} </span>
            <span style="font-size:20% ">（{{ scope.row.packDescribe }}）</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="freezeQuantity" label="冻结数量">
          <template slot-scope="scope">
            <span style="color:black;font-weight:900;">{{
              Math.floor(scope.row.freezeQuantity / scope.row.transRatio * 10000) / 10000
            }} </span>
            <span style="font-size:20% ">（{{ scope.row.packDescribe }}）</span>
          </template>
        </el-table-column>   
        <el-table-column show-overflow-tooltip prop="batchName" label="批次">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.batchId, 'batch')">{{
              scope.row.batchName
            }}</span>
          </template>
        </el-table-column>     
        <el-table-column prop="state" label="批次状态" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.state == -2">已放入回收站</span>
            <span v-else>正常</span>
          </template>
        </el-table-column>
        <!--  <el-table-column

            show-overflow-tooltip
            prop="wareName"
            label="仓库"
            width="130">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
            </template>
          </el-table-column> -->

        <el-table-column show-overflow-tooltip prop="organizationName" label="货主">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{
              scope.row.organizationName
            }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="boxCode" label="追踪号">
        </el-table-column>
      </el-table>
      <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="handleCurrentChange"
        @size-change="handleSizeChange" :page-sizes="[15, 30, 50, 100]" :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize" layout="sizes, total, prev, pager, next, jumper" :total="searchData.total">
      </el-pagination>
    </div>

    <el-dialog title="操作放入回收站" :visible.sync="recovoryVisible" :before-close="recoveryClose">
      <h3 style="text-align:center;">当前选择了{{ batchNum }}个库存批次，{{ skuNum }}个SKU，{{ inventoryNum }}件货物</h3>
      <h3 style="text-align:center;margin-bottom:10px">是否将全部批次库存放入回收站？</h3>
      <el-form :model="recoveryForm" :rules="rules" ref="recoveryForm" class="recovery">
        <el-form-item label="填写原因" prop="recoveryReason" style="margin-left:10%">
          <el-input type="textarea" rows="5" v-model="recoveryForm.recoveryReason" style="width:70%"
            placeholder="请填写放入回收站的原因（至少10个字，不超过100个字）"></el-input>
        </el-form-item>
        <el-form-item style="text-align:center;">
          <el-button type="primary" @click="submitRecovery('recoveryForm')">确认</el-button>
          <el-button @click="resetForm('recoveryForm'); recovoryVisible = false">取消</el-button>
        </el-form-item>

        <h3 style="color:	#8B0000;text-align:center;">温馨提示：此操作请慎重，请操作后无法退回</h3>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {mapState} from "vuex";
export default {
  name: 'InventoryBalance',
  data() {
    return {
      isMoreSearch: false,
      tableLoading: false,
      exportListLoading: false,
      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        organizationId: null,
        itemName: '',
        itemCode: '',
        cellCode: '',
        startModifyTime: '',
        endModifyTime: '',
        state: 0

      },
      states: [
        {
          value: 0,
          label: '正常'
        },
        {
          value: -2,
          label: '已放入回收站'
        },
      ],
      createTimeArr: [],

      inventoryBalances: [],
      organizations: [],
      multipleSelection: [],
      freezeReasons: [],

      dialogNewVisible: false,
      dialogUpdateVisible: false,
      dialogFreezeVisible: false,

      recovoryVisible: false,
      batchNum: 0,
      skuNum: 0,
      inventoryNum: 0,
      recoveryForm: {
        inventoryIds: [],
        recoveryReason: ''
      },

      formLabelWidth: '120px',

      form: {
        freezeMaster: {
          freezeDescribe: '',
          memo: '',
          wareId: null,
          wareName: '',
          organizationId: null,
          organizationName: '',
        },
        freezeDetails: []
      },
      rules: {
        freezeDescribe: [
          { required: true, message: '请输入冻结原因', trigger: 'blur' }
        ],
        memo: [
          { required: true, message: '请输入备注', trigger: 'blur' }
        ],
        recoveryReason: [
          { required: true, message: '请输入放入回收站的原因', trigger: 'blur' },
          { min: 10, message: '原因不得为空  填写内容少于十个字', trigger: 'blur' },
          { max: 100, message: '最多不可超过100字', trigger: 'blur' }
        ]
      },

    }
  },
  computed: {
    ...mapState([
      "cellUseTypes",
    ]),
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
    },
    isRecoveryBtn() {
      if (this.isFreezeBtn || this.searchData.state == -2) {
        return true;
      }
      return false;
    }
    
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getInventoryBalanceData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getInventoryBalanceData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      this.searchData.organizationId = null;
      this.searchData.itemCode = null;
      this.searchData.itemName = null;
      this.searchData.cellCode = null;
      this.searchData.cellUseType = null;
      this.createTimeArr = [];
      this.searchData.startModifyTime = '';
      this.searchData.endModifyTime = '';
      this.searchData.state =0;
    },
    setPageNumFlush(){
      this.searchData.pageNum =1;
      this.getInventoryBalanceData();
    },
    getInventoryBalanceData() {
      let searchData = PF.JSON(this.searchData);
      if (!PF.isNull(searchData.itemCode)) {
        delete searchData.itemCode
      }
      if (!PF.isNull(searchData.itemName)) {
        delete searchData.itemName
      }
      if (!PF.isNull(searchData.cellCode)) {
        delete searchData.cellCode
      }
      searchData.pageSize = searchData.pageSize;
      this.tableLoading = true;
      if (this.createTimeArr != null && this.createTimeArr.length > 0) {
        searchData.startModifyTime = this.createTimeArr[0];
        searchData.endModifyTime = this.createTimeArr[1];
      }
      this.searchData.sort = 'cellUseTypeUseful';
      IOT.getServerData('/inventorys/list', 'get', searchData, (ret) => {
        this.tableLoading = false;
        if (ret.code === 200) {
          let list = ret.rows;
          /*for(let i=0;i<list.length;i++){
            list[i].quantity2 = list[i].quantity;
            list[i].packDetailId2 = list[i].packDetailId;
          }*/
          this.inventoryBalances = list;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    handleClose(done) {
      this.resetForm('form.freezeMaster');
      this.dialogFreezeVisible = false;
    },
    recoveryClose(done) {
      this.resetForm('recoveryForm');
      this.recovoryVisible = false;
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
    },
    showFreezeModal() {
      let list = PF.JSON(this.multipleSelection);
      console.log(list);
      this.form.freezeMaster.wareId = list[0].wareId;
      this.form.freezeMaster.wareName = list[0].wareName;
      this.form.freezeMaster.organizationId = list[0].organizationId;
      this.form.freezeMaster.organizationName = list[0].organizationName;

      for (let i = 0; i < list.length; i++) {
        list[i].nowFreezeQuantity = list[i].freezeQuantity;
        list[i].freezeQuantity = null;
        if(list[i].cellUseType==-1){
            IOT.tips('包含垃圾货位，不可进行库存冻结', 'error');
            return false;
          }
      }
      this.form.freezeDetails = list;
      this.dialogFreezeVisible = true;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteDetail(index) {
      this.form.freezeDetails.splice(index, 1)
    },
    commitForm(formName, insertType) {
      let that = this;
      IOT.showOverlay('保存中，请稍等...');
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = this.form;
          if (form.freezeDetails.length <= 0) {
            IOT.tips('未选择库存,请重新选择！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          for (let i = 0; i < form.freezeDetails.length; i++) {
            if (!PF.isNull(form.freezeDetails[i].freezeQuantity) && form.freezeDetails[i].freezeQuantity <= 0) {
              IOT.tips('数量需大于零！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }

            // 主单位数量
            form.freezeDetails[i].freezeQuantity = form.freezeDetails[i].freezeQuantity * form.freezeDetails[i].transRatio;

          }

          form.insertType = insertType;

          IOT.getServerData('/freeze/masters/insert', 'post', form, (ret) => {
            IOT.hideOverlay();

            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getInventoryBalanceData()
              });
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
          this.resetForm('form.freezeMaster');
          this.dialogFreezeVisible = false
        } else {
          IOT.hideOverlay();
          console.error('error submit!!');
          return false;
        }
      });
    },
    changePackDetailId(index) {
      /*let packDetailId = this.inventoryBalances[index].packDetailId;
      let packDetailId2 = this.inventoryBalances[index].packDetailId2;
      let quantity = this.inventoryBalances[index].quantity;
      let packDetails = this.inventoryBalances[index].packDetails;
      let oldTransRatio = 0;
      let nowTransRatio = 0;
      for(let i=0;i<packDetails.length;i++){
        if(packDetailId === oldPackDetailId){
          nowTransRatio = oldTransRatio = 1;
        }else if(packDetails[i].packDetailId1 === packDetailId){
          oldTransRatio = packDetails[i].transRatio
        }else if(packDetails[i].packDetailId1 === packDetailId2){
          nowTransRatio = packDetails[i].transRatio
        }
      }
      let quantity2 = (quantity / oldTransRatio) * nowTransRatio;
      this.inventoryBalances[index].quantity2 = quantity2;*/

      let detail = PF.JSON(this.inventoryBalances[index]);
      for (let i = 0; i < detail.packDetails.length; i++) {
        if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
          this.inventoryBalances[index].packDescribe = detail.packDetails[i].packDescribe1;
          this.inventoryBalances[index].transRatio = detail.packDetails[i].transRatio1;
        }
      }
    },
    //回收库存弹窗
    recovery() {
      this.recovoryVisible = true;
      this.recoveryForm.inventoryIds = [];
      let list = PF.JSON(this.multipleSelection);
      let batchs = new Set();
      let skus = new Set();
      this.inventoryNum = 0;
      list.forEach(item => {
        batchs.add(item.batchName);
        skus.add(item.itemCode);
        this.recoveryForm.inventoryIds.push(item.inventoryId);
        this.inventoryNum += item.quantity;
      });
      this.batchNum = batchs.size;
      this.skuNum = skus.size;
    },
    //确认回收
    submitRecovery(formName) {
      let form = this.recoveryForm;

      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$confirm('是否将库存放入回收站?', '再次确认', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            IOT.showOverlay('提交中，请稍等...');
            IOT.getServerData('/inventorys/recovery', 'post', form, (ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.getInventoryBalanceData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.recovoryVisible = false;
            this.resetForm('recoveryForm');
          }).catch(() => {
            IOT.hideOverlay();
          });

        } else {
          IOT.tips('请按要求完善原因！', 'error');
          return false;
        }
      });



    },
    // 导出
    exportList(){
      let searchData = PF.JSON(this.searchData);
      if (!PF.isNull(searchData.itemCode)) {
        delete searchData.itemCode
      }
      if (!PF.isNull(searchData.itemName)) {
        delete searchData.itemName
      }
      if (!PF.isNull(searchData.cellCode)) {
        delete searchData.cellCode
      }
      searchData.pageSize = searchData.pageSize;
      this.exportListLoading = true;
      if (this.createTimeArr != null && this.createTimeArr.length > 0) {
        searchData.startModifyTime = this.createTimeArr[0];
        searchData.endModifyTime = this.createTimeArr[1];
      }
      searchData.pageNum = null;
      searchData.pageSize = null;
      searchData.total = null;
      IOT.getServerData('/inventorys/exportList', 'get', searchData, (ret) => {
        this.exportListLoading = false;
        if (ret.code === 200) {
          window.location.href = ret.data;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      },true);
    }

  },
  created() {
    this.getInventoryBalanceData();
    PF.getDataDictUdfData('freeze_reason', (rows) => {
      this.freezeReasons = rows;
    });
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.inventoryBalance {
  /deep/ .cellUseType .el-input{
    width:180px;
  }
}
</style>
