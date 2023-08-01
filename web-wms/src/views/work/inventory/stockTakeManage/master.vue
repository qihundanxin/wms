<template>
    <div class="freeze">
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
                    <span>关键字搜索</span>
                    <el-input
                        v-model="searchData.keyWords"
                        size="mini"
                        placeholder="SKU名称/SKU"
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
                type="primary"
                round
                icon="iconfont icon-xinzeng"
                @click="$router.push({ name: 'CreateStockMasterTask' })"
            >
                新增</el-button
            >
            <el-button
                size="small"
                type="danger"
                round
                icon="iconfont icon-shanchu"
                @click="deleteStoreTake()"
                :disabled="multipleSelection.length === 0"
                >删除</el-button
            >
        </div>

        <el-dialog
            title="新增库存调整单"
            :visible.sync="dialogNewVisible"
            :before-close="handleClose"
            width="600px"
            style="margin-top: -10vh"
        >
            <el-form :model="form" :rules="rules" ref="form">
                <el-form-item label="货主" :label-width="formLabelWidth">
                    <el-input
                        v-model="form.organizationName"
                        placeholder="请输入"
                        disabled="true"
                    ></el-input>
                </el-form-item>
                <el-form-item label="盘点单号" :label-width="formLabelWidth">
                    <el-input
                        v-model="form.billNo"
                        placeholder=""
                        disabled="true"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="调整原因"
                    prop="reason"
                    :label-width="formLabelWidth"
                >
                    <el-select
                        v-model="form.reason"
                        filterable
                        placeholder="请输入"
                        allow-create
                    >
                        <el-option
                            v-for="item in reasons"
                            :key="item.reason"
                            :label="item.reason"
                            :value="item.reason"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="原因描述"
                    prop="reasonDescription"
                    :label-width="formLabelWidth"
                >
                    <el-input
                        v-model="form.reasonDescription"
                        placeholder="请输入原因描述"
                    ></el-input>
                </el-form-item>
                <el-form-item label="仓库" :label-width="formLabelWidth">
                    <el-input
                        v-model="form.wareName"
                        placeholder=""
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="备注"
                    prop="memo"
                    :label-width="formLabelWidth"
                >
                    <el-input
                        v-model="form.memo"
                        placeholder="请输入备注"
                    ></el-input>
                </el-form-item>
            </el-form>
            

            <div slot="footer" class="dialog-footer">
                
                <el-button
                    @click="
                        resetForm('form');
                        dialogNewVisible = false;
                    "
                    >取 消</el-button
                >
                <el-button type="primary" @click="commitForm('form', 0)"
                    >确 定</el-button
                >
            </div>
        </el-dialog>

        <div class="tableBox">
            <el-table
                v-loading="loading.masterTable"
                highlight-current-row
                @selection-change="handleSelectionChange"
                :data="tasks"
                style="width: 100%"
            >
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column fixed="right" label="操作" width="150">
                    <template slot-scope="scope">
                        <!-- <el-button
                            @click="
                                $router.push({
                                    name: 'UpdateStockMasterTask',
                                    params: scope.row,
                                })
                            "
                            icon="iconfont icon-bianji"
                            type="primary"
                            circle
                            plain
                            size="small"
                            :disabled="scope.row.state !== 0"
                            >&nbsp;修改</el-button
                        > -->
                        <el-button
                        :disabled="scope.row.state!=5"
                        
                            @click="commitForm('form', scope.row.stockTakeMasterId)"
                            
                            type="primary"
                       
                            size="small"
                          
                            >&nbsp;库存调整</el-button
                        >
                    </template>
                </el-table-column>
                <el-table-column fixed="left" type="index" width="50">
                </el-table-column>
                <el-table-column
                    fixed="left"
                    sortable
                    prop="billNo"
                    label="盘点单号"
                    width="150"
                >
                    <template slot-scope="scope">
                        <span
                            class="textBtn"
                            @click="
                                $router.push({
                                    name: 'StockMasterTaskDetail',
                                    params: scope.row,
                                })
                            "
                            >{{ scope.row.billNo }}</span
                        >
                     
                    </template>
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="state"
                    label="状态"
                    width="150"
                >
                    <template slot-scope="scope">
                        <span   v-for="a in stockTaskMasterState" v-if="a.value==scope.row.state"   >{{a.label}}</span >
                    </template>
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="organizationName"
                    label="货主"
                    width="150"
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
                    sortable
                    show-overflow-tooltip
                    prop="createUserName"
                    label="创建人"
                    width="150"
                >
                </el-table-column>
                 <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="itemCode"
                    label="SKU" 
                    width="150"
                >
                 </el-table-column>
                <el-table-column
                    sortable
                    prop="itemName"
                    label="SKU名称"
                    width="150"
                >
                </el-table-column>
               
               
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="dynamicFromTime"
                    label="盘点开始时间"
                    width="150"
                >
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="dynamicToTime"
                    label="盘点结束时间"
                    width="150"
                >
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="boxCode"
                    label="追踪号"
                    width="150"
                >
                </el-table-column>
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="fromAreaCode"
                    label="货区"
                    width="150"
                >
                </el-table-column>
                <!-- <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="toAreaCode"
                    label="货区TO"
                    width="150"
                >
                </el-table-column> -->
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="fromCellCode"
                    label="货位"
                    width="150"
                >
                </el-table-column>
                <!-- <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="toCellCode"
                    label="货位TO"
                    width="150"
                >
                </el-table-column> -->
                <!-- <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="fromShelfCode"
                    label="货位组TO"
                    width="150"
                > 
                </el-table-column>-->
                <el-table-column
                    sortable
                    show-overflow-tooltip
                    v-for="(item, index) in asnUdfHs"
                    :key="index"
                    :prop="item.paramCode"
                    :label="item.value"
                    width="180"
                >
                </el-table-column>
                <!-- <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="randomNum"
                    label="任务数"
                    width="150"
                >
                </el-table-column> -->
            </el-table>
            <el-pagination
                style="text-align: right"
                class="pagination-bottom"
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
import { mapState } from "vuex";

