<template>
  <div class="area">

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
          <span>类型</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="请选择类型" clearable>
            <el-option
                v-for="item in areaTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
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

    <el-dialog title="新增货区" :visible.sync="dialogNewVisible" :before-close="handleClose" width="700px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div class="areaModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{ form.wareName }}</span>
          </el-form-item>
          <!--     <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
                 <el-select v-model="form.type" filterable placeholder="请选择类型" style="width: 90%;">
                   <el-option
                     v-for="item in areaTypes"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value">
                   </el-option>
                 </el-select>
               </el-form-item> -->
          <el-form-item label="编码" prop="areaCode" :label-width="formLabelWidth">
            <el-input v-model="form.areaCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="areaName" :label-width="formLabelWidth">
            <el-input v-model="form.areaName" placeholder="请输入名称" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="优先级" prop="priorityLevel" :label-width="formLabelWidth">
            <el-input-number v-model="form.priorityLevel" :min="0" label="请输入优先级" style="width: 90%;"></el-input-number>
          </el-form-item>
          <!--<el-form-item label="拣货过渡货位" prop="pickTransitCell" :label-width="formLabelWidth">
            <el-input v-model="form.pickTransitCell" placeholder="请输入拣货过渡货位" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="入库过渡货位" prop="storageTransitCell" :label-width="formLabelWidth">
            <el-input v-model="form.storageTransitCell" placeholder="请输入入库过渡货位" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货区基点" prop="areaCentre" :label-width="formLabelWidth">
            <el-input v-model="form.areaCentre" placeholder="请输入货区基点" style="width: 90%;"></el-input>
          </el-form-item>
          <div style="width: 100%;float:left;"></div>
          <el-form-item label="" :label-width="formLabelWidth">
            <el-checkbox v-model="form.boxReplenish">整箱补货来源</el-checkbox>
          </el-form-item>
          <el-form-item label="" :label-width="formLabelWidth">
            <el-checkbox v-model="form.scatteredReplenish">拆零补货来源</el-checkbox>
          </el-form-item> -->
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改货区" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="700px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div class="areaModalBox">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData">
          <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
            <span>{{ rowData.wareName }}</span>
          </el-form-item>
          <!-- <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
             <el-select v-model="rowData.type" filterable placeholder="请选择类型" style="width: 90%;">
               <el-option
                 v-for="item in areaTypes"
                 :key="item.value"
                 :label="item.label"
                 :value="item.value">
               </el-option>
             </el-select>
           </el-form-item> -->
          <el-form-item label="编码" prop="areaCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.areaCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="areaName" :label-width="formLabelWidth">
            <el-input v-model="rowData.areaName" placeholder="请输入名称" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="优先级" prop="priorityLevel" :label-width="formLabelWidth">
            <el-input-number v-model="rowData.priorityLevel" :min="0" label="请输入优先级"
                             style="width: 90%;"></el-input-number>
          </el-form-item>
          <!--<el-form-item label="拣货过渡货位" prop="pickTransitCell" :label-width="formLabelWidth">
            <el-input v-model="rowData.pickTransitCell" placeholder="请输入拣货过渡货位" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="入库过渡货位" prop="storageTransitCell" :label-width="formLabelWidth">
            <el-input v-model="rowData.storageTransitCell" placeholder="请输入入库过渡货位" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="货区基点" prop="areaCentre" :label-width="formLabelWidth">
            <el-input v-model="rowData.areaCentre" placeholder="请输入货区基点" style="width: 90%;"></el-input>
          </el-form-item>
          <div style="width: 100%;float:left;"></div>
          <el-form-item label="" :label-width="formLabelWidth">
            <el-checkbox v-model="rowData.boxReplenish">整箱补货来源</el-checkbox>
          </el-form-item>
          <el-form-item label="" :label-width="formLabelWidth">
            <el-checkbox v-model="rowData.scatteredReplenish">拆零补货来源</el-checkbox>
          </el-form-item> -->
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
          :data="areas"
          style="width: 100%">
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            sortable
            prop="areaName"
            label="名称"
            min-width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="areaCode"
            label="编码"
            min-width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="wareName"
            label="仓库"
            width="220">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="priorityLevel"
            label="优先级"
            min-width="100">
        </el-table-column>
        <!--<el-table-column
          sortable
          show-overflow-tooltip
          prop="type"
          label="货区类型"
          min-width="180">
          <template slot-scope="scope">
            <span v-for="(item, index) in areaTypes" :key="index" v-if="item.value === scope.row.type">{{item.label}}</span>
          </template>
        </el-table-column> -->
        <!--<el-table-column
                sortable
                show-overflow-tooltip
                prop="pickTransitCell"
                label="拣货过渡货位"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="storageTransitCell"
                label="入库过渡货位"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="areaCentre"
                label="货区基点"
                width="180">
        </el-table-column>-->
        <!-- <el-table-column
           sortable
           show-overflow-tooltip
           prop="boxReplenish"
           label="整箱补货来源"
           width="130">
           <template slot-scope="scope">
             <i v-show="scope.row.boxReplenish" class="iconfont icon-xuanzhong1"></i>
             <i v-show="!scope.row.boxReplenish" class="iconfont icon-weixuanzhong"></i>
           </template>
         </el-table-column>
         <el-table-column
           sortable
           show-overflow-tooltip
           prop="scatteredReplenish"
           label="拆零补货来源"
           width="130">
           <template slot-scope="scope">
             <i v-show="scope.row.scatteredReplenish" class="iconfont icon-xuanzhong1"></i>
             <i v-show="!scope.row.scatteredReplenish" class="iconfont icon-weixuanzhong"></i>
           </template>
         </el-table-column> -->
        <el-table-column
            fixed="right"
            label="操作"
            min-width="110">
          <template slot-scope="scope">
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                       class="tableBtn"></el-button>
            <el-button @click="isDelete(scope.row.areaId)" icon="iconfont icon-shanchu" type="text"
                       class="tableBtn danger"></el-button>
          </template>

        </el-table-column>
      </el-table>
      <el-pagination
          class="pagination-bottom"
          style="text-align: right;"
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
export default {
  name: 'Area',
  data() {
    let validateAreaCodeNew = (rule, value, callback) => {
      PF.validatorNew(rule, value, callback, 'areaCode', this.form.wareId);
    };
    let validateAreaNameNew = (rule, value, callback) => {
      PF.validatorNew(rule, value, callback, 'areaName', this.form.wareId);
    };
    let validateAreaCode = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'area_info', 'area_code', this.form.areaCode, 0);
    };
    let validateAreaCode2 = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'area_info', 'area_code', this.rowData.areaCode, 1, this.rowData.areaId);
    };
    let validateAreaName = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'area_info', 'area_name', this.form.areaName, 0);
    };
    let validateAreaName2 = (rule, value, callback) => {
      PF.validator(rule, value, callback, 'area_info', 'area_name', this.rowData.areaName, 1, this.rowData.areaId);
    };
    return {
      isMoreSearch: false,

      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        type: null,
      },

      areas: [],
      wares: [],

      dialogNewVisible: false,
      dialogUpdateVisible: false,


      form: {
        wareId: PF.getLocal('wareId', 'number'),
        wareName: PF.getLocal('wareName'),
        areaCode: '',
        areaName: '',
        priorityLevel: 0,
        type: null,
        boxReplenish: false,
        scatteredReplenish: false,
        pickTransitCell: '',
        storageTransitCell: '',
        areaCentre: '',
        memo: '',
      },
      rules: {
        wareId: [
          {required: true, message: '请选择仓库', trigger: 'blur'}
        ],
        areaCode: [
          {required: true, message: '请输入编码', trigger: 'blur'},
          {validator: validateAreaCodeNew, trigger: 'blur'}
        ],
        areaName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          // {validator: validateAreaNameNew, trigger: 'blur'}
        ],
        priorityLevel: [
          {required: true, message: '请输入优先级', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '请选择货区类型', trigger: 'blur'}
        ],

      },
      formLabelWidth: '120px',

      rowData: {
        wareId: null,
        areaCode: '',
        areaName: '',
        priorityLevel: 0,
        type: null,
        boxReplenish: false,
        scatteredReplenish: false,
        pickTransitCell: '',
        storageTransitCell: '',
        areaCentre: '',
        memo: '',
      },
      rules2: {
        wareId: [
          {required: true, message: '请选择仓库', trigger: 'blur'}
        ],
        areaCode: [
          {required: true, message: '请输入编码', trigger: 'blur'},
        ],
        areaName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
        ],
        priorityLevel: [
          {required: true, message: '请输入优先级', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '请选择货区类型', trigger: 'blur'}
        ],

      },
      areaTypes: [
        {
          value: 1,
          label: '收货区'
        },
        {
          value: 2,
          label: '仓储区'
        },
        {
          value: 3,
          label: '拣货区'
        },
        {
          value: 4,
          label: '集货区'
        },
        {
          value: 5,
          label: '移动货位区'
        },
      ]
    }
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getAreaData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getAreaData()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      // this.searchData.wareId = null;
      this.searchData.type = null;
    },
    setPageNumFlush(){
      this.searchData.pageNum =1;
      this.getAreaData();
    },
    getAreaData() {
      IOT.getServerData('/area/infos/list', 'get', this.searchData, (ret) => {
        if (ret.code === 200) {
          this.areas = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber
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
    commitForm(formName) {
      let that = this;
      IOT.showOverlay('保存中，请稍等...');
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData('/area/infos/insert', 'post', this.form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getAreaData()
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
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData('/area/infos/update', 'post', this.rowData, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getAreaData()
              });
              this.resetForm('rowData')
              this.dialogUpdateVisible = false
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
      this.dialogUpdateVisible = true
    },
    resetForm(formName) { // 重置
      if (formName === 'form') {
        this.form.boxReplenish = false;
        this.form.scatteredReplenish = false;
      } else if (formName === 'rowData') {
        this.rowData.boxReplenish = false;
        this.rowData.scatteredReplenish = false;
      }
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
            this.deleteArea(id)
          })
          .catch(_ => {
          });
    },
    deleteArea(id) {
      let that = this;
      IOT.showOverlay('删除中，请稍等...');
      IOT.getServerData('/area/infos/delete', 'get', {id: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips('删除成功！', 'success', 1000, () => {
            that.getAreaData()
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },

  },
  created() {
    this.getAreaData();
    this.getWareData();
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.area {

}
</style>
