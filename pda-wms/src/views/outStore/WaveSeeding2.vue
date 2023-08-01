<template>
    <div class="waveSeeding">
        <FeaturesHeader :header-title="headerTitle"></FeaturesHeader>

 
 <el-input v-model="seedingWallCode" v-if="!isStart"  placeholder="播种墙编码"  style="width: 100%;"></el-input>
  <el-input v-model="itemCode" v-else  placeholder="SKU/UPC"  style="width: 100%;"></el-input>
   <el-button @click="startSeeding"  v-if="!isStart" style="width: 100%;"  type="primary">确定</el-button>
      <el-button @click="getBillNo"  v-else style="width:100%" type="primary">确定</el-button>
        <div style="text-align: left;">
            <el-form class="noMargin BoxingPickTaskBox" :model="form" ref="form">

 <el-form-item label="容器号"  >
                    <el-input v-model="boxCode" size="mini" style="width: 80%;"></el-input>
                </el-form-item>
           
            </el-form>
        </div>
        <el-tabs
        v-loading="loading"
         v-model="activeName" >
            <el-tab-pane label="播种信息" name="first">
                <div class="tableBox" style="height: 500px; overflow: auto">
                                <el-table
                                        
                                        highlight-current-row
                                        :data="details"
                                        style="width: 100%"> 
                                    <el-table-column
                                            prop="seedingCellCode"
                                            min-width="80"
                                            label="播种位">
                                            <template slot-scope="scope">
                                                   <span  style=" font-size:15px">{{scope.row.seedingCellCode}}</span>
                                                </template>
                                            </el-table-column>
                                     <el-table-column
                                            min-width="80"
                                            prop="orderQuantity"
                                            label="数量">
                                        <template slot-scope="scope">
                                            <span v-if="scope.row.orderQuantity==scope.row.checkQuantity" style="color:#67C23A;font-size:15px">{{scope.row.checkQuantity}} </span> 
                                            <span v-else-if="scope.row.orderQuantity>scope.row.checkQuantity" style="color:#E6A23C;font-size:15px">{{scope.row.checkQuantity}} </span>
                                             /
                                           <span style="color:#409EFF;font-size:15px">{{scope.row.orderQuantity}} </span>
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
            </el-tab-pane>
            <el-tab-pane label="订单信息" name="second">
                    <div class="tableBox" style="height: 500px; overflow: auto">
                                <el-table
                                :row-class-name="tableRowClassName"
                                        v-loading="loading"
                                        highlight-current-row
                                        :data="cells"
                                        @row-click ="showDetails"
                                        style="width: 100%"> 
                                       
                                    <el-table-column
                                            prop="billNo"
                                            min-width="160"
                                            label="播种位/订单号/状态">
                                            
                                            <template slot-scope="scope">
                                                 
                                                <span   >{{scope.row.seedingCellCode}}</span>
                                                <br/>
                                                   <span   >{{scope.row.billNo}}</span>
                                                </template>
                                            </el-table-column>
                                             <el-table-column
                                            show-overflow-tooltip
                                            prop="soState"
                                            label="状态"
                                            min-width="80">
                                            <template slot-scope="scope"> 
                                                <span v-if="scope.row.soState==1" style="color:#909399">待处理</span>
                                                <span v-if="scope.row.soState==3" style="color:#F56C6C">缺货</span>
                                                <span v-if="scope.row.soState==4" style="color:#409EFF">已分配</span>
                                                <span v-if="scope.row.soState==-1" style="color:#909399">取消</span>
                                                <span v-if="scope.row.soState==6" style="color:#543044">拣货完成</span>
                                                <span v-if="scope.row.soState==7" style="color:#E6A23C">已复核</span>
                                                <span v-if="scope.row.soState==8" style="color:#67C23A">已发运</span>
                                            </template>
                                            </el-table-column>
                                     
                                </el-table>
                            </div>

            </el-tab-pane>
             
        </el-tabs>
        <el-card class="box-card">
                            
                            
                        </el-card> 

                        <el-dialog title="订单信息" :visible.sync="soDetailsVisible">
        <el-table
            v-if="soDetails.length>0"
            highlight-current-row
           
            :data="soDetails"
            style="width: 100%">
            
        
        
            <el-table-column
             
              prop="itemName"
              label="物料名称"
              min-width="300">
              <template slot-scope="scope">
                <el-row :gutter="24">
              <el-col :span="6"> 
                <div style="display: inline-block;   width:50px;height:50px">
                  <img :src="scope.row.imgUrl"  style="width:50px;height:50px">
                </div>
               </el-col>
              <el-col :span="16">
                <div @click="$parent.showPublicModal(scope.row.itemCode, 'item')" style="display: inline-block;margin-left:8px; height:50px">
                    <span class="textBtn"  >{{scope.row.itemCode}}</span>
                      
                  
                     <div >
                      <span style="font-size:15px;color:#E6A23C">{{ scope.row.checkQuantity  }}</span>
                        <span style="font-size:15px;margin-left:20px;margin-right:20px">/</span>
                        <span style="font-size:15px;color:#409EFF">{{ scope.row.orderQuantity   }}</span>

                  </div>
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
    </div>

    

