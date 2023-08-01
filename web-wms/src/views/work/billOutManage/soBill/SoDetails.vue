<template>
  <div class="SoDetails" v-on:keyup.enter="getSoData">
    <div class="publicSearch">
      <div class="searchBar" :style="{height: 'auto', }">
        <div>
          <span>出库单单号</span>
          <el-input v-model="searchData.billNos" size="mini" placeholder="多个请使用，分隔" clearable style="width: calc(100% - 90px);"></el-input>
        </div>
        <div>
          <span>sku</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入sku" clearable ></el-input>
        </div>
          <div>
          <span>创建时间</span>
          <el-date-picker
              clearable
              style="width: 220px;"
              v-model="searchData.createTimeArr"
              size="mini"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div class="state">
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择状态" clearable class="so-bill-select">
            <el-option key=0 value=0 style="color:#909399" label="待处理"></el-option>
            <el-option key=1 value=1 style="color:#F56C6C" label="部分分配"></el-option>
            <el-option key=2 value=2 style="color:#409EFF" label="全部分配"></el-option>
            <el-option key=3 value=3 style="color:#E11111" label="部分拣货"></el-option>
            <el-option key=5 value=5 style="color:#543044" label="全部拣货"></el-option>
            <el-option key=7 value=7 style="color:#67C23A" label="部分发运"></el-option>
            <el-option key=6 value=6 style="color:#67C23A" label="完全发运"></el-option>
            <el-option key=-1 value=-1 style="color:#F56C6C" label="取消"></el-option>
          </el-select>
        </div>
        <el-button-group>
          <el-button @click="getSoData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
    
      </div>
    </div>
    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="soBills"
          @selection-change="handleSelectionChange"
          style="width: 100%">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
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
            min-width="130">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$router.push({name: 'soBillDetail',params: scope.row})">{{ scope.row.billNo }}</span>
          </template>
        </el-table-column>
          <!-- <el-table-column
            show-overflow-tooltip
            prop="shopDailyCount"
            label="订单日序号"
            min-width="100">
        </el-table-column> -->
        <el-table-column
            show-overflow-tooltip
            prop="soStructure"
            label="sku种类"
            min-width="80">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="state"
            label="状态"
            min-width="80">
          <template slot-scope="scope">

            <span v-if="scope.row.state==0" style="color:#909399">待处理</span>
            <span v-if="scope.row.state==1" style="color:#F56C6C">部分分配</span>
            <span v-if="scope.row.state==2" style="color:#409EFF">全部分配</span>
            <span v-if="scope.row.state==3" style="color:#E11111">部分拣货</span>
            <span v-if="scope.row.state==5" style="color:#543044">全部拣货</span>
            <span v-if="scope.row.state==7" style="color:#67C23A">部分发运</span>
            <span v-if="scope.row.state==6" style="color:#67C23A">完全发运</span>
            <span v-if="scope.row.state==-1" style="color:#909399">取消</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
            show-overflow-tooltip
            prop="masterState"
            label="订单状态"
            min-width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.masterState==1" style="color:#909399">待处理</span>
            <span v-if="scope.row.masterState==2" style="color:#909399">待处理</span>
            <span v-if="scope.row.masterState==3" style="color:#F56C6C">缺货</span>
            <span v-if="scope.row.masterState==4" style="color:#409EFF">已分配</span>
            <span v-if="scope.row.masterState==5" style="color:#E11111">部分拣货</span>
            <span v-if="scope.row.masterState==6" style="color:#543044">拣货完成</span>
            <span v-if="scope.row.masterState==7" style="color:#E6A23C">已复核</span>
            <span v-if="scope.row.masterState==8" style="color:#67C23A">已发运</span>
            <span v-if="scope.row.masterState==-1" style="color:#909399">取消</span>
            <span v-if="scope.row.masterState==-2" style="color:#909399">关闭</span>
          </template>
        </el-table-column> -->
        <el-table-column
            show-overflow-tooltip
            prop="createTime"
            label="创建时间"
            min-width="100">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="memo"
            label="备注"
            min-width="180">
        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination-bottom"
          style="text-align: right;"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page.sync="searchData.pageNum"
          :page-size="searchData.pageSize"
          :page-sizes="[15, 30, 50, 100]"
          layout="sizes,total, prev, pager, next, jumper"
          :total="searchData.total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
