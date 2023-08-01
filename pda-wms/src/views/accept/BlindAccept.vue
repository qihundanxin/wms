<template>
  <div v-loading="loading" class="blindAccept2">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div style="font-size: 14px;height: 14px; line-height: 14px; color: #F56C6C;text-align: center;">*盲收数量10件及以上时，需要WMS后台审核</div>
    <template  v-if="organizationId == null">
      <el-button @click="commitOrg(item)"
        style="display:block ;width:80%;margin:10px 0px 0px 10%" v-for="item in organizations" type="primary">{{
          item.organizationName
        }}</el-button>
    </template>
    <div v-if="organizationId != null && form == null" class="scanInput">
      <el-input clearable placeholder="请扫描UPC或者SKU" id="itemCode" v-model="itemCode"></el-input>
      <i class="el-icon-camera" @click="scan(1)"></i>
    </div>
    <div v-if="organizationId != null && form != null" class="scanInput">
      <el-input clearable placeholder="请扫描验收货位" id="cellCode" v-model="cellCode"></el-input>
      <i class="el-icon-camera" @click="scan(1)"></i>
    </div>
    <div v-if="organizationId != null && form != null && cellCode != null">
      <el-select v-model="form.toPackDetailId" @change="changePack()" filterable placeholder="请选择单位" style="width:100%">
        <el-option v-for="item in form.packDetails" :key="item.packDetailId" :label="item.packDescribe"
          :value="item.packDetailId">
        </el-option>
      </el-select>
    </div>
    <br />
    <div v-if="organizationId != null && form != null && cellCode != null" class="scanInput">
      <el-input clearable placeholder="请输入数量" id="quantity" v-model="quantity"></el-input>
      <div style="font-size: 12px;height: 12px; color: #F56C6C;text-align: center;">* 超过10件时需要审核</div>
    </div>
    <div v-if="organizationId != null && form != null">
      <div class="skuDetail" v-loading="loading" element-loading-text="拼命加载中">
        <div class="top">
          <div class="left">
            <img :src="form.imgUrl" alt="">
          </div>
          <div class="right">
            <div>SKU：{{ form.itemCode }}</div>
            <div v-if="form.upcs != null && form.upcs.length > 0">UPC：{{ form.upcs[0].upc }}</div>
          </div>
        </div>
        <div class="bottom">{{ form.itemName }}</div>
      </div>
      <div class="cellInfos">
        <template v-if="hasCellUseType1">
          <el-divider content-position="left">拣货位：</el-divider>
          <div v-for="itemCellInfo in itemCellInfos">
            <div v-if="itemCellInfo.cellUseType==1" style="display: flex;">
              <div style="width: 60%;">{{ itemCellInfo.cellCode }}</div>
              <div style="width: 40%;text-align: right;">{{ itemCellInfo.quantity + '(' + (itemCellInfo.quantity - itemCellInfo.allotQuantity - itemCellInfo.freezeQuantity) + ')' }}</div>
            </div>
          </div>
        </template>
        <template v-if="hasCellUseType2">
        <el-divider content-position="left">仓储位：</el-divider>
          <div v-for="itemCellInfo in itemCellInfos">
            <div v-if="itemCellInfo.cellUseType==2" style="display: flex;">
              <div style="width: 60%;">{{ itemCellInfo.cellCode }}</div>
              <div style="width: 40%;text-align: right;">{{ itemCellInfo.quantity + '(' + (itemCellInfo.quantity - itemCellInfo.allotQuantity - itemCellInfo.freezeQuantity) + ')' }}</div>
            </div>
          </div>
        </template>
        <template v-if="!hasCellUseType1 && !hasCellUseType2">
          <span v-if="itemCellInfosLoading">正在查询库存...</span>
          <span v-else>在拣货位、仓储位无库存记录</span>
        </template>
      </div>
    </div>
    <div class="featuresFooter">
      <div class="footerBox1">
        <el-button type="warning" @click="clear()">清空</el-button>
      </div>
      <div class="footerBox">
        <el-button type="primary" @click="commitForm">{{ footerBtn }}</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
