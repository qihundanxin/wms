<template>
  <div class="packData">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPackDataData" size="mini" icon="el-icon-search">查询</el-button>
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

    <el-dialog title="新增包材" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="名称" prop="packData" :label-width="formLabelWidth">
          <el-input v-model="form.packData" placeholder="请输入名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改包材" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="名称" prop="packData" :label-width="formLabelWidth">
          <el-input v-model="rowData.packData" placeholder="请输入名称"></el-input>
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
              :data="packDatas"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="packData"
                label="名称">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
  export default{
    name: 'PackData',
    data(){
      let validatePackData = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack', 'pack_data', this.form.packData, 0);
      };
      let validatePackData2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack', 'pack_data', this.rowData.packData, 1, this.rowData.id);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },
        packDatas: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,


        form: {
          packData: '',
        },
        rules:{
          packData: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validatePackData, trigger: 'blur'  }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          packData: '',
        },
        rules2:{
          packData: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validatePackData2, trigger: 'blur'  }
          ],
        },
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getPackDataData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getPackDataData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getPackDataData(){
        IOT.getServerData('/packs/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.packDatas = ret.rows;
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
            IOT.getServerData('/packs/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getPackDataData()
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
            IOT.getServerData('/packs/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getPackDataData()
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
            this.deletePackData(id)
          })
          .catch(_ => {});
      },
      deletePackData(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/packs/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getPackDataData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getPackDataData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .packData{

  }
</style>
