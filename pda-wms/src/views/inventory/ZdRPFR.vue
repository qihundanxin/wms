<template>
  <div class="BaseMove">
    <FeaturesHeader :headerTitle="headerTitle" /> 
    <div style="text-align: center;">
        <el-input v-model="searchData.eqCellCode" size="mini" placeholder="请输入货位号" clearable style="width: 90%;"></el-input>
        <el-input v-model="searchData.boxCode" size="mini" placeholder="请输入跟踪号" clearable style="width: 90%;"></el-input>
        <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入SKU" clearable style="width: 90%;"></el-input>
      <el-button @click="searchData.pageSize = 2;getInventoryBalanceData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div> 
    <el-divider content-position="center">待上架库存</el-divider> 
    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in inventoryBalances" @click="$router.push({name: 'baseMove2',params: item})">
          <el-card> 
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
                    <span>货位号:</span>
                    <span>&nbsp;{{item.cellCode}}</span>
                  </li>
                  <!-- <li>
                    <span>LPN:</span>
                    <span>&nbsp;{{item.lpn}}</span>
                  </li> -->
                </ul>
                <ul class="rightFloat">
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
                    <span v-if="item.quantity">&nbsp;{{item.quantity}}</span>
                    <span v-else-if="item.quantity === 0">&nbsp;0</span>
                    <span v-else>&nbsp;-</span>
                    <span>数量:</span>
                  </li>
                  <!-- <li>
                    <span v-if="item.allotQuantity">&nbsp;{{item.allotQuantity}}</span>
                    <span v-else-if="item.allotQuantity === 0">&nbsp;0</span>
                    <span v-else>&nbsp;-</span>
                    <span>分配数:</span>
                  </li> -->
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span v-if="item.packDescribe">&nbsp;{{item.packDescribe}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>单位:</span>
                  </li>
                  <!-- <li>
                    <span v-if="item.freezeQuantity">&nbsp;{{item.freezeQuantity}}</span>
                    <span v-else-if="item.freezeQuantity === 0">&nbsp;0</span>
                    <span v-else>&nbsp;-</span>
                    <span>冻结数:</span>
                  </li> -->
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span>&nbsp;{{item.organizationName}}</span>
                  </li>
                  <li>
                    <span>批次:</span>
                    <span>&nbsp;{{item.batchName}}</span>
                  </li>
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="inventoryBalances.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BaseMove",
    data() {
      return {
        headerTitle: "主动补货",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        inventoryBalances: [],

        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          boxCode: '',
          itemCode: '',
          lpn:'',
          eqCellCode:''
        },
      };
    },
    methods: {
      getInventoryBalanceData(){
        this.loading = true;
        let searchData = this.searchData;
        if(!PF.isNull(searchData.toBoxCode)){
          delete searchData.toBoxCode;
        }
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode;
        }
        IOT.getServerData('/inventorys/moveList','post',searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.inventoryBalances = ret.rows;
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
        if(this.inventoryBalances.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getInventoryBalanceData();
        }
      },
    },
    created() {
      let toBoxCode = this.$route.params.toBoxCode;
      if(PF.isNull(toBoxCode)){
        this.searchData.toBoxCode = toBoxCode;
      }

    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseUpCell {
  }
</style>
