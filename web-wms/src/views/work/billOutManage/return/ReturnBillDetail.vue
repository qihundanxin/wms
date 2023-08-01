<template>
    <div class="returnBillDetail">

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
            </el-button>
            <el-button v-if="returnMaster.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao"
                       @click="showUpdateModal"> 修改
            </el-button>
            <el-button v-if="returnMaster.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng"
                       @click="isCommit()"> 提交
            </el-button>
            <el-button size="small" type="danger" round icon="iconfont icon-shanchu"
                       @click="isDelete(returnMaster.returnMasterId)"> 删除
            </el-button>
            <!--<el-button size="small" type="warning" round icon="iconfont icon-jianhuo"
                       @click="bomSplit" :disabled="multipleSelection.length !== 1 "> 拆分BOM
            </el-button>
            <el-button v-show="activeName === 'first'" size="small" type="warning" round icon="iconfont icon-jianhuo"
                       @click="addPickTask" :disabled="multipleSelection.length !== 1 "> 分配
            </el-button>
            <el-button v-show="activeName === 'second'" size="small" type="danger" plain round
                       icon="iconfont icon-chexiao" @click="cancelPickTask" :disabled="multipleSelection2.length === 0">
                撤销
            </el-button>-->

            <el-button size="small" type="primary" plain round icon="iconfont icon-shangjia"
                       @click="dialogUpFileVisible = true">导入
            </el-button>
            <el-button size="small" type="primary" plain round icon="iconfont icon-shangjia"
                       @click="printEOrder(returnMaster.returnMasterId)">打印电子面单
            </el-button>
            <el-button v-if="(returnMaster.state === 0 || returnMaster.state === 2) && formReview.caseDetailId !== null" size="small"
                       type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核
            </el-button>
            <el-button class="rightFloat" size="small" type="success" round icon="iconfont icon-fahuo"
                       @click="commitDelivery('acceptList')" :disabled="multipleSelection.length=== 0 "> 退货
            </el-button>

        </div>

        <el-dialog title="导入" :visible.sync="dialogUpFileVisible" width="500px">
            <a href="/download/SoImport.xls">
                <el-button size="small" type="primary" plain round icon="iconfont icon-xiazaimoban">下载模板</el-button>
            </a>

            <div style="margin-top: 20px;">
                <el-button size="small" type="primary" round icon="iconfont icon-shangjia" @click="fileList">导入
                </el-button>
                <input type="file" id="upfile" name="upfile"/>
            </div>

        </el-dialog>
        <el-dialog title="修改退货单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
                   style="margin-top: -10vh;">
            <!--master-->
            <div class="bigFormBox2">
                <el-form class="clearFloat" :model="form.returnMaster" :rules="rules" ref="form.returnMaster">
                    <el-form-item label="客户" prop="customerId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.customerId" @change="changeCustomerId" filterable placeholder="请选择客户"
                                   style="width: 70%;">
                            <el-option
                                    v-for="item in customers"
                                    :key="item.customerId"
                                    :label="item.customerName"
                                    :value="item.customerId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="发运单类型" prop="soType" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.soType" filterable placeholder="请选择发运单类型" style="width: 70%;">
                            <el-option
                                    v-for="item in soBillTypes"
                                    :key="item.dataDictDetailId"
                                    :label="item.dictValue"
                                    :value="item.dataDictDetailId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="收货人" prop="receivingAddressId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.receivingAddressId" @change="changeReceivingAddressId"
                                   :disabled="form.returnMaster.customerId === null" filterable placeholder="请选择收货人"
                                   style="width: 70%;">
                            <el-option
                                    v-for="item in receivingAddresss"
                                    :key="item.receivingAddressId"
                                    :label="item.linkMan"
                                    :value="item.receivingAddressId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="承运方" prop="carrierId" :label-width="formLabelWidth">
                        <el-select v-model="form.returnMaster.carrierId" filterable placeholder="请选择承运方"
                                   style="width: 70%;">
                            <el-option
                                    v-for="item in carriers"
                                    :key="item.carrierId"
                                    :label="item.carrierName"
                                    :value="item.carrierId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="交货时间" prop="deliveryTime" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="form.returnMaster.deliveryTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择交货时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间FM" prop="expectTimeFm" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="form.returnMaster.expectTimeFm"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择预发时间FM">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="预发时间TO" prop="expectTimeTo" :label-width="formLabelWidth">
                        <el-date-picker
                                v-model="form.returnMaster.expectTimeTo"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择预发时间TO">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="优先级" :label-width="formLabelWidth">
                        <el-input-number v-model="form.returnMaster.orderNo" placeholder="请输入优先级" style="width: 70%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="备注" :label-width="formLabelWidth">
                        <el-input v-model="form.returnMaster.memo" placeholder="请输入备注"></el-input>
                    </el-form-item>
                    <el-form-item v-for="(item,index) in returnUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
                        <el-input v-model="form.returnMaster[item.paramCode]" :placeholder="'请输入'+item.value"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <!--detail-->
            <div class="headerBtns">
                <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectItemModal">
                    选择商品
                </el-button>
            </div>

            <div class="tableBox">
                <el-table
                        highlight-current-row
                        :data="form.returnDetails"
                        max-height="360"
                        style="width: 100%">
                    <el-table-column
                            fixed="left"
                            type="index"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            fixed="left"
                            show-overflow-tooltip
                            prop="itemName"
                            label="SKU名称"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            show-overflow-tooltip
                            prop="itemCode"
                            label="SKU"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            show-overflow-tooltip
                            prop="returnQuantity"
                            label="退货数量"
                            width="180">
                        <template slot-scope="scope">
                            <input type="number" class="my_input small" v-model.number="scope.row.returnQuantity" placeholder="请输入数量"
                                   style="width: 80%;">
                            <!--              <el-input-number v-model="scope.row.orderQuantity" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
                        </template>
                    </el-table-column>

                    <el-table-column
                            show-overflow-tooltip
                            prop="packDescribe"
                            label="单位"
                            width="180">
                        <template slot-scope="scope">
                            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" filterable
                                       placeholder="请选择单位" size="small">
                                <el-option
                                        v-for="item in form.returnDetails[scope.$index].packDetails"
                                        :key="item.packDetailId1"
                                        :label="item.packDescribe1"
                                        :value="item.packDetailId1">
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
                            <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.label" size="small"
                                      clearable></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="60">
                        <template slot-scope="scope">
                            <el-button @click="deleteDetail(scope.$index)" :disabled="form.returnDetails.length === 1"
                                       icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <el-dialog width="800px" title="选择商品" :visible.sync="innerVisibleItem" append-to-body
                       style="margin-top: -10vh;">

                <div class="publicSearch body">
                    <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
                        <el-button-group>
                            <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
                            <el-button @click="clearSearchSelectItem" size="mini" icon="el-icon-refresh">重置</el-button>
                        </el-button-group>
                        <div>
                            <span>关键字搜索</span>
                            <el-input v-model="searchDataItem.keyWords" size="mini" placeholder="SKU名称/SKU"
                                      clearable></el-input>
                        </div>
                    </div>
                    <span class="moreSearchBtn"
                          :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"
                          @click="isMoreSearch2 = !isMoreSearch2"></span>
                </div>

                <div class="tableBox">
                    <el-table
                            max-height="460"
                            :data="items"
                            style="width: 100%;">
                        <el-table-column
                                fixed="left"
                                label="操作"
                                width="60">
                            <template slot-scope="scope">
                                <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary"
                                           circle plain size="small"></el-button>
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
                                sortable
                                show-overflow-tooltip
                                prop="itemTypeName"
                                label="商品种类"
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
                                label="包装名称"
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
                                prop="abcLevel"
                                label="产品循环级别"
                                width="150">
                            <template slot-scope="scope">
                                <span v-for="(item,index) in abcLevels" :key="index"
                                      v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
                            </template>
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
                            @current-change="handleCurrentChange2"
                            :current-page.sync="searchDataItem.pageNum"
                            :page-size="searchDataItem.pageSize"
                            layout="total, prev, pager, next, jumper"
                            :total="searchDataItem.total">
                    </el-pagination>
                </div>

            </el-dialog>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogNewVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateForm('form')">修 改</el-button>
            </div>
        </el-dialog>
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
                    <el-tab-pane label="退货单据信息" name="first">
                        <div class="newBillData clearFloat">
                            <ul class="leftFloat">
                                <li>
                                    <span>单号:</span>
                                    <span :title="returnMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{returnMaster.mergeNo}}</span>
                                    <span :title="returnMaster.billNo" v-else>&nbsp;{{returnMaster.billNo}}</span>
                                </li>
                                <li v-if="returnMaster.insertType === 0">
                                    <span>审核状态:</span>&nbsp;
                                    <span v-if="returnMaster.state === -2">草稿</span>
                                    <span v-else-if="returnMaster.state === 2">待审核</span>
                                    <span v-else-if="returnMaster.state === 1">审核通过</span>
                                    <span v-else-if="returnMaster.state === 0">审核中</span>
                                    <span v-else-if="returnMaster.state === -1">审核退回</span>
                                </li>
                                <li>
                                    <span>保存类型:</span>&nbsp;
                                    <span v-if="returnMaster.insertType === 0">单据</span>
                                    <span v-else-if="returnMaster.insertType === -2">草稿</span>
                                <li>
                                <li>
                                    <span>承运方:</span>
                                    <span :title="returnMaster.carrierName">&nbsp;{{returnMaster.carrierName}}</span>
                                </li>
                            </ul>
                            <ul class="leftFloat">
                                <li>
                                    <span>仓库:</span>
                                    <span :title="returnMaster.wareName" class="textBtn"
                                          @click="$parent.showPublicModal(returnMaster.wareId, 'ware')">&nbsp;{{returnMaster.wareName}}</span>
                                </li>
                                <li>
                                    <span>货主:</span>
                                    <span :title="returnMaster.organizationName" class="textBtn"
                                          @click="$parent.showPublicModal(returnMaster.organizationId, 'organization')">&nbsp;{{returnMaster.organizationName}}</span>
                                </li>
                                <li>
                                    <span>供应商:</span>
                                    <span :title="returnMaster.supplierName" class="textBtn"
                                          @click="$parent.showPublicModal(returnMaster.supplierId, 'supplierManage')">&nbsp;{{returnMaster.supplierName}}</span>
                                </li>
                                <li>
                                    <span>收货人:</span>
                                    <span :title="returnMaster.linkMan">&nbsp;{{returnMaster.linkMan}}</span>
                                </li>
                            </ul>
                            <ul class="leftFloat">
                                <li>
                                    <span>创建人:</span>
                                    <span :title="returnMaster.createUserName" class="textBtn"
                                          @click="$parent.showPublicModal(returnMaster.userId, 'account')">&nbsp;{{returnMaster.createUserName}}</span>
                                </li>
                                <li>
                                    <span>创建时间:</span>
                                    <span :title="returnMaster.createTime">&nbsp;{{returnMaster.createTime}}</span>
                                </li>
                                <li v-for="(item,index) in returnUdfHs" :key="index">
                                    <span>{{item.value}}:</span>
                                    <span :title="returnMaster[item.paramCode]">&nbsp;{{returnMaster[item.paramCode]}}</span>
                                </li>
                            </ul>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane v-if="returnMaster.insertType === 0" label="审核进程" name="second">
                        <ReviewStepBox :masterData="returnMaster.billNo" :activeName="activeTabName"></ReviewStepBox>
                    </el-tab-pane>
                    <el-tab-pane label="订单进程" name="third">
                        <BillRecordBox :masterData="returnMaster.billNo" :activeName="activeTabName"></BillRecordBox>
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
                            :data="returnDetails"
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
                                label="状态"
                                width="180">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.state === 1" type="info">待处理</el-tag>
                                <el-tag v-else-if="scope.row.state === 2">完成</el-tag>
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
                                prop="itemCode"
                                label="SKU"
                                width="180">
                            <template slot-scope="scope">
                                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
                            </template>
                        </el-table-column>

                        <el-table-column
                                show-overflow-tooltip
                                prop="returnQuantity"
                                label="退货数量"
                                width="180">
                            <template slot-scope="scope">
                                <span>{{ scope.row.returnQuantity / scope.row.transRatio }}</span>
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
                                v-for="(item,index) in returnUdfDs"
                                :key="index"
                                :prop="item.paramCode"
                                :label="item.value"
                                width="180">
                        </el-table-column>
                       <!-- <el-table-column
                                show-overflow-tooltip
                                prop="acceptTime"
                                label="验收时间"
                                width="180">
                        </el-table-column>-->
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
        name: 'ReturnBillDetail',
        components: {
            ReviewStepBox,
            BillRecordBox,
        },
        data() {
            let validateExpectTimeTo = (rule, value, callback) => {
                // let expectTimeFm = new Date(Date.parse(this.form.returnMaster.expectTimeFm));
                // let expectTimeTo = new Date(Date.parse(this.form.returnMaster.expectTimeTo));
                if (this.form.returnMaster.expectTimeTo >= this.form.returnMaster.expectTimeFm) {
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

                returnMaster: {},
                returnDetails: [],
                form: {
                    returnMaster: {},
                    returnDetails: [],
                },
                returnUdfHs: [],
                returnUdfDs: [],
                ids: [],
                items: [],
                customers: [],
                organizations: [],
                platforms: [],
                carriers: [],
                pickTasks: [],
                inventoryBalances: [],
                multipleSelection: [],
                soBillTypes: [],
                traces: [],
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

                boxDetails: [],
                loadDetails: []

            }
        },
        computed: {
            ...mapState([
                "abcLevels",
                "pickTaskType"
            ]),
            customerId() {
                return this.form.returnMaster.customerId
            },
            billNoType() {
                return this.$route.params.billNo.substr(0, 2);
            },
            sumOneTwoLengthSize() {
                let sum = String(this.one).length + String(this.two).length + 3;
                let size = 600 / sum;
                if (size > 70) {
                    return 70;
                } else if (size < 12) {
                    return 12;
                } else {
                    return size;
                }
            },
            one() {
                let list = this.inventoryBalances;
                let quantity = 0;
                for (let i = 0; i < list.length; i++) {
                    quantity += list[i].pickQuantity * list[i].transRatio
                }
                return String(quantity / this.pickTaskMaster.transRatio);
            },
            isPickTask() {
                let one = Number(this.one);
                let two = Number(this.two);
                if (one > two) {
                    return false;
                } else {
                    return true;
                }
            }
        },
        watch: {
            customerId(val) {
                for (let i = 0; i < this.customers.length; i++) {
                    if (this.customers[i].customerId === val) {
                        this.form.returnMaster.customerName = this.customers[i].customerName;
                    }
                }
            },
            activeName(val) {
                if (val === 'second') {
                    this.getPickTaskData();
                }
            },
            one(val, oval) {
                if (val > oval && !this.isPickTask) {
                    IOT.tips('所选数量已超过可选数量,请减去相应数量！', 'error');
                }
            }
        },
        methods: {
            checkSelectable(row) {
                if (row.state === 2 || row.state === 3) {
                    return false
                } else {
                    return true;
                }
            },
            fileList() {
                let upfile = $('#upfile').val();
                IOT.getServerData('/return/masters/list', 'get', {model: upfile}, (ret) => {
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
            getReturnMasterData() {
                let searchData = PF.JSON(this.searchData);
                IOT.getServerData('/return/masters/list', 'get', searchData, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows[0];
                        if (PF.isNull(list.createTime)) {
                            list.createTime = list.createTime.substr(0, 19);
                        }
                       /* if (PF.isNull(list.deliveryTime)) {
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
                        }*/
                        this.returnMaster = list;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getSoDetailData() {
                let searchData = PF.JSON(this.searchData);
                IOT.getServerData('/return/details/list', 'get', searchData, (ret) => {
                    if (ret.code === 200) {
                        this.returnDetails = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getBoxDetailsData() {
                IOT.getServerData('/box/details/list', 'get', {billNo: this.$route.params.billNo}, (ret) => {
                    if (ret.code === 200) {
                        this.boxDetails = ret.rows
                    } else {
                        IOT.tips(ret.message || 'server wrong!', 'error')
                    }
                })
            },
            getLoadDetailData() {
                IOT.getServerData('/load/details/list', 'get', {billNo: this.$route.params.billNo}, (ret) => {
                    if (ret.code === 200) {
                        this.loadDetails = ret.rows
                    } else {
                        IOT.tips(ret.message || 'server wrong!', 'error')
                    }
                })
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
                IOT.getServerData('/platforms/list', 'get', {wareId: this.returnMaster.wareId}, (ret) => {
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
                IOT.getServerData('/pick/tasks/list', 'get', {
                    billNo: this.$route.params.billNo,
                    wareId: this.returnMaster.wareId,
                }, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        /*let newList = [];
                        for (let i = 0; i < list.length; i++) {
                            if (list[i].state !== 2) {
                                newList.push(list[i])
                            }
                        }*/
                        this.pickTasks = list;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            changeCustomerId(val) {
                let list = PF.JSON(this.customers);
                for (let i = 0; i < list.length; i++) {
                    if (list[i].customerId === val) {
                        this.form.returnMaster.carrierId = list[i].carrierId
                        this.form.returnMaster.carrierName = list[i].carrierName
                    }
                }
                this.getReceivingAddressData(val);
                this.getCarrierData();
            },
            getReceivingAddressData(id) {
                IOT.getServerData('/receiving/addresss/list', 'get', {customerId: id}, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        for (let i = 0; i < list.length; i++) {
                            if (list[i].state === 1) {
                                this.form.returnMaster.receivingAddressId = list[i].receivingAddressId
                                this.form.returnMaster.linkMan = list[i].linkMan
                            }
                        }
                        this.receivingAddresss = ret.rows;

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
            showUpdateModal(){
                let returnDetails = PF.JSON(this.returnDetails);
                for (let i = 0; i < returnDetails.length; i++) {
                    returnDetails[i].orderQuantity = returnDetails[i].orderQuantity / returnDetails[i].transRatio;
                }
                this.form = {
                    insertType: -2,
                    returnMaster: PF.JSON(this.returnMaster),
                    returnDetails: returnDetails
                };
                PF.getOrganizations((rows) => {
                    this.organizations = rows;
                });
               /* this.getPlatformData();
                this.changeCustomerId(this.form.returnMaster.customerId);*/
                // this.getCarrierData();
                this.dialogNewVisible = true;
            },
            showSelectItemModal() {
                this.ids = [];
                for (let i = 0; i < this.form.returnDetails.length; i++) {
                    this.ids.push(this.form.returnDetails[i].itemCode)
                }
                this.searchDataItem.wareId = PF.getLocal('wareId', 'number');
                this.searchDataItem.organizationId = this.form.returnMaster.organizationId;
                this.getItemData();
                this.innerVisibleItem = true
            },
            selectItem(row) {
                if (this.ids.indexOf(row.itemCode) !== -1) {
                    IOT.tips('相同商品不能重复添加！', 'error');
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
                this.form.returnDetails.push(row);
                // this.form.returnDetails.expectQuantity = 0;
                this.innerVisibleItem = false;
            },
            deleteDetail(index) {
                this.form.returnDetails.splice(index, 1)
            },
            updateForm(formName) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.form;
                        if (form.returnDetails.length <= 0) {
                            IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
                                IOT.hideOverlay();
                            });
                            return false
                        }
                        for (let i = 0; i < form.returnDetails.length; i++) {
                            if (!PF.isNull(form.returnDetails[i].orderQuantity) && form.returnDetails[i].orderQuantity <= 0) {
                                IOT.tips('数量需大于零！', 'error', 1000, () => {
                                    IOT.hideOverlay();
                                });
                                return false
                            }
                            /*if(!PF.isNull(form.returnDetails[i].expectAllottedQuantity) && form.returnDetails[i].expectAllottedQuantity <= 0){
                              IOT.tips('数量需大于零！', 'error', 1000 , () => {
                                IOT.hideOverlay();
                              });
                              return false
                            }*/
                            // 主单位数量
                            form.returnDetails[i].orderQuantity = form.returnDetails[i].orderQuantity * form.returnDetails[i].transRatio;
                            // form.returnDetails[i].expectAllottedQuantity = form.returnDetails[i].expectAllottedQuantity * form.returnDetails[i].transRatio;
                        }
                        IOT.getServerData('/so/masters/update', 'post', form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getreturnMasterData();
                                    that.getSoDetailData();
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.dialogNewVisible = false
                    } else {
                        IOT.hideOverlay();
                        console.error('error submit!!');
                        return false;
                    }
                });
            },
            isCommit() {
                this.$confirm('确认提交？')
                    .then(_ => {
                        this.commitForm()
                    })
                    .catch(_ => {
                    });
            },
            commitForm(formName) {
                IOT.showOverlay('保存中，请稍等...');
                let returnMaster = PF.JSON(this.returnMaster);
                delete returnMaster.returnMasterId;
                let returnDetails = PF.JSON(this.returnDetails);
                for (let i = 0; i < returnDetails.length; i++) {
                    delete returnDetails[i].soDetailId;
                }
                let form = {
                    billSource: returnMaster.billSource,
                    insertType: 0,
                    returnMaster: returnMaster,
                    returnDetails: returnDetails
                };
                if (form.returnDetails.length <= 0) {
                    IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false
                }
                for (let i = 0; i < form.returnDetails.length; i++) {
                    if (form.returnDetails[i].expectQuantity === 0) {
                        IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
                            IOT.hideOverlay();
                        });
                        return false
                    }
                    // 主单位数量
                    form.returnDetails[i].expectQuantity = form.returnDetails[i].expectQuantity * form.returnDetails[i].transRatio;
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
                let form = PF.JSON(this.inventoryBalances[index]);
                let packDetails = PF.JSON(this.inventoryBalances[index].packDetails);
                for (let i = 0; i < packDetails.length; i++) {
                    if (packDetails[i].packDetailId === form.toPackDetailId) {
                        this.inventoryBalances[index].packDescribe = packDetails[i].packDescribe1;
                        this.inventoryBalances[index].transRatio = packDetails[i].transRatio1;
                    }
                }
                // let inventoryBalances = this.inventoryBalances;
                // this.inventoryBalances[index].quantity = PF.changePToChangeQ(this.inventoryBalances[index]);
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
                this.two = String(PF.subtraction(this.pickTaskMaster.orderQuantity, this.pickTaskMaster.allottedQuantity));
                this.getInventoryBalanceData(this.multipleSelection[0].itemCode);
            },
            getInventoryBalanceData(itemCode) {
                IOT.getServerData('/inventorys/list', 'get', {
                    wareId: PF.getLocal('wareId', 'number'),
                    itemCode: itemCode,
                    // cellUseType: 2
                }, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows;
                        for (let i = 0; i < list.length; i++) {
                            list[i].pickQuantity = null;

                            // list[i].oldQ = list[i].quantity;
                            // list[i].oldP = list[i].packDetailId;
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
                if (!this.isPickTask) {
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
           /* cancelPickTask() {
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

            },*/
            commitDelivery(type) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                let form = [];
                if (type === 'acceptList') {
                    form = PF.JSON(this.multipleSelection);
                }
                IOT.getServerData('/return/masters/return', 'post', {returnDetailData: form}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('发货成功！', 'success', 1000, () => {
                            that.getSoDetailData()
                            that.getPickTaskData()
                            that.resetForm('returnDetailData');
                            that.handleClose();
                        });
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
            commitReview(formName, reviewResult) {
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
                        if (ret.rows.length > 0) {
                            this.formReview.caseDetailId = ret.rows[0].caseDetailId;
                        } else {
                            this.formReview.caseDetailId = null;
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            changeReceivingAddressId(val) {
                let list = PF.JSON(this.receivingAddresss);
                for (let i = 0; i < list.length; i++) {
                    if (list[i].receivingAddressId === val) {
                        this.form.returnMaster.linkMan = list[i].linkMan
                    }
                }
            },
            changePackDetailId(index) {
                let detail = PF.JSON(this.form.returnDetails[index]);
                for (let i = 0; i < detail.packDetails.length; i++) {
                    if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
                        this.form.returnDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
                        this.form.returnDetails[index].transRatio = detail.packDetails[i].transRatio1;
                    }
                }
            },
            bomSplit() {
                IOT.showOverlay('拆分中……')
                IOT.getServerData('/so/details/bomSplit', 'post', this.multipleSelection, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.message || '拆分成功！', 'success')
                        this.getSoDetailData()
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },

        },
        created() {
            this.getCaseNowList();
            PF.getSysParamUdfData('returnUdfHs', [], (data, udfs) => {
                this.returnUdfHs = udfs;
            });
            PF.getSysParamUdfData('returnUdfDs', [], (data, udfs) => {
                this.returnUdfDs = udfs;
            });
            PF.getDataDictUdfData('soBillType', (rows) => {
                this.soBillTypes = rows;
            });
            this.getReturnMasterData();
            this.getSoDetailData();
            this.getBoxDetailsData()
            this.getLoadDetailData()
            PF.getCustomers((rows) => {
                this.customers = rows;
            });

        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .returnBillDetail {
    }
</style>

