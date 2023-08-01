<template>
  <div class="sysParam clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSysParamData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/电话" clearable></el-input>
        </div>
        <div>
          <span>所属模块</span>
          <el-select v-model="searchData.moduleCode" size="mini" filterable placeholder="请选择所属模块" clearable>
            <el-option
              v-for="item in sysSettings"
              :key="item.dictKey"
              :label="item.dictValue"
              :value="item.dictKey">
            </el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增参数" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="编码" prop="paramCode" :label-width="formLabelWidth">
          <el-input v-model="form.paramCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="paramName" :label-width="formLabelWidth">
          <el-input v-model="form.paramName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 70%;">
            <el-option
                    v-for="item in sysParamTypes"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属模块" prop="moduleCode" :label-width="formLabelWidth">
          <el-select v-model="form.moduleCode" placeholder="请选择所属模块" style="width: 70%;">
            <el-option
                    v-for="item in sysSettings"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值" prop="value" :label-width="formLabelWidth">
          <el-input v-model="form.value" placeholder="请输入值"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="form.memo" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改参数" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="编码" prop="paramCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.paramCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="paramName" :label-width="formLabelWidth">
          <el-input v-model="rowData.paramName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
          <el-select v-model="rowData.type" placeholder="请选择类型" style="width: 70%;">
            <el-option
                    v-for="item in sysParamTypes"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属模块" prop="moduleCode" :label-width="formLabelWidth">
          <el-select v-model="rowData.moduleCode" placeholder="请选择所属模块" style="width: 70%;">
            <el-option
                    v-for="item in sysSettings"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值" prop="value" :label-width="formLabelWidth">
          <el-input v-model="rowData.value" placeholder="请输入值"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="rowData.memo" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为参数 '+detailForm.paramName+' 新增详情'" :visible.sync="dialogNewDetailVisible" :before-close="handleCloseDetail" width="500px">
      <el-form :model="detailForm" :rules="rulesDetail" ref="detailForm">
        <el-form-item label="名称" prop="detailName" :label-width="formLabelWidth">
          <el-input v-model="detailForm.detailName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="detailValue" :label-width="formLabelWidth">
          <el-input v-model="detailForm.detailValue" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailForm');dialogNewDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitDetailForm('detailForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为参数 '+detailForm.paramName+' 修改详情'" :visible.sync="dialogUpdateDetailVisible" :before-close="handleCloseDetail2" width="500px">
      <el-form :model="detailRowData" :rules="rulesDetail2" ref="detailRowData">
        <el-form-item label="名称" prop="detailName" :label-width="formLabelWidth">
          <el-input v-model="detailRowData.detailName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="detailValue" :label-width="formLabelWidth">
          <el-input v-model="detailRowData.detailValue" placeholder="请输入内容"></el-input>
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
              :data="sysParams"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="paramName"
                label="名称">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.sysParamId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
      <div class="organizationRightTitle">参数信息</div>
      <p class="MasterBox">
        <span><b>参数编码：</b>{{lookRowData.paramCode}}</span>
        <span v-for="(item, index) in sysParamTypes" :key="index" v-if="item.dictKey == lookRowData.type"><b>类型：</b>{{item.dictValue}}</span>
      </p>
      <p class="MasterBox">
        <span><b>参数名称：</b>{{lookRowData.paramName}}</span>
        <span v-for="(item, index) in sysSettings" :key="index" v-if="item.dictKey == lookRowData.moduleCode"><b>所属模块：</b>{{item.dictValue}}</span>
      </p>
      <p class="MasterBox">
        <span><b>内容：</b>{{lookRowData.value}}</span>
        <span><b>备注信息：</b>{{lookRowData.memo}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>
      <div class="headerBtns">
        <el-button @click="dialogNewDetailVisible = true;showDetailModal()" size="small" plain icon="iconfont icon-xinzeng">新增详情</el-button>
      </div>
      <div class="tableBox">
        <el-table
                v-loading="loading.masterTable"
                highlight-current-row
                :data="sysParamDetails"
                style="width: 100%">
          <el-table-column
                  fixed="left"
                  type="index"
                  width="50">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="detailName"
                  label="名称">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="detailValue"
                  label="内容">
          </el-table-column>
          <el-table-column
                  fixed="right"
                  label="操作"
                  width="110">
            <template slot-scope="scope">
                <el-button @click="showUpdateDetailModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
                <el-button @click="isDeleteDetail(scope.row)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
  import {mapState} from "vuex";

  export default{
    name: 'SysParam',
    data(){
      let validateParamCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_param', 'param_code', this.form.paramCode, 0);
      };
      let validateParamCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_param', 'param_code', this.rowData.paramCode, 1, this.rowData.sysParamId);
      };
      let validateParamName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_param', 'param_name', this.form.paramName, 0);
      };
      let validateParamName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'sys_param', 'param_name', this.rowData.paramName, 1, this.rowData.sysParamId);
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
          moduleCode: '',
        },
        searchData2:{
          pageNum: 1,
          pageSize: 5,
          total: null,
        },

        sysParams: [],
        sysSettings: [],
        sysParamDetails: [],
        lookRowData: {},
        dataDictSysSettings: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogNewDetailVisible: false,
        dialogUpdateDetailVisible: false,

        form: {
          paramCode: '',
          paramName: '',
          type: null,
          value: '',
          moduleCode: '',
          memo: '',
        },
        rules:{
          paramCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateParamCode, trigger: 'blur'  }
          ],
          paramName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateParamName, trigger: 'blur'  }
          ],
          type: [
            { required: true, message: '请选择类型', trigger: 'blur' }
          ],
          moduleCode: [
            { required: true, message: '请选择所属模块', trigger: 'blur' }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          paramCode: '',
          paramName: '',
          type: null,
          value: '',
          moduleCode: '',
          memo: '',
        },
        rules2:{
          paramCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateParamCode2, trigger: 'blur'  }
          ],
          paramName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateParamName2, trigger: 'blur'  }
          ],
          type: [
            { required: true, message: '请选择类型', trigger: 'blur' }
          ],
          moduleCode: [
            { required: true, message: '请选择所属模块', trigger: 'blur' }
          ],
        },
        detailForm: {
          sysParamId: null,
          paramName: '',
          detailName: '',
          detailValue: '',
        },
        rulesDetail:{
          detailName: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          detailValue: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
        },
        detailRowData: {
          sysParamId: null,
          paramName: '',
          detailName: '',
          detailValue: '',
        },
        rulesDetail2:{
          detailName: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          detailValue: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ],
        },
      }
    },
    computed: {
      ...mapState([
        "sysParamTypes"
      ])
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getSysParamData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getSysParamDetailData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.moduleCode = '';
      },
      getSysParamData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/sys/params','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.sysParams = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSysParamDetailData(id){
        this.searchData2.sysParamId = id
        let that = this;
        that.loading.masterTable = true;
        let searchData2 = PF.JSON(this.searchData2);
        IOT.getServerData('/sys/param/details/list','get',searchData2,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.sysParamDetails = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        this.lookRowData = JSON.parse(JSON.stringify(row))
        this.getSysParamDetailData(this.lookRowData.sysParamId)
      },
      commitForm(formName) {
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/sys/params/save','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success');
                this.getSysParamData()
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
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            delete this.rowData.details;
            IOT.getServerData('/sys/params/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success');
                this.getSysParamData()
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
        this.resetForm('detailForm');
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
            this.deleteSysParam(id)
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
      deleteSysParam(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/sys/params/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getSysParamData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteDetail(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/sys/param/details/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getSysParamDetailData(that.lookRowData.sysParamId)
            });
          }else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showDetailModal(){
        this.detailForm.sysParamId = this.lookRowData.sysParamId;
        this.detailForm.paramName = this.lookRowData.paramName;
      },
      commitDetailForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/sys/param/details/save','post',this.detailForm,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getSysParamDetailData(that.lookRowData.sysParamId)
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
            IOT.getServerData('/sys/param/details/update','post',this.detailRowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getSysParamDetailData(that.lookRowData.sysParamId)
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
      this.getSysParamData();
      PF.getDataDictUdfData('sysSetting', (rows) => {
          this.sysSettings = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .sysParam{

  }
</style>
