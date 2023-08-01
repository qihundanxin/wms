<template>
    <div class="asnBill">
        <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <!--<div>
          <span>关键字搜索</span> 
          <el-input v-model="searchData.keyWords" size="mini" placeholder="仓库名称/货主名称" clearable></el-input>
        </div>-->
          <div>
              <span>仓库</span>
                <el-select v-model="searchData.wareId" size="mini" filterable placeholder=" " clearable>
                    <el-option
                    v-for="item in wares"
                    :key="item.wareId"
                    :label="item.wareName"
                    :value="item.wareId">
                    </el-option>
                </el-select>
          </div>
          <div>
              <span>类型</span>
               <el-select v-model="searchData.type" size="mini" filterable placeholder=" " clearable>
                    <el-option
                    v-for="item in types"
                    :key="item.value"
                    :label="item.lable"
                    :value="item.value">
                    </el-option>
                </el-select>
          </div>
      
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

        <el-dialog title="新建" :visible.sync="dialogNewVisible" :before-close="handleClose" width="400px"
                   style="margin-top: -10vh;">
            <div  >
                <el-form :model="printTemp" :rules="rules"  ref="printTemp"  >
                    <el-form-item  prop="tempName" label="模板名称"  label-width=80px >
                        <el-input v-model="printTemp.tempName"  ></el-input>
                    </el-form-item>
                    <el-form-item   prop="type"  label="模板类别" label-width=80px  >
                        <el-select v-model="printTemp.type"  >
                            <el-option
                                    v-for="item in types"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item   prop="type"   label="设计方式" label-width=80px  >
                        <el-select v-model="printTemp.sjType"  >
                            <el-option
                                    v-for="item in sjTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <div  v-if="printTemp.sjType==2">
                        <el-form-item   prop="fileName"   label="文件名称"  label-width=80px >
                            <el-input v-model="printTemp.fileName"  ></el-input>
                        </el-form-item>
                        
                    </div>
                    <div v-if="printTemp.sjType==1">
                        <el-form-item   prop="shipCode"    v-if="printTemp.type=='快递面单'"  label="承运商" label-width=80px  >
                        <el-select filterable  v-model="printTemp.shipCode"  >
                            <el-option
                                    v-for="ship in carriers"
                                    :key="ship.carrierCode"
                                    :label="ship.carrierName"
                                    :value="ship.carrierCode"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="模板规格" label-width=80px  >
                        <el-radio-group v-model="printTemp.guigeLeixing" >
                            <el-radio-button label="标准"></el-radio-button>
                            <el-radio-button label="自定义"></el-radio-button>
                        </el-radio-group>
                         
                    </el-form-item>
                    

                     <el-form-item v-if="printTemp.guigeLeixing == '自定义'" label=" 宽"  label-width=80px >
                         <el-input placeholder="请输入宽" v-model="printTemp.width">
                            <template slot="append">mm</template>
                        </el-input>
                        
                    </el-form-item>
                     <el-form-item  v-if="printTemp.guigeLeixing == '自定义'"  label="高 "  label-width=80px >
                         <el-input placeholder="请输入高" v-model="printTemp.height">
                            <template slot="append">mm</template>
                        </el-input>
                       
                    </el-form-item>
                     <el-form-item  v-if="printTemp.guigeLeixing == '标准'"   label-width=80px    >
                        
                         <el-select v-model="routeTactic.state"  >
                            <el-option
                                    v-for="item in states"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>

                    </div>

                     

                     
                    

                

                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="commitNewForm('printTemp')">确 定</el-button>
            </div>

        </el-dialog>


         <el-dialog title="修改" :visible.sync="dialogUpdateVisible" :before-close="handleClose" width="400px"
                   style="margin-top: -10vh;">
            <div  >
                <el-form :model="rowData" :rules="rules"  ref="printTemp"  >
                    <el-form-item  prop="tempName" label="模板名称"  label-width=80px >
                        <el-input v-model="rowData.tempName"  ></el-input>
                    </el-form-item>
                    <el-form-item   prop="type"  label="模板类别" label-width=80px  >
                        <el-select v-model="rowData.type"  >
                            <el-option
                                    v-for="item in types"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item   prop="type"   label="设计方式" label-width=80px  >
                        <el-select disabled=false v-model="rowData.sjType"  >
                            <el-option
                                    v-for="item in sjTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <div  v-if="rowData.sjType==2">
                        <el-form-item   prop="fileName"   label="文件名称"  label-width=80px >
                            <el-input v-model="rowData.fileName"  ></el-input>
                        </el-form-item>
                        
                    </div>
                     
                     

                     
                    

                

                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogUpdateVisible=false">取 消</el-button>
                <el-button type="primary" @click="commitUpdateForm('printTemp')">确 定</el-button>
            </div>

        </el-dialog>
 
 

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng"
                       @click="showNewModal"
            > 新增
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    v-loading="loading.masterTable"
                    highlight-current-row
                    :data="routeTactics"
                    style="width: 100%">
                <!-- <el-table-column
                        prop="tempId"
                        label="ID"
                >
                </el-table-column> -->
                <el-table-column
                        prop="tempName"
                        label="模板名称"
                >
                </el-table-column>
              
                <el-table-column
                        prop="type"
                        label="打印类型"
                >
                 <template slot-scope="scope">
                         <span>{{scope.row.type}}</span>
                          <span v-if="scope.row.type =='快递面单'">（{{scope.row.shipCode}}）</span>
                       
                    </template>
                
                </el-table-column>
                 <el-table-column
                        prop="shType"
                        label="设计方式"
                >
                 <template slot-scope="scope">
                        
                          <span v-if="scope.row.sjType ==1">WMS设计器</span>
                           <span v-if="scope.row.sjType ==2">Ureport设计器</span> 
                       
                    </template>
                
                </el-table-column>
                 <el-table-column
                        prop="fileName"
                        label="文件名"
                >
                 <template slot-scope="scope">
                        
                          <span v-if="scope.row.sjType ==1">-</span>
                           <span v-if="scope.row.sjType ==2">{{scope.row.fileName}}</span> 
                       
                    </template>
                </el-table-column>
                <!-- <el-table-column
                        prop="shipCode"
                        label="快递公司"
                >
                
                </el-table-column> -->
                 <el-table-column
                        prop="tempSizeType"
                        label="规格"
                >
                <template slot-scope="scope">
                         <span>{{scope.row.width}}*{{scope.row.height}}</span>
                       
                    </template>
                
                </el-table-column>


                 <el-table-column
                        prop="wareName"
                        label="仓库"
                >
                </el-table-column>


                <el-table-column
                        prop="state"
                        label="状态"
                >
                    <template slot-scope="scope">
                        <el-switch
                        @change="update(scope.row)"
                        :active-value='1'
                        :inactive-value='0'
                        v-model="scope.row.state"
                        active-color="#13ce66"
                        inactive-color="#ff4949">
                        </el-switch>
                        <!-- <span>{{scope.row.state===1 ? '激活' : '未激活'}}</span> -->
                    </template>
                </el-table-column>

                

                                <el-table-column
                                width="300"
                                        fixed="right"
                                        label="操作"
                                         >
                                    <template slot-scope="scope">
                             
                                         <el-button  @click="printTest(scope.row.tempHtml)"     type="text"    
                                                   >预览</el-button>
                                       
                                        <el-button  @click="showUpdateModal(scope.row)" class="tableBtn"  icon="iconfont icon-bianji" type="text"  
                                                   ></el-button>
                                        <el-button @click="isDelete(scope.row.tempId)"  icon="iconfont icon-shanchu" type="text"  
                                                 class="tableBtn danger"     ></el-button>
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
        name: 'RouteTactic',
        data() {
            return {
                carriers:[],
                wareId:PF.getLocal('wareId', 'number'),
                 wareName:PF.getLocal('wareName', 'string'),
                isMoreSearch: false,
                loading:{
                    masterTable:true,

                },
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number'),
                    type:''
                },
                childSearchData: {
                    waveId: ''
                },

                routeTactics: [],
                routeTactic: {
                    route: null,
                    code: '',
                    minWeight: null,
                    maxWeight: null,
                    minSize: null,
                    maxSize: null,
                    carrierId: null,
                    carrierName: '',
                    organizationId: null,
                    wareId: null,
                    state: 0
                },

                printTemp:{
                    guigeLeixing:"自定义",
                    sjType:'',
                },

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],
                multipleSelection: [],

                rowData: {
                    soCountFloor: null,
                    soCountCeilinig: null
                },
                dialogUpdateVisible: false,
                dialogNewVisible: false,
                sjTypes:[
                    {
                        label: 'WMS设计器',
                        value: '1'
                    },
                    {
                        label: 'Ureport设计器',
                        value: '2'
                    } ,
                ],

                types: [
                    {
                        label: '验收标签',
                        value: '验收标签'
                    },
                    {
                        label: '商品标签',
                        value: '商品标签'
                    } ,
                    {
                        label: '货位标签',
                        value: '货位标签'
                    } ,
                    {
                        label: '拣货标签',
                        value: '拣货标签'
                    } ,
                     {
                        label: '快递面单',
                        value: '快递面单'
                    } ,
                    {
                        label: '装箱标签',
                        value: '装箱标签'
                    } ,
                    {
                        label: '货位组标签',
                        value: '货位组标签'
                    },
                    {
                        label: '货位标签',
                        value: '货位标签'
                    },
                    {
                        label: '二维码/条形码',
                        value: '二维码/条形码'
                    },
                    {
                        label: '出库单',
                        value: '出库单'
                    },
                    


                    
                ],
              
                wares: [],
                customers: [],
                carriers: [],

                rules: {
                    tempName: [{  required: true,  message: '请输入模板名称',  trigger: 'blur'   }],
                    type: [{  required: true,  message: '请选择模板类型',  trigger: 'blur'   }],
                    shType: [{  required: true,  message: '请选择设计方式',  trigger: 'blur'   }],
                },
                rules2: {
                     tempName: [{  required: true,  message: '请输入模板名称',  trigger: 'blur'   }],

                },
            }
        },
        computed: {
            ...mapState([
                "soStates"
            ])
        },
        methods: {
            update(temp){
                IOT.getServerData('/print/temp/infos/update', 'post', temp, (ret) => {
                    if (ret.code === 200) {
                        this.$notify({
                            title: '成功',
                            message: '更新成功',
                            type: 'success'
                        });
                        this.getTempInfo();
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },


             //打印
            printTest:function (strHTML){
                this.$parent.printHtml(strHTML,1);
                
            },
            getWareData() {
                IOT.getServerData('/ware/infos/list', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.wares = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getTempInfo();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getTempInfo()
            },

            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.wareId = null;
                this.searchData.type = '';
                this.type = '';
                this.wareId = null;
            },
            setPageNumFlush(){
                this.searchData.pageNum =1;
                this.getTempInfo();
            },
            getTempInfo() {
                let that = this;
                that.loading.masterTable = true;
                let searchData = PF.JSON(this.searchData);
                IOT.getServerData('/print/temp/infos/list', 'get', searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        this.routeTactics = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            handleSelectionChange(val) {
                this.multipleSelection = val;
            },

            showNewModal() {
                this.dialogNewVisible = true
            },

            showUpdateModal(row) {
                if(row.sjType==1){
                     window.location.href ='printTemp/printTemp.html?tempId='+row.tempId;
                }else if(row.sjType==2){
                          this.rowData = JSON.parse(JSON.stringify(row));
                this.dialogUpdateVisible = true
                }
               
                
              //  window.open('printTemp/printTemp.html?tempId='+=row.tempId);   
              
            },
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteRouteTactic(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteRouteTactic(id) {
                
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/print/temp/infos/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getTempInfo()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleClose() {
                this.dialogNewVisible = false
            },
            handleClose2() {
                this.dialogUpdateVisible = false
            },
            resetForm() {
                this.dialogNewVisible = false
                this.routeTactic = {
                    route: null,
                    code: '',
                    minWeight: null,
                    maxWeight: null,
                    minSize: null,
                    maxSize: null,
                    carrierId: null,
                    carrierName: '',
                    organizationId: null,
                    wareId: null,
                    state: 0
                }
            },
            resetForm2() {
              this.dialogUpdateVisible = false
            },
            commitNewForm(formName) {
                let that = this
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(that.printTemp.sjType==1){
                                window.location.href ='printTemp/printTemp.html?tempName='+that.printTemp.tempName+'&type='+that.printTemp.type+'&height='+that.printTemp.height+'&width='+that.printTemp.width+'&wareId='+that.wareId+'&wareName='+that.wareName+'&shipCode='+that.printTemp.shipCode;
                        }else if(that.printTemp.sjType==2){
                             IOT.showOverlay('保存中，请稍等...');
                                IOT.getServerData('/print/temp/infos/add', 'post', that.printTemp, (ret) => {
                                IOT.hideOverlay();
                                if (ret.code === 200) {
                                    IOT.tips('保存成功！', 'success', 1000, () => {
                                        this.getTempInfo()
                                    });
                                } else {
                                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                                }
                        });
                        }
                         
                    } else {
                        console.log('validate failed')
                    }
                })
            },
            commitUpdateForm(formName) {
                let that = this
                let rowData = PF.JSON(this.rowData);
                this.$refs[formName].validate((valid) => {
                    if (valid) { 
                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/print/temp/infos/update', 'post', rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getTempInfo()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm2()
                        this.dialogNewVisible = false
                        IOT.hideOverlay();
                    } else {
                        console.log('validate failed')
                    }
                })
            }

        },
        created() {
            PF.getCarriers((rows) => {
                this.carriers = rows;
            });
            this.getWareData();

            this.getTempInfo()
            

        },
        mounted() {
        }

    }
</script>

<style scoped lang="less">
    .asnBill {

    }
</style>
