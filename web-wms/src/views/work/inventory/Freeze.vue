<template>
  <div class="freeze">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>冻结单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="冻结单号" clearable></el-input>
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
          <span>审核状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择审核状态" clearable>
            <el-option
                    v-for="item in reviewStates"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>冻结原因</span>
          <el-select v-model="searchData.freezeDescribe" size="mini" filterable placeholder="请选择冻结原因" clearable>
            <el-option
                    v-for="item in freezeReasons"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker
                  v-model="searchData.createTimeArr"
                  size="mini"
                  type="daterange"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="创建人/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNew">新增</el-button>
    </div>

    <el-dialog title="新增冻结单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px" style="margin-top: -10vh;">
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
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.freezeMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.freezeMaster.wareId" disabled filterable placeholder="请选择仓库"
                        >
              <el-option
                      :key="form.freezeMaster.wareId"
                      :label="form.freezeMaster.wareName"
                      :value="form.freezeMaster.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.freezeMaster.organizationId"  filterable placeholder="请选择货主"
                        >
              <el-option
                      v-for="item in organizations"
                      :key="item.organizationId"
                      :label="item.organizationName"
                      :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="headerBtns">
          <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectInventoryModal">选择库存</el-button>
        </div>
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
              prop="itemName"
              label="SKU名称"
              width="150">
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
              show-overflow-tooltip
              prop="nowFreezeQuantity"
              label="已冻结数量"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="quantity"
              label="数量"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="packDescribe"
              label="单位"
              width="100">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="batchName"
              label="批次"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="itemCode"
              label="SKU"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="allotQuantity"
              label="分配数量"
              width="100">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="wareName"
              label="仓库"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="cellCode"
              label="货位"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="boxCode"
              label="追踪号"
              width="180">
            </el-table-column>
            <el-table-column
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

        <el-dialog width="800px" title="选择库存" :visible.sync="innerVisibleInventory" append-to-body style="margin-top: -10vh;">

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
                <span>货位</span>
                <el-input v-model="searchSelectInventory.eqCellCode" size="mini" placeholder="请输入货位" clearable></el-input>
              </div>
              <div>
                <span>批次</span>
                <el-input v-model="searchSelectInventory.eqBatchName" size="mini" placeholder="请输入批次" clearable></el-input>
              </div>
