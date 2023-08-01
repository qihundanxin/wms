<template>
  <div class="acceptRecord">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>入库单明细号</span>
          <el-input v-model="searchData.asnDetailNo" size="mini" placeholder="入库单明细号" clearable></el-input>
        </div>
        <div>
          <span>采购单明细号</span>
          <el-input v-model="searchData.requestDetailDetailNo" size="mini" placeholder="采购单明细号" clearable></el-input>
        </div>
        <div>
          <span>SKU名称</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>关键字搜索</span>
          <el-input style="width:255px" v-model="searchData.keyWords" size="mini" title= "批次名称/验收人/追踪号/货位编码/包装" placeholder="批次名称/验收人/追踪号/货位编码/包装" clearable></el-input>
        </div>
        <div>
          <span>收货方式</span>
          <el-select v-model="searchData.billType" size="mini" filterable placeholder="请选择收货方式" clearable>
            <el-option :key="2" label="快速验收" :value="2">快速验收</el-option>
            <el-option :key="3" label="盲 收" :value="3">盲 收</el-option>
            <el-option :key="6" label="异常验收" :value="6">异常验收</el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary"    icon="iconfont icon-xinzeng" @click="showNewModal">盲收</el-button>
      <!-- <el-dropdown style="margin-left:10px;margin-right:10px"  size="small"   type="primary"    icon="iconfont icon-print"  @click="demoPrint()" split-button  >
        打印
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item> 

        </el-dropdown-menu>
      </el-dropdown> -->

      <!-- <button @click="printTest">1111111111111</button> -->
 

    </div>

    <el-dialog title="新增盲收" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{form.wareName}}</span>

          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.organizationId"   filterable placeholder="请选择货主" style="width: 90%;">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <el-input
              :disabled="form.organizationId === null || form.organizationId === undefined || form.organizationId === ''"
              :title="form.itemCode" v-model="form.itemCode" placeholder="请输入SKU" style="width: 80%;"></el-input>
            <el-button @click="showSelectItemModal" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </el-form-item>
          <el-form-item label="单位" prop="toPackDetailId" :label-width="formLabelWidth">
            <el-select :disabled="form.itemCode === null || form.itemCode === undefined || form.itemCode === ''"
                       v-model="form.toPackDetailId" filterable placeholder="请选择单位" @change="changePackDetailId" style="width: 90%;">
              <el-option
                v-for="item in packDetails"
                :key="item.packDetailId"
                :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数量" prop="toQuantity" :label-width="formLabelWidth">
            <input type="number" class="my_input" v-model.number="form.toQuantity" :disabled="form.itemCode === null || form.itemCode === undefined || form.itemCode === '' || form.toPackDetailId === null || form.toPackDetailId === undefined || form.toPackDetailId === ''" placeholder="请输入数量" style="width: 80%;">
<!--            <el-input-number :disabled="form.itemCode === null || form.itemCode === undefined || form.itemCode === '' || form.toPackDetailId === null || form.toPackDetailId === undefined || form.toPackDetailId === ''" v-model="form.toQuantity" controls-position="right" :min="0" label="请输入数量" style="width: 90%;"></el-input-number>-->
          </el-form-item>
          <el-form-item label="货位编码" prop="toCellCode" :label-width="formLabelWidth">
            <el-input :disabled="form.wareId === null || form.wareId === undefined || form.wareId === ''"
                      :title="form.toCellCode" v-model="form.toCellCode" placeholder="请输入编码"
                      style="width: 80%;"></el-input>
            <el-button @click="showSelectCellModal" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>
          </el-form-item>
          <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
            <el-input v-model="form.boxCode" placeholder="请输入追踪号" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="序列号"  :label-width="formLabelWidth" class="checkItemBoxHeight">
            <el-checkbox  v-model="serialNoBoolean"></el-checkbox>
          </el-form-item>
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">批次策略</el-divider>
          </div>
          <div v-if="batchTactics.batchTacticDetails === undefined
          || batchTactics.batchTacticDetails === null
          || batchTactics.batchTacticDetails === []"
               style="float: left;width: 100%;line-height: 60px;color: #909399;text-align: center;">请选择商品
          </div>
          <el-form-item v-for="(item,index) in batchTactics.batchTacticDetails" :key="index" :label="item.detailName" :prop="item.detailCode"
                        :label-width="formLabelWidth">
            <el-select v-if="item.detailCode === 'qcState'" v-model="form[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in qcStates"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>

            <el-input v-else-if="item.format === 0" v-model="form[item.detailCode]" :placeholder="'请输入'+item.detailName"
                      style="width: 90%;"></el-input>
            <input v-else-if="item.format === 1" type="number" class="my_input" v-model.number="form[item.detailCode]" placeholder="请输入" style="width: 80%;">
