<template>
    <div class="ware">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input style="width:160px" v-model="searchData.keyWords" size="mini" placeholder="仓库/地区/地址/备注" clearable></el-input>
                </div>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">
                新增
            </el-button>
        </div>

        <el-dialog title="新增仓库" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px" :close-on-click-modal="false">
            <el-form :model="form" :rules="rules" ref="form">
                 <el-form-item label="编码" prop="wareCode" :label-width="formLabelWidth">
                    <el-input v-model="form.wareCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="wareName" :label-width="formLabelWidth">
                    <el-input v-model="form.wareName" placeholder="请输入名称" maxlength="20" style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
                    <el-input
                            :value="form.province===''||form.city===''||form.area===''?'':form.country+' / '+form.province+' / '+form.city+' / '+form.area"
                            placeholder="请选择所在地区" style="width: 60%;"></el-input>
                    <el-button type="primary" plain icon="iconfont icon-dizhi"
                               @click="isShowAddressSelectBox = !isShowAddressSelectBox;clearAddressData()">选择
                    </el-button>
                </el-form-item>
                <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
                    <el-input
                            type="textarea"
                            :rows="2"
                            placeholder="请输入详细地址"
                            v-model="form.detailAddress"
                            style="width: 90%;"></el-input>
                </el-form-item>
                <!-- <el-form-item label="拣货任务Cron表达式" style="width: 90%;" :label-width="formLabelWidth">
                    <el-input v-model="form.cronExpression"
                              placeholder="请输入cron表达式" ></el-input>
                    <a href="http://www.bejson.com/othertools/cron/" target="_blank">
                        <el-button
                                icon="iconfont icon-secondsearch_accurate" type="primary" circle plain size="mini"></el-button>
                    </a>
                </el-form-item> -->
                <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
                    <el-input v-model="form.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
                    <el-radio-group v-model="form.status">
                        <el-radio label="0">暂停</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox" width="600px"
                       style="margin-top: -10vh;" :close-on-click-modal="false">
                <el-tabs v-model="activeAddressName" type="border-card">
                    <el-tab-pane label="国家" name="G">
                        <el-radio v-model="radioG" :label="index" :key="index" border v-for="(item,index) in treeDataG"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="省份" name="S">
                        <el-radio v-model="radioS" :label="index" :key="index" border v-for="(item,index) in treeDataS"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="城市" name="C">
                        <el-radio v-model="radioC" :label="index" :key="index" border v-for="(item,index) in treeDataC"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="区/县" name="Q">
                        <el-radio v-model="radioQ" :label="index" :key="index" border v-for="(item,index) in treeDataQ"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                </el-tabs>
            </el-dialog>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
                <el-button type="primary" @click="commitForm('form')">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="修改仓库" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px" :close-on-click-modal="false">
            <el-form :model="rowData" :rules="rules2" ref="rowData">
                <el-form-item label="编码" prop="wareCode" :label-width="formLabelWidth">
                    <el-input v-model="rowData.wareCode" placeholder="请输入编码" maxlength="20"
                              style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="wareName" :label-width="formLabelWidth">
                    <el-input v-model="rowData.wareName" placeholder="请输入名称" maxlength="20"
                              style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
                    <el-input
                            :value="rowData.province===''||rowData.city===''||rowData.area===''?'':rowData.country+' / '+rowData.province+' / '+rowData.city+' / '+rowData.area"
                            placeholder="请选择所在地区" style="width: 60%;"></el-input>
                    <el-button type="primary" plain icon="iconfont icon-dizhi"
                               @click="isShowAddressSelectBox2 = !isShowAddressSelectBox2;clearAddressData()">选择
                    </el-button>
                </el-form-item>
                <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
                    <el-input
                            type="textarea"
                            :rows="2"
                            placeholder="请输入详细地址"
                            v-model="rowData.detailAddress"
                            style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
                    <el-input v-model="rowData.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
                </el-form-item>
            </el-form>
            <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox2" width="600px"
                       style="margin-top: -10vh;" :close-on-click-modal="false">
                <el-tabs v-model="activeAddressName" type="border-card">
                    <el-tab-pane label="国家" name="G">
                        <el-radio v-model="radioG" :label="index" border v-for="(item,index) in treeDataG" :key="index"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="省份" name="S">
                        <el-radio v-model="radioS" :label="index" border v-for="(item,index) in treeDataS" :key="index"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="城市" name="C">
                        <el-radio v-model="radioC" :label="index" border v-for="(item,index) in treeDataC" :key="index"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                    <el-tab-pane label="区/县" name="Q">
                        <el-radio v-model="radioQ" :label="index" border v-for="(item,index) in treeDataQ" :key="index"
                                  class="organizationRadios">{{item.name}}
                        </el-radio>
                    </el-tab-pane>
                </el-tabs>
            </el-dialog>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
            </div>
        </el-dialog>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="wares"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="wareId"
                        label="编码"
                        width="180">
                    
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="wareCode"
                        label="编码"
                        width="180">
                    
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="wareName"
                        label="名称"
                        width="180">
                    <!--<template slot-scope="scope">
                      <span :title="scope.row.wareName" style="white-space:nowrap;">{{scope.row.wareName}}</span>
                    </template>-->
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        label="所在地区"
                        width="200">
                    <template slot-scope="scope">
                        <span>{{scope.row.province}} / {{scope.row.city}} / {{scope.row.area}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="detailAddress"
                        label="详细地址"
                        width="300">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="memo"
                        label="备注">
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="status"
                    label="状态"
                    min-width="80">
                    <template slot-scope="scope">
                        <el-switch 
                        v-model="scope.row.status"
                        @change="changeStatusTimeTask(scope.row.wareId, scope.row.status)"
                        active-value="0"
                        inactive-value="1">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                                   class="tableBtn"></el-button>
                        <el-button @click="isDelete(scope.row.wareId)" icon="iconfont icon-shanchu" type="text"
                                   class="tableBtn danger"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
              class="pagination-bottom"
                    style="text-align: right;"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    :page-sizes="[15, 30, 50, 100]"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="sizes, total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Ware',
        data() {
            let validateWareName = (rule, value, callback) => {
                PF.validator(rule, value, callback, 'ware_info', 'ware_name', this.form.wareName, 0);
            };
            let validateWareName2 = (rule, value, callback) => {
                PF.validator(rule, value, callback, 'ware_info', 'ware_name', this.rowData.wareName, 1, this.rowData.wareId);
            };
            return {
                isMoreSearch: false,

                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                },

                wares: [],

                modalTitle: '',
                dialogNewVisible: false,
                isShowAddressSelectBox: false,
                isShowAddressSelectBox2: false,

                activeAddressName: 'G',
                treeDataG: [],
                treeDataS: [],
                treeDataC: [],
                treeDataQ: [],
                treeDataG: [],
                radioG: '',
                radioS: '',
                radioC: '',
                radioQ: '',
                form: {
                    wareName: '',
                    memo: '',
                    province: '',
                    city: '',
                    area: '',
                    detailAddress: '',
                    cronExpression:'0 0 0 1/1 * ? *',
                    status: '0',
                },
                rules: {
                    wareCode: [
                        {required: true, message: '请输入编码', trigger: 'blur'},
                    ],
                    wareName: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {validator: validateWareName, trigger: 'blur'}
                    ],
                    city: [
                        {required: true, message: '请选择所在地区', trigger: 'blur'}
                    ],
                    area: [
                        {required: true, message: '请选择所在地区', trigger: 'blur'}
                    ],
                    detailAddress: [
                        {required: true, message: '请输入详细地址', trigger: 'blur'}
                    ],
                    status: [
                        { required: true, message: '请选择状态', trigger: 'blur' }
                    ],
                },
                formLabelWidth: '120px',

                dialogUpdateVisible: false,
                rowData: {
                    wareName: '',
                    memo: '',
                    province: '',
                    city: '',
                    area: '',
                    detailAddress: '',

                },
                rules2: {
                    wareCode: [
                        {required: true, message: '请输入编码', trigger: 'blur'},
                    ],
                    wareName: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {validator: validateWareName2, trigger: 'blur'}
                    ],
                    city: [
                        {required: true, message: '请选择所在地区', trigger: 'blur'}
                    ],
                    area: [
                        {required: true, message: '请选择所在地区', trigger: 'blur'}
                    ],
                    detailAddress: [
                        {required: true, message: '请输入详细地址', trigger: 'blur'}
                    ],
                    status: [
                        { required: true, message: '请选择状态', trigger: 'blur' }
                    ],
                },

                dialogVisible: false,

                wareScheduleTask: {
                    jobName: 'waveTask',
                    jobGroup: '波次任务',
                    methodName: 'waveParams',
                    methodParams: '',
                    cronExpression: '0 0 0 * * ?',
                    misfirePolicy: '3',
                    concurrent: '0',
                    status: '1',
                    remark: '',
                },
            }
        },
        watch: {
            radioG(val) {
                if (this.isShowAddressSelectBox) {
                    this.treeDataS = this.treeDataG[val].children;
                    this.form.country = this.treeDataG[val].name;
                    if (this.treeDataS === undefined) {
                        this.form.city = '-';
                        this.form.area = '-';
                        this.isShowAddressSelectBox = false
                    } else {
                        this.activeAddressName = 'S'
                    }
                }
                if (this.isShowAddressSelectBox2) {
                    this.treeDataS = this.treeDataG[val].children;
                    this.rowData.country = this.treeDataG[val].name;
                    if (this.treeDataS === undefined) {
                        this.rowData.city = '-';
                        this.rowData.area = '-';
                        this.isShowAddressSelectBox2 = false
                    } else {
                        this.activeAddressName = 'S'
                    }
                }
            },
            radioS(val) {
                if (this.isShowAddressSelectBox) {
                    this.treeDataC = this.treeDataS[val].children;
                    this.form.province = this.treeDataS[val].name;
                    if (this.treeDataC === undefined) {
                        this.form.city = '-';
                        this.form.area = '-';
                        this.isShowAddressSelectBox = false
                    } else {
                        this.activeAddressName = 'C'
                    }
                }
                if (this.isShowAddressSelectBox2) {
                    this.treeDataC = this.treeDataS[val].children;
                    this.rowData.province = this.treeDataS[val].name;
                    if (this.treeDataC === undefined) {
                        this.rowData.city = '-';
                        this.rowData.area = '-';
                        this.isShowAddressSelectBox2 = false
                    } else {
                        this.activeAddressName = 'C'
                    }
                }
            },
            radioC(val) {
                if (this.isShowAddressSelectBox) {
                    this.treeDataQ = this.treeDataC[val].children;
                    this.form.city = this.treeDataC[val].name;
                    if (this.treeDataQ === undefined) {
                        this.form.area = '-';
                        this.isShowAddressSelectBox = false
                    } else {
                        this.activeAddressName = 'Q'
                    }
                }
                if (this.isShowAddressSelectBox2) {
                    this.treeDataQ = this.treeDataC[val].children;
                    this.rowData.city = this.treeDataC[val].name;
                    if (this.treeDataQ === undefined) {
                        this.rowData.area = '-';
                        this.isShowAddressSelectBox2 = false
                    } else {
                        this.activeAddressName = 'Q'
                    }
                }
            },
            radioQ(val) {
                if (this.isShowAddressSelectBox) {
                    this.treeDataQ2 = this.treeDataC[val].children;
                    this.form.area = this.treeDataQ[val].name;
                    this.isShowAddressSelectBox = false
                }
                if (this.isShowAddressSelectBox2) {
                    this.treeDataQ2 = this.treeDataC[val].children;
                    this.rowData.area = this.treeDataQ[val].name;
                    this.isShowAddressSelectBox2 = false
                }
            }
        },
        methods: {
            handleSizeChange(val) {
                this.searchData.pageSize = val
                this.getWareData()
            },
            handleCurrentChange(val) {
                this.pageNum = val;
                this.getWareData()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
            },
            setPageNumFlush(){
                this.searchData.pageNum =1;
                this.getWareData();
            },
            getWareData() {
                IOT.getServerData('/ware/infos/findSome', 'get', this.searchData, (ret) => {
                    if (ret.code === 200) {
                        this.wares = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
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
                        IOT.getServerData('/ware/infos/insert', 'post', this.form, (ret) => {
                            if (ret.code === 200) {
                                IOT.hideOverlay();
                                IOT.tips('新增成功', 'success', 1000, () => {
                                    // that.addPickTaskScheduleTask(ret.data.wareId, ret.data.wareName);
                                    // that.getWareData();
                                    // that.addScheduleTask(ret.data.wareId);
                                    // 更新系统中仓库localstorage
                                    IOT.showOverlay('正在刷新，请稍等...');
                                    that.$router.go(0);
                                    IOT.hideOverlay();
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                IOT.hideOverlay();
                            }
                        }, true);
                        this.resetForm('form');
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
                        IOT.getServerData('/ware/infos/update', 'post', this.rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('修改成功', 'success', 1000, () => {
                                    // 更新系统中仓库localstorage
                                    IOT.showOverlay('正在刷新，请稍等...');
                                    that.$router.go(0);
                                    IOT.hideOverlay();
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('rowData');
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
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteWare(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteWare(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/ware/infos/delete', 'get', {wareId: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功', 'success', 1000, () => {
                            // that.getWareData()
                            // 更新系统中仓库localstorage
                            IOT.showOverlay('正在刷新，请稍等...');
                            that.$router.go(0);
                            IOT.hideOverlay();
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getAddressJson() {
                let that = this;
                $.getJSON('/json/address.json', (data) => {
                    let treeData = PF.toTree(data)
                    that.treeDataG = treeData;
                    //that.treeDataS = treeData[0].children
                })
            },
            clearAddressData() {
                this.activeAddressName = 'G';
                this.treeDataS = [];
                this.treeDataC = [];
                this.treeDataQ = [];
                this.radioS = '';
                this.radioC = '';
                this.radioQ = '';
                this.inputS = '';
                this.inputC = '';
                this.inputQ = '';
            },
            addScheduleTask(wareId) {
              this.wareScheduleTask.methodParams = wareId + ''
                IOT.getServerData('/monitor/job/add', 'post', this.wareScheduleTask, (ret) => {
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success')
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            addPickTaskScheduleTask(wareId,wareName){
                let sysjob = {
                    jobGroup: '拣货任务',
                    jobName: 'ryTask',
                    methodName: 'autoCreatePickTask',
                    methodParams: wareId+'',
                    cronExpression: this.form.cronExpression,
                    misfirePolicy: '3',
                    concurrent: '1',
                    status: '1',
                    remark: wareName+'自动生成拣货任务',
                }
                IOT.getServerData('/monitor/job/add','post', sysjob, (ret) => {
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            changeStatusTimeTask(id, status){ 
                IOT.getServerData('/ware/infos/changeStatus','post',{wareId: id,status: status},(ret) => {
                if (ret.code === 200) {
                // this.getTimeTaskData(); 
                } else {
                    this.wares = this.wares.map(item => {
                        if(id === item.wareId) {
                            if(status === '0') {
                                item.status = '1'
                            } else if (status === '1') {
                                item.status = '0'
                            }
                        }
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                        return item
                    })
                    
                }
                });
                
            }
        },
        created() {
            this.getWareData();
            this.getAddressJson()
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .ware {

    }
</style>
