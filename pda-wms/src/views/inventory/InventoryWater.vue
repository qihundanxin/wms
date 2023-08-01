<template>
  <div class="inventoryWater">
    <FeaturesHeader :headerTitle="headerTitle" />
    <header>
      <!-- <span class="leftFloat">前一天</span> -->
      <!-- <span>{{nowDate}}</span> -->
      <!-- <span class="rightFloat">后一天</span> -->
      <el-date-picker
        v-model="dateValue"
        align="right"
        type="date"
        placeholder="选择日期"
      ></el-date-picker>
        <!-- :picker-options="pickerOptions" -->
    </header>
    <div class="bills">
      <div class="bill" v-for="billData in billDatas">
        <p>
          <span class="sign1" v-if="billData.type == 1">快出</span>
          <span class="sign2" v-if="billData.type == 2">出</span>
          <span>{{billData.billNum}}</span>
          <span>[操作员&nbsp;{{billData.userName}}]</span>
        </p>
        <p>
          <span>操作时间</span>
          <span>{{billData.createDate}}</span>
        </p>
        <p v-if="billData.type == 1">
          <span>客户</span>
          <span>{{billData.kehu}}</span>
          <span>货品名</span>
          <span>{{billData.itemName}}</span>
        </p>
        <p v-if="billData.type == 2">
          <span>需方商</span>
          <span>{{billData.xufang}}</span>
          <span>货品名</span>
          <span>{{billData.itemName}}</span>
        </p>
        <p>
          <span>生产批号</span>
          <span>{{billData.batch}}</span>
          <span>数量</span>
          <span>{{billData.quantity}}</span>
        </p>
        <p>
          <span>库区/货架</span>
          <span>{{billData.ware}}</span>
          <span>库位</span>
          <span>{{billData.cell}}</span>
        </p>
      </div>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "inventoryWater",
  data() {
    return {
      billDatas: [
        {
          type: 1,
          billNum: "RK2017111700005",
          userName: "杜明月",
          createDate: "15:19:14",
          kehu: "华润万家",
          itemName: "205g天鹰纯白薯粉丝",
          batch: "",
          quantity: "50.0",
          ware: "acbee01区-架1",
          cell: "1"
        },
        {
          type: 2,
          billNum: "CK2017111700004",
          userName: "杜明月",
          createDate: "14:40:05",
          xufang: "盛大",
          itemName: "电脑7",
          batch: "",
          quantity: "15.0",
          ware: "acbee01区-架1",
          cell: "1"
        }
      ],
      headerTitle: "库存流水",
      nowDate: "",
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            }
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            }
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            }
          }
        ]
      },
      dateValue: ""
    };
  },
  methods: {
    getDate() {
      var myDate = new Date();
      let Y = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
      let M = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
      M < 10 ? (M = "0" + M) : (M = M);
      let D = myDate.getDate(); //获取当前日(1-31)
      D < 10 ? (D = "0" + D) : (D = D);
      // let W = this.weeks[myDate.getDay()]; //获取当前星期X(0-6,0代表星期天)
      this.nowDate = Y + "-" + M + "-" + D;
    }
  },
  created() {
    this.getDate();
  },
  components: {
    FeaturesHeader
  }
};
</script>
<style lang="less" scoped>
.inventoryWater {
  header {
    // line-height: 40px;
    // background: #e9e9eb;
    /* span:nth-child(1) {
      margin-left: 20px;
    }
    span:nth-child(3) {
      margin-right: 20px;
    }
    span:active {
      color: #409eff;
    } */
  }
  .bills {
    .bill {
      width: 90%;
      margin: 0 5%;
      border-bottom: 2px solid #dcdfe6;
      p:nth-child(1) {
        span:nth-child(1) {
          width: 30px;
          height: 30px;
          line-height: 30px;
          color: #fff;
          font-size: 14px;
          text-align: center;
          border-radius: 50%;
          float: left;
          margin-top: 5px;
          margin-right: 5px;
        }
        .sign1 {
          background: #67c23a;
        }
        .sign2 {
          background: #f56c6c;
        }
        span:nth-child(2),
        span:nth-child(3) {
          width: auto;
          margin-left: 10px;
        }
      }
      p {
        text-align: left;
        height: 40px;
        border-bottom: 1px solid #ebeef5;
        span {
          display: inline-block;
          height: 40px;
          line-height: 40px;
          // text-align: left;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        span:nth-child(1),
        span:nth-child(3) {
          width: 20%;
          color: #606266;
          font-weight: 550;
        }
        span:nth-child(2),
        span:nth-child(4) {
          width: 30%;
        }
      }
    }
  }
}
</style>