import {mapState} from "vuex";
import moment from "moment";

export default {
  name: 'SoDetails',
  data() {
    return {
      loading: {
        masterTable: true,
      },
        wareId: PF.getLocal('wareId', 'number'),
      unLoadData: {
        model: "",
        wareId: 111,
        insertType: 111
      },
      beforOneMonth: moment().subtract(30, "days").format('YYYY-MM-DD'),
      nowDate:moment().add(1, "days").format('YYYY-MM-DD'),
      searchData: {
        pageNum: 1,
        pageSize: 100,
        total: null,
        keyWords: '', 
        wareId: PF.getLocal('wareId', 'number'),
        state: null,
        customerId: null,
        billNos: '',
        mergeNo: '',
        sku:'',
        createTimeArr: [],
        startCreateTime: '',
        endCreateTime: '',
      },
      soBills: [],
      formLabelWidth: '120px',
      soUdfHs: [],
      organizations: [],
      customers: [],
      soBillTypes: [],
      carriers: [],
      multipleSelection: [],
      dialogVisible: false,
      dialogVisible2soa: false,
    }
  },
  computed: {
    ...mapState([
      "reviewStates",
    ]),
    mergeFlg() {
      let list = PF.JSON(this.multipleSelection);
      let flg = false;
      if (list.length > 1) {
        for (let i = 0; i < list.length; i++) {
          if (list[i].wareId !== list[0].wareId
              || list[i].customerId !== list[0].customerId
              || list[i].receivingAddressId !== list[0].receivingAddressId
              || list[i].billSource === 2
              || list[i].mergeNo !== null) {

            flg = true;
          }
        }
        return flg;
      } else {
        return true;
      }
    }
  },
  methods: {
    adjustLevel(soMasterId) {
      IOT.getServerData('/so/masters/priority', 'get', {soMasterId}, (ret) => {
        if (ret.code === 200) {
          IOT.tips('优先级调整成功！', 'success');
          this.getSoData();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getSoData();
    },
    downloadModel() {
      window.open('/model/SoImport.xls');
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getSoData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      this.searchData.state = null;
      this.searchData.customerId = null;
      this.searchData.billNos = '';
      this.searchData.mergeNo = '';
      this.searchData.itemCode = '';
      this.searchData.startCreateTime =  this.beforOneMonth;
      this.searchData.endCreateTime =this.nowDate;
      this.searchData.createTimeArr = [this.beforOneMonth,this.nowDate];
    },
    //查询出库单详情列表
    getSoData() {
      let searchData = PF.JSON(this.searchData);
      if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) {
        searchData.startCreateTime = searchData.createTimeArr[0];
        searchData.endCreateTime = searchData.createTimeArr[1];
      }
      if (!searchData.billNos || !searchData.billNos.trim()) {
        searchData.billNos = undefined;
      } else {
        const billNoList = searchData.billNos.split(',');
        const newBillNoList = [];
        for (const billNo of billNoList) {
          const nBillNo = +billNo;
          if (!Number.isInteger(nBillNo)) {
            newBillNoList.push(billNo);
          } else {
            newBillNoList.push(SoPrefix.SO + nBillNo);
            newBillNoList.push(SoPrefix.SO8000 + nBillNo);
            newBillNoList.push(SoPrefix.SO9000 + nBillNo);
          }
        }
        searchData.billNos = newBillNoList.join(',');
      }
      let that = this;
      that.loading.masterTable = true;
      IOT.getServerData('/so/details/listAndMaster', 'get', searchData, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {

          this.soBills = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
          IOT.hideOverlay();
        } else {
            IOT.hideOverlay();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },


  },
  created() {
    this.searchData.startCreateTime =  this.beforOneMonth;
    this.searchData.endCreateTime =this.nowDate;
    this.searchData.createTimeArr=[this.searchData.startCreateTime,this.searchData.endCreateTime]
    PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
      this.soUdfHs = udfs;
    });
    this.getSoData();
    PF.getCarriers((rows) => {
      this.carriers = rows;
    })
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    PF.getCustomers((rows) => {
      this.customers = rows;
    });
    PF.getDataDictUdfData('soBillType', (rows) => {
      this.soBillTypes = rows;
    });
  },
  mounted() {
  }
}
</script>

<style  lang="less">
.SoDetails {
  .state > div > div.el-input{
    width:160px;
  }
}
</style>
