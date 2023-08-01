<template>

    <div class="Seeding">  
        <div id="inside">
             <h1>一单多品播种工位</h1>
             
        
            
            <div class=" ">
                <el-card class="box-card">
                            <el-form :inline="true" class="clearFloat" ref="form"> 
                                 <!-- <el-form-item label="扫描模式：" label-width="formLabelWidth"  > 
                                                <el-radio-group v-model="scanType">
                                                    <el-radio
                                                    v-for="item in scanTypes"
                                                    :key="item.value"
                                                    :label="item.value"
                                                     >{{item.label}}
                                                    </el-radio>  
                                                </el-radio-group>  
                                </el-form-item> -->
                                
                                
                                 
                                 <el-form-item style="margin-left:20px" label="CODE：" label-width="formLabelWidth"  >
                                        <el-input v-model="code"  s placeholder="code" clearable></el-input> 
                                </el-form-item>
                                <el-form-item  style=" "  label="波次/容器" label-width="formLabelWidth"    >
                                        <el-input :disabled="isStart" v-model="boxCode"  style="width:100px" s placeholder="code" clearable></el-input> 
                                </el-form-item> 
                                <el-form-item  style=" "  label="播种墙" label-width="formLabelWidth"    >
                                        <el-input  :disabled="isStart" v-model="seedingWallCode"  style="width:100px" s placeholder="code" clearable></el-input> 
                                </el-form-item> 
                                <el-form-item>
                                    <el-button  v-if="!isStart" type="primary" @click="startSeeding">确定</el-button>  
                                    <el-button v-if="isStart" type="primary" @click="startSeeding">结束</el-button>  
                                </el-form-item>
                                <el-form-item    > 
                                    <el-button v-if="waveMaster.waveId!=null" @click="getPickTaskReturns();exTableVisible=true" type="danger">异常上架</el-button>
                                </el-form-item   >
                        
                                
                              
                            </el-form>
                        </el-card>
                <div class="div-inline1 leftFloat"> 
                    
                        <el-card class="box-card">
                            <h2 style="text-align: center;margin-bottom: 20px;">播种</h2>
                            <div class="tableBox" style="height: 500px; overflow: auto">
                                <el-table
                                        v-loading="loading"
                                        highlight-current-row
                                        :data="details"
                                        style="width: 100%"> 
                                    <el-table-column
                                            prop="billNo"
                                            min-width="80"
                                            label="格子">
                                            <template slot-scope="scope">
                                                   <span  style=" font-size:30px">{{scope.row.x}}-{{scope.row.y}}</span>
                                                </template>
                                            </el-table-column>
                                     <el-table-column
                                            min-width="80"
                                            prop="itemCode"
                                            label="数量">
                                        <template slot-scope="scope">
                                            <span v-if="scope.row.orderQuantity==scope.row.checkQuantity" style="color:#67C23A;font-size:30px">{{scope.row.checkQuantity}} </span> 
                                            <span v-else-if="scope.row.orderQuantity>scope.row.checkQuantity" style="color:#E6A23C;font-size:30px">{{scope.row.checkQuantity}} </span>
                                             /
                                           <span style="color:#409EFF;font-size:30px">{{scope.row.orderQuantity}} </span>
                                        </template>
                                    </el-table-column> 
                                     <el-table-column
                                            min-width="100"
                                            prop="itemCode"
                                            label="操作">
                                        <template slot-scope="scope">
                                              <el-button v-if="scope.row.orderQuantity>scope.row.checkQuantity" @click="check(scope.row,scope.$index)" type="primary">确定</el-button>
                                        </template>
                                    </el-table-column> 
                                </el-table>
                            </div>
                        </el-card> 
                </div>

                <div class="div-inline-right rightFloat">
                    
                        <div class="inventoryListBox">
                            <el-card class="box-card">
                            <!-- <h2 style="text-align: center;margin-bottom: 20px;">
                                播种墙
                                <el-select
                                        v-model="seedingWallIndex"
                                        @change="changeSeedingWall"
                                >
                                    <el-option
                                            v-for="(item,index) in seedingWalls"
                                            :key="index"
                                            :label="item.code"
                                            :value="index"
                                    >  
                                    </el-option>
                                </el-select>
                            </h2> -->
                            <div     style=" padding-top:10px;padding-left:10px;border-radius:15px">  
                                    <div v-for="cell in  cells" :key="cell.seedingCellId" :style="' width:'+92/seedingWall.columnCount+'%;height:'+700/seedingWall.rowCount +'px;position: relative;display:inline-block;'"  >
                                        <div  @click="showDetails(cell.billNo,cell.soMasterId)" :style="'background-color:'+cell.isNOW+'; width:90%;height:90%;border: 3px solid   #409EFF;border-radius:5px;text-align:center; '">
                                            <div    :style="' font-size: ' + 60/seedingWall.rowCount + 'px; margin-top:  '+ 80/seedingWall.rowCount+'px;  color: red;'">
                                                    <span style="color:#ffffff" v-if="cell.billNo=='' ||cell.billNo==null "> 000</span>
                                                    <span v-else>{{cell.billNo}}</span>
                                            </div>
                                            
                                            <div   :style="'    margin-top:'+ 30/seedingWall.rowCount+'px; font-size:'+ 100/seedingWall.rowCount+'px; color: #409EFF'">{{cell.x}}-{{cell.y}} </div>

                                             <!-- <div  :style="'     margin-top: '+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #909399'">篮子</div> -->
                                            <!-- <div  v-if="cell.billNo=='' ||cell.billNo==null "  :style="'     margin-top: 13%; font-size:'+ 100/seedingWall.columnCount+'px; color: #ffffff'">空闲</div> -->

                                            <div  v-if="cell.soState==6 "   :style="'     margin-top: '+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #E6A23C'">拣货完成</div>
                                            <div   v-if="cell.soState==7 "  :style="'     margin-top: '+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #67C23A'">已复核 </div>
                                             <div   v-if="cell.soState==8 "  :style="'     margin-top:'+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #909399'">已发运</div>
                                             <div   v-if="cell.soState==-1 "  :style="'     margin-top:'+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #909399'">已取消</div>
                                            <div  v-if="cell.billNo=='' ||cell.billNo==null "  :style="'     margin-top: '+ 30/seedingWall.rowCount+'px; font-size:'+ 80/seedingWall.rowCount+'px; color: #ffffff'">空闲</div>
                                        </div>
                                    </div>
                                
                            </div>
 
                            
                            </el-card>
                        </div>
                    
                    
                </div>

            </div>
        </div>

        
        <el-dialog title="订单信息" :visible.sync="soDetailsVisible">
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
         
            <!-- <el-table-column
              show-overflow-tooltip
              label="状态"
              width="180">
              <template slot-scope="scope"> 
                 <el-tag v-if="scope.row.orderQuantity === scope.row.deliveryQuantity" type="success">全部发货</el-tag>
                 <el-tag v-else-if="scope.row.orderQuantity === scope.row.pickQuantity" >全部拣货</el-tag>
                 <el-tag v-if="scope.row.orderQuantity > scope.row.pickQuantity" type="info">部分拣货</el-tag> 
              </template>
            </el-table-column>
            -->

            <el-table-column
              show-overflow-tooltip
              prop="orderQuantity"
              label=" "
              min-width="150">
              <template slot-scope="scope">
                  <div style=" height:86px;line-height:86px">
                      <span style="font-size:40px;color:#E6A23C">{{ scope.row.checkQuantity  }}</span>
                        <span style="font-size:40px;margin-left:20px;margin-right:20px">/</span>
                        <span style="font-size:40px;color:#409EFF">{{ scope.row.orderQuantity   }}</span>

                  </div>
                
              </template>
            </el-table-column>
          
            <!-- <el-table-column
              show-overflow-tooltip
              prop="checkQuantity"
              label="播种数量"
              min-width="150">
              <template slot-scope="scope">
                <span>{{ scope.row.checkQuantity   }}</span>
              </template>
            </el-table-column> -->
           
          
     </el-table>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="soDetailsVisible = false">取 消</el-button> -->
        <el-button type="primary" @click="cancelSo">取消订单</el-button>
    </span>
        </el-dialog>

        <el-dialog title="还货任务" :visible.sync="exTableVisible">
      <el-dialog
        width="30%"
        title="内层 Dialog"
        :visible.sync="addExTableVisible"
        append-to-body>
         <el-form :model="addReturn">
          <el-form-item label="SKU"  >
            <el-input v-model="addReturn.itemCode" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="数量"  >
            <el-input v-model="addReturn.quantity" autocomplete="off"></el-input>
          </el-form-item>
          
          
        </el-form>
         <span slot="footer" class="dialog-footer"> 
          <el-button type="primary" @click="addReturnTask">确 定</el-button>
        </span>
      </el-dialog>
      
        <el-button  type="primary" @click="addExTableVisible = true">新增</el-button>
      
      <el-table style="margin-top:10px" :data="pickTaskReturns">
        <el-table-column property="itemCode" label="SKU" width="150"></el-table-column>
        <el-table-column property="itemName" label="名字" width="200"></el-table-column>
        <el-table-column property="quantity" label="数量"></el-table-column>
        <el-table-column property="toCellCode" label="货位"></el-table-column>
        <el-table-column property="type" label="类型"></el-table-column>
      </el-table>
       
    </el-dialog>

     

    </div>
