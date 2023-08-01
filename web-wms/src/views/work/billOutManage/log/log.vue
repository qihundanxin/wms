<template>
  <div class="buyBillIn">
    <div class="header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>出库管理</el-breadcrumb-item>
        <el-breadcrumb-item>出库日志</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getBuyBillInData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>sku</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="请输入sku" clearable></el-input>
        </div>
        <!--        <div>-->
        <!--          <span>审核状态</span>-->
        <!--          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择审核状态" clearable>-->
        <!--            <el-option-->
        <!--                v-for="item in reviewStates"-->
        <!--                :key="item.value"-->
        <!--                :label="item.label"-->
        <!--                :value="item.value">-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>货主</span>-->
        <!--          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>-->
        <!--            <el-option-->
        <!--                v-for="item in organizations"-->
        <!--                :key="item.organizationId"-->
        <!--                :label="item.organizationName"-->
        <!--                :value="item.organizationId">-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>供应商</span>-->
        <!--          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" clearable>-->
        <!--            <el-option-->
        <!--                v-for="item in suppliers"-->
        <!--                :key="item.supplierId"-->
        <!--                :label="item.supplierName"-->
        <!--                :value="item.supplierId">-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>采购类型</span>-->
        <!--          <el-select v-model="searchData.buyType" size="mini" filterable placeholder="请选择采购单类型" clearable>-->
        <!--            <el-option-->
        <!--                v-for="item in buyBillInTypes"-->
        <!--                :key="item.dataDictDetailId"-->
        <!--                :label="item.dictValue"-->
        <!--                :value="item.dataDictDetailId">-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>创建时间</span>-->
        <!--          <el-date-picker-->
        <!--              v-model="searchData.createTimeArr"-->
        <!--              size="mini"-->
        <!--              type="daterange"-->
        <!--              value-format="yyyy-MM-dd"-->
        <!--              range-separator="至"-->
        <!--              start-placeholder="开始日期"-->
        <!--              end-placeholder="结束日期">-->
        <!--          </el-date-picker>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>预计到货时间</span>-->
        <!--          <el-date-picker-->
        <!--              v-model="searchData.expectTimeArr"-->
        <!--              size="mini"-->
        <!--              type="daterange"-->
        <!--              value-format="yyyy-MM-dd"-->
        <!--              range-separator="至"-->
        <!--              start-placeholder="开始日期"-->
        <!--              end-placeholder="结束日期">-->
        <!--          </el-date-picker>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span>关键字搜索</span>-->
        <!--          <el-input v-model="searchData.keyWords" size="mini" placeholder="自定义字段/创建者/备注" clearable></el-input>-->
        <!--        </div>-->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>
    <!--    <div class="headerBtns">-->
    <!--      <el-button size="small" type="primary" icon="iconfont icon-xinzeng"-->
    <!--                 @click="$router.push({name: 'createBuyBillIn'})"> 新增-->
    <!--      </el-button>-->
    <!--      <el-button class="rightFloat" size="small" type="info" plain icon="iconfont icon-backup" @click="commitFile"-->
    <!--                 :disabled="multipleSelection.length <= 0"> 归档-->
    <!--      </el-button>-->
    <!--    </div>-->
    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="buyBillIns"
          @selection-change="handleSelectionChange"
          style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table
                :data="props.row.items"
                style="width: 100%">
              <el-table-column
                  label="sku编号/中文名称"
              >
                <template slot-scope="scope">
                  <div>{{ scope.row.itemCode }} / {{ scope.row.itemName }}</div>
                </template>
              </el-table-column>
              <el-table-column
                  label="状态"
              >
                <template slot-scope="scope">
                  <div>{{ parseState(scope.row.status) }}</div>
                </template>
              </el-table-column>
              <el-table-column
                  label="数量"
              >
                <template slot-scope="scope">
                  <div>{{ scope.row.quantity }}</div>
                </template>
              </el-table-column>
              <el-table-column
                  label="已配货量"
              >
                <template slot-scope="scope">
                  <div>{{ scope.row.deliveryQuantity }}</div>
                </template>
              </el-table-column>
              <el-table-column
                  label="仓库"
              >
                <template slot-scope="scope">
                  <div>{{ scope.row.wareName }}</div>
                </template>
              </el-table-column>
              <el-table-column
                  label="仓位"
              >
                <template slot-scope="scope">
                  <div>{{ scope.row.cellCode }}</div>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <!--        <el-table-column-->
        <!--            type="selection"-->
        <!--            width="55">-->
        <!--        </el-table-column>-->
        <el-table-column
            fixed="left"
            show-overflow-tooltip
            label="订单编号"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.billNo }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="篮子编号"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.pickCellCode }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="称重重量"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.weight }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="店铺/仓库"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.shop }} / {{ scope.row.wareName }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="状态/类型"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.status == 1 ? '完成' : '' }} / {{ scope.row.soStructure > 1 ? '多品' : '单品' }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="创建人/时间"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.createUserName }} / {{ scope.row.createTime | timestamp2str }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="配货人/时间"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.pickUserName }} / {{ scope.row.pickTime | timestamp2str }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="打包人/时间"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.outUserName }} / {{ scope.row.outTime | timestamp2str }}</div>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            label="发货人/时间"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.outUserName }} / {{ scope.row.outTime | timestamp2str }}</div>
          </template>
        </el-table-column>
        <!--        <el-table-column-->
        <!--            sortable-->
        <!--            show-overflow-tooltip-->
        <!--            v-for="(item,index) in poUdfHs"-->
        <!--            :key="index"-->
        <!--            :prop="item.paramCode"-->
        <!--            :label="item.value"-->
        <!--            width="130">-->
        <!--        </el-table-column>-->
        <el-table-column
            show-overflow-tooltip
            label="物流信息/单号"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.shipCode }} /{{ scope.row.shipBillCode }}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination-bottom"
          style="text-align: right;"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page.sync="searchData.pageNum"
          :page-sizes="[10, 15, 20, 25]"
          :page-size="searchData.pageSize"
          layout="sizes,total, prev, pager, next"
          :total="searchData.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'outLog',
  data() {
    return {
      isMoreSearch: false,
      loading: {
        masterTable: true,
      },
      searchData: {
        pageNum: 35,
        pageSize: 15,
        total: null,
        // keyWords: '',
        // insertType: 0,
        // wareId: PF.getLocal('wareId', 'number'),
        // state: null,
        // organizationId: null,
        // supplierId: null,
        // buyType: null,
        // acceptState: null,
        // billNo: '',
        // createTimeArr: [],
        // expectTimeArr: [],
        // startCreateTime: '',
        // endCreateTime: '',
        // startExpectTime: '',
        // endExpectTime: '',
        itemName: ''
      },
      buyBillIns: [],
      formLabelWidth: '120px',
      poUdfHs: [],
      buyBillInTypes: [],
      multipleSelection: [],
      organizations: [],
      suppliers: [],
    }
  },
  computed: {
    ...mapState([
      "reviewStates",
      "asnStates",
    ])
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getBuyBillInData();
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getBuyBillInData()
    },
    clearSearchData() {
      // this.searchData.keyWords = '';
      // this.searchData.state = null;
      // this.searchData.organizationId = null;
      // this.searchData.supplierId = null;
      // this.searchData.buyType = null;
      // this.searchData.billNo = '';
      // this.searchData.createTimeArr = [];
      // this.searchData.expectTimeArr = [];
      // this.searchData.startCreateTime = '';
      // this.searchData.endCreateTime = '';
      // this.searchData.startExpectTime = '';
      // this.searchData.endExpectTime = '';
      this.searchData.itemName = ''
    },
    getBuyBillInData() {
      let that = this;
      that.loading.masterTable = true;
      let searchData = PF.JSON(this.searchData);
      //  if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) {
      //   searchData.startCreateTime = searchData.createTimeArr[0];
      //   searchData.endCreateTime = searchData.createTimeArr[1];
      // }
      // if (searchData.expectTimeArr !=null && searchData.expectTimeArr.length > 0) {
      //   searchData.startExpectTime = searchData.expectTimeArr[0];
      //   searchData.endExpectTime = searchData.expectTimeArr[1];
      // }
      IOT.getServerData('/so/log/list', 'post', searchData, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {
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
      IOT.getServerData('/request/buys/file', 'post', {billNos: billNos}, (ret) => {
        IOT.hideOverlay();
        this.multipleSelection = [];
        if (ret.code === 200) {
          IOT.tips('归档成功！', 'success');
          this.getBuyBillInData();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    parseState(status) {
      switch (status) {
        case 1:
          return '完成'
        default:
          return ''
      }
    }
  },
  created() {
    this.getBuyBillInData();
    // PF.getSysParamUdfData('poUdfHs', [], (data, udfs) => {
    //   this.poUdfHs = udfs;
    // });
    // PF.getDataDictUdfData('buyBillInType', (rows) => {
    //   this.buyBillInTypes = rows;
    // });
    // PF.getOrganizations((rows) => {
    //   this.organizations = rows;
    // });
    // PF.getSuppliers((rows) => {
    //   this.suppliers = rows;
    // });
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
