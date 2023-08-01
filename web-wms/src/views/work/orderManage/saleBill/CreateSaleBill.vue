<template>
  <div class="createSaleBill">

    <el-dialog width="900px" title="选择物料" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

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
          <div>
            <span style="font-size: 15px; margin-right: 10px; margin-left: 20px">仅查询有库存</span>
            <el-switch v-model="searchItemInventoryLimit" active-value="1" inactive-value="0"
              @change="switchStateChanged"></el-switch>
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
            <template slot-scope="scope">
              <span class="textBtn"
                @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemName }}</span>
            </template>
          </el-table-column>
          <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU" width="180">
            <template slot-scope="scope">
              <span class="textBtn"
                @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemCode }}</span>
            </template>
          </el-table-column>
          <el-table-column show-overflow-tooltip label="库存数量" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.one - (scope.row.two + scope.row.three) }}</span>
            </template>
          </el-table-column>
          <el-table-column show-overflow-tooltip prop="packDescribe" label="单位" width="180">
          </el-table-column>
          <!--<el-table-column
              sortable
              show-overflow-tooltip
              prop="organizationName"
              label="货主"
              width="180">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
            </template>
          </el-table-column>-->
        </el-table>
        <el-pagination style="text-align: right;" @current-change="handleCurrentChange"
          :current-page.sync="searchData.pageNum" :page-size="searchData.pageSize"
          layout="total, prev, pager, next, jumper" :total="searchData.total">
        </el-pagination>
      </div>

    </el-dialog>

    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">销售单信息</p>
        <el-form class="clearFloat" :model="form.saleMaster" :rules="rules" ref="form.saleMaster">
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
              style="width: 100%;">
              <el-option :key="form.saleMaster.wareId" :label="form.saleMaster.wareName"
                :value="form.saleMaster.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.organizationId" @change="changeOrganizationId" filterable
              placeholder="请选择货主" size="small" style="width: 100%;">
              <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="客户" prop="customerId" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.customerId" filterable placeholder="请选择客户" size="small"
              style="width: 100%;">
              <el-option v-for="item in customers" :key="item.customerId" :label="item.customerName"
                :value="item.customerId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="销售类型" prop="saleType" :label-width="formLabelWidth">
            <el-select v-model="form.saleMaster.saleType" filterable placeholder="请选择销售类型" size="small"
              style="width: 100%;">
              <el-option v-for="item in saleTypes" :key="item.dataDictDetailId" :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="预期到货时间" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker
              v-model="form.saleMaster.expectTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              size="small"
              placeholder="请选择预期到货时间">
            </el-date-picker>
          </el-form-item>-->
          <el-form-item label="预期到货日期" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker v-model="form.saleMaster.expectTime" type="date" value-format="yyyy-MM-dd" size="small"
              placeholder="请选择预期到货日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.saleMaster.memo" placeholder="请输入备注" size="small"></el-input>
          </el-form-item>
          <el-form-item v-for="(item, index) in saleUdfHs" :key="index" :label="item.value"
            :label-width="formLabelWidth">
            <el-input v-model="form.saleMaster[item.paramCode]" :placeholder="'请输入' + item.value" size="small"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table highlight-current-row :data="form.saleDetails" style="width: 100%">
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
              @clear="clearItemCode(scope.$index)" :disabled="isOrganization && (form.saleMaster.organizationId === '' || form.saleMaster.organizationId === undefined || form.saleMaster.organizationId === null
              || form.saleDetails[scope.$index].id > 0)" placeholder="请输入SKU" size="small" clearable
              style="width: 80%;"></el-input>
            <el-button @click="showItemModal(scope.$index)" icon="iconfont icon-secondsearch_accurate" type="primary"
              circle plain size="mini"></el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemName" label="SKU名称" width="180">
        </el-table-column>
        <el-table-column show-overflow-tooltip label="可销售数量" width="180">
          <template slot-scope="scope" v-if="scope.row.orderQuantityMax">
            <span>{{ Math.floor(scope.row.orderQuantityMax / scope.row.transRatio * 10000) / 10000 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="销售数量" width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.expectQuantity" @blur="handleBlur(scope)"
              :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
              placeholder="请输入数量" style="width: 80%;">
            <!--            <el-input-number v-model="scope.row.expectQuantity" @change="changeExpectQuantity(scope.$index)" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" controls-position="right" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
          </template>
        </el-table-column>
        <el-table-column prop="transRatio" label="主单位" width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.expectQuantity * scope.row.transRatio }} / {{ scope.row.transRatio }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="itemTypeName" label="单位" width="180">
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
              :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
              filterable placeholder="请选择单位" size="small">
              <el-option v-for="item in scope.row.packDetails" :key="item.packDetailId" :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in saleUdfDs" :key="index" :prop="item.paramCode"
          :label="item.value" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row[item.paramCode]"
              :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
              :placeholder="'请输入' + item.value" size="small" clearable></el-input>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="memo" label="备注" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.memo"
              :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
              placeholder="请输入备注" size="small" clearable></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="createFootBox clearFloat" :style="{ width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)' }">
      <div class="leftFloat">
        <el-button type="info" @click="$router.push({ name: 'saleBill' })">返 回</el-button>
      </div>
      <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <!-- <el-button @click="commitForm('form.saleMaster',-2)" type="info" plain>保存为草稿</el-button> -->
        <el-button @click="commitForm('form.saleMaster', 0)" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'CreateSaleBill',
  data() {
    return {
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
        hasInventoryLimit: 0,
      },

      buyBillIns: [],
      organizations: [],
      customers: [],
      items: [],
      ids: [],
      saleUdfHs: [],
      saleUdfDs: [],
      saleTypes: [],

      isOrganization: true,
      selectDetailIndex: null,
      oldOrganizationId: null,

      innerVisibleItem: false,

      formLabelWidth: '120px',

      form: {
        insertType: 0,
        billSource: 1,
        saleMaster: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          organizationId: null,
          organizationName: '',
          customerId: null,
          customerName: '',
          expectTime: '',
          saleType: null,
          memo: '',
          "saleUdfHs1": "",
          "saleUdfHs2": "",
          "saleUdfHs3": ""
        },
        saleDetails: [
          {
            itemCode: '',
            itemName: '',
            expectQuantity: null,
            // oldQ: 0,
            packDetails: [],
            packDetailId: null,
            transRatio: null,
            oldP: null,
            packDescribe: '',
            "saleUdfDs1": "",
            "saleUdfDs2": "",
            "saleUdfDs3": ""
          }
        ]
      },
      saleDetail: {
        itemCode: '',
        itemName: '',
        expectQuantity: null,
        packDetails: [],
        packDetailId: null,
        transRatio: null,
        packDescribe: '',
        "saleUdfDs1": "",
        "saleUdfDs2": "",
        "saleUdfDs3": ""
      },
      rules: {
        organizationId: [
          { required: true, message: '请选择货主', trigger: 'blur' }
        ],
        customerId: [
          { required: true, message: '请选择客户', trigger: 'blur' }
        ],
        saleType: [
          { required: true, message: '请选择销售类型', trigger: 'blur' }
        ],
        expectTime: [
          { required: true, message: '预计到货时间不得为空', trigger: 'blur' }
        ],
      },

      searchItemInventoryLimit: 0
    }
  },
  computed: {
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
    organizationId() {
      return this.form.saleMaster.organizationId
    },
    customerId() {
      return this.form.saleMaster.customerId
    },
  },
  watch: {
    organizationId(val) {
      for (let i = 0; i < this.organizations.length; i++) {
        if (this.organizations[i].organizationId === val) {
          this.form.saleMaster.organizationName = this.organizations[i].organizationName;
        }
      }
    },
    customerId(val) {
      for (let i = 0; i < this.customers.length; i++) {
        if (this.customers[i].customerId === val) {
          this.form.saleMaster.customerName = this.customers[i].customerName;
        }
      }
    },
  },
  methods: {
    handleBlur(scope) {
      if (scope.row.expectQuantity < 0) {
        scope.row.expectQuantity = 0;
      }
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getItemData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    getItemData() {
      this.loading.itemTable = true;
      IOT.getServerData('/inventorys/SumList', 'get', this.searchData, (ret) => {
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
    addRow() {
      this.form.saleDetails.push(PF.JSON(this.saleDetail));
    },
    deleteRow(index) {
      this.form.saleDetails.splice(index, 1);
      this.ids.splice(index, 1);
    },
    showItemModal(index) {
      if (this.isOrganization) {
        if (!PF.isNull(this.form.saleMaster.organizationId)) {
          IOT.tips('请选择货主！', 'error');
          return false
        }
        this.searchData.organizationId = this.form.saleMaster.organizationId;
      } else {
        this.searchData.organizationId = null;
      }
      this.selectDetailIndex = index;
      this.getItemData();
      this.innerVisibleItem = true;
    },
    selectItem(row) {
      if (this.ids.indexOf(row.itemName) !== -1) {
        IOT.tips('相同物料不能重复添加！', 'error');
        this.innerVisibleItem = false;
        return false
      }
      row.orderQuantityMax = row.one - (row.two + row.three);
      IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
        if (ret.code === 200) {
          row.packDetails = ret.rows;
          row.packDetailId = ret.rows[0].packDetailId;
          row.packDescribe = ret.rows[0].packDescribe;
          row.transRatio = ret.rows[0].transRatio;
          this.ids.push(row.itemName);
          for (let key in row) {
            this.form.saleDetails[this.selectDetailIndex][key] = row[key];
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
          for (let i = 0; i < form.saleDetails.length; i++) {
            // if(!PF.isNull(form.soDetails[i].id)){
            if (!PF.isNull(form.saleDetails[i].itemName)) {
              IOT.tips('不得有空行！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.saleDetails[i].itemCode)) {
              IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            form.saleDetails[i].orderQuantity = Number(form.saleDetails[i].orderQuantity);
            if (!PF.isNull(form.saleDetails[i].orderQuantity) && form.saleDetails[i].orderQuantity <= 0) {
              IOT.tips('订货数量需大于零！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            if (!PF.isNull(form.saleDetails[i].packDetailId)) {
              IOT.tips('未选择单位！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }

            // 主单位数量
            form.saleDetails[i].expectQuantity = form.saleDetails[i].expectQuantity * form.saleDetails[i].transRatio;
            if (form.saleDetails[i].orderQuantity > form.saleDetails[i].orderQuantityMax) {
              IOT.tips('输入数量不得大于可库存数量！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
          }
          form.insertType = insertType;
          IOT.getServerData('/sale/masters/insert', 'post', form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success');
              this.$router.push({ name: 'saleBill' })
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
          this.resetForm(formName);
          this.cleaerDetails();
          this.form.saleMaster.memo = '';
        } else {
          IOT.hideOverlay();
          console.error('error submit!!');
          return false;
        }
      });
    },
    cleaerDetails() {
      this.ids = [];
      this.form.saleDetails = [{
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
          this.$confirm('修改货主已选物料将清空？')
            .then(_ => {
              this.oldOrganizationId = val;
              this.cleaerDetails();
            })
            .catch(_ => {
              this.form.saleMaster.organizationId = this.oldOrganizationId;
            });
        }
      }
    },
    clearItemCode(index) {
      this.form.saleDetails[index] = this.saleDetail;
      delete this.ids[index];
    },
    changeItemCode(itemCode, index) {
      let that = this;
      if (!PF.isNull(itemCode)) {
        return false
      }
      let searchData = {
        wareId: this.form.saleMaster.wareId,
        organizationId: this.form.saleMaster.organizationId,
        itemCode: itemCode,
      };
      if (!that.isOrganization) {
        delete searchData.organizationId;
      }
      IOT.showOverlay('查询中，请稍等...');
      IOT.getServerData('/inventorys/SumList', 'get', searchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          if (list.length === 1) {
            if (this.ids.indexOf(list[0].itemName) !== -1) {
              IOT.tips('相同商品不能重复添加！', 'error');
              IOT.hideOverlay();
              return false
            }

            let row = list[0];
            IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                that.ids.push(row.itemName);
                row.packDetails = ret.rows;
                console.log( ret.rows);
                row.packDetailId = ret.rows[0].packDetailId;
                row.orderQuantityMax = row.one - (row.two + row.three);
                row.transRatio = ret.rows[0].transRatio;
                row.packDescribe = ret.rows[0].packDescribe;
                row.packDetailId = ret.rows[0].packDetailId;
                row.packCode = row.packCode;
                for (let key in row) {
                  console.log("key");
                  console.log(key);
                  that.form.saleDetails[index][key] = row[key];
                }
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });

          } else {
            IOT.hideOverlay();
            IOT.tips('未查询到此商品编码,请确认后输入！', 'error');
          }
        } else {
          IOT.hideOverlay();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    changePackDetailId(index) {
      let detail = PF.JSON(this.form.saleDetails[index]);
      for (let i = 0; i < detail.packDetails.length; i++) {
        if (detail.packDetails[i].packDetailId === detail.packDetailId) {
          this.form.saleDetails[index].packDescribe = detail.packDetails[i].packDescribe;
          this.form.saleDetails[index].transRatio = detail.packDetails[i].transRatio;
        }
      }
      // this.form.saleDetails[index].expectQuantity = PF.changePToChangeQ(this.form.saleDetails[index]);
    },
    /*changeExpectQuantity(index){
      let detail = PF.JSON(this.form.saleDetails[index]);
      for(let i=0;i<detail.packDetails.length;i++){
        if(detail.packDetails[i].packDetailId === detail.packDetailId){
          let transRatio = detail.packDetails[i].transRatio; // 当前
          if(transRatio > 0){
            this.form.saleDetails[index].oldQ = this.form.saleDetails[index].expectQuantity * transRatio;
          }
        }
      }
    },*/
    switchStateChanged() {
      this.searchData.hasInventoryLimit = this.searchItemInventoryLimit
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
    PF.getCustomers((rows) => {
      this.customers = rows;
    });
    PF.getSysParamUdfData('saleUdfHs', [this.form.saleMaster], (data, udfs) => {
      this.form.saleMaster = data[0];
      this.saleUdfHs = udfs;
    });
    PF.getSysParamUdfData('saleUdfDs', [], (data, udfs) => {
      this.saleUdfDs = udfs;
    });
    PF.getDataDictUdfData('saleType', (rows) => {
      this.saleTypes = rows;
    });
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.createSaleBill {
  padding-bottom: 100px;

  input:focus {
    outline: none; // 如何把蓝色去掉？
    border: 1px solid #30B101; // 如何改成别的颜色？
  }
}
</style>
