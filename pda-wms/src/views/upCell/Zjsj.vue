<template>
  <div class="baseUpCell">
      <FeaturesHeader :headerTitle="headerTitle" />  
    
    
    <div class="scanInput">
        <el-input clearable   placeholder="商品标签库存码"   id="inventoryId" v-model="inventoryId"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
    </div>

    <div v-if="inventory!=null"  class="scanInput">
        <el-input    placeholder="货位号"  clearable id="cellCode" v-model="cellCode"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(2)" ></i>
    </div>

      
           
         
         
        <br/> 
          <el-input   v-if="inventory!=null"   placeholder="数量"  clearable id="quantity" v-model="quantity"></el-input>
       

     <div style="text-align: center;margin-top:10px">
     
     
        
       
          
         
       

        <br/>
     

         <button   v-if="inventory!=null"    style="  width:94%;   margin-top:10px ;margin-left:3%">
                  <el-row>
                    <el-col :span="9">   <el-image   style="width: 80px; height: 80px"  :src="inventory.imgUrl"   ></el-image> 
                    </el-col>
                    <el-col :span="15"> 
                        <div  style="text-align:left;padding-left:5px;padding-top:0px;font-size:15px"   > 
                                    <div  >  
                                          <span>SKU：</span>  
                                          <span>{{inventory.itemCode}} </span>  
                                    </div>  
                                     <div  >  
                                          <span>UPC：</span>  
                                          <span>{{inventory.batchName}} </span>  
                                    </div>  
                                    <div  >  
                                          <span>数量：</span>  
                                          <span>{{inventory.quantity/inventory.transRatio}} {{inventory.packDescribe}}</span>  
                                    </div>  
                                    <div  >  
                                          <span>跟踪号：</span>  
                                          <span>{{inventory.boxCode}} </span>  
                                    </div>  
                                    
                        </div> 
                    </el-col>
                  </el-row> 
                 
                   <br/>
                  
                </button> 
     <br/>
        <br/>

       <el-button @click="add" style="width:100px;height:50px"  type="primary" >提交</el-button>
       
       <el-button @click="clear"  style="width:100px;height:50px" type="info" >清空</el-button>
       
     
    </div>  

    

    <!-- <div style="text-align: center;margin-top:30px">
       <span  v-if="memo.type==3"  style="font-size:20px;color:#67C23A;font-weight:bold;">{{memo.msg}}</span>
        
        <span    v-else-if="memo.type==-1"  style="font-size:20px;color:red;font-weight:bold;">{{memo.msg}}</span>
        <span    v-else-if="memo.type!=3"  style="font-size:20px;color:#409EFF;font-weight:bold;">{{memo.msg}}</span>
     
    </div> -->
 

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import Scan from "@/components/Scan"; 
  export default {
    name: "BaseUpCell",
    
    data() {
      return {
        scanResult:"",
        scanning:false,
        wareId:PF.getLocal('wareId', 'number'),
        quantity:'',
        inventory:null,
        headerTitle: "直接上架",
        formLabelWidth: '150px',

        loading: false,
        noMore: false,

        cellCode:'',
        inventoryId:'',
        memo:{
          code:1,
          msg:'请扫描商品标签库存码'
        }

         
      };
    },
    watch: {
		    
        inventoryId: function(newVal, oldVal) { 
          var that = this;
          if(newVal !=null && newVal.length>0){
             setTimeout(function () {  
                 that.getInventory(newVal);  
  　　        }, 200);
          }
          
          
			  } ,
        // cellCode: function(newVal, oldVal) {
        //   if(newVal.trim().length>=3){
        //      var domFocus = document.getElementById("quantity"); 
        //                domFocus.focus();
             
        //   } 
			  // } ,

		},

     
    methods: {
      scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.inventoryId = result;
            } 
            else if(scanType==2){
              this.cellCode=result;
            }
            SCAN.close();

         });
      },
        getInventory(newValue){
          var that = this;
          if(newValue!=that.inventoryId){
              return;
          }
          var  inventoryId =    parseInt(that.inventoryId.trim());
            IOT.getServerData('/inventorys/getAccept','get',{id:inventoryId,wareId:this.wareId},(ret) => { 
              if (ret.code === 200) {
                  this.inventory = ret.data;
                  if(ret.data != null){
                     setTimeout(function () {
　　　　 
                　　　　var domFocus = document.getElementById("cellCode"); 
                      domFocus.focus();
                　　}, 100);
                      
                  } else{
                      this.inventoryId = "";
                      this.$message.error('该库存码无效！'); 
                      that.memo={
                        code:1,
                        msg:'请扫描商品标签库存码'
                      }
                  }
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            },true)
        },
        clear(){
            var that = this;
            that.inventoryId  ='';
            that.sjcCellCode  ='';
            that.inventory = null;
             var domFocus = document.getElementById("inventoryId"); 
              domFocus.focus();
        },
        siCcw(){
             IOT.getServerData('/sjc/upper/tasks/zjUp','get',data,(ret) => { 
              if (ret.code === 200) {
                 that.inventoryId="";
                 that.cellCode="";
                 that.quantity="";
                 that.inventory=null;
                 var domFocus = document.getElementById("inventoryId"); 
                 domFocus.focus();
                 that.memo={
                    type:3,
                    msg:'成功,请继续下一次操作，扫描商品标签库存码'
                  }
              } else {
                 that.memo={
                    type:-1,
                    msg:ret.message
                  }
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            })
        },
        add(){
           var that = this;
            var  quantity =    parseInt(that.quantity.trim());
            var  inventoryId =    parseInt(that.inventoryId.trim());
             if(that.inventoryId.length<1){
                this.$message.error('请扫描库存码！'); 
              return;
            }
            if(that.cellCode.length<1){
                this.$message.error('请扫描货位号'); 
              return;
            }
            if(that.quantity.length<1){
                this.$message.error('请输入数量'); 
              return;
            }
            
            if(quantity>that.inventory.quantity){
                this.$message.error('请校验数量！'); 
              return;
            }


            if(quantity>0){
               
            }else{
               this.quantity="";
                this.$message.error('输入数量无效！'); 
                return;
            }
             
            
           let data = {
             wareId: PF.getLocal('wareId', 'number'),
             InventoryId:inventoryId,
             toCellCode:that.cellCode.trim(),
             quantity:quantity,
           }
           
            IOT.getServerData('/sjc/upper/tasks/zjUp','get',data,(ret) => {
               
              if (ret.code === 200) {
                 that.inventoryId="";
                 that.cellCode="";
                 that.quantity="";
                 that.inventory=null;
                 var domFocus = document.getElementById("inventoryId"); 
                 domFocus.focus();
                 that.memo={
                    type:3,
                    msg:'成功,请继续下一次操作，扫描商品标签库存码'
                  }
              } else {
                 that.memo={
                    type:-1,
                    msg:ret.message
                  }
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            })
        },
       //分析数据
        scanItemCode(code){
          var that  =this;
            if(that.inventoryId=='' || that.memo.type==1){
              that.inventoryId = code;
              that.memo={
                type:2,
                msg:'请扫描货位号'
              }
            }else if(that.cellCode=='' || that.memo.type==2){
              that.cellCode = code;
              that.add();
            }else if(that.memo.type==3){
              that.inventoryId = code;
              that.cellCode = '', 
              that.memo={
                type:2,
                msg:'请扫描上架车货位号'
              }
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

                    
                    else if(lastCode != null && lastTime != null && nextTime - lastTime > 500){ 
                        //监听正常键盘回车事件
                        if(e.key=="Enter"){
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
                    else if (lastCode != null && lastTime != null && nextTime - lastTime <= 500) {// 扫码枪
                        
                        if(e.key=="Enter"){
                          //这里监听扫码器或者称重设备的回车键
                            that.scanItemCode(code);
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
　　　　 
  　　　　var domFocus = document.getElementById("inventoryId"); 
         domFocus.focus();
  　　}, 100); 
        // EventBus.$on('scanResult', val => { // two 监听到 one 发送过来的消息 
        //   this.inventoryId = val;
        //   this.scanning=false; 
        // }) 
    },
    beforeDestroy() {
      SCAN.close();
      alert("ok");
       //EventBus.$off('scanResult')  // 监听后一定要在 beforeDestroy 中使用 $off 释放
    },
    
    components: {
      FeaturesHeader,
      Scan,
    }
  };
</script>
<style lang="less" scoped>
   
</style>
