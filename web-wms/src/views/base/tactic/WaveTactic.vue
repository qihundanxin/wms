<template>
    <div class="WaveTactic">
 

         <el-dialog 
            title="新增波次策略" 
            :visible.sync="newDialogUpdateVisible" 
           
            width="1200px" 
            style="margin-top: -10vh;">
            <div class="upperTacticBox">
                <el-form :model="newData" :rules="rules" ref="waveTacticAddForm" class="clearFloat">
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次信息</el-divider>
                    </div>
                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select
                        disabled
                                v-model="newData.wareId"
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
                    <el-form-item prop="waveTacticCode" label="波次策略编码" :label-width="formLabelWidth">
                        <el-input v-model="newData.waveTacticCode" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="waveTacticName" label="波次策略名称" :label-width="formLabelWidth">
                        <el-input v-model="newData.waveTacticName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item  prop="state"  label="激活状态" :label-width="formLabelWidth">
                        <el-select v-model="newData.state" placeholder="请选择" size="mini">
                            
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="priority" label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="newData.priority" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="maxWaitTime" label="最长等待时间(分)" :label-width="formLabelWidth">
                        <el-input-number v-model="newData.maxWaitTime" :step="1" step-strictly :min="0" size="mini"></el-input-number>
                    </el-form-item>
                   

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">约束条件</el-divider>
                    </div>

                    
                   
                    <el-form-item prop="carrierId" label="承运人" :label-width="formLabelWidth">
                        <el-select
                                v-model="newData.carrierId"
                                placeholder="请选择"
                                size="mini">
                            <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                     <el-form-item prop="customerId" label="客户" :label-width="formLabelWidth">
                        <el-select
                                v-model="newData.customerId"
                                placeholder="请选择"
                                size="mini">
                               <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in customers"
                                    :key="item.customerId"
                                    :label="item.customerName"
                                    :value="item.customerId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                     

                    <el-form-item  prop="soStructure"  label="订单结构" :label-width="formLabelWidth">
                       <el-select
                                v-model="newData.soStructure"
                                placeholder="请选择"
                                size="mini">
                               
                            <el-option v-for="item in soStructures" :key="item.value" :value="item.value" :label="item.label"></el-option>
                             
 
                        </el-select>
                    </el-form-item>
                    
                     
                    <!-- <el-form-item label="最迟发运时间" :label-width="formLabelWidth">
                        <el-time-picker
                                v-model="newData.soDeadline"
                                type="time"
                                placeholder="选择日期时间"
                                size="mini">
                        </el-time-picker>
                    </el-form-item> -->
                    <el-form-item prop="soCountLimit" label="最大订单数 " :label-width="formLabelWidth">
                        <el-input-number v-model="newData.soCountLimit" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="itemCount"  label="最大SKU数" :label-width="formLabelWidth">
                        <el-input-number v-model="newData.itemCount" :step="1" step-strictly :min="1" size="mini"></el-input-number>
                    </el-form-item>

                     <!-- <el-form-item prop="soType" label="订单类型" :label-width="formLabelWidth">
                        <el-select
                                v-model="newData.soType"
                                placeholder="请选择"
                                size="mini">
                            <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in soTypes"
                                    :key="item.dictKey"
                                    :label="item.dictValue"
                                    :value="item.dictKey"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item> -->

                     
                    
                    
                    
                    

                     <div style="float: left;width: 100%;">
                        <el-divider content-position="left">作业相关设置</el-divider>
                    </div>

                     <el-form-item prop="isSeeding" label="是否播种" :label-width="formLabelWidth">
                        <template>
                             <el-select
                                v-model="newData.isSeeding"
                                placeholder="请选择"
                                size="mini"> 
                            <el-option v-for="item in isSeedings" :key="item.value" :value="item.value" :label="item.label">

                            </el-option>
                               
                        </el-select>
                           
                        </template>
                       
                    </el-form-item>

                   
                    
                    
                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="newDialogUpdateVisible = false">取 消</el-button>
                <el-button type="primary" @click="commitForm('waveTacticAddForm')">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog 
            title="修改波次策略" 
            :visible.sync="updateDialogUpdateVisible" 
            
            width="1200px" 
            style="margin-top: -10vh;">
            <div class="upperTacticBox">
                <el-form :model="rowData" :rules="rules" ref="waveTacticUpdateForm" class="clearFloat">
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次信息</el-divider>
                    </div>
                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select
                        disabled
                                v-model="rowData.wareId"
                                placeholder="请选择"
                                size="mini">
                               <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="waveTacticCode" label="波次策略编码" :label-width="formLabelWidth">
                        <el-input v-model="rowData.waveTacticCode" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="waveTacticName" label="波次策略名称" :label-width="formLabelWidth">
                        <el-input v-model="rowData.waveTacticName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item  prop="state"  label="激活状态" :label-width="formLabelWidth">
                        <el-select v-model="rowData.state" placeholder="请选择" size="mini">
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="priority" label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.priority" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="maxWaitTime" label="最长等待时间(分)" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.maxWaitTime" :step="1" step-strictly :min="0" size="mini"></el-input-number>
                    </el-form-item>
                   

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">约束条件</el-divider>
                    </div>

                    
                   
                    <el-form-item prop="carrierId" label="承运人" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.carrierId"
                                placeholder="请选择"
                                size="mini">
                           <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                     <el-form-item prop="customerId" label="客户" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.customerId"
                                placeholder="请选择"
                                size="mini">
                             <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in customers"
                                    :key="item.customerId"
                                    :label="item.customerName"
                                    :value="item.customerId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                     

                    <el-form-item label="订单结构" :label-width="formLabelWidth">
                       <el-select
                                v-model="rowData.soStructure"
                                placeholder="请选择"
                                size="mini">
                                
                            <el-option v-for="item in soStructures" :key="item.value" :value="item.value" :label="item.label"></el-option>
                             
                             
 
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="最迟发运时间" :label-width="formLabelWidth">
                        <el-time-picker
                                v-model="rowData.soDeadline"
                                type="time"
                                placeholder="选择日期时间"
                                size="mini">
                        </el-time-picker>
                    </el-form-item> -->
                    <el-form-item prop="soCountLimit" label="最大订单数 " :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.soCountLimit" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item  prop="itemCount"  label="最大SKU数" :label-width="formLabelWidth">
                        <el-input-number v-model="rowData.itemCount" :step="1" step-strictly :min="1" size="mini"></el-input-number>
                    </el-form-item>

                     <!-- <el-form-item prop="soType" label="订单类型" :label-width="formLabelWidth">
                        <el-select
                                v-model="rowData.soType"
                                placeholder="请选择"
                                size="mini">
                            <el-option key=-1 label=" " value=-1></el-option>
                            <el-option
                                    v-for="item in soTypes"
                                    :key="item.dataDictDetailId"
                                    :label="item.dictValue"
                                    :value="item.dataDictDetailId"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item> -->

                     
                    
                    
                    
                    

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">作业相关设置</el-divider>
                    </div>

                   
                    
                    <el-form-item prop="isSeeding" label="是否播种" :label-width="formLabelWidth">
                        <template>
                             <el-select
                                v-model="rowData.isSeeding"
                                placeholder="请选择"
                                size="mini">
                               
                            <el-option v-for="item in isSeedings" :key="item.value" :value="item.value" :label="item.label">
                                
                            </el-option>
                               
                        </el-select>
                         
                        </template>
                       
                    </el-form-item>
                   
                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="updateDialogUpdateVisible = false">取 消</el-button>
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
                       @click="newDialogUpdateVisible=true"> 新增
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="WaveTactics"
                    style="width: 100%">
                
                
                <el-table-column
                         
                        prop="waveTacticCode"
                        label=" 编码"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="waveTacticName"
                        label=" 名称"
                >
                </el-table-column>
                <el-table-column
                              
                        prop="priorityLevel"
                        label="优先级"
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
                
                <!-- <el-table-column
                        prop="soTyName"
                        label="订单类型">
                     
                </el-table-column> -->
              
                <el-table-column
                        prop="soStructure"
                        label="订单结构"
                        width="150"
                >
                    <template slot-scope="scope">
                          <span v-if="scope.row.soStructure==0">不限制</span>
                        <span v-if="scope.row.soStructure==1">一单一品</span>
                        <span v-if="scope.row.soStructure==2">一单多品</span>
                        <!-- <span v-if="scope.row.soStructure==3">一单一品一件</span>
                        <span v-if="scope.row.soStructure==4">一单一品多件</span> -->
                    </template>
                </el-table-column>
            
                <el-table-column
                              
                        prop="soCountLimit"
                        label="最大订单数"
                >
                </el-table-column>

                <el-table-column
                              
                        prop="itemCount"
                        label="最大SKU数"
                >
                </el-table-column>
               
              
                
                <el-table-column
                              
                        prop="maxWaitTime"
                        label="最长等待时间（分）"
                >
                </el-table-column>
            
               
                
              
              
              
                
                
                <el-table-column
                              
                        prop="autoSeeding"
                        label="是否播种"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.isSeeding===1 ? '先拣后分' : '边拣边分'}}</span>
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
                isSeedings:[
                     {
                        value:1,
                        label:"先拣后分"
                    },
                    {
                        value:0,
                        label:"边拣边分"
                    },
                ],
                soStructures:[
                    {
                        value:0,
                        label:"不限制"
                    },
                    {
                        value:1,
                        label:"一单一品"
                    },
                    {
                        value:2,
                        label:"一单多品"
                    },
                    // {
                    //     value:3,
                    //     label:"一单一品一件"
                    // },
                    // {
                    //     value:4,
                    //     label:"一单一品多件"
                    // }

                ],
               updateDialogUpdateVisible:false,
                newDialogUpdateVisible:false,
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

                newData:{
                    wareId: PF.getLocal('wareId', 'number')
                },
                rowData: {
                    
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
                    state: [
                        {required: true, message: '请选择激活状态', trigger: 'blur'}
                    ],
                    customerId: [
                        {required: false, message: '请填选择客户', trigger: 'blur'}
                    ],
                    maxWaitTime: [
                        {required: true, message: '请填写最长等待时间', trigger: 'blur'}
                    ],
                    soStructure: [
                        {required: true, message: '请填选择订单结构', trigger: 'blur'}
                    ],
                    isSeeding: [
                        {required: true, message: '请选择分拣方式', trigger: 'blur'}
                    ],
                   
                    soCountLimit: [
                        {required: true, message: '请填写最大订单数', trigger: 'blur'}
                    ],
                    itemCount: [
                        {required: true, message: '请填写最大SKU数', trigger: 'blur'}
                    ],


   
                   
                
                }
            }
        },
        computed: {
            ...mapState([
                "soStates"
            ])
        },
        methods: {
            newHandleClose(){
                
            },
            updateHandleClose(){

            },
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
                IOT.getServerData('/wave/tactic/dianshangs/list', 'get', searchData, (ret) => {
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
                this.rowData = row;//JSON.parse(JSON.stringify(row)); 
                this.updateDialogUpdateVisible = true
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
                IOT.getServerData('/wave/tactic/dianshangs/delete', 'get', {id: id}, (ret) => {
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
                this.newDialogUpdateVisible = false;
                this.updateDialogUpdateVisible = false
            },
            resetForm() {
                this.newDialogUpdateVisible = false;
                this.updateDialogUpdateVisible = false
            },
            commitForm(formName) {
                let rowData = PF.JSON(this.rowData);
                let newData = PF.JSON(this.newData);
                this.$refs[formName].validate((valid) => {
                    
                    if (valid) {
                        IOT.showOverlay('保存中，请稍等...');
                        var url;
                        var data;
                        if(formName =="waveTacticAddForm"){
                            url = '/wave/tactic/dianshangs/add';
                            data = newData;
                        }
                        if(formName =="waveTacticUpdateForm"){
                            url = '/wave/tactic/dianshangs/update';
                            data = rowData;
                        }
                        IOT.getServerData(url, 'post', data, (ret) => {
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
                    //    this.dialogUpdateVisible = false
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
            // PF.getDataDictUdfData('soBillType', (rows) => {
                 
            //     this.soTypes = rows
            // });
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
