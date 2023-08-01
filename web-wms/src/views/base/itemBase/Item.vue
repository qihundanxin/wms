<template>
  <div class="item">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          {{$t('m.base.itemBase.Item.productsetting')}}
<!-- productsetting: '商品设置', //物料设置
			changeItemTypeId: '请选择商品种类', // 请选择物料种类
			itemTypeId: '商品种类', // 物料种类
			itemCode: 'SKU', // SKU
			itemNewModal: '新增商品', // 新增物料
			itemName: 'SKU名称', // SKU名称
			inputItemName: '请输入SKU名称', // 请输入SKU名称
			updateItem: '修改商品', // 修改物料
			iD: '商品编号', // 
			enProduct: '商品英文名', // 产品英文名 -->
      
          <span>关键字搜索</span>
          <el-input style="width:210px" v-model="searchData.keyWords" size="mini" placeholder="SKU名称/规格/型号/包装/单价" clearable></el-input>
        </div>
        <div>
          <span> {{$t('m.base.itemBase.Item.changeItemTypeId')}}</span>
          <el-cascader size="mini" placeholder="" v-model="searchData.itemTypeId" :options="itemTypeTree"
            filterable></el-cascader>
        </div>
        <div>
          <span>无UPC</span>
          <el-switch
            v-model="searchData.noUpc"
            active-color="#13ce66"
            inactive-color="#dcdfe6">
          </el-switch>
        </div>
        <div>
          <span>散件</span>
          <el-switch
              v-model="searchData.unpacking"
              active-color="#13ce66"
              inactive-color="#dcdfe6">
          </el-switch>
        </div>
        <div>
          <span>称重</span>
          <el-switch
              v-model="searchData.weighting"
              active-color="#13ce66"
              inactive-color="#dcdfe6">
          </el-switch>
        </div>
        <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
            <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName"
              :value="item.supplierId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn" :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
        @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round   @click="showSaveDialogNewVisible()">新增
      </el-button>
      <!-- <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogPlNewVisible = true">新增
      </el-button> -->

      <!-- <el-upload style="display:inline-block; margin-left:20px" class="upload-demo"
        action="http://localhost:8003/wms/select/products/importData" :on-preview="handlePreview" :on-remove="handleRemove"
        :before-remove="beforeRemove" :on-exceed="handleExceed" :file-list="fileList" name="model">
        <el-button size="small" type="primary" round>点击上传</el-button>
      </el-upload>
      <el-upload style="display:inline-block; margin-left:20px" class="upload-demo"
        action="http://localhost:8003/wms/item/infos/uploadIUPC2" :on-preview="handlePreview" :on-remove="handleRemove"
        :before-remove="beforeRemove" :on-exceed="handleExceed" :file-list="fileList" name="model">
        <el-button size="small" type="primary" round>UPC上传</el-button>
      </el-upload> -->

      <el-button size="small"   v-loading="tongbuLoading" type="primary" round  @click="tongBu(27)">同步
      </el-button>

      <el-button size="small" :disabled="itemSelection==null||itemSelection.length<=0" v-loading="tongbuLoading" type="primary" round @click="printLabel()">打印标签</el-button>

    </div>

    <el-dialog title="批量新增" :visible.sync="dialogPlNewVisible" :before-close="handleClose" width="900px"
      style="margin-top: -5vh;">
      <div class="itemNewModal">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">

          <el-form-item v-if="isOrganization" label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
              <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-upload class="upload-demo" ref="upload" action="https://jsonplaceholder.typicode.com/posts/"
            :on-preview="handlePreview" :on-remove="handleRemove" :file-list="fileList" data="" :auto-upload="false">
            <el-button slot="trigger" size="small">下载模板</el-button>
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <!--<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button> -->
            <!--<div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
          </el-upload>

        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox" style="height:100px">
      <el-table ref="itemTable" v-loading="loading.masterTable" highlight-current-row :data="items" style="width: 100%" @selection-change="handleItemSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="缩略图/SKU/ID/名称"
          min-width="250">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8"> 
                <div style="display: inline-block;margin-left:0px;  width:50px;height:50px">
                   <el-image 
                      style="width: 50px; height: 50px"
                      :src="scope.row.imgUrl" 
                      :preview-src-list="[scope.row.imgUrl]">
                    </el-image>
                  <!-- <img v-bind:src="scope.row.img_url"  style="width:80px;height:86px"> -->
                </div>
               </el-col>
              <el-col :span="16">
                     <span class="textBtn" style="color:red"  >{{scope.row.itemCode}}</span>
                        <br>
                    <span>{{scope.row.itemBarCode}}</span>
                     <br>
                     <span>{{scope.row.itemTypeCode}}</span>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <!-- <el-table-column sortable show-overflow-tooltip prop="itemCode" label="SKU" width="180">
        </el-table-column>
        <el-table-column sortable show-overflow-tooltip prop="itemBarCode" label="ID" width="180">
        </el-table-column> -->

        <!-- <el-table-column label="一级类目名" prop="categoryId"></el-table-column>
        <el-table-column label="子级类目名" prop="enCategory"></el-table-column> -->
        
        <el-table-column width="200" label="SKU名称" prop="itemName"></el-table-column>
        <el-table-column width="200" label="UPC" prop="upcs">
           <template slot-scope="scope">
                          <el-tag
                         
                            :key="upc.upc"
                            v-for="upc in scope.row.upcs"
                            closable
                            :disable-transitions="false"
                            @close="deleteUPC(upc.itemUpcId)">
                            {{upc.upc}}
                          </el-tag>
                          <el-input
                            class="input-new-tag"
                            v-if="addUPCItemId==scope.row.id"
                            v-model="newUpc"
                            ref="saveTagInput"
                            size="small"
                            @keyup.enter.native="saveUPC(scope.row)"
                            
                          >
                          <!-- @blur="saveUPC(scope.row)" -->
