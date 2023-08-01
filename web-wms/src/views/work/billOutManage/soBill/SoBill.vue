<template>
  <div class="soBill" v-on:keyup.enter="getSoData">
    <div class="publicSearch">
      <div class="searchBar" :style="{ height: 'auto', }">
        <div style="width: 40%;">
          <span>单号</span>
          <el-input v-model="searchData.billNos" size="mini" placeholder="请使用,分割单号" clearable
            style="width: calc(100% - 50px);"></el-input>
        </div>
        <div>
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="状态" clearable class="so-bill-select">
            <el-option key=1 value=1 style="color:#909399" label="待处理"></el-option>
            <!-- <el-option key=2 value=2 style="color:#909399" label="待处理"></el-option> -->
            <el-option key=3 value=3 style="color:#F56C6C" label="缺货"></el-option>
            <el-option key=4 value=4 style="color:#409EFF" label="已分配"></el-option>
            <el-option key=6 value=6 style="color:#543044" label="拣货完成"></el-option>
            <el-option key=7 value=7 style="color:#E6A23C" label="已复核"></el-option>
            <el-option key=12 value=12 style="color:#67C23A" label="部分发运"></el-option>
            <el-option key=8 value=8 style="color:#67C23A" label="全部发运"></el-option>
            <el-option key=-1 value=-1 style="color:#F56C6C" label="取消"></el-option>
            <el-option key=-2 value=-2 style="color:#F56C6C" label="关闭"></el-option>
          </el-select>
        </div>
        <div>
          <span>发运状态</span>
          <el-select v-model="searchData.sendState" size="mini" filterable placeholder="发运状态" clearable class="so-bill-select">
            <el-option key=13 value="NORMAL" style="color:#909399" label="未发运"></el-option>
              <el-option key=14 value="PARTIAL" style="color:#67C23A" label="部分发运"></el-option>
            <el-option key=15 value="ALL" style="color:#67C23A" label="全部发运"></el-option>
          </el-select>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker style="width: 220px;" v-model="searchData.createTimeArr" size="mini" type="daterange"
            value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <el-button-group>
          <el-button @click="getSoData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <el-button @click="getDriversInfo" size="mini">刷新司机</el-button>
          <span>司机</span>
          <el-select v-model="selectedDriverOrders" size="mini" clearable @clear="clearDriver"
            @change="selectedDriverChange">
            <el-option v-for="driverName in Object.keys(driverInfos).sort()" :key="driverName"
              :value="driverInfos[driverName].orders" :label="driverName">
              {{ driverName }}
              <input type="checkbox" v-model="driverInfos[driverName].sortByDesc" @click.stop />
            </el-option>
          </el-select>
        </div>
        <div>
          <el-button size="mini" type="primary" round @click="$router.push({ name: 'createSoBill' })">
            <span class="iconfont icon-xinzeng" style="font-size: 0.8rem; "></span>
            新增
          </el-button>
          <el-dropdown :disabled="multipleSelection.length <= 0" style="margin-left:10px;margin-right:10px" size="mini"
            type="primary" icon="iconfont icon-print" @click="demoPrint()" split-button>
            打印送货确认单
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
              </el-dropdown-item>
              <!-- <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item>
              <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item>  -->
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown :disabled="multipleSelection.length <= 0" style="margin-left:10px;margin-right:10px" size="mini"
            type="primary" icon="iconfont icon-print" @click="demoPrint2()" split-button>
            打印拣货标签
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
              </el-dropdown-item>
              <!-- <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item>
              <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item>  -->
            </el-dropdown-menu>
          </el-dropdown>
          <el-dialog title="选择" :visible.sync="dialogVisible" width="450px">
            <div style="margin-top: -60px;">
              <div>请选择打印方式</div>
              <div style="display: flex; justify-content: flex-end; ">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="demoPrint2WithConfirm(true)">每单SKU仅打印1张</el-button>
                <el-button type="primary" @click="demoPrint2WithConfirm(false)">默认打印1</el-button>
              </div>
            </div>
          </el-dialog>
          <!-- 打印拣货两件套 -->
          <el-dialog title="选择" :visible.sync="dialogVisible2soa" width="840px">
            <div style="margin-top: -60px;">
              <div>请选择打印方式</div>
              <div style="display: flex; justify-content: flex-end; ">
                <el-button @click="dialogVisible2soa = false">Cancel</el-button>
                <el-button type="primary" @click="demoPrint2WithConfirm2soaH5()">按订单打印</el-button>
                <el-button type="primary" @click="bcPrint()">按波次打印</el-button>
              </div>
            </div>
          </el-dialog>
          <el-dropdown :disabled="multipleSelection.length <= 0" style="margin-left:10px;margin-right:10px" size="mini"
            type="primary" icon="iconfont icon-print" @click="demoPrintJHD()" split-button>
            打印拣货单
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown :disabled="multipleSelection.length <= 0" style="margin-left:10px;margin-right:10px" size="mini"
            type="primary" icon="iconfont icon-print" @click="demoPrint22soa()" split-button>
            打印拣货2件套
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
              </el-dropdown-item>
              <!-- <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item>
              <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item>  -->
            </el-dropdown-menu>
          </el-dropdown>
          <el-switch style="margin:auto 10px" v-model="isAll" v-show="isDriver" inactive-text="干冷混打" @change="getSoData">
          </el-switch>
          <el-button size="mini" type="success" round @click="exportSoMaster()">
            <span class="iconfont icon-xiazai" style="font-size: 0.8rem; "></span>
            下载本日缺货清单
          </el-button>
        </div>
      </div>
    </div>
    <div class="tableBox">
      <el-table v-loading="loading.masterTable" highlight-current-row :data="soBills"
        @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <!-- <el-table-column
                 fixed="left"
                 type="index"
                 width="50">
         </el-table-column> -->
        <el-table-column fixed="left" show-overflow-tooltip prop="billNo" label="单号" min-width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({ name: 'soBillDetail', params: scope.row })">{{
              scope.row.billNo
            }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="shopDailyCount" label="订单日序号" min-width="100">
        </el-table-column>
        <!-- <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="shipBillCode"
            label="物流单号"
            min-width="150">
        </el-table-column> -->
        <!-- <el-table-column
            show-overflow-tooltip
            prop="priority"
            label="优先发货"
            min-width="80">
        </el-table-column> -->
        <!-- <el-table-column
            show-overflow-tooltip
            prop="shipName"
            label="承运方"
            min-width="120">
        </el-table-column> -->
        <el-table-column show-overflow-tooltip prop="soStructure" label="sku种类" min-width="80">
        </el-table-column>
        <!-- <el-table-column
                show-overflow-tooltip
                prop="billSource"
                label="单据来源"
                width="80">
            <template slot-scope="scope">
                <span v-if="scope.row.billSource === 1">新建</span>
                <span v-if="scope.row.billSource === 2"  >合并</span>
                <span v-if="scope.row.billSource === 3"  >调拨</span>
            </template>
        </el-table-column> -->
        <!-- <el-table-column
                show-overflow-tooltip
                prop="state"
                label="审核状态"
                width="80">
            <template slot-scope="scope">
                <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.state === item.value" :type="item.type">
                    {{item.label}}
                </el-tag>
            </template>
        </el-table-column> -->
        <el-table-column show-overflow-tooltip prop="state" label="状态" min-width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.state == 1" style="color:#909399">待处理</span>
            <span v-if="scope.row.state == 2" style="color:#909399">待处理</span>
            <span v-if="scope.row.state == 3" style="color:#F56C6C">缺货</span>
            <span v-if="scope.row.state == 4" style="color:#409EFF">已分配</span>
            <span v-if="scope.row.state == 5" style="color:#E11111">部分拣货</span>
            <span v-if="scope.row.state == 6" style="color:#543044">拣货完成</span>
            <span v-if="scope.row.state == 7" style="color:#E6A23C">已复核</span>
            <span v-if="scope.row.state == 8" style="color:#67C23A">全部发运</span>
            <span v-if="scope.row.state == 12" style="color:#67C23A">部分发运</span>
            <span v-if="scope.row.state == -1" style="color:#909399">取消</span>
            <span v-if="scope.row.state == -2" style="color:#909399">关闭</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="state" label="发运状态" min-width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.sendState == 'NORMAL'" style="color:#909399">未发运</span>
            <span v-if="scope.row.sendState == 'PARTIAL'" style="color:#67C23A">部分发运</span>
            <span v-if="scope.row.sendState == 'ALL'" style="color:#67C23A">全部发运</span>
          </template>
        </el-table-column>
        <!-- <el-table-column show-overflow-tooltip prop="soType" label="发运单类型" min-width="80">
          <template slot-scope="scope">
            <span v-for="(item, index) in soBillTypes" :key="index"
              v-if="scope.row.soType === item.dataDictDetailId">{{ item.dictValue }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="customerName" label="客户" min-width="100">
          <template slot-scope="scope">
            <span class="textBtn"
              @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{ scope.row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="weight" label="重量" min-width="80">
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="linkMan" label="收货人" width="80">
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="carrierName" label="承运方" width="100">
        </el-table-column>
        <el-table-column sortable show-overflow-tooltip prop="deliveryTime" label="要求交货时间" width="110">
          <template slot-scope="scope">
            <span v-if="scope.row.deliveryTime != undefined">{{ scope.row.deliveryTime.substr(5, 11) }}</span>
          </template>
        </el-table-column>
        <el-table-column sortable show-overflow-tooltip prop="expectTimeFm" label="预发时间FM" width="110">
          <template slot-scope="scope">
            <span v-if="scope.row.deliveryTime != undefined">{{ scope.row.expectTimeFm.substr(5, 11) }}</span>
          </template>
        </el-table-column>
        <el-table-column sortable show-overflow-tooltip prop="expectTimeTo" label="预发时间TO" width="110">
          <template slot-scope="scope">
            <span v-if="scope.row.deliveryTime != undefined">{{ scope.row.expectTimeTo.substr(5, 11) }}</span>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="orderNo" label="优先级" width="80">
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in soUdfHs" :key="index" :prop="item.paramCode"
          :label="item.value" width="180">
        </el-table-column> -->
        <el-table-column show-overflow-tooltip prop="createTime" label="创建时间" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.createTime.substr(0, 16) }}
          </template>
        </el-table-column>
        <el-table-column sortable show-overflow-tooltip prop="memo" label="备注" min-width="180">
        </el-table-column>
        <!-- <el-table-column fixed="right" show-overflow-tooltip prop="createUserName" label="操作" min-width="100">
          <template slot-scope="scope">
            <el-button @click="adjustLevel(scope.row.soMasterId)" type="text" size="small">
              优先级
            </el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip v-for="(item, index) in soUdfHs" :key="index" :prop="item.paramCode"
          :label="item.value" width="160">
        </el-table-column>
        <el-table-column prop="manufactureType" label="生成加工单" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.manufactureType === 1 ? '是' : '否' }}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="left" show-overflow-tooltip prop="mergeNo" label="合并单号" width="80">
          <template slot-scope="scope">
            <span class="textBtn"
              @click="$router.push({ name: 'soBillDetail', params: { billNo: scope.row.mergeNo } })">{{
                  scope.row.mergeNo
              }}</span>
            <span></span>
          </template>
        </el-table-column> -->

      </el-table>
      <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="handleCurrentChange"
        @size-change="handleSizeChange" :current-page.sync="searchData.pageNum" :page-size="searchData.pageSize"
        :page-sizes="[15, 30, 50, 100]" layout="sizes,total, prev, pager, next, jumper" :total="searchData.total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { SoPrefix } from '@/utils/enum';
