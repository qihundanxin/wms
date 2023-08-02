<template>
  <div class="BoxPickTaskList">
    <FeaturesHeader     :headerTitle="headerTitle" />
   
    
      <div v-if="pickTasks.length==0" style="text-align:center ;">
       
        <ul   >
            <li v-for="(item,index) in waves"  @click="getPickTasks(item.waveId)"  >
              <el-card >  
                  <div>  
                        <span>波次号：</span>
                        <span>{{item.waveId}}</span>
                      </div>
                      <div> 
                        <span>订单结构：</span>
                              <span v-if="item.soStructure ==1"> 一单一品</span>
                              <span v-if="item.soStructure ==2"> 一单多品</span>
                              <span v-if="item.soStructure ==3"> 一单一品一件</span>
                              <span v-if="item.soStructure ==4"> 一单一品多件</span>
                    </div>
                    <div> 
                        <span>sku总数：</span>
                        <span>{{item.totalSkuCount}}</span> 
                  </div> 
              </el-card>
            </li>
        </ul>
          
          
       
        </div> 

       <div  v-if="pickTasks.length>0"  style="text-align:center ;" class="   ">
         <div   v-if="pickTask==null"   class="scanInput">
            <el-input  v-if="pickTask==null"   id="cellCode"  clearable  v-model="cellCode" placeholder="库位"></el-input>
            <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
        </div>

        <div  v-if="pickTask!=null  "  class="scanInput">
            <el-input  v-if="pickTask!=null  " id="itemCode" clearable   v-model="itemCode" placeholder="SKU"></el-input>
            <i  class="el-icon-camera" style="font-size:30px" @click="scan(2)" ></i>
        </div>

          
          
          </div>
          
          <div style="overflow: auto; height: 500px; margin-top:5px ">
                  
                <div @click="commitBoxCode(item)" v-for="(item,index) in pickTasks"   style="  width:94%;   margin-top:10px ;margin-left:3%">
                  <el-row>
                    <el-col :span="7">
                      <el-image
                        style="width: 80px; height: 80px"
                        :src="item.imgUrl"
                        ></el-image>
                        
                    </el-col>
                    <el-col :span="17"> 
                        <div  style="padding-left:5px;padding-top:0px;font-size:15px"   >
                            
                              
                                    <div  > 
                                    
                                          <span>SKU：</span> {{item.sjcUpperTaskId}}
                                          <span>{{item.itemCode}} </span> 
                                          
                                    </div>  
                                    <div > 
                                         <span>数量:</span>
                                          <span @click="quantityDialog(item.pickTaskId)" v-if="item.reviewQuantity<item.pickQuantity" style="color:#E6A23C; ">{{item.reviewQuantity}} </span>
                                          <span @click="quantityDialog(item.pickTaskId)" v-else style="color:#409EFF; ">{{item.reviewQuantity}}</span>
                                          <span @click="quantityDialog(item.pickTaskId)" style="color:#409EFF; ">/{{item.pickQuantity}}</span>
                                          <el-button @click="ex(item)" style="margin-left:20px" type="text">异常</el-button>
                                    </div>

                                    <div> 
                                          <span>单号：</span>
                                           <span>{{item.billNo}} {{item.billNo}}</span>
                                    </div>
                                    <div> 
                                          <span>店铺订单日序号：</span>
                                           <span>{{item.shopDailyCount}}</span> 
                                    </div>
                                    
                                   
                                    <div> 
                                          <span>货位：</span>
                                           <span>{{item.cellCode}} </span> 
                                    </div>
                                    
                                    <div> 
                                            
                                            <span v-if="pickTask!=null &&item.pickTaskId ==pickTask.pickTaskId" style="color:#67C23A; ">请拣货</span> 
                                              <span v-else-if="item.state==2" style="color:#67C23A; ">完成</span>  
                                            <span v-else-if="item.reviewQuantity>0.1&& item.state!=2" style="color:#E6A23C; ">拣货中</span>
                                            <span v-else-if="item.state==0" style="color:#909399; ">待拣货</span> 
                                    </div> 
                                    
                        </div>
                        
                    </el-col>
                  </el-row>
                  
                  <div v-if="pickTask!=null&&pickTask.pickTaskId==item.pickTaskId" style="color:#FFFFFF; line-height:20px;padding-left:20px;width:100%;height:20px;background-color: #409EFF;"> 
                                {{item.itemName}}            
                  </div>
                  <div v-else style="color:#FFFFFF;line-height:20px;padding-left:20px;width:100%;height:20px;background-color: #909399;"> 
                                {{item.itemName}}            
                  </div>

                
                              
                </div>
         </div> 
         <el-dialog   title="异常"   :visible.sync="exDialogVisible"    width="90%"    >
          <div  style="padding-left:5px;padding-top:0px;font-size:15px"   >
                            
                              
                      <div  >  
                            <span>SKU：</span>  
                            <span>{{exTask.itemCode}} </span>  
                      </div>  
                      <div> 
                            <span>需求数：</span>
                           
                            <span style="color:#409EFF; ">{{exTask.pickQuantity}}</span>
                           
                      </div> 
                       <div> 
                            <span>拣货数：</span>
                            <span   style="color:#E6A23C; ">{{exTask.reviewQuantity}} </span>
                          
                           
                             
                      </div> 
                      <div> 
                            <span>货位：</span>
                              <span>{{exTask.cellCode}} </span> 
                      </div> 
                      
                                    
            </div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="exDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="exCommit">确 定</el-button>
          </span>
        </el-dialog>
        <el-dialog   title="数量"   :visible.sync="quantityDialogVisible"    width="90%"    >
          <div  v-if="pickTask!=null" style="padding-left:5px;padding-top:0px;font-size:15px"   >
                            
                              
                       <div> 
                            <span>货位：</span>
                              <span>{{pickTask.cellCode}} </span> 
                      </div> 
                      <div  >  
                            <span>SKU：</span>  
                            <span>{{pickTask.itemCode}} </span>  
                      </div>  
                      <div> 
                            <span>需求数：</span>
                           
                            <span style="color:#409EFF; ">{{pickTask.pickQuantity}}</span>
                           
                      </div> 
                       <div> 
                            <!-- <span>拣货数：</span> -->
                             <el-input  size="mini" v-model="pickTask.reviewQuantity"  ></el-input>
                            <!-- <span   style="color:#E6A23C; ">{{pickTask.reviewQuantity}} </span> -->
                          
                           
                             
                      </div> 
                      
                      
                                    
            </div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="quantityDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="commit(pickTask)">确 定</el-button>
          </span>
        </el-dialog>

        <el-dialog title="容器号" :visible.sync="boxCodeVisible">
          <el-input id="boxCode" v-model="boxCode" autocomplete="off"></el-input> 
          <div slot="footer" class="dialog-footer">
            <el-button @click="boxCodeVisible = false">取 消</el-button>
            <el-button type="primary" @click="commit">确 定</el-button>
          </div>
        </el-dialog>
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BoxPickTaskList",
    data() {
      return {
        boxCodeVisible:false,
        quantityDialogVisible:false,
        waveMaster:{},
        exTask:{},
        exDialogVisible:false,
        pickTask:null,
        cellCodeOK:false,
        cellCode:'',
        itemCode:'',
        boxCode:'', 
       
        wareId:PF.getLocal('wareId', 'number'),
        waves:[],
        waveId:null,
        dialogFormVisible:false,
        pickTaskVisible:false,
        billBoxs:[],
       
        
        headerTitle: "边拣边分",
        formLabelWidth: '80px',
        pickTasks:[],
        timer1:null, 
        taskSearch:{
          wareId:PF.getLocal('wareId', 'number'),
          orgId:null,
          areaCode:'',
          shelfCode:'',
          itemCode:'',
          state:0,
          waveId:'',

        },
      
      };
    },
    watch: {
        
        cellCode:function(newVal, oldVal){
          if(newVal!=null && newVal.length>1){
          var flag = false;
            this.pickTasks.forEach(task => {
              if(task.cellCode.trim() ==newVal.trim() &&   task.state==0){ 
                   flag=true;  
              }
          });
          this.cellCodeOK=flag;
          if(flag){
             
            this.getTask();
              setTimeout(function () { 
          　　　　var domFocus = document.getElementById("itemCode"); 
                domFocus.focus();
          　　}, 100);
          }
          }
        },

        

        
   
		    itemCode: function(newVal, oldVal) { 
          var thisTask = this.pickTask;
          if(newVal!=null && newVal.length>1){
            this.pickTasks.forEach(task => {
              if(thisTask.pickTaskId==task.pickTaskId
              &&task.cellCode.trim() ==this.cellCode.trim()
               &&
               (
                 task.itemCode.trim() ==newVal.trim() || this.mateUpc(newVal.trim(),task.upcs) 
               )
                && task.state==0){
                  if(task.pickQuantity==task.reviewQuantity){
                    this.$message({
                        message: '该SKU数量已满！',
                        type: 'warning'
                      });
                  }else{
                      task.reviewQuantity +=1;
                      if(task.pickQuantity==task.reviewQuantity){
                       var billBox =  this.getBillBox(task.soMasterId);
                       if(billBox!=null){
                          this.boxCode = billBox.boxCode;
                          this.commit();
                       }else{
                         this.boxCodeVisible = true;
                          setTimeout(function () { 
                      　　　　var domFocus = document.getElementById("boxCode"); 
                            domFocus.focus();
                      　　}, 100);

                       }
                        
                         // this.commit(task);
                      }
                    
                      // this.$message({
                      //   message: '成功',
                      //   type: 'success'
                      // });
                  }
                  this.itemCode='';
                  return;
              }
          });

          }
          
			  } 
		},
    
    methods: {
      mateUpc(upc,upcs){
          if(upcs==null || upcs.length==0){
              return false;
          }

          for(var i =0;i<upcs.length;i++){
              if(upcs[i].upc==upc){
                  return true;
              }
          }
          return false;

        },
      scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.cellCode = result;
            } 
            else if(scanType==2){
              this.itemCode=result;
            }
            SCAN.close(); 
         });
      },
      commitBoxCode(task){
        var pickTask = this.pickTask;
        if(pickTask!=null&&pickTask.pickTaskId==task.pickTaskId&&task.pickQuantity==task.reviewQuantity){
            this.boxCodeVisible = true;
            setTimeout(function () { 
        　　　　var domFocus = document.getElementById("boxCode"); 
              domFocus.focus();
        　　}, 100);
        }
        
        
      },
      async getWaves2(){
         
        let that = this; 
         if(that.waves.length==0 || that.waveId==null){ 
           that.getWaves();
         } 
      },
     
      getWaves(){
        let that = this
        that.loading=true;
        IOT.getServerData('/wave/masters/allotWave','get', {isSeeding:0,state:11,wareId:this.wareId},(ret) => {
          that.loading=false;
          if (ret.code === 200) { 
            that.waves = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      quantityDialog(pickTaskId){
          if(this.pickTask!=null && this.pickTask.pickTaskId==pickTaskId){
              this.quantityDialogVisible =true;
          }
      },
        ex(task){
          this.exTask = task;
            this.exDialogVisible  =true;
        },
        exCommit(){
          var task = this.exTask;
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
         
          let param = {
            pickTaskId:task.pickTaskId, 
            wareId:this.wareId ,
            boxCode:this.boxCode,
            pickQuantity:task.reviewQuantity,
            exQuantity:task.pickQuantity-task.reviewQuantity,
            waveMasterId:task.waveMasterId

          }
          
          //String boxCode,Integer wareId,Integer pickTaskId,String toCellCode,Double pickQuantity,Double exQuantity
          IOT.getServerData('/pick/tasks/pickTaskEx','get',param,(ret) => {
            loading.close();
            if (ret.code === 200) {
              IOT.tips('成功！', 'success', 1000, () => {
              });
              this.exDialogVisible = false
              this.pickTasks.splice(0 , 1)
              this.pickTask =null;
              this.cellCode = null;
              this.itemCode=null;
              this.exPickTask={};
               
              this.getPickTasks();
            //     setTimeout(function () { 
            // 　　　　var domFocus = document.getElementById("cellCode"); 
            //       domFocus.focus();
            // 　　}, 100);

            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
        });

        },
        addBillBox(data){
          var that = this;
          for(var i=0;i<that.billBoxs.length;i++){
              var billBox = that.billBoxs[i];
              if(billBox.billNo = data.billNo){
                  return;
              }
          }
          that.billBoxs.push(data);
        },
        getBillBox(billNo){
             var that = this;
              for(var i=0;i<that.billBoxs.length;i++){
                  var billBox = that.billBoxs[i];
                  if(billBox.billNo == billNo){
                      return billBox;
                  }
              }
               return null;
        },
         commit( ){
           var task = this.pickTasks[0];
            if(task.pickQuantity!=task.reviewQuantity){
                  this.$message({
                    message: '数量不符合',
                    type: 'warning'
                  }); 
                  return;
           }
           const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          
        
          let param = {
            pickTaskId:task.pickTaskId, 
            wareId:this.wareId ,
            boxCode:this.boxCode,
            reviewQuantity:task.reviewQuantity
          }
        IOT.getServerData('/pick/tasks/pickTaskBjbfAffirm','post',param,(ret) => {
          
          if (ret.code === 200) {
            IOT.tips('成功！', 'success', 1000, () => {
            });
            this.boxCodeVisible = false;
            this.pickTasks.splice(0 , 1);
            if(this.pickTasks.length==0){
                this.pickTask =null;
                this.cellCode = null;
                this.itemCode=null;
                  this.getWaves();
                  loading.close(); 
            }else{
              this.addBillBox({billNo:task.soMasterId,boxCode:this.boxCode});
                this.pickTask =null;
                this.cellCode = null;
                this.itemCode=null;
                  setTimeout(function () { 
              　　　　var domFocus = document.getElementById("cellCode"); 
                    domFocus.focus();
              　　}, 100);
                  loading.close();
            }
             
          }else if(ret.code === 1234){
              this.pickTasks.splice(0 , 1)
             this.pickTask =null;
             this.cellCode = null;
             this.itemCode=null;
             setTimeout(function () { 
          　　　 var domFocus = document.getElementById("cellCode"); 
                domFocus.focus();
          　　}, 100);
             loading.close();
              IOT.tips(ret.message  , 'error');
          } 
          else if(ret.code === 12345){
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              this.pickTasks[0]=ret.data;
             this.pickTask =ret.data;
             loading.close();
          //    this.cellCode = null;
          //    this.itemCode=null;
          //    setTimeout(function () { 
          // 　　　 var domFocus = document.getElementById("cellCode"); 
          //       domFocus.focus();
          // 　　}, 100);
          //    loading.close();
          //     IOT.tips(ret.message  , 'error');
          } 
          else {
            loading.close();
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      clear(){
            this.boxCode="";
            　var domFocus = document.getElementById("boxCode"); 
            domFocus.focus();
      },
      clear2(){
            this.boxCode="";
            　var domFocus = document.getElementById("itemCode"); 
            domFocus.focus();
      },

      getTask(){
        var that = this;
        var tasks  =this.pickTasks;
        for (let i = 0; i < tasks.length; i++) { 
            if(tasks[i].cellCode.trim()==that.cellCode.trim()){ 

              this.pickTask = tasks[i];
                this.pickTasks.unshift(this.pickTasks.splice(i , 1)[0]); 
                // this.$notify({
                //   title: '成功',
                //   message: '请扫描SKU',
                //   type: 'success'
                // });
            }
        }

      },
     
      
     
      getPickTasks( waveId ){   
        let that = this;
        that.pickTasks = [];
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
        
        if(waveId==null || waveId==undefined || waveId==NaN || waveId==""){
            return;
        }
        that.boxCode='';
        that.billBoxs=[];
        that.taskSearch.waveId = waveId; 
        IOT.getServerData('/pick/tasks/findByWaveMasterId','get', that.taskSearch,(ret) => {
           loading.close();
          if (ret.code === 200) {  
            that.pickTasks = ret.rows;
            if(that.pickTasks.length>0){
              PF.setLocal("waveId",waveId);
              that.waveId = waveId;
            }else{
                for(var i =0;i<that.waves;i++){
                    if(that.waves[i].waveId ==waveId){
                        that.waves.splice(i,1);
                    }
                }
               PF.removeLocal("waveId");
               that.waveId = null;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }, 
    },
    created() {
  //     setTimeout(function () { 
  // 　　　　var domFocus = document.getElementById("cellCode"); 
  //        domFocus.focus();
  // 　　}, 100);
      var that =this;
      
      that.getWaves2();
      that.timer1 =setInterval(that.getWaves2,10000); 
     
       
      //that.getPickTasks();

       
    },
    destroyed(){
        
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>

.BoxPickTaskList{
  .el-card__body{
    padding: 10px; 
  }
  .el-button--text {
    color: #409EFF;
    background: 0 0;
    padding : 0 0 0 0 ; 
}
  

   .el-dialog__body {
      padding: 10px 10px;
      color: #606266;
      font-size: 14px;
      word-break: break-all;
  }
 
  .el-dialog__body {
      padding: 10px 10px;  
      color: #606266;
      font-size: 14px;
      word-break: break-all;
  }
}
  
</style>
