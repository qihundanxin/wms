<template>
  <div class="inventoryAdjust">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getInventoryAdjustData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>调整单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="调整单号" clearable></el-input>
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
          <el-select v-model="searchData.caseState" size="mini" filterable placeholder="请选择审核状态" clearable>
            <el-option
                    v-for="item in reviewStates"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>调整原因</span>
          <el-select v-model="searchData.reason" size="mini" filterable placeholder="请选择调整原因" clearable>
            <el-option
                    v-for="item in reasons"
                    :key="item.reason"
                    :label="item.reason"
                    :value="item.reason">
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
          <el-input v-model="searchData.keyWords" size="mini" placeholder="原因描述/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              :data="inventoryAdjusts"
              style="width: 100%">
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                fixed="left"
                sortable
                prop="billNo"
                label="单号"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'inventoryAdjustDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="wareName"
                label="仓库"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="organizationName"
                label="货主"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <!--<el-table-column
                sortable
                show-overflow-tooltip
                prop="caseState"
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
        </el-table-column>-->
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="type"
                label="类型"
                width="180">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in inventoryAdjustType" :key="index" v-if="scope.row.type === item.value">{{item.label}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="reason"
                label="调整原因"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="reasonDescription"
                label="原因描述"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="备注">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="userName"
                label="创建人"
                width="150">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.userId, 'account')" type="text">{{scope.row.userName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="createTime"
                label="创建时间"
                width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,14)}}
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
    name: 'inventoryAdjust',
    data(){
      return {
        isMoreSearch: false,
        isMoreSearch2: false,
        loading:{
          masterTable:true,
        },
        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          insertType: -2,
          wareId: PF.getLocal('wareId', 'number'),
          billNo:'',
          caseState:'',
          organizationId:null,
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          reason:''
        },
        reasons:[],
        searchSelectInventory:{
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
        },

        wares: [],
        organizations: [],
        inventoryAdjusts: [],
        inventorys: [],
        multipleSelection: [],
        selectIds: [],


        dialogNewVisible: false,
        dialogUpdateVisible: false,
        innerVisibleInventory: false,

        formLabelWidth: '120px',

        form: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          wareCode:'',
          reason: '',
          memo: '',
          type:2,
          organizationId: null,
          organizationName: '',
          reasonDescription:'',
          adjustDetails: []
        },
        rules:{
          reason: [
            { required: true, message: '请选择或输入调整原因', trigger: 'blur' }
          ],
          reasonDescription:[{required:true,message:'请输入原因描述',trigger:'blur'}],
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
              "inventoryAdjustType",
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
      changeOrganizationId(val){
        if(!PF.isNull(this.oldOrganizationId)){
          this.oldOrganizationId = val;
        }else{
          this.$confirm('修改货主已选物料将清空？')
                  .then(_ => {
                    this.oldOrganizationId = val;
                    this.form.adjustDetails = [];
                  })
                  .catch(_ => {
                    this.form.organizationId = this.oldOrganizationId;
                  });
        }
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryAdjustData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryAdjustData()
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
        this.searchData.caseState = '';
        this.searchData.organizationId = null;
        this.searchData.reason = '';
      },
      clearSearchSelectInventory(){
        this.searchSelectInventory.keyWords = '';
        this.searchSelectInventory.wareId = null;
        this.searchSelectInventory.organizationId = null;
      },
      getReasonData(){
        IOT.getServerData('/reasons/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.reasons = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getInventoryAdjustData(){
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        IOT.getServerData('/inventory/adjusts/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.inventoryAdjusts = ret.rows;
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
        this.form.adjustDetails = [];
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form');
        this.dialogNewVisible = false;
      },
      showSelectInventoryModal(){
        if(!PF.isNull(this.form.wareId)){
          IOT.tips('未获取到仓库！', 'error');
          return false
        }else if(!PF.isNull(this.form.organizationId)){
          IOT.tips('请先选择货主！', 'error');
          return false
        }
        this.searchSelectInventory.wareId = this.form.wareId;
        this.searchSelectInventory.organizationId = this.form.organizationId;
        this.selectIds = [];
        for(let i=0;i<this.form.adjustDetails.length;i++){
          this.selectIds.push(this.form.adjustDetails[i].inventoryId)
        }
        this.getInventoryData();
        this.innerVisibleInventory = true
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clickSelectInventory(){
        let list = PF.JSON(this.multipleSelection);
        this.form.wareId = list[0].wareId;
        this.form.wareName = list[0].wareName;
        this.form.organizationId = list[0].organizationId;
        this.form.organizationName = list[0].organizationName;

        let isRepeat = false;
        for(let i=0;i<list.length;i++){
          list[i].nowFreezeQuantity = list[i].freezeQuantity;
          list[i].freezeQuantity = 0;
          if(this.form.adjustDetails.length > 0){
            if(this.selectIds.indexOf(list[i].inventoryId) === -1 && list[i].organizationId === this.form.adjustDetails[0].organizationId){
              this.form.adjustDetails.push(list[i]);
            }else{
              isRepeat = true
            }
          }else{
            if(this.selectIds.indexOf(list[i].inventoryId) === -1){
              this.form.adjustDetails.push(list[i]);
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
        this.form.adjustDetails.splice(index,1)
      },
      commitForm(formName,insertType) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = this.form;
            if(form.adjustDetails.length <= 0){
              IOT.tips('未选择库存,请重新选择！', 'error', 1000 , () => {
                IOT.hideOverlay();
              });
              return false
            }
            for(let i=0;i<form.adjustDetails.length;i++){
              if(form.adjustDetails[i].adjustQuantity === 0){
                IOT.tips('数量不得为零,请重新输入！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
            for (let i = 0; i < this.wares.length; i++) {
              if (this.wares[i].wareId === form.wareId) {
                form.wareName = this.wares[i].wareName;
                form.wareCode = this.wares[i].wareCode;
                break;
              }
            }
            for(let i=0;i<this.organizations.length;i++){
              if(this.organizations[i].organizationId === form.organizationId){
                this.form.organizationName = this.organizations[i].organizationName;
                break;
              }
            }
            form.insertType = insertType;
            
            IOT.getServerData('/inventory/adjusts/createInventoryAdjust','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getInventoryAdjustData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
    },
    created() {
      this.getInventoryAdjustData();
      this.getReasonData();
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

  }
</style>