</el-input>
<el-button v-else class="button-new-tag" size="small" @click="showInput(scope.row.id)">+ New UPC</el-button>
                       
                    </template>
        </el-table-column>
        <el-table-column   show-overflow-tooltip prop="weight" label="重量（KG）" width="100">
        </el-table-column>
        <el-table-column   show-overflow-tooltip prop="spec" label="体积（CM*CM*CM）" width="100">
        </el-table-column>
        <el-table-column   show-overflow-tooltip prop="tag" label="标记" width="150">
          <template  slot-scope="scope">
            <el-tag v-if="scope.row.weighting">称重</el-tag>
            <el-tag v-if="scope.row.unpacking" type="warning">散件</el-tag>
          </template>
        </el-table-column>
<!--        <el-table-column   show-overflow-tooltip prop="model" label="型号" width="100">-->
<!--        </el-table-column>-->
        <el-table-column   show-overflow-tooltip prop="packName" label="包装编码" width="100">
        </el-table-column>
        <el-table-column   show-overflow-tooltip prop="organizationName" label="货主" width="150">
        </el-table-column>
        <el-table-column width="200" label="产品英文名" prop="enProduct"></el-table-column>
        <el-table-column label="品牌" prop="brand"></el-table-column>
        <el-table-column width="200" label="简要描述" prop="itemDesc">
           <template  slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.shortDesc" placement="top">
                <el-button>简要描述</el-button>
              </el-tooltip>
          </template>
        </el-table-column>
        <!-- <el-table-column width="200" label="详细描述" prop="shortDesc">
          <template  slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.shortDesc" placement="top">
                <el-button>详细描述</el-button>
              </el-tooltip>
          </template>
           
        </el-table-column> -->

        <!-- <el-table-column sortable show-overflow-tooltip prop="supplierName" label="供应商" width="180">
        </el-table-column> -->
        
        <el-table-column sortable show-overflow-tooltip prop="batchTacticName" label="批次策略" width="180">
        </el-table-column>
        <!-- <el-table-column sortable show-overflow-tooltip prop="upperTacticName" label="上架策略" width="180">
        </el-table-column> -->
        <el-table-column sortable show-overflow-tooltip prop="qcTacticName" label="质检策略" width="180">
        </el-table-column>
        <!-- <el-table-column sortable show-overflow-tooltip prop="description" label="拣货策略" width="180">
        </el-table-column> -->
        <el-table-column sortable show-overflow-tooltip prop="turnoverDescription" label="库存周转策略" width="180">
        </el-table-column>
        
        <!--<el-table-column
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
        </el-table-column>-->
        <!-- <el-table-column sortable show-overflow-tooltip prop="abcLevel" label="产品循环级别" width="150">
          <template slot-scope="scope">
            <span v-for="item in abcLevels" :key="item.value"
              v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
          </template>
        </el-table-column> -->
        
        <!-- <el-table-column sortable show-overflow-tooltip fixed="right" prop="itemStatus" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.itemStatus"
              @change="changeEnableOrDisable(scope.row.itemCode, scope.row.itemStatus)" active-color="#13ce66"
              inactive-color="#ff4949" active-value="enable" inactive-value="disable">
            </el-switch>
          </template>
        </el-table-column> -->
        <el-table-column fixed="right" label="操作" width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
              size="small"></el-button>
            <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle plain
              size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="text-align: right;" class="pagination-bottom" @current-change="handleCurrentChange"
        @size-change="handleSizeChange" :page-sizes="[5,10,15,30,50,100]" :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize" layout="sizes, total, prev, pager, next, jumper" :total="searchData.total">
      </el-pagination>
    </div>

    <el-dialog title="新增商品" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
      style="margin-top: -5vh;">
      <div class="itemNewModal">



        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">

          <!-- <el-form-item label="图片" prop="itemName" :label-width="formLabelWidth">
            <div id="bcd"></div>
            <input type="file" @input="uploadImg(this)" id="abc" />
          </el-form-item> -->
           <el-form-item v-if="isOrganization" label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
              <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input v-model="form.itemName" placeholder="请输入SKU名称"></el-input>
          </el-form-item>
           <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <el-input v-model="form.itemCode" placeholder="请输入SKU"></el-input>
          </el-form-item>
           <el-form-item label="产品ID" prop="itemBarCode" :label-width="formLabelWidth">
            <el-input v-model="form.itemBarCode" placeholder="请输入产品ID"></el-input>
          </el-form-item>
           <el-form-item label="图片URL" prop="imgUrl" :label-width="formLabelWidth">
            <el-input v-model="form.imgUrl" placeholder="图片URL"></el-input>
          </el-form-item>
         
          <el-form-item label="商品种类" prop="itemTypeId" :label-width="formLabelWidth">
            <el-cascader style="width: 70%;" @change="changeItemTypeId" placeholder="请选择商品种类" v-model="form.itemTypeId"
              :options="itemTypeTree" filterable></el-cascader>
          </el-form-item>
          <el-form-item label="包装编码" prop="packCode" :label-width="formLabelWidth">
            <el-select v-model="form.packCode" filterable placeholder="请选择包装编码" style="width: 70%;">
              <el-option v-for="item in packMasters" :key="item.packCode" :label="item.packName" :value="item.packCode">
              </el-option>
            </el-select>
          </el-form-item>

            <el-form-item label="体积(CM)"  :label-width="formLabelWidth"  prop="spec">
                <template slot="label">
                    <span style="color: rgba(255, 0, 0, 0.603);">*</span> 体积(CM)
                 </template>
                <div class="item-container" style="width: fit-content">
                    <el-form-item prop="itemLength"   >
                        <el-input-number style="width:72px"  :value="formatNumber(form.itemLength,2)" placeholder="长" :min="0" :controls="false"  :step="0.01"  @input="updateLength"></el-input-number>
                    </el-form-item>

                    <el-form-item prop="itemWidth"  >
                        <el-input-number style="width:72px"  :value="formatNumber(form.itemWidth,2)" placeholder="宽" :min="0" :controls="false"  :step="0.01"  @input="updateWidth"></el-input-number>
                    </el-form-item>
                    <el-form-item prop="itemHeight" >
                        <el-input-number style="width:72px" :value="formatNumber(form.itemHeight,2)" placeholder="高" :min="0" :controls="false"  :step="0.01" @input="updateHeight"></el-input-number>
                    </el-form-item>
                </div>
            </el-form-item>
          <el-form-item label="重量（kg）" prop="weight" :label-width="formLabelWidth"  >
            <el-input    v-model="form.weight"  placeholder="请输入重量"    style="width:217px"  > </el-input>
          </el-form-item>
