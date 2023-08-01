<template>
  <div v-loading="loading" class="ydyp">
    <!-- <audio id="myaudio" controls="controls" hidden src="../../../assets/mp3/wms称重成功.mp3" ref="audio"></audio> -->
    <h1>出库打印</h1>
    <div class=" ">
      <el-card style="padding">
        <el-form :inline="true">
          <el-form-item label="工号/姓名">
            <el-input v-model="userName" placeholder="工号/姓名"></el-input>
          </el-form-item> 
          <el-form-item label=" ">
            <el-button size="small" @click="getYdYp();" type="primary">确定</el-button> 
            <el-button size="small" @click="$parent.setPrinter" type="primary">打印设置</el-button> 
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div class="main">
      <div id="left" class="tableBox">
        <el-card>
          <el-table
         
              :data="waves"
              style="width: 100%">
            <el-table-column
                prop="bills"
                label="订单号"
                min-width="200">
              <template slot-scope="scope"> 
                  
                    <div  v-for="bill in scope.row.bills "
                         style="display: inline-block;margin-left:8px; height:70px">
                      <span>{{ bill.billNo }}</span> 
                      <span style="color:#409EFF" v-if="bill.state==4">拣货中</span>
                      <span style="color:#409EFF" v-if="bill.state==6">拣货完成</span>
                      <span v-if="bill.state==5" style="color:#E11111">部分拣货</span>
                      <span style="color:#E6A23C" v-if="bill.state==7">复核完成</span>
                      <span style="color:#67C23A" v-if="bill.state==8">发运完成</span>
                      <span style="color:#909399" v-if="bill.state==-1">取消</span>
                    </div>
                
                 
                
              </template>
            </el-table-column>
            <el-table-column
            min-width="200"
             label="波次"
            >
              <template slot-scope="scope">
                        <el-card class="box-card" style="margin-right: 25px;">
                        <div slot="header" class="clearfix">
                            <span>{{scope.row.waveId}}</span>

                            <div  style="float: right; padding: 3px 0" v-if="scope.row.state != null" >
                            状态：
                            <span   style="color:#F56C6C"   v-if="scope.row.state ==0">订单接收中</span>
                            <span   style="color:#8B3A62"  v-if="scope.row.state ==1">待拣货</span>
                            <span   style="color:#8B3A62"  v-if="scope.row.state ==11">拣货中</span>
                            <span  style="color:#543044"  v-if="scope.row.state ==2">拣货完成</span> 
                            <span   style="color:#E6A23C"  v-if="scope.row.state ==3">播种完成</span>
                            <span  style="color:#67C23A"  v-if="scope.row.state ==4">发运完成</span>
                            <span  style="color:#67C23A"  v-if="scope.row.state ==5">结束</span>
                             
                            </div>
                         
                        </div>
                      
                        
                        <div   v-if="scope.row.createTime != null" >
                         
                           生成时间：{{scope.row.createTime}} 
                        </div>
                     
                   
                       

                          <div >
                              <span   v-if="scope.row.totalSkuCount != null" >订单总数：{{scope.row.totalBillCount}} </span>
                            <span style="margin-left:15px"  v-if="scope.row.totalSkuCount != null"  >sku总数：{{scope.row.totalSkuCount}}  </span>
                            
                        </div>
                         
                      
                        <div v-if="scope.row.waveTacticName != null" >
                            波次策略：{{scope.row.waveTacticName}} 
                            <span v-if="scope.row.soStructure ==1">（一单一品）</span>
                            <span v-if="scope.row.soStructure ==2">（ 一单多品）</span>
                            <span v-if="scope.row.soStructure ==3">（ 一单一品一件）</span>
                            <span v-if="scope.row.soStructure ==4">（一单一品多件）</span>
                        </div>
                        <div v-if="scope.row.isSeeding != null" >
                            分拣方式：
                            <span v-if="scope.row.isSeeding ==1">先拣后分</span>
                            <span v-if="scope.row.isSeeding ==0">边拣边分</span>
                           
                        </div>
                         <div v-if="scope.row.carrierName != null" >
                            承运方：{{scope.row.carrierName}}
                        </div>
                         <div v-if="scope.row.customerName != null" >
                            客户：{{scope.row.customerName}}
                        </div> 
                        </el-card>
                    </template>
                </el-table-column> 
         
            <el-table-column
                prop="quantity"
                min-width="180"
                label="操作">
              <template slot-scope="scope">
                
                <el-button size="mini" @click="print(scope.row,2)" type="danger">打印拣货标签</el-button>
                 <br/>
                 <br/>
                  <el-button size="mini" @click="print(scope.row,1)" type="primary">打印出库单/发票/拣货单</el-button>
              
             
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <div id="right" class="tableBox">
        <el-card>
            <div slot="header" class="clearfix">
            <span>拣货标签</span>
            <el-button style="float: right; padding: 3px 0" type="text"> </el-button>
          </div>
          <div v-for="pick in picks" :key="pick" class="text item">
              {{pick.index}}
          </div>
        </el-card>
        <br/>
        <br/>
        <el-card>
            <div slot="header" class="clearfix">
            <span>出库单/发票</span>
            <el-button style="float: right; padding: 3px 0" type="text"> </el-button>
          </div>
          <div v-for="bill in bills" :key="bill" class="text item">
              {{bill.index}}
          </div>
        </el-card>
      </div>
    </div>
  
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'Check',
  data() {
    let validateQuantityNo0 = (rule, value, callback) => {
      if (value > 0) {
        callback();
      } else {
        callback(new Error('数量不得为0!'));
      }
    };
    return {
      states:[
                    {
                        value:0,
                        label:"接收订单中",
                        color:"#F56C6C"
                    },
                    {
                        value:1,
                        label:"待拣货",
                        color:"#8B3A62"
                    },
                    {
                        value:11,
                        label:"拣货中",
                        color:"#8B3A62"
                    },
                    {
                        value:2,
                        label:"拣货完成",
                        color:"#543044"
                    },
                    {
                        value:3,
                        label:"播种完成",
                        color:"#E6A23C"
                    },
                    {
                        value:4,
                        label:"发运完成",
                        color:"#67C23A"
                    }
                ],
      userName:'',
      
      wave: {},
      bills: [],
      picks:[],
      waves:[],
      autoPrint: true,
       
      wareId: PF.getLocal('wareId', 'number'),
      loading:false,
      // loading: {
      //   boxTable: false,
      //   pickTaskTable: false,
      // },
      
    }
  },
  computed: {
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
    isCheckBox() {
      return PF.isNull(this.form.boxNo)
    },
  },
  methods: {
    print(wave,type){
       
      var that = this; 
      var url="";
        if(type==1){
            url = '/so/masters/getBillPrintData';
        }else if(type==2){
             url = '/so/masters/getPickPrintData';
        }
        that.loading = true;
        that.bills=[];
        that.picks=[];  
         IOT.getServerData(url, 'get', {waveId:wave.waveId,wareId:that.wareId}, (ret) => { 
           that.loading = false;
          if (ret.code === 200) { 
                if(type==1){
                    that.bills = ret.data;
                    that.bills.forEach(bill => { 
                      that.$parent.printHtml(bill.html,null);  
                      that.$parent.printUrl(bill.pdfUrl); 
                      that.$parent.printHtml(bill.soPickTask,null);  
                       
                    });
                }else if(type==2){
                   that.picks = ret.data;
                    that.picks.forEach(pick => {
                      that.$parent.printHtml(pick.html,null); 
                    });
                }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
    },
    demoPrint3 (type){
        
      },
    cancelSo(soMasterId) {
      //Integer waveMasterId, Integer soMasterId, Integer wareId
      var param = {
        wareId: this.wareId,
        waveMasterId: this.wave.waveId,
        soMasterId: soMasterId
      };
      IOT.showOverlay('保存中，请稍等...');
      IOT.getServerData('/pick/task/returns/cancelSo', 'get', param, (ret) => {
        IOT.hideOverlay();
        this.loading = false;
        this.soDetailsVisible = false
        if (ret.code === 200) {
          for (var i = 0; i < this.bills.length; i++) {
            if (this.bills[i].soMasterId == soMasterId) {
              this.bills[i].state = -1;
            }
          }
        } else {
          this.loading = false
          IOT.tips(ret.message || 'server error', 'error')
        }
      })

    },
    
    getYdYp() {
      var that = this;
       that.loading = true;
       that.waves=[];
       that.bills=[];
       that.picks=[];
      IOT.getServerData('/wave/masters/findByUser', 'get', {
        userName: that.userName,
        wareId: that.wareId
      }, (ret) => {
        if (ret.code === 200) {
          
          that.waves = ret.data;
           that.loading = false;
        } else {
          that.loading = false;
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);

    },
    
    
    
    printDzmd(billNo) {
      IOT.showOverlay('处理中，请稍等...');
      var that = this;
      IOT.getServerData('/so/masters/getDzmd', 'get', {billNo: billNo}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          var strHTML = ret.data;
          that.$parent.printHtml(strHTML, null,);

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    
    
    
  },
  created() {
     
  }
}
</script>

<style scoped lang="less">

.el-card__body {
  padding: 0;
}

.ydyp {
  padding-bottom: 100px;

  .header {
    height: auto;
  }

  .main {
    display: flex;
    justify-content: space-around;
    margin-top: 10px;
  }

  #left, #right {
    width: 50%;
    height: 600px;
    overflow: auto;
  }
}
</style>