import { mapState } from "vuex";
export default {
  name: "BlindAccept2",
  data() {
    let validateItemCode = (rule, value, callback) => {
      this.validator(rule, value, callback, 'item_info', 'item_code', this.form.itemCode, 0);
    };
    let validateCellCode = (rule, value, callback) => {
      this.validator(rule, value, callback, 'cell_info', 'cell_code', this.form.toCellCode, 0);
    };
    return {
      loading: false,
      headerTitle: "盲收",
      footerBtn: "验收",
      formLabelWidth: '120px',
      params: this.$route.params,
      organizationId: null,
      organizationName: null,
      cellCode: null,
      itemCode: null,
      quantity: null,
      form: null,
      itemCellInfos: [],
      itemCellInfosLoading: false,
      wareId: PF.getLocal('wareId', 'number'),
      wareName: PF.getLocal('wareName'),
      searchSelectCell: {
        pageNum: 1,
        pageSize: 5,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        areaId: '',
        cellUseType: 0,
      },
      cells: [],
      organizations: [],
      suppliers: [],
      activeName: 'first',
      batchTactics: {},
      packDetails: [],
      isSerialNo: false,
      isAuto: false,
      serialNo: '',
      serialNos: [],
      rules: {
        wareId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        organizationId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        itemCode: [
          { required: true, message: '请输入商品编码', trigger: 'blur' },
          { validator: validateItemCode, trigger: 'blur' }
        ],
        toCellCode: [
          { required: true, message: '请输入货位编码', trigger: 'blur' },
          { validator: validateCellCode, trigger: 'blur' }
        ],
        toPackDetailId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        toQuantity: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        toBoxCode: [
          { required: false, message: '请输入追踪号', trigger: 'blur' }
        ],
        'productDate': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'exDate': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'inDate': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'supplierCode': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'supplierBatch': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'qcState': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'costPrice': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'salePrice': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'detailNo': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'memo1': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'memo2': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'memo3': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'batchAttribute1': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
        'batchAttribute2': [
          { required: false, message: '必填项不得为空', trigger: 'blur' }
        ],
      },
    };
  },
  watch: {
    itemCode: function (newVal, oldVal) {
      let qrObject = PF.simpleQrEncode(newVal);
      if (qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
        this.itemCode = qrObject.index2;
        return;
      }
      setTimeout(() => {
        if (this.itemCode == newVal && newVal && newVal.length > 1) {
          this.getItemData(newVal);
        }
      }, 200);
    },
  },
  computed: {
    ...mapState([
      "qcStates",
    ]),
    hasCellUseType1(){
      return this.itemCellInfos.filter(x => x.cellUseType === 1).length > 0;
    },
    hasCellUseType2(){
      return this.itemCellInfos.filter(x => x.cellUseType === 2).length > 0;
    },
  },
  methods: {
    changePack() {
      var that = this;
      setTimeout(function () {
        for (var i = 0; i < that.form.packDetails.length; i++) {
          if (that.form.toPackDetailId == that.form.packDetails[i].packDetailId) {
            that.form.toPackDetailId = that.form.packDetails[i].packDetailId;
            that.form.toTransRatio = that.form.packDetails[i].transRatio;
            that.form.toPackDescribe = that.form.packDetails[i].packDescribe;
            return;
          }
        }
      }, 100);
    },
    clear() {
      this.form = null;
      this.organizationId = null;
      this.cellCode = null;
      this.itemCode = null;
      this.quantity = null;
    },
    clear2() {
      this.form = null;
      this.cellCode = null;
      this.itemCode = null;
      this.quantity = null;
      setTimeout(function () {
        var domFocus = document.getElementById("itemCode");
        domFocus.focus();
      }, 100);
    },
    commitOrg(item) {
      this.organizationId = item.organizationId;
      this.organizationName = item.organizationName;
      setTimeout(function () {
        var domFocus = document.getElementById("itemCode");
        domFocus.focus();
      }, 100);
    },
    querySearchMethod(queryString, cb) {
      let that = this;
      var solList = []
      that.cells.forEach(item => {
        solList.push({ 'label': item.cellCode, 'value': item.cellCode })
      })
      var results = queryString
        ? solList.filter(this.createFilterMethod(queryString))
        : solList;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilterMethod(queryString) {
      let that = this;
      var cells = that.cells;
      return cells => {
        if (cells.label.toLowerCase().indexOf(queryString.toLowerCase()) === 0) {
          return cells.label
        }
      };
    },
    handleSelectMethod(item) {
      console.log(item);
    },
    validator(rule, value, callback, tableName, fieldName, fieldValue, type, idValue) {
      let form = {
        tableName: tableName, // 表名
        fieldName: fieldName, // 字段名
        fieldValue: fieldValue, // 字段值
        type: type, // 0-新增 1-更新
        idValue: idValue, // type为0时不传，type为1是必传，id值
      };
      IOT.getServerData('/validator', 'post', form, (ret) => {
        if (ret.code === 100001 || ret.code === 100002 || ret.code === 100004) {
          callback();
        } else if (ret.code === 100003) {
          callback(new Error('不存在'));
        } else {
          PF.publicMessage(ret.message || '服务器请求失败，稍后再试！', 'error');
          callback();
        }
      });
    },
    // 查询SKU
    getItemData(val) {
      var that = this;
      if (val != that.itemCode) {
        return;
      }
      let searchSelectItem = {
        itemCode: val.trim(),
        organizationId: that.organizationId
      }
      that.loading = true;
      IOT.getServerData('/item/infos/findBYCode', 'get', searchSelectItem, (ret) => {
        that.loading = false;
        if (ret.code === 200) {
          if (ret.data == null) {
            that.itemCode = "";
            return;
          }
          var item = ret.data;
          this.form = item;
          // 查询当前SKU的库存货位
          if (item.itemCode) {
            this.itemCellInfosLoading = true;
            this.itemCellInfos = [];
            IOT.getServerData('/inventorys/list/mergeQtyBySkuAndCell', 'get', {
              wareId: this.wareId,
              itemCode: item.itemCode
            }, (invResponse) => {
              console.log(invResponse)
              if (invResponse.code === 200 && item.itemCode === this.form.itemCode) {
                this.itemCellInfosLoading = false;
                if (invResponse.rows && invResponse.rows.length > 0) {
                  this.itemCellInfos = invResponse.rows;
                } else {
                  this.itemCellInfos = [];
                }
              }
            })
          }
          setTimeout(function () {
            var domFocus = document.getElementById("cellCode");
            domFocus.focus();
          }, 100);
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    getCellData() {
      IOT.getServerData('/cell/infos/list', 'get', this.searchSelectCell, (ret) => {
        if (ret.code === 200) {
          this.cells = ret.rows;
          this.searchSelectCell.total = ret.total;
          this.searchSelectCell.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    handleClose(tag) {
      this.serialNos.splice(this.serialNos.indexOf(tag), 1);
    },
    setSerialNo() {
      if (!PF.isNull(this.serialNo)) {
        IOT.tips('输入框为空！', 'error');
      } else if (this.serialNos.length < this.form.toQuantity) {
        this.serialNos.push(this.serialNo);
        this.serialNo = '';
        document.querySelector('#serialNo').focus();
      } else {
        IOT.tips('所有数量都已录入,请增加数量！', 'error');
        this.serialNo = '';
      }
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
    },
    Number(val) {
      if (parseFloat(val).toString() == "NaN") {
        return false;
      } else {
        return true;
      }
    },
    commitForm(formName) {
      let that = this;
      if (this.organizationId == null) {
        IOT.tips("请选择货主", 'error');
        return;
      }
      if (this.itemCode == null) {
        IOT.tips("请扫描SKU/UPC", 'error');
        return;
      }
      if (this.form == null) {
        IOT.tips("请扫描正确的SKU/UPC", 'error');
        this.itemCode = null;
        return;
      }
      if (this.cellCode == null) {
        IOT.tips("请扫描货位", 'error');
        return;
      }
      if (this.form.toPackDetailId == null) {
        IOT.tips("请选择单位", 'error');
        return;
      }
      if (!this.Number(this.quantity)) {
        IOT.tips("请校验数量", 'error');
        return;
      }
      if(this.quantity<=0){
          IOT.tips( '验收数量至少为1', 'error');
          return;
        }
      if (true) {
        let form = PF.JSON(this.form);
        form.wareId = this.wareId;
        form.wareName = this.wareName;
        form.organizationId = this.organizationId;
        form.organizationName = this.organizationName;
        form.billType = 3;
        form.toQuantity = this.quantity;
        form.toCellCode = this.cellCode;
        IOT.showOverlay('保存中，请稍等...');
        IOT.getServerData('/accept/records/acceptList', 'post', { acceptInserts: [form] }, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.clear2();
            IOT.tips('验收成功！', 'success', 1000, () => {
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      };
    },
    isCommit(val) {
      if (JSON.stringify(this.form) === '{}') {
        IOT.tips('请选择需要验收的商品！', 'error');
      } else if (JSON.stringify(this.batchTactics) === '{}') {
        IOT.tips('请填写批次策略！', 'error');
      } else if (val) {
        this.commitForm('form');
      }
    }
  },
  created() {
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    // PF.getSuppliers((rows) => {
    //   this.suppliers = rows;
    // });
    this.getCellData();
  },
  components: {
    FeaturesHeader,
  }
};
</script>
<style lang="less" scoped>
.blindAccept2 {
  padding-bottom: 90px;

  .skuDetail {
    border-bottom: 1px solid #ccc;

    .top {
      display: flex;
      justify-content: space-around;
      align-items: center;

      .left {
        width: 120px;
        height: 120px;

        img {
          width: 100%;
          height: 100%;
        }
      }

      .right {
        line-height: 25px;

        .otherWare {
          text-decoration: underline;
          color: #2b7eb4;
          cursor: pointer;
        }
      }
    }

    .bottom {
      margin: 5px 0 8px 5px;
    }
  }

  .cellInfos{
    margin: 10px;
    min-height: 100px;
  }

  .featuresFooter {
    .footerBox {
      z-index: 99;
      position: fixed;
      bottom: 20px;
      width: 50%;
      right: 0;
      text-align: right;
      border-top: 1px solid #e4e7ed;

      button {
        width: 100%;
        border-radius: 0;
      }
    }

    .footerBox1 {
      z-index: 99;
      background: #fff;
      position: fixed;
      width: 50%;
      bottom: 20px;
      left: 0;
      text-align: left;
      border-top: 1px solid #e4e7ed;

      button {
        width: 100%;
        border-radius: 0;
      }
    }

    .bottomBackground {
      background: #e6e6e6;

      .bottomHj {
        height: 40px;
        line-height: 40px;
        margin-right: 25px;
      }
    }
  }
}
</style>
