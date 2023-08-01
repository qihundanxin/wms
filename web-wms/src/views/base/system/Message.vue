<template>
  <div class="message">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getMessageData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="名称" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增消息" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="名称" prop="messageName" :label-width="formLabelWidth">
          <el-input v-model="form.messageName" maxlength="8" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="memo" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.memo" maxlength="20" placeholder="请输入内容" style="width: 70%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改消息" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="名称" prop="messageName" :label-width="formLabelWidth">
          <el-input v-model="rowData.messageName" maxlength="8" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="memo" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="rowData.memo" maxlength="20" placeholder="请输入内容" style="width: 70%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              :data="messages"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="messageName"
                label="名称"
                width="260">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="内容">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.messageId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
  export default{
    name: 'Message',
    data(){
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
        },
        messages: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,


        form: {
          messageName: '',
          memo: '',
        },
        rules:{
          messageName: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          memo: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          messageName: '',
          memo: '',
        },
        rules2:{
          messageName: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          memo: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
        },
      }
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getMessageData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getMessageData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/messages/list','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.messages = ret.rows;
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
            IOT.getServerData('/messages/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getMessageData()
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
            IOT.getServerData('/messages/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getMessageData()
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
      isDelete(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteMessage(id)
          })
          .catch(_ => {});
      },
      deleteMessage(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/messages/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getMessageData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getMessageData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .message{

  }
</style>
