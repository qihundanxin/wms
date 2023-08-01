<template>
  <div class="createAsnReturnBill">
    <el-dialog width="900px" title="选择商品" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">
      <div class="publicSearch body">
        <div class="searchBar" :style="{ height: isMoreSearch2 ? 'auto' : '' }">
          <el-button-group>
            <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
            <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
          </el-button-group>
          <div>
            <span>关键字搜索</span>
            <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
          </div>
        </div>
        <span class="moreSearchBtn" :class="isMoreSearch2 ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
          @click="isMoreSearch2 = !isMoreSearch2"></span>
      </div>
      <div class="tableBox" style="height:500px">
        <el-table v-loading="loading.itemTable" max-height="460" :data="items" style="width: 100%">
          <el-table-column fixed="left" label="操作" width="60">
            <template slot-scope="scope">
              <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                size="small"></el-button>
            </template>
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip fixed="left" prop="itemName" label="SKU名称" width="160">
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU" width="160">
          </el-table-column>
          <el-table-column width="200" label="UPC" prop="upcs">
            <template slot-scope="scope">
              <el-tag :key="upc.upc" v-for="upc in scope.row.upcs">
                {{ upc.upc }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column show-overflow-tooltip prop="itemTypeCode" label="商品种类" width="120">
          </el-table-column>
          <el-table-column show-overflow-tooltip prop="spec" label="规格" width="130">
          </el-table-column>
          <el-table-column show-overflow-tooltip prop="model" label="型号" width="130">
          </el-table-column>
          <el-table-column show-overflow-tooltip prop="packName" label="包装编码" width="100">
          </el-table-column>
        </el-table>
        <el-pagination style="text-align: right;" @current-change="handleCurrentChange"
          :current-page.sync="searchData.pageNum" :page-size="searchData.pageSize"
          layout="total, prev, pager, next, jumper" :total="searchData.total">
        </el-pagination>
      </div>
    </el-dialog>
    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">退货单信息</p>
        <el-form class="clearFloat" :model="form.asnMaster" :rules="rules" ref="form.asnMaster">
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
              style="width: 100%;">
              <el-option :key="form.asnMaster.wareId" :label="form.asnMaster.wareName" :value="form.asnMaster.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.organizationId" @change="changeOrganizationId" filterable
              placeholder="请选择货主" size="small" style="width: 100%;">
              <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.supplierId" filterable placeholder="请选择供应商" size="small"
              style="width: 100%;">
              <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="退货单类型" prop="asnType" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.asnType" filterable placeholder="请选择退货单类型" size="small"
              style="width: 100%;" disabled>
              <el-option v-for="item in asnTypes" :key="item.dataDictDetailId" :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预期到货时间" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker v-model="form.asnMaster.expectTime" type="date" value-format="yyyy-MM-dd" size="small"
              placeholder="请选择预期到货时间">
            </el-date-picker>
          </el-form-item>
          <!-- <el-form-item label="承运方" prop="carrierCode" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.carrierCode" filterable placeholder="请选择承运方" size="small" style="width: 100%;">
              <el-option
                v-for="item in carriers"
                :key="item.carrierCode"
                :label="item.carrierName"
                :value="item.carrierCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <!-- <el-form-item label="月台" prop="platformCode" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.platformCode" filterable placeholder="请选择月台" size="small" style="width: 100%;">
              <el-option
                      v-for="item in platforms"
                      :key="item.platformCode"
                      :label="item.label"
                      :value="item.platformCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <!--<el-form-item label="预期到货时间" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker
              v-model="form.asnMaster.expectTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              size="small"
              placeholder="请选择预期到货时间">
            </el-date-picker>
          </el-form-item>-->
          <el-form-item label="优先级" :label-width="formLabelWidth">
            <el-input-number v-model="form.asnMaster.orderNo" placeholder="请输入优先级" size="small"
              style="width: 100%;"></el-input-number>
          </el-form-item>
          <el-form-item label="装货地址" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.loadingPlace" placeholder="请输入装货地址" size="small"></el-input>
          </el-form-item>
          <el-form-item label="交货地址" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.deliveryPlace" placeholder="请输入交货地址" size="small"></el-input>
          </el-form-item>
          <el-form-item label="运输描述" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.trafficDescribe" placeholder="请输入运输描述" size="small"></el-input>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.memo" placeholder="请输入备注" size="small"></el-input>
          </el-form-item>
          <el-form-item v-for="(item, index) in asnUdfHs" :key="index" :label="item.value"
            :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster[item.paramCode]" :placeholder="'请输入' + item.value"
              size="small"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>
    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">物流信息</p>
        <el-form class="clearFloat" :rules="rules" ref="form.soMaster">
          <el-form-item label="承运方" :label-width="formLabelWidth">
            <!--  <el-form-item label="承运方" prop="shipCode" :label-width="formLabelWidth"> -->
            <el-select v-model="shipCode" filterable placeholder="请选择承运方" size="small" style="width: 100%;">
              <el-option v-for="item in carriers" :key="item.carrierCode" :label="item.carrierName"
                :value="item.carrierCode">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="物流单号" prop="shipBillCode" :label-width="formLabelWidth"> -->
          <el-form-item label="物流单号" :label-width="formLabelWidth">
            <el-input v-model="shipBillCode" placeholder="请物流单号" size="small"></el-input>
          </el-form-item>
          <br />
        </el-form>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>
    <div class="tableBox">
      <el-table highlight-current-row :data="form.asnDetails" style="width: 100%">
        <el-table-column fixed="left" type="index" width="50">
        </el-table-column>
        <el-table-column fixed="left" label="操作" width="60">
          <template slot-scope="scope">
            <el-button @click="deleteRow(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain
              size="small"></el-button>
          </template>
        </el-table-column>
        <el-table-column label="SKU" width="240">
          <template slot-scope="scope">
            <el-input v-model="scope.row.itemCode" @change="changeItemCode(scope.row.itemCode, scope.$index)"
              @clear="clearItemCode(scope.$index)" :disabled="isOrganization && (form.asnMaster.organizationId === '' || form.asnMaster.organizationId === undefined || form.asnMaster.organizationId === null
              || form.asnDetails[scope.$index].id > 0)" placeholder="请输入SKU" size="small" clearable
              style="width: 80%;"></el-input>
            <el-button @click="showItemModal(scope.$index)" icon="iconfont icon-secondsearch_accurate" type="primary"
              circle plain size="mini"></el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemName" label="SKU名称">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemName
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="退货单数量" width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.expectQuantity"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" placeholder="请输入数量"
              style="width: 80%;">
            <!--            <el-input-number v-model="scope.row.expectQuantity" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemTypeName" label="单位" width="180">
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" filterable
              placeholder="请选择单位" size="small">
              <el-option v-for="item in scope.row.packDetails" :key="item.packDetailId" :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="upc" width="180" label="UPC">
          <template slot-scope="scope">
            <el-select :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null"
              v-model="scope.row.upc" placeholder="请选择">
              <el-option v-for="upc in scope.row.upcs" :key="upc.upc" :label="upc.upc" :value="upc.upc">
              </el-option>
            </el-select>
            <!-- <el-input v-model="scope.row.batch"   :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null"
                      placeholder="请输入UPC" size="small" clearable style="width: 80%;"></el-input> -->
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in asnUdfDs" :key="index" :prop="item.paramCode"
          :label="item.value" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row[item.paramCode]"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null"
              :placeholder="'请输入' + item.value" size="small" clearable></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="createFootBox clearFloat" :style="{ width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)' }">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <el-button size="small" type="primary" icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <!-- <el-button @click="commitForm('form.asnMaster',-2)" type="info" plain>保存为草稿</el-button> -->
        <el-button @click="commitForm('form.asnMaster', 0)" type="primary">提 交</el-button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'CreateAsnReturnBill',
  data() {
    let validateExpectTime = (rule, value, callback) => {
      let expectTime = this.form.asnMaster.expectTime;
      let nowTime = PF.getNowFormatDate();
      if (!PF.isNull(expectTime)) {
        callback(new Error('请选择预计到货时间(早)'));
      } else if (!PF.isNull(nowTime)) {
        callback(new Error('未获取到当前日期'));
      } else if (nowTime > expectTime) {
        callback(new Error('预计到货时间应该大于等于当前日期'));
      } else {
        callback();
      }
    };
    return {
      shipCode: '',
      shipBillCode: '',
      isMoreSearch: true,
      isMoreSearch2: true,
      loading: {
        itemTable: true,
      },
      searchData: {
        pageNum: 1,
        pageSize: 10,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        organizationId: null,
      },
      organizations: [],
      suppliers: [],
      platforms: [],
      carriers: [],
      items: [],
      asnUdfHs: [],
      asnUdfDs: [],
      asnTypes: [],
      isOrganization: true,
      selectDetailIndex: null,
      oldOrganizationId: null,
      innerVisibleItem: false,
      formLabelWidth: '120px',
      form: {
        billSource: 1,
        insertType: 0,
        asnMaster: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          organizationId: null,
          organizationName: '',
          supplierId: null,
          supplierName: '',
          asnType: 44,
          expectTime: '',
          orderNo: 0,
          loadingPlace: '',
          deliveryPlace: '',
          trafficDescribe: '',
          memo: '',
          "asnUdfHs1": "",
          "asnUdfHs2": "",
          "asnUdfHs3": "",
          shipCode: '',
          shipBillCode: ''
        },
        asnDetails: [
          {
            batch: '',
            itemCode: '',
            itemName: '',
            upc: '',
            expectQuantity: null,
            packDetails: [],
            packDetailId: null,
            packDescribe: '',
            "asnUdfDs1": "",
            "asnUdfDs2": "",
            "asnUdfDs3": ""
          }
        ],
        asnShipDetails: [
          {
            shipCode: '',
            shipBillCode: ''
          }
        ]
      },
      asnDetail: {
        itemCode: '',
        itemName: '',
        upc: '',
        expectQuantity: null,
        packDetails: [],
        packDetailId: null,
        packDescribe: '',
        "asnUdfDs1": "",
        "asnUdfDs2": "",
        "asnUdfDs3": "",
      },
      rules: {
        organizationId: [
          { required: true, message: '请选择货主', trigger: 'blur' }
        ],
        shipCode: [
          { required: true, message: '请选择物流公司', trigger: 'blur' }
        ],
        shipBillCode: [
          { required: true, message: '请选择物流单号', trigger: 'blur' }
        ],
        // supplierId: [
        //   {required: true, message: '请选择供应商', trigger: 'blur'}
        // ],
        // asnType: [
        //   {required: true, message: '请选择退货单类型', trigger: 'blur'}
        // ],
        // supplierId: [
        //   {required: true, message: '请选择供应商', trigger: 'blur'}
        // ],
        // asnType: [
        //   {required: true, message: '请选择退货单类型', trigger: 'blur'}
        // ],
        // expectTime: [
        //   {required: true, message: '预计到货时间不得为空', trigger: 'blur'},
        //   { validator: validateExpectTime, trigger: 'blur'  }
        // ],
      }
    }
  },
  computed: {
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
    organizationId() {
      return this.form.asnMaster.organizationId
    },
    supplierId() {
      return this.form.asnMaster.supplierId
    },
  },
  watch: {
    organizationId(val) {
      for (let i = 0; i < this.organizations.length; i++) {
        if (this.organizations[i].organizationId === val) {
          this.form.asnMaster.organizationName = this.organizations[i].organizationName;
        }
      }
    },
    supplierId(val) {
      for (let i = 0; i < this.suppliers.length; i++) {
        if (this.suppliers[i].supplierId === val) {
          this.form.asnMaster.supplierName = this.suppliers[i].supplierName;
        }
      }
    },
  },
  methods: {
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getItemData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    getItemData() {
      this.loading.itemTable = true;
      IOT.getServerData('/item/infos/selectItem', 'get', this.searchData, (ret) => {
        this.loading.itemTable = false;
        if (ret.code === 200) {
          this.items = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getPlatformData() {
      IOT.getServerData('/platforms/list', 'get', { wareId: this.form.asnMaster.wareId }, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          for (let i = 0; i < list.length; i++) {
            list[i].label = list[i].platformCode
          }
          this.platforms = list;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getCarrierData() {
      IOT.getServerData('/carriers/list', 'get', {}, (ret) => {
        if (ret.code === 200) {
          this.carriers = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    addRow() {
      this.form.asnDetails.push(PF.JSON(this.asnDetail));
    },
    deleteRow(index) {
      this.form.asnDetails.splice(index, 1);
    },
    showItemModal(index) {
      if (this.isOrganization) {
        if (!PF.isNull(this.form.asnMaster.organizationId)) {
          IOT.tips('请选择货主！', 'error');
          return false
        }
        this.searchData.organizationId = this.form.asnMaster.organizationId;
      } else {
        this.searchData.organizationId = null;
      }
      this.selectDetailIndex = index;
      this.getItemData();
      this.innerVisibleItem = true;
    },
    detailHasId(id) {
      for (let i in this.form.asnDetails) {
        if (this.form.asnDetails[i].id == id) {
          return true;
        }
      }
      return false;
    },
    selectItem(row) {
      if (this.detailHasId(row.id)) {
        IOT.tips('相同商品不能重复添加！', 'error');
        this.innerVisibleItem = false;
        return false
      }
      if (row.packCode == null || row.packCode == undefined || row.packCode == '') {
        IOT.tips('该商品未添加包装', 'error');
        return false
      }
      IOT.getServerData('/pack/details/findByPackCode', 'get', { packCode: row.packCode }, (ret) => {
        if (ret.code === 200) {
          row.packDetails = ret.rows;
          if (ret.rows.length == 0) {
            IOT.tips('该商品未添加包装', 'error');
            return false
          }
          var index = this.selectDetailIndex;
          this.form.asnDetails[index].packDetails = ret.rows;
          this.form.asnDetails[index].itemCode = row.itemCode;
          this.form.asnDetails[index].id = row.id;
          this.form.asnDetails[index].itemName = row.itemName;
          this.form.asnDetails[index].model = row.model;
          this.form.asnDetails[index].spec = row.spec;
          this.form.asnDetails[index].upcs = row.upcs;
          this.form.asnDetails[index].imgUrl = row.imgUrl;
          this.form.asnDetails[index].transRatio = ret.rows[0].transRatio;
          this.form.asnDetails[index].packDescribe = ret.rows[0].packDescribe;
          this.form.asnDetails[index].packDetailId = ret.rows[0].packDetailId;
          this.form.asnDetails[index].packCode = row.packCode;
          if (row.upcs != null && row.upcs.length > 0) {
            this.form.asnDetails[index].upc = row.upcs[0].upc;
          }
          //   this.form.asnDetails[index]["expectQuantity"] = 0;
          // row.packCode = ret.rows[0].packCode;
          // row.packDetailId = ret.rows[0].packDetailId;
          // row.packDescribe = ret.rows[0].packDescribe;
          // row.transRatio = ret.rows[0].transRatio;
          // for(let key in row){
          //   this.form.asnDetails[this.selectDetailIndex].transRatio = ret.rows[0].transRatio;
          //   this.form.asnDetails[this.selectDetailIndex].packDescribe = ret.rows[0].packDescribe;
          //   this.form.asnDetails[this.selectDetailIndex].packDetailId =ret.rows[0].packDetailId;
          //   this.form.asnDetails[this.selectDetailIndex].packCode = ret.rows[0].packCode;
          //   this.form.asnDetails[this.selectDetailIndex]["expectQuantity"] = 0;
          // }
          this.innerVisibleItem = false;
          this.addRow();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
    },
    commitForm(formName, insertType) {
      IOT.showOverlay('保存中，请稍等...');
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = PF.JSON(this.form);
          for (let i = 0; i < form.asnDetails.length; i++) {
            if (!PF.isNull(form.asnDetails[i].id)) {
              IOT.tips('不得有空行！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.asnDetails[i].itemCode)) {
              IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.asnDetails[i].expectQuantity) || form.asnDetails[i].expectQuantity <= 0) {
              IOT.tips('数量需大于零！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.asnDetails[i].packDetailId)) {
              IOT.tips('未选择单位！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            // 主单位数量
            form.asnDetails[i].expectQuantity = form.asnDetails[i].expectQuantity * form.asnDetails[i].transRatio;
          }
          form.insertType = insertType;
          form.asnShipDetails = [
            {
              shipCode: this.shipCode,
              shipBillCode: this.shipBillCode
            }
          ]
          IOT.getServerData('/asn/masters/insert', 'post', form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success');
              this.$router.push({ name: 'asnReturnBill' })
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
          this.resetForm(formName);
          this.cleaerDetails();
          this.form.asnMaster.memo = '';
        } else {
          IOT.hideOverlay();
          console.error('error submit!!');
          return false;
        }
      });
    },
    cleaerDetails() {
      this.form.asnDetails = [{
        itemCode: '',
        itemName: '',
        expectQuantity: null,
        transRatio: 1
      }];
    },
    changeOrganizationId(val) {
      if (this.isOrganization) {
        if (!PF.isNull(this.oldOrganizationId)) {
          this.oldOrganizationId = val;
        } else {
          this.$confirm('修改货主已选商品将清空？')
            .then(_ => {
              this.oldOrganizationId = val;
              this.cleaerDetails();
            })
            .catch(_ => {
              this.form.asnMaster.organizationId = this.oldOrganizationId;
            });
        }
      }
    },
    clearItemCode(index) {
      this.form.requestBuyDetails[index] = this.requestBuyDetail;
    },
    changeItemCode(itemCode, index) {
      let that = this;
      if (!PF.isNull(itemCode)) {
        that.form.asnDetails[index] = PF.JSON(that.asnDetail);
        return false
      }
      let searchData = {
        wareId: this.form.asnMaster.wareId,
        organizationId: this.form.asnMaster.organizationId,
        itemCode: itemCode,
      };
      if (!that.isOrganization) {
        delete searchData.organizationId;
      }
      IOT.showOverlay('查询中，请稍等...');
      IOT.getServerData('/item/infos/list', 'get', searchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          if (list.length === 1) {
            if (this.detailHasId(list[0].id)) {
              IOT.tips('相同商品不能重复添加！', 'error');
              return false
            }
            let row = list[0];
            IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                row.packDetails = ret.rows;
                this.form.asnDetails[index].packDetails = ret.rows;
                this.form.asnDetails[index].itemCode = row.itemCode;
                this.form.asnDetails[index].id = row.id;
                this.form.asnDetails[index].itemName = row.itemName;
                this.form.asnDetails[index].model = row.model;
                this.form.asnDetails[index].upcs = row.upcs;
                this.form.asnDetails[index].spec = row.spec;
                this.form.asnDetails[index].imgUrl = row.imgUrl;
                this.form.asnDetails[index].transRatio = ret.rows[0].transRatio;
                this.form.asnDetails[index].packDescribe = ret.rows[0].packDescribe;
                this.form.asnDetails[index].packDetailId = ret.rows[0].packDetailId;
                this.form.asnDetails[index].packCode = row.packCode;
                //   this.form.asnDetails[index]["expectQuantity"] = 0;
                if (row.upcs != null && row.upcs.length > 0) {
                  this.form.asnDetails[index].upc = row.upcs[0].upc;
                }
                this.addRow();
              } else {
                that.form.asnDetails[index].itemCode = '';
                that.asnDetail.itemCode = '';
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
          } else {
            IOT.hideOverlay();
            that.form.asnDetails[index].itemCode = '';
            that.asnDetail.itemCode = '';
            IOT.tips('未查询到此SKU,请确认后输入！', 'error');
          }
        } else {
          IOT.hideOverlay();
          that.form.asnDetails[index].itemCode = '';
          that.asnDetail.itemCode = '';
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    changePackDetailId(index) {
      let detail = PF.JSON(this.form.asnDetails[index]);
      for (let i = 0; i < detail.packDetails.length; i++) {
        if (detail.packDetails[i].packDetailId === detail.packDetailId) {
          this.form.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe;
          this.form.asnDetails[index].transRatio = detail.packDetails[i].transRatio;
        }
      }
    },
  },
  created() {
    PF.getSysParamUdfData('isOrganization', [], (data, udfs) => {
      if (udfs[0].value === '1') {
        this.isOrganization = true;
      } else if (udfs[0].value === '0') {
        this.isOrganization = false;
      }
    });
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    PF.getSuppliers((rows) => {
      this.suppliers = rows;
    });
    PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
      this.asnUdfHs = udfs;
    });
    PF.getSysParamUdfData('asnUdfDs', [], (data, udfs) => {
      this.asnUdfDs = udfs;
    });
    this.getPlatformData();
    this.getCarrierData();
    PF.getDataDictUdfData('asnType', (rows) => {
      this.asnTypes = rows;
    });
  },
  mounted() {
  }
}
</script>
<style scoped lang="less">
.createAsnReturnBill {
  padding-bottom: 100px;

  input:focus {
    outline: none; // 如何把蓝色去掉？
    border: 1px solid #30B101; // 如何改成别的颜色？
  }
}
</style>
