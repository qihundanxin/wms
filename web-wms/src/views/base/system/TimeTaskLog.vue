<template>
  <div class="timeTaskLog">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getTimeTaskLogData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData2" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">/monitor/jobLog/list
          <span>关键字搜索</span>
          <el-input v-model="searchData2.keyWord" size="mini" placeholder="名称" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.push({name: 'timeTask'})">返回</el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" :disabled="multipleSelection.length === 0">删除</el-button>
      <el-button size="small" type="warning" round icon="iconfont icon-7">导出</el-button>
    </div>

    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
        highlight-current-row
        :data="timeTaskLogs"
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
         
          prop="jobLogId"
          label="编号"
          width="80">
        </el-table-column>
        <el-table-column
          
          width="120"
          show-overflow-tooltip
          prop="jobName"
          label="类名">
        </el-table-column>
        <el-table-column
          width="150"
          show-overflow-tooltip
          prop="jobGroup"
          label="任务名">
        </el-table-column>
        <el-table-column
          width="160"
          show-overflow-tooltip
          prop="methodName"
          label="方法名">
        </el-table-column>
        <el-table-column
         
          show-overflow-tooltip
          prop="methodParams"
          width="100"
          label="方法参数">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="jobMessage"
          label="日志信息">
          <template slot-scope="scope">
            <div v-if="scope.row.status==0">{{scope.row.jobMessage}}</div>
            <div v-if="scope.row.status==1" type="danger">{{scope.row.exceptionInfo}}</div>
             
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="status"
          label="状态"
          width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status==0">成功</el-tag>
            <el-tag v-if="scope.row.status==1" type="danger">失败</el-tag>
             
          </template>
        </el-table-column>
        <el-table-column
          sortable
          width="250"
          show-overflow-tooltip
          prop="createTime"
          label="创建时间">
        </el-table-column>
      </el-table>
      
    </div>
    <el-pagination
        style="text-align: right;"
        @current-change="handleCurrentChange2"
        :current-page.sync="searchData2.pageNum"
        :page-size="searchData2.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData2.total">
      </el-pagination>

  </div>
</template>

<script>
  export default {
    name: 'TimeTaskLog',
    data() {
      /*let validateJobName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_job', 'job_name', this.form.jobName, 0);
      };
      let validateJobName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_job', 'job_name', this.rowData.jobName, 1, this.rowData.jobId);
      };*/
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,
        },
        searchData2:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWord: '',
        },
        oneSearchBarHeight: '50px', // 收起高度(50px/行)
        autoSearchBarHeight: '50px', // 展开高度(50px/行)
        searchBarHeight: '50px',
        // timeTasks: [],
        timeTaskLogs: [],
        multipleSelection: [],

        // dialogNewVisible: false,
        // dialogUpdateVisible: false,
        // isMoreBtnClassName: false,

        /*form: {
          jobName: '',
          jobGroup: '',
          methodName: '',
          methodParams: '',
          cronExpression: '0/10 * * * * ?',
          misfirePolicy: '1',
          concurrent: '2',
          status: '1',
          remark: '',
        },
        rules:{
          jobName: [
            { required: true, message: '请输入任务名称', trigger: 'blur' },
            { validator: validateJobName, trigger: 'blur'  }
          ],
          jobGroup: [
            { required: true, message: '请输入任务组名', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '请输入方法名称', trigger: 'blur' }
          ],
          methodParams: [
            { required: true, message: '请输入方法参数', trigger: 'blur' }
          ],
          cronExpression: [
            { required: true, message: '请输入cron表达式', trigger: 'blur' }
          ],
          misfirePolicy: [
            { required: true, message: '请选择执行策略', trigger: 'blur' }
          ],
          concurrent: [
            { required: true, message: '请选择并发执行', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '请选择状态', trigger: 'blur' }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          jobName: '',
          jobGroup: '',
          methodName: '',
          methodParams: '',
          cronExpression: '0/10 * * * * ?',
          misfirePolicy: '1',
          concurrent: '2',
          status: '1',
          remark: '',
        },
        rules2:{
          jobName: [
            { required: true, message: '请输入任务名称', trigger: 'blur' },
            { validator: validateJobName2, trigger: 'blur'  }
          ],
          jobGroup: [
            { required: true, message: '请输入任务组名', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '请输入方法名称', trigger: 'blur' }
          ],
          methodParams: [
            { required: true, message: '请输入方法参数', trigger: 'blur' }
          ],
          cronExpression: [
            { required: true, message: '请输入cron表达式', trigger: 'blur' }
          ],
          misfirePolicy: [
            { required: true, message: '请选择执行策略', trigger: 'blur' }
          ],
          concurrent: [
            { required: true, message: '请选择并发执行', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '请选择状态', trigger: 'blur' }
          ],
        },*/

      }
    },
    methods: {

      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getTimeTaskLogData()
      },
      clearSearchData2(){
        this.searchData2.keyWord = '';
      },
      getTimeTaskLogData(){
        this.loading.masterTable = true;
        IOT.getServerData('/monitor/jobLog/list','get',this.searchData2,(ret) => {
          this.loading.masterTable = false;
          if (ret.code === 200) {
            this.timeTaskLogs = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      /*clearSearchData(){
        this.searchData.keyWords = '';
      },
      getTimeTaskData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/monitor/job/list','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.timeTasks = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/monitor/job/add','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getTimeTaskData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form')
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/monitor/job/changeStates','post',this.rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getTimeTaskData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData')
            this.dialogUpdateVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row){
        this.rowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form');
        this.dialogNewVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData');
        this.dialogUpdateVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },*/

    },
    created() {
      this.getTimeTaskLogData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .timeTaskLog {

  }
</style>