<!--            还没有添加字段-->
            <el-form-item label="磅数" prop="pound" :label-width="formLabelWidth"  >
                <el-input   v-model="form.pound" placeholder="请输入磅数" style="width:217px" ></el-input>
            </el-form-item>




          <!-- <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.batchTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option v-for="item in batchTactics" :key="item.batchTacticCode" :label="item.batchTacticName"
                :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="上架策略" prop="upperTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.upperTacticCode" filterable placeholder="请选择上架策略" style="width: 70%;">
              <el-option v-for="item in upperTactics" :key="item.upperTacticCode" :label="item.upperTacticName"
                :value="item.upperTacticCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="质检策略" prop="qcTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.qcTacticCode" filterable placeholder="请选择质检策略" style="width: 70%;">
              <el-option v-for="item in qcTactics" :key="item.qcTacticCode" :label="item.qcTacticName"
                :value="item.qcTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="form.abcLevel" filterable placeholder="请选择产品循环级别" style="width: 70%;">
              <el-option v-for="item in abcLevels" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.pickTacticCode" filterable placeholder="请选择拣货策略" style="width: 70%;">
              <el-option v-for="item in pickTactics" :key="item.allocationRuleCode" :label="item.description"
                :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="库存周转策略" prop="turnoverTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.turnoverTacticCode" filterable placeholder="请选择库存周转策略" style="width: 70%;">
              <el-option v-for="item in turnoverTactics" :key="item.turnoverCode" :label="item.turnoverDescription"
                :value="item.turnoverCode">
              </el-option>
            </el-select>
          </el-form-item>

         <el-form-item label="型号" prop="model" :label-width="formLabelWidth">
             <el-input v-model="form.model" placeholder="请输入型号"></el-input>
         </el-form-item>
          <el-form-item label="是否为散件" prop="unpacking" :label-width="formLabelWidth">
            <el-radio-group v-model="form.unpacking">
              <el-radio :label=false>否</el-radio>
              <el-radio :label=true>是</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否需称重" prop="weighting" :label-width="formLabelWidth">
            <el-radio-group v-model="form.weighting">
              <el-radio :label=false>否</el-radio>
              <el-radio :label=true>是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改商品" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px"
      style="margin-top: -5vh;">
      <div class="itemNewModal">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
            <el-form-item v-if="isOrganization" label="货主" prop="organizationId" :label-width="formLabelWidth">
                <el-select v-model="rowData.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
                    <el-option v-for="item in organizations" :key="item.organizationId" :label="item.organizationName"
                               :value="item.organizationId">
                    </el-option>
                </el-select>
            </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input v-model="rowData.itemName" placeholder="请输入SKU名称"></el-input>
          </el-form-item>
          <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.itemCode" placeholder="请输入SKU"></el-input>
          </el-form-item>
           <el-form-item label="产品ID" prop="itemBarCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.itemBarCode" placeholder="请输入产品ID"></el-input>
          </el-form-item>
          <el-form-item label="图片URL" prop="imgUrl" :label-width="formLabelWidth">
            <el-input v-model="rowData.imgUrl" placeholder="图片URL"></el-input>
          </el-form-item>
            <el-form-item label="商品种类" prop="itemTypeId" :label-width="formLabelWidth">
                <el-cascader :show-all-levels="false" style="width: 70%;" placeholder="请选择商品种类"
                             v-model="rowData.itemTypeId" :options="itemTypeTree" filterable></el-cascader>
            </el-form-item>
          <el-form-item label="包装编码" prop="packCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.packCode" filterable placeholder="请选择包装编码" style="width: 70%;">
              <el-option v-for="item in packMasters" :key="item.packCode" :label="item.packName" :value="item.packCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="重量（kg）" prop="weight" :label-width="formLabelWidth" >
            <el-input v-model="rowData.weight" placeholder="请输入重量"  style="width:217px" ></el-input>
          </el-form-item>

            <el-form-item label="磅数" prop="pound" :label-width="formLabelWidth" >
                <el-input    v-model="rowData.pound" placeholder="请输入磅数"  style="width:217px"  ></el-input>
            </el-form-item>

            <el-form-item label="体积(CM)"  :label-width="formLabelWidth" prop="spec" v-model="combinedSpec">
                 <template slot="label">
                    <span style="color: rgba(255, 0, 0, 0.603);">*</span> 体积(CM)
                 </template>
                <div class="item-container" style="width: fit-content">
                 <el-form-item prop="itemLength"   >
                <el-input-number style="width:72px"  @input="onInput()" v-model="rowData.itemLength" placeholder="长" :min="0" :controls="false" :precision="2" ></el-input-number>
                </el-form-item>

                <el-form-item prop="itemWidth"  >
                <el-input-number style="width:72px"  @input="onInput()" v-model="rowData.itemWidth" placeholder="宽" :min="0" :controls="false" :precision="2"></el-input-number>
                </el-form-item>
                <el-form-item prop="itemHeight"  >
                <el-input-number style="width:72px" @input="onInput()"  v-model="rowData.itemHeight" placeholder="高" :min="0" :controls="false" :precision="2"></el-input-number>
                </el-form-item>
                </div>


            </el-form-item>
            <!--<el-form-item label="库存上限" prop="upperLimit" :label-width="formLabelWidth">
  <el-input v-model.number="rowData.upperLimit" placeholder="请输入库存上限"></el-input>
