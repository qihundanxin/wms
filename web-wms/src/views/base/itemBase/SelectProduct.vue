<template>
  <div class="item">

    

    <div class="headerBtns">
        
                <div style="display: inline-block;">
                    <input type="file" id="file_upload_input" >
                </div>
      
    </div>
    <el-row>
      <el-col :span="6"> 
          <div class="publicSearch">
            <div class="searchBar" style="height:210px"> 
              <div>
                <span>搜索次数>{{m4000}}</span>
                <el-input v-model="m4000" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <div>
                <span>点击率低于{{click20}}</span>
                <el-input v-model="click20" size="mini" placeholder=" " clearable></el-input>
              </div>
              <div>
                <span>关键词过滤</span>
                <el-input v-model="keywords" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <br/>
              <el-button size="small" style="margin-left:35%" type="primary" round icon="iconfont icon-xinzeng" @click="importData(1)">确定
                </el-button>
            </div> 
          </div>
      </el-col>
      <el-col :span="6"><div class="publicSearch">
            <div class="searchBar" style="height:210px"> 
              <div>
                <span>搜索次数>{{m4000}}</span>
                <el-input v-model="m4000" size="mini" placeholder=" " clearable></el-input>
              </div>  
              <div>
                <span>点击率倒数{{click10}}</span>
                <el-input v-model="click10" size="mini" placeholder=" " clearable></el-input>
              </div>
               
              <div>
                <span>关键词过滤</span>
                <el-input v-model="keyWords" size="mini" placeholder=" " clearable></el-input>
              </div> 


         
              <br/>
             <el-button size="small" style="margin-left:35%" type="primary" round icon="iconfont icon-xinzeng" @click="importData(2)">确定
                </el-button>
            </div> 
          </div></el-col>
      <el-col :span="6"><div class="publicSearch">
            <div class="searchBar" style="height:210px"> 
              <div>
                <span>搜索次数>{{m4000}}</span>
                <el-input v-model="m4000" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <div>
                <span>搜索/付款 >= {{md600}}</span>
                <el-input v-model="md600" size="mini" placeholder=" " clearable></el-input>
              </div>
              <div>
                <span>关键词过滤</span>
                <el-input v-model="keyWords" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <br/>
              <el-button size="small" style="margin-left:35%" type="primary" round icon="iconfont icon-xinzeng" @click="importData(3)">确定
                </el-button>
            </div> 
          </div></el-col>
      <el-col :span="6"><div class="publicSearch">
            <div class="searchBar" style="height:210px"> 
              <div>
                <span>搜索次数>{{m4000}}</span>
                <el-input v-model="m4000" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <div>
                <span>支付率倒数{{payRate10}}</span>
                <el-input v-model="payRate10" size="mini" placeholder=" " clearable></el-input>
              </div>
              <div>
                <span>关键词过滤</span>
                <el-input v-model="keyWords" size="mini" placeholder=" " clearable></el-input>
              </div> 
              <br/>
              <el-button size="small" style="margin-left:35%" type="primary" round icon="iconfont icon-xinzeng" @click="importData(4)">确定
                </el-button>
            </div> 
          </div></el-col>
    </el-row>
    
     

    
   

    <div class="tableBox">
      <el-table
       v-loading="loading.masterTable"
        highlight-current-row
        :data="items"
        style="width: 100%">
       
        <el-table-column
          sortable
          show-overflow-tooltip
          fixed="left"
          prop="id"
          label="id"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="targetKeyword"
          label="关键词"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="paidOrderCount"
          label="付款订单数"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="clickThroughRate"
          label="点击率"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="searchPopularity"
          label="搜索次数"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="paymentRate"
          label="付款率"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="paymentRate"
          label="搜索/付款"
          width="180">
          <template slot-scope="scope">
                        <span  >{{(scope.row.searchPopularity/scope.row.paidOrderCount).toFixed(1)}}</span>
                        
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
         
         loading:{
           masterTable:false
         },
        items: [],
        m4000:4000,
        click20:0.2,
        keyWords:'',   
        click10:10,
        md600:600,
        payRate10:10,
        searchData:{},
        
        formLabelWidth: '120px',

       
       
      }
    },
    computed: {
      ...mapState([
        "abcLevels",
      ]),
    },
    methods: {

      importData: function (type) {  //上传文件

               var that = this;
               that.loading.masterTable=true;
                 that.items =[];
                let params =  new FormData();
                params.append("model",$('#file_upload_input')[0].files[0]);
                params.append("m4000",this.m4000);
                params.append("click20",this.click20);
                params.append("click10",this.click10);
                params.append("md600",this.md600);
                params.append("payRate10",this.payRate10);
                
                params.append("keywords",this.keywords);
                params.append("selectType",type);


  
                
                IOT.upLoadFile("/select/products/importData", params, function (ret) {
                    IOT.hideOverlay();
                    if (ret && ret.code === 200) {
                      
                         that.items = ret.data.list;
                        that.searchData.total = ret.total;
                        that.searchData.pageNum = ret.pageNumber
                       that.loading.masterTable=false;
                    } else {
                      that.loading.masterTable=false;
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                        
                    }
                });
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
        IOT.getServerData('/tests/list', 'get', searchData, (ret) => {
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
        if(this.isOrganization = true){
          this.form.organizationId = tree.organizationId;
        }

      },
    },
    created() {
     
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .item {

  }
</style>
