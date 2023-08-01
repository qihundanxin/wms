<template>
  <div class="stockTakeTask">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div v-if="stockTakeTasks.length == 0" style="text-align: center">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-input v-model="searchData.masterBillNo" id="masterBillNo" placeholder="请输入盘点单号" clearable
          style="width: 100%"></el-input>
        <!-- <el-form-item label="盘点单号" :label-width="formLabelWidth">
      <el-input
      v-model="searchData.masterBillNo"
      size="mini"
      placeholder="请输入盘点单号"
      clearable
      style="width: 90%"
      ></el-input>
    </el-form-item> -->
        <!-- <el-form-item label="货区" :label-width="formLabelWidth">
      <el-input
      v-model="searchData.areaCode"
      size="mini"
      placeholder="请输入货区"
      clearable
      style="width: 90%"
      ></el-input>
    </el-form-item>
    <el-form-item label="货位组" :label-width="formLabelWidth">
      <el-input
      v-model="searchData.shelfCode"
      size="mini"
      placeholder="请输入货位组"
      clearable
      style="width: 90%"
      ></el-input>
    </el-form-item>
    <el-form-item label="追踪号" :label-width="formLabelWidth">
      <el-input
      v-model="searchData.boxCode"
      size="mini"
      placeholder="请输入追踪号"
      clearable
      style="width: 90%"
      ></el-input>
    </el-form-item>
    <el-form-item label="产品" :label-width="formLabelWidth">
      <el-input
      v-model="searchData.itemCode"
      size="mini"
      placeholder="请输入产品"
      clearable
      style="width: 90%"
      ></el-input>
    </el-form-item> -->
      </el-form>
      <el-button @click="getData()" type="primary" size="mini" round icon="iconfont icon-chaxun"
        style="width: 200px; margin-top: 5px">
        查询</el-button>
    </div>
    <!-- <el-divider content-position="center">盘点任务</el-divider> -->
    <div v-loading="loading" id="list" v-if="stockTakeTasks.length > 0" class="baseAcceptBillBox noScrollbar">
      <div v-if="!cellCodeOK" class="scanInput">
        <el-input clearable placeholder="请扫描货位编码" id="cellCode" v-model="cellCode"></el-input>
        <i class="el-icon-camera" @click="scan(1)"></i>
      </div>
      <div v-else class="scanInput">
        <el-input clearable placeholder="请扫描SKU/UPC" id="itemCode" v-model="itemCode"></el-input>
        <i class="el-icon-camera" @click="scan(2)"></i>
      </div>
      <ul v-infinite-scroll="lazy">
        <li v-for="(item, index) in stockTakeTasks" :key="index">
          <el-card class="clearFloat">
            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{ index + 1 }}/{{
                  stockTakeTasksTotal
                }}</span>
                <!-- <span>{{ item.itemName }}</span> -->
                <span>{{ item.itemCode }}</span>
              </div>
              <div style="font-size:15px" class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul style="font-size:15px" class="leftFloat">
                  <!-- <li>
          <span>单号:</span>
          <span>&nbsp;{{ item.billNo }}</span>
          </li> -->
                  <!-- <li>
          <span>类型:</span>
          <span
            v-for="(i,index) in stockTaskDetailType"
            :key="index"
            v-if="i.value === item.type"
            >{{ i.label }}</span
          >
          </li> -->
                  <li>
                    <span style="font-size:15px">货位:</span>
                    <span style="font-size:15px">&nbsp;{{ item.cellCode }}</span>
                  </li>
                  <li>
                    <span style="font-size:15px">追踪号:</span>
                    <span style="font-size:15px">&nbsp;{{ item.boxCode }}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <!-- <li>
          <span>仓库:</span>
          <span>&nbsp;{{ item.wareName }}</span>
          </li> -->
                  <li>
                    <span style="font-size:15px">货主:</span>
                    <span style="font-size:15px" :title="item.organizationName">&nbsp;{{
                      item.organizationName
                    }}</span>
                  </li>
                  <li>
                    <span style="font-size:15px">批次:</span>
                    <span style="font-size:15px" :title="item.batchName">&nbsp;{{
                      item.batchName
                    }}</span>
                  </li>
                </ul>
              </div>
              <div v-if="item.type == 1" class="mainData clearFloat">
                <!-- 重要信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{ item.quantity }}</span>
                    <span>库存:</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>&nbsp;{{ item.packDescribe }}</span>
                    <span>单位</span>
                  </li>
                </ul>
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{ item.quantity + item.differQuantity }}</span>
                    <span>初盘:</span>
                  </li>
                </ul>
              </div>
              <div class="minorData" v-show="stockTake != null && stockTake.stockTakeTaskId == item.stockTakeTaskId">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>实盘数:</span>
                    <span>
                      <el-input-number v-bind:id="'tianxie' + index" v-model="item.toQuantity" size="mini"
                        controls-position="right" placeholder="实盘数" style="width: 100%"></el-input-number>
                    </span>
                  </li>
                </ul>
              </div>
            </div>
            <div style="color:#FFFFFF;line-height:20px;padding:0 5px;width:100%;word-break:break-all;"
              :style="(stockTake != null && stockTake.stockTakeTaskId == item.stockTakeTaskId) ? { 'background-color': '#409EFF' } : { 'background-color': '#909399' }">
              {{ item.itemName }}
            </div>
            <div v-if="stockTake != null && stockTake.stockTakeTaskId == item.stockTakeTaskId"
              class="cardBottomBtn clearFloat">
              <!-- 底部按钮 -->
              <div class="leftFloat">
              </div>
              <div class="rightFloat" v-bind:id="'commit' + index" @click="commit(item, index)">确认</div>
            </div>
          </el-card>
        </li>
      </ul>
      <p v-show="stockTakeTasks.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
