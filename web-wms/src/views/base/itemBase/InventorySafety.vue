<template>
    <div class="inventorySafety">

        <el-dialog title="新建库存安全" :visible.sync="dialogNewVisible" :before-close="newCloseHandler" width="90%"
                   style="margin-top: -5vh;">
            <div class="itemNewModal">
                <el-form class="clearFloat" :rules="rules" ref="inventorySafetyForm">
                    <el-form-item style="width:30%" label="货主" prop="organizationId" :label-width="formLabelWidth">
                        <el-select size="mini" v-model="organization.organizationId" @change="setOrganizationName" style="width: 90%">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  style="width:30%"   :label-width="formLabelWidth">
                        <el-button type="primary" size="mini" @click="dialogItemVisible = true">选择商品</el-button>
                    </el-form-item>
                    <!-- <el-form-item  style="width:30%" label="仓库" prop="wareName" :label-width="formLabelWidth">
                        <el-input size="mini" disabled style="width: 90%" v-model="wareName"></el-input>
                    </el-form-item> -->
                </el-form>

                <div style="height: 400px!important;
  
  overflow: auto;" class=" ">
                    <el-table
                            :data="newList">
                        <el-table-column
                        width="180"
                                prop="itemCode"
                                label="SKU">
                        </el-table-column>
                        <el-table-column
                                prop="itemName"
                                label="SKU名称">
                        </el-table-column>
                        <el-table-column
                                prop="ceiling"
                                label="采购补货范围">
                            <template slot-scope="scope">
                                <el-input-number v-model="scope.row.floor" size="mini"></el-input-number>
                                <el-input-number v-model="scope.row.ceiling" size="mini"></el-input-number>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="ceiling"
                                label="拣货补货范围">
                            <template slot-scope="scope">
                                <el-input-number v-model="scope.row.cpfrFloor" size="mini"></el-input-number>
                                <el-input-number v-model="scope.row.cpfrCeiling" size="mini"></el-input-number>
                            </template>
                        </el-table-column>
                        
                       <!-- <el-table-column
                               prop="description"
                               label="描述">
                           <template slot-scope="scope">
                               <el-input v-model="scope.row.description" size="mini"></el-input>
                           </template>
                       </el-table-column> -->
                    </el-table>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="clearInput">取 消</el-button>
                <el-button type="primary" @click="commitForm">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="修改库存安全" :visible.sync="dialogUpdateVisible" :before-close="updateCloseHandler" width="900px"
                   style="margin-top: -5vh;">
            <div class="itemNewModal">
                <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowDataForm">
                    <el-form-item label="采购补货范围" prop="ceiling" :label-width="formLabelWidth">
                         <el-input-number :min="0" v-model="rowData.floor" size="mini"
                                         style="width: 45%"></el-input-number>
                        <el-input-number :min="0" v-model="rowData.ceiling" size="mini"
                                         style="width: 45%"></el-input-number>
                                        
                    </el-form-item>
                     <el-form-item label="拣货补货范围" prop="ceiling" :label-width="formLabelWidth">
                         <el-input-number :min="0" v-model="rowData.cpfrFloor" size="mini"
                                         style="width: 45%"></el-input-number>
                        <el-input-number :min="0" v-model="rowData.cpfrCeiling" size="mini"
                                         style="width: 45%"></el-input-number>
                                        
                    </el-form-item>
                  
                    <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
                        <el-input v-model="rowData.itemCode" disabled @focus="dialogItemVisible = true" size="mini"
                                  style="width: 90%"></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="单位" prop="packDescribe" :label-width="formLabelWidth">
                        <el-input v-model="rowData.packDescribe" size="mini" disabled></el-input>
                    </el-form-item> -->
                    <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
                        <el-input v-model="rowData.itemName" disabled size="mini" style="width: 90%"></el-input>
                    </el-form-item>
                    <el-form-item label="仓库" prop="wareName" :label-width="formLabelWidth">
                        <el-input v-model="rowData.wareName" size="mini" disabled style="width: 90%"></el-input>
                    </el-form-item>
                    <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                        <el-select size="mini" disabled v-model="rowData.organizationId" style="width: 90%">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organization"
                                    :label="item.organizationName"
                                    :value="item.organizationId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="commitForm2('rowDataForm')">确 定</el-button>
            </div>
        </el-dialog>


        <el-dialog width="900px" title="选择商品" :visible.sync="dialogItemVisible" :before-close="itemCloseHandler"
                   style="margin-top: -10vh;">

            <div class="publicSearch body">
                <div class="searchBar" style="height: auto">
                    <el-button-group>
                        <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                        <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                    </el-button-group>
                    <div>
                        <span>关键字搜索</span>
                        <el-input v-model="itemSearchData.keyWords" size="mini" placeholder="SKU名称/SKU"
                                  clearable></el-input>
                    </div>
                </div>
            </div>

            <div class=" ">
                <el-table
                        max-height="460"
                        :data="items"
                        @selection-change="handleItemSelChange"
                        style="width: 100%">
                    <el-table-column
                            type="selection"
                            width="55">
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
                            label="商品种类"
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
            <div class="el-dialog__footer">
                <el-button type="primary" @click="commitItems">确定</el-button>
            </div>

        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" style="height:auto">
                <el-button-group>
                    <el-button @click="getInventorySafetyData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                </div>

            </div>

        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">
                新增
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    v-loading="itemTableLoading"
                    highlight-current-row
                    :data="inventorySafeties"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="selection"
                        width="55">
                </el-table-column>
                
               
            
                <el-table-column
                        prop="itemCode"
                        label="SKU/名称"
                        width="500"
                >
                 <template slot-scope="scope">
                        <span>{{scope.row.itemCode}}</span>
                        <br/>
                        <span>{{scope.row.itemName}}</span>
                    </template>
                </el-table-column>
                <!-- <el-table-column
                        prop="itemName"
                        label="SKU名称"
                >
                </el-table-column> -->
                <!-- <el-table-column
                        prop="wareName"
                        label="仓库"
                >
                </el-table-column> -->
                <el-table-column
                        prop="organizationName"
                        label="货主"
                >
                </el-table-column>

                <el-table-column
                        prop="ceiling"
                        label="采购补货范围"
                >
                 <template slot-scope="scope">
                        <span>{{scope.row.floor}}——{{scope.row.ceiling}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="ceiling"
                        label="拣货补货范围"
                >
                 <template slot-scope="scope">
                        <span>{{scope.row.cpfrFloor}}——{{scope.row.cpfrCeiling}}</span>
                    </template>
                </el-table-column>

                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="editItem(scope.row)" icon="iconfont icon-bianji"
                                   type="primary" circle
                                   plain size="small"></el-button>
                        <el-button @click="deleteConfirm(scope.row.id)" icon="iconfont icon-shanchu"
                                   type="danger" circle
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
        name: 'InventorySafety',
        data() {
            let validateCeiling = (rule, value, callback) => {
                if (this.inventorySafety.ceiling >= this.inventorySafety.floor) {
                    callback();
                } else {
                    callback(new Error('上限不能小于下限'));
                }
            };
            let validateFloor = (rule, value, callback) => {
                if (this.inventorySafety.floor <= this.inventorySafety.ceiling) {
                    callback();
                } else {
                    callback(new Error('下限不能大于上限'));
                }
            };
            let validateCeiling2 = (rule, value, callback) => {
                if (this.rowData.ceiling >= this.rowData.floor) {
                    callback();
                } else {
                    callback(new Error('上限不能小于下限'));
                }
            };
            let validateFloor2 = (rule, value, callback) => {
                if (this.rowData.floor <= this.rowData.ceiling) {
                    callback();
                } else {
                    callback(new Error('下限不能大于上限'));
                }
            };
            return {
                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    keyWords: '', 
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    itemCode: '',
                    combinedItemCode: ''
                },
                itemSearchData: {
                    organizationId:'',
                    pageNum: 1,
                    pageSize: 10,
                    total: 0,
                    keyWords: ''
                },
                inventorySafeties: [],
                itemList: [],
                newList: [],
                organization: {
                    organizationId: null,
                    organizationName: ''
                },
                wareId: PF.getLocal('wareId', 'number'),
                wareName: PF.getLocal('wareName'),
                packDetails: [],


                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],

                itemSelections: [],

                dialogNewVisible: false,
                dialogUpdateVisible: false,
                dialogItemVisible: false,
                items: [],
                rules: {
                    floor: [
                        {required: true, message: '不得为空', trigger: 'blur'},
                        {validator: validateFloor, trigger: 'blur'}
                    ],
                    ceiling: [
                        {required: true, message: '不得为空', trigger: 'blur'},
                        {validator: validateCeiling, trigger: 'blur'}
                    ],
                    itemCode: [
                        {required: true, message: '不得为空', trigger: 'blur'}
                    ],
                    packDetailId: [
                        {required: true, message: '不得为空', trigger: 'blur'}
                    ],
                    organizationId: [
                        {required: true, message: '不得为空', trigger: 'blur'}
                    ]
                },
                rules2: {
                    floor: [
                        {required: true, message: '不得为空', trigger: 'blur'},
                        {validator: validateFloor2, trigger: 'blur'}
                    ],
                    ceiling: [
                        {required: true, message: '不得为空', trigger: 'blur'},
                        {validator: validateCeiling2, trigger: 'blur'}
                    ],
                    itemCode: [
                        {required: true, message: '不得为空', trigger: 'blur'}
                    ],
                    organizationId: [
                        {required: true, message: '不得为空', trigger: 'blur'}
                    ]
                },
                rowData: {},
                itemTableLoading:true,
            }
        },
        computed: {
            ...mapState([])
        },
        methods: {

            newCloseHandler() {
                this.dialogNewVisible = false
            },
            updateCloseHandler() {
                this.dialogUpdateVisible = false
            },
            itemCloseHandler() {
                this.dialogItemVisible = false
            },

            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getInventorySafetyData();
            },
            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getInventorySafetyData()
            },

            handleItemSelChange(val) {
                this.itemList = val
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
            getInventorySafetyData() {
                var that = this;
                that.itemTableLoading = true;
                IOT.getServerData('/inventory/safetys', 'get', this.searchData, (ret) => {
                    that.itemTableLoading = false;
                    if (ret.code === 200) {
                        this.inventorySafeties = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },


            deleteConfirm(id) {
                this.$confirm('确认删除库存安全吗？')
                    .then(_ => {
                        this.deleteItem(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteItem(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/inventory/safetys/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getInventorySafetyData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            editItem(row) {
                this.rowData = JSON.parse(JSON.stringify(row))
                this.dialogUpdateVisible = true
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

            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            commitForm() {
                let that = this;
                
                if(that.newList.length==0){
                         IOT.tips( '请选择商品！', 'error');
                         return
                }
                IOT.showOverlay('保存中，请稍等...');
                let insert = PF.JSON(that.newList)
                IOT.getServerData('/inventory/safetys/batchAdd', 'post', {inventorySafeties: insert}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        that.clearInput()
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getInventorySafetyData() 
                        });

                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            clearInput() {
                this.organization = {
                    organizationId: null,
                    organizationName: ''
                }
                this.newList = []
                this.dialogNewVisible = false
            },
            commitForm2(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = JSON.parse(JSON.stringify(this.rowData))
                        IOT.getServerData('/inventory/safetys/update', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getInventorySafetyData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('rowDataForm')
                        this.dialogUpdateVisible = false
                    } else {
                        console.error('error submit!!');
                        return false;
                    }
                });
            },

            commitItems() {
                for (let item of this.itemList) {
                    let inventorySafety = {
                        ceiling: null,
                        floor: null,
                        cpfrCeiling: null,
                        cpfrFloor: null,
                        itemCode: item.itemCode,
                        itemName: item.itemName,
                        wareId: PF.getLocal('wareId', 'number'),
                        wareName: PF.getLocal('wareName'),
                        organizationId: this.organization.organizationId,
                        organizationName: this.organization.organizationName,
                        description: ''
                    }
                    if(!this.hasItem(item)){
                        this.newList.push(inventorySafety);
                    }
                    
                }
                
                this.dialogItemVisible = false
            },
            hasItem(item){
                for(var i=0;i<this.newList.length;i++){
                    var element = this.newList[i];
                     if(element.itemCode ==item.itemCode){
                        return true;
                    }
                }
                
                return false;
            },
            setOrganizationName() {
                for (let organization of this.organizations) {
                    if (this.organization.organizationId === organization.organizationId) {
                        this.organization.organizationName = organization.organizationName;
                        this.itemSearchData.organizationId = organization.organizationId;
                        break
                    }
                }
                this.getItemData();
            }
        },
        created() {
            this.getInventorySafetyData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
          //  this.getItemData()
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
