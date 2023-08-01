<template>
    <div class="item">

        <el-dialog title="新增打印配置" :visible.sync="dialogNewVisible" :before-close="handleCloseNew" width="500px"
                   style="margin-top: -10vh;">
            <div>
                <el-form :model="printConfig" :rules="rules2" ref="createPrintConfigForm" class="clearFloat">

                    <el-form-item prop="userId" label="用户名" :label-width="formLabelWidth">
                        <el-select v-model="printConfig.userId" size="mini">
                            <el-option
                                    v-for="item in accounts"
                                    :key="item.userId"
                                    :label="item.userName"
                                    :value="item.userId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="printConfig.wareId" size="mini">
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="organizationId" label="货主" :label-width="formLabelWidth">
                        <el-select v-model="printConfig.organizationId" size="mini">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="ip" label="IP地址(包括端口)" :label-width="formLabelWidth">
                        <el-input size="mini" v-model="printConfig.ip" @blur="demoSetClodopJS('')"></el-input>
                    </el-form-item>
                    <el-form-item prop="printerName" label="打印机名称" :label-width="formLabelWidth">
                        <select id = "Select01"  @change="demoCreatePagSizeList()" v-model="printer" style="height: 28px; width: 70%; border:1px solid rgba(220,223,230)">
                            <option></option>
                        </select>
                    </el-form-item>
                    <el-form-item prop="printSize" label="打印尺寸" :label-width="formLabelWidth">
                        <select id = "Select02" v-model="printConfig.printSize" style="height: 28px; width: 70%; border:1px solid rgba(220,223,230)">
                            <option></option>
                        </select>
                    </el-form-item>
                    <el-form-item prop="type" label="打印类型" :label-width="formLabelWidth">
                        <el-select v-model="printConfig.type" size="mini">
                            <el-option
                                    v-for="item in types"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="激活状态" :label-width="formLabelWidth">
                        <el-switch
                                v-model="printConfig.state"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>


                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm2">取 消</el-button>
                <el-button type="primary" @click="commitForm2('createPrintConfigForm')">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="修改打印配置" :visible.sync="dialogUpdateVisible" :before-close="handleCloseUpdate" width="500px"
                   style="margin-top: -10vh;">
            <div>
                <el-form :model="rowData" :rules="rules" ref="updatePrintConfigForm" class="clearFloat">

                    <el-form-item prop="userId" label="用户名" :label-width="formLabelWidth">
                        <el-select v-model="rowData.userId" size="mini">
                            <el-option
                                    v-for="item in accounts"
                                    :key="item.userId"
                                    :label="item.userName"
                                    :value="item.userId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="wareId" label="仓库" :label-width="formLabelWidth">
                        <el-select v-model="rowData.wareId" size="mini">
                            <el-option
                                    v-for="item in wares"
                                    :key="item.wareId"
                                    :label="item.wareName"
                                    :value="item.wareId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="organizationId" label="货主" :label-width="formLabelWidth">
                        <el-select v-model="rowData.organizationId" size="mini">
                            <el-option
                                    v-for="item in organizations"
                                    :key="item.organizationId"
                                    :label="item.organizationName"
                                    :value="item.organizationId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="ip" label="IP地址" :label-width="formLabelWidth">
                        <el-input v-model="rowData.ip" size="mini" @blur="setClodopJS(rowData.ip)"></el-input>
                    </el-form-item>
                    <el-form-item prop="printerName" label="打印机名称" :label-width="formLabelWidth">
                        <el-input v-model="rowData.printerName" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="printSize" label="打印尺寸" :label-width="formLabelWidth">
                        <el-input v-model="rowData.printSize" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item prop="type" label="打印类型" :label-width="formLabelWidth">
                        <el-select v-model="rowData.type" size="mini">
                            <el-option
                                    v-for="item in types"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="激活状态" :label-width="formLabelWidth">
                        <el-switch
                                v-model="rowData.state"
                                active-value=1
                                inactive-value=0>
                        </el-switch>
                    </el-form-item>


                </el-form>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="commitForm('updatePrintConfigForm')">确 定</el-button>
            </div>
        </el-dialog>

        <div class="headerBtns">
            <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.go(-1)"> 返回
            </el-button>
            <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="showCreateModal"> 新建
            </el-button>
        </div>

        <div class="tableBox">
            <el-table
                    highlight-current-row
                    :data="configs"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="用户名">
                </el-table-column>
                <el-table-column
                        prop="wareName"
                        label="仓库">
                </el-table-column>
                <el-table-column
                        prop="organizationName"
                        label="货主">
                </el-table-column>
                <el-table-column
                        prop="ip"
                        label="IP 地址">
                </el-table-column>
                <el-table-column
                        prop="printerName"
                        label="打印机名称">
                </el-table-column>
                <el-table-column
                        prop="printSize"
                        label="打印尺寸">
                </el-table-column>
                <el-table-column
                        prop="type"
                        label="打印类型">
                    <template slot-scope="scope">
                        <span>{{scope.row.type === 1 ? '普通打印' : (scope.row.type === 2 ? '电子面单打印' : '条码打印')}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed
                        prop="state"
                        label="状态"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.state===1 ? '激活' : '未激活'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="110">
                    <template slot-scope="scope">
                        <el-button @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle
                                   plain
                                   size="small"></el-button>
                        <el-button @click="isDelete(scope.row.id)" icon="iconfont icon-shanchu" type="danger" circle
                                   plain size="small"></el-button>
                    </template>
                </el-table-column>


            </el-table>
        </div>

    </div>
</template>

<script>
    import {mapState} from "vuex";
    import {getLodop} from "./LodopFuncs";

    // let CLODOP = getLodop()

    export default {
        name: 'PrintConfig',
        data() {
            return {
                ips: [
                    {
                        label: 'A',
                        value: '192.168.0.29:8000'
                    },
                    {
                        label: 'B',
                        value: 'localhost:18000'
                    }
                ],
                configs: [],

                dialogNewVisible: false,
                dialogUpdateVisible: false,

                formLabelWidth: '120px',

                rowData: {},
                rules: [],
                rules2: [],
                types: [
                    {
                        value: 1,
                        label: '普通打印'
                    },
                    {
                        value: 2,
                        label: '电子面单打印'
                    },
                    {
                        value: 3,
                        label: '条码打印'
                    }
                ],
                wares: [],
                organizations: [],
                accounts: [],
                printConfig: {
                    userId: null,
                    userName: '',
                    ip: '',
                    printerName: '',
                    state: 0,
                    wareId: null,
                    wareName: '',
                    organizationId: null,
                    organizationName: '',
                    printSize: '',
                    type: null
                },
                printer: {}
            }
        },
        computed: {
            ...mapState([
                "abcLevels",
            ]),
        },
        methods: {


            //根据所选的IP和端口号注册打印服务
            demoSetClodopJS(strSrc) {
                let that = this
                
                strSrc = this.printConfig.ip
                strSrc = "http://" + strSrc + "/CLodopfuncs.js?priority=1";
                var oscript = this.demoCreateCLodopJSscript(strSrc);

                oscript.onload = oscript.onreadystatechange = function () {
                    if ((!oscript.readyState || /loaded|complete/.test(oscript.readyState)))
                        that.demoCreatePrinterList();
                };
            },


            demoCreateCLodopJSscript(strSrc) {
                var ScriptSS = document.getElementsByTagName("script");
                for (var i in ScriptSS) {
                    if (ScriptSS[i].src && (ScriptSS[i].src.indexOf("CLodopfuncs.js") >= 0)) {
                        if (ScriptSS[i].parentNode) ScriptSS[i].parentNode.removeChild(ScriptSS[i]);
                    }
                    ;
                }
                ;
                var oscript = document.createElement("script");
                if (strSrc.indexOf("src=") >= 0) {
                    strSrc = strSrc.match(/=[\',\"][^\',^\"].*(?=[\',\"])/i);
                    strSrc = strSrc[0].slice(2);
                }
                oscript.src = strSrc;
                var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
                head.insertBefore(oscript, head.firstChild);
                return oscript;
            },


            //获取打印机列表
            demoCreatePrinterList() {
                
                CLODOP.Create_Printer_List(document.getElementById('Select01'));
                this.demoCreatePagSizeList();


            },

            //获取打印机纸张大小
            demoCreatePagSizeList() {
                var oSelect = document.getElementById('Select02');
                var iPrintIndex = document.getElementById("Select01").value;
                CLODOP.Create_PageSize_List(oSelect, iPrintIndex);
            },


            showUpdateModal(row) {
                this.rowData = JSON.parse(JSON.stringify(row));
                this.rowData.state = String(row.state)
                this.dialogUpdateVisible = true
            },
            showCreateModal() {
                this.dialogNewVisible = true
            },
            isDelete(id) {
                this.$confirm('确认删除？')
                    .then(_ => {
                        this.deletePrintConfig(id)
                    })
                    .catch(_ => {
                    });
            },
            deletePrintConfig(id) {
                let that = this;
                IOT.showOverlay('删除中，请稍等...');
                IOT.getServerData('/print/params/delete', 'get', {id: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('删除成功！', 'success', 1000, () => {
                            that.getConfigData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            handleCloseUpdate() {

            },
            handleCloseNew() {
            },
            getConfigData() {
                IOT.getServerData('/print/params/list', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.configs = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            resetForm() {
                this.dialogUpdateVisible = false
            },
            resetForm2() {
                this.dialogNewVisible = false
            },
            commitForm(formName) {
                let rowData = PF.JSON(this.rowData);
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/print/params/update', 'post', rowData, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getConfigData()
                                });
                            } else {
                                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                            }
                        });
                        this.resetForm()
                        this.dialogUpdateVisible = false
                        IOT.hideOverlay();
                    } else {
                        console.log('validate failed')
                    }
                })
            },
            commitForm2(formName) {
                
                let printerName = $('#Select01 option:selected').text()
                this.printConfig.printerName = printerName
                let printConfig = PF.JSON(this.printConfig);
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        IOT.showOverlay('保存中，请稍等...');
                        IOT.getServerData('/print/params/add', 'post', printConfig, (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips('保存成功！', 'success', 1000, () => {
                                    this.getConfigData()
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
            getAccountData() {
                IOT.getServerData('/users/userList', 'get', {}, (ret) => {
                    if (ret.code === 200) {
                        this.accounts = ret.rows
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },

            getLodopData() {
                
                LODOP = getLodop();
                let arr = []
                let iPrinterCount = LODOP.GET_PRINTER_COUNT();
                for (let i = 0; i < iPrinterCount; i++) {
                    arr.push({
                        label: LODOP.GET_PRINTER_NAME(i),
                        value: i
                    })
                }
                let defaultName = LODOP.GET_PRINTER_NAME('-1')  //GET_PRINTER_NAME(intPrinterIndex);用序号获得打印机名，一般序号从0开始，-1特指默认打印机；
                arr.forEach(item => {
                    if (item.label === defaultName) {
                        item.value = '-1'
                        item.label += '-默认打印机'
                    }
                })
                console.log(defaultName)
                this.printOptions = arr
            }

        },
        created() {

            this.getConfigData()
            PF.getOrganizations((rows) => {
                this.organizations = rows
            })
            this.getWareData()
            this.getAccountData()


        }
        ,
        mounted() {

        }
    }
</script>

<style scoped lang="less">
    .item {

    }
</style>