</template>

<script>
    function myFunction() {
        alert("fadf");
    }
    import pako from 'pako';
    import {mapState} from "vuex";

    export default {
        name: 'seeding',
        data() {
            return {
                pickTaskReturns:[], 
                addReturn:{},
                addExTableVisible:false,
                exTableVisible:false,
                itemCode:'',
                soMasterId:'',
                soDetailsVisible:false,
                soDetails:[],
                isStart:false,
                boxCode:'',
                seedingWallCode:'',
                waveId:'',
                details:[
                    
                ],
                timer:'',
                code:'',
                scanTypes:[
                    {
                        label:"扫描商品",
                        value:0,
                    },
                    {
                        label:"打印面单",
                        value:1,
                    }
                    ,
                    {
                        label:"清除位置",
                        value:2,
                    }
                ],
                scanType:0,
                bill:{},
                seedingWallIndex:'',
                wareId:PF.getLocal('wareId', 'number'),
                cellIndex:0,
                itemCode:'',
                quantity:0,
                billNo:'',
                htmlStr:'',
                formLabelWidth: '400px',
                formData: {
                    waveCode: '',
                    seedingWallNo: '',
                    scanData: '',
                    itemCode: '',
                    inventoryId: ''
                },
                dialogHoleInfoVisible: false,
                dialogSoSelVisible: false,
                dialogQuantityVisible: false,
                holeInfo: {
                    holeId: '',
                    soNo: ''
                },
                soBills: [],
                seedingWalls: [],
                seedingWall: {
                   
                },
                waveMaster:{},
                soMasters: [],
                loading: false,
                cells: [],
                updateInfo: {
                    soNo: '',
                    itemCode: '',
                    quantity: null,
                    inventoryId: null
                }

            }
        },
        computed: {
            ...mapState([]),
            isCollapse() {
                return this.$parent.leftMenuIsCollapse;
            },
             
            seedingCell: function (x,y) { 
                    this.cells.forEach(cell => {
                        if(cell.x==x && cell.y==y){
                        return cell;
                        }
                    });
            },
     
        },
        
        methods: {
            addReturnTask(){
                var that = this; 
                var param  ={
                    waveMasterId:that.waveMaster.waveId,
                    boxCode:that.boxCode,
                    wareId:that.wareId,
                    itemCode:that.addReturn.itemCode,
                    quantity:that.addReturn.quantity
                };
                 IOT.showOverlay('保存中，请稍等...');
                //Integer waveMasterId,String boxCode,String itemCode,Integer wareId,Double quantity,
                IOT.getServerData('/pick/task/returns/create', 'get', param, (ret) => {
                    IOT.hideOverlay();
                if (ret.code === 200) { 
                    that.addReturn = {};
                    that.getPickTaskReturns();
                    that.addExTableVisible = false;
                       // that.pickTaskReturns= ret.data;  
                    
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
                },true);

            },
            getPickTaskReturns(){
                var that = this; 
                IOT.getServerData('/pick/task/returns/getByBox', 'get', {boxCode:that.boxCode,wareId:that.wareId}, (ret) => {
                if (ret.code === 200) { 
                        that.pickTaskReturns= ret.data;  
                    
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
                },true);
            },
            cancelSo(){
                //Integer waveMasterId, Integer soMasterId, Integer wareId
                var param = {
                    wareId:this.wareId,
                    waveMasterId:this.waveMaster.waveId,
                    soMasterId:this.soMasterId
                };
                 IOT.getServerData('/pick/task/returns/cancelSo', 'get', param, (ret) => {
                    this.loading = false;
                    this.soDetailsVisible = false
                    if (ret.code === 200) {
                        for(var i=0;i<this.cells.length;i++){
                            if(this.cells[i].soMasterId==this.soMasterId){
                                    this.cells[i].soState=-1;
                                    return;
                             }
                        }
                       
                    } else {
                        this.loading = false
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })

            },
             
            check(detail,index){ 
                 IOT.getServerData('/so/details/check', 'get', {soDetailId:detail.soDetailId}, (ret) => {
                    this.loading = false
                    if (ret.code === 200) { 
                        this.details.splice(index,1);
                        this.clearNowBill(detail,ret.data);
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)
            },
             check2(detail){ 
                 IOT.getServerData('/so/details/check2', 'get', {soDetailId:detail.soDetailId,quantity:detail.checkQuantity}, (ret) => {
                    // this.loading = false
                    if (ret.code === 200) { 
                       
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)
            },
            startSeeding(){
                var that = this;
                var  boxCode =     that.boxCode.trim();
                var  seedingWallCode =     that.seedingWallCode.trim(); 
                this.loading = true; 
                IOT.getServerData('/wave/masters/startSeeding', 'get', {boxCode:boxCode,seedingWallCode:seedingWallCode,wareId:this.wareId}, (ret) => {
                    this.loading = false
                    if (ret.code === 200) {
                         that.isStart  =true;
                        that.seedingWall = ret.data.seedingWall;
                        that.cells = ret.data.seedingCells;
                        that.waveMaster = ret.data.waveMaster;
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)

            },
     
         
            
            showDetails( billNo,soMasterId){
                if(billNo==null || billNo ==""){
                    return;
                }
                this.soMasterId = soMasterId;
                  IOT.getServerData('/so/details/list', 'get', {billNo:billNo}, (ret) => {
                    if (ret.code === 200) {
                        this.soDetails = ret.rows;
                        this.soDetailsVisible = true;

                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                    });
            },
            
            printDzmd(billNo){
               var that = this; 
                IOT.getServerData('/so/masters/getDzmd', 'get', {billNo:billNo}, (ret) => {
                if (ret.code === 200) { 
                        var strHTML= ret.data; 
                        that.$parent.printHtml(strHTML);  
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
                },true);
            },
        

            getBillNo(itemCode){
                var itemCode = itemCode.trim();
                var that = this; 
                that.code='';
                if(itemCode==that.itemCode && that.details.length>0){
                    
                }
                that.itemCode  =itemCode;
                itemCode = itemCode.trim();
                IOT.getServerData('/so/masters/getSeedingByItemCode2', 'get', {itemCode:itemCode,wareId:this.wareId,boxCode:this.boxCode}, (ret) => { 
                    that.code = '';
                    if (ret.code === 200) {  
                          for(var i=0;i<that.cells.length;i++){
                                    that.cells[i].isNOW = "#FFFFFF";
                          }
                          that.details  =ret.data.list;
                            if(ret.data.list.length==0){
                                    this.$parent.playMusic("wms未匹配到订单");
                                    this.$notify({
                                        title: '警告',
                                        message: '无该商品的订单（'+itemCode+'）',
                                        type: 'warning'
                                        }); 
                                        return ; 
                            }else{

                                    for(var i=0;i<that.details.length;i++){
                                        if(i==0){
                                            that.details[i].checkQuantity+=1;
                                             this.nowBill2(that.details[i]);
                                            this.check2(that.details[i]);
                                        }else{
                                             this.nowBill(that.details[i]);
                                        }
                                       
                                    }
                            }  
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)  
            }, 
           
           nowBill(bill){
               var that = this; 
               for(var i=0;i<that.cells.length;i++){ 
                                   if(that.cells[i].billNo !=null && that.cells[i].billNo!= '' && that.cells[i].billNo == bill.billNo){
                                   that.cells[i].isNOW = "#E6A23C";
                                   bill.x = that.cells[i].x;
                                   bill.y = that.cells[i].y; 
                    }
               }
                
               
           },
           nowBill2(bill){
               var that = this; 
               for(var i=0;i<that.cells.length;i++){ 
                                   if(that.cells[i].billNo !=null && that.cells[i].billNo!= '' && that.cells[i].billNo == bill.billNo){
                                   that.cells[i].isNOW = "#409EFF";
                                   bill.x = that.cells[i].x;
                                   bill.y = that.cells[i].y; 
                    }
               }
                
               
           },
           clearNowBill(bill,soState){
               var that = this;
                
               for(var i=0;i<that.cells.length;i++){ 
                        if(that.cells[i].billNo !=null && that.cells[i].billNo!= '' && that.cells[i].billNo == bill.billNo){
                        that.cells[i].isNOW = "#FFFFFF";   
                        that.cells[i].soState =soState;       
                    }
               }
                
               
           },

            scanItemCode(itemCode){
                var that = this; 
               // that.quantity+=1;
                if(that.scanType==0){
                    that.getBillNo(itemCode,that.quantity);   
                }
                // else if(that.scanType==1){
                //     that.printDzmd(itemCode) ;
                // }else if(that.scanType==2){
                //     that.downSeeding(itemCode) ;
                // } 
                
            }, 
             downSeeding(code){
                 var that = this;
                IOT.getServerData('/so/masters/downSeeding', 'get', {
                  data:code,
                  wareId:this.wareId
                }, (ret) => { 
                  if (ret.code === 200) {  
                      that.clearCell(code);
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });    
             },
          


        
          

            getCode(){
                var that = this;
                
                document.onkeypress = function (e) { 
                   if(e.key=="Enter"){ 
                        //这里监听正常的键盘回车键
                        that.scanItemCode(that.code); 
                    } 
                } 
            },

        
            
            resetForm(formName) { // 重置
                this.$refs[formName].resetFields();
            },
            asd(){
                    alert("fas");
            },
 
            generateSeedWall2(code, row, column) {
                 
                this.getSeedingCells(code);
              
            },
            getSeedingCells(code){
                var that = this; 
                IOT.getServerData('/seeding/cells/findByWallCode', 'get', {wallCode:code,wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        that.cells = ret.rows;  
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                },false)
            },
            handleCloseHoleInfo() {
                this.dialogHoleInfoVisible = false
            },
            showSoSelModal() {
              //  this.getSoData()
                this.dialogSoSelVisible = true
            },
            handleCloseSoSel() {
                this.dialogSoSelVisible = false
            },
            handleSoSelChange(soNo) {
                this.dialogSoSelVisible = false
                this.holeInfo.soNo = soNo
            },
            handleCloseQuantity() {
                this.dialogQuantityVisible = false
            },
            commitChangeHoleInfo() {
                PF.setLocal(this.holeInfo.holeId, this.holeInfo.soNo)
                this.dialogHoleInfoVisible = false

                this.displayHoleLabel()
            },
            
            getSeedingWallData() {
                IOT.getServerData('/seeding/walls', 'get', {wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        this.seedingWalls = ret.rows
                        if(this.seedingWalls.length>0){
                            this.seedingWallIndex=0;
                            this.changeSeedingWall();
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
            changeSeedingWall() { 
                this.seedingWall = this.seedingWalls[this.seedingWallIndex];
                let row = this.seedingWall.rowCount
                let column = this.seedingWall.columnCount
                this.generateSeedWall2(this.seedingWall.code, row, column)
                
            },
           
      
          
             
            commitSeeding() {
                let that = this
                IOT.tips('保存中~~~')
                IOT.getServerData('/pick/tasks/updateSeededQuantity', 'get', this.updateInfo, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.messages || '保存成功！', 'success', 1000,() => {
                            that.dialogQuantityVisible = false
                            that.doAlert()
                            that.getPickTaskData()
                        })
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
         
         
        },
        created() {
             this.getCode();
           //  this.getSeedingWallData()
        },
        mounted() {
            
        },
        destoryed(){
            document.onkeypress = null;
        },
        mounted() {
      
        },
        beforeDestroy() {
        
        }
    }
</script>

<style scoped lang="less">
    .Seeding {
        .el-card__body {
            padding: 5px 10px 5px 20px;
        }
        .box-card{
            // border-color: #c1deff;
            // border-width: 1mm;
            // margin-bottom:10px;
        }
        padding-bottom: 100px;

        #billin {
            font-size: 30px;
            color: #4499FF;
        }

        .div-inline1 {
            margin-top: 5px;
            width: 25%;
        }

        .div-inline-right {
            width: 70%;
            float: left;
            margin-top: 5px;
            margin-left: 10px; 
        }
        .el-form-item {
            margin-bottom: 2px; 
        }
        .el-card__body {
            padding: 10px 15px 10px 20px;
        }
    }
</style>
