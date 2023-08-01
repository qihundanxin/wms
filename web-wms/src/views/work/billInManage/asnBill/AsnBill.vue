<template>
  <div class="asnBill">
    <div class="publicSearch">
      <div
          class="searchBar"
          :style="{ height: isMoreSearch ? 'auto' : '' }"
      >
        <el-button-group>
          <el-button
              @click="setPageNumFlush"
              size="mini"
              icon="el-icon-search"
          >查询
          </el-button
          >
          <el-button
              @click="clearSearchData"
              size="mini"
              icon="el-icon-refresh"
          >重置
          </el-button
          >
        </el-button-group>
        <div>
          <span>入库单单号</span>
          <el-input
              v-model="searchData.billNo"
              size="mini"
              placeholder="入库单单号"
              clearable
          ></el-input>
        </div>
        <div>
          <span>收货状态</span>
          <el-select
              v-model="searchData.asnState"
              size="mini"
              filterable
              placeholder="请选择收货状态"
              clearable
          >
            <el-option
                v-for="(item,index) in asnStates"
                :key="index"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>审核状态</span>
          <el-select
              v-model="searchData.state"
              size="mini"
              filterable
              placeholder="请选择审核状态"
              clearable
          >
            <el-option
                v-for="(item,index) in reviewStates"
                :key="index"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select
              v-model="searchData.organizationId"
              size="mini"
              filterable
              placeholder="请选择货主"
              clearable
          >
            <el-option
                v-for="(item,index) in organizations"
                :key="index"
                :label="item.organizationName"
                :value="item.organizationId"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>供应商</span>
          <el-select
              v-model="searchData.supplierId"
              size="mini"
              filterable
              placeholder="请选择供应商"
              clearable
          >
            <el-option
                v-for="(item,index) in suppliers"
                :key="index"
                :label="item.supplierName"
                :value="item.supplierId"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>单据来源</span>
          <el-select
              v-model="searchData.billSource"
              size="mini"
              filterable
              placeholder="请选择单据来源"
              clearable
          >
            <!-- <el-option  label="采购单"  >全部</el-option>
