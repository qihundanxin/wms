<template>
    <div class="ivTransOrgCreate">
        <el-dialog title="选择库存" :visible.sync="dialogInventoryVisible" :before-close="handleClose" width="900px"
                   style="margin-top: -10vh;">

            <div class="publicSearch">
                <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                    <el-button-group>
                        <el-button @click="getInventoryData" size="mini" icon="el-icon-search">查询</el-button>
                        <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                    </el-button-group>
                    <div>
                        <span>货主</span>
                        <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主"
                                   clearable>
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </div>
                    <div>
                        <span>SKU名称</span>
                        <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
                    </div>
                    <div>
                        <span>SKU</span>
                        <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
                    </div>
                    <div>
                        <span>货位</span>
                        <el-input v-model="searchData.cellCode" size="mini" placeholder="货位" clearable></el-input>
                    </div>
                    <div>
                        <span>关键字搜索</span>
                        <el-input v-model="searchData.keyWords" size="mini" placeholder="批次/追踪号/LPN号"
                                  clearable></el-input>
                    </div>
                </div>
                <span class="moreSearchBtn"
                      :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                      @click="isMoreSearch = !isMoreSearch"></span>
            </div>

            <el-table
                    highlight-current-row
                    :data="inventories"
                    @selection-change="handleSelectionChange"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        fixed="left"
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        fixed="left"
                        sortable
                        prop="itemName"
                        label="SKU名称"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="itemCode"
                        label="SKU"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="wareName"
                        label="仓库"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="organizationName"
                        label="货主"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn"
                              @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="batchName"
                        label="批次"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.batchId, 'batch')">{{scope.row.batchName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="quantity"
                        label="数量"
                        width="150">
                    <template slot-scope="scope">
                        <span>{{ Math.floor(scope.row.quantity / scope.row.transRatio * 10000) / 10000 }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="packDescribe"
                        label="单位"
                        width="150">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
                                   filterable placeholder="请选择单位" size="small">
                            <el-option
                                    v-for="item in scope.row.packDetails"
                                    :key="item.packDetailId1"
                                    :label="item.packDescribe1"
                                    :value="item.packDetailId1">
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="allotQuantity"
                        label="分配数量"
                        width="150">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="freezeQuantity"
                        label="冻结数量"
                        width="150">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="cellCode"
                        label="货位"
                        width="180">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="boxCode"
                        label="追踪号"
                        width="180">
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="lpn"
                        label="LPN码"
                        width="180">
                </el-table-column>
            </el-table>
            <el-pagination
              class="pagination-bottom"
                    style="text-align: right;"
                    @current-change="handleCurrentChange"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>

            <div slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="commitSelections">确 定</el-button>
            </div>
        </el-dialog>

        <div class="publicSearch bigFormBox3">
            <div class="searchBar" style="height: auto">
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">货权转移单信息</p>
                <el-form class="clearFloat" :model="form.ivTransOrgMaster" :rules="rules" ref="form.ivTransOrgMaster">
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-input disabled v-model="form.ivTransOrgMaster.wareName" size="mini"
                                  style="width: 100%;"></el-input>
                    </el-form-item>
                    <el-form-item label="转出货主" :label-width="formLabelWidth">
                        <el-select v-model="form.ivTransOrgMaster.fromOrgId" size="mini" filterable placeholder="请选择货主" disabled>
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="转入货主" :label-width="formLabelWidth">
                        <el-select
                                v-model="form.ivTransOrgMaster.toOrgId"
                                size="mini"
                                style="width: 100%">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>
        </div>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="form.ivTransOrgDetails"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="index"
                        width="50">
                </el-table-column>
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
                        label="数量"
                ></el-table-column>
                <el-table-column
                        fixed="left"
                        label="操作"
                        width="60">
                    <template slot-scope="scope">
                        <el-button @click="deleteRow(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle
                                   plain
                                   size="small"></el-button>
                    </template>
                </el-table-column>

            </el-table>
        </div>

        <div class="createFootBox clearFloat"
             :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="showInventories">选择库存
            </el-button>
            <div class="rightFloat">
                <el-button @click="commitForm('form.ivTransOrgMaster')" type="primary">提 交</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'IvTransOrgCreate',
        data() {
            return {
                loading: {
                    inventory: true,
                },
                wares: [],
                searchData: {
                    pageNum: 1,
                    pageSize: 5,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number'),
                    organizationId: null,
                    itemName: '',
                    itemCode: '',
                    cellCode: ''
                },
                organizations: [],
                formLabelWidth: '120px',

                form: {
                    ivTransOrgMaster: {
                        fromOrgId: null,
                        fromOrgName: '',
                        toOrgId: null,
                        toOrgName: '',
                        wareId: PF.getLocal('wareId', 'number'),
                        wareName: PF.getLocal('wareName')
                    },
                    ivTransOrgDetails: []
                },
                ivTransOrgDetail: {},
                rules: {},
                dialogInventoryVisible: false,

                inventories: [],
                selections: [],
                isMoreSearch: false
            }
        },
        computed: {},
        watch: {
            organizationId(val) {
                for (let i = 0; i < this.organizations.length; i++) {
                    if (this.organizations[i].organizationId === val) {
                        this.form.asnMaster.organizationName = this.organizations[i].organizationName;
                    }
                }
            },
        },
        methods: {
            handleSelectionChange(val) {
                this.selections = val
            },
            handleCurrentChange(val) {
                this.searchData.pageNum = val
                this.getInventoryData()
            },
            clearSearchData() {
                this.searchData = {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number'),
                    organizationId: null,
                    itemName: '',
                    itemCode: '',
                    cellCode: ''
                }
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
            showInventories() {
                this.dialogInventoryVisible = true
            },
            deleteRow(index) {
                this.form.ivTransOrgDetails.splice(index, 1);
            },

            commitForm(formName) {
                for (let item of this.organizations) {
                    if (item.organizationId === this.form.ivTransOrgMaster.fromOrgId) {
                        this.form.ivTransOrgMaster.fromOrgName = item.organizationName
                    }
                    if (item.organizationId === this.form.ivTransOrgMaster.toOrgId) {
                        this.form.ivTransOrgMaster.toOrgName = item.organizationName
                    }
                }
                IOT.showOverlay('保存中~~~')
                IOT.getServerData('/iv/trans/org/masters/insert', 'post', this.form, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.message || '保存成功！', 'success')
                        this.form = {
                            ivTransOrgMaster: {
                                fromOrgId: null,
                                fromOrgName: '',
                                toOrgId: null,
                                toOrgName: '',
                                wareId: null,
                                wareName: ''
                            },
                            ivTransOrgDetails: []
                        }
                        this.$router.go(-1)
                    }
                })
            },
            handleClose() {
                this.dialogInventoryVisible = false
            },

            getInventoryData() {
                let that = this
                if (!PF.isNull(this.searchData.organizationId)) {
                    IOT.tips('请选择货主！', 'error');
                    return false
                }
                IOT.getServerData('/inventorys/list', 'get', this.searchData, (ret) => {
                    if (ret.code === 200) {
                        that.inventories = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitSelections() {
                for (let item of this.selections) {
                    let ivTransOrgDetail = {
                        masterId: null,
                        itemCode: item.itemCode,
                        itemName: item.itemName,
                        inventoryId: item.inventoryId,
                        quantity: item.quantity
                    }
                    this.form.ivTransOrgDetails.push(ivTransOrgDetail)
                }
                this.dialogInventoryVisible = false
                this.form.ivTransOrgMaster.fromOrgId = this.searchData.organizationId
                this.clearSearchData()
            }
        },
        created() {
            this.getWareData()
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
</style>