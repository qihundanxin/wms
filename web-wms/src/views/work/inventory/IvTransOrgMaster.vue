<template>
    <div class="ivTransOrgMaster">


        <div class="publicSearch">
            <div class="searchBar" style="height: auto">
                <el-button-group>
                    <el-button @click="getTableData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>
            </div>

        </div>

        <div class="headerBtns">
            <el-button type="primary" size="small" @click="$router.push('ivTransOrgCreate')" round
                       icon="iconfont icon-xinzeng">新增
            </el-button>
        </div>


        <div class="tableBox">
            <el-table
                    highlight-current-row
                    v-loading="loading.parent"
                    :data="tableData"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="index"
                        label="序号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="code"
                        label="转移单号">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$router.push({name:'ivTransOrgDetail', params:scope.row})">{{scope.row.code}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="fromOrgName"
                        label="转出货主"
                ></el-table-column>
                <el-table-column
                        prop="toOrgName"
                        label="转入货主"
                ></el-table-column>
                <el-table-column
                        prop="caseState"
                        label="审核状态"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.caseState === item.value" :type="item.type">
                            {{item.label}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="创建人"
                ></el-table-column>
                <el-table-column
                        prop="wareName"
                        label="仓库"
                ></el-table-column>
                <el-table-column
                        prop="createTime"
                        label="创建时间"
                ></el-table-column>

                <!--                <el-table-column-->
                <!--                        fixed="right"-->
                <!--                        label="操作"-->
                <!--                        width="110">-->
                <!--                    <template slot-scope="scope">-->
                <!--                        <el-button @click="deleteWaveConfirm(scope.row.waveId)" icon="iconfont icon-shanchu"-->
                <!--                                   type="danger" circle-->
                <!--                                   plain size="small"></el-button>-->
                <!--                    </template>-->
                <!--                </el-table-column>-->

            </el-table>
            <el-pagination
              class="pagination-bottom"
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
        name: 'IvTransOrgMaster',
        data() {
            return {
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                rowData: {},
                childSearchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    masterId: null
                },
                tableData: [],
                childTableData: [],

                loading: {
                    parent: false,
                    child: false
                }
            }
        },
        computed: {
            ...mapState([
                "reviewStates",
            ])
        },
        methods: {

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

            getTableData() {
                let that = this
                this.loading.parent = true
                    IOT.getServerData('/iv/trans/org/masters/list', 'post', this.searchData, (ret) => {
                        that.loading.parent = false
                    if (ret.code === 200) {
                        that.tableData = ret.rows
                        that.searchData.total = ret.total
                        that.searchData.pageNum = ret.pageNumber
                        that.rowData = ret.rows[0]
                        that.childSearchData.masterId = ret.rows[0].id
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            }
        },
        created() {
            this.getTableData()
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
