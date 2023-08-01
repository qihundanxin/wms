<template>
  <div class="soMergeTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSoMergeTacticData" size="mini" icon="el-icon-search">查询</el-button>
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
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
      </el-button>
    </div>

    <el-dialog title="新增策略" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px">
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="编码" prop="soMergeTacticCode" :label-width="formLabelWidth">
            <el-input v-model="form.soMergeTacticCode" placeholder="请输入编码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="soMergeTacticName" :label-width="formLabelWidth">
            <el-input v-model="form.soMergeTacticName" placeholder="请输入名称"></el-input>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">配置项</el-divider>
          </div>
          <el-form-item label="单据来源" prop="billSource" :label-width="formLabelWidth">
            <el-select v-model="form.billSource" placeholder="请选择">
              <el-option :key="0" label="不可合并" :value="0"></el-option>
              <el-option :key="1" label="强制合并" :value="1"></el-option>
              <el-option :key="2" label="提示" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发运单类型" prop="soType" :label-width="formLabelWidth">
            <el-select v-model="form.soType" placeholder="请选择">
              <el-option :key="0" label="不可合并" :value="0"></el-option>
              <el-option :key="1" label="强制合并" :value="1"></el-option>
              <el-option :key="2" label="提示" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">配置限制项 (勾选限制合并,不勾选不限制)</el-divider>
          </div>
        </el-form>
        <div style="padding-left: 50px;">
          <el-checkbox v-model="form.deliveryTime" :true-label="1" :false-label="0">交货时间</el-checkbox>
          <el-checkbox v-model="form.expectTimeFm" :true-label="1" :false-label="0">预发时间FM</el-checkbox>
          <el-checkbox v-model="form.expectTimeTo" :true-label="1" :false-label="0">预发时间TO</el-checkbox>
          <el-checkbox v-for="(item,index) in soUdfHs" :key="index" v-model="form[item.paramCode]" :true-label="1" :false-label="0">{{item.value}}</el-checkbox>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改策略" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px">
      <div class="bigFormBox2">
        <el-form :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="编码" prop="soMergeTacticCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.soMergeTacticCode" placeholder="请输入编码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="soMergeTacticName" :label-width="formLabelWidth">
            <el-input v-model="rowData.soMergeTacticName" placeholder="请输入名称"></el-input>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">配置项</el-divider>
          </div>
          <el-form-item label="单据来源" prop="billSource" :label-width="formLabelWidth">
            <el-select v-model="rowData.billSource" placeholder="请选择">
              <el-option :key="0" label="不可合并" :value="0"></el-option>
              <el-option :key="1" label="强制合并" :value="1"></el-option>
              <el-option :key="2" label="提示" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发运单类型" prop="soType" :label-width="formLabelWidth">
            <el-select v-model="rowData.soType" placeholder="请选择">
              <el-option :key="0" label="不可合并" :value="0"></el-option>
              <el-option :key="1" label="强制合并" :value="1"></el-option>
              <el-option :key="2" label="提示" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">配置限制项 (勾选限制合并,不勾选不限制)</el-divider>
          </div>
        </el-form>
        <div style="padding-left: 50px;">
          <el-checkbox v-model="rowData.deliveryTime" :true-label="1" :false-label="0">交货时间</el-checkbox>
          <el-checkbox v-model="rowData.expectTimeFm" :true-label="1" :false-label="0">预发时间FM</el-checkbox>
          <el-checkbox v-model="rowData.expectTimeTo" :true-label="1" :false-label="0">预发时间TO</el-checkbox>
          <el-checkbox v-for="(item,index) in soUdfHs" :key="index" v-model="rowData[item.paramCode]" :true-label="1" :false-label="0">{{item.value}}</el-checkbox>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        v-loading="loading.masterTable"
        highlight-current-row
        @current-change="getLookRowData"
        :data="soMergeTactics"
        style="width: 100%">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="soMergeTacticName"
          label="名称">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="billSource"
          label="单据来源">
          <template slot-scope="scope">
            <span v-show="scope.row.billSource === 0">不可合并</span>
            <span v-show="scope.row.billSource === 1">强制合并</span>
            <span v-show="scope.row.billSource === 2">提示</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="soType"
          label="发运单类型">
          <template slot-scope="scope">
            <span v-show="scope.row.soType === 0">不可合并</span>
            <span v-show="scope.row.soType === 1">强制合并</span>
            <span v-show="scope.row.soType === 2">提示</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="deliveryTime"
          label="交货时间">
          <template slot-scope="scope">
            <i v-show="scope.row.deliveryTime === 1" class="iconfont icon-xuanzhong1"></i>
            <i v-show="scope.row.deliveryTime === 0" class="iconfont icon-weixuanzhong"></i>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="expectTimeFm"
          label="预发时间FM">
          <template slot-scope="scope">
            <i v-show="scope.row.expectTimeFm === 1" class="iconfont icon-xuanzhong1"></i>
            <i v-show="scope.row.expectTimeFm === 0" class="iconfont icon-weixuanzhong"></i>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="expectTimeTo"
          label="预发时间TO">
          <template slot-scope="scope">
            <i v-show="scope.row.expectTimeTo === 1" class="iconfont icon-xuanzhong1"></i>
            <i v-show="scope.row.expectTimeTo === 0" class="iconfont icon-weixuanzhong"></i>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          v-for="item in soUdfHs"
          :key="item.value"
          :prop="item.paramCode"
          :label="item.value"
          width="180">
          <template slot-scope="scope">
            <i v-show="scope.row[item.paramCode] === 1" class="iconfont icon-xuanzhong1"></i>
            <i v-show="scope.row[item.paramCode] === 0" class="iconfont icon-weixuanzhong"></i>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.soMergeTacticId)" icon="iconfont icon-shanchu" type="danger" circle
                       plain size="small"></el-button>
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

  export default {
    name: 'SoMergeTactic',
    data() {
      let validateSoMergeTacticCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'so_merge_tactic', 'so_merge_tactic_code', this.form.soMergeTacticCode, 0);
      };
      let validateSoMergeTacticCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'so_merge_tactic', 'so_merge_tactic_code', this.rowData.soMergeTacticCode, 1, this.rowData.soMergeTacticId);
      };
      let validateSoMergeTacticName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'so_merge_tactic', 'so_merge_tactic_name', this.form.soMergeTacticName, 0);
      };
      let validateSoMergeTacticName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'so_merge_tactic', 'so_merge_tactic_name', this.rowData.soMergeTacticName, 1, this.rowData.soMergeTacticId);
      };
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },

        soMergeTactics: [],
        soUdfHs: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        notLimit: '#E6A23C',

        form: {
          soMergeTacticCode: '',
          soMergeTacticName: '',
          billSource: 0,
          soType: 0,
          deliveryTime: 0,
          expectTimeFm: 0,
          expectTimeTo: 0,
          "soUdfHs1": 0,
          "soUdfHs2": 0,
          "soUdfHs3": 0,
        },
        rules: {
          soMergeTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateSoMergeTacticCode, trigger: 'blur'}
          ],
          soMergeTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateSoMergeTacticName, trigger: 'blur'}
          ],
          billSource: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          soType: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
        },
        formLabelWidth: '150px',

        rowData: {
          soMergeTacticCode: '',
          soMergeTacticName: '',
          billSource: 0,
          soType: 0,
          deliveryTime: 0,
          expectTimeFm: 0,
          expectTimeTo: 0,
          "soUdfHs1": 0,
          "soUdfHs2": 0,
          "soUdfHs3": 0,
        },
        rules2: {
          soMergeTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateSoMergeTacticCode2, trigger: 'blur'}
          ],
          soMergeTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateSoMergeTacticName2, trigger: 'blur'}
          ],
          billSource: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          soType: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
        },
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getSoMergeTacticData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getSoMergeTacticData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      getSoMergeTacticData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/so/merge/tactics/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.soMergeTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
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
            IOT.getServerData('/so/merge/tactics/insert', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getSoMergeTacticData()
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
            IOT.getServerData('/so/merge/tactics/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getSoMergeTacticData()
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
      showUpdateModal(row) {
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
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteSoMergeTactic(id)
          })
          .catch(_ => {
          });
      },
      deleteSoMergeTactic(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/so/merge/tactics/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getSoMergeTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getSoMergeTacticData();
      PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
        this.soUdfHs = udfs;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .soMergeTactic {

  }
</style>
