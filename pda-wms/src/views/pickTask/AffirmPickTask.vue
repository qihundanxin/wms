<template>
  <div class="orderPickTask">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="订单号" :label-width="formLabelWidth">
          <el-input v-model="searchData.billNo" size="mini" placeholder="请输入订单号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货区" :label-width="formLabelWidth">
          <el-input v-model="searchData.areaCode" size="mini" placeholder="请扫描货区编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货架" :label-width="formLabelWidth">
          <el-input v-model="searchData.shelfCode" size="mini" placeholder="请扫描货架编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货位" :label-width="formLabelWidth">
          <el-input v-model="searchData.cellCode" size="mini" placeholder="请扫描货位编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="getPickTask()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 100px;margin-top: 5px;left: auto"> 查询</el-button>
    </div>
    <div style="text-align: center;">
      <el-form class="noMargin"  ref="form">
        <el-form-item label="目标货位" :label-width="formLabelWidth">
            <el-select v-model="toCellCode" filterable placeholder="请选择目标货位" size="small" v-if="firstPickTask.toCellCode === null || firstPickTask.toCellCode === undefined"
                       style="width: 70%;">
              <el-option
                      v-for="item in cells"
                      :key="item.cellCode"
                      :label="item.cellCode"
                      :value="item.cellCode">
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="追踪号" :label-width="formLabelWidth">
          <el-input v-model="trackingNum" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="pickTaskCommit()" type="primary" size="mini" round icon="iconfont icon-dui" style="width: 100px;margin-top: 5px;right: auto"> 拣货确认</el-button>
    </div>
    <el-divider content-position="center">拣货信息</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in pickTasks">
          <el-card class="clearFloat">
            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{searchData.total}}</span>
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
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                  </li>
                  <li>
                    <span>货区:</span>
                    <span>&nbsp;{{item.areaCode}}</span>
                  </li>
                  <li>
                    <span>货架:</span>
                    <span>&nbsp;{{item.shelfCode}}</span>
                  </li>
                  <li>
                    <span>货位:</span>
                    <span>&nbsp;{{item.cellCode}}</span>
                  </li>
                  <li>
                    <span>目标货位:</span>
                    <span>&nbsp;{{item.toCellCode}}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>追踪号:</span>
                    <span>&nbsp;{{item.boxCode}}</span>
                  </li>
                  <li>
                    <span>料号:</span>
                    <span>&nbsp;{{item.itemCode}}</span>
                  </li>
                  <li>
                    <span>批次:</span>
                    <span>&nbsp;{{item.batchName}}</span>
                  </li>
                  <li>
                    <span>单位:</span>
                    <span>&nbsp;{{item.packDescribe}}</span>
                  </li>
                  <li>
                    <span>lpn:</span>
                    <span>&nbsp;{{item.lpn}}</span>
                  </li>
                </ul>
              </div>
              <div class="mainData clearFloat">
                <!-- 重要信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{item.pickQuantity}}</span>
                    <span>需拣</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>&nbsp;{{item.reviewQuantity}}</span>
                    <span>已拣</span>
                  </li>
                </ul>
              </div>
            </div>
            <!--<div class="cardBottomBtn clearFloat">
              &lt;!&ndash; 底部按钮 &ndash;&gt;
              <div class="leftFloat"></div>
              <div class="rightFloat" @click="$router.push({name: 'orderPickTaskOne',params: item})">拣货</div>
            </div>-->
          </el-card>
        </li>
      </ul>
      <p v-show="pickTasks.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "orderPickTask",
    data() {
      return {
        headerTitle: "订单拣货确认",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,


        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          states:[0,1],
          cellCode:'',
          shelfCode:'',
          areaCode:'',
          billNo:'',
        },
        trackingNum:'',
        toCellCode:'',
        pickTasks:[],
        cells:[],
      };
    },
    methods: {
      pickTaskCommit(){
        let ids = [];
        if(this.pickTasks.length <=0){
          IOT.tips('请输入订单号并查询！', 'error');
          return false;
        }
        if(this.toCellCode === null || this.toCellCode === ''){
          IOT.tips('请输入目标货位号！', 'error');
          return false;
        }
        if(this.trackingNum === null || this.trackingNum === ''){
          IOT.tips('请输入追踪号！', 'error');
          return false;
        }
        for(let i=0;i<this.pickTasks.length;i++){
          ids.push(this.pickTasks[i].pickTaskId);
        }
        IOT.getServerData('/pick/tasks/commitPick','post',{ids:ids},(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            IOT.tips('确认成功！', 'success', 1000, () => {
              this.pickTasks = [];
              this.searchData.cellCode = '';
              this.searchData.shelfCode = '';
              this.searchData.areaCode = '';
              this.searchData.billNo = '';
              this.toCellCode = '';
              this.trackingNum = '';
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPickTask(){
        /*this.searchData.states.push(0);
        this.searchData.states.push(1);*/
        if(!PF.isNull(this.searchData.billNo)){
          IOT.tips('请输入订单号！', 'error');
          return false;
        }
        this.loading = true;
        IOT.getServerData('/pick/tasks/findFirst','post',this.searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.pickTasks = ret.rows;
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
        if(this.pickTasks.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getPickTask();
        }
      },
      getCellData(){
        IOT.getServerData('/cell/infos/list','get',{cellUseType:5},(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      let cellCode = this.$route.params.cellCode;
      if(PF.isNull(cellCode)){
        this.searchData.cellCode = cellCode;
      }
      this.getCellData();
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .orderPickTask {
  }
</style>
