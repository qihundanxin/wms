<template>
    <div>
         

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="getShipBoxsData" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.shipBoxCode" size="mini" placeholder=" " clearable></el-input>
                </div>

            </div>

        </div>

        <div class="headerBtns">
            <el-button size="mini" type="primary" round icon="iconfont icon-xinzeng" @click="$router.push({name: 'ShipCode'})">
                装箱
            </el-button>
        </div>


        <div class="tableBox organizationLeftTab" style="width:60%">
            <el-table
                    highlight-current-row
                    @current-change="handleSelChange"
                    :data="shipBoxs"
                    style="width: 100%">
             
                <el-table-column
                     
                        prop="shipBoxCode"
                        label="编码"
                >
                
                </el-table-column>
                <el-table-column
                     
                        prop="shipName"
                        label="承运商"
                > 
                </el-table-column>
                <el-table-column
                        prop="createUserName"
                        label="装箱人"
                >
                </el-table-column> 
                <el-table-column
                        prop="createTime"
                        label="装箱时间"
                >
                 <template slot-scope="scope">
                    {{scope.row.createTime.substr(5,11)}} 
                </template>
                </el-table-column>
                <el-table-column
                        prop="weight"
                        label="重量"
                >
                </el-table-column>
                <el-table-column
                        prop="length"
                        label="长/宽/高"
                >
                 <template slot-scope="scope">
                    {{scope.row.length}}/{{scope.row.width}}/{{scope.row.height}}
                </template>
                </el-table-column>
                 
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint(scope.row.shipBoxId)" split-button  >
                        打印
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
                            <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(scope.row.shipBoxId,1)">打印预览1</el-button></el-dropdown-item> 
                        
                            <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(scope.row.shipBoxId,2)">打印预览2</el-button></el-dropdown-item> 

                        </el-dropdown-menu>
                        </el-dropdown>
                        <el-button @click="deleteConfirm(scope.row.shipBoxId)" icon="iconfont icon-shanchu" type="text"
                                   class="tableBtn danger"   ></el-button>
                      
                    </template>
                </el-table-column>

            </el-table>
            <el-pagination
                    style="text-align: right;"
                    class="pagination-left-bottom"
                    @current-change="handleCurrentChange"
                    :current-page.sync="searchData.pageNum"
                    :page-size="searchData.pageSize"
                    @size-change="handleSizeChange"
                    :page-sizes="[15, 30, 50, 100]"
                    layout="sizes,total, prev, pager, next, jumper"
                    :total="searchData.total">
            </el-pagination>
        </div>

        <div class="organizationMiddle">
            <i class="el-icon-d-arrow-right"></i>
        </div>

        <div class="organizationRight"   style="width:30%">
             
         
            
            <div class="tableBox">
                <el-table
                        highlight-current-row
                        @selection-change="handleWaveDetailSelChange"
                        :data="shipBoxDetails"
                        style="width: 100%">
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            prop="shipBillCode"
                            label="快递单号"
                    ></el-table-column>
                    

                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="110">
                        <template slot-scope="scope">
                            <el-button @click="deleteDetailConfirm(scope.row.shipBoxDetailId)" icon="iconfont icon-shanchu" type="text"
                                   class="tableBtn danger"   ></el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div>
                <!-- <el-button @click="showUpdateModal">修改</el-button>
                <el-button @click="$router.push({name: 'ChoosePickTask', params: {loadMasterId: loadMasterId}})">
                    添加拣货任务
                </el-button> -->
            </div>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'LoadMaster',
        data() {
            return {
            
                shipBoxId:'',
                isMoreSearch: false,
                activeName: 'first',
                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    keyWords: '',
                    wareId: PF.getLocal('wareId', 'number')
                },
                childSearchData: {
                    loadMasterCode: ''
                },
                pickTaskSearch: {},
                shipBoxDetails:[],
                shipBoxs: [],
                loadMaster: {
                    code: '',
                    licensePlateNo: '',
                    vehicleType: null,
                    driverName: '',
                    loaderName: '',
                    loadStart: '',
                    loadFinish: '',
                    sizeLimit: null,
                    weightLimit: null,
                    wareId: PF.getLocal('wareId', 'number'),
                    wareName: '',
                    waveId: null,
                    desc1: '',
                    desc2: '',
                    desc3: '',
                    desc4: ''
                },
                loadDetails: [],
                pickTasks: [],

                formLabelWidth: '120px',

                asnUdfHs: [],
                organizations: [],
                suppliers: [],

                loadSelection: [],
                loadDetailSelection: [],
                newLoadSelection: [],

                loadMasterId: '',
                loadDetailIds: [],

                dialogTransVisible: false,

                vehicleTypes: [
                    {
                        value: 1,
                        label: 'type1'
                    },
                    {
                        value: 2,
                        label: 'type2'
                    },
                    {
                        value: 3,
                        label: 'type3'
                    },
                ],
                dialogNewVisible: false,
                dialogUpdateVisible: false,
                wares: [],
                rowData: {},
                rules: [],
                rules2: []

            }
        },
        computed: {
            ...mapState([])
        },
        methods: {
            demoPrint:function (id,isReview){
                var that = this;  
                IOT.getServerData('/ship/boxs/boxPrint', 'get', {shipBoxId:id}, (ret) => {
                if (ret.code === 200) { 
                        var strHTML= ret.data; 
                        that.$parent.printHtml(strHTML,isReview);  
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
                },true);
            
            },

            handleCurrentChange(val) {
                this.searchData.pageNum = val;
                this.getShipBoxsData()
                
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getShipBoxsData()
             
            },
            handleSelChange(rowData) {
               
                this.shipBoxId = rowData.shipBoxId

                this.rowData = JSON.parse(JSON.stringify(rowData))

                this.getDetails(rowData.shipBoxId)
            },
            handleNewWaveSelChange() {

            },
            handleWaveDetailSelChange(val) {
                this.waveDetailSelection = val
            },
            clearSearchData() {
                this.searchData.keyWords = '';
                this.searchData.state = null;
                this.searchData.organizationId = null;
                this.searchData.supplierId = null;
            },
            getShipBoxsData() {
                 const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                });
              
                IOT.getServerData('/ship/boxs/list', 'get',  this.searchData, (ret) => {
                    if (ret.code === 200) {
                        this.shipBoxs = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber
                        if(ret.rows.length>0){
                             this.rowData = ret.rows[0]
                             this.getDetails(this.rowData.shipBoxId);
                        }else{
                            this.rowData ={};
                        }
                       
                        loading.close();
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getDetails(shipBoxId) {
                IOT.getServerData('/ship/box/details/list', 'get', {shipBoxId:shipBoxId}, (ret) => {
                    if (ret.code === 200) {
                        this.shipBoxDetails = ret.rows; 
                       
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
          
          

            deleteConfirm(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteShipBox(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteShipBox(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/ship/boxs/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getShipBoxsData()
                        
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            deleteDetailConfirm(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deleteDetail(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteDetail(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/ship/box/details/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getShipBoxsData()
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
            },
            resetForm2() {
                this.dialogUpdateVisible = false
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
            showUpdateModal() {
                this.dialogUpdateVisible = true
                // this.rowData =
            }
        },
        created() {
            this.getShipBoxsData()
            this.getWareData()
            PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
                this.asnUdfHs = udfs;
            });
            PF.getOrganizations((rows) => {
                this.organizations = rows;
            });
            PF.getSuppliers((rows) => {
                this.suppliers = rows;
            });
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
    .detailItemBox {
        display: inline-block;
        margin-top: 20px;
        width: 300px;

        span {
            flex: 1;
        }
    }
</style>
