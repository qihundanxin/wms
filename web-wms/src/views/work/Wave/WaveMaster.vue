<template>
    <div class="asnBill">

        <el-dialog title="转移SO到新的波次" :visible.sync="dialogTransVisible" :before-close="handleClose" width="500px"
                   style="margin-top: -10vh;">
            <el-table
                    highlight-current-row
                    @current-change="handleSelChange"
                    :data="WaveMasters"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="index"
                        width="55">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="waveId"
                        label="波次编码"
                >
                </el-table-column>

                <el-table-column
                        fixed="right"
                        label="操作"
                        width="150">
                    <template slot-scope="scope">
                        <el-button @click="transToWaveConfirm(scope.row.waveId)"
                                   type="edit"
                                   plain size="small">转移至此波次
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>
        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getWaveMasterData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>
                 <div>
                    <span>拣货人</span>
                    <el-select v-model="searchData.userId" size="mini" filterable placeholder="拣货人" clearable>
                        <el-option
                        v-for="user in users"
                          :key="user.userId"    :value="user.userId" style="color:#409EFF"  :label="user.userName"  ></el-option>
                    </el-select>
                </div>
                <div>
                    <span>状态</span>
                    <el-select v-model="searchData.state" size="mini" filterable placeholder="状态" clearable>
                        <el-option
                        v-for="state in states"
                          :key="state.value"    :value="state.value" :style="'color:'+state.color"  :label="state.label"  ></el-option>
                    </el-select>
                </div>
                <div>
                    <span>拣货方式</span>
                    <el-select v-model="searchData.isSeeding" size="mini" filterable placeholder="状态" clearable>
                        <el-option
                        v-for="state in isSeedings"
                          :key="state.value"    :value="state.value" :style="'color:'+state.color"  :label="state.label"  ></el-option>
                    </el-select>
                </div>

            </div>

        </div>

        

        <div class="headerBtns">
          

        </div>


        <div  class="tableBox organizationLeftTab" style="width: 30%">
            <el-table
            id="divContainer"
                    ref="waveTable"
                    highlight-current-row  
                    @current-change="handleSelChange"
                    v-loading="loading.parent"
                    :data="WaveMasters"
                    
                    style="width: 100%">
                
                <el-table-column
                        sortable
                        prop="code"
                        label="波次编码"
                >
                <template slot-scope="scope">
                        <el-card class="box-card" style="margin-right: 25px;">
                        <div slot="header" class="clearfix">
                            <span>{{scope.row.waveId}}</span>

                            <div style="float: right;  ">
                                 {{scope.row.createTime.substr(5,14)}} 
                            </div>

                            <div  style="float: right;  " v-if="scope.row.state != null" >
                          
                                <span   style="color:#F56C6C"   v-if="scope.row.state ==0">订单接收中</span>
                                <span   style="color:#8B3A62"  v-if="scope.row.state ==1">待拣货</span>
                                <span   style="color:#8B3A62"  v-if="scope.row.state ==11">拣货中</span>
                                <span  style="color:#543044"  v-if="scope.row.state ==2">拣货完成</span> 
                                <span   style="color:#E6A23C"  v-if="scope.row.state ==3">播种完成</span>
                                <span  style="color:#67C23A"  v-if="scope.row.state ==4">发运完成</span>
                                <span  style="color:#67C23A"  v-if="scope.row.state ==5">结束</span>
                             
                            </div>
                           
                         </div>
                      
                        
                        <!-- <div   v-if="scope.row.createTime != null" >
                       
                           {{scope.row.createTime}} 
                        </div>  -->
                        <div  > 
                        </div> 
                          <div >
                              <span   v-if="scope.row.totalSkuCount != null" >订单总数：{{scope.row.totalBillCount}} </span>
                            <span style="margin-left:15px"  v-if="scope.row.totalSkuCount != null"  >sku总数：{{scope.row.totalSkuCount}}  </span>
                            
                        </div> 
                      
                        <div v-if="scope.row.waveTacticName != null" >
                            波次策略：{{scope.row.waveTacticName}} 
                            <span v-if="scope.row.soStructure ==1">（一单一品）</span>
                            <span v-if="scope.row.soStructure ==2">（ 一单多品）</span>
                            <!-- <span v-if="scope.row.soStructure ==3">（ 一单一品一件）</span>
                            <span v-if="scope.row.soStructure ==4">（一单一品多件）</span> -->
                        </div>
                        <div v-if="scope.row.isSeeding != null" >
                            分拣方式：
                            <span v-if="scope.row.isSeeding ==1">先拣后分</span>
                            <span v-if="scope.row.isSeeding ==0">边拣边分</span>
                           
                        </div>
                         <div v-if="scope.row.carrierName != null" >
                            承运方：{{scope.row.carrierName}}
                        </div>
                         <div v-if="scope.row.customerName != null" >
                            客户：{{scope.row.customerName}}
                        </div> 
                        </el-card>
                    </template>
                </el-table-column> 
            </el-table>
            <el-pagination
                    style="text-align: right;"
                  
                    @current-change="handleCurrentChange"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    @size-change="handleSizeChange"
                    :page-sizes="[15, 30, 50, 100]"
                    layout="sizes,total, prev, pager, next  "
                    :total="searchData.total">
            </el-pagination>
        </div> 
        <div class="organizationMiddle">
          <!--  <i class="el-icon-d-arrow-right"></i> -->
        </div>
        <div  class=" tabs  organizationRight"  style="width: 65%">
            <el-card class="box-card">
          
                        <el-form v-if="WaveData.state == 1" :inline="true"    >
                            <el-form-item label="拣货人：">
                                <el-input v-model="gh" placeholder="拣货人"></el-input>
                            </el-form-item>
                            <el-form-item v-if="WaveData.isSeeding == 1" label="容器号：">
                                 <el-input   v-model="boxCode" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="allotPickUserAndBox(WaveData.waveId,WaveData.isSeeding)">确定</el-button>
                            </el-form-item>
                        </el-form>
                        <el-form v-if="WaveData.state > 1" :inline="true"    >
                            <el-form-item label="拣货人：">
                                <span>{{WaveData.allotUserName}}</span> 
                            </el-form-item>
                            <el-form-item  v-if="WaveData.isSeeding == 1"   label="容器号：">
                                <span>{{WaveData.boxCode}}</span> 
                            </el-form-item>
                            <el-form-item label="播种墙：">
                                <span>{{WaveData.seedingWallCode}}</span> 
                            </el-form-item>
                            
                        </el-form> 
                        <!-- <div   v-if="WaveData.createTime != null">
                            生成时间：{{WaveData.createTime.substr(5,11)}}
                        </div>
                         <div   v-if="WaveData.createTime != null">
                            最长等待时间：{{WaveData.maxWaitTime}}分钟
                        </div> 
                        <div  >
                            <span v-if="WaveData.soStructure ==1">订单结构：一单一品</span>
                            <span v-if="WaveData.soStructure ==2">订单结构：一单多品</span>
                            <span v-if="WaveData.soStructure ==3">订单结构：一单一品一件</span>
                            <span v-if="WaveData.soStructure ==4">订单结构：一单一品多件</span>
                            
                        </div>
                        <div   v-if="WaveData.totalSkuCount != null" >
                            订单总数：{{WaveData.totalBillCount}}/{{WaveData.soCountLimit}}
                        </div>
                        <div  v-if="WaveData.totalSkuCount != null"  >
                            sku总数：{{WaveData.totalSkuCount}}/{{WaveData.itemCount}}
                        </div>
                       
                        <div v-if="WaveData.waveTacticName != null" >
                            波次策略：{{WaveData.waveTacticName}}
                        </div>
                         <div v-if="WaveData.carrierName != null" >
                            承运方：{{WaveData.carrierName}}
                        </div>
                         <div v-if="WaveData.customerName != null" >
                            客户：{{WaveData.customerName}}
                        </div> -->
                       
                        
            </el-card>
            <br/>
             <el-tabs class=" tabs  organizationRight" style="width: 100%" type="border-card" tab-position="top" v-model="activeName">
            <el-tab-pane label="波次信息" name="first">
               <!-- <p class="detailItemBox">
                    <span><b>波次策略编码：</b>{{WaveData.waveTacticCode}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>仓库名称：</b>{{WaveData.wareName}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>组织名称：</b>{{WaveData.organizationName}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>创建时间：</b>{{WaveData.createTime}}</span>
                </p>
                <el-divider content-position="left">详细信息</el-divider> -->
                <div class="headerBtns">

                </div>
                <div class="tableBox">
                    <el-table
                            highlight-current-row
                            v-loading="loading.child"
                            @selection-change="handleWaveDetailSelChange"
                            :data="WaveDetails"
                            style="width: 100%">
                        <!-- <el-table-column
                                type="selection"
                                width="55">
                        </el-table-column> -->
                       <!-- <el-table-column
                                prop="waveMasterCode"
                                label="波次编码"
                        ></el-table-column>
                        <el-table-column
                                prop="soMasterId"
                                label="发运单ID"
                        ></el-table-column> -->
                         <el-table-column
                                fixed="left"
                                type="index"
                                width="50">
                        </el-table-column>
                        <el-table-column
                                prop="billNo"
                                label="单号"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                prop="state"
                                label="状态"
                                min-width="80"> 
                            <template slot-scope="scope">
                                <span  v-if="scope.row.state==1" style="color:#909399">待处理</span> 
                                <span  v-if="scope.row.state==3"  style="color:#F56C6C"  >缺货</span>
                                <span  v-if="scope.row.state==4"  style="color:#8B3A62"  >已分配</span>
                                <span  v-if="scope.row.state==5"  style="color:#E11111"  >部分拣货</span>
                                <span  v-if="scope.row.state==-1"  style="color:#909399"  >取消</span>
                                <span  v-if="scope.row.state==-2"  style="color:#909399"  >关闭</span>
                                <span  v-if="scope.row.state==6"  style="color:#543044"  >拣货完成</span>
                                <span  v-if="scope.row.state==7"  style="color:#E6A23C"  >已复核</span>
                                <span  v-if="scope.row.state==8"  style="color:#67C23A"  >已发运</span> 
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="organizationName"
                                label="货主"
                                width="180">
                            <template slot-scope="scope">
                        <span class="textBtn"
                              @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="customerName"
                                label="客户"
                                width="180">
                            <template slot-scope="scope">
                                <span class="textBtn" @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{scope.row.customerName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="carrierName"
                                label="承运方"
                                width="180">
                        </el-table-column>
                        <!-- <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="deliveryTime"
                                label="要求交货时间"
                                width="180">
                            <template slot-scope="scope">
                                {{scope.row.deliveryTime.substr(0,10)}}
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="expectTimeFm"
                                label="预发时间FM"
                                width="180">
                            <template slot-scope="scope">
                                {{scope.row.expectTimeFm.substr(0,10)}}
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="expectTimeTo"
                                label="预发时间TO"
                                width="180">
                            <template slot-scope="scope">
                                {{scope.row.expectTimeTo.substr(0,10)}}
                            </template>
                        </el-table-column> -->
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="createTime"
                                label="创建时间"
                                width="180">
                            <template slot-scope="scope">
                                {{scope.row.createTime.substr(5,14)}}
                            </template>
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="createUserName"
                                label="创建人"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                sortable
                                show-overflow-tooltip
                                prop="memo"
                                label="备注"
                                width="180">
                        </el-table-column>

                        <!-- <el-table-column
                                fixed="right"
                                label="操作"
                                width="110">
                            <template slot-scope="scope">
                                <el-button @click="isDelete(scope.row.waveDetailId)" icon="iconfont icon-shanchu"
                                           type="danger" circle
                                           plain size="small"></el-button>
                            </template>
                        </el-table-column> -->
                    </el-table>
                </div>
                <div>
                    <!-- <el-button @click="$router.push({name: 'ChooseSo', params: {waveMasterId: WaveData.waveId}})">添加波次明细
                    </el-button>
                    <el-button @click="showTransWaveModal">转移至其它波次
                    </el-button> -->
                </div>
            </el-tab-pane>

            <el-tab-pane label="拣货任务" name="second">
                <div class="tableBox">
                    <el-table
                            highlight-current-row
                            v-loading="loading.child"
                            :data="pickTasks"
                            style="width: 100%">
                        <el-table-column
                                fixed="left"
                                type="index"
                                width="50">
                        </el-table-column>
                       <!-- <el-table-column
                                prop="soDetailId"
                                label="发运单明细ID"
                        ></el-table-column> -->
                        <!-- <el-table-column
                                prop="soMasterId"
                                label="发运单ID"
                        ></el-table-column> -->
                         <!-- <el-table-column
                                prop="billNo"
                                label="单号"
                        ></el-table-column> -->
                        <!-- <el-table-column
                                prop="wareName"
                                label="仓库"
                        ></el-table-column> -->
<!--                        <el-table-column-->
<!--                                prop="organizationName"-->
<!--                                label="货主"-->
<!--                        ></el-table-column>-->
                       <!-- <el-table-column
                                prop="areaName"
                                label="货区"
                        ></el-table-column>
                        <el-table-column
                                prop="shelfName"
                                label="货架"
                        ></el-table-column>-->
                        <el-table-column
                                prop="cellCode"
                                label="货位"
                        ></el-table-column>
                      
                        <el-table-column
                                prop="itemName"
                                label="SKU名称"
                        ></el-table-column>
                        <el-table-column
                                prop="itemCode"
                                label="SKU"
                        ></el-table-column>
<!--                        <el-table-column-->
<!--                                prop="batchName"-->
<!--                                label="批次"-->
<!--                        ></el-table-column>-->
                        <!-- <el-table-column
                                prop="boxCode"
                                label="箱号"
                        ></el-table-column> -->
                        <!-- <el-table-column
                                prop="parentTaskId"
                                label="parentTaskId"
                        ></el-table-column> -->
                     <!--   <el-table-column
                                prop="packDetailId"
                                label="packDetailId"
                        ></el-table-column> -->
                        <!-- <el-table-column
                                prop="packDescribe"
                                label="packDescribe"
                        ></el-table-column> -->
                        <el-table-column
                                prop="pickQuantity"
                                label="拣货数量"
                        ></el-table-column>
<!--                        <el-table-column-->
<!--                                prop="state"-->
<!--                                label="状态"-->
<!--                        ></el-table-column>-->
<!--                        <el-table-column-->
<!--                                prop="inventoryId"-->
<!--                                label="库存ID"-->
<!--                        ></el-table-column>-->
                        <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="state"
                            label="状态">
                            <template slot-scope="scope">
                                <el-tag   v-if="scope.row.state === 5"   type=""   effect="dark">已发运  </el-tag>
                                <el-tag   v-if="scope.row.state === 0"   type=""   effect="success">作业中  </el-tag> 
                                <el-tag   v-if="scope.row.state === 2"   effect="dark" type="success">完成</el-tag>  
                                <el-tag   v-if="scope.row.parentTaskId !=null"   type=""   effect="info">被合并  </el-tag> 
                                <span  v-if="scope.row.state==-1"  style="color:#909399"  >取消</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination
                            style="text-align: right;"
                            @current-change="handleChildCurrentChange"
                            :current-page.sync="pickTaskSearch.pageNum"
                            :page-size="pickTaskSearch.pageSize"
                            @size-change="handleChildSizeChange"
                            :page-sizes="[15, 30, 50, 100]"
                            layout="sizes,total, prev, pager, next, jumper"
                            :total="pickTaskSearch.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
        </el-tabs>
        </div>

       

    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'WaveMaster',
        data() {
            return { 
                boxCode:'',
                gh:'',

                states:[
                    {
                        value:0,
                        label:"接收订单中",
                        color:"#F56C6C"
                    },
                    {
                        value:1,
                        label:"待拣货",
                        color:"#8B3A62"
                    },
                    {
                        value:11,
                        label:"拣货中",
                        color:"#8B3A62"
                    },
                    {
                        value:2,
                        label:"拣货完成",
                        color:"#543044"
                    },
                    {
                        value:3,
                        label:"播种完成",
                        color:"#E6A23C"
                    },
                    {
                        value:4,
                        label:"发运完成",
                        color:"#67C23A"
                    }
                ],
                isSeedings:[
                    {
                        value:0,
                        label:"边拣边分",
                        color:"#F56C6C"
                    },
                    {
                        value:1,
                        label:"先拣后分",
                        color:"#8B3A62"
                    },
                   
                ],
                wareId: PF.getLocal('wareId', 'number'),
                users:[],
                isMoreSearch: false,
                activeName: 'first',
                searchData: {
                    isSeeding:'',
                    userId:'',
                    state:1,
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    waveId: ''
                },
                pickTaskSearch: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    waveId: '',
                    wareId:PF.getLocal('wareId', 'number')
                },

                WaveMasters: [],
                WaveData: {},
                WaveDetails: [],
                pickTasks: [],

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],

                waveSelection: [],
                waveDetailSelection: [],
                newWaveSelection: [],

                waveMasterId: '',
                waveDetailIds: [],

                dialogTransVisible: false,

                waveScheduleState: 0,

                editCronVisible: false,
                cronExpression: '',
                jobId: null,

                loading: {
                    parent: false,
                    child: false
                }
            }
        },
        computed: {
            ...mapState([])
        },
        methods: {
            allotPickUserAndBox(waveId,isSeeding){
                var that = this;
                
                var boxCode = that.boxCode;
                var wareId = that.wareId;
                var gh = that.gh;
                IOT.getServerData('/wave/masters/allotPickUserAndBox', 'get', {
                    waveId:waveId,
                    boxCode:boxCode,
                    wareId:wareId,
                    gh:gh,
                    isSeeding:isSeeding
                }, (ret) => { 
                    if (ret.code === 200) {
                        that.getWaveMasterData ();
                    } else {
                      
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                }); 
            }, 
            getUserName(userId){
                this.users.forEach(user => {
                    if(user.userId == userId){
                        return user.userName;
                    }
                });
            },
            allotWave(waveMaster){
                waveMaster.allotUserName = this.getUserName(waveMaster.allotUserId);
                IOT.getServerData('/wave/masters/update', 'post', waveMaster, (ret) => { 
                    if (ret.code === 200) {
                       
                    } else {
                        waveMaster.allotUserId = null;
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getUsers(){
                IOT.getServerData('/ware/infos/findUserByWareId', 'get', {wareId:this.wareId}, (ret) => { 
                    if (ret.code === 200) {
                        this.users = ret.rows; 
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });

            },

            showEditCron() {
                this.editCronVisible = true
            },
            handleCronClose() {
                this.editCronVisible = false
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getWaveMasterData();
                this.getWaveDetailData()
                this.getPickTaskData()
            },
            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getWaveMasterData()
                this.getWaveDetailData()
                this.getPickTaskData()
            },
            handleChildCurrentChange(val) {
                this.pageNum = val;
                this.getPickTaskData()
            },
            handleChildSizeChange(val){
                this.searchData.pageSize = val;
                this.getPickTaskData();
            },
            handleSelChange(rowData) {
                this.childSearchData.waveId = rowData.waveId
                this.pickTaskSearch.waveId = rowData.waveId
                this.WaveData = JSON.parse(JSON.stringify(rowData))
                this.waveMasterId = rowData.waveId

                this.getWaveDetailData()
                this.getPickTaskData()
            },
            handleNewWaveSelChange() {

            },
            handleWaveDetailSelChange(val) {
                this.waveDetailSelection = val
            },
            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.userId = null;
            },
            getWaveMasterData() {
                this.loading.parent = true;
                this. WaveDetails= [];
                this.pickTasks= [];
                IOT.getServerData('/wave/masters/list', 'get', this.searchData, (ret) => {
                    this.loading.parent = false
                    if (ret.code === 200) {
                        this.WaveMasters = ret.rows;
                        this.WaveData = ret.rows[0] || {}
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                        if (ret.rows.length == 0) {
                            this.loading.parent = false;
                            return 
                        }
                        this.$refs.waveTable.setCurrentRow(ret.rows[0]);
                        this.childSearchData.waveId = ret.rows[0].waveId
                        this.pickTaskSearch.waveId = ret.rows[0].waveId
                        this.getWaveDetailData()
                        this.getPickTaskData()
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getWaveDetailData() {
                this.loading.child = true
                IOT.getServerData('/wave/details', 'get', this.childSearchData, (ret) => {
                    this.loading.child = false
                    if (ret.code === 200) {
                        this.WaveDetails = ret.rows
                    }
                })
            },
            getPickTaskData() {
                this.loading.child = true
                IOT.getServerData('/pick/tasks/findByWaveMasterId', 'get', this.pickTaskSearch, (ret) => {
                    this.loading.child = false
                    if (ret.code === 200) {
                        this.pickTasks = ret.rows
                        this.pickTaskSearch.total = ret.total;
                        this.pickTaskSearch.pageNum = ret.pageNumber
                    }
                })
            },

            deleteWaveConfirm(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteWave(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteWave(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/wave/masters/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getWaveMasterData()
                            that.getWaveDetailData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteWaveDetail(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteWaveDetail(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/wave/details/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getWaveDetailData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            generateWaves() {
                let that = this;
                IOT.showOverlay('生成中，请稍等...');
                IOT.getServerData('/wave/masters/generateWaves', 'get', {wareId: PF.getLocal('wareId', 'number')}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('生成成功！', 'success', 1000, () => {
                            that.getWaveMasterData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            showTransWaveModal() {
                if (this.waveDetailSelection.length <= 0) {
                    IOT.tips('请至少选择一个波次明细！', 'error', 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false
                }
                let list = PF.JSON(this.waveDetailSelection);
                let waveDetailIds = [];
                for (let i = 0; i < list.length; i++) {
                    waveDetailIds.push(list[i].waveDetailId)
                }
                this.waveDetailIds = waveDetailIds

                this.dialogTransVisible = true
            },
            handleClose() {
                this.dialogTransVisible = false
            },
            transToWaveConfirm(waveId) {
                this.$confirm('确认转移至此波次？')
                    .then(_ => {
                        this.transToWave(waveId)
                    })
                    .catch(_ => {
                    });
            },
            transToWave(waveId) {
                IOT.showOverlay('转移中，请稍等...');
                IOT.getServerData('/wave/masters/transToWave', 'post', {
                    waveId: waveId,
                    waveDetailIds: this.waveDetailIds
                }, (ret) => {
                    IOT.hideOverlay();
                    this.waveDetailSelection = [];
                    this.waveDetailIds = []
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success');
                        this.dialogTransVisible = false
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            switchScheduledWaveTaskState(){
                let that = this
                let state = this.waveScheduleState
                IOT.getServerData('/monitor/job/changeStatus','post',{jobId: that.jobId, status: state},(ret) => {
                    that.getWaveTaskState()
                    if (ret.code === 200) {
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getWaveTaskState(){
                IOT.getServerData('/monitor/job/list','get', {methodName: 'waveParams', methodParams: PF.getLocal('wareId', 'number')},(ret) => {
                    if (ret.code === 200) {
                        this.waveScheduleState = ret.rows[0] ? ret.rows[0].status : 0;
                        this.cronExpression =  ret.rows[0] ? ret.rows[0].cronExpression : '';
                        this.jobId = ret.rows[0] ? ret.rows[0].jobId : null;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitCronUpdate(){
                let that = this
                IOT.showOverlay('保存中~~~')
                IOT.getServerData('/monitor/job/updateCron','post', {jobId: that.jobId, cronExpression: that.cronExpression},(ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.message || '保存成功！')
                        that.getWaveTaskState()
                        that.editCronVisible = false
                    } else {
                        IOT.tips(ret.message || 'server error!', 'error')
                    }
                })
            }
        },
        created() {
            this.getUsers();
            this.getWaveMasterData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
            this.getWaveTaskState()
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
#divContainer {
    //height: 900px;
  //overflow: auto;
}

#divContainer::-webkit-scrollbar {
	border-width:1px;
}
    .detailItemBox {
        display: inline-block;
        margin-top: 20px;
        width: 300px;
        span {
            flex: 1;
        } 
    } 
    .current-row {
        .el-button--text  {
            color: white;
        }
        .el-card{
        background-color: #409EFF;
        color:white; 
        } 
    }
    
</style>
