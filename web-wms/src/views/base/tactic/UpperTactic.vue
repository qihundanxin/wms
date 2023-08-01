<template>
  <div class="upperTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getUpperTacticData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人/电话" clearable></el-input>
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

    <el-dialog title="新增策略" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="编码" prop="upperTacticCode" :label-width="formLabelWidth">
          <el-input v-model="form.upperTacticCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="upperTacticName" :label-width="formLabelWidth">
          <el-input v-model="form.upperTacticName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
          <el-select v-model="form.wareId" filterable placeholder="请选择仓库" style="width: 70%;">
            <el-option
              v-for="item in wares"
              :key="item.wareId"
              :label="item.wareName"
              :value="item.wareId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改策略" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="编码" prop="upperTacticCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.upperTacticCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="upperTacticName" :label-width="formLabelWidth">
          <el-input v-model="rowData.upperTacticName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
          <el-select v-model="rowData.wareId" filterable placeholder="请选择仓库" style="width: 70%;">
            <el-option
              v-for="item in wares"
              :key="item.wareId"
              :label="item.wareName"
              :value="item.wareId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="rowData.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为策略 '+lookRowData.upperTacticName+' 修改详情'" :visible.sync="dialogUpdateDetailVisible"
               :before-close="handleCloseDetail2" width="900px" style="margin-top: -10vh">
      <div class="upperTacticBox">
        <el-form class="clearFloat" :model="detailRowData" :rules="rulesDetail2" ref="detailRowData">
          <el-form-item label="顺序" prop="orderNo" :label-width="formLabelWidth">
            <el-input-number v-model="detailRowData.orderNo" :min="0" size="mini" style="width: 100%;"></el-input-number>
          </el-form-item>
          <el-form-item label="启用" prop="state" :label-width="formLabelWidth">
            <el-switch
              v-model="detailRowData.state"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="规则方式" prop="upperTacticType" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.upperTacticType" filterable placeholder="请选择规则代码" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in upperTacticTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item> 
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailRowData.upperTacticType!==3  && detailRowData.upperTacticType!==4" label="原始货位" prop="fromCellCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailRowData.fromCellCode" filterable placeholder="请选择原始货位" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cells"
                :key="item.cellCode"
                :label="item.celName"
                :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-show="detailRowData.upperTacticType!==1 &&  detailRowData.upperTacticType!==4" label="目标货区" prop="toAreaCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailRowData.toAreaCode" filterable placeholder="请选择目标货区" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in areas"
                :key="item.areaCode"
                :label="item.areaName"
                :value="item.areaCode">
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item v-show="detailRowData.upperTacticType!==2 && detailRowData.upperTacticType!==3" label="目标货位" prop="toCellCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailRowData.toCellCode" filterable placeholder="请选择目标货位" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cells"
                :key="item.cellCode"
                :label="item.celName"
                :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="订单类型" prop="orderType" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.orderType" filterable placeholder="请选择订单类型" size="mini" style="width: 100%;">
              <el-option
                v-for="item in orderTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="包装级别" prop="packLevel" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.packLevel" filterable placeholder="请选择包装级别" size="mini" style="width: 100%;">
              <el-option
                v-for="item in packLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.abcLevel" filterable placeholder="请选择产品循环级别" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in abcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="必须空货位"    prop="isEmptyCell" :label-width="formLabelWidth">
            <el-switch
              v-model="detailRowData.isEmptyCell"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
         
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailRowData.isEmptyCell == 0" label="混批" prop="noConfusedBatch"
                        :label-width="formLabelWidth">
            <el-select  size="mini" v-model="detailRowData.noConfusedBatch">
              <el-option v-for="item in confusedBatchs" 
              :key="item.value"  
              :value="item.value"  
              :label="item.label">
              </el-option>
               
            </el-select>
            
            
          </el-form-item>
         
          
          <el-form-item v-show="detailRowData.isEmptyCell == 0 && detailRowData.noConfusedBatch!=0" label="必须有相同批号" prop="isEqualBatch"
                        :label-width="formLabelWidth">
            <el-switch
              v-model="detailRowData.isEqualBatch"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
          
         
          <el-form-item v-show="detailRowData.isEmptyCell == 0 && detailRowData.noConfusedBatch!=0" label="混放批号最大值" prop="canBatchMax"
                        :label-width="formLabelWidth">
            <el-input-number v-model="detailRowData.canBatchMax" :min="0" size="mini"
                             style="width: 100%;"></el-input-number>
          </el-form-item>
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailRowData.isEmptyCell == 0" label="混放" prop="noConfusedItem" size="mini"
                        :label-width="formLabelWidth">



            <el-select v-model="detailRowData.noConfusedItem">
              <el-option v-for="item in confusedItems" 
              :key="item.value"  
              :value="item.value"  
              :label="item.label">
              </el-option>
               
            </el-select>
           
          </el-form-item>
          <el-form-item v-show="detailRowData.isEmptyCell == 0 && detailRowData.noConfusedItem!=0" label="必须有相同产品" prop="isEqualItem"
                        :label-width="formLabelWidth">
            <el-switch
              v-model="detailRowData.isEqualItem"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
           <el-form-item v-show="detailRowData.isEmptyCell == 0 && detailRowData.noConfusedItem!=0" label="混放产品最大值" prop="canItemMax"
                        :label-width="formLabelWidth">
            <el-input-number v-model="detailRowData.canItemMax" :min="0" size="mini"
                             style="width: 100%;"></el-input-number>
          </el-form-item>
          
          <div style="float: left;width: 100%;"></div>
          <el-form-item label="货位类型" prop="cellType" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.cellType" filterable placeholder="请选择货位类型" size="mini" style="width: 100%;">
              <el-option
                v-for="item in cellTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.cellUseType" filterable placeholder="请选择货位使用类型" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellUseTypes"
              
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === -1 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
        <!--  <el-form-item label="货位状态" prop="cellState" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellState" filterable placeholder="请选择货位状态" size="mini" style="width: 100%;">
              <el-option
                v-for="item in cellStates"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item> -->
          
        <!--  <el-form-item label="货位拣货方式" prop="cellPickType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellPickType" filterable placeholder="请选择货位拣货方式" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellPickTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="货位周转" prop="cellAbcLevel" :label-width="formLabelWidth">
            <el-select v-model="detailRowData.cellAbcLevel" filterable placeholder="请选择货位周转" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellAbcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
        <!--  <el-form-item label="货位组" prop="shelfInfoId" :label-width="formLabelWidth">
            <el-select v-model="detailForm.shelfInfoId" filterable placeholder="请选择货位组" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in shelfs"
                :key="item.shelfInfoId"
                :label="item.shelfName"
                :value="item.shelfInfoId">
              </el-option>
            </el-select>
          </el-form-item>  -->
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailRowData');dialogUpdateDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateDetailForm('detailRowData')">确 定</el-button>
      </div>
    </el-dialog>
     <el-dialog :title="'为策略 '+lookRowData.upperTacticName+' 新增详情'" :visible.sync="dialogNewDetailVisible"
               :before-close="handleCloseDetail" width="900px" style="margin-top: -10vh">
      <div class="upperTacticBox">
        <el-form class="clearFloat" :model="detailForm" :rules="rulesDetail" ref="detailForm">
          <el-form-item label="顺序" prop="orderNo" :label-width="formLabelWidth">
            <el-input-number v-model="detailForm.orderNo" :min="0" size="mini" style="width: 100%;"></el-input-number>
          </el-form-item>
          <el-form-item label="启用" prop="state" :label-width="formLabelWidth">
            <el-switch
              v-model="detailForm.state"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="规则方式" prop="upperTacticType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.upperTacticType" filterable placeholder="请选择规则代码" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in upperTacticTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item> 
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailForm.upperTacticType!==3  && detailForm.upperTacticType!==4" label="原始货位" prop="fromCellCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailForm.fromCellCode" filterable placeholder="请选择原始货位" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cells"
                :key="item.cellCode"
                :label="item.celName"
                :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-show="detailForm.upperTacticType!==1 &&  detailForm.upperTacticType!==4" label="目标货区" prop="toAreaCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailForm.toAreaCode" filterable placeholder="请选择目标货区" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in areas"
                :key="item.areaCode"
                :label="item.areaName"
                :value="item.areaCode">
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item v-show="detailForm.upperTacticType!==2 && detailForm.upperTacticType!==3" label="目标货位" prop="toCellCode"
                        :label-width="formLabelWidth">
            <el-select v-model="detailForm.toCellCode" filterable placeholder="请选择目标货位" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cells"
                :key="item.cellCode"
                :label="item.celName"
                :value="item.cellCode">
              </el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="订单类型" prop="orderType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.orderType" filterable placeholder="请选择订单类型" size="mini" style="width: 100%;">
              <el-option
                v-for="item in orderTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="包装级别" prop="packLevel" :label-width="formLabelWidth">
            <el-select v-model="detailForm.packLevel" filterable placeholder="请选择包装级别" size="mini" style="width: 100%;">
              <el-option
                v-for="item in packLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="detailForm.abcLevel" filterable placeholder="请选择产品循环级别" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in abcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"> </el-divider>
          </div>
          <el-form-item label="必须空货位"    prop="isEmptyCell" :label-width="formLabelWidth">
            <el-switch
              v-model="detailForm.isEmptyCell"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
         
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailForm.isEmptyCell == 0" label="混批" prop="noConfusedBatch"
                        :label-width="formLabelWidth">
            <el-select  size="mini" v-model="detailForm.noConfusedBatch">
              <el-option v-for="item in confusedBatchs" 
              :key="item.value"  
              :value="item.value"  
              :label="item.label">
              </el-option>
               
            </el-select>
            
            
          </el-form-item>
         
          
          <el-form-item v-show="detailForm.isEmptyCell == 0 && detailForm.noConfusedBatch!=0" label="必须有相同批号" prop="isEqualBatch"
                        :label-width="formLabelWidth">
            <el-switch
              v-model="detailForm.isEqualBatch"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
          
         
          <el-form-item v-show="detailForm.isEmptyCell == 0 && detailForm.noConfusedBatch!=0" label="混放批号最大值" prop="canBatchMax"
                        :label-width="formLabelWidth">
            <el-input-number v-model="detailForm.canBatchMax" :min="0" size="mini"
                             style="width: 100%;"></el-input-number>
          </el-form-item>
          <div style="float: left;width: 100%;"></div>
          <el-form-item v-show="detailForm.isEmptyCell == 0" label="混放" prop="noConfusedItem" size="mini"
                        :label-width="formLabelWidth">



            <el-select v-model="detailForm.noConfusedItem">
              <el-option v-for="item in confusedItems" 
              :key="item.value"  
              :value="item.value"  
              :label="item.label">
              </el-option>
               
            </el-select>
           
          </el-form-item>
          <el-form-item v-show="detailForm.isEmptyCell == 0 && detailForm.noConfusedItem!=0" label="必须有相同产品" prop="isEqualItem"
                        :label-width="formLabelWidth">
            <el-switch
              v-model="detailForm.isEqualItem"
              active-value=1
              inactive-value=0>
            </el-switch>
          </el-form-item>
           <el-form-item v-show="detailForm.isEmptyCell == 0 && detailForm.noConfusedItem!=0" label="混放产品最大值" prop="canItemMax"
                        :label-width="formLabelWidth">
            <el-input-number v-model="detailForm.canItemMax" :min="0" size="mini"
                             style="width: 100%;"></el-input-number>
          </el-form-item>
          
          <div style="float: left;width: 100%;"></div>
          <el-form-item label="货位类型" prop="cellType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellType" filterable placeholder="请选择货位类型" size="mini" style="width: 100%;">
              <el-option
                v-for="item in cellTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellUseType" filterable placeholder="请选择货位使用类型" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellUseTypes"
              
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === -1 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
        <!--  <el-form-item label="货位状态" prop="cellState" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellState" filterable placeholder="请选择货位状态" size="mini" style="width: 100%;">
              <el-option
                v-for="item in cellStates"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item> -->
          
        <!--  <el-form-item label="货位拣货方式" prop="cellPickType" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellPickType" filterable placeholder="请选择货位拣货方式" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellPickTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="货位周转" prop="cellAbcLevel" :label-width="formLabelWidth">
            <el-select v-model="detailForm.cellAbcLevel" filterable placeholder="请选择货位周转" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in cellAbcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="{color: item.value === 0 ? notLimit : ''}">
              </el-option>
            </el-select>
          </el-form-item>
        <!--  <el-form-item label="货位组" prop="shelfInfoId" :label-width="formLabelWidth">
            <el-select v-model="detailForm.shelfInfoId" filterable placeholder="请选择货位组" size="mini"
                       style="width: 100%;">
              <el-option
                v-for="item in shelfs"
                :key="item.shelfInfoId"
                :label="item.shelfName"
                :value="item.shelfInfoId">
              </el-option>
            </el-select>
          </el-form-item>  -->
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailForm');dialogNewDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitDetailForm('detailForm')">确 定</el-button>
      </div>
    </el-dialog>
    

    <div class="tableBox organizationLeftTab" style="width:20%">
      <el-table
        v-loading="loading.masterTable"
        highlight-current-row
        @current-change="getLookRowData"
        :data="upperTactics"
        style="width: 100%">
       
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="upperTacticName"
          label="名称">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.upperTacticId)" icon="iconfont icon-shanchu" type="danger" circle
                       plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-left-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout=" total, prev, pager, next "
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight" style="width:72%">
      <div class="organizationRightTitle">策略信息</div>
      <p class="MasterBox">
        <span><b>策略编码：</b>{{lookRowData.upperTacticCode}}</span>
        <span v-for="(item, index) in wares" :key="index" v-if="item.wareId == lookRowData.wareId"><b>仓库：</b>{{item.wareName}}</span>
      </p>
      <p class="MasterBox">
        <span><b>策略名称：</b>{{lookRowData.upperTacticName}}</span>
        <span v-for="(item, index) in organizations" :key="index" v-if="item.organizationId == lookRowData.organizationId"><b>货主：</b>{{item.organizationName}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>
      <div class="headerBtns">
        <el-button @click="dialogNewDetailVisible = true;showDetailModal()" size="small" plain
                   icon="iconfont icon-xinzeng">新增详情
        </el-button>
      </div>
      <div class="tableBox">
        <el-table
        border=true
        header-row-class-name="deerTableHeader"
          v-loading="loading.masterTable"
          highlight-current-row
          :data="upperTacticDetails"  
          style="width: 100%">
        
        <el-table-column
           type="index"
            width="80">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="upperTacticDetailId"
            label="顺序"
            width="80">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="upperTacticType"
            label="规则方式"
            width="120">
            <template slot-scope="scope">
              <span v-for="(item, index) in upperTacticTypes" :key="index"
                    v-if="item.value === scope.row.upperTacticType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="state"
            label="启用"
            width="80">
            <template slot-scope="scope">
              <i v-show="scope.row.state === 1" class="iconfont icon-xuanzhong1"></i>
              <i v-show="scope.row.state === 0" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="toAreaCode"
            label="目标货区"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in areas" :key="index"
                    v-if="item.areaCode === scope.row.toAreaCode && scope.row.upperTacticType !== 1">{{item.areaName}}</span>
              <span v-if="scope.row.upperTacticType === 1">-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="fromCellCode"
            label="原始货位"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cells" :key="index"
                    v-if="item.cellCode === scope.row.fromCellCode && scope.row.upperTacticType !== 3">{{item.cellName}}</span>
              <span v-if="scope.row.upperTacticType === 3">-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="toCellCode"
            label="目标货位"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cells" :key="index"
                    v-if="item.cellCode === scope.row.toCellCode && scope.row.upperTacticType !== 2">{{item.cellName}}</span>
              <span v-if="scope.row.upperTacticType === 2">-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="orderType"
            label="订单类型"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in orderTypes" :key="index" v-if="item.value === scope.row.orderType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="packLevel"
            label="包装级别"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in packLevels" :key="index" v-if="item.value === scope.row.packLevel">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="abcLevel"
            label="产品循环级别"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in abcLevels" :key="index" v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="isEmptyCell"
            label="空货位"
            width="150">
            <template slot-scope="scope">
              <i v-show="scope.row.isEmptyCell === 1" class="iconfont icon-xuanzhong1"></i>
              <i v-show="scope.row.isEmptyCell === 0" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="noConfusedItem"
            label="不许混放"
            width="150">
            <template slot-scope="scope">
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.noConfusedItem === 1"
                 class="iconfont icon-xuanzhong1"></i>
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.noConfusedItem === 0"
                 class="iconfont icon-weixuanzhong"></i>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="noConfusedBatch"
            label="不许混批"
            width="150">
            <template slot-scope="scope">
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.noConfusedBatch === 1"
                 class="iconfont icon-xuanzhong1"></i>
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.noConfusedBatch === 0"
                 class="iconfont icon-weixuanzhong"></i>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="isEqualItem"
            label="必须有相同产品"
            width="150">
            <template slot-scope="scope">
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.isEqualItem === 1"
                 class="iconfont icon-xuanzhong1"></i>
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.isEqualItem === 0"
                 class="iconfont icon-weixuanzhong"></i>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="isEqualBatch"
            label="必须有相同批号"
            width="150">
            <template slot-scope="scope">
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.isEqualBatch === 1"
                 class="iconfont icon-xuanzhong1"></i>
              <i v-if="scope.row.isEmptyCell === 0" v-show="scope.row.isEqualBatch === 0"
                 class="iconfont icon-weixuanzhong"></i>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="canItemMax"
            label="混放产品最大值"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.isEmptyCell === 0">{{scope.row.canItemMax}}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="canBatchMax"
            label="混放批号最大值"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.isEmptyCell === 0">{{scope.row.canBatchMax}}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="cellType"
            label="货位类型"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cellTypes" :key="index" v-if="item.value === scope.row.cellType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="cellUseType"
            label="货位使用类型"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cellUseTypes" :key="index" v-if="item.value === scope.row.cellUseType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="cellState"
            label="货位状态"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cellStates" :key="index" v-if="item.value === scope.row.cellState">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="cellPickType"
            label="货位拣货方式"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cellPickTypes" :key="index" v-if="item.value === scope.row.cellPickType">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="cellAbcLevel"
            label="货位周转"
            width="150">
            <template slot-scope="scope">
              <span v-for="(item, index) in cellAbcLevels" :key="index" v-if="item.value === scope.row.cellAbcLevel">{{item.label}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="shelfName"
            label="货位组"
            width="150">
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="250">
            <template slot-scope="scope">
              <el-button @click="upDetail(scope.$index)" icon="iconfont el-icon-top" type="primary"   circle
                         plain size="small"> </el-button>
                          <el-button @click="showUpdateDetailModal(scope.row)" icon="iconfont el-icon-bottom" type="primary"  circle
                         plain  size="small"> </el-button>
              <el-button @click="showUpdateDetailModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle
                         plain size="small"></el-button>
              <el-button @click="isDeleteDetail(scope.row.upperTacticDetailId)" icon="iconfont icon-shanchu"
                         type="danger" circle plain size="small"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          style="text-align: right;"
          class="pagination-bottom"
          @current-change="handleCurrentChange2"
          :current-page.sync="searchData2.pageNum"
          :page-size="searchData2.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="searchData2.total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'UpperTactic',
    data() {
      let validateUpperTacticCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_code', this.form.upperTacticCode, 0);
      };
      let validateUpperTacticCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_code', this.rowData.upperTacticCode, 1, this.rowData.upperTacticId);
      };
      let validateUpperTacticName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_name', this.form.upperTacticName, 0);
      };
      let validateUpperTacticName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'upper_tactic', 'upper_tactic_name', this.rowData.upperTacticName, 1, this.rowData.upperTacticId);
      };
      return {
        wareId:PF.getLocal("wareId","number"),
        cellUseTypes: [ // 货位使用类型   拣货位 仓储位  理货位  过度货位
            {
            value: -1,
            label: '垃圾货位'
          },
          {
            value: 0,
            label: '验收位'
          },
          {
            value: 1,
            label: '拣货位'
          },
          {
            value: 2,
            label: '仓储位'
          },
          {
            value: 3,
            label: '集货位'
          },
          {
            value: 4,
            label: '上架车'
          }
        
        ],
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData: {
          wareId:PF.getLocal("wareId","number"),
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

        upperTactics: [],
        upperTacticDetails: [],
        lookRowData: {},
        wares: [],
        organizations: [],
        areas: [],
        cells: [],
        shelfs: [],
        confusedBatchs:[
          {
            value:0,
            label:"不许混批"
          },
          {
            value:1,
            label:"允许混批（优先）"
          },
          {
            value:2,
            label:"允许混批（不优先）"
          }
        ],
        confusedItems:[
          {
            value:0,
            label:"不许混放"
          },
          {
            value:1,
            label:"允许混放（优先）"
          },
          {
            value:2,
            label:"允许混放（不优先）"
          }
        ],
        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogNewDetailVisible: false,
        dialogUpdateDetailVisible: false,

        notLimit: '#E6A23C',

        form: {
          upperTacticCode: '',
          upperTacticName: '',
          wareId: null,
          organizationId: null,
        },
        rules: {
          upperTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateUpperTacticCode, trigger: 'blur'}
          ],
          upperTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateUpperTacticName, trigger: 'blur'}
          ],
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
        },
        formLabelWidth: '150px',

        rowData: {
          upperTacticCode: '',
          upperTacticName: '',
          wareId: null,
          organizationId: null,
        },
        rules2: {
          upperTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateUpperTacticCode2, trigger: 'blur'}
          ],
          upperTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateUpperTacticName2, trigger: 'blur'}
          ],
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
        },
        detailForm: {
          upperTacticId: null,
        
          orderNo: 0,
          upperTacticType: '',
          state: '1',
          toAreaCode: null,
          fromCellCode: null,
          toCellCode: null,
          orderType: 0,
          packLevel: 0,
          abcLevel: 0,
          isEmptyCell: 0,
          noConfusedItem: 0,
          noConfusedBatch: 0,
          isEqualItem: 0,
          isEqualBatch: 0,
          canItemMax: 0,
          canBatchMax: 0,
          cellType: 0,
          cellUseType: -1,
          cellState: 0,
          cellPickType: 0,
          cellAbcLevel: 0,
          shelfInfoId: null,
        },
        rulesDetail: {
          orderNo: [
            {required: true, message: '请输入行号', trigger: 'blur'}
          ],
          upperTacticType: [
            {required: true, message: '请选择规则代码', trigger: 'blur'}
          ],
          toAreaCode: [
            {required: true, message: '请选择目标货区', trigger: 'blur'}
          ],
          fromCellCode: [
            {required: true, message: '请选择原始货位', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '请选择目标货位', trigger: 'blur'}
          ],
          orderType: [
            {required: true, message: '请选择订单类型', trigger: 'blur'}
          ],
          packLevel: [
            {required: true, message: '请选择包装级别', trigger: 'blur'}
          ],
          abcLevel: [
            {required: true, message: '请选择产品循环级别', trigger: 'blur'}
          ],
          canItemMax: [
            {required: true, message: '请输入混放产品最大值', trigger: 'blur'}
          ],
          canBatchMax: [
            {required: true, message: '请输入混放批号最大值', trigger: 'blur'}
          ],
          cellType: [
            {required: true, message: '请选择货位类型', trigger: 'blur'}
          ],
          cellUseType: [
            {required: true, message: '请选择货位使用类型', trigger: 'blur'}
          ],
          cellState: [
            {required: true, message: '请选择货位状态', trigger: 'blur'}
          ],
          cellPickType: [
            {required: true, message: '请选择货位拣货方式', trigger: 'blur'}
          ],
          cellAbcLevel: [
            {required: true, message: '请选择货位周转', trigger: 'blur'}
          ],
          shelfInfoId: [
            {required: true, message: '请选择货位组', trigger: 'blur'}
          ],
        },
        detailRowData: {
          upperTacticId: null,
          orderNo: 0,
          upperTacticType: '',
          state: '1',
          toAreaCode: null,
          fromCellCode: null,
          toCellCode: null,
          orderType: 0,
          packLevel: 0,
          abcLevel: 0,
          isEmptyCell: 0,
          noConfusedItem: 0,
          noConfusedBatch: 0,
          isEqualItem: 0,
          isEqualBatch: 0,
          canItemMax: 0,
          canBatchMax: 0,
          cellType: 0,
          cellUseType: -1,
          cellState: 0,
          cellPickType: 0,
          cellAbcLevel: 0,
          shelfInfoId: null,
        },
        rulesDetail2: {
          orderNo: [
            {required: true, message: '请输入行号', trigger: 'blur'}
          ],
          upperTacticType: [
            {required: true, message: '请选择规则代码', trigger: 'blur'}
          ],
          toAreaCode: [
            {required: true, message: '请选择目标货区', trigger: 'blur'}
          ],
          fromCellCode: [
            {required: true, message: '请选择原始货位', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '请选择目标货位', trigger: 'blur'}
          ],
          orderType: [
            {required: true, message: '请选择订单类型', trigger: 'blur'}
          ],
          packLevel: [
            {required: true, message: '请选择包装级别', trigger: 'blur'}
          ],
          abcLevel: [
            {required: true, message: '请选择产品循环级别', trigger: 'blur'}
          ],
          canItemMax: [
            {required: true, message: '请输入混放产品最大值', trigger: 'blur'}
          ],
          canBatchMax: [
            {required: true, message: '请输入混放批号最大值', trigger: 'blur'}
          ],
          cellType: [
            {required: true, message: '请选择货位类型', trigger: 'blur'}
          ],
          cellUseType: [
            {required: true, message: '请选择货位使用类型', trigger: 'blur'}
          ],
          cellState: [
            {required: true, message: '请选择货位状态', trigger: 'blur'}
          ],
          cellPickType: [
            {required: true, message: '请选择货位拣货方式', trigger: 'blur'}
          ],
          cellAbcLevel: [
            {required: true, message: '请选择货位周转', trigger: 'blur'}
          ],
          shelfInfoId: [
            {required: true, message: '请选择货位组', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      ...mapState([
        
        "cellAbcLevels",
        "cellPickTypes",

        "upperTacticTypes",
        "orderTypes",
        "packLevels",
        "abcLevels",
        "cellTypes",
        "cellStates",
      ]),
    },
    methods: {
      
      upDetail(index){
        var that =this;
        if(index==0){
             return
        }
        
       var aaa =  that.upperTacticDetails[index];
       var bbb= that.upperTacticDetails[index-1];
       if(bbb.orderNo ==aaa.orderNo && bbb.orderNo==0){
          bbb.orderNo+=1;
          aaa.orderNo-=1;
       }
       if(bbb.orderNo ==aaa.orderNo && bbb.orderNo==1){
          bbb.orderNo+=1; 
       }
       if(bbb.orderNo !=aaa.orderNo ){
         var ccc = bbb.orderNo;
         bbb.orderNo =aaa.orderNo ;
         aaa.orderNo = ccc; 
       }
       aaa.upperTacticName=null;
       bbb.upperTacticName=null;
       IOT.getServerData('/upper/tactic/details/update', 'post', aaa, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                that.getUpperTacticDetailData(that.lookRowData.upperTacticId)
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
       });
       IOT.getServerData('/upper/tactic/details/update', 'post', bbb, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                that.getUpperTacticDetailData(that.lookRowData.upperTacticId)
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
       });

        
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getUpperTacticData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getUpperTacticData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getUpperTacticDetailData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      getUpperTacticData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/upper/tactics/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.upperTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getUpperTacticDetailData(id) {
        this.searchData2.upperTacticId = id
        let that = this;
        that.loading.masterTable = true;
        let searchData2 = PF.JSON(that.searchData2);
        IOT.getServerData('/upper/tactic/details/list', 'get', searchData2, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            that.upperTacticDetails = ret.rows;
            that.searchData2.total = ret.total;
            that.searchData2.pageNum = ret.pageNumber
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
      getAreaData() {
        IOT.getServerData('/area/infos/list', 'get', {wareId:this.wareId}, (ret) => {
          if (ret.code === 200) {
            this.areas = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getShelfData() {
        IOT.getServerData('/shelf/infos/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.shelfs = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData() {
        IOT.getServerData('/cell/infos/list', 'get', {wareId:this.wareId}, (ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row) {
        this.lookRowData = JSON.parse(JSON.stringify(row))
        this.getUpperTacticDetailData(this.lookRowData.upperTacticId)
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/upper/tactics/insert', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getUpperTacticData()
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
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/upper/tactics/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getUpperTacticData()
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
      showUpdateModal(row) {
        this.rowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form');
        this.dialogNewVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData');
        this.dialogUpdateVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseDetail(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('detailForm');
        this.dialogNewDetailVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleCloseDetail2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('detailRowData');
        this.dialogUpdateDetailVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteUpperTactic(id)
          })
          .catch(_ => {
          });
      },
      isDeleteDetail(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteDetail(id)
          })
          .catch(_ => {
          });
      },
      deleteUpperTactic(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/upper/tactics/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getUpperTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteDetail(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/upper/tactic/details/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getUpperTacticDetailData(that.lookRowData.upperTacticId)
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showDetailModal() {
        
        this.detailForm.upperTacticId = this.lookRowData.upperTacticId;
        
      },
      commitDetailForm(formName) {
        let that = this;
        let rules = this.rulesDetail;
        rules.toAreaCode[0].required = true;
        rules.fromCellCode[0].required = true;
        rules.toCellCode[0].required = true;
        let upperTacticType = this.detailForm.upperTacticType;
        if (upperTacticType === 1) {
          rules.toAreaCode[0].required = false;
        } else if (upperTacticType === 3) {
          rules.fromCellCode[0].required = false;
        } else if (upperTacticType === 2) {
          rules.toCellCode[0].required = false;
        }
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/upper/tactic/details/insert', 'post', this.detailForm, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getUpperTacticDetailData(that.lookRowData.upperTacticId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('detailForm')
            this.dialogNewDetailVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
        rules.toAreaCode[0].required = true;
        rules.fromCellCode[0].required = true;
        rules.toCellCode[0].required = true;
      },
      updateDetailForm(formName) {
        let that = this;
        let rules = this.rulesDetail2;
        let upperTacticType = this.detailRowData.upperTacticType;
        rules.toAreaCode[0].required = true;
        rules.fromCellCode[0].required = true;
        rules.toCellCode[0].required = true;
        if (upperTacticType === 1) {
          rules.toAreaCode[0].required = false;
        } else if (upperTacticType === 3) {
          rules.fromCellCode[0].required = false;
        } else if (upperTacticType === 2) {
          rules.toCellCode[0].required = false;
        }
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/upper/tactic/details/update', 'post', this.detailRowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getUpperTacticDetailData(that.lookRowData.upperTacticId)
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('detailRowData')
            this.dialogUpdateDetailVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
        rules.toAreaCode[0].required = true;
        rules.fromCellCode[0].required = true;
        rules.toCellCode[0].required = true;
      },
      showUpdateDetailModal(row) {
        this.detailRowData = JSON.parse(JSON.stringify(row));
        this.dialogUpdateDetailVisible = true
      },
    },
    created() {
      this.getUpperTacticData();
      this.getWareData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      this.getAreaData();
      this.getCellData();
      this.getShelfData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">

  .upperTactic {
    .el-table th, .el-table tr {
      background-color: aqua;
    } 

  }
  .deerTableHeader{
     background-color: aqua;
  }
</style>