<el-option :key="0" label="采购单" :value="0">采购单</el-option>
<el-option :key="1" label="新建" :value="1">新建</el-option>
<el-option :key="2" label="调拨" :value="2">ERP导入</el-option>
<el-option :key="4" label="调拨" :value="4">调拨</el-option> -->
            <el-option
                v-for="(item,index) in billSourceOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>入库单类型</span>
          <el-select
              v-model="searchData.asnType"
              size="mini"
              filterable
              placeholder="请选择入库单类型"
              clearable
          >
            <el-option
                v-for="(item,index) in asnTypes"
                :key="index"
                :label="item.dictValue"
                :value="item.dataDictDetailId"
            >
            </el-option>
          </el-select>
        </div>
        <!-- <div>
          <span>月台</span>
          <el-select
              v-model="searchData.platformCode"
              size="mini"
              filterable
              placeholder="请选择月台"
              clearable
          >
            <el-option
                v-for="(item,index) in platforms"
                :key="index"
                :label="item.label"
                :value="item.platformCode"
            >
            </el-option>
          </el-select>
        </div> -->
        <div>
          <span>承运方</span>
          <el-select
              v-model="searchData.shipCode"
              size="mini"
              filterable
              placeholder="请选择承运方"
              clearable
          >
            <el-option
                v-for="(item,index) in carriers"
                :key="index"
                :label="item.carrierName"
                :value="item.carrierCode"
            >
            </el-option>
          </el-select>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker
              v-model="searchData.createTimeArr"
              size="mini"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          >
          </el-date-picker>
        </div>
        <div>
          <span>预计到货时间</span>
          <el-date-picker
              v-model="searchData.expectTimeArr"
              size="mini"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          >
          </el-date-picker>
        </div>
        <div>
          <span>优先级</span>
          <el-input
              v-model="searchData.orderNo"
              size="mini"
              placeholder="请输入优先级"
              clearable
          ></el-input>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input
              style="width:330px"
              v-model="searchData.keyWords"
              size="mini"
              placeholder="发货地址/交货地址/运输描述/自定义字段/创建人/备注"
              clearable
          ></el-input>
        </div>
      </div>
      <span
          class="moreSearchBtn"
          :class="
                    isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'
                "
          @click="isMoreSearch = !isMoreSearch"
      ></span>
    </div>
    <div class="headerBtns">
      <el-button
          size="mini"
          type="primary"
          icon="iconfont icon-xinzeng"
          @click="$router.push({ name: 'createAsnBill' })"
      >
        新增
      </el-button
      >
      <el-button
          class="rightFloat"
          size="small"
          type="info"
          plain
          icon="iconfont icon-backup"
          @click="commitFile"
          :disabled="multipleSelection.length <= 0"
      >
        归档
      </el-button
      >
    </div>
    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="asnBills"
          @selection-change="handleSelectionChange"
          style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="billNo"
            label="单号"
            width="120"
        >
          <template slot-scope="scope">
                        <span
                            class="textBtn"
                            @click="
                                $router.push({
                                    name: 'asnBillDetail',
                                    params: scope.row,
                                })
                            "
                        >{{ scope.row.billNo }}</span
                        >
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="asnState"
            label="收货状态"
            width="100"
        >
          <template slot-scope="scope"
          > 
            <span
                v-if="scope.row.asnState == 0"
                style="color: #909399"
            >待收货</span
            >
            <span
                v-if="scope.row.asnState == 2"
                style="color: #543044"
            >部分收货</span
            >
            <span
                v-if="scope.row.asnState == 3"
                style="color: #67c23a"
            >全部收货</span
            >
             <span
                v-if="scope.row.asnState == 5"
                style="color: red"
            >超额收货</span
            >
          </template>
        </el-table-column>
        <!-- <el-table-column
  show-overflow-tooltip
  prop="billNo"
  label="单号"
  width="180">
  <template slot-scope="scope">
    <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
  </template>
</el-table-column>
  <el-table-column
  show-overflow-tooltip
  prop="asnState"
  label="收货状态"
    width="100">
  <template slot-scope="scope">{{scope.row.asnState}}
      <span  v-if="scope.row.asnState==0" style="color:#909399">待收货</span>
                <span  v-if="scope.row.asnState==2"  style="color:#543044"  >部分验收</span>
                <span  v-if="scope.row.asnState==3"  style="color:#67C23A"  >全部验收</span>
  </template>
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="requestBuyBillNo"
  label="采购单号"
  width="100">
  <template slot-scope="scope">
    <span v-if="scope.row.requestBuyBillNo === null">无</span>
    <span v-else class="textBtn" @click="$router.push({name: 'buyBillInDetail',params: {billNo: scope.row.requestBuyBillNo}})">{{scope.row.requestBuyBillNo}}</span>
  </template>
</el-table-column> -->
        <!--    <el-table-column
  show-overflow-tooltip
  prop="billSource"
  label="单据来源"
    width="80">
  <template slot-scope="scope">
      <el-tag v-if="scope.row.billSource === 0" type="info">采购单</el-tag>
      <el-tag v-else-if="scope.row.billSource === 1">新建</el-tag>
      <el-tag v-else-if="scope.row.billSource === 4">调拨</el-tag>
  </template>
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="asnType"
  label="入库单类型"
  width="80">
  <template slot-scope="scope">
    <span v-for="(item,index) in asnTypes" :key="index" v-if="scope.row.asnType === item.dataDictDetailId">{{item.dictValue}}</span>
  </template>
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="state"
  label="审核状态"
    width="80">
  <template slot-scope="scope">
    <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.state === item.value" :type="item.type">{{item.label}}</el-tag>
  </template>
