<template>
    <div>
        <el-dialog title="新建装车单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
                   style="margin-top: -10vh;">
            <div class="bigFormBox2">
                <el-form :model="loadMaster" :rules="rules" ref="loadMasterNewForm" class="clearFloat">
                    <el-form-item label="车牌号" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.licensePlateNo" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="车型" :label-width="formLabelWidth">
                        <el-select v-model="loadMaster.vehicleType" size="mini">
                            <el-option
                                    v-for="item in vehicleTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="司机" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.driverName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="装车人员" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.loaderName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="装车结束时间" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="loadMaster.loadFinish"
                                size="mini"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="loadMaster.wareId" size="mini" disabled>
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="波次号" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.waveId" size="mini"></el-input>
                    </el-form-item>
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left"></el-divider>
                    </div>
                    <el-form-item label="desc1" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.desc1" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc2" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.desc2" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc3" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.desc3" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc4" :label-width="formLabelWidth">
                        <el-input v-model="loadMaster.desc4" size="mini"></el-input>
                    </el-form-item>
                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="commitNewForm('loadMasterNewForm')">确 定</el-button>
            </div>

        </el-dialog>

        <el-dialog title="修改装车单" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px"
                   style="margin-top: -10vh;">
            <div class="bigFormBox2">
                <el-form :model="rowData" :rules="rules2" ref="loadMasterUpdateForm" class="clearFloat">
                    <el-form-item label="装车单号" :label-width="formLabelWidth">
                        <el-input v-model="rowData.code" size="mini" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="车牌号" :label-width="formLabelWidth">
                        <el-input v-model="rowData.licensePlateNo" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="车型" :label-width="formLabelWidth">
                        <el-select v-model="rowData.vehicleType" size="mini">
                            <el-option
                                    v-for="item in vehicleTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="司机" :label-width="formLabelWidth">
                        <el-input v-model="rowData.driverName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="装车人员" :label-width="formLabelWidth">
                        <el-input v-model="rowData.loaderName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="装车结束时间" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="rowData.loadFinish"
                                size="mini"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="rowData.wareId" size="mini" disabled>
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="波次号" :label-width="formLabelWidth">
                        <el-input v-model="rowData.waveId" size="mini"></el-input>
                    </el-form-item>
                    <div style="float: left;width: 100%;">
                        <el-divider content-position="left"></el-divider>
                    </div>
                    <el-form-item label="desc1" :label-width="formLabelWidth">
                        <el-input v-model="rowData.desc1" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc2" :label-width="formLabelWidth">
                        <el-input v-model="rowData.desc2" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc3" :label-width="formLabelWidth">
                        <el-input v-model="rowData.desc3" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="desc4" :label-width="formLabelWidth">
                        <el-input v-model="rowData.desc4" size="mini"></el-input>
                    </el-form-item>
                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm2">取 消</el-button>
                <el-button type="primary" @click="commitUpdateForm('loadMasterUpdateForm')">确 定</el-button>
            </div>

        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getLoadMasterData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>

            </div>

        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">
                新建
            </el-button>
        </div>


        <div class="tableBox organizationLeftTab">
            <el-table
                    highlight-current-row
                    @current-change="handleSelChange"
                    :data="loadMasters"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="code"
                        label="编码"
                >
                </el-table-column>
                <el-table-column
                        prop="licensePlateNo"
                        label="车牌号"
                >
                </el-table-column>

                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="deleteLoadConfirm(scope.row.id)" icon="iconfont icon-shanchu"
                                   type="danger" circle
                                   plain size="small"></el-button>
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
                    layout="sizes,total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

        <div class="organizationMiddle">
            <i class="el-icon-d-arrow-right"></i>
        </div>

        <div class="organizationRight">
            <div class="organizationRightTitle">装车单信息</div>
            <div style="margin-left: 20px;">
                <p class="detailItemBox">
                    <span><b>编码：</b>{{rowData.code}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>车牌号：</b>{{rowData.licensePlateNo}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>车辆类型：</b>{{rowData.vehicleType}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>司机：</b>{{rowData.driverName}}</span>
                </p>
            </div>
            <el-divider content-position="left">详细信息</el-divider>
            <div class="headerBtns">

            </div>
            <div class="tableBox">
                <el-table
                        highlight-current-row
                        @selection-change="handleWaveDetailSelChange"
                        :data="loadDetails"
                        style="width: 100%">
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            prop="loadMasterCode"
                            label="装车单编码"
                    ></el-table-column>
                    <el-table-column
                            prop="pickTaskId"
                            label="拣货单ID"
                    ></el-table-column>
                    <el-table-column
                            prop="itemCode"
                            label="SKU">
                    </el-table-column>
                    <el-table-column
                            prop="itemName"
                            label="SKU名称">
                    </el-table-column>
                    <el-table-column
                            prop="pickQuantity"
                            label="拣货数量">
                    </el-table-column>
                    <el-table-column
                            prop="quantity"
                            label="装车数量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="packDescribe"
                            label="单位">
                    </el-table-column>
                    <el-table-column
                            prop="boxNo"
                            label="箱号">
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="110">
                        <template slot-scope="scope">
                            <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu"
                                       type="danger" circle
                                       plain size="small"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div>
                <el-button @click="showUpdateModal">修改</el-button>
                <el-button @click="$router.push({name: 'ChoosePickTask', params: {loadMasterId: loadMasterId}})">
                    添加拣货任务
                </el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'LoadMaster',
        data() {
            return {
                isMoreSearch: false,
                activeName: 'first',
                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    loadMasterCode: ''
                },
                pickTaskSearch: {},

                loadMasters: [],
                loadMaster: {
                    code: '',
                    licensePlateNo: '',
                    vehicleType: null,
                    driverName: '',
                    loaderName: '',
                    loadStart: '',
                    loadFinish: '',
                    sizeLimit: null,
                    weightLimit: null,
                    wareId: PF.getLocal('wareId', 'number'),
                    wareName: '',
                    waveId: null,
                    desc1: '',
                    desc2: '',
                    desc3: '',
                    desc4: ''
                },
                loadDetails: [],
                pickTasks: [],

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],

                loadSelection: [],
                loadDetailSelection: [],
                newLoadSelection: [],

                loadMasterId: '',
                loadDetailIds: [],

                dialogTransVisible: false,

                vehicleTypes: [
                    {
                        value: 1,
                        label: 'type1'
                    },
                    {
                        value: 2,
                        label: 'type2'
                    },
                    {
                        value: 3,
                        label: 'type3'
                    },
                ],
                dialogNewVisible: false,
                dialogUpdateVisible: false,
                wares: [],
                rowData: {},
                rules: [],
                rules2: []

            }
        },
        computed: {
            ...mapState([])
        },
        methods: {

            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getLoadMasterData()
                this.getLoadDetailData()
                this.getPickTaskData()
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getLoadMasterData()
                this.getLoadDetailData()
                this.getPickTaskData()
            },
            handleSelChange(rowData) {
                this.childSearchData.loadMasterCode = rowData.code
                this.loadMasterId = rowData.id

                this.rowData = JSON.parse(JSON.stringify(rowData))

                this.getLoadDetailData()
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
                this.searchData.supplierId = null;
            },
            getLoadMasterData() {
                IOT.getServerData('/load/masters/list', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.loadMasters = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                        this.childSearchData.loadMasterCode = ret.rows[0].code
                        this.rowData = ret.rows[0]
                        this.getLoadDetailData()
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getLoadDetailData() {
                IOT.getServerData('/load/details', 'get', this.childSearchData, (ret) => {
                    if (ret.code === 200) {
                        this.loadDetails = ret.rows
                    }
                })
            },
            getPickTaskData() {
                IOT.getServerData('/pick/tasks/list', 'get', this.pickTaskSearch, (ret) => {
                    if (ret.code === 200) {
                        this.pickTasks = ret.rows
                    }
                })
            },

            deleteLoadConfirm(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteLoad(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteLoad(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/load/masters/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getLoadMasterData()
                            that.getLoadDetailData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteLoadDetail(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteLoadDetail(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/load/details/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getLoadMasterData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            showTransWaveModal() {
                if (this.loadDetailSelection.length <= 0) {
                    IOT.tips('请至少选择一个拣货任务！', 'error', 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false
                }
                let list = PF.JSON(this.waveDetailSelection);
                let loadDetailIds = [];
                for (let i = 0; i < list.length; i++) {
                    loadDetailIds.push(list[i].id)
                }
                this.loadDetailIds = loadDetailIds

                this.dialogTransVisible = true
            },
            handleClose() {
                this.dialogNewVisible = false
            },
            handleClose2() {
                this.dialogUpdateVisible = false
            },

            resetForm() {
                this.dialogNewVisible = false
            },
            resetForm2() {
                this.dialogUpdateVisible = false
            },
            commitNewForm(formName) {
                let loadMaster = PF.JSON(this.loadMaster);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/load/masters', 'post', loadMaster, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getLoadMasterData()
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
                let rowData = PF.JSON(this.rowData);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/load/masters/update', 'post', rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getLoadMasterData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm()
                        this.dialogUpdateVisible = false
                        IOT.hideOverlay();
                    } else {
                        console.log('validate failed')
                    }
                })
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
            showUpdateModal() {
                this.dialogUpdateVisible = true
                // this.rowData =
            }
        },
        created() {
            this.getLoadMasterData()
            this.getWareData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .detailItemBox {
        display: inline-block;
        margin-top: 20px;
        width: 300px;

        span {
            flex: 1;
        }
    }
</style>
