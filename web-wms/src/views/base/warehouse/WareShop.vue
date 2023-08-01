<template>
    <div class="WareShop">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <!-- <div>
                    <span>关键字搜索</span>
                    <el-input style="width:160px" v-model="searchData.keyWords" size="mini" placeholder="仓库/地区/地址/备注" clearable></el-input>
                </div> -->
            </div>
            <span class="moreSearchBtn"
                  :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
                  @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">
                新增
            </el-button>
        </div>

        <el-dialog title="新增店铺" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px" :close-on-click-modal="false">
            <el-form :model="form" :rules="rules" ref="form">
                 <el-form-item label="店铺编码" prop="shopId" :label-width="formLabelWidth">
                    <el-input type="number" v-model="form.shopId" placeholder="请输入编码(仅可使用数字)" :min=0 maxlength="20" style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
                      <el-select v-model="form.wareId"  filterable placeholder="请选择仓库"  style="width: 90%;" clearable>
                        <el-option
                            v-for="ware in wares"
                            :key="ware.wareId"
                            :label="ware.wareName"
                            :value="ware.wareId">
                        </el-option>
                    </el-select>
                </el-form-item>
               
                
            </el-form>
         
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('form');dialogNewVisible = false">取 消</el-button>
                <el-button type="primary" @click="commitForm('form')">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="修改" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px" :close-on-click-modal="false">
            <el-form :model="rowData" :rules="rules" ref="form">
                 <el-form-item label="店铺编码" prop="shopId" :label-width="formLabelWidth">
                    <el-input disabled v-model="rowData.shopId" placeholder="请输入编码" maxlength="20" style="width: 90%;"></el-input>
                </el-form-item>
                <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
                      <el-select v-model="rowData.wareId"  filterable placeholder="请选择仓库"  style="width: 90%;" clearable>
                        <el-option
                            v-for="ware in wares"
                            :key="ware.wareId"
                            :label="ware.wareName"
                            :value="ware.wareId">
                        </el-option>
                    </el-select>
                </el-form-item>
               
                
            </el-form>
          
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
            </div>
        </el-dialog>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="wareShops"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        width="50">
                </el-table-column>
                     <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="shopId"
                        label="店铺编码"
                        >
                    
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="wareName"
                        label="仓库"
                         >
                    
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="createTime"
                        label="创建时间"
                         >
                    
                </el-table-column>
                <el-table-column
                        sortable
                        show-overflow-tooltip
                        prop="createUserName"
                        label="创建人"
                       >
                    
                </el-table-column>
              
           
               
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <!-- <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="text"
                                   class="tableBtn"></el-button> -->
                        <el-button @click="isDelete(scope.row.wareShopId)" icon="iconfont icon-shanchu" type="text"
                                   class="tableBtn danger"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
              class="pagination-bottom"
                    style="text-align: right;"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    :page-sizes="[15, 30, 50, 100]"
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
        name: 'WareShop',
        data() {
            let validateShopId = (rule, value, callback) => {
                if(value.indexOf(".")>-1){
                    callback("不可输入小数");
                }
                PF.validator(rule, value, callback, 'ware_shop', 'shop_id', this.form.shopId, 0);
            };
         
            return {
                isMoreSearch: false,

                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                },
                wareShops:[],

                wares: [],

                modalTitle: '',
                dialogNewVisible: false,
               
                form: {
                    wareId: '',
                    shopId: null,
                  
                },
                rules: {
                    shopId: [
                        {required: true, message: '请输入店铺编码', trigger: 'blur'},
                        {validator: validateShopId, trigger: 'blur'},
                    ],
                    wareId: [
                        {required: true, message: '请选择仓库', trigger: 'blur'}
                    ],
                   
                },
                formLabelWidth: '120px',

                dialogUpdateVisible: false,
                rowData: {
                    wareName: '',
                    memo: '',
                    province: '',
                    city: '',
                    area: '',
                    detailAddress: '',
                },
                
                dialogVisible: false,

               
            }
        },
        
        methods: {
            handleSizeChange(val) {
                this.searchData.pageSize = val
                this.getWareShops()
            },
            handleCurrentChange(val) {
                this.pageNum = val;
                this.getWareShops()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
            },
            setPageNumFlush(){
                this.searchData.pageNum =1;
                this.getWareShops();
            },
            getWareShops() {
                IOT.getServerData('/ware/shops/list', 'get', this.searchData, (ret) => {
                    if (ret.code === 200) {
                        this.wareShops = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
             getWareData() {
                IOT.getServerData('/ware/infos/allList', 'get', this.searchData, (ret) => {
                    if (ret.code === 200) {
                        this.wares = ret.rows;
                        
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitForm(formName) {
                if(this.form.shopId<=0){
                    IOT.tips('店铺编码必须大于0', 'error');
                }
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        IOT.getServerData('/ware/shops/add', 'post', this.form, (ret) => {
                            if (ret.code === 200) {
                                IOT.hideOverlay();
                                IOT.tips('新增成功', 'success', 1000, () => {
                                   
                                    this.getWareShops()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                IOT.hideOverlay();
                            }
                        }, true);
                        this.resetForm('form');
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
                        IOT.getServerData('/ware/infos/update', 'post', this.rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('修改成功', 'success', 1000, () => {
                                     this.getWareShops()
                                     
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm('rowData');
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
                        this.deleteWare(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteWare(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/ware/shops/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功', 'success', 1000, () => {
                           this.getWareShops()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
          
           
        },
        created() {
            this.getWareShops();
             this.getWareData();
           
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .WareShop {

    }
</style>
