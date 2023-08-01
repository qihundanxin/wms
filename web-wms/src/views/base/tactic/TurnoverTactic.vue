<template>
  <div class="turnoverTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getTurnoverTacticData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="描述/编码" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>
    <el-dialog title="新增库存周转规则" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="库存周转编码" prop="turnoverCode" :label-width="formLabelWidth">
            <el-input v-model="form.turnoverCode" placeholder="请输入编码"
                      style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="规则描述" prop="turnoverDescription" :label-width="formLabelWidth">
            <el-input v-model="form.turnoverDescription" placeholder="请输入描述"
                      style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="批次规则" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.batchTacticCode" filterable placeholder="请选择批次规则" @change="getBatchTacticDetailData"
                       style="width: 90%;">
              <el-option
                      v-for="item in batchTactics"
                      :key="item.batchTacticCode"
                      :label="item.batchTacticCode"
                      :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <div class="my_table my_tab_5">
          <div style="width: 15%;">优先级</div>
          <div style="width: 35%;">批次属性编码</div>
          <div>排序</div>
          <div>操作</div>
        </div>
        <div class="my_table my_tab_5" v-for="(item,index) in form.turnoverTacticDetails" :key="index">
          <div style="width: 15%;">{{index+1}}</div>
          <div style="width: 35%;">
            <el-select
                    v-model="item.batchTacticDetailCode"
                    collapse-tags
                    placeholder="请选择" style="width: 90%;" >
              <el-option
                      v-for="item in batchTacticDetails"
                      :key="item.detailCode"
                      :label="item.detailCode"
                      :value="item.detailCode">
              </el-option>
            </el-select>
          </div>
          <div>
            <el-select
                    v-model="item.sort"
                    collapse-tags
                    placeholder="请选择" style="width: 90%;">
              <el-option
                      v-for="item in sort"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div>
            <el-button @click="nodeUpDown(index,'save','up')" icon="iconfont icon-up" type="text"
                       :disabled="index===0?true:false"></el-button>
            <el-button @click="nodeUpDown(index,'save','down')" icon="iconfont icon-down" type="text"
                       :disabled="index===form.turnoverTacticDetails.length-1?true:false"></el-button>
            <el-button @click="nodeAdd(index,'save')" icon="iconfont icon-jia" type="text"></el-button>
            <el-button @click="nodeDelete(index,'save')" icon="iconfont icon-shanchu" type="text"
                       :disabled="form.turnoverTacticDetails.length<=1?true:false"></el-button>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');handleClose();dialogNewVisible = false">取 消
        </el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog >

    <el-dialog title="修改库存周转规则" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="库存周转编码" prop="turnoverCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.turnoverCode" placeholder="请输入编码"
                      style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="规则描述" prop="turnoverDescription" :label-width="formLabelWidth">
            <el-input v-model="rowData.turnoverDescription" placeholder="请输入描述"
                      style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="批次规则" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.batchTacticCode" filterable placeholder="请选择批次规则" @change="getBatchTacticDetailData"
                       style="width: 90%;">
              <el-option
                      v-for="item in batchTactics"
                      :key="item.batchTacticCode"
                      :label="item.batchTacticCode"
                      :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <div class="my_table my_tab_5">
          <div style="width: 15%;">优先级</div>
          <div style="width: 35%;">批次属性编码</div>
          <div>排序</div>
          <div>操作</div>
        </div>
        <div class="my_table my_tab_5" v-for="(item,index) in rowData.turnoverTacticDetails" :key="index">
          <div style="width: 15%;">{{index+1}}</div>
          <div style="width: 35%;">
            <el-select
                    v-model="item.batchTacticDetailCode"
                    collapse-tags
                    placeholder="请选择" style="width: 90%;" >
              <el-option
                      v-for="item in batchTacticDetails"
                      :key="item.detailCode"
                      :label="item.detailCode"
                      :value="item.detailCode">
              </el-option>
            </el-select>
          </div>
          <div>
            <el-select
                    v-model="item.sort"
                    collapse-tags
                    placeholder="请选择" style="width: 90%;">
              <el-option
                      v-for="item in sort"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div>
            <el-button @click="nodeUpDown(index,'update','up')" icon="iconfont icon-up" type="text"
                       :disabled="index===0?true:false"></el-button>
            <el-button @click="nodeUpDown(index,'update','down')" icon="iconfont icon-down" type="text"
                       :disabled="index===rowData.turnoverTacticDetails.length-1?true:false"></el-button>
            <el-button @click="nodeAdd(index,'update')" icon="iconfont icon-jia" type="text"></el-button>
            <el-button @click="nodeDelete(index,'update')" icon="iconfont icon-shanchu" type="text"
                       :disabled="rowData.turnoverTacticDetails.length<=1?true:false"></el-button>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
                @click="resetForm('rowData');handleClose2();dialogUpdateVisible = false">取 消
        </el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab" style="width: 50%;">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              @current-change="getLookRowData"
              :data="turnoverTactics"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="turnoverCode"
                label="库存周转编码">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="turnoverDescription"
                label="描述">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="batchTacticCode"
                label="批次规则编码">
        </el-table-column>
        <el-table-column
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-left-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight" style="width: 44%;">
      <div class="organizationRightTitle">库存周转策略信息</div>
      <p class="MasterBox">
        <span><b>库存周转策略代码：</b>{{nowTurnoverTactic.TurnoverTactic ? nowTurnoverTactic.TurnoverTactic.turnoverCode : '无'}}</span>
        <span><b>库存周转策略描述：</b>{{nowTurnoverTactic.TurnoverTactic ? nowTurnoverTactic.TurnoverTactic.turnoverDescription : '无'}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>

      <div class="tableBox table-400h">
        <el-table
                v-loading="loading.detailTable"
                highlight-current-row
                :data="nowTurnoverTactic.TurnoverTacticDetails"
                style="width: 100%">
          <el-table-column
                  type="index"
                  width="50">
          </el-table-column>
          <el-table-column
                sortable
                show-overflow-tooltip
                  label="批次属性编码"
                  prop="batchTacticDetailCode"
                  width="180">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="sortPriority"
                  label="优先级">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="sort"
                  label="排序">
            <template slot-scope="scope">
              <el-tag v-for="(item, index) in sort" :key="index" v-if="scope.row.sort === item.value">{{item.label}}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from 'vuex'
  export default{
    name: 'turnoverTactic',
    data(){
      let validateTurnoverCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'turnover_tactic', 'turnover_code', this.form.turnoverCode, 0);
      };

      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,
          detailTable:true,
        },
        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },

        turnoverTactics: [],
        nowTurnoverTactic: {
          batchTactic: {},
          TurnoverTacticDetails: [],
        },


        dialogNewVisible: false,
        dialogUpdateVisible: false,

        innerVisible1: false,
        innerVisible2: false,

        batchTactics:[],
        batchTacticDetails:[],

        form: {
          turnoverCode: '',
          turnoverDescription: '',
          batchTacticCode: '',
          turnoverTacticDetails: [
            {
              sortPriority: 0,
              batchTacticDetailCode: '',
              sort: 1,
            }
          ]
        },

        rules:{
          turnoverDescription: [
            { required: true, message: '请输入规则描述', trigger: 'blur' },
          ],
          turnoverCode: [
            { required: true, message: '请输入库存周转规则', trigger: 'blur' },
            { validator: validateTurnoverCode, trigger: 'blur'  }
          ],
          batchTacticCode:[
            { required: true, message: '请选择批次策略编码', trigger: 'blur' },
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          turnoverMasterId:'',
          turnoverCode: '',
          turnoverDescription: '',
          batchTacticCode: '',
          turnoverTacticDetails: [
            {
              sortPriority: 0,
              batchTacticDetailCode: '',
              sort: 1,
            }
          ]
        },
        rules2:{
          turnoverDescription: [
            { required: true, message: '请输入规则描述', trigger: 'blur' },
          ],
          turnoverCode: [
            { required: true, message: '请输入库存周转规则', trigger: 'blur' },
            { validator: validateTurnoverCode, trigger: 'blur'  }
          ],
          batchTacticCode:[
            { required: true, message: '请选择批次策略编码', trigger: 'blur' },
          ],
        },
      }
    },
    computed: {
      ...mapState([
        "sort",

      ]),
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getTurnoverTacticData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getTurnoverTacticData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getTurnoverTacticData(){
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/turnover/tactics/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.turnoverTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if(ret.rows[0] === null || ret.rows[0] === undefined){
              this.loading.detailTable = false
              return false
            }
            this.getLookRowData(ret.rows[0])
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        if(row === null || row === undefined){
          return false
        }
        this.nowTurnoverTactic.TurnoverTactic = JSON.parse(JSON.stringify(row));
        this.getTurnoverTacticDetailData(this.nowTurnoverTactic.TurnoverTactic.turnoverCode)
      },
      getTurnoverTacticDetailData(code,type){
        this.loading.detailTable = true;
        IOT.getServerData('/turnover/tactic/details/list','get',{turnoverCode: code},(ret) => {
          this.loading.detailTable = false;
          if (ret.code === 200) {
            if(type === 'update'){
              this.rowData.turnoverTacticDetails = ret.rows;
            }else{
              this.nowTurnoverTactic.TurnoverTacticDetails = ret.rows;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      nodeUpDown(index, type, state) {
        let turnoverTacticDetails = [];
        if (type === 'save') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.form.turnoverTacticDetails));
        } else if (type === 'update') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.rowData.turnoverTacticDetails));
        }
        let nowTurnoverTacticDetails = turnoverTacticDetails[index];
        if (state === 'up') {
          turnoverTacticDetails[index] = turnoverTacticDetails[index - 1];
          turnoverTacticDetails[index - 1] = nowTurnoverTacticDetails;
        } else if (state === 'down') {
          turnoverTacticDetails[index] = turnoverTacticDetails[index + 1];
          turnoverTacticDetails[index + 1] = nowTurnoverTacticDetails;
        }
        if (type === 'save') {
          this.form.turnoverTacticDetails = turnoverTacticDetails;
        } else if (type === 'update') {
          this.rowData.turnoverTacticDetails = turnoverTacticDetails;
        }
      },
      nodeAdd(index, type) {
        let turnoverTacticDetails = [];
        if (type === 'save') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.form.turnoverTacticDetails));
        } else if (type === 'update') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.rowData.turnoverTacticDetails));
        }
        turnoverTacticDetails.splice(index + 1, 0, {
          sortPriority: 0,
          batchTacticDetailCode: '',
          sort: 1,
        });
        if (type === 'save') {
          this.form.turnoverTacticDetails = turnoverTacticDetails;
        } else if (type === 'update') {
          this.rowData.turnoverTacticDetails = turnoverTacticDetails;
        }
      },
      nodeDelete(index, type) {
        let turnoverTacticDetails = [];
        if (type === 'save') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.form.turnoverTacticDetails));
        } else if (type === 'update') {
          turnoverTacticDetails = JSON.parse(JSON.stringify(this.rowData.turnoverTacticDetails));
        }
        turnoverTacticDetails.splice(index, 1);
        if (type === 'save') {
          this.form.turnoverTacticDetails = turnoverTacticDetails;
        } else if (type === 'update') {
          this.rowData.turnoverTacticDetails = turnoverTacticDetails;
        }
      },
      handleClose() {
        this.form={
          turnoverCode: '',
          turnoverDescription: '',
          batchTacticCode: '',
          turnoverTacticDetails: [
            {
              sortPriority: 0,
              batchTacticDetailCode: '',
              sort: 1,
            }
          ]
        }
        this.dialogNewVisible = false;
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = JSON.parse(JSON.stringify(this.form));
            for(let i=0;i<form.turnoverTacticDetails.length;i++){
              if(!PF.isNull(form.turnoverTacticDetails[i].batchTacticDetailCode)){
                IOT.hideOverlay();
                IOT.tips('请选择批次属性或删除此行！', 'error');
                return false;
              }
              if(!PF.isNull(form.turnoverTacticDetails[i].sort)){
                IOT.hideOverlay();
                IOT.tips('请选择排序或删除此行！', 'error');
                return false;
              }
              for(let j=i+1;j<form.turnoverTacticDetails.length;j++){
                if(form.turnoverTacticDetails[i].batchTacticDetailCode === form.turnoverTacticDetails[j].batchTacticDetailCode){
                  IOT.hideOverlay();
                  IOT.tips('批次属性重复请重新选择！', 'error');
                  return false;
                }
              }
            }
            IOT.getServerData('/turnover/tactics/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getTurnoverTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form');
            this.handleClose();
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      handleClose2(done) {
        this.rowData={
          turnoverMasterId:'',
          turnoverCode: '',
          turnoverDescription: '',
          batchTacticCode: '',
          turnoverTacticDetails: [
            {
              sortPriority: 0,
              batchTacticDetailCode: '',
              sort: 1,
            }
          ]
        }
        this.dialogUpdateVisible = false;
      },
      showUpdateModal(row){
        this.getTurnoverTacticDetailData(row.turnoverCode,'update');
        let a = JSON.parse(JSON.stringify(row));
        this.rowData.turnoverMasterId = a.turnoverMasterId,
        this.rowData.turnoverCode = a.turnoverCode;
        this.rowData.turnoverDescription = a.turnoverDescription;
        this.rowData.batchTacticCode = a.batchTacticCode;
        this.dialogUpdateVisible = true
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let rowData = JSON.parse(JSON.stringify(this.rowData));
            for(let i=0;i<rowData.turnoverTacticDetails.length;i++){
              if(!PF.isNull(rowData.turnoverTacticDetails[i].batchTacticDetailCode)){
                IOT.hideOverlay();
                IOT.tips('请选择批次属性或删除此行！', 'error');
                return false;
              }
              if(!PF.isNull(rowData.turnoverTacticDetails[i].sort)){
                IOT.hideOverlay();
                IOT.tips('请选择排序或删除此行！', 'error');
                return false;
              }
              for(let j=i+1;j<rowData.turnoverTacticDetails.length;j++){
                if(rowData.turnoverTacticDetails[i].batchTacticDetailCode === rowData.turnoverTacticDetails[j].batchTacticDetailCode){
                  IOT.hideOverlay();
                  IOT.tips('批次属性重复请重新选择！', 'error');
                  return false;
                }
              }
            }
            IOT.getServerData('/turnover/tactics/updateById','post',rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getTurnoverTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData')
            this.dialogUpdateVisible = false;
            this.handleClose2();
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      deleteTurnoverTactic(row){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/turnover/tactics/deleteByIdAndTurnoverCode','get',{turnoverMasterId: row.turnoverMasterId,turnoverCode:row.turnoverCode},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.nowTurnoverTactic.TurnoverTactic = {};
              that.getTurnoverTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      isDelete(row){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteTurnoverTactic(row)
          })
          .catch(_ => {});
      },
      getBatchTacticDetailData(){
        if(this.batchTacticDetails.length>0){
          this.batchTacticDetails = [];
          this.form={
            turnoverCode: this.form.turnoverCode,
            turnoverDescription: this.form.turnoverDescription,
            batchTacticCode: this.form.batchTacticCode,
            turnoverTacticDetails: [
              {
                sortPriority: 0,
                batchTacticDetailCode: '',
                sort: 1,
              }
            ]
          }
          this.rowData={
            turnoverMasterId:this.rowData.turnoverMasterId,
            turnoverCode: this.rowData.turnoverCode,
            turnoverDescription: this.rowData.turnoverDescription,
            batchTacticCode: this.rowData.batchTacticCode,
            turnoverTacticDetails: [
              {
                sortPriority: 0,
                batchTacticDetailCode: '',
                sort: 1,
              }
            ]
          }
        }
        IOT.getServerData('/batch/tactic/details/list', 'get', {batchTacticCode:this.form.batchTacticCode}, (ret) => {
          if (ret.code === 200) {
            this.batchTacticDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getBatchTacticData() {
        IOT.getServerData('/batch/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.batchTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getBatchTacticData();
      this.getTurnoverTacticData();
      this.getSysParamMasterDetailData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .batchTactic{

  }
</style>
