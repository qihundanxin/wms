<template>
  <div class="department">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getDepartmentData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="名称/备注/编码" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="form.parentId = null;dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增部门" :visible.sync="dialogNewVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="父级部门" prop="parentId" :label-width="formLabelWidth">
          <el-select v-model="form.parentId" disabled placeholder="无父级部门" style="width: 70%;">
            <el-option
                    v-for="department in departments"
                    :key="department.deptId"
                    :label="department.deptName"
                    :value="department.deptId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="deptCode" :label-width="formLabelWidth">
          <el-input v-model="form.deptCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="deptName" :label-width="formLabelWidth">
          <el-input v-model="form.deptName" placeholder="请输入名称"></el-input>
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
    <el-dialog title="修改部门" :visible.sync="dialogUpdateVisible" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="父级部门" prop="parentId" :label-width="formLabelWidth">
          <el-select v-model="rowData.parentId" disabled placeholder="无父级部门" style="width: 70%;">
            <el-option
                    v-for="department in departments"
                    :key="department.deptId"
                    :label="department.deptName"
                    :value="department.deptId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="deptCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.deptCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="deptName" :label-width="formLabelWidth">
          <el-input v-model="rowData.deptName" placeholder="请输入名称"></el-input>
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
    <el-dialog title="选择新增级别" :visible.sync="dialogNew2Visible" width="300px" style="text-align: center;">
      <!--<span>选择新增级别</span>-->
      <el-button type="info" @click="form.parentId = new2Info.parentId;dialogNew2Visible = false;dialogNewVisible = true">同 级</el-button>
      <el-button type="primary" @click="form.parentId = new2Info.deptId;dialogNew2Visible = false;dialogNewVisible = true">下 级</el-button>
      <!--<div slot="footer" class="dialog-footer">
      </div>-->
    </el-dialog>

    <div class="tableBox">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              max-height="600"
              :data="departmentTree"
              style="width: 100%"
              row-key="id"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="deptName"
                label="名称"
                width="260">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="deptCode"
                label="编码"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="备注">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="150">
          <template slot-scope="scope">
              <el-button @click="new2Info = scope.row;dialogNew2Visible = true" icon="iconfont icon-xinzeng" type="info" circle plain size="small"></el-button>
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.deptId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--<el-pagination
              style="text-align: right;"
              @current-change="handleCurrentChange"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>-->
    </div>
  </div>
</template>

<script>
  export default{
    name: 'Department',
    data(){
      let validateDeptCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'dept', 'dept_code', this.form.deptCode, 0);
      };
      let validateDeptCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'dept', 'dept_code', this.rowData.deptCode, 1, this.rowData.deptId);
      };
      let validateDeptName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'dept', 'dept_name', this.form.deptName, 0);
      };
      let validateDeptName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'dept', 'dept_name', this.rowData.deptName, 1, this.rowData.deptId);
      };
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData:{
          // pageNum: 1,
          // pageSize: 10,
          // total: null,
          keyWords: '',
        },

        departments: [],
        departmentTree: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        dialogVisible: false,

        dialogNew2Visible: false,

        form: {
          parentId: null,
          deptCode: '',
          deptName: '',
          memo: '',
        },
        rules:{
          deptCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateDeptCode, trigger: 'blur'  }
          ],
          deptName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateDeptName, trigger: 'blur'  }
          ]
        },
        formLabelWidth: '120px',

        rowData: {
          parentId: null,
          deptCode: '',
          deptName: '',
          memo: '',
        },
        rules2:{
          deptCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateDeptCode2, trigger: 'blur'  }
          ],
          deptName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateDeptName2, trigger: 'blur'  }
          ]
        },


        new2Info: {}
      }
    },
    watch: {
      radioS(val){
        if(this.isShowAddressSelectBox){
          this.treeDataC = this.treeDataS[val].children;
          this.form.province = this.treeDataS[val].name;
          if(this.treeDataC === undefined){
            this.form.city = '-';
            this.form.area = '-';
            this.isShowAddressSelectBox = false
          }else{
            this.activeAddressName = 'C'
          }
        }
        if(this.isShowAddressSelectBox2){
          this.treeDataC = this.treeDataS[val].children;
          this.rowData.province = this.treeDataS[val].name;
          if(this.treeDataC === undefined){
            this.rowData.city = '-';
            this.rowData.area = '-';
            this.isShowAddressSelectBox2 = false
          }else{
            this.activeAddressName = 'C'
          }
        }
      },
      radioC(val){
        if(this.isShowAddressSelectBox){
          this.treeDataQ = this.treeDataC[val].children;
          this.form.city = this.treeDataC[val].name;
          if(this.treeDataQ === undefined){
            this.form.area = '-';
            this.isShowAddressSelectBox = false
          }else{
            this.activeAddressName = 'Q'
          }
        }
        if(this.isShowAddressSelectBox2){
          this.treeDataQ = this.treeDataC[val].children;
          this.rowData.city = this.treeDataC[val].name;
          if(this.treeDataQ === undefined){
            this.rowData.area = '-';
            this.isShowAddressSelectBox2 = false
          }else{
            this.activeAddressName = 'Q'
          }
        }
      },
      radioQ(val){
        if(this.isShowAddressSelectBox){
          this.form.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox = false
        }
        if(this.isShowAddressSelectBox2){
          this.rowData.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox2 = false
        }
      }
    },
    methods: {

      /*handleCurrentChange(val) {
          this.searchData.pageNum = val;
          this.getDepartmentData()
      },*/
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getDepartmentData(){
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/depts/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].id = list[i].deptId;
            }
            this.departments = list;
            this.departmentTree = PF.toTree(list);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitForm(formName) {
        let that =this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/depts/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDepartmentData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form');
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateForm(formName) {
        let that =this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/depts/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getDepartmentData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData');
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
      /*handleClose(done) {
          this.$confirm('确认关闭？')
              .then(_ => {
                  this.resetForm('form');
                  this.dialogNewVisible = false;
                  done();
              })
              .catch(_ => {});
      },
      handleClose2(done) {
          this.$confirm('确认关闭？')
              .then(_ => {
                  this.resetForm('rowData');
                  this.dialogUpdateVisible = false;
                  done();
              })
              .catch(_ => {});
      },*/
      isDelete(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteDepartment(id)
          })
          .catch(_ => {});
      },
      deleteDepartment(id){
        let that =this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/depts/delete','get',{deptId: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getDepartmentData()
            });
          }else if(ret.code === 100020){
            IOT.tips(ret.message || '该部门已绑定,不可删除！', 'error');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

    },
    created() {
      this.getDepartmentData()
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .department{

  }
</style>
