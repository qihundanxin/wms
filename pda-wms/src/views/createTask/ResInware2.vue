<template>
  <div class="resInware2">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="formBox">
      <div class="formRow">
        <span class="lable">货物</span>
        <input class="noBdIpt" type="text" v-model="params.itemData.name" placeholder="请输入货物" />
      </div>
      <div class="formRow">
        <span class="lable">货物</span>
        <input class="noBdIpt" type="text" v-model="params.itemData.code" placeholder="请输入货物" />
      </div>
    </div>
    <div class="formBox2 clearFloat" v-for="batchItem,index in batchItems">
      <div class="formBox leftFloat">
        <div class="formRow">
          <span class="lable">批号</span>
          <!-- <input class="noBdIpt" type="text" placeholder="请输入批号" /> -->
          <div style="display: inline-block;width: 58%;text-align: left;">
            <el-select size="mini" v-model="value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="formRow">
          <span class="lable">数量</span>
          <!-- <input class="noBdIpt" type="number" v-model="params.code" /> -->
          <div style="display: inline-block;width: 58%;text-align: left;">
            <el-input-number size="mini" v-model="batchItem.batchQuantity"></el-input-number>
          </div>
        </div>
        <div v-if="params.fromRoute == 'resInware' || params.fromRoute == 'resOutware'" class="formRow">
          <span class="lable">生产日期</span>
          <!-- <input class="noBdIpt" type="text" placeholder="年-月-日 时:分:秒" /> -->
          <div style="display: inline-block;width: 58%;text-align: left;">
            <el-date-picker v-model="date" size="mini" type="datetime" placeholder="选择日期时间"></el-date-picker>
          </div>
        </div>
        <div v-if="params.fromRoute == 'resTuiware'" class="formRow">
          <span class="lable">库位</span>
          <!-- <input class="noBdIpt" type="text" placeholder="年-月-日 时:分:秒" /> -->
          <div style="display: inline-block;width: 58%;text-align: left;">
            <el-select size="mini" v-model="value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div v-if="params.fromRoute == 'resTuiitem'" class="formRow">
          <span class="lable">客户</span>
          <!-- <input class="noBdIpt" type="text" placeholder="年-月-日 时:分:秒" /> -->
          <div style="display: inline-block;width: 58%;text-align: left;">
            <el-select size="mini" v-model="value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
      </div>
      <el-button class="rightFloat" type="text" icon="el-icon-delete" @click="deleteBatchItem(index)"></el-button>
    </div>
    <el-button type="text" class="otherBox" @click="addBatch">其他批号</el-button>
    <FeaturesFooter :footerBtn="footerBtn" :batchItems="batchItems" :itemMaster="params" />
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
import FeaturesFooter from "@/components/FeaturesFooter";
export default {
  name: "resInware2",
  data() {
    return {
      date: "",
      headerTitle: "",
      footerBtn: "确定",
      params: this.$route.params,
      batchItems: [
        {
          batchQuantity: 2
        }
      ],
      options: [
        {
          value: "1",
          label: "黄金糕"
        },
        {
          value: "2",
          label: "双皮奶"
        },
        {
          value: "3",
          label: "蚵仔煎"
        },
        {
          value: "4",
          label: "龙须面"
        },
        {
          value: "5",
          label: "北京烤鸭"
        }
      ],
      value: ""
    };
  },
  methods: {
    toAddItem() {
      this.$router.push({ name: "addItem" });
    },
    addBatch() {
      this.batchItems.push({ batchQuantity: null });
    },
    deleteBatchItem(index) {
      this.batchItems.splice(index, 1);
    }
  },
  created() {
    if (this.params.fromRoute == undefined) {
      this.$router.push({ name: "home" });
    }
    if (
      this.params.itemData.name == undefined ||
      this.params.itemData.code == undefined
    ) {
      this.$router.push({ name: this.params.fromRoute });
    }
    if (this.params.fromRoute == "resInware") {
      this.headerTitle = "预约入库";
    } else if (this.params.fromRoute == "resOutware") {
      this.headerTitle = "预约出库";
    } else if (this.params.fromRoute == "resTuiware") {
      this.headerTitle = "预约退库";
    } else if (this.params.fromRoute == "resTuiitem") {
      this.headerTitle = "预约退货";
    }
  },
  mounted() {},
  components: {
    FeaturesHeader,
    FeaturesFooter
  }
};
</script>
<style lang="less" scoped>
.resInware2 {
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
  .formBox2 {
    border-bottom: 2px solid #ebeef5;
    position: relative;
    .formBox {
      width: 70%;
      border-bottom: 0;

      .formRow {
        .lable {
          width: 40%;
        }
        .noBdIpt {
          width: 58%;
        }
      }
    }
    button {
      width: 20%;
      border: 0;
      font-size: 40px;
      position: absolute;
      top: 50%;
      right: 0;
      margin-top: -32px;
    }
  }
  .otherBox {
    position: fixed;
    bottom: 41px;
    right: 25px;
  }
}
</style>