import { mapState } from "vuex";
export default {
  name: "stockTakeTask",
  data() {
    return {
      headerTitle: "盘点任务（复盘）",
      formLabelWidth: "120px",
      cellCode: '',
      itemCode: '',
      cellCodeOK: '',
      loading: false,
      noMore: false,
      stockTakes: [],
      stockTakeTasks: [],
      organizations: [],
      suppliers: [],
      asnUdfHs: [],
      asnTypes: [],
      stockTake: null,
      searchData: {
        pageNum: 1,
        pageSize: 8,
        total: null,
        keyWords: "",
        insertType: 0,
        wareId: PF.getLocal("wareId", "number"),
        state: 2,
        masterBillNo: "",
        areaCode: "",
        shelfCode: "",
        itemCode: "",
        boxCode: "",
        organizationId: null,
        supplierId: null,
      },
      stockTakeTasksTotal:0,
    };
  },
  watch: {
    cellCode: function (newVal, oldVal) {
      if (newVal != null && newVal.length > 1) {
        var flag = false;
        this.stockTakeTasks.forEach(task => {
          if (task.cellCode != null && task.cellCode.trim() == newVal.trim()
            &&
            (task.state == 1 || task.state == 2)
          ) {
            flag = true;
          }
        });
        this.cellCodeOK = flag;
        if (flag) {
          this.getTask();
          // $("#list").scrollTo(0,0);
        }
      }
    },
    itemCode: function (newVal, oldVal) {
      let qrObject = PF.simpleQrEncode(newVal);
      if (qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
        this.itemCode = qrObject.index2;
        return;
      }
      var task = this.stockTakeTasks[0];
      if (task.itemCode == newVal || this.mateUpc(newVal.trim(), task.upcs)) {
        if (task.toQuantity == null || task.toQuantity == undefined) {
          task.toQuantity = 1;
        } else {
          task.toQuantity += 1;
        }
        this.itemCode = '';
        return;
      }
    }
  },
  computed: {
    ...mapState(["stockTaskDetailType"]),
  },
  methods: {
    mateUpc(upc, upcs) {
      if (upcs == null || upcs.length == 0) {
        return false;
      }
      for (var i = 0; i < upcs.length; i++) {
        if (upcs[i].upc == upc) {
          return true;
        }
      }
      return false;
    },
    getTask() {
      var that = this;
      var tasks = this.stockTakeTasks;
      for (let i = 0; i < tasks.length; i++) {
        if (tasks[i].cellCode != null && tasks[i].cellCode.trim() == that.cellCode.trim()) {
          this.stockTake = tasks[i];
          this.stockTakeTasks.unshift(this.stockTakeTasks.splice(i, 1)[0]);
          this.cellCode = "";
          $("#list").scrollTop(0);
          setTimeout(function () {
            var domFocus = document.getElementById("itemCode");
            domFocus.focus();
          }, 100);
          return;
          // this.$notify({
          //   title: '成功',
          //   message: '请扫描SKU',
          //   type: 'success'
          // });
        }
      }
    },
    commit(row, index) {
      if (
        row.toQuantity === null ||
        row.toQuantity === "" ||
        row.toQuantity <= 0
      ) {
        IOT.tips("请输入实盘数量！", "error");
        return false;
      }
      this.loading = true;
      IOT.getServerData("/stock/take/tasks/calculateStockTake", "post",
        { id: row.stockTakeTaskId, quantity: row.toQuantity }, (ret) => {
          if (ret.code === 200) {
            this.loading = false;
            this.cellCodeOK = false;
            this.stockTake = null;
            this.stockTakeTasks.splice(0, 1)
            this.getData();
            IOT.tips("成功！", "success", 1000, () => { });
          } else {
            this.loading = false;
            IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
          }
        }
      );
    },
    getData() {
      if (
        this.searchData.masterBillNo == null ||
        this.searchData.masterBillNo == ""
      ) {
        IOT.tips("请输入任务号！", "error");
        return false;
      }
      this.loading = true;
      IOT.getServerData(
        "/stock/take/tasks/list",
        "get",
        this.searchData,
        (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            if (this.stockTakeTasks.length == 0 && ret.total == 0) {
              IOT.tips("没找到盘点单" + (this.searchData.masterBillNo == null ? '' : this.searchData.masterBillNo) + "，或复盘任务已完成", "error");
              return;
            }
            this.stockTakeTasks = ret.rows;
            this.searchData.total = ret.total;
            this.stockTakeTasksTotal =ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if (this.searchData.pageSize >= ret.total) {
              this.noMore = true;
            } else {
              this.noMore = false;
            }
          } else {
            IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
          }
        }
      );
    },
    lazy() {
      if (
        this.stockTakeTasks.length > 0 &&
        !this.noMore &&
        !this.loading
      ) {
        this.searchData.pageSize += 2;
        this.getData();
      }
    },
  },
  created() {
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    // PF.getSuppliers((rows) => {
    //   this.suppliers = rows;
    // });
    // PF.getSysParamUdfData("asnUdfHs", [], (data, udfs) => {
    //   this.asnUdfHs = udfs;
    // });
    // PF.getDataDictUdfData("asnType", (rows) => {
    //   this.asnTypes = rows;
    // });
    setTimeout(function () {
      var domFocus = document.getElementById("masterBillNo");
      domFocus.focus();
    }, 100);
  },
  components: {
    FeaturesHeader,
  },
};
</script>
<style lang="less" scoped>
.demo {}
</style>
