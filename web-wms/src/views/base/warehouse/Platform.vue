<template>
  <div class="platform">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPlatformData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>月台编码</span>
          <el-input v-model="searchData.platformCode" size="mini" placeholder="月台编码" clearable></el-input>
        </div>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增月台" :visible.sync="dialogNewVisible" :before-close="handleClose" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
          <span>{{form.wareName}}</span>
        </el-form-item>
        <el-form-item label="月台编码" prop="platformCode" :label-width="formLabelWidth">
          <el-input v-model="form.platformCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="上午时段" prop="startTimeOne" :label-width="formLabelWidth">
          <el-time-picker
                  v-model="form.startTimeOne"
                  value-format="HH:mm:ss"
                  placeholder="开始时间"
                  style="width: 40%;">
          </el-time-picker>
          至
          <el-time-picker
                  v-model="form.endTimeOne"
                  value-format="HH:mm:ss"
                  placeholder="结束时间"
                  style="width: 40%;">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="下午时段" prop="startTimeTwo" :label-width="formLabelWidth">
          <el-time-picker
                  v-model="form.startTimeTwo"
                  value-format="HH:mm:ss"
                  placeholder="开始时间"
                  style="width: 40%;">
          </el-time-picker>
          至
          <el-time-picker
                  v-model="form.endTimeTwo"
                  value-format="HH:mm:ss"
                  placeholder="结束时间"
                  style="width: 40%;">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="标志" :label-width="formLabelWidth">
          <el-checkbox v-model="form.receiving">收货标志</el-checkbox>
          <el-checkbox v-model="form.delivery">发货标志</el-checkbox>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');clearForm();dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改月台" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="700px"  :close-on-click-modal="false">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
          <span>{{rowData.wareName}}</span>
        </el-form-item>
        <el-form-item label="月台编码" prop="platformCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.platformCode" placeholder="请输入月编码" maxlength="20" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="上午时段" prop="startTimeOne" :label-width="formLabelWidth">
          <el-time-picker
                  v-model="rowData.startTimeOne"
                  value-format="HH:mm:ss"
                  placeholder="开始时间"
                  style="width: 40%;">
          </el-time-picker>
          至
          <el-time-picker
                  v-model="rowData.endTimeOne"
                  value-format="HH:mm:ss"
                  placeholder="结束时间"
                  style="width: 40%;">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="下午时段" prop="startTimeTwo" :label-width="formLabelWidth">
          <el-time-picker
                  v-model="rowData.startTimeTwo"
                  value-format="HH:mm:ss"
                  placeholder="开始时间"
                  style="width: 40%;">
          </el-time-picker>
          至
          <el-time-picker
                  v-model="rowData.endTimeTwo"
                  value-format="HH:mm:ss"
                  placeholder="结束时间"
                  style="width: 40%;">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="标志" :label-width="formLabelWidth">
          <el-checkbox v-model="rowData.receiving">收货标志</el-checkbox>
          <el-checkbox v-model="rowData.delivery">发货标志</el-checkbox>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');clearForm();dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
              highlight-current-row
              :data="platforms"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="wareName"
                label="仓库">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="platformCode"
                label="月台编码">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="startTimeOne"
                label="上午时段">
          <template slot-scope="scope">
            <span>{{scope.row.startTimeOne}} - {{scope.row.endTimeOne}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="startTimeTwo"
                label="下午时段">
          <template slot-scope="scope">
            <span>{{scope.row.startTimeTwo}} - {{scope.row.endTimeTwo}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="receiving"
                label="收货标志">
          <template slot-scope="scope">
            <i v-show="scope.row.receiving" class="iconfont icon-xuanzhong1"></i>
            <i v-show="!scope.row.receiving" class="iconfont icon-weixuanzhong"></i>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="delivery"
                label="发货标志">
          <template slot-scope="scope">
            <i v-show="scope.row.delivery" class="iconfont icon-xuanzhong1"></i>
            <i v-show="!scope.row.delivery" class="iconfont icon-weixuanzhong"></i>
          </template>
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
  export default {
      name: 'Platform',
      data(){
          let validatePlatformCode = (rule, value, callback) => {
              PF.validator(rule, value, callback, 'platform', 'platform_code', this.form.platformCode, 0);
          };
          let validatePlatformCode2 = (rule, value, callback) => {
              PF.validator(rule, value, callback, 'platform', 'platform_code', this.rowData.platformCode, 1, this.rowData.id);
          };
          let validateDataOne = (rule, value, callback) => {
              let startTime = this.form.startTimeOne;
              let endTime = this.form.endTimeOne;
              if(startTime === '' || startTime === null){
                  callback(new Error('请选择开始时间(早)'));
              }else if(endTime === '' || endTime === null){
                  callback(new Error('请选择结束时间(早)'));
              }else if(startTime >= endTime){
                  callback(new Error('开始时间应该小于结束时间'));
              }else {
                  callback();
              }
          };
          let validateDataTwo = (rule, value, callback) => {
              let startTime = this.form.startTimeTwo;
              let endTime = this.form.endTimeTwo;
              if(startTime === '' || startTime === null){
                  callback(new Error('请选择开始时间(早)'));
              }else if(endTime === '' || endTime === null){
                  callback(new Error('请选择结束时间(早)'));
              }else if(startTime >= endTime){
                  callback(new Error('开始时间应该小于结束时间'));
              }else {
                  callback();
              }
          };
          let validateDataOne2 = (rule, value, callback) => {
              let startTime = this.rowData.startTimeOne;
              let endTime = this.rowData.endTimeOne;
              if(startTime === '' || startTime === null){
                  callback(new Error('请选择开始时间(早)'));
              }else if(endTime === '' || endTime === null){
                  callback(new Error('请选择结束时间(早)'));
              }else if(startTime >= endTime){
                  callback(new Error('开始时间应该小于结束时间'));
              }else {
                  callback();
              }
          };
          let validateDataTwo2 = (rule, value, callback) => {
              let startTime = this.rowData.startTimeTwo;
              let endTime = this.rowData.endTimeTwo;
              if(startTime === '' || startTime === null){
                  callback(new Error('请选择开始时间(早)'));
              }else if(endTime === '' || endTime === null){
                  callback(new Error('请选择结束时间(早)'));
              }else if(startTime >= endTime){
                  callback(new Error('开始时间应该小于结束时间'));
              }else {
                  callback();
              }
          };
          return {
              isMoreSearch: false,

              searchData:{
                  pageNum: 1,
                  pageSize: 15,
                  total: null,
                  keyWords: '',
                  platformCode:'',
                  wareId: PF.getLocal('wareId', 'number'),
              },

              platforms: [],
              wares: [],

              dialogNewVisible: false,
              dialogUpdateVisible: false,


              form: {
                  wareId: PF.getLocal('wareId', 'number'),
                  wareName: PF.getLocal('wareName'),
                  platformCode: '',
                  startTimeOne: '',
                  endTimeOne: '',
                  startTimeTwo: '',
                  endTimeTwo: '',
                  receiving: false,
                  delivery: false,
              },
              rules:{
                  wareId: [
                      { required: true, message: '请选择仓库', trigger: 'blur' }
                  ],
                  platformCode: [
                      { required: true, message: '请输入编码', trigger: 'blur' },
                      { validator: validatePlatformCode, trigger: 'blur' }
                  ],
                  startTimeOne: [
                      { required: true, message: '请选择开始时间(早)', trigger: 'blur' },
                      { validator: validateDataOne, trigger: 'blur' }
                  ],
                  startTimeTwo: [
                      { required: true, message: '请选择开始时间(晚)', trigger: 'blur' },
                      { validator: validateDataTwo, trigger: 'blur' }
                  ],
              },
              formLabelWidth: '120px',

              rowData: {
                  wareId: '',
                  platformCode: '',
                  startTimeOne: '',
                  endTimeOne: '',
                  startTimeTwo: '',
                  endTimeTwo: '',
                  receiving: false,
                  delivery: false,
              },
              rules2:{
                  wareId: [
                      { required: true, message: '请选择仓库', trigger: 'blur' }
                  ],
                  platformCode: [
                      { required: true, message: '请输入编码', trigger: 'blur' },
                      { validator: validatePlatformCode2, trigger: 'blur' }
                  ],
                  startTimeOne: [
                      { required: true, message: '请选择开始时间(早)', trigger: 'blur' },
                      { validator: validateDataOne2, trigger: 'blur' }
                  ],
                  startTimeTwo: [
                      { required: true, message: '请选择开始时间(晚)', trigger: 'blur' },
                      { validator: validateDataTwo2, trigger: 'blur' }
                  ],
              },
          }
      },
      methods: {

        handleSizeChange(val){
          this.searchData.pageSize = val;
          this.getPlatformData();
        },

          handleCurrentChange(val) {
              this.pageNum = val;
              this.getPlatformData()
          },
          clearSearchData(){
              this.searchData.keyWords = '';
              this.searchData.platformCode = '';
          },
          getPlatformData(){
              IOT.getServerData('/platforms/list','get',this.searchData,(ret) => {
                  if (ret.code === 200) {
                      this.platforms = ret.rows;
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
          clearForm(){
              this.form.endTimeOne = '';
              this.form.endTimeTwo = '';
              this.form.receiving = false;
              this.form.delivery = false;

              this.rowData.endTimeOne = '';
              this.rowData.endTimeTwo = '';
              this.rowData.receiving = false;
              this.rowData.delivery = false;
          },
          commitForm(formName) {
              let that = this;
              IOT.showOverlay('保存中，请稍等...');
              this.$refs[formName].validate((valid) => {
                  if (valid) {
                      IOT.getServerData('/platforms/insert','post',this.form,(ret) => {
                          IOT.hideOverlay();
                          if (ret.code === 200) {
                              IOT.tips('保存成功！', 'success', 1000 , () => {
                                  that.getPlatformData()
                              });
                          } else {
                              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                          }
                      });
                      this.resetForm('form');
                      this.clearForm();
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
                      IOT.getServerData('/platforms/update','post',this.rowData,(ret) => {
                          IOT.hideOverlay();

                          if (ret.code === 200) {
                              IOT.tips('保存成功！', 'success', 1000 , () => {
                                  that.getPlatformData()
                              });
                          } else {
                              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                          }
                      });
                      this.resetForm('rowData');
                      this.clearForm();
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
              this.clearForm();
              this.dialogNewVisible = false;
              //     done();
              // })
              // .catch(_ => {});
          },
          handleClose2(done) {
              // this.$confirm('确认关闭？')
              //     .then(_ => {
              this.resetForm('rowData');
              this.clearForm();
              this.dialogUpdateVisible = false;
              //     done();
              // })
              // .catch(_ => {});
          },
          isDelete(id){
              this.$confirm('确认删除？')
                  .then(_ => {
                      this.deletePlatform(id)
                  })
                  .catch(_ => {});
          },
          deletePlatform(id){
              let that = this;
              IOT.showOverlay('删除中，请稍等...');
              IOT.getServerData('/platforms/delete','get',{id: id},(ret) => {
                  IOT.hideOverlay();
                  if (ret.code === 200) {
                      IOT.tips('删除成功！', 'success', 1000 , () => {
                          that.getPlatformData()
                      });
                  } else {
                      IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
              });
          },
      },
      created() {
          this.getPlatformData();
          this.getWareData();
      },
      mounted() {
      }
  }
</script>

<style scoped lang="less">
  .platform{

  }
</style>
