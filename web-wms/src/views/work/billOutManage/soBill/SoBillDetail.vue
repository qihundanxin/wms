<template>
  <div   v-loading="loading" class="soBillDetail">

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
      </el-button>
      <!-- <el-button v-if="soMaster.insertType === -2" size="small" type="info" round icon="iconfont icon-tijiao"
                 @click="showUpdateModal"> 修改
      </el-button>
      <el-button v-if="soMaster.insertType === -2" size="small" type="success" round icon="iconfont icon-xinzeng"
                 @click="isCommit()"> 提交
      </el-button>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu"
                 @click="isDelete(soMaster.soMasterId)"> 删除
      </el-button>
      <el-button size="small" type="warning" round icon="iconfont icon-jianhuo"
                 @click="bomSplit" :disabled="multipleSelection.length !== 1 " v-if="soMaster.mergeNo ===null" > 拆分BOM
      </el-button>
      <el-button v-show="activeName === 'first'" size="small" type="warning" round icon="iconfont icon-jianhuo"
                 @click="addPickTask" :disabled="multipleSelection.length !== 1" v-if="soMaster.mergeNo ===null"> 分配
      </el-button>
      <el-button v-show="activeName === 'second'" size="small" type="danger" plain round
                 icon="iconfont icon-chexiao" @click="cancelPickTask" :disabled="multipleSelection2.length === 0">
        撤销
      </el-button> -->
      <!-- <el-button v-if="soMaster.mergeNo ===null" size="small" type="primary" plain round icon="iconfont icon-shangjia"
                 @click="dialogUpFileVisible = true">导入
      </el-button> -->
       <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint()" split-button  >
        打印电子面单
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
          <!--<el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item> 
          -->

        </el-dropdown-menu>
      </el-dropdown>
       <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint2()" split-button  >
        打印出库单
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
          <!--<el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint2(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint2(2)">打印预览2</el-button></el-dropdown-item> -->

        </el-dropdown-menu>
      </el-dropdown>
        <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint4()" split-button  >
        打印发票
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
          <!--<el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint4(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint4(2)">打印预览2</el-button></el-dropdown-item> -->

        </el-dropdown-menu>
      </el-dropdown>
       <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint3()" split-button  >
        打印拣货标签
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
         <!-- <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint3(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint3(2)">打印预览2</el-button></el-dropdown-item> -->

        </el-dropdown-menu>
      </el-dropdown>
       <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint5()" split-button  >
        打印拣货单
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
         <!-- <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint5(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint5(2)">打印预览2</el-button></el-dropdown-item> -->

        </el-dropdown-menu>
      </el-dropdown>
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu"
                 @click="isDelete(soMaster.billNo, soMaster.state)"> 取消
      </el-button>
      <!--      <el-button size="small" type="primary" plain round icon="iconfont icon-7">导出</el-button>-->
      <!--      <el-button v-if="soMaster.mergeNo ===null" size="small" type="primary" plain round icon="iconfont icon-shangjia"
                 @click="printEOrder(soMaster.soMasterId)">打印电子面单
      </el-button> -->
      <!-- <el-button v-if="(soMaster.state === 0 || soMaster.state === 2) && formReview.caseDetailId && soMaster.mergeNo ===null!== null" size="small"
                 type="warning" round icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核
      </el-button>
      <el-button v-if="soMaster.mergeNo ===null" class="rightFloat" size="small" type="success" round icon="iconfont icon-fahuo"
                 @click="commitDelivery('acceptList')" :disabled="multipleSelection2.length=== 0"> 发货
      </el-button> -->

    </div>

    <!-- <el-dialog title="导入" :visible.sync="dialogUpFileVisible" width="500px">
      <a href="/download/SoImport.xls">
        <el-button v-if="soMaster.mergeNo ===null" size="small" type="primary" plain round icon="iconfont icon-xiazaimoban">下载模板</el-button>
      </a>

      <div style="margin-top: 20px;">
        <el-button v-if="soMaster.mergeNo ===null" size="small" type="primary" round icon="iconfont icon-shangjia" @click="fileList">导入
        </el-button>
        <input type="file" id="upfile" name="upfile"/>
      </div>

    </el-dialog> -->
    <el-dialog title="修改 SO 单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <!--master-->
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form.soMaster" :rules="rules" ref="form.soMaster">
          <el-form-item label="客户" prop="customerId" :label-width="formLabelWidth">
            <el-select v-model="form.soMaster.customerId" @change="changeCustomerId" filterable placeholder="请选择客户"
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
            <el-select v-model="form.soMaster.soType" filterable placeholder="请选择发运单类型" style="width: 70%;">
              <el-option
                v-for="item in soBillTypes"
                :key="item.dataDictDetailId"
                :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="收货人" prop="receivingAddressId" :label-width="formLabelWidth">
            <el-select v-model="form.soMaster.receivingAddressId" @change="changeReceivingAddressId"
                       :disabled="form.soMaster.customerId === null" filterable placeholder="请选择收货人"
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
            <el-select v-model="form.soMaster.carrierId" filterable placeholder="请选择承运方"
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
              v-model="form.soMaster.deliveryTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择交货时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="预发时间FM" prop="expectTimeFm" :label-width="formLabelWidth">
            <el-date-picker
              v-model="form.soMaster.expectTimeFm"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择预发时间FM">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="预发时间TO" prop="expectTimeTo" :label-width="formLabelWidth">
            <el-date-picker
              v-model="form.soMaster.expectTimeTo"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择预发时间TO">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="优先级" :label-width="formLabelWidth">
            <el-input-number v-model="form.soMaster.orderNo" placeholder="请输入优先级" style="width: 70%;"></el-input-number>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.soMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in soUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.soMaster[item.paramCode]" :placeholder="'请输入'+item.value"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div class="headerBtns">
        <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showSelectItemModal">
          选择物料
        </el-button>
      </div>

      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="form.soDetails"
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
            prop="orderQuantity"
            label="订货数量"
            width="180">
            <template slot-scope="scope">
              <input type="number" class="my_input small" v-model.number="scope.row.orderQuantity" placeholder="请输入数量"
                     style="width: 80%;">
              <!--              <el-input-number v-model="scope.row.orderQuantity" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
            </template>
          </el-table-column>
          <!--<el-table-column
            show-overflow-tooltip
            prop="expectAllottedQuantity"
            label="预分配数"
            width="180">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.expectAllottedQuantity" controls-position="right"
                               :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>-->
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" filterable
                         placeholder="请选择单位" size="small">
                <el-option
                  v-for="item in form.soDetails[scope.$index].packDetails"
                  :key="item.packDetailId1"
                  :label="item.packDescribe1"
                  :value="item.packDetailId1">
                </el-option>
              </el-select>
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
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.label" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="60">
            <template slot-scope="scope">
              <el-button @click="deleteDetail(scope.$index)" :disabled="form.soDetails.length === 1"
                         icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog width="800px" title="选择物料" :visible.sync="innerVisibleItem" append-to-body
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
              label="物料种类"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="customerName"
              label="客户"
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
    <el-dialog title="生成拣货任务" :visible.sync="dialogPickTaskVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -10vh;">
      <div class="billMasterBox" style="padding: 0;margin-bottom: 20px;">
        <div class="billData clearFloat" style="width: 100%;">
          <ul class="leftFloat">
            <li>
              <span>SKU名称:</span>
              <span>&nbsp;{{pickTaskMaster.itemName}}</span>
            </li>
            <li>
              <span>SKU:</span>
              <span>&nbsp;{{pickTaskMaster.itemCode}}</span>
            </li>
            <li>
              <span>订货数量:</span>
              <span>&nbsp;{{pickTaskMaster.orderQuantity}}</span>
            </li>
            <li>
              <span>已分配数:</span>
              <span>&nbsp;{{pickTaskMaster.allottedQuantity}}</span>
            </li>
            <li>
              <span>单位:</span>
              <span>&nbsp;{{pickTaskMaster.packDescribe}}</span>
            </li>
          </ul>
          <div class="rightFloat"
               style="width: 360px;height: 90px;display: flex;justify-content:center;align-items:center;"
               :style="{fontSize: sumOneTwoLengthSize + 'px'}">
            <span style="color: #409EFF;">{{one}}</span>
            <span style="color: #909399;"> / </span>
            <span style="color: #F56C6C;">{{two}}</span>
          </div>
        </div>
      </div>
      <p v-show="!isPickTask" class="messageP danger">所选数量已超过可选数量,请减去相应数量!</p>
      
      <div class="tableBox" style="height:500px">
        <el-table
          highlight-current-row
          :data="inventoryBalances"
          max-height="360"
          style="width: 100%">
          <el-table-column
            fixed="left"
            type="index"
            width="50">
          </el-table-column>
          <!--<el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="100">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="itemCode"
            label="SKU"
            width="100">
          </el-table-column>-->
          <el-table-column
            show-overflow-tooltip
            prop="batchName"
            label="批次"
            width="100">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="quantity"
            label="现有数量"
            width="100">
            <template slot-scope="scope">
              <!--              <span>{{(scope.row.quantity / scope.row.transRatio).toFixed(4)}}</span>-->
              <!--              <span>{{(scope.row.quantity / scope.row.transRatio)}}</span>-->
              <span>{{ Math.floor(scope.row.quantity / scope.row.transRatio * 10000) / 10000 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="pickQuantity"
            label="拣货数量"
            width="150">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.pickQuantity" controls-position="right" :min="0"
                               :max="scope.row.quantity" label="请输入数量" size="small"
                               style="width: 90%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePickDetailId(scope.$index)"
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
            show-overflow-tooltip
            prop="boxCode"
            label="追踪号"
            width="120">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="cellCode"
            label="货位编码"
            width="130">
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose2">取 消</el-button>
        <el-button type="primary" @click="commitPickTask">提 交</el-button>
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
          <el-tab-pane label="SO单据信息" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                  <!--<span :title="soMaster.mergeNo" v-if="billNoType === 'HB'">&nbsp;{{soMaster.mergeNo}}</span>-->
                  <span :title="soMaster.billNo">&nbsp;{{soMaster.billNo}}</span>
                </li>
                 <li>
                  <span>物流单号:</span>
                 
                  <span :title="soMaster.shipBillCode">&nbsp;{{soMaster.shipBillCode}}</span>
                </li>
                 <li>
                    <span>状态:</span>
                  <span  v-if="soMaster.state==-1" style="color:#909399">取消</span>
                  <span  v-if="soMaster.state==-2" style="color:#909399">关闭</span> 
                  <span  v-if="soMaster.state==1" style="color:#909399">待处理</span> 
                  <span  v-if="soMaster.state==3"  style="color:#F56C6C"  >缺货</span>
                  <span  v-if="soMaster.state==4"  style="color:#409EFF"  >已分配</span>
                  <span  v-if="soMaster.state==5"  style="color:#F56C6C"  >拣货中</span>
                  <span  v-if="soMaster.state==6"  style="color:#543044"  >拣货完成</span>
                  <span  v-if="soMaster.state==7"  style="color:#E6A23C"  >已复核</span>
                  <span  v-if="soMaster.state==8"  style="color:#67C23A"  >全部发运</span>
                  <span  v-if="soMaster.state==12"  style="color:#67C23A"  >部分发运</span>
                  
                </li>
                <li>
                  <span>发运状态:</span>
                  <span v-if="soMaster.sendState == 'NORMAL'" style="color:#909399">未发运</span>
                  <span v-if="soMaster.sendState == 'PARTIAL'" style="color:#67C23A">部分发运</span>
                  <span v-if="soMaster.sendState == 'ALL'" style="color:#67C23A">全部发运</span>
                </li>
                 <li>
                  <span>重量:</span>
                 
                  <span :title="soMaster.weight">&nbsp;{{soMaster.weight}}</span>
                </li>


                
                <!-- <li>
                  <span>单据来源:</span>
                  <span v-if="billNoType === 'HB'">合并</span>
                  <span v-else-if="soMaster.billSource === 1">新建</span>
                  <span v-else-if="soMaster.billSource === 2">合并</span>
                </li>
                <li>
                  <span>发运单类型:</span>&nbsp;
                  <span :title="soMaster.dictValue" v-for="(item,index) in soBillTypes" :key="index"
                        v-if="soMaster.soType === item.dataDictDetailId">{{item.dictValue}}</span>
                </li> -->
                <li v-if="soMaster.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="soMaster.state === -2">草稿</span>
                  <span v-else-if="soMaster.state === 2">待审核</span>
                  <span v-else-if="soMaster.state === 1">审核通过</span>
                  <span v-else-if="soMaster.state === 0">审核中</span>
                  <span v-else-if="soMaster.state === -1">审核退回</span>
                </li>
                <!-- <li>
                  <span>保存类型:</span>&nbsp;
                  <span v-if="soMaster.insertType === 0">单据</span>
                  <span v-else-if="soMaster.insertType === -2">草稿</span>
                </li>
                <li>
                  <span>预发时间FM:</span>
                  <span :title="soMaster.expectTimeFm">&nbsp;{{soMaster.expectTimeFm}}</span>
                </li>
                <li>
                  <span>预发时间TO:</span>
                  <span :title="soMaster.expectTimeTo">&nbsp;{{soMaster.expectTimeTo}}</span>
                </li> -->
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="soMaster.wareName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.wareId, 'ware')">&nbsp;{{soMaster.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="soMaster.organizationName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.organizationId, 'organization')">&nbsp;{{soMaster.organizationName}}</span>
                </li>
                <li>
                  <span>客户:</span>
                  <span :title="soMaster.customerName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.customerId, 'customer')">&nbsp;{{soMaster.customerName}}</span>
                </li>
                <li>
                  <span>收货人:</span>
                  <span :title="soMaster.linkMan">&nbsp;{{soMaster.linkMan}}</span>
                </li>
                <li>
                  <span>承运方:</span>
                  <span :title="soMaster.shipCode">&nbsp;{{soMaster.shipCode}}</span>
                </li>
                <!-- <li>
                  <span>交货时间:</span>
                  <span :title="soMaster.deliveryTime">&nbsp;{{soMaster.deliveryTime}}</span>
                </li>
                <li>
                  <span>生成加工单:</span>
                  <span>{{soMaster.manufactureType === 1 ? '是' : '否'}}</span>
                </li> -->
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>创建人:</span>
                  <span :title="soMaster.createUserName" class="textBtn"
                        @click="$parent.showPublicModal(soMaster.createUserId, 'account')">&nbsp;{{soMaster.createUserName}}</span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span :title="soMaster.createTime">&nbsp;{{soMaster.createTime}}</span>
                </li>
                <li>
                  <span>备注:</span>
                  <span :title="soMaster.memo">&nbsp;{{soMaster.memo}}</span>
                </li>
                <!-- <li v-for="(item,index) in soUdfHs" :key="index">
                  <span>{{item.value}}:</span>
                  <span :title="soMaster[item.paramCode]">&nbsp;{{soMaster[item.paramCode]}}</span>
                </li> -->
              </ul>
            </div>
          </el-tab-pane>
          <el-tab-pane v-if="soMaster.insertType === 0" label="审核进程" name="reviewStep">
            <ReviewStepBox :masterData="soMaster.billNo" :activeName="activeTabName"></ReviewStepBox>
          </el-tab-pane>
          <!-- <el-tab-pane v-if="soMaster.insertType === 0" label="物流信息" name="righttab">
            <div hidden>
              <form id="printParamsForm" action="http://www.kdniao.com/External/PrintOrder.aspx" method="post"
                    target="_self">
                <div style="">
                  <div>RequestData<input v-model="printParams.RequestData" type="text" id="RequestData"
                                         name="RequestData"/></div>
                  <div>EBusinessID<input v-model="printParams.EBusinessID" type="text" id="EBusinessID"
                                         name="EBusinessID"/></div>
                  <div>DataSign<input v-model="printParams.DataSign" type="text" id="DataSign" name="DataSign"/>
                  </div>
                  <div>IsPreview<input v-model="printParams.IsPreview" type="text" id="IsPreview" name="IsPreview"/>
                  </div>
                </div>
              </form>

            </div>

            <div style="margin-left: 20px; height: 250px; overflow: auto">
              <ul v-for="(trace,index) in traces" :key="index" :class="'primary'">
                <li style="color: #7bb2ff;">{{trace.acceptTime}}</li>
                <li>{{trace.acceptStation}}</li>
                <li>{{trace.remark}}</li>
              </ul>

            </div>


          </el-tab-pane> -->
          <el-tab-pane label="订单进程" name="billRecord">
            <BillRecordBox :masterData="soMaster.billNo" :activeName="activeTabName"></BillRecordBox>
          </el-tab-pane>
           <el-tab-pane label="发货地址" name="5">
             <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>发件人:</span>
                  
                  <span :title="soMaster.shipLinkMan">&nbsp;{{soMaster.shipLinkMan}}</span>
                </li>
                <li>
                  <span>联系方式:</span>
                  
                  <span  >&nbsp;{{soMaster.linkPhone}}</span>
                </li>
                <li>
                  <span>国家:</span>
                  
                  <span  >&nbsp;{{soMaster.shipCountry}}</span>
                </li>
                <li>
                  <span>省:</span>
                  
                  <span  >&nbsp;{{soMaster.shipProvince}}</span>
                </li>
                <li>
                  <span>市:</span>
                  
                  <span  >&nbsp;{{soMaster.shipCity}}</span>
                </li>
                 <li>
                  <span>区:</span>
                  
                  <span  >&nbsp;{{soMaster.shipArea}}</span>
                </li>
                 <li>
                  <span>详细地址:</span>
                  
                  <span  >&nbsp;{{soMaster.shipDetailAddress}}</span>
                </li>
                
              </ul>
            
              
            </div>
          </el-tab-pane>
           <el-tab-pane label="收货地址" name="6">
             <div class="newBillData clearFloat">
            <ul class="leftFloat">
                <li>
                  <span>收件人:</span>
                  
                  <span :title="soMaster.linkMan">&nbsp;{{soMaster.linkMan}}</span>
                </li>
                <li>
                  <span>联系方式:</span>
                  
                  <span  >&nbsp;{{soMaster.linkPhone}}</span>
                </li>
                <li>
                  <span>国家:</span>
                  
                  <span  >&nbsp;{{soMaster.country}}</span>
                </li>
                <li>
                  <span>省:</span>
                  
                  <span  >&nbsp;{{soMaster.province}}</span>
                </li>
                <li>
                  <span>市:</span>
                  
                  <span  >&nbsp;{{soMaster.city}}</span>
                </li>
                 <li>
                  <span>区:</span>
                  
                  <span  >&nbsp;{{soMaster.area}}</span>
                </li>
                 <li>
                  <span>详细地址:</span>
                  
                  <span  >&nbsp;{{soMaster.detailAddress}}</span>
                </li>
                
              </ul>
             </div>
          </el-tab-pane>
        </el-tabs>


        <!--<div class="billMasterBox clearFloat">
          <div class="billData leftFloat clearFloat">
            <div class="title">
              <span>SO单据信息</span>
            </div>
            <ul class="leftFloat">
              <li>
                <span>单号:</span>
                <span v-if="billNoType === 'HB'">&nbsp;{{soMaster.mergeNo}}</span>
                <span v-else>&nbsp;{{soMaster.billNo}}</span>
              </li>
              <li>
                <span>单据来源:</span>
                <span v-if="billNoType === 'HB'">合并</span>
                <span v-else-if="soMaster.billSource === 1">新建</span>
                <span v-else-if="soMaster.billSource === 2">合并</span>
              </li>
              <li>
                <span>发运单类型:</span>&nbsp;
                <span v-for="item in soBillTypes"
                      v-if="soMaster.soType === item.dataDictDetailId">{{item.dictValue}}</span>
              </li>
              <li v-if="soMaster.insertType === 0">
                <span>审核状态:</span>&nbsp;
                <span v-if="soMaster.state === -2">草稿</span>
                <span v-else-if="soMaster.state === 2">待审核</span>
                <span v-else-if="soMaster.state === 1">审核通过</span>
                <span v-else-if="soMaster.state === 0">审核中</span>
                <span v-else-if="soMaster.state === -1">审核退回</span>
              </li>
              <li>
                <span>保存类型:</span>&nbsp;
                <span v-if="soMaster.insertType === 0">单据</span>
                <span v-else-if="soMaster.insertType === -2">草稿</span>
              </li>
              <li>
                <span>备注:</span>
                <span>&nbsp;{{soMaster.memo}}</span>
              </li>
              <li v-for="item in soUdfHs">
                <span>{{item.value}}:</span>
                <span :title="soMaster[item.paramCode]">&nbsp;{{soMaster[item.paramCode]}}</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span>仓库:</span>
                <span>&nbsp;
                  <span class="textBtn"
                        @click="$parent.showPublicModal(soMaster.wareId, 'ware')">{{soMaster.wareName}}</span>
                </span>
              </li>
              <li>
                <span>货主:</span>
                <span :title="soMaster.organizationName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(soMaster.organizationId, 'organization')">{{soMaster.organizationName}}</span>
                </span>
              </li>
              <li>
                <span>客户:</span>
                <span :title="soMaster.customerName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(soMaster.customerId, 'customer')">{{soMaster.customerName}}</span>
                </span>
              </li>
              <li>
                <span>收货人:</span>
                <span>&nbsp;{{soMaster.linkMan}}</span>
              </li>
              <li>
                <span>承运方:</span>
                <span>&nbsp;{{soMaster.carrierName}}</span>
              </li>
              <li>
                <span>创建人:</span>
                <span :title="soMaster.createUserName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(soMaster.userId, 'account')">{{soMaster.createUserName}}</span>
                </span>
              </li>
              <li>
                <span>创建时间:</span>
                <span>&nbsp;{{soMaster.createTime}}</span>
              </li>
              <li>
                <span>交货时间:</span>
                <span>&nbsp;{{soMaster.deliveryTime}}</span>
              </li>
              <li>
                <span>预发时间FM:</span>
                <span>&nbsp;{{soMaster.expectTimeFm}}</span>
              </li>
              <li>
                <span>预发时间TO:</span>
                <span>&nbsp;{{soMaster.expectTimeTo}}</span>
              </li>
            </ul>
          </div>

          <div style="margin-left: 40%; width: 55%; height: 315px;">
            <el-tabs type="card" v-model="activeTabName" @tab-click="handleTabClick">
              <el-tab-pane label="审核信息" name="lefttab">
                <ReviewStepBox :masterData="soMaster.billNo"></ReviewStepBox>

              </el-tab-pane>
              <el-tab-pane label="物流信息" name="righttab">
                <div hidden>
                  <form id="printParamsForm" action="http://www.kdniao.com/External/PrintOrder.aspx" method="post"
                        target="_self">
                    <div style="">
                      <div>RequestData<input v-model="printParams.RequestData" type="text" id="RequestData"
                                             name="RequestData"/></div>
                      <div>EBusinessID<input v-model="printParams.EBusinessID" type="text" id="EBusinessID"
                                             name="EBusinessID"/></div>
                      <div>DataSign<input v-model="printParams.DataSign" type="text" id="DataSign" name="DataSign"/>
                      </div>
                      <div>IsPreview<input v-model="printParams.IsPreview" type="text" id="IsPreview" name="IsPreview"/>
                      </div>
                    </div>
                  </form>

                </div>

                <div style="margin-left: 20px; height: 250px; overflow: auto">
                  <ul v-for="trace in traces" :class="'primary'">
                    <li style="color: #7bb2ff;">{{trace.acceptTime}}</li>
                    <li>{{trace.acceptStation}}</li>
                    <li>{{trace.remark}}</li>
                  </ul>

                </div>


              </el-tab-pane>

            </el-tabs>
          </div>

        </div>-->
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
            :data="soDetails"
            style="width: 100%">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
          <!--  <el-table-column
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
            </el-table-column>-->
        
            <el-table-column
              show-overflow-tooltip
              prop="itemName"
              label="SKU名称"
              min-width="300">
              <template slot-scope="scope">
                <el-row :gutter="24">
              <el-col :span="7"> 
                <div style="display: inline-block;   width:80px;height:86px">
                  <img :src="scope.row.imgUrl"  style="width:80px;height:86px">
                </div>
               </el-col>
              <el-col :span="15">
                  <span class="textBtn" style="color:red"  >{{scope.row.state==-1?"【已取消】":""}}{{scope.row.itemCode}}</span>
                        <br>
                    <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{scope.row.itemName}}</span>
                    <br>
                    <span>{{scope.row.spec}}</span>
                    <br>
                    <span>{{scope.row.model}}</span>
               
              </el-col>
              
            </el-row>
                <!--<span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>-->
              </template>
            </el-table-column>
         
            <!-- <el-table-column
              show-overflow-tooltip
              label="状态"
              width="180">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.state === 1" type="info">部分发货</el-tag>
                <el-tag v-else-if="scope.row.state === 2">全部发货</el-tag>
                <el-tag v-else-if="scope.row.state === 3">部分拣货</el-tag>
                <el-tag v-else-if="scope.row.state === 4">全部拣货</el-tag>
                
              </template>
            </el-table-column> -->
            <!-- <el-table-column
                    prop="bomSplit"
                    label="拆分BOM"
                    width="100">
              <template slot-scope="scope">
                <span>{{scope.row.bomSplit === 1 ? '需要拆分' : '不拆分'}}</span>
              </template>
            </el-table-column> -->

            <el-table-column
              show-overflow-tooltip
              prop="orderQuantity"
              label="订货数量"
              min-width="180">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.orderQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
             <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePack(scope.$index)"
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
              show-overflow-tooltip
              prop="allottedQuantity"
              label="分配数"
              min-width="180">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.allottedQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="pickQuantity"
              label="拣货数量"
              min-width="180">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.pickQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="checkQuantity"
              label="复核数量"
              min-width="180">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.checkQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="deliveryQuantity"
              label="发货数量"
              min-width="180">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.deliveryQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column
              show-overflow-tooltip
              v-for="(item,index) in soUdfDs"
              :key="index"
              :prop="item.paramCode"
              :label="item.value"
              width="180">
            </el-table-column> -->
            
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="拣货任务" name="second">
        <div class="tableBox">
          <el-table
            highlight-current-row
            @selection-change="handleSelectionChange2"
            :data="pickTasks"
            style="width: 100%">
            <el-table-column
              type="selection"
              :selectable="checkSelectable"
              width="55">
            </el-table-column>
            <el-table-column
              fixed="left"
              type="index"
              width="50">
            </el-table-column>
          
             <el-table-column
             
              show-overflow-tooltip
              prop="inventoryId"
              label="inventoryId"
              width="180">
             
            </el-table-column>
             <el-table-column
             
              show-overflow-tooltip
              prop="invenId"
              label="invenId"
              width="180">
             
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
              sortable
              show-overflow-tooltip
              prop="state"
              label="状态"
              min-width="80">
              <template slot-scope="scope">
                <span style="color:#F56C6C" v-if="scope.row.state==0">待拣货</span>
                <span style="color:#E6A23C"  v-if="scope.row.state==1">部分完成</span>
                <span style="color:#409EFF"  v-if="scope.row.state==2">拣货完成</span>
                <span style="color:#67C23A"  v-if="scope.row.state==5">发运</span>
                <span style="color:#909399"  v-if="scope.row.state==-1">取消</span>
                <!-- <el-tag v-for="(item,index) in pickTaskType" :key="index" v-if="scope.row.state === item.value">{{item.label}}</el-tag> -->
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="pickUserName"
              label="拣货人"
              width="180">
              
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="pickTime"
              label="拣货时间"
              width="180">
              
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="organizationName"
              label="货主"
              width="180">
              
            </el-table-column>
            <!-- <el-table-column
              show-overflow-tooltip
              prop="batchName"
              label="批次"
              width="180">
            </el-table-column> -->
            <el-table-column
              show-overflow-tooltip
              prop="pickQuantity"
              label="拣货数量"
              width="180">
              <template slot-scope="scope">
                <span style="color: #409EFF;">{{ scope.row.reviewQuantity / scope.row.transRatio }}</span>
                <span v-if="scope.row.reviewQuantity<scope.row.pickQuantity">{{' / ' + scope.row.pickQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column
              show-overflow-tooltip
              prop="packDescribe"
              label="单位"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="boxCode"
              label="追踪号"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="lpn"
              label="lpn"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="areaName"
              label="货区"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="shelfName"
              label="货架"
              width="180">
            </el-table-column> -->
            <el-table-column
              show-overflow-tooltip
              prop="cellCode"
              label="货位"
              width="180">
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="出库任务" name="third">
        <div class="tableBox">
          <el-table
            highlight-current-row
            @selection-change="handleSelectionChange3"
            :data="sendDetails"
            style="width: 100%">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="itemName"
              label="SKU名称">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="itemCode"
              label="SKU">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              label="状态"
              width="180">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.state === 0" type="info">待处理</el-tag>
                <el-tag v-if="scope.row.state === 1" type="info">部分分配</el-tag>
                <el-tag v-else-if="scope.row.state === 2">全部分配</el-tag>
                <el-tag v-else-if="scope.row.state === 3">部分拣货</el-tag>
                <el-tag v-else-if="scope.row.state === 5">全部拣货</el-tag>
                <el-tag v-else-if="scope.row.state === 7">部分发运</el-tag>
                <el-tag v-else-if="scope.row.state === 6">完全发运</el-tag>
                <el-tag v-else-if="scope.row.state === -1">取消</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="sendUserName"
              label="发运人">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="orderQuantity"
              label="订单数量">
              <template slot-scope="scope">
                <span :style="scope.row.state==-1?{'text-decoration':'line-through'}:{}">{{ scope.row.orderQuantity / scope.row.transRatio }}</span>
              </template>
            </el-table-column>

            <el-table-column
              show-overflow-tooltip
              prop="pickQuantity"
              label="实际扫码数量">
              <template slot-scope="scope">
                <span >{{ scope.row.sendQuantity }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="sendTime"
              label="发运扫码时间">
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <!-- <el-tab-pane label="装箱明细" name="third">
        <div class="tableBox">
          <el-table
            highlight-current-row
            :data="boxDetails"
            style="width: 100%">
            <el-table-column
              fixed="left"
              show-overflow-tooltip
              prop="itemName"
              label="SKU名称"
              width="150">
              <template slot-scope="scope">
                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="itemCode"
              label="SKU"
              width="150">
              <template slot-scope="scope">
                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="pickTaskId"
              label="拣货任务"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="pickQuantity"
              label="拣货数量"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="quantity"
              label="装箱数量"
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
              prop="billNo"
              label="SO单号"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="memo"
              label="memo"
              width="150">
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane> -->
      <!-- <el-tab-pane label="装车明细" name="fourth">
        <el-table
          highlight-current-row
          :data="loadDetails"
          style="width: 100%">
          <el-table-column
            prop="loadMasterCode"
            label="装车单编码"
          ></el-table-column>
          <el-table-column
            prop="pickTaskId"
            label="拣货单ID"
          ></el-table-column>
          <el-table-column
            prop="itemCode"
            label="SKU">
          </el-table-column>
          <el-table-column
            prop="itemName"
            label="SKU名称">
          </el-table-column>
          <el-table-column
            prop="pickQuantity"
            label="拣货数量">
          </el-table-column>
          <el-table-column
            prop="quantity"
            label="装车数量"
          >
          </el-table-column>
          <el-table-column
            prop="packDescribe"
            label="单位">
          </el-table-column>
          <el-table-column
            prop="boxNo"
            label="箱号">
          </el-table-column>
        </el-table>
      </el-tab-pane> -->
    </el-tabs>

  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'
import { off } from 'process';


  export default {
    name: 'SoBillDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
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
        loading:false,
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
        

        soMaster: {},
        soDetails: [],
        sendDetails:[],
        form: {
          soMaster: {},
          soDetails: [],
        },
        soUdfHs: [],
        soUdfDs: [],
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
        multipleSelection3: [],
        soBillTypes: [],
        traces: [],
        pickTaskMaster: {},
        two: '0',
        receivingAddresss: [],

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
        return this.form.soMaster.customerId
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
            this.form.soMaster.customerName = this.customers[i].customerName;
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
      demoPrint:function (isReview){
        var that = this; 
         IOT.getServerData('/so/masters/getDzmd', 'get', {billNo:this.$route.params.billNo}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data; 
                that.$parent.printHtml(strHTML,isReview); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      demoPrint2:function (isReview){
        var that = this; 
        var soId =this.soMaster.soMasterId;
         IOT.getServerData('/so/masters/getPrintData', 'get', {soId:soId}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data; 
                that.$parent.printHtml(strHTML,isReview); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      demoPrint4:function (isReview){
        var that = this; 
        var soId =this.soMaster.soMasterId;
         IOT.getServerData('/so/masters/getPrintInvoice', 'get', {soId:soId}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data; 
                that.$parent.printUrl(strHTML); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      demoPrint3:function (isReview){
        var that = this; 
        var soId =this.soMaster.soMasterId;
         IOT.getServerData('/so/masters/getPickPrintData', 'get', {waveId:80,wareId:287}, (ret) => {
         
          if (ret.code === 200) { 
                var strHTML= ret.data[0].html; 
                that.$parent.printHtml(strHTML,isReview); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      demoPrint5:function (isReview){
        var that = this; 
        var billNo =this.soMaster.billNo;
         IOT.getServerData('/so/masters/getSOPickTask', 'get', {billNo:billNo}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data; 
                that.$parent.printHtml(strHTML,isReview); 
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      checkSelectable(row) {
        if (row.state === 2 || row.state === 3) {
          return false
        } else {
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
      handleSelectionChange3(val) {
        this.multipleSelection3 = val;
      },
      printList() {
        this.$router.push({name: 'printView', params: {printList: this.multipleSelection}});
      },
      getSoMasterData() {
         this.loading = true;
        let searchData = PF.JSON(this.searchData);
        /*if (this.billNoType === 'HB') {
          searchData.mergeNo = this.$route.params.billNo;
          delete searchData.billNo;
        }*/
        IOT.getServerData('/so/masters/list', 'get', searchData, (ret) => {
           this.loading = false;
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
            this.soMaster = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSoDetailData() { 
        this.loading = true;
        let searchData = PF.JSON(this.searchData);
        /*if (this.billNoType === 'HB') {
          searchData.mergeNo = this.$route.params.billNo;
          delete searchData.billNo;
        }*/
        IOT.getServerData('/so/details/list', 'get', searchData, (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.soDetails = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      //出库任务列表
      getSendDetailData() { 
        this.loading = true;
        let data={
          billNo:this.$route.params.billNo,
        }
        IOT.getServerData('/so/details/listAndSend', 'post', data, (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.sendDetails = ret.rows;
            console.log(this.sendDetails);
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
        IOT.getServerData('/platforms/list', 'get', {wareId: this.soMaster.wareId}, (ret) => {
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
        IOT.getServerData('/pick/tasks/findBySoMasterId', 'get', {
          billNo: this.$route.params.billNo,
          wareId: this.soMaster.wareId,
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
            this.form.soMaster.carrierId = list[i].carrierId
            this.form.soMaster.carrierName = list[i].carrierName
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
      isDelete(billNo, state) {
        if (state == -1) {
          IOT.tips('该出库单已是取消状态！', 'success');
          return false
        }
        this.$confirm('确认取消？')
          .then(_ => {
            this.deleteBuyBillIn(billNo)
          })
          .catch(_ => {
          });
      },
      deleteBuyBillIn(billNo) {
        IOT.showOverlay('处理中，请稍等...');
        IOT.getServerData('/so/masters/cancelSO', 'get', {billNo: billNo}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.go(-1);
            IOT.tips('取消成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showUpdateModal() {
        let soDetails = PF.JSON(this.soDetails);
        for (let i = 0; i < soDetails.length; i++) {
          soDetails[i].orderQuantity = soDetails[i].orderQuantity / soDetails[i].transRatio;
        }
        this.form = {
          insertType: -2,
          soMaster: PF.JSON(this.soMaster),
          soDetails: soDetails
        };
        PF.getOrganizations((rows) => {
          this.organizations = rows;
        });
        this.getPlatformData();
        this.changeCustomerId(this.form.soMaster.customerId);
        // this.getCarrierData();
        this.dialogNewVisible = true;
      },
      showSelectItemModal() {
        this.ids = [];
        for (let i = 0; i < this.form.soDetails.length; i++) {
          this.ids.push(this.form.soDetails[i].itemCode)
        }
        this.searchDataItem.wareId = PF.getLocal('wareId', 'number');
        this.searchDataItem.organizationId = this.form.soMaster.organizationId;
        this.getItemData();
        this.innerVisibleItem = true
      },
      selectItem(row) {
        if (this.ids.indexOf(row.itemCode) !== -1) {
          IOT.tips('相同物料不能重复添加！', 'error');
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
        this.form.soDetails.push(row);
        // this.form.soDetails.expectQuantity = 0;
        this.innerVisibleItem = false;
      },
      deleteDetail(index) {
        this.form.soDetails.splice(index, 1)
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = this.form;
            if (form.soDetails.length <= 0) {
              IOT.tips('未选择物料,请重新选择！', 'error', 1000, () => {
                IOT.hideOverlay();
              });
              return false
            }
            for (let i = 0; i < form.soDetails.length; i++) {
              if (!PF.isNull(form.soDetails[i].orderQuantity) && form.soDetails[i].orderQuantity <= 0) {
                IOT.tips('数量需大于零！', 'error', 1000, () => {
                  IOT.hideOverlay();
                });
                return false
              }
              /*if(!PF.isNull(form.soDetails[i].expectAllottedQuantity) && form.soDetails[i].expectAllottedQuantity <= 0){
                IOT.tips('数量需大于零！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }*/
              // 主单位数量
              form.soDetails[i].orderQuantity = form.soDetails[i].orderQuantity * form.soDetails[i].transRatio;
              // form.soDetails[i].expectAllottedQuantity = form.soDetails[i].expectAllottedQuantity * form.soDetails[i].transRatio;
            }
            IOT.getServerData('/so/masters/update', 'post', form, (ret) => {
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
        let soMaster = PF.JSON(this.soMaster);
        delete soMaster.soMasterId;
        let soDetails = PF.JSON(this.soDetails);
        for (let i = 0; i < soDetails.length; i++) {
          delete soDetails[i].soDetailId;
        }
        let form = {
          billSource: soMaster.billSource,
          insertType: 0,
          soMaster: soMaster,
          soDetails: soDetails
        };
        if (form.soDetails.length <= 0) {
          IOT.tips('未选择物料,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        for (let i = 0; i < form.soDetails.length; i++) {
          if (form.soDetails[i].expectQuantity === 0) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.soDetails[i].expectQuantity = form.soDetails[i].expectQuantity * form.soDetails[i].transRatio;
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
      changePack(index){
         let form = PF.JSON(this.soDetails[index]);
        let packDetails = PF.JSON(this.soDetails[index].packDetails);
        for (let i = 0; i < packDetails.length; i++) {
          if (packDetails[i].packDetailId1 === form.packDetailId) {
            this.soDetails[index].packDescribe = packDetails[i].packDescribe1;
            this.soDetails[index].transRatio = packDetails[i].transRatio1;
          }
        }

      },
      changePickDetailId(index) {
        
        let form = PF.JSON(this.inventoryBalances[index]);
        let packDetails = PF.JSON(this.inventoryBalances[index].packDetails);
        for (let i = 0; i < packDetails.length; i++) {
          if (packDetails[i].packDetailId === form.packDetailId) {
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
              packDetailId:list[i].packDetailId
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
      commitDelivery(type) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        let form = [];
        
        if (type === 'acceptList') {
          form = PF.JSON(this.multipleSelection2);
        } else if (type === 'accept') {
          form.push(PF.JSON(this.pickTasks));
        }
        // for (let i = 0; i < form.length; i++) {
        //   if (form[i].toQuantity === 0) {
        //     IOT.tips('数量不得为零！', 'error', 1000, () => {
        //       IOT.hideOverlay();
        //     });
        //     return false
        //   }
        //   if(form[i].toCellCode === ''){
        //     IOT.tips('货位不得为空！', 'error', 1000, () => {
        //       IOT.hideOverlay();
        //     });
        //     return false
        //   }
        //   if(type === 'acceptList' && !form[i].isSetBatch){
        //     let isNext = this.checkBatchTactic(form[i]);
        //     if(!isNext){
        //       return false
        //     }
        //   }
        //
        //   form[i].asnDetailNo = form[i].detailNo;
        //   form[i].toPackDetailId = form[i].packDetailId;
        //   form[i].toPackDescribe = form[i].packDescribe;
        //   form[i].asnBillNo = form[i].billNo;
        //
        // }
        IOT.getServerData('/pick/tasks/delivery', 'post', {pickTasks: form}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('发货成功！', 'success', 1000, () => {
              that.getSoDetailData()
              that.getPickTaskData()
              that.resetForm('pickTasks');
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
            this.form.soMaster.linkMan = list[i].linkMan
          }
        }
      },
      changePackDetailId(index) {
        let detail = PF.JSON(this.form.soDetails[index]);
        for (let i = 0; i < detail.packDetails.length; i++) {
          if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
            this.form.soDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
            this.form.soDetails[index].transRatio = detail.packDetails[i].transRatio1;
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
      PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
        this.soUdfHs = udfs;
      });
      PF.getSysParamUdfData('soUdfDs', [], (data, udfs) => {
        this.soUdfDs = udfs;
      });
      PF.getDataDictUdfData('soBillType', (rows) => {
        this.soBillTypes = rows;
      });
      this.getSoMasterData();
      this.getSoDetailData();
      this.getSendDetailData();
      // this.getBoxDetailsData()
      // this.getLoadDetailData()
      PF.getCustomers((rows) => {
        this.customers = rows;
      });

    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .soBillDetail {
  }
</style>
