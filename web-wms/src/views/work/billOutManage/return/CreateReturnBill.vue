<template>
    <div class="createReturnBill">

        <el-dialog width="900px" title="选择商品" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

            <div class="publicSearch body">
                <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
                    <el-button-group>
                        <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                        <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                    </el-button-group>
                    <div>
                        <span>关键字搜索</span>
                        <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                    </div>
                    <div>
                        <span style="font-size: 15px; margin-right: 10px; margin-left: 20px">仅查询有库存</span>
                        <el-switch v-model="searchItemInventoryLimit"
                                   active-value="1"
                                   inactive-value="0"
                                   @change="switchStateChanged"
                        ></el-switch>
                    </div>
                </div>
            </div>

            <div class="tableBox">
                <el-table
                        v-loading="loading.itemTable"
                        max-height="460"
                        :data="items"
                        style="width: 100%">
                    <el-table-column
                            fixed="left"
                            label="操作"
                            width="60">
                        <template slot-scope="scope">
                            <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle
                                       plain size="small"></el-button>
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
                            show-overflow-tooltip
                            label="库存数量"
                            width="180">
                        <template slot-scope="scope">
                            <span>{{scope.row.one - (scope.row.two + scope.row.three)}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            show-overflow-tooltip
                            prop="packDescribe"
                            label="单位"
                            width="180">
                    </el-table-column>
                    <!--<el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="organizationName"
                        label="货主"
                        width="180">
                      <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
                      </template>
                    </el-table-column>-->
                </el-table>
                <el-pagination
                        style="text-align: right;"
                        @current-change="handleCurrentChange"
                        :current-page.sync="searchData.pageNum"
                        :page-size="searchData.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="searchData.total">
                </el-pagination>
            </div>

        </el-dialog>

        <div class="publicSearch bigFormBox3">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">退供应商信息</p>
                <el-form class="clearFloat" :model="form.returnMaster" :rules="rules" ref="form.returnMaster">
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
                                   style="width: 100%;">
                            <el-option
                                    :key="form.returnMaster.wareId"
                                    :label="form.returnMaster.wareName"
                                    :value="form.returnMaster.wareId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.organizationId" @change="changeOrganizationId" filterable
                                   placeholder="请选择货主" size="small"
                                   style="width: 100%;">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.supplierId" @change="changeSupplierId" filterable
                                   placeholder="请选择供应商" size="small" style="width: 100%;">
                            <el-option
                                    v-for="item in suppliers"
                                    :key="item.supplierId"
                                    :label="item.supplierName"
                                    :value="item.supplierId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="收货人" prop="receivingAddressId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.linkMan" @change="changeSupplierId"
                                   disabled  filterable placeholder="请选择收货人"
                                   size="small" style="width: 100%;">
                        </el-select>
                    </el-form-item>
                    <el-form-item label="承运方" prop="carrierId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.carrierId" @change="changeCarrierId"
                                   filterable placeholder="请选择承运方"
                                   size="small" style="width: 100%;">
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-for="(item,index) in returnUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
                        <el-input v-model="form.returnMaster[item.paramCode]" :placeholder="'请输入'+item.value"
                                  size="small"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="form.returnDetails"
                    style="width: 100%">
                <el-table-column
                        fixed="left"
                        type="index"
                        width="50">
                </el-table-column>
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
                <el-table-column
                        label="SKU"
                        width="240">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.itemCode" @change="changeItemCode(scope.row.itemCode, scope.$index)"

                                  :disabled="form.returnMaster.organizationId === '' || form.returnMaster.organizationId === undefined || form.returnMaster.organizationId === null
                      || form.returnDetails[scope.$index].id > 0"
                                  placeholder="请输入SKU" size="small" clearable style="width: 80%;"></el-input>
                        <el-button @click="showItemModal(scope.$index)"
                                   icon="iconfont icon-secondsearch_accurate" type="primary" circle plain
                                   size="mini"></el-button>
                    </template>
                </el-table-column>
                <el-table-column
                        show-overflow-tooltip
                        prop="itemName"
                        label="SKU名称"
                        width="180">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        show-overflow-tooltip
                        label="库存数量"
                        width="180">
                    <template slot-scope="scope">
                        <span>{{scope.row.orderQuantityMax / scope.row.transRatio}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        label="退货数量"
                        width="180">
                    <template slot-scope="scope">
                        <input type="number" class="my_input small" v-model.number="scope.row.returnQuantity"
                               :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                               :max="scope.row.orderQuantityMax" placeholder="请输入数量" style="width: 80%;">
                    </template>
                </el-table-column>
                <el-table-column
                        show-overflow-tooltip
                        prop="itemTypeName"
                        label="单位"
                        width="180">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
                                   :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                                   filterable placeholder="请选择单位" size="small">
                            <el-option
                                    v-for="item in scope.row.packDetails"
                                    :key="item.packDetailId"
                                    :label="item.packDescribe"
                                    :value="item.packDetailId">
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column
                        show-overflow-tooltip
                        v-for="(item,index) in returnUdfDs"
                        :key="index"
                        :prop="item.paramCode"
                        :label="item.value"
                        width="180">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row[item.paramCode]"
                                  :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                                  :placeholder="'请输入' + item.value" size="small" clearable></el-input>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="createFootBox clearFloat"
             :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
            <div class="rightFloat">
                <!--<el-button @click="commitForm('form.returnMaster',-2)" type="info" plain>保存为草稿</el-button>-->
                <el-button @click="commitForm('form.returnMaster',0)" type="primary">提 交</el-button>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'CreateReturnBill',
        data() {
            let validateExpectTimeTo = (rule, value, callback) => {
                if (this.form.returnMaster.expectTimeTo >= this.form.returnMaster.expectTimeFm) {
                    callback();
                } else {
                    callback(new Error('预发时间FM 应小于 预发时间TO'));
                }
            };
            return {
                isMoreSearch: true,
                isMoreSearch2: true,
                loading:{
                    itemTable:true,
                },

                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number'),
                    organizationId: null,
                    hasInventoryLimit: 0
                },

                organizations: [],
                suppliers: [],
                items: [],
                ids: [],
                returnUdfHs: [],
                returnUdfDs: [],
                soBillTypes: [],
                carriers: [],

                selectDetailIndex: null,
                oldOrganizationId: null,

                innerVisibleItem: false,

                formLabelWidth: '120px',

                form: {
                    billSource: 1,
                    insertType: 0,
                    returnMaster: {
                        wareId: PF.getLocal('wareId', 'number'),
                        wareName: PF.getLocal('wareName'),
                        organizationId: null,
                        organizationName: '',
                        customerId: null,
                        customerName: '',
                        soType: null,
                        receivingAddressId: null,
                        linkMan: '',
                        carrierId: null,
                        carrierName: '',
                        deliveryTime: '',
                        expectTimeFm: '',
                        expectTimeTo: '',
                        orderNo: 0,
                        memo: '',
                        "returnUdfHs1": "",
                        "returnUdfHs2": "",
                        "returnUdfHs3": "",
                        manufactureType: false
                    },
                    returnDetails: [
                        {
                            itemCode: '',
                            itemName: '',
                            returnQuantity: null,
                            packDetails: [],
                            packDetailId: null,
                            packDescribe: '',
                            transRatio: null,
                            "returnUdfDs1": "",
                            "returnUdfDs2": "",
                            "returnUdfDs3": ""
                        }
                    ]
                },
                returnDetail: {
                    itemCode: '',
                    itemName: '',
                    orderQuantity: null,
                    expectAllottedQuantity: 0,
                    allottedQuantity: 0,
                    pickQuantity: 0,
                    deliveryQuantity: 0,
                    packDetails: [],
                    packDetailId: null,
                    packDescribe: '',
                    transRatio: null,
                    "returnUdfDs1": "",
                    "returnUdfDs2": "",
                    "returnUdfDs3": ""
                },
                rules: {
                    organizationId: [
                        {required: true, message: '请选择货主', trigger: 'blur'}
                    ],
                    supplierId: [
                        {required: true, message: '请选择供应商', trigger: 'blur'}
                    ],
                    carrierId: [
                        {required: true, message: '请选择承运方', trigger: 'blur'}
                    ],
                    deliveryTime: [
                        {required: true, message: '交货时间不得为空', trigger: 'blur'}
                    ],
                    expectTimeFm: [
                        {required: true, message: '预发时间FM不得为空', trigger: 'blur'}
                    ],
                    expectTimeTo: [
                        {required: true, message: '预发时间TO不得为空', trigger: 'blur'},
                        {validator: validateExpectTimeTo, trigger: 'blur'}
                    ],
                },
                searchItemInventoryLimit: 0
            }
        },
        computed: {
            isCollapse() {
                return this.$parent.leftMenuIsCollapse;
            },
            organizationId() {
                return this.form.returnMaster.organizationId
            },
            supplierId() {
                return this.form.returnMaster.supplierId
            },
        },
        watch: {
            organizationId(val) {
                for (let i = 0; i < this.organizations.length; i++) {
                    if (this.organizations[i].organizationId === val) {
                        this.form.returnMaster.organizationName = this.organizations[i].organizationName;
                    }
                }
            },
            supplierId(val) {
                for (let i = 0; i < this.suppliers.length; i++) {
                    if (this.suppliers[i].supplierId === val) {
                        this.form.returnMaster.supplierName = this.suppliers[i].supplierName;
                    }
                }
            },
        },
        methods: {
            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getItemData()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
            },
            getItemData() {
                this.loading.itemTable = true;
                IOT.getServerData('/inventorys/SumList', 'get', this.searchData, (ret) => {
                    
                    this.loading.itemTable = false;
                    if (ret.code === 200) {
                        this.items = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            addRow() {
                this.form.returnDetails.push(PF.JSON(this.soDetail));
            },
            deleteRow(index) {
                this.form.returnDetails.splice(index, 1);
                this.ids.splice(index, 1);
            },
            showItemModal(index) {
                if (!PF.isNull(this.form.returnMaster.organizationId)) {
                    IOT.tips('请选择货主！', 'error');
                    return false
                }
                this.searchData.organizationId = this.form.returnMaster.organizationId;
                this.selectDetailIndex = index;
                this.getItemData();
                this.innerVisibleItem = true;
            },
            selectItem(row) {
                if (this.ids.indexOf(row.id) !== -1) {
                    IOT.tips('相同商品不能重复添加！', 'error');
                    this.innerVisibleItem = false;
                    return false
                }
                row.orderQuantityMax = row.one - (row.two + row.three);
                IOT.getServerData('/pack/details/list', 'get', {packCode: row.packCode}, (ret) => {
                    if (ret.code === 200) {
                        row.packDetails = ret.rows;
                        row.packDetailId = ret.rows[0].packDetailId;
                        row.packDescribe = ret.rows[0].packDescribe;
                        row.transRatio = ret.rows[0].transRatio;
                        this.ids.push(row.batchId);
                        for (let key in row) {
                            this.form.returnDetails[this.selectDetailIndex][key] = row[key];
                        }
                        this.innerVisibleItem = false;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            resetForm(formName) { // 重置
                this.$refs[formName].resetFields();
            },
            commitForm(formName, insertType) {
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = PF.JSON(this.form);
                        form.returnMaster.manufactureType = form.returnMaster.manufactureType ? 1 : 0;
                        for (let i = 0; i < form.returnDetails.length; i++) {
                            // if(!PF.isNull(form.returnDetails[i].id)){
                            if (!PF.isNull(form.returnDetails[i].itemName)) {
                                IOT.tips('不得有空行！', 'error', 1000, () => {
                                    IOT.hideOverlay();
                                });
                                return false
                            }
                            if (!PF.isNull(form.returnDetails[i].itemCode)) {
                                IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                                    IOT.hideOverlay();
                                });
                                return false
                            }
                            form.returnDetails[i].orderQuantity = Number(form.returnDetails[i].orderQuantity);
                            if (!PF.isNull(form.returnDetails[i].orderQuantity) && form.returnDetails[i].orderQuantity <= 0) {
                                IOT.tips('订货数量需大于零！', 'error', 1000, () => {
                                    IOT.hideOverlay();
                                });
                                return false
                            }
                            // if(form.returnDetails[i].expectAllottedQuantity <= 0){
                            //   IOT.tips('预分配数不得为零！', 'error', 1000 , () => {
                            //     IOT.hideOverlay();
                            //   });
                            //     return false
                            // }
                            if (!PF.isNull(form.returnDetails[i].packDetailId)) {
                                IOT.tips('未选择单位！', 'error', 1000, () => {
                                    IOT.hideOverlay();
                                });
                                return false
                            }
                            // 主单位数量
                            form.returnDetails[i].orderQuantity = form.returnDetails[i].orderQuantity * form.returnDetails[i].transRatio;
                        }
                        form.insertType = insertType;
                        IOT.getServerData('/return/masters/insert', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success');
                                this.resetForm(formName);
                                this.clearDetails();
                                this.$router.push({name: 'soBill'})
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });

                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            clearDetails() {
                this.ids = [];
                this.form.returnDetails = [{
                    itemCode: '',
                    itemName: '',
                    orderQuantity: null,
                    expectAllottedQuantity: 0,
                    allottedQuantity: 0,
                    pickQuantity: 0,
                    deliveryQuantity: 0,
                }];
            },
            changeOrganizationId(val) {
                if (!PF.isNull(this.oldOrganizationId)) {
                    this.oldOrganizationId = val;
                } else {
                    this.$confirm('修改货主已选商品将清空？')
                        .then(_ => {
                            this.oldOrganizationId = val;
                            this.clearDetails();
                        })
                        .catch(_ => {
                            this.form.returnMaster.organizationId = this.oldOrganizationId;
                        });
                }
            },
            changeSupplierId(val) {
                let list = PF.JSON(this.suppliers);
                for (let i = 0; i < list.length; i++) {
                    if (list[i].supplierId === val) {
                        this.form.returnMaster.linkMan = list[i].linkMan
                    }
                }
            },
            changeCarrierId(val) {
                let list = PF.JSON(this.carriers);
                for (let i = 0; i < list.length; i++) {
                    if (list[i].carrierId === val) {
                        this.form.returnMaster.carrierName = list[i].carrierName
                    }
                }
            },
            changeItemCode(itemCode, index) {
                let that = this;
                if (!PF.isNull(itemCode)) {
                    return false
                }
                let searchData = {
                    wareId: this.form.returnMaster.wareId,
                    organizationId: this.form.returnMaster.organizationId,
                    itemCode: itemCode,
                };
                IOT.getServerData('/inventorys/SumList', 'get', searchData, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        if (list.length === 1) {
                            if (this.ids.indexOf(list[0].id) !== -1) {
                                IOT.tips('相同商品不能重复添加！', 'error');
                                return false
                            }

                            let row = list[0];
                            IOT.getServerData('/pack/details/list', 'get', {packCode: row.packCode}, (ret) => {
                                if (ret.code === 200) {
                                    that.ids.push(row.id);
                                    row.packDetails = ret.rows;
                                    row.packDetailId = ret.rows[0].packDetailId;
                                    for (let key in row) {
                                        that.form.returnDetails[index][key] = row[key];
                                    }
                                } else {
                                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                }
                            });

                        } else {
                            IOT.tips('未查询到此SKU,请确认后输入！', 'error');
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            changePackDetailId(index) {
                let detail = PF.JSON(this.form.returnDetails[index]);
                for (let i = 0; i < detail.packDetails.length; i++) {
                    if (detail.packDetails[i].packDetailId === detail.packDetailId) {
                        this.form.returnDetails[index].packDescribe = detail.packDetails[i].packDescribe;
                        this.form.returnDetails[index].transRatio = detail.packDetails[i].transRatio;
                    }
                }
            },
            switchStateChanged() {
                this.searchData.hasInventoryLimit = this.searchItemInventoryLimit
            }
        },
        created() {
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getCarriers((rows) => {
                this.carriers = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
            PF.getSysParamUdfData('returnUdfHs', [], (data, udfs) => {
                this.returnUdfHs = udfs;
            });
            PF.getSysParamUdfData('returnUdfDs', [], (data, udfs) => {
                this.returnUdfDs = udfs;
            });
            PF.getDataDictUdfData('soBillType', (rows) => {
                this.soBillTypes = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .createReturnBill {
        padding-bottom: 100px;
        input:focus{
            outline: none;    // 如何把蓝色去掉？
            border: 1px solid #30B101; // 如何改成别的颜色？
        }
    }
</style>
