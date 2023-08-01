<template>
  <div class="freezeDraft">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getFreezeData" size="mini" icon="el-icon-search">查询</el-button>
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
          <el-select v-model="form.freezeMaster.freezeDescribe" filterable placeholder="请选择冻结原因" clearable>
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

    <div class="tableBox">
      <el-table
              highlight-current-row
              :data="freezes"
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
            <span class="textBtn" @click="$router.push({name: 'freezeDetail',params: scope.row})">{{scope.row.billNo}}</span>
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
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="state"
                label="审核状态"
                width="100">
          <template slot-scope="scope">
            <div v-show="scope.row.insertType === -2">
              <el-tag v-show="scope.row.state === -1" type="danger">不通过</el-tag>
              <el-tag v-show="scope.row.state === 0">流转中</el-tag>
              <el-tag v-show="scope.row.state === 1" type="success">通过</el-tag>
              <el-tag v-show="scope.row.state === 2" type="info">初始化</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="freezeDescribe"
                label="冻结原因"
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
                prop="createUseName"
                label="创建人"
                width="150">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUseId, 'account')" type="text">{{scope.row.createUseName}}</el-button>
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
        class="pagination-bottom"
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
  import {mapState} from "vuex";

  export default{
    name: 'FreezeDraft',
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
          insertType:-2,
          organizationId:null,
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:''
        },
        searchSelectInventory:{
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: null,
          organizationId: null,
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
                  that.getInventoryBalanceData()
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
  .freezeDraft{

  }
</style>
