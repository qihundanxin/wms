<template>
  <div class="qcTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getQcTacticData" size="mini" icon="el-icon-search">查询</el-button>
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
        <el-form-item label="编码" prop="qcTacticCode" :label-width="formLabelWidth">
          <el-input v-model="form.qcTacticCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="qcTacticName" :label-width="formLabelWidth">
          <el-input v-model="form.qcTacticName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="质检类型" prop="qcType" :label-width="formLabelWidth">
          <el-select v-model="form.qcType" filterable placeholder="请选择质检类型"
                     style="width: 70%;">
            <el-option
              v-for="item in qcTypes"
              :key="item.dataDictDetailId"
              :label="item.dictValue"
              :value="item.dataDictDetailId">
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
        <el-form-item label="编码" prop="qcTacticCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.qcTacticCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="qcTacticName" :label-width="formLabelWidth">
          <el-input v-model="rowData.qcTacticName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="质检类型" prop="qcType" :label-width="formLabelWidth">
          <el-select v-model="rowData.qcType" filterable placeholder="请选择质检类型"
                     style="width: 70%;">
            <el-option
              v-for="item in qcTypes"
              :key="item.dataDictDetailId"
              :label="item.dictValue"
              :value="item.dataDictDetailId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为策略 '+detailForm.qcTacticName+' 新增详情'" :visible.sync="dialogNewDetailVisible"
               :before-close="handleCloseDetail" width="500px" style="margin-top: -10vh">
      <el-form class="clearFloat" :model="detailForm" :rules="rulesDetail" ref="detailForm">
        <el-form-item label="入库单类型" prop="asnType" :label-width="formLabelWidth">
          <el-select v-model="detailForm.asnType" filterable placeholder="请选择入库单类型" style="width: 70%;">
            <el-option :key="-1" label="所有类型" :value="-1"></el-option>
            <el-option
              v-for="item in asnTypes"
              :key="item.dataDictDetailId"
              :label="item.dictValue"
              :value="item.dataDictDetailId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批量起始数量" prop="startQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.startQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="批量结束数量" prop="endQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.endQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="最小质检数量" prop="minQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.minQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
       <!-- <el-form-item label="样本数量" prop="sampleQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.sampleQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>-->
        <el-form-item label="检验百分比(%)" prop="percentage" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.percentage" :min="0" :max="100" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="最小合格率(%)" prop="percentage" :label-width="formLabelWidth">
          <el-input-number v-model="detailForm.minPassRate" :min="0" :max="100" style="width: 70%;"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailForm');dialogNewDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitDetailForm('detailForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'为策略 '+detailForm.qcTacticName+' 修改详情'" :visible.sync="dialogUpdateDetailVisible"
               :before-close="handleCloseDetail2" width="500px" style="margin-top: -10vh">
      <el-form class="clearFloat" :model="detailRowData" :rules="rulesDetail2" ref="detailRowData">
        <el-form-item label="入库单类型" prop="asnType" :label-width="formLabelWidth">
          <el-select v-model="detailRowData.asnType" filterable placeholder="请选择入库单类型" style="width: 70%;">
            <el-option :key="-1" label="所有类型" :value="-1"></el-option>
            <el-option
              v-for="item in asnTypes"
              :key="item.dataDictDetailId"
              :label="item.dictValue"
              :value="item.dataDictDetailId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批量起始数量" prop="startQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailRowData.startQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="批量结束数量" prop="endQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailRowData.endQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="最小质检数量" prop="minQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="detailRowData.minQuantity" :min="0" style="width: 70%;"></el-input-number>
        </el-form-item>
