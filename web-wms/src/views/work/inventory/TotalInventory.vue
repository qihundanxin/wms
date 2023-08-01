<template>
    <div class="inventoryBalance">
        <div class="publicSearch">
            <div
                class="searchBar"
                :style="{ height: isMoreSearch ? 'auto' : '' }"
            >
                <el-button-group>
                    <el-button
                        @click="setPageNumFlush"
                        size="mini"
                        icon="el-icon-search"
                        >查询</el-button
                    >
                    <el-button
                        @click="clearSearchData"
                        size="mini"
                        icon="el-icon-refresh"
                        >重置</el-button
                    >
                </el-button-group>
                <div>
                    <span>SKU名称</span>
                    <el-input
                        v-model="searchData.itemName"
                        size="mini"
                        placeholder="SKU名称"
                        clearable
                    ></el-input>
                </div>
                <div>
                    <span>SKU</span>
                    <el-input
                        v-model="searchData.itemCode"
                        size="mini"
                        placeholder="SKU"
                        clearable
                    ></el-input>
                </div>

                <div>
                    <span>货主</span>
                    <el-select
                        v-model="searchData.organizationId"
                        size="mini"
                        filterable
                        placeholder="请选择货主"
                        clearable
                    >
                        <el-option
                            v-for="item in organizations"
                            :key="item.organizationId"
                            :label="item.organizationName"
                            :value="item.organizationId"
                        >
                        </el-option>
                    </el-select>
                </div>
                <div>
                    <span>关键字搜索</span>
                    <el-input
                        v-model="searchData.keyWords"
                        size="mini"
                        placeholder="批次/追踪号/LPN号"
                        clearable
                    ></el-input>
                </div>
            </div>
            <span
                class="moreSearchBtn"
                :class="
                    isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'
                "
                @click="isMoreSearch = !isMoreSearch"
            ></span>
        </div>
        <div class="headerBtns">
            <el-button
                size="small"
                type="danger"
                round
                icon="iconfont icon-user-jinyong"
                @click="exportInventory()"
                >导出</el-button
            >
        </div>
        <div class="tableBox rowHeight">
            <el-table
                highlight-current-row
                :data="inventoryBalances"
                @selection-change="handleSelectionChange"
                style="width: 100%"
            >
                <el-table-column fixed="left" type="selection" width="55">
                </el-table-column>

                <el-table-column
                show-overflow-tooltip
                    fixed="left"
                    prop="itemName"
                    label="SKU名称"
                    min-width="150"
                >
                    <template slot-scope="scope">
                        <span
                            style="width:150px"
                            class="textBtn"
                            @click="
                                $parent.showPublicModal(
                                    scope.row.itemCode,
                                    'item'
                                )
                            "
                            >{{ scope.row.itemName }}</span
                        >
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="itemCode"
                    label="SKU"
                    min-width="150"
                >
                    
                </el-table-column>
                <!--  <el-table-column
           
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column> -->
                <el-table-column
                    show-overflow-tooltip
                    prop="organizationName"
                    label="货主"
                    min-width="130"
                >
                    <template slot-scope="scope">
                        <span
                            class="textBtn"
                            @click="
                                $parent.showPublicModal(
                                    scope.row.organizationId,
                                    'organization'
                                )
                            "
                            >{{ scope.row.organizationName }}</span
                        >
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalQuantity"
                    label="总数"
                    min-width="140"
                >
                    <!-- <template slot-scope="scope">
                    <span style="color:black;font-weight:900;">{{ Math.floor(scope.row.totalQuantity / scope.row.transRatio * 10000) / 10000 }} </span>
                    <span style="font-size:20% ">（{{scope.row.packDescribe}}）</span>
                    </template> -->
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalQuantity"
                    label="可用数"
                    min-width="140">
                    <template slot-scope="scope">
                        <span style="color: black; font-weight: 900"   >
                            {{
                                scope.row.totalQuantity -
                                scope.row.totalAllotQuantity -
                                scope.row.totalFreezeQuantity -
                                scope.row.totalAcceptQuantity -
                                scope.row.totalSjcQuantity-
                                scope.row.totalOutQuantity-
                                scope.row.totalCcwQuantity

                            }}
                        </span>
 <br/>
                        <span style="color: black; font-weight: 900"   >
                            {{
                                scope.row.canAllotQuantity }} 
                        </span>
                        <!-- <br/>
                        <span style="color: black; font-weight: 900"   >
                            {{
                                scope.row.totalQuantity }}-
                               {{ scope.row.totalAllotQuantity}} -
                                {{scope.row.totalFreezeQuantity}} -
                                {{scope.row.totalAcceptQuantity}} -
                                {{scope.row.totalSjcQuantity}}-
                                {{scope.row.totalOutQuantity}}
                        </span> -->
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalAcceptQuantity"
                    label="验收位数"
                    min-width="140"
                >
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalCcwQuantity"
                    label="仓储位数"
                    min-width="140"
                >
                </el-table-column>
                 <el-table-column
                    show-overflow-tooltip
                    prop="totalSjcQuantity"
                    label="上架车数"
                    min-width="140"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalAllotQuantity"
                    label="分配数量"
                    min-width="140"
                >
                    <!-- <template slot-scope="scope">
                    <span style="color:black;font-weight:900;">{{ Math.floor(scope.row.totalAllotQuantity / scope.row.transRatio * 10000) / 10000 }} </span>
                    <span style="font-size:20% ">（{{scope.row.packDescribe}}）</span>
                    </template>
                    -->
                </el-table-column>
                 <el-table-column
                    show-overflow-tooltip
                    prop="totalOutQuantity"
                    label="出货区数"
                    min-width="140"
                >
                    <!-- <template slot-scope="scope">
                    <span style="color:black;font-weight:900;">{{ Math.floor(scope.row.totalOutQuantity / scope.row.transRatio * 10000) / 10000 }} </span>
                    <span style="font-size:20% ">（{{scope.row.packDescribe}}）</span>
                    </template> -->
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="totalFreezeQuantity"
                    label="冻结数量"
                    min-width="140"
                >
                    <!-- <template slot-scope="scope">
                    <span style="color:black;font-weight:900;">{{ Math.floor(scope.row.totalFreezeQuantity / scope.row.transRatio * 10000) / 10000 }} </span>
                    <span style="font-size:20% ">（{{scope.row.packDescribe}}）</span>
                    </template> -->
                </el-table-column>
                
                <el-table-column
                    show-overflow-tooltip
                    prop="canSaleQuantity"
                    label="可售卖数量"
                    min-width="140">
                    <template slot-scope="scope">
                        <span style="color: black; font-weight: 900"   >
                            {{
                                scope.row.totalQuantity -
                                scope.row.totalAllotQuantity -
                                scope.row.totalFreezeQuantity -
                                scope.row.totalOutQuantity

                            }}
                        </span>
                        <!-- <br/>
                        <span style="color: black; font-weight: 900"   >
                            {{
                                scope.row.totalQuantity }}-
                               {{ scope.row.totalAllotQuantity}} -
                                {{scope.row.totalFreezeQuantity}} -
                                {{scope.row.totalAcceptQuantity}} -
                                {{scope.row.totalSjcQuantity}}-
                                {{scope.row.totalOutQuantity}}
                        </span> -->
                    </template>
                </el-table-column>
                    <!-- <template slot-scope="scope">
                    <span style="color:black;font-weight:900;">{{ Math.floor(scope.row.totalAcceptQuantity / scope.row.transRatio * 10000) / 10000 }} </span>
                    <span style="font-size:20% ">（{{scope.row.packDescribe}}）</span>
                   </template> -->
                
               
                <!-- <el-table-column
                    show-overflow-tooltip
                    prop="packDescribe"
                    label="单位"
                    width="100">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" filterable placeholder="请选择单位" size="small">
                        <el-option
                         v-for="item in scope.row.packDetails"
                        :key="item.packDetailId1"
                        :label="item.packDescribe1"
                        :value="item.packDetailId1">
                        </el-option>
                      </el-select>
                    </template>
                </el-table-column>
                  -->
            </el-table>
            <el-pagination
                class="pagination-bottom"
                style="text-align: right"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                :page-sizes="[15, 30, 50, 100]"
                :current-page.sync="searchData.pageNum"
                :page-size="searchData.pageSize"
                layout="sizes, total, prev, pager, next, jumper"
                :total="searchData.total"
            >
            </el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    name: "InventoryBalance",
    data() {
        return {
            isMoreSearch: false,

            searchData: {
                pageNum: 1,
                pageSize: 15,
                total: null,
                keyWords: "",
                wareId: PF.getLocal("wareId", "number"),
                organizationId: null,
                itemName: "",
                itemCode: "",
            },

            inventoryBalances: [],
            organizations: [],
            multipleSelection: [],
            freezeReasons: [],

            dialogNewVisible: false,
            dialogUpdateVisible: false,
            dialogFreezeVisible: false,

            formLabelWidth: "120px",

            form: {
                freezeMaster: {
                    freezeDescribe: "",
                    memo: "",
                    wareId: null,
                    wareName: "",
                    organizationId: null,
                    organizationName: "",
                },
                freezeDetails: [],
            },
            rules: {
                freezeDescribe: [
                    {
                        required: true,
                        message: "请输入冻结原因",
                        trigger: "blur",
                    },
                ],
                memo: [
                    { required: true, message: "请输入备注", trigger: "blur" },
                ],
            },
        };
    },
    computed: {},
    methods: {
        handleSizeChange(val) {
            this.searchData.pageSize = val;
            this.getInventoryBalanceData();
        },
        handleCurrentChange(val) {
            this.pageNum = val;
            this.getInventoryBalanceData();
        },
        clearSearchData() {
            this.searchData.keyWords = "";
            this.searchData.organizationId = null;
            this.searchData.itemCode = null;
            this.searchData.itemName = null;
        },
        setPageNumFlush(){
            this.searchData.pageNum =1;
            this.getInventoryBalanceData();
        },
        getInventoryBalanceData() {
            let searchData = PF.JSON(this.searchData);
            if (!PF.isNull(searchData.itemCode)) {
                delete searchData.itemCode;
            }
            if (!PF.isNull(searchData.itemName)) {
                delete searchData.itemName;
            }

            searchData.pageSize = searchData.pageSize;
            IOT.getServerData(
                "/inventorys/findGroupByWareOrgItem",
                "get",
                searchData,
                (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;

                        this.inventoryBalances = list;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        exportInventory() {
            let searchData = PF.JSON(this.searchData);
            if (!PF.isNull(searchData.itemCode)) {
                delete searchData.itemCode;
            }
            if (!PF.isNull(searchData.itemName)) {
                delete searchData.itemName;
            }

            searchData.pageSize = searchData.pageSize;
            IOT.showOverlay("正在导出，请稍等...");

            IOT.getServerData(
                "/inventorys/exportFindGroupByWareOrgItem",
                "get",
                searchData,
                (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        var path = ret.data; 
                        window.location.href=path;
                     //   window.open(path);
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                },
                true
            );
        },
        resetForm(formName) {
            // 重置
            this.$refs[formName].resetFields();
        },

        handleSelectionChange(val) {
            this.multipleSelection = val;
        },

        changePackDetailId(index) {
            let detail = PF.JSON(this.inventoryBalances[index]);
            for (let i = 0; i < detail.packDetails.length; i++) {
                if (
                    detail.packDetails[i].packDetailId1 === detail.packDetailId
                ) {
                    this.inventoryBalances[index].packDescribe =
                        detail.packDetails[i].packDescribe1;
                    this.inventoryBalances[index].transRatio =
                        detail.packDetails[i].transRatio1;
                }
            }
        },
    },
    created() {
        this.getInventoryBalanceData();

        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
    },
    mounted() {},
};
</script>

<style scoped lang="less">
.inventoryBalance {
}
</style>
