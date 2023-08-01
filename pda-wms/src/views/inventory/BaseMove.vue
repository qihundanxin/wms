<template>
  <div v-loading="loading" class="BaseMove">
    <FeaturesHeader :headerTitle="headerTitle" /> 
    <div style="text-align: center;">
        <el-input v-model="searchData.eqCellCode" size="mini" placeholder="请输入货位号" clearable style="width: 90%;"></el-input>
        <el-input v-model="searchData.boxCode" size="mini" placeholder="请输入跟踪号" clearable style="width: 90%;"></el-input>
        <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入SKU" clearable style="width: 90%;"></el-input>
      <el-button @click="searchData.pageSize = 8;getInventoryBalanceData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div> 
    <el-divider content-position="center">库存</el-divider> 
    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
          <li v-for="(item,index) in inventoryBalances" @click="baseMove2(item)">
          <el-card> 
            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{inventoryBalances.length}}</span>
                
                <span>{{item.itemCode}}</span>
              </div>
              <div class="minorData ">
                <!-- 基础信息 -->
                <ul  >
                  <li>
                    <span style="width:60px" v-for="cellUseType in cellUseTypes" v-if="cellUseType.value === item.cellUseType">{{cellUseType.label}}</span>
                    <span>&nbsp;{{item.cellCode}} </span>
                    
                  </li>
                  <!-- <li>
                    <span>LPN:</span>
                    <span>&nbsp;{{item.lpn}}</span>
                  </li> -->
                </ul>
                <!-- <ul class="rightFloat">
                  <li>
                    <span>追踪号:</span>
                    <span>&nbsp;{{item.boxCode}}</span>
                  </li>
                </ul> -->
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
                  <li>
                    <span v-if="item.allotQuantity">&nbsp;{{item.allotQuantity}}</span>
                    <span v-else-if="item.allotQuantity === 0">&nbsp;0</span>
                    <span v-else>&nbsp;-</span>
                    <span>分配数:</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span v-if="item.packDescribe">&nbsp;{{item.packDescribe}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>单位:</span>
                  </li>
                  <li>
                    <span v-if="item.freezeQuantity">&nbsp;{{item.freezeQuantity}}</span>
                    <span v-else-if="item.freezeQuantity === 0">&nbsp;0</span>
                    <span v-else>&nbsp;-</span>
                    <span>冻结数:</span>
                  </li>
                </ul>
              </div>
              <div class="minorData clearFloat">
                <!-- 次要/整行信息 -->
                <ul>
                  <!-- <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li> -->
                  
                  <li>
                    <span>名称:</span>
                    <span>&nbsp;{{item.itemName}}</span>
                  </li>
                 <li>
                    <span>批次:</span>
                    <span>&nbsp;{{item.batchName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span>&nbsp;{{item.organizationName}}</span>
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

import { mapState } from "vuex";

export default {
  name: "BaseMove",
  data() {
    return {
      headerTitle: "基础移库",
      formLabelWidth: '120px',

      loading: false,
      noMore: false,

      inventoryBalances: [],

      searchData: {
        pageNum: 1,
        pageSize: 8,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        boxCode: '',
        itemCode: '',
        lpn: '',
        eqCellCode: '',
        sort:"cellUseTypeUseful"
      },
    };
  },
  computed: {
    ...mapState([
      "cellUseTypes",
    ]),
  },
  methods: {
        //点击后的跳转到移库详情页
      baseMove2(item) {
      // 缓存本页查询参数
      if(this.lastSearchData){
        PF.setLocal('PageParam_BaseMove_Search',this.lastSearchData);
      }
      //判断是否是垃圾货位，如果是，提示用户不可操作
      if (item.cellUseType == -1) {
        IOT.tips('此货位不可进行基础移库操作', 'error');
      } 
      if(item.cellUseType==3){
         IOT.tips('警告，集货位不可操作基础移库！','warning',2000)
       }
      else {
        this.$router.push({ name: 'baseMove2', params: item });
      }
    },
    getInventoryBalanceData() {
      this.loading = true;
      let searchData = this.searchData;
      if (!PF.isNull(searchData.toBoxCode)) {
        delete searchData.toBoxCode;
      }
      if (!PF.isNull(searchData.itemCode)) {
        delete searchData.itemCode;
      }
      //原基础移库
      IOT.getServerData('/inventorys/moveList', 'post', searchData, (ret) => {
        this.loading = false;
        if (ret.code === 200) {
          this.lastSearchData = JSON.parse(JSON.stringify(searchData));
          this.inventoryBalances = ret.rows;
          console.log(this.inventoryBalances);
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
          if (this.searchData.pageSize >= ret.total) {
            this.noMore = true;
          } else {
            this.noMore = false;
          }
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    lazy() {
      if (this.inventoryBalances.length > 0 && !this.noMore && !this.loading) {
        this.searchData.pageSize += 2;
        this.getInventoryBalanceData();
      }
    },
  },
  created() {
    let toBoxCode = this.$route.params.toBoxCode;
    if (PF.isNull(toBoxCode)) {
      this.searchData.toBoxCode = toBoxCode;
    }
  },
  mounted() {
    // PageParam_BaseMove_Search参数与PageParam_BaseMove_Search_Use标识共同生效
    const cacheSearchData = PF.getLocal('PageParam_BaseMove_Search','json');
    const use = PF.getLocal('PageParam_BaseMove_Search_Use');
    if(cacheSearchData && use){
      this.searchData = cacheSearchData;
      this.getInventoryBalanceData();
    }
    PF.removeLocal('PageParam_BaseMove_Search');
    PF.removeLocal('PageParam_BaseMove_Search_Use');
  },
  watch: {
    'searchData.itemCode': {
      handler: function (newVal) {
        let qrObject = PF.simpleQrEncode(newVal);
        if (qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
          this.searchData.itemCode = qrObject.index2;
        }
      },
      deep: true
    },
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
