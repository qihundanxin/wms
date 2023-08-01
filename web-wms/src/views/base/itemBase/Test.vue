<template>
  <div class="item">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getItemData" size="mini" icon="el-icon-search">123</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button> 
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/规格/型号/包装/单价等" clearable></el-input>
        </div>
        <div>
          <span>物料种类</span>
          <el-cascader
            size="mini"
            placeholder="请选择物料种类"
            v-model="searchData.itemTypeId"
            :options="itemTypeTree"
            filterable></el-cascader>
        </div>
        <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
            <el-option
              v-for="item in suppliers"
              :key="item.supplierId"
              :label="item.supplierName"
              :value="item.supplierId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
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

    <el-dialog title="新增物料" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px"
               style="margin-top: -5vh;">
      <div class="itemNewModal">


        
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">

           <el-form-item label="图片" prop="itemName" :label-width="formLabelWidth">
             <div id="bcd"></div>
             <input type="file" @input="uploadImg(this)"  id="abc"/>
          </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input v-model="form.itemName" placeholder="请输入SKU名称"></el-input>
          </el-form-item>
           <el-form-item v-if="isOrganization" label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
         <el-form-item label="物料种类" prop="itemTypeId" :label-width="formLabelWidth">
            <el-cascader
              style="width: 70%;"
              @change="changeItemTypeId"
              placeholder="请选择物料种类"
              v-model="form.itemTypeId"
              :options="itemTypeTree"
              filterable></el-cascader>
          </el-form-item>
          <el-form-item label="包装编码" prop="packCode" :label-width="formLabelWidth">
            <el-select v-model="form.packCode" filterable placeholder="请选择包装编码" style="width: 70%;">
              <el-option
                v-for="item in packMasters"
                :key="item.packCode"
                :label="item.packName"
                :value="item.packCode">
              </el-option>
            </el-select>
          </el-form-item>
             
          <el-form-item label="重量" prop="weight" :label-width="formLabelWidth">
            <el-input v-model="form.weight" placeholder="请输入重量"></el-input>
          </el-form-item>
          <!--<el-form-item label="库存上限" prop="upperLimit" :label-width="formLabelWidth">
            <el-input v-model.number="form.upperLimit" placeholder="请输入库存上限"></el-input>
          </el-form-item>-->
          <el-form-item label="规格" prop="spec" :label-width="formLabelWidth">
            <el-input v-model="form.spec" placeholder="请输入规格"></el-input>
          </el-form-item>
          <!--<el-form-item label="库存下限" prop="floorLimit" :label-width="formLabelWidth">
            <el-input v-model.number="form.floorLimit" placeholder="请输入库存下限"></el-input>
          </el-form-item>-->
          <el-form-item label="型号" prop="model" :label-width="formLabelWidth">
            <el-input v-model="form.model" placeholder="请输入型号"></el-input>
          </el-form-item>
         
         
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
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
            <el-select v-model="form.upperTacticCode" filterable placeholder="请选择上架策略" style="width: 70%;">
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
          <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="form.abcLevel" filterable placeholder="请选择产品循环级别" style="width: 70%;">
              <el-option
                v-for="item in abcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.pickTacticCode" filterable placeholder="请选择拣货策略" style="width: 70%;">
              <el-option
                      v-for="item in pickTactics"
                      :key="item.allocationRuleCode"
                      :label="item.description"
                      :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="库存周转策略" prop="turnoverTacticCode" :label-width="formLabelWidth">
            <el-select v-model="form.turnoverTacticCode" filterable placeholder="请选择库存周转策略" style="width: 70%;">
              <el-option
                      v-for="item in turnoverTactics"
                      :key="item.turnoverCode"
                      :label="item.turnoverDescription"
                      :value="item.turnoverCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改物料" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px"
               style="margin-top: -5vh;">
      <div class="itemNewModal">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input v-model="rowData.itemName" placeholder="请输入SKU名称"></el-input>
          </el-form-item>
           
          <el-form-item label="包装编码" prop="packCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.packCode" filterable placeholder="请选择包装编码" style="width: 70%;">
              <el-option
                v-for="item in packMasters"
                :key="item.packCode"
                :label="item.packName"
                :value="item.packCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="重量" prop="weight" :label-width="formLabelWidth">
            <el-input v-model="rowData.weight" placeholder="请输入重量"></el-input>
          </el-form-item>
          <!--<el-form-item label="库存上限" prop="upperLimit" :label-width="formLabelWidth">
            <el-input v-model.number="rowData.upperLimit" placeholder="请输入库存上限"></el-input>
          </el-form-item>-->
          <el-form-item label="规格" prop="spec" :label-width="formLabelWidth">
            <el-input v-model="rowData.spec" placeholder="请输入规格"></el-input>
          </el-form-item>
          <!--<el-form-item label="库存下限" prop="floorLimit" :label-width="formLabelWidth">
            <el-input v-model.number="rowData.floorLimit" placeholder="请输入库存下限"></el-input>
          </el-form-item>-->
          <el-form-item label="型号" prop="model" :label-width="formLabelWidth">
            <el-input v-model="rowData.model" placeholder="请输入型号"></el-input>
          </el-form-item>
          <el-form-item label="物料种类" prop="itemTypeId" :label-width="formLabelWidth">
            <el-cascader
              style="width: 70%;"
              placeholder="请选择物料种类"
              v-model="rowData.itemTypeId"
              :options="itemTypeTree"
              filterable></el-cascader>
          </el-form-item>
          <el-form-item v-if="isOrganization" label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="rowData.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="rowData.supplierId" filterable placeholder="请选择供应商" style="width: 70%;">
              <el-option
                v-for="item in suppliers"
                :key="item.supplierId"
                :label="item.supplierName"
                :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="批次策略" prop="batchTacticCode" :label-width="formLabelWidth">
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
            <el-select v-model="rowData.upperTacticCode" filterable placeholder="请选择上架策略" style="width: 70%;">
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
          <el-form-item label="产品循环级别" prop="abcLevel" :label-width="formLabelWidth">
            <el-select v-model="rowData.abcLevel" filterable placeholder="请选择产品循环级别" style="width: 70%;">
              <el-option
                v-for="item in abcLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.pickTacticCode" filterable placeholder="请选择拣货策略" style="width: 70%;">
              <el-option
                      v-for="item in pickTactics"
                      :key="item.allocationRuleCode"
                      :label="item.description"
                      :value="item.allocationRuleCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="库存周转策略" prop="turnoverTacticCode" :label-width="formLabelWidth">
            <el-select v-model="rowData.turnoverTacticCode" filterable placeholder="请选择库存周转策略" style="width: 70%;">
              <el-option
                      v-for="item in turnoverTactics"
                      :key="item.turnoverCode"
                      :label="item.turnoverDescription"
                      :value="item.turnoverCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="items"
        style="width: 100%">
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
          label="物料种类"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="supplierName"
          label="供应商"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
        </el-table-column>
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
          prop="qcTacticName"
          label="质检策略"
          width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="description"
                label="拣货策略"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="turnoverDescription"
                label="库存周转策略"
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
          label="包装编码"
          width="180">
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
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="abcLevel"
          label="产品循环级别"
          width="150">
          <template slot-scope="scope">
            <span v-for="item in abcLevels" :key="item.value" v-if="item.value === scope.row.abcLevel">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="weight"
          label="重量"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          fixed="right"
          prop="itemStatus"
          label="状态"
          width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.itemStatus"
              @change="changeEnableOrDisable(scope.row.itemCode, scope.row.itemStatus)"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-value="enable"
              inactive-value="disable">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle plain
                       size="small"></el-button>
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

  </div>
