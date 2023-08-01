<template>
  <div class="BoxPickTaskList">
    <FeaturesHeader     :headerTitle="headerTitle" />
   
    
      <div v-if="pickTasks.length==0" style="text-align:center ;">
       
          
             
              <el-input id="boxCode" v-model="boxCode" placeholder="容器号"></el-input>
            
            
              <el-input v-if="false" v-model="boxCode" placeholder="容器号"></el-input>
             
            
              <br/>
              <br/>
            
              <el-button style="width:200px;height:50px" type="primary" @click="getPickTasks">查询</el-button>
            <br/>
            <br/>
              <el-button style="width:200px;height:50px" type="info" @click="clear">清空</el-button>
           
           
        
       
        </div> 
       <div  v-if="pickTasks.length>0"  style="text-align:center ;" class="   ">
            <el-input  v-if="exTask==null" id="itemCode" clearable   v-model="itemCode" placeholder="请扫描SKU"></el-input>
              <el-input  v-if="exTask!==null"   id="cellCode"  clearable  v-model="cellCode" placeholder="请扫描库位"></el-input>
           
              
          
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
                                    
                                          <span>SKU：</span>  
                                          <span>{{item.itemCode}} </span> 
                                          
                                    </div>  
                                    <div> 
                                         <span>数量：</span>
                                         <span>{{item.quantity}} </span> 
                                          <!-- <span v-if="item.reviewQuantity<item.pickQuantity" style="color:#E6A23C; ">{{item.reviewQuantity}} </span>
                                          <span v-else style="color:#409EFF; ">{{item.reviewQuantity}}</span>
                                          <span style="color:#409EFF; ">/{{item.pickQuantity}}</span>
                                          <el-button @click="ex(item)" style="margin-left:20px" type="text">异常</el-button> -->
                                    </div>

                                    <div> 
                                          <span>容器号：</span>
                                           <span>{{item.boxCode}} </span> 
                                    </div>
                                    
                                   
                                    <div> 
                                          <span>货位：</span>
                                           <span>{{item.toCellCode}} </span> 
                                    </div>
                                    
                                    <div> 
                                            
                                            <span v-if="exTask!=null &&item.pickTaskReturnId ==exTask.pickTaskReturnId" style="color:#67C23A; ">请上架</span> 
                                              <span v-else-if="item.state==1" style="color:#67C23A; ">完成</span>   
                                            <span v-else-if="item.state==0" style="color:#909399; ">待上架</span> 
                                    </div> 
                                    
                        </div>
                        
                    </el-col>
                  </el-row>
                  
                  <div v-if="exTask!=null&&exTask.pickTaskReturnId==item.pickTaskReturnId" style="color:#FFFFFF; line-height:20px;padding-left:20px;width:100%;height:20px;background-color: #409EFF;"> 
                                {{item.itemName}}            
                  </div>
                  <div v-else style="color:#FFFFFF;line-height:20px;padding-left:20px;width:100%;height:20px;background-color: #909399;"> 
                                {{item.itemName}}            
                  </div>

                
                              
                </div>
         </div> 
        
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BoxPickTaskList",
    data() {
      return {
         
        exDialogVisible:false,
        exTask:null,
        cellCodeOK:false,
        cellCode:'',
        itemCode:'',
       
        wareId:PF.getLocal('wareId', 'number'),
        waves:[],
        dialogFormVisible:false,
        pickTaskVisible:false,
       
        
        headerTitle: "任务列表",
        formLabelWidth: '80px',
        pickTasks:[],
        timer1:null,
        boxCode:"",
      
      };
    },
    watch: {
        itemCode: function(newVal, oldVal) { 
          if(newVal!=null && newVal.length>2){
               var flag = false;
            this.pickTasks.forEach(task => {
              if(
                task.itemCode.trim() ==newVal.trim()
                ||
                  this.mateUpc(newVal.trim(),task.upcs) 
              
              &&   task.state==0){ 
                   flag=true;  
              }
          });
           if(flag){
            
            this.getTask();
              setTimeout(function () { 
          　　　　var domFocus = document.getElementById("cellCode"); 
                domFocus.focus();
          　　}, 100);
          } 
          }
         
			  } , 
        cellCode:function(newVal, oldVal){
           
          if(newVal!=null && newVal.length>2){
              this.commit( );
          }
          
        }, 
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
         
       
         commit( ){
            const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
            var task = this.exTask;
          let param = {
            pickTaskReturnId:task.pickTaskReturnId, 
            wareId:this.wareId ,
            commitCellCode:this.cellCode
          }
          IOT.getServerData('/pick/task/returns/up','get',param,(ret) => {
            if (ret.code === 200) {
              IOT.tips('成功！', 'success', 1000, () => {
              });
              this.pickTasks.splice(0 , 1)
              this.exTask =null;
              this.cellCode = null;
              this.itemCode=null;
              loading.close();
              if(this.pickTasks.length>0){
                  setTimeout(function () { 
              　　　　var domFocus = document.getElementById("itemCode"); 
                    domFocus.focus();
              　　}, 100);
              }
              

            } else {
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
            if(
              tasks[i].itemCode.trim()==that.itemCode.trim()
            || this.mateUpc(that.itemCode.trim(),tasks[i].upcs) 
            ){  
              this.exTask = tasks[i];
                this.pickTasks.unshift(this.pickTasks.splice(i , 1)[0]); 
                return;
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
        var boxCode = that.boxCode;
        // if(boxCode==null || boxCode==undefined || boxCode==NaN || boxCode==""){
        //     return;
        // }
         
        var wareId = that.wareId; 
        IOT.getServerData('/pick/task/returns/getByBox','get', {
          boxCode:boxCode,
          wareId:wareId
        },(ret) => {
          if (ret.code === 200) {  
            that.pickTasks = ret.rows;
            if(that.pickTasks.length>0){
                setTimeout(function () { 
          　　　　var domFocus = document.getElementById("itemCode"); 
                domFocus.focus();
          　　}, 100);
            }
              
            if(that.pickTasks.length==0){
              IOT.tips('未查询到还货上架任务', 'error');
              setTimeout(function () { 
          　　　　var domFocus = document.getElementById("boxCode"); 
                domFocus.focus();
          　　}, 100);
            }
             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }, 
    },
    created() {
      setTimeout(function () {
　　　　 
  　　　　var domFocus = document.getElementById("boxCode"); 
         domFocus.focus();
  　　}, 100);
      var that =this;
     
       
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
