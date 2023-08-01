<template>
  <div class="baseUpCell">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;margin-top:15px">
     
     
      <div v-if="inventory==null"  class="scanInput">
        <el-input clearable placeholder="请扫描商品库存标签"  id="inventoryId" v-model="inventoryId"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
    </div>

    <div v-if="inventory!=null"  class="scanInput">
        <el-input     placeholder="请扫描上架车虚拟位"   clearable id="sjcCellCode" v-model="sjcCellCode"></el-input>
        <i  class="el-icon-camera" style="font-size:30px" @click="scan(2)" ></i>
    </div>
        
         
         
        
         
          <!-- <el-input   v-if="inventory!=null"  clearable id="quantity" v-model="quantity"></el-input>
           -->

         
      
 
       <!-- <el-button @click="add" style="width:200px;height:50px"  type="primary" >提交</el-button>
       <br/>
       <br/>
       <el-button @click="clear"  style="width:200px;height:50px" type="info" >清空</el-button> -->
     
    </div>

     <div v-if="inventory!=null" style="  width:90%;   margin-top:15px ;margin-left:5%">
       <el-row>
        <el-col :span="6">
          <el-image
            style="width: 100%; height: 80px"
            :src="inventory.imgUrl"
            ></el-image>
            
        </el-col>
        <el-col :span="18"> 
            <div  style="padding-left:8px;padding-top:10px;"   >
                   
                        <div class="topData clearFloat"> 
                              <span>SKU：</span>
                              <span>{{inventory.itemCode}} </span> 
                              <!-- <span style="font-weight:800;font-size:20px">{{inventory.itemCode}}</span> 
                              <span>/{{inventory.itemName}}</span> -->
                        </div>  
                        <div> 
                              <span>采购数量：</span>
                              <span>{{inventory.quantity}} </span> 
                        </div>
                        <div> 
                              
                              <el-input
                                size="mini"
                                placeholder="请输入内容" 
                                v-model="quantity">
                              </el-input>
                             
                        </div>
                      
                         
            </div>
            
        </el-col>
      </el-row>
      <div style="color:#FFFFFF;line-height:30px;padding-left:20px;width:100%;height:30px;background-color: #909399;"> 
                   {{inventory.itemName}}            
     </div>
                  
     </div>
     
 

    <div style="text-align: center;margin-top:30px">
       <span  v-if="memo.type==3"  style="font-size:20px;color:#67C23A;font-weight:bold;">{{memo.msg}}</span>
        <span    v-if="memo.type!=3"  style="font-size:20px;color:#409EFF;font-weight:bold;">{{memo.msg}}</span>
     
    </div>
 

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BaseUpCell",
    data() {
      return {
       
        wareId:PF.getLocal('wareId', 'number'),
        quantity:'',
        inventory:null,
        headerTitle: "上架车",
        formLabelWidth: '150px',

        loading: false,
        noMore: false,

        sjcCellCode:'',
        inventoryId:'',
        memo:{
          code:1,
          msg:'请扫描商品标签库存码'
        }

         
      };
    },
    watch: {
		    inventoryId: function(newVal, oldVal) {
          if(newVal.trim().length>=4){
            this.getInventory();
             
          } 
			  } ,
        sjcCellCode: function(newVal, oldVal) {
          if(newVal.trim().length>=3){
              this.add();
             
          } 
			  } ,

		},

     
    methods: {
       scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.inventoryId = result;
            } 
            else if(scanType==2){
              this.sjcCellCode=result;
            }
            SCAN.close();

         });
      },
        getInventory(){
          var that = this;
          var  inventoryId =    parseInt(that.inventoryId.trim());
            IOT.getServerData('/inventorys/getAccept','get',{id:inventoryId,wareId:this.wareId},(ret) => { 
              if (ret.code === 200) {
                  this.inventory = ret.data;
                  this.quantity =this.inventory.quantity; 
                  if(ret.data != null){
                    that.memo={
                        code:1,
                        msg:'请扫描上架车虚拟位'
                      }
                    setTimeout(function () {
　　　　 
                　　　　var domFocus = document.getElementById("sjcCellCode"); 
                      domFocus.focus();
                　　}, 100);
                        
                  } else{
                      this.inventoryId = "";
                      this.$message.error('该库存码无效（如果是爆款产品，请直接送到打包台发货）！'); 
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
            that.quantity  ='';
            that.inventory = null;
             var domFocus = document.getElementById("inventoryId"); 
              domFocus.focus();
        },
        add(){
           var that = this;
            var  quantity =that.quantity;//    parseInt(that.quantity.trim());
             if(that.inventoryId.length<1){
                this.$message.error('请扫描库存码！'); 
              return;
            }
            if(that.sjcCellCode.length<1){
                this.$message.error('请扫描上架车货位号'); 
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
             acceptInventoryId:that.inventoryId.trim(),
             sjcCellCode:that.sjcCellCode.trim(),
             quantity:quantity,
             orgId:that.inventory.organizationId
           }
           
            IOT.getServerData('/sjc/upper/tasks/add','post',data,(ret) => {
               
              if (ret.code === 200) {
                 that.sjcCellCode="";
                 that.inventoryId="";
                 that.quantity="";
                 that.inventory=null;
                 setTimeout(function () {
  　　　　 
            　　　　   var domFocus = document.getElementById("inventoryId"); 
                      domFocus.focus();
            　　  }, 100);
                 
                 that.memo={
                    type:3,
                    msg:'成功,请继续下一次操作，扫描商品标签库存码'
                  }
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            })
        },
       //分析数据
        scanItemCode(code){
          var that  =this;
            if(that.inventory==null){
               that.getInventory();
            }else if(that.inventory!=null){ 
              that.add();
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
                           that.scanItemCode();
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
　　　　 
  　　　　var domFocus = document.getElementById("inventoryId"); 
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
   
</style>
