<template>
  <div class="buyBillIn">
    <div class="header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>采购单</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="publicSearch">
      <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>采购单号</span>
          <el-input v-model="searchData.billNo" size="mini" placeholder="采购单号" clearable></el-input>
        </div>
        <div>
          <span>审核状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择审核状态" clearable>
            <el-option v-for="(item, index) in reviewStates" :key="index" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option v-for="(item, index) in organizations" :key="index" :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" clearable>
            <el-option v-for="(item, index) in suppliers" :key="index" :label="item.supplierName" :value="item.supplierId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>收货状态</span>
          <el-select v-model="searchData.acceptState" size="mini" filterable placeholder="请选择收货状态" clearable>
            <el-option v-for="(item, index) in asnStates" :key="index" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>采购类型</span>
          <el-select v-model="searchData.buyType" size="mini" filterable placeholder="请选择采购单类型" clearable>
            <el-option v-for="(item, index) in buyBillInTypes" :key="index" :label="item.dictValue"
              :value="item.dataDictDetailId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker v-model="searchData.createTimeArr" size="mini" type="daterange" value-format="yyyy-MM-dd"
            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>预计到货时间</span>
          <el-date-picker v-model="searchData.expectTimeArr" size="mini" type="daterange" value-format="yyyy-MM-dd"
            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="创建者/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>
    <div class="headerBtns">
      <el-button size="mini" type="primary" icon="iconfont icon-xinzeng" @click="$router.push({ name: 'createBuyBillIn' })">
        新增
      </el-button>
      <el-button size="mini" type="primary" icon="iconfont icon-xinzeng"
        @click="$router.push({ path: '/work/billInManage/buyBillIn/create', query: { orderBillNo: orderBillNo } })"
        :disabled="isAnotherOrderBtn"> 再来一单
      </el-button>
      <el-button class="rightFloat" size="small" type="info" plain icon="iconfont icon-backup" @click="commitFile"
        :disabled="true"> 归档
        <!--   :disabled="multipleSelection.length <= 0"> 归档 -->
      </el-button>
    </div>
    <div class="tableBox">
      <el-table v-loading="loading.masterTable" highlight-current-row :data="buyBillIns"
        @selection-change="handleSelectionChange" @sort-change="sortChange" style="width: 100%">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column fixed="left" show-overflow-tooltip prop="billNo" label="单号" width="120">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({ name: 'buyBillInDetail', params: scope.row })">{{ scope.row.billNo
            }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="buyType" label="类型" width="80">
          <template slot-scope="scope">
            <span v-for="(item, index) in buyBillInTypes" :key="index"
              v-if="scope.row.buyType === item.dataDictDetailId">{{ item.dictValue }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="state" label="审核状态" width="80">
          <template slot-scope="scope">
            <span v-for="(item, index) in reviewStates" :key="index" v-if="scope.row.state === item.value"
              :type="item.type">{{ item.label }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="acceptState" label="收货状态" width="100">
          <template slot-scope="scope">
            <span v-for="(item, index) in asnStates" :key="index" v-if="scope.row.acceptState === item.value"
              :type="item.type">{{ item.label }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="organizationName" label="货主" width="160">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{
              scope.row.organizationName
            }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="supplierName" label="供应商" width="160">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.supplierId, 'supplier')">{{
              scope.row.supplierName }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="createTime" label="创建时间" width="170">
          <template slot-scope="scope">
            {{ scope.row.createTime}}
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="createUserName" label="创建人" width="80">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{
              scope.row.createUserName }}</span>
          </template>
        </el-table-column>
        <el-table-column sortable="custom" show-overflow-tooltip prop="expectTime" label="预期到货" width="100">
          <template slot-scope="scope">
            {{ scope.row.expectTimeStr }}
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in poUdfHs" :key="index" :prop="item.paramCode"
          :label="item.value" width="130">
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="forceCompletionReason" label="强制完成原因" width="150">
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="memo" label="备注" width="130">
        </el-table-column>
      </el-table>
      <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="handleCurrentChange"
        @size-change="handleSizeChange" :current-page.sync="searchData.pageNum" :page-sizes="[15, 30, 50, 100]"
        :page-size="searchData.pageSize" layout="sizes,total, prev, pager, next" :total="searchData.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import DateUtils from "@/utils/DateUtils";