<!--            <el-input-number v-else-if="item.format === 1" v-model="form[item.detailCode]" controls-position="right" :min="0" label="请输入" style="width: 90%;"></el-input-number>-->
            <el-date-picker v-else-if="item.format === 2" v-model="form[item.detailCode]" value-format="yyyy-MM-dd"
                            type="date" :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-date-picker>
            <el-time-picker v-else-if="item.format === 3" v-model="form[item.detailCode]" value-format="HH:mm:ss"
                            :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-time-picker>
            <el-select v-else-if="item.format === 4" v-model="form[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in item.values"
                :key="item2.batchTacticDetailDetailValue"
                :label="item2.batchTacticDetailDetailValue"
                :value="item2.batchTacticDetailDetailValue">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- 根据数量生成输入框
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">序列号</el-divider>
          </div>
          <div v-if="form.toQuantity === undefined || form.toQuantity === null || form.toQuantity === 0"
               style="float: left;width: 100%;line-height: 60px;color: #909399;text-align: center;">请输入数量
          </div>
          <el-form-item v-for="i in form.toQuantity" :label="'序列号'+i"  :label-width="formLabelWidth">
            <el-input v-model="form.serialNos[i]" placeholder="请输入序列号" style="width: 80%;"></el-input>
          </el-form-item>-->
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button style="margin-right: 550px;" type="primary" @click="showSerialNo()" v-if="serialNoBoolean">序列号({{form.serialNos.length}})</el-button>
        <el-button @click="resetForm('form');batchTactics = {};dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
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
              <el-input v-model="searchSelectItem.keyWords" size="mini" placeholder="名称/规格/型号/包装/单价等"
                        clearable></el-input>
            </div>
            <div>
              <span>商品种类</span>
              <el-cascader
                size="mini"
                placeholder="请选择商品种类"
                v-model="searchSelectItem.itemTypeId"
                :options="itemTypeTree"
                filterable></el-cascader>
            </div>
            <div>
              <span>供应商</span>
              <el-select v-model="searchSelectItem.supplierId" size="mini" filterable placeholder="请选择供应商"
                         style="width: 70%;">
                <el-option
                  v-for="item in suppliers"
                  :key="item.supplierId"
                  :label="item.supplierName"
                  :value="item.supplierId">
                </el-option>
              </el-select>
            </div>
            <!--<div>
              <span>货主</span>
              <el-select v-model="searchSelectItem.organizationId" size="mini" filterable placeholder="请选择货主" style="width: 70%;">
                <el-option
                  v-for="item in organizations"
                  :key="item.organizationId"
                  :label="item.organizationName"
                  :value="item.organizationId">
                </el-option>
              </el-select>
            </div>-->
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
              type="index"
              width="50">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              fixed="left"
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
              prop="itemTypeName"
              label="商品种类"
              width="180">
            </el-table-column>
           
            <el-table-column
              show-overflow-tooltip
              prop="spec"
              label="规格"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="model"
              label="型号"
              width="180">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="packName"
              label="包装编码"
              width="180">
            </el-table-column>
            
            <el-table-column
              fixed="right"
              label="添加"
              width="50">
              <template slot-scope="scope">
                <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain
                           size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="text-align: right;"
            @current-change="handleCurrentChange2"
            :current-page.sync="searchSelectItem.pageNum"
            :page-size="searchSelectItem.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="searchSelectItem.total">
          </el-pagination>
        </div>

      </el-dialog>
      <el-dialog width="800px" title="选择货位" :visible.sync="innerVisibleCell" append-to-body style="margin-top: -10vh;">

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
              type="index"
              width="50">
            </el-table-column>
            <el-table-column
                fixed="left"
              show-overflow-tooltip
              prop="cellCode"
              label="编码"
              width="150">
            </el-table-column>
            <el-table-column
                show-overflow-tooltip
                prop="cellName"
                label="名称"
                width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="shelfName"
              label="所属货位组"
              width="150">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="cellUseType"
              label="货位使用类型"
              width="150">
              <template slot-scope="scope">
                <span v-for="(item,index) in cellUseTypes" :key="index" v-if="item.value === scope.row.cellUseType">{{item.label}}</span>
              </template>
            </el-table-column>
        
            <el-table-column
              fixed="right"
              label="操作"
              width="110">
              <template slot-scope="scope">
                <el-button @click="selectCell(scope.row.cellCode)" icon="iconfont icon-jia1" type="primary" circle plain
                           size="small"></el-button>
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
    </el-dialog>
    <!-- 序列号 -->
    <el-dialog title="序列号" :visible.sync="serialNo" :before-close="handleCloseSerialNo" width="900px"
               style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="扫描输入框"  :label-width="formLabelWidth">
            <el-input v-model="scanner" id="scanner" @change="addSerialNo()" placeholder="请扫描序列号" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="affirmSerialNo()" :disabled="select">确 定</el-button>
          </el-form-item>
          <el-form-item label="是否自动提交">
            <el-switch
                    v-model="select"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-text="是"
                    inactive-text="否"
                    :active-value="true"
                    :inactive-value="false"
            >
            </el-switch>
          </el-form-item>
          <el-form-item>
            <el-progress :text-inside="true" :stroke-width="24" :format="format"
                         :percentage="((parseFloat(form.serialNos.length)/parseFloat(Math.ceil(form.toQuantity)))*100).toFixed(1)" ></el-progress>
          </el-form-item>
          <!-- 根据数量生成输入框 -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left">序列号</el-divider>
          </div>
