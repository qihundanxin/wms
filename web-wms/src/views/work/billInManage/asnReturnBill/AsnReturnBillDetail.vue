<template>
  <div   v-loading="loading" class="AsnReturnBillDetail">

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
      </el-button>
       <el-button   size="small" type="success"   icon="iconfont icon-xinzeng"
                 @click="exportAns()"> 导出
      </el-button>
      <!-- <el-button size="small" type="primary"   icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回</el-button>
      <el-button v-if="asnMaster.insertType === -2" size="small" type="info"   icon="iconfont icon-tijiao"
                 @click="showUpdateModal"> 修改
      </el-button>
      <el-button v-if="asnMaster.insertType === -2" size="small" type="success"   icon="iconfont icon-xinzeng"
                 @click="isCommit()"> 提交
      </el-button>
      <el-button size="small" type="danger"   icon="iconfont icon-shanchu" @click="isDelete(asnMaster.asnMasterId)">
        删除
      </el-button>
      <el-button v-if="!isFile && asnMaster.insertType === 0 && asnMaster.qcState === -1" size="small" type="warning"   icon="iconfont icon-qc"
                 @click="showQcModal"> 质检
      </el-button>
      <el-button v-if="(asnMaster.state === 0 || asnMaster.state === 2) && formReview.caseDetailId !== null" size="small" type="warning"  
                 icon="iconfont icon-shenhe" @click="dialogReviewVisible = true"> 审核
      </el-button> -->
    </div>

    <el-dialog title="修改入库单" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <!--master-->
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form.asnMaster" :rules="rules" ref="form.asnMaster">
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="月台" prop="platformCode" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.platformCode" filterable placeholder="请选择月台" style="width: 70%;">
              <el-option
                v-for="item in platforms"
                :key="item.platformCode"
                :label="item.label"
                :value="item.platformCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="承运方" prop="carrierCode" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.carrierCode" filterable placeholder="请选择承运方" style="width: 70%;">
              <el-option
                v-for="item in carriers"
                :key="item.carrierCode"
                :label="item.carrierName"
                :value="item.carrierCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="入库单类型" prop="asnType" :label-width="formLabelWidth">
            <el-select v-model="form.asnMaster.asnType" filterable placeholder="请选择入库单类型" style="width: 70%;">
              <el-option
                v-for="item in asnTypes"
                :key="item.dataDictDetailId"
                :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="优先级" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.orderNo" placeholder="请输入优先级"></el-input>
          </el-form-item>
          <el-form-item label="交货地址" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.deliveryPlace" placeholder="请输入交货地址"></el-input>
          </el-form-item>
          <el-form-item label="运输描述" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.trafficDescribe" placeholder="请输入运输描述"></el-input>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in asnUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.asnMaster[item.paramCode]" :placeholder="'请输入'+item.value"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div class="headerBtns">
        <el-button size="small" type="primary"   icon="iconfont icon-xinzeng" @click="showSelectItemModal"> 选择商品
        </el-button>
      </div>

      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="form.asnDetails"
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
            label="预约数量"
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
              <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index, 'update')" filterable placeholder="请选择单位" size="small">
                <el-option
                  v-for="item in form.asnDetails[scope.$index].packDetails"
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
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.label" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="60">
            <template slot-scope="scope">
              <el-button @click="deleteDetail(scope.$index)" :disabled="form.asnDetails.length === 1"
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
    <el-dialog title="生成 QC 单据" :visible.sync="dialogQcVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -10vh;">
      <!--master-->
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="formQc.qcMaster" :rules="rulesQc" ref="formQc.qcMaster">
          <el-form-item label="入库单号" prop="AsnReturnBillNo" :label-width="formLabelWidth">
            <el-input v-model="formQc.qcMaster.AsnReturnBillNo" placeholder="请输入入库单号" disabled></el-input>
          </el-form-item>
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <el-select v-model="formQc.qcMaster.wareId" filterable placeholder="请选择仓库" disabled style="width: 70%;">
              <el-option
                :key="formQc.qcMaster.wareId"
                :label="formQc.qcMaster.wareName"
                :value="formQc.qcMaster.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="formQc.qcMaster.organizationId" filterable placeholder="请选择货主" disabled
                       style="width: 70%;">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="formQc.qcMaster.supplierId" filterable placeholder="请选择供应商" disabled
                       style="width: 70%;">
              <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="质检类型" prop="qcType" :label-width="formLabelWidth">
            <el-select v-model="formQc.qcType" filterable placeholder="请选择质检类型" style="width: 70%;">
              <el-option
                v-for="item in qcTypes"
                :key="item.dataDictDetailId"
                :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="formQc.qcMaster.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in qcUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="formQc.qcMaster[item.paramCode]" :placeholder="'请输入' + item.value"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!--detail-->
      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="formQc.qcDetails"
          max-height="360"
          style="width: 100%">
          <el-table-column
            fixed="left"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="itemCode"
            label="SKU"
            width="120">
          </el-table-column>
          <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="120">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="expectQuantity"
            label="预约数量"
            width="100">
            <template slot-scope="scope">
              <span>{{ Math.floor(scope.row.expectQuantity / scope.row.transRatio * 10000) / 10000 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="qcQuantity"
            label="质检数量"
            width="150">
            <template slot-scope="scope">
              <input type="number" class="my_input small" v-model.number="scope.row.qcQuantity" placeholder="质检数量"
                     style="width: 80%;">
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index,'qc')" filterable
                         placeholder="请选择单位" size="small">
                <el-option
                  v-for="item in scope.row.packDetailsList"
                  :key="item.packDetailId"
                  :label="item.packDescribe"
                  :value="item.packDetailId">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            v-for="(item,index) in qcUdfDs"
            :key="index"
            :prop="item.paramCode"
            :label="item.value"
            width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row[item.paramCode]" :placeholder="'请输入' + item.value" size="small"
                        clearable></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
       <!-- <el-button class="leftFloat" type="primary" plain @click="commitFormQc('formQc.qcMaster',-2)">保存为草稿</el-button> -->
        <el-button @click="handleClose2">取 消</el-button>
        <el-button type="primary" @click="commitFormQc('formQc.qcMaster',0)">提 交</el-button>
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
    <el-dialog title="入库单验收" :visible.sync="dialogAcceptVisible" :before-close="handleClose4" width="900px" style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="formAccept" :rules="rulesAccept" ref="formAccept">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{formAccept.wareName}}</span>
            
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <span>{{formAccept.organizationName}}</span>
             </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <span>{{formAccept.itemName}}</span>
            </el-form-item>
          <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <span>{{formAccept.itemCode}}</span>
            </el-form-item>
          <el-form-item label="预约数 " prop="expectQuantity+'/'+acceptQuantity" :label-width="formLabelWidth">
            <span>{{formAccept.expectQuantity/formAccept.transRatio}}{{formAccept.packDescribe}}/{{formAccept.expectQuantity}}主单位</span>
          </el-form-item>
          <el-form-item label="已验收数量" prop="acceptQuantity" :label-width="formLabelWidth">
            <span>{{formAccept.acceptQuantity}}（主单位）</span>
          </el-form-item>
          <el-form-item label="单位" prop="toPackDetailId" :label-width="formLabelWidth">
            <el-select v-model="formAccept.toPackDetailId" @change="changePackDetailId(0,'accept')" filterable placeholder="请选择单位" style="width: 90%;">
              <el-option
                v-for="item in packDetails"
                :key="item.packDetailId"
                :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="验收数量" prop="toQuantity" :label-width="formLabelWidth">
            <el-input-number v-model="formAccept.toQuantity" controls-position="right" :min="0"
                             :max="formAccept.toQuantityMax * acceptExcess" label="请输入数量"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="验收数量" prop="toQuantity" :label-width="formLabelWidth">
          <span style="color:red">{{formAccept.toQuantity*formAccept.toTransRatio}}（主单位） </span>
          </el-form-item>
          
          <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
            <el-input v-model="formAccept.toBoxCode" placeholder="请输入追踪号" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="formAccept.memo" placeholder="请输入备注" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货位编码" prop="toCellCode" :label-width="formLabelWidth">
            <el-input :title="formAccept.toCellCode" v-model="formAccept.toCellCode" placeholder="请输入编码" style="width: 80%;"></el-input>
            <el-button @click="showSelectCellModal()" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">批次策略</el-divider>
          </div>
          <div v-if="batchTactics.batchTacticDetails === undefined
          || batchTactics.batchTacticDetails === null
          || batchTactics.batchTacticDetails === []"
               style="float: left;width: 100%;line-height: 60px;color: #909399;text-align: center;">无可用属性
          </div>
          <el-form-item v-for="(item,index) in batchTactics.batchTacticDetails" :key="index" :label="item.detailName" :prop="item.detailCode"
                        :label-width="formLabelWidth">
            <el-select v-if="item.detailCode === 'qcState'" v-model="formAccept[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in qcStates"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>

            <el-input :title="formAccept[item.detailCode]" v-else-if="item.format === 0" v-model="formAccept[item.detailCode]"
                      :disabled="item.detailCode === 'detailNo' || item.detailCode === 'supplierCode'" :placeholder="'请输入'+item.detailName"
                      style="width: 90%;"></el-input>
            <el-input-number v-else-if="item.format === 1" v-model="formAccept[item.detailCode]" controls-position="right"
                             :min="0" label="请输入" style="width: 90%;"></el-input-number>
            <el-date-picker v-else-if="item.format === 2" v-model="formAccept[item.detailCode]" value-format="yyyy-MM-dd"
                            type="date" :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-date-picker>
            <el-time-picker v-else-if="item.format === 3" v-model="formAccept[item.detailCode]" value-format="HH:mm:ss"
                            :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-time-picker>
            <el-select v-else-if="item.format === 4" v-model="formAccept[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in item.values"
                :key="item2.batchTacticDetailDetailValue"
                :label="item2.batchTacticDetailDetailValue"
                :value="item2.batchTacticDetailDetailValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose4">取 消</el-button>
