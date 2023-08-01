<template>
  <div class="selectFirstPickTask">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="  noScrollbar">
      <!--<div class="formRow">
        <span class="lable ">货区</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.areaCode" disabled="true" />
      </div>
      <div class="formRow">
        <span class="lable ">货架</span>
        <input class="noBdIpt" type="text" v-model="firstPickTask.shelfCode" disabled="true" />
      </div>-->
       <el-card class="clearFloat" style="text-align: center;">
      <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                 
                
                
              </div>
              <div class="baseData clearFloat">
                <el-row>
                 
                  <el-col :span="12"> 
                    <span>sku：</span>
                    <span>{{firstPickTask.itemCode}}</span>
                  </el-col>
                  <el-col :span="12"> 
                    <span>产品名称：</span>
                    <span>{{firstPickTask.itemName}}</span>
                  </el-col>
                </el-row>
                <!-- <el-row>
                  <el-col :span="12"> 
                      <span>批次：</span>
                    <span>{{firstPickTask.batchName}}</span>
                  </el-col>
                  <el-col :span="12"> 
                    <span>单位：</span>
                    <span>{{firstPickTask.packDescribe}}</span>
                  </el-col>
                </el-row> -->
                <el-row>
                   <el-col :span="12"> 
                      <span class="name">货位：</span>
                      <span class="value">{{firstPickTask.cellCode}}</span>
                  </el-col>
                  <el-col :span="12"> 
                    <span>数量：</span>
                    <span>{{firstPickTask.pickQuantity}}</span>
                  </el-col>
                </el-row>
                  
                
              </div>
              
      </div>
       <el-form :model="firstPickTask"   label-width="100px"  >
          <el-form-item label="sku "     >
          <el-input    v-model="itemCode"  @change="checkItemCode()"></el-input>
        </el-form-item>
        <el-form-item label="货位 "   >
          <el-input   v-model="cellCode" @change="checkCellCode()"></el-input>
        </el-form-item>
         <!-- <el-form-item label="批次（校验）" v-if="pickCheck.batch==1" >
          <el-input size="mini" v-model.number="batchName" @change="checkCellCode()"></el-input>
        </el-form-item>
        -->
        <!-- <el-form-item label="拣货数"  >
          <el-input size="mini" v-model.number="quantity" @change="checkCellCode()"></el-input>{{firstPickTask.packDescribe}}
        </el-form-item> -->
        <el-form-item label="篮子"  >
          <el-autocomplete popper-class="my-autocomplete"
                            v-model="firstPickTask.toCellCode"
                            :fetch-suggestions="querySearchMethod"
                            placeholder="篮子"
                          
                            >
              <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
              </template>
          </el-autocomplete>
        </el-form-item>
        <!-- <el-form-item label="目标跟踪号"  >
          <el-input size="mini"   v-model="firstPickTask.trackingNum"  ></el-input>
        </el-form-item> -->
       
        <!-- <el-form-item>
          
          <el-button @click="$router.push({ name: 'GetNewTask'});">返回</el-button>
          <el-button type="primary" @click="commit()">提交</el-button>
        </el-form-item> -->
      </el-form>
      <br/>
      <el-button  @click="commit()" style="width:200px;height:50px"  type="primary" >提交</el-button>
       
       <br/>
       <br/>
       <el-button  @click="$router.push({ name: 'GetNewTask'});"  style="width:200px;height:50px" type="info" >        返回  </el-button>
       </el-card>
     
     
      
      
       
      
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "selectFirstPickTask",
  data() {
    return {
      wareId: PF.getLocal('wareId', 'number'),
      headerTitle: "拣货",
      searchData: {
        pageNum: 1,
        pageSize: 1,
        total: null,
        keyWords: '',
        insertType: 0,
        wareId: PF.getLocal('wareId', 'number'),
        states:[0,1],
      },
      firstPickTask:this.$route.params,
      quantity:'',
      itemCode:'',
      cellCode:'',
      batchName:'',
      cells:[],
      pickCheck:{
        item:null,
        cell:null,
        batch:null,
      },

      trackingNum:false,
    };
  },
  methods: {
    querySearchMethod(queryString, cb) {
                let that = this;
                var solList = []
                that.cells.forEach(item => {
                    solList.push({ 'label': item.cellCode, 'value': item.cellCode })
                })
 
                var results = queryString
                    ? solList.filter(this.createFilterMethod(queryString))
                    : solList;
                // 调用 callback 返回建议列表的数据
                cb(results);
            },
            createFilterMethod(queryString) {
                let that = this;
                var solList = that.cells;
                return solList => {
                    return (
                        //solList.label.toLowerCase().indexOf(queryString.toLowerCase()) === 0
                        solList.label
                    );
                };
            },
            handleSelectMethod(item) {
                console.log(item);
            },
    updateTrackingNum(){
      this.trackingNum = true;
    },
    checkCellCode() {
      if (this.cellCode.trim() !== this.firstPickTask.cellCode.trim()) {
        this.cellCode = "";
      IOT.tips('请选择正确的货位！', 'error');
      return false;
      }else{
        IOT.tips('货位验证成功！', 'success');
      }
    },
    checkItemCode(){
      if(this.itemCode.trim()  !== this.firstPickTask.itemCode.trim() ){
         this.itemCode = "";
        IOT.tips('请选择正确的物料！', 'error');
        return false;
      }else{
        IOT.tips('物料验证成功！', 'success');
      }
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
      if(this.itemCode !== this.firstPickTask.itemCode  ){
        IOT.tips('请校验SKU', 'error');
        return false;
      }
      if(this.cellCode !== this.firstPickTask.cellCode  ){
        IOT.tips('请校验拣货货位', 'error');
        return false;
      }
      // if(this.firstPickTask.trackingNum === null || this.firstPickTask.trackingNum === '' || this.firstPickTask.trackingNum === undefined){
      //   IOT.tips('请输入追踪号！', 'error');
      //   return false;
      // }

      if((parseFloat(this.quantity) + parseFloat(this.firstPickTask.reviewQuantity)) > this.firstPickTask.pickQuantity){
        IOT.tips('数量超过拣货数量！', 'error');
        return false;
      }
      // let pickForm = {
      //   pickTaskId:this.firstPickTask.pickTaskId,
      //   toCellCode:this.firstPickTask.toCellCode,
      //   pickQuantity:this.quantity,
      //   toTrackingNum:this.firstPickTask.trackingNum 
      //   }
         let param = {
          pickTaskId:this.firstPickTask.pickTaskId,
          toCellCode:this.firstPickTask.toCellCode,
          trackingNum:this.firstPickTask.trackingNum,
          wareId:this.wareId
          
        }
      IOT.getServerData('/pick/tasks/pickTaskAffirm','post',param,(ret) => {
        if (ret.code === 200) {
          IOT.tips('成功！', 'success', 1000, () => {
          });
          if(this.firstPickTask.pickQuantity === parseFloat(this.firstPickTask.reviewQuantity)+parseFloat(this.quantity)){
              this.backOut();
          }else{
              this.getFirstPickTask();
          }
            this.quantity = '';
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
      backOut() {
          this.$router.push({ name: 'GetNewTask'});
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
      IOT.getServerData('/cell/infos/list','get',{cellUseType:3,wareId:localStorage.getItem("wareId")},(ret) => {
        if (ret.code === 200) {
          this.cells = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
  },
  created() {
    // this.getFirstPickTask();
    this.getCellData();
    this.quantity = this.firstPickTask.pickQuantity;
    // this.getDate();
     this.pickCheck.item = PF.getSysParam("pick-check-item");
     this.pickCheck.cell = PF.getSysParam("pick-check-cell");
     this.pickCheck.batch = PF.getSysParam("pick-check-batch");
     if(this.firstPickTask.itemCode ==undefined){
         this.$router.push({ name: 'GetNewTask'});
     }
      
     
  },
  components: {
    FeaturesHeader
  }
};
</script>
<style lang="less" scoped>
  .selectFirstPickTask {
    font-size: 13px;
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

    .el-col{
        height: 30px;
        
      }

      .el-form-item {
    margin-bottom: 5px;
}
     
  }
</style>