import moment from "moment";
export default {
  name: 'SoBill',
  data() {
    return {
      isAll: false,
      isDriver: false,
      loading: {
        masterTable: true,
      },
      wareId: PF.getLocal('wareId', 'number'),
      unLoadData: {
        model: "",
        wareId: 111,
        insertType: 111
      },
      billSources: [
        {
          text: "手动",
          value: 1
        },
        {
          text: "接口",
          value: 4
        },
      ],
      beforOneMonth: moment().subtract(30, "days").format('YYYY-MM-DD'),
      nowDate: moment().add(1, "days").format('YYYY-MM-DD'),
      searchData: {
        pageNum: 1,
        pageSize: 100,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        state: null,
        sendState: null,
        organizationId: null,
        customerId: null,
        billSource: null,
        carrierId: null,
        soType: null,
        startCreateTime: '',
        endCreateTime: '',
        createTimeArr: '',
        deliveryTimeArr: [],
        startDeliveryTime: '',
        endDeliveryTime: '',
        expectTimeFmArr: [],
        startExpectTimeFm: '',
        endExpectTimeTo: '',
        billNos: '',
        mergeNo: '',
        orderNo: '',
      },
      soBills: [],
      formLabelWidth: '120px',
      soUdfHs: [],
      organizations: [],
      customers: [],
      soBillTypes: [],
      carriers: [],
      multipleSelection: [],
      selectedDriverOrders: [],
      driverInfos: [],
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
            || list[i].organizationId !== list[0].organizationId
            || list[i].customerId !== list[0].customerId
            || list[i].carrierId !== list[0].carrierId
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
    clearDriver() {
      this.isAll = false;
    },
    demoPrintJHD() {
      this.$confirm('是否确认打印？', '选择').then(_ => {
        this.demoPrintJHDWithConfirm();
      }).catch(() => { });
    },
    demoPrintJHDWithConfirm() {
      const printerLog = { startTime: Date.now() / 1000, type: 'Pickup List' };
      const that = this;
      const params = this.getPrintParams();
      let url = '/so/masters/getPickTaskAll';
      if (this.selectedDriverOrders && this.selectedDriverOrders.length) {
        url = '/so/masters/getPickTaskAllForDriver';
        const driverName = this.selectedDriverOrders[0].driverName;
        printerLog.driverName = driverName;
        const wareId = PF.getLocal('wareId', 'number');
        if (this.driverInfos[driverName].sortByDesc) {
          params.printInfos.reverse();
        }
        params.printInfos = params.printInfos.filter(printInfo => printInfo.wareId === wareId);
      }
      printerLog.detailList = [];
      IOT.getServerData(url, 'post', params, (ret) => {
        if (ret.code === 200) {
          const printInfos = params.printInfos;
          const bills = ret.data;
          bills.forEach((bill, index) => {
            const printInfo = printInfos[index];
            const detail = { startTime: Date.now() / 1000, billNo: printInfo.billNo, index: printInfo.index };
            that.$parent.printHtml(bill.soPickTask, null);
            detail.endTime = Date.now() / 1000;
            printerLog.detailList.push(detail);
          });
        } else {
          const error = ret.message || '服务器请求失败，稍后再试！';
          printerLog.description = error;
          IOT.tips(error, 'error');
        }
        IOT.getServerData('/so/masters/printerLog', 'post', printerLog);
      }, true);

    },
    batchSendOut() {

    },
    demoPrint() {
      this.$confirm('是否确认打印？', '选择').then(_ => {
        this.demoPrintWithConfirm();
      }).catch(() => { });
    },
    demoPrintWithConfirm() {
      const printerLog = { startTime: Date.now() / 1000, type: 'All Three' };
      const that = this;
      const params = this.getPrintParams();
      let url = '/so/masters/getSoPrintDataAll';
      if (this.selectedDriverOrders && this.selectedDriverOrders.length) {
        url = '/so/masters/getSoPrintDataAllForDriver';
        printerLog.driverName = this.selectedDriverOrders[0].driverName;
      }
      printerLog.detailList = [];
      IOT.getServerData(url, 'post', params, (ret) => {
        if (ret.code === 200) {
          const bills = ret.data;
          const printInfos = params.printInfos;
          bills.forEach((bill, index) => {
            const printInfo = printInfos[index];
            const detail = { startTime: Date.now() / 1000, billNo: printInfo.billNo, index: printInfo.index };
            that.$parent.printHtml(bill.html, null);
            that.$parent.printUrl(bill.pdfUrl);

            detail.endTime = Date.now() / 1000;
            printerLog.detailList.push(detail);
          });
        } else {
          const error = ret.message || '服务器请求失败，稍后再试！';
          printerLog.description = error;
          IOT.tips(error, 'error');
        }
        IOT.getServerData('/so/masters/printerLog', 'post', printerLog);
      }, true);

    },
    demoPrint2() {
      if (this.selectedDriverOrders && this.selectedDriverOrders.length > 0) {
        this.dialogVisible = true;
      } else {
        this.$confirm('是否确认打印？', '选择').then(_ => {
          this.demoPrint2WithConfirm();
        }).catch(() => {
        });
      }
    },
    async demoPrint2WithConfirm(simplify) {
      this.dialogVisible = false;
      const printerLog = { startTime: Date.now() / 1000, type: 'Label' };
      const promisify = (callback, url, type, param) => {
        return new Promise((resolve, reject) => {
          callback(url, type, param, ret => {
            resolve(ret);
          });
        })
      }

      const sleep = (ms) => {
        return new Promise(resolve => setTimeout(resolve, ms))
      }

      const that = this;
      const params = this.getPrintParams();
      let printInfos = [];
      let url = '/so/masters/getPickTaskLabel';
      if (this.selectedDriverOrders && this.selectedDriverOrders.length) {
        url = '/so/masters/getPickTaskLabelForDriver';
        const driverName = this.selectedDriverOrders[0].driverName;
        printerLog.driverName = driverName;
        if (this.driverInfos[driverName].sortByDesc) {
          params.printInfos.reverse();
        }
        const wareId = PF.getLocal('wareId', 'number');
        printInfos = params.printInfos.filter(printInfo => printInfo.wareId === wareId);
      } else {
        printInfos = params.soMasterIds.map((soMasterId, index) => ({ soMasterId, wareId: this.wareId, ...params.printInfos[index] }));
      }
      printerLog.detailList = [];
      for (const printInfo of printInfos) {
        const detail = { startTime: Date.now() / 1000, billNo: printInfo.billNo, index: printInfo.index };
        const ret = await promisify(IOT.getServerData, url, 'get', { ...printInfo, simplify });
        if (ret.code === 200) {
          const picks = ret.rows;
          let htmlStr = '';
          picks.forEach(pick => {
            htmlStr += pick.html;
          });
          // todo 后续验证后删除
          await sleep(1000);
          that.$parent.printHtml(htmlStr, null);
        } else {
          const error = ret.message || '服务器请求失败，稍后再试！';
          IOT.tips(error, 'error');
          detail.description = error;
        }
        detail.endTime = Date.now() / 1000;
        printerLog.detailList.push(detail);
      }
      printerLog.endTime = Date.now() / 1000;
      IOT.getServerData('/so/masters/printerLog', 'post', printerLog);
    },
    demoPrint22soa() {
      this.dialogVisible2soa = true;
    },
    // 原生打印两件套
    demoPrint2WithConfirm2soaH5() {
      const loading = this.$loading({
        lock: true,
        text: '加载打印数据中，请稍等',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      setTimeout(() => {
        loading.close();
      }, 30000);
      const printerLog = { startTime: Date.now() / 1000, type: 'Label' };
      const params = this.getPrintParams();
      let printInfos = [];
      if (this.selectedDriverOrders && this.selectedDriverOrders.length) {//选择过司机
        const driverName = this.selectedDriverOrders[0].driverName;
        printerLog.driverName = driverName;
        if (this.driverInfos[driverName].sortByDesc) {
          params.printInfos.reverse();
        }
        printInfos = params.printInfos
      } else {
        printInfos = params.soMasterIds.map((soMasterId, index) => ({ soMasterId, wareId: this.wareId, ...params.printInfos[index] }));
      }
      printerLog.detailList = [];
      for (const printInfo of printInfos) {
        printerLog.detailList.push({ billNo: printInfo.billNo, index: printInfo.index });
      }
      IOT.getServerData('/so/masters/getPickTaskLabelForDriverData', 'post', {
        pickTaskPrintParamList: printInfos
      }, ret => {
        if (ret.code === 200) {
          loading.close();
          IOT.tips('打印页面可能被浏览器拦截，请允许弹出', 'success', 5000);
          PF.setLocal('PrintPickSuit', ret.data)
          const printPage = this.$router.resolve({ name: 'PrintPickSuit' })
          window.open(printPage.href, '_blank')
          printerLog.endTime = Date.now() / 1000;
          IOT.getServerData('/so/masters/printerLog', 'post', printerLog);
        } else {
          loading.close();
          IOT.tips(ret.message || '拣货两件套打印失败。', 'error');
        }
      }, true);
    },

    // 波次打印
    bcPrint() {
      const params = this.getPrintParams();
      console.log(params)
      let printInfos = [];

      //选择过司机
      let driverName;
      if (this.selectedDriverOrders && this.selectedDriverOrders.length) {
        driverName = this.selectedDriverOrders[0].driverName;
        // delivererId = this.selectedDriverOrders[0].delivererId;
        if (this.driverInfos[driverName].sortByDesc) {
          params.printInfos.reverse();
        }
        const wareId = PF.getLocal('wareId', 'number');
        if (this.isAll == false) {
          printInfos = params.printInfos.filter(printInfo => printInfo.wareId === wareId);
        } else {
          printInfos = params.printInfos;
        }
      } else {
        printInfos = params.soMasterIds.map((soMasterId, index) => ({ soMasterId, wareId: this.wareId, ...params.printInfos[index] }));
      }

      //获取波次信息
      let soBillNos = "";
      for (var i = 0; i < printInfos.length; i++) {
        if (i == printInfos.length - 1) {
          soBillNos += printInfos[i].billNo;
        } else {
          soBillNos += printInfos[i].billNo + ",";
        }
      }
      // 生成并打印
      let newBatchFunction = () => {
        IOT.getServerData('/so/masters/pickupBatch', 'post', { billNos: soBillNos, driverName: driverName }, (ret) => {
          if (ret.code === 200) {
            this.bcGroupPrint(ret.data.pickBatchGroupNo);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true)
      }
      // 查询波次是否打印过
      IOT.getServerData('/so/masters/getPickupBatchGroup', 'post', { billNos: soBillNos, driverName: driverName }, (ret) => {
        if (ret.code === 200) {
          let hasPrinted = ret.data.has;
          let pickBatchGroupNo = ret.data.pickBatchGroupNo;
          if (hasPrinted) {
            this.$confirm('您已经打印过该波次（' + pickBatchGroupNo + '），是否重新打印？', '提示', {
              distinguishCancelAndClose: true,
              confirmButtonText: '打印之前的波次',
              cancelButtonText: '重新生成波次',
              type: 'warning',
              showCancelButton: true,
              cancelButtonClass: 'el-button--primary'
            }).then(() => {
              this.bcGroupPrint(pickBatchGroupNo);
            }).catch(tag => {
              if (tag == 'cancel') {
                newBatchFunction();
              }
            });
          } else {
            newBatchFunction();
          }
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true)
    },

    /**
     * 打印波次组
     * @param {string} pickBatchGroupNo 波次组号
     */
    bcGroupPrint(pickBatchGroupNo) {
      const loading = this.$loading({
        lock: true,
        text: '加载打印数据中，请稍等',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      setTimeout(() => {
        loading.close();
      }, 30000);
      IOT.getServerData('/so/masters/getBcPrintInfo', 'post', { pickBatchGroupNo, wareId: this.wareId }, ret => {
        if (ret.code === 200) {
          loading.close();
          IOT.tips('打印页面可能被浏览器拦截，请允许弹出', 'success', 5000);
          PF.setLocal('PrintPickBatch', ret.data)
          const printPage = this.$router.resolve({ name: 'PrintPickBatch' })
          window.open(printPage.href, '_blank')
        } else {
          loading.close();
          IOT.tips(ret.message || '波次拣货打印失败。', 'error');
        }
      }, true)
    },
    adjustLevel(soMasterId) {
      IOT.getServerData('/so/masters/priority', 'get', { soMasterId }, (ret) => {
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
    upLoad() {
      var formData = new FormData();
      formData.append("model", document.querySelector("#file").files[0]);  //上传一个files对象
      formData.append("wareId", 111);//需要上传的多个参数

      IOT.upLoadFile('/so/masters/importSoBills', 'post', formData, (ret) => {

      }, true);
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getSoData()
    },
    clearSearchData() {
      this.selectedDriverOrders = "";
      this.driverInfos = '';
      this.searchData.keyWords = '';
      this.searchData.state = null;
      this.searchData.organizationId = null;
      this.searchData.customerId = null;
      this.searchData.soType = null;
      this.searchData.billSource = null;
      this.searchData.carrierId = null;
      this.searchData.startCreateTime = this.beforOneMonth;
      this.searchData.endCreateTime = this.nowDate;
      this.searchData.createTimeArr = [this.beforOneMonth, this.nowDate];
      this.searchData.deliveryTimeArr = [];
      this.searchData.startDeliveryTime = '';
      this.searchData.endDeliveryTime = '';
      this.searchData.expectTimeFmArr = [];
      this.searchData.startExpectTimeFm = '';
      this.searchData.endExpectTimeTo = '';
      this.searchData.billNos = '';
      this.searchData.mergeNo = '';
      this.searchData.orderNo = '';
      this.searchData.sendState = null;
      this.isAll = false;
    },
    getSoData() {
      if (this.isAll == false) {
        this.searchData.wareId = PF.getLocal('wareId', 'number');
      } else {
        this.searchData.wareId = null;
      }
      let searchData = PF.JSON(this.searchData);
      if (searchData.createTimeArr != null && searchData.createTimeArr.length > 0) {
        searchData.startCreateTime = searchData.createTimeArr[0];
        searchData.endCreateTime = searchData.createTimeArr[1];
      }
      if (searchData.deliveryTimeArr != null && searchData.deliveryTimeArr.length > 0) {
        searchData.startDeliveryTime = searchData.deliveryTimeArr[0];
        searchData.endDeliveryTime = searchData.deliveryTimeArr[1];
      }
      if (searchData.expectTimeFmArr != null && searchData.expectTimeFmArr.length > 0) {
        searchData.startExpectTimeFm = searchData.expectTimeFmArr[0];
        searchData.endExpectTimeTo = searchData.expectTimeFmArr[1];
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
      // searchData.wareId = PF.getLocal('wareId', 'number');
      if (searchData.billNos != null) {
        searchData.billNos = searchData.billNos.split(',');
      }
      IOT.getServerData('/so/masters/list', 'post', searchData, (ret) => {
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
    commitFile() {
      IOT.showOverlay('保存中，请稍等...');
      let list = PF.JSON(this.multipleSelection);
      let billNos = [];
      for (let i = 0; i < list.length; i++) {
        billNos.push(list[i].billNo)
      }
      IOT.getServerData('/so/masters/file', 'post', { billNos: billNos }, (ret) => {
        this.multipleSelection = [];
        if (ret.code === 200) {
          IOT.tips('归档成功！', 'success');
          this.getSoData();
        } else {
          IOT.hideOverlay();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    commitMerge() {
      IOT.showOverlay('保存中，请稍等...');
      let list = PF.JSON(this.multipleSelection);
      let billNos = [];
      IOT.getServerData('/organizations/list', 'get', { organizationId: list[0].organizationId }, (ret) => {
        if (ret.code === 200) {
          let organization = ret.rows[0];
          IOT.getServerData('/so/merge/tactics/list', 'get', { soMergeTacticCode: organization.soMergeTacticCode }, (ret) => {
            if (ret.code === 200) {
              if (ret.rows.length !== 1) {
                IOT.tips('未查询到该货主下的合并策略!', 'error', 1000, () => {
                  IOT.hideOverlay();
                });
                return false
              }
              let soMergeTactic = ret.rows[0];
              let noMergerMsg = '';
              for (let i = 0; i < list.length; i++) {
                if (soMergeTactic.billSource === 1) {
                  if (list[i].billSource !== list[0].billSource) {
                    noMergerMsg = '单据来源'
                  }
                }
                // else if (soMergeTactic.billSource === 2) {
                //   if (list[i].billSource !== list[0].billSource) {
                //     this.$confirm('单据来源不同,是否继续？', '提示', {
                //       confirmButtonText: '确定',
                //       cancelButtonText: '取消',
                //       type: 'warning',
                //       center: true
                //     }).then(_ => {
                //       this.deleteOrganization(id)
                //     }).catch(_ => {
                //       IOT.tips('已取消合并!', 'error', 1000, () => {
                //         IOT.hideOverlay();
                //       });
                //       return false
                //     });
                //   }
                // }
                if (soMergeTactic.deliveryTime === 1) {
                  if (list[i].deliveryTime !== list[0].deliveryTime) {
                    noMergerMsg = 'qwe数量'
                  }
                }
                if (soMergeTactic.expectTimeFm === 1) {
                  if (list[i].expectTimeFm !== list[0].expectTimeFm) {
                    noMergerMsg = '预发数量FM'
                  }
                }
                if (soMergeTactic.expectTimeTo === 1) {
                  if (list[i].expectTimeTo !== list[0].expectTimeTo) {
                    noMergerMsg = '预发数量TO'
                  }
                }
                for (let j = 0; j < this.soUdfHs.length; j++) {
                  if (soMergeTactic[this.soUdfHs[j].paramCode] === 1) {
                    if (list[i][this.soUdfHs[j].paramCode] !== list[0][this.soUdfHs[j].paramCode]) {
                      noMergerMsg = this.soUdfHs[j].value
                    }
                  }
                }
                billNos.push(list[i].billNo)
              }
              if (noMergerMsg === '') {
                IOT.getServerData('/so/masters/merge', 'post', { billNos: billNos }, (ret) => {
                  IOT.hideOverlay();
                  this.multipleSelection = [];
                  if (ret.code === 200) {
                    IOT.tips('合并成功！', 'success');
                    this.getSoData();
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });
              } else {
                IOT.tips(noMergerMsg + '不同,不能合并! 请重新选择', 'error', 1000, () => {
                  IOT.hideOverlay();
                });
              }
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },

    getDriversInfo() {
      IOT.showOverlay('刷新司机中，请稍等...');
      IOT.getServerData('/erp/rpc/passquan/order/assignedOrders', 'get', {}, (ret) => {
        if (ret.code === 200) {
          const orders = ret.rows;
          const driverMap = {};
          for (const order of orders) {
            const driverName = order['driver_nickname'];
            const newOrder = {
              id: order.id,
              delivererId: order.deliverer_id,
              driverName,
              groupNo: order['batch_group_no'],
              index: order.index,
            };
            if (!driverMap[driverName]) {
              driverMap[driverName] = {
                sortByDesc: false,
                orders: []
              };
            }
            driverMap[driverName].orders.push(newOrder);
          }
          this.driverInfos = driverMap;
        }
        IOT.hideOverlay();
      });
    },

    selectedDriverChange(orders) {
      this.isAll = true;
      this.isDriver = true;
      if (!orders) {
        return;
      }
      const assignedOrders = orders.filter(order => order.groupNo)
        .sort((orderA, orderB) => orderA.index - orderB.index);
      const otherOrders = orders.filter(order => !order.groupNo)
        .sort((orderA, orderB) => orderA.id - orderB.id);
      orders = [...assignedOrders, ...otherOrders];
      const billNoList = [];
      for (const order of orders) {
        billNoList.push(order.id);
      }
      if (billNoList.length > 0) {
        this.searchData.billNos = billNoList.join(',');
        this.getSoData();
      } else {
        IOT.tips('该司机没有订单', 'warn');
      }
    },

    getPrintParams() {
      const soMasters = this.multipleSelection;
      soMasters.sort((soMasterA, soMasterB) => soMasterA.shopDailyCount - soMasterB.shopDailyCount);
      if (!this.selectedDriverOrders) {
        return { soMasterIds: soMasters.map(soMaster => soMaster.soMasterId), wareId: this.wareId, printInfos: soMasters.map(soMaster => ({ billNo: soMaster.billNo })) };
      }
      const params = { wareId: this.wareId };
      const printInfos = [];
      let needReSort = false;
      for (const soMaster of soMasters) {
        const soOrderId = soMaster.billNo.replace(SoPrefix.SO8000, '').replace(SoPrefix.SO9000, '')
          .replace(SoPrefix.SO, '');
        let printInfo = { soMasterId: soMaster.soMasterId };
        for (const order of this.selectedDriverOrders) {
          if (soOrderId === order.id.toString()) {
            printInfo = {
              orderId: order.id,
              index: order.groupNo ? order.index + 1 : 0,
              soMasterId: soMaster.soMasterId,
              driverName: order.driverName,
              billNo: soMaster.billNo,
              wareId: soMaster.wareId,
            };
            needReSort = true;
          }
        }
        printInfos.push(printInfo);
      }
      if (needReSort) {
        printInfos.sort((infoA, infoB) => infoA.index - infoB.index || infoA.orderId - infoB.orderId);
      }
      params.printInfos = printInfos;
      return params;
    },

    //下载本日缺货
    exportSoMaster() {
      let url = '/so/masters/findByShortageStatistics';
      IOT.showOverlay("正在导出，请稍等...");
      let data = {
        wareId: PF.getLocal('wareId', 'number')
      }
      IOT.getServerData(url, 'post', data, (ret) => {
        if (ret.code === 200) {
          var path = ret.data;
          if (path != undefined && path != null) {
            window.location.href = path;
          } else {
            IOT.tips(
              "暂无数据！",
            );
          }
        } else {
          IOT.tips(
            ret.message || "服务器请求失败，稍后再试！",
            "error"
          );
        }
        IOT.hideOverlay();
      }, true);
    },
  },
  created() {
    this.searchData.startCreateTime = this.beforOneMonth;
    this.searchData.endCreateTime = this.nowDate;
    this.searchData.createTimeArr = [this.searchData.startCreateTime, this.searchData.endCreateTime]
    PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
      this.soUdfHs = udfs;
    });
    this.getSoData();
    PF.getCarriers((rows) => {
      this.carriers = rows;
    })
    PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
      this.soUdfHs = udfs;
    });
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

<style scoped lang="less">
.soBill {}
</style>
