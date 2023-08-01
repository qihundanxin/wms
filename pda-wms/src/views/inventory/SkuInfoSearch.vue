<template>
  <div class="skuInfoSearch">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="radio">
      <div :class="radio == 1 ? 'active' : ''" @click="radio = 1">扫码查询</div>
      <div :class="radio == 2 ? 'active' : ''" @click="radio = 2">直接输入</div>
    </div>
    <div class="skuSearch">
      <div style="width:93%" class="scanInput" v-if="radio == 1">
        <el-input ref="skuInput" style="width:92%" v-model="searchData.itemCode" @keyup.enter.native="search"
          placeholder="SKU/UPC" clearable></el-input>
        <i class="el-icon-camera" style="font-size:30px;margin-left:80%" @click="scan(1)"></i>
      </div>
      <div style="width:98%" class="scanInput" v-if="radio == 2">
            <el-input ref="skuInput" style="width:94%" v-model="itemSearchData.keyWords" @keyup.enter.native="search(2)"
          placeholder="SKU/名称/体积/型号/货主名称"></el-input>
      </div>
      <!-- <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="商品编号或仓位" :label-width="formLabelWidth">
          <el-input
              ref="skuInput"
              v-model="searchData.itemCode"
              @keyup.enter.native="search"
              size="mini"
              placeholder="商品编号/仓位"
              clearable
              style="width: 90%"
          ></el-input>
        </el-form-item>
      </el-form> -->
      <el-button @click="search(radio)" type="primary" size="mini" round icon="iconfont icon-chaxun" class="searchBtn">
        查询
      </el-button>
    </div>
    <div class="skuDetail" v-loading="loading" element-loading-text="拼命加载中" v-show="skuDetailShow">
      <div class="top">
        <div class="left">
          <img :src="result.imgUrl" alt="">
        </div>
        <div class="right">
          <div>SKU：{{ result.itemCode }}</div>
          <div v-if="result.upcs != null && result.upcs.length > 0">UPC：{{ result.upcs[0].upc }}</div>
          <div>仓库：{{ result.wareName }}</div>
          <div>货位： <el-button type="text" @click="showOthers = !showOthers">所有货位</el-button></div>
          <!-- <div v-else >仓位：{{ result.cellCode }}</div> -->
          <div>库存总量：{{ result.quantity }}</div>
          <!-- <el-button  type="text" @click="showOthers=!showOthers">所有仓位</el-button> -->
        </div>
      </div>
      <div class="bottom">{{ result.itemName }}</div>
    </div>
    <div class="noScrollbar itemSearch" v-show="itemShow">
      <ul v-infinite-scroll="lazy" infinite-scroll-disabled="busy" infinite-scroll-distance="20">
        <li v-for="(item,index) in itemOptions" :key="index" @click="searchOne(item.itemCode)" class="itemList">
          <div>
            <p>sku：{{item.itemCode}}</p>
            <p>历史货位：{{ historyPickCell }}</p>
            <p>{{item.itemName}}</p>
          </div>
           
        </li>
    </ul>

    </div>

    <p style="text-align: center; margin:5px auto" v-show="loading  && radio==2">加载中...</p>
    <p style="text-align: center;" v-show="noMore  && radio==2">没有更多了</p>
    <div class="wareList" id="wareList" v-show="showOthers">
      <ul v-for="(item, index) in results" :key="index">
        <li>货位：<span class="item">{{ item.cellCode }}</span></li>
        <li>数量：<span class="item">{{ item.quantity }}</span></li>
        <li>货位使用类型：<span class="item" style="width:60px" v-for="cellUseType in cellUseTypes" v-if="cellUseType.value === item.cellUseType">{{cellUseType.label}}</span></li>
      </ul>
    </div>

    <p style="font-size:14px; text-align: center; position: fixed; bottom:20px;color:grey;width: 100%;background-color: #ffffff;padding-top:5px;padding-bottom: 5px;">该查询的库存数量仅包含：拣货位、验收位、仓储位！</p>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";

import { mapState } from "vuex";