<!--              <div>-->
<!--                <span>仓库</span>-->
<!--                <el-select v-model="searchSelectInventory.wareId" size="mini" filterable placeholder="请选择仓库" clearable>-->
<!--                  <el-option-->
<!--                    v-for="item in wares"-->
<!--                    :key="item.wareId"-->
<!--                    :label="item.wareName"-->
<!--                    :value="item.wareId">-->
<!--                  </el-option>-->
<!--                </el-select>-->
<!--              </div>-->
<!--              <div>-->
<!--                <span>货主</span>-->
<!--                <el-select v-model="searchSelectInventory.organizationId" size="mini" filterable placeholder="请选择货主" clearable>-->
<!--                  <el-option-->
<!--                    v-for="item in organizations"-->
<!--                    :key="item.organizationId"-->
<!--                    :label="item.organizationName"-->
<!--                    :value="item.organizationId">-->
<!--                  </el-option>-->
<!--                </el-select>-->
<!--              </div>-->
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
                prop="organizationName"
                label="货主"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="batchName"
                label="批次"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="packDescribe"
                label="单位"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="quantity"
                label="数量"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="allotQuantity"
                label="分配数量"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="freezeQuantity"
                label="冻结数量"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="wareName"
                label="仓库"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="cellCode"
                label="货位"
                width="180">
              </el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="boxCode"
                label="追踪号"
                width="180">
              </el-table-column>
              <el-table-column
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
      </div>
      <div slot="footer" class="dialog-footer">
       <!-- <el-button class="leftFloat" type="primary" plain @click="commitForm('form.freezeMaster',-2)">保存为草稿</el-button>-->
        <el-button @click="resetForm('form.freezeMaster'),dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.freezeMaster',0)">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="freezes"
        @sort-change="sortChange"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          fixed="left"
          prop="billNo"
          label="单号"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'freezeDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="state"
          label="审核状态"
          width="100">
          <template slot-scope="scope">
            <div v-show="scope.row.insertType === 0">
              <el-tag v-show="scope.row.state === -1" type="danger">不通过</el-tag>
              <el-tag v-show="scope.row.state === 0">流转中</el-tag>
              <el-tag v-show="scope.row.state === 1" type="success">通过</el-tag>
              <el-tag v-show="scope.row.state === 2" type="info">初始化</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="freezeDescribe"
          label="冻结原因"
          width="180">
          <template slot-scope="scope">
            <span v-for="(item,index) in freezeReasons" :key="index" v-if="scope.row.freezeDescribe === item.dictKey">{{item.dictValue}}</span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="memo"
          label="备注">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="createUseName"
          label="创建人"
          width="150">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUseId, 'account')" type="text">{{scope.row.createUseName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          sortable="custom"
          show-overflow-tooltip
          prop="createTime"
          label="创建时间"
          width="200">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(0,16)}}
          </template>
        </el-table-column>
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
  import {mapState} from "vuex";

  export default{
    name: 'Freeze',
    data(){
      return {
        isMoreSearch: false,
        isMoreSearch2: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          billNo:'',
          state:'',
          organizationId:null,
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          freezeDescribe:'',
          sort:null
        },
        searchSelectInventory:{
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          eqCellCode:'',
          eqBatchName:'',
        },

        wares: [],
        organizations: [],
        freezes: [],
        inventorys: [],
        multipleSelection: [],
        selectIds: [],
        freezeReasons: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        innerVisibleInventory: false,

        formLabelWidth: '120px',

        form: {
          freezeMaster: {
            wareId: PF.getLocal('wareId', 'number'),
            wareName: PF.getLocal('wareName'),
            freezeDescribe: '',
            memo: '',
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
          organizationId: [
            { required: true, message: '请选择货主', trigger: 'blur' }
          ],
        },

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
      ]),
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
      dialogNew(){
        this.dialogNewVisible = true;
        this.form.freezeDetails = [];
      },
      sortChange(column){
        if(column.order=="ascending"){
          this.searchData.sort="ASC";
        }else{
          this.searchData.sort="DESC"
        }
        this.getFreezeData();
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getFreezeData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getFreezeData()
      },
      handleCurrentChange2(val) {
        this.searchSelectInventory.pageNum = val;
        this.getInventoryData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.createTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
        this.searchData.billNo = '';
        this.searchData.state = '';
        this.searchData.organizationId = null;
        this.searchData.freezeDescribe = '';


      },
      clearSearchSelectInventory(){
        this.searchSelectInventory.keyWords = '';
        this.searchSelectInventory.wareId = null;
        this.searchSelectInventory.organizationId = null;
         this.searchSelectInventory.eqCellCode = '';
        this.searchSelectInventory.eqBatchName = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getFreezeData();
      },
      getFreezeData(){
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        IOT.getServerData('/freeze/masters/list','get',searchData,(ret) => {
          if (ret.code === 200) {
            this.freezes = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getWareData(){
        IOT.getServerData('/ware/infos/findSome','get',{},(ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getInventoryData(){
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
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form.freezeMaster');
        this.dialogNewVisible = false;
      },
      showSelectInventoryModal(){
        if(!PF.isNull(this.form.freezeMaster.wareId)){
          IOT.tips('未获取到仓库！', 'error');
          return false
        }else if(!PF.isNull(this.form.freezeMaster.organizationId)){
          IOT.tips('请先选择货主！', 'error');
          return false
        }
        this.searchSelectInventory.wareId = this.form.freezeMaster.wareId;
        this.searchSelectInventory.organizationId = this.form.freezeMaster.organizationId;
        this.selectIds = [];
        for(let i=0;i<this.form.freezeDetails.length;i++){
          this.selectIds.push(this.form.freezeDetails[i].inventoryId)
        }
        this.getInventoryData();
        this.innerVisibleInventory = true
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clickSelectInventory(){
        let list = PF.JSON(this.multipleSelection);
        this.form.freezeMaster.wareId = list[0].wareId;
        this.form.freezeMaster.wareName = list[0].wareName;
        this.form.freezeMaster.organizationId = list[0].organizationId;
        this.form.freezeMaster.organizationName = list[0].organizationName;

        let isRepeat = false;
        for(let i=0;i<list.length;i++){
          if(list[i].cellUseType==-1){
            IOT.tips('包含垃圾货位，不可进行库存冻结', 'error');
            return false;
          }
          list[i].nowFreezeQuantity = list[i].freezeQuantity;
          list[i].freezeQuantity = 0;
          if(this.form.freezeDetails.length > 0){
            if(this.selectIds.indexOf(list[i].inventoryId) === -1 && list[i].organizationId === this.form.freezeDetails[0].organizationId){
              this.form.freezeDetails.push(list[i]);
            }else{
              isRepeat = true
            }
          }else{
            if(this.selectIds.indexOf(list[i].inventoryId) === -1){
              this.form.freezeDetails.push(list[i]);
            }else{
              isRepeat = true
            }
          }
        }
        if(isRepeat){
          IOT.tips('不能重复添加或添加非此货主！', 'error');
        }
        this.innerVisibleInventory = false;
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
              if(form.freezeDetails[i].freezeQuantity === 0){
                IOT.tips('数量不得为零,请重新输入！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
            form.insertType = insertType;
            IOT.getServerData('/freeze/masters/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getFreezeData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogNewVisible = false;

          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }

        });
      },
    },
    created() {
      this.getFreezeData();
      PF.getDataDictUdfData('freeze_reason', (rows) => {
        this.freezeReasons = rows;
      });
      this.getWareData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .freeze{
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
      .publicSearch.body .moreSearchBtn{
      z-index:199 !important;
    }
</style>
