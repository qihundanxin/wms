<template>
  <div class="buyBillInDetail">
    
    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回</el-button>
       <el-button   size="small" type="success"   icon="iconfont icon-xinzeng"
                 @click="exportAns()"> 导出
      </el-button>
      
      <el-button v-if="requestBuy.state === 1 && [0,2,3].includes(requestBuy.acceptState) && sumAsnQuantity<=0" size="small" type="info" round icon="iconfont icon-tijiao" @click="showUpdateModal"> 修改</el-button>
      <el-button v-if="requestBuy.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng" @click="isCommit()"> 提交</el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="isDelete(requestBuy.requestBuyId)"> 删除</el-button>
      <el-button v-if="requestBuy.state === 1" size="small" type="warning" round icon="iconfont icon-xinzeng"  :disabled="multipleSelection.length <= 0" @click="showASNModal"> 生成入库单</el-button>
       |
      <el-button size="small" type="info" round icon="iconfont icon-tijiao" @click="isForceCompletion(requestBuy.requestBuyId)"> 采购强制完成</el-button>
      <span style="display: flex;">提示：强制完成采购单时，建议采购人员填写强制完成原因 ！</span>
      <el-input v-model="searchData.reason" size="mini" placeholder="采购填写：强制完成原因" clearable ></el-input>
      <el-button v-if="(requestBuy.state === 0 || requestBuy.state === 2) && formReview.caseDetailId !== null" size="small" type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核</el-button>
    </div>

    <el-dialog title="修改采购单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <!--master-->
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form.requestBuy" :rules="rules" ref="form.requestBuy">
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option
                      v-for="item in suppliers"
                      :key="item.supplierId"
                      :label="item.supplierName"
                      :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="采购单类型" prop="buyType" :label-width="formLabelWidth">
            <el-select v-model="form.requestBuy.buyType" filterable placeholder="请选择采购单类型" style="width: 70%;">
              <el-option
                      v-for="item in buyBillInTypes"
                      :key="item.dataDictDetailId"
                      :label="item.dictValue"
                      :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="付款方式"  prop="paymentMethod" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy.paymentMethod" placeholder="请输入付款方式" size="small"></el-input>
          </el-form-item>
          <el-form-item label="采购总价"  prop="buyTotalPrices"  :label-width="formLabelWidth" >
            <el-input-number v-model="form.requestBuy.buyTotalPrices" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入采购总价" size="small" style="width: 70%;"></el-input-number>
          </el-form-item>
          <el-form-item label="服务费" prop="serviceCharge" :label-width="formLabelWidth">
            <el-input-number v-model="form.requestBuy.serviceCharge" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入服务费" size="small" style="width: 70%;"></el-input-number>
          </el-form-item>
          <el-form-item label="其他费用" prop="otherExpenses" :label-width="formLabelWidth">
            <el-input-number v-model="form.requestBuy.otherExpenses" :controls="false" :precision="2" :min="0" :max="999999.99" placeholder="请输入其他费用" size="small" style="width: 70%;"></el-input-number>
          </el-form-item>
          <el-form-item label="付款说明"  prop="paymentInstruction" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy.paymentInstruction" placeholder="请输入付款说明" size="small"></el-input>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in poUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.requestBuy[item.paramCode]" :placeholder="'请输入'+item.value"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div class="headerBtns">
        <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectItemModal"> 选择商品
        </el-button>
      </div>
      <div class="tableBox" style="height:auto !important">
        <el-table
                highlight-current-row
                :data="form.requestBuyDetails"
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
                  prop="expectQuantity"
                  label="采购数量"
                  width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.expectQuantity" controls-position="right" :min="0" label="请输入数量"
                               size="small" style="width: 90%;"></el-input-number>
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
                         <!-- xuesinuo : 同新增、修改，只有显示第一个单位（件） -->
                <el-option
                        v-if="form.requestBuyDetails[scope.$index].packDetails && form.requestBuyDetails[scope.$index].packDetails.length>0"
                        :key="form.requestBuyDetails[scope.$index].packDetails[0].packDetailId1"
                        :label="form.requestBuyDetails[scope.$index].packDetails[0].packDescribe1"
                        :value="form.requestBuyDetails[scope.$index].packDetails[0].packDetailId1">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="priceIn"
                  label="进货单价（加元）"
                  width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.priceIn" controls-position="right" :min="0" label="请输入" :controls="false"
                               size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="taxRateIn"
                  label="进货税率（%）"
                  width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.taxRateIn" controls-position="right" label="请输入" :controls="false"
                               size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  v-for="(item,index) in poUdfDs"
                  :key="index"
                  :prop="item.paramCode"
                  :label="item.value"
                  width="180">
            <template slot-scope="scope">
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入'" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
          <el-table-column
                  fixed="right"
                  label="操作"
                  width="60">
            <template slot-scope="scope">
              <el-button @click="deleteDetail(scope.$index)" :disabled="form.requestBuyDetails.length === 1"
                         icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog width="800px" title="选择商品" :visible.sync="innerVisibleItem" append-to-body style="margin-top: -10vh;">

        <div class="publicSearch body">
          <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
            <el-button-group>
              <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
              <el-button @click="clearSearchSelectItem" size="mini" icon="el-icon-refresh">重置</el-button>
            </el-button-group>
            <div>
              <span>关键字搜索</span>
              <el-input v-model="searchDataItem.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
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
                <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                           size="small"></el-button>
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
            <!-- <el-table-column
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
                <span v-for="(item,index) in abcLevels" :key="index" v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
              </template>
            </el-table-column>
            <el-table-column
                    sortable
                    show-overflow-tooltip
                    prop="weight"
                    label="重量"
                    width="180">
            </el-table-column> -->
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
        <el-button type="primary" @click="updateForm">修 改</el-button>
      </div>
    </el-dialog>
    <el-dialog title="生成入库单单据" :visible.sync="dialogASNVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -10vh ">
      <!--master-->
      <div class="bigFormBox3"  >
        <el-form class="clearFloat" :model="formASN.asnMaster" :rules="rules" ref="formASN.asnMaster">
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="formASN.asnMaster.supplierId" @change="changeSupplierId()" filterable
                       placeholder="请选择供应商" style="width: 90%;">
              <el-option
                      v-for="item in suppliers"
                      :key="item.supplierId"
                      :label="item.supplierName"
                      :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="采购单类型" prop="buyType" :label-width="formLabelWidth">
            <el-select v-model="formASN.asnMaster.buyType" filterable placeholder="请选择采购单类型" style="width: 90%;">
              <el-option
                      v-for="item in buyBillInTypes"
                      :key="item.dataDictDetailId"
                      :label="item.dictValue"
                      :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预期到货时间" prop="expectTime" :label-width="formLabelWidth">
            <el-date-picker
                    v-model="formASN.asnMaster.expectTime"
                    type="date"
                    style="width: 90%;"
                    value-format="yyyy-MM-dd"
                    placeholder="请选择预期到货时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="入库单类型" prop="asnType" :label-width="formLabelWidth">
            <el-select v-model="formASN.asnMaster.asnType" filterable placeholder="请选择入库单类型" style="width: 90%;">
              <el-option
                      v-for="item in asnTypes"
                      :key="item.dataDictDetailId"
                      :label="item.dictValue"
                      :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="月台" prop="platformCode" :label-width="formLabelWidth">
            <el-select v-model="formASN.asnMaster.platformCode" filterable placeholder="请选择月台" style="width: 90%;">
              <el-option
                      v-for="item in platforms"
                      :key="item.platformCode"
                      :label="item.label"
                      :value="item.platformCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="承运方" prop="carrierCode" :label-width="formLabelWidth">
            <el-select v-model="formASN.asnMaster.carrierCode" filterable placeholder="请选择承运方" style="width: 90%;">
              <el-option
                      v-for="item in carriers"
                      :key="item.carrierCode"
                      :label="item.carrierName"
                      :value="item.carrierCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="优先级" :label-width="formLabelWidth">
            <el-input-number v-model="formASN.asnMaster.orderNo" :min="0" label="请输入优先级"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="formASN.asnMaster.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="发货地址" :label-width="formLabelWidth">
            <el-input v-model="formASN.asnMaster.loadingPlace" placeholder="请输入发货地址" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="交货地址" :label-width="formLabelWidth">
            <el-input v-model="formASN.asnMaster.deliveryPlace" placeholder="请输入交货地址" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="运输描述" :label-width="formLabelWidth">
            <el-input v-model="formASN.asnMaster.trafficDescribe" placeholder="请输入运输描述" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in asnUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="formASN.asnMaster[item.paramCode]" :placeholder="'请输入'+item.value"
                      style="width: 90%;"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div   >
        <el-table
        
                highlight-current-row
                :data="formASN.asnDetails"
                max-height="360"
                style="width: 100%;height:300px">
          <el-table-column
                  
                  type="index"
                  width="50">
          </el-table-column>
          <el-table-column
                 
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
                  prop="oldExpectQuantity"
                  label="可预约数量"
                  width="100">
            <template slot-scope="scope">
              <span>{{scope.row.expectQuantityMax}}</span>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="expectQuantity"
                  label="预约数量"
                  width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.expectQuantity" controls-position="right" :min="0"
                               :max="scope.row.expectQuantityMax" label="请输入数量" size="small"
                               style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  prop="packDescribe"
                  label="单位"
                  width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index,true)" disabled
                         filterable placeholder="请选择单位" size="small">
                <el-option
                        v-for="item in formASN.asnDetails[scope.$index].packDetails"
                        :key="item.packDetailId1"
                        :label="item.packDescribe1"
                        :value="item.packDetailId1">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
                  show-overflow-tooltip
                  v-for="(item,index) in asnUdfDs"
                  :key="index"
                  :prop="item.paramCode"
                  :label="item.value"
                  width="180">
            <template slot-scope="scope">
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.value" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <!-- <el-button class="leftFloat" type="primary" plain @click="commitASN(-2)">保存为草稿</el-button>  -->
        <el-button @click="dialogASNVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitASN(0)">提 交</el-button>
      </div>
    </el-dialog>
    <el-dialog title="审核" :visible.sync="dialogReviewVisible" :before-close="handleClose3" width="500px">
      <el-form :model="formReview" :rules="rulesReview" ref="formReview">
        <el-form-item label="审核意见" prop="memo" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="formReview.memo" placeholder="请输入审核意见"></el-input>
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

        <el-tabs v-model="activeName" @tab-click="handleClick" style="width: 100%;">
          <el-tab-pane label="采购单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                  <span :title="requestBuy.billNo">&nbsp;{{requestBuy.billNo}}</span>
                </li>
                <li v-if="requestBuy.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="requestBuy.state === -2">草稿</span>
                  <!-- <span v-else-if="requestBuy.state === 2">待审核</span> -->
                  <span v-else-if="requestBuy.state === 1">审核通过</span>
                  <span v-else-if="requestBuy.state === 0">审核中</span>
                  <span v-else-if="requestBuy.state === -1">审核退回</span>
                </li>
                
                <li>
                  <span>采购单类型:</span>&nbsp;
                  <span :title="requestBuy.dictValue" v-for="(item,index) in buyBillInTypes" :key="index"
                        v-if="requestBuy.buyType === item.dataDictDetailId">&nbsp;{{item.dictValue}}</span>
                </li>
                  <li>
                      <span>预期到货时间:</span>
                      <span :title="requestBuy.expectTimeStr">&nbsp;{{requestBuy.expectTimeStr}}</span>
                  </li>
                  <li>
                      <span>采购总价:</span>
                      <span :title="requestBuy.buyTotalPrices">&nbsp;{{requestBuy.buyTotalPrices}}</span>
                  </li>
