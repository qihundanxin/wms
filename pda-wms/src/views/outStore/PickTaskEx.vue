<template>
  <div class="BoxPickTaskList">
    <FeaturesHeader     :headerTitle="headerTitle" />
   
    
      <!-- <div v-if="pickTasks.length==0" style="text-align:center ;">
       
          <el-form   style="margin-top:20px"  class="demo-form-inline">
            <el-form-item label=" ">
              <el-input id="boxCode" v-model="boxCode" placeholder="容器号"></el-input>
            </el-form-item>
            <el-form-item style="display: none;" label=" ">
              <el-input v-model="boxCode" placeholder="容器号"></el-input>
            </el-form-item>
            
            <el-form-item style="margin-top:20px" >
              <el-button style="width:200px;height:50px" type="primary" @click="getPickTasks">查询</el-button>
            </el-form-item>
            <el-form-item style="margin-top:20px" >
              <el-button style="width:200px;height:50px" type="info" @click="clear">清空</el-button>
            </el-form-item>
          </el-form> 
          <div style="text-align: center;margin-top:30px">
            <span  v-if="waveMaster.soStructure==2"  style="font-size:20px;color:#67C23A;font-weight:bold;">多品</span>
            <span     v-if="waveMaster.soStructure==1"   style="font-size:20px;color:#409EFF;font-weight:bold;">单品</span>
            <span     v-if="waveMaster.soStructure==3"   style="font-size:20px;color:#409EFF;font-weight:bold;">单品一件</span>
            <span     v-if="waveMaster.soStructure==4"   style="font-size:20px;color:#409EFF;font-weight:bold;">单品多件</span>
          </div>
       
        </div>  -->

       <div  v-if="pickTasks.length>0"  style="text-align:center ;" class="   ">
            
              <el-input      id="cellCode"  clearable  v-model="cellCode" placeholder="库位"></el-input>
           
              <!-- <el-input  v-if="pickTask!=null" id="itemCode" clearable   v-model="itemCode" placeholder="SKU"></el-input> -->
          
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
                                    <div> 
                                         <!-- <span>数量:</span>
                                          <span v-if="item.reviewQuantity<item.pickQuantity" style="color:#E6A23C; ">{{item.reviewQuantity}} </span>
                                          <span v-else style="color:#409EFF; ">{{item.reviewQuantity}}</span>
                                          <span style="color:#409EFF; ">/{{item.pickQuantity}}</span> -->
                                          <!-- <el-button @click="ex(item)" style="margin-left:20px" type="text">异常</el-button> -->
                                    </div>
                                    
                                   
                                    <div> 
                                          <span>货位：</span>
                                           <span>{{item.cellCode}} </span> 
                                    </div>
                                    <div> 
                                         <!-- <span>数量:</span>
                                          <span v-if="item.reviewQuantity<item.pickQuantity" style="color:#E6A23C; ">{{item.reviewQuantity}} </span>
                                          <span v-else style="color:#409EFF; ">{{item.reviewQuantity}}</span>
                                          <span style="color:#409EFF; ">/{{item.pickQuantity}}</span> -->
                                          <el-button  v-if="pickTask!=null&&pickTask.pickTaskId==item.pickTaskId"  @click="ex(item)" style="margin-left:2px;margin-top:10px"  type="text" >处理</el-button>
                                    </div>
                                    
                                    <!-- <div> 
                                            
                                            <span v-if="pickTask!=null &&item.pickTaskId ==pickTask.pickTaskId" style="color:#67C23A; ">请拣货</span> 
                                              <span v-else-if="item.state==2" style="color:#67C23A; ">完成</span>  
                                            <span v-else-if="item.reviewQuantity>0.1&& item.state!=2" style="color:#E6A23C; ">拣货中</span>
                                            <span v-else-if="item.state==0" style="color:#909399; ">待拣货</span> 
                                    </div>  -->
                                    
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
         <el-dialog   title="处理"   :visible.sync="exDialogVisible"    width="90%"    >
          <div  style="padding-left:5px;padding-top:0px;font-size:15px"   >
                            
                              
                      <div  >  
                            <span>SKU：</span>  
                            <span>{{exTask.itemCode}} </span>  
                      </div>  
                      <!-- <div> 
                            <span>需求数：</span>
                           
                            <span style="color:#409EFF; ">{{exTask.pickQuantity}}</span>
                           
                      </div> 
                       <div> 
                            <span>拣货数：</span>
                            <span   style="color:#E6A23C; ">{{exTask.reviewQuantity}} </span>
                          
                           
                             
                      </div>  -->
                      <div> 
                            <span>货位：</span>
                              <span>{{exTask.cellCode}} </span> 
                      </div> 
                      <div> 
                            <!-- <span>数量：</span> -->
                    
                            <el-input-number id="quantity" v-model="quantity"  :min="0"   label="请输入数量"></el-input-number>
 
                            <!-- <el-input id="quantity" v-model="quantity" placeholder="请输入数量"></el-input> -->
                              <!-- <span>{{quantity}} </span>  -->
                      </div> 
                      
                                    
            </div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="exDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="exCommit">确 定</el-button>
          </span>
        </el-dialog>
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BoxPickTaskList",
    data() {
      return {
        quantity:'',
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
       
        
        headerTitle: "拣货异常",
        formLabelWidth: '80px',
        pickTasks:[],
        timer1:null,
        boxCode:"",
      
      };
    },
    watch: {
        
        cellCode:function(newVal, oldVal){
          var flag = false;
            this.pickTasks.forEach(task => {
              if(task.cellCode.trim() ==newVal.trim() &&   task.state==0){ 
                   flag=true;  
              }
          });
          this.cellCodeOK=flag;
          if(flag){
             
            this.getTask();
          //     setTimeout(function () { 
          // 　　　　var domFocus = document.getElementById("itemCode"); 
          //       domFocus.focus();
          // 　　}, 100);
          }
        },

        
   
		    itemCode: function(newVal, oldVal) { 
          this.pickTasks.forEach(task => {
              if(task.cellCode.trim() ==this.cellCode.trim() && task.itemCode.trim() ==newVal.trim() && task.state==0){
                  if(task.pickQuantity==task.reviewQuantity){
                    this.$message({
                        message: '该SKU数量已满！',
                        type: 'warning'
                      });
                  }else{
                      task.reviewQuantity +=1;
                      if(task.pickQuantity==task.reviewQuantity){
                          this.commit(task);
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
		},
    methods: {
        ex(task){
          this.exTask = task;
            this.exDialogVisible  =true;
                setTimeout(function () { 
          　　　　var domFocus = document.getElementById("quantity"); 
                domFocus.focus();
          　　}, 100);
        },
        exCommit( ){
          var task = this.pickTask;
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          let param = {
          id:task.pickTaskExId,
          
          commitQuantity:this.quantity
        } 
          IOT.getServerData('/pick/task/exs/commit','get',param,(ret) => {
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
                setTimeout(function () { 
            　　　　var domFocus = document.getElementById("cellCode"); 
                  domFocus.focus();
            　　}, 100);

            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
        });

        },
         commit(task){
           const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
        
          let param = {
            pickTaskId:task.pickTaskId, 
            wareId:this.wareId 
          }
        IOT.getServerData('/pick/tasks/pickTaskAffirm','post',param,(ret) => {
          if (ret.code === 200) {
            IOT.tips('成功！', 'success', 1000, () => {
            });
            this.pickTasks.splice(0 , 1)
             this.pickTask =null;
             this.cellCode = null;
             this.itemCode=null;
              setTimeout(function () { 
          　　　　var domFocus = document.getElementById("cellCode"); 
                domFocus.focus();
          　　}, 100);
              loading.close();
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
          else {
            loading.close();
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
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
     
      
     
      getPickTasks(  ){  
        let that = this;
        // var boxCode = that.boxCode;
        // if(boxCode==null || boxCode==undefined || boxCode==NaN || boxCode==""){
        //     return;
        // }
         
        var wareId = that.wareId; 
        IOT.getServerData('/pick/task/exs/list','get', {
          state:0,
          wareId:wareId
        },(ret) => {
          if (ret.code === 200) {  
            that.pickTasks = ret.rows;
         //   that.waveMaster = ret.data.waveMaster;
         if(that.pickTasks.length>0){
           setTimeout(function () { 
          　　　　var domFocus = document.getElementById("cellCode"); 
                domFocus.focus();
          　　}, 100);
         }
              
            if(that.pickTasks.length==0){
              IOT.tips('未查询到拣货异常', 'error');
          //     setTimeout(function () { 
          // 　　　　var domFocus = document.getElementById("boxCode"); 
          //       domFocus.focus();
          // 　　}, 100);
            }
             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }, 
    },
    created() {
      this.getPickTasks();
//       setTimeout(function () {
// 　　　　 
//   　　　　var domFocus = document.getElementById("boxCode"); 
//          domFocus.focus();
//   　　}, 100);
//       var that =this;
     
       
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
