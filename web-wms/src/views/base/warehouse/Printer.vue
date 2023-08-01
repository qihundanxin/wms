<template>
  <div class="printer">

    <div class="publicSearch">
      <div class="searchBar"  >
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div>
        
      </div>
   
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
      </el-button>
    </div>

    <el-dialog title="新增打印机" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div  >
        <el-form   :model="form" :rules="rules" ref="form">
          <span style="margin-left:30%;font-size:15px;color:red">新增打印机前请刷新浏览器</span>
          <br/>
           <br/>
            <br/>
          <el-form-item label="编码" prop="code" :label-width="formLabelWidth">
            <el-input v-model="form.code" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
           <el-form-item label="IP" prop="IP" :label-width="formLabelWidth">
            <el-input v-model="form.ip" @blur="openYc(form.ip)" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
          </el-form-item>
        
          <el-form-item label="打印机" prop="page" :label-width="formLabelWidth">
             <select
                         id="addPrinterIndex"
                        @change="demoCreatePagSizeList(form.printerIndex)"
                        v-model="form.printerIndex"
                        style="
                            height: 35px;
                            width: 90%;
                            border: 1px solid rgba(220, 223, 230);
                        "
                    >
                      
                    </select>
            <!-- <el-input v-model="form.printerIndex" placeholder="请输入打印机" maxlength="20" style="width: 90%;"></el-input> -->
          </el-form-item>
           <el-form-item label="纸张" prop="page" :label-width="formLabelWidth">
              <select
                         id="addPrinterPage"
                     
                        v-model="form.page"
                        style="
                            height: 35px;
                            width: 90%;
                            border: 1px solid rgba(220, 223, 230);
                        "
                    > 
                    </select>
            <!-- <el-input v-model="form.page" placeholder="请输入纸张" maxlength="20" style="width: 90%;"></el-input> -->
          </el-form-item>
           <el-form-item label="打印方向" prop="fx" :label-width="formLabelWidth">
             <el-select v-model="form.fx"   filterable placeholder="请选择打印方向" clearable>
              <el-option     key="1"    label="纵向"    value="1">  </el-option>
              <el-option     key="2"    label="横向"    value="2">  </el-option>
            </el-select>
          </el-form-item>
           <el-form-item label="单页/双页 " prop="ds" :label-width="formLabelWidth">
            <el-select v-model="form.ds"   filterable placeholder="请选择单页/双叶" clearable>
              <el-option     key="1"    label="单页"    value="1">  </el-option>
              <el-option     key="2"    label="双页"    value="2">  </el-option>
            </el-select>
          
          </el-form-item>
          
          
          
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改打印机" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="700px"
               style="margin-top: -5vh;" :close-on-click-modal="false">
      <div class="areaModalBox">
        <el-form class="clearFloat" :model="rowData" :rules="rules" ref="rowData"> 
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
     
          highlight-current-row
          :data="printers"
          style="width: 100%">
        
        <el-table-column
              
            show-overflow-tooltip
            prop="code"
            label="编码"
            min-width="180">
        </el-table-column> 
         <el-table-column
              
            show-overflow-tooltip
            prop="ip"
            label="IP"
            min-width="180">
        </el-table-column> 
        
         <el-table-column
              
            show-overflow-tooltip
            prop="printerIndex"
            label="打印机"
            min-width="100">
        </el-table-column> 
         <el-table-column
              
            show-overflow-tooltip
            prop="page"
            label="纸张"
            min-width="120">
        </el-table-column> 
        <el-table-column
              
            show-overflow-tooltip
            prop="fx"
            label="打印方向"
            min-width="120">
            <template slot-scope="scope">
                <span v-if="scope.row.fx=='1'">纵向</span>
                <span v-if="scope.row.fx=='2'">横向</span>
            </template>
        </el-table-column> 
         <el-table-column
              
            show-overflow-tooltip
            prop="ds"
            label="单页/双页"
            min-width="180">
             <template slot-scope="scope">
                <span v-if="scope.row.ds=='1'">单页</span>
                <span v-if="scope.row.ds=='2'">双页</span>
            </template>
        </el-table-column> 
         <el-table-column 
            prop="pdaDefault"
            label="默认"
            min-width="180">
             <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.pdaDefault"
                   @change="changePda(scope.row)"
                   active-value="1"
              inactive-value="0"
                  >
                </el-switch>
            </template>
        </el-table-column> 
       
        <el-table-column
            fixed="right"
            label="操作"
            min-width="180">
          <template slot-scope="scope">
             <el-dropdown style="margin-left:10px;margin-right:10px" size="mini" type="primary"
                         icon="iconfont icon-print" @click="demoPrint(scope.row.id)" split-button>
              打印
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.id,1)">打印预览1
                  </el-button>
                </el-dropdown-item>

                <el-dropdown-item>
                  <el-button style="margin-top:2px; " size="small" type="primary"
                             @click="demoPrint(scope.row.id,2)">打印预览2
                  </el-button>
                </el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
            
            <!-- <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                       class="tableBtn"></el-button> -->
            <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="text"
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
  name: 'printer',
  data() {
    
    
    
    return {
      formLabelWidth:'120px',
      
      wareId: PF.getLocal('wareId', 'number'),

      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: '',
        wareId: PF.getLocal('wareId', 'number'),
        type: null,
      },
      printers:[],
      form:{
        port:"8000",
        wareId: PF.getLocal('wareId', 'number'),
        fx:'1',
        ds:'1'
      },
      rowData:{}, 
      dialogNewVisible: false,
      dialogUpdateVisible: false, 
      rules: { 

      },
      
      
    }
  },
  methods: {
    //获取打印机纸张大小
        demoCreatePagSizeList(printIndex) { 
            var oSelect = document.getElementById("addPrinterPage");
            CLODOP.Create_PageSize_List(oSelect, printIndex); 
        },
      //获取打印机列表
        demoCreatePrinterList( ) { 
            var ele = document.getElementById("addPrinterIndex");
            CLODOP.Create_Printer_List(ele); 
        },
       openYc(ip ){
        var aaa = IOT.userSetClodopJS(ip);
        if(aaa!=undefined && aaa!=null){
            setTimeout(this.demoCreatePrinterList( ), 500); 
        };
           
      }, 
    printBoxs(){
        var that = this; 
        var cellCodes  =[];
        that.selectBoxs.forEach(box => {
             this.demoPrint(box.boxCode);
        });
      
    },
    //打印
    demoPrint: function (id, isReview) {
      var that = this;

      IOT.getServerData('/printer/infos/print', 'get', {id: id, wareId: that.wareId}, (ret) => {
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
      this.getPrinters();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getPrinters()
    },
    clearSearchData() {
      this.searchData.keyWords = '';
      // this.searchData.wareId = null;
      this.searchData.type = null;
    },
    setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getPrinters();
    },
    getPrinters() {
      IOT.getServerData('/printer/infos/list', 'get', this.searchData, (ret) => {
        if (ret.code === 200) {
          this.printers = ret.rows;
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
          IOT.getServerData('/printer/infos/add', 'post', this.form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips('保存成功！', 'success', 1000, () => {
                that.getPrinters()
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
                 that.getPrinters()
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
            this.deletePrinter(id)
          })
          .catch(_ => {
          });
    },
    deletePrinter(id) {
      let that = this;
      IOT.showOverlay('删除中，请稍等...');
      IOT.getServerData('/printer/infos/delete', 'get', {id: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips('删除成功！', 'success', 1000, () => {
            that.getPrinters()
          });
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },

    changePda(row){
        IOT.getServerData('/printer/infos/changePda','get',row,(ret) => {
          if (ret.code === 200) { 
            this.getPrinters();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        
      },

  },
  created() {
    this.getPrinters();
     
  },
  mounted() {
  }
}
</script>

<style scoped lang="less">
.box {

}
</style>
