<template>
    <div class="asnBill">

        <el-dialog title="新建组合产品" :visible.sync="dialogNewVisible" :before-close="handleClose3" width="900px">
            <el-form :model="form" :rules="rule" ref="form" class="width-format">
                <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
                    <el-input v-model="form.itemCode" size="mini" @focus="showItemModal1"></el-input>
                </el-form-item>
                <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
                    <el-input v-model="form.itemName" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="费率" prop="price" :label-width="formLabelWidth">
                    <el-input-number :min="0" size="mini" v-model="form.price"></el-input-number>
                </el-form-item>
                <el-form-item label="工序" prop="procedure" :label-width="formLabelWidth">
                    <el-input size="mini" v-model="form.procedure"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="state" :label-width="formLabelWidth">
                    <el-select v-model="form.state" size="mini">
                        <el-option
                                v-for="item in states"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="包装材料标志" prop="isPacking" :label-width="formLabelWidth">
                    <el-checkbox v-model="form.isPacking"></el-checkbox>
                </el-form-item>
                <el-form-item label="desc1" prop="desc1" :label-width="formLabelWidth">
                    <el-input v-model="form.desc1" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="desc2" prop="desc2" :label-width="formLabelWidth">
                    <el-input v-model="form.desc2" size="mini"></el-input>
                </el-form-item>
