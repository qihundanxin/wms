<template>
  <div class="addItem">
    <div class="headerBox clearFloat">
      <span class="el-icon-arrow-left leftFloat clickColor" @click="backOut()"></span>
      <input type="text" class="headerInput" placeholder="编号/品名/条码" />
      <i class="el-icon-search clickColor" @click="qwe()"></i>
    </div>
    <div class="itemList">
      <div class="item clickColor" v-for="item in items" @click="toResInware2(item)">
        <p>
          <span>货物</span>
          <span>{{item.name}}</span>
        </p>
        <p>
          <span>编号</span>
          <span>{{item.code}}</span>
        </p>
        <p>
          <span>条码</span>
          <span>{{item.code}}</span>
        </p>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "addItem",
  data() {
    return {
      params: this.$route.params,
      items: [],
      items1: [
        {
          name: "LG手机01",
          code: "LG01"
        },
        {
          name: "LG手机02",
          code: "LG02"
        },
        {
          name: "罗技鼠标",
          code: "LG03"
        },
        {
          name: "正午1",
          code: "ZC20000088000010"
        },
        {
          name: "app测试01",
          code: "cead01"
        }
      ],
      items2: [
        {
          name: "LG手机01",
          code: "LG01"
        },
        {
          name: "LG手机02",
          code: "LG02"
        },
        {
          name: "罗技鼠标",
          code: "LG03"
        }
      ]
    };
  },
  methods: {
    backOut() {
      this.$router.go(-1);
    },
    toResInware2(itemData) {
      this.$router.push({
        name: "resInware2",
        params: { itemData: itemData, fromRoute: this.params.fromRoute }
      });
    },
    listData() {
      if (this.params.fromRoute == "resInware") {
        this.items = this.items1;
      } else if (this.params.fromRoute == "resOutware") {
        this.items = this.items2;
      } else if (this.params.fromRoute == "resTuiware") {
        this.items = this.items2;
      } else if (this.params.fromRoute == "resTuiitem") {
        this.items = this.items1;
      }
    }
  },
  created() {
    if ((this.params.fromRoute == undefined)) {
      this.$router.push({name: 'home'})
    }
    this.listData();
  },
  components: {}
};
</script>
<style lang="less" scoped>
.addItem {
  .headerBox {
    height: 50px;
    background: #f6f8f7;
    border-bottom: 2px solid #f0f0f0;
    font-size: 30px;
    display: flex;
    align-items: center;
    position: relative;
    .leftFloat {
      margin-left: 12px;
    }
    .headerInput {
      background: #e9e9eb;
      width: 60%;
      position: absolute;
      left: 50%;
      margin-left: -30%;
      border: 0;
      height: 32px;
      border-radius: 8px;
      text-align: center;
      outline: 0;
    }
    i {
      position: absolute;
      right: 0;
      margin-right: 12px;
    }
    .headerInput::-webkit-input-placeholder {
      color: #909399;
      font-size: 14px;
      text-align: center;
    }
  }
  .itemList {
    background: #e9e9eb;
    padding: 8px 0;
    .item {
      background: #fff;
      margin-bottom: 5px;
      padding: 0 5%;
      p {
        line-height: 30px;
        border-bottom: 1px solid #ebeef5;
        text-align: left;
        span {
          display: inline-block;
        }
        span:first-child {
          width: 25%;
        }
        span:last-child {
          width: 75%;
        }
      }
      p:last-child {
        border: 0;
      }
    }
    .item:last-child {
      margin-bottom: 0;
    }
    .item:active {
      background: #f2f6fc;
    }
  }
}
</style>
