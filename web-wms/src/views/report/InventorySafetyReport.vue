<template>
    <div class="inventorySafetyReport">
        <div class="publicSearch">
            <div class="searchBar" style="height: auto">
                <el-button-group>
                    <el-button @click="getTableData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字</span>
                    <el-input size="mini" v-model="searchData.keyWords"></el-input>
                </div>
                <div>
                    <span style="margin-right: 10px">库存状态</span>
                    <el-select
                            v-model="searchData.inventorySafetyState"
                            size="mini"
                    >
                        <el-option
                                v-for="item in states"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                        ></el-option>
                    </el-select>
                </div>
            </div>
        </div>
        <div class="tableBox">
            <el-table
                    v-loading="loading"
                    :data="tableData"
                    highlight-current-row
                    style="width: 100%"
            >
                <el-table-column
                        type="index"
                        label="序号"
                        fixed="left"
                        width="50"
                ></el-table-column>
                <el-table-column
                        prop="itemCode"
                        label="SKU"
                ></el-table-column>
                <el-table-column
                 show-overflow-tooltip
                        prop="itemName"
                        label="SKU名称"
                ></el-table-column>
                <el-table-column
                        prop="wareName"
                        label="仓库"
                ></el-table-column>
                <el-table-column
                        prop="organizationName"
                        label="货主"
                ></el-table-column>
                <el-table-column
                        prop="packDescribe"
                        label="单位"
                ></el-table-column>
                <el-table-column
                        prop="quantity"
                        label="库存数量"
                ></el-table-column>
                <el-table-column
                        prop="ceiling"
                        label="上限"
                ></el-table-column>
                <el-table-column
                        prop="floor"
                        label="下限"
                ></el-table-column>
                <el-table-column
                        prop="state"
                        label="库存状态"
                ></el-table-column>
            </el-table>
            <el-pagination
              class="pagination-bottom"
                    style="text-align: right;"
                    @current-change="handlePageChange"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
    export default {
        name: "InventorySafetyReport",
        data() {
            return {
                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    inventorySafetyState: 0
                },
                loading: false,
                tableData: [],
                states: [
                    {
                        value: 1,
                        label: '超储'
                    },
                    {
                        value: -1,
                        label: '短缺'
                    },
                    {
                        value: 0,
                        label: '全部'
                    }
                ]
            }
        },
        methods: {
            getTableData() {
                let that = this
                this.loading = true
                IOT.getServerData('/inventorys/inventorySafetyReport', 'post', this.searchData, (ret) => {
                    that.loading = false
                    if (ret.code === 200) {
                        that.tableData = ret.rows
                        that.searchData.total = ret.total
                        that.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || 'server error')
                    }
                })
            },
            clearSearchData() {
                this.searchData = {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: ''
                }
            },
            handlePageChange(val) {
                this.searchData.pageNum = val
                this.getTableData()
            }
        },
        created() {
            this.getTableData()
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>