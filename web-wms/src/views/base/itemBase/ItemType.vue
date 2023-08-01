<template>
  <div class="itemType">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
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
      <!-- <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增 -->
      <!-- </el-button> -->

      <el-upload style="display:inline-block; margin-left:20px" class="upload-demo"
        action="http://localhost:8003/wms/item/types/uploadItemTypes" :on-preview="handlePreview" :on-remove="handleRemove"
        :before-remove="beforeRemove" :on-exceed="handleExceed" :file-list="fileList" name="model">
        <el-button size="small" type="primary" round>点击上传</el-button>
      </el-upload>
    </div>

    <el-dialog title="新增物料种类" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px"
               style="margin-top: -10vh;">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="父级" prop="parentId" :label-width="formLabelWidth">
          <el-select v-model="form.parentId" disabled placeholder="无父级" style="width: 70%;">
            <el-option
              v-for="itemType in itemTypes"
              :key="itemType.itemTypeId"
              :label="itemType.itemTypeName"
              :value="itemType.itemTypeId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="itemTypeCode" :label-width="formLabelWidth">
          <el-input v-model="form.itemTypeCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="itemTypeName" :label-width="formLabelWidth">
          <el-input v-model="form.itemTypeName" placeholder="请输入名称"></el-input>
        </el-form-item>
       <!-- <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="form.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item> -->
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
        <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
          <el-select v-model="form.pickTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
            <el-option
                    v-for="item in pickTactics"
                    :key="item.allocationRuleCode"
                    :label="item.description"
                    :value="item.allocationRuleCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="form.memo" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改物料种类" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px">
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item label="父级" prop="parentId" :label-width="formLabelWidth">
          <el-select v-model="rowData.parentId" disabled placeholder="无父级" style="width: 70%;">
            <el-option
              v-for="itemType in itemTypes"
              :key="itemType.itemTypeId"
              :label="itemType.itemTypeName"
              :value="itemType.itemTypeId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="itemTypeCode" :label-width="formLabelWidth">
          <el-input v-model="rowData.itemTypeCode" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="itemTypeName" :label-width="formLabelWidth">
          <el-input v-model="rowData.itemTypeName" placeholder="请输入名称"></el-input>
        </el-form-item>
       <!-- <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="rowData.organizationId" filterable placeholder="请选择货主" style="width: 70%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item> -->
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
        <el-form-item label="拣货策略" prop="pickTacticCode" :label-width="formLabelWidth">
          <el-select v-model="rowData.pickTacticCode" filterable placeholder="请选择合并策略" style="width: 70%;">
            <el-option
                    v-for="item in pickTactics"
                    :key="item.allocationRuleCode"
                    :label="item.description"
                    :value="item.allocationRuleCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
          <el-input v-model="rowData.memo" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="选择新增级别" :visible.sync="dialogNew2Visible" width="300px" style="text-align: center;">
      <el-button type="info"
                 @click="form.parentId = new2Info.parentId;dialogNew2Visible = false;dialogNewVisible = true">同 级
      </el-button>
      <el-button type="primary"
                 @click="form.parentId = new2Info.itemTypeId;dialogNew2Visible = false;dialogNewVisible = true">下 级
      </el-button>
    </el-dialog>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="itemTypeTree"
        max-height="600"
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        style="width: 100%">
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemTypeName"
          label="名称">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemTypeCode"
          label="编码">
        </el-table-column>
       <!-- <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主">
        </el-table-column> -->
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
          prop="memo"
          label="备注">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150">
          <template slot-scope="scope">
            <el-button @click="new2Info = scope.row;dialogNew2Visible = true" icon="iconfont icon-xinzeng" type="info"
                       circle plain size="small"></el-button>
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain
                       size="small"></el-button>
            <el-button @click="isDelete(scope.row.itemTypeId)" icon="iconfont icon-shanchu" type="danger" circle plain
                       size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ItemType',
    data() {
      let validateItemTypeCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_type', 'item_type_code', this.form.itemTypeCode, 0);
      };
      let validateItemTypeCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_type', 'item_type_code', this.rowData.itemTypeCode, 1, this.rowData.itemTypeId);
      };
      let validateItemTypeName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_type', 'item_type_name', this.form.itemTypeName, 0);
      };
      let validateItemTypeName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'item_type', 'item_type_name', this.rowData.itemTypeName, 1, this.rowData.itemTypeId);
      };
      return {
        isMoreSearch: false,

        searchData: {
          keyWords: '',
          organizationId: '',
        },
        itemTypes: [],
        itemTypeTree: [],
        organizations: [],
        batchTactics: [],
        upperTactics: [],
        qcTactics: [],
        pickTactics:[],

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        dialogNew2Visible: false,
        fileList:[],
        form: {
          parentId: null,
          itemTypeCode: '',
          itemTypeName: '',
          organizationId: '',
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode:'',
          memo: '',
        },
        rules: {
          itemTypeCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateItemTypeCode, trigger: 'blur'}
          ],
          itemTypeName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateItemTypeName, trigger: 'blur'}
          ],
          organizationId: [
            {required: false, message: '请选择货主', trigger: 'blur'}
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
          pickTacticCode:[{required:false,message:'请选择拣货策略',trigger:'blur'}],
        },
        formLabelWidth: '120px',

        rowData: {
          parentId: null,
          itemTypeCode: '',
          itemTypeName: '',
          organizationId: '',
          batchTacticCode: '',
          upperTacticCode: '',
          qcTacticCode: '',
          pickTacticCode:'',
          memo: '',
        },
        rules2: {
          itemTypeCode: [
            {required: true, message: '请输入编码', trigger: 'blur'},
            {validator: validateItemTypeCode2, trigger: 'blur'}
          ],
          itemTypeName: [
            {required: true, message: '请输入名称', trigger: 'blur'},
            {validator: validateItemTypeName2, trigger: 'blur'}
          ],
          organizationId: [
            {required: false, message: '请选择货主', trigger: 'blur'}
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
          pickTacticCode:[{required:true,message:'请选择拣货策略',trigger:'blur'}],
        },

        new2Info: {}
      }
    },
    methods: {

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


      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.organizationId = '';
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getItemTypeData();
      },
      getItemTypeData() {
        IOT.getServerData('/item/types/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for (let i = 0; i < list.length; i++) {
              list[i].id = list[i].itemTypeId;
            }
            this.itemTypes = list;
            this.itemTypeTree = PF.toTree(list);
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
            IOT.getServerData('/item/types/insert', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getItemTypeData()
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
            
            IOT.getServerData('/item/types/update', 'post', this.rowData, (ret) => {
              IOT.hideOverlay();

              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  that.getItemTypeData()
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
            this.deleteItemType(id)
          })
          .catch(_ => {
          });
      },
      deleteItemType(id) {
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/item/types/delete', 'get', {itemTypeId: id}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000, () => {
              that.getItemTypeData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getPickTacticData();
      this.getItemTypeData();
      PF.getOrganizations((rows) => {
       this.organizations = rows;
      });
      this.getBatchTacticData();
      this.getUpperTacticData();
      this.getQcTacticData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .itemType {

  }
</style>
