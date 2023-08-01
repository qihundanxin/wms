<template>
  <div v-loading="loading" class="BoxPickPrint">
    <FeaturesHeader     :headerTitle="headerTitle" />
   
    
    <el-dialog
      title="发运"
      :visible.sync="dialogVisible"
      width="80%"
     >
      <span>确定发运？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="fy">确 定</el-button>
      </span>
    </el-dialog>
     

       <div    style="text-align:center ;" class="   ">
         
         <div   v-if="printType==2"  class="scanInput">
            <el-input     id="printer2"  clearable  v-model="printer" placeholder="请扫描标签打印机二维码"></el-input>
            <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
        </div> 
        <div    v-if="printType==1"  class="scanInput">
            <el-input     id="printer1"  clearable  v-model="printer" placeholder="请扫描出库单打印机二维码"></el-input>
            <i  class="el-icon-camera" style="font-size:30px" @click="scan(1)" ></i>
        </div> 
        <el-button :style="'background-color:'+printType==2?'#409EFF':''"    @click="printType=2;focusScan(2)">打印标签</el-button>
        <el-button :style="'background-color:'+printType==1?'#409EFF':''"    @click="printType=1;focusScan(1)">打印出库单</el-button>
      </div> 
          <div v-if="picks.length>0" style="text-align:center">
            <span style="font-size:20px;">有{{picks.length}}张待打标签</span>
            <br/>
            <br/>
            <span style="font-size:20px;">有{{bills.length}}张待打出库单</span>
            <br/>
            <br/>
            <span style="font-size:20px;">请扫描打印机二维码进行打印</span>

          </div>
          
       

         
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
        printType:null,
       
        wave: this.$route.params,
        wareId:PF.getLocal('wareId', 'number'),
        waves:[], 
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
      getPrintData( type){
       
      var that = this; 
      var wave = that.wave; 
       
      if(wave.wareId==null){
          this.$router.push({name: 'PickTaskPrint'})
      }
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
       
      print1(printer){
        var that = this;
        if(printer !=that.printer ){
            return;
        }
        var printParam = printer.split('%');
        var ip = printParam[0];
        var printerIndex = printParam[1];
        var size = printParam[1];
        var fx = printParam[3];
        var ds = printParam[4];
        that.printer="";
        
        if(that.printType==1){
           that.bills.forEach(bill => { 
               IOT.qrPrint(bill.html,ip,printerIndex,size,fx,ds,1); 
               IOT.qrPrintUrl(bill.pdfUrl,ip,printerIndex,size,fx,ds,1);  
               IOT.qrPrint(bill.soPickTask,ip,printerIndex,size,fx,ds,1); 
              
                      
                    });
           
        }else if(that.printType==2){
          var htmlStr="";
            that.picks.forEach(pick => {
              htmlStr+=pick.html;
                      
           });
           IOT.qrPrint(htmlStr,ip,printerIndex,size,fx,ds,1); 
        }
        that.printType=null;
         

      },
    
      scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.printer = result;
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

      focusScan(type){
         
        if(type==1){
            setTimeout(function () { 
        　　　　var domFocus = document.getElementById("printer1"); 
              domFocus.focus();
        　　}, 100);
        }
        if(type==2){
            setTimeout(function () { 
        　　　　var domFocus = document.getElementById("printer2"); 
              domFocus.focus();
        　　}, 100);
        }
         
      }
 
        
       
       
      
      
      
      
      
    
    },
    
    created() {
     
      var that =this;
     // that.getWave();
      that.getPrintData(1);
      that.getPrintData(2);
      
      
       
   

       
    },
    destroyed(){
        
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>

.BoxPickPrint{
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

  .el-button:focus, .el-button:hover {
    color: #ecf5ff;
    border-color: #c6e2ff;
    background-color:#409EFF ;
  }

  .cuPrint {
    color: #ecf5ff;
    border-color: #c6e2ff;
    background-color:#409EFF ;
  }
}
  
</style>
