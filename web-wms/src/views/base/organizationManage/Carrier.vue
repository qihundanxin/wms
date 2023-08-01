<template>
  <div class="carrier">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/联系电话" clearable></el-input>
        </div>
        <div class="item">
          <span>物流类型</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="请选择物流类型">
            <el-option
                    v-for="item in typeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
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

    <el-dialog title="新增承运方" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="编码" prop="carrierCode" :label-width="formLabelWidth">
          <el-input v-model="form.carrierCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="承运方名称" prop="carrierName" :label-width="formLabelWidth">
          <el-input v-model="form.carrierName" placeholder="请输入承运方名称"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="form.linkMan" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="form.linkPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="物流类型" prop="type" :label-width="formLabelWidth">
          <el-select v-model="form.type" filterable placeholder="请选择物流类型">
            <el-option
                    v-for="item in typeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改承运方" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px" :close-on-click-modal="false">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="编码" prop="carrierCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.carrierCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="承运方名称" prop="carrierName" :label-width="formLabelWidth">
          <el-input v-model="rowData.carrierName" placeholder="请输入承运方名称"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="rowData.linkMan" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="rowData.linkPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="物流类型" prop="type" :label-width="formLabelWidth">
          <el-select v-model="rowData.type" filterable placeholder="请选择物流类型">
            <el-option
                    v-for="item in typeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
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
              :data="carriers"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="carrierName"
                label="承运方名称">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="carrierCode"
                label="编码">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="type"
                label="物流类型">
          <template slot-scope="scope">
            <span v-for="item in typeOptions" :key="item.value" v-if="scope.row.type == item.value"><i class="iconfont icon-point" :style="{color: item.color}"></i>{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="linkMan"
                label="联系人">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="linkPhone"
                label="联系电话">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.carrierId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
    name: 'Carrier',
    data() {
      let validateCarrierCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'carrier', 'carrier_code', this.form.carrierCode, 0);
      };
      let validateCarrierCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'carrier', 'carrier_code', this.rowData.carrierCode, 1, this.rowData.carrierId);
      };
      let validateCarrierName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'carrier', 'carrier_name', this.form.carrierName, 0);
      };
      let validateCarrierName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'carrier', 'carrier_name', this.rowData.carrierName, 1, this.rowData.carrierId);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          type: '',
        },
        carriers: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,


        form: {
          carrierCode: '',
          carrierName: '',
          linkMan: '',
          linkPhone: '',
          type: '',
        },
        rules:{
          carrierCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateCarrierCode, trigger: 'blur'  }
          ],
          carrierName: [
            { required: true, message: '请输入承运方名称', trigger: 'blur' },
            { validator: validateCarrierName, trigger: 'blur'  }
          ],
          // linkMan: [
          //   { required: true, message: '请输入联系人', trigger: 'blur' }
          // ],
          // linkPhone: [
          //   { required: true, message: '请输入联系电话', trigger: 'blur' },
          //   { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          // ],
          // type: [
          //   { required: true, message: '请输入物流类型', trigger: 'blur' }
          // ],
        },
        formLabelWidth: '120px',

        rowData: {
          carrierCode: '',
          carrierName: '',
          linkMan: '',
          linkPhone: '',
          type: '',
        },
        rules2:{
          carrierCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateCarrierCode2, trigger: 'blur'  }
          ],
          carrierName: [
            { required: true, message: '请输入承运方名称', trigger: 'blur' },
            { validator: validateCarrierName2, trigger: 'blur'  }
          ],
          // linkMan: [
          //   { required: true, message: '请输入联系人', trigger: 'blur' }
          // ],
          // linkPhone: [
          //   { required: true, message: '请输入联系电话', trigger: 'blur' },
          //   { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          // ],
          // type: [
          //   { required: true, message: '请输入物流类型', trigger: 'blur' }
          // ],
        },
        typeOptions: [{
          value: '0',
          label: '国外物流',
          color: '#F56C6C'
        }, {
          value: '1',
          label: '国内物流',
          color: '#E6A23C'
        }, {
          value: '2',
          label: '其他物流',
          color: '#409EFF'
        }, {
          value: '3',
          label: '自提',
          color: '#67C23A'
        }],
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getCarrierData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getCarrierData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.type = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getCarrierData();
      },
      getCarrierData(){
        IOT.getServerData('/carriers/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
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
            IOT.getServerData('/carriers/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getCarrierData()
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
            IOT.getServerData('/carriers/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getCarrierData()
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
        row.linkPhone = Number(row.linkPhone)
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
            this.deleteCarrier(id)
          })
          .catch(_ => {});
      },
      deleteCarrier(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/carriers/delete','get',{carrierId: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getCarrierData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getCarrierData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .carrier {

  }
</style>
