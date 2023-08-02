<template>
    <div class="createSoBill">

        <el-dialog width="900px" title="选择商品" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

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
                    <div>
                        <span style="font-size: 15px; margin-right: 10px; margin-left: 20px">仅查询有库存</span>
                        <el-switch v-model="searchData.hasInventoryLimit" :active-value="1"
                            :inactive-value="null"></el-switch>
                        <!--                                   @change="switchStateChanged"-->
                    </div>
                </div>
            </div>

            <div class="tableBox">
                <el-table v-loading="loading.itemTable" max-height="460" :data="items" style="width: 100%">
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
                    <el-table-column
                            show-overflow-tooltip
                            label="可拣库存">
                        <template slot-scope="scope">
                            <span>{{scope.row.canPickQuantity-scope.row.allotQuantity-scope.row.freezeQuantity}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            show-overflow-tooltip
                            label="库存数量">
                        <template slot-scope="scope" v-if="scope.row.quantity">
                            <span>{{scope.row.quantity-scope.row.allotQuantity-scope.row.freezeQuantity}}</span>
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
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">发运单信息</p>
                <el-form class="clearFloat" :model="form.soMaster" :rules="rules" ref="form.soMaster">
                    <el-form-item label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
                            style="width: 100%;">
                            <el-option :key="form.soMaster.wareId" :label="form.soMaster.wareName"
                                :value="form.soMaster.wareId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.organizationId" @change="changeOrganizationId" filterable
                            placeholder="请选择货主" size="small" style="width: 100%;">
                            <el-option v-for="item in organizations" :key="item.organizationId"
                                :label="item.organizationName" :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="下单时间" prop="orderTime" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.soMaster.orderTime" type="date" value-format="yyyy-MM-dd"
                            size="small" placeholder="请选择下单时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="发运单类型" prop="soType" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.soType" filterable placeholder="请选择发运单类型" size="small"
                            style="width: 100%;">
                            <el-option v-for="item in soBillTypes" :key="item.dataDictDetailId" :label="item.dictValue"
                                :value="item.dataDictDetailId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="客户" prop="customerId" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.customerId" @change="changeCustomerId" filterable
                            placeholder="请选择客户" size="small" style="width: 100%;">
                            <el-option v-for="item in customers" :key="item.customerId" :label="item.customerName"
                                :value="item.customerId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="收货人" prop="receivingAddressId" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.receivingAddressId" @change="changeReceivingAddressId"
                            :disabled="form.soMaster.customerId === null" filterable placeholder="请选择收货人" size="small"
                            style="width: 100%;">
                            <el-option v-for="item in receivingAddresss" :key="item.receivingAddressId"
                                :label="item.linkMan" :value="item.receivingAddressId">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="要求交货时间" prop="deliveryTime" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.soMaster.deliveryTime" type="date" value-format="yyyy-MM-dd"
                            size="small" placeholder="请选择交货时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间FM" prop="expectTimeFm" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.soMaster.expectTimeFm" type="date" value-format="yyyy-MM-dd"
                            size="small" placeholder="请选择预发时间FM">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间TO" prop="expectTimeTo" :label-width="formLabelWidth">
                        <el-date-picker v-model="form.soMaster.expectTimeTo" type="date" value-format="yyyy-MM-dd"
                            size="small" placeholder="请选择预发时间TO">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="form.soMaster.orderNo" placeholder="请输入优先级" size="small"
                            style="width: 100%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="备注" :label-width="formLabelWidth">
                        <el-input v-model="form.soMaster.memo" placeholder="请输入备注" size="small"></el-input>
                    </el-form-item>
                    <el-form-item v-for="(item, index) in soUdfHs" :key="index" :label="item.value"
                        :label-width="formLabelWidth">
                        <el-input v-model="form.soMaster[item.paramCode]" :placeholder="'请输入' + item.value"
                            size="small"></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="生成加工单" :label-width="formLabelWidth">
                        <el-checkbox v-model="form.soMaster.manufactureType"></el-checkbox>
                    </el-form-item> -->
                </el-form>
            </div>
            <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
                @click="isMoreSearch = !isMoreSearch"></span>
        </div>


        <div class="publicSearch bigFormBox3">
            <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
                <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">物流信息</p>
                <el-form class="clearFloat" :model="form.soMaster" :rules="rules" ref="form.soMaster">
                    <el-form-item label="承运方" prop="shipCode" :label-width="formLabelWidth">
                        <el-select v-model="form.soMaster.shipCode" filterable placeholder="请选择承运方" size="small"
                            style="width: 100%;">
                            <el-option v-for="item in carriers" :key="item.carrierCode" :label="item.carrierName"
                                :value="item.carrierCode">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="物流单号" prop="shipBillCode" :label-width="formLabelWidth">
                        <el-input v-model="form.soMaster.shipBillCode" placeholder="请物流单号" size="small"></el-input>
                    </el-form-item>

                    <br />


                </el-form>






            </div>
            <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
                @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="tableBox">
            <el-table highlight-current-row :data="form.soDetails" style="width: 100%">
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
                            :disabled="form.soMaster.organizationId === '' || form.soMaster.organizationId === undefined || form.soMaster.organizationId === null
                            || form.soDetails[scope.$index].id > 0" placeholder="请输入SKU" size="small" clearable
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
                <el-table-column
                        show-overflow-tooltip
                        label="库存数量"
                        width="180">
                    <template slot-scope="scope" v-if="scope.row.orderQuantityMax">
                        <span>{{scope.row.orderQuantityMax / scope.row.transRatio}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="订货数量" width="180">
                    <template slot-scope="scope">
                        <input type="number" class="my_input small" v-model.number="scope.row.orderQuantity"
                            :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
                            min=0 placeholder="请输入数量" style="width: 80%;">
                        <!--            <el-input-number v-model="scope.row.orderQuantity" :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null" controls-position="right" :min="0" :max="scope.row.one - (scope.row.two + scope.row.three)" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
                    </template>
                </el-table-column>
                <!--        <el-table-column-->
                <!--          label="预分配数"-->
                <!--          width="180">-->
                <!--          <template slot-scope="scope">-->
                <!--            <el-input-number v-model="scope.row.expectAllottedQuantity" :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null" controls-position="right" :min="0" :max="scope.row.one - (scope.row.two + scope.row.three)" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
                <!--          </template>-->
                <!--        </el-table-column>-->
                <!--<el-table-column
                  label="分配数"
                  width="180">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.allottedQuantity" :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column
                  label="拣货数量"
                  width="180">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.pickQuantity" :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column
                  label="发货数量"
                  width="180">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.deliveryQuantity" :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>
                  </template>
                </el-table-column>-->
                <el-table-column show-overflow-tooltip prop="itemTypeName" label="单位" width="180">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)"
                            :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
                            filterable placeholder="请选择单位" size="small">
                            <el-option v-for="item in scope.row.packDetails" :key="item.packDetailId"
                                :label="item.packDescribe" :value="item.packDetailId">
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>
                <!-- <el-table-column
                        show-overflow-tooltip
                        prop="bomSplit"
                        label="拆分BOM"
                        width="100">
                    <template slot-scope="scope">
                        <el-switch v-model="scope.row.bomSplit" active-value="1" inactive-value="0"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column
                        show-overflow-tooltip
                        v-for="(item,index) in soUdfDs"
                        :key="index"
                        :prop="item.paramCode"
                        :label="item.value"
                        width="180">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row[item.paramCode]"
                                  :disabled="scope.row.itemName === '' || scope.row.itemName === undefined || scope.row.itemName === null"
                                  :placeholder="'请输入' + item.value" size="small" clearable></el-input>
                    </template>
                </el-table-column> -->
            </el-table>
        </div>

        <div class="createFootBox clearFloat"
            :style="{ width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)' }">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
            <div class="rightFloat">
                <!-- <el-button @click="commitForm('form.soMaster',-2)" type="info" plain>保存为草稿</el-button> -->
                <el-button @click="commitForm('form.soMaster', 0)" type="primary">提 交</el-button>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: 'CreateSoBill',
    data() {
        let validateExpectTimeTo = (rule, value, callback) => {
            // let expectTimeFm = new Date(Date.parse(this.form.soMaster.expectTimeFm));
            // let expectTimeTo = new Date(Date.parse(this.form.soMaster.expectTimeTo));
            if (this.form.soMaster.expectTimeTo >= this.form.soMaster.expectTimeFm) {
                callback();
            } else {
                callback(new Error('预发时间FM 应小于 预发时间TO'));
            }
        };
        return {
            isMoreSearch: true,
            isMoreSearch2: true,
            loading: {
                itemTable: true,
            },

            searchData: {
                pageNum: 1,
                pageSize: 10,
                total: null,
                keyWords: '',
                wareId: PF.getLocal('wareId', 'number'),
                organizationId: null,
                hasInventoryLimit: 1
            },

            organizations: [],
            customers: [],
            items: [],
            ids: [],
            soUdfHs: [],
            soUdfDs: [],
            soBillTypes: [],
            receivingAddresss: [],
            carriers: [],

            selectDetailIndex: null,
            oldOrganizationId: null,

            innerVisibleItem: false,

            formLabelWidth: '120px',

            form: {
                billSource: 1,
                insertType: 0,
                soMaster: {
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
                    "soUdfHs1": "",
                    "soUdfHs2": "",
                    "soUdfHs3": "",
                    manufactureType: false
                },
                soDetails: [
                    {
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
                        "soUdfDs1": "",
                        "soUdfDs2": "",
                        "soUdfDs3": ""
                    }
                ]
            },
            soDetail: {
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
                "soUdfDs1": "",
                "soUdfDs2": "",
                "soUdfDs3": ""
            },
            rules: {
                organizationId: [
                    { required: true, message: '请选择货主', trigger: 'blur' }
                ],
                // customerId: [
                //     {required: true, message: '请选择客户', trigger: 'blur'}
                // ],
                // soType: [
                //     {required: true, message: '请选择发运单类型', trigger: 'blur'}
                // ],
                // receivingAddressId: [
                //     {required: true, message: '请选择收货人', trigger: 'blur'}
                // ],
                // carrierId: [
                //     {required: true, message: '请选择承运方', trigger: 'blur'}
                // ],
                // deliveryTime: [
                //     {required: true, message: '交货时间不得为空', trigger: 'blur'}
                // ],
                // expectTimeFm: [
                //     {required: true, message: '预发时间FM不得为空', trigger: 'blur'}
                // ],
                // expectTimeTo: [
                //     {required: true, message: '预发时间TO不得为空', trigger: 'blur'},
                //     {validator: validateExpectTimeTo, trigger: 'blur'}
                // ],
            },
            // searchItemInventoryLimit: 1
        }
    },
    computed: {
        isCollapse() {
            return this.$parent.leftMenuIsCollapse;
        },
        organizationId() {
            return this.form.soMaster.organizationId
        },
        customerId() {
            return this.form.soMaster.customerId
        },
    },
    watch: {
        organizationId(val) {
            for (let i = 0; i < this.organizations.length; i++) {
                if (this.organizations[i].organizationId === val) {
                    this.form.soMaster.organizationName = this.organizations[i].organizationName;
                }
            }
        },
        customerId(val) {
            for (let i = 0; i < this.customers.length; i++) {
                if (this.customers[i].customerId === val) {
                    this.form.soMaster.customerName = this.customers[i].customerName;
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
            this.searchData.hasInventoryLimit = null;
        },
        getItemData() {
            this.loading.itemTable = true;
            IOT.getServerData('/inventorys/canOut', 'get', this.searchData, (ret) => {
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
            this.form.soDetails.push(PF.JSON(this.soDetail));
        },
        deleteRow(index) {
            this.form.soDetails.splice(index, 1);
            this.ids.splice(index, 1);
        },
        showItemModal(index) {
            if (!PF.isNull(this.form.soMaster.organizationId)) {
                IOT.tips('请选择货主！', 'error');
                return false
            }
            this.searchData.organizationId = this.form.soMaster.organizationId;
            this.selectDetailIndex = index;
            this.getItemData();
            this.innerVisibleItem = true;
        },
        selectItem(row) {
            var that = this;
            var index = this.selectDetailIndex;
            if (this.ids.indexOf(row.itemName) !== -1) {
                IOT.tips('相同商品不能重复添加！', 'error');
                this.innerVisibleItem = false;
                return false
            }

            IOT.getServerData('/pack/details/list', 'get', { packCode: row.packCode }, (ret) => {
                IOT.hideOverlay();
                if (ret.code === 200) {
                    that.ids.push(row.itemName);

                    this.form.soDetails[index].weight = row.weight;

                    this.form.soDetails[index].packDetails = ret.rows;
                    this.form.soDetails[index].orderQuantityMax = row.quantity - row.allotQuantity - row.freezeQuantity;
                    this.form.soDetails[index].packDetails = ret.rows;
                    this.form.soDetails[index].itemCode = row.itemCode;
                    this.form.soDetails[index].id = row.id;
                    this.form.soDetails[index].itemName = row.itemName;
                    this.form.soDetails[index].model = row.model;
                    this.form.soDetails[index].upcs = row.upcs;
                    this.form.soDetails[index].spec = row.spec;
                    this.form.soDetails[index].imgUrl = row.imgUrl;
                    this.form.soDetails[index].transRatio = ret.rows[0].transRatio;
                    this.form.soDetails[index].packDescribe = ret.rows[0].packDescribe;
                    this.form.soDetails[index].packDetailId = ret.rows[0].packDetailId;
                    this.form.soDetails[index].packCode = row.packCode;
                    this.innerVisibleItem = false;
                    //   this.form.asnDetails[index]["expectQuantity"] = 0;

                } else {
                    // that.form.asnDetails[index].itemCode='';  

                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        resetForm(formName) { // 重置
            this.$refs[formName].resetFields();
        },
        commitForm(formName, insertType) {


            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let form = PF.JSON(this.form);
                    form.soMaster.manufactureType = form.soMaster.manufactureType ? 1 : 0;
                    if (form.soDetails.length == 0) {
                        IOT.tips('不得有空行！', 'error', 1000, () => {
                            IOT.hideOverlay();
                        });
                        return false
                    }
                    for (let i = 0; i < form.soDetails.length; i++) {
                        // if(!PF.isNull(form.soDetails[i].id)){
                        if (!PF.isNull(form.soDetails[i].itemName)) {
                            IOT.tips('不得有空行！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        if (!PF.isNull(form.soDetails[i].itemCode)) {
                            IOT.tips('SKU不得为空,请重新填写！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        form.soDetails[i].orderQuantity = Number(form.soDetails[i].orderQuantity);
                        if (!PF.isNull(form.soDetails[i].orderQuantity) || form.soDetails[i].orderQuantity <= 0) {
                            IOT.tips('订货数量需大于零！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        // if(form.soDetails[i].expectAllottedQuantity <= 0){
                        //   IOT.tips('预分配数不得为零！', 'error', 1000 , () => {
                        //     IOT.hideOverlay();
                        //   });
                        //     return false
                        // }
                        if (!PF.isNull(form.soDetails[i].packDetailId)) {
                            IOT.tips('未选择单位！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        // 主单位数量
                        form.soDetails[i].orderQuantity = form.soDetails[i].orderQuantity * form.soDetails[i].transRatio;
                        // if(form.soDetails[i].orderQuantity > form.soDetails[i].orderQuantityMax){
                        //     IOT.tips('输入数量不得大于可库存数量！', 'error', 1000 , () => {
                        //         IOT.hideOverlay();
                        //     });
                        //     return false
                        // }
                    }
                    form.insertType = insertType;
                    IOT.showOverlay('保存中，请稍等...');
                    IOT.getServerData('/so/masters/insert', 'post', form, (ret) => {
                        IOT.hideOverlay();
                        if (ret.code === 200) {
                            IOT.tips('保存成功！', 'success');
                            this.resetForm(formName);
                            this.cleaerDetails();
                            this.form.soMaster.memo = '';
                            this.$router.push({ name: 'soBill' })
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
        cleaerDetails() {
            this.ids = [];
            this.form.soDetails = [{
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
                        this.cleaerDetails();
                    })
                    .catch(_ => {
                        this.form.soMaster.organizationId = this.oldOrganizationId;
                    });
            }
        },
        changeCustomerId(val) {
            let list = PF.JSON(this.customers);
            for (let i = 0; i < list.length; i++) {
                if (list[i].customerId === val) {
                    this.form.soMaster.carrierId = list[i].carrierId
                    this.form.soMaster.carrierName = list[i].carrierName
                }
            }
            this.getReceivingAddressData(val);

        },
        getReceivingAddressData(id) {
            IOT.getServerData('/receiving/addresss/list', 'get', { customerId: id }, (ret) => {
                if (ret.code === 200) {
                    let list = ret.rows;
                    for (let i = 0; i < list.length; i++) {
                        if (list[i].state === 1) {
                            this.form.soMaster.receivingAddressId = list[i].receivingAddressId
                            this.form.soMaster.linkMan = list[i].linkMan
                        }
                    }
                    this.receivingAddresss = ret.rows;

                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        getCarrierData() {
            IOT.getServerData('/carriers/list', 'get', {}, (ret) => {
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
                    this.form.soMaster.linkMan = list[i].linkMan
                }
            }
        },

        changeItemCode(itemCode, index) {
            let that = this;
            if (!PF.isNull(itemCode)) {
                return false
            }
            let searchData = {
                wareId: this.form.soMaster.wareId,
                organizationId: this.form.soMaster.organizationId,
                itemCode: itemCode,
            };
            IOT.showOverlay('查询中，请稍等...');
            IOT.getServerData('/inventorys/canOut', 'get', searchData, (ret) => {
                if (ret.code === 200) {
                    let list = ret.rows;
                    this.loading.itemTable = false;
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
                                    this.form.soDetails[index].packDetails = ret.rows;
                                    this.form.soDetails[index].orderQuantityMax = row.quantity - row.allotQuantity - row.freezeQuantity;
                                    this.form.soDetails[index].packDetails = ret.rows;
                                    this.form.soDetails[index].itemCode = row.itemCode;
                                    this.form.soDetails[index].id = row.id;
                                    this.form.soDetails[index].itemName = row.itemName;
                                    this.form.soDetails[index].model = row.model;
                                    this.form.soDetails[index].upcs = row.upcs;
                                    this.form.soDetails[index].spec = row.spec;
                                    this.form.soDetails[index].imgUrl = row.imgUrl;
                                    this.form.soDetails[index].transRatio = ret.rows[0].transRatio;
                                    this.form.soDetails[index].packDescribe = ret.rows[0].packDescribe;
                                    this.form.soDetails[index].packDetailId = ret.rows[0].packDetailId;
                                    this.form.soDetails[index].packCode = row.packCode;
                                    this.innerVisibleItem = false;

                                } else {
                                    IOT.hideOverlay();
                                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                }
                            });

                        

                    } else {
                        IOT.hideOverlay();
                        IOT.tips('未查询到此SKU,请确认后输入！', 'error');
                    }
                }
            });
        },
        changePackDetailId(index) {
            let detail = PF.JSON(this.form.soDetails[index]);
            for (let i = 0; i < detail.packDetails.length; i++) {
                if (detail.packDetails[i].packDetailId === detail.packDetailId) {
                    this.form.soDetails[index].packDescribe = detail.packDetails[i].packDescribe;
                    this.form.soDetails[index].transRatio = detail.packDetails[i].transRatio;
                }
            }
        },
        /*switchStateChanged() {
            this.searchData.hasInventoryLimit = this.searchItemInventoryLimit
        }*/
    },
    created() {
        this.getCarrierData();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
        PF.getCustomers((rows) => {
            this.customers = rows;
        });
        PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
            this.soUdfHs = udfs;
        });
        PF.getSysParamUdfData('soUdfDs', [], (data, udfs) => {
            this.soUdfDs = udfs;
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
.createSoBill {
    padding-bottom: 100px;

    input:focus {
        outline: none; // 如何把蓝色去掉？
        border: 1px solid #30B101; // 如何改成别的颜色？
    }
}
</style>
