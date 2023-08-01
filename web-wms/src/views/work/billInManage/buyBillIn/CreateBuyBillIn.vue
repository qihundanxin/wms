<template>
  <div class="createBuyBillIn">
    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">采购单信息</p>
        <el-form class="clearFloat" :model="form.requestBuy" :rules="rules" ref="form.requestBuy">
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.wareId" disabled filterable placeholder="请选择仓库" size="small"
              style="width: 100%;">
              <el-option :key="form.requestBuy.wareId" :label="form.requestBuy.wareName"
                :value="form.requestBuy.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.organizationId" @change="changeOrganizationId" filterable
              placeholder="请选择货主" size="small" style="width: 100%;">
              <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.supplierId" filterable placeholder="请选择供应商" size="small"
              style="width: 100%;">
              <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="采购单类型" prop="buyType" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.buyType" filterable placeholder="请选择采购单类型" size="small"
              style="width: 100%;">
              <el-option v-for="item in buyBillInTypes" :key="item.dataDictDetailId" :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="预期到货时间" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker
              v-model="form.requestBuy.expectTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              size="small"
              placeholder="请选择预期到货时间">
            </el-date-picker>
          </el-form-item>-->
          <el-form-item label="预期到货日期" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker v-model="form.requestBuy.expectTime" type="date" value-format="yyyy-MM-dd" size="small"
              placeholder="请选择预期到货日期">
            </el-date-picker>
          </el-form-item>
            <el-form-item label="付款方式" :label-width="formLabelWidth">
                <el-input v-model="form.requestBuy.paymentMethod" placeholder="请输入付款方式" size="small"></el-input>
            </el-form-item>

          <el-form-item label="采购总价" :label-width="formLabelWidth"   prop="buyTotalPrices">
            <el-input-number v-model="form.requestBuy.buyTotalPrices" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入采购总价" size="small" style="width: 100%;"></el-input-number>
          </el-form-item>

          <el-form-item label="服务费" :label-width="formLabelWidth">
            <el-input-number v-model="form.requestBuy.serviceCharge" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入服务费" size="small" style="width: 100%;"></el-input-number>
          </el-form-item>
            <el-form-item label="其他费用" :label-width="formLabelWidth">
            <el-input-number v-model="form.requestBuy.otherExpenses" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入其他费用" size="small" style="width: 100%;" ></el-input-number>
          </el-form-item>
            <el-form-item label="付款说明" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy.paymentInstruction" placeholder="请输入付款说明" size="small"></el-input>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy.memo" placeholder="请输入备注" size="small"></el-input>
          </el-form-item>
          <el-form-item v-for="(item, index) in poUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy[item.paramCode]" :placeholder="'请输入' + item.value" size="small"></el-input>
          </el-form-item>
        </el-form>
        <!-- auto-upload 表示选择完文件后不自动上传-->
        <el-upload
                class="upload-demo"
                ref="upload"
                :headers="headerObj"
                :action="uploadUrl"
                :auto-upload="true"
                :on-exceed="handleExceed"
                :on-success="handleSuccess"
                :before-upload="beforeUpload"
                :on-remove="handleRemove"
                :on-preview="handlePreview"
                multiple
                :limit="5"
                accept="image/png,image/jpeg,image/jpg,application/pdf">
          <el-button slot="trigger" size="small" type="primary">上传Invoice</el-button>
          <div slot="tip" class="el-upload__tip">
            支持多个文件上传，单个文件大小不超过10MB，仅支持png/jpeg/jpg/pdf格式。
          </div>
        </el-upload>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>

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

      <div class="tableBox">
        <el-table v-loading="loading.itemTable" max-height="460" :data="items" style="width: 100%">
          <el-table-column fixed="left" label="操作" width="60">
            <template slot-scope="scope">
              <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                size="small"></el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="left" type="index" width="50">
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip fixed="left" prop="itemName" label="SKU名称" width="180">
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU" width="180">
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip prop="itemTypeName" label="商品种类" width="180">
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip prop="supplierName" label="供应商" width="180">
          </el-table-column>

        </el-table>
        <el-pagination style="text-align: right;" @current-change="handleCurrentChange"
          :current-page.sync="searchData.pageNum" :page-size="searchData.pageSize"
          layout="total, prev, pager, next, jumper" :total="searchData.total">
        </el-pagination>
      </div>

    </el-dialog>

    <div class="tableBox">
      <el-table highlight-current-row :data="form.requestBuyDetails" style="width: 100%">
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
              @clear="clearItemCode(scope.$index)" :disabled="isOrganization && (form.requestBuy.organizationId === '' || form.requestBuy.organizationId === undefined || form.requestBuy.organizationId === null
              || form.requestBuyDetails[scope.$index].id > 0)" placeholder="SKU" size="small" clearable
              style="width: 80%;"></el-input>
            <el-button @click="showItemModal(scope.$index)" icon="iconfont icon-secondsearch_accurate" type="primary"
              circle plain size="mini"></el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemName" label="SKU名称" width="180">
        </el-table-column>
        <el-table-column label="采购数量" width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.expectQuantity"
              @change="changeExpectQuantity(scope.$index)"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" placeholder="请输入数量"
              style="width: 80%;">
          </template>
        </el-table-column>
        <el-table-column prop="transRatio" label="单位系数" width="180">
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
        <el-table-column label="进货单价（加元）" width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.priceIn"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" placeholder="请输入数量"
              style="width: 80%;">
          </template>
        </el-table-column>
        <el-table-column label="进货税率（%）" width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.taxRateIn"
              :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" placeholder="请输入数量"
              style="width: 80%;">
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in poUdfDs" :key="index" :prop="item.paramCode"
          :label="item.value" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row[item.paramCode]" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" :placeholder="'请输入' + item.value" size="small" clearable></el-input>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="spec" label="规格" width="180">
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="model" label="型号" width="180">
        </el-table-column>

      </el-table>
    </div>

    <div class="createFootBox clearFloat" :style="{ width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)' }">
      <div class="leftFloat">
        <el-button type="info" @click="$router.push({ name: 'buyBillIn' })">返 回</el-button>
      </div>
      <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <!-- <el-button @click="commitForm('form.requestBuy',-2)" type="info" plain>保存为草稿</el-button> -->
