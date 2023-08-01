<template>
  <div class="GetNewTask">
    <FeaturesHeader  v-if="waveId==null"  :headerTitle="headerTitle" />
    <FeaturesHeader  v-if="waveId!=null"  headerTitle="边拣边分" />
     <div style=" ;">
       <div class="   ">
          <span style="color:#E6A23C;" v-if="waves.length == 0">暂无待拣波次</span>
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
           <div  v-if="pickTasks.length>0"  style="text-align:center ;" class="   "> 
              <el-input  v-if="pickTask==null"   id="cellCode"  clearable  v-model="cellCode" placeholder="库位"></el-input> 
              <el-input  v-if="pickTask!=null" id="itemCode" clearable   v-model="itemCode" placeholder="SKU"></el-input> 
          </div>
          
          <div style="overflow: auto; height: 500px; margin-top:5px ">
                  
                <div v-for="(item,index) in pickTasks"   style="  width:94%;   margin-top:10px ;margin-left:3%">
                  <el-row>
                    <el-col :span="9">
                      <el-image
                        style="width: 80px; height: 80px"
                        :src="item.imgUrl"
                        ></el-image>
                        
                    </el-col>
                    <el-col :span="15"> 
                        <div  style="padding-left:5px;padding-top:0px;font-size:15px"   >
                            
                              
                                    <div  > 
                                    
                                          <span>SKU：</span> {{item.sjcUpperTaskId}}
                                          <span>{{item.itemCode}} </span> 
                                          
                                    </div>  
                                    <div > 
                                         <span>数量：</span>
                                          <span @click="quantityDialog(item.pickTaskId)" v-if="item.reviewQuantity<item.pickQuantity" style="color:#E6A23C; ">{{item.reviewQuantity}} </span>
                                          <span @click="quantityDialog(item.pickTaskId)" v-else style="color:#409EFF; ">{{item.reviewQuantity}}</span>
                                          <span @click="quantityDialog(item.pickTaskId)" style="color:#409EFF; ">/{{item.pickQuantity}}</span>
                                          <el-button @click="ex(item)" style="margin-left:20px" type="text">异常</el-button>
                                    </div>
                                    <div> 
                                          <span>单号：</span>
                                           <span>{{item.billNo}} {{item.shopDailyCount}}</span> 
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
       
    </div>
    
      
       
    </div>
   
    
       
    </div>


 
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "GetNewTask",
    data() {
      return {
        boxCode:'',
        waveId:PF.getLocal('waveId', 'number'),
        wareId:PF.getLocal('wareId', 'number'),
        waves:[],
        dialogFormVisible:false,
        pickTaskVisible:false,
        pickTask:{},
        taskSearch:{
          wareId:PF.getLocal('wareId', 'number'),
          orgId:null,
          areaCode:'',
          shelfCode:'',
          itemCode:'',
          state:0,
          waveId:'',

        },
        headerTitle: "获取任务",
        formLabelWidth: '80px', 
        timer1:null,

        quantityDialogVisible:false,
        waveMaster:{},
        exTask:{},
        exDialogVisible:false,
        pickTask:null,
        cellCodeOK:false,
        cellCode:'',
        itemCode:'',
       
        wareId:PF.getLocal('wareId', 'number'),
        waves:[],
        dialogFormVisible:false,
        pickTaskVisible:false,
       
        
       
        pickTasks:[],
        timer1:null,
        boxCode:"",
      
      };
    },
    
    methods: {
      async getWaves2(){
        let that = this; 
         if(that.waves.length==0 || that.waveId==null){ 
           that.getWaves();
         } 
      },
      async getPickTasks2(){
        let that = this;
          for(var i =0;i<that.pickTasks.length;i){ 
          } 
         if(that.pickTasks.length==0){ 
           that.getPickTasks();
         } 
      },
      getWaves(){
        let that = this
        IOT.getServerData('/wave/masters/allotWave','get', {isSeeding:0,state:11,wareId:this.wareId},(ret) => {
          if (ret.code === 200) { 
            that.waves = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPickTasks(waveId){ 
        let that = this;
        if(waveId==null || waveId==undefined || waveId==NaN || waveId==""){
            return;
        }
        that.taskSearch.waveId = waveId; 
        IOT.getServerData('/pick/tasks/allotTask','get', that.taskSearch,(ret) => {
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
      var that =this;
      that.getWaves2();
      that.timer1 =setInterval(that.getWaves2,10000); 
    //  that.getPickTasks(that.waveId);

       
    },
    destroyed(){
       clearInterval(this.timer1); 
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseAccept {
  }
</style>
