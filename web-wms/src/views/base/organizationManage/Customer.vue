<template>
  <div class="pack clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/电话" clearable></el-input>
        </div>
        <div class="item">
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
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

    <el-dialog title="新增客户" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="承运方" prop="carrierId" :label-width="formLabelWidth">
          <el-select v-model="form.carrierId" filterable placeholder="请选择承运方" style="width: 70%;">
            <el-option
                    v-for="item in carriers"
                    :key="item.carrierId"
                    :label="item.carrierName"
                    :value="item.carrierId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="customerCode" :label-width="formLabelWidth">
          <el-input v-model="form.customerCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="customerName" :label-width="formLabelWidth">
          <el-input v-model="form.customerName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="form.linkMan" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="form.linkPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改客户" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px" :close-on-click-modal="false">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="rowData.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="承运方" prop="carrierId" :label-width="formLabelWidth">
          <el-select v-model="rowData.carrierId" filterable placeholder="请选择承运方" style="width: 70%;">
            <el-option
              v-for="item in carriers"
              :key="item.carrierId"
              :label="item.carrierName"
              :value="item.carrierId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="customerCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.customerCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="customerName" :label-width="formLabelWidth">
          <el-input v-model="rowData.customerName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="rowData.linkMan" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="rowData.linkPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为客户 '+addressForm.customerName+' 新增收货地址'" :visible.sync="dialogAddressVisible" :before-close="handleCloseAddress" width="800px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="addressForm" :rules="rulesAddress" ref="addressForm">
        <el-form-item label="发货人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="addressForm.linkMan" placeholder="请输入发货人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="addressForm.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input :value="addressForm.province===''||addressForm.city===''||addressForm.area===''?'':addressForm.province+' / '+addressForm.city+' / '+addressForm.area" placeholder="请选择所在地区" style="width: 70%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi" @click="isShowAddressSelectBox = !isShowAddressSelectBox;clearAddressData()">选择</el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入详细地址"
                  v-model="addressForm.detailAddress"
                  style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="设为此客户默认地址" prop="state" label-width="180px">
            <el-switch
                    v-model="addressForm.state"
                    active-color="#13ce66"
                    inactive-color="#dcdfe6"
                    active-text="是"
                    inactive-text="否"
                    active-value="1"
                    inactive-value="0">
            </el-switch>
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
        <el-button @click="resetForm('addressForm');dialogAddressVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitAddressForm('addressForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为客户 '+addressFormRowData.customerName+' 修改收货地址'" :visible.sync="dialogUpdateAddressVisible" :before-close="handleCloseAddress2" width="800px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="addressFormRowData" :rules="rulesAddress2" ref="addressFormRowData">
        <el-form-item label="发货人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="addressFormRowData.linkMan" placeholder="请输入发货人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="addressFormRowData.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input :value="addressFormRowData.province===''||addressFormRowData.city===''||addressFormRowData.area===''?'':addressFormRowData.province+' / '+addressFormRowData.city+' / '+addressFormRowData.area" placeholder="请选择所在地区" style="width: 70%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi" @click="isShowAddressSelectBox2 = !isShowAddressSelectBox2;clearAddressData()">选择</el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入详细地址"
                  v-model="addressFormRowData.detailAddress"
                  style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="设为此仓库默认地址" prop="state" label-width="180px">
            <el-switch
                    v-model="addressFormRowData.state"
                    active-color="#13ce66"
                    inactive-color="#dcdfe6"
                    active-text="是"
                    inactive-text="否"
                    active-value="1"
                    inactive-value="0">
            </el-switch>
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
        <el-button @click="resetForm('addressFormRowData');dialogUpdateAddressVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAddressForm('addressFormRowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
              highlight-current-row
              @current-change="getLookRowData"
              :data="customers"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="customerName"
                label="名称">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.customerId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-left-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight">
      <div class="organizationRightTitle">客户信息</div>
      <p class="MasterBox">
        <span><b>客户编码：</b>{{lookRowData.customerCode}}</span>
        <span><b>联系人：</b>{{lookRowData.linkMan}}</span>
        <span><b>货主：</b>{{lookRowData.organizationName}}</span>
      </p>
      <p class="MasterBox">
        <span><b>客户名称：</b>{{lookRowData.customerName}}</span>
        <span><b>联系电话：</b>{{lookRowData.linkPhone}}</span>
        <span>&nbsp;</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>
      <div class="headerBtns">
        <el-button @click="dialogAddressVisible = true;showAddressModal()" size="small" plain icon="iconfont icon-xinzeng">新增地址</el-button>
      </div>
      <div class="tableBox">
        <el-table
                :data="receivingAddresss"
                style="width: 100%">
          <el-table-column
                  fixed="left"
                  label="默认"
                  width="50">
            <template slot-scope="scope">
              <i v-if="scope.row.state == '1'" class="iconfont icon-star" style="color: #f7ba2a;display: inline-block;width: 100%;text-align: center;"></i>
            </template>
          </el-table-column>
          <!--  <el-table-column
                    fixed="left"
                    type="index"
                    width="50">
            </el-table-column>-->
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="linkMan"
                  label="发货人"
                  width="300">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="linkPhone"
                  label="联系方式"
                  width="120">
          </el-table-column>
          <el-table-column
                  label="所在地区"
                  width="200">
            <template slot-scope="scope">
              <span>{{scope.row.province}} / {{scope.row.city}} / {{scope.row.area}}</span>
            </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="detailAddress"
                  label="详细地址"
                  width="300">
          </el-table-column>
          <el-table-column
                  fixed="right"
                  label="操作"
                  width="110">
            <template slot-scope="scope">
                <el-button @click="showUpdateAddressModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
                <el-button @click="isDeleteAddress(scope.row.receivingAddressId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
    name: 'Pack',
    data(){
      let validateCustomerCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'customer', 'customer_code', this.form.customerCode, 0);
      };
      let validateCustomerCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'customer', 'customer_code', this.rowData.customerCode, 1, this.rowData.customerId);
      };
      let validateCustomerName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'customer', 'customer_name', this.form.customerName, 0);
      };
      let validateCustomerName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'customer', 'customer_name', this.rowData.customerName, 1, this.rowData.customerId);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          organizationId: null,
        },
        searchData2:{
          pageNum: 1,
          pageSize: 5,
          total: null,
        },

        customers: [],
        receivingAddresss: [],
        organizations: [],
        carriers: [],
        lookRowData: {},

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogAddressVisible: false,
        dialogUpdateAddressVisible: false,
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
          organizationId: '',
          carrierId: '',
          customerCode: '',
          customerName: '',
          linkMan: '',
          linkPhone: '',
        },
        rules:{
          organizationId: [
            { required: true, message: '请选择货主', trigger: 'blur' }
          ],
          carrierId: [
            { required: true, message: '请选择承运方', trigger: 'blur' }
          ],
          customerCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateCustomerCode, trigger: 'blur'  }
          ],
          customerName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateCustomerName, trigger: 'blur'  }
          ],
          linkMan: [
            { required: true, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
            { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          ],
        },
        formLabelWidth: '120px',

        rowData: {
          organizationId: '',
          carrierId: '',
          customerCode: '',
          customerName: '',
          linkMan: '',
          linkPhone: '',
        },
        rules2:{
          organizationId: [
            { required: true, message: '请选择货主', trigger: 'blur' }
          ],
          carrierId: [
            { required: true, message: '请选择承运方', trigger: 'blur' }
          ],
          customerCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateCustomerCode2, trigger: 'blur'  }
          ],
          customerName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateCustomerName2, trigger: 'blur'  }
          ],
          linkMan: [
            { required: true, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
            { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          ],
        },
        addressForm: {
          customerId: null,
          linkMan: '',
          linkPhone: null,
          province: '',
          city: '',
          area: '',
          detailAddress: '',
          state: '0'
        },
        rulesAddress:{
          linkMan: [
            { required: true, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
            { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '请选择所在地区', trigger: 'blur' }
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
        addressFormRowData: {
          customerId: null,
          linkMan: '',
          linkPhone: null,
          province: '',
          city: '',
          area: '',
          detailAddress: '',
          state: '0'
        },
        rulesAddress2:{
          linkMan: [
            { required: true, message: '请输入联系人', trigger: 'blur' }
          ],
          linkPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
            { type: 'number', message: '联系电话必须为纯数字', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '请选择所在地区', trigger: 'blur' }
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
      }
    },
    watch: {
      activeName(val){
        if(val === 'fifth'){
          this.getReviewData()
        }
      },
      radioS(val){
        if(this.isShowAddressSelectBox){
          this.treeDataC = this.treeDataS[val].children;
          this.addressForm.province = this.treeDataS[val].name;
          if(this.treeDataC === undefined){
            this.addressForm.city = '-';
            this.addressForm.area = '-';
            this.isShowAddressSelectBox = false
          }else{
            this.activeAddressName = 'C'
          }
        }
        if(this.isShowAddressSelectBox2){
          this.treeDataC = this.treeDataS[val].children;
          this.addressFormRowData.province = this.treeDataS[val].name;
          if(this.treeDataC === undefined){
            this.addressFormRowData.city = '-';
            this.addressFormRowData.area = '-';
            this.isShowAddressSelectBox2 = false
          }else{
            this.activeAddressName = 'C'
          }
        }
      },
      radioC(val){
        if(this.isShowAddressSelectBox){
          this.treeDataQ = this.treeDataC[val].children;
          this.addressForm.city = this.treeDataC[val].name;
          if(this.treeDataQ === undefined){
            this.addressForm.area = '-';
            this.isShowAddressSelectBox = false
          }else{
            this.activeAddressName = 'Q'
          }
        }
        if(this.isShowAddressSelectBox2){
          this.treeDataQ = this.treeDataC[val].children;
          this.addressFormRowData.city = this.treeDataC[val].name;
          if(this.treeDataQ === undefined){
            this.addressFormRowData.area = '-';
            this.isShowAddressSelectBox2 = false
          }else{
            this.activeAddressName = 'Q'
          }
        }
      },
      radioQ(val){
        if(this.isShowAddressSelectBox){
          this.addressForm.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox = false
        }
        if(this.isShowAddressSelectBox2){
          this.addressFormRowData.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox2 = false
        }
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getCustomerData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getCustomerData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getReceivingAddressData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getCustomerData();
      },
      getCustomerData(){
        IOT.getServerData('/customers/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.customers = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
            if ( ret.rows.length == 0 ) {
              this.receivingAddresss = [];
              this.lookRowData = {};
              return
            }
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getReceivingAddressData(id){
        this.searchData2.customerId = id
        IOT.getServerData('/receiving/addresss/list','get',this.searchData2,(ret) => {
          if (ret.code === 200) {
            this.receivingAddresss = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        this.lookRowData = row == null ? {} : JSON.parse(JSON.stringify(row))
        this.getReceivingAddressData(this.lookRowData.customerId)
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/customers/insert','post',this.form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getCustomerData()
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
            IOT.getServerData('/customers/update','post',this.rowData,(ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getCustomerData()
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
      handleCloseAddress(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('addressForm');
        this.dialogAddressVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseAddress2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('addressFormRowData');
        this.dialogUpdateAddressVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      isDelete(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteCustomer(id)
          })
          .catch(_ => {});
      },
      isDeleteAddress(id){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteAddress(id)
          })
          .catch(_ => {});
      },
      deleteCustomer(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/customers/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getCustomerData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteAddress(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/receiving/addresss/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.getReceivingAddressData(that.lookRowData.customerId)
            });
          }else {
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
      showAddressModal(){
        this.addressForm.customerId = this.lookRowData.customerId;
        this.addressForm.customerName = this.lookRowData.customerName;
        this.addressForm.linkMan = this.lookRowData.linkMan;
        this.addressForm.linkPhone = Number(this.lookRowData.linkPhone);
      },
      commitAddressForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/receiving/addresss/insert','post',this.addressForm,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getReceivingAddressData(that.lookRowData.customerId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('addressForm')
            this.dialogAddressVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateAddressForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/receiving/addresss/update','post',this.addressFormRowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getReceivingAddressData(that.lookRowData.customerId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('addressFormRowData')
            this.dialogUpdateAddressVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateAddressModal(row){
        row.linkPhone = Number(row.linkPhone);
        row.state = String(row.state);
        this.addressFormRowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateAddressVisible = true
      },
      getCarrierData(){
        IOT.getServerData('/carriers/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getCustomerData();
      this.getAddressJson();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      this.getCarrierData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .pack{

  }
</style>