</template>

<script>
  import {mapState} from "vuex";

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
        newImg:'',
        imageUrl :'',
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          itemTypeId: '',
          supplierId: '',
          organizationId: '',
        },
        oneSearchBarHeight: '50px', // 收起高度(50px/行)
        autoSearchBarHeight: 'auto', // 展开高度(50px/行)
        searchBarHeight: '50px',
        items: [],
        itemTypeTree: [],
        organizations: [],
        packMasters: [],
        suppliers: [],
        batchTactics: [],
        upperTactics: [],
        qcTactics: [],
        pickTactics:[],
        dialogNewVisible: false,
        dialogUpdateVisible: false,
        isOrganization: true,
        turnoverTactics:[],

        form: {
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
          weight: '',
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode:'',
          abcLevel: '',
          turnoverTacticCode: '',
        },
        rules: {
          itemName: [
            {required: true, message: '请输入SKU名称', trigger: 'blur'},
            {validator: validateItemName, trigger: 'blur'}
          ],
          itemTypeId: [
            {required: true, message: '请选择物料种类', trigger: 'blur'}
          ],
          supplierId: [
            {required: false, message: '请选择供应商', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          
          spec: [
            {required: false, message: '请输入规格', trigger: 'blur'}
          ],
          model: [
            {required: false, message: '请输入型号', trigger: 'blur'}
          ],
          packCode: [
            {required: true, message: '请选择包装编码', trigger: 'blur'}
          ],
          upperLimit: [
            {required: false, message: '请输入库存上限', trigger: 'blur'},
            {type: 'number', message: '库存上限必须为数字值', trigger: ['blur', 'change']}
          ],
          floorLimit: [
            {required: false, message: '请输入库存下限', trigger: 'blur'},
            {type: 'number', message: '库存上限必须为数字值', trigger: ['blur', 'change']}
          ],
          weight: [
            {required: false, message: '请输入重量', trigger: 'blur'}
          ],
          batchTacticCode: [
            {required: false, message: '请选择批次策略', trigger: 'blur'}
          ],
          upperTacticCode: [
            {required: false, message: '请选择上架策略', trigger: 'blur'}
          ],
          qcTacticCode: [
            {required: false, message: '请选择质检策略', trigger: 'blur'}
          ],
          abcLevel: [
            {required: false, message: '请选择产品循环级别', trigger: 'blur'}
          ],
          pickTacticCode:[{required:false,message:'请选择拣货策略',trigger:'blur'}],
          turnoverTacticCode:[{required:false,message:'请选择库存周转策略',trigger:'blur'}],
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
          weight: '',
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode:'',
          abcLevel: '',
          turnoverTacticCode:'',
        },
        rules2: {
          itemName: [
            {required: true, message: '请输入SKU名称', trigger: 'blur'},
            {validator: validateItemName2, trigger: 'blur'}
          ],
          itemTypeId: [
            {required: true, message: '请选择物料种类', trigger: 'blur'}
          ],
          supplierId: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
       
          spec: [
            {required: true, message: '请输入规格', trigger: 'blur'}
          ],
          model: [
            {required: true, message: '请输入型号', trigger: 'blur'}
          ],
          packCode: [
            {required: true, message: '请选择包装编码', trigger: 'blur'}
          ],
          upperLimit: [
            {required: true, message: '请输入库存上限', trigger: 'blur'},
            {type: 'number', message: '库存上限必须为数字值', trigger: ['blur', 'change']}
          ],
          floorLimit: [
            {required: true, message: '请输入库存下限', trigger: 'blur'},
            {type: 'number', message: '库存上限必须为数字值', trigger: ['blur', 'change']}
          ],
          weight: [
            {required: true, message: '请输入重量', trigger: 'blur'}
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
          abcLevel: [
            {required: true, message: '请选择产品循环级别', trigger: 'blur'}
          ],
          pickTacticCode:[{required:true,message:'请选择拣货策略',trigger:'blur'}],
          turnoverTacticCode:[{required:true,message:'请选择库存周转策略',trigger:'blur'}],
        },
      }
    },
    watch:{
      newImg(){
        var imgBox = $("#abc");
        uploadImg('#bcd',imgBox);
      }
    },
    computed: {
      ...mapState([
        "abcLevels",
      ]),
    },
    methods: {
      

         uploadImg( ) {
           var imgBox = $("#abc");
            var file = imgBox[0].files[0];
            var imgSrc;
            if (!/image\/\w+/.test(file.type)) {
                alert("看清楚，这个需要图片！");
                return false;
            }
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function() {
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
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getItemData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getItemData()
      },
      clearSearchData() {
        
        this.searchData.keyWords = '';
        this.searchData.itemTypeId = '';
        this.searchData.supplierId = '';
        this.searchData.organizationId = '';
      },
      getItemData() {
        let searchData = JSON.parse(JSON.stringify(this.searchData))
        searchData.itemTypeId = searchData.itemTypeId[searchData.itemTypeId.length - 1]
        IOT.getServerData('/item/infos/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
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
        IOT.showOverlay('保存中，请稍等...');
        
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            
            // delete form.organizationId;
            form.itemTypeId = form.itemTypeId[form.itemTypeId.length - 1]
            
            IOT.getServerData('/item/infos/insert', 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getItemData()
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
            let rowData = PF.JSON(this.rowData);
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
      isDelete(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteItem(id)
          })
          .catch(_ => {
          });
      },
      deleteItem(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/item/infos/delete', 'get', {id: id}, (ret) => {
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
      changeEnableOrDisable(itemCode, type){
        IOT.getServerData('/item/infos/' + type, 'get', {itemCode: itemCode}, (ret) => {
          if (ret.code === 200) {
            if(type === 'enable'){
              IOT.tips(itemCode + ' 已启用！', 'success');
            }else if(type === 'disable'){
              IOT.tips(itemCode + ' 已禁用！', 'error');
            }
            this.getItemData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeItemTypeId(val){
        let tree = PF.JSON(this.itemTypeTree);
        for(let i=0;i<val.length;i++){
          for(let j=0;j<tree.length;j++){
            if(val[i] === tree[j].itemTypeId && PF.isNull(tree[j].children)){
              tree = tree[j].children;
              break
            }else if(val[i] === tree[j].itemTypeId && !PF.isNull(tree[j].children)){
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
        if(udfs[0].value === '1'){
          this.isOrganization = true;
        }else if(udfs[0].value === '0'){
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
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .item{

  }
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
 
</style>
