<template>
    <!--选择波次-->
    <div class="waveSeeding1">
        <FeaturesHeader :header-title="headerTitle"></FeaturesHeader>

         <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
 
          <el-input v-model="searchData.waveMasterCode" size="mini" placeholder="波次号" clearable style="width: 90%;"></el-input>
           <el-input v-model="searchData.boxCode" size="mini" placeholder="容器号" clearable style="width: 90%;"></el-input>
   
         
      </el-form>
      <el-button @click="searchData.pageSize = 2;waves = [];getWaveData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待播种波次</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in waves" @click="$router.push({name: 'waveSeeding2',params: {boxCode:item.boxCode}})">
          <el-card class="clearFloat">

            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{searchData.total}}</span>
               
              </div>
               
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>波次号:</span>
                    <span>&nbsp;{{item.waveId}}</span>
                  </li>
                  <li>
                    <span>容器号:</span>
                    <span>&nbsp;{{item.boxCode}}</span>
                  </li>
                  
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                   
                  <li>
                    <span>订单总数：</span>
                    <span :title="item.totalBillCount">&nbsp;{{item.totalBillCount}}</span>
                  </li>
                  <li>
                    <span>sku总数：</span>
                    <span>&nbsp;{{item.totalSkuCount}}</span>
                  </li>
                   
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="waves.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>
    </div>

</template>

<script>
    import FeaturesHeader from "../../components/FeaturesHeader";

    export default {
        name: "waveSeeding1",
        components: {FeaturesHeader},
        data() {
            return {
                noMore:false,
                loading:false,
                headerTitle: "波次播种",
                formLabelWidth: "200",
                waves:[],
                searchData:{
                    isSeeding:1,
                    state:2,
                    waveMasterCode: '',
                    wareId:localStorage.getItem("wareId"),
                },
                form: {
                    waveMasterCode: '',
                    itemCode: '',
                    itemName: '',
                    quantity: null
                }
            }
        },
        methods: {
             getWaveData (){
                this.loading = true;
                IOT.getServerData('/wave/masters/list','get',this.searchData,(ret) => {
                this.loading = false;
                if (ret.code === 200) {
                    this.waves = ret.rows;
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
                if(this.waves.length > 0 && !this.noMore && !this.loading){
                  this.searchData.pageSize += 2;
                  this.getAsnData();
                }
              },
            
        },
        created() {
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>