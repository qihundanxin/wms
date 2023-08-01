<template>
    <div class="allotBillDetail">

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
            </el-button>
            <el-button v-if="allotMaster.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao"
                       @click="showUpdateModal"> 修改
            </el-button>
            <el-button v-if="allotMaster.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng"
                       @click="isCommit()"> 提交
            </el-button>
            <!-- <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="isDelete(allotMaster.allotMasterId)"> 删除
            </el-button> -->
 
            <el-button v-if="(allotMaster.caseState === 0 || allotMaster.caseState === 2) && formReview.caseDetailId !== null" size="small" type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核</el-button>
         <el-button v-if=" allotMaster.caseState === 1  && allotMaster.state===0" class="rightFloat" size="small" type="success" round icon="iconfont icon-fahuo" @click="isCommitBill('入库单和SO单')"> 生成入库单和SO单</el-button>
 

        </div>
 
        <el-dialog title="审核" :visible.sync="dialogReviewVisible" :before-close="handleClose3" width="500px">
            <el-form :model="formReview" :rules="rulesReview" ref="formReview">
                <el-form-item label="审核原因" prop="memo" :label-width="formLabelWidth">
                    <el-input v-model="formReview.memo" placeholder="请输入审核原因"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button class="leftFloat" @click="dialogReviewVisible = false">取 消</el-button>
                <el-button type="danger" @click="commitReview('formReview',-1)">退 回</el-button>
                <el-button type="success" @click="commitReview('formReview',1)">通 过</el-button>
            </div>
        </el-dialog>

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearchMaster?'auto':''}">


                <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="width: 100%;">
                    <el-tab-pane label="SO单据信息" name="first">
                        <div class="newBillData clearFloat">
                            <ul class="leftFloat">
                                <li>
                                    <span>单号:</span>
                                    <span :title="allotMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{allotMaster.mergeNo}}</span>
                                    <span :title="allotMaster.billNo" v-else>&nbsp;{{allotMaster.billNo}}</span>
                                </li>
                                <li>
                                    <span>调出仓库:</span>
                                    <span :title="allotMaster.fromWareName" class="textBtn" @click="$parent.showPublicModal(allotMaster.fromWareId, 'ware')">&nbsp;{{allotMaster.fromWareName}}</span>
                                </li>
                                <li>
                                    <span>调入仓库:</span>
                                    <span :title="allotMaster.toWareName" class="textBtn" @click="$parent.showPublicModal(allotMaster.toWareId, 'ware')">&nbsp;{{allotMaster.toWareName}}</span>
                                </li>
                                <li v-if="allotMaster.insertType === 0">
                                    <span>审核状态:</span>&nbsp;
                                    <span v-if="allotMaster.caseState === -2">草稿</span>
                                    <span v-else-if="allotMaster.caseState === 2">待审核</span>
                                    <span v-else-if="allotMaster.caseState === 1">审核通过</span>
                                    <span v-else-if="allotMaster.caseState === 0">审核中</span>
                                    <span v-else-if="allotMaster.caseState === -1">审核退回</span>
                                </li>
                                <li>
                                    <span>预发时间FM:</span>
                                    <span :title="allotMaster.expectTimeFm">&nbsp;{{allotMaster.expectTimeFm}}</span>
                                </li>
                                <li>
                                    <span>预发时间TO:</span>
                                    <span :title="allotMaster.expectTimeTo">&nbsp;{{allotMaster.expectTimeTo}}</span>
                                </li>
                            </ul>
                            <ul class="leftFloat">
                                <li>
                                    <span>货主:</span>
                                    <span :title="allotMaster.organizationName" class="textBtn" @click="$parent.showPublicModal(allotMaster.organizationId, 'organization')">&nbsp;{{allotMaster.organizationName}}</span>
                                </li>
                                <li>
                                    <span>客户:</span>
                                    <span :title="allotMaster.customerName" class="textBtn" @click="$parent.showPublicModal(allotMaster.customerId, 'customer')">&nbsp;{{allotMaster.customerName}}</span>
                                </li>
                                <li>
                                    <span>收货人:</span>
                                    <span :title="allotMaster.linkMan">&nbsp;{{allotMaster.linkMan}}</span>
                                </li>
                                <li>
                                    <span>承运方:</span>
                                    <span :title="allotMaster.carrierName">&nbsp;{{allotMaster.carrierName}}</span>
                                </li>
                                <li>
                                    <span>交货时间:</span>
                                    <span :title="allotMaster.deliveryTime">&nbsp;{{allotMaster.deliveryTime}}</span>
                                </li>
                                <li>
                                    <span>备注:</span>
                                    <span :title="allotMaster.memo">&nbsp;{{allotMaster.memo}}</span>
                                </li>
                            </ul>
                            <ul class="leftFloat">
                                <li>
                                    <span>创建人:</span>
                                    <span :title="allotMaster.createUserName" class="textBtn" @click="$parent.showPublicModal(allotMaster.createUserId, 'account')">&nbsp;{{allotMaster.createUserName}}</span>
                                </li>
                                <li>
                                    <span>创建时间:</span>
                                    <span :title="allotMaster.createTime">&nbsp;{{allotMaster.createTime}}</span>
                                </li>
                                <!-- <li v-for="(item,index) in allotUdfHs" :key="index">
                                    <span>{{item.value}}:</span>
                                    <span :title="allotMaster[item.paramCode]">&nbsp;{{allotMaster[item.paramCode]}}</span>
                                </li> -->
                            </ul>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane v-if="allotMaster.insertType === 0" label="审核进程" name="second">
                    <ReviewStepBox :masterData="allotMaster.billNo" :activeName="activeTabName"></ReviewStepBox>
                    </el-tab-pane>
                    <el-tab-pane label="订单进程" name="third">
                        <BillRecordBox :masterData="allotMaster.billNo" :activeName="activeTabName"></BillRecordBox>
                    </el-tab-pane>
 
                </el-tabs>
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
        </div>

        <el-tabs type="card" v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="单据详情" name="first">
                <div class="tableBox">
                    <el-table
                            highlight-current-row
                            @selection-change="handleSelectionChange"
                            :data="allotDetails"
                            style="width: 100%">
                        <el-table-column
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
                                show-overflow-tooltip
                                prop="detailNo"
                                label="单号"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                prop="itemName"
                                label=" SKU名称"
                                width="180">
                            <template slot-scope="scope">
                                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                prop="itemCode"
                                label=" SKU"
                                width="180">
                            <template slot-scope="scope">
                                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                label="状态"
                                width="180">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.state === 1" type="info">部分调拨</el-tag>
                                <el-tag v-else-if="scope.row.state === 2">全部调拨</el-tag>
                                <el-tag v-else-if="scope.row.state === 3">部分拣货</el-tag>
                                <el-tag v-else-if="scope.row.state === 4">全部拣货</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                prop="expectQuantity"
                                label="调拨数量"
                                width="180">
                            <template slot-scope="scope">
                                <span>{{scope.row.expectQuantity / scope.row.transRatio}}</span>
                            </template>
                        </el-table-column>
 
                        <el-table-column
                                show-overflow-tooltip
                                prop="pickQuantity"
                                label="拣货数量"
                                width="180">
                            <template slot-scope="scope">
                                <span>{{scope.row.pickQuantity / scope.row.transRatio}}</span>
                            </template>
                        </el-table-column>
 
                        <el-table-column
                                show-overflow-tooltip
                                prop="packDescribe"
                                label="单位"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                show-overflow-tooltip
                                v-for="(item,index) in allotUdfDs"
                                :key="index"
                                :prop="item.paramCode"
                                :label="item.value"
                                width="180">
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
 
        </el-tabs>

    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import ReviewStepBox from '@/components/ReviewStepBox'
    import BillRecordBox from '@/components/BillRecordBox'


    export default {
        name: 'AllotBillDetail',
        components: {
            ReviewStepBox,
            BillRecordBox,
        },
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
                isMoreSearch: false,
                isMoreSearchMaster: true,
                isMoreSearch2: false,
                activeName: 'first',
                activeTabName: 'first',

                searchData: {
                    billNo: this.$route.params.billNo,
                },
                searchDataItem: {
                    pageNum: 1,
                    pageSize: 5,
                    total: null,
                    keyWords: '',
                    wareId: null,
                    organizationId: null,
                },

                allotMaster: {},
                allotDetails: [],
                form: {
                    allotMaster: {},
                    allotDetails: [],
                },
                allotUdfHs: [],
                allotUdfDs: [],
                ids: [],
                items: [],
                customers: [],
                organizations: [],
                platforms: [],
                carriers: [],
                pickTasks: [],
                inventoryBalances: [],
                multipleSelection: [],
                multipleSelection2: [],
                soBillTypes: [],
                traces: [],
                pickTaskMaster: {},
                two: '0',

                innerVisibleItem: false,
                dialogUpFileVisible: false,
                dialogNewVisible: false,
                dialogPickTaskVisible: false,
                dialogReviewVisible: false,

                rules: {
                    customerId: [
                        {required: true, message: '请选择客户', trigger: 'blur'}
                    ],
                    soType: [
                        {required: true, message: '请选择发运单类型', trigger: 'blur'}
                    ],
                    receivingAddressId: [
                        {required: true, message: '请选择收货人', trigger: 'blur'}
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

                formLabelWidth: '120px',

                printParams: {
                    RequestData: '',
                    EBusinessID: '',
                    DataSign: '',
                    IsPreview: ''
                },

                formReview: {
                    billNo: this.$route.params.billNo,
                    reviewResult: null,
                    caseDetailId: null,
                    memo: '',
                },
                rulesReview: {
                    memo: [
                        {required: true, message: '请填写审核原因', trigger: 'blur'}
                    ],
                },

            }
        },
        computed: {
            ...mapState([
                "abcLevels",
                "pickTaskType"
            ]),
            customerId() {
                return this.form.allotMaster.customerId
            },
            billNoType() {
                return this.$route.params.billNo.substr(0, 2);
            },
            sumOneTwoLengthSize(){
                let sum = String(this.one).length + String(this.two).length + 3;
                let size = 600 / sum;
                if(size > 70){
                    return 70;
                }else if(size < 12){
                    return 12;
                }else{
                    return size;
                }
            },
            one(){
                let list = this.inventoryBalances;
                let quantity = 0;
                for(let i=0;i<list.length;i++){
                    quantity += list[i].pickQuantity * list[i].transRatio
                }
                return String(quantity / this.pickTaskMaster.transRatio);
            },
            isPickTask(){
                let one = Number(this.one);
                let two = Number(this.two);
                if(one > two){
                    return false;
                }else{
                    return true;
                }
            }
        },
        watch: {
            customerId(val) {
                for (let i = 0; i < this.customers.length; i++) {
                    if (this.customers[i].customerId === val) {
                        this.form.allotMaster.customerName = this.customers[i].customerName;
                    }
                }
            },
            activeName(val) {
                if (val === 'second') {
                    this.getPickTaskData();
                }
            },
            one(val,oval){
                if(val>oval && !this.isPickTask){
                    IOT.tips('所选数量已超过可选数量,请减去相应数量！', 'error');
                }
            }
        },
        methods: {
            checkSelectable(row){
                if(row.state === 2 || row.state === 3){
                    return false
                }else{
                    return true;
                }
            },
            fileList() {
                let upfile = $('#upfile').val();
                IOT.getServerData('/so/masters/list', 'get', {model: upfile}, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleCurrentChange2(val) {
                this.pageNum = val;
                this.getItemData()
            },
            clearSearchSelectItem() {
                this.searchDataItem.keyWords = '';
                this.searchDataItem.wareId = null;
                this.searchDataItem.organizationId = null;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleSelectionChange2(val) {
                this.multipleSelection2 = val;
            },
            getSoMasterData() {
                let searchData = PF.JSON(this.searchData);
                if (this.billNoType === 'HB') {
                    searchData.mergeNo = this.$route.params.billNo;
                    delete searchData.billNo;
                }
                IOT.getServerData('/allot/masters/list', 'get', searchData, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows[0];
                        if (PF.isNull(list.createTime)) {
                            list.createTime = list.createTime.substr(0, 19);
                        }
                        if (PF.isNull(list.deliveryTime)) {
                            list.deliveryTime = list.deliveryTime.substr(0, 19);
                        }
                        if (PF.isNull(list.expectTimeTo)) {
                            list.expectTimeTo = list.expectTimeFm.substr(0, 19);
                        }
                        if (PF.isNull(list.expectTimeTo)) {
                            list.expectTimeTo = list.expectTimeTo.substr(0, 19);
                        }
                        if (PF.isNull(list.actualDeliveryTime)) {
                            list.actualDeliveryTime = list.actualDeliveryTime.substr(0, 19);
                        }
                        this.allotMaster = list;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getSoDetailData() {
                let searchData = PF.JSON(this.searchData);
                if (this.billNoType === 'HB') {
                    searchData.mergeNo = this.$route.params.billNo;
                    delete searchData.billNo;
                }
                IOT.getServerData('/allot/details/list', 'get', searchData, (ret) => {
                    if (ret.code === 200) {
                        this.allotDetails = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getItemData() {
                IOT.getServerData('/item/infos/list', 'get', this.searchDataItem, (ret) => {
                    if (ret.code === 200) {
                        this.items = ret.rows;
                        this.searchDataItem.total = ret.total;
                        this.searchDataItem.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getPlatformData() {
                IOT.getServerData('/platforms/list', 'get', {wareId: this.allotMaster.wareId}, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        for (let i = 0; i < list.length; i++) {
                            list[i].label = list[i].platformCode
                        }
                        this.platforms = list;
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
            getPickTaskData() {
                IOT.getServerData('/pick/tasks/list', 'get', {billNo: this.$route.params.billNo,wareId:this.allotMaster.wareId,}, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        let newList = [];
                        for(let i=0;i<list.length;i++){
                            if(list[i].state !== 2){
                                newList.push(list[i])
                            }
                        }
                        this.pickTasks = newList;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleClose(done) {
                this.dialogNewVisible = false;
            },
            handleClose2(done) {
                this.inventoryBalances = [];
                this.dialogPickTaskVisible = false;
            },
            handleClose3(done) {
                this.dialogReviewVisible = false;
            },
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteBuyBillIn(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteBuyBillIn(id) {
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/so/masters/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        this.$router.go(-1);
                        IOT.tips('删除成功！', 'success');
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            showUpdateModal() {
                this.form = {
                    insertType: -2,
                    allotMaster: PF.JSON(this.allotMaster),
                    allotDetails: PF.JSON(this.allotDetails)
                };
                PF.getOrganizations((rows) => {
                    this.organizations = rows;
                });
                this.getPlatformData();
                this.getCarrierData();
                this.dialogNewVisible = true;
            },
            showSelectItemModal() {
                this.ids = [];
                for (let i = 0; i < this.form.allotDetails.length; i++) {
                    this.ids.push(this.form.allotDetails[i].itemCode)
                }
                this.searchDataItem.wareId = PF.getLocal('wareId', 'number');
                this.searchDataItem.organizationId = this.form.allotMaster.organizationId;
                this.getItemData();
                this.innerVisibleItem = true
            },
            selectItem(row) {
                if (this.ids.indexOf(row.itemCode) !== -1) {
                    IOT.tips('相同 商品不能重复添加！', 'error');
                    this.innerVisibleItem = false;
                    return false
                }
                this.ids.push(row.itemCode);
                IOT.getServerData('/pack/details/list', 'get', {packCode: row.packCode}, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        row.packDetails = [];
                        for (let i = 0; i < list.length; i++) {
                            row.packDetails.push({
                                packDetailId1: list[i].packDetailId,
                                packDescribe1: list[i].packDescribe
                            })
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                }, false, false);
                this.form.allotDetails.push(row);
                this.form.allotDetails.expectQuantity = 0;
                this.innerVisibleItem = false;
            },
            deleteDetail(index) {
                this.form.allotDetails.splice(index, 1)
            },
            updateForm() {
                let that = this;
                let form = this.form;
                if (form.allotDetails.length <= 0) {
                    IOT.tips('未选择 商品,请重新选择！', 'error', 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false
                }
                for (let i = 0; i < form.allotDetails.length; i++) {
                    if (form.allotDetails[i].orderQuantity === 0 || !PF.isNull(form.allotDetails[i].orderQuantity)) {
                        IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
                            IOT.hideOverlay();
                        });
                        return false
                    }
                    if (form.allotDetails[i].expectAllottedQuantity === 0 || !PF.isNull(form.allotDetails[i].expectAllottedQuantity)) {
                        IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
                            IOT.hideOverlay();
                        });
                        return false
                    }
                }
                IOT.getServerData('/allot/masters/update', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getSoMasterData();
                            that.getSoDetailData();
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
                this.dialogNewVisible = false
            },
            isCommit() {
                this.$confirm('确认提交？')
                    .then(_ => {
                        this.commitForm()
                    })
                    .catch(_ => {
                    });
            },
            commitForm() {
                IOT.showOverlay('保存中，请稍等...');
                let form = {
                    billSource: this.allotMaster.billSource,
                    insertType: 0,
                    allotMaster: PF.JSON(this.allotMaster),
                    allotDetails: PF.JSON(this.allotDetails)
                };
                if (form.allotDetails.length <= 0) {
                    IOT.tips('未选择 商品,请重新选择！', 'error', 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false
                }
                for (let i = 0; i < form.allotDetails.length; i++) {
                    if (form.allotDetails[i].expectQuantity === 0) {
                        IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
                            IOT.hideOverlay();
                        });
                        return false
                    }
                }
                IOT.getServerData('/so/masters/insert', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        this.$router.push({name: 'soBill'});
                        IOT.tips('保存成功！', 'success');
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            changePickDetailId(index) {
                this.inventoryBalances[index].quantity = PF.changePToChangeQ(this.inventoryBalances[index]);
            },
            resetForm(formName) { // 重置
                this.$refs[formName].resetFields();
            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            handleTabClick() {

            },
            addPickTask() {
                if (this.multipleSelection[0].orderQuantity <= this.multipleSelection[0].allottedQuantity) {
                    IOT.tips('分配数大于或等于订货数！', 'error');
                    return false
                }
                this.pickTaskMaster = this.multipleSelection[0];
                this.two = String(PF.subtraction( this.pickTaskMaster.orderQuantity, this.pickTaskMaster.allottedQuantity ));
                this.getInventoryBalanceData(this.multipleSelection[0].itemCode);
            },
            getInventoryBalanceData(itemCode) {
                IOT.getServerData('/inventorys/list', 'get', {
                    wareId: PF.getLocal('wareId', 'number'),
                    itemCode: itemCode
                }, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        for (let i = 0; i < list.length; i++) {
                            list[i].pickQuantity = 0;

                            list[i].oldQ = list[i].quantity;
                            list[i].oldP = list[i].packDetailId;
                        }
                        this.inventoryBalances = list;
                        this.dialogPickTaskVisible = true;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitPickTask() {
                let that = this;
                if(!this.isPickTask){
                    IOT.tips('所选数量已超过可选数量,请减去相应数量！', 'error');
                    return false;
                }
                IOT.showOverlay('保存中，请稍等...');
                let list = PF.JSON(this.inventoryBalances);
                let form = {
                    billNo: this.multipleSelection[0].billNo,
                    createPickTaskDetails: [],
                    soDetailId: this.multipleSelection[0].soDetailId
                };
                for (let i = 0; i < list.length; i++) {
                    if (list[i].pickQuantity > 0) {
                        form.createPickTaskDetails.push({
                            inventoryId: list[i].inventoryId,
                            pickQuantity: list[i].pickQuantity,
                        })
                    }
                }
                IOT.getServerData('/pick/tasks/manualCreatePickTask', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getSoDetailData()
                            that.getPickTaskData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
                this.handleClose2();
            },
            cancelPickTask() {
                let that = this;
                let list = PF.JSON(this.multipleSelection2);
                let ids = [];
                for (let i = 0; i < list.length; i++) {
                    ids.push(list[i].pickTaskId);
                }
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/pick/tasks/cancelPickTask', 'post', {ids: ids}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getSoDetailData()
                            that.getPickTaskData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });

            },
            isCommitBill(type) {
                this.$confirm('确认生成' + type + '？')
                    .then(_ => {
                        this.commitBill(type)
                    })
                    .catch(_ => {
                    });
            },
            commitBill(type){
                IOT.showOverlay('保存中，请稍等...');
                let form = {
                    insertType: 0,
                    allotBillNo:this.allotMaster.billNo 
                };
                IOT.getServerData('/asn/masters/generate', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success');
                        this.$router.push({name: 'allotBill'})
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            printEOrder(soId) {
                
                let that = this
                IOT.showOverlay('发送中，请稍等...')
                IOT.getServerData('/so/masters/getPrintParams', 'get', {soId: soId, tag: 3}, (ret) => {
                    if (ret.code === 200) {
                        let printP = ret.rows[0]
                        that.printParams.RequestData = printP.requestData
                        that.printParams.EBusinessID = printP.ebusinessID
                        that.printParams.DataSign = printP.dataSign
                        that.printParams.IsPreview = printP.isPreview

                        setTimeout(function () {
                            IOT.hideOverlay()
                            $("#printParamsForm").submit()
                        }, 2000)

                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
            commitReview(formName,reviewResult){
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let formReview = PF.JSON(this.formReview);
                        formReview.reviewResult = reviewResult;
                        IOT.getServerData('/so/masters/review', 'get', formReview, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('审核成功！', 'success');
                                this.getCaseNowList();
                                this.dialogReviewVisible = false;
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm(formName);
                        this.dialogReviewVisible = false;
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            getCaseNowList() {
                IOT.getServerData('/case/details/nowList', 'get', {billNo: this.searchData.billNo}, (ret) => {
                    if (ret.code === 200) {
                        if(ret.rows.length > 0){
                            this.formReview.caseDetailId = ret.rows[0].caseDetailId;
                        }else{
                            this.formReview.caseDetailId = null;
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

        },
        created() {
            this.getCaseNowList();
            PF.getSysParamUdfData('allotUdfHs', [], (data, udfs) => {
                this.allotUdfHs = udfs;
            });
            PF.getSysParamUdfData('allotUdfDs', [], (data, udfs) => {
                this.allotUdfDs = udfs;
            });
            PF.getDataDictUdfData('soBillType', (rows) => {
                this.soBillTypes = rows;
            });
            this.getSoMasterData();
            this.getSoDetailData();
            PF.getCustomers((rows) => {
                this.customers = rows;
            });

        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .allotBillDetail {
    }
</style>
