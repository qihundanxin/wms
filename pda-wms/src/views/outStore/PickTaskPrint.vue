<template>
  <div class="PickTaskPrint">
    <FeaturesHeader     :headerTitle="headerTitle" />
   
    
  
     
 
         
             <el-button v-for="wave in waves"style="margin-top:5px;width:100%;margin-left:0px"  @click="$router.push({name: 'PickTaskPrint2',params: wave})"  >

              
                <div  v-for="bill in  wave.bills "  style="display: inline-block;margin-left:8px;  ">
                  <span style="margin-right:20px">{{ bill.billNo }}</span> 
                  <span style="color:#409EFF" v-if="bill.state==4">拣货中</span>
                  <span style="color:#409EFF" v-if="bill.state==6">拣货完成</span>
                  <span v-if="bill.state==5" style="color:#E11111">部分拣货</span>
                  <span style="color:#E6A23C" v-if="bill.state==7">复核完成</span>
                  <span style="color:#67C23A" v-if="bill.state==8">发运完成</span>
                  <span style="color:#909399" v-if="bill.state==-1">取消</span>
                </div>
              

          </el-button>
                  
               
         
       

         
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BoxPickPrint",
    data() {
      return {
        dialogVisible:false,
        printer:'',
        soMaster:{},
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
        wave:null,
        waveId:null,
        dialogFormVisible:false,
        pickTaskVisible:false,
       
        
        headerTitle: "标签打印",
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
        waves:[],
        bills:[],
        picks:[],
      
      };
    },
    watch: {
        
        printer:function(newVal, oldVal){
          var that = this;
          if(newVal!=null && newVal.trim().length>1){
             setTimeout(function () {  
                   that.print1(newVal); 
      　　    }, 200);
            
          } 
        }, 
		},
    
    methods: {
      getPrintData(wave,type){
       
      var that = this; 
      that.wave = wave;
      var url="";
        if(type==1){
            url = '/so/masters/getBillPrintData';
        }else if(type==2){
             url = '/so/masters/getPickPrintData';
        }
        that.loading = true;
       
         IOT.getServerData(url, 'get', {waveId:wave.waveId,wareId:that.wareId}, (ret) => { 
           that.loading = false;
          if (ret.code === 200) { 
                if(type==1){
                    that.bills = ret.data;
                    // that.bills.forEach(bill => { 
                    //   that.$parent.printHtml(bill.html,null);  
                    //   that.$parent.printUrl(bill.pdaUrl);  
                    // });
                }else if(type==2){
                   that.picks = ret.data;
                    // that.picks.forEach(pick => {
                    //   that.$parent.printHtml(pick.html,null); 
                    // });
                }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
    },
      getWave() {
      var that = this;
       that.loading = true;
       that.waves=[];
       that.bills=[];
       that.picks=[];
      IOT.getServerData('/wave/masters/findByCUser', 'get', {
        wareId:that.wareId 
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
      print1(printer){
        var that = this;
        if(printer !=that.printer ){
            return;
        }
        var printParam = printer.split('%');
        var ip = printParam[0];
        var printerIndex = printParam[1];
        var fx = printParam[2];
        that.printer="";
        that.picks.forEach(pick => {
                      IOT.qrPrint(pick.html,ip,printerIndex,fx,1); 
        });
         

      },
    
      scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.orderNo = result;
            } 
           
            SCAN.close(); 
         });
      },

      fy1(){
        if(this.soMaster.state!=undefined ){
            this.dialogVisible = true;
        }

      },

      fy(){
        let that = this
        that.loading=true;
        IOT.getServerData('/so/masters/sendOut','get', {billNo:this.soMaster.billNo,wareId:this.wareId},(ret) => {
          that.loading=false;
          if (ret.code === 200) { 
            that.soMaster = {};
            this.dialogVisible=false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);

      },
 
        
       
       
     
      getSoMasters(orderNo){
        let that = this
        that.loading=true;
        
        that.soMaster={};
        IOT.getServerData('/so/masters/findBillDetailByOrderNo','get', {orderNo:orderNo,wareId:this.wareId},(ret) => {
          that.loading=false;
          if (ret.code === 200) { 
            that.soMaster = ret.data;
            that.orderNo = "";
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      
      
      
      
    
    },
    
    created() {
  //     setTimeout(function () { 
  // 　　　　var domFocus = document.getElementById("printer"); 
  //        domFocus.focus();
  // 　　}, 100);
      var that =this;
      that.getWave();
      
      
       
   

       
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