export default {
  name: 'BuyBillIn',
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
        keyWords: '',
        insertType: 0,
        wareId: PF.getLocal('wareId', 'number'),
        state: null,
        organizationId: null,
        supplierId: null,
        buyType: null,
        acceptState: null,
        billNo: '',
        createTimeArr: [],
        expectTimeArr: [],
        startCreateTime: '',
        endCreateTime: '',
        startExpectTime: '',
        endExpectTime: '',
        expectTimeSort: '',
      },
      buyBillIns: [],
      formLabelWidth: '120px',
      poUdfHs: [],
      buyBillInTypes: [],
      multipleSelection: [],
      organizations: [],
      suppliers: [],
      orderBillNo: ''
    }
  },
  computed: {
    ...mapState([
      "reviewStates",
      "asnStates",
    ]),
    isAnotherOrderBtn() {
      if (this.multipleSelection.length == 1) {
        this.orderBillNo = this.multipleSelection[0].billNo;
        console.log(this.orderBillNo);
        return false
      }
      return true
    },
  },
  methods: {
    sortChange(column) {
      if (column.order == "ascending") {
        this.searchData.expectTimeSort = "ASC";
      } else {
        this.searchData.expectTimeSort = "DESC"
      }
      this.getBuyBillInData();
    },
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getBuyBillInData();
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getBuyBillInData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      this.searchData.state = null;
      this.searchData.organizationId = null;
      this.searchData.supplierId = null;
      this.searchData.buyType = null;
      this.searchData.billNo = '';
      this.searchData.createTimeArr = [];
      this.searchData.expectTimeArr = [];
      this.searchData.startCreateTime = '';
      this.searchData.endCreateTime = '';
      this.searchData.startExpectTime = '';
      this.searchData.endExpectTime = '';
      this.searchData.acceptState = null;
    },
    setPageNumFlush(){
      this.searchData.pageNum =1;
      this.getBuyBillInData();
    },
    getBuyBillInData() {
      let that = this;
      that.loading.masterTable = true;
      let searchData = PF.JSON(this.searchData);
      if (searchData.createTimeArr != null && searchData.createTimeArr.length > 0) {
        searchData.startCreateTime = searchData.createTimeArr[0];
        searchData.endCreateTime = searchData.createTimeArr[1];
      }
      if (searchData.expectTimeArr != null && searchData.expectTimeArr.length > 0) {
        searchData.startExpectTime = searchData.expectTimeArr[0];
        searchData.endExpectTime = searchData.expectTimeArr[1];
      }
      IOT.getServerData('/request/buys/list', 'get', searchData, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {
          ret.rows.map(row => row.expectTimeStr = DateUtils.formatDay(row.expectTime, row.createTime = DateUtils.formatM(row.createTime)));
          this.buyBillIns = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    commitFile() {
      IOT.showOverlay('保存中，请稍等...');
      let list = PF.JSON(this.multipleSelection);
      let billNos = [];
      for (let i = 0; i < list.length; i++) {
        billNos.push(list[i].billNo)
      }
      IOT.getServerData('/request/buys/file', 'post', { billNos: billNos }, (ret) => {
        IOT.hideOverlay();
        this.multipleSelection = [];
        if (ret.code === 200) {
          IOT.tips('归档成功！', 'success');
          this.getBuyBillInData();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    }
  },
  created() {
    this.getBuyBillInData();
    PF.getSysParamUdfData('poUdfHs', [], (data, udfs) => {
      this.poUdfHs = udfs;
    });
    PF.getDataDictUdfData('buyBillInType', (rows) => {
      this.buyBillInTypes = rows;
    });
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    PF.getSuppliers((rows) => {
      this.suppliers = rows;
    });
  },
  mounted() {
  },


}
</script>

<style scoped lang="less">
.buyBillIn {}
</style>
