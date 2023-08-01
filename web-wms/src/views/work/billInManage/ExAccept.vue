<template>

  <div class="exAccept">
  
    <div class="publicSearch">
          
      <div class="searchBar" style="height:140px">
         <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
         
         <div>
          <span>异常类型</span>
          <el-select  class="type" v-model="searchData.type" size="mini" filterable placeholder="请选择异常类型" clearable>
              <el-option  value=1  key=1  label="包裹异常"  > </el-option>
                  <el-option  value=2  key=2  label="商品异常"  > </el-option> 
          </el-select>
        </div>
        <div>
          <span>异常原因</span>
          <el-select v-model="searchData.reason" size="mini" filterable placeholder="异常原因" clearable>
            <el-option  key=1    value=1  label="多发"  ></el-option>
            <el-option  key=2    value=2  label="少发" ></el-option>
            <el-option  key=3    value=3 label="错发" >错发</el-option>
            <el-option  key=4    value=4 label="破损" >破损</el-option>
            <el-option  key=5    value=5 label="其他" >其他</el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>采购单号</span>
          <el-input v-model="searchData.billNo" size="mini"   clearable></el-input>
        </div> 
        

          <br/>
       
       
       <!-- <div>
          <span>验收位置</span>
          <el-select v-model="acceptCellCode" placeholder="请选择" size="mini">
            <el-option
                v-for="cellInfo in cellInfos"
                :key="cellInfo.cellCode"
                :label="cellInfo.cellCode"
                :value="cellInfo.cellCode">
            </el-option>
          </el-select>

        </div> -->
         <!-- <div >
          <span>快递单号</span>
          <span style="font-size:20px;color:#409EFF;font-weight:bold;">{{shipInfo.shipBillNo}}</span> 
        </div> -->

        <!-- <div>
          <span>包裹重量</span>
          <span style="font-size:20px;color:#409EFF;font-weight:bold;">{{shipInfo.weight}}</span> 
        </div>

        <div>
          <span>长</span>
          <el-input :disabled="shipInfo.acceptTime !=null" style="width:80px" v-model="shipInfo.length" size="mini" placeholder="长"  ></el-input>
        </div>

        <div>
          <span>宽</span>
          <el-input :disabled="shipInfo.acceptTime !=null"  style="width:80px"  v-model="shipInfo.width" size="mini" placeholder="宽"  ></el-input>
        </div>

        <div>
          <span>高</span>
          <el-input :disabled="shipInfo.acceptTime !=null"  style="width:80px"  v-model="shipInfo.height" size="mini" placeholder="高"  ></el-input>
        </div> -->

        <div>
          <span>自动打印</span>
          <el-switch
            v-model="autoPrint"
            active-color="#13ce66"
            >
          </el-switch>
          
        </div>
        
       
        <div>
          <el-dropdown style="margin-left:10px;margin-right:10px"  size="small"   type="primary"    icon="iconfont icon-print"  @click="demoPrint()" split-button  >
            打印
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
              <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item> 
            
              <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item> 

            </el-dropdown-menu>
          </el-dropdown>
        </div>

        
        
        
      </div>
    </div>


     <div class="headerBtns">
        
            
       <span v-if="asnMaster.memo!=null" style="color:red;font-size:30px">采购备注：{{asnMaster!=null?asnMaster.memo:''}}</span>
    </div> 

    <div class="tableBox none" >
      <el-table
      border
      v-loading="loading.masterTable"
        highlight-current-row
        :data="asnDetails"
         @row-click="clickRow"
        
        style="width: 100%">
    
        <el-table-column
          type="index"
          width="30">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/SKU名称"
          min-width="300">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8"> 
                <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                   <el-image 
                      style="width: 80px; height: 80px"
                      :src="scope.row.imgUrl" 
                      :preview-src-list="[scope.row.imgUrl]">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="16">
                <div  style="display: inline-block;margin-left:5px;  ">
                    <span class="textBtn" style="color:red"
                        >{{scope.row.itemCode}}</span>
                        <br/>
                    <span    >{{scope.row.itemName}}</span>
                    <br/>
                    
                     
                     <span  v-if="scope.row.shipBillNo !=null"   >{{scope.row.shipBillNo}}</span>
                <br/>
               
                <span   >{{scope.row.billNo}}</span>
                <br/>
                   
              </div>
              </el-col>
              
            </el-row>
            
            
          </template>
        </el-table-column>

        <el-table-column
        
          show-overflow-tooltip
          prop="billNo"
          label="异常信息"
          min-width="230">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="12"> 
                <div style="display: inline-block;margin-left:5px;  width:80px;height:80px" v-if="scope.row.imgUrls && scope.row.imgUrls.length > 0">
                   <el-image 
                      style="width: 80px; height: 80px"
                      :src="scope.row.imgUrls[0]" 
                      :preview-src-list="scope.row.imgUrls">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="12">
               
                <!-- <span>数量: </span>
                <span class="textBtn"  >{{scope.row.quantity}}</span>
                <br> -->
                <span>类型: </span>
                 <span   class="textBtn"   v-if="scope.row.type==1" >包裹异常</span>
                <span   class="textBtn"   v-if="scope.row.type==2" >商品异常</span> 
          
                <br>
                <span>原因: </span>
                <span   class="textBtn"   v-if="scope.row.reason==1" >多发</span>
                <span   class="textBtn"   v-if="scope.row.reason==2" >少发</span>
                <span   class="textBtn"   v-if="scope.row.reason==3" >错发</span>
                <span   class="textBtn"   v-if="scope.row.reason==4" >破损</span>
                <span   class="textBtn"   v-if="scope.row.reason==5" >其他</span>
 
                <br>
                <span>描述: </span>
                <span class="textBtn"  >{{scope.row.memo}}</span>
                <br>
              </el-col>
            </el-row>
            
            
          </template>
        </el-table-column> 

         <el-table-column
        
          show-overflow-tooltip
          prop="billNo"
          label="处理方案"
          min-width="180">
          <template slot-scope="scope">
            
             <el-tag type="warning" v-if="scope.row.fangan==1">全部退件</el-tag>
             <el-tag type="danger" v-if="scope.row.fangan==2">全部弃件</el-tag>
             <el-tag  type="success" v-if="scope.row.fangan==3">全部入库</el-tag>
             <el-tag type="warning" v-if="scope.row.fangan==4">部分入库部分退件</el-tag>
             <el-tag type="danger" v-if="scope.row.fangan==5">部分入库部分弃件</el-tag> 
 
           
            
            
          </template>
        </el-table-column> 
       
       
        <el-table-column
         :render-header="renderHeader"
          show-overflow-tooltip
          prop="expectQuantity"
          label="数量"
          min-width="130">
          <template slot-scope="scope">
            订单数：<span style="color:#409EFF;margin-left:2px;font-size:20px">{{scope.row.orderQuantity/1 }}  </span>
             <br>
             已入库数：<span style="color:#409EFF;margin-left:2px;font-size:20px">{{scope.row.inQuantity/1 }}  </span>
             <br>
            异常数：<span style="color:red;margin-left:2px;font-size:20px">{{scope.row.quantity/1 }}  </span>
             <br>
            <!-- 入库数：<span style="color:#67C23A;margin-left:2px;font-size:30px">  {{scope.row.inQuantity /1}}</span>
            <br>
            退库数：<span style="color:#409EFF;margin-left:2px;font-size:30px">  {{scope.row.returnQuantity / 1}}</span>
          <br>
             -->
            <br>
      
          </template>
        </el-table-column>
        
        <el-table-column
          show-overflow-tooltip
          :render-header="renderHeader"
          prop="toQuantity"
          label="验收信息/异常信息"
          min-width="200">
          <template slot-scope="scope">
               <el-tag  v-if="scope.row.state==0"   type="success">初始化  </el-tag>
               <el-tag  v-if="scope.row.state==1"   type="success">提交ERP  </el-tag>
               <el-tag  v-if="scope.row.state==2"   type="danger">待处理  </el-tag>
               <el-tag  v-if="scope.row.state==3"   type="success">已经处理  </el-tag>
               
              
            <!-- <div style="width:90%"   v-if="   scope.row.state==2">
                入库数量：<el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.toQuantity" controls-position="right" min=0
                              placeholder="" label="请输入数量" size="small"
                             style="width: 90%;"></el-input-number>
            
                <div style="height:5px"></div>
                <11!-- 退库数量：<el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.returnQuantity" controls-position="right" min=0
                                 placeholder="" label="请输入数量" size="small"
                                style="width: 90%;"></el-input-number>
                
                <div style="height:5px"></div> --11>
                打印数量： <el-input-number  :controls='controls' @focus="input0Set" v-model="scope.row.printQuantity" controls-position="right" min=0
                               label="请输入数量" size="small"
                                style="width: 90%;"></el-input-number>
                <div style="height:5px"></div>
                
             
                <span    v-if="scope.row.weight==null && searchData.weightTableIndex == scope.$index"  style="font-size:20px;color:#409EFF;font-weight:bold;">单位重量：请称重</span>
                
                <span  v-if="scope.row.weight!=null" style="font-size:20px;color:#67C23A;font-weight:bold;">单位重量：{{scope.row.weight}}</span>
                 

            </div> -->

          
            
            
          </template>
        </el-table-column>
         
   
        <el-table-column
          fixed="right"
          label="操作"
          min-width="130">
          <template slot-scope="scope">
            <div   v-if=" searchData.weightTableIndex == scope.$index" >
                
                  <!-- <el-button  v-if="scope.row.state==2" @click="accept(scope.row)" icon="iconfont icon-shouhuo" type="primary" 
                  size="small">入库</el-button> -->
                    <div style="height:10px"></div>
                   <el-button  v-if="scope.row.state==0" @click="fangan(scope.row)" icon="iconfont icon-shouhuo" type="primary" 
                     size="small">解决方案</el-button>
                        <div style="height:10px"></div>
                   
                             
            </div>
            
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
       class="pagination-bottom"
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>
    </div>


    <el-dialog title="解决方案推送" :visible.sync="dialogFormVisible">
      <el-form :model="returnInfo" >

          <span>异常方案推送是erp推送到wms的，现在为了测试方便，在这里向wms发送处理方案</span>
           <el-divider></el-divider>
        
        <el-form-item label="方案"  label-width="120px">
          <el-select v-model="returnInfo.fangan" placeholder="请选择方案">
              <el-option  label="全部退件"  value=1>全部退件</el-option>
              <el-option  label="全部弃件"  value=2>全部弃件</el-option>
              <el-option  label="全部入库"  value=3>全部入库</el-option>
              <el-option  label="部分入库部分退件"  value=4>部分入库部分退件</el-option>
              <el-option  label="部分入库部分弃件"  value=5>部分入库部分弃件</el-option>  
          </el-select>
        </el-form-item>
        <div v-if="returnInfo.fangan==1 || returnInfo.fangan==4">
            <el-form-item label="收件人名字" label-width="120px">
                <el-input v-model="returnInfo.linkMan"  ></el-input>
              </el-form-item> 
            <el-form-item label="收件人电话" label-width="120px">
                <el-input v-model="returnInfo.linkPhone"></el-input>
              </el-form-item>
              <el-form-item label="快递单号" label-width="120px">
                <el-input v-model="returnInfo.shipBillCode"></el-input>
              </el-form-item>
              <el-form-item label="国家" label-width="120px">
                <el-input v-model="returnInfo.country"  ></el-input>
              </el-form-item>
              <el-form-item label="省" label-width="120px">
                <el-input v-model="returnInfo.province"  ></el-input>
              </el-form-item>
              <el-form-item label="市" label-width="120px">
                <el-input v-model="returnInfo.city"  ></el-input>
              </el-form-item>
              <el-form-item label="区" label-width="120px">
                <el-input v-model="returnInfo.area"  ></el-input>
              </el-form-item>
              <el-form-item label="详细地址" label-width="120px">
                <el-input v-model="returnInfo.detailAddress"  ></el-input>
              </el-form-item>
              <el-form-item label="面单地址" label-width="120px">
                <el-input v-model="returnInfo.expressBillUrl"  ></el-input>
              </el-form-item>

        </div>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="toWmsFangan ">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {mapState} from "vuex";
  export default {
    name: 'exAccept',
    data() {
      return {
        acceptCellCode: PF.getLocal('acceptCellCode'),
        cellInfos: [],
         
        dialogFormVisible:false,
        rowData:{
        },
        returnInfo:{
            linkMan:'',
            linkPhone:'',
            shipBillCode:'',
            country:'',
            province:'',
            city:'',
            area:'',
            detailAddress:'',
            expressBillUrl:'',
            fangan:''
          },
        ex:false,
        fileList:[],
        asnMaster:{},
        chengzhong:false,
        ws:'',
        form:[],
        controls:false,

        multipleSelection:[],
        loading:{
            masterTable:false,
        },
        acceptExcess:null,
        autoPrint:true,
        baokuan:false,
        autoBoxCode:true,
        isMoreSearch: false,
        rows:[],
        spans:[],
        asnDetails:[], 
        shipInfo:{ 
          weight:'',
          shipBillCode:'',
          lenght:'',
          width:'',
          height:'' 
        },
        organizations: [],
        searchData: {
          weightTableIndex:null,
          pageNum: 1,
          pageSize: 20,
          total: 0,
          keyWords: '',
          reason:'',
          type:'',
          billNo:'',
          wareId: PF.getLocal('wareId', 'number'),
          
        },
        exReasons:[
          {
            value:'1',
            label:'多发'
          },
          {
            value:'2',
            label:'少发'
          },
          {
            value:'3',
            label:'错发'
          },
          {
            value:'4',
            label:'破损'
          } ,
          {
            value:'5',
            label:'其他'
          } 
        ],

        inCellTasks: [],

        rules: {
        },
        asnUdfHsArr:[],
        asnUdfDsArr:[],

        batchTacticDetails:[],
        batchTactics: [],
        
      }
    },
    watch: {
      acceptCellCode: function (newVal, oldVal) {
        PF.setLocal('acceptCellCode', newVal);
      }
    },
    computed: {
      ...mapState([
      
        "qcStates",
        
      ]),
      
    },
    methods: {
      getAcceptCell() {
      let param = {
        wareId: PF.getLocal('wareId', 'number'),
        cellUseTypes: {0: 0}
      };
      IOT.getServerData('/cell/infos/findByCellUserTypes', 'get', param, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {

          this.cellInfos = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      })
    },
      accept(detail){
        
        var that = this;
     
          let acceptData ;
          acceptData = {
            wareId:detail.wareId,
            wareName:detail.wareName,
            organizationId:detail.organizationId,
            organizationName:detail.organizationName,
            itemCode:detail.itemCode,
            itemName:detail.itemName,
            spec:detail.spec,
            model:detail.model,
            imgUrl:detail.imgUrl,
            toQuantity:detail.toQuantity,
            quantity:detail.toQuantity,
            billType:6,
            asnBillNo:detail.bill_no,
            asnDetailNo:detail.billDetailNo,
            shipBillCode:detail.shipBillCode,  
            acceptExId:detail.acceptExId,
            toPackDetailId:166,
            toPackDescribe:'each',
            toTransRatio:1,
            autoPrint:that.autoPrint,
            toCellCode : that.acceptCellCode,
            // version:
          }
          IOT.showOverlay('保存中，请稍等...');
          IOT.getServerData('/accept/records/accept', 'post', acceptData, (ret) => { 
            IOT.hideOverlay();
              if (ret.code === 200) { 
                detail.state=3;
                var strHTML = printData[0].data; 
                that.$parent.printHtml(strHTML,null,detail.printQuantity);
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            },true)


      },
      fangan(row){
          var that  =this;
          that.rowData = row;
          that.rowData.returnInfo={};
          that.dialogFormVisible=true;
          this.returnInfo = {};

      },
      toWmsFangan(){
        
        var that  =this;
        that.dialogFormVisible = false
        var rowData = that.rowData;
        let data = {
            billNo:rowData.billNo,
            exFangan:this.returnInfo.fangan,
            qcNo:rowData.acceptExId,
            skuCode:rowData.itemCode,
            shipBillNo:rowData.shipBillNo,
            returnInfo: this.returnInfo  
          };
          IOT.showOverlay('保存中，请稍等...');
          IOT.getServerData('/asn/masters/addInExResult', 'post', data, (ret) => { 
            IOT.hideOverlay();
              if (ret.code === 200) {  
                that.getExData();
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            },true)

          

      },
      clickRow(row, column, event){
        this.itemWieht(row.index);
      },
      // acceptEx(asnDetail){
       
      //     let exData = {
      //       wareId:this.asnMaster.wareId,
      //       wareName:this.asnMaster.wareName,
      //       organizationId:this.asnMaster.organizationId,
      //       organizationName:this.asnMaster.organizationName,
      //       itemCode:asnDetail.itemCode,
      //       itemName:asnDetail.itemName,
      //       spec:asnDetail.spec,
      //       model:asnDetail.model,
      //       imgUrl:asnDetail.imgUrl,
      //       acceptQuantity:asnDetail.exQuantity,
      //       type:asnDetail.exType,
      //       reason:asnDetail.exReason,
      //       memo:asnDetail.exMemo,
      //       shipBillNo:this.shipInfo.shipBillNo,
      //       billNo:this.asnMaster.billNo,
      //       billDetailNo:asnDetail.asnDetailNo
      //     };

      //     IOT.showOverlay('保存中，请稍等...');
      //     IOT.getServerData('/accept/exs/add', 'post', exData, (ret) => { 
      //       IOT.hideOverlay();
      //         if (ret.code === 200) {  
      //         } else {
      //           IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
      //         }
      //       },true)
      // },
      
      shipAccept(){
          IOT.getServerData('/asn/ship/details/accept', 'get', this.shipInfo, (ret) => { 
              if (ret.code === 200) { 
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            })
      },

      itemWieht(index){
         
       this.searchData.weightTableIndex = index;
        
      },
      getBatchInfo(batchTacticCode,index) {
         IOT.getServerData('/batch/tactics/findByBatchTacticCode', 'get', {
          batchTacticCode:batchTacticCode
        }, (ret) => { 
          if (ret.code === 200) {
             
            let list2 = ret.data;
            this.asnDetails[index].batchInfo = list2; 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true)
      },
       
      input0Set(e){
        if(e.currentTarget.value=='0'){
        e.currentTarget.ariaValueNow='';
        e.currentTarget.ariaValueText='';
        e.currentTarget.value='';
        }
       

            
      },
     
      renderHeader(cerateElement, { column }) {
        return cerateElement("div", [
          cerateElement("span", {
            domProps: {
              innerHTML: "*"
            },
            style: {
              width:"10px",
              height:"5px",
              display:"inline-block",
              vertical_align:"middle",
              text_align:"center",
              color:"#f00",
            }
          }),
          cerateElement("span", column.label),
        ]);
      },
      changePackDetailId(index){  
        var that = this;
          let detail = PF.JSON(this.asnDetails[index]);
          detail.packDetails.forEach(packDetail => {
            if(detail.toPackDetailId == packDetail.packDetailId){
                 that.asnDetails[index].toPackDescribe =packDetail.packDescribe;
                  that.asnDetails[index].toTransRatio =packDetail.transRatio;  
                  that.asnDetails[index].toPackDetailId =packDetail.packDetailId;  
            }
          });
         
      
      },
      handleSelectionChange(val) {
         
        this.multipleSelection = val;
        this.itemWieht(val.index);
      },
      handleCurrentChange(val){
        this.searchData.pageNum = val;
        this.getExData();
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getExData();
      },
      clearSearchData() {
        this.searchData= {
          weightTableIndex:null,
          pageNum: 1,
          pageSize: 20,
          total: 0,
          keyWords: '',
          reason:'',
          type:'',
          billNo:''
          
        };
      },
        setPageNumFlush(){
            this.searchData.pageNum =1;
            this.getExData();
        },
       getExData() {
         let that = this;
         that.searchData.weightTableIndex=null;
         that.shipInfo.weight='';
         this.asnDetails = [];
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData); 
         
        IOT.getServerData('/accept/exs/list', 'get', that.searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            
        
         
            let list = ret.rows; 
            // if(list.length==0){
            //    this.$alert('系统中无此快递单号，请联系采购员进行处理', '提示', {
            //     confirmButtonText: '确定',
            //     callback: null
            //   });
            //   return;
            // }
            let billNos = [];
            let spans = [];
            let rows = [];
            for (let i = 0; i < list.length; i++) {
              let index = billNos.indexOf(list[i].billNo);
               
 

               
              list[i].toQuantity = list[i].orderQuantity-list[i].quantity-list[i].inQuantity ;
              list[i].printQuantity =list[i].orderQuantity-list[i]-list[i].inQuantity ;

              
              list[i].toPackDescribe = list[i].packDescribe;
              list[i].toTransRatio =  list[i].transRatio;
              list[i].toPackDetailId =list[i].packDetailId;
              
              list[i].billType = 2;
              list[i].toBoxCode = '';
              list[i].toCellCode = ''; 
              list[i].returnQuantity = 0; 
              list[i].index = i; 
              list[i].batchInfo={};
              list[i].batchInfo.batchTacticDetails=[];
              list[i].weight = null;
              list[i].asnDetailNo = list[i].detailNo;
              list[i].asnBillNo = list[i].billNo;
              list[i].fileList = [];
              list[i].imgUrls = []
              // async:{
              //   that.getImgs(list[i].acceptExId,i);
              // } 
            }
            
            this.asnDetails = list;
            this.searchData.total = ret.total ;
            this.searchData.pageNum = ret.pageNumber;

             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      // getImgs(exId,i){
      //     IOT.getServerData('/accept/ex/imgs/findByAcceptExId', 'get', {acceptExId:exId}, (ret) => {
      //    this.asnDetails[i].imgUrls = ret.data;
      //     if (ret.code === 200) {

      //     } else {
      //       IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
      //     }
      //   },true);
      // },
       getAsnMasterData() {
        var that = this;
       that.asnMaster = {};
      
        IOT.getServerData('/asn/masters/findByBillNo', 'get', {billNo:that.shipInfo.shipBillCode}, (ret) => {
        
          if (ret.code === 200) {
            if(ret.data==null){
                that.asnMaster = [];
                 that.shipInfo.weight ='';
                that.shipInfo.length ='';
                that.shipInfo.width ='';
                that.shipInfo.height ='';
                that.shipInfo.acceptTime =null;
                that.shipInfo.createTime =null;
            }else{
                 that.asnMaster = ret.data; 
                 that.shipInfo.weight =ret.data.weight;
                 that.shipInfo.length =ret.data.length;
                 that.shipInfo.width =ret.data.width;
                 that.shipInfo.height =ret.data.height;
                 that.shipInfo.acceptTime =ret.data.acceptTime;
            }
            
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },

      ToggleConnectionClicked() { 
         var that = this;         
                try {
                    this.ws = new WebSocket("ws://127.0.0.1:4649");        
                    
                   
                     this.ws.onmessage = function (evt) {
                       that.getData(evt.data);
                         
                      };

                      // when the connection is established, this method is called
                      this.ws.onopen = function () {
                        that.chengzhong = true;
                          
                      };

                      // when the connection is closed, this method is called
                      this.ws.onclose = function () {
                        that.chengzhong = false;
                          
                      }
                } catch (ex) {
                   
                    alert(ex.message);      
                }
                //document.getElementById("ToggleConnection").innerHTML = "断开";
        },

        SendData() {
            try{
                this.ws.send("hello");
            }catch(ex){
                alert(ex.message);
            }
        },

          close() {
            try{
                this.ws.close();
                this.ws=null;
            }catch(ex){
                alert(ex.message);
            }
        },

        getData( data){
               this.shipInfo.shipBillCode = data;
              this.getAsnBillDetailData();
        },

        seestate(){
            alert(this.ws.readyState);
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

                     if(e.key=="Enter"){ 
                       var list = code.split('.');
                       if(list.length==2){
                          var index = that.searchData.weightTableIndex;
                          if(index!=null ){ 
                              that.asnDetails[index].weight=code;
                          }else if(that.shipInfo.acceptTime==null  ){
                              that.shipInfo.weight = code;
                          }
                          
                           lastCode = null;
                          lastTime = null;
                       }else{
                          
                          that.shipInfo.shipBillCode = code;
                          that.searchData.shipBillCode = code;
                          async:{
                              that.getExData();
                              that.getAsnMasterData();
                             
                          }
                           lastCode = null;
                          lastTime = null;
                          
                       }
                        
                    }
                    else if(lastCode == null && lastTime == null){
                        code = "";
                        code += e.key;
                        lastCode = nextCode;
                        lastTime = nextTime;
                       
                    }
                    else if(lastCode != null && lastTime != null && nextTime - lastTime > 500){
                        code = "";
                        lastCode = nextCode;
                        lastTime = nextTime;
                       
                    }
                    else if (lastCode != null && lastTime != null && nextTime - lastTime <= 500) {// 扫码枪
                        code += e.key;
                        lastCode = nextCode;
                        lastTime = nextTime;
                    }  
                    

                    
                } 
        }
      
    },
    created() {
      this.getAcceptCell();
      PF.getOrganizations((rows) => {
          this.organizations = rows;
        });
      this.getExData();
      this.getCode();
   
      
      
    
    },
    mounted() {
    }
  }

  
</script>

<style lang="less">
  .exAccept{
    .type{
      .el-input{
        width:190px !important;
      }
    }
  }
</style>

  