<!--                  <li v-for="(item,index) in poUdfHs" :key="index">
                      <span>{{item.value}}:</span>
                      <span :title="requestBuy[item.paramCode]">&nbsp;{{requestBuy[item.paramCode]}}</span>
                  </li>-->
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="requestBuy.wareName" class="textBtn"
                        @click="$parent.showPublicModal(requestBuy.wareId, 'ware')">&nbsp;{{requestBuy.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="requestBuy.organizationName" class="textBtn"
                        @click="$parent.showPublicModal(requestBuy.organizationId, 'organization')">&nbsp;{{requestBuy.organizationName}}</span>
                </li>
                <li>
                  <span>供应商:</span>
                  <span :title="requestBuy.supplierName" class="textBtn"
                        @click="$parent.showPublicModal(requestBuy.supplierId, 'supplier')">&nbsp;{{requestBuy.supplierName}}</span>
                </li>
                <li>
                  <span>创建人:</span>
                  <span :title="requestBuy.createUserName" class="textBtn"
                        @click="$parent.showPublicModal(requestBuy.createUserId, 'account')">&nbsp;{{requestBuy.createUserName}}</span>
                </li>
                  <li>
                      <span>服务费:</span>
                      <span :title="requestBuy.serviceCharge">&nbsp;{{requestBuy.serviceCharge}}</span>
                  </li>
              </ul>
              <ul class="leftFloat">
                  <li>
                      <span>备注:</span>
                      <span :title="requestBuy.memo">&nbsp;{{requestBuy.memo}}</span>
                  </li>
                  <li>
                      <span>付款方式:</span>
                      <span :title="requestBuy.paymentMethod">&nbsp;{{requestBuy.paymentMethod}}</span>
                  </li>
                <li>
                  <span>付款说明:</span>
                  <span :title="requestBuy.paymentInstruction">&nbsp;{{requestBuy.paymentInstruction}}</span>
                </li>
                  <li>
                      <span>创建时间:</span>
                      <span :title="requestBuy.createTimeStr">&nbsp;{{requestBuy.createTimeStr}}</span>
                  </li>
                  <li>
                      <span>其他费用:</span>
                      <span :title="requestBuy.otherExpenses">&nbsp;{{requestBuy.otherExpenses}}</span>
                  </li>
              </ul>

                <div>
                    invoice信息：
                    <el-link v-for="(item,index) in invoiceFilePathList" :key="index" @click.prevent="openImageUrl(index)"  target="_blank" type="primary" style="margin-left: 20px;">
                      {{ (item.split('/').pop().length > 12) ? (item.split('/').pop().substring(0, 12) + '...' + item.split('/').pop().split('.').pop()) : item.split('/').pop() }}
                      <!-- {{ item.split('/').pop().substring(0, 12) + (item.split('/').pop().length > 12 ? '...' : '') }}.{{ item.split('/').pop().split('.').pop() }}-->
                    </el-link>
                    <el-button v-if="requestBuy.state === 1 && [0,2,3].includes(requestBuy.acceptState) && sumAsnQuantity<=0" size="small" type="info" round @click="showDialog" style="margin-left: 20px;">修改invoice</el-button>
                    <el-dialog
                            :visible.sync="dialogVisible"
                            title="invoice上传"
                            :before-close="beforeDialogClose"
                    > 
                      <div>{{ invoiceFilePathListForm && invoiceFilePathListForm.length > 0 ? '':'暂无文件' }}</div>
                      <el-upload
                              class="upload-demo"
                              ref="upload"
                              :headers="headerObj"
                              :action="uploadUrl"
                              :auto-upload="true"
                              :file-list="fileList"
                              :on-exceed="handleExceed"
                              :on-success="handleSuccess"
                              :before-upload="beforeUpload"
                              :on-remove="handleRemove"
                              :on-preview="handlePreview"
                              multiple
                              :limit="5"
                              accept="image/png,image/jpeg,image/jpg,application/pdf">
                        <el-button slot="trigger" size="small" type="primary">上传Invoice</el-button>
                        <div slot="tip" class="el-upload__tip">
                          支持多个文件上传，单个文件大小不超过10MB，仅支持png/jpeg/jpg/pdf格式。
                        </div>
                      </el-upload>
                      <el-button type="text" @click="open"  native-type = "button">确定</el-button>
                    </el-dialog>
                </div>
            </div>
          </el-tab-pane>

          <el-tab-pane v-if="requestBuy.insertType === 0" label="审核进程" name="reviewStep">
            <ReviewStepBox :masterData="requestBuy.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程" name="billRecord">
            <BillRecordBox :masterData="requestBuy.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
    </div>

    <div class="tableBox">
      <el-table
              v-loading="loading.detailTable"
              highlight-current-row
              :data="requestBuyDetails"
              @selection-change="handleSelectionChange"
              style="width: 100%">
        <el-table-column
                v-if="requestBuy.insertType === 0"
                type="selection"
                width="55">
        </el-table-column>
          <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/SKU名称"
          min-width="250">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8"> 
                <div style="display: inline-block;margin-left:5px;  width:50px;height:50px">
                   <el-image 
                      style="width: 50px; height: 50px"
                      :src="scope.row.imgUrl" 
                      :preview-src-list="[scope.row.imgUrl]">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.img_url"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="16">
                <div @click="$parent.showPublicModal(scope.row.itemCode, 'item')" style="display: inline-block;margin-left:5px;  ">
                    <span class="textBtn" style="color:red"
                        >{{scope.row.itemCode}}</span>
                        <br>
                    <span  
                   >{{scope.row.itemName}}</span>
                    <br>
                    <span>{{scope.row.spec}}</span>
              </div>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
      
        <el-table-column
                show-overflow-tooltip
                label="状态"
                width="150">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.acceptQuantity === 0" type="info">待验收</el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity > 0 && scope.row.acceptQuantity < scope.row.expectQuantity">
              部分验收
            </el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity > 0 && scope.row.acceptQuantity === scope.row.expectQuantity"
              type="success">全部验收
            </el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity > 0 && scope.row.acceptQuantity > scope.row.expectQuantity"
              type="danger">超额验收
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="expectQuantity"
                label="采购数量"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="acceptQuantity"
                label="验收数量"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="asnQuantity"
                label="入库单数量"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="packDescribe"
                label="单位"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="priceIn"
                label="进货单价（加元）"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taxRateIn"
                label="进货税率（%）"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                v-for="(item,index) in poUdfDs"
                :key="index"
                :prop="item.paramCode"
                :label="item.value"
                width="150">
        </el-table-column>
      </el-table>
      <!--<el-pagination
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>-->
    </div>
  </div>
</template>

<script>
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'
  import DateUtils from "@/utils/DateUtils";

  export default {
    name: 'BuyBillIn',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
        invoiceFilePathList: [],// 外层展示的invoice列表
        invoiceFilePathListForm:[],// 修改时的invoice列表（提交表单）
        fileList: [],// elementui 组件文件列表初始值
        dialogVisible: false,
        isModified: false, // 是否进行了修改并未保存
        loading:{
          detailTable:true,

        },
        // 上传的后端URL
        uploadUrl: localStorage.getItem("baseUrl") + '/request/buys/uploadInvoice',
        headerObj: {
          "access-token": localStorage.getItem('token')
        },
        isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        activeName: 'first',

        searchData: {
          // pageNum: 1,
          // pageSize: 10,
          // total: null,
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

        requestBuy: {},
        requestBuyDetails: [],
        form: {
          requestBuy: {
          },
          requestBuyDetails: [],
        },
        formASN: {
          billSource: 0,
          insertType: 0,
          asnMaster: {},
          asnDetails: [],
          requestBuyVersion:1,
          requestBuyId:'',
        },
        poUdfHs: [],
        poUdfDs: [],
        asnUdfHs: [],
        asnUdfDs: [],
        ids: [],
        items: [],
        buyBillInTypes: [],
        suppliers: [],
        multipleSelection: [],
        platforms: [],
        carriers: [],
        asnTypes: [],
        innerVisibleItem: false,
        dialogNewVisible: false,
        dialogASNVisible: false,
        dialogReviewVisible: false,

        rules: {
          supplierId: [
            {required: false, message: '请选择供应商', trigger: 'blur'}
          ],
          // buyType: [
          //   {required: false, message: '请选择供应商', trigger: 'blur'}
          // ],
          asnType: [
            {required: false, message: '请选择入库单类型', trigger: 'blur'}
          ],
          expectTime: [
            {required: true, message: '请选择预约到货时间', trigger: 'blur'}
          ],
        },

        formLabelWidth: '120px',

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
        //入库单数量
        sumAsnQuantity:0

      }
    },
    computed: {
      supplierId() {
        return this.form.requestBuy.supplierId
      },
    },
    watch: {
      supplierId(val) {
        for (let i = 0; i < this.suppliers.length; i++) {
          if (this.suppliers[i].supplierId === val) {
            this.form.requestBuy.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
    },
    methods: {
      exportAns(){
          let url = '/request/buys/daochu';
         IOT.showOverlay("正在导出，请稍等...");
         IOT.getServerData(url, 'get', {requestBuyId:this.requestBuy.requestBuyId}, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                  var path = ret.data; 
                    if(path!=undefined && path != null){
                        window.location.href=path;
                    }
                
                //   window.open(path);
              } else {
                  IOT.tips(
                      ret.message || "服务器请求失败，稍后再试！",
                      "error"
                  );
              }
           
        },true);
      },
      handleClick(tab, event) {
        console.log(tab, event);
      },
      /*handleCurrentChange(val) {
        this.pageNum = val;
        this.getBuyBillInDetailData()
      },*/
      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getItemData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      clearSearchSelectItem() {
        this.searchDataItem.keyWords = '';
        this.searchDataItem.wareId = null;
        this.searchDataItem.organizationId = null;
      },
      getBuyBillInData() {

        IOT.getServerData('/request/buys/list', 'get', {billNo: this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows[0];
            if (PF.isNull(list.createTime)) {
              list.createTimeStr = DateUtils.formatM(list.createTime);
            }
            if (PF.isNull(list.expectTime)) {
              list.expectTimeStr = DateUtils.formatDay(list.expectTime);
            }
            this.requestBuy = list;

          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
    },
    beforeDialogClose(done) {
      if (this.isModified) {
        this.$confirm('您还没有保存修改，确定要退出吗？', '确定退出', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getBuyBillInData();
          done();
        }).catch(() => {
        });
      } else {
        done();
      }
    },
    exportAns() {
      let url = '/request/buys/daochu';
      IOT.showOverlay("正在导出，请稍等...");
      IOT.getServerData(url, 'get', { requestBuyId: this.requestBuy.requestBuyId }, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          var path = ret.data;
          if (path != undefined && path != null) {
            window.location.href = path;
          }

          //   window.open(path);
        } else {
          IOT.tips(
            ret.message || "服务器请求失败，稍后再试！",
            "error"
          );
        }

      }, true);
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    /*handleCurrentChange(val) {
      this.pageNum = val;
      this.getBuyBillInDetailData()
    },*/
    handleCurrentChange2(val) {
      this.pageNum = val;
      this.getItemData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    clearSearchSelectItem() {
      this.searchDataItem.keyWords = '';
      this.searchDataItem.wareId = null;
      this.searchDataItem.organizationId = null;
    },
    getBuyBillInData() {

      IOT.getServerData('/request/buys/list', 'get', { billNo: this.searchData.billNo }, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows[0];
          if (PF.isNull(list.createTime)) {
            list.createTimeStr = DateUtils.formatM(list.createTime);
          }
          if (PF.isNull(list.expectTime)) {
            list.expectTimeStr = DateUtils.formatDay(list.expectTime);
          }
          this.requestBuy = list;

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    open() {
      this.$confirm('确定要提交数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        IOT.getServerData('/request/buys/detailUploadInvoice', 'post', {
          listInvoiceFilePath: this.invoiceFilePathListForm, 
          requestBuyId: this.requestBuy.requestBuyId,
          version: this.requestBuy.version
        }, (res) => {
          if (res.code === 200) {
            IOT.tips('更新成功！', 'success');
            this.dialogVisible = false;
            this.getBuyBillInData();
          } else {
            IOT.tips(res.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        });
      });
    },
    showDialog() {
      this.dialogVisible = true;
      this.isModified = false;
    },
    exportAns() {
      let url = '/request/buys/daochu';
      IOT.showOverlay("正在导出，请稍等...");
      IOT.getServerData(url, 'get', { requestBuyId: this.requestBuy.requestBuyId }, (ret) => {
        if (ret.code === 200) {
          IOT.hideOverlay();
          if (ret.code === 200) {
            var path = ret.data;
            if (path != undefined && path != null) {
              window.location.href = path;
            }

            //   window.open(path);
          } else {
            IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
          }

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
      const isPDF = file.type === 'application/pdf';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isJPG && !isPDF) {
        this.$message.error('仅支持上传PNG/JPEG/JPG/PDF格式的文件');
        return false
      }
      if (!isLt10M) {
        this.$message.error('单一文件大小不超过10MB');
        return false
      }
      return isJPG || isPDF && isLt10M;
    },
    handleSuccess(response, file, fileList) {
      if (response.code != 200) {
        this.$message.error('上传出错：' + response.message);
        // 从文件列表中删除上传出错的文件
        const index = fileList.findIndex(item => item.uid === file.uid);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      } else {
        this.$message.success(file.name + '上传成功');
        this.invoiceFilePathListForm = fileList.map(item => item.response.data.fullPath);
        this.isModified = true;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning('最多只能上传5个文件');
    },
    handleRemove(file, fileList) {
      this.invoiceFilePathListForm = fileList.map(item => item.response.data.fullPath);
      this.isModified = true;
    },
    handlePreview(file) {
      IOT.getServerData('/request/buys/selectInvoice', 'get', { fullPath: file.response.data.fullPath }, (res) => {
        if (res.code === 200) {
          window.open(res.data.url);
          IOT.tips('保存成功！', 'success');
        } else {
          IOT.tips(res.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    openImageUrl(index) {
      var fullPath = this.invoiceFilePathList[index]
      IOT.getServerData('/request/buys/selectInvoice', 'get', { fullPath }, (ret) => {
        if (ret.code === 200) {
          window.open(ret.data.url);
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    handleCurrentChange2(val) {
      this.pageNum = val;
      this.getItemData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    clearSearchSelectItem() {
      this.searchDataItem.keyWords = '';
      this.searchDataItem.wareId = null;
      this.searchDataItem.organizationId = null;
    },
    getBuyBillInData() {
      IOT.getServerData('/request/buys/list', 'get', { billNo: this.searchData.billNo }, (ret) => {
        if (ret.code === 200) {
          let asn = ret.rows[0];
          if (PF.isNull(asn.createTime)) {
            asn.createTimeStr = DateUtils.formatM(asn.createTime);
          }
          if (PF.isNull(asn.expectTime)) {
            asn.expectTimeStr = DateUtils.formatDay(asn.expectTime);
          }
          this.requestBuy = asn;
          this.invoiceFilePathList = asn.listInvoiceFilePath;
          this.invoiceFilePathListForm = asn.listInvoiceFilePath;
          this.fileList = asn.listInvoiceFilePath.map(url => {
            return {
              name: url.split('/').pop(),
              response: { data: { fullPath: url } }
            };
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },

    getBuyBillInDetailData() {
      var that = this;
      that.loading.detailTable = true;
      IOT.getServerData('/request/buy/details/list', 'get', this.searchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          for (let i = 0; i < list.length; i++) {
            if (list[i].transRatio == null) { list[i].transRatio = 1 };
            list[i].expectQuantity = list[i].expectQuantity / list[i].transRatio;
            list[i].acceptQuantity = list[i].acceptQuantity / list[i].transRatio;
            list[i].asnQuantity = list[i].asnQuantity / list[i].transRatio;
            //入库单数量
            this.sumAsnQuantity = this.sumAsnQuantity + list[i].asnQuantity;
            if (list[i].priceIn == null) { list[i].priceIn = undefined };
            if (list[i].taxRateIn == null) { list[i].taxRateIn = undefined };
          }
          this.requestBuyDetails = list;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
          var that = this;
          that.loading.detailTable = false;
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
      /**
       * 查询月台信息
       */
      getPlatformData() {
        IOT.getServerData('/platforms/list', 'get', {wareId: this.requestBuy.wareId}, (ret) => {
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
      /**
       * 查询快递信息
       */
      getCarrierData() {
        IOT.getServerData('/carriers/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleClose(done) {
        this.dialogNewVisible = false;
      },
      handleClose2(done) {
        this.dialogASNVisible = false;
      },
      handleClose3(done) {
        this.dialogReviewVisible = false;
      },
      isForceCompletion(id) {
        this.$confirm('确认强制完成本订单？')
                .then(_ => {
                  this.forceCompletionForBuy(id)
                })
                .catch(_ => {
                });
      },
      forceCompletionForBuy(id) {
        IOT.showOverlay('强制完成订单中，请稍等...');
        IOT.getServerData('/request/buys/forcecompletion', 'get', {id: id,reason:this.searchData.reason}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'buyBillIn'});
            IOT.tips('强制完成订单成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      isDelete(id) {
        if(this.requestBuy.acceptState==4){
          IOT.tips('此采购单已生成入库单，无法删除!', 'error');
          return;
        }
        this.$confirm('确认删除？')
                .then(_ => {
                  this.deleteBuyBillIn(id)
                })
                .catch(_ => {
                });
      },
      deleteBuyBillIn(id) {
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/request/buys/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'buyBillIn'});
            IOT.tips('删除成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showUpdateModal() {
        const requestBuy = PF.JSON(this.requestBuy)
        if(requestBuy.serviceCharge === null){
          requestBuy.serviceCharge = undefined
        }
        if(requestBuy.buyTotalPrices === null){
          requestBuy.buyTotalPrices = undefined
        }
        if(requestBuy.otherExpenses === null){
          requestBuy.otherExpenses = undefined
        }
        this.form = {
          requestBuy: requestBuy,
          requestBuyDetails: PF.JSON(this.requestBuyDetails),
          insertType: -2
        };
        PF.getSuppliers((rows) => {
          this.suppliers = rows;
        });
        this.dialogNewVisible = true;
      },
      // 生成入库单
      showASNModal() {
        let formASN = {
          billSource: 0,
          insertType: 0,
          asnMaster: PF.JSON(this.requestBuy),
          asnDetails: PF.JSON(this.multipleSelection),
        };
        formASN.asnMaster.platformCode = null;
        formASN.asnMaster.carrierCode = null;
        formASN.asnMaster.loadingPlace = '';
        formASN.asnMaster.deliveryPlace = '';
        formASN.asnMaster.trafficDescribe = '';
        const expectTime = Math.max(DateUtils.strToTime(this.requestBuy.expectTime).getTime(), Date.now());
        formASN.asnMaster.expectTime = DateUtils.timeToStr(expectTime, 'yyyy-MM-dd');
        formASN.asnMaster.orderNo = 1;
        formASN.asnMaster.asnUdfHs1 = formASN.asnMaster.poUdfHs1;
        formASN.asnMaster.asnUdfHs2 = formASN.asnMaster.poUdfHs2;
        formASN.asnMaster.asnUdfHs3 = formASN.asnMaster.poUdfHs3;
        formASN.asnMaster.requestBuyBillNo = formASN.asnMaster.billNo;
        formASN.requestBuyVersion = this.requestBuy.version;
        formASN.requestBuyId = this.requestBuy.requestBuyId;
        delete formASN.asnMaster.billNo;
        for (let i = 0; i < formASN.asnDetails.length; i++) {
          formASN.asnDetails[i].asnUdfDs1 = formASN.asnDetails[i].poUdfDs1;
          formASN.asnDetails[i].asnUdfDs2 = formASN.asnDetails[i].poUdfDs2;
          formASN.asnDetails[i].asnUdfDs3 = formASN.asnDetails[i].poUdfDs3;
          formASN.asnDetails[i].oldExpectQuantity = formASN.asnDetails[i].expectQuantity;
          formASN.asnDetails[i].expectQuantityMax = PF.subtraction(formASN.asnDetails[i].oldExpectQuantity,formASN.asnDetails[i].asnQuantity);
          // formASN.asnDetails[i].expectQuantityMax = (formASN.asnDetails[i].oldExpectQuantity - formASN.asnDetails[i].asnQuantity);
          formASN.asnDetails[i].requestDetailDetailNo = formASN.asnDetails[i].detailNo;
          delete formASN.asnDetails[i].detailNo;
          delete formASN.asnDetails[i].billNo;
        }
        this.formASN = formASN;
        //getSysParamUdfData 查询系统参数
        PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
          this.asnUdfHs = udfs;
        });
        PF.getSysParamUdfData('asnUdfDs', [], (data, udfs) => {
          this.asnUdfDs = udfs;
        });
        // 查询供应商信息
        PF.getSuppliers((rows) => {
          this.suppliers = rows;
        });
        //查询月台信息
        this.getPlatformData();
        //查询快递信息
        this.getCarrierData();
        this.dialogASNVisible = true;
      },
      showSelectItemModal() {
        this.ids = [];
        for (let i = 0; i < this.form.requestBuyDetails.length; i++) {
          this.ids.push(this.form.requestBuyDetails[i].itemCode)
        }
        this.searchDataItem.wareId = this.requestBuy.wareId;
        this.searchDataItem.organizationId = this.form.requestBuy.organizationId;
        this.getItemData();
        this.innerVisibleItem = true
      },
      selectItem(row) {
        if (this.ids.indexOf(row.itemCode) !== -1) {
          IOT.tips('相同商品不能重复添加！', 'error');
          this.innerVisibleItem = false;
          return false
        }
        IOT.getServerData('/pack/details/list','get',{packCode: row.packCode},(ret) => {
          if (ret.code === 200) {
            row.packDetails = [];
            for(let i=0;i<ret.rows.length;i++){
              row.packDetails.push({
                packDetailId1: ret.rows[i].packDetailId,
                packDescribe1: ret.rows[i].packDescribe,
                transRatio: ret.rows[i].transRatio,
              });
            }
            row.packDetailId = ret.rows[0].packDetailId;
            row.packDescribe = ret.rows[0].packDescribe;
            this.ids.push(row.itemCode);
            row.state = 0;
            row.transRatio = row.packDetails[0].transRatio;
            row.acceptQuantity = 0;
            row.expectQuantity = 0;
            row.asnQuantity = 0;
            this.form.requestBuyDetails.push(row);
            this.$set(this.form, 'requestBuyDetails', this.form.requestBuyDetails);
            this.innerVisibleItem = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteDetail(index) {
        this.form.requestBuyDetails.splice(index, 1)
      },
      updateForm() {
        let that = this;
        let form = this.form;
        if (form.requestBuyDetails.length <= 0) {
          IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        delete form.requestBuy.createTime;
        for (let i = 0; i < form.requestBuyDetails.length; i++) {
          if (form.requestBuyDetails[i].expectQuantity === 0 || !PF.isNull(form.requestBuyDetails[i].expectQuantity)) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          form.requestBuyDetails[i].priceInSetNull = form.requestBuyDetails[i].priceIn?false:true;
          form.requestBuyDetails[i].taxRateInSetNull = form.requestBuyDetails[i].taxRateIn?false:true;
          // 主单位数量
          form.requestBuyDetails[i].expectQuantity = form.requestBuyDetails[i].expectQuantity * form.requestBuyDetails[i].transRatio;
        }

        form.requestBuy.serviceChargeSetNull = form.requestBuy.serviceCharge?false:true;
        form.requestBuy.otherExpensesSetNull = form.requestBuy.otherExpenses?false:true;
        IOT.getServerData('/request/buys/update', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getBuyBillInData();
              that.getBuyBillInDetailData();
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
          requestBuy: this.requestBuy,
          requestBuyDetails: this.requestBuyDetails,
          insertType: 0
        };
        delete form.requestBuy.createTime;
        if (form.requestBuyDetails.length <= 0) {
          IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.requestBuyDetails.length; i++) {
          if (form.requestBuyDetails[i].expectQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.requestBuyDetails[i].expectQuantity = form.requestBuyDetails[i].expectQuantity * form.requestBuyDetails[i].transRatio;
        }
        IOT.getServerData('/request/buys/insert', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'buyBillIn'});
            IOT.tips('保存成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      //提交-生成入库单单据
      commitASN(insertType) {
        IOT.showOverlay('保存中，请稍等...');
        let form = this.formASN;
        form.insertType = insertType;
        for (let i = 0; i < form.asnDetails.length; i++) {
          if (form.asnDetails[i].expectQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.asnDetails[i].expectQuantity = form.asnDetails[i].expectQuantity * form.asnDetails[i].transRatio;
        }
        IOT.getServerData('/asn/masters/insert', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'buyBillIn'});
            IOT.tips('提交成功！', 'success');
          } else if (ret.code === 10002 && (ret.message.includes('无重量信息') || ret.message.includes('无体积信息'))) {


            // 弹出提示并跳转到商品修改页面
            // 弹出确认取消提示框
            this.$confirm('请先去添加商品的重量和体积信息,点击确定跳转', '提示', {
              confirmButtonText: '确定',
              type: 'warning'
            }).then(() => {
              this.$router.push({
                        name: 'item',
                        path: '/base/itemBase/item',
                        params: {itemRow: ret.data}
                      }
              );
              // this.$router.push({ name: 'Item' })
            }).catch(() => {
              // 用户点击了取消按钮
            });

          }
        }, true);
      },

      changePackDetailId(index, type) {
        if (type) {
          let detail = PF.JSON(this.formASN.asnDetails[index]);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.formASN.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
              this.formASN.asnDetails[index].transRatio = detail.packDetails[i].transRatio1;
            }
          }
        } else {
          let detail = PF.JSON(this.form.requestBuyDetails[index]);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.form.requestBuyDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
              this.form.requestBuyDetails[index].transRatio = detail.packDetails[i].transRatio1;
            }
          }
        }
      },
      changeSupplierId() {
        for (let i = 0; i < this.suppliers.length; i++) {
          if (this.suppliers[i].supplierId === this.formASN.asnMaster.supplierId) {
            this.formASN.asnMaster.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitReview(formName,reviewResult){
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let formReview = PF.JSON(this.formReview);
            formReview.reviewResult = reviewResult;
            IOT.getServerData('/request/buys/review', 'get', formReview, (ret) => {
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
      PF.getSysParamUdfData('poUdfHs', [], (data, udfs) => {
        this.poUdfHs = udfs;
      });
      PF.getSysParamUdfData('poUdfDs', [], (data, udfs) => {
        this.poUdfDs = udfs;
      });
      this.getBuyBillInData();
      // 查询采购单详情
      this.getBuyBillInDetailData();
      PF.getDataDictUdfData('buyBillInType', (rows) => {
        this.buyBillInTypes = rows;
      });
      PF.getDataDictUdfData('asnType', (rows) => {
        this.asnTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .buyBillInDetail {
      /*设置el-input-number中的placeholder中的字靠左*/
      /deep/.el-input-number.is-without-controls .el-input__inner {
          text-align:left;
      }
  }
</style>
