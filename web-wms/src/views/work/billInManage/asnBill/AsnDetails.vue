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
          <span>sku</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入sku" clearable ></el-input>
        </div>

        <!-- <div>
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
        </div> -->
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
          <span>收货状态</span>
          <el-select
              v-model="searchData.state"
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
      </div>
      <span
          class="moreSearchBtn"
          :class="
                    isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'
                "
          @click="isMoreSearch = !isMoreSearch"
      ></span>
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
            show-overflow-tooltip
            prop="itemCode"
            label="sku"
            min-width="100">
        </el-table-column>

        <el-table-column
            show-overflow-tooltip
            prop="billNo"
            label="单号"
            width="150"
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
            prop="state"
            label="收货状态"
        >
          <template slot-scope="scope"
          > 
            <span
                v-if="scope.row.state == 0"
                style="color: #909399"
            >待收货</span
            >
            <span
                v-if="scope.row.state == 1"
                style="color: #909399"
            >已码盘</span
            >
            <span
                v-if="scope.row.state == 2"
                style="color: #543044"
            >部分收货</span
            >
            <span
                v-if="scope.row.state == 3"
                style="color: #67c23a"
            >全部收货</span
            >
            <span
                v-if="scope.row.state == 4"
                style="color: #67c23a"
            >ASN完成</span
            >
             <span
                v-if="scope.row.state == 5"
                style="color: red"
            >超额收货</span
            >
          </template>
        </el-table-column>
        <!-- <el-table-column
            show-overflow-tooltip
            prop="asnState"
            label="订单收货状态"
        >
          <template slot-scope="scope"
          > 
            <span
                v-if="scope.row.asnState == 0"
                style="color: #909399"
            >待收货</span
            >
            <span
                v-if="scope.row.asnState == 1"
                style="color: #909399"
            >已码盘</span
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
                v-if="scope.row.asnState == 4"
                style="color: #67c23a"
            >ASN完成</span
            >
             <span
                v-if="scope.row.asnState == 5"
                style="color: red"
            >超额收货</span
            >
          </template>
        </el-table-column> -->
        <!-- <el-table-column
            show-overflow-tooltip
            prop="organizationName"
            label="货主"
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
        </el-table-column> -->
        <el-table-column
  show-overflow-tooltip
  prop="supplierName"
  label="供应商">
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
        >
        </el-table-column>
        <el-table-column
  show-overflow-tooltip
  prop="expectTime"
  label="预期到货时间">
  <template slot-scope="scope">
    <span>{{scope.row.expectTimeStr}}</span>
    
  </template>
</el-table-column>
        <el-table-column
  show-overflow-tooltip
  prop="memo"
  label="备注">
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
        state:null,
        asnState: null,
        itemCode:"",
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
      this.searchData.state = null;
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
      this.searchData.itemCode = "";
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
      IOT.getServerData("/asn/details/list", "get", searchData, (ret) => {
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
