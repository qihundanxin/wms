<template>
  <div class="batchTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getBatchTacticData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增批次策略" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px" style="margin-top: -10vh">
      <div class="batchTacticMasterBox">
        <el-form class="clearFloat" :model="form.batchTactic" :rules="rules" ref="form.batchTactic">
          <el-form-item label="代码" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-input v-model="form.batchTactic.batchTacticCode" placeholder="请输入代码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="batchTacticName" :label-width="formLabelWidth">
            <el-input v-model="form.batchTactic.batchTacticName" placeholder="请输入名称"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="batchTacticDetailBox clearFloat">
        <ul style="width: 8%;"><li>序号</li><li v-for="(item,index) in form.batchTacticDetails" :key="index">{{index+1}}</li></ul>
        <ul style="width: 20%;"><li>描述</li><li v-for="(item, index) in form.batchTacticDetails" :key="index">
          <el-input v-if="item.detailCode==='memo1'
          || item.detailCode==='memo2'
          || item.detailCode==='memo3'
          || item.detailCode==='batchAttribute1'
          || item.detailCode==='batchAttribute2'" maxlength="7" v-model="item.detailName" size="mini" clearable></el-input>
          <span v-else>{{item.detailName}}</span>
        </li></ul>
        <ul style="width: 10%;"><li>启用</li><li v-for="(item,index) in form.batchTacticDetails" :key="index"><el-switch v-model="item.state" active-value=1 inactive-value=0></el-switch></li></ul>
        <ul style="width: 20%;"><li>RF界面显示</li><li v-for="(item,index) in form.batchTacticDetails" :key="index"><el-input v-model="item.detailRfName" :disabled="item.state === 0 || item.state === '0'" size="mini"></el-input></li></ul>
        <ul style="width: 10%;"><li>RF显示</li><li v-for="(item,index) in form.batchTacticDetails" :key="index">
          <el-checkbox v-model="item.isShow" :disabled="item.state === 0" true-label=1 false-label=0 ></el-checkbox>
        </li></ul>
        <ul style="width: 10%;"><li>是否必输</li><li v-for="(item,index) in form.batchTacticDetails" :key="index">
          <el-checkbox v-model="item.isRequired" :disabled="item.state === 0" true-label=1 false-label=0 ></el-checkbox>
        </li></ul>
        <ul style="width: 20%;"><li>格式</li>
          <li v-for="(item,index) in form.batchTacticDetails" :key="index">
            <el-select style="width: 80%;" v-model="item.format" size="mini" placeholder="请选择格式"
                       :disabled="item.detailCode!=='batchAttribute1'&&item.detailCode!=='batchAttribute2'">
              <el-option
                      v-for="format in formats"
                      :key="format.value"
                      :label="format.label"
                      :value="format.value"
                      v-if="index !== 12 && index !== 13"
                      :disabled="format.value === 4">
              </el-option>
              <el-option
                      v-for="format in formats"
                      :key="format.value"
                      :label="format.label"
                      :value="format.value"
                      v-if="index === 12 || index === 13">
              </el-option>
            </el-select>
            <i @click="showDetailDetailValue(index)" v-if="item.format === 4" class="iconfont icon-xinzeng1 iconBtn"></i>
            <i v-else class="iconfont icon-xinzeng1" style="color: transparent;"></i>
          </li>
        </ul>
        <el-dialog width="30%" :title="form.batchTacticDetails[12].detailName" :visible.sync="innerVisible1" :before-close="handleCloseInnerVisible1" append-to-body>
          <el-form>
            <el-form-item v-for="(item,index) in nowDetailDetails1" :key="index" label="内容" :label-width="formLabelWidth">
              <el-input v-model="item.batchTacticDetailDetailValue"></el-input>
              <i @click="addInput(index,1)" class="iconfont icon-xinzeng1 iconBtn"></i>
              <i @click="deleteInput(index,1)" class="iconfont icon-shanchu iconBtn delete" v-show="nowDetailDetails1.length > 1"></i>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="closeInput(1)">重置</el-button>
            <el-button type="primary" @click="handleCloseInnerVisible1">设 置</el-button>
          </div>
        </el-dialog>
        <el-dialog width="30%" :title="form.batchTacticDetails[13].detailName" :visible.sync="innerVisible2" :before-close="handleCloseInnerVisible2" append-to-body>
          <el-form :model="nowDetailDetails2" ref="nowDetailDetails2">
            <el-form-item v-for="(item,index) in nowDetailDetails2" :key="index" label="内容" :label-width="formLabelWidth">
              <el-input v-model="item.batchTacticDetailDetailValue"></el-input>
              <i @click="addInput(index,2)" class="iconfont icon-xinzeng1 iconBtn"></i>
              <i @click="deleteInput(index,2)" class="iconfont icon-shanchu iconBtn delete" v-show="nowDetailDetails2.length > 1"></i>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="closeInput(2)">重置</el-button>
            <el-button type="primary" @click="handleCloseInnerVisible2">设 置</el-button>
          </div>
        </el-dialog>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form.batchTactic');clearDetails();dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.batchTactic')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改批次策略" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px" style="margin-top: -10vh">
      <div class="batchTacticMasterBox">
        <el-form class="clearFloat" :model="rowData.batchTactic" :rules="rules" ref="rowData.batchTactic">
          <el-form-item label="代码" prop="batchTacticCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.batchTactic.batchTacticCode" placeholder="请输入代码"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="batchTacticName" :label-width="formLabelWidth">
            <el-input v-model="rowData.batchTactic.batchTacticName" placeholder="请输入名称"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="batchTacticDetailBox clearFloat">
        <ul style="width: 8%;"><li>序号</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index">{{index+1}}</li></ul>
        <ul style="width: 20%;"><li>描述</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index">
          <el-input v-if="item.detailCode==='memo1'
          || item.detailCode==='memo2'
          || item.detailCode==='memo3'
          || item.detailCode==='batchAttribute1'
          || item.detailCode==='batchAttribute2'" maxlength="7" v-model="item.detailName" size="mini"></el-input>
          <span v-else>{{item.detailName}}</span>
        </li></ul>
        <ul style="width: 10%;"><li>启用</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index"><el-switch v-model="item.state" active-value=1 inactive-value=0></el-switch></li></ul>
        <ul style="width: 20%;"><li>RF界面显示</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index"><el-input v-model="item.detailRfName" size="mini"></el-input></li></ul>
        <ul style="width: 10%;"><li>RF显示</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index">
          <el-checkbox v-model="item.isShow" true-label=1 false-label=0 ></el-checkbox>
        </li></ul>
        <ul style="width: 10%;"><li>是否必输</li><li v-for="(item,index) in rowData.batchTacticDetails" :key="index">
          <el-checkbox v-model="item.isRequired" true-label=1 false-label=0 ></el-checkbox>
        </li></ul>
        <ul style="width: 20%;"><li>格式</li>
          <li v-for="(item,index) in rowData.batchTacticDetails" :key="index">
            <el-select style="width: 80%;" v-model="item.format" size="mini" placeholder="请选择格式"
                       :disabled="item.detailCode!=='batchAttribute1'&&item.detailCode!=='batchAttribute2'">
              <el-option
                      v-for="format in formats"
                      :key="format.value"
                      :label="format.label"
                      :value="format.value"
                      v-if="index !== 12 && index !== 13"
                      :disabled="format.value === 4">
              </el-option>
              <el-option
                      v-for="format in formats"
                      :key="format.value"
                      :label="format.label"
                      :value="format.value"
                      v-if="index === 12 || index === 13">
              </el-option>
            </el-select>
            <i @click="showDetailDetailValue(index)" v-if="item.format === 4" class="iconfont icon-xinzeng1 iconBtn"></i>
            <i v-else class="iconfont icon-xinzeng1" style="color: transparent;"></i>
          </li>
        </ul>
        <el-dialog width="30%" :title="rowData.batchTacticDetails[12].detailName" :visible.sync="innerVisible1" :before-close="handleCloseInnerVisible1" append-to-body>
          <el-form>
            <el-form-item v-for="(item,index) in nowDetailDetails1" :key="index" label="内容" :label-width="formLabelWidth">
              <el-input v-model="item.batchTacticDetailDetailValue"></el-input>
              <i @click="addInput(index,1)" class="iconfont icon-xinzeng1 iconBtn"></i>
              <i @click="deleteInput(index,1)" class="iconfont icon-shanchu iconBtn delete" v-show="nowDetailDetails1.length > 1"></i>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="closeInput(1)">重置</el-button>
            <el-button type="primary" @click="handleCloseInnerVisible1">设 置</el-button>
          </div>
        </el-dialog>
        <el-dialog width="30%" :title="rowData.batchTacticDetails[13].detailName" :visible.sync="innerVisible2" :before-close="handleCloseInnerVisible2" append-to-body>
          <el-form>
            <el-form-item v-for="(item,index) in nowDetailDetails2" :key="index" label="内容" :label-width="formLabelWidth">
              <el-input v-model="item.batchTacticDetailDetailValue"></el-input>
              <i @click="addInput(index,2)" class="iconfont icon-xinzeng1 iconBtn"></i>
              <i @click="deleteInput(index,2)" class="iconfont icon-shanchu iconBtn delete" v-show="nowDetailDetails2.length > 1"></i>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="closeInput(2)">重置</el-button>
            <el-button type="primary" @click="handleCloseInnerVisible2">设 置</el-button>
          </div>
        </el-dialog>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData.batchTactic');clearDetails();dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData.batchTactic')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
              v-loading="loading.masterTable"
              highlight-current-row
              @current-change="getLookRowData"
              :data="batchTactics"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="batchTacticName"
                label="名称">
        </el-table-column>
        <el-table-column
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button v-if="!scope.row.isSys" @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button  v-if="!scope.row.isSys"  @click="isDelete(scope.row.batchTacticId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
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
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight">
      <div class="organizationRightTitle">批次策略信息</div>
      <p class="MasterBox">
        <span><b>批次策略代码：</b>{{nowBatchTactic.batchTactic.batchTacticCode}}</span>
        <span><b>批次策略描述：</b>{{nowBatchTactic.batchTactic.batchTacticName}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>

      <div class="tableBox" style="height: 400px;overflow: auto">
        <el-table
                v-loading="loading.masterTable"
                highlight-current-row
                :data="nowBatchTactic.batchTacticDetails"
                style="width: 100%">
          <el-table-column
                  type="index"
                  width="50">
          </el-table-column>
          <!--<el-table-column
                sortable
                show-overflow-tooltip
                  label="描述"
                  width="80">
            <template slot-scope="scope">
              <span>{{form.batchTacticDetails[scope.$index].}}</span>
            </template>
          </el-table-column>-->
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="detailName"
                  label="描述">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="state"
                  label="启用">
            <template slot-scope="scope">
              <i v-show="scope.row.state === 1" class="iconfont icon-xuanzhong1"></i>
              <i v-show="scope.row.state === 0" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="detailRfName"
                  label="RF界面显示">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="isShow"
                  label="RF显示">
            <template slot-scope="scope">
              <i v-show="scope.row.isShow === 1" class="iconfont icon-xuanzhong1"></i>
              <i v-show="scope.row.isShow === 0" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="isRequired"
                  label="是否必输">
            <template slot-scope="scope">
              <i v-show="scope.row.isRequired === 1" class="iconfont icon-xuanzhong1"></i>
              <i v-show="scope.row.isRequired === 0" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  prop="format"
                  label="格式">
            <template slot-scope="scope">
              <span v-for="(item,index) in formats" :key="index" v-if="item.value === scope.row.format && scope.row.format !== 4">{{item.label}}</span>
              <span v-for="(value,index) in scope.row.batchTacticDetailDetails" :key="index" v-if="scope.row.format === 4">
                          <i v-if="index !== 0">/</i>
                          {{value.batchTacticDetailDetailValue}}
                      </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'BatchTactic',
    data(){
      let validateBatchTacticCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'batch_tactic', 'batch_tactic_code', this.form.batchTactic.batchTacticCode, 0);
      };
      let validateBatchTacticCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'batch_tactic', 'batch_tactic_code', this.rowData.batchTactic.batchTacticCode, 1, this.rowData.batchTactic.batchTacticId);
      };
      let validateBatchTacticName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'batch_tactic', 'batch_tactic_name', this.form.batchTactic.batchTacticName, 0);
      };
      let validateBatchTacticName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'batch_tactic', 'batch_tactic_name', this.rowData.batchTactic.batchTacticName, 1, this.rowData.batchTactic.batchTacticId);
      };
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },

        batchTactics: [],
        nowBatchTactic: {
          batchTactic: {},
          batchTacticDetails: [],
          batchTacticDetailDetails: []
        },

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        innerVisible1: false,
        innerVisible2: false,

        nowDetailDetails1:[{detailCode: "batchAttribute1",batchTacticDetailDetailValue: ""}],
        nowDetailDetails2:[{detailCode: "batchAttribute2",batchTacticDetailDetailValue: ""}],
        normalFormDetails: [
          { detailName: "生产日期", detailCode: "productDate", batchTacticCode: "", detailRfName: "PR_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "失效日期", detailCode: "exDate", batchTacticCode: "", detailRfName: "EX_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "入库日期", detailCode: "inDate", batchTacticCode: "", detailRfName: "IN_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "供应商代码", detailCode: "supplierCode", batchTacticCode: "", detailRfName: "SP_CODE", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "厂家批号", detailCode: "supplierBatch", batchTacticCode: "", detailRfName: "SP_BATCH", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "质量状态", detailCode: "qcState", batchTacticCode: "", detailRfName: "QC_STATE", format: 1, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "成本单价", detailCode: "costPrice", batchTacticCode: "", detailRfName: "COST_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "销售单价", detailCode: "salePrice", batchTacticCode: "", detailRfName: "SALE_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "单据行编码", detailCode: "detailNo", batchTacticCode: "", detailRfName: "DETAIL_NO", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "单位重量", detailCode: "weight", batchTacticCode: "", detailRfName: "WEIGHT", format: 1, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "", detailCode: "memo1", batchTacticCode: "", detailRfName: "MEMO1", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "", detailCode: "memo2", batchTacticCode: "", detailRfName: "MEMO2", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "", detailCode: "memo3", batchTacticCode: "", detailRfName: "MEMO3", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "", detailCode: "batchAttribute1", batchTacticCode: "", detailRfName: "BATCH_AT_1", format: 0, isShow: 0, isRequired: 0, state: 0 },
          { detailName: "", detailCode: "batchAttribute2", batchTacticCode: "", detailRfName: "BATCH_AT_2", format: 0, isShow: 0, isRequired: 0, state: 0 },
        ],

        form: {
          batchTactic: {
            batchTacticCode: '',
            batchTacticName: ''
          },
          batchTacticDetails: [
            { detailName: "生产日期", detailCode: "productDate", batchTacticCode: "", detailRfName: "PR_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "失效日期", detailCode: "exDate", batchTacticCode: "", detailRfName: "EX_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "入库日期", detailCode: "inDate", batchTacticCode: "", detailRfName: "IN_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "供应商代码", detailCode: "supplierCode", batchTacticCode: "", detailRfName: "SP_CODE", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "厂家批号", detailCode: "supplierBatch", batchTacticCode: "", detailRfName: "SP_BATCH", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "质量状态", detailCode: "qcState", batchTacticCode: "", detailRfName: "QC_STATE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "成本单价", detailCode: "costPrice", batchTacticCode: "", detailRfName: "COST_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "销售单价", detailCode: "salePrice", batchTacticCode: "", detailRfName: "SALE_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "单据行编码", detailCode: "detailNo", batchTacticCode: "", detailRfName: "DETAIL_NO", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "单位重量", detailCode: "weight", batchTacticCode: "", detailRfName: "WEIGHT", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo1", batchTacticCode: "", detailRfName: "MEMO1", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo2", batchTacticCode: "", detailRfName: "MEMO2", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo3", batchTacticCode: "", detailRfName: "MEMO3", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "batchAttribute1", batchTacticCode: "", detailRfName: "BATCH_AT_1", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "batchAttribute2", batchTacticCode: "", detailRfName: "BATCH_AT_2", format: 0, isShow: 0, isRequired: 0, state: 0 },
          ],
          batchTacticDetailDetails: []
        },
        rules:{
          batchTacticCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateBatchTacticCode, trigger: 'blur'  }
          ],
          batchTacticName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateBatchTacticName, trigger: 'blur'  }
          ]
        },
        formLabelWidth: '120px',
        rowData: {
          batchTactic: {
            batchTacticCode: '',
            batchTacticName: ''
          },
          batchTacticDetails: [
            { detailName: "生产日期", detailCode: "productDate", batchTacticCode: "", detailRfName: "PR_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "失效日期", detailCode: "exDate", batchTacticCode: "", detailRfName: "EX_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "入库日期", detailCode: "inDate", batchTacticCode: "", detailRfName: "IN_DATE", format: 2, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "供应商代码", detailCode: "supplierCode", batchTacticCode: "", detailRfName: "SP_CODE", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "厂家批号", detailCode: "supplierBatch", batchTacticCode: "", detailRfName: "SP_BATCH", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "质量状态", detailCode: "qcState", batchTacticCode: "", detailRfName: "QC_STATE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "成本单价", detailCode: "costPrice", batchTacticCode: "", detailRfName: "COST_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "销售单价", detailCode: "salePrice", batchTacticCode: "", detailRfName: "SALE_PRICE", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "单据行编码", detailCode: "detailNo", batchTacticCode: "", detailRfName: "DETAIL_NO", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "单位重量", detailCode: "weight", batchTacticCode: "", detailRfName: "WEIGHT", format: 1, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo1", batchTacticCode: "", detailRfName: "MEMO1", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo2", batchTacticCode: "", detailRfName: "MEMO2", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "memo3", batchTacticCode: "", detailRfName: "MEMO3", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "batchAttribute1", batchTacticCode: "", detailRfName: "BATCH_AT_1", format: 0, isShow: 0, isRequired: 0, state: 0 },
            { detailName: "", detailCode: "batchAttribute2", batchTacticCode: "", detailRfName: "BATCH_AT_2", format: 0, isShow: 0, isRequired: 0, state: 0 },
          ],
          batchTacticDetailDetails: []
        },
        rules2:{
          batchTacticCode: [
            { required: true, message: '请输入编码', trigger: 'blur' },
            { validator: validateBatchTacticCode2, trigger: 'blur'  }
          ],
          batchTacticName: [
            { required: true, message: '请输入名称', trigger: 'blur' },
            { validator: validateBatchTacticName2, trigger: 'blur'  }
          ]
        },
        formats: [
          {
            value: 0,
            label: '字符'
          },
          {
            value: 1,
            label: '数字'
          },
          {
            value: 2,
            label: '日期'
          },
          {
            value: 3,
            label: '日期时间'
          },
          {
            value: 4,
            label: '选择'
          },
        ]
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getBatchTacticData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getBatchTacticData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getBatchTacticData(){
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/batch/tactics/list','get',searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.batchTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0])
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        if(row === null){
          return false
        }
        this.nowBatchTactic.batchTactic = JSON.parse(JSON.stringify(row));
        this.getBatchTacticDetailData(this.nowBatchTactic.batchTactic.batchTacticCode)
        this.loading.masterTable = true;
      },
      getBatchTacticDetailData(code,type){

        IOT.getServerData('/batch/tactic/details/list','get',{batchTacticCode: code},(ret) => {
          if (ret.code === 200) {
            this.loading.masterTable = false;
            if(type === 'update'){
              let list = JSON.parse(JSON.stringify(ret.rows));
              for(let i in list){
                list[i].state = String(list[i].state);
                list[i].isRequired = String(list[i].isRequired);
                list[i].isShow = String(list[i].isShow);
              }
              if(list[12].format === 4){
                this.nowDetailDetails1 = [];
                for(let i in list[12].batchTacticDetailDetails){
                  this.nowDetailDetails1.push({detailCode: 'batchAttribute1',batchTacticDetailDetailValue: list[12].batchTacticDetailDetails[i].batchTacticDetailDetailValue})
                }
              }
              if(list[13].format === 4){
                this.nowDetailDetails2 = [];
                for(let i in list[13].batchTacticDetailDetails){
                  this.nowDetailDetails2.push({detailCode: 'batchAttribute2',batchTacticDetailDetailValue: list[13].batchTacticDetailDetails[i].batchTacticDetailDetailValue})
                }
              }
              this.rowData.batchTacticDetails = list
            }else{
              this.nowBatchTactic.batchTacticDetails = ret.rows;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form.batchTactic');
        this.clearDetails();
        this.dialogNewVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData.batchTactic');
        this.clearDetails();
        this.dialogUpdateVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      clearDetails(){
        this.nowDetailDetails1 = [{detailCode: "batchAttribute1",batchTacticDetailDetailValue: ""}];
        this.nowDetailDetails2 = [{detailCode: "batchAttribute2",batchTacticDetailDetailValue: ""}];

        this.form.batchTacticDetails = PF.JSON(this.normalFormDetails);
        this.form.batchTacticDetailDetails = [];

        this.rowData.batchTacticDetails = PF.JSON(this.normalFormDetails);
        this.rowData.batchTacticDetailDetails = [];
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = JSON.parse(JSON.stringify(this.form));
            let nowDetailDetails1 = JSON.parse(JSON.stringify(this.nowDetailDetails1));
            let nowDetailDetails2 = JSON.parse(JSON.stringify(this.nowDetailDetails2));
            for(let i in form.batchTacticDetails){
              form.batchTacticDetails[i].state = Number(form.batchTacticDetails[i].state);
              form.batchTacticDetails[i].isRequired = Number(form.batchTacticDetails[i].isRequired);
              form.batchTacticDetails[i].isShow = Number(form.batchTacticDetails[i].isShow);
            }
            if(form.batchTacticDetails[12].format === 4){
              for(let i in nowDetailDetails1){
                form.batchTacticDetailDetails.push(nowDetailDetails1[i])
              }
            }
            if(form.batchTacticDetails[13].format === 4){
              for(let i in nowDetailDetails2){
                form.batchTacticDetailDetails.push(nowDetailDetails2[i])
              }
            }
            IOT.getServerData('/batch/tactics/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getBatchTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form.batchTactic');
            this.clearDetails();
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row){
        this.getBatchTacticDetailData(row.batchTacticCode,'update');
        this.rowData.batchTactic = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let rowData = JSON.parse(JSON.stringify(this.rowData));
            let nowDetailDetails1 = JSON.parse(JSON.stringify(this.nowDetailDetails1));
            let nowDetailDetails2 = JSON.parse(JSON.stringify(this.nowDetailDetails2));
            for(let i in rowData.batchTacticDetails){
              rowData.batchTacticDetails[i].state = Number(rowData.batchTacticDetails[i].state);
              rowData.batchTacticDetails[i].isRequired = Number(rowData.batchTacticDetails[i].isRequired);
              rowData.batchTacticDetails[i].isShow = Number(rowData.batchTacticDetails[i].isShow);
            }
            if(rowData.batchTacticDetails[12].format === 4){
              for(let i in nowDetailDetails1){
                rowData.batchTacticDetailDetails.push(nowDetailDetails1[i])
              }
            }
            if(rowData.batchTacticDetails[13].format === 4){
              for(let i in nowDetailDetails2){
                rowData.batchTacticDetailDetails.push(nowDetailDetails2[i])
              }
            }
            IOT.getServerData('/batch/tactics/update','post',rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.nowBatchTactic.batchTactic = {};
                  that.getBatchTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData.batchTactic')
            this.dialogUpdateVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      deleteBatchTactic(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/batch/tactics/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.nowBatchTactic.batchTactic = {};
              that.getBatchTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      isDelete(code){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteBatchTactic(code)
          })
          .catch(_ => {});
      },
      showDetailDetailValue(index){
        if(index === 12){
          this.innerVisible1 = true
        }else if(index === 13){
          this.innerVisible2 = true
        }
        // this.form.batchTacticDetails[index].format = 4
      },
      addInput(index,i){
        if(i === 1){
          this.nowDetailDetails1.splice(index+1, 0, {detailCode: "batchAttribute1",batchTacticDetailDetailValue: ""});
        }else if(i === 2){
          this.nowDetailDetails2.splice(index+1, 0, {detailCode: "batchAttribute2",batchTacticDetailDetailValue: ""});
        }
      },
      deleteInput(index,i){
        if(i === 1){
          this.nowDetailDetails1.splice(index,1)
        }else if(i === 2){
          this.nowDetailDetails2.splice(index,1)
        }
      },
      handleCloseInnerVisible1(){
        for(let i in this.nowDetailDetails1){
          if(this.nowDetailDetails1[i].batchTacticDetailDetailValue === ''){
              IOT.tips('不能为空！', 'error');
            return false
          }
        }
        this.form.batchTacticDetails[12].format = 4;
        this.innerVisible1 = false
      },
      handleCloseInnerVisible2(){
        for(let i in this.nowDetailDetails2){
          if(this.nowDetailDetails2[i].batchTacticDetailDetailValue === ''){
              IOT.tips('不能为空！', 'error');
            return false
          }
        }
        this.form.batchTacticDetails[13].format = 4;
        this.innerVisible2 = false
      },
      closeInput(i){
        if(i === 1){
          this.nowDetailDetails1 = [{detailCode: "batchAttribute1",batchTacticDetailDetailValue: ''}]
          this.form.batchTacticDetails[12].format = 0;
          this.innerVisible1 = false
        }else if(i === 2){
          this.nowDetailDetails2 = [{detailCode: "batchAttribute2",batchTacticDetailDetailValue: ''}]
          this.form.batchTacticDetails[13].format = 0;
          this.innerVisible2 = false
        }
      },
      getSysParamMasterDetailData(){
        IOT.getServerData('/sys/params','get',{moduleCode: "tactic"},(ret) => {
          if (ret.code === 200) {
            let sysParams = ret.rows;
            for(let i in this.form.batchTacticDetails){
              for(let j in sysParams){
                if(this.form.batchTacticDetails[i].detailCode === sysParams[j].paramCode){
                  this.form.batchTacticDetails[i].detailName = sysParams[j].value;
                  this.rowData.batchTacticDetails[i].detailName = sysParams[j].value;
                  this.normalFormDetails[i].detailName = sysParams[j].value;
                }
              }
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getBatchTacticData();
      this.getSysParamMasterDetailData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .batchTactic{

  }
</style>