<!--          <div v-if="form.toQuantity === undefined || form.toQuantity === null || form.toQuantity === 0"-->
<!--               style="float: left;width: 100%;line-height: 60px;color: #909399;text-align: center;">请输入数量-->
<!--          </div>-->

          <el-tag
                  v-for="(item,i) in form.serialNos"
                  :key="i"
                  closable
                  @close="handleClose2(item)"
                  :style="i===0?'':'margin-left: 10px;margin-top:10px;'">
            {{item}}
          </el-tag>
<!--          <el-form-item v-for="i in form.toQuantity" :label="'序列号'+i"  :label-width="formLabelWidth">-->
<!--            <el-input v-model="form.serialNos[i]" placeholder="请扫描" style="width: 80%;" disabled></el-input>-->
<!--          </el-form-item>-->
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button style="margin-right: 550px;" type="primary" @click="showImportExcel()" >导入</el-button>
        <el-button @click="handleCloseSerialNo()">取 消</el-button>
        <el-button type="primary" @click="serialNo = false">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导入" :visible.sync="importExcel" :before-close="handleCloseImportExcel" width="600px"
               style="margin-top: -10vh;">
      <div class="cellModalBox">
        <el-form class="clearFloat"  ref="form">
          <el-form-item   :label-width="formLabelWidth1">
            <el-button size="small" type="primary"    icon="iconfont icon-xiazai"  @click="downloadModel()"> 下载模板</el-button>
          </el-form-item>
          <el-form-item  label="文件" :label-width="formLabelWidth">
            <input type="file" ref="upload" accept=".xls,.xlsx" @change="changeExcel">
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloseImportExcel()">关 闭</el-button>
        <el-button @click="clearExcel()">清 空</el-button>
      </div>
    </el-dialog>
    <div class="tableBox">
      <el-table
      v-loading="loading.masterTable"
        highlight-current-row
        :data="acceptRecords"
        @sort-change="sortChange"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
       <!-- <el-table-column
                show-overflow-tooltip
                prop="billType"
                label="验收类型"
                width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.billType === 1" type="success">采 购</span>
            <span v-else-if="scope.row.billType === 2" type="">asn</span>
            <span v-else-if="scope.row.billType === 3" type="info">盲 收</span>
          </template>
        </el-table-column>-->
         <el-table-column
                show-overflow-tooltip
                prop="state"
                label="状态"
                width="80">
                <template slot-scope="scope">
                  <span v-if="scope.row.state==0"   style="color:#409EFF" >已验收</span>
                  <span v-if="scope.row.state==1" style="color:#F56C6C" >同步失败</span>
                  <span v-if="scope.row.state==2"  style="color:#67C23A" >同步成功</span>
                </template>
         
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="state"
                label="收货方式"
                width="100">
                <template slot-scope="scope">
                  <span v-if="scope.row.billType==2"   style="color:#409EFF" >快速验收</span>
                  <span v-if="scope.row.billType==3" style="color:#909399" >盲收</span>
                  <span v-if="scope.row.billType==6"  style="color:#F56C6C" >异常验收</span>
                </template> 
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="organizationName"
                label="货主"
                width="110">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="itemName"
                label="SKU名称"
                width="160">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="itemCode"
                label="SKU"
                width="130">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="batchName"
                label="批次"
                width="100">
        </el-table-column>
        <el-table-column
               
                show-overflow-tooltip
                prop="quantity"
                label="数量"
                width="80">
                 <!-- <template slot-scope="scope">
                  <span >{{scope.row.quantity}}（主单位）</span>
                </template> -->
        </el-table-column>
        <!-- <el-table-column
              
                show-overflow-tooltip
                prop="packDescribe"
                label="验收包装"
                width="80">
        </el-table-column> -->
        <el-table-column
                show-overflow-tooltip
                prop="cellCode"
                label="货位编码"
                width="120">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="boxCode"
                label="追踪号"
                width="70">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="acceptUserName"
          label="验收人"
          width="100">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.acceptUserId, 'account')" type="text">
              {{scope.row.acceptUserName}}
            </span>
          </template>
        </el-table-column>
        <el-table-column
                sortable="custom"
                show-overflow-tooltip
                prop="acceptTime"
                label="收货时间"
                width="160">
          <template slot-scope="scope">
            {{scope.row.acceptTime.substr(0,16)}}
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="requestDetailDetailNo"
                label="采购单明细号"
                width="120">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="asnDetailNo"
                label="入库单明细号"
                width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: {billNo: scope.row.asnBillNo}})">{{scope.row.asnDetailNo}}</span>
          </template>
        </el-table-column>

         <el-table-column
         fixed="right"
                show-overflow-tooltip
                prop=" "
                label="操作" 
                min-width="130">
          <template slot-scope="scope">
             <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint(scope.row.acceptRecordId)" split-button  >
        打印
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(scope.row.acceptRecordId,1)">打印预览1</el-button></el-dropdown-item> 
        
          <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(scope.row.acceptRecordId,2)">打印预览2</el-button></el-dropdown-item> 

        </el-dropdown-menu>
      </el-dropdown>
          </template>
        </el-table-column>





        
        
      </el-table>
      <el-pagination
       class="pagination-bottom"
        style="text-align: right;"
        @size-change="handleSizeChange"      
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-sizes="[15, 30, 50, 100]"
        :page-size="searchData.pageSize"
        layout="sizes,total, prev, pager, next"
        :total="searchData.total">
      </el-pagination>


    
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'AcceptRecord',
    data() {
      let validateItemCode = (rule, value, callback) => {
        this.validator(rule, value, callback, 'item_info', 'item_code', this.form.itemCode, 0);
      };
      let validateCellCode = (rule, value, callback) => {
        this.validator(rule, value, callback, 'cell_info', 'cell_code', this.form.toCellCode, 0);
      };
      return {
        loading:{
            masterTable:true,
        },

        batchTacticCode:'',
        isMoreSearch: false,
        isMoreSearch2: false,
        isMoreSearch3: false,

        serialNoBoolean:false,
        serialNoNum:0,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          itemCode:'',
          itemName:'',
          organizationId: null,
          asnDetailNo:'',
          requestDetailDetailNo:'',
          billType:'',
          sort:null

        },
        searchSelectItem: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          itemTypeId: '',
          supplierId: '',
          organizationId: '',
        },
        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseTypes: { 0: 0},
        },

        acceptRecords: [],
        wares: [],
        organizations: [],
        items: [],
        cells: [],
        itemTypeTree: [],
        suppliers: [],
        areas: [],
        batchTactics: {},
        packDetails: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        serialNo:false,
        innerVisibleItem: false,
        innerVisibleCell: false,

        scanner:'',
        select:true,

        importExcel:false,

        form: {
          wareId: PF.getLocal('wareId', 'number'),
          wareName: PF.getLocal('wareName'),
          organizationId: null,
          organizationName: '',
          toPackDetailId: null,
          toPackDescribe: '',
          toTransRatio: null,
          itemCode: '',
          itemName: '',
          packCode: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: null,
          billType: 3,
          serialNos:[],

          'productDate': '',
          'exDate': '',
          'inDate': '',
          'supplierCode': '',
          'supplierBatch': '',
          'qcState': '',
          'costPrice': null,
          'salePrice': null,
          'detailNo': '',
          'memo1': '',
          'memo2': '',
          'memo3': '',
          'batchAttribute1': '',
          'batchAttribute2': '',
        },
        rules: {
          wareId: [
            {required: true, message: '请选择仓库', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '请选择单位', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '请输入SKU', trigger: 'blur'},
            {validator: validateItemCode, trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '请输入货位编码', trigger: 'blur'},
            {validator: validateCellCode, trigger: 'blur'}
          ],
          toBoxCode: [
            {required: true, message: '请输入追踪号', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            // {type: 'number', message: '数量必须为数字值', trigger: ['blur', 'change']}
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
        formLabelWidth: '120px',

        formLabelWidth1: '30px',
      }
    },
    computed: {
      ...mapState([
        "cellUseTypes",
        "cellAbcLevels",
        "cellPickTypes",
        "qcStates",
      ]),
      toPackDetailId() {
        return this.form.toPackDetailId
      },
      wareId() {
        return this.form.wareId
      },
      organizationId() {
        return this.form.organizationId
      },
      itemCode() {
        return this.form.itemCode
      },
    },
    watch: {

      toPackDetailId(val) {
        for (let i = 0; i < this.packDetails.length; i++) {
          if (this.packDetails[i].packDetailId === val) {
            this.form.toPackDescribe = this.packDetails[i].packDescribe;
          }
        }
      },
      wareId(val) {
        for (let i = 0; i < this.wares.length; i++) {
          if (this.wares[i].wareId === val) {
            this.form.wareName = this.wares[i].wareName;
          }
        }
      },
      organizationId(val) {
        for (let i = 0; i < this.organizations.length; i++) {
          if (this.organizations[i].organizationId === val) {
            this.form.organizationName = this.organizations[i].organizationName;

            this.batchTacticCode = this.organizations[i].batchTacticCode;

          }
        }
      },
      itemCode(val) {
        for (let i = 0; i < this.items.length; i++) {
          if (this.items[i].itemCode === val) {
            this.form.itemName = this.items[i].itemName;
          }
        }
      },
    },
    methods: {
        sortChange(column){
        if(column.order=="ascending"){
          this.searchData.sort="ASC";
        }else{
          this.searchData.sort="DESC"
        }
        this.getAcceptRecordData();
      },
      printTest(){
        var urlStr = 'http://lazerp2.oss-cn-hangzhou.aliyuncs.com/label/ztoth/77130044426681.pdf';
        this.$parent.printUrl(urlStr);
      },
    
       //打印
      demoPrint:function (acceptRecordId,isReview){
        var that = this;
         
         IOT.getServerData('/accept/records/acceptPrint', 'get', {acceptRecordId:acceptRecordId}, (ret) => {
          if (ret.code === 200) { 
                var strHTML= ret.data;
                 
                that.$parent.printHtml(strHTML,isReview);
                
            
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
       
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getAcceptRecordData();
      },
      changeExcel(val){
        PF.readExcel(val, (data)=>{
          let a = data;
          
          var hash = {};
          if(a.length<=0){
            IOT.tips('请在表格中填写序列号！', 'error');
          }else{
            if(parseFloat(this.form.serialNos.length)+parseFloat(a.length) > Math.ceil(this.form.toQuantity)){
              IOT.tips('序列号超过录入数量，请核对后重新上传！', 'error');
              this.clearExcel();
              return false;
            }
            for(let i=0;i<a.length;i++){
              if(hash[a[i].serialNo]) {
                IOT.tips('序列号'+a[i].serialNo+'重复，请修改后重新上传！', 'error');
                return false;
             }
             // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
              hash[a[i].serialNo] = true;
            }
            if(this.form.serialNos.length>0){
             for(let j=0;j<this.form.serialNos.length;j++){
                for(let k=0;k<a.length;k++){
                  if(this.form.serialNos[j] === (a[k].serialNo).toString()){
                    IOT.tips('序列号'+a[k].serialNo+'与已录入序列号重复，请修改后重新上传！', 'error');
                    return false;
                  }
               }
              }
              for(let i=0;i<a.length;i++){
                this.form.serialNos.push(a[i].serialNo.toString());
              }
              this.handleCloseImportExcel();
            }else{
              for(let i=0;i<a.length;i++){
                this.form.serialNos.push(a[i].serialNo.toString());
              }
              this.handleCloseImportExcel();
            }
          }
        });
      },
      clearExcel(){
        this.$refs.upload.value='';
      },
      handleCloseImportExcel(){
        this.importExcel = false;
        this.$refs.upload.value='';
      },
      downloadModel(){
        window.open('/model/serialNosImportModel.xls');
      },
      showImportExcel(){
        this.importExcel = true;
      },
      format(percentage) {
        return percentage === 100.0 ? '满' : `${percentage}%`;
      },
      changeOrg(value){
        var orgId = value;
        var orgs = this.organizations;
        var org = null;
        for(var i =0;i<orgs.length;i++){
          if(orgs[i].orgnizationId ==orgId){
            org =orgs[i];
          }
        }
        this.batchTacticCode = org.batchTacticCode;

      },

      addSerialNo(){
        if(this.select){
          this.changeSerialNos();
        }
      },
      affirmSerialNo(){
        this.changeSerialNos();
      },
      handleClose2(tag) {
        this.form.serialNos.splice(this.form.serialNos.indexOf(tag), 1);
      },
      changeSerialNos(){
        let scanner = this.scanner.trim();
        if(!PF.isNull(scanner)){
          IOT.tips('输入框为空！', 'error');
        }else if(this.form.serialNos.length < this.form.toQuantity){
          for(let i=0;i<this.form.serialNos.length;i++){
            
            if(scanner === this.form.serialNos[i]){
              IOT.tips('此序列号已录入！', 'error');
              this.scanner = '';
              document.querySelector('#scanner').focus();
              return false;
            }
          }
          this.form.serialNos.push(scanner);
          this.scanner = '';
          document.querySelector('#scanner').focus();
        }else{
          IOT.tips('所有数量都已录入,请增加数量！', 'error');
          this.scanner = '';
        }
        /*if(this.form.toQuantity < this.form.serialNos.length){
          IOT.tips('序列号已录入完成!', 'error', 1000, () => {
            this.scanner = '';
            let pFocus = document.getElementById("scanner");
            pFocus.focus();
          });
          return false;
        }
        for(let i= 1;i<=this.form.toQuantity;i++){
          let serial = this.form.serialNos[i];
          if(serial === null || serial === undefined || serial === ''){
            this.form.serialNos[i] = this.scanner;
            this.scanner = '';
            let pFocus = document.getElementById("scanner");
            pFocus.focus();
            break;
          }
        }*/
      },
      handleCloseSerialNo(){
        this.serialNo = false;
        this.scanner = '';
      },
      showSerialNo(){
        if(!PF.isNull(this.form.toQuantity,null)){
          IOT.tips('请先录入数量！', 'error');
        }else{
          this.serialNo = true;
          let pFocus = document.getElementById("scanner");
          pFocus.focus();
        }
      },
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getAcceptRecordData()
      },
      handleCurrentChange2(val) {
        this.searchSelectItem.pageNum = val;
        this.getItemData()
      },
      handleCurrentChange3(val) {
        this.searchSelectCell.pageNum = val;
        this.getCellData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.itemCode = '';
        this.searchData.itemName = '';
        this.searchData.organizationId = '';
        this.searchData.asnDetailNo = '';
        this.searchData.requestDetailDetailNo = '';
        this.searchData.billType = '';
      },
      clearSearchSelectItem() {
        this.searchSelectItem.keyWords = '';
        this.searchSelectItem.itemTypeId = '';
        this.searchSelectItem.supplierId = '';
        this.searchSelectItem.organizationId = '';
      },
      clearSearchSelectCell() {
        this.searchSelectCell.keyWords = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getAcceptRecordData();
      },
      getAcceptRecordData() {
        var that = this;
        that.acceptRecords =[];
        that.loading.masterTable  =true;
        IOT.getServerData('/accept/records/list', 'get', that.searchData, (ret) => {
          that.loading.masterTable  =false;
          if (ret.code === 200) {
            that.acceptRecords = ret.rows;
            that.searchData.total = ret.total;
            that.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getWareData() {
        IOT.getServerData('/ware/infos/findSome', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPackDetailData(packCode) {
        IOT.getServerData('/pack/details/list', 'get', {packCode: packCode}, (ret) => {
          if (ret.code === 200) {
            this.packDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getItemData() {
        let searchSelectItem = JSON.parse(JSON.stringify(this.searchSelectItem))
        searchSelectItem.itemTypeId = searchSelectItem.itemTypeId[searchSelectItem.itemTypeId.length - 1]
        IOT.getServerData('/item/infos/list', 'get', searchSelectItem, (ret) => {
          if (ret.code === 200) {
            this.items = ret.rows;
            this.searchSelectItem.total = ret.total;
            this.searchSelectItem.pageNum = ret.pageNumber
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
      getItemTypeData() {
        IOT.getServerData('/item/types/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for (let i = 0; i < list.length; i++) {
              list[i].value = list[i].itemTypeId;
              list[i].id = list[i].itemTypeId;
              list[i].label = list[i].itemTypeName;
            }
            // this.itemTypes = list;
            this.itemTypeTree = PF.toTree(list);
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
      getBatchTacticData(batchTacticCode) {
        if(batchTacticCode==null || batchTacticCode==''){
          batchTacticCode = this.batchTacticCode;
        }
        IOT.getServerData('/batch/tactics/findByBatchTacticCode', 'get', {
           batchTacticCode:batchTacticCode
        }, (ret) => {
          if (ret.code === 200) {
            let list = PF.JSON(ret.rows[0])
            let detailDetails = list.batchTacticDetailDetails;
            let testDetailDetails = [];
            list.batchTacticDetails[12].values = [];
            list.batchTacticDetails[13].values = [];
            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let i = 0; i < batchTacticDetails.length; i++) {
              if (batchTacticDetails[i].state === 1) {
                testBatchTacticDetails.push(batchTacticDetails[i])
                if (batchTacticDetails[i].isRequired === 1) {
                  this.rules[batchTacticDetails[i].detailCode][0].required = true;
                }
                if (batchTacticDetails[i].detailCode === 'inDate') {
                  this.form[batchTacticDetails[i].detailCode] = PF.getNowFormatDate();
                }
              }
            }

            list.batchTacticDetailDetails = testDetailDetails;
            list.batchTacticDetails = testBatchTacticDetails;
            this.batchTactics = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        this.resetForm('form');
        this.batchTactics = {};
        this.dialogNewVisible = false;
      },
      showNewModal() {
       // this.getWareData();
        PF.getOrganizations((rows) => {
          this.organizations = rows;
        });
        this.dialogNewVisible = true
      },
      showSelectItemModal() {
        if (!PF.isNull(this.form.organizationId)) {
          IOT.tips('未选择货主！', 'error');
          return false;
        }
        this.searchSelectItem.organizationId = this.form.organizationId;
        this.getItemData();
        this.getItemTypeData();

        this.innerVisibleItem = true;
      },
      showSelectCellModal() {
        if (!PF.isNull(this.form.wareId)) {
          IOT.tips('未选择仓库！', 'error');
          return false;
        }
        this.searchSelectCell.wareId = this.form.wareId;
        this.getCellData();
        this.getAreaData();
        this.innerVisibleCell = true;
      },
      selectItem(row) {
        console.log(this.form);
        this.form.itemCode = row.itemCode;
        this.getPackDetailData(row.packCode);
        this.getBatchTacticData(row.batchTacticCode);
        this.innerVisibleItem = false;
      },
      selectCell(cellCode) {
        this.form.toCellCode = cellCode;
        this.innerVisibleCell = false;
      },
      /*requiredFalse(){
        let arr = ['productDate', 'exDate', 'inDate', 'supplierCode', 'supplierBatch', 'qcState', 'costPrice', 'salePrice', 'detailNo', 'memo1', 'memo2', 'memo3', 'batchAttribute1', 'batchAttribute2',];
        for(let i=0;i<arr.length;i++){
          this.rules[arr[i]][0].required = false;
        }
      },*/
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let a = Math.ceil(that.form.toQuantity);
            if(that.form.serialNos.length <= 0) {
              if (that.serialNoBoolean) {
                this.$confirm('当前验收未录入序列号,会影响后续序列号追踪,是否继续？')
                      .then(_ => {
                        this.commitform(formName);
                      })
                      .catch(_ => {
                        IOT.hideOverlay();
                      });
              }else{
                this.commitform(formName);
              }
            }else if(that.form.serialNos.length > 0 && that.form.serialNos.length < a){
              if (that.serialNoBoolean) {
                this.$confirm('当前验收数量与录入序列号不一致,会影响后续序列号追踪,是否继续？')
                        .then(_ => {
                          this.commitform(formName);
                        })
                        .catch(_ => {
                          IOT.hideOverlay();
                        });
              }else{
                this.commitform(formName);
              }
            }else{
              this.commitform(formName);
            }
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      commitform(formName){
        var that = this;
        this.form.toQuantity = Number(this.form.toQuantity);
        for(let i=1;i<that.form.serialNos.length;i++){
          let serialNo = that.form.serialNos[i];
          if(serialNo === null || serialNo === undefined || serialNo === ''){
            IOT.tips('请输入序列号!', 'error', 1000, () => {
              IOT.hideOverlay();
            });
            return false;
          }
        }
        console.log(this.form);
        let form = PF.JSON(this.form); 
        IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: [form]}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              that.getAcceptRecordData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.serialNoBoolean = false;
        this.resetForm(formName);
        this.batchTactics = {};
        this.dialogNewVisible = false
      },
      validator(rule, value, callback, tableName, fieldName, fieldValue, type, idValue) {
        let form = {
          tableName: tableName, // 表名
          fieldName: fieldName, // 字段名
          fieldValue: fieldValue, // 字段值
          type: type, // 0-新增 1-更新
          idValue: idValue, // type为0时不传，type为1是必传，id值
        };

        IOT.getServerData('/validator', 'post', form, (ret) => {
          if (ret.code === 100001 || ret.code === 100002 || ret.code === 100004) {
            callback();
          } else if (ret.code === 100003) {
            callback(new Error('不存在'));
          } else {
            PF.publicMessage(ret.message || '服务器请求失败，稍后再试！', 'error');
            callback();
          }
        });
      },
      // 2424

      changePackDetailId(){
        let form = PF.JSON(this.form);
        let packDetails = PF.JSON(this.packDetails);
        for(let i=0;i<packDetails.length;i++){
          if(packDetails[i].packDetailId === form.toPackDetailId){
            this.form.toPackDescribe = packDetails[i].packDescribe;
            this.form.toTransRatio = packDetails[i].transRatio;
          }
        }
      },

    },
    created() {
      this.getAcceptRecordData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
        PF.getSuppliers((rows) => {
            this.suppliers = rows;
        });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .acceptRecord {

  }
</style>
