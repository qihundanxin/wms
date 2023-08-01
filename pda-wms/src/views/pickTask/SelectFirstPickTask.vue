<template>
  <div class="selectFirstPickTask">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="formBox">
      <div class="formRow">
        <span class="lable ">库位</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.cellCode" disabled="true" />
      </div>
      <div class="formRow">
        <span class="lable ">产品</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.itemCode" disabled="true"/>
      </div>
      <div class="formRow">
        <span class="lable ">品名</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.itemName" disabled="true"/>
      </div>
      <div class="formRow">
        <span class="lable">单位</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.packDescribe" disabled="true"/>
      </div>
      <div class="formRow">
        <span class="lable">应拣数量</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.pickQuantity" disabled="true"/>
      </div>
      <div class="formRow">
        <span class="lable">已拣数量</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.reviewQuantity" disabled="true"/>
      </div>
      <div class="formRow">
        <span class="lable">实拣数量</span>
        <input class="noBdIpt" type="text" v-model="quantity"/>
      </div>
      <div class="formRow">
        <span class="lable">目标货位</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.toCellCode" v-if="firstPickTask.toCellCode != null || firstPickTask.toCellCode != undefined" :disabled="firstPickTask.toCellCode != null || firstPickTask.toCellCode != undefined"/>
        <el-select v-model="firstPickTask.toCellCode" filterable placeholder="请选择目标货位" size="small" v-if="firstPickTask.toCellCode === null || firstPickTask.toCellCode === undefined"
                   style="width: 70%;">
          <el-option
                  v-for="item in cells"
                  :key="item.cellCode"
                  :label="item.cellCode"
                  :value="item.cellCode">
          </el-option>
        </el-select>
      </div>
      <div class="formRow">
        <span class="lable">追踪号</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.trackingNum" :disabled="trackingNum" @change="updateTrackingNum()"/>
      </div>
      <div style="text-align:center">
        <el-button @click="commit()" type="primary" size="mini" round  style="width: 200px;margin-top: 5px;">确定</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "selectFirstPickTask",
  data() {
    return {

      headerTitle: "拣货任务查询",
      searchData: {
        pageNum: 1,
        pageSize: 1,
        total: null,
        keyWords: '',
        insertType: 0,
        wareId: PF.getLocal('wareId', 'number'),
        states:[0,1],
      },
      firstPickTask:{},
      quantity:'',
      cells:[],

      trackingNum:false,
    };
  },
  methods: {
    updateTrackingNum(){
      this.trackingNum = true;
    },
    commit(){
      if(this.quantity === null || this.quantity === '' || this.quantity < 0){
        IOT.tips('请输入数量！', 'error');
        return false;
      }
      if(this.firstPickTask.toCellCode === null || this.firstPickTask.toCellCode === '' || this.firstPickTask.toCellCode === undefined){
        IOT.tips('请输入目标货位！', 'error');
        return false;
      }
      if(this.firstPickTask.trackingNum === null || this.firstPickTask.trackingNum === '' || this.firstPickTask.trackingNum === undefined){
        IOT.tips('请输入追踪号！', 'error');
        return false;
      }
      IOT.getServerData('/pick/tasks/updatePick','post',{pickTaskId:this.firstPickTask.pickTaskId,toCellCode:this.firstPickTask.toCellCode,reviewQuantity:this.quantity,trackingNum:this.firstPickTask.trackingNum},(ret) => {
        if (ret.code === 200) {
          IOT.tips('成功！', 'success', 1000, () => {
          });
          this.quantity = '';
          this.getFirstPickTask();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getFirstPickTask(){
      /*this.searchData.states.push(0);
      this.searchData.states.push(1);*/
      IOT.getServerData('/pick/tasks/findFirst','post',this.searchData,(ret) => {
        if (ret.code === 200) {
          let list = ret.rows[0];
          this.firstPickTask = ret.rows[0];
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getCellData(){
      IOT.getServerData('/cell/infos/list','get',{cellUseType:5},(ret) => {
        if (ret.code === 200) {
          this.cells = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
  },
  created() {
    this.getFirstPickTask();
    this.getCellData();
    // this.getDate();
  },
  components: {
    FeaturesHeader
  }
};
</script>
<style lang="less" scoped>
  .selectFirstPickTask {
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
