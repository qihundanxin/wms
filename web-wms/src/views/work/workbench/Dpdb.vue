<template>
  <div class="Dpdb">
    <!-- <audio id="myaudio" controls="controls" hidden src="../../../assets/mp3/wms称重成功.mp3" ref="audio"></audio> -->
    <h1>多品打包工位</h1>

    <div class="checkTop">
      <el-card class="box-card">
              <el-form  class="clearFloat" :inline="true">
              <el-form-item label="播种格子">
                <el-input style="width:260px" v-model="shipBillNo" placeholder="播种格子"></el-input>
              </el-form-item>
              <el-form-item style="display: none" label="shipBillNo">
                <el-input v-model="shipBillNo" placeholder="shipBillNo"></el-input>
              </el-form-item>
              <!-- <el-form-item label="重量">
                <el-input v-model="weight" placeholder="重量"></el-input>
              </el-form-item> -->
              <el-form-item>
                <el-button type="primary" @click="getSoMasterData">查询</el-button> 
                <el-button type="primary"  >发运</el-button>
                <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint()" split-button  >
                  打印
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
                    <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item> 
                  
                    <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item> 

                  </el-dropdown-menu>
                </el-dropdown>
                 
              </el-form-item>
              <el-form-item label="自动打印">
                <el-switch
                      v-model="autoPrint"
                      active-color="#13ce66"
                  >
                  </el-switch>
              </el-form-item>
            </el-form>


            <div class="soBillDetail">

    

     

    

    

       </div>       
      </el-card>
      <br/>
      <div class="publicSearch" v-if="soMaster.billNo!=undefined">
      <div class="searchBar" :style="{height: isMoreSearchMaster?'auto':''}">
        <el-tabs v-model="activeTabName"   style="width: 100%;">
          <el-tab-pane label="SO单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                 
                  <span :title="soMaster.billNo">&nbsp;{{soMaster.billNo}}</span>
                </li>
                 <li>
                  <span>物流单号:</span>
                 
                  <span :title="soMaster.shipBillCode">&nbsp;{{soMaster.shipBillCode}}</span>
                </li>
                  <li>
                    <span>状态:</span>
                  <span  v-if="soMaster.state==-1" style="color:#909399">已取消</span> 
                  <span  v-if="soMaster.state==-2" style="color:#909399">关闭</span> 
                  <span  v-if="soMaster.state==1" style="color:#909399">待处理</span> 
                  <span  v-if="soMaster.state==3"  style="color:#F56C6C"  >缺货</span>
                  <span  v-if="soMaster.state==4"  style="color:#409EFF"  >已分配</span>
                  <span  v-if="soMaster.state==5"  style="color:#F56C6C"  >拣货中</span>
                  <span  v-if="soMaster.state==6"  style="color:#543044"  >拣货完成</span>
                  <span  v-if="soMaster.state==7"  style="color:#E6A23C"  >已复核</span>
                  <span  v-if="soMaster.state==8"  style="color:#67C23A"  >已发运</span>
                  
                </li>
                 <li>
                  <span>重量:</span>
                 
                  <span :title="soMaster.weight">&nbsp;{{soMaster.weight}}</span>
                </li>
              
                
                
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="soMaster.wareName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.wareId, 'ware')">&nbsp;{{soMaster.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="soMaster.organizationName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.organizationId, 'organization')">&nbsp;{{soMaster.organizationName}}</span>
                </li>
                <!-- <li>
                  <span>客户:</span>
                  <span :title="soMaster.customerName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.customerId, 'customer')">&nbsp;{{soMaster.customerName}}</span>
                </li> -->
                <li>
                  <span>收货人:</span>
                  <span :title="soMaster.linkMan">&nbsp;{{soMaster.linkMan}}</span>
                </li>
                <li>
                  <span>承运方:</span>
                  <span :title="soMaster.shipCode">&nbsp;{{soMaster.shipCode}}</span>
                </li>
               
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>创建人:</span>
                  <span :title="soMaster.createUserName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.userId, 'account')">&nbsp;{{soMaster.createUserName}}</span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span :title="soMaster.createTime">&nbsp;{{soMaster.createTime}}</span>
                </li>
                <li>
                  <span>备注:</span>
                  <span :title="soMaster.memo">&nbsp;{{soMaster.memo}}</span>
                </li>
               
              </ul>
            </div>
          </el-tab-pane>
          
       
           <el-tab-pane label="发货地址" name="5">
             <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>发件人:</span>
                  <!--<span :title="soMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{soMaster.mergeNo}}</span>-->
                  <span :title="soMaster.shipLinkMan">&nbsp;{{soMaster.shipLinkMan}}</span>
                </li>
                <li>
                  <span>联系方式:</span>
                  
                  <span  >&nbsp;{{soMaster.shipLinkPhone}}</span>
                </li>
                <li>
                  <span>国家:</span>
                  
                  <span  >&nbsp;{{soMaster.shipCountry}}</span>
                </li>
                <li>
                  <span>省:</span>
                  
                  <span  >&nbsp;{{soMaster.shipProvince}}</span>
                </li>
                <li>
                  <span>市:</span>
                  
                  <span  >&nbsp;{{soMaster.shipCity}}</span>
                </li>
                 <li>
                  <span>区:</span>
                  
                  <span  >&nbsp;{{soMaster.shipArea}}</span>
                </li>
                 <li>
                  <span>详细地址:</span>
                  
                  <span  >&nbsp;{{soMaster.shipDetailAddress}}</span>
                </li>
                
              </ul>
            
              
            </div>
          </el-tab-pane>
           <el-tab-pane label="收货地址" name="6">
             <div class="newBillData clearFloat">
            <ul class="leftFloat">
                <li>
                  <span>收件人:</span>
                  <!--<span :title="soMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{soMaster.mergeNo}}</span>-->
                  <span :title="soMaster.linkMan">&nbsp;{{soMaster.linkMan}}</span>
                </li>
                <li>
                  <span>联系方式:</span>
                  
                  <span  >&nbsp;{{soMaster.linkPhone}}</span>
                </li>
                <li>
                  <span>国家:</span>
                  
                  <span  >&nbsp;{{soMaster.country}}</span>
                </li>
                <li>
                  <span>省:</span>
                  
                  <span  >&nbsp;{{soMaster.province}}</span>
                </li>
                <li>
                  <span>市:</span>
                  
                  <span  >&nbsp;{{soMaster.city}}</span>
                </li>
                 <li>
                  <span>区:</span>
                  
                  <span  >&nbsp;{{soMaster.area}}</span>
                </li>
                 <li>
                  <span>详细地址:</span>
                  
                  <span  >&nbsp;{{soMaster.detailAddress}}</span>
                </li>
                
              </ul>
             </div>
          </el-tab-pane>
        </el-tabs>


        
      </div>
      
      </div>
       <br/>
     <el-table
            v-if="soDetails.length>0"
            highlight-current-row
           
            :data="soDetails"
            style="width: 100%">
            
        
        
            <el-table-column
              show-overflow-tooltip
              prop="itemName"
              label="SKU名称"
              min-width="300">
              <template slot-scope="scope">
                <el-row :gutter="24">
              <el-col :span="6"> 
                <div style="display: inline-block;   width:80px;height:86px">
                  <img :src="scope.row.imgUrl"  style="width:80px;height:86px">
                </div>
               </el-col>
              <el-col :span="16">
                <div @click="$parent.showPublicModal(scope.row.itemCode, 'item')" style="display: inline-block;margin-left:8px; height:86px">
                    <span class="textBtn" style="color:red"  >{{scope.row.itemCode}}</span>
                        <br>
                    <span    >{{scope.row.itemName}}</span>
                    <br>
                    <span>{{scope.row.spec}}</span>
                    <br>
                    <span>{{scope.row.model}}</span>
              </div>
              </el-col>
              
            </el-row>
                <!--<span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>-->
              </template>
            </el-table-column>
         
           
           

            <el-table-column
              show-overflow-tooltip
              prop="orderQuantity"
              label="数量"
              min-width="180">
              <template slot-scope="scope">
                <span style="font-size:30px">{{ scope.row.orderQuantity  }}</span>
              </template>
            </el-table-column>
            
           
            
          
     </el-table>
      
       
    </div>

   
    
  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'Dpdb',
    data(){
      
      return {
        autoPrint:false,
        wareId:PF.getLocal('wareId', 'number'),
        shipBillNo:'',
        weight:'',
         isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        activeName: 'first',
        activeTabName: 'first',

        searchData: {
          billNo: this.$route.params.billNo,
        },
        searchDataItem: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: null,
          organizationId: null,
        },

        soMaster: {},
        soDetails: [],
        form: {
          soMaster: {},
          soDetails: [],
        },
        soUdfHs: [],
        soUdfDs: [],
        ids: [],
        items: [],
        customers: [],
        organizations: [],
        platforms: [],
        carriers: [],
        pickTasks: [],
        inventoryBalances: [],
        multipleSelection: [],
        multipleSelection2: [],
        soBillTypes: [],
        traces: [],
        pickTaskMaster: {},
        two: '0',
        receivingAddresss: [],

        innerVisibleItem: false,
        dialogUpFileVisible: false,
        dialogNewVisible: false,
        dialogPickTaskVisible: false,
        dialogReviewVisible: false,

       

        formLabelWidth: '120px',

        printParams: {
          RequestData: '',
          EBusinessID: '',
          DataSign: '',
          IsPreview: ''
        },

        formReview: {
          billNo: this.$route.params.billNo,
          reviewResult: null,
          caseDetailId: null,
          memo: '',
        },
        rulesReview: {
          memo: [
            {required: true, message: '请填写审核原因', trigger: 'blur'}
          ],
        },

        boxDetails: [],
        loadDetails: []
      }
    },
    computed: {
      
    },
    methods: {
      demoPrint:function (isReview){
        var that = this; 
        if(that.soMaster.billNo==undefined || that.soMaster.billNo==null||that.soMaster.billNo==""  ){
          return;
        }
         IOT.getServerData('/so/masters/getDzmd', 'get', {billNo:that.soMaster.billNo}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data;
                 
                that.$parent.printHtml(strHTML,isReview);
                
            
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
       
      },
      sendOut(billNo,wareId,weight){
        var that = this;
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
       
        IOT.getServerData('/so/masters/sendOut', 'get', {
          billNo:billNo,
          wareId:wareId,
          weight:weight,
          autoPrint:that.autoPrint
        }, (ret) => {
         loading.close();
          if (ret.code === 200) {
             that.soMaster.state=8;
             if(that.autoPrint){
                that.$parent.printHtml(ret.data,undefined);
             }
             
             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      getSoMasterData() {
        var that  =this;
        if(that.shipBillNo==undefined || that.shipBillNo==null||that.shipBillNo==""  ){
            IOT.tips(  '请扫描面单条码！', 'error');
            return;
        }
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
       
        IOT.getServerData('/so/masters/getDtoByBoxOrCell', 'get', {code:that.shipBillNo,wareId:this.wareId}, (ret) => {
          loading.close();
          if (ret.code === 200) {
            that.shipBillNo = '';
            that.soMaster = ret.data;
            this.soDetails = that.soMaster.list;
             if(that.soMaster.state==7){
                 
                
            }else if(that.soMaster.state==-1){
                 this.$message({
                  showClose: true,
                  message: '该订单已取消！',
                  type: 'warning'
                });
              

            }
            // if(ret.datah==0){
            //    IOT.tips(  '未查询到订单！', 'error');
            //    return;
            // }else if(ret.rows.length>1){
            //     IOT.tips(  '订单异常，请重试', 'error');
            //     return;
            // }
            // let list = ret.rows[0];
            
            // that.soMaster = list;
            // that.getSoDetailData(that.soMaster.billNo);
           
           
          } else {
            that.shipBillNo = '';
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      getSoDetailData(billNo) {
        
        IOT.getServerData('/so/details/list', 'get', {billNo:billNo}, (ret) => {
          if (ret.code === 200) {
            this.soDetails = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      downSeeding(code){
                IOT.getServerData('/so/masters/downSeeding', 'get', {
                  data:code,
                  wareId:this.wareId
                }, (ret) => { 
                  if (ret.code === 200) { 
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });    
      },

      
        scanItemCode(code){
                var that = this;
                
                var billNo = that.soMaster.billNo;
                var state = that.soMaster.state;
                var list = code.split('.');
                //如果拆分成两端则说明是称重数据，billInfo不为{}则有匹配到订单
                if(  billNo!=undefined && state==7 ){ 
                  if(isNaN(Number(code))){
                      IOT.tips("请称重！", 'error');
                      return;
                  }
                  
                  that.shipBillNo = '';
                  that.weight = code;
                  that.soMaster.weight = code;
                  that.sendOut(billNo,that.wareId,code);
                } 
                else  {
                    that.soMaster = {};
                    that.soDetails = []; 
                    that.shipBillNo = code; 
                    that.getSoMasterData();
                    
                } 
                
              
            },
     
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
                        if(e.key=="Enter"){
                             //这里监听正常的键盘回车键
                            that.getSoMasterData();
                             code='';
                            lastCode = null;
                            lastTime = null;
                        }else{
                            code = e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        }   
                    }
                    //间隔大于500毫秒的是键盘输入的
                    else if(lastCode != null && lastTime != null && nextTime - lastTime > 5000){ 
                        if(e.key=="Enter"){
                            //这里监听正常的键盘回车键 
                            that.getSoMasterData();
                            code='';
                            lastCode = null;
                            lastTime = null;
                        }else {
                          code =e.key;
                          lastCode = nextCode;
                          lastTime = nextTime;
                        }   
                    }
                     //间隔小于500毫秒的是扫码或者称重进来的
                    else if (lastCode != null && lastTime != null && nextTime - lastTime <= 5000) {// 扫码枪
                        
                        if(e.key=="Enter"){
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
        },

        

    },
    created() {
      this.getCode();
      
            
            
    },
    destoryed(){
            document.onkeypress = null;
        },
    
  }
</script>

<style scoped lang="less">
   
</style>
