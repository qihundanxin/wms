<template>
  <div class="timeTask">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.jobGroup" size="mini" placeholder="名称" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
      <el-button size="small" type="success" round icon="iconfont icon-bianji" @click="showUpdateModal(multipleSelection[0])" :disabled="multipleSelection.length === 0 || multipleSelection.length > 1">修改</el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="deleteTimeTask(multipleSelection)" :disabled="multipleSelection.length === 0">删除</el-button>
      <el-button size="small" type="warning" round icon="iconfont icon-7" disabled>导出</el-button>
      <el-button size="small" type="success" round icon="iconfont icon-zhihang1" @click="runTimeTask(multipleSelection[0].jobId)" :disabled="multipleSelection.length === 0 || multipleSelection.length > 1">执行</el-button>
      <el-button size="small" type="info" round icon="iconfont icon-rizhi" @click="$router.push({name: 'timeTaskLog'})">日志</el-button>
    </div>

    <el-dialog title="新增消息" :visible.sync="dialogNewVisible" :before-close="handleClose" width="700px" style="margin-top: -10vh;">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="中文名" prop="jobGroup" :label-width="formLabelWidth">
          <el-input v-model="form.jobGroup" placeholder="请输入中文名" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="类名" prop="jobName" :label-width="formLabelWidth">
          <el-input v-model="form.jobName" placeholder="类名" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName" :label-width="formLabelWidth">
          <el-input v-model="form.methodName" placeholder="请输入方法名称" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="方法参数"  :label-width="formLabelWidth">
          <el-input v-model="form.methodParams" placeholder="请输入名称" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression" :label-width="formLabelWidth">
          <el-input v-model="form.cronExpression" placeholder="请输入名称" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="执行策略" prop="misfirePolicy" :label-width="formLabelWidth">
          <el-radio-group v-model="form.misfirePolicy">
            <el-radio label="1">立即执行</el-radio>
            <el-radio label="2">执行一次</el-radio>
            <el-radio label="3">放弃执行</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="并发执行" prop="concurrent" :label-width="formLabelWidth">
          <el-radio-group v-model="form.concurrent">
            <el-radio label="0">允许</el-radio>
            <el-radio label="1">禁止</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">暂停</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入内容" style="width: 90%"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改消息" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="700px" style="margin-top: -10vh;">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
       
        <el-form-item label="中文名" prop="jobGroup" :label-width="formLabelWidth">
          <el-input v-model="rowData.jobGroup" placeholder="请输入中文名" style="width: 90%"></el-input>
        </el-form-item>
         <el-form-item label="类名" prop="jobName" :label-width="formLabelWidth">
          <el-input v-model="rowData.jobName" placeholder="类名" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName" :label-width="formLabelWidth">
          <el-input v-model="rowData.methodName" placeholder="请输入方法名称" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="方法参数" prop="methodParams" :label-width="formLabelWidth">
          <el-input v-model="rowData.methodParams" placeholder="请输入方法参数" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression" :label-width="formLabelWidth">
          <el-input v-model="rowData.cronExpression" placeholder="请输入cron表达式" style="width: 90%"></el-input>
        </el-form-item>
        <el-form-item label="执行策略" prop="misfirePolicy" :label-width="formLabelWidth">
          <el-radio-group v-model="rowData.misfirePolicy">
            <el-radio label="1">立即执行</el-radio>
            <el-radio label="2">执行一次</el-radio>
            <el-radio label="3">放弃执行</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="并发执行" prop="concurrent" :label-width="formLabelWidth">
          <el-radio-group v-model="rowData.concurrent">
            <el-radio label="0">允许</el-radio>
            <el-radio label="1">禁止</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
          <el-radio-group v-model="rowData.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">暂停</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="rowData.remark" placeholder="请输入内容" style="width: 90%"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        v-loading="loading.masterTable"
        :data="timeTasks"
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="jobId"
          label="ID"
          min-width="50">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="jobGroup"
           min-width="150"
          label="名称">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="jobName"
           min-width="100"
          label="类名">
        </el-table-column>
        
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="methodName"
           min-width="150"
          label="方法名称">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="methodParams"
          min-width="100"
          label="方法参数">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="cronExpression"
          min-width="120"
          label="执行表达式">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="status"
          label="状态"
          min-width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              @change="changeStatusTimeTask(scope.row.jobId, scope.row.status)"
              active-value="0"
              inactive-value="1">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="createTime"
          min-width="160"
          label="创建时间">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          min-width="60">
          <template slot-scope="scope">
            <el-button @click="runTimeTask(scope.row.jobId)" icon="iconfont icon-zhihang1" type="text"     size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="text-align: right;"
        class="pagination-bottom"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'TimeTask',
    data() {
      let validateJobName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_job', 'job_name', this.form.jobName, 0);
      };
      let validateJobName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_job', 'job_name', this.rowData.jobName, 1, this.rowData.jobId);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 20,
          total: null,
          jobGroup: '',
        },
        searchData2:{
          pageNum: 1,
          pageSize: 20,
          total: null,
          jobGroup: '',
        },
        oneSearchBarHeight: '50px', // 收起高度(50px/行)
        autoSearchBarHeight: '50px', // 展开高度(50px/行)
        searchBarHeight: '50px',
        timeTasks: [],
        timeTaskLogs: [],
        multipleSelection: [],
        loading:{
          masterTable:true,

        },
        dialogNewVisible: false,
        dialogUpdateVisible: false,
        isMoreBtnClassName: false,

        form: {
          jobName: '',
          jobGroup: '',
          methodName: '',
          methodParams: '',
          cronExpression: '0/10 * * * * ?',
          misfirePolicy: '1',
          concurrent: '1',
          status: '1',
          remark: '',
        },
        rules:{
          jobName: [
            { required: true, message: '请输入类名', trigger: 'blur' },
            
          ],
          jobGroup: [
            { required: true, message: '请输入中文名', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '请输入方法名称', trigger: 'blur' }
          ],
          // methodParams: [
          //   { required: true, message: '请输入方法参数', trigger: 'blur' }
          // ],
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
          ],
          jobGroup: [
            { required: true, message: '请输入任务组名', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '请输入方法名称', trigger: 'blur' }
          ],
          /*methodParams: [
            { required: true, message: '请输入方法参数', trigger: 'blur' }
          ],*/
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

      }
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getTimeTaskData()
      },
      clearSearchData(){
        this.searchData.jobGroup = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getTimeTaskData();
      },
      getTimeTaskData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/monitor/job/list','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.timeTasks = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;

          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getTimeTaskLogData(){
        IOT.getServerData('/monitor/jobLog/list','get',this.searchData2,(ret) => {
          if (ret.code === 200) {
            this.timeTaskLogs = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
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
            IOT.getServerData('/monitor/job/edit','post',this.rowData,(ret) => {
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
      },
      /*isDelete(id){
          this.$confirm('确认删除？')
              .then(_ => {
                  this.deleteTimeTask(id)
              })
              .catch(_ => {});
      },*/
      deleteTimeTask(selects){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        let ids = {};
        for(let i in selects){
          ids[i] = selects[i].jobId
        }
        // ids = PF.JSON(ids);
        IOT.getServerData('/monitor/job/remove','get',{ids: ids},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getTimeTaskData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      showLogBox(){
        document.querySelector('.box1').style.display = 'none';
        document.querySelector('.box2').style.display = 'block';
        this.getTimeTaskLogData();
      },
      runTimeTask(id){
        IOT.getServerData('/monitor/job/run','post',{jobId: id},(ret) => {
          if (ret.code === 200) {
            IOT.tips('运行成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeStatusTimeTask(id, status){
        IOT.getServerData('/monitor/job/changeStatus','post',{jobId: id,status: status},(ret) => {
          if (ret.code === 200) {
            this.getTimeTaskData(); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        
      },
    },
    created() {
      this.getTimeTaskData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .timeTask {

  }
</style>
