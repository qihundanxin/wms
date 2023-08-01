<template>
    <div class="returnBill">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getSoData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>SO类型</span>
                    <el-select v-model="searchData.soType" size="mini" filterable placeholder="请选择SO类型" clearable>
                        <el-option
                                v-for="item in soBillTypes"
                                :key="item.dataDictDetailId"
                                :label="item.dictValue"
                                :value="item.dataDictDetailId">
                        </el-option>
                    </el-select>
                </div>
                <div>
                    <span>单据来源</span>
                    <el-select v-model="searchData.billSource" size="mini" filterable placeholder="请选择单据来源" clearable>
                        <el-option :key="1" label="新建" :value="1">新建</el-option>
                        <el-option :key="2" label="合并" :value="2">合并</el-option>
                        <el-option :key="3" label="调拨" :value="3">调拨</el-option>
                    </el-select>
                </div>
                <div>
                    <span>单号</span>
                    <el-input v-model="searchData.billNo" size="mini" placeholder="请输入单号" clearable></el-input>
                </div>
                <div>
                    <span>合并单号</span>
                    <el-input v-model="searchData.mergeNo" size="mini" placeholder="请输入合并单号" clearable></el-input>
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
                    <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择审核状态" clearable>
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
                    <el-select v-model="searchData.customerId" size="mini" filterable placeholder="请选择客户"
                               style="width: 70%;">
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
                    <el-input v-model="searchData.keyWords" size="mini" title="自定义字段/创建人/备注" placeholder="自定义字段/创建人/备注"
                              clearable></el-input>
                </div>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng"
                       @click="$router.push({name: 'createReturnBill'})"> 新增
            </el-button>

            <el-button class="rightFloat" size="small" type="info" plain round icon="iconfont icon-backup"
                       @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档
            </el-button>
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="downloadModel()"> 下载模板
            </el-button>

            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="upLoad()"> 上传</el-button>
            <input type="file" id="file">

           <!-- <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="setCarrier()"> 分配承运人-->
            <!-- </el-button> -->

        </div>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="returnBills"
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
                        <span class="textBtn" @click="$router.push({name: 'returnBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="state"
                        label="审核状态"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.state === item.value" :type="item.type">
                            {{item.label}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="supplierName"
                        label="供应商"
                        width="150">
                    <template slot-scope="scope">
                        <span class="textBtn"
                              @click="$parent.showPublicModal(scope.row.supplierId, 'supplierManage')">{{scope.row.supplierName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="linkMan"
                        label="收货人"
                        width="100">
                </el-table-column>
                <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="carrierName"
                        label="承运方"
                        width="180">
                </el-table-column>-->
                <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="deliveryTime"
                        label="要求交货时间"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.deliveryTime.substr(5,11)}}
                    </template>
                </el-table-column>-->
                <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="expectTimeFm"
                        label="预发时间FM"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.expectTimeFm.substr(5,11)}}
                    </template>
                </el-table-column>-->
                <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="expectTimeTo"
                        label="预发时间TO"
                        width="110">
                    <template slot-scope="scope">
                        {{scope.row.expectTimeTo.substr(5,11)}}
                    </template>
                </el-table-column>-->
                <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="orderNo"
                        label="优先级"
                        width="100">
                </el-table-column>-->
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        v-for="(item,index) in returnUdfHs"
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
                        <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">
                            {{scope.row.createUserName}}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
              class="pagination-bottom"
                    style="text-align: right;"
                    @current-change="handleCurrentChange"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    @size-change="handleSizeChange"
                    :page-sizes="[15, 30, 50, 100]"
                    layout="sizes,total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'ReturnBill',
        data() {
            return {
                loading: {
                    masterTable: true,
                },
                unLoadData: {
                    model: "",
                    wareId: 111,
                    insertType: 111
                },

                isMoreSearch: false,

                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    insertType: 0,
                    wareId: PF.getLocal('wareId', 'number'),
                    state: null,
                    organizationId: null,
                    customerId: null,
                    billSource: null,
                    carrierId: null,
                    soType: null,
                    createTimeArr: [],
                    startCreateTime: '',
                    endCreateTime: '',
                    deliveryTimeArr: [],
                    startDeliveryTime: '',
                    endDeliveryTime: '',
                    expectTimeFmArr: [],
                    startExpectTimeFm: '',
                    endExpectTimeTo: '',
                    billNo: '',
                    mergeNo: '',
                    orderNo: '',
                },

                returnBills: [],

                formLabelWidth: '120px',

                returnUdfHs: [],
                organizations: [],
                customers: [],
                soBillTypes: [],
                carriers: [],
                multipleSelection: [],

            }
        },
        computed: {
            ...mapState([
                "reviewStates",
            ]),
            mergeFlg() {
                let list = PF.JSON(this.multipleSelection);
                let flg = false;
                if (list.length > 1) {
                    for (let i = 0; i < list.length; i++) {
                        if (list[i].wareId !== list[0].wareId
                            || list[i].organizationId !== list[0].organizationId
                            || list[i].customerId !== list[0].customerId
                            || list[i].carrierId !== list[0].carrierId
                            || list[i].receivingAddressId !== list[0].receivingAddressId
                            || list[i].billSource === 2
                            || list[i].mergeNo !==null) {

                            flg = true;
                        }
                    }
                    return flg;
                } else {
                    return true;
                }
            }
        },
        methods: {

            downloadModel() {
                window.open('/model/SoImport.xls');
            },

            upLoad() {

                var formData = new FormData();
                formData.append("model", document.querySelector("#file").files[0]);  //上传一个files对象
                formData.append("wareId", 111);//需要上传的多个参数
                
                IOT.upLoadFile('/so/masters/importSoBills', 'post', formData, (ret) => {
                    

                }, true);


            },

            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getSoData()
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getSoData()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.customerId = null;
                this.searchData.soType = null;
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
                this.searchData.mergeNo = '';
                this.searchData.orderNo = '';

            },
            getSoData() {
                let searchData = PF.JSON(this.searchData);
                if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) {
                    searchData.startCreateTime = searchData.createTimeArr[0];
                    searchData.endCreateTime = searchData.createTimeArr[1];
                }
                ;
                if (searchData.deliveryTimeArr !=null && searchData.deliveryTimeArr.length > 0) {
                    searchData.startDeliveryTime = searchData.deliveryTimeArr[0];
                    searchData.endDeliveryTime = searchData.deliveryTimeArr[1];
                }
                ;
                if (searchData.expectTimeFmArr !=null  && searchData.expectTimeFmArr.length > 0) {
                    searchData.startExpectTimeFm = searchData.expectTimeFmArr[0];
                    searchData.endExpectTimeTo = searchData.expectTimeFmArr[1];
                }
                ;
                let that = this;
                that.loading.masterTable = true;
                IOT.getServerData('/return/masters/list', 'get', searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        this.returnBills = ret.rows;
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
            commitFile() {
                IOT.showOverlay('保存中，请稍等...');
                let list = PF.JSON(this.multipleSelection);
                let billNos = [];
                for (let i = 0; i < list.length; i++) {
                    billNos.push(list[i].billNo)
                }
                IOT.getServerData('/so/masters/file', 'post', {billNos: billNos}, (ret) => {
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
            commitMerge() {
                IOT.showOverlay('保存中，请稍等...');
                let list = PF.JSON(this.multipleSelection);
                let billNos = [];


                IOT.getServerData('/organizations/list', 'get', {organizationId: list[0].organizationId}, (ret) => {
                    if (ret.code === 200) {
                        let organization = ret.rows[0];
                        IOT.getServerData('/so/merge/tactics/list', 'get', {soMergeTacticCode: organization.soMergeTacticCode}, (ret) => {
                            if (ret.code === 200) {
                                if (ret.rows.length !== 1) {
                                    IOT.tips('未查询到该货主下的合并策略!', 'error', 1000, () => {
                                        IOT.hideOverlay();
                                    });
                                    return false
                                }
                                let soMergeTactic = ret.rows[0];

                                let noMergerMsg = '';

                                for (let i = 0; i < list.length; i++) {

                                    if (soMergeTactic.billSource === 1) {
                                        if (list[i].billSource !== list[0].billSource) {
                                            noMergerMsg = '单据来源'
                                        }
                                    }/*else if(soMergeTactic.billSource === 2){
                    if(list[i].billSource !== list[0].billSource){
                      this.$confirm('单据来源不同,是否继续？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                        center: true
                      }).then(_ => {
                          this.deleteOrganization(id)
                      }).catch(_ => {
                        IOT.tips( '已取消合并!', 'error', 1000, () => {
                          IOT.hideOverlay();
                        });
                        return false
                      });
                    }
                  }*/
                                    if (soMergeTactic.deliveryTime === 1) {
                                        if (list[i].deliveryTime !== list[0].deliveryTime) {
                                            noMergerMsg = 'qwe数量'
                                        }
                                    }
                                    if (soMergeTactic.expectTimeFm === 1) {
                                        if (list[i].expectTimeFm !== list[0].expectTimeFm) {
                                            noMergerMsg = '预发数量FM'
                                        }
                                    }
                                    if (soMergeTactic.expectTimeTo === 1) {
                                        if (list[i].expectTimeTo !== list[0].expectTimeTo) {
                                            noMergerMsg = '预发数量TO'
                                        }
                                    }
                                    for (let j = 0; j < this.returnUdfHs.length; j++) {
                                        if (soMergeTactic[this.returnUdfHs[j].paramCode] === 1) {
                                            if (list[i][this.returnUdfHs[j].paramCode] !== list[0][this.returnUdfHs[j].paramCode]) {
                                                noMergerMsg = this.returnUdfHs[j].value
                                            }
                                        }
                                    }

                                    billNos.push(list[i].billNo)
                                }

                                if (noMergerMsg === '') {
                                    IOT.getServerData('/so/masters/merge', 'post', {billNos: billNos}, (ret) => {
                                        IOT.hideOverlay();
                                        this.multipleSelection = [];
                                        if (ret.code === 200) {
                                            IOT.tips('合并成功！', 'success');
                                            this.getSoData();
                                        } else {
                                            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                        }
                                    });
                                } else {
                                    IOT.tips(noMergerMsg + '不同,不能合并! 请重新选择', 'error', 1000, () => {
                                        IOT.hideOverlay();
                                    });
                                }
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });


            }
        },
        created() {
            this.getSoData();
            PF.getCarriers((rows) => {
                this.carriers = rows;
            })
            PF.getSysParamUdfData('returnUdfHs', [], (data, udfs) => {
                this.returnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getCustomers((rows) => {
                this.customers = rows;
            });
            PF.getDataDictUdfData('soBillType', (rows) => {
                this.soBillTypes = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .returnBill {

    }
</style>
