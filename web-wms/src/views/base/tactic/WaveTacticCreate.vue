<template>
    <div class="createSoBill">

        <div class="publicSearch bigFormBox3">
            <div class="searchBar2" :style="{height: 'auto'}">
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">新建波次</p>
                <el-form class="clearFloat" :model="form" :rules="rules" ref="waveTacticForm">
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次信息</el-divider>
                    </div>
                    <el-form-item prop="waveTacticCode" label="波次策略编码" :label-width="formLabelWidth">
                        <el-input v-model="form.waveTacticCode" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="waveTacticName" label="波次策略名称" :label-width="formLabelWidth">
                        <el-input v-model="form.waveTacticName" size="mini"></el-input>
                    </el-form-item>
                  <!--  <el-form-item label="上次波次时间" :label-width="formLabelWidth">
                        <el-input disabled v-model="lastWaveTime" size="mini"></el-input>
                    </el-form-item> -->
                    <el-form-item label="激活状态" :label-width="formLabelWidth">
                        <el-select v-model="form.state" placeholder="请选择" size="mini">
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="显示顺序" :label-width="formLabelWidth">
                        <el-input-number v-model="form.displayIndex" size="mini"></el-input-number>
                    </el-form-item>

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">约束条件</el-divider>
                    </div>

                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select
                        disabled
                                v-model="form.wareId"
                                placeholder="请选择"
                                size="mini">
                              
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
                                v-model="form.customerId"
                                placeholder="请选择"
                                size="mini">
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
                                v-model="form.carrierId"
                                placeholder="请选择"
                                size="mini">
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
                        <el-input-number v-model="form.priority" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="soType" label="订单类型" :label-width="formLabelWidth">
                        <el-select v-model="form.soType" size="mini">
                            <el-option
                                    :key="null"
                                    label="不限制"
                                    :value="null"
                            ></el-option>
                            <el-option
                                    v-for="item in soTypes"
                                    :key="item.dataDictDetailId"
                                    :label="item.dictValue"
                                    :value="item.dataDictDetailId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    
                  
                  

                    <el-form-item label="单品" :label-width="formLabelWidth">
                        <el-switch
                                v-model="form.isSingle"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    <el-form-item prop="soDeadline" label="订单创建时间" :label-width="formLabelWidth">
                        <el-time-picker
                                v-model="form.soDeadline"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择时间"
                                size="mini">
                        </el-time-picker>
                    </el-form-item>
                    <el-form-item prop="soCountFloor" label="订单数量下限" :label-width="formLabelWidth">
                        <el-input-number v-model="form.soCountFloor" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="soCountCeiling" label="订单数量上限" :label-width="formLabelWidth">
                        <el-input-number v-model="form.soCountCeiling" :min="0" size="mini"></el-input-number>
                    </el-form-item>
                  
                    <el-form-item prop="sizeLimit" label="体积限制" :label-width="formLabelWidth">
                        <el-input-number v-model="form.sizeLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="weightLimit" label="重量限制" :label-width="formLabelWidth">
                        <el-input-number v-model="form.weightLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="quantityLimit" label="总件数设置" :label-width="formLabelWidth">
                        <el-input-number v-model="form.quantityLimit" :min="1" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="maxWaitTime" label="最长等待时间(分)" :label-width="formLabelWidth">
                        <el-input-number v-model="form.maxWaitTime" :step="1" step-strictly :min="0"
                                         size="mini"></el-input-number>
                    </el-form-item>
                  

                    <el-form-item label="产品组合" :label-width="formLabelWidth">
                        <el-switch
                                v-model="form.itemCombine"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="允许跨区" :label-width="formLabelWidth">
                        <el-switch
                                v-model="form.crossArea"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                   
                   

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left">波次相关设置</el-divider>
                    </div>

                   
                    <el-form-item label="下达分拣任务" :label-width="formLabelWidth">
                        <el-switch
                                v-model="form.autoPick"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>
                    
                    <el-form-item label="是否播种" :label-width="formLabelWidth">
                        <template>
                        <el-radio v-model="form.autoSeeding" label=0>边拣边分</el-radio>
                        <el-radio v-model="form.autoSeeding" label=1>先拣后分</el-radio>
                        </template>
                       
                    </el-form-item>
                    

                </el-form>
            </div>
        </div>

        <div class="createFootBox clearFloat"
             :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <div class="rightFloat">
                <el-button @click="commitForm('waveTacticForm')" type="primary">提 交</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex"

    export default {
        name: 'WaveTacticCreate',
        data() {
            let soCountValidationResultFloor = (rule, value, callback) => {
                this.validateSoCountFloor(rule, value, callback)
            }
            let soCountValidationResultCeiling = (rule, value, callback) => {
                this.validateSoCountCeiling(rule, value, callback)
            }

            return {
                organizations: [],
                customers: [],
                carriers: [],
                items: [],
                wares: [],
                ids: [],
                soUdfHs: [],
                soUdfDs: [],
                soTypes: [],
               
                innerVisibleItem: false,

                formLabelWidth: '120px',

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

                form: {
                    waveTacticCode: '',
                    waveTacticName: '',
                    state: '',
                    displayIndex: '',
                    wareId: PF.getLocal('wareId', 'number'),
                  
                    customerId: '',
                    customerName: '',
                    carrierId: '',
                    carrierName: '',
                    priority: '',
                    distributeTactic: '',
                    soType: '',
                    soState: '',
                    deliveryType: '',
                    route: '',
                    isSingle: 0,
                    soDeadline: '',
                    soCountFloor: '',
                    soCountCeiling: '',
                    acceptorCount: '',
                    itemCount: '',
                    minSkuQuantity: '',
                    sizeLimit: '',
                    weightLimit: '',
                    quantityLimit: '',
                    maxWaitTime: '',
                    autoPick: '',
                    bindCacheBox: '',
                    waveCount: '',
                    autoSeeding: 0,
                    autoOptimize: '',
                    itemCombine: '',
                    crossArea: '',
                    wholeBox: '',
                    soSingleArea: '',
                    extraSql: ''
                },

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
                    quantityLimit: [
                        {required: false, message: '请填写数量限制', trigger: 'blur'}
                    ],
                    maxWaitTime: [
                        {required: false, message: '请选择最长等待时间', trigger: 'blur'}
                    ],
                    soCountFloor: [
                        { validator: soCountValidationResultFloor, trigger: 'blur'}
                    ],
                    soCountCeiling: [
                        { validator: soCountValidationResultCeiling, trigger: 'blur'}
                    ]
                }
            }
        },
        computed: {
            ...mapState([
                "soStates"
            ]),
            isCollapse() {
                return this.$parent.leftMenuIsCollapse;
            },

        },
        methods: {

            validateSoCountFloor(rule, value, callback) {
                let ceiling = this.form.soCountCeiling
                if (ceiling != null && value != null && value > ceiling) {
                    callback(new Error('订单数量下限不能高于上限'));
                } else {
                    callback();
                }
            },
            validateSoCountCeiling(rule, value, callback) {
                let floor = this.form.soCountFloor
                if (floor != null && value != null && value < floor) {
                    callback(new Error('订单数量上限不能低于下限'));
                } else {
                    callback();
                }
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

            commitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.showOverlay('保存中，请稍等...');
                        let form = PF.JSON(this.form);
                        

                        IOT.getServerData('/wave/tactics', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success');
                                this.$router.push({name: 'WaveTactic'})
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        IOT.hideOverlay();
                    }
                })
            }
        },
        created() {
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getCustomers((rows) => {
                this.customers = rows;
            });
            PF.getCarriers((rows) => {
                this.carriers = rows
            })
            this.getWareData();
            PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
                this.soUdfHs = udfs;
            });
            PF.getSysParamUdfData('soUdfDs', [], (data, udfs) => {
                this.soUdfDs = udfs;
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
    .createSoBill {
        padding-bottom: 100px;
    }
</style>