</template>

<script>
    import FeaturesHeader from "../../components/FeaturesHeader";

    export default {
        name: "WaveSeeding",
        components: {FeaturesHeader},
        data() {
            return {
                activeName:"first",
                headerTitle: "波次播种",
                formLabelWidth: 200,
                form: {
                    waveCode: '',
                    itemCode: '',
                    itemName: '',
                    quantity: null
                },
                wareId:localStorage.getItem("wareId"),
                seedingWallCode:'',
                boxCode:this.$route.params.boxCode,
                details:[],
                itemCode:'',
                loading:false,
                seedingWall:'',
                cells:[],
                waveMaster:'',
                isStart:false,
                soDetails :[],
               soDetailsVisible :false,
            }
        },
        methods: {
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
            showDetails( row,column,aaa){
                if(row.billNo==null || row.billNo ==""){
                    return;
                }
                this.soMasterId = row.soMasterId;
                  IOT.getServerData('/so/details/list', 'get', {billNo:row.billNo}, (ret) => {
                    if (ret.code === 200) {
                        this.soDetails = ret.rows;
                        this.soDetailsVisible = true;

                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                    });
            },
            tableRowClassName({row, rowIndex}) {
                if (row.state === 6) {
                    return 'warning-row';
                }
                else if (row.state === 6 === 7) {
                    return 'success-row';
                }
                    return '';
                
            },
            startSeeding(){
                var that = this;
                that.activeName="second";
                if(that.boxCode==null || that.boxCode.trim()==""){
                         this.$router.push({name: 'waveSeeding',params: {}})
                }
                var  boxCode =     that.boxCode.trim();
                var  seedingWallCode =     that.seedingWallCode.trim(); 
                this.loading = true; 
                IOT.getServerData('/wave/masters/startSeeding', 'get', {boxCode:boxCode,seedingWallCode:seedingWallCode,wareId:this.wareId}, (ret) => {
                    this.loading = false
                    if (ret.code === 200) {
                         that.isStart  =true;
                        that.seedingWall = ret.data.seedingWall;
                        var cells = ret.data.seedingCells;
                        that.waveMaster = ret.data.waveMaster;
                        for(var i=cells.length-1;i>=0;i--){
                            //cells[i].state=6;
                            if(cells[i].soState==null){ 
                                cells.splice(i,1); 
                            }
                        }
                        that.cells = cells;
                          
                        
                    } 
                    else if(ret.code==100069) {
                        IOT.tips(ret.message || 'server error', 'error');
                        this.$router.push({name: 'waveSeeding',params: {}})
                    }
                    else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)

            },

            check(detail,index){ 
                var that = this;
                 IOT.getServerData('/so/details/check', 'get', {soDetailId:detail.soDetailId}, (ret) => {
                    this.loading = false
                    if (ret.code === 200) { 
                        this.details.splice(index,1); 
                        var state  =ret.data;
                        for(var i=0;i<that.cells.length;i++){
                            
                            if(that.cells[i].billNo== detail.billNo){ 
                                that.cells[i].soState=state;
                            }
                        }

                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)
            },

            getBillNo( ){
               var  itemCode = this.itemCode.trim();
                var that = this; 
                 
                if(itemCode==that.itemCode && that.details.length>0){
                    
                }
                that.itemCode  =itemCode;
                
                itemCode = itemCode.trim();
                IOT.getServerData('/so/masters/getSeedingByItemCode2', 'get', {itemCode:itemCode,wareId:this.wareId,boxCode:this.boxCode}, (ret) => { 
                    
                    that.itemCode='';
                    if (ret.code === 200) {  
                        //   for(var i=0;i<that.cells.length;i++){
                        //             that.cells[i].isNOW = "#FFFFFF";
                        //   }
                        
                          that.details  =ret.data.list;
                           that.activeName="first";
                           
                            if(ret.data.list.length==0){
                                    this.$parent.playMusic("wms未匹配到订单");
                                    this.$notify({
                                        title: '警告',
                                        message: '无该商品的订单（'+itemCode+'）',
                                        type: 'warning'
                                        }); 
                                        return ; 
                            }else{

                                    // 
                                    // for(var i=0;i<that.details.length;i++){
                                    //     if(i==0){
                                    //         that.details[i].checkQuantity+=1;
                                    //          this.nowBill2(that.details[i]);
                                    //         this.check2(that.details[i]);
                                    //     }else{
                                    //          this.nowBill(that.details[i]);
                                    //     }
                                       
                                    // }
                            }  
                    }
                    else if(ret.code==100069) {
                        IOT.tips(ret.message || 'server error', 'error');
                        this.$router.push({name: 'waveSeeding',params: {}})
                    }
                    else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)  
            },

        },
        created() {
        },
        mounted() {
        }
    }
</script>

<style scoped>

.el-table .warning-row {
    background: #543044;
  }

  .el-table .success-row {
    background: #E6A23C;
  }

</style>