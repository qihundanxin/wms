<template>
  <div class="inventoryBalance">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
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
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="批次/追踪号/LPN号" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="danger" round icon="iconfont icon-user-jinyong" @click="showFreezeModal" :disabled="isFreezeBtn">冻结库存</el-button>
    </div>

    <el-dialog title="新增冻结单" :visible.sync="dialogFreezeVisible" :before-close="handleClose" width="900px" style="margin-top: -10vh;">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form.freezeMaster" :rules="rules" ref="form.freezeMaster">
          <el-form-item label="冻结原因" prop="freezeDescribe" :label-width="formLabelWidth">
            <el-select v-model="form.freezeMaster.freezeDescribe" filterable placeholder="请选择冻结原因">
              <el-option
                v-for="item in freezeReasons"
                :key="item.dictKey"
                :label="item.dictValue"
                :value="item.dictKey">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.freezeMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
        </el-form>
        <div class="tableBox noHeight">
          <el-table
            highlight-current-row
            :data="form.freezeDetails"
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
                sortable
                show-overflow-tooltip
                prop="quantity"
                label="数量"
                width="120">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.quantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column
                sortable
                show-overflow-tooltip
                prop="nowFreezeQuantity"
                label="已冻结数量"
                width="120">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.nowFreezeQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              label="冻结数量"
              width="180">
              <template slot-scope="scope">
                <input type="number" class="my_input small" v-model.number="form.freezeDetails[scope.$index].freezeQuantity" :max="scope.row.quantity - scope.row.nowFreezeQuantity" placeholder="冻结数量" style="width: 80%;">
<!--                <el-input-number v-model="form.freezeDetails[scope.$index].freezeQuantity" controls-position="right" :min="0" :max="scope.row.quantity - scope.row.nowFreezeQuantity" style="width: 90%;"></el-input-number>-->
              </template>
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
              prop="allotQuantity"
              label="分配数量"
              width="100">
              <template slot-scope="scope">
                <span>{{ Math.floor(scope.row.allotQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
              </template>
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
                <el-button @click="deleteDetail(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button class="leftFloat" type="primary" plain @click="commitForm('form.freezeMaster',-2)">保存为草稿</el-button>
        <el-button @click="resetForm('form.freezeMaster');dialogFreezeVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.freezeMaster',0)">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox rowHeight">
      <el-table
      v-loading="tableLoading"
        highlight-current-row
        :data="inventoryBalances"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="selection"
          min-width="55">
        </el-table-column>
       
        <el-table-column
          fixed="left"
           
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
          min-width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
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
        <el-table-column
           
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          min-width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
           
          show-overflow-tooltip
          prop="batchName"
          label="批次"
          min-width="100">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.batchId, 'batch')">{{scope.row.batchName}}</span>
          </template>
        </el-table-column> -->
        <el-table-column
           
          show-overflow-tooltip
          prop="quantity"
          label="数量"
          min-width="140">
          <template slot-scope="scope">
            <span style="color:black;font-weight:900;">{{scope.row.canPickQuantity-scope.row.allotQuantity-scope.row.freezeQuantity}}   </span>
            
          </template>
        </el-table-column>
        
     
        <!-- <el-table-column
           
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="100">
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" filterable placeholder="请选择单位" size="small">
              <el-option
                v-for="item in scope.row.packDetails"
                :key="item.packDetailId1"
                :label="item.packDescribe1"
                :value="item.packDetailId1">
              </el-option>
            </el-select>
          </template>
        </el-table-column>-->
        <!-- <el-table-column 
           
          show-overflow-tooltip
          prop="cellCode"
          label="货位"
          min-width="120">
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="boxCode"
          label="追踪号"
          min-width="120">
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="lpn"
          label="LPN码"
          min-width="120">
        </el-table-column> -->
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-bottom"
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
  export default {
    name: 'InventoryBalance',
    data() {
      return {
        isMoreSearch: false,
        tableLoading:false,
        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          itemName:'',
          itemCode:'',
          cellCode:'',
          hasInventoryLimit:1

        },

        inventoryBalances: [],
        organizations: [],
        multipleSelection: [],
        freezeReasons: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        dialogFreezeVisible: false,

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
        rules:{
          freezeDescribe: [
            { required: true, message: '请输入冻结原因', trigger: 'blur' }
          ],
          memo: [
            { required: true, message: '请输入备注', trigger: 'blur' }
          ],
        },

      }
    },
    computed: {
      isFreezeBtn(){
        if(this.multipleSelection.length <= 0){
          return true
        }
        let organizationId = this.multipleSelection[0].organizationId;
        for(let i=0;i<this.multipleSelection.length;i++){
          if(organizationId !== this.multipleSelection[i].organizationId){
            return true
          }
        }
        return false
      }
    },
    methods: {
      handleSizeChange(val){
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
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getInventoryBalanceData();
      },
      getInventoryBalanceData() {
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
        if(!PF.isNull(searchData.cellCode)){
          delete searchData.cellCode
        }
        searchData.pageSize = searchData.pageSize ;
        this.tableLoading = true;
        IOT.getServerData('/inventorys/canOut', 'get', searchData, (ret) => {
           this.tableLoading = false;
          if (ret.code === 200) {
            let list = ret.rows; 
            this.inventoryBalances = list;
            this.searchData.total = ret.total ;
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
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      showFreezeModal(){
        let list = PF.JSON(this.multipleSelection);
        this.form.freezeMaster.wareId = list[0].wareId;
        this.form.freezeMaster.wareName = list[0].wareName;
        this.form.freezeMaster.organizationId = list[0].organizationId;
        this.form.freezeMaster.organizationName = list[0].organizationName;

        for(let i=0;i<list.length;i++){
          list[i].nowFreezeQuantity = list[i].freezeQuantity;
          list[i].freezeQuantity = null;
          if(list[i].transRatio==null || list[i].transRatio==undefined){
            list[i].transRatio = 1;
          }
        
        }
        this.form.freezeDetails = list;
        this.dialogFreezeVisible = true;
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      deleteDetail(index){
        this.form.freezeDetails.splice(index,1)
      },
      commitForm(formName,insertType) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = this.form;
            if(form.freezeDetails.length <= 0){
              IOT.tips('未选择库存,请重新选择！', 'error', 1000 , () => {
                IOT.hideOverlay();
              });
              return false
            }
            for(let i=0;i<form.freezeDetails.length;i++){
              if(!PF.isNull(form.freezeDetails[i].freezeQuantity) && form.freezeDetails[i].freezeQuantity <= 0){
                IOT.tips('数量需大于零！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }

              // 主单位数量
              form.freezeDetails[i].freezeQuantity = form.freezeDetails[i].freezeQuantity * form.freezeDetails[i].transRatio;
              console.log("-----------------------------------")
              console.log( form.freezeDetails[i].freezeQuantity);
            }
            
            form.insertType = insertType;
            
            IOT.getServerData('/freeze/masters/insert','post',form,(ret) => {
              IOT.hideOverlay();
              
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
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
      changePackDetailId(index){
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

  }
</style>