<!--                <el-form-item label="版本" prop="ver" :label-width="formLabelWidth">-->
<!--                    <el-input v-model="form.ver" size="mini"></el-input>-->
<!--                </el-form-item>-->
                <el-form-item>
                    <el-button type="primary" class="rightFloat" @click="commitForm1('form')">提交</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-dialog title="设置组件信息" :visible.sync="dialogComponentSetVisible" :before-close="handleClose2" width="900px"
                   style="margin-top: -5vh;">
            <div class="itemNewModal">
                   <el-form class="clearFloat" :model="itemComponent" :rules="rules" ref="componentForm">
                       <el-form-item label="组合SKU" prop="combinedItemCode" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.combinedItemCode" disabled size="mini"></el-input>
                       </el-form-item>
                       <el-form-item label="组件SKU" prop="componentItemCode" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.componentItemCode" disabled size="mini"></el-input>
                       </el-form-item>
                       <!-- <el-form-item label="组件SKU名称" prop="componentItemName" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.componentItemName" disabled size="mini"></el-input>
                       </el-form-item> -->
                       <el-form-item label="数量" prop="quantity" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.quantity" size="mini"></el-input>
                       </el-form-item>
                       <el-form-item label="工序" prop="workIndex" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.workIndex" size="mini"></el-input>
                       </el-form-item>
                       <el-form-item label="desc1" prop="desc1" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.desc1" size="mini"></el-input>
                       </el-form-item>
                       <el-form-item label="desc2" prop="desc2" :label-width="formLabelWidth">
                           <el-input v-model="itemComponent.desc2" size="mini"></el-input>
                       </el-form-item>
                   </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="commitForm('componentForm')">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog width="900px" title="选择物料" :visible.sync="dialogItemsVisible" :before-close="handleClose" style="margin-top: -10vh;">

            <div class="publicSearch body">
                <div class="searchBar" style="height: auto">
                    <el-button-group>
                        <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                        <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                    </el-button-group>
                    <div>
                        <span>关键字搜索</span>
                        <el-input v-model="itemSearchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                    </div>
                </div>
            </div>

            <div class="tableBox">
                <el-table
                        max-height="460"
                        :data="items"
                        @current-change="handleItemSelChange"
                        style="width: 100%">
                    <el-table-column
                            fixed="left"
                            label="操作"
                            width="150">
                        <template slot-scope="scope">
                            <el-button @click="SelectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain size="small"></el-button>
                        </template>
                    </el-table-column>
                    <el-table-column
                            fixed="left"
                            type="index"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            fixed="left"
                            prop="itemName"
                            label="SKU名称"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="itemCode"
                            label="SKU"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="itemTypeName"
                            label="物料种类"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="supplierName"
                            label="供应商"
                            width="180">
                    </el-table-column>
                    <!--<el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="organizationName"
                            label="货主"
                            width="180">
                    </el-table-column>-->
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="batchTacticName"
                            label="批次策略"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="upperTacticName"
                            label="上架策略"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="defaultPrice"
                            label="默认单价"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="spec"
                            label="规格"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="model"
                            label="型号"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="packName"
                            label="包装编码"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="upperLimit"
                            label="库存上限"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="floorLimit"
                            label="库存下限"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            sortable
                            show-overflow-tooltip
                            prop="weight"
                            label="重量"
                            width="180">
                    </el-table-column>

                </el-table>
                <el-pagination
                        style="text-align: right;"
                        @current-change="handleItemSelChange"
                        :current-page.sync="itemSearchData.pageNum"
                        :page-size="itemSearchData.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="itemSearchData.total">
                </el-pagination>
            </div>

        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getItemComponentsData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>

            </div>

        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
        </div>

        <div class="tableBox organizationLeftTab">
            <el-table
                    highlight-current-row
                    @current-change="handleSelChange"
                    :data="itemCombineds"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="itemCode"
                        label="SKU"
                >
                </el-table-column>
                <!-- <el-table-column
                        sortable
                        prop="itemName"
                        label="SKU名称"
                >
                </el-table-column> -->

                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="deleteWaveConfirm(scope.row.id)" icon="iconfont icon-shanchu"
                                   type="danger" circle
                                   plain size="small"></el-button>
                    </template>
                </el-table-column>

            </el-table>
            <el-pagination
                    style="text-align: right;"
                    class="pagination-left-bottom"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    :page-sizes="[15,30,50,100]"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    layout="sizes, total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

        <div class="organizationMiddle">
            <i class="el-icon-d-arrow-right"></i>
        </div>

        <div class="organizationRight">
            <div class="organizationRightTitle">组件信息</div>
            
            <div style="margin-left: 20px;">
                <p class="detailItemBox">
                    <span><b>SKU名称：</b>{{itemCombined.itemName}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>SKU：</b>{{itemCombined.itemCode}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>费率：</b>{{itemCombined.price}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>工序描述：</b>{{itemCombined.procedure}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>状态：</b>{{itemCombined.state === 1 ? '激活' : '未激活'}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>包装材料标志：</b>{{itemCombined.isPacking === 1 ? '是' : '否'}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>desc1：</b>{{itemCombined.desc1}}</span>
                </p>
                <p class="detailItemBox">
                    <span><b>desc2：</b>{{itemCombined.desc2}}</span>
                </p>
            </div>

            <el-divider content-position="left">详细信息</el-divider>
            <div class="headerBtns">

            </div>
            <div class="tableBox table-300h">
                <el-table
                        highlight-current-row
                        :data="itemComponents"
                        style="width: 100%">
                    <el-table-column
                            prop="componentItemCode"
                            label="SKU"
                    ></el-table-column>
                    <!-- <el-table-column
                            prop="componentItemName"
                            label="SKU名称"
                    ></el-table-column> -->
                    <el-table-column
                            prop="quantity"
                            label="数量">
                    </el-table-column>
                    <el-table-column
                            prop="workIndex"
                            label="工序">
                    </el-table-column>
                    <el-table-column
                            prop="desc1"
                            label="desc1">
                    </el-table-column>
                    <el-table-column
                            prop="desc2"
                            label="desc2">
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
                <el-button @click="showItemModal2">添加组件
                </el-button>
            </div>

        </div>

    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'ItemCombined',
        data() {
            return {
                isMoreSearch: false,
                activeName: 'first',
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    itemCode: '',
                    combinedItemCode: ''
                },
                itemSearchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: 0,
                    keyWords: ''
                },
                pickTaskSearch: {},

                itemCombineds: [],
                itemCombined: {},
                itemComponents: [],
                itemComponent: {
                    combinedItemCode: '',
                    componentItemCode: '',
                    componentItemName: '',
                    quantity: null,
                    workIndex: null,
                    desc1: '',
                    desc2: ''
                },
                pickTasks: [],

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],

                itemSelections: [],

                dialogItemsVisible: false,
                dialogComponentSetVisible: false,
                dialogNewVisible: false,
                items: [],
                rules: [],
                form: {
                    itemCode: '',
                    itemName: '',
                    desc1: '',
                    desc2: '',
                    price: null,
                    procedure: '',
                    state: 1,
                    isPacking: '',
                    ver: '',
                    organizationId: null
                },
                rule: {},
                itemTag: null,
                states: [
                    {
                        label: '激活',
                        value: 1
                    },
                    {
                        label: '未激活',
                        value: 0
                    }
                ]
            }
        },
        computed: {
            ...mapState([])
        },
        methods: {
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getItemCombinedData();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getItemCombinedData()
            },
            handleSelChange(rowData) {
                this.childSearchData.combinedItemCode = rowData.itemCode
                this.itemCombined = JSON.parse(JSON.stringify(rowData))
                this.getItemComponentsData()
            },
            handleNewWaveSelChange() {

            },
            handleItemSelChange(val) {
                this.searchData.pageNum = val;
                this.getItemData()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.supplierId = null;
            },
            clearItemSearchData() {
              this.itemSearchData.keyWords = ''
            },
            getItemCombinedData() {
                IOT.getServerData('/item/combined/list', 'post', {}, (ret) => {
                    if (ret.code === 200) {
                        this.itemCombineds = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                        this.getItemComponentsData()
                        if (ret.rows.length == 0) {
                            return
                        }
                        this.childSearchData.combinedItemCode = ret.rows[0].itemCode
                        this.handleSelChange(ret.rows[0])
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getItemComponentsData() {
                IOT.getServerData('/item/components/list', 'post', this.childSearchData, (ret) => {
                    if (ret.code === 200) {
                        this.itemComponents = ret.rows
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

            deleteWaveConfirm(id) {
                this.$confirm('确认删除组合产品及组件吗？')
                    .then(_ => {
                        this.deleteItemCombined(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteItemCombined(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/item/combined/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getItemCombinedData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteItemComponent(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteItemComponent(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/item/components/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getItemComponentsData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            showItemModal1() {
                this.dialogItemsVisible = true
                this.itemTag = 1
            },
            showItemModal2() {
                this.dialogItemsVisible = true
                this.itemTag = 2
            },
            handleClose() {
                this.dialogItemsVisible = false
            },
            getItemData() {
                let param = this.itemSearchData
                IOT.getServerData('/item/infos/list', 'get', param, (ret) => {
                    if (ret.code === 200) {
                        this.items = ret.rows;
                        this.itemSearchData.total = ret.total
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            SelectItem(row) {
                if (this.itemTag === 1) {
                    this.form.itemCode = row.itemCode
                    this.form.itemName = row.itemName
                    this.dialogItemsVisible = false

                } else if (this.itemTag === 2) {
                    this.itemComponent.combinedItemCode = this.itemCombined.itemCode
                    this.itemComponent.componentItemCode = row.itemCode
                    this.itemComponent.componentItemName = row.itemName
                    this.dialogItemsVisible = false
                    this.dialogComponentSetVisible = true
                }
            },
            handleClose2() {

            },
            handleClose3() {
                this.dialogNewVisible = false
            },
            commitForm(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = JSON.parse(JSON.stringify(this.itemComponent))
                        IOT.getServerData('/item/components/add', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getItemComponentsData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        // this.resetForm('form')
                        this.dialogComponentSetVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            commitForm1(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = JSON.parse(JSON.stringify(this.form))
                        form.isPacking = form.isPacking === true ? 1 : 0
                        IOT.getServerData('/item/combined/add', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.dialogNewVisible = false
                                    that.getItemCombinedData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                    }
                });
            }
        },
        created() {
            this.getItemCombinedData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
            this.getItemData()
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
    .width-format {
        .el-input,.el-input-number--mini,.el-select {
            width: 70%;
        }
        .el-form-item {
            display: inline-block;
            width: 50%;
        }
    }

</style>
