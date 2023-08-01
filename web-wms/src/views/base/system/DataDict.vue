<template>
  <div class="dataDict clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getDataDictData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/电话" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增数据" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="编码" prop="dataDictCode" :label-width="formLabelWidth">
          <el-input v-model="form.dataDictCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="dataDictName" :label-width="formLabelWidth">
          <el-input v-model="form.dataDictName" placeholder="请输入名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改数据" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="编码" prop="dataDictCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.dataDictCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="dataDictName" :label-width="formLabelWidth">
          <el-input v-model="rowData.dataDictName" placeholder="请输入名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为数据 '+detailForm.dataDictMasterName+' 新增详情'" :visible.sync="dialogNewDetailVisible" :before-close="handleCloseDetail" width="500px">
      <el-form :model="detailForm" :rules="rulesDetail" ref="detailForm">
        <el-form-item label="内容" prop="dictValue" :label-width="formLabelWidth">
          <el-input v-model="detailForm.dictValue" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="key" prop="dictKey" :label-width="formLabelWidth">
          <el-input v-model="detailForm.dictKey" placeholder="请输入key"></el-input>
        </el-form-item>
        <el-form-item label="icon" prop="iconClass" :label-width="formLabelWidth">
          <el-input v-model="detailForm.iconClass" placeholder="请输入icon"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailForm');dialogNewDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitDetailForm('detailForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为数据 '+detailForm.dataDictMasterName+' 修改详情'" :visible.sync="dialogUpdateDetailVisible" :before-close="handleCloseDetail2" width="500px">
      <el-form :model="detailRowData" :rules="rulesDetail2" ref="detailRowData">
        <el-form-item label="内容" prop="dictValue" :label-width="formLabelWidth">
          <el-input v-model="detailRowData.dictValue" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="key" prop="dictKey" :label-width="formLabelWidth">
          <el-input v-model="detailRowData.dictKey" placeholder="请输入key"></el-input>
        </el-form-item>
        <el-form-item label="icon" prop="iconClass" :label-width="formLabelWidth">
          <el-input v-model="detailRowData.iconClass" placeholder="请输入icon"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailRowData');dialogUpdateDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateDetailForm('detailRowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              @current-change="getLookRowData"
              :data="dataDicts"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="dataDictName"
                label="名称">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.dataDictMasterId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-left-bottom"
              @current-change="handleCurrentChange"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight">
      <div class="organizationRightTitle">数据信息</div>
      <p class="MasterBox">
        <span><b>数据编码：</b>{{lookRowData.dataDictCode}}</span>
        <span><b>数据名称：</b>{{lookRowData.dataDictName}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>
      <div class="headerBtns">
        <el-button @click="dialogNewDetailVisible = true;showDetailModal()" size="small" plain icon="iconfont icon-xinzeng">新增详情</el-button>
      </div>
      <div class="tableBox">
        <el-table
                v-loading="loading.masterTable"
                highlight-current-row
                :data="dataDictDetails"
                style="width: 100%">
          <el-table-column
                  fixed="left"
                  type="index"
                  width="50">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="dictValue"
            label="内容">
          </el-table-column>
          <!--<el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="dictKey"
                  label="key">
          </el-table-column>-->
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="iconClass"
                  label="icon">
          </el-table-column>
          <el-table-column
                  fixed="right"
                  label="操作"
                  width="110">
            <template slot-scope="scope">
                <el-button @click="showUpdateDetailModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
                <el-button @click="isDeleteDetail(scope.row.dataDictDetailId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
                style="text-align: right;"
                class="pagination-bottom"
                @current-change="handleCurrentChange2"
                :current-page.sync="searchData2.pageNum"
                :page-size="searchData2.pageSize"
                layout="total, prev, pager, next, jumper"
                :total="searchData2.total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'DataDict',
    data(){
      let validateDataDictCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'data_dict_master', 'data_dict_code', this.form.dataDictCode, 0);
      };
      let validateDataDictCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'data_dict_master', 'data_dict_code', this.rowData.dataDictCode, 1, this.rowData.dataDictMasterId);
      };
      let validateDataDictName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'data_dict_master', 'data_dict_name', this.form.dataDictName, 0);
      };
      let validateDataDictName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'data_dict_master', 'data_dict_name', this.rowData.dataDictName, 1, this.rowData.dataDictMasterId);
      };
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
        searchData2:{
          pageNum: 1,
          pageSize: 5,
          total: null,
        },

        dataDicts: [],
        dataDictDetails: [],
        lookRowData: {},

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogNewDetailVisible: false,
        dialogUpdateDetailVisible: false,

        form: {
          dataDictCode: '',
          dataDictName: '',
        },
        rules:{
          dataDictCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateDataDictCode, trigger: 'blur'  }
          ],
          dataDictName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateDataDictName, trigger: 'blur'  }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          dataDictCode: '',
          dataDictName: '',
        },
        rules2:{
          dataDictCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateDataDictCode2, trigger: 'blur'  }
          ],
          dataDictName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateDataDictName2, trigger: 'blur'  }
          ],
        },
        detailForm: {
          dataDictMasterId: null,
          dataDictMasterName: '',
          dataDictCode: '',
          dataDictName: '',
          dictKey: '',
          dictValue: '',
          iconClass: '',
        },
        rulesDetail:{
          dictValue: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
          dictKey: [
            { required: true, message: '请输入键', trigger: 'blur' }
          ],
        },
        detailRowData: {
          dataDictMasterId: null,
          dataDictMasterName: '',
          dataDictCode: '',
          dataDictName: '',
          dictKey: '',
          dictValue: '',
          iconClass: '',
        },
        rulesDetail2:{
          dictValue: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
          dictKey: [
            { required: true, message: '请输入键', trigger: 'blur' }
          ],
        },
      }
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getDataDictData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getDataDictDetailData(this.lookRowData.dataDictCode)
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getDataDictData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/data/dict/masters/list','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.dataDicts = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getDataDictDetailData(code){
        this.searchData2.dataDictCode = code;
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/data/dict/details/list','get',this.searchData2,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.dataDictDetails = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        this.lookRowData = JSON.parse(JSON.stringify(row));
        this.getDataDictDetailData(this.lookRowData.dataDictCode)
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/data/dict/masters/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDataDictData()
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
            IOT.getServerData('/data/dict/masters/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDataDictData()
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
      handleCloseDetail(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('detailRowData');
        this.dialogNewDetailVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseDetail2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('detailRowData');
        this.dialogUpdateDetailVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      isDelete(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteDataDict(id)
          })
          .catch(_ => {});
      },
      isDeleteDetail(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteDetail(id)
          })
          .catch(_ => {});
      },
      deleteDataDict(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/data/dict/masters/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getDataDictData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteDetail(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/data/dict/details/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getDataDictDetailData(that.lookRowData.dataDictCode)
            });
          }else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showDetailModal(){
        this.detailForm.dataDictMasterId = this.lookRowData.dataDictMasterId;
        this.detailForm.dataDictMasterName = this.lookRowData.dataDictName;
        this.detailForm.dataDictCode = this.lookRowData.dataDictCode;
        this.detailForm.dataDictName = this.lookRowData.dataDictName;
      },
      commitDetailForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/data/dict/details/insert','post',this.detailForm,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDataDictDetailData(that.lookRowData.dataDictCode)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('detailForm')
            this.dialogNewDetailVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateDetailForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/data/dict/details/update','post',this.detailRowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDataDictDetailData(that.lookRowData.dataDictCode)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('detailRowData')
            this.dialogUpdateDetailVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateDetailModal(row){
        this.detailRowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateDetailVisible = true
      },
    },
    created() {
      this.getDataDictData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .dataDict{

  }
</style>
