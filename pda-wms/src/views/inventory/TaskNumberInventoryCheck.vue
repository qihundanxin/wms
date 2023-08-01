<template>
  <div class="taskInventory">
    <h1>此页面为废弃页面！请注意！</h1>
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="盘点任务单号" :label-width="formLabelWidth">
          <el-input v-model="searchData.billNo" size="mini" placeholder="请输入盘点单号" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="getData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待盘点确认</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in stockTakeTasks">
          <el-card class="clearFloat">
            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
<!--                <span>{{index+1}}/{{searchData.total}}</span>-->
                <span>{{item.itemName}}</span>
                <span>{{item.itemCode}}</span>
              </div>
              <div class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>单号:</span>
                    <span>&nbsp;{{item.billNo}}</span>
                  </li>
                  <li>
                    <span>类型:</span>
                    <span v-for="i in stockTaskDetailType" v-if="i.value === item.type">{{i.label}}</span>
                  </li>

                  <li>
                    <span>单号:</span>
                    <span>&nbsp;{{item.billNo}}</span>
                  </li>
                  <li>
                    <span>货位:</span>
                    <span>&nbsp;{{item.cellCode}}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                  </li>
                  <li>
                    <span>批次:</span>
                    <span>&nbsp;{{item.batchName}}</span>
                  </li>
                  <li>
                    <span>追踪号:</span>
                    <span>&nbsp;{{item.boxCode}}</span>
                  </li>
                </ul>
              </div>
              <div class="mainData clearFloat">
                <!-- 重要信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{item.quantity}}</span>
                    <span>库存:</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>&nbsp;{{item.packDescribe}}</span>
                    <span>单位</span>
                  </li>
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>实盘数:</span>
                    <span>
                    <el-input-number v-model="item.taskQuantity" size="mini" controls-position="right" placeholder="实盘数" style="width: 100%;"></el-input-number>
                  </span>
                  </li>
                </ul>
              </div>
            </div>
            <div class="cardBottomBtn clearFloat">
              <!-- 底部按钮 -->
              <div class="leftFloat"></div>
              <div class="rightFloat" @click="commit(item)">确认</div>
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
  import {mapState} from "vuex";
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "Demo",
    data() {
      return {
        headerTitle: "任务号盘点",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        stockTakeTasks: [],
        organizations: [],
        suppliers: [],
        asnUdfHs: [],
        asnTypes: [],

        searchData: {
          pageNum: 1,
          pageSize: 1,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          billNo:'',
          state:1,
        },

        stockTakes:[],
      };
    },
    computed: {
      ...mapState([
        "stockTaskDetailType",
      ]),
    },
    methods: {
      commit(row){
        if(row.taskQuantity === null || row.taskQuantity === '' || row.taskQuantity <=0){
          IOT.tips('请输入实盘数量！', 'error');
          return false;
        }
        this.stockTakes.push(row);
        this.loading = true;
        IOT.getServerData('/stock/take/tasks/calculateStockTake','post',{stockTakeTasks:this.stockTakes},(ret) => {
          this.loading = false;
          this.stockTakes = [];
          if (ret.code === 200) {
            IOT.tips('确认成功！', 'success', 1000, () => {
              this.stockTakeTasks = [];
              this.searchData.billNo = '';
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getData(){
        if(this.searchData.billNo === null || this.searchData.billNo === ''){
          IOT.tips('请输入任务号！', 'error');
          return false;
        }
        this.loading = true;
        IOT.getServerData('/stock/take/tasks/list','get',this.searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.stockTakeTasks = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if(this.searchData.pageSize >= ret.total){
              this.noMore = true;
            }else{
              this.noMore = false;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      lazy(){
        if(this.asnBills.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getAsnData();
        }
      },
    },
    created() {
      /*PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
        this.asnUdfHs = udfs;
      });
      PF.getDataDictUdfData('asnType', (rows) => {
        this.asnTypes = rows;
      });*/
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .demo {
  }
</style>
