<template>
    <div class="manufactureBill">
        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="manufactureBills"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="code"
                        label="单号"
                >
                </el-table-column>
                <el-table-column
                        prop="itemCode"
                        label="SKU"
                ></el-table-column>
                <el-table-column
                        prop="itemName"
                        label="SKU名称"
                ></el-table-column>
                <el-table-column
                        prop="quantity"
                        label="加工数量"
                ></el-table-column>
                <el-table-column
                        prop="finishedQuantity"
                        label="已完成数量"
                ></el-table-column>
                <el-table-column
                        prop="procedureDesc"
                        label="工序描述"
                ></el-table-column>
                <el-table-column
                        prop="state"
                        label="状态">
                    <template slot-scope="scope">
                        <span>{{scope.row.state === 0 ? '初始化' : '已完成'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        label="创建时间"
                ></el-table-column>
                <el-table-column
                        prop="finishTime"
                        label="完成时间"
                ></el-table-column>

            </el-table>
        </div>
    </div>
</template>

<script>
    export default {
        name: "ManufactureBill",
        data() {
            return {
                manufactureBills: []
            }
        },
        methods: {
            getManufactureBillData() {
                IOT.getServerData('/manufacture/bills', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.manufactureBills = ret.rows
                    }
                })
            }

        },
        created() {
            this.getManufactureBillData()
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>