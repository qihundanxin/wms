<template>
  <div class="featuresHeader">
    <div class="headerBox clearFloat">
      <span class="iconfont icon-zuo leftFloat clickColor" @click="backOut" style="font-size: 22px;"></span>
      <span class="iconfont icon-shouye leftFloat clickColor" @click="$router.push({name: 'home'})" style="margin-left: 10px;font-size: 22px;"></span>
      <span class="headerTitle">{{headerTitle}}</span>
      <span  style="font-size: 15px;" @click="printParam"  class="rightFloat  ">打印参数</span>
      <span
              v-if="sxFlg"
              class="rightFloat el-icon-set-up"
              @click.stop="showModal"
      ></span>
    </div>
    <div v-if="headerTitle == '库存流水'" class="sxBox" @click.self="hideModal">
      <div :class="isClickSx?'clickSx sx':'sx'">
        <div class="head">筛选条件</div>
        <div class="content">
          <!-- <router-link tag="div" :to="{name: 'filter'}" class="row clearFloat" v-for="sxRow in sxRows"> -->
          <div class="row clearFloat" v-for="sxRow in sxRows" @click="toFilterShelf(sxRow.name)">
            <span>{{sxRow.name}}</span>
            <span>{{sxRow.selectItems}}</span>
            <span class="el-icon-arrow-right"></span>
          </div>
          <!-- </router-link> -->
        </div>
        <div class="foot">
          <span>清除筛选</span>
          <span @click.stop="hideModal">确定</span>
        </div>
      </div>
    </div>
    <div v-else-if="headerTitle == '保质期预警'" class="sxBox" @click.self="hideModal">
      <div :class="isClickSx?'clickSx sx':'sx'">
        <div class="head">筛选条件</div>
        <div class="content">
          <!-- <router-link tag="div" :to="{name: 'filter'}" class="row clearFloat" v-for="sxRow in sxRows"> -->
          <div class="row clearFloat" v-for="sxRow in sxRows" @click="toFilterShelf(sxRow.name)">
            <span>{{sxRow.name}}</span>
            <span>{{sxRow.selectItems}}</span>
            <span class="el-icon-arrow-right"></span>
          </div>
          <!-- </router-link> -->
        </div>
        <div class="foot">
          <span>清除筛选</span>
          <span @click.stop="hideModal">确定</span>
        </div>
      </div>
    </div>
    <div v-else-if="headerTitle == '库存报表'" class="sxBox" @click.self="hideModal">
      <div :class="isClickSx?'clickSx sx':'sx'">
        <div class="head">筛选条件</div>
        <div class="content">
          <!-- <router-link tag="div" :to="{name: 'filter'}" class="row clearFloat" v-for="sxRow in sxRows"> -->
          <div class="row clearFloat" v-for="sxRow in sxRows" @click="toFilterShelf(sxRow.name)">
            <span>{{sxRow.name}}</span>
            <span>{{sxRow.selectItems}}</span>
            <span class="el-icon-arrow-right"></span>
          </div>
          <!-- </router-link> -->
        </div>
        <div class="foot">
          <span>清除筛选</span>
          <span @click.stop="hideModal">确定</span>
        </div>
      </div>
    </div>
    <div v-else-if="headerTitle == '入库商品明细表'" class="sxBox" @click.self="hideModal">
      <div :class="isClickSx?'clickSx sx':'sx'">
        <div class="head">筛选条件</div>
        <div class="content">
          <!-- <router-link tag="div" :to="{name: 'filter'}" class="row clearFloat" v-for="sxRow in sxRows"> -->
          <div class="row clearFloat" v-for="sxRow in sxRows" @click="toFilterShelf(sxRow.name)">
            <span>{{sxRow.name}}</span>
            <span>{{sxRow.selectItems}}</span>
            <span class="el-icon-arrow-right"></span>
          </div>
          <!-- </router-link> -->
        </div>
        <div class="foot">
          <span>清除筛选</span>
          <span @click.stop="hideModal">确定</span>
        </div>
      </div>
    </div>
    <div v-else-if="headerTitle == '出库商品明细表'" class="sxBox" @click.self="hideModal">
      <div :class="isClickSx?'clickSx sx':'sx'">
        <div class="head">筛选条件</div>
        <div class="content">
          <!-- <router-link tag="div" :to="{name: 'filter'}" class="row clearFloat" v-for="sxRow in sxRows"> -->
          <div class="row clearFloat" v-for="sxRow in sxRows" @click="toFilterShelf(sxRow.name)">
            <span>{{sxRow.name}}</span>
            <span>{{sxRow.selectItems}}</span>
            <span class="el-icon-arrow-right"></span>
          </div>
          <!-- </router-link> -->
        </div>
        <div class="foot">
          <span>清除筛选</span>
          <span @click.stop="hideModal">确定</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "featuresHeader",
  data() {
    return {
      isClickSx: false,
      routes: [
        "resInware",
        "resOutware",
        "resTuiware",
        "resTuiitem",
        "inventoryWater",
        "inventoryWarning"
      ],
      sxFlg: false,
      sxRows: [
        {
          name: "货架",
          selectItems: 'LG手机01,LG手机02'
        },
        {
          name: "库位",
          selectItems: ''
        },
        {
          name: "客户",
          selectItems: ''
        },
        {
          name: "货物",
          selectItems: ''
        },
        {
          name: "需方商",
          selectItems: ''
        },
        {
          name: "类型",
          selectItems: ''
        }
      ],
      sxRows: [
        {
          name: "货架",
          selectItems: ''
        },
        {
          name: "库位",
          selectItems: ''
        },
        {
          name: "客户",
          selectItems: ''
        },
        {
          name: "货物",
          selectItems: ''
        }
      ]
    };
  },
  props: {
    headerTitle: String,
    beforeBack: Function,
  },
  methods: {
    printParam() {
      IOT.setPrintParam();
    },
    backOut() {
      let goBack = true;
      if (this.beforeBack) {
        goBack = this.beforeBack();
      }
      if (goBack !== false) {// 仅当明确返回false时拦截
        let path = this.$router.currentRoute.name;
        if (this.routes.indexOf(path) == -1) {
          this.$router.go(-1);
        } else {
          this.$router.push({ name: "home" });
        }
      }
    },
    sxShowFlg() {
      if (this.headerTitle == "库存流水"
        || this.headerTitle == "保质期预警"
        || this.headerTitle == "库存报表"
        || this.headerTitle == "入库商品明细表"
        || this.headerTitle == "出库商品明细表") {
        this.sxFlg = true;
      }
    },
    showModal() {
      let sxBox = document.querySelector(".sxBox");
      sxBox.style.display = "block";
      this.isClickSx = true;
    },
    hideModal() {
      let sxBox = document.querySelector(".sxBox");
      sxBox.style.display = "none";
      this.isClickSx = false;
    },
    toFilterShelf(name) {

      this.$router.push({ name: "filterShelf", params: { name: name } });
    }
  },
  created() {
    this.sxShowFlg();
  }
};
</script>
<style lang="less" scoped>
  .featuresHeader {
    .headerBox {
      height: 50px;
      background: #f6f8f7;
      border-bottom: 2px solid #f0f0f0;
      font-size: 30px;
      display: flex;
      align-items: center;
      position: relative;

      .leftFloat {
        margin-left: 20px;
      }

      .rightFloat {
        position: absolute;
        right: 20px;
        color: #409eff;
      }
     font-size: 15px;

      .headerTitle {
        width: 50%;
        font-size: 18px;
        position: absolute;
        left: 50%;
        margin-left: -25%;
        text-align: center;
      }
    }

    .sxBox {
      display: none;
      font-size: 20px;
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      background: rgba(0, 0, 0, 0.6);
      z-index: 2019;

      .sx {
        width: 75%;
        background: #fff;
        float: right;
        position: absolute;
        right: -75%;
        top: 0;
        bottom: 0;
        padding-bottom: 60px;

        .head {
          line-height: 50px;
          background: #f0f0f0;
        }

        .content {
          .row {
            line-height: 50px;
            border-bottom: 1px solid #f0f0f0;

            span {
              line-height: 50px;
              overflow: hidden;
              white-space: nowrap;
            }

            span:first-child {
              width: 26%;
              text-align: left;
              margin-left: 4%;
              float: left;
            }

            span:nth-child(2) {
              width: 60%;
              font-size: 16px;
              text-align: right;
              float: left;
            }

            span:last-child {
              width: 8%;
              text-align: right;
              float: right;
              padding-right: 2%;
              color: #409eff;
              font-size: 24px;
            }
          }
        }

        .foot {
          width: 100%;
          position: absolute;
          bottom: 0;

          span {
            line-height: 60px;
          }

          span:active {
            color: #409eff;
          }

          span:first-child {
            margin-left: 20px;
            float: left;
          }

          span:last-child {
            float: right;
            margin-right: 20px;
          }
        }
      }

      .sx.clickSx {
        right: 0%;
        transition: right .5s;
      }
    }
  }
</style>
