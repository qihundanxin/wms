<template>
    <!--选择波次-->
    <div class="waveSeeding1">
        <FeaturesHeader :header-title="headerTitle"></FeaturesHeader>
<el-button @click="searchData.pageSize = 5;waves = [];getWaveData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 100%;margin-top: 5px;"> 刷新</el-button>
   
         <!-- <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
 
          <el-input v-model="searchData.waveMasterCode" size="mini" placeholder="波次号" clearable style="width: 90%;"></el-input>
           <el-input v-model="searchData.boxCode" size="mini" placeholder="容器号" clearable style="width: 90%;"></el-input>
   
         
      </el-form>
      <el-button @click="searchData.pageSize = 2;waves = [];getWaveData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div> -->

    <el-divider content-position="center">待拣货波次</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in waves" >
          <el-card class="clearFloat">

            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{searchData.total}}</span>
               
              </div>
              <el-row>
                <el-col  :span="14">
                    <el-form ref="item"  :model="item" label-width="80px" size="mini">
                  <el-form-item label="仓库">
                    <span>{{item.wareName}}</span>
                  </el-form-item>
                  <el-form-item label="波次号">
                    <span>{{item.waveId}}</span>
                  </el-form-item>
                  <el-form-item v-if="item.boxCode!=null" label="容器号">
                    <span>{{item.boxCode}}</span>
                  </el-form-item>
                  <el-form-item label="订单总数">
                    <span>{{item.totalBillCount}}</span>
                  </el-form-item>
                  <el-form-item label="sku总数">
                    <span>{{item.totalSkuCount}}</span>
                  </el-form-item>
                   <el-form-item label="分拣方式">
                    <span v-if="item.isSeeding==1">先拣后分</span>
                    <span v-if="item.isSeeding==0">边拣边分</span>
                  </el-form-item>
                   <!-- <el-form-item label="拣货人">
                    <span v-if="item.allotUserName!=null">{{item.allotUserName}}</span>
                     
                  </el-form-item> -->
              </el-form>
                </el-col>
                <el-col  :span="10">
                    <el-input v-if="item.isSeeding==1" v-model="item.boxCode" size="mini" placeholder="容器号"></el-input>
                      <el-button  style="width:100%" @click="allotPickUserAndBox(item.waveId,item.boxCode,item.isSeeding)" type="primary">领取</el-button>
                </el-col>
              </el-row>
              
               
              
           
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
                headerTitle: "任务领取",
                formLabelWidth: "200",
                waves:[],
                searchData:{
                  pageSize:2,
                  pageNum:1,
                  noAllot:1,
                    state:1,
                    waveMasterCode: '',
                    wareId:localStorage.getItem("wareId"),
                },
                 wareId:localStorage.getItem("wareId"),
                form: {
                    waveMasterCode: '',
                    itemCode: '',
                    itemName: '',
                    quantity: null
                }
            }
        },
        methods: {
          allotPickUserAndBox(waveId,boxCode,isSeeding){
                var that = this;
                
               // var boxCode = that.boxCode;
                var wareId = that.wareId;
               // var gh = that.gh;
                IOT.getServerData('/wave/masters/allotPickUserAndBoxPAD', 'get', {
                    waveId:waveId,
                    boxCode:boxCode,
                    wareId:wareId,
                  //  gh:gh,
                    isSeeding:isSeeding
                }, (ret) => { 
                    if (ret.code === 200) {
                      that.getWaveData();
                      IOT.tips('领取成功！', 'success', 1000, () => { 
                      }); 
                    } else {
                      
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                }); 
            }, 
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
                  this.getWaveData();
                }
              },
            
        },
        created() {
          this.getWaveData();
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>