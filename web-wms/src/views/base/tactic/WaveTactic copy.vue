<template>
    <div class="WaveTactic">

        <el-dialog 
        title="修改波次策略" 
        :visible.sync="dialogUpdateVisible" 
        :before-close="handleClose" 
        width="1200px" 
        style="margin-top: -10vh;">
            <div class="upperTacticBox">
                <el-form :model="rowData" :rules="rules" ref="waveTacticUpdateForm" class="clearFloat">
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次信息</el-divider>
                    </div>
                    <el-form-item prop="waveTacticCode" label="波次策略编码" :label-width="formLabelWidth">
                        <el-input v-model="rowData.waveTacticCode" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="waveTacticName" label="波次策略名称" :label-width="formLabelWidth">
                        <el-input v-model="rowData.waveTacticName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="激活状态" :label-width="formLabelWidth">
                        <el-select v-model="rowData.state" placeholder="请选择" size="mini">
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="显示顺序" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.displayIndex" size="mini"></el-input-number>
                    </el-form-item>

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">约束条件</el-divider>
                    </div>

                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.wareId"
                                placeholder="请选择"
                                size="mini">
                                <el-option value=null></el-option>
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="customerId" label="客户" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.customerId"
                                placeholder="请选择"
                                size="mini">
                               <el-option value=null></el-option>
                            <el-option
                                    v-for="item in customers"
                                    :key="item.customerId"
                                    :label="item.customerName"
                                    :value="item.customerId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="carrierId" label="承运人" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.carrierId"
                                placeholder="请选择"
                                size="mini">
                            <el-option value=null></el-option>
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="priority" label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.priority" size="mini"></el-input-number>
                    </el-form-item>
                 
                   
                     

                    <el-form-item label="单品" :label-width="formLabelWidth">
                        <el-switch
                                v-model="rowData.isSingle"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="订单创建时间" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="rowData.soDeadline"
                                type="datetime"
                                placeholder="选择日期时间"
                                size="mini">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item prop="soCountFloor" label="订单数量下限" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.soCountFloor" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="soCountCeiling" label="订单数量上限" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.soCountCeiling" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                    
                    <el-form-item label="固定产品数量" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.itemCount" :step="1" step-strictly :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    
                    <el-form-item label="体积限制" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.sizeLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="重量限制" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.weightLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="quantityLimit" label="总件数设置" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.quantityLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="maxWaitTime" label="最长等待时间(分)" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.maxWaitTime" :step="1" step-strictly :min="0" size="mini"></el-input-number>
                    </el-form-item>
                   

                    <el-form-item label="产品组合" :label-width="formLabelWidth">
                        <el-switch
                                v-model="rowData.itemCombine"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    
                    

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次相关设置</el-divider>
                    </div>

                  
                    <el-form-item label="下达分拣任务" :label-width="formLabelWidth">
                        <el-switch
                                v-model="rowData.autoPick"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    
                     <el-form-item label="是否播种" :label-width="formLabelWidth">
                        <template>
                        <el-radio v-model="rowData.autoSeeding" label=0>边拣边分</el-radio>
                        <el-radio v-model="rowData.autoSeeding" label=1>先拣后分</el-radio>
                        </template>
                       
                    </el-form-item>
                   
                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="commitForm('waveTacticUpdateForm')">确 定</el-button>
            </div>
        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getWaveTacticData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>

            </div>

        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng"
                       @click="$router.push({name: 'WaveTacticCreate'})"> 新增
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="WaveTactics"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        label="序号"
                        width="55">
                </el-table-column>
                <el-table-column
                         
                        prop="waveTacticId"
                        label="波次策略ID"
                >
                </el-table-column>
                <el-table-column
                         
                        prop="waveTacticCode"
                        label="波次策略编码"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="waveTacticName"
                        label="波次策略名称"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="state"
                        label="激活状态"
                >
                <template slot-scope="scope">
                        <span v-if="scope.row.state===1">激活</span>
                        <span v-if="scope.row.state===0">未激活</span>
                         
                    </template>
                    
                </el-table-column>
                <el-table-column
                              
                        prop="wareName"
                        label="仓库"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="customerName"
                        label="客户"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="carrierName"
                        label="承运人"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="priority"
                        label="优先级"
                >
                </el-table-column>
                <el-table-column
                        prop="soType"
                        label="订单类型">
                    <template slot-scope="scope">
                        <el-tag v-for="(item, index) in soTypes" :key="index" v-if="scope.row.soType === item.value" :type="item.type">
                            {{item.label}}
                        </el-tag>
                    </template>
                </el-table-column>
              
                <el-table-column
                        prop="isSingle"
                        label="单品"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.isSingle===1 ? '是' : '否'}}</span>
                    </template>
                </el-table-column>
            
                <el-table-column
                              
                        prop="soCountFloor"
                        label="订单数量下限"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="soCountCeiling"
                        label="订单数量上限"
                >
                </el-table-column>
              
                <el-table-column
                              
                        prop="sizeLimit"
                        label="体积限制"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="weightLimit"
                        label="重量限制"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="quantityLimit"
                        label="总件数设置"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="maxWaitTime"
                        label="最长等待时间（分）"
                >
                </el-table-column>
            
                <el-table-column
                              
                        prop="itemCombine"
                        label="产品组合"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.itemCombine===1 ? '是' : '否'}}</span>
                    </template>
                </el-table-column>
                
              
              
              
                <el-table-column
                              
                        prop="autoPick"
                        label="下发分拣任务"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.autoPick===1 ? '是' : '否'}}</span>
                    </template>
                </el-table-column>
                
                <el-table-column
                              
                        prop="autoSeeding"
                        label="是否播种"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.autoSeeding===1 ? '是' : '否'}}</span>
                    </template>
                </el-table-column>
                

                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                                   size="small"></el-button>
                        <el-button @click="isDelete(scope.row.waveTacticId)" icon="iconfont icon-shanchu" type="danger" circle
                                   plain size="small"></el-button>
                    </template>
                </el-table-column>

            </el-table>

            <el-pagination
                    style="text-align: right;"
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

    export default {
        name: 'WaveTactic',
        data() {

            let soCountValidationResultFloor = (rule, value, callback) => {
                this.validateSoCountFloor(rule, value, callback)
            }
            let soCountValidationResultCeiling = (rule, value, callback) => {
                this.validateSoCountCeiling(rule, value, callback)
            }

            return {
                isMoreSearch: false,
                loading:{
                    masterTable:true,

                },
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    waveId: ''
                },

                WaveTactics: [],

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],
                multipleSelection: [],

                rowData: {
                    soCountFloor: null,
                    soCountCeilinig: null
                },
                dialogUpdateVisible: false,

                states: [
                    {
                        label: '激活',
                        value: 1
                    },
                    {
                        label: '非激活',
                        value: 0
                    } 
                ],
                lastWaveTime: '2020-02-03',
               
                 
                wares: [],
                customers: [],
                carriers: [],
                soTypes: [],

                rules: {
                    waveTacticCode: [
                        {required: true, message: '请填写策略编码', trigger: 'blur'}
                    ],
                    waveTacticName: [
                        {required: true, message: '请填写策略名称', trigger: 'blur'}
                    ],
                    wareId: [
                        {required: false, message: '请选择仓库', trigger: 'blur'}
                    ],
                    customerId: [
                        {required: false, message: '请填选择客户', trigger: 'blur'}
                    ],
                    carrierId: [
                        {required: false, message: '请填选择承运人', trigger: 'blur'}
                    ],
                    priority: [
                        {required: false, message: '请填选择优先级', trigger: 'blur'}
                    ],
                    soType: [
                        {required: false, message: '请选择 SO 类型', trigger: 'blur'}
                    ],
                    deliveryType: [
                        {required: false, message: '请选择运输方式', trigger: 'blur'}
                    ],
                    route: [
                        {required: false, message: '请选择路线', trigger: 'blur'}
                    ],
                    quantityLimit: [
                        {required: false, message: '请填写数量限制', trigger: 'blur'}
                    ],
                    maxWaitTime: [
                        {required: false, message: '请选择最长等待时间', trigger: 'blur'}
                    ]
                
                }
            }
        },
        computed: {
            ...mapState([
                "soStates"
            ])
        },
        methods: {

            validateSoCountFloor(rule, value, callback) {
                
                let ceiling = this.rowData.soCountCeiling
                if (ceiling != null && value != null && value > ceiling) {
                    PF.publicMessage('订单数量下限不能高于上限！', 'error');
                    return false
                }
                return true
            },
            validateSoCountCeiling(rule, value, callback) {
                
                let floor = this.rowData.soCountFloor
                if (floor != null && value != null && value < floor) {
                    PF.publicMessage('订单数量上限不能低于下限！', 'error')
                    return false
                }
                return true
            },

            getWareData() {
                IOT.getServerData('/ware/infos/list', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.wares = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getWaveTacticData();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getWaveTacticData()
            },

            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.supplierId = null;
            },
            getWaveTacticData() {
                let that = this;
                that.loading.masterTable = true;
                let searchData = PF.JSON(this.searchData);
                IOT.getServerData('/wave/tactics', 'get', searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        this.WaveTactics = ret.rows;
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

            showUpdateModal(row) {
                this.rowData = JSON.parse(JSON.stringify(row));

                 

                this.rowData.itemCombine = String(row.itemCombine);
                this.rowData.crossArea = String(row.crossArea);
                this.rowData.wholeBox = String(row.wholeBox);
                this.rowData.soSingleArea = String(row.soSingleArea);
                this.rowData.distributeTactic = String(row.distributeTactic);
                this.rowData.autoPick = String(row.autoPick);
                this.rowData.bindCacheBox = String(row.bindCacheBox);
                this.rowData.autoSeeding = String(row.autoSeeding);
                this.rowData.autoOptimize = String(row.autoOptimize);

                this.dialogUpdateVisible = true
            },
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteWaveTactic(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteWaveTactic(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/wave/tactics/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getWaveTacticData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleClose() {

            },
            resetForm() {
                this.dialogUpdateVisible = false
            },
            commitForm(formName) {
                let rowData = PF.JSON(this.rowData);
                this.$refs[formName].validate((valid) => {
                   
                    if (valid) {
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/wave/tactics/update', 'post', rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getWaveTacticData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm()
                        this.dialogUpdateVisible = false
                        IOT.hideOverlay();
                    }
                    else {
                        console.log('validate failed')
                    }
                })
            }
        },
        created() {
            PF.getCustomers((rows) => {
                this.customers = rows;
            });
            PF.getCarriers((rows) => {
                this.carriers = rows
            })
            this.getWareData();

            this.getWaveTacticData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
            PF.getDataDictUdfData('soBillType', (rows) => {
                this.soTypes = rows
            });
        },
        mounted() {
        }

    }
</script>

<style scoped lang="less">
    .WaveTactic {
        .el-form-item {
            margin-bottom: 5px;
        }
    }
</style>
