<template>
  <div class="organization clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
      </el-button>
    </div>

    <el-dialog title="新增货主" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="编码" prop="organizationCode" :label-width="formLabelWidth">
            <el-input v-model="form.organizationCode" placeholder="请输入编码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="organizationName" :label-width="formLabelWidth">
            <el-input v-model="form.organizationName" placeholder="请输入名称"></el-input>
          </el-form-item>
          <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
            <el-input v-model="form.linkMan" placeholder="请输入联系人"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
            <el-input v-model.number="form.linkPhone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <!-- <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.batchTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option
                v-for="item in batchTactics"
                :key="item.batchTacticCode"
                :label="item.batchTacticName"
                :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上架策略" prop="upperTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.upperTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option
                v-for="item in upperTactics"
                :key="item.upperTacticCode"
                :label="item.upperTacticName"
                :value="item.upperTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="质检策略" prop="qcTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.qcTacticCode" filterable placeholder="请选择质检策略" style="width: 70%;">
              <el-option
                v-for="item in qcTactics"
                :key="item.qcTacticCode"
                :label="item.qcTacticName"
                :value="item.qcTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="合并策略" prop="qcTacticCode" :label-width="formLabelWidth">
          <el-select v-model="form.soMergeTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
            <el-option
                    v-for="item in soMergeTactics"
                    :key="item.soMergeTacticCode"
                    :label="item.soMergeTacticName"
                    :value="item.soMergeTacticCode">
            </el-option>
          </el-select>
        </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.pickTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
              <el-option
                      v-for="item in pickTactics"
                      :key="item.allocationRuleCode"
                      :label="item.description"
                      :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改货主" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <div class="bigFormBox2">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="编码" prop="organizationCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.organizationCode" placeholder="请输入编码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="organizationName" :label-width="formLabelWidth">
            <el-input v-model="rowData.organizationName" placeholder="请输入名称"></el-input>
          </el-form-item>
          <el-form-item label="联系人" prop="linkMan" :label-width="formLabelWidth">
            <el-input v-model="rowData.linkMan" placeholder="请输入联系人"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
            <el-input v-model.number="rowData.linkPhone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <!-- <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.batchTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option
                v-for="item in batchTactics"
                :key="item.batchTacticCode"
                :label="item.batchTacticName"
                :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上架策略" prop="upperTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.upperTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option
                v-for="item in upperTactics"
                :key="item.upperTacticCode"
                :label="item.upperTacticName"
                :value="item.upperTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="质检策略" prop="qcTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.qcTacticCode" filterable placeholder="请选择质检策略" style="width: 70%;">
              <el-option
                v-for="item in qcTactics"
                :key="item.qcTacticCode"
                :label="item.qcTacticName"
                :value="item.qcTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="合并策略" prop="qcTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.soMergeTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
              <el-option
                v-for="item in soMergeTactics"
                :key="item.soMergeTacticCode"
                :label="item.soMergeTacticName"
                :value="item.soMergeTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.pickTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
              <el-option
                      v-for="item in pickTactics"
                      :key="item.allocationRuleCode"
                      :label="item.description"
                      :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="rowData.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为货主 '+addressForm.organizationName+' 新增发货地址'" :visible.sync="dialogAddressVisible"
               :before-close="handleCloseAddress" width="800px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="addressForm" :rules="rulesAddress" ref="addressForm">
        <el-form-item label="发货仓库" prop="wareId" :label-width="formLabelWidth">
          <el-select v-model="addressForm.wareId" @change="newAddressChangeWare" filterable placeholder="请选择发货仓库"
                     style="width: 90%;">
            <el-option
              v-for="ware in wares"
              :key="ware.wareId"
              :label="ware.wareName"
              :value="ware.wareId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发货人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="addressForm.linkMan" placeholder="请输入发货人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="addressForm.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input
            :value="addressForm.province===''||addressForm.city===''||addressForm.area===''?'':addressForm.province+' / '+addressForm.city+' / '+addressForm.area"
            placeholder="请选择所在地区" style="width: 70%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi"
                     @click="isShowAddressSelectBox = !isShowAddressSelectBox;clearAddressData()">选择
          </el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入详细地址"
            v-model="addressForm.detailAddress"
            style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="设为此仓库默认地址" prop="state" label-width="180px">
          <el-switch
            v-model="addressForm.state"
            active-color="#13ce66"
            inactive-color="#dcdfe6"
            active-text="是"
            inactive-text="否"
            active-value="1"
            inactive-value="0">
          </el-switch>
        </el-form-item>
      </el-form>
      <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox" width="600px"
                 style="margin-top: -10vh;" :close-on-click-modal="false">
        <el-tabs v-model="activeAddressName" type="border-card">
          <el-tab-pane label="省份" name="S">
            <el-radio v-model="radioS" :label="index" border v-for="(item,index) in treeDataS" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
          <el-tab-pane label="城市" name="C">
            <el-radio v-model="radioC" :label="index" border v-for="(item,index) in treeDataC" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
          <el-tab-pane label="区/县" name="Q">
            <el-radio v-model="radioQ" :label="index" border v-for="(item,index) in treeDataQ" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('addressForm');dialogAddressVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitAddressForm('addressForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为货主 '+addressFormRowData.organizationName+' 修改发货地址'" :visible.sync="dialogUpdateAddressVisible"
               :before-close="handleCloseAddress2" width="800px" style="margin-top: -10vh;" :close-on-click-modal="false">
      <el-form :model="addressFormRowData" :rules="rulesAddress2" ref="addressFormRowData">
        <el-form-item label="发货仓库" prop="wareId" :label-width="formLabelWidth">
          <el-select v-model="addressFormRowData.wareId" filterable placeholder="请选择发货仓库" style="width: 90%;">
            <el-option
              v-for="ware in wares"
              :key="ware.wareId"
              :label="ware.wareName"
              :value="ware.wareId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发货人" prop="linkMan" :label-width="formLabelWidth">
          <el-input v-model="addressFormRowData.linkMan" placeholder="请输入发货人" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone" :label-width="formLabelWidth">
          <el-input v-model.number="addressFormRowData.linkPhone" placeholder="请输入联系电话" style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="area" :label-width="formLabelWidth">
          <el-input
            :value="addressFormRowData.province===''||addressFormRowData.city===''||addressFormRowData.area===''?'':addressFormRowData.province+' / '+addressFormRowData.city+' / '+addressFormRowData.area"
            placeholder="请选择所在地区" style="width: 70%;"></el-input>
          <el-button type="primary" plain icon="iconfont icon-dizhi"
                     @click="isShowAddressSelectBox2 = !isShowAddressSelectBox2;clearAddressData()">选择
          </el-button>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress" :label-width="formLabelWidth">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入详细地址"
            v-model="addressFormRowData.detailAddress"
            style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="设为此仓库默认地址" prop="state" label-width="180px">
          <el-switch
            v-model="addressFormRowData.state"
            active-color="#13ce66"
            inactive-color="#dcdfe6"
            active-text="是"
            inactive-text="否"
            active-value="1"
            inactive-value="0">
          </el-switch>
        </el-form-item>
      </el-form>
      <el-dialog title="地址" append-to-body :visible.sync="isShowAddressSelectBox2" width="600px"
                 style="margin-top: -10vh;" :close-on-click-modal="false">
        <el-tabs v-model="activeAddressName" type="border-card">
          <el-tab-pane label="省份" name="S">
            <el-radio v-model="radioS" :label="index" border v-for="(item,index) in treeDataS" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
          <el-tab-pane label="城市" name="C">
            <el-radio v-model="radioC" :label="index" border v-for="(item,index) in treeDataC" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
          <el-tab-pane label="区/县" name="Q">
            <el-radio v-model="radioQ" :label="index" border v-for="(item,index) in treeDataQ" :key="index" class="organizationRadios">
              {{item.name}}
            </el-radio>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('addressFormRowData');dialogUpdateAddressVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAddressForm('addressFormRowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="新增审核流" :visible.sync="dialogNewReviewVisible" width="900px" :close-on-click-modal="false">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="reviewForm.reviewMaster" :rules="rulesReview" ref="reviewForm.reviewMaster">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{reviewForm.reviewMaster.wareName}}</span>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <span>{{reviewForm.reviewMaster.organizationName}}</span>
          </el-form-item>
          <el-form-item label="名称" prop="reviewMasterName" :label-width="formLabelWidth">
            <el-input v-model="reviewForm.reviewMaster.reviewMasterName" placeholder="请输入名称"
                      style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="单据类型" prop="billTypeId" :label-width="formLabelWidth">
            <el-select v-model="reviewForm.reviewMaster.billTypeId" filterable placeholder="请选择单据类型"
                       style="width: 90%;">
              <el-option
                v-for="item in reviewBillTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <div class="my_table my_tab_5">
          <div style="width: 10%;">顺序</div>
          <div style="width: 25%;">节点名</div>
          <div style="width: 35%;">审核人</div>
          <div style="width: 15%;">节点类型</div>
          <div style="width: 15%;">操作</div>
        </div>
        <div class="my_table my_tab_5" v-for="(item,index) in reviewForm.reviewNodes" :key="index">
          <div style="width: 10%;">{{index+1}}</div>
          <div style="width: 25%;">
            <el-input
              placeholder="请输入节点名"
              v-model="item.reviewNodeName"
              clearable style="width: 90%;">
            </el-input>
          </div>
          <div style="width: 35%;">
            <el-select
              v-model="item.reviewUsers"
              multiple
              collapse-tags
              filterable
              placeholder="请选择" style="width: 90%;">
              <el-option
                v-for="item in accounts"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId">
              </el-option>
            </el-select>
          </div>
          <div style="width: 15%;">
            <el-switch
              v-model="item.type"
              active-text="或"
              inactive-text="与"
              active-value="1"
              inactive-value="0">
            </el-switch>
          </div>
          <div style="width: 15%;">
            <el-button @click="reviewNodeUpDown(index,'save','up')" icon="iconfont icon-up" type="text"
                       :disabled="index===0?true:false"></el-button>
            <el-button @click="reviewNodeUpDown(index,'save','down')" icon="iconfont icon-down" type="text"
                       :disabled="index===reviewForm.reviewNodes.length-1?true:false"></el-button>
            <el-button @click="reviewNodeAdd(index,'save')" icon="iconfont icon-jia" type="text"></el-button>
            <el-button @click="reviewNodeDelete(index,'save')" icon="iconfont icon-shanchu" type="text"
                       :disabled="reviewForm.reviewNodes.length<=1?true:false"></el-button>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('reviewForm.reviewMaster');clearReviewNodes();dialogNewReviewVisible = false">取 消
        </el-button>
        <el-button type="primary" @click="commitReviewForm('reviewForm.reviewMaster')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改审核流" :visible.sync="dialogUpdateReviewVisible" width="900px" :close-on-click-modal="false">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="reviewRowData.reviewMaster" :rules="rulesReview"
                 ref="reviewRowData.reviewMaster">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{reviewRowData.reviewMaster.wareName}}</span>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <span>{{reviewRowData.reviewMaster.organizationName}}</span>
          </el-form-item>
          <el-form-item label="名称" prop="reviewMasterName" :label-width="formLabelWidth">
            <el-input v-model="reviewRowData.reviewMaster.reviewMasterName" placeholder="请输入名称" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="单据类型" prop="billTypeId" :label-width="formLabelWidth">
            <el-select v-model="reviewRowData.reviewMaster.billTypeId" filterable placeholder="请选择单据类型"
                       style="width: 90%;">
              <el-option
                v-for="item in reviewBillTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <div class="my_table my_tab_5">
          <div style="width: 10%;">顺序</div>
          <div style="width: 25%;">节点名</div>
          <div style="width: 25%;">审核人</div>
          <div>节点类型</div>
          <div>操作</div>
        </div>
        <div class="my_table my_tab_5" v-for="(item,index) in reviewRowData.reviewNodes" :key="index">
          <div style="width: 10%;">{{index+1}}</div>
          <div style="width: 25%;">
            <el-input
              placeholder="请输入节点名"
              v-model="item.reviewNodeName"
              clearable style="width: 90%;">
            </el-input>
          </div>
          <div style="width: 25%;">
            <el-select
              v-model="item.userIds"
              multiple
              collapse-tags
              placeholder="请选择审核人" style="width: 90%;">
              <el-option
                v-for="item in accounts"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId">
              </el-option>
            </el-select>
          </div>
          <div>
            <el-switch
              v-model="item.type"
              active-text="或"
              inactive-text="与"
              active-value="1"
              inactive-value="0">
            </el-switch>
          </div>
          <div>
            <el-button @click="reviewNodeUpDown(index,'update','up')" icon="iconfont icon-up" type="text"
                       :disabled="index===0?true:false"></el-button>
            <el-button @click="reviewNodeUpDown(index,'update','down')" icon="iconfont icon-down" type="text"
                       :disabled="index===reviewRowData.reviewNodes.length-1?true:false"></el-button>
            <el-button @click="reviewNodeAdd(index,'update')" icon="iconfont icon-jia" type="text"></el-button>
            <el-button @click="reviewNodeDelete(index,'update')" icon="iconfont icon-shanchu" type="text"
                       :disabled="reviewRowData.reviewNodes.length<=1?true:false"></el-button>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="resetForm('reviewRowData.reviewMaster');clearReviewNodes();dialogUpdateReviewVisible = false">取 消
        </el-button>
        <el-button type="primary" @click="updateReviewForm('reviewRowData.reviewMaster')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
        highlight-current-row
        @current-change="getLookRowData"
        :row-class-name="tableRowClassName"
        :data="organizations"
        style="width: 100%">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="名称">
        </el-table-column>
        <!--<el-table-column
                sortable
                show-overflow-tooltip
                prop="organizationStatus"
                label="状态"
                width="80">
          <template slot-scope="scope">
            <el-tag v-show="scope.row.organizationStatus === 'enable'" type="success">启用中</el-tag>
            <el-tag v-show="scope.row.organizationStatus === 'disable'" type="danger">禁用中</el-tag>
          </template>
        </el-table-column>-->
        <el-table-column
          label="操作"
          width="150">
          <template slot-scope="scope">
            <el-button v-if="scope.row.organizationStatus === 'disable'" @click="enableAccount(scope.row.organizationId)" icon="iconfont icon-qiyong" type="success" circle plain size="small" title="点击启用"></el-button>
            <el-button v-if="scope.row.organizationStatus === 'enable'" @click="disableAccount(scope.row.organizationId)" icon="iconfont icon-user-jinyong" type="info" circle plain size="small" title="点击禁用"></el-button>
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.organizationId)" icon="iconfont icon-shanchu" type="danger" circle
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

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <el-tabs class="organizationRight tabs" type="border-card" tab-position="top" v-model="activeName">
      <el-tab-pane label="货主信息" name="first">
        <p class="MasterBox">
          <span><b>货主编码：</b>{{lookRowData.organizationCode}}</span>
          <span><b>联系人：</b>{{lookRowData.linkMan}}</span>
        </p>
        <p class="MasterBox">
          <span><b>货主名称：</b>{{lookRowData.organizationName}}</span>
          <span><b>联系电话：</b>{{lookRowData.linkPhone}}</span>
        </p>
         <p class="MasterBox">
          <span><b>批次策略：</b>{{lookRowData.batchTacticName}}</span>
          <span><b>上架策略：</b>{{lookRowData.upperTacticName}}</span>
        </p>
        <p class="MasterBox">
          <span><b>质检策略：</b>{{lookRowData.qcTacticName}}</span>
          <span><b>拣货策略：</b>{{lookRowData.description}}</span>
        </p> 
        <p class="MasterBox">
           <span><b>合并策略：</b>{{lookRowData.soMergeTacticName}}</span> 
          <span><b>备注：</b>{{lookRowData.memo}}</span>
        </p>
      </el-tab-pane>
        
      <el-tab-pane label="发货地址" name="fourth">
        <div class="headerBtns">
          <el-button @click="dialogAddressVisible = true;showAddressModal()" size="small" type="primary" round plain
                     icon="iconfont icon-xinzeng">新增地址
          </el-button>
        </div>
        <div class="tableBox">
          <el-table
            highlight-current-row
            v-loading="addressLoading"
            :data="shippingAddresss"
            style="width: 100%">
            <el-table-column
              fixed="left"
              label="默认"
              width="50">
              <template slot-scope="scope">
                <i v-if="scope.row.state == '1'" class="iconfont icon-star"
                   style="color: #f7ba2a;display: inline-block;width: 100%;text-align: center;"></i>
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
              prop="wareName"
              label="发货仓库"
              width="180">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="linkMan"
              label="发货人"
              width="300">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="linkPhone"
              label="联系方式"
              width="120">
            </el-table-column>
            <el-table-column
              label="所在地区"
              width="200">
              <template slot-scope="scope">
                <span>{{scope.row.province}} / {{scope.row.city}} / {{scope.row.area}}</span>
              </template>
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="detailAddress"
              label="详细地址"
              width="300">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="110">
              <template slot-scope="scope">
                <el-button @click="showUpdateAddressModal(scope.row)" icon="iconfont icon-bianji" type="primary"
                           circle plain size="small"></el-button>
                <el-button @click="isDeleteAddress(scope.row.shippingAddressId)" icon="iconfont icon-shanchu"
                           type="danger" circle plain size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="text-align: right;"
            @current-change="handleCurrentChange2"
            :current-page.sync="searchData2.pageNum"
            :page-size="searchData2.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="searchData2.total">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="审核流程" name="fifth" class="clearFloat">
        <div class="headerBtns">
          <el-button @click="showNewReviewModal" size="small" type="primary" round plain
                     icon="iconfont icon-xinzeng">新增审核流
          </el-button>
        </div>
        <div class="tableBox organizationLeftTab">
          <el-table
            highlight-current-row
            :data="reviews"
            v-loading="reviewLoading"
            @current-change="getLookReviewRowData"
            style="width: 100%">
            <el-table-column
              type="index"
              width="50">
            </el-table-column>
            <el-table-column
              sortable
              show-overflow-tooltip
              prop="reviewMasterName"
              label="名称">
              <template slot-scope="scope">
                  <span @click="getLookReviewRowData(scope.row)"
                        style="text-decoration: underline;color: #409EFF;cursor: pointer;">{{scope.row.reviewMasterName}}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="110">
              <template slot-scope="scope">
                <el-button @click="getWareData();showUpdateReviewModal(scope.row)" icon="iconfont icon-bianji"
                           type="primary" circle plain size="small"></el-button>
                <el-button @click="isDeleteReview(scope.row.reviewMasterId)" icon="iconfont icon-shanchu"
                           type="danger" circle plain size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="text-align: right;"
            @current-change="handleCurrentChange3"
            :current-page.sync="searchData3.pageNum"
            :page-size="searchData3.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="searchData3.total">
          </el-pagination>
        </div>
        <div class="organizationRight" style="margin-top: -25px;">
          <div class="organizationRightTitle" style="background: #fff;">审核流信息</div>
          <p class="MasterBox">
            <span><b>名称：</b>{{lookReviewRowData.reviewMasterName}}</span>
            <span><b>单据类型：</b>
                    <span v-for="item in reviewBillTypes"  :key="item.value"
                          v-if="lookReviewRowData.billTypeId === item.value">{{item.label}}</span>
                  </span>
          </p>
          <p class="MasterBox">
            <span><b>仓库：</b>{{lookReviewRowData.wareName}}</span>
          </p>
          <el-divider content-position="left">详细信息</el-divider>

          <div class="tableBox">
            <el-table
              highlight-current-row
              :data="reviewNodes"
              v-loading="reviewNodeLoading"
              style="width: 100%">
              <el-table-column
                sortable
                show-overflow-tooltip
                prop="reviewNodeName"
                label="节点名称">
              </el-table-column>
              <el-table-column
                sortable
                show-overflow-tooltip
                label="审核人">
                <template slot-scope="scope">
                        <span v-for="(item,index) in scope.row.userNames" :key="index">
                          <i v-if="index !== 0"> / </i>
                          {{item}}
                        </span>
                </template>
              </el-table-column>
              <el-table-column
                sortable
                show-overflow-tooltip
                label="节点类型"
                width="100">
                <template slot-scope="scope">
                  <span v-if="scope.row.type === 0">与</span>
                  <span v-if="scope.row.type === 1">或</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

      </el-tab-pane> 
    </el-tabs>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'Organization',
    data() {
      let validateOrganizationCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'organization', 'organization_code', this.form.organizationCode, 0);
      };
      let validateOrganizationCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'organization', 'organization_code', this.rowData.organizationCode, 1, this.rowData.organizationId);
      };
      let validateOrganizationName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'organization', 'organization_name', this.form.organizationName, 0);
      };
      let validateOrganizationName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'organization', 'organization_name', this.rowData.organizationName, 1, this.rowData.organizationId);
      };
      return {
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },
        searchData2: {
          pageNum: 1,
          pageSize: 5,
          total: null,
        },
        searchData3: {
          pageNum: 1,
          pageSize: 5,
          total: null,
        },

        organizations: [],
        accounts: [],
        reviews: [],
        reviewNodes: [],
        shippingAddresss: [],
        wares: [],
        lookRowData: {},
        lookReviewRowData: {},
        batchTactics: [],
        upperTactics: [],
        qcTactics: [],
        soMergeTactics: [],
        pickTactics:[],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        dialogVisible: false,
        dialogAddressVisible: false,
        dialogUpdateAddressVisible: false,
        isShowAddressSelectBox: false,
        isShowAddressSelectBox2: false,
        dialogNewReviewVisible: false,
        dialogUpdateReviewVisible: false,
        reviewLoading: false,
        reviewNodeLoading: false,
        addressLoading: false,


        form: {
          organizationCode: '',
          organizationName: '',
          linkMan: '',
          linkPhone: null,
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          soMergeTacticCode: '',
          pickTacticCode:'',
          memo: '',
        },
        rules: {
          organizationCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateOrganizationCode, trigger: 'blur'}
          ],
          organizationName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateOrganizationName, trigger: 'blur'}
          ],
          linkMan: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ],
          linkPhone: [
            {required: true, message: '请输入联系电话', trigger: 'blur'},
            {type: 'number', message: '联系电话必须为纯数字', trigger: 'blur'}
          ],
          batchTacticCode: [
            {required: true, message: '请选择批次策略', trigger: 'blur'}
          ],
          upperTacticCode: [
            {required: true, message: '请选择上架策略', trigger: 'blur'}
          ],
          qcTacticCode: [
            {required: true, message: '请选择质检策略', trigger: 'blur'}
          ],
          soMergeTacticCode: [
            {required: true, message: '请选择合并策略', trigger: 'blur'}
          ],
          pickTacticCode:[{required:true,message:'请选择拣货策略',trigger:'blur'}],
        },
        formLabelWidth: '120px',

        rowData: {
          organizationCode: '',
          organizationName: '',
          linkMan: '',
          linkPhone: null,
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          soMergeTacticCode: '',
          pickTacticCode:'',
          memo: '',
        },
        rules2: {
          organizationCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateOrganizationCode2, trigger: 'blur'}
          ],
          organizationName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateOrganizationName2, trigger: 'blur'}
          ],
          linkMan: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ],
          linkPhone: [
            {required: true, message: '请输入联系电话', trigger: 'blur'},
            {type: 'number', message: '联系电话必须为纯数字', trigger: 'blur'}
          ],
          batchTacticCode: [
            {required: true, message: '请选择批次策略', trigger: 'blur'}
          ],
          upperTacticCode: [
            {required: true, message: '请选择上架策略', trigger: 'blur'}
          ],
          qcTacticCode: [
            {required: true, message: '请选择质检策略', trigger: 'blur'}
          ],
          soMergeTacticCode: [
            {required: true, message: '请选择合并策略', trigger: 'blur'}
          ],
          pickTacticCode:[{required:true,message:'请选择拣货策略',trigger:'blur'}],
        },


        activeName: 'first',
        activeAddressName: 'S',
        treeDataS: [],
        treeDataC: [],
        treeDataQ: [],
        radioS: '',
        radioC: '',
        radioQ: '',

        addressForm: {
          organizationId: null,
          wareId: null,
          linkMan: '',
          linkPhone: null,
          province: '',
          city: '',
          area: '',
          detailAddress: '',
          state: '0'
        },
        rulesAddress: {
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          linkMan: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ],
          linkPhone: [
            {required: true, message: '请输入联系电话', trigger: 'blur'},
            {type: 'number', message: '联系电话必须为纯数字', trigger: 'blur'}
          ],
          province: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          city: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          area: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          detailAddress: [
            {required: true, message: '请输入详细地址', trigger: 'blur'}
          ],
        },
        addressFormRowData: {
          organizationId: null,
          wareId: null,
          linkMan: '',
          linkPhone: null,
          province: '',
          city: '',
          area: '',
          detailAddress: '',
          state: '0'
        },
        rulesAddress2: {
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          linkMan: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ],
          linkPhone: [
            {required: true, message: '请输入联系电话', trigger: 'blur'},
            {type: 'number', message: '联系电话必须为纯数字', trigger: 'blur'}
          ],
          province: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          city: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          area: [
            {required: true, message: '请选择所在地区', trigger: 'blur'}
          ],
          detailAddress: [
            {required: true, message: '请输入详细地址', trigger: 'blur'}
          ],
        },
        reviewForm: {
          reviewMaster: {
            billTypeId: '',
            reviewMasterName: '',
            wareId: PF.getLocal('wareId', 'number'),
            wareName: PF.getLocal('wareName'),
            organizationId: null,
            organizationName: '',
          },
          reviewNodes: [
            {
              orderNo: 0,
              reviewNodeName: '',
              reviewUsers: [],
              type: 0
            }
          ]
        },
        rulesReview: {
          billTypeId: [
            {required: true, message: '请选择单据类型', trigger: 'blur'}
          ],
          reviewMasterName: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ],
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
        },
        reviewRowData: {
          reviewMaster: {
            billTypeId: '',
            reviewMasterName: '',
            wareId: '',
            organizationId: ''
          },
          reviewNodes: [
            {
              orderNo: 0,
              reviewNodeName: '',
              reviewUsers: [],
              type: 0
            }
          ]
        },
        rulesReview2: {
          billTypeId: [
            {required: true, message: '请选择单据类型', trigger: 'blur'}
          ],
          reviewMasterName: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ],
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
        },
      }
    },
    computed:{
      ...mapState([
        "reviewBillTypes"
      ])
    },
    watch: {
      activeName(val) {
        if (val === 'fifth') {
          this.getReviewData()
        }
      },
      radioS(val) {
        if (this.isShowAddressSelectBox) {
          this.treeDataC = this.treeDataS[val].children;
          this.addressForm.province = this.treeDataS[val].name;
          if (this.treeDataC === undefined) {
            this.addressForm.city = '-';
            this.addressForm.area = '-';
            this.isShowAddressSelectBox = false
          } else {
            this.activeAddressName = 'C'
          }
        }
        if (this.isShowAddressSelectBox2) {
          this.treeDataC = this.treeDataS[val].children;
          this.addressFormRowData.province = this.treeDataS[val].name;
          if (this.treeDataC === undefined) {
            this.addressFormRowData.city = '-';
            this.addressFormRowData.area = '-';
            this.isShowAddressSelectBox2 = false
          } else {
            this.activeAddressName = 'C'
          }
        }
      },
      radioC(val) {
        if (this.isShowAddressSelectBox) {
          this.treeDataQ = this.treeDataC[val].children;
          this.addressForm.city = this.treeDataC[val].name;
          if (this.treeDataQ === undefined) {
            this.addressForm.area = '-';
            this.isShowAddressSelectBox = false
          } else {
            this.activeAddressName = 'Q'
          }
        }
        if (this.isShowAddressSelectBox2) {
          this.treeDataQ = this.treeDataC[val].children;
          this.addressFormRowData.city = this.treeDataC[val].name;
          if (this.treeDataQ === undefined) {
            this.addressFormRowData.area = '-';
            this.isShowAddressSelectBox2 = false
          } else {
            this.activeAddressName = 'Q'
          }
        }
      },
      radioQ(val) {
        if (this.isShowAddressSelectBox) {
          this.addressForm.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox = false
        }
        if (this.isShowAddressSelectBox2) {
          this.addressFormRowData.area = this.treeDataQ[val].name;
          this.isShowAddressSelectBox2 = false
        }
      }
    },
    methods: {

      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getOrganizationData();
      },
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getOrganizationData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getShippingAddressData()
      },
      handleCurrentChange3(val) {
        this.searchData3.pageNum = val;
        this.getReviewData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getOrganizationData();
      },
      getOrganizationData() {
        IOT.getServerData('/organizations/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.organizations = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if (ret.rows.length == 0) {
              this.lookRowData = {};
              return
            }
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getShippingAddressData(id) {
        this.addressLoading = true;
        this.searchData2.organizationId = id;
        IOT.getServerData('/shipping/addresss/list', 'get', this.searchData2, (ret) => {
          this.addressLoading = false;
          if (ret.code === 200) {
            this.shippingAddresss = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
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
      getAccountData() {
        IOT.getServerData('/users/userList', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.accounts = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getReviewData() {
        this.reviewLoading = true;
        let searchData3 = PF.JSON(this.searchData3);
        searchData3.organizationId = this.lookRowData.organizationId;
        IOT.getServerData('/review/masters/list', 'get', searchData3, (ret) => {
          this.reviewLoading = false;
          if (ret.code === 200) {
            this.reviews = ret.rows;
            this.searchData3.total = ret.total;
            this.searchData3.pageNum = ret.pageNumber
            this.getLookReviewRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getReviewNodeData(id, type) {
        this.reviewNodeLoading = true;
        IOT.getServerData('/review/nodes/list', 'get', {reviewMasterId: id}, (ret) => {
          this.reviewNodeLoading = false;
          if (ret.code === 200) {
            if (type === 'update') {
              this.reviewRowData.reviewNodes = ret.rows
            } else {
              this.reviewNodes = ret.rows;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getBatchTacticData() {
        IOT.getServerData('/batch/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.batchTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getUpperTacticData() {
        IOT.getServerData('/upper/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.upperTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getQcTacticData() {
        IOT.getServerData('/qc/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.qcTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSoMergeTacticData() {
        IOT.getServerData('/so/merge/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.soMergeTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPickTacticData() {
        IOT.getServerData('/pick/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.pickTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/organizations/insert', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getOrganizationData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form')
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      clearReviewNodes() {
        this.reviewForm.reviewNodes = [{
          orderNo: 0,
          reviewNodeName: '',
          reviewUsers: [],
          type: 0
        }];
        this.reviewRowData.reviewNodes = [{
          orderNo: 0,
          reviewNodeName: '',
          reviewUsers: [],
          type: 0
        }]
      },
      showNewReviewModal(){
        let lookRowData = PF.JSON(this.lookRowData);
        this.reviewForm.reviewMaster.organizationId = lookRowData.organizationId;
        this.reviewForm.reviewMaster.organizationName = lookRowData.organizationName;
        this.dialogNewReviewVisible = true
      },
      commitReviewForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let reviewForm = JSON.parse(JSON.stringify(this.reviewForm));
            let reviewNodes = reviewForm.reviewNodes;
            for (let i in reviewNodes) {
              reviewNodes[i].orderNo = Number(i) + 1;
              let reviewUsers = reviewNodes[i].reviewUsers;
              for (let j in reviewUsers) {
                reviewUsers[j] = {
                  userId: reviewUsers[j]
                }
              }
            }
            IOT.getServerData('/review/masters/add', 'post', reviewForm, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getReviewData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('reviewForm.reviewMaster');
            this.clearReviewNodes();
            this.dialogNewReviewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      commitAddressForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/shipping/addresss/insert', 'post', this.addressForm, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getShippingAddressData(this.lookRowData.organizationId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('addressForm')
            this.dialogAddressVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/organizations/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getOrganizationData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData')
            this.dialogUpdateVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateReviewForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let reviewRowData = JSON.parse(JSON.stringify(this.reviewRowData));
            let reviewNodes = reviewRowData.reviewNodes;
            for (let i in reviewNodes) {
              reviewNodes[i].orderNo = Number(i) + 1;
              let userIds = reviewNodes[i].userIds;
              reviewNodes[i].reviewUsers = [];
              for (let j in userIds) {
                reviewNodes[i].reviewUsers[j] = {
                  userId: userIds[j]
                }
              }
            }
            IOT.getServerData('/review/masters/update', 'post', reviewRowData, (ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.reviewRowData.reviewMaster = {};
                  that.getReviewData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('reviewRowData.reviewMaster')
            this.dialogUpdateReviewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      updateAddressForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/shipping/addresss/update', 'post', this.addressFormRowData, (ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getShippingAddressData(this.lookRowData.organizationId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('addressFormRowData')
            this.dialogUpdateAddressVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row) {
        row.linkPhone = Number(row.linkPhone)
        this.rowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      showUpdateReviewModal(row) {
        this.getReviewNodeData(row.reviewMasterId, 'update');
        this.reviewRowData.reviewMaster = JSON.parse(JSON.stringify(row));
        this.dialogUpdateReviewVisible = true
      },
      showUpdateAddressModal(row) {
        row.linkPhone = Number(row.linkPhone);
        row.state = String(row.state);
        this.addressFormRowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateAddressVisible = true
      },
      getLookRowData(row) {
        this.lookRowData = row == null ? {} : JSON.parse(JSON.stringify(row));
        this.getShippingAddressData(this.lookRowData.organizationId);
         this.getReviewData();
      },
      getLookReviewRowData(row) {
        this.lookReviewRowData = JSON.parse(JSON.stringify(row))
        this.getReviewNodeData(this.lookReviewRowData.reviewMasterId)
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form')
        this.dialogNewVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData')
        this.dialogUpdateVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseAddress(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('addressForm');
        this.dialogAddressVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseAddress2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('addressFormRowData');
        this.dialogUpdateAddressVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteOrganization(id)
          })
          .catch(_ => {
          });
      },
      isDeleteReview(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteReview(id)
          })
          .catch(_ => {
          });
      },
      isDeleteAddress(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteAddress(id)
          })
          .catch(_ => {
          });
      },
      deleteOrganization(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/organizations/delete', 'get', {organizationId: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getOrganizationData()
            });
          } else if (ret.code === 100019) {
            IOT.tips(ret.message || '该货主已绑定,不可删除！', 'error');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteReview(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/review/masters/delete', 'get', {reviewMasterId: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getReviewData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteAddress(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/shipping/addresss/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getShippingAddressData(this.lookRowData.organizationId)
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      enableAccount(id) {
        IOT.getServerData('/organizations/enable', 'get', {organizationId: id}, (ret) => {
          if (ret.code === 200) {
            this.getOrganizationData()
            IOT.tips('已启用！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      disableAccount(id) {
        IOT.getServerData('/organizations/disable', 'get', {organizationId: id}, (ret) => {
          if (ret.code === 200) {
            this.getOrganizationData()
            IOT.tips('已禁用！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getAddressJson() {
        let that = this;
        $.getJSON('/json/address.json', (data) => {
          let treeData = PF.toTree(data)
          that.treeDataS = treeData[0].children
        })
      },
      clearAddressData() {
        this.activeAddressName = 'S';
        this.treeDataC = [];
        this.treeDataQ = [];
        this.radioS = '';
        this.radioC = '';
        this.radioQ = '';
        this.inputS = '';
        this.inputC = '';
        this.inputQ = '';
      },
      showAddressModal() {
        this.addressForm.organizationId = this.lookRowData.organizationId;
        this.addressForm.organizationName = this.lookRowData.organizationName;
        this.addressForm.linkMan = this.lookRowData.linkMan;
        this.addressForm.linkPhone = Number(this.lookRowData.linkPhone);
        this.getWareData();
      },
      newAddressChangeWare() {
        for (let i in this.wares) {
          if (this.wares[i].wareId === this.addressForm.wareId) {
            this.addressForm.province = this.wares[i].province
            this.addressForm.city = this.wares[i].city
            this.addressForm.area = this.wares[i].area
            this.addressForm.detailAddress = this.wares[i].detailAddress
          }
        }
      },
      reviewNodeUpDown(index, type, state) {
        let reviewNodes = [];
        if (type === 'save') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewForm.reviewNodes));
        } else if (type === 'update') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewRowData.reviewNodes));
        }
        let nowReviewNode = reviewNodes[index];
        if (state === 'up') {
          reviewNodes[index] = reviewNodes[index - 1];
          reviewNodes[index - 1] = nowReviewNode;
        } else if (state === 'down') {
          reviewNodes[index] = reviewNodes[index + 1];
          reviewNodes[index + 1] = nowReviewNode;
        }
        if (type === 'save') {
          this.reviewForm.reviewNodes = reviewNodes;
        } else if (type === 'update') {
          this.reviewRowData.reviewNodes = reviewNodes;
        }
      },
      reviewNodeAdd(index, type) {
        let reviewNodes = [];
        if (type === 'save') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewForm.reviewNodes));
        } else if (type === 'update') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewRowData.reviewNodes));
        }
        reviewNodes.splice(index + 1, 0, {
          orderNo: 0,
          reviewNodeName: '',
          reviewUsers: [],
          type: 0
        });
        if (type === 'save') {
          this.reviewForm.reviewNodes = reviewNodes;
        } else if (type === 'update') {
          this.reviewRowData.reviewNodes = reviewNodes;
        }
      },
      reviewNodeDelete(index, type) {
        let reviewNodes = [];
        if (type === 'save') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewForm.reviewNodes));
        } else if (type === 'update') {
          reviewNodes = JSON.parse(JSON.stringify(this.reviewRowData.reviewNodes));
        }
        reviewNodes.splice(index, 1);
        if (type === 'save') {
          this.reviewForm.reviewNodes = reviewNodes;
        } else if (type === 'update') {
          this.reviewRowData.reviewNodes = reviewNodes;
        }
      },
      tableRowClassName({row, rowIndex}) {
        if (row.organizationStatus === 'disable') {
          return 'disable-row';
        } else if (row.organizationStatus === 'enable') {
          return 'enable-row';
        }
        return '';
      }
    },
    created() {
      // this.getPickTacticData();
      this.getOrganizationData();
      this.getAddressJson();
      this.getAccountData();
      // this.getBatchTacticData();
      // this.getUpperTacticData();
      // this.getQcTacticData();
      // this.getSoMergeTacticData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .organization {

  }
</style>