<!--        <el-button type="primary" @click="commitBatchTactic('form')">确 定</el-button>-->
        <el-button type="primary" @click="commitAccept()">验 收</el-button>
      </div>
    </el-dialog>
    <el-dialog width="1200px" title="选择货位" :visible.sync="innerVisibleCell" style="margin-top: -10vh;">

      <div class="publicSearch body">
        <div class="searchBar" :style="{height: isMoreSearch3?'auto':''}">
          <el-button-group>
            <el-button @click="getCellData" size="mini" icon="el-icon-search">查询</el-button>
            <el-button @click="clearSearchSelectCell" size="mini" icon="el-icon-refresh">重置</el-button>
          </el-button-group>
          <div>
            <span>关键字搜索</span>
            <el-input v-model="searchSelectCell.keyWords" size="mini" placeholder="名称" clearable></el-input>
          </div>
          <div>
            <span>货区</span>
            <el-select v-model="searchSelectCell.areaId" size="mini" filterable placeholder="请选择货区"
                       style="width: 70%;">
              <el-option
                v-for="item in areas"
                :key="item.areaId"
                :label="item.areaName"
                :value="item.areaId">
              </el-option>
            </el-select>
          </div>
        </div>
        <span class="moreSearchBtn"
              :class="isMoreSearch3?'el-icon-caret-top':'el-icon-caret-bottom'"
              @click="isMoreSearch3 = !isMoreSearch3"></span>
      </div>

      <div class="tableBox">
        <el-table
          highlight-current-row
          :data="cells"
          style="width: 100%">
          <el-table-column
            fixed="left"
            label="操作"
            width="110">
            <template slot-scope="scope">
              <el-button @click="selectCell(scope.row.cellCode)" icon="iconfont icon-jia1" type="primary" circle plain
                         size="small"></el-button>
            </template>
          </el-table-column>
         
          <el-table-column
            fixed="left" 
            prop="cellName"
            label="名称"
            width="180">
          </el-table-column>
          <el-table-column  
            prop="cellCode"
            label="编码"
            width="180">
          </el-table-column>
          <el-table-column 
            prop="shelfName"
            label="所属货位组"
            width="180">
          </el-table-column>
          <el-table-column 
            prop="cellUseType"
            label="货位使用类型"
            width="180">
            <template slot-scope="scope">
              <span v-for="(item,index) in cellUseTypes" :key="index" v-if="item.value === scope.row.cellUseType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column 
            prop="cellAbcLevel"
            label="周转需求"
            width="180">
            <template slot-scope="scope">
              <span v-for="(item,index) in cellAbcLevels" :key="index" v-if="item.value === scope.row.cellAbcLevel">{{item.label}}</span>
            </template>
          </el-table-column>
          
          
        </el-table>
        <el-pagination
          style="text-align: right;"
          @current-change="handleCurrentChange3"
          :current-page.sync="searchSelectCell.pageNum"
          :page-size="searchSelectCell.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="searchSelectCell.total">
        </el-pagination>
      </div>

    </el-dialog>

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearchMaster?'auto':''}">

        <el-tabs v-model="activeName" @tab-click="handleClick" style="width: 100%;">
          <el-tab-pane :label="isFile?'入库单归档单据信息':'入库单据信息'" name="first">
            <div class="newBillData clearFloat">
              <ul class="leftFloat">
                <li>
                  <span>单号:</span>
                  <span :title="asnMaster.billNo">&nbsp;{{asnMaster.billNo}}</span>
                </li>
                <!-- <li>
                  <span>单据来源:</span>
                  <span v-if="asnMaster.billSource === 0">采购单</span>
                  <span v-else-if="asnMaster.billSource === 1">新建</span>
                </li> -->
                <!-- <li>
                  <span>入库单类型:</span>
                  <span v-for="(item,index) in asnTypes" :key="index"
                        v-if="asnMaster.asnType === item.dataDictDetailId">{{item.dictValue}}</span>
                </li> -->
                <li v-if="asnMaster.billSource === 0">
                  <span>采购单号:</span>
                  <span :title="asnMaster.requestBuyBillNo" class="textBtn"
                        @click="$router.push({name: 'buyBillInDetail',params: {billNo: asnMaster.requestBuyBillNo}})">{{asnMaster.requestBuyBillNo}}</span>
                </li>
                <!-- <li v-if="asnMaster.insertType === 0">
                  <span>审核状态:</span>&nbsp;
                  <span v-if="asnMaster.state === -2">草稿</span>
                  <span v-else-if="asnMaster.state === 2">待审核</span>
                  <span v-else-if="asnMaster.state === 1">审核通过</span>
                  <span v-else-if="asnMaster.state === 0">审核中</span>
                  <span v-else-if="asnMaster.state === -1">审核退回</span>
                  <span v-else-if="asnMaster.state === 6">已生成QC单</span>
                </li> -->
                <!-- <li>
                  <span>保存类型:</span>&nbsp;
                  <span v-if="asnMaster.insertType === 0">单据</span>
                  <span v-else-if="asnMaster.insertType === -2">草稿</span>
                </li> -->
                <!-- <li>
                  <span>发货地址:</span>
                  <span :title="asnMaster.loadingPlace">&nbsp;{{asnMaster.loadingPlace}}</span>
                </li> -->
              </ul>
              <ul class="leftFloat">
                <li>
                  <span>仓库:</span>
                  <span :title="asnMaster.wareName" class="textBtn"
                        @click="$parent.showPublicModal(asnMaster.wareId, 'ware')">{{asnMaster.wareName}}</span>
                </li>
                <li>
                  <span>货主:</span>
                  <span :title="asnMaster.organizationName" class="textBtn"
                        @click="$parent.showPublicModal(asnMaster.organizationId, 'organization')">{{asnMaster.organizationName}}</span>
                </li>
                <li>
                  <span>供应商:</span>
                  <span :title="asnMaster.supplierName" class="textBtn"
                        @click="$parent.showPublicModal(asnMaster.supplierId, 'supplier')">{{asnMaster.supplierName}}</span>
                </li>
                <li>
                  <span>创建人:</span>
                  <span :title="asnMaster.createUserName" class="textBtn"
                        @click="$parent.showPublicModal(asnMaster.createUserId, 'account')">{{asnMaster.createUserName}}</span>
                </li>
                <li>
                  <span>创建时间:</span>
                  <span :title="asnMaster.createTime1">&nbsp;{{asnMaster.createTime1}}</span>
                </li>
                <!-- <li>
                  <span>预期到货时间:</span>
                  <span :title="asnMaster.expectTimeStr">&nbsp;{{asnMaster.expectTimeStr}}</span>
                </li> -->
              </ul>
              <!-- <ul class="leftFloat">
                <li>
                  <span>备注:</span>
                  <span :title="asnMaster.memo">&nbsp;{{asnMaster.memo}}</span>
                </li>
                <li v-for="(item,index) in asnUdfHs" :key="index">
                  <span>{{item.value}}:</span>
                  <span :title="asnMaster[item.paramCode]">&nbsp;{{asnMaster[item.paramCode]}}</span>
                </li>
                <li>
                  <span>交货地址:</span>
                  <span :title="asnMaster.deliveryPlace">&nbsp;{{asnMaster.deliveryPlace}}</span>
                </li>
                <li>
                  <span>运输描述:</span>
                  <span :title="asnMaster.trafficDescribe">&nbsp;{{asnMaster.trafficDescribe}}</span>
                </li>
              </ul> -->
            </div>
          </el-tab-pane>
          <el-tab-pane v-if="asnMaster.insertType === 0" label="审核进程" name="reviewStep">
            <ReviewStepBox :masterData="asnMaster.billNo" :activeName="activeName"></ReviewStepBox>
          </el-tab-pane>
          <el-tab-pane label="订单进程" name="billRecord">
            <BillRecordBox :masterData="asnMaster.billNo" :activeName="activeName"></BillRecordBox>
          </el-tab-pane>
        </el-tabs>

        <!--<div class="billMasterBox clearFloat">
          <div class="billData leftFloat clearFloat">
            <div class="title">
              <span>入库单<span v-if="isFile">归档</span>单据信息</span>
            </div>
            <ul class="leftFloat">
              <li>
                <span>单号:</span>
                <span>&nbsp;{{asnMaster.billNo}}</span>
              </li>
              <li>
                <span>单据来源:</span>
                <span v-if="asnMaster.billSource === 0">采购单</span>
                <span v-else-if="asnMaster.billSource === 1">新建</span>
              </li>
              <li>
                <span>入库单类型:</span>
                <span v-for="item in asnTypes" v-if="asnMaster.asnType === item.dataDictDetailId">{{item.dictValue}}</span>
              </li>
              <li v-if="asnMaster.billSource === 0">
                <span>采购单号:</span>
                <span :title="asnMaster.requestBuyBillNo">&nbsp;
                  <span class="textBtn" @click="$router.push({name: 'buyBillInDetail',params: {billNo: asnMaster.requestBuyBillNo}})">{{asnMaster.requestBuyBillNo}}</span>
                </span>
              </li>
              <li v-if="asnMaster.insertType === 0">
                <span>审核状态:</span>&nbsp;
                <span v-if="asnMaster.state === -2">草稿</span>
                <span v-else-if="asnMaster.state === 2">待审核</span>
                <span v-else-if="asnMaster.state === 1">审核通过</span>
                <span v-else-if="asnMaster.state === 0">审核中</span>
                <span v-else-if="asnMaster.state === -1">审核退回</span>
              </li>
              <li>
                <span>保存类型:</span>&nbsp;
                <span v-if="asnMaster.insertType === 0">单据</span>
                <span v-else-if="asnMaster.insertType === -2">草稿</span>
              </li>
              <li>
                <span>备注:</span>
                <span>&nbsp;{{asnMaster.memo}}</span>
              </li>
              <li v-for="item in asnUdfHs">
                <span>{{item.value}}:</span>
                <span :title="asnMaster[item.paramCode]">&nbsp;{{asnMaster[item.paramCode]}}</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span>仓库:</span>
                <span>&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(asnMaster.wareId, 'ware')">{{asnMaster.wareName}}</span>
                </span>
              </li>
              <li>
                <span>货主:</span>
                <span :title="asnMaster.organizationName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(asnMaster.organizationId, 'organization')">{{asnMaster.organizationName}}</span>
                </span>
              </li>
              <li>
                <span>供应商:</span>
                <span :title="asnMaster.supplierName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(asnMaster.supplierId, 'supplier')">{{asnMaster.supplierName}}</span>
                </span>
              </li>
              <li>
                <span>创建人:</span>
                <span :title="asnMaster.createUserName">&nbsp;
                  <span class="textBtn" @click="$parent.showPublicModal(asnMaster.userId, 'account')">{{asnMaster.createUserName}}</span>
                </span>
              </li>
              <li>
                <span>创建时间:</span>
                <span>&nbsp;{{asnMaster.createTime}}</span>
              </li>
              <li>
                <span>预期到货时间:</span>
                <span>&nbsp;{{asnMaster.expectTime}}</span>
              </li>
              <li>
                <span>发货地址:</span>
                <span>&nbsp;{{asnMaster.loadingPlace}}</span>
              </li>
              <li>
                <span>交货地址:</span>
                <span>&nbsp;{{asnMaster.deliveryPlace}}</span>
              </li>
              <li>
                <span>运输描述:</span>
                <span>&nbsp;{{asnMaster.trafficDescribe}}</span>
              </li>
            </ul>
          </div>
          <ReviewStepBox :masterData="asnMaster.billNo"></ReviewStepBox>
        </div>-->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearchMaster?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearchMaster = !isMoreSearchMaster"></span>
    </div>

    <el-tabs type="card" v-model="activeName2" @tab-click="handleClick">
      <el-tab-pane label="单据详情" name="first">
        <div class="tableBox">
          <el-table
            highlight-current-row
            @selection-change="handleSelectionChange"
            :data="asnDetails"
            style="width: 100%">
            <el-table-column
              type="selection"
              width="30">
            </el-table-column>
            <el-table-column
              fixed="left"
              type="index"
              width="30">
            </el-table-column>
             <!-- <el-table-column
              fixed="left"
              show-overflow-tooltip
              prop="detailNo"
              label="单号"
              width="120">
            </el-table-column> -->
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
              show-overflow-tooltip
              label="状态"
              min-width="60">
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
            <!-- <el-table-column
              show-overflow-tooltip
              prop="expectQuantity"
              label="预约数"
              width="180">
              <template slot-scope="scope">
                 <span>{{scope.row.packExpectQuantity}}（{{scope.row.packDescribe}}）||{{scope.row.expectQuantity}}（个）</span>
              </template>
            </el-table-column> -->
            <el-table-column
              show-overflow-tooltip
              prop="acceptQuantity"
              label="验收数"
              min-width="60">
              <template slot-scope="scope">
                 <span style="font-size:15px">
                   {{scope.row.acceptQuantity/scope.row.acceptTransRatio}}
                  
                    
                  </span>
                  <span v-if="scope.row.acceptPackDescribe!=null && scope.row.acceptPackDescribe!=''" style="font-size:15px">
                   
                   {{scope.row.acceptPackDescribe}}
                    
                  </span>
                  <br/>
                   <span style="font-size:15px">
                   {{scope.row.acceptQuantity}}
                   主单位 
                  </span>
              </template>
            </el-table-column>

             <el-table-column
              show-overflow-tooltip
              prop="acceptQuantity"
              label="采购数"
              min-width="60">
              <template slot-scope="scope">
                 <span style="font-size:15px">
                   
                   {{scope.row.expectQuantity/scope.row.transRatio}}
                   {{scope.row.packDescribe}}
                   
                  </span>
                   <br/>
                   <span style="font-size:15px">
                   {{scope.row.expectQuantity}}
                   主单位
                  </span>
              </template>
            </el-table-column>
           <!-- <el-table-column
              show-overflow-tooltip
              prop="packDescribe"
              label="单位"
              width="80">
            </el-table-column> -->

            <el-table-column
              show-overflow-tooltip
              prop="upc"
              label="UPC"
              min-width="100">
            </el-table-column>
            <!-- <el-table-column
              show-overflow-tooltip
              v-for="(item,index) in asnUdfDs"
              :key="index"
              :prop="item.paramCode"
              :label="item.value"
              width="180">
            </el-table-column> -->
            <el-table-column
              show-overflow-tooltip
              prop="acceptTime"
              label="验收时间"
              min-width="110">
            </el-table-column>
            <el-table-column
              v-if="asnMaster.insertType === 0 && asnMaster.asnState !==3 || asnMaster.insertType === 0 && asnMaster.asnState !==5 || asnMaster.insertType === 0 && asnMaster.asnState !==4"
              fixed="right"
              label="操作"
              min-width="60">
              <template slot-scope="scope">
                <el-button @click="showAcceptModal(scope.row)" icon="iconfont icon-shouhuo" type="success" circle plain
                           size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="快递信息" name="second">
        <div class="tableBox">
          <el-table
            highlight-current-row
            @selection-change="handleSelectionChange"
            :data="asnShipDetails"
            style="width: 99%">
           
            <el-table-column
             
              show-overflow-tooltip
              prop="shipName"
              label="快递公司"
             >
            </el-table-column>
            <el-table-column
             
              show-overflow-tooltip
              prop="shipCode"
              label="快递公司编码"
             >
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="shipBillCode"
              label="快递单号"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="createTime"
              label="创建时间"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="acceptTime"
              label="验收时间"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="length"
              label="长"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="width"
              label="宽"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="height"
              label="高"
             > 
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="weight"
              label="重量"
             > 
            </el-table-column>
            
            
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="验收记录" name="third">
        <div class="tableBox" >
          <el-table
            highlight-current-row
            @selection-change="handleSelectionChange"
            :data="acceptRecords"
            style="width: 100%">
            
           
              <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/SKU名称"
          min-width="350">
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
              show-overflow-tooltip
              prop="quantity"
              label="验收数量"
              min-width="100">
              <template slot-scope="scope">
                 <span>{{scope.row.quantity}} </span>
              </template>
            </el-table-column>
           <!-- <el-table-column
              show-overflow-tooltip
              prop="packDescribe"
              label="单位"
              width="80">
            </el-table-column> -->
            
            <el-table-column
              show-overflow-tooltip
              prop="acceptTime"
              label="验收时间"
              min-width="110">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="acceptUserName"
              label="验收人"
              min-width="110">
            </el-table-column>
             <el-table-column
              show-overflow-tooltip
              prop="boxCode"
              label="跟踪号"
              min-width="110">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="cellCode"
              label="货位号"
              min-width="110">
            </el-table-column>
            
             
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="异常记录" name="2">
          <div class="tableBox none" >
      <el-table
      border
      
        highlight-current-row
        :data="exDetails"
         @row-click="clickRow"
        
        style="width: 100%">
    
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/SKU名称"
          min-width="300">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8"> 
                <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                   <el-image 
                      style="width: 80px; height: 80px"
                      :src="scope.row.imgUrl" 
                      :preview-src-list="[scope.row.imgUrl]">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="16">
                <div  style="display: inline-block;margin-left:15px;  ">
                    <span class="textBtn" style="color:red"
                        >{{scope.row.itemCode}}</span>
                        <br>
                    <span  
                   >{{scope.row.itemName}}</span>
                    <br>
                    <span>{{scope.row.spec}}</span>
                    <br>
                    <span>{{scope.row.model}}</span>
              </div>
              </el-col>
              
            </el-row>
            
            
          </template>
        </el-table-column>

        <el-table-column
        
          show-overflow-tooltip
          prop="billNo"
          label="异常信息"
          min-width="230">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="12"> 
                <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                   <el-image 
                      style="width: 80px; height: 80px"
                      :src="scope.row.imgUrls[0]" 
                      :preview-src-list="scope.row.imgUrls">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="12">
                <span>快递单号: </span>
                <span class="textBtn"  >{{scope.row.shipBillNo}}</span>
                <br/>
                <!-- <span>数量: </span>
                <span class="textBtn"  >{{scope.row.quantity}}</span>
                <br> -->
                <span>类型: </span>
                 <span   class="textBtn"   v-if="scope.row.type==1" >包裹异常</span>
                <span   class="textBtn"   v-if="scope.row.type==2" >商品异常</span> 
          
                <br>
                <span>原因: </span>
                <span   class="textBtn"   v-if="scope.row.reason==1" >多发</span>
                <span   class="textBtn"   v-if="scope.row.reason==2" >少发</span>
                <span   class="textBtn"   v-if="scope.row.reason==3" >错发</span>
                <span   class="textBtn"   v-if="scope.row.reason==4" >破损</span>
                <span   class="textBtn"   v-if="scope.row.reason==5" >其他</span>
 
                <br>
                <span>描述: </span>
                <span class="textBtn"  >{{scope.row.memo}}</span>
                <br>
              </el-col>
            </el-row>
            
            
          </template>
        </el-table-column> 

         <el-table-column
        
          show-overflow-tooltip
          prop="billNo"
          label="处理方案"
          min-width="180">
          <template slot-scope="scope">
            
             <el-tag type="warning" v-if="scope.row.fangan==1">全部退件</el-tag>
             <el-tag type="danger" v-if="scope.row.fangan==2">全部弃件</el-tag>
             <el-tag  type="success" v-if="scope.row.fangan==3">全部入库</el-tag>
             <el-tag type="warning" v-if="scope.row.fangan==4">部分入库部分退件</el-tag>
             <el-tag type="danger" v-if="scope.row.fangan==5">部分入库部分弃件</el-tag> 
 
           
            
            
          </template>
        </el-table-column> 
       
       
        
        <el-table-column
          show-overflow-tooltip
          :render-header="renderHeader"
          prop="toQuantity"
          label="验收信息/异常信息"
          min-width="200">
          <template slot-scope="scope">
               <el-tag  v-if="scope.row.state==0"   type="success">初始化  </el-tag>
               <el-tag  v-if="scope.row.state==1"   type="success">提交ERP  </el-tag>
               <el-tag  v-if="scope.row.state==2"   type="danger">待处理  </el-tag>
               <el-tag  v-if="scope.row.state==3"   type="success">已经处理  </el-tag>
               
              
         
          
            
            
          </template>
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
    </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReviewStepBox from '@/components/ReviewStepBox'
  import BillRecordBox from '@/components/BillRecordBox'
  import DateUtils from "@/utils/DateUtils";

  export default {
    name: 'AsnReturnBillDetail',
    components: {
      ReviewStepBox,
      BillRecordBox,
    },
    data() {
      return {
         loading : false,
        isMoreSearch: false,
        isMoreSearchMaster: true,
        isMoreSearch2: false,
        isMoreSearch3: false,
        activeName: 'first',
        activeName2: 'first',

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
        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseType: 0,
        },

        asnMaster: {},
        asnDetails: [],
        asnShipDetails:[],
        acceptRecords:[],
        exDetails:[],
        form: {
          asnMaster: {},
          asnDetails: [],
        },
        asnUdfHs: [],
        asnUdfDs: [],
        asnTypes: [],
        ids: [],
        items: [],
        suppliers: [],
        organizations: [],
        platforms: [],
        carriers: [],
        multipleSelection: [],
        multipleSelection2: [],
        qcUdfHs: [],
        qcUdfDs: [],
        qcTypes: [],
        batchTactics: [],
        cells: [],
        areas: [],
        packDetails: [],
        acceptExcess: null,

        innerVisibleItem: false,
        dialogNewVisible: false,
        dialogQcVisible: false,
        dialogReviewVisible: false,
        dialogAcceptVisible: false,
        innerVisibleCell: false,

        rules: {
          supplierId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          buyType: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
        },

        formLabelWidth: '120px',

        formQc: {
          billSource: 0,
          insertType: -2,
          qcMaster: {},
          qcDetails: [],
        },
        rulesQc: {
          AsnReturnBillNo: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
          supplierId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          qcType: [
            {required: true, message: '请选择质检类型', trigger: 'blur'}
          ],
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

        formAccept: {
          AsnReturnBillNo:'',
          asnDetailNo:'',
          wareId: null,
          wareName: '',
          organizationId: null,
          organizationName: '',
          toPackDetailId: null,
          toTransRatio: null,
          toPackDescribe: '',
          itemCode: '',
          itemName: '',
          packCode: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: 0,
          billType: 2,
          expectQuantity: 0,
          acceptQuantity: 0,
          'productDate': '',
          'exDate': '',
          'inDate': '',
          'supplierCode': '',
          'supplierBatch': '',
          'qcState': '',
          'costPrice': null,
          'salePrice': '',
          'detailNo': '',
          'memo1': '',
          'memo2': '',
          'memo3': '',
          'batchAttribute1': '',
          'batchAttribute2': ''
        },
        rulesAccept: {
          wareId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          'productDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'exDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'inDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierCode': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierBatch': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'qcState': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'costPrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'salePrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'detailNo': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo3': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
        },

      }
    },
    computed: {
      ...mapState([
        "abcLevels",
        "qcStates",
        "cellUseTypes",
        "cellAbcLevels",
        "cellPickTypes",
      ]),
      supplierId() {
        return this.form.asnMaster.supplierId
      },
      isFile() {
        if (this.$route.params.fileUserId) {
          return true
        } else {
          return false
        }
      }
    },
    watch: {
      supplierId(val) {
        for (let i = 0; i < this.suppliers.length; i++) {
          if (this.suppliers[i].supplierId === val) {
            this.form.asnMaster.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
    },
    methods: {
      exportAns(){
          let url = '/asn/masters/daochu';
         IOT.showOverlay("正在导出，请稍等...");
        IOT.getServerData(url, 'get', {asnMasterId:this.asnMaster.asnMasterId}, (ret) => {
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
       getExData() {
         let that = this;
         
         
        IOT.getServerData('/accept/exs/list', 'get', {billNo:this.$route.params.billNo}, (ret) => {
         
          if (ret.code === 200) {
            
        
         
            let list = ret.rows; 
           
           
            for (let i = 0; i < list.length; i++) {
             
               
 

               
              list[i].toQuantity = list[i].orderQuantity-list[i].quantity-list[i].inQuantity ;
              list[i].printQuantity =list[i].orderQuantity-list[i]-list[i].inQuantity ;

              
              list[i].toPackDescribe = list[i].packDescribe;
              list[i].toTransRatio =  list[i].transRatio;
              list[i].toPackDetailId =list[i].packDetailId;
              
              list[i].billType = 2;
              list[i].toBoxCode = '';
              list[i].toCellCode = ''; 
              list[i].returnQuantity = 0; 
             
              list[i].batchInfo={};
              list[i].batchInfo.batchTacticDetails=[];
              list[i].weight = null;
              list[i].asnDetailNo = list[i].detailNo;
              list[i].AsnReturnBillNo = list[i].billNo;
              list[i].fileList = [];
              list[i].imgUrls = []
             
            }
            
            this.exDetails = list;
            

             
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      handleClick(tab, event) {
        console.log(tab, event);
      },
      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getItemData()
      },
      handleCurrentChange3(val) {
        this.searchSelectCell.pageNum = val;
        this.getCellData()
      },
      clearSearchSelectItem() {
        this.searchDataItem.keyWords = '';
        this.searchDataItem.wareId = null;
        this.searchDataItem.organizationId = null;
      },
      clearSearchSelectCell() {
        this.searchSelectCell.keyWords = '';
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getAsnMasterData() {
        this.loading = true;
        let url = '';
        if (this.isFile) {
          url = '/asn/master/files/list';
        } else {
          url = '/asn/masters/list';
        }
        IOT.getServerData(url, 'get', {billNo: this.searchData.billNo}, (ret) => {
        this.loading = false;
          if (ret.code === 200) {
            let list = ret.rows[0];
            if (PF.isNull(list.createTime)) {
              list.createTime1 = list.createTime.substr(0, 19);
            }
            if (PF.isNull(list.expectTime)) {
              list.expectTimeStr = DateUtils.formatDay(list.expectTime);
            }
            this.asnMaster = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAsnDetailData() {
        
        this.loading = true;
        let url = '';
        if (this.isFile) {
          url = '/asn/detail/files/list';
        } else {
          url = '/asn/details/list';
        }
        IOT.getServerData(url, 'get', this.searchData, (ret) => {
          
        this.loading = false;
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].packExpectQuantity = list[i].expectQuantity / list[i].transRatio;
              list[i].packAcceptQuantity = list[i].acceptQuantity / list[i].transRatio;
               
            }
            this.asnDetails = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
       getAsnShipDetailData() {
        let url = '/asn/ship/details/findByBillNo';
        
        IOT.getServerData(url, 'get', {billNo:this.searchData.billNo}, (ret) => {
          if (ret.code === 200) {
            this.asnShipDetails = ret.rows;
           
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAsnRecordData() {
         
        var that = this;
        that.acceptRecords =[];
         
        IOT.getServerData('/accept/records/list', 'get', {AsnReturnBillNo: this.searchData.billNo}, (ret) => {
       
          if (ret.code === 200) {
            that.acceptRecords = ret.rows;
            that.searchData.total = ret.total;
            that.searchData.pageNum = ret.pageNumber
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
        IOT.getServerData('/platforms/list', 'get', {wareId: this.asnMaster.wareId}, (ret) => {
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
      getCellData() {
        IOT.getServerData('/cell/infos/list', 'get', this.searchSelectCell, (ret) => {
          if (ret.code === 200) {
            
            this.cells = ret.rows;
            this.searchSelectCell.total = ret.total;
            this.searchSelectCell.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAreaData() {
        IOT.getServerData('/area/infos/list', 'get', {wareId: this.searchSelectCell.wareId}, (ret) => {
          if (ret.code === 200) {
            this.areas = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPackDetailData(packCode) {
        IOT.getServerData('/pack/details/findByPackCode', 'get', {packCode: packCode}, (ret) => {
          if (ret.code === 200) {
            this.packDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleClose(done) {
        this.dialogNewVisible = false;
      },
      handleClose2(done) {
        this.formQc.qcDetails = [];
        this.dialogQcVisible = false;
      },
      handleClose3(done) {
        this.dialogReviewVisible = false;
      },
      handleClose4(done) {
        this.resetForm('formAccept');
        this.dialogAcceptVisible = false;
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
        IOT.getServerData('/asn/masters/delete', 'get', {id: id}, (ret) => {
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
          asnMaster: PF.JSON(this.asnMaster),
          asnDetails: PF.JSON(this.asnDetails)
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
        for (let i = 0; i < this.form.asnDetails.length; i++) {
          this.ids.push(this.form.asnDetails[i].itemCode)
        }
        this.searchDataItem.wareId = PF.getLocal('wareId', 'number');
        this.searchDataItem.organizationId = this.form.asnMaster.organizationId;
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
                packDescribe1: list[i].packDescribe,
                transRatio: list[i].transRatio
              })
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, false, false);
        this.form.asnDetails.push(row);
        this.form.asnDetails.expectQuantity = 0;
        this.innerVisibleItem = false;
      },
      deleteDetail(index) {
        this.form.asnDetails.splice(index, 1)
      },
      updateForm() {
        let that = this;
        let form = this.form;
        if (form.asnDetails.length <= 0) {
          IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
        delete form.asnMaster.createTime;
        for (let i = 0; i < form.asnDetails.length; i++) {
          if (form.asnDetails[i].expectQuantity === 0 || !PF.isNull(form.asnDetails[i].expectQuantity)) {
            IOT.tips('数量不得为零,请重新输入！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          // 主单位数量
          form.asnDetails[i].expectQuantity = form.asnDetails[i].expectQuantity * form.asnDetails[i].transRatio;
        }
        IOT.getServerData('/asn/masters/update', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getAsnMasterData();
              that.getAsnDetailData();
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
          billSource: this.asnMaster.billSource,
          insertType: 0,
          asnMaster: PF.JSON(this.asnMaster),
          asnDetails: PF.JSON(this.asnDetails)
        };
        if (form.asnDetails.length <= 0) {
          IOT.tips('未选择商品,请重新选择！', 'error', 1000, () => {
            IOT.hideOverlay();
          });
          return false
        }
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
        //暂时未使用
        IOT.getServerData('/asn/masters/insert', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.$router.push({name: 'AsnReturnBill'});
            IOT.tips('保存成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCreateList(data, callback) {
        IOT.getServerData('/qc/details/getCreateList', 'get', {
          AsnReturnBillNo: data.billNo,
          asnType: data.asnType
        }, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            callback(ret.rows)
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, false, false);
      },
      showQcModal() {
        let formQc = {
          billSource: 0,
          insertType: -2,
          qcMaster: PF.JSON(this.asnMaster),
          qcDetails: [],
        };
        this.getCreateList(this.asnMaster, (data) => {
          let list = data;
          let itemCodes = [];
          for(let i=0;i<list.length;i++){
            itemCodes.push(list[i].itemCode);
            // list[i].expectQuantity = list[i].expectQuantity / list[i].transRatio;
            list[i].qcQuantity = list[i].qcQuantity / list[i].transRatio;
          }
          let asnDetails = PF.JSON(this.asnDetails);
          if(list.length > asnDetails.length){
            for(let j=0;j<asnDetails.length;j++){
              if(itemCodes.indexOf(asnDetails[j].itemCode)){
                asnDetails[j].expectQuantity = null;
                asnDetails[j].qcQuantity = null;
                formQc.qcDetails.push(asnDetails[j]);
              }
            }
          }
          formQc.qcDetails = list;
        });
        formQc.qcMaster.insertType = -2;
        formQc.qcMaster.qcType = 1;
        formQc.qcMaster.AsnReturnBillNo = formQc.qcMaster.billNo;
        delete formQc.qcMaster.billNo;
        formQc.qcMaster.wareId = PF.getLocal('wareId', 'number');
        formQc.qcMaster.wareName = PF.getLocal('wareName');

        for (let i = 0; i < formQc.qcDetails.length; i++) {
          formQc.qcDetails[i].oldQ = formQc.qcDetails[i].expectQuantity;
          formQc.qcDetails[i].oldP = formQc.qcDetails[i].packDetailId;
          // formQc.qcDetails[i].asnDetailNo = formQc.qcDetails[i].detailNo;
          delete formQc.qcDetails[i].detailNo;
          delete formQc.qcDetails[i].billNo;
        }

        this.formQc = formQc;
        PF.getOrganizations((rows) => {
          this.organizations = rows;
        });
        PF.getSuppliers((rows) => {
          this.suppliers = rows;
        });
        PF.getSysParamUdfData('qcUdfHs', [this.formQc.qcMaster], (data, udfs) => {
          this.formQc.qcMaster = data[0];
          this.qcUdfHs = udfs;
        });
        PF.getSysParamUdfData('qcUdfDs', this.formQc.qcDetails, (data, udfs) => {
          this.formQc.qcDetails = data;
          this.qcUdfDs = udfs;
        });
        this.dialogQcVisible = true;
      },
      changePackDetailId(index, type) {
        if (type === 'qc') {
          let detail = PF.JSON(this.formQc.qcDetails[index]);
          for (let i = 0; i < detail.packDetailsList.length; i++) {
            if (detail.packDetailsList[i].packDetailId === detail.packDetailId) {
              this.formQc.qcDetails[index].packDescribe = detail.packDetailsList[i].packDescribe;
              this.formQc.qcDetails[index].transRatio = detail.packDetailsList[i].transRatio;
            }
          }
          // this.formQc.qcDetails[index].expectQuantity = PF.changePToChangeQ(this.formQc.qcDetails[index]);
        } else if (type === 'update')  {
          let detail = PF.JSON(this.form.asnDetails[index]);
          for (let i = 0; i < detail.packDetails.length; i++) {
            if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
              this.form.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe1;
              this.form.asnDetails[index].transRatio = detail.packDetails[i].transRatio1;
            }
          }
          // this.form.asnDetails[index].expectQuantity = PF.changePToChangeQ(this.form.asnDetails[index]);
        } else if (type === 'accept')  {
          for (let i = 0; i < this.packDetails.length; i++) {
            if (this.packDetails[i].packDetailId === this.formAccept.toPackDetailId) {
              this.formAccept.toPackDescribe = this.packDetails[i].packDescribe;
              this.formAccept.toTransRatio = this.packDetails[i].transRatio;
            }
          }
          // this.form.asnDetails[index].expectQuantity = PF.changePToChangeQ(this.form.asnDetails[index]);
        }
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitFormQc(formName, insertType) {
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let formQc = this.formQc;
            formQc.insertType = insertType;
            // formQc.qcDetailList = PF.JSON(formQc.qcDetails);
            // delete formQc.qcDetails;
            IOT.getServerData('/qc/masters/insert', 'post', formQc, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                this.$router.push({name: 'AsnReturnBill'});
                IOT.tips('保存成功！', 'success');
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.handleClose2();
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      commitReview(formName, reviewResult) {
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let formReview = PF.JSON(this.formReview);
            formReview.reviewResult = reviewResult;
            IOT.getServerData('/asn/masters/review', 'get', formReview, (ret) => {
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
      showAcceptModal(row) {
        
        // for(let key in this.formAccept){
        //   if(PF.isNull(row[key])){
        //     this.formAccept[key] = row[key];
        //   }
        // }
        this.formAccept = row;
        this.formAccept.AsnReturnBillNo = row.billNo;
        this.formAccept.asnDetailNo = row.detailNo;
        this.formAccept.billType = 2;
        this.formAccept.toQuantity = PF.subtraction( this.formAccept.expectQuantity, this.formAccept.acceptQuantity );
        PF.getSysParamUdfData('acceptExcess', [], (data, udfs) => {
          this.acceptExcess = 1 + (Number(udfs[0].value) / 100);
        });
         
        this.getPackDetailData(row.packCode);
        this.getBatchTacticData(row);
        this.dialogAcceptVisible = true;
      },
      getBatchTacticData(row) {

        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: row.organizationId,
          itemCode: row.itemCode
        }, (ret) => {

          if (ret.code === 200) {
            let list2 = PF.JSON(ret.rows[0]);
            let detailDetails = list2.batchTacticDetailDetails;
            let testDetailDetails = [];
            list2.batchTacticDetails[12].values = [];
            list2.batchTacticDetails[13].values = [];

            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list2.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list2.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list2.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let j = 0; j < batchTacticDetails.length; j++) {
              if (batchTacticDetails[j].state === 1) {
                this.formAccept[batchTacticDetails[j].detailCode] = '';
                testBatchTacticDetails.push(batchTacticDetails[j]);
                if (batchTacticDetails[j].isRequired === 1) {
                  this.rulesAccept[batchTacticDetails[j].detailCode][0].required = true;
                }
                if (batchTacticDetails[j].detailCode === 'supplierCode') {
                  this.formAccept[batchTacticDetails[j].detailCode] = row.supplierCode;
                }else if (batchTacticDetails[j].detailCode === 'detailNo') {
                  this.formAccept[batchTacticDetails[j].detailCode] = row.detailNo;
                }else if (batchTacticDetails[j].detailCode === 'inDate') {
                  this.formAccept[batchTacticDetails[j].detailCode] = PF.getNowFormatDate();
                }
              }
            }

            list2.batchTacticDetailDetails = testDetailDetails;
            list2.batchTacticDetails = testBatchTacticDetails;

            this.batchTactics = list2;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showSelectCellModal(){
        if (!PF.isNull(this.formAccept.wareId)) {
          IOT.tips('未选择仓库！', 'error');
          return false;
        }
        this.searchSelectCell.wareId = this.formAccept.wareId;
        this.getCellData();
        this.getAreaData();
        this.innerVisibleCell = true;
      },
      selectCell(cellCode) {
        this.formAccept.toCellCode = cellCode;
        this.innerVisibleCell = false;
      },
       commitAccept ( ) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        let form = PF.JSON(this.formAccept);
        if (form.toQuantity === 0) {
            IOT.tips('数量不得为零！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          if(form.toCellCode === ''){
            IOT.tips('货位不得为空！', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false
          }
          

          form.asnDetailNo = form.detailNo;
          // form[i].toPackDetailId = form[i].packDetailId;
          // form[i].toPackDescribe = form[i].packDescribe;
          form.AsnReturnBillNo = form.billNo;

          // 主单位数量
          form.toQuantity = form.toQuantity * form.toTransRatio;
        IOT.getServerData('/accept/records/accept', 'post', this.formAccept, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('验收成功！', 'success', 1000, () => {
              that.getAsnDetailData();
              that.getAsnRecordData();
              that.resetForm('formAccept');
              that.handleClose4();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getCaseNowList();
      PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
        this.asnUdfHs = udfs;
      });
      PF.getSysParamUdfData('asnUdfDs', [], (data, udfs) => {
        this.asnUdfDs = udfs;
      });
      this.getAsnMasterData();
      this.getAsnDetailData();
      this.getAsnRecordData();
      this.getAsnShipDetailData();
      this.getExData();
      PF.getDataDictUdfData('asnType', (rows) => {
        this.asnTypes = rows;
      });
      PF.getDataDictUdfData('qcType', (rows) => {
        this.qcTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .AsnReturnBillDetail {
  }
</style>
