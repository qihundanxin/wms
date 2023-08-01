<template>
    <div class="allotBill">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>单号</span>
                    <el-input v-model="searchData.billNo" size="mini" placeholder="请输入单号" clearable></el-input>
                </div>
                <div>
                    <span>调入仓库</span>
                    <el-select v-model="searchData.toWareId" size="mini" filterable placeholder="请选择调入仓库" clearable>
                        <el-option
                                v-for="item in wares"
                                :key="item.wareId"
                                :label="item.wareName"
                                :value="item.wareId">
                        </el-option>
                    </el-select>
                </div>
                <div>
                    <span>承运方</span>
                    <el-select v-model="searchData.carrierId" size="mini" filterable placeholder="请选择承运方" clearable>
                        <el-option
                                v-for="item in carriers"
                                :key="item.carrierId"
                                :label="item.carrierName"
                                :value="item.carrierId">
                        </el-option>
                    </el-select>
                </div>

                <div>
                    <span>审核状态</span>
                    <el-select v-model="searchData.caseState" size="mini" filterable placeholder="请选择审核状态" clearable>
                        <el-option
                                v-for="item in reviewStates"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
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
                    <span>客户</span>
                    <el-select v-model="searchData.customerId" size="mini" filterable placeholder="请选择客户" style="width: 70%;">
                        <el-option
                                v-for="item in customers"
                                :key="item.customerId"
                                :label="item.customerName"
                                :value="item.customerId">
                        </el-option>
                    </el-select>
                </div>
                <div>
                    <span>创建时间</span>
                    <el-date-picker
                            v-model="searchData.createTimeArr"
                            size="mini"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <div>
                    <span>要求交货时间</span>
                    <el-date-picker
                            v-model="searchData.deliveryTimeArr"
                            size="mini"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <div>
                    <span>预期发货时间</span>
                    <el-date-picker
                            v-model="searchData.expectTimeFmArr"
                            size="mini"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <div>
                    <span>优先级</span>
                    <el-input v-model="searchData.orderNo" size="mini" placeholder="请输入优先级" clearable></el-input>
                </div>
                <div>
                    <span>关键字搜索</span>
                    <el-input style="width:180px" v-model="searchData.keyWords" size="mini" title="自定义字段/创建人/备注" placeholder="自定义字段/创建人/备注" clearable></el-input>
                </div>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng" @click="$router.push({name: 'createAllotBill'})"> 新增</el-button>
            <!-- <el-button class="rightFloat" size="mini" type="info" plain round icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button> -->
            <!-- <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng"  @click="downloadModel()"> 下载模板</el-button>
            <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng"  @click="upLoad()"> 上传</el-button>
            <input type="file" id="file" class="mgl-10"> -->
        </div>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="allotBills"
                    @selection-change="handleSelectionChange"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        fixed="left"
                        type="index"
                        width="20">
                </el-table-column>
                <el-table-column
                       
                         
                        show-overflow-tooltip
                        prop="billNo"
                        label="单号"
                        width="100">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$router.push({name: 'allotBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="caseState"
                        label="审核状态"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.caseState === item.value" :type="item.type">{{item.label}}</el-tag>
                    </template>
                </el-table-column>
                 <el-table-column
                         
                        show-overflow-tooltip
                        prop="state"
                        label="调拨状态"
                        width="90">
                    <template slot-scope="scope">
                         <el-tag   v-if="scope.row.state==0">待调拨</el-tag>
                         <el-tag   v-if="scope.row.state==2">生成ASN和SO</el-tag>
 
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="fromWreName"
                        label="调出仓库"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.fromWareId, 'ware')">{{scope.row.fromWareName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="toWreName"
                        label="调入仓库"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.toWareId, 'ware')">{{scope.row.toWareName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="organizationName"
                        label="货主"
                        width="130">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="customerName"
                        label="客户"
                        width="100">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{scope.row.customerName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="linkMan"
                        label="收货人"
                        width="100">
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="carrierName"
                        label="承运方"
                        width="110">
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="deliveryTime"
                        label="要求交货时间"
                        width="120">
                    <template slot-scope="scope">
                        <span v-if="scope.row.deliveryTime!=null">{{scope.row.deliveryTime.substr(0,16)}}</span>
                        
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="expectTimeFm"
                        label="预发时间FM"
                        width="120">
                    <template slot-scope="scope">
                        <span v-if="scope.row.expectTimeFm!=null">{{scope.row.expectTimeFm.substr(0,16)}}</span>
                      
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="expectTimeTo"
                        label="预发时间TO"
                        width="120">
                    <template slot-scope="scope">
                         <span v-if="scope.row.expectTimeTo!=null">{{scope.row.expectTimeTo.substr(0,16)}}</span>
                        
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="orderNo"
                        label="优先级"
                        width="100">
                </el-table-column>
                <!-- <el-table-column
                         
                        show-overflow-tooltip
                        v-for="(item,index) in allotUdfHs"
                        :key="index"
                        :prop="item.paramCode"
                        :label="item.value"
                        width="180">
                </el-table-column> -->
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="createTime"
                        label="创建时间"
                        width="120">
                    <template slot-scope="scope">
                        {{scope.row.createTime.substr(0,16)}}
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="createUserName"
                        label="创建人"
                        width="100">
                    <template slot-scope="scope">
                        <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{scope.row.createUserName}}</el-button>
                    </template>
                </el-table-column>
                <el-table-column
                         
                        show-overflow-tooltip
                        prop="memo"
                        label="备注"
                        width="180">
                </el-table-column>
            </el-table>
            <el-pagination
                    style="text-align: right;"
                    class="pagination-bottom"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    :page-sizes="[15,30,50,100]"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="sizes, total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default{
        name: 'AllotBill',
        data(){
            return {
                unLoadData:{
                    model:"",
                    wareId:111,
                    insertType:111
                },

                isMoreSearch: false,
                loading:{
                    masterTable:true,

                },
                searchData:{
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                    insertType: 0,
                    fromWareId: PF.getLocal('wareId', 'number'),
                    state: null,
                    caseState:null,
                    organizationId: null,
                    customerId: null,
                    toWareId: null,
                    billSource: null,
                    carrierId: null,
                    createTimeArr:[],
                    startCreateTime:'',
                    endCreateTime:'',
                    deliveryTimeArr:[],
                    startDeliveryTime:'',
                    endDeliveryTime:'',
                    expectTimeFmArr:[],
                    startExpectTimeFm:'',
                    endExpectTimeTo:'',
                    billNo:'',
                    orderNo:'',
                },

                allotBills: [],

                formLabelWidth: '120px',

                // allotUdfHs: [],
                organizations: [],
                customers: [],
                carriers: [],
                multipleSelection: [],
                wares: [],

            }
        },
        computed: {
            ...mapState([
                "reviewStates",
            ]),
            mergeFlg(){
                let list = PF.JSON(this.multipleSelection);
                let flg = false;
                if(list.length > 1){
                    for(let i=0;i<list.length;i++){
                        if(list[i].wareId !== list[0].wareId
                            || list[i].organizationId !== list[0].organizationId
                            || list[i].customerId !== list[0].customerId
                            || list[i].carrierId !== list[0].carrierId
                            || list[i].receivingAddressId !== list[0].receivingAddressId
                            || list[i].billSource === 2){
                            flg = true;
                        }
                    }
                    return flg;
                }else{
                    return true;
                }
            }
        },
        methods: {

            downloadModel(){
                window.open('/model/SoImport.xls');
            },

            upLoad(){

                var formData = new FormData();
                formData.append("model",document.querySelector("#file").files[0]);  //上传一个files对象
                formData.append("wareId", 111);//需要上传的多个参数
                
                IOT.upLoadFile('/so/masters/importSoBills','post',formData,(ret) => {
                    

                },true);



            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getSoData();
            },
            handleCurrentChange(val) {
                this.pageNum = val;
                this.getSoData()
            },
            clearSearchData(){
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.caseState = null;
                this.searchData.organizationId = null;
                this.searchData.customerId = null;
                this.searchData.billSource = null;
                this.searchData.carrierId = null;
                this.searchData.createTimeArr = [];
                this.searchData.startCreateTime = '';
                this.searchData.endCreateTime = '';
                this.searchData.deliveryTimeArr = [];
                this.searchData.startDeliveryTime = '';
                this.searchData.endDeliveryTime = '';
                this.searchData.expectTimeFmArr = [];
                this.searchData.startExpectTimeFm = '';
                this.searchData.endExpectTimeTo = '';
                this.searchData.billNo = '';
                this.searchData.orderNo = '';
                this.searchData.toWareId = null

            },
            setPageNumFlush(){
                this.searchData.pageNum =1;
                this.getSoData();
            },
            getSoData(){
                let that = this;
                that.loading.masterTable = true;
                let searchData = PF.JSON(this.searchData);
                if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
                    searchData.startCreateTime = searchData.createTimeArr[0];
                    searchData.endCreateTime = searchData.createTimeArr[1];
                };
                if(searchData.deliveryTimeArr !=null && searchData.deliveryTimeArr.length > 0){
                    searchData.startDeliveryTime = searchData.deliveryTimeArr[0];
                    searchData.endDeliveryTime = searchData.deliveryTimeArr[1];
                };
                if(searchData.expectTimeFmArr !=null && searchData.expectTimeFmArr.length > 0){
                    searchData.startExpectTimeFm = searchData.expectTimeFmArr[0];
                    searchData.endExpectTimeTo = searchData.expectTimeFmArr[1];
                };

                IOT.getServerData('/allot/masters/list','get',searchData,(ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        
                        this.allotBills = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            commitFile(){
                IOT.showOverlay('保存中，请稍等...');
                let list = PF.JSON(this.multipleSelection);
                let billNos = [];
                for(let i=0;i<list.length;i++){
                    billNos.push(list[i].billNo)
                }
                IOT.getServerData('/so/masters/file','post',{billNos: billNos},(ret) => {
                    IOT.hideOverlay();
                    this.multipleSelection = [];
                    if (ret.code === 200) {
                        IOT.tips('归档成功！', 'success');
                        this.getSoData();
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitMerge(){
                IOT.showOverlay('保存中，请稍等...');
                let list = PF.JSON(this.multipleSelection);
                let billNos = [];

            }
        },
        created() {
            this.getSoData();
            PF.getCarriers((rows) => {
                this.carriers = rows;
            })
            PF.getWares((rows) => {
                this.wares = rows;
            })
            PF.getSysParamUdfData('allotUdfHs', [], (data, udfs) => {
                this.allotUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getCustomers((rows) => {
                this.customers = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .allotBill{

    }
</style>