</el-form-item>-->
<!--            <el-form-item label="长(cm)" prop="itemLength" :label-width="formLabelWidth">
                <el-input-number v-model="rowData.itemLength" placeholder="请输入长" :min="0" :controls="false"  size="medium" :precision="2"></el-input-number>
            </el-form-item>
            <el-form-item label="宽(cm)" prop="itemWidth" :label-width="formLabelWidth">
                <el-input-number v-model="rowData.itemWidth" placeholder="请输入宽" :min="0" :controls="false" size="medium" :precision="2"></el-input-number>
            </el-form-item>
            <el-form-item label="高(cm)" prop="itemHeight" :label-width="formLabelWidth">
                <el-input-number v-model="rowData.itemHeight" placeholder="请输入高" :min="0" :controls="false" size="medium" :precision="2"></el-input-number>
            </el-form-item>-->
          <!--<el-form-item label="库存下限" prop="floorLimit" :label-width="formLabelWidth">
            <el-input v-model.number="rowData.floorLimit" placeholder="请输入库存下限"></el-input>
          </el-form-item>-->

            <el-form-item label="型号" prop="model" :label-width="formLabelWidth">
                <el-input v-model="rowData.model" placeholder="请输入型号"></el-input>
            </el-form-item>

          <!-- <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="rowData.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.batchTacticCode" filterable placeholder="请选择批次策略" style="width: 70%;">
              <el-option v-for="item in batchTactics" :key="item.batchTacticCode" :label="item.batchTacticName"
                :value="item.batchTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="上架策略" prop="upperTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.upperTacticCode" filterable placeholder="请选择上架策略" style="width: 70%;">
              <el-option v-for="item in upperTactics" :key="item.upperTacticCode" :label="item.upperTacticName"
                :value="item.upperTacticCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="质检策略" prop="qcTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.qcTacticCode" filterable placeholder="请选择质检策略" style="width: 70%;">
              <el-option v-for="item in qcTactics" :key="item.qcTacticCode" :label="item.qcTacticName"
                :value="item.qcTacticCode">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="rowData.abcLevel" filterable placeholder="请选择产品循环级别" style="width: 70%;">
              <el-option v-for="item in abcLevels" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.pickTacticCode" filterable placeholder="请选择拣货策略" style="width: 70%;">
              <el-option v-for="item in pickTactics" :key="item.allocationRuleCode" :label="item.description"
                :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="库存周转策略" prop="turnoverTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.turnoverTacticCode" filterable placeholder="请选择库存周转策略" style="width: 70%;">
              <el-option v-for="item in turnoverTactics" :key="item.turnoverCode" :label="item.turnoverDescription"
                :value="item.turnoverCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否为散件" prop="unpacking" :label-width="formLabelWidth">
            <el-radio-group v-model="rowData.unpacking">
              <el-radio :label=false>否</el-radio>
              <el-radio :label=true>是</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否需称重" prop="weighting" :label-width="formLabelWidth">
            <el-radio-group v-model="rowData.weighting">
              <el-radio :label=false>否</el-radio>
              <el-radio :label=true>是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    mapState
  } from "vuex";

  export default {
    name: 'Item',
    data() {
      let validateItemName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_info', 'item_name', this.form.itemName, 0);
      };
      let validateItemName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_info', 'item_name', this.rowData.itemName, 1, this.rowData.id);
      };
      return {
        tongbuLoading:false,
        newUpc:'',
        addUPCItemId:'',
        fileList:[],
        inputVisible:false,
        loading: {
          masterTable: false,
        },
        newImg: '',
        imageUrl: '',
        isMoreSearch: false,

        searchData: {
          noUpc:null,
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          itemTypeId: '',
          supplierId: '',
          organizationId: '',
          unpacking: null,
          weighting: null,
        },
        oneSearchBarHeight: '50px', // 收起高度(50px/行)
        autoSearchBarHeight: 'auto', // 展开高度(50px/行)
        searchBarHeight: '50px',
        items: [],
        itemSelection: [],
        itemTypeTree: [],
        organizations: [],
        packMasters: [],
        suppliers: [],
        batchTactics: [],
        upperTactics: [],
        qcTactics: [],
        pickTactics: [],
        dialogNewVisible: false,
        dialogUpdateVisible: false,
        dialogPlNewVisible: false,
        isOrganization: true,
        turnoverTactics: [],

        form: {
          itemCode:'',
          itemName: '',
          itemTypeId: '',
          supplierId: '',
          organizationId: '',
          defaultPrice: '',
          spec: '',
          model: '',
          packCode: '',
          upperLimit: '',
          floorLimit: '',
          weight: undefined,
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode: '',
          abcLevel: '',
          turnoverTacticCode: '',
          spec1:'',
          spec2:'',
          spec3:'',
          itemLength:undefined,
          itemWidth:undefined,
          itemHeight:undefined,
          pound:undefined,
          weighting: false,
          unpacking: false,
        },
        rules: {
          itemName: [{
              required: true,
              message: '请输入SKU名称',
              trigger: 'blur'
            },
            {
              validator: validateItemName,
              trigger: 'blur'
            }
          ],
          itemTypeId: [{
            required: true,
            message: '请选择商品种类',
            trigger: 'blur'
          }],
          supplierId: [{
            required: false,
            message: '请选择供应商',
            trigger: 'blur'
          }],
          organizationId: [{
            required: true,
            message: '请选择货主',
            trigger: 'blur'
          }],
          weight: [

                  {
                      validator: (rule, value, callback) => {
                          if (isNaN(parseFloat(value))) {
                              callback(new Error('请输入重量'))
                          } else if (parseFloat(value) === 0 ) {
                              callback(new Error('重量必须大于0'))
                          } else {
                              callback()
                          }
                      },
                      trigger: 'blur',
                      required: true
                  },
              PF.numberRule(0, false, 1000000, false, 0.0001)
          ],
          model: [{
            required: false,
            message: '请输入型号',
            trigger: 'blur'
          }],
          packCode: [{
            required: true,
            message: '请选择包装编码',
            trigger: 'blur'
          }],
          upperLimit: [{
            type: 'number',
            message: '库存上限必须为数字值',
            trigger: ['blur', 'change']
          }],
          floorLimit: [{
            type: 'number',
            message: '库存上限必须为数字值',
            trigger: ['blur', 'change']
          }],

          batchTacticCode: [{
            required: true,
            message: '请选择批次策略',
            trigger: 'blur'
          }],
            itemLength: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入长'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('长必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            itemWidth: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入宽'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('宽必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            itemHeight: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入高'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('高必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            pound: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入磅数'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('磅数必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
                PF.numberRule(0, false, 1000000, false, 0.01)
            ]
        },

        formLabelWidth: '120px',

        rowData: {
          itemName: '',
          itemTypeId: '',
          supplierId: '',
          organizationId: '',
          defaultPrice: '',
          spec: '',
          model: '',
          packCode: '',
          upperLimit: '',
          floorLimit: '',
          weight: undefined,
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode: '',
          abcLevel: '',
          turnoverTacticCode: '',
          spec1:'',
          spec2:'',
          spec3:'',
          itemLength:undefined,
          itemWidth:undefined,
          itemHeight:undefined,
          pound:undefined,
          unpacking: false,
          weighting: false,
        },
        rules2: {
          itemName: [{
              required: true,
              message: '请输入SKU名称',
              trigger: 'blur'
            },
            {
              validator: validateItemName2,
              trigger: 'blur'
            }
          ],
          itemTypeId: [{
            required: false,
            message: '请选择商品种类',
            trigger: 'blur'
          }],

          organizationId: [{
            required: true,
            message: '请选择货主',
            trigger: 'blur'
          }],
            weight:
              [
                  {
                      validator: (rule, value, callback) => {
                          if (isNaN(parseFloat(value))) {
                              callback(new Error('请输入重量'))
                          } else if (parseFloat(value) <= 0 ) {
                              callback(new Error('重量必须大于0'))
                          } else {
                              callback()
                          }
                      },
                      trigger: 'blur',
                      required: true
                  },
                  PF.numberRule(0, false, 1000000, false, 0.0001)
              ],

          packCode: [{
            required: true,
            message: '请选择包装编码',
            trigger: 'blur'
          }],

          batchTacticCode: [{
            required: true,
            message: '请选择批次策略',
            trigger: 'blur'
          }],
          upperTacticCode: [{
            required: true,
            message: '请选择上架策略',
            trigger: 'blur'
          }],
            itemLength: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入长'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('长必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            itemWidth: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入宽'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('宽必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            itemHeight: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入高'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('高必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
            ],
            pound: [
                {
                    validator: (rule, value, callback) => {
                        if (isNaN(parseFloat(value))) {
                            callback(new Error('请输入磅数'))
                        } else if (parseFloat(value) <= 0 ) {
                            callback(new Error('磅数必须大于0'))
                        } else {
                            callback()
                        }
                    },
                    trigger: 'blur',
                    required: true
                },
                PF.numberRule(0, false, 1000000, false, 0.01)
            ],
        },
      }
    },
    watch: {
      newImg() {
        var imgBox = $("#abc");
        uploadImg('#bcd', imgBox);
      },
        'form.weight'(newWeight) {
            if(isNaN(newWeight)){
                return
            }
            const pound = (newWeight * 2.20462262).toFixed(2)
            if(this.form.pound != pound && this._notuppound == null){
                this._notupweight = true
                this.form.pound = pound
            }
            if(this._notuppound){
                this._notuppound = null
            }
        },
        'form.pound'(newPound) {
            if(isNaN(newPound)){
                return
            }
            const weight = (newPound / 2.20462262).toFixed(4)
            if(this.form.weight != weight && this._notupweight == null){
                this._notuppound = true
                this.form.weight = weight
            }
            if(this._notupweight){
                this._notupweight = null
            }
        },
        'rowData.weight'(newWeight) {
            if(isNaN(newWeight)){
                return
            }
            const pound = (newWeight * 2.20462262).toFixed(2)
            if(this.rowData.pound != pound && this._notdatauppound == null){
                this._notdataupweight = true
                this.rowData.pound = pound
            }
            if(this._notdatauppound){
                this._notdatauppound = null
            }
        },
        'rowData.pound'(newPound) {
            if(isNaN(newPound)){
                return
            }
            const weight = (newPound / 2.20462262).toFixed(4)
            if(this.rowData.weight != weight && this._notdataupweight == null){
                this._notdatauppound = true
                this.rowData.weight = weight
            }
            if(this._notupweight){
                this._notdataupweight = null
            }
        },
    },
    computed: {
      ...mapState([
        "abcLevels",
      ]),
      combinedSpec() {
          const length = this.rowData.itemLength || 0;
          const width = this.rowData.itemWidth || 0;
          const height = this.rowData.itemHeight || 0;
          this.rowData.spec =`${length}*${width}*${height}`
      }
    },
    methods: {
      printLabel(){
        PF.setLocal('PrintItemLabel', this.itemSelection);
        const printPage = this.$router.resolve({ name: 'PrintItemLabel' })
        window.open(printPage.href, '_blank')
      },
      handleItemSelectionChange(val){
        this.itemSelection = val;
      },
      deleteUPC(id){
          IOT.getServerData('/item/upcs/delete','get',{id: id},(ret) => {
            if (ret.code === 200) {
               this.getItemData();
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
      },
       validateLength(rule, value, callback) {
           if (value === undefined || value === null) {
               callback(new Error('请输入长'));
               return
           }

           if (value === 0) {
               callback(new Error('长度应该大于0'));
               return
           }
       },
        showSaveDialogNewVisible(){
            this.dialogNewVisible = true
        },
      saveUPC(item){
        var itemUpc = {
          itemCode:item.itemCode,
          organizationId:item.organizationId,
          itemId:item.id,
          upc:this.newUpc
        }
        IOT.getServerData('/item/upcs/add','post',itemUpc,(ret) => {
            if (ret.code === 200) {
              this.newUpc="";
              this.addUPCItemId="";
              this.getItemData();
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
      },
      tongBu(id){
        this.tongbuLoading=true;
          IOT.getServerData('/item/infos/xhcSku','get',{},(ret) => {
            this.tongbuLoading=false;
            if (ret.code === 200) {
              IOT.tips('运行成功！', 'success');
              this.getItemData();
            } else {
              IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
          });
      },
      onInput(){
          this.$forceUpdate();
      },
      showInput(id){
        this.addUPCItemId = id;
        // this.inputVisible = true;
          this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
          });
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },


      uploadImg() {
        var imgBox = $("#abc");
        var file = imgBox[0].files[0];
        var imgSrc;
        if (!/image\/\w+/.test(file.type)) {
          alert("看清楚，这个需要图片！");
          return false;
        }
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
          console.log(this.result);
          imgSrc = this.result;
          var imgs = document.createElement("img");
          $(imgs).attr("src", imgSrc);
          $(imgs).attr("style", "height:120px;width:120px");
          $("#bcd").append(imgs);
        };
      },
      handleAvatarSuccess(res, file) {

        this.imageUrl = URL.createObjectURL(file.raw);
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      handleSizeChange(val) {
        this.searchData.pageSize = val;
        this.getItemData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getItemData()
      },
      clearSearchData() {
        this.searchData.noUpc = null;
        this.searchData.keyWords = '';
        this.searchData.itemTypeId = '';
        this.searchData.supplierId = '';
        this.searchData.organizationId = '';
        this.searchData.weighting = null;
        this.searchData.unpacking = null;
      },
      setPageNumFlush(){
          this.searchData.pageNum =1;
          this.getItemData();
      },
      getItemData() {
        this.loading.masterTable = true;
        let searchData = JSON.parse(JSON.stringify(this.searchData))
        searchData.itemTypeId = searchData.itemTypeId[searchData.itemTypeId.length - 1]
        if(searchData.noUpc=="true"){
            searchData.noUpc=1
        }
        else if(searchData.noUpc=="false"){
            searchData.noUpc=null
        }
         else if(searchData.noUpc==null){
            searchData.noUpc=null
        }
         else if(searchData.noUpc==true){
            searchData.noUpc=1
        }
         else if(searchData.noUpc==false){
            searchData.noUpc=null
        }
         if(searchData.weighting) {
           searchData.weighting = true;
         } else {
           searchData.weighting = null;
         }

        if(searchData.unpacking) {
          searchData.unpacking = true;
        } else {
          searchData.unpacking = null;
        }

        IOT.getServerData('/item/infos/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
            this.loading.masterTable = false;
            this.items = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
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
      getPackMasterData() {
        IOT.getServerData('/pack/masters/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.packMasters = ret.rows;
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
      commitForm(formName) {
        let that = this;
       

        this.$refs[formName].validate((valid) => {
          if (valid) {
             IOT.showOverlay('保存中，请稍等...');
            let form = PF.JSON(this.form);

            // delete form.organizationId;
            form.itemTypeId = form.itemTypeId[form.itemTypeId.length - 1];
            if(form.itemLength!="" || form.itemWidth!="" || form.itemHeight!=""){
                form.spec = form.itemLength +"*"+ form.itemWidth +"*"+ form.itemHeight;
            }

            IOT.getServerData('/item/infos/insert', 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getItemData()
                });
              } else {
                //berwin 2022-4
                IOT.tips(ret.data +" " +ret.message || '服务器请求失败，稍后再试！', 'error');
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
            let rowData = PF.JSON(this.rowData);
            if (rowData.itemTypeId != undefined && rowData.itemTypeId != null && rowData.itemTypeId.length > 0) {
              rowData.itemTypeId = rowData.itemTypeId[0];
            }

              if(rowData.itemLength!="" || rowData.itemWidth!="" || rowData.itemHeight!=""){
                rowData.spec = rowData.itemLength +"*"+ rowData.itemWidth +"*"+ rowData.itemHeight;
            }
            
            // delete rowData.organizationId;
            IOT.getServerData('/item/infos/update', 'post', rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getItemData()
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

        /*if(row.spec!=null){
            var sprcs = row.spec.split('*');
            if(sprcs.length==3){
                row.spec1=sprcs[0];
                row.spec2=sprcs[1];
                row.spec3=sprcs[2];
            }else{
              row.spec1='';
                row.spec2='';
                row.spec3='';
            }
        }else{
              row.spec1='';
                row.spec2='';
                row.spec3='';
            }*/
          if (row.weight === null) {
              row.weight = undefined
          }
          if (row.pound === null) {
              row.pound = undefined
          }
          if (row.itemLength === null) {
              row.itemLength = undefined
          }
          if (row.itemWidth === null) {
              row.itemWidth = undefined
          }
          if (row.itemHeight === null) {
              row.itemHeight = undefined
          }
          if (this.rowData.weight !== null && this.rowData.weight !== undefined) {
              this.rowData.pound = (this.rowData.weight * 2.20462262).toFixed(2);
          }
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
        this.rowData.weight = undefined;
        this.dialogUpdateVisible = false;
        //     done();
        // })
        // .catch(_ => {});
      },
        formatNumber(value, precision) {
            if (typeof value === 'number') {
                const stringValue = value.toString();
                const dotIndex = stringValue.indexOf('.');
                if (dotIndex !== -1) {
                    const decimalPart = stringValue.substr(dotIndex + 1, precision);
                    return parseFloat(`${stringValue.substr(0, dotIndex + 1)}${decimalPart}`);
                }
            }
            return value;
        },

        updateLength(newValue) {
            const parsedValue = parseFloat(newValue);
            if (!isNaN(parsedValue)) {
                this.form.itemLength = parsedValue;
            }
        },
        updateWidth(newValue) {
            const parsedValue = parseFloat(newValue);
            if (!isNaN(parsedValue)) {
                this.form.itemWidth = parsedValue;
            }
        },
        updateHeight(newValue) {
            const parsedValue = parseFloat(newValue);
            if (!isNaN(parsedValue)) {
                this.form.itemHeight = parsedValue;
            }
        },

        updatePound(value) {
            const parsedValue = parseFloat(value);
            if (!isNaN(parsedValue)) {
                this.form.pound = parsedValue;
                this.form.weight = (parsedValue / 2.20462262).toFixed(4);
            }
        },
        formatWeight(value, precision) {
            if (typeof value === 'number') {
                const stringValue = value.toString();
                const dotIndex = stringValue.indexOf('.');
                if (dotIndex !== -1) {
                    const decimalPart = stringValue.substr(dotIndex + 1, precision);
                    return parseFloat(`${stringValue.substr(0, dotIndex + 1)}${decimalPart}`);
                }
            }
            return value;
        },
        updateWeight(value) {
            const parsedValue = parseFloat(value);
            if (!isNaN(parsedValue)) {
                this.form.weight = parsedValue;
                this.form.pound = (parsedValue * 2.20462262).toFixed(2);
            }
        },
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteItem(id)
          })
          .catch(_ => {});
      },
      deleteItem(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/item/infos/delete', 'get', {
          id: id
        }, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getItemData()
            });
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
      getTurnoverTacticData() {
        IOT.getServerData('/turnover/tactics/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.turnoverTactics = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeEnableOrDisable(itemCode, type) {
        IOT.getServerData('/item/infos/' + type, 'get', {
          itemCode: itemCode
        }, (ret) => {
          if (ret.code === 200) {
            if (type === 'enable') {
              IOT.tips(itemCode + ' 已启用！', 'success');
            } else if (type === 'disable') {
              IOT.tips(itemCode + ' 已禁用！', 'error');
            }
            this.getItemData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeItemTypeId(val) {
        let tree = PF.JSON(this.itemTypeTree);
        for (let i = 0; i < val.length; i++) {
          for (let j = 0; j < tree.length; j++) {
            if (val[i] === tree[j].itemTypeId && PF.isNull(tree[j].children)) {
              tree = tree[j].children;
              break
            } else if (val[i] === tree[j].itemTypeId && !PF.isNull(tree[j].children)) {
              tree = tree[j];
              break
            }
          }
        }

        this.form.batchTacticCode = tree.batchTacticCode;
        this.form.upperTacticCode = tree.upperTacticCode;
        this.form.qcTacticCode = tree.qcTacticCode;
        this.form.pickTacticCode = tree.pickTacticCode;


      },
    },
    created() {
      PF.getSysParamUdfData('isOrganization', [], (data, udfs) => {
        if (udfs[0].value === '1') {
          this.isOrganization = true;
        } else if (udfs[0].value === '0') {
          this.isOrganization = false;
        }
      });
      this.getPickTacticData();
      this.getItemData();
      this.getItemTypeData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      this.getPackMasterData();
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      this.getBatchTacticData();
      this.getUpperTacticData();
      this.getQcTacticData();
      this.getTurnoverTacticData();
        if (this.$route.params.itemRow != null){
            this.showUpdateModal(this.$route.params.itemRow);
        }
    },
    mounted() {}
  }
</script>

<style scoped lang="less">
  .item {}

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .item-container {
      display: flex;
      white-space: nowrap;

  }
  /deep/.el-input-number .el-input__inner {
      text-align: left;
  }
  .item {
      margin-right: 0;
      margin-left: 0;
  }
</style>