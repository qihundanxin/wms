<template>
    <div class="shelf">

        <div class="publicSearch">
            <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
                <el-button-group>
                    <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>关键字搜索</span>
                    <el-input v-model="searchData.keyWords" size="mini" placeholder="名称" clearable></el-input>
                </div>
            </div>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">
                新增
            </el-button>
        </div>

        <el-dialog title="新增播种墙" :visible.sync="dialogNewVisible" :before-close="handleClose" width="600px"
                   style="margin-top: -10vh;">
            <div class="singleColumnModalBox">
                <el-form :model="form" :rules="rules" ref="form">
                    <el-form-item label="编码" prop="code" :label-width="formLabelWidth">
                        <el-input v-model="form.code" placeholder="请输入编码" style="width: 90%;"></el-input>
                    </el-form-item>
<!--                    <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">-->
<!--                        <el-select v-model="form.wareId" filterable placeholder="请选择仓库" style="width: 90%;">-->
<!--                            <el-option-->
<!--                                    v-for="item in wares"-->
<!--                                    :key="item.wareId"-->
<!--                                    :label="item.wareName"-->
<!--                                    :value="item.wareId">-->
<!--                            </el-option>-->
<!--                        </el-select>-->
<!--                    </el-form-item>-->
                    <el-form-item label="行数" prop="rowCount" :label-width="formLabelWidth">
                        <el-input-number min="1" max="99" step="1" v-model="form.rowCount"
                                         style="width: 90%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="列数" prop="columnCount" :label-width="formLabelWidth">
                        <el-input-number min="1" max="99" step="1" v-model="form.columnCount"
                                         style="width: 90%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="描述" prop="description" :label-width="formLabelWidth">
                        <el-input v-model="form.description" style="width: 90%;"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('form'); dialogNewVisible = false">取 消</el-button>
                <el-button type="primary" @click="commitForm('form')">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="修改" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="600px"
                   style="margin-top: -10vh;">
            <div class="singleColumnModalBox">
                <el-form :model="rowData" :rules="rules2" ref="rowData">
                    <el-form-item label="编码" prop="code" :label-width="formLabelWidth">
                        <el-input v-model="rowData.code" placeholder="请输入编码" style="width: 90%;"></el-input>
                    </el-form-item>
<!--                    <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">-->
<!--                        <el-select v-model="rowData.wareId" filterable placeholder="请选择仓库" style="width: 90%;">-->
<!--                            <el-option-->
<!--                                    v-for="item in wares"-->
<!--                                    :key="item.wareId"-->
<!--                                    :label="item.wareName"-->
<!--                                    :value="item.wareId">-->
<!--                            </el-option>-->
<!--                        </el-select>-->
<!--                    </el-form-item>-->
                    <el-form-item label="行数" prop="rowCount" :label-width="formLabelWidth">
                        <el-input-number min="1" max="99" step="1" v-model="rowData.rowCount"
                                         style="width: 90%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="列数" prop="columnCount" :label-width="formLabelWidth">
                        <el-input-number min="1" max="99" step="1" v-model="rowData.columnCount"
                                         style="width: 90%;"></el-input-number>
                    </el-form-item>
                    <el-form-item label="描述" prop="description" :label-width="formLabelWidth">
                        <el-input v-model="rowData.description" style="width: 90%;"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('rowData');dialogUpdateVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateForm('rowData')">确 定</el-button>
            </div>
        </el-dialog>
        <div class="tableBox organizationLeftTab" style="width: 20%">
            <el-table
                    ref="waveTable"
                    highlight-current-row  
                    @row-click="changeSeedingWall"
                 
                    :data="seedingWalls"
                    
                    style="width: 100%">
                
                <el-table-column
                        sortable
                        prop="rowCount"
                        label="播种墙"
                >
                <template slot-scope="scope">
                        <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>{{scope.row.code}}</span>
                            <el-button @click="isDelete(scope.row.id)" style="float: right; padding: 3px 0" type="text">删除</el-button>
                        </div>
                        
                         <div  >
                            行数：{{scope.row.rowCount}}
                        </div>
                        <div  >
                            列数：{{scope.row.columnCount}}
                        </div>
                        <div  >
                            仓库：{{scope.row.wareName}}
                        </div>
                        <div  >
                            描述：{{scope.row.description}}
                        </div>

                        
                        
                        
                        </el-card>
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
                    layout="sizes,total, prev, pager, next  "
                    :total="searchData.total">
            </el-pagination>
        </div>
        <div class="organizationMiddle">
            <i class="el-icon-d-arrow-right"></i>
            </div>
    
        <el-card class="box-card organizationRight" style="width:900px;left:100px">
        <h2 style="text-align: center;margin-bottom: 20px;">
            播种墙
            
        </h2>

        <div id="seed_wall" v-html="htmlStr" style="width:800px,height:600px,border: 5px solid dodgerblue">
        </div>
        
        </el-card>
    </div> 
     
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: 'SeedingWall',


        data() {
            return {
                 wareId: PF.getLocal('wareId'),
                htmlStr:'',
                isMoreSearch: false,
                searchData: {
                    pageNum: 1,
                    pageSize: 15,
                    total: null,
                    keyWords: '',
                },

                wares: [],
                seedingWalls: [],

                dialogNewVisible: false,
                dialogUpdateVisible: false,

                form: {
                    code: '',
                    rowCount: null,
                    columnCount: null,
                    wareId: PF.getLocal('wareId'),
                    wareName: '',
                    organizationId: null,
                    description: ''
                },
                rules: {},
                formLabelWidth: '120px',

                rowData: {},
                rules2: {},
                seedingWall:{}, 
                seedingWallIndex:0,
                cells:[],
                soMasters:[]


            }
        },
        computed: {},
        methods: {
            isOK(cellId){
                var that = this;
                var list = that.soMasters;
                var billNo = null;
                for(var i=0;i<list.length;i++){
                    var details = list[i].list; 
                    if(list[i].cellId  ==cellId && list[i].isOK==true){
                        return true;
                    } 
                }
                return  false; 
            },
            changeCellColor(cellId,color,lable,billNo){
                
                var that = this;
                that.cells.forEach((element,index) => {
                    if(that.isOK(element)){
                        document.getElementById(element).style.backgroundColor = "#67C23A"
                        document.getElementById('lable-' + element).innerText = "OK"
                    }else if(element==cellId){
                            document.getElementById('billNo-' + cellId).innerText = billNo
                            document.getElementById(cellId).style.backgroundColor = color;
                            document.getElementById('lable-' + cellId).innerText = lable
                    }else if(index<=this.cellIndex-1){
                            document.getElementById(element).style.backgroundColor = "#909399"; 
                    }
                    else{
                        document.getElementById(element).style.backgroundColor = "white"
                    } 
                });
                
            },
            getSeedingCells(code){
                var that = this;
                that.soMasters = [];
                IOT.getServerData('/seeding/cells/findByWallCode', 'get', {wallCode:code,wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        var cells = ret.rows; 
                        for(var i =0;i<cells.length;i++){
                            var cell = cells[i];
                            if(cell.billNo!=null && cell.state==1){
                                this.changeCellColor(cell.seedingCellCode,"#67C23A","OK",cell.billNo);
                                var soMaster = {billNo:cell.billNo,cellId:cell.seedingCellCode,list:[],isOk:true}
                                
                            }
                            if(cell.billNo!=null && cell.state==0){
                                this.changeCellColor(cell.seedingCellCode,"red","重新扫描",cell.billNo);
                                var soMaster = {billNo:cell.billNo,cellId:cell.seedingCellCode,list:[],isOk:true}
                                
                            }
                        }  
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
            generateSeedWall(code, row, column) {
                this.cells = [];
                let str = ''
                var width1 =97 / column;
                var width =  width1.toFixed(2);
                width = width+"%";
                var fontSize1 = 100/column +"px";
                var fontSize2 = 120/column +"px";

               
     
                let height = 600 / row
                for (let i = 1; i <= row; i++) {
                    for (let j = 1; j <= column; j++) {
                       
                        let cellId = code+'-'+i+'-'+j
                        let cellLabelId ='lable-'+  cellId;
                        let cellBillNoId = 'billNo-'+cellId;
                        //let digit = (i - 1) * column + j
                       
                        str += '<div id="' + cellId + '" class="seed_hole_class" style="width: ' + width + ';height: ' + height + 'px;float: left; border: 1px solid dodgerblue; text-align: center">' +
                                     '<div id="'+cellBillNoId+'" style="height; ' + width / 6 + '; font-size: ' + fontSize1 + '; margin-top:  10%; color: #FFFFFF;"></div>' +
                                    '<div id="' + cellLabelId + '" style="width:90%;    margin-top: 13%; font-size:'+fontSize2+'; color: white"></div>' +
                           
                                '</div>' 
                        this.cells.push(cellId);

                    }
                    str += '<div style="clear: both"></div>'
                }
                this.htmlStr = str;
                this.getSeedingCells(code);
                //document.getElementById('seed_wall').innerHTML = str
            },
            changeSeedingWall(seedingWall) { 
                
                //this.seedingWall = this.seedingWalls[this.seedingWallIndex];
                let row = seedingWall.rowCount
                let column = seedingWall.columnCount
                this.generateSeedWall(seedingWall.code, row, column)
                // this.addBtnClickEvent()
                // this.displayHoleLabel()
            },
            handleSizeChange(val){
                this.searchData.pageSize = val;
                this.getSeedingWallData();
            },

            handleCurrentChange(val) {
                this.pageNum = val;
                this.getSeedingWallData()
            },
            clearSearchData() {
                this.searchData.keyWords = '';
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
            setPageNumFlush(){
                this.searchData.pageNum =1;
                this.getSeedingWallData();
            },
            getSeedingWallData() {
                IOT.getServerData('/seeding/walls', 'get', {wareId: PF.getLocal('wareId', 'number')}, (ret) => {
                    if (ret.code === 200) {
                        this.seedingWalls = ret.rows;
                        this.searchData.pageNum = ret.pageNum
                        this.searchData.total = ret.total

                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            getSysParamData() {
                IOT.getServerData('/sys/params', 'get', {moduleCode: 'cellValue'}, (ret) => {
                    if (ret.code === 200) {
                        this.sysParams = ret.rows;
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
                        IOT.getServerData('/seeding/walls', 'post', this.form, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getSeedingWallData()
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
                        IOT.getServerData('/seeding/walls/update', 'post', this.rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    that.getSeedingWallData()
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
                        this.deleteSeedingWall(id)
                    })
                    .catch(_ => {
                    });
            },
            deleteSeedingWall(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/seeding/walls/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getSeedingWallData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
        },
        created() {
            this.getSeedingWallData();
            this.getWareData();
            this.getSysParamData();
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
     

    .current-row {
        .el-button--text  {
            color: white;
        }
        .el-card{
            background-color: #409EFF;
            color:white; 
        }
        
    }
</style>