</el-table-column> -->
        <!-- <el-table-column
            show-overflow-tooltip
            prop="asnState"
            label="收货状态"
            width="80"
        >
          <template slot-scope="scope">
            <el-tag
                v-for="(item, index) in asnStates"
                :key="index"
                v-if="scope.row.asnState === item.value"
                :type="item.type"
            >{{ item.label }}
            </el-tag
            >
          </template>
        </el-table-column> -->
        <!--<el-table-column
    show-overflow-tooltip
    prop="qcState"
    label="质检状态"
    width="80">
  <template slot-scope="scope">
    <el-tag v-for="(item,index) in qcStates" :key="index" v-if="scope.row.qcState === item.value" :type="item.type">{{item.label}}</el-tag>
  </template>
</el-table-column> -->
        <el-table-column
            show-overflow-tooltip
            prop="organizationName"
            label="货主"
            min-width="120"
        >
          <template slot-scope="scope">
                        <span
                            class="textBtn"
                            @click="
                                $parent.showPublicModal(
                                    scope.row.organizationId,
                                    'organization'
                                )
                            "
                        >{{ scope.row.organizationName }}</span
                        >
          </template>
        </el-table-column>
        <el-table-column
  show-overflow-tooltip
  prop="supplierName"
  label="供应商"
  width="120">
  <template slot-scope="scope">
    <span class="textBtn" @click="$parent.showPublicModal(scope.row.supplierId, 'supplier')">{{scope.row.supplierName}}</span>
  </template>
</el-table-column>
        <!-- <el-table-column
  show-overflow-tooltip
  prop="platformCode"
  label="月台编码"
  width="100">
</el-table-column> -->
        <!-- <el-table-column
  show-overflow-tooltip
  prop="carrierName"
  label="承运方"
  min-width="120">
</el-table-column> -->
        <el-table-column
            show-overflow-tooltip
            prop="createTime"
            label="创建时间"
            min-width="160"
        >
          <template slot-scope="scope">
            {{ scope.row.createTime.substr(0, 16) }}
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="createUserName"
            label="创建人"
            min-width="100"
        >
          <template slot-scope="scope">
                        <span
                            @click="
                                $parent.showPublicModal(
                                    scope.row.createUserId,
                                    'account'
                                )
                            "
                            type="text"
                        >{{ scope.row.createUserName }}</span
                        >
          </template>
        </el-table-column>
        <el-table-column
  show-overflow-tooltip
  prop="expectTime"
  label="预期到货时间"
  min-width="110">
  <template slot-scope="scope">
    <span>{{scope.row.expectTimeStr}}</span>
    
  </template>
</el-table-column>
        <el-table-column
  show-overflow-tooltip
  prop="orderNo"
  label="优先级"
  width="80">
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="loadingPlace"
  label="发货地址"
  width="180">
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="deliveryPlace"
  label="交货地址"
  width="180">
</el-table-column>
<el-table-column
  show-overflow-tooltip
  prop="trafficDescribe"
  label="运输描述"
  width="180">
</el-table-column>  
        <el-table-column
  show-overflow-tooltip
  v-for="(item,index) in asnUdfHs"
  :key="index"
  :prop="item.paramCode"
  :label="item.value"
  width="180">
</el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="memo"
            label="备注"
            min-width="180"
        >
        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination-bottom"
          style="text-align: right"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page.sync="searchData.pageNum"
          :page-sizes="[5, 10, 15, 20, 25]"
          :page-size="searchData.pageSize"
          layout="sizes,total, prev, pager, next"
          :total="searchData.total"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import {mapState} from "vuex";
import DateUtils from "@/utils/DateUtils";