export default {
    name: "StockTakeMaster",
    data() {
        return {
            isMoreSearch: false,
            isMoreSearch2: false,

            searchData: {
                pageNum: 1,
                pageSize: 15,
                total: null,
                wareId: PF.getLocal("wareId", "number"),
                keyWords: "",
            },
            searchSelectInventory: {
                pageNum: 1,
                pageSize: 5,
                total: null,
                keyWords: "",
                wareId: null,
                organizationId: null,
                itemCode: null,
                itemName: null,
            },

            loading: {
                masterTable: true,
            },

            wares: [],
            organizations: [],
            items: [],
            tasks: [],
            inventorys: [],
            multipleSelection: [],
            selectIds: [],
            asnUdfHs: [],
            reasons: [],

            dialogNewVisible: false,
            dialogUpdateVisible: false,
            innerVisibleInventory: false,

            formLabelWidth: "120px",

            rowData: {},
            form: {
                stockTakeMasterId: "",
                organizationName: "",
                organizationId: "",
                billNo: "",
                reason: "",
                reasonDescription: "",
                wareName: "",
                wareId: "",
                wareCode: "",
                memo: "",
                type: 1,
            },
            rules: {
                reason: [
                    { required: true, message: "请输入原因", trigger: "blur" },
                ],
                reasonDescription: [
                    {
                        required: true,
                        message: "请输入原因描述",
                        trigger: "blur",
                    },
                ],
                memo: [
                    { required: true, message: "请输入备注", trigger: "blur" },
                ],
            },
        };
    },
    computed: {
        ...mapState([
            "stockTaskMasterType",
            "stockTaskMasterState",
            "stockTaskMasterOrder",
            "stockTaskDetailType",
            "stockTaskDetailState",
        ]),
        isFreezeBtn() {
            if (this.multipleSelection.length <= 0) {
                return true;
            }
            let organizationId = this.multipleSelection[0].organizationId;
            for (let i = 0; i < this.multipleSelection.length; i++) {
                if (
                    organizationId !== this.multipleSelection[i].organizationId
                ) {
                    return true;
                }
            }
            return false;
        },
    },
    methods: {
        showNewInventoryDetail(row) {
            this.dialogNewVisible = true;
            let rowData = JSON.parse(JSON.stringify(row));
            this.form.stockTakeMasterId = rowData.stockTakeMasterId;
            this.form.organizationName = rowData.organizationName;
            this.form.organizationId = rowData.organizationId;
            this.form.billNo = rowData.billNo;
            this.form.wareName = rowData.wareName;
            this.form.wareId = rowData.wareId;
            this.form.wareCode = rowData.wareCode;
        },
        deleteStoreTake() {
            let that = this;
            IOT.showOverlay("保存中，请稍等...");
            let form = [];
            form = PF.JSON(this.multipleSelection);
            let ids = [];
            for (let i = 0; i < form.length; i++) {
                ids.push(form[i].stockTakeMasterId);
            }
            IOT.getServerData(
                "/stock/take/masters/deleteByIds",
                "post",
                { ids: ids },
                (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips("取消成功！", "success", 1000, () => {
                            that.getFreezeData();
                        });
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        handleSizeChange(val) {
            this.searchData.pageSize = val;
            this.getFreezeData();
        },
        handleCurrentChange(val) {
            this.pageNum = val;
            this.getFreezeData();
        },
        handleCurrentChange2(val) {
            this.searchSelectInventory.pageNum = val;
            this.getInventoryData();
        },
        clearSearchData() {
            this.searchData.keyWords = "";
        },
        clearSearchSelectInventory() {
            this.searchSelectInventory.keyWords = "";
            this.searchSelectInventory.wareId = null;
            this.searchSelectInventory.organizationId = null;

        },
        setPageNumFlush(){
            this.searchData.pageNum =1;
            this.getFreezeData();
        },
        getFreezeData() {
            let that = this;
            that.loading.masterTable = true;

            IOT.getServerData(
                "/stock/take/masters/list",
                "get",
                this.searchData,
                (ret) => {
                    if (ret.code === 200) {
                        this.tasks = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber;
                        that.loading.masterTable = false;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },

        getWareData() {
            IOT.getServerData("/ware/infos/findSome", "get", {}, (ret) => {
                if (ret.code === 200) {
                    this.wares = ret.rows;
                } else {
                    IOT.tips(
                        ret.message || "服务器请求失败，稍后再试！",
                        "error"
                    );
                }
            });
        },
        getReasonData() {
            IOT.getServerData("/reasons/list", "get", {}, (ret) => {
                if (ret.code === 200) {
                    this.reasons = ret.rows;
                } else {
                    IOT.tips(
                        ret.message || "服务器请求失败，稍后再试！",
                        "error"
                    );
                }
            });
        },
        getItemData() {
            IOT.getServerData("/item/infos/list", "get", {}, (ret) => {
                if (ret.code === 200) {
                    this.items = ret.rows;
                } else {
                    IOT.tips(
                        ret.message || "服务器请求失败，稍后再试！",
                        "error"
                    );
                }
            });
        },
        getInventoryData() {
            IOT.getServerData(
                "/inventorys/list",
                "get",
                this.searchSelectInventory,
                (ret) => {
                    if (ret.code === 200) {
                        this.inventorys = ret.rows;
                        this.searchSelectInventory.total = ret.total;
                        this.searchSelectInventory.pageNum = ret.pageNumber;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        resetForm(formName) {
            // 重置
            this.$refs[formName].resetFields();
        },
        handleClose() {
            this.resetForm("form");
            this.dialogNewVisible = false;
        },
        showSelectInventoryModal() {
            this.selectIds = [];
            for (let i = 0; i < this.form.tasks.length; i++) {
                this.selectIds.push(this.form.tasks[i].inventoryId);
            }
            this.getInventoryData();
            this.innerVisibleInventory = true;
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        clickSelectInventory() {
            let list = PF.JSON(this.multipleSelection);
            this.form.freezeMaster.wareId = list[0].wareId;
            this.form.freezeMaster.wareName = list[0].wareName;
            this.form.freezeMaster.organizationId = list[0].organizationId;
            this.form.freezeMaster.organizationName = list[0].organizationName;

            let isRepeat = false;
            for (let i = 0; i < list.length; i++) {
                list[i].nowFreezeQuantity = list[i].freezeQuantity;
                list[i].freezeQuantity = 0;
                if (this.form.tasks.length > 0) {
                    if (
                        this.selectIds.indexOf(list[i].inventoryId) === -1 &&
                        list[i].organizationId ===
                            this.form.tasks[0].organizationId
                    ) {
                        this.form.tasks.push(list[i]);
                    } else {
                        isRepeat = true;
                    }
                } else {
                    if (this.selectIds.indexOf(list[i].inventoryId) === -1) {
                        this.form.tasks.push(list[i]);
                    } else {
                        isRepeat = true;
                    }
                }
            }
            if (isRepeat) {
                IOT.tips("不能重复添加或添加非此货主！", "error");
            }
            this.innerVisibleInventory = false;
        },
        deleteDetail(index) {
            this.form.tasks.splice(index, 1);
        },
        commitForm(formName, pdId) {
            let that = this;
             
                     IOT.showOverlay("保存中，请稍等...");
                    IOT.getServerData(
                        "/inventory/adjusts/createByPd",
                        "get",
                        {
                            pdId,pdId
                        },
                        (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips("保存成功！", "success", 1000, () => {
                                    that.getFreezeData();
                                    this.getReasonData();
                                });
                            } else {
                                IOT.tips(
                                    ret.message || "服务器请求失败，稍后再试！",
                                    "error"
                                );
                            }
                        }
                    );
                    this.resetForm("form");
                    this.dialogNewVisible = false;
                 
        },
    },
    created() {
        this.getFreezeData();
        this.getReasonData();
        PF.getDataDictUdfData("freeze_reason", (rows) => {
            this.freezeReasons = rows;
        });
        this.getWareData();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
    },
    mounted() {},
};
</script>

<style scoped lang="less">
.freeze {
}
</style>
