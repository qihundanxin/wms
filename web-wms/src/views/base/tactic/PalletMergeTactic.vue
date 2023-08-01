<template>
  <div class="palletMergeTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPalletMergeTacticData" size="mini" icon="el-icon-search">查询</el-button>
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
          <el-form-item label="混批次" prop="mixBatch" :label-width="formLabelWidth">
            <el-select v-model="form.mixBatch" placeholder="请选择">
              <el-option :key="0" label="不限制" :value="0"></el-option>
              <el-option :key="1" label="不可混批" :value="1"></el-option>
              <el-option :key="2" label="可以混批" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="混物料" prop="mixItem" :label-width="formLabelWidth">
            <el-select v-model="form.mixItem" placeholder="请选择">
              <el-option :key="0" label="不限制" :value="0"></el-option>
              <el-option :key="1" label="不可混物料" :value="1"></el-option>
              <el-option :key="2" label="可以混物料" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
            <el-select v-model="form.type" placeholder="请选择">
              <el-option :key="0" label="1" :value="0"></el-option>
              <el-option :key="1" label="2" :value="1"></el-option>
              <el-option :key="2" label="3" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数量上限" prop="quantityLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.quantityLimit" :min="0" label="请输入数量上限" style="width: 80%;"></el-input-number>
          </el-form-item>
          <el-form-item label="体积上限" prop="sizeLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.sizeLimit" :min="0" label="请输入体积上限" style="width: 80%;"></el-input-number>
          </el-form-item>
          <el-form-item label="重量上限" prop="weightLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.weightLimit" :min="0" label="请输入重量上限" style="width: 80%;"></el-input-number>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改策略" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px">
      <div class="bigFormBox2">
        <el-form :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="混批次" prop="mixBatch" :label-width="formLabelWidth">
            <el-select v-model="rowData.mixBatch" placeholder="请选择">
              <el-option :key="0" label="不限制" :value="0"></el-option>
              <el-option :key="1" label="不可混批" :value="1"></el-option>
              <el-option :key="2" label="可以混批" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="混物料" prop="mixItem" :label-width="formLabelWidth">
            <el-select v-model="rowData.mixItem" placeholder="请选择">
              <el-option :key="0" label="不限制" :value="0"></el-option>
              <el-option :key="1" label="不可混物料" :value="1"></el-option>
              <el-option :key="2" label="可以混物料" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
            <el-select v-model="rowData.type" placeholder="请选择">
              <el-option :key="0" label="1" :value="0"></el-option>
              <el-option :key="1" label="2" :value="1"></el-option>
              <el-option :key="2" label="3" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数量上限" prop="quantityLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.quantityLimit" :min="0" label="请输入数量上限" style="width: 80%;"></el-input-number>
          </el-form-item>
          <el-form-item label="体积上限" prop="sizeLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.sizeLimit" :min="0" label="请输入体积上限" style="width: 80%;"></el-input-number>
          </el-form-item>
          <el-form-item label="重量上限" prop="weightLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.weightLimit" :min="0" label="请输入重量上限" style="width: 80%;"></el-input-number>
          </el-form-item>
        </el-form>
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
        :data="palletMergeTactics"
        style="width: 100%">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="mixBatch"
          label="混批次">
          <template slot-scope="scope">
            <span v-show="scope.row.mixBatch === 0">不限制</span>
            <span v-show="scope.row.mixBatch === 1">不可混批</span>
            <span v-show="scope.row.mixBatch === 2">可以混批</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="mixItem"
          label="混物料">
          <template slot-scope="scope">
            <span v-show="scope.row.mixItem === 0">不限制</span>
            <span v-show="scope.row.mixItem === 1">不可混物料</span>
            <span v-show="scope.row.mixItem === 2">可以混物料</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="type"
            label="类型">
          <template slot-scope="scope">
            <span v-show="scope.row.type === 0">1</span>
            <span v-show="scope.row.type === 1">2</span>
            <span v-show="scope.row.type === 2">3</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="quantityLimit"
          label="数量上限">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="sizeLimit"
          label="体积上限">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="weightLimit"
          label="重量上限">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle
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
    name: 'PalletMergeTactic',
    data() {
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

        palletMergeTactics: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        notLimit: '#E6A23C',

        form: {
          mixBatch: 0,
          mixItem: 0,
          quantityLimit: 0,
          sizeLimit: 0,
          type: 0,
          weightLimit: 0,
        },
        rules: {
          mixBatch: [
            {required: true, message: '请选择', trigger: 'blur'},
          ],
          mixItem: [
            {required: true, message: '请选择', trigger: 'blur'},
          ],
          quantityLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          sizeLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          type: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          weightLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
        },
        formLabelWidth: '150px',

        rowData: {
          mixBatch: 0,
          mixItem: 0,
          quantityLimit: 0,
          sizeLimit: 0,
          type: 0,
          weightLimit: 0,
        },
        rules2: {
          mixBatch: [
            {required: true, message: '请选择', trigger: 'blur'},
          ],
          mixItem: [
            {required: true, message: '请选择', trigger: 'blur'},
          ],
          quantityLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          sizeLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          type: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
          weightLimit: [
            {required: true, message: '不得为空', trigger: 'blur'},
          ],
        },
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getPalletMergeTacticData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getPalletMergeTacticData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      getPalletMergeTacticData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/combine/pallet/tactics/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.palletMergeTactics = ret.rows;
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
            IOT.getServerData('/combine/pallet/tactics/add', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getPalletMergeTacticData()
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
            IOT.getServerData('/combine/pallet/tactics/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getPalletMergeTacticData()
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
            this.deletePalletMergeTactic(id)
          })
          .catch(_ => {
          });
      },
      deletePalletMergeTactic(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/combine/pallet/tactics/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getPalletMergeTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getPalletMergeTacticData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .palletMergeTactic {

  }
</style>