export default {
  name: "AsnBill",
  data() {
    return {
      isMoreSearch: false,
      loading: {
        masterTable: true,
      },
      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: "",
        wareId: PF.getLocal("wareId", "number"),
        state: null,
        organizationId: null,
        supplierId: null,
        carrierId: null,
        asnState: null,
        billNo: "",
        billSource: "",
        asnType: "",
        platformCode: "",
        orderNo: "",
        createTimeArr: [],
        expectTimeArr: [],
        startCreateTime: "",
        endCreateTime: "",
        startExpectTime: "",
        endExpectTime: "",
      },
      asnBills: [],
      platforms: [],
      carriers: [],
      formLabelWidth: "120px",
      asnUdfHs: [],
      organizations: [],
      suppliers: [],
      multipleSelection: [],
      asnTypes: [],
      billSourceOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "0",
          label: "采购单",
        },
        {
          value: "1",
          label: "新建",
        },
        {
          value: "2",
          label: "ERP导入",
        },
        {
          value: "4",
          label: "调拨",
        },
      ],
    };
  },
  computed: {
    ...mapState(["reviewStates", "asnStates", "qcStates"]),
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getAsnData();
    },

    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getAsnData();
    },
    clearSearchData() {
      this.searchData.keyWords = "";
      this.searchData.state = null;
      this.searchData.organizationId = null;
      this.searchData.supplierId = null;
      this.searchData.carrierId = null;
      this.searchData.asnState = null;
      this.searchData.billNo = "";
      this.searchData.billSource = "";
      this.searchData.asnType = "";
      this.searchData.platformCode = "";
      this.searchData.orderNo = "";
      this.searchData.createTimeArr = [];
      this.searchData.expectTimeArr = [];
      this.searchData.startCreateTime = "";
      this.searchData.endCreateTime = "";
      this.searchData.startExpectTime = "";
      this.searchData.endExpectTime = "";
    },
    setPageNumFlush(){
      this.searchData.pageNum =1;
      this.getAsnData();
    },
    getAsnData() {
      var that = this;
      let searchData = PF.JSON(this.searchData);
      if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0)  {
        searchData.startCreateTime = searchData.createTimeArr[0];
        searchData.endCreateTime = searchData.createTimeArr[1];
      }
      if (searchData.expectTimeArr !=null && searchData.expectTimeArr.length > 0) {
        searchData.startExpectTime = searchData.expectTimeArr[0];
        searchData.endExpectTime = searchData.expectTimeArr[1];
      }
      if(!searchData.asnType && !searchData.asnTypeIn && !searchData.asnTypeNotIn){
        searchData.asnTypeNotIn = 44;// 排除退货单
      }
      that.loading.masterTable = true;
      IOT.getServerData("/asn/masters/list", "get", searchData, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {
          ret.rows.map(row => row.expectTimeStr = DateUtils.formatDay(row.expectTime));
          this.asnBills = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    getPlatformData() {
      IOT.getServerData(
          "/platforms/findByWareId",
          "get",
          {wareId: PF.getLocal("wareId", "number")},
          (ret) => {
            if (ret.code === 200) {
              this.platforms = ret.rows;
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          }
      );
    },
    getCarrierData() {
      IOT.getServerData("/carriers/allList", "get", {}, (ret) => {
        if (ret.code === 200) {
          this.carriers = ret.rows;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    resetForm(formName) {
      // 重置
      this.$refs[formName].resetFields();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    commitFile() {
      IOT.showOverlay("保存中，请稍等...");
      let list = PF.JSON(this.multipleSelection);
      let billNos = [];
      for (let i = 0; i < list.length; i++) {
        billNos.push(list[i].billNo);
      }
      IOT.getServerData(
          "/asn/masters/file",
          "post",
          {billNos: billNos},
          (ret) => {
            IOT.hideOverlay();
            this.multipleSelection = [];
            if (ret.code === 200) {
              IOT.tips("归档成功！", "success");
              this.getAsnData();
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          }
      );
    },
  },
  created() {
    this.getAsnData();
    this.getPlatformData();
    this.getCarrierData();
    PF.getSysParamUdfData("asnUdfHs", [], (data, udfs) => {
      this.asnUdfHs = udfs;
    });
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    PF.getSuppliers((rows) => {
      this.suppliers = rows;
    });
    PF.getDataDictUdfData("asnType", (rows) => {
      this.asnTypes = rows;
    });
    PF.getCarriers((rows) => {
      this.carriers = rows;
    });
  },
  mounted() {
  },
};
</script>

<style scoped lang="less">
.asnBill {
}
</style>
