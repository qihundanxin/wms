<template>
    <div class="pickTactic">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getPickTacticData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div class="item">
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="策略编码/描述" clearable></el-input>
                </div>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
            </el-button>
        </div>

        <el-dialog title="新增策略" :visible.sync="dialogNewVisible" :before-close="handleClose" width="650px">
        <el-form :model="form" :rules="rules" ref="form">
            <el-form-item label="编码" prop="upperTacticCode" :label-width="formLabelWidth">
                <el-input v-model="form.allocationRuleCode" placeholder="请输入编码"></el-input>
            </el-form-item>
            <el-form-item label="名称" prop="upperTacticName" :label-width="formLabelWidth">
                <el-input v-model="form.description" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="存储位" prop="checkWare" :label-width="formLabelWidth" class="checkItemBoxHeight">
                <el-checkbox  v-model="form.upDismantle" :true-label="1" :false-label="0">存储位向上拆解</el-checkbox>
                <el-checkbox v-model="form.downDismantle" :true-label="1" :false-label="0">存储位向下拆解</el-checkbox>
                <el-checkbox v-model="form.whetherDismantle" :true-label="1" :false-label="0">存储位是否拆包</el-checkbox>
                <el-checkbox v-model="form.clearancePriority" :true-label="1" :false-label="0">存储位清仓优先</el-checkbox>
                <el-checkbox v-model="form.optimalAlgorithm" :true-label="1" :false-label="0">存储位最优算法</el-checkbox>
            </el-form-item>
            <el-form-item label="拣货位" prop="checkWare" :label-width="formLabelWidth" class="checkItemBoxHeight">
                <el-checkbox v-model="form.excessiveAllocation" :true-label="1" :false-label="0">拣货位超量分配</el-checkbox>
                <el-checkbox v-model="form.autoReplenishment" :true-label="1" :false-label="0">拣货位自动补货</el-checkbox>
                <el-checkbox v-model="form.dynamicPickCell" :true-label="1" :false-label="0">动态拣货位</el-checkbox>
                <el-checkbox v-model="form.storageChooseEA" :true-label="1" :false-label="0">存储位拣选(EA)</el-checkbox>
                <el-checkbox v-model="form.storageChooseCS" :true-label="1" :false-label="0">存储位拣选(CS)</el-checkbox>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
            <el-button type="primary" @click="commitForm('form')">确 定</el-button>
        </div>
    </el-dialog>
        <el-dialog title="修改策略" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="650px">
            <el-form :model="rowData"  ref="rowData">
                <el-form-item label="编码" prop="upperTacticCode" :label-width="formLabelWidth">
                    <el-input v-model="rowData.allocationRuleCode" placeholder="请输入编码"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="upperTacticName" :label-width="formLabelWidth">
                    <el-input v-model="rowData.description" placeholder="请输入名称"></el-input>
                </el-form-item>
                <el-form-item label="存储位" prop="checkWare" :label-width="formLabelWidth" class="checkItemBoxHeight">
                    <el-checkbox  v-model="rowData.upDismantle" :true-label="1" :false-label="0">存储位向上拆解</el-checkbox>
                    <el-checkbox v-model="rowData.downDismantle" :true-label="1" :false-label="0">存储位向下拆解</el-checkbox>
                    <el-checkbox v-model="rowData.whetherDismantle" :true-label="1" :false-label="0">存储位是否拆包</el-checkbox>
                    <el-checkbox v-model="rowData.clearancePriority" :true-label="1" :false-label="0">存储位清仓优先</el-checkbox>
                    <el-checkbox v-model="rowData.optimalAlgorithm" :true-label="1" :false-label="0">存储位最优算法</el-checkbox>
                </el-form-item>
                <el-form-item label="拣货位" prop="checkWare" :label-width="formLabelWidth" class="checkItemBoxHeight">
                    <el-checkbox v-model="rowData.excessiveAllocation" :true-label="1" :false-label="0">拣货位超量分配</el-checkbox>
                    <el-checkbox v-model="rowData.autoReplenishment" :true-label="1" :false-label="0">拣货位自动补货</el-checkbox>
                    <el-checkbox v-model="rowData.dynamicPickCell" :true-label="1" :false-label="0">动态拣货位</el-checkbox>
                    <el-checkbox v-model="rowData.storageChooseEA" :true-label="1" :false-label="0">存储位拣选(EA)</el-checkbox>
                    <el-checkbox v-model="rowData.storageChooseCS" :true-label="1" :false-label="0">存储位拣选(CS)</el-checkbox>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
            </div>
        </el-dialog>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="pickTactics"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="pickTacticId"
                        label="策略规则id">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="allocationRuleCode"
                        label="策略规则编码">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="description"
                        label="描述">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="upDismantle"
                        label="存储位向上拆解">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.upDismantle">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="downDismantle"
                        label="存储位向下拆解">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.downDismantle">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="whetherDismantle"
                        label="存储位是否拆包">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.whetherDismantle">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="clearancePriority"
                        label="存储位清仓优先">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.clearancePriority">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="optimalAlgorithm"
                        label="存储位最优算法">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.optimalAlgorithm">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="excessiveAllocation"
                        label="拣货位超量分配">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.excessiveAllocation">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="autoReplenishment"
                        label="拣货位自动补货">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.autoReplenishment">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="dynamicPickCell"
                        label="动态拣货位">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.dynamicPickCell">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="storageChooseEA"
                        label="存储位拣选(EA)">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.storageChooseEA">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="storageChooseCS"
                        label="存储位拣选(CS)">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in whether" :key="index" v-if="item.value === scope.row.storageChooseCS">{{item.label}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                                   size="small"></el-button>
                        <el-button @click="isDelete(scope.row.pickTacticId)" icon="iconfont icon-shanchu" type="danger" circle
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
        name: 'PickTactic',
        data() {
            let validatePickTacticCode = (rule, value, callback) => {
                PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_code', this.form.upperTacticCode, 0);
            };
            let validatePickTacticName = (rule, value, callback) => {
                PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_name', this.form.upperTacticName, 0);
            };

            return {
                isMoreSearch: false,

                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',

                },
                pickTactics: [],

                dialogNewVisible: false,
                dialogUpdateVisible: false,

                dialogNewDetailVisible: false,
                loading:{
                    masterTable:true,

                },

                notLimit: '#E6A23C',

                form: {
                    allocationRuleCode: '',
                    description: '',
                    upDismantle: 0,
                    downDismantle: 0,
                    whetherDismantle: 0,
                    clearancePriority: 0,
                    optimalAlgorithm: 0,
                    excessiveAllocation: 0,
                    autoReplenishment: 0,
                    dynamicPickCell: 0,
                    storageChooseEA: 0,
                    storageChooseCS: 0,
                },
                rules: {
                    /*allocationRuleCode: [
                      {required: true, message: '请输入编码', trigger: 'blur'},
                      {validator: validatePickTacticCode, trigger: 'blur'}
                    ],
                    description: [
                      {required: true, message: '请输入名称', trigger: 'blur'},
                      {validator: validatePickTacticName, trigger: 'blur'}
                    ],
                    type1: [
                      {required: true, message: '请选择规则代码', trigger: 'blur'}
                    ],
                    type2: [
                      {required: true, message: '请选择规则代码', trigger: 'blur'}
                    ],*/
                },
                formLabelWidth: '150px',

                rowData: {
                    pickTacticId:'',
                    allocationRuleCode: '',
                    description: '',
                    upDismantle: 0,
                    downDismantle: 0,
                    whetherDismantle: 0,
                    clearancePriority: 0,
                    optimalAlgorithm: 0,
                    excessiveAllocation: 0,
                    autoReplenishment: 0,
                    dynamicPickCell: 0,
                    storageChooseEA: 0,
                    storageChooseCS: 0,
                },
            }
        },
        computed: {
            ...mapState([
                "pickTacticType",
                "whether",
            ]),
        },
        methods: {
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getPickTacticData();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getPickTacticData()
            },

            clearSearchData() {
                this.searchData.keyWords = '';
            },
            getPickTacticData() {
                let that = this;
                that.loading.masterTable = true;
                IOT.getServerData('/pick/tactics/list', 'get', this.searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        this.pickTactics = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            isDelete(pickTacticId) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deletePicTactic(pickTacticId)
                    })
                    .catch(_ => {
                    });
            },
            deletePicTactic(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/pick/tactics/deleteById', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getPickTacticData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },


            commitForm(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.getServerData('/pick/tactics/add', 'post', this.form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getPickTacticData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('form')
                        this.dialogNewVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            updateForm(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        IOT.getServerData('/pick/tactics/update', 'post', this.rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getPickTacticData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('rowData')
                        this.dialogUpdateVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            showUpdateModal(row) {
                this.rowData = JSON.parse(JSON.stringify(row));
                
                /*this.rowData.upDismantle = String(row.upDismantle);
                this.rowData.downDismantle = String(row.downDismantle);
                this.rowData.whetherDismantle = String(row.whetherDismantle);
                this.rowData.clearancePriority = String(row.clearancePriority);
                this.rowData.optimalAlgorithm = String(row.optimalAlgorithm);
                this.rowData.excessiveAllocation = String(row.excessiveAllocation);
                this.rowData.autoReplenishment = String(row.autoReplenishment);
                this.rowData.dynamicPickCell = String(row.dynamicPickCell);
                this.rowData.storageChooseEA = String(row.storageChooseEA);
                this.rowData.storageChooseCS = String(row.storageChooseCS);*/
                this.dialogUpdateVisible = true
            },
            resetForm(formName) { // 重置
                this.$refs[formName].resetFields();
            },
            handleClose(done) {
                // this.$confirm('确认关闭？')
                //     .then(_ => {
                this.resetForm('form');
                this.dialogNewVisible = false;
                //     done();
                // })
                // .catch(_ => {});
            },
            handleClose2(done) {
                // this.$confirm('确认关闭？')
                //     .then(_ => {
                this.resetForm('rowData');
                this.dialogUpdateVisible = false;
                //     done();
                // })
                // .catch(_ => {});
            },
            handleCloseDetail(done) {
                // this.$confirm('确认关闭？')
                //     .then(_ => {
                this.resetForm('detailForm');
                this.dialogNewDetailVisible = false
                //     done();
                // })
                // .catch(_ => {});
            },
            handleCloseDetail2(done) {
                // this.$confirm('确认关闭？')
                //     .then(_ => {
                this.resetForm('detailRowData');
                // this.dialogUpdateDetailVisible = false
                //     done();
                // })
                // .catch(_ => {});
            },

            deleteDetail(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/upper/tactic/details/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {

                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            showDetailModal() {
                this.detailForm.upperTacticId = this.lookRowData.upperTacticId;
                this.detailForm.upperTacticName = this.lookRowData.upperTacticName;
            },
            commitDetailForm(formName) {
                let that = this;
                let rules = this.rulesDetail;
                rules.toAreaCode[0].required = true;
                rules.fromCellCode[0].required = true;
                rules.toCellCode[0].required = true;
                let upperTacticType = this.detailForm.upperTacticType;
                if (upperTacticType === 1) {
                    rules.toAreaCode[0].required = false;
                } else if (upperTacticType === 3) {
                    rules.fromCellCode[0].required = false;
                } else if (upperTacticType === 2) {
                    rules.toCellCode[0].required = false;
                }
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.getServerData('/upper/tactic/details/insert', 'post', this.detailForm, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {

                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('detailForm')
                        this.dialogNewDetailVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
                rules.toAreaCode[0].required = true;
                rules.fromCellCode[0].required = true;
                rules.toCellCode[0].required = true;
            },
            updateDetailForm(formName) {
                let that = this;
                let rules = this.rulesDetail2;
                let upperTacticType = this.detailRowData.upperTacticType;
                rules.toAreaCode[0].required = true;
                rules.fromCellCode[0].required = true;
                rules.toCellCode[0].required = true;
                if (upperTacticType === 1) {
                    rules.toAreaCode[0].required = false;
                } else if (upperTacticType === 3) {
                    rules.fromCellCode[0].required = false;
                } else if (upperTacticType === 2) {
                    rules.toCellCode[0].required = false;
                }
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.getServerData('/upper/tactic/details/update', 'post', this.detailRowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {

                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('detailRowData')
                        this.dialogUpdateDetailVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
                rules.toAreaCode[0].required = true;
                rules.fromCellCode[0].required = true;
                rules.toCellCode[0].required = true;
            },

        },
        created() {
            this.getPickTacticData();
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .upperTactic {

    }
</style>
