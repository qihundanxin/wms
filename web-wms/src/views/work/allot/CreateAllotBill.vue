<template>
    <div class="createAllotBill">

        <el-dialog width="900px" title="选择物料" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

            <div class="publicSearch body">
                <div class="searchBar" :style="{ height: isMoreSearch2 ? 'auto' : '' }">
                    <el-button-group>
                        <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                        <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                    </el-button-group>
                    <div>
                        <span>关键字搜索</span>
                        <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
                    </div>
                </div>
                <span class="moreSearchBtn" :class="isMoreSearch2 ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
                    @click="isMoreSearch2 = !isMoreSearch2"></span>
            </div>

            <div class="tableBox">
                <el-table max-height="460" :data="items" style="width: 100%">
                    <el-table-column fixed="left" label="操作" width="60">
                        <template slot-scope="scope">
                            <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle
                                plain size="small"></el-button>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="left" type="index" width="50">
                    </el-table-column>
                    <el-table-column sortable show-overflow-tooltip fixed="left" prop="itemName" label="SKU名称"
                        width="180">
                        <template slot-scope="scope">
                            <span class="textBtn"
                                @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU" width="180">
                        <template slot-scope="scope">
                            <span class="textBtn"
                                @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemCode }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column show-overflow-tooltip label="1" width="180">
                        <template slot-scope="scope" v-if="scope.row.one">
                            <span>{{ scope.row.one - (scope.row.two + scope.row.three) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column show-overflow-tooltip prop="packDescribe" label="单位" width="180">
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
                <el-pagination style="text-align: right;" @current-change="handleCurrentChange"
                    :current-page.sync="searchData.pageNum" :page-size="searchData.pageSize"
                    layout="total, prev, pager, next, jumper" :total="searchData.total">
                </el-pagination>
            </div>

        </el-dialog>
        <div class="publicSearch bigFormBox3">
            <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">调拨单信息</p>
                <el-form class="clearFloat" :model="form.allotMaster" :rules="rules" ref="form.allotMaster">
                    <el-form-item label="调出仓库" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.fromWareId" disabled filterable placeholder="请选择仓库"
                            size="small" style="width: 100%;">
                            <el-option :key="form.allotMaster.fromWareId" :label="form.allotMaster.fromWareName"
                                :value="form.allotMaster.fromWareId">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="调入仓库" prop="toWareId" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.toWareId" @change="changeWareId" filterable
                            placeholder="请选择调出仓库" clearable size="small" style="width: 100%;">
                            <el-option v-for="item in wares" :key="item.wareId" :label="item.wareName"
                                :disabled="item.wareId == wareId" :value="item.wareId">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.organizationId" @change="changeOrganizationId" filterable
                            placeholder="请选择货主" clearable size="small" style="width: 100%;">
                            <el-option v-for="item in organizations" :key="item.organizationId"
                                :label="item.organizationName" :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="客户" prop="customerId" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.customerId" @change="changeCustomerId" filterable placeholder="请选择客户" clearable size="small" style="width: 100%;">
                            <el-option
                                    v-for="item in customers"
                                    :key="item.customerId"
                                    :label="item.customerName"
                                    :value="item.customerId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="收货人" prop="receivingAddressId" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.receivingAddressId" @change="changeReceivingAddressId" :disabled="form.allotMaster.customerId === null" filterable placeholder="请选择收货人" clearable size="small" style="width: 100%;">
                            <el-option
                                    v-for="item in receivingAddresss"
                                    :key="item.receivingAddressId"
                                    :label="item.linkMan"
                                    :value="item.receivingAddressId">
                            </el-option>
                        </el-select>
                    </el-form-item> -->
                    <el-form-item label="承运方" prop="carrierId" :label-width="formLabelWidth">
                        <el-select v-model="form.allotMaster.carrierId" @change="changeCarrierId"
                            :disabled="form.allotMaster.customerId === null" filterable placeholder="请选择承运方" clearable
                            size="small" style="width: 100%;">
                            <el-option v-for="item in carriers" :key="item.carrierId" :label="item.carrierName"
                                :value="item.carrierId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="要求交货时间" prop="deliveryTime" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.allotMaster.deliveryTime" type="date" value-format="yyyy-MM-dd"
                            clearable size="small" placeholder="请选择交货时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间FM" prop="expectTimeFm" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.allotMaster.expectTimeFm" type="date" value-format="yyyy-MM-dd"
                            clearable size="small" placeholder="请选择预发时间FM">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间TO" prop="expectTimeTo" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.allotMaster.expectTimeTo" type="date" value-format="yyyy-MM-dd"
                            clearable size="small" placeholder="请选择预发时间TO">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="form.allotMaster.orderNo" placeholder="请输入优先级" size="small"
                            style="width: 100%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="备注" :label-width="formLabelWidth">
                        <el-input v-model="form.allotMaster.memo" placeholder="请输入备注" size="small"></el-input>
                    </el-form-item>
                    <!-- <el-form-item v-for="(item,index) in allotUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
                        <el-input v-model="form.allotMaster[item.paramCode]" :placeholder="'请输入'+item.value" size="small"></el-input>
                    </el-form-item> -->
                </el-form>
            </div>
            <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
                @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="tableBox">
            <el-table highlight-current-row :data="form.allotDetails" style="width: 100%">
                <el-table-column fixed="left" type="index" width="50">
                </el-table-column>
                <el-table-column fixed="left" label="操作" width="60">
                    <template slot-scope="scope">
                        <el-button @click="deleteRow(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle
                            plain size="small"></el-button>
                    </template>
                </el-table-column>
                <el-table-column label="SKU" width="240">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.itemCode" @change="changeItemCode(scope.row.itemCode, scope.$index)"
                            :disabled="form.allotMaster.organizationId === '' || form.allotMaster.organizationId === undefined || form.allotMaster.organizationId === null
                            || form.allotDetails[scope.$index].id > 0" placeholder="请输入SKU" size="small" clearable
                            style="width: 80%;"></el-input>
                        <el-button @click="showItemModal(scope.$index)" icon="iconfont icon-secondsearch_accurate"
                            type="primary" circle plain size="mini"></el-button>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="itemName" label="SKU名称" width="180">
                    <template slot-scope="scope">
                        <span class="textBtn"
                            @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{ scope.row.itemName }}</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip label="库存数量2" width="180">
                    <template slot-scope="scope" v-if="scope.row.expectQuantityMax">
                        <span>{{ scope.row.expectQuantityMax / scope.row.transRatio }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="调拨数量" width="180">
                    <template slot-scope="scope">
                        <input type="number" class="my_input small" v-model.number="scope.row.expectQuantity"
                            :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                            placeholder="请输入数量" style="width: 80%;">
                        <!--                        <el-input-number v-model="scope.row.expectQuantity" :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null" controls-position="right" :min="0" :max="scope.row.one - (scope.row.two + scope.row.three)" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="itemTypeName" label="单位" width="180">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
                            :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                            filterable placeholder="请选择单位" size="small">
                            <el-option v-for="item in scope.row.packDetails" :key="item.packDetailId"
                                :label="item.packDescribe" :value="item.packDetailId">
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip v-for="(item, index) in allotUdfDs" :key="index"
                    :prop="item.paramCode" :label="item.value" width="180">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row[item.paramCode]"
                            :disabled="scope.row.itemCode === '' || scope.row.itemCode === undefined || scope.row.itemCode === null"
                            :placeholder="'请输入' + item.value" size="small" clearable></el-input>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="createFootBox clearFloat"
            :style="{ width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)' }">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
            <div class="rightFloat">
                <!--<el-button @click="commitForm('form.allotMaster',-2)" type="info" plain>保存为草稿</el-button> -->
                <el-button @click="commitForm('form.allotMaster', 0)" type="primary">提 交</el-button>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: 'CreateAllotBill',
    data() {
        let validateExpectTimeTo = (rule, value, callback) => {
            // let expectTimeFm = new Date(Date.parse(this.form.soMaster.expectTimeFm));
            // let expectTimeTo = new Date(Date.parse(this.form.soMaster.expectTimeTo));
            if (this.form.allotMaster.expectTimeTo >= this.form.allotMaster.expectTimeFm) {
                callback();
            } else {
                callback(new Error('预发时间FM 应小于 预发时间TO'));
            }
        };
        return {
            isMoreSearch: true,
            isMoreSearch2: true,
            wareId: PF.getLocal('wareId', 'number'),

            searchData: {
                pageNum: 1,
                pageSize: 10,
                total: null,
                keyWords: '',
                wareId: PF.getLocal('wareId', 'number'),
                organizationId: null,
            },

            organizations: [],
            customers: [],
            items: [],
            wares: [],
            ids: [],
            allotUdfHs: [],
            allotUdfDs: [],
            receivingAddresss: [],
            carriers: [],

            selectDetailIndex: null,
            oldOrganizationId: null,

            innerVisibleItem: false,

            formLabelWidth: '120px',

            form: {
                insertType: 0,
                allotMaster: {
                    fromWareId: PF.getLocal('wareId', 'number'),
                    fromWareName: PF.getLocal('wareName'),
                    toWareId: null,
                    toWareName: null,
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
                    "allotUdfHs1": "",
                    "allotUdfHs2": "",
                    "allotUdfHs3": ""
                },
                allotDetails: [
                    {
                        itemCode: '',
                        itemName: '',
                        expectQuantity: null,
                        expectAllottedQuantity: 0,
                        allottedQuantity: 0,
                        pickQuantity: 0,
                        deliveryQuantity: 0,
                        packDetails: [],
                        packDetailId: null,
                        packDescribe: '',
                        "allotUdfDs1": "",
                        "allotUdfDs2": "",
                        "allotUdfDs3": ""
                    }
                ]
            },
            allotDetail: {
                itemCode: '',
                itemName: '',
                expectQuantity: null,
                expectAllottedQuantity: 0,
                allottedQuantity: 0,
                pickQuantity: 0,
                deliveryQuantity: 0,
                packDetails: [],
                packDetailId: null,
                packDescribe: '',
                "allotUdfDs1": "",
                "allotUdfDs2": "",
                "allotUdfDs3": ""
            },
            rules: {
                organizationId: [
                    { required: true, message: '请选择货主', trigger: 'blur' }
                ],
                customerId: [
                    { required: false, message: '请选择客户', trigger: 'blur' }
                ],
                toWareId: [
                    { required: true, message: '请选择调入仓库', trigger: 'blur' }
                ],
                receivingAddressId: [
                    { required: false, message: '请选择收货人', trigger: 'blur' }
                ],
                carrierId: [
                    { required: false, message: '请选择承运方', trigger: 'blur' }
                ],
                deliveryTime: [
                    { required: false, message: '交货时间不得为空', trigger: 'blur' }
                ],
                expectTimeFm: [
                    { required: false, message: '预发时间FM不得为空', trigger: 'blur' }
                ],
                expectTimeTo: [
                    { required: false, message: '预发时间TO不得为空', trigger: 'blur' },
                    { validator: validateExpectTimeTo, trigger: 'blur' }
                ],
            }

        }
    },
    computed: {
        isCollapse() {
            return this.$parent.leftMenuIsCollapse;
        },
        organizationId() {
            return this.form.allotMaster.organizationId
        },
        customerId() {
            return this.form.allotMaster.customerId
        },
        toWareId() {
            return this.form.allotMaster.toWareId
        },
    },
    watch: {
        organizationId(val) {
            for (let i = 0; i < this.organizations.length; i++) {
                if (this.organizations[i].organizationId === val) {
                    this.form.allotMaster.organizationName = this.organizations[i].organizationName;
                }
            }
        },
        customerId(val) {
            for (let i = 0; i < this.customers.length; i++) {
                if (this.customers[i].customerId === val) {
                    this.form.allotMaster.customerName = this.customers[i].customerName;
                } items
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
            IOT.getServerData('/inventorys/SumList', 'get', this.searchData, (ret) => {
                if (ret.code === 200) {
                    this.items = ret.rows;
                    this.searchData.total = ret.total;
                    this.searchData.pageNum = ret.pageNumber
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
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
        addRow() {
            this.form.allotDetails.push(PF.JSON(this.allotDetail));
        },
        deleteRow(index) {
            this.form.allotDetails.splice(index, 1);
            this.ids.splice(index, 1);
        },
        showItemModal(index) {
            if (!PF.isNull(this.form.allotMaster.organizationId)) {
                IOT.tips('请选择货主！', 'error');
                return false
            }
            this.searchData.organizationId = this.form.allotMaster.organizationId;
            this.selectDetailIndex = index;
            this.getItemData();
            this.innerVisibleItem = true;
        },
        selectItem(row) {
            // IOT.tips(row.itemCode);
            this.ids = [];
            this.ids.push(row.itemCode);
            let form = PF.JSON(this.form);
            for (let i = 0; i < form.allotDetails.length; i++) {
                if (PF.isNull(form.allotDetails[i].itemCode)) {
                    //IOT.tips('SKU不得为空,请重新填写！');
                    if (this.ids.indexOf(form.allotDetails[i].itemCode) !== -1) {
                        IOT.tips(form.allotDetails[i].itemCode + ' 相同物料 不能重复添加！', 'error');
                        this.innerVisibleItem = false;
                        return false
                    }
                    //IOT.tips('添加--SKU！');
                    this.ids.push(form.allotDetails[i].itemCode);
                }
            }
            //IOT.tips(row.itemCode + '添加--SKU！');

            // if(this.ids.indexOf(row.itemCode) !== -1){
            //     IOT.tips('相同物料不能重复添加！', 'error');
            //     this.innerVisibleItem = false;
            //     return false
            // }
            row.expectQuantityMax = row.one - (row.two + row.three);
            IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
                if (ret.code === 200) {
                    row.packDetails = ret.rows;
                    row.packDetailId = ret.rows[0].packDetailId;
                    row.packDescribe = ret.rows[0].packDescribe;
                    row.transRatio = ret.rows[0].transRatio;
                    this.ids.push(row.itemCode);
                    for (let key in row) {
                        this.form.allotDetails[this.selectDetailIndex][key] = row[key];
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
                    for (let j = 0; j < this.wares.length; j++) {
                        if (this.wares[j].fromWareId === form.allotMaster.fromWareId) {
                            form.allotMaster.fromWareName = this.wares[j].fromWareName;
                        }
                    }
                    for (let z = 0; z < this.wares.length; z++) {
                        if (this.wares[z].toWareId === form.allotMaster.toWareId) {
                            form.allotMaster.toWareName = this.wares[z].toWareName;
                        }
                    }

                    for (let i = 0; i < form.allotDetails.length; i++) {
                        // if(!PF.isNull(form.soDetails[i].id)){
                        if (!PF.isNull(form.allotDetails[i].itemName)) {
                            IOT.tips('不得有空行！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        if (!PF.isNull(form.allotDetails[i].itemCode)) {
                            IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        if (form.allotDetails[i].expectQuantity <= 0) {
                            IOT.tips('调拨数量不得为零！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        if (form.allotDetails[i].expectQuantity > form.allotDetails[i].expectQuantityMax) {
                            IOT.tips('调拨数量不得为大于可调拨数量！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }

                        if (!PF.isNull(form.allotDetails[i].packDetailId)) {
                            IOT.tips('未选择单位！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        // 主单位数量
                        form.allotDetails[i].expectQuantity = form.allotDetails[i].expectQuantity * form.allotDetails[i].transRatio;
                    }
                    form.insertType = insertType;

                    IOT.getServerData('/allot/masters/insert', 'post', form, (ret) => {
                        IOT.hideOverlay();
                        if (ret.code === 200) {
                            IOT.tips('保存成功！', 'success');
                            this.$router.push({ name: 'allotBill' })
                        } else {
                            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                        }
                    });
                    this.resetForm(formName);
                    this.cleaerDetails();
                    this.form.allotMaster.memo = '';
                } else {
                    IOT.hideOverlay();
                    console.error('error submit!!');
                    return false;
                }
            });
        },
        clearDetails() {
            this.ids = [];
            this.form.allotDetails = [{
                itemCode: '',
                itemName: '',
                expectQuantity: null,
                allottedQuantity: 0,
                pickQuantity: 0,
                deliveryQuantity: 0,
            }];
        },
        changeOrganizationId(val) {
            if (!PF.isNull(this.oldOrganizationId)) {
                this.oldOrganizationId = val;
            } else {
                this.$confirm('修改货主已选物料将清空？')
                    .then(_ => {
                        this.oldOrganizationId = val;
                        this.clearDetails();
                    })
                    .catch(_ => {
                        this.form.allotMaster.organizationId = this.oldOrganizationId;
                    });
            }
        },
        changeCustomerId(val) {
            let list = PF.JSON(this.customers);
            for (let i = 0; i < list.length; i++) {
                if (list[i].customerId === val) {

                    this.form.allotMaster.carrierId = list[i].carrierId
                    this.form.allotMaster.carrierName = list[i].carrierName
                }
            }
            this.getReceivingAddressData(val);
            this.getCarrierData();
        },
        getReceivingAddressData(id) {
            IOT.getServerData('/receiving/addresss/list', 'get', { customerId: id }, (ret) => {
                if (ret.code === 200) {
                    let list = ret.rows;
                    for (let i = 0; i < list.length; i++) {
                        if (list[i].state === 1) {
                            this.form.allotMaster.receivingAddressId = list[i].receivingAddressId
                            this.form.allotMaster.linkMan = list[i].linkMan
                        }
                    }
                    this.receivingAddresss = ret.rows;

                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        getCarrierData() {
            IOT.getServerData('/carriers/list', 'get', this.searchData, (ret) => {
                if (ret.code === 200) {
                    this.carriers = ret.rows;
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        changeReceivingAddressId(val) {
            let list = PF.JSON(this.receivingAddresss);
            for (let i = 0; i < list.length; i++) {
                if (list[i].receivingAddressId === val) {
                    this.form.allotMaster.linkMan = list[i].linkMan
                }
            }
        },
        changeCarrierId(val) {
            let list = PF.JSON(this.carriers);
            for (let i = 0; i < list.length; i++) {
                if (list[i].carrierId === val) {
                    this.form.allotMaster.carrierName = list[i].carrierName
                }
            }
        },
        changeItemCode(itemCode, index) {
            let that = this;
            if (!PF.isNull(itemCode)) {
                return false
            }
            let searchData = {
                wareId: PF.getLocal('wareId', 'number'),
                organizationId: this.form.allotMaster.organizationId,
                itemCode: itemCode,
            };
            IOT.showOverlay('查询中，请稍等...');
            IOT.getServerData('/inventorys/SumList', 'get', searchData, (ret) => {
                if (ret.code === 200) {
                    let list = ret.rows;
                    if (list.length === 1) {
                        if (this.ids.indexOf(list[0].itemName) !== -1) {
                            IOT.tips('相同商品不能重复添加！', 'error');
                            IOT.hideOverlay();
                            return false
                        }

                        let row = list[0];
                        IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                that.ids.push(row.itemName);
                                row.packDetails = ret.rows;
                                console.log(ret.rows);
                                row.packDetailId = ret.rows[0].packDetailId;
                                row.expectQuantityMax = row.one - (row.two + row.three);
                                row.transRatio = ret.rows[0].transRatio;
                                row.packDescribe = ret.rows[0].packDescribe;
                                row.packDetailId = ret.rows[0].packDetailId;
                                row.packCode = row.packCode;
                                for (let key in row) {
                                    console.log("key");
                                    console.log(key);
                                    that.form.allotDetails[index][key] = row[key];
                                }
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });

                    } else {
                        IOT.hideOverlay();
                        IOT.tips('未查询到此商品编码,请确认后输入！', 'error');
                    }
                } else {
                    IOT.hideOverlay();
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        changePackDetailId(index) {
            let detail = PF.JSON(this.form.allotDetails[index]);
            for (let i = 0; i < detail.packDetails.length; i++) {
                if (detail.packDetails[i].packDetailId === detail.packDetailId) {
                    this.form.allotDetails[index].packDescribe = detail.packDetails[i].packDescribe;
                    this.form.soDetails[index].transRatio = detail.packDetails[i].transRatio;
                }
            }
        },
        changeWareId() {
            let detail = PF.JSON(this.form.allotMaster);
            for (let i = 0; i < this.wares.length; i++) {
                if (this.form.allotMaster.toWareId === this.wares[i].wareId) {
                    this.form.allotMaster.toWareName = this.wares[i].wareName;
                }
            }
        },
    },
    created() {
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
        PF.getCustomers((rows) => {
            this.customers = rows;
        });
        this.getWareData();
        // PF.getSysParamUdfData('allotUdfHs', [], (data, udfs) => {
        //     this.allotUdfHs = udfs;
        // });
    },
    mounted() {
    }
}
</script>

<style scoped lang="less">
.createAllotBill {
    padding-bottom: 100px;

    input:focus {
        outline: none; // 如何把蓝色去掉？
        border: 1px solid #30B101; // 如何改成别的颜色？
    }
}
</style>
