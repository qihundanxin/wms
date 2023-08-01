<template>
  <div class="baseUpCell">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div  >
       <div  v-if="tasks.length==0"   class="scanInput">
        <el-input clearable placeholder="请扫描上架车编码"  id="shelfCode" v-model="shelfCode"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
    </div>

    <div v-if="taskId==null&&tasks.length>0"  class="scanInput">
        <el-input     placeholder="请扫描上架车虚拟位"   clearable id="sjcCellCode" v-model="sjcCellCode"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(2)" ></i>
    </div>
    <div   v-if="sjcCellCode!=null&&tasks.length>0&&taskId!=null"    class="scanInput">
        <el-input     placeholder="请扫描仓位"   clearable id="toCellCode" v-model="toCellCode"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(3)" ></i>
    </div>
       <!-- <el-input v-if="tasks.length==0" id="shelfCode" v-model="shelfCode"   placeholder="请扫描上架车编码" clearable  ></el-input>
        <el-input  v-if="taskId==null&&tasks.length>0"  id="sjcCellCode" v-model="sjcCellCode"  placeholder="请扫描上架车虚拟位"  clearable ></el-input>
        <el-input  v-if="sjcCellCode!=null&&tasks.length>0&&taskId!=null"   id="toCellCode"   v-model="toCellCode"   placeholder="请扫描仓位"   ></el-input>
     -->

    </div>
    <!-- <div v-if="tasks.length==0" style="text-align: center;margin-top:20px">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="上架车编码" :label-width="formLabelWidth">
          <el-input id="shelfCode" v-model="searchData.shelfCode"   placeholder="请输入上架车编码" clearable style="width: 90%;"></el-input>
        </el-form-item>

      </el-form>
      <br/>
      <br/>
      <el-button @click="tasks = [];getSjcTasks()" type="primary"       style="height:50px;width: 200px;margin-top: 5px;"> 查询</el-button>
    </div> -->

     <!-- <div  v-if="tasks.length>0" style="text-align: center;margin-top:20px">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="from" :label-width="formLabelWidth">
          <el-input id="sjcCellCode" v-model="sjcCellCode" size="mini" placeholder="from" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="to" :label-width="formLabelWidth">
          <el-input  id="toCellCode"  v-model="toCellCode" size="mini" placeholder="to" clearable style="width: 90%;"></el-input>
        </el-form-item>

      </el-form>

      <br/>
      {{thisIndex+1}}/{{tasks.length}}
      <el-button @click="check()" type="primary" size="mini"   style="width: 200px;margin-top: 5px;"> 上架</el-button>
    </div> -->

    <!-- <el-divider content-position="center">待上架库存</el-divider> -->
    <div v-for="(item,index) in tasks"   style="  width:94%;   margin-top:10px ;margin-left:3%">
       <el-row>
        <el-col :span="10">
          <el-image
            style="width: 100%; height: 100px"
            :src="item.imgUrl"
            ></el-image>
            
        </el-col>
        <el-col :span="14"> 
            <div  style="padding-left:5px;padding-top:0px;"   >
                
                   
                        <div class="topData clearFloat"> 
                         
                              <span>SKU：</span> {{item.sjcUpperTaskId}}
                              <span>{{item.itemCode}} </span> 
                              
                        </div>  
                        <div> 
                              <span>数量：</span>
                              <span>{{item.quantity}} </span> 
                        </div>
                        <div> 
                              <span>上架车</span>
                              <span>{{item.sjcCellCode}} </span> 
                        </div>
                        <div> 
                              <span>货位号</span>
                              <span>{{item.toCellCode}} </span> 
                        </div>
                         
                        <!-- <div> 
                              <span>数量:</span>
                              <span v-if="task.reviewQuantity<task.pickQuantity" style="color:#E6A23C;font-size:30px">{{task.reviewQuantity}} </span>
                              <span v-else style="color:#409EFF;font-size:30px">{{task.reviewQuantity}}</span>
                              <span style="color:#409EFF;font-size:30px">/{{task.pickQuantity}}</span> 
                              <span style="margin-left:90px">状态:</span>
                              <span v-if="task.state==2" style="color:#67C23A;font-size:20px">完成</span> 
                              <span v-else-if="task.reviewQuantity>0.1" style="color:#E6A23C;font-size:20px">拣货中</span>
                              <span v-else-if="task.state==0" style="color:#909399;font-size:20px">待拣货</span> 
                        </div>  -->
                         
            </div>
            
        </el-col>
      </el-row>
      
     <div v-if="taskId==item.sjcUpperTaskId" style="color:#FFFFFF; line-height:30px;padding-left:20px;width:100%;height:30px;background-color: #409EFF;"> 
                   {{item.itemName}}            
     </div>
     <div v-else style="color:#FFFFFF;line-height:30px;padding-left:20px;width:100%;height:30px;background-color: #909399;"> 
                   {{item.itemName}}            
     </div>

     
                  
     </div>

    <div class="baseAcceptBillBox noScrollbar">
      
     
       
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BaseUpCell",
    data() {
      return {
        shelfCode:'',
        task:null,
        taskId:null,
        wareId: PF.getLocal('wareId', 'number'),

          sjcCellCode:null,
          toCellCode:null,

        thisIndex:0,
        headerTitle: "上架车上架",
        formLabelWidth: '120px',
        tasks:[],
        loading: false,
        noMore: false,

        inventoryBalances: [],

        searchData: {
          pageNum: 1,
          pageSize: 100,
          total: null,

          wareId: PF.getLocal('wareId', 'number'),
          shelfCode: '',

        },
      };
    },
     watch: {
       shelfCode: function(newVal, oldVal) {
         if(newVal.trim().length>=3){
              this.getSjcTasks();
          } 
          
			  } ,
        sjcCellCode: function(newVal, oldVal) {
          if(newVal.trim().length>=3){
              this.getTask(); 
          } 
           
			  } ,

        toCellCode: function(newVal, oldVal) {
          if(newVal.trim().length>=3){
              this.upTask(); 
          } 
			  } ,

		     
		},
    methods: {
       scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(shelfCode==1){
                this.inventoryId = result;
            } 
            else if(scanType==2){
              this.sjcCellCode=result;
            }
            else if(scanType==3){
              this.toCellCode=result;
            }
            SCAN.close();

         });
      },
      upTask(){
        var that = this;
        var task = that.task;
        var  sjcCellCode = that.sjcCellCode.trim();
        var  toCellCode = that.toCellCode.trim();
        var inventoryId = task.inventoryId; 
        if(sjcCellCode!= task.sjcCellCode){
            this.$message.error('上架车货位错误');
              return;
        }
        if(task.toCellCode !=null && toCellCode!= task.toCellCode){
              that.$confirm("系统推荐货位为："+task.toCellCode+"，当前扫描货位为："+toCellCode+"，是否继续？", '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                  that.commit(task.sjcUpperTaskId,toCellCode);
              }).catch(() => {
                return;
              });

        }else{
            that.commit(task.sjcUpperTaskId,toCellCode);
        }

      },
      check(){
        var that = this;
        var task = that.task;
        var  sjcCellCode = that.sjcCellCode.trim();
        var  toCellCode = that.toCellCode.trim(); 
        if(sjcCellCode!= task.sjcCellCode){
            this.$message.error('上架车货位错误');
              return;
        } 
        IOT.getServerData('/sjc/upper/tasks/check','get',{sjcUpperTaskId:task.sjcUpperTaskId,toCellCode:toCellCode,wareId:this.wareId},(ret) => {
          if (ret.code === 200) {
              that.upTask();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
       });
      },

      commit(taskId,toCellCode){
        var that = this;
          IOT.getServerData('/sjc/upper/tasks/commit','get',{sjcUpperTaskId:taskId,toCellCode:toCellCode,wareId:this.wareId},(ret) => {
          if (ret.code === 200) {
            that.task = null;
            that.taskId=null;
            that.sjcCellCode=null;
            that.toCellCode=null;
              that.tasks.splice(0, 1);
              this.$message({
                type: 'success',
                message: '上架成功!'
              });
          } else {
            that.toCellCode=null;
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSjcTasks(){
        this.tasks = [];
        this.loading = true;
        this.searchData.shelfCode =this.shelfCode;
        IOT.getServerData('/sjc/upper/tasks/findBySjcShellCode','get',this.searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.tasks = ret.rows;
            if(this.tasks.length>0){
              this.shelfCode = "";
              this.searchData.shelfCode = "";
              // var domFocus = document.getElementById("sjcCellCode");
              //     domFocus.focus();
                setTimeout(function () { 
            　　　　var domFocus = document.getElementById("sjcCellCode");
                  domFocus.focus();
            　　}, 100);
            }else{
              this.searchData.shelfCode = "";
               var domFocus = document.getElementById("shelfCode");
                domFocus.focus();
            }
            // this.searchData.total = ret.total;
            // this.searchData.pageNum = ret.pageNum;
            // if(this.searchData.pageSize >= ret.total){
            //   this.noMore = true;
            // }else{
            //   this.noMore = false;
            // }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getTask(){
        var that = this;
        var tasks  =this.tasks;
        for (let i = 0; i < tasks.length; i++) { 
            if(tasks[i].sjcCellCode.trim()==that.sjcCellCode.trim()){
              that.task = tasks[i];
              that.taskId = tasks[i].sjcUpperTaskId;
                tasks.unshift(tasks.splice(i , 1)[0]);
                 setTimeout(function () { 
            　　　　var domFocus = document.getElementById("toCellCode");
                    domFocus.focus();
            　　 }, 100);
               
            }
        }

      },


       //分析数据
        scanItemCode(code){
          var that  =this;
            if(that.tasks.length==0){
               that.getSjcTasks();
            }else if(that.tasks.length>0 && that.toCellCode!=null && that.toCellCode !=null){ 
              that.upTask();
            }else if (that.tasks.length>0 && that.toCellCode==null && that.toCellCode ==null){
              that.getTask();
            } 
        },
        //得到数据
       getCode(){
           var that = this;
                var code = "";
                var lastTime, nextTime;
                var lastCode, nextCode;
                document.onkeypress = function (e) {
                  
                    nextCode = e.which;
                    nextTime = new Date().getTime();
                    var now = new Date().getTime(); 
                    
                    if(lastCode == null && lastTime == null){
                        //监听正常键盘回车事件
                        if(e.key=="Enter"){
                          that.scanItemCode();
                            code='';
                            lastCode = null;
                            lastTime = null; 
                        } 
                        //
                        else{
                            code = e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        } 
                    }

                    
                    else if(lastCode != null && lastTime != null && nextTime - lastTime > 50){ 
                        //监听正常键盘回车事件
                        if(e.key=="Enter"){
                          that.scanItemCode( );
                            code='';
                            lastCode = null;
                            lastTime = null; 
                        } else{
                            code =e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        } 
                    }
                    //间隔小于500毫秒的是扫码或者称重进来的
                    else if (lastCode != null && lastTime != null && nextTime - lastTime <= 50) {// 扫码枪
                        
                        if(e.key=="Enter"){
                          //这里监听扫码器或者称重设备的回车键
                            that.scanItemCode( );
                            code='';
                            lastCode = null;
                            lastTime = null;
                        }else{
                            code += e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        }
                        
                    } 

 
                } 
        }

    },
    created() {
     // this.getCode();
      
       setTimeout(function () { 
  　　　　var domFocus = document.getElementById("shelfCode");
         domFocus.focus();
  　　 }, 100);

    },
    destoryed(){
            document.onkeypress = null;
        },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseUpCell {
  }
</style>