<!--        <el-button @click="commitForm('form.requestBuy', 0)" type="primary">提 交</el-button>-->
        <el-button @click="submitForm('form.requestBuy')" type="primary">提交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
import DateUtils from "@/utils/DateUtils";
export default {
  name: 'CreateBuyBillIn',
  data() {
    return {
      isMoreSearch: true,
      isMoreSearch2: true,
      loading: {
        itemTable: true,
      },
      // 上传的后端URL
      uploadUrl: localStorage.getItem("baseUrl") + '/request/buys/uploadInvoice',
      headerObj: {
        "access-token": localStorage.getItem('token')
      },
      searchData: {
        pageNum: 1,
        pageSize: 10,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        organizationId: null,
      },
      buyBillIns: [],
      organizations: [],
      suppliers: [],
      items: [],
      // wares: [],
      ids: [],
      poUdfHs: [],
      poUdfDs: [],
      buyBillInTypes: [],

      isOrganization: true,
      selectDetailIndex: null,
      oldOrganizationId: null,

      innerVisibleItem: false,

      formLabelWidth: '120px',

      form: {
        insertType: 0,
        requestBuy: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          organizationId: null,
          organizationName: '',
          supplierId: null,
          supplierName: '',
          buyTotalPrices: undefined,
          serviceCharge: undefined,
          otherExpenses: undefined,
          paymentInstruction:'',
          paymentMethod:'',
          listInvoiceFilePath:[],
          expectTime: null,
          buyType: null,
          memo: '',
          "poUdfHs1": "",
          "poUdfHs2": "",
          "poUdfHs3": ""
        },
        requestBuyDetails: [
          {
            itemCode: '',
            itemName: '',
            expectQuantity: null,
            oldQ: 0,
            packDetails: [],
            packDetailId: null,
            transRatio: null,
            paymentInstruction:'',
            paymentMethod:'',
            listInvoiceFilePath:[],
            oldP: null,
            packDescribe: '',
            "poUdfDs1": "",
            "poUdfDs2": "",
            "poUdfDs3": ""
          }
        ]
      },
      requestBuyDetail: {
        itemCode: '',
        itemName: '',
        expectQuantity: null,
        packDetails: [],
        packDetailId: null,
        transRatio: null,
        packDescribe: '',
        paymentInstruction:'',
        paymentMethod:'',
        listInvoiceFilePath:[],
        "poUdfDs1": "",
        "poUdfDs2": "",
        "poUdfDs3": "",
        priceIn: undefined,
        taxRateIn: undefined
      },
      rules: {
        organizationId: [
          { required: true, message: '请选择货主', trigger: 'blur' }
        ],
        supplierId: [
          { required: false, message: '请选择供应商', trigger: 'blur' }
        ],
        buyType: [
          { required: false, message: '请选择采购单类型', trigger: 'blur' }
        ],
        expectTime: [
          { required: true, message: '请选择预计到货日期', trigger: 'blur' }
        ],
        buyTotalPrices: [
          { required: true, message: '采购总价不能为空', trigger: 'blur' }
        ],
      },
      orderBillNo: this.$route.query.orderBillNo,

    }
  },
  computed: {
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
    organizationId() {
      return this.form.requestBuy.organizationId;
    },
    supplierId() {
      return this.form.requestBuy.supplierId
    },
  },
  watch: {
    organizationId(val) {
      for (let i = 0; i < this.organizations.length; i++) {
        if (this.organizations[i].organizationId === val) {
          this.form.requestBuy.organizationName = this.organizations[i].organizationName;
        }
      }
    },
    supplierId(val) {
      for (let i = 0; i < this.suppliers.length; i++) {
        if (this.suppliers[i].supplierId === val) {
          this.form.requestBuy.supplierName = this.suppliers[i].supplierName;
        }
      }
    },
  },
  methods: {
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getItemData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    getItemData() {
      this.loading.itemTable = true;
      IOT.getServerData('/item/infos/list', 'get', this.searchData, (ret) => {
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
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
      const isPDF = file.type === 'application/pdf';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isJPG && !isPDF) {
        this.$message.error('仅支持上传PNG/JPEG/JPG/PDF格式的文件');
        return false
      }
      if (!isLt10M) {
        this.$message.error('单一文件大小不超过10MB');
        return false
      }
      return isJPG || isPDF && isLt10M;
    },
    handleSuccess(response, file, fileList) {
      if (response.code != 200) {
        this.$message.error('上传出错：' + response.message);
        // 从文件列表中删除上传出错的文件
        const index = fileList.findIndex(item => item.uid === file.uid);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      } else {
        this.$message.success(file.name + '上传成功');
        this.form.requestBuy.listInvoiceFilePath = fileList.map(item => item.response.data.fullPath);
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning('最多只能上传5个文件');
      this.form.requestBuy.listInvoiceFilePath = fileList.map(item => item.response.data.fullPath);
    },
    handleRemove(file, fileList) {
      this.form.requestBuy.listInvoiceFilePath = fileList.map(item => item.response.data.fullPath);
    },
    handlePreview(file) {
      IOT.getServerData('/request/buys/selectInvoice', 'get', { fullPath: file.response.data.fullPath }, (res) => {
        if (res.code === 200) {
          window.open(res.data.url);
          IOT.tips('保存成功！', 'success');
        } else {
          IOT.tips(res.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$confirm('是否提交采购单？', '提示', {
            confirmButtonText: '提交',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.commitForm('form.requestBuy', 0);
          });
        } else {
          return false;
        }
      });
    },

    addRow() {
      this.form.requestBuyDetails.push(PF.JSON(this.requestBuyDetail));
    },
    deleteRow(index) {
      this.form.requestBuyDetails.splice(index, 1);
      this.ids.splice(index, 1);
    },
    showItemModal(index) {
      if (this.isOrganization) {
        if (!PF.isNull(this.form.requestBuy.organizationId)) {
          IOT.tips('请选择货主！', 'error');
          return false
        }
        this.searchData.organizationId = this.form.requestBuy.organizationId;
      } else {
        this.searchData.organizationId = null;
      }
      this.selectDetailIndex = index;
      this.getItemData();
      this.innerVisibleItem = true;
    },
    selectItem(row) {
      if (this.ids.indexOf(row.id) !== -1) {
        IOT.tips('相同商品不能重复添加！', 'error');
        this.innerVisibleItem = false;
        return false
      }
      IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
        if (ret.code === 200) {
          row.packDetails = [];
          for (let i = 0; i < ret.rows.length; i++) {
            row.packDetails.push({
              packDetailId: ret.rows[i].packDetailId,
              packDescribe: ret.rows[i].packDescribe,
              transRatio: ret.rows[i].transRatio,
            });
          }
          row.packDetailId = ret.rows[0].packDetailId;
          row.oldP = ret.rows[0].packDetailId;
          row.packDescribe = ret.rows[0].packDescribe;
          row.transRatio = ret.rows[0].transRatio;
          this.ids.push(row.id);
          for (let key in row) {
            this.form.requestBuyDetails[this.selectDetailIndex][key] = row[key];
          }
          this.innerVisibleItem = false;
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
          /*for(let j=0;j<this.wares.length;i++){
            if(this.wares[j].wareId === form.requestBuy.wareId){
              form.requestBuy.wareName = this.wares[j].wareName;
            }
          }*/
          for (let i = 0; i < form.requestBuyDetails.length; i++) {
            if (!PF.isNull(form.requestBuyDetails[i].id)) {
              IOT.tips('不得有空行！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.requestBuyDetails[i].itemCode)) {
              IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.requestBuyDetails[i].expectQuantity) || form.requestBuyDetails[i].expectQuantity <= 0) {
              IOT.tips('数量需大于零！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.requestBuyDetails[i].packDetailId)) {
              IOT.tips('未选择单位！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            // 主单位数量
            form.requestBuyDetails[i].expectQuantity = form.requestBuyDetails[i].expectQuantity * form.requestBuyDetails[i].transRatio;
          }
          form.insertType = insertType;
          IOT.getServerData('/request/buys/insert', 'post', form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              this.resetForm(formName);
              this.cleaerDetails();
              this.form.requestBuy.memo = '';
              IOT.tips('保存成功！', 'success');
              this.$router.push({ name: 'buyBillIn' })
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
        } else {
          IOT.hideOverlay();
          console.error('error submit!!');
          return false;
        }
      });
    },
    cleaerDetails() {
      this.ids = [];
      this.form.requestBuyDetails = [{
        itemCode: '',
        itemName: '',
        expectQuantity: null,
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
              this.form.requestBuy.organizationId = this.oldOrganizationId;
            });
        }
      }
    },
    clearItemCode(index) {
      this.form.requestBuyDetails[index] = this.requestBuyDetail;
      delete this.ids[index];
    },
    changeItemCode(itemCode, index) {
      const that = this;
      if (!PF.isNull(itemCode)) {
        return false
      }
      let searchData = {
        wareId: this.form.requestBuy.wareId,
        organizationId: this.form.requestBuy.organizationId,
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
            if (this.ids.indexOf(list[0].id) !== -1) {
              IOT.tips('相同商品不能重复添加！', 'error');
              IOT.hideOverlay();
              return false
            }

            let row = list[0];
            row.id=list[0].id;
            IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                row.packDetails = [];
                for (let i = 0; i < ret.rows.length; i++) {
                  row.packDetails.push({
                    packDetailId: ret.rows[i].packDetailId,
                    packDescribe: ret.rows[i].packDescribe,
                    transRatio: ret.rows[i].transRatio,
                  });
                }
                row.packDetailId = ret.rows[0].packDetailId;
                row.oldP = ret.rows[0].packDetailId;
                row.packDescribe = ret.rows[0].packDescribe;
                row.transRatio = ret.rows[0].transRatio;
                this.ids.push(row.id);
                for (let key in row) {
                  this.form.requestBuyDetails[index][key] = row[key];
                }
                this.innerVisibleItem = false;
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });

          } else {
            IOT.hideOverlay();
            IOT.tips('未查询到此SKU,请确认后输入！', 'error');
          }
        } else {
          IOT.hideOverlay();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    changePackDetailId(index) {
      let detail = PF.JSON(this.form.requestBuyDetails[index]);
      for (let i = 0; i < detail.packDetails.length; i++) {
        if (detail.packDetails[i].packDetailId === detail.packDetailId) {
          this.form.requestBuyDetails[index].packDescribe = detail.packDetails[i].packDescribe;
          this.form.requestBuyDetails[index].transRatio = detail.packDetails[i].transRatio;
        }
      }
      // this.form.requestBuyDetails[index].expectQuantity = PF.changePToChangeQ(this.form.requestBuyDetails[index]);
    },
    changeExpectQuantity(index) {
      let detail = PF.JSON(this.form.requestBuyDetails[index]);
      for (let i = 0; i < detail.packDetails.length; i++) {
        if (detail.packDetails[i].packDetailId === detail.packDetailId) {
          let transRatio = detail.packDetails[i].transRatio; // 当前
          if (transRatio > 0) {
            this.form.requestBuyDetails[index].oldQ = this.form.requestBuyDetails[index].expectQuantity * transRatio;
          }
        }
      }
    },


    getBuyBillInData() {

      IOT.getServerData('/request/buys/list', 'get', { billNo: this.orderBillNo }, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows[0];
          if (PF.isNull(list.createTime)) {
            list.createTimeStr = DateUtils.formatTime(list.createTime);
          }
          if (PF.isNull(list.expectTime)) {
            list.expectTimeStr = DateUtils.formatDay(list.expectTime);
          }
          this.form.requestBuy.wareId = list.wareId;
          this.form.requestBuy.wareName = list.wareName;
          this.form.requestBuy.organizationId = list.organizationId;
          this.form.requestBuy.organizationName = list.organizationName;
          this.form.requestBuy.supplierId = list.supplierId;
          this.form.requestBuy.supplierName = list.supplierName;
          this.form.requestBuy.expectTime = list.expectTime;
          this.form.requestBuy.buyType = list.buyType;
          this.form.requestBuy.memo = list.memo;
          this.form.requestBuy.poUdfHs1 = list.poUdfHs1;
          this.form.requestBuy.poUdfHs2 = list.poUdfHs2;
          this.form.requestBuy.poUdfHs3 = list.poUdfHs3;
          if(list.buyTotalPrices){
            this.form.requestBuy.buyTotalPrices = list.buyTotalPrices;
          }
          if(list.buyTotalPrices) {
            this.form.requestBuy.serviceCharge = list.serviceCharge;
          }
          if(list.buyTotalPrices){
            this.form.requestBuy.otherExpenses = list.otherExpenses;
          }
          this.form.requestBuy.paymentInstruction = list.paymentInstruction;
          this.form.requestBuy.paymentMethod = list.paymentMethod;
          this.form.requestBuy.listInvoiceFilePath = list.listInvoiceFilePath;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getBuyBillInDetailData() {
      const loading = this.$loading({
        lock: true,
        text: '正在复制，请稍后。。',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      var that = this;
      IOT.getServerData('/request/buy/details/list', 'get', { billNo: this.orderBillNo }, (ret) => {
        if (ret.code === 200) {
          this.form.requestBuyDetails = [];
          let list = ret.rows;
          for (let i = 0; i < list.length; i++) {
            IOT.getServerData('/item/infos/list', 'get', { itemCode: list[i].itemCode }, (ret) => {
              this.loading.itemTable = false;
              if (ret.code === 200 && ret.rows.length > 0) {
                IOT.getServerData('/pack/details/list', 'get', { packCode: list[i].packCode }, (ret2) => {
                  if (ret2.code === 200) {
                    let expectQuantity = list[i].expectQuantity / list[i].transRatio;
                    let poUdfDs1 = list[i].poUdfDs1;
                    let poUdfDs2 = list[i].poUdfDs2;
                    let poUdfDs3 = list[i].poUdfDs3;
                    let transRatio = list[i].transRatio;
                    let items = ret.rows;
                    this.requestBuyDetail.id = items[0].id;
                    this.requestBuyDetail = items[0];
                    this.requestBuyDetail.expectQuantity = expectQuantity;
                    this.requestBuyDetail.transRatio = transRatio;
                    this.requestBuyDetail.poUdfDs1 = poUdfDs1;
                    this.requestBuyDetail.poUdfDs2 = poUdfDs2;
                    this.requestBuyDetail.poUdfDs3 = poUdfDs3;
                    this.requestBuyDetail.paymentInstruction = list[i].paymentInstruction;
                    this.requestBuyDetail.paymentMethod = list[i].paymentMethod;
                    this.requestBuyDetail.listInvoiceFilePath = list[i].listInvoiceFilePath;
                    if(list[i].priceIn){
                      this.requestBuyDetail.priceIn = list[i].priceIn;
                    }
                    if(list[i].taxRateIn) {
                      this.requestBuyDetail.taxRateIn = list[i].taxRateIn;
                    }
                    this.ids.push(items[0].id);
                    this.requestBuyDetail.packDetails = ret2.rows;
                    this.requestBuyDetail.packDetailId = ret2.rows[0].packDetailId;
                    this.requestBuyDetail.packDescribe = ret2.rows[0].packDescribe;
                    if (this.requestBuyDetail.itemCode != '') {
                      this.form.requestBuyDetails.push(PF.JSON(this.requestBuyDetail));
                      this.clearRequestDetail();
                    }

                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }

                });


              }
            })
            if (list.length > 4 && list.length < 10) {
              setTimeout(() => {
                loading.close();
              }, 1500)
            } else if (list.length > 8) {
              setTimeout(() => {
                loading.close();
              }, 3000)
            } else {
              loading.close();
            }

          }
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
          var that = this;
        } else {
          loading.close();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    clearRequestDetail() {
      this.requestBuyDetail = {
        itemCode: '',
        itemName: '',
        expectQuantity: null,
        packDetails: [],
        packDetailId: null,
        transRatio: null,
        packDescribe: '',
        "poUdfDs1": "",
        "poUdfDs2": "",
        "poUdfDs3": ""
      }
    }

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
    // this.getWareData();
    PF.getSysParamUdfData('poUdfHs', [this.form.requestBuy], (data, udfs) => {
      this.form.requestBuy = data[0];
      this.poUdfHs = udfs;
    });
    PF.getSysParamUdfData('poUdfDs', [], (data, udfs) => {
      this.poUdfDs = udfs;
    });
    PF.getDataDictUdfData('buyBillInType', (rows) => {
      this.buyBillInTypes = rows;
    });
  },
  mounted() {
    if (this.orderBillNo != null) {
      this.getBuyBillInData();
      this.getBuyBillInDetailData();
    }
  }

}
 </script>

<style scoped lang="less">
.createBuyBillIn {
  padding-bottom: 100px;

  input:focus {
    outline: none; // 如何把蓝色去掉？
    border: 1px solid #30B101; // 如何改成别的颜色？
  }
    /*设置el-input-number中的placeholder中的字靠左*/
    /deep/.el-input-number.is-without-controls .el-input__inner {
        text-align:left;
    }
}
</style>