export default {
  name: "skuInfoSearch",
  data() {
    return {
      results: [],
      headerTitle: "商品基本信息查询",
      formLabelWidth: "150px",
      loading: false,
      searchData: {
        sort:'cellUseTypeUseful',
        itemCode: '',
        wareId: PF.getLocal("wareId", "number"),
      },
      result: {
        upcs: []
      },
      wareList: [],
      height: 0,
      showOthers: false,
      radio: 1,
      itemSearchData: {
        pageNum: 1,
        pageSize: 7,
        keyWords: '',
        total: null,
        wareId: PF.getLocal('wareId', 'number'),
      },
      itemOptions:[],
      itemLoading:false,
      itemValue:[],
      skuDetailShow:false,
      itemShow:false,
      noMore: false,
      busy:false,
      keyWordsUpdate:false,
      historyPickCell: '',
    };
  },
  mounted() {
    // 自动聚焦
    this.$nextTick(() => {
      if(this.radio==1){
        this.$refs.skuInput.focus()
      }
    })
    // 其他仓库列表高度自适应
    this.height = document.documentElement.clientHeight - 342
    document.getElementById('wareList').style.height = this.height + 'px'
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
  computed: {
    ...mapState([
      "cellUseTypes",
    ]),
  },
  methods: {
    lazy(){
        if(this.itemOptions.length > 0 && !this.noMore){
          this.loading=true;
          this.itemSearchData.pageSize+=5;
          this.searchItem();
        }
      },
    searchItem() {
      this.loading = true;
      this.busy=true;
      this.noMore = false;
          IOT.getServerData('/item/infos/list', 'get', this.itemSearchData, (ret) => {
            this.loading = false;
            this.noMore = false;
            if (ret.code === 200) {
              this.busy=false;
              this.itemOptions = ret.rows;
              this.itemSearchData.pageNum = ret.pageNumber;
              if(this.itemSearchData.pageNum>=ret.pages){
                this.noMore = true;
              }
              this.itemShow =true;
              this.skuDetailShow =false;
              this.checkHistoryPickCell();
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
            console.log("-----------------状态----------------");
            console.log(this.busy);
            
            
          });   
      },
    checkHistoryPickCell() {
      IOT.getServerData('/inventorys/historyPickCell', 'post', this.itemSearchData, (ret) => {
        if(ret.code == 200) {
          this.historyPickCell = ret.data.cellName;
        }
      }, true);
    },
    scan(scanType) {
      SCAN.startRecognize(
        (type, result) => {
          if (scanType == 1) {
            this.searchData.itemCode = result;
          }

          SCAN.close();
        });
    },
    searchOne(param){
      this.itemSearchData.keyWords = '';
      this.searchData.itemCode = param;
      this.noMore =false;
      this.loading =false;
      this.search();
    },
    search(num) {
      if(num==2){
        this.itemOptions=[];
        this.itemSearchData.pageSize=7;
      }
      this.showOthers = false;
      if(this.radio==2 && this.searchData.itemCode==''){
        this.searchItem();
        return;
      }
      if (!this.searchData.itemCode || typeof this.searchData.itemCode != 'string' || this.searchData.itemCode.trim().length <= 1) {
        IOT.tips("请输入商品编码！", "error");
        return false;
      }
      this.loading = true;
      IOT.getServerData(
        "/inventorys/pdaSearch",
        "get",
        this.searchData,
        (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.skuDetailShow=true;
            this.results = ret.rows;
            if (this.results.length > 0) {
              this.result = JSON.parse(JSON.stringify(this.results[0]));
              var totalQuantoty = 0;
              this.results.forEach(element => {
                totalQuantoty += element.quantity;
              });
              this.result.quantity = totalQuantoty;
            } else {
              this.result.upcs = [];
            }
            this.searchData.itemCode = '';
            this.itemSearchData.keyWords = '';
            this.itemOptions =[];

          } else {
            IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
          }
          this.searchData.itemCode = ''
          this.showOthers = false
          if(this.radio==1){
            this.$refs.skuInput.focus()
          }

        }
      );
    },
    searchOthers() {
      if (!this.showOthers) {
        this.showOthers = true
      } else {
        this.showOthers = false
      }
    }
  },
  components: {
    FeaturesHeader,
  }
};
</script>
<style lang="less" scoped>
.skuInfoSearch {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  font-family: "Microsoft YaHei";

  .radio {
    width: 100%;
    margin: auto 5px;
    display: flex;
    height: 40px;
    line-height: 40px;

    div {
      width: 50%;
      text-align: center;
      border: 1px solid rgb(197, 194, 194);
    }

    .active {
      background: #409EFF;
      color: white;
    }
  }

  .skuSearch {
    width: calc(100% - 25px);
    margin: 5px auto;
    padding: 5px;
    text-align: center;
    border: 1px solid #ccc;

    .searchBtn {
      width: calc(100% - 20px);
    }
  }

  .skuDetail {
    width: calc(100% - 15px);
    margin: 0 auto 5px;
    border: 1px solid #ccc;

    .top {
      display: flex;
      justify-content: space-around;
      align-items: center;

      .left {
        width: 120px;
        height: 120px;

        img {
          width: 100%;
          height: 100%;
        }
      }

      .right {
        line-height: 25px;

        .otherWare {
          text-decoration: underline;
          color: #2b7eb4;
          cursor: pointer;
        }
      }
    }

    .bottom {
      margin: 5px 0 8px 5px;
    }
  }
  .itemSearch{
    overflow:auto;
    max-height: 420px;
  }
  .itemList{
    width: calc(100% - 15px);
    margin: 0 auto 5px;
    border: 1px solid #ccc;
    padding:auto 5px;
    div{
      margin:8px 5px;
      p{
        margin:2px 0;
      }
      :nth-child(-n+2) {
        font-weight: bold;
      }
    }
  }

  .wareList {
    width: calc(100% - 25px);
    margin: 0 auto;
    border: 1px solid #ccc;
    padding: 5px;
    overflow: auto;
    margin-bottom: 20px;
    // max-height: 210px;

    ul {
      border-bottom: 1px solid #ccc;

      li {
        line-height: 25px;

        .item {
          font-weight: bold;
        }
      }
    }
  }
}
</style>
<style>
.el-select-dropdown{
  max-width:98% !important;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