<!--        <el-form-item label="样本数量" prop="sampleQuantity" :label-width="formLabelWidth">-->
<!--          <el-input-number v-model="detailRowData.sampleQuantity" :min="0" style="width: 70%;"></el-input-number>-->
<!--        </el-form-item>-->
        <el-form-item label="检验百分比(%)" prop="percentage" :label-width="formLabelWidth">
          <el-input-number v-model="detailRowData.percentage" :min="0" :max="100" style="width: 70%;"></el-input-number>
        </el-form-item>
        <el-form-item label="最小合格率(%)" prop="percentage" :label-width="formLabelWidth">
          <el-input-number v-model="detailRowData.minPassRate" :min="0" :max="100" style="width: 70%;"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('detailRowData');dialogUpdateDetailVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateDetailForm('detailRowData')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
        v-loading="loading.masterTable"
        highlight-current-row
        @current-change="getLookRowData"
        :data="qcTactics"
        style="width: 100%">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qcTacticName"
          label="名称">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.qcTacticId)" icon="iconfont icon-shanchu" type="danger" circle
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
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight">
      <div class="organizationRightTitle">策略信息</div>
      <p class="MasterBox">
        <span><b>策略编码：</b>{{lookRowData.qcTacticCode}}</span>
        <span><b>策略名称：</b>{{lookRowData.qcTacticName}}</span>
      </p>
      <p class="MasterBox">
        <span><b>质检类型：</b>
          <span v-for="(item,index) in qcTypes" :key="index" v-if="item.dataDictDetailId === lookRowData.qcType">{{item.dictValue}}</span>
        </span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>
      <div class="headerBtns">
        <el-button @click="showDetailModal" size="small" plain
                   icon="iconfont icon-xinzeng">新增详情
        </el-button>
      </div>
      <div class="tableBox">
        <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="qcTacticDetails"
          style="width: 100%">
          <el-table-column
            fixed="left"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="asnType"
            label="入库单类型"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.asnType === -1">所有类型</span>
              <span v-for="(item,index) in asnTypes" :key="index" v-if="item.dataDictDetailId === scope.row.asnType">{{item.dictValue}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="startQuantity"
            label="批量起始数量"
            width="150">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="endQuantity"
            label="批量结束数量"
            width="150">
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="minQuantity"
            label="最小质检数量"
            width="150">
          </el-table-column>
<!--          <el-table-column-->
<!--            sortable-->
<!--            show-overflow-tooltip-->
<!--            prop="sampleQuantity"-->
<!--            label="样本数量"-->
<!--            width="150">-->
<!--          </el-table-column>-->
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="percentage"
            label="检验百分比(%)"
            width="150">
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="110">
            <template slot-scope="scope">
              <el-button @click="showUpdateDetailModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle
                         plain size="small"></el-button>
              <el-button @click="isDeleteDetail(scope.row.qcTacticDetailId)" icon="iconfont icon-shanchu"
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

  export default {
    name: 'QcTactic',
    data() {
      let validateQcTacticCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'qc_tactic', 'qc_tactic_code', this.form.qcTacticCode, 0);
      };
      let validateQcTacticCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'qc_tactic', 'qc_tactic_code', this.rowData.qcTacticCode, 1, this.rowData.qcTacticId);
      };
      let validateQcTacticName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'qc_tactic', 'qc_tactic_name', this.form.qcTacticName, 0);
      };
      let validateQcTacticName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'qc_tactic', 'qc_tactic_name', this.rowData.qcTacticName, 1, this.rowData.qcTacticId);
      };
      let validateQuantityNo0 = (rule, value, callback) => {
        if (value > 0) {
          callback();
        } else {
          callback(new Error('数量不得为0!'));
        }
      };
      let validateEndQuantity = (rule, value, callback) => {
        if (this.detailForm.endQuantity >= this.detailForm.startQuantity) {
          callback();
        } else {
          callback(new Error('结束数量不得小于起始数量!'));
        }
      };
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
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

        qcTactics: [],
        qcTacticDetails: [],
        lookRowData: {},
        qcTypes: [],
        asnTypes: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogNewDetailVisible: false,
        dialogUpdateDetailVisible: false,

        notLimit: '#E6A23C',

        form: {
          qcTacticCode: '',
          qcTacticName: '',
          qcType: null,
        },
        rules: {
          qcTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateQcTacticCode, trigger: 'blur'}
          ],
          qcTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateQcTacticName, trigger: 'blur'}
          ],
          qcType: [
            {required: true, message: '请选择质检类型', trigger: 'blur'},
          ],
        },
        formLabelWidth: '150px',

        rowData: {
          qcTacticCode: '',
          qcTacticName: '',
          qcType: null,
        },
        rules2: {
          qcTacticCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateQcTacticCode2, trigger: 'blur'}
          ],
          qcTacticName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateQcTacticName2, trigger: 'blur'}
          ],
          qcType: [
            {required: true, message: '请选择质检类型', trigger: 'blur'},
          ],
        },
        detailForm: {
          qcTacticCode: null,
          asnType: null,
          startQuantity: null,
          endQuantity: null,
          minQuantity: null,
          sampleQuantity: null,
          percentage: null,
        },
        rulesDetail: {
          asnType: [
            {required: true, message: '请选择入库单类型', trigger: 'blur'}
          ],
          startQuantity: [
            {required: true, message: '请输入批量起始数量', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
          endQuantity: [
            {required: true, message: '请输入批量结束数量', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'},
            {validator: validateEndQuantity, trigger: 'blur'}
          ],
          minQuantity: [
            {required: true, message: '请输入最小质检数量', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
       /*   sampleQuantity: [
            {required: true, message: '请输入样本数量', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],*/
          percentage: [
            {required: true, message: '请输入检验百分比(%)', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
          minPassRate: [
            {required: true, message: '请输入最小合格率(%)', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
        },
        detailRowData: {
          qcTacticCode: null,
          asnType: null,
          startQuantity: null,
          endQuantity: null,
          minQuantity: null,
          sampleQuantity: null,
          percentage: null,
          minPassRate:null,
        },
        rulesDetail2: {
          asnType: [
            {required: true, message: '请选择入库单类型', trigger: 'blur'}
          ],
          startQuantity: [
            {required: true, message: '请输入批量起始数量', trigger: 'blur'}
          ],
          endQuantity: [
            {required: true, message: '请输入批量结束数量', trigger: 'blur'}
          ],
          minQuantity: [
            {required: true, message: '请输入最小质检数量', trigger: 'blur'}
          ],
         /* sampleQuantity: [
            {required: true, message: '请输入样本数量', trigger: 'blur'}
          ],*/
          percentage: [
            {required: true, message: '请输入检验百分比(%)', trigger: 'blur'}
          ],
          minPassRate: [
            {required: true, message: '请输入最小合格率(%)', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getQcTacticData();
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getQcTacticData()
      },
      handleCurrentChange2(val) {
        this.searchData2.pageNum = val;
        this.getQcTacticDetailData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      getQcTacticData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/qc/tactics/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.qcTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getQcTacticDetailData(code) {
        this.searchData2.qcTacticCode = code;
        let that = this;
        that.loading.masterTable = true;
        let searchData2 = PF.JSON(this.searchData2);
        IOT.getServerData('/qc/tactic/details/list', 'get', searchData2, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.qcTacticDetails = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row) {
        this.lookRowData = JSON.parse(JSON.stringify(row))
        this.getQcTacticDetailData(this.lookRowData.qcTacticCode)
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/qc/tactics/insert', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getQcTacticData()
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
            IOT.getServerData('/qc/tactics/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getQcTacticData()
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
            this.deleteQcTactic(id)
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
      deleteQcTactic(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/qc/tactics/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getQcTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      deleteDetail(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/qc/tactic/details/delete', 'get', {id: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getQcTacticDetailData(that.lookRowData.qcTacticCode)
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      showDetailModal() {
        this.detailForm.qcTacticCode = this.lookRowData.qcTacticCode;
        this.detailForm.qcTacticName = this.lookRowData.qcTacticName;
        this.dialogNewDetailVisible = true;
      },
      commitDetailForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/qc/tactic/details/insert', 'post', this.detailForm, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getQcTacticDetailData(that.lookRowData.qcTacticCode)
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
      },
      updateDetailForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/qc/tactic/details/update', 'post', this.detailRowData, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getQcTacticDetailData(that.lookRowData.qcTacticCode)
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
      },
      showUpdateDetailModal(row) {
        this.detailRowData = PF.JSON(row);
        this.dialogUpdateDetailVisible = true
      },
    },
    created() {
      this.getQcTacticData();
      PF.getDataDictUdfData('qcType', (rows) => {
        this.qcTypes = rows;
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
  .qcTactic {

  }
</style>
