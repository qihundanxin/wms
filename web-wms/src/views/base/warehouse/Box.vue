<template>
  <div class="box">

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
                v-for="item in boxTypes"
                :key="item.value"
                :label="item.text"
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

    <el-dialog title="新增容器" :visible.sync="dialogNewVisible" :before-close="handleClose" width="700px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div class="areaModalBox">
        <el-form class="clearFloat" :model="form" :rules="rules2" ref="form">
          
          <el-form-item label="编码" prop="boxCode" :label-width="formLabelWidth">
            <el-input v-model="form.boxCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
             <el-select v-model="form.type" placeholder="请选择"  >
                            <el-option
                                    
                                     key=0
                                     label="先拣后分"
                                     value=0>
                            </el-option>
                            <el-option
                                    
                                     key=1
                                     label="边拣边分"
                                     value=1>
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
    <el-dialog title="修改容器" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="700px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div class="areaModalBox">
        <el-form class="clearFloat" :model="rowData" :rules="rules2" ref="rowData"> 
          <el-form-item label="编码" prop="boxCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.boxCode" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item> 
           <el-form-item label="类型" prop="type" :label-width="formLabelWidth">
             <el-select v-model="rowData.type" placeholder="请选择"  >
                            <el-option
                                    
                                     key=0
                                     label="先拣后分"
                                     value=0>
                            </el-option>
                            <el-option
                                    
                                     key=1
                                     label="边拣边分"
                                     value=1>
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
      @selection-change="handleSelectionChange"
          highlight-current-row
          :data="boxs"
          style="width: 100%">
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column> 
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="boxCode"
            label="编码"
            min-width="180">
        </el-table-column> 
         <el-table-column
            sortable
            show-overflow-tooltip
            prop="createTime"
            label="创建时间"
            min-width="180">
        </el-table-column> 
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="type"
            label="类型"
            min-width="180">
             <template slot-scope="scope">
                         <span v-if="scope.row.type===0">先拣后分</span>
                        
                         <span v-if="scope.row.type===1">边拣边分</span>
                       
                    </template>
        </el-table-column> 
        <el-table-column
            fixed="right"
            label="操作"
            min-width="110">
          <template slot-scope="scope">
             <el-dropdown style="margin-left:10px;margin-right:10px" size="mini" type="primary"
                         icon="iconfont icon-print" @click="demoPrint(scope.row.boxCode)" split-button>
              打印
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.boxCode,1)">打印预览1
                  </el-button>
                </el-dropdown-item>

                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.boxCode,2)">打印预览2
                  </el-button>
                </el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
            
            <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                       class="tableBtn"></el-button>
            <el-button @click="isDelete(scope.row.boxId)" icon="iconfont icon-shanchu" type="text"
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
  name: 'box',
  data() {
    
    
    
    return {
      selectBoxs:[],
      isMoreSearch: false,
      wareId: PF.getLocal('wareId', 'number'),

      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        type: null,
      },

      boxs: [],
      wares: [],
      boxTypes:[
        {
          text:"先拣后分",
          value:0,
        },
        {
          text:"边拣边分",
          value:1,
        }
      ],

      dialogNewVisible: false,
      dialogUpdateVisible: false,


      form: {
        wareId: PF.getLocal('wareId', 'number'), 
        boxCode: '',
        
      },
      rules: {
        
        

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
          boxCode: [
                        {required: true, message: '请填写编码', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择类型', trigger: 'blur'}
                    ],
        
        

      },
      
    }
  },
  methods: {
    printBoxs(){
        var that = this; 
        var cellCodes  =[];
        that.selectBoxs.forEach(box => {
             this.demoPrint(box.boxCode);
        });
      
    },
    //打印
    demoPrint: function (boxCode, isReview) {
      var that = this;

      IOT.getServerData('/box/infos/print', 'get', {boxCode: boxCode, wareId: that.wareId}, (ret) => {
        if (ret.code === 200) {
          var strHTML = ret.data;
          that.$parent.printHtml(strHTML, isReview);  
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);

    },
    handleSelectionChange(val){
        this.selectBoxs = val;
    },
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
      this.getBoxData();
    },
    getBoxData() {
      IOT.getServerData('/box/infos/list', 'get', this.searchData, (ret) => {
        if (ret.code === 200) {
          this.boxs = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber
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
          IOT.getServerData('/box/infos/add', 'post', this.form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getBoxData()
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
          IOT.getServerData('/box/infos/update', 'post', this.rowData, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                 that.getBoxData()
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
      IOT.getServerData('/box/infos/delete', 'get', {id: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips('删除成功！', 'success', 1000, () => {
            that.getBoxData()
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },

  },
  created() {
    this.getBoxData();
     
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.box {

}
</style>
