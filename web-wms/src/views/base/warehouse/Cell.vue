<template>
  <div class="cell">
    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="编码" clearable></el-input>
        </div>
        <div>
          <span>货位组</span>
          <el-select v-model="searchData.shelfInfoId" size="mini" filterable placeholder="请选择货位组" clearable>
            <el-option
                v-for="item in shelfs"
                :key="item.shelfInfoId"
                :label="item.shelfCode"
                :value="item.shelfInfoId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货位使用类型</span>
          <el-select class="cellUseType" v-model="searchData.cellUseType" size="mini" filterable placeholder="请选择货位使用类型" clearable>
            <el-option
                v-for="item in cellUseTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </div>
        <!-- <div>
          <span>周转需求</span>
          <el-select v-model="searchData.cellAbcLevel" size="mini" filterable placeholder="请选择周转需求" clearable>
            <el-option
                v-for="item in cellAbcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </div> -->
        <!-- <div>
          <span>拣货方式</span>
          <el-select v-model="searchData.cellPickType" size="mini" filterable placeholder="请选择拣货方式" clearable>
            <el-option
              v-for="item in cellPickTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div> -->
        <!-- <div>
          <span>X坐标</span>
          <el-input v-model="searchData.xCoordinate" size="mini" placeholder="X坐标" clearable></el-input>
        </div>
        <div>
          <span>Y坐标</span>
          <el-input v-model="searchData.yCoordinate" size="mini" placeholder="Y坐标" clearable></el-input>
        </div> -->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng" @click="showNewModal">新增
      </el-button>
      <el-button size="mini" type="primary" :disabled="!this.selectCells.length>0" round icon="iconfont icon-print" @click="printCells">批量打印
      </el-button>
      <el-button size="mini" type="primary" :disabled="!this.selectCells.length>0" round icon="iconfont icon-shanchu" @click="printdelete">批量删除
      </el-button>
      <!-- <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng" @click="cellSort">导入库位排序
      </el-button> -->
      <el-button size="mini" round icon="iconfont icon-xiazai" @click="downloadCellInfoModel">下载导入模版</el-button>
      <el-upload style="display: inline-block;"
                 action="fileUrl"
                 :limit="limitNum"
                 ref="upload"
                 accept=".xls,.xlsx"
                 :file-list="fileList"
                 :on-exceed="onExceed"
                 :show-file-list="showFileList"
                 :http-request="uploadFile">
        <el-button size="mini" icon="iconfont icon-shangchuan" type="primary" style="margin-left: 10px;padding: 7px 15px;border-radius: 20px;">导入货位</el-button>
      </el-upload>
    </div>
    <el-dialog title="新增货位" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -10vh;" :close-on-click-modal="false">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="编码" prop="cellCode" :label-width="formLabelWidth">
            <el-input v-model="form.cellCode" placeholder="请输入编码" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="cellName" :label-width="formLabelWidth">
            <el-input v-model="form.cellName" placeholder="请输入名称" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货位组" prop="shelfInfoId" :label-width="formLabelWidth">
            <el-select v-model="form.shelfInfoId" filterable placeholder="请选择货位组" style="width: 90%;">
              <el-option
                  v-for="item in shelfs"
                  :key="item.shelfInfoId"
                  :label="item.shelfName"
                  :value="item.shelfInfoId">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="上架顺序" prop="groundOrder" :label-width="formLabelWidth">
            <el-input-number v-model="form.groundOrder" :min="0" label="请输入上架顺序" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="拣货顺序" prop="pickOrder" :label-width="formLabelWidth">
            <el-input-number v-model="form.pickOrder" :min="0" label="请输入拣货顺序" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
            <el-select v-model="form.cellUseType" filterable placeholder="请选择货位使用类型" style="width: 90%;">
              <el-option
                  v-for="item in cellUseTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="周转需求" prop="cellAbcLevel" :label-width="formLabelWidth">
            <el-select v-model="form.cellAbcLevel" filterable placeholder="请选择周转需求" style="width: 90%;">
              <el-option
                  v-for="item in cellAbcLevels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item> -->
          <!--  <el-form-item label="拣货方式" prop="cellPickType" :label-width="formLabelWidth">
              <el-select v-model="form.cellPickType" filterable placeholder="请选择拣货方式" style="width: 90%;">
                <el-option
                  v-for="item in cellPickTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
          <!-- <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <el-form-item label="长(m)" prop="length" :label-width="formLabelWidth">
            <el-input-number v-model="form.length" controls-position="right" :min="0" label="请输入长"
                             style="width: 90%;" @change="changeVolume"></el-input-number>
          </el-form-item>
          <el-form-item label="宽(m)" prop="wide" :label-width="formLabelWidth">
            <el-input-number v-model="form.wide" controls-position="right" :min="0" label="请输入宽"
                             style="width: 90%;" @change="changeVolume"></el-input-number>
          </el-form-item>
          <el-form-item label="高(m)" prop="high" :label-width="formLabelWidth">
            <el-input-number v-model="form.high" controls-position="right" :min="0" label="请输入高"
                             style="width: 90%;" @change="changeVolume"></el-input-number>
          </el-form-item> -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <!-- <el-form-item label="数量限制"  :label-width="formLabelWidth">
            <el-input-number v-model="form.quantityLimit" controls-position="right" :precision="2" :min="0"
                              label="数量限制" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <el-form-item label="sku种类限制"   :label-width="formLabelWidth">
            <el-input-number v-model="form.skuLimit" controls-position="right" :precision="0" :min="0"
                             label="sku种类限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <!-- <el-form-item label="体积限制(m³)" prop="volumeLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.volumeLimit" controls-position="right" :precision="2" :min="0"
                             :max="formVolumeLimitMax" label="请输入体积限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="重量限制(kg)" prop="weightLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.weightLimit" controls-position="right" :precision="2" :min="0"
                             label="请输入重量限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="箱数限制" prop="boxLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.boxLimit" controls-position="right" :precision="2" :min="0" label="请输入箱数限制"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="托盘限制" prop="palletLimit" :label-width="formLabelWidth">
            <el-input-number v-model="form.palletLimit" controls-position="right" :precision="2" :min="0"
                             label="请输入托盘限制" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <el-form-item label="X坐标" prop="xcoordinate" :label-width="formLabelWidth">
            <el-input-number v-model="form.xcoordinate" controls-position="right" :min="0" label="请输入X坐标"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="Y坐标" prop="ycoordinate" :label-width="formLabelWidth">
            <el-input-number v-model="form.ycoordinate" controls-position="right" :min="0" label="请输入Y坐标"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改货位" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -10vh;" :close-on-click-modal="false">
      <div class="cellModalBox">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="编码" prop="cellCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.cellCode" placeholder="请输入编码" style="width: 90%;" disabled="disabled"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="cellName" :label-width="formLabelWidth">
            <el-input v-model="rowData.cellName" placeholder="请输入名称" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货位组" prop="shelfInfoId" :label-width="formLabelWidth">
            <el-select v-model="rowData.shelfInfoId" filterable placeholder="请选择货位组" style="width: 90%;">
              <el-option
                  v-for="item in shelfs"
                  :key="item.shelfInfoId"
                  :label="item.shelfName"
                  :value="item.shelfInfoId">
              </el-option>
            </el-select>
          </el-form-item>
           <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
            <el-select v-model="rowData.cellUseType" filterable placeholder="请选择货位使用类型" style="width: 90%;">
              <el-option
                  v-for="item in cellUseTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
            <!-- <span v-for="item in cellUseTypes">{{ rowData.cellUseType==item.value?item.label:'' }}</span> -->
            <!-- <span style="font-size: 10px;color: red;">* 不可修改</span> -->
          </el-form-item>
          <!-- <el-form-item label="上架顺序" prop="groundOrder" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.groundOrder" :min="0" label="请输入上架顺序"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="拣货顺序" prop="pickOrder" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.pickOrder" :min="0" label="请输入拣货顺序" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <!-- <el-form-item label="货位使用类型" prop="cellUseType" :label-width="formLabelWidth">
            <el-select v-model="rowData.cellUseType" filterable placeholder="请选择货位使用类型" style="width: 90%;">
              <el-option
                  v-for="item in cellUseTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="周转需求" prop="cellAbcLevel" :label-width="formLabelWidth">
            <el-select v-model="rowData.cellAbcLevel" filterable placeholder="请选择周转需求" style="width: 90%;">
              <el-option
                  v-for="item in cellAbcLevels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item> -->
          <!--     <el-form-item label="拣货方式" prop="cellPickType" :label-width="formLabelWidth">
                 <el-select v-model="rowData.cellPickType" filterable placeholder="请选择拣货方式" style="width: 90%;">
                   <el-option
                     v-for="item in cellPickTypes"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value">
                   </el-option>
                 </el-select>
               </el-form-item> -->
          <!-- <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <el-form-item label="长(m)" prop="length" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.length" controls-position="right" :min="0" label="请输入长"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="宽(m)" prop="wide" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.wide" controls-position="right" :min="0" label="请输入宽"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="高(m)" prop="high" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.high" controls-position="right" :min="0" label="请输入高"
                             style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
           <!-- <el-form-item label="数量限制"  :label-width="formLabelWidth">
            <el-input-number v-model="rowData.quantityLimit" controls-position="right" :precision="2" :min="0"
                              label="数量限制" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <el-form-item label="sku种类限制"   :label-width="formLabelWidth">
            <el-input-number v-model="rowData.skuLimit" controls-position="right" :precision="0" :min="0"
                             label="sku种类限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <!-- <el-form-item label="体积限制(m³)" prop="volumeLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.volumeLimit" controls-position="right" :precision="2" :min="0"
                             :max="rowDataVolumeLimitMax" label="请输入体积限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="重量限制(kg)" prop="weightLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.weightLimit" controls-position="right" :precision="2" :min="0"
                             label="请输入重量限制" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="箱数限制" prop="boxLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.boxLimit" controls-position="right" :precision="2" :min="0"
                             label="请输入箱数限制" style="width: 90%;"></el-input-number>
          </el-form-item>
       
          <el-form-item label="托盘限制" prop="palletLimit" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.palletLimit" controls-position="right" :precision="2" :min="0"
                             label="请输入托盘限制" style="width: 90%;"></el-input-number>
          </el-form-item> -->
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <el-form-item label="X坐标" prop="xcoordinate" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.xcoordinate" controls-position="right" :min="0" label="请输入X坐标"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="Y坐标" prop="ycoordinate" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.ycoordinate" controls-position="right" :min="0" label="请输入Y坐标"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <div class="tableBox" >
      <el-table
          highlight-current-row
          v-loading="loading.table"
          :data="cells"
           @selection-change="handleSelectionChange"
          style="width: 100%">
          <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="cellCode"
            label="编码"
            min-width="150">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="xcoordinate"
            label="X坐标"
            min-width="60">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="ycoordinate"
            label="Y坐标"
            min-width="60">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="shelfCode"
            label="货位组"
            min-width="90">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="cellUseType"
            label="使用类型"
            min-width="80">
          <template slot-scope="scope">
            <span v-for="(item,index) in cellUseTypes" :key="index"
                  v-if="item.value === scope.row.cellUseType">{{ item.label }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column

          show-overflow-tooltip
          prop="quantityLimit"
          label="数量限制"
          min-width="100">
          
        </el-table-column> -->
        <el-table-column
       
          show-overflow-tooltip
          prop="skuLimit"
          label="sku种类限制"
          min-width="100">
          
        </el-table-column>
        <!-- <el-table-column
            show-overflow-tooltip
            prop="cellAbcLevel"
            label="周转需求"
            min-width="80">
          <template slot-scope="scope">
            <span v-for="(item,index) in cellAbcLevels" :key="index"
                  v-if="item.value === scope.row.cellAbcLevel">{{ item.label }}</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column

           show-overflow-tooltip
           prop="cellPickType"
           label="拣货方式"
           width="80">
           <template slot-scope="scope">
             <span v-for="(item, index) in cellPickTypes" :key="index" v-if="item.value === scope.row.cellPickType">{{item.label}}</span>
           </template>
         </el-table-column> -->
        <el-table-column
           
          show-overflow-tooltip
          prop="groundOrder"
          label="上架顺序"
          width="80">
        </el-table-column>
        <el-table-column
         
          show-overflow-tooltip
          prop="pickOrder"
          label="拣货顺序"
          width="100">
        </el-table-column>
        <!--<el-table-column
          sortable
          show-overflow-tooltip
          prop="cellAttribute"
          label="货位属性"
          width="180">
        </el-table-column>-->
        <!--<el-table-column
          sortable
          show-overflow-tooltip
          prop="cellHandle"
          label="货位处理"
          width="180">
        </el-table-column>-->
        <!-- <el-table-column
            show-overflow-tooltip
            prop="length"
            label="长"
            min-width="50">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="wide"
            label="宽"
            min- width="50">
        </el-table-column>
        <el-table-column
            align=""
            show-overflow-tooltip
            prop="high"
            label="高"
            min-width="50">
        </el-table-column> -->
        <!-- <el-table-column

          show-overflow-tooltip
          prop="volumeLimit"
          label="体积限制"
          width="80">
          <template slot-scope="scope">
            <span>{{scope.row.volumeLimit }}</span>
          </template>
        </el-table-column>
        <el-table-column
       
          show-overflow-tooltip
          prop="weightLimit"
          label="重量限制"
          width="80">
          <template slot-scope="scope">
            <span>{{scope.row.weightLimit }}</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
          
          show-overflow-tooltip
          prop="boxLimit"
          label="箱数限制"
          width="80">
          <template slot-scope="scope">
            <span>{{scope.row.boxLimit }}</span>
          </template>
        </el-table-column> -->
        <!--<el-table-column
          sortable
          show-overflow-tooltip
          prop="quantityLimit"
          label="数量限制"
          width="180">
          <template slot-scope="scope">
            <span>{{scope.row.quantityLimit.toFixed(2)}}</span>
          </template>
        </el-table-column>-->
        <!-- <el-table-column
          
          show-overflow-tooltip
          prop="palletLimit"
          label="托盘数"
          width="0">
          <template slot-scope="scope">
            <span>{{scope.row.palletLimit }}</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
            filter-multiple
            show-overflow-tooltip
            fixed="right"
            prop="state"
            label="状态"
            min-width="60"
        >
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.state"
                @change="changeEnableOrDisable(scope.row)"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="enable"
                inactive-value="disable">
            </el-switch>
          </template>
        </el-table-column> -->
        <el-table-column
            fixed="right"
            label="操作"
            min-width="160"
        >
          <template slot-scope="scope">
            <el-dropdown style="margin-left:10px;margin-right:10px" size="mini" type="primary"
                         icon="iconfont icon-print" @click="demoPrint(scope.row.cellCode)" split-button>
              打印
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.cellCode,1)">打印预览1
                  </el-button>
                </el-dropdown-item>

                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.cellCode,2)">打印预览2
                  </el-button>
                </el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.cellId)" icon="iconfont icon-shanchu" type="text"
                       class="tableBtn danger"
                       size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination-bottom"
          style="text-align: right;"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :page-sizes="[10,15,20,25]"
          :current-page.sync="searchData.pageNum"
          :page-size="searchData.pageSize"
          layout="sizes, total, prev, pager, next, jumper"
          :total="searchData.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'Shelf',
  data() {
    let validateCellCode = (rule, value, callback) => {
      this.validatorCell(rule, value, callback, 'cellCode', this.cellId);
    };
    let validateCellCode2 = (rule, value, callback) => {
      this.validatorCell(rule, value, callback, 'cellCode', this.cellId);
    };
    let validateCellName = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'cell_info', 'cell_name', this.form.cellName, 0);
    };
    let validateCellName2 = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'cell_info', 'cell_name', this.rowData.cellName, 1, this.rowData.cellId);
    };
    let validateNo0 = (rule, value, callback) => {
      if (value === 0) {
        callback(new Error('不能为 0'));
      } else {
        callback();
      }
    };
    return {
      isMoreSearch: false,
      loading: {
        table: true,
      },
      wareId: PF.getLocal('wareId', 'number'),
      cellId: null,
      searchData: {
        pageNum: 1,
        pageSize: 10,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        shelfInfoId: null,
        cellUseType: null,
        cellAbcLevel: null,
        cellPickType: null,
        xcoordinate: '',
        ycoordinate: '',
      },
      selectCells:[],
      cells: [],
      shelfs: [],
      lengthWideHigh: [],

      dialogNewVisible: false,
      dialogUpdateVisible: false,


      form: {
        shelfInfoId: null,
        cellCode: '',
        cellName: '',
        groundOrder: 1,
        pickOrder: 1,
        cellUseType: null,
        length: null,
        wide: null,
        high: null,
        cellAbcLevel: '',
        volumeLimit: null,
        weightLimit: null,
        boxLimit: null,
        quantityLimit: null,
        palletLimit: 1,
        xcoordinate: null,
        ycoordinate: null,
        cellPickType: '',
      },
      rules: {
        shelfInfoId: [
          {required: true, message: '请选择货位组', trigger: 'blur'}
        ],
        cellCode: [
          {required: true, message: '请输入编码', trigger: 'blur'},
          {validator: validateCellCode, trigger: 'blur'}
        ],
        cellName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          // {validator: validateCellName, trigger: 'blur'}
        ],
        // groundOrder: [
        //   {required: true, message: '请输入上架顺序', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // pickOrder: [
        //   {required: true, message: '请输入拣货顺序', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        cellUseType: [
          {required: true, message: '请选择货位使用类型', trigger: 'blur'}
        ],
        // length: [
        //   {required: true, message: '请输入长', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // wide: [
        //   {required: true, message: '请输入宽', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // high: [
        //   {required: true, message: '请输入高', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // cellAbcLevel: [
        //   {required: true, message: '请选择周转需求', trigger: 'blur'}
        // ],
        // volumeLimit: [
        //   {required: true, message: '请输入体积限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // weightLimit: [
        //   {required: true, message: '请输入重量限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // boxLimit: [
        //   {required: true, message: '请输入箱数限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // palletLimit: [
        //   {required: true, message: '请输入托盘限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // xCoordinate: [
        //   {required: true, message: '请输入X坐标', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // yCoordinate: [
        //   {required: true, message: '请输入Y坐标', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // cellPickType: [
        //   {required: true, message: '请选择拣货方式', trigger: 'blur'}
        // ],
      },
      formLabelWidth: '120px',

      rowData: {
        shelfInfoId: null,
        cellCode: '',
        cellName: '',
        groundOrder: null,
        pickOrder: null,
        cellUseType: null,
        // cellAttribute: '',
        // cellHandle: '',
        length: null,
        wide: null,
        high: null,
        cellAbcLevel: '',
        volumeLimit: null,
        weightLimit: null,
        boxLimit: null,
        quantityLimit: null,
        palletLimit: null,
        xcoordinate: null,
        ycoordinate: null,
        cellPickType: '',
      },
      rules2: {
        shelfInfoId: [
          {required: true, message: '请选择货位组', trigger: 'blur'}
        ],
        cellCode: [
          {required: true, message: '请输入编码', trigger: 'blur'},
          {validator: validateCellCode2, trigger: 'blur'}
        ],
        cellName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          // {validator: validateCellName2, trigger: 'blur'}
        ],
        // groundOrder: [
        //   {required: true, message: '请输入上架顺序', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // pickOrder: [
        //   {required: true, message: '请输入拣货顺序', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        cellUseType: [
          {required: true, message: '请选择货位使用类型', trigger: 'blur'}
        ],
        /*cellAttribute: [
          {required: true, message: '请输入货位属性', trigger: 'blur'}
        ],*/
        /*cellHandle: [
          {required: true, message: '请输入货位处理', trigger: 'blur'}
        ],*/
        // length: [
        //   {required: true, message: '请输入长', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // wide: [
        //   {required: true, message: '请输入宽', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // high: [
        //   {required: true, message: '请输入高', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // cellAbcLevel: [
        //   {required: true, message: '请选择周转需求', trigger: 'blur'}
        // ],
        // volumeLimit: [
        //   {required: true, message: '请输入体积限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // weightLimit: [
        //   {required: true, message: '请输入重量限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // boxLimit: [
        //   {required: true, message: '请输入箱数限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // quantityLimit: [
        //   {required: true, message: '请输入数量限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // palletLimit: [
        //   {required: true, message: '请输入托盘限制', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // xCoordinate: [
        //   {required: true, message: '请输入X坐标', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // yCoordinate: [
        //   {required: true, message: '请输入Y坐标', trigger: 'blur'},
        //   {validator: validateNo0, trigger: 'blur'}
        // ],
        // cellPickType: [
        //   {required: true, message: '请选择拣货方式', trigger: 'blur'}
        // ],
      },
      fileUrl: '', //上传文件的域名地址
      limitNum: 1, //文件上传个数限制
      fileList: [], //文件列表
      showFileList: false, //文件列表是否显示,默认不显示
      excelModel: {
        "wareId": "",
        "list": []
      }
    }
  },
  computed: {
    ...mapState([
      "cellUseTypes",
      "cellAbcLevels",
      "cellPickTypes",
    ]),
    formVolumeLimitMax() {
      if (this.form.length > 0 && this.form.wide > 0 && this.form.high > 0) {
        let volume = this.form.length * this.form.wide * this.form.high;
        return volume
      } else {
        return 0
      }
    },
    rowDataVolumeLimitMax() {
      if (this.rowData.length > 0 && this.rowData.wide > 0 && this.rowData.high > 0) {
        let volume = this.rowData.length * this.rowData.wide * this.rowData.high;
        return volume
      } else {
        return 0
      }
    },
  },
  methods: {
    printdelete(){
      var that = this; 
        var cellIds  =[];
        var ids = '';
        for(var i =0;i<that.selectCells.length;i++){
          var cellInfo = that.selectCells[i];
          if(i<that.selectCells.length-1){
              ids+=cellInfo.cellId+',';
          }else{
            ids+=cellInfo.cellId;
          }
        }
      IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/cell/infos/deleteByIds', 'get', {ids:ids}, (ret) => {
          IOT.hideOverlay();
        if (ret.code === 200) {
           IOT.tips('删除成功！', 'success', 1000, () => {
                that.getCellData()
              });
    

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    handleSelectionChange(val){
        this.selectCells = val;
    },
    printCells(){
        var that = this; 
        var cellCodes  =[];
        that.selectCells.forEach(cellInfo => {
             this.demoPrint(cellInfo.cellCode);
        });
      
    },
    //打印
    demoPrint: function (cellCode, isReview) {
      var that = this;

      IOT.getServerData('/cell/infos/print', 'get', {cellCode: cellCode, wareId: that.wareId}, (ret) => {
        if (ret.code === 200) {
          var strHTML = ret.data;
          that.$parent.printHtml(strHTML, isReview);


        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);

    },
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getCellData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getCellData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      this.searchData.shelfInfoId = null;
      this.searchData.cellUseType = null;
      this.searchData.cellAbcLevel = null;
      this.searchData.cellPickType = null;
      this.searchData.xcoordinate = '';
      this.searchData.ycoordinate = '';
    },
    setPageNumFlush(){
      this.searchData.pageNum =1;
      this.getCellData();
    },
    getCellData() {
      this.loading.table = true;
      IOT.getServerData('/cell/infos/list', 'get', this.searchData, (ret) => {
        this.loading.table = false;
        if (ret.code === 200) {
          this.cells = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getShelfData() {
      IOT.getServerData('/shelf/infos/list', 'get', {wareId: PF.getLocal('wareId', 'number')}, (ret) => {
        if (ret.code === 200) {
          this.shelfs = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getSysParamData() {
      IOT.getServerData('/sys/params', 'get', {moduleCode: 'cellValue'}, (ret) => {
        if (ret.code === 200) {
          this.sysParams = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    commitForm(formName) {
      let that = this;
      IOT.showOverlay('保存中，请稍等...');
      this.form.wareId =this.wareId;
       
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData('/cell/infos/insert', 'post', this.form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getCellData()
              });
              this.resetForm('form')
              this.dialogNewVisible = false
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
    updateForm(formName) {
      let that = this;
      IOT.showOverlay('保存中，请稍等...');
      if(this.rowData.wareId!==undefined || this.rowData.wareName!==undefined){
        this.$delete(this.rowData,"wareId");
        this.$delete(this.rowData,"wareName");
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData('/cell/infos/update', 'post', this.rowData, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getCellData()
              });
              this.resetForm('rowData')
              this.dialogUpdateVisible = false
              this.cellId = null;
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
    showUpdateModal(row) {
      this.rowData = JSON.parse(JSON.stringify(row));
      this.cellId = this.rowData.cellId;
      this.dialogUpdateVisible = true
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
      this.cellId = null;
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
    isDelete(id) {
      this.$confirm('确认删除？')
          .then(_ => {
            this.deleteCell(id)
          })
          .catch(_ => {
          });
    },
    deleteCell(id) {
      let that = this;
      IOT.showOverlay('删除中，请稍等...');
      IOT.getServerData('/cell/infos/delete', 'get', {id: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips('删除成功！', 'success', 1000, () => {
            that.getCellData()
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    changeEnableOrDisable(row) {
      IOT.getServerData('/cell/infos/' + row.state, 'get', {
        cellCode: row.cellCode,
        wareId: PF.getLocal('wareId', 'number')
      }, (ret) => {
        if (ret.code === 200) {
          if (row.state === 'enable') {
            IOT.tips(row.cellCode + ' 已启用！', 'success');
          } else if (row.state === 'disable') {
            IOT.tips(row.cellCode + ' 已禁用！', 'error');
          }
          this.getCellData();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          this.getCellData();
        }
      });
    },
    showNewModal() {
      for (let i = 0; i < this.lengthWideHigh.length; i++) {
        this.form[this.lengthWideHigh[i].paramCode] = this.lengthWideHigh[i].value;
      }
      this.dialogNewVisible = true;
      this.changeVolume()
    },
    changeVolume() {
      if (this.form.length > 0 && this.form.wide > 0 && this.form.high > 0) {
        this.form.volumeLimit = (this.form.length * this.form.wide * this.form.high).toFixed(4)
      }
    },
    cellSort(){
      console.log('导入库位排序')
    },
    // 文件上传类型
    // application/vnd.openxmlformats-officedocument.spreadsheetml.sheet --- 后缀为 .xlsx
    // application/vnd.ms-excel --- 后缀为 .xls
    //文件上传之前做一些验证或限制
    beforeUpload(file) {
      let regExp = file.name.replace(/.+\./, '');
      let lower = regExp.toLowerCase(); //把大写字符串全部转为小写字符串
      let suffix = ['xls', 'xlsx'];
      if (suffix.indexOf(lower) === -1) {
        this.$refs.upload.clearFiles();
        IOT.tips("请上传后缀名为 xls、xlsx 的附件 !", 'error')
        return false;
      }
      let isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$refs.upload.clearFiles();
        IOT.tips("请上传文件大小不能超过 2MB 的附件 !", 'error')
        return false;
      }
    },

    //文件超出个数限制时的钩子
    onExceed(files, fileList) {
      return this.$message.warning(`只能选择${this.limitNum}个文件,当前共选择了${files.length+fileList.length}个`)
    },
    //覆盖默认的上传行为,可以自定义上传的实现
    uploadFile(item) {
      this.beforeUpload(item.file);
      let formData = new FormData();
      formData.append('file', item.file);
      //请求接口
      IOT.upLoadFile('/cell/infos/readCellList', formData, (ret) => {
        if (ret.code === 200) {
          let msg = "本次将导入" + ret.data.length + "条数据，确认导入吗？";
          this.excelModel.wareId = this.wareId;
          this.excelModel.list = ret.data;
          this.$confirm(msg, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$refs.upload.clearFiles();
            IOT.showOverlay('数据导入中，请稍等...');
            console.log(this.excelModel);
            IOT.getServerData('/cell/infos/import', 'post', this.excelModel, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.getCellData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
          }).catch(() => {
            this.$refs.upload.clearFiles();
          });
        } else {
          this.$refs.upload.clearFiles();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    downloadCellInfoModel(){
      window.open("/importModel/cellInfo.xlsx");
    },
    validatorCell(rule, value, callback, fieldName, cellId) {
      let form = {
        [fieldName]: value, // 字段名
        cellId,
      };
      IOT.getServerData("/cell/infos/validator", "post", form, (ret) => {
        if (
                ret.code === 100001 ||
                ret.code === 100002 ||
                ret.code === 100004
        ) {
          callback(new Error(ret.message || "已重复"));
        } else if (ret.code === 100003) {
          callback();
        } else {
          PF.publicMessage(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
          );
          callback();
        }
      });
    },
  },
  created() {
    this.getCellData();
    this.getShelfData();
    this.getSysParamData();
    PF.getSysParamUdfData('lengthWideHigh', [], (data, udfs) => {
      this.lengthWideHigh = udfs;
    });
  },
  mounted() {
  }
}
</script>

<style lang="less">
.cell {
  .cellUseType{
    .el-input{
      width:160px !important;
    }
  }
}
</style>
