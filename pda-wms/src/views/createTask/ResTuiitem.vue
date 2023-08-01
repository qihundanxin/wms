<template>
  <div class="resTuiitem">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="formBox">
      <div class="formRow">
        <span class="lable">需方商</span>
        <input class="noBdIpt" type="text" />
      </div>
      <div class="formRow">
        <span class="lable">客户单号</span>
        <input class="noBdIpt" type="text" />
      </div>
      <div class="formRow">
        <span class="lable">备注</span>
        <input class="noBdIpt" type="text" />
      </div>
    </div>
    <p class="middleP clearFloat">
      <el-button v-show="!bianjiFlg" type="text" class="rightFloat" @click="bianjiFlg = !bianjiFlg">编辑</el-button>
      <el-button v-show="bianjiFlg" type="text" class="rightFloat" @click="bianjiFlg = !bianjiFlg">完成</el-button>
    </p>
    <div
      class="batchItems"
      v-if="params.itemMaster !== undefined || params.batchItems !== undefined"
    >
      <p class="itemName">{{params.itemMaster.name}}</p>
      <div class="batchItem" v-for="batchItem,index in params.batchItems">
        <p class="top clearFloat">
          <i
            v-show="bianjiFlg"
            class="el-icon-circle-close clickColor"
            @click="deleteBatchItem(index)"
          ></i>
          <span class="span1">3.0</span>
          <span>188</span>
          <span>科技公司</span>
          <span
            :class="jiantouArr[index]?'el-icon-arrow-up':'el-icon-arrow-down'"
            @click="hideTable(index)"
          ></span>
        </p>
        <div class="tableBox">
          <div class="my_table">
            <div class="row clearFloat">
              <div>数量</div>
              <div>批号</div>
            </div>
            <div class="row clearFloat">
              <div>
                <input type="text" placeholder="请输入" v-model="batchItem.batchQuantity" />
              </div>
              <div>
                <input type="text" placeholder="请选择" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="addBox el-icon-circle-plus-outline clickColor" @click="toAddItem"></div>
    <FeaturesFooter :footerBtn="footerBtn" />
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
import FeaturesFooter from "@/components/FeaturesFooter";
export default {
  name: "resTuiitem",
  data() {
    return {
      value1: "",
      headerTitle: "预约退货",
      footerBtn: "保存",
      params: this.$route.params,
      jiantouArr: [],
      bianjiFlg: false
    };
  },
  methods: {
    toAddItem() {
      this.$router.push({
        name: "addItem",
        params: { fromRoute: "resTuiitem" }
      });
      this.$route.params;
    },
    hideTable(index) {
      let table = document.querySelectorAll(".my_table")[index];
      if (this.jiantouArr[index]) {
        table.style.marginTop = "-64px";
      } else {
        table.style.marginTop = "0px";
      }
      this.jiantouArr[index] = !this.jiantouArr[index];
      this.$forceUpdate();
    },
    jiantou() {
      let batchItems = this.params.batchItems;
      if (batchItems !== undefined) {
        for (let i = 0; i < batchItems.length; i++) {
          this.jiantouArr.push(true);
        }
      }
    },
    deleteBatchItem(index) {
      this.params.batchItems.splice(index, 1);
    }
  },
  created() {
    this.jiantou();
    console.log(this.params);
  },
  components: {
    FeaturesHeader,
    FeaturesFooter
  }
};
</script>
<style lang="less" scoped>
.resTuiitem {
  padding-bottom: 90px;
  .formBox {
    padding: 0 5%;
    border-bottom: 3px solid #ebeef5;
    .formRow {
      border-bottom: 2px solid #f2f6fc;
      line-height: 40px;
      padding: 0 1%;
      .lable {
        display: inline-block;
        width: 30%;
        text-align: left;
      }
      .lable.required::after {
        content: "*";
        color: red;
      }
      .noBdIpt {
        width: 68%;
        border: 0;
        outline: 0;
        font-size: 14px;
      }
    }
    .formRow:last-child {
      border: 0;
    }
  }
  .middleP {
    border-bottom: 2px solid #e4e7ed;
    button {
      margin-right: 25px;
    }
  }
  .batchItems {
    width: 80%;
    margin: auto;
    .itemName {
      color: #222;
      text-align: left;
      font-size: 18px;
      line-height: 30px;
    }
    .batchItem {
      padding-bottom: 5px;
      border-bottom: 1px solid #e4e7ed;
      .top {
        i {
          width: 10%;
          line-height: 30px;
          text-align: left;
          float: left;
        }
        span {
          width: 28%;
          float: left;
          text-align: left;
          line-height: 30px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .span1 {
          width: 18%;
        }
        span:last-child {
          width: 10%;
          float: right;
          color: #409eff;
          font-size: 30px;
        }
      }
      .tableBox {
        overflow: hidden;
        .my_table {
          border: 2px solid #656565;
          margin-top: 0px;
          transition: margin-top 0.5s;
          .row {
            border: 1px solid #fff;
            display: flex;
            div {
              flex: 2;
              line-height: 24px;
              // display: inline-block;
              // width: 30%;
              // float: left;
              border: 2px solid #656565;
            }
            div:first-child {
              flex: 1;
            }
            input {
              width: 100%;
              border: 0;
              outline: 0;
              text-align: center;
            }
          }
          .row:first-child {
            background: #656565;
            color: #d1d1d1;
          }
        }
      }
    }
  }
  .addBox {
    font-size: 46px;
    color: #409eff;
    position: fixed;
    bottom: 45px;
    left: 50%;
    margin-left: -23px;
  }
}
</style>
