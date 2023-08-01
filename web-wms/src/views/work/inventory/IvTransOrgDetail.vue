<template>
    <div class="ivTransOrgDetail">

        <el-dialog title="审核" :visible.sync="dialogReviewVisible" :before-close="reviewHandleClose" width="500px">
            <el-form :model="formReview" :rules="rulesReview" ref="formReview">
                <el-form-item label="审核原因" prop="memo" :label-width="formLabelWidth">
                    <el-input v-model="formReview.memo" placeholder="请输入审核原因"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button class="leftFloat" @click="dialogReviewVisible = false">取 消</el-button>
                <el-button type="danger" @click="commitReview('formReview',-1)">退 回</el-button>
                <el-button type="success" @click="commitReview('formReview',1)">通 过</el-button>
            </div>
        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" style="height: auto">
                <el-button-group>
                    <el-button @click="getChildTableData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>
            </div>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
            </el-button>
            <el-button v-if="ivTransOrgMaster.caseState === 0 && formReview.caseDetailId !== null" size="small"
                       type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核
            </el-button>
            <el-button v-if="ivTransOrgMaster.caseState === 1 && ivTransOrgMaster.state === 0" size="small"
                       type="warning" round icon="iconfont icon-shenhe" @click="commitTransfer"> 确认转移
            </el-button>
        </div>

        <el-tabs type="card" v-model="activeName" style="width: 100%;">
            <el-tab-pane name="first" label="单据明细">
                <div class="newBillData clearFloat">
                    <div style="margin-left: 20px;">
                        <p class="detailItemBox">
                            <span><b>转出货主：</b>{{ivTransOrgMaster.fromOrgName}}</span>
                        </p>
                        <p class="detailItemBox">
                            <span><b>转入货主：</b>{{ivTransOrgMaster.toOrgName}}</span>
                        </p>
                        <p class="detailItemBox">
                            <span><b>创建人：</b>{{ivTransOrgMaster.userName}}</span>
                        </p>
                        <p class="detailItemBox">
                            <span><b>仓库：</b>{{ivTransOrgMaster.wareName}}</span>
                        </p>
                    </div>
                    <el-divider content-position="left">详细信息</el-divider>
                    <div class="tableBox">
                        <el-table
                                highlight-current-row
                                v-loading="loading.child"
                                :data="childTableData"
                                style="width: 100%">
<!--                            <el-table-column-->
<!--                                    type="selection"-->
<!--                                    width="55">-->
<!--                            </el-table-column>-->
                            <el-table-column
                                    prop="itemCode"
                                    label="SKU">
                            </el-table-column>
                            <el-table-column
                                    prop="itemName"
                                    label="SKU名称"
                            ></el-table-column>
                            <el-table-column
                                    prop="quantity"
                                    label="转移数量"
                            >
                            </el-table-column>

                            <!--                        <el-table-column-->
                            <!--                                fixed="right"-->
                            <!--                                label="操作"-->
                            <!--                                width="110">-->
                            <!--                            <template slot-scope="scope">-->
                            <!--                                <el-button @click="isDelete(scope.row.waveDetailId)" icon="iconfont icon-shanchu"-->
                            <!--                                           type="danger" circle-->
                            <!--                                           plain size="small"></el-button>-->
                            <!--                            </template>-->
                            <!--                        </el-table-column>-->
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
            </el-tab-pane>

            <el-tab-pane name="second" label="审核进程">
                <ReviewStepBox :masterData="ivTransOrgMaster.code" :activeName="activeName"></ReviewStepBox>
            </el-tab-pane>
        </el-tabs>


    </div>
</template>

<script>
    import {mapState} from "vuex";
    import ReviewStepBox from '@/components/ReviewStepBox'

    export default {
        name: 'IvTransOrgDetail',
        components: {
            ReviewStepBox
        },
        data() {
            return {
                activeName: 'first',
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    masterId: this.$route.params.id
                },
                tableData: [],
                childTableData: [],

                loading: {
                    parent: false,
                    child: false
                },

                formReview: {
                    billNo: this.$route.params.code,
                    reviewResult: null,
                    caseDetailId: null,
                    memo: '',
                },
                dialogReviewVisible: false,
                rulesReview: {
                    // memo: [
                    //     {required: true, message: '请填写审核原因', trigger: 'blur'}
                    // ],
                },
                formLabelWidth: 120,
                ivTransOrgMaster: this.$route.params,
                reviewStates: [
                    {
                        value: -1,
                        type: '退回'
                    },
                    {
                        value: 0,
                        type: '初始化'
                    },
                    {
                        value: 1,
                        type: '通过'
                    }
                ]
            }
        },
        computed: {
            ...mapState([])
        },
        methods: {

            reviewHandleClose() {
                this.dialogReviewVisible = false
            },

            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getTableData();
            },
            handleCurrentChange(val) {
                this.pageNum = val;
                this.getTableData()
            },

            handleSelChange(rowData) {
                this.rowData = rowData
                this.childSearchData.masterId = rowData.id
                this.getChildTableData()
            },

            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.supplierId = null;
            },

            getCaseNowList() {
                
                IOT.getServerData('/case/details/nowList', 'get', {billNo: this.$route.params.code}, (ret) => {
                    if (ret.code === 200) {
                        if (ret.rows.length > 0) {
                            this.formReview.caseDetailId = ret.rows[0].caseDetailId;
                        } else {
                            this.formReview.caseDetailId = null;
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getChildTableData() {
                let that = this
                this.loading.child = true
                IOT.getServerData('/iv/trans/org/details/list', 'post', this.childSearchData, (ret) => {
                    that.loading.child = false
                    if (ret.code === 200) {
                        that.childTableData = ret.rows
                        that.childSearchData.total = ret.total
                        that.childSearchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
            commitReview(formName, reviewResult) {
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let formReview = PF.JSON(this.formReview);
                        formReview.reviewResult = reviewResult;
                        IOT.getServerData('/iv/trans/org/masters/review', 'get', formReview, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('审核成功！', 'success');
                                this.getCaseNowList();
                                this.dialogReviewVisible = false;
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm(formName);
                        this.dialogReviewVisible = false;
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            commitTransfer() {
                
                IOT.tips('保存中~~~')
                IOT.getServerData('/iv/trans/org/masters/commitTransfer', 'get', {masterId: this.ivTransOrgMaster.id}, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.message || '保存成功！', 'success')
                        this.$router.go(-1)
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            }

        },
        created() {
            this.getChildTableData()
            this.getCaseNowList()
            PF.getOrganizations((rows) => {
                this.organizations = rows;
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
