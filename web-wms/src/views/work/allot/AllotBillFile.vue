<template>
    <div class="allotBillFile">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getBuyBillInData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>
                <div>
                    <span>单据来源</span>
                    <el-select v-model="searchData.billSource" size="mini" filterable placeholder="请选择单据来源" clearable>
                        <el-option :key="0" label="入库单" :value="0"></el-option>
                        <el-option :key="1" label="新建" :value="1"></el-option>
                        <el-option :key="2" label="合并" :value="2"></el-option>
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
                    <span>供应商</span>
                    <el-select v-model="searchData.customerId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
                        <el-option
                                v-for="item in customers"
                                :key="item.customerId"
                                :label="item.customerName"
                                :value="item.customerId">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="buyBillIns"
                    @selection-change="handleSelectionChange"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        fixed="left"
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        fixed="left"
                        sortable
                        show-overflow-tooltip
                        prop="billNo"
                        label="单号"
                        width="120">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$router.push({name: 'allotBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="state"
                        label="审核状态"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.caseState === item.value" :type="item.type">{{item.label}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="fromWreName"
                        label="调出仓库"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.fromWareId, 'ware')">{{scope.row.fromWareName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="toWreName"
                        label="调入仓库"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.toWareId, 'ware')">{{scope.row.toWareName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="organizationName"
                        label="货主"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="customerName"
                        label="客户"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{scope.row.customerName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="linkMan"
                        label="收货人"
                        width="100">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="carrierName"
                        label="承运方"
                        width="180">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="deliveryTime"
                        label="要求交货时间"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.deliveryTime.substr(5,11)}}
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="expectTimeFm"
                        label="预发时间FM"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.expectTimeFm.substr(5,11)}}
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="expectTimeTo"
                        label="预发时间TO"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.expectTimeTo.substr(5,11)}}
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="orderNo"
                        label="优先级"
                        width="100">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        v-for="(item,index) in allotUdfHs"
                        :key="index"
                        :prop="item.paramCode"
                        :label="item.value"
                        width="180">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="createTime"
                        label="创建时间"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.createTime.substr(5,11)}}
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="createUserName"
                        label="创建人"
                        width="100">
                    <template slot-scope="scope">
                        <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{scope.row.createUserName}}</el-button>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
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
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default{
        name: 'AllotBillFile',
        data(){
            return {
                isMoreSearch: false,

                searchData:{
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number'),
                    state: null,
                    organizationId: null,
                    customerId: null,
                },

                buyBillIns: [],

                formLabelWidth: '120px',

                soUdfHs: [],
                organizations: [],
                customers: [],

            }
        },
        computed: {
            ...mapState([
                "reviewStates",
                "asnStates"
            ])
        },
        methods: {

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getBuyBillInData()
            },
            clearSearchData(){
                this.searchData.keyWords = '';
                this.searchData.billSource = null;
                this.searchData.organizationId = null;
                this.searchData.customerId = null;
            },
            getBuyBillInData(){
                IOT.getServerData('/allot/master/list','get',this.searchData,(ret) => {
                    if (ret.code === 200) {
                        this.buyBillIns = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber;
                        PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
                            this.soUdfHs = udfs;
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
        },
        created() {
            this.getBuyBillInData();
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
    .allotBillFile{

    }
</style>
