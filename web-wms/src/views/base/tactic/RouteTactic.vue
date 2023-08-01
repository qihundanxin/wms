<template>
    <div class="asnBill">

        <el-dialog title="新建装车单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
                   style="margin-top: -10vh;">
            <div class="bigFormBox2">
                <el-form :model="routeTactic" :rules="rules" ref="routeTacticNewForm" class="clearFloat">
                    <el-form-item label="路线" :label-width="formLabelWidth">
                        <el-select v-model="routeTactic.route" size="mini">
                            <el-option
                                    v-for="item in deliveryTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="状态" :label-width="formLabelWidth">
                        <el-select v-model="routeTactic.state" size="mini">
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="编码" :label-width="formLabelWidth">
                        <el-input v-model="routeTactic.code" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="承运人" :label-width="formLabelWidth">
                        <el-select v-model="routeTactic.carrierId" size="mini">
                           <el-option
                                   v-for="item in carriers"
                                   :key="item.carrierId"
                                   :label="item.carrierName"
                                   :value="item.carrierId"
                           ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="routeTactic.wareId" size="mini">
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            ></el-option>
                        </el-select>
                    </el-form-item>

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left"></el-divider>
                    </div>

                    <el-form-item label="最小重量" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="routeTactic.minWeight" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最大重量" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="routeTactic.maxWeight" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最小体积" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="routeTactic.minSize" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最大体积" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="routeTactic.maxSize" size="mini"></el-input-number>
                    </el-form-item>

                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="commitNewForm('routeTacticNewForm')">确 定</el-button>
            </div>

        </el-dialog>

        <el-dialog title="修改装车单" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px"
                   style="margin-top: -10vh;">
            <div class="bigFormBox2">
                <el-form :model="rowData" :rules="rules2" ref="routeTacticUpdateForm" class="clearFloat">
                    <el-form-item label="路线" :label-width="formLabelWidth">
                        <el-select v-model="rowData.route" size="mini">
                            <el-option
                                    v-for="item in deliveryTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="状态" :label-width="formLabelWidth">
                        <el-select v-model="rowData.state" size="mini">
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="编码" :label-width="formLabelWidth">
                        <el-input v-model="rowData.code" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="承运人" :label-width="formLabelWidth">
                        <el-select v-model="rowData.carrierId" size="mini">
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="rowData.wareId" size="mini">
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            ></el-option>
                        </el-select>
                    </el-form-item>

                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left"></el-divider>
                    </div>

                    <el-form-item label="最小重量" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="rowData.minWeight" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最大重量" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="rowData.maxWeight" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最小体积" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="rowData.minSize" size="mini"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最大体积" :label-width="formLabelWidth">
                        <el-input-number min="0" v-model="rowData.maxSize" size="mini"></el-input-number>
                    </el-form-item>

                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm2">取 消</el-button>
                <el-button type="primary" @click="commitUpdateForm('routeTacticUpdateForm')">确 定</el-button>
            </div>

        </el-dialog>


        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng"
                       @click="showNewModal"
            > 新增
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="routeTactics"
                    style="width: 100%">
                <el-table-column
                        prop="route"
                        label="路线"
                >
                </el-table-column>
                <el-table-column
                        prop="code"
                        label="规则编码"
                >
                </el-table-column>
                <el-table-column
                        prop="maxWeight"
                        label="最大重量"
                >
                </el-table-column>
                <el-table-column
                        prop="minWeight"
                        label="最小重量"
                >
                </el-table-column>
                <el-table-column
                        prop="maxSize"
                        label="最大体积"
                >
                </el-table-column>
                <el-table-column
                        prop="minSize"
                        label="最小体积"
                >
                </el-table-column>
                <el-table-column
                        prop="carrierName"
                        label="承运人"
                >
                </el-table-column>


                <el-table-column
                        prop="state"
                        label="状态"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.state===1 ? '激活' : '未激活'}}</span>
                    </template>
                </el-table-column>

                                <el-table-column
                                        fixed="right"
                                        label="操作"
                                        width="110">
                                    <template slot-scope="scope">
                                        <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                                                   size="small"></el-button>
                                        <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle
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
        name: 'RouteTactic',
        data() {
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

                routeTactics: [],
                routeTactic: {
                    route: null,
                    code: '',
                    minWeight: null,
                    maxWeight: null,
                    minSize: null,
                    maxSize: null,
                    carrierId: null,
                    carrierName: '',
                    organizationId: null,
                    wareId: null,
                    state: 0
                },

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
                dialogNewVisible: false,

                states: [
                    {
                        label: '激活',
                        value: 0
                    },
                    {
                        label: '非激活',
                        value: -1
                    },
                    {
                        label: '激活一次',
                        value: 1
                    },
                ],
                lastWaveTime: '2020-02-03',
                soTypes: [
                    {
                        label: '类型一',
                        value: 0
                    },
                    {
                        label: '类型二',
                        value: 1
                    },
                    {
                        label: '类型三',
                        value: 2
                    }
                ],
                deliveryTypes: [
                    {
                        label: '方式一',
                        value: 0
                    },
                    {
                        label: '方式二',
                        value: 1
                    },
                    {
                        label: '方式三',
                        value: 2
                    }
                ],
                routes: [
                    {
                        label: '路线一',
                        value: 0
                    },
                    {
                        label: '路线二',
                        value: 1
                    },
                    {
                        label: '路线三',
                        value: 2
                    }
                ],
                wares: [],
                customers: [],
                carriers: [],

                rules: {

                },
                rules2: {

                },
            }
        },
        computed: {
            ...mapState([
                "soStates"
            ])
        },
        methods: {

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
                this.getRouteTacticData();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getRouteTacticData()
            },

            clearSearchData() {
                this.searchData.keyWords = '';
            },
            getRouteTacticData() {
                let that = this;
                that.loading.masterTable = true;
                let searchData = PF.JSON(this.searchData);
                IOT.getServerData('/route/tactics', 'get', searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        this.routeTactics = ret.rows;
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

            showNewModal() {
                this.dialogNewVisible = true
            },

            showUpdateModal(row) {
                this.rowData = JSON.parse(JSON.stringify(row));
                this.dialogUpdateVisible = true
            },
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteRouteTactic(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteRouteTactic(id) {
                
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/route/tactics/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getRouteTacticData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleClose() {
                this.dialogNewVisible = false
            },
            handleClose2() {
                this.dialogUpdateVisible = false
            },
            resetForm() {
                this.dialogNewVisible = false
                this.routeTactic = {
                    route: null,
                    code: '',
                    minWeight: null,
                    maxWeight: null,
                    minSize: null,
                    maxSize: null,
                    carrierId: null,
                    carrierName: '',
                    organizationId: null,
                    wareId: null,
                    state: 0
                }
            },
            resetForm2() {
              this.dialogUpdateVisible = false
            },
            commitNewForm(formName) {
                let that = this
                let routeTactic = PF.JSON(this.routeTactic);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        for (let item of that.carriers) {
                            if (item.carrierId == routeTactic.carrierId) {
                                routeTactic.carrierName = item.carrierName
                            }
                        }
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/route/tactics', 'post', routeTactic, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getRouteTacticData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm()
                        this.dialogNewVisible = false
                        IOT.hideOverlay();
                    } else {
                        console.log('validate failed')
                    }
                })
            },
            commitUpdateForm(formName) {
                let that = this
                let routeTactic = PF.JSON(this.rowData);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        for (let item of that.carriers) {
                            if (item.carrierId == routeTactic.carrierId) {
                                routeTactic.carrierName = item.carrierName
                            }
                        }
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/route/tactics/update', 'post', routeTactic, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getRouteTacticData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm2()
                        this.dialogNewVisible = false
                        IOT.hideOverlay();
                    } else {
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

            this.getRouteTacticData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });

        },
        mounted() {
        }

    }
</script>

<style scoped lang="less">
    .asnBill {

    }
</style>
