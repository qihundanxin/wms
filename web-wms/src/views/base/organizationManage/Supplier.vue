<template>
  <div class="supplier">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input style="width:190px" v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/联系电话" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增供应商" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="名称" prop="supplierName" :label-width="formLabelWidth">
          <el-input v-model="form.supplierName" placeholder="请输入名称" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="supplierCode" :label-width="formLabelWidth">
          <el-input v-model="form.supplierCode" placeholder="请输入编码" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="form.linkMan" placeholder="请输入联系人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model="form.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input :value="form.province===''||form.city===''||form.area===''?'':form.province+' / '+form.city+' / '+form.area" placeholder="请选择所在地区" style="width: 60%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi" @click="isShowAddressSelectBox = !isShowAddressSelectBox;clearAddressData()">选择</el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入详细地址"
                  v-model="form.detailAddress"
                  style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="form.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox" width="600px" style="margin-top: -10vh;" :close-on-click-modal="false">
        <el-tabs v-model="activeAddressName" type="border-card">
          <el-tab-pane label="省份" name="S">
            <el-radio v-model="radioS" :label="index" border v-for="(item,index) in treeDataS" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
          <el-tab-pane label="城市" name="C">
            <el-radio v-model="radioC" :label="index" border v-for="(item,index) in treeDataC" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
          <el-tab-pane label="区/县" name="Q">
            <el-radio v-model="radioQ" :label="index" border v-for="(item,index) in treeDataQ" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改供应商" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="名称" prop="supplierName" :label-width="formLabelWidth">
          <el-input v-model="rowData.supplierName" placeholder="请输入名称" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="supplierCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.supplierCode" placeholder="请输入编码" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="rowData.linkMan" placeholder="请输入联系人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model="rowData.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input :value="rowData.province===''||rowData.city===''||rowData.area===''?'':rowData.province+' / '+rowData.city+' / '+rowData.area" placeholder="请选择所在地区" style="width: 60%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi" @click="isShowAddressSelectBox2 = !isShowAddressSelectBox2;clearAddressData()">选择</el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入详细地址"
                  v-model="rowData.detailAddress"
                  style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="rowData.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox2" width="600px" style="margin-top: -10vh;" :close-on-click-modal="false">
        <el-tabs v-model="activeAddressName" type="border-card">
          <el-tab-pane label="省份" name="S">
            <el-radio v-model="radioS" :label="index" border v-for="(item,index) in treeDataS" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
          <el-tab-pane label="城市" name="C">
            <el-radio v-model="radioC" :label="index" border v-for="(item,index) in treeDataC" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
          <el-tab-pane label="区/县" name="Q">
            <el-radio v-model="radioQ" :label="index" border v-for="(item,index) in treeDataQ" :key="index" class="organizationRadios">{{item.name}}</el-radio>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
              highlight-current-row
              :data="suppliers"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
              
                show-overflow-tooltip
                prop="supplierName"
                label="名称"
                width="180">
        </el-table-column>
        <el-table-column
              
                show-overflow-tooltip
                prop="supplierCode"
                label="编码"
                width="150">
        </el-table-column>
        <el-table-column
              
                show-overflow-tooltip
                prop="linkMan"
                label="联系人"
                width="120">
        </el-table-column>
        <el-table-column
               
                show-overflow-tooltip
                prop="linkPhone"
                label="联系电话"
                width="150">
        </el-table-column>
        <el-table-column
                label="所在地区"
                width="200">
          <template slot-scope="scope">
            <span>{{scope.row.province}} / {{scope.row.city}} / {{scope.row.area}}</span>
          </template>
        </el-table-column>
        <el-table-column
             
                show-overflow-tooltip
                prop="detailAddress"
                label="详细地址"
                width="300">
        </el-table-column>
        <el-table-column
          
                show-overflow-tooltip
                prop="memo"
                label="备注">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="150">
          <template slot-scope="scope">
            
              <el-button @click="showUpdateModal(scope.row)"   type="text"   plain size="small">修改</el-button>
              <el-button @click="isDelete(scope.row.supplierCode)"   type="text"   plain size="small">删除</el-button>
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
    name: 'Supplier',
    data() {
      let validateSupplierName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'supplier_manage', 'supplier_name', this.form.supplierName, 0);
      };
      let validateSupplierName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'supplier_manage', 'supplier_name', this.rowData.supplierName, 1, this.rowData.supplierId);
      };
      let validateSupplierCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'supplier_manage', 'supplier_code', this.form.supplierCode, 0);
      };
      let validateSupplierCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'supplier_manage', 'supplier_code', this.rowData.supplierCode, 1, this.rowData.supplierId);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },
        oneSearchBarHeight: '50px', // 收起高度(50px/行)
        autoSearchBarHeight: '100px', // 展开高度(50px/行)
        searchBarHeight: '50px',
        suppliers: [],

        modalTitle: '',
        dialogNewVisible: false,
        isShowAddressSelectBox: false,
        isShowAddressSelectBox2: false,

        activeAddressName: 'S',
        treeDataS: [],
        treeDataC: [],
        treeDataQ: [],
        radioS: '',
        radioC: '',
        radioQ: '',
        form: {
          supplierName: '',
          supplierCode: '',
          linkMan: '',
          linkPhone: '',
          memo: '',
          province: '',
          city: '',
          area: '',
          detailAddress: '',
        },
        rules:{
          supplierName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateSupplierName, trigger: 'blur'  }
          ],
          supplierCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateSupplierCode, trigger: 'blur'  }
          ],
          linkMan: [
            { required: false, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: false, message: '请输入联系电话', trigger: 'blur' }
          ],
          city: [
            { required: false, message: '请选择所在地区', trigger: 'blur' }
          ],
          area: [
            { required: false, message: '请选择所在地区', trigger: 'blur' }
          ],
          detailAddress: [
            { required: false, message: '请输入详细地址', trigger: 'blur' }
          ],
        },
        formLabelWidth: '120px',

        dialogUpdateVisible: false,
        rowData: {
          supplierName: '',
          supplierCode: '',
          linkMan: '',
          linkPhone: '',
          memo: '',
          province: '',
          city: '',
          area: '',
          detailAddress: '',
        },
        rules2:{
          supplierName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateSupplierName2, trigger: 'blur'  }
          ],
          supplierCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateSupplierCode2, trigger: 'blur'  }
          ],
          linkMan: [
            { required: true, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '请选择所在地区', trigger: 'blur' }
          ],
          area: [
            { required: true, message: '请选择所在地区', trigger: 'blur' }
          ],
          detailAddress: [
            { required: true, message: '请输入详细地址', trigger: 'blur' }
          ],
        },

        dialogVisible: false,
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

      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getSupplierData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getSupplierData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getSupplierData();
      },
      getSupplierData(){
        IOT.getServerData('/supplier/manages/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.suppliers = ret.rows;
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
            IOT.getServerData('/supplier/manages/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getSupplierData()
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
            IOT.getServerData('/supplier/manages/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getSupplierData()
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
      isDelete(code){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteSupplier(code)
          })
          .catch(_ => {});
      },
      deleteSupplier(code){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/supplier/manages/delete','get',{supplierCode: code},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getSupplierData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAddressJson(){
        let that = this;
        $.getJSON('/json/address.json', (data) => {
          let treeData = PF.toTree(data)
          that.treeDataS = treeData[0].children
        })
      },
      clearAddressData(){
        this.activeAddressName = 'S';
        this.treeDataC = [];
        this.treeDataQ = [];
        this.radioS = '';
        this.radioC = '';
        this.radioQ = '';
        this.inputS = '';
        this.inputC = '';
        this.inputQ = '';
      },
    },
    created() {
      this.getSupplierData();
      this.getAddressJson()
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .supplier {

  }
</style>
