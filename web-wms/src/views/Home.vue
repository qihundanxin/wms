<template>
    <div
        class="home clearFloat"
        @click="hideBigFloatBox"
        @mouseover="updateTimeOut"
    >
        <div v-show="isFullScreen" class="leftBox leftFloat">
            <div class="logoBox">
                <router-link :to="{ name: 'homepage' }">
                    <img
                        v-show="!leftMenuIsCollapse"
                        src="@/assets/images/foodLogo.png"
                        alt=" "
                        title="  WMS系统"
                    />
                    <img
                        v-show="leftMenuIsCollapse"
                        src="@/assets/images/foodLogoS.png"
                        alt=" "
                        title="  WMS系统"
                    />
                 
                </router-link>
            </div>
            <div class="menuBox" id="menuWrapper">
                <LeftMenu
                    @isCollapse="isCollapse"
                    :leftMenusIndex="leftMenusIndex"
                ></LeftMenu>
            </div>
        </div>
        <div class="rightBox rightFloat">
            <Header
                v-show="isFullScreen"
                @changeLeftMenusIndex="changeLeftMenusIndex"
                :showBigFloatBoxUserHome="showBigFloatBoxUser"
                @changeShowBigFloatBoxUser="changeShowBigFloatBoxUser"
                :showBigFloatBoxWareHome="showBigFloatBoxWare"
                @changeShowBigFloatBoxWare="changeShowBigFloatBoxWare"
                :showBigFloatBoxSysHome="showBigFloatBoxSys"
                @changeShowBigFloatBoxSys="changeShowBigFloatBoxSys"
                @fullScreen="fullScreen"
            ></Header>
            <router-view class="contentBox" />
        </div>
        <!-- <Footer></Footer> -->

        <el-dialog
            :title="publicModalList.title + '信息'"
            :visible.sync="dialogPublicVisible"
            width="30%"
            style="margin-top: -10vh"
        >
            <ul class="publicModalUl">
                <li v-for="(item,index) in publicModalList.list" :key="item.value+index">
                    <div>
                        <span v-if="item.label">{{ item.label }}:</span>
                        <span v-else>-</span>
                        <i></i>
                    </div>
                    <div>
                        <span v-if="item.value">{{ item.value }}</span>
                        <span v-else>-</span>
                    </div>
                </li>
            </ul>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogPublicVisible = false"
                    >关 闭</el-button
                >
            </span>
        </el-dialog>

        <el-dialog
            title="设置打印机"
            :visible.sync="dialogPrinterVisible"
            width="30%"
            style="margin-top: -10vh"
        >
            <el-form class="clearFloat">
                <!--  <el-form-item prop="ip" label="IP地址(包括端口)" :label-width="formLabelWidth">
                        <el-input size="mini" v-model="printConfig.ip" @blur="demoSetClodopJS('')"></el-input>
                    </el-form-item>-->
                <el-form-item label=" " label-width="80px">
                    <el-link
                        href="http://demo.c-lodop.com:8000/Lodop6.226_Clodop4.127.zip"
                        type="primary"
                        >如果没有安装C-LODOP，请点击下载安装</el-link
                    >
                </el-form-item>
                <el-form-item label="参数类型" label-width="120px">
                     <el-select 
                       
                            v-model="qzParam"
                            @change="changeQzParam"
                            
                        >
                        <el-option  
                            label="配置参数"
                            key="0"
                            value="0"></el-option>
                             <el-option  
                            label="强制参数"
                            key="1"
                            value="1"></el-option>
                        
                    </el-select>
                    <!-- <template>
                        <el-radio v-model="qzParam" label=0
                            >配置参数</el-radio
                        >
                        <el-radio v-model="qzParam" label=1
                            >强制参数</el-radio
                        >
                    </template> -->
                </el-form-item>
                <el-form-item  v-if="qzParam==0" label="打印参数" label-width="120px">
                    <el-select 
                       
                            v-model="printIpId"
                            @change="setPrintParam(printIpId)"
                        >
                        <el-option v-for="ip in printIps"
                           
                            :label="ip.ip"
                            :key="ip.printIpId"
                            :value="ip.printIpId"></el-option>
                        
                    </el-select>
                </el-form-item>
                 
                <div v-if="qzParam==1">
                    <el-form-item label="远程LODOP" label-width="120px">
                        <el-input v-model="ycIP" placeholder="请输入远程IP"></el-input>
                        <el-button @click="openYc" type="primary">连接</el-button>
                    </el-form-item>
                    <el-form-item label="打印机名称" label-width="120px">
                        <select
                            id="Select011"
                            @change="demoCreatePagSizeList()"
                            v-model="printerIndex"
                            style="
                                height: 28px;
                                width: 70%;
                                border: 1px solid rgba(220, 223, 230);
                            "
                        >
                            <option></option>
                        </select>
                    </el-form-item>
                    <el-form-item label="打印尺寸" label-width="120px">
                        <select
                            id="Select022"
                            v-model="printSize"
                            style="
                                height: 28px;
                                width: 70%;
                                border: 1px solid rgba(220, 223, 230);
                            "
                        >
                            <option></option>
                        </select>
                    </el-form-item>
                    <el-form-item label="上边距" label-width="120px">
                        <el-input
                            v-model="printTop"
                            placeholder="请输入内容"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="左边距" label-width="120px">
                        <el-input
                            v-model="printLeft"
                            placeholder="请输入内容"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="打印方向" label-width="120px">
                        <template>
                            <el-radio v-model="printFangxiang" label="1"
                                >纵向</el-radio
                            >
                            <el-radio v-model="printFangxiang" label="2"
                                >横向</el-radio
                            >
                        </template>
                    </el-form-item>
                    <el-form-item label="是否双面" label-width="120px">
                        <template>
                            <el-radio v-model="printDuplex" label="1"
                                >单面</el-radio
                            >
                            <el-radio v-model="printDuplex" label="2"
                                >双面</el-radio
                            >
                        </template>
                    </el-form-item>

                </div>
                
                <!--  <el-form-item   label="宽"  label-width="120px">
                        <el-input v-model="printWidth" placeholder="请输入内容"></el-input>
                    </el-form-item> 
                    <el-form-item   label="高"  label-width="120px">
                        <el-input v-model="printHeight" placeholder="请输入内容"></el-input>
                    </el-form-item>   -->
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogPrinterVisible = false"
                    >关 闭</el-button
                >
                <el-button type="primary" @click="commitPrinter"
                    >确定</el-button
                >
            </span>
        </el-dialog>

        <el-dialog
            title="打印预览"
            :visible.sync="printAreaPrinterVisible"
            width="80%"
            style="margin-top: -10vh"
        >
            <div v-html="printAreaHtml" id="printArea"></div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="printUrl(1)">printUrl</el-button>
                 <el-button @click="llqPrint()">打印</el-button>
                <el-button @click="printAreaPrinterVisible = false"
                    >关 闭</el-button
                >
            </span>
        </el-dialog>
        <BillRecordBox></BillRecordBox>
    </div>
</template>

<script>
 
// @ is an alias to /src
import LeftMenu from "@/components/LeftMenu";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import BillRecordBox from '@/components/BillRecordBox.vue';
import { getLodop } from './base/user/LodopFuncs';

export default {
    name: "Home",
    components: {
        LeftMenu,
        Header,
        BillRecordBox,
        Footer,
    },
    data() {
        return {
            logoUrl1:IOT.getSysParam("logoUrl1"),
            logoUrl2:IOT.getSysParam("logoUrl2"),
            printIps:[],
            printIpId:'',
            qzParam:1,
            ycIP:'',
            strHTML: "",
            token: localStorage.getItem("token"),
            params: this.$route.params,
            fullHeight: document.documentElement.clientHeight,
            minHeight: 0,
            leftMenuIsCollapse: false,
            showBigFloatBoxUser: null,
            showBigFloatBoxWare: null,
            showBigFloatBoxSys: null,
            leftMenusIndex: 0,
            timeOut: null,
            timeOutTime: 1000 * 60 * 60,
            dialogPublicVisible: false,
            publicModalList: {},
            isFullScreen: true,
            dialogPrinterVisible: false,
            printAreaPrinterVisible: false,
            printerIndex: "",
            printSize: "",
            printTop: 0,
            printLeft: 0,
            printWidth: "100%",
            printHeight: "100%",
            printFangxiang: "1",
            printAreaHtml: "",
            printDuplex:'1',
        };
    },
    provide(){
        return {
            showPublicModal: this.showPublicModal,
        };
    },
    methods: {
        setPrintParam(id){
            var that = this;
            IOT.setPrintParam(id,null  );
        },
        getPrintIps(){
            let that = this;
            
            this.printIps = [];
            let searchData = {
                wareId:PF.getLocal("wareId","number")
            }
            IOT.getServerData('/print/ips/list','get',searchData,(ret) => {
           
            if (ret.code === 200) {
                this.printIps = ret.rows; 
            } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            }
            });
      },
        initLodop(){
            var that = this;
             var ycIP = localStorage.getItem("ycIP");
            if(ycIP!=undefined && ycIP!=null && ycIP!=''){
                that.ycIP = ycIP;
                IOT.userSetClodopJS(ycIP);
                PF.setLocal("ycIP", that.ycIP);
                setTimeout(this.demoCreatePrinterList, 1000);
            }
        },
        openYc(){
            IOT.userSetClodopJS(this.ycIP);
             PF.setLocal("ycIP", this.ycIP);
            this.setPrinter(); 
        },
        llqPrint() {
            $("#printArea").jqprint();
        },

        playMusic(name) {
            var mp3 = "mp3/" + name + ".mp3";
            mp3 = new Audio(mp3);
            mp3.play(); //播放 mp3这个音频对象
        },
        printHtmlByParam(strHTML, isRreview, quantity,type) {
            var cssFile =   "<link rel='stylesheet' type='text/css' href='printTemp/css/deerPrint.css'>";
            strHTML = cssFile + "<body>" + strHTML + "</body>";
            if (isRreview == 1) {
                this.strHTML = strHTML;
                this.printArea(strHTML);
                return;
            } 
            if(this.qzParam==1){
                this.printHtml(strHTML,isRreview,quantity);
                return;
            }

            var paramDetail = IOT.getPrintParam(type);
            if(param==undefined || pram==null  ){
                this.$notify.info({
                    title: '提示',
                    message: '打印参数未设置'
                });
            }else{
                
               
                var printerIndex = paramDetail.printerIndex;
                var printSize = paramDetail.printSize;
                var printTop = paramDetail.printTop;
                var printLeft =paramDetail.printLeft;
                // var printWidth= PF.getLocal("printWidth","String");
                // var printHeight = PF.getLocal("printHeight","String");
                var printWidth = paramDetail.printWidth;
                var printHeight = paramDetail.printHeight;
                var printFangxiang = paramDetail.printFangxiang;
                var printDuplex = paramDetail.printDuplex;
                LODOP.SET_LICENSES("","A099C4F906A9700980194E334A43D85A300","","");
            //    LODOP.SET_LICENSES("","A4615E073C71151D8FC86D466B6E7F90929","23580B189D96F33252722697A57AC00506B","");
            // alert("SET_LICENSES执行了");
                LODOP.SET_PRINTER_INDEXA(printerIndex);
                LODOP.SET_PRINT_STYLEA(0,"HtmlWaitMilSecs",1000);
                LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
                LODOP.SET_PRINT_MODE("PRINT_DUPLEX",printDuplex);//0-不控制 1-不双面 2-双面(长边装订) 3-小册子双面(短边装订_长边水平) 
                LODOP.SET_PRINT_MODE("PRINT_DEFAULTSOURCE",1);//1-纸盒 4-手动 7-自动 0-不控制 
                LODOP.ADD_PRINT_HTM(
                    printTop,
                    printLeft,
                    printWidth,
                    printHeight,
                    strHTML
                );
                if(quantity==undefined && quantity==null && quantity==NaN){

                }else{
                    LODOP.SET_PRINT_COPIES(quantity);
                }
                
                if (isRreview == 2) {
                    LODOP.PREVIEW();
                } else if (isRreview == undefined) {
                    LODOP.PRINT();
                }
            }
            
        },

        printUrlByParam() {
            var paramDetail = IOT.getPrintParam(type);
            var strUrl  ="http://15.222.240.207/pdf?ids=1424273";
            var printerIndex = paramDetail.printerIndex;
            var printSize = paramDetail.printSize;
            var printTop = paramDetail.printTop;
            var printLeft =paramDetail.printLeft;
            // var printWidth= PF.getLocal("printWidth","String");
            // var printHeight = PF.getLocal("printHeight","String");
            var printWidth = paramDetail.printWidth;
            var printHeight = paramDetail.printHeight;
            var printFangxiang = paramDetail.printFangxiang;
            var printDuplex = paramDetail.printDuplex;
            LODOP.SET_LICENSES("","A099C4F906A9700980194E334A43D85A300","",""); 
            LODOP.SET_PRINTER_INDEXA(printerIndex);
            LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
            LODOP.ADD_PRINT_PDF(
                printTop,
                printLeft,
                printWidth,
                printHeight,
                strUrl
            );
            LODOP.PRINT();
        },


        printHtml(strHTML, isRreview, quantity) {
            var cssFile =
                "<link rel='stylesheet' type='text/css' href='printTemp/css/deerPrint.css'>";
            strHTML = cssFile + "<body>" + strHTML + "</body>";
            // strHTML =   "<body>" + strHTML + "</body>";
            if (isRreview == 1) {
                this.strHTML = strHTML;
                this.printArea(strHTML);
                return;
            }

            var printerIndex = PF.getLocal("printerIndex", "number");
            var printSize = PF.getLocal("printSize", "String");
            var printTop = PF.getLocal("printTop", "number");
            var printLeft = PF.getLocal("printLeft", "number");
         
            var printWidth = this.printWidth;
            var printHeight = this.printHeight;
            var printFangxiang = PF.getLocal("printFangxiang", "number");
            var printDuplex = PF.getLocal("printDuplex", "number");
            LODOP.SET_LICENSES("","A099C4F906A9700980194E334A43D85A300","","");
         
            LODOP.SET_PRINTER_INDEXA(printerIndex);
            LODOP.SET_PRINT_STYLEA(0,"HtmlWaitMilSecs",1000);
            LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
            LODOP.SET_PRINT_MODE("PRINT_DUPLEX",printDuplex);//0-不控制 1-不双面 2-双面(长边装订) 3-小册子双面(短边装订_长边水平) 
            LODOP.SET_PRINT_MODE("PRINT_DEFAULTSOURCE",1);//1-纸盒 4-手动 7-自动 0-不控制 
            LODOP.ADD_PRINT_HTM(
                printTop,
                printLeft,
                printWidth,
                printHeight,
                strHTML
            );
            if(quantity==undefined && quantity==null && quantity==NaN){

            }else{
                LODOP.SET_PRINT_COPIES(quantity);
            }
            while(true){
                if (LODOP.webskt && LODOP.webskt.readyState==1){ 
                   if (isRreview == 2) {
                        LODOP.PREVIEW();
                    } else if (isRreview == undefined) {
                        LODOP.PRINT();
                    }
                    return;
                }  
            }
            
            
        },

        demoDownloadPDF(url){
            if (!(/^https?:/i.test(url))) return;
            if (window.XMLHttpRequest) var xhr = new XMLHttpRequest(); else var xhr = new ActiveXObject("MSXML2.XMLHTTP");
            xhr.open('GET',url,false); //同步方式
            if (xhr.overrideMimeType)
            try{
                xhr.responseType = 'arraybuffer';
                var arrybuffer=true;
            }catch(err){
                xhr.overrideMimeType('text/plain; charset=x-user-defined');
            }
            xhr.send(null);
            var data = xhr.response || xhr.responseBody;
            if (typeof Uint8Array !== 'undefined') {
                if (arrybuffer) var dataArray = new Uint8Array(data); else {
                        var dataArray = new Uint8Array(data.length);
                        for (var i = 0; i < dataArray.length; i++) {dataArray[i] = data.charCodeAt(i);}
                }
            } else
            var dataArray = VBS_BinaryToArray(data).toArray(); //兼容IE低版本
            return this.demoGetBASE64(dataArray);
        },

        demoGetBASE64(dataArray) {
            var digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
            var strData = "";
            for (var i = 0, ii = dataArray.length; i < ii; i += 3) {
                if (isNaN(dataArray[i])) break;
                var b1 = dataArray[i] & 0xFF, b2 = dataArray[i + 1] & 0xFF, b3 = dataArray[i + 2] & 0xFF;
                var d1 = b1 >> 2, d2 = ((b1 & 3) << 4) | (b2 >> 4);
                var d3 = i + 1 < ii ? ((b2 & 0xF) << 2) | (b3 >> 6) : 64;
                var d4 = i + 2 < ii ? (b3 & 0x3F) : 64;
                strData += digits.substring(d1, d1 + 1) + digits.substring(d2, d2 + 1) + digits.substring(d3, d3 + 1) + digits.substring(d4, d4 + 1);
            }
            return strData;
        },

      

        printUrl(strUrl) {
             var that = this;
            var  LODOP2 = getLodop();
             LODOP2.PRINT_INIT("001");
           
             LODOP2.SET_LICENSES("","A099C4F906A9700980194E334A43D85A300","","");
            
         
            var printerIndex = PF.getLocal("printerIndex", "number");
            var printSize = PF.getLocal("printSize", "String");
            var printTop = PF.getLocal("printTop", "number");
            var printLeft = PF.getLocal("printLeft", "number");
            var printWidth = PF.getLocal("printWidth", "String");
            var printHeight = PF.getLocal("printHeight", "String");
            var printFangxiang = PF.getLocal("printFangxiang", "number");
           
            LODOP2.SET_PRINTER_INDEXA(printerIndex);
            LODOP2.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
            LODOP2.ADD_PRINT_PDF(
                printTop,
                printLeft,
                '100%',
                '100%',
                that.demoDownloadPDF(strUrl)
            ); 
             LODOP2.PRINT();
            while(true){
                if (LODOP2.webskt && LODOP2.webskt.readyState==1){ 
                   //  准备好
                   LODOP2.PRINT();
                   return;
                }  
            }
            
            
        },
        changeQzParam(){
            PF.setLocal("qzParam", this.qzParam);
        },

        commitPrinter() {
            
            PF.setLocal("ycIP", this.ycIP);
            PF.setLocal("printerIndex", this.printerIndex);
            PF.setLocal("printSize", this.printSize);
            PF.setLocal("printTop", this.printTop);
            PF.setLocal("printLeft", this.printLeft);
            PF.setLocal("printWidth", this.printWidth);
            PF.setLocal("printHeight", this.printHeight);
            PF.setLocal("printFangxiang", this.printFangxiang);
            PF.setLocal("printDuplex", this.printDuplex);
            PF.setLocal("qzParam", this.qzParam);

            var aaaa = PF.getLocal("printerIndex", "String");
            var bbbb = PF.getLocal("printSize", "String");

            this.dialogPrinterVisible = false;
        },
        setPrinter() {
            this.ycIP = PF.getLocal("ycIP");
            this.qzParam = PF.getLocal("qzParam");
            this.printerIndex = PF.getLocal("printerIndex","number");
            this.printSize = PF.getLocal("printSize","number");
            this.printTop = PF.getLocal("printTop","number");
            this.printLeft = PF.getLocal("printLeft","number");
            this.printFangxiang = PF.getLocal("printFangxiang");
            this.printDuplex = PF.getLocal("printDuplex"); 

 

            
            this.dialogPrinterVisible = true;
          //   IOT.userSetClodopJS(this.ycIP+":8000"); 

            setTimeout(this.demoCreatePrinterList, 1000);
          
        },
        printArea(html) {
            this.printAreaPrinterVisible = true;
            this.printAreaHtml = html;
        },
        //获取打印机列表
        demoCreatePrinterList() {
        
            var ele = document.getElementById("Select011");
            CLODOP.Create_Printer_List(ele);
           
            document.getElementById("Select011").value = this.printerIndex;

            this.demoCreatePagSizeList(true);

        },

        //获取打印机纸张大小
        demoCreatePagSizeList(noUpdate) {
            if (noUpdate) {
            }
            var iPrintIndex = document.getElementById("Select011").value;

            var oSelect = document.getElementById("Select022");
            CLODOP.Create_PageSize_List(oSelect, iPrintIndex);
            var printSize = PF.getLocal("printSize", "String");
            if (noUpdate && printSize != null && printSize != "null") {
                this.printSize = printSize;
                document.getElementById("Select022").value = printSize;
            }
        },
        isCollapse(val) {
            this.leftMenuIsCollapse = val;
            if (val) {
                document.querySelector(".leftBox").style.width = "64px";
                document.querySelector(".rightBox").style.width =
                    "calc(100% - 64px)";
            } else {
                document.querySelector(".leftBox").style.width = "160px";
                document.querySelector(".rightBox").style.width =
                    "calc(100% - 160px)";
            }
        },
        changeLeftMenusIndex(val) {
            this.leftMenusIndex = val;
        },
        changeShowBigFloatBoxUser(val) {
            this.showBigFloatBoxUser = val;
        },
        changeShowBigFloatBoxWare(val) {
            this.showBigFloatBoxWare = val;
        },
        changeShowBigFloatBoxSys(val) {
            this.showBigFloatBoxSys = val;
        },
        hideBigFloatBox() {
            this.showBigFloatBoxUser = false;
            this.showBigFloatBoxWare = false;
            this.showBigFloatBoxSys = false;
        },
        handleResize(event) {
            this.fullHeight = document.documentElement.clientHeight;
            this.minHeight =
                document.querySelector(".contentBox").clientHeight +
                60 -
                20 -
                30;
          this.changeMenuHeight();
        },
        changeMenuHeight() {
          document.getElementById('menuWrapper').style.height = this.fullHeight - 70 +  'px'
          // document.querySelector(".menuBox").style.minHeight = '500px'
          //   let boxHeight = this.fullHeight + 70;
            // document.querySelector(".menuBox").style.height = boxHeight + "px";
            // //  document.querySelector('.contentBox').style.height = boxHeight + 'px';
            // if (this.minHeight > 0) {
            //     let boxMinHeight = this.minHeight + 70;
            //     document.querySelector(".menuBox").style.minHeight =
            //         boxMinHeight + "px";
            //     //  document.querySelector('.contentBox').style.minHeight = boxMinHeight + 'px';
            // }
        },
        getTimeOutTime() {
            IOT.getServerData(
                "/sys/params",
                "get",
                { paramCode: "timeOutTime" },
                (ret) => {
                    if (ret.code === 200) {
                        let value = ret.rows[0].value;
                        this.timeOutTime = Number(value) * 60 * 1000;
                        this.updateTimeOut();
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        updateTimeOut() {
            clearTimeout(this.timeOut);
            /*this.timeOut = setTimeout(() => {
          PF.publicMessage('您长时间未操作,请重新登录!', 'error');
          this.$router.push({name: 'login'})
        },this.timeOutTime)*/
        },
        handleGrandfatherMethod(param,modalName) {
            this.showPublicModal(param, modalName)
        },
        showPublicModal(param, modalName) {
            if (!PF.isNull(param)) {
                PF.publicMessage("页面错误,请联系管理员!", "error");
                return false;
            }
            if (modalName === "ware") {
                this.getWareData(param);
            } else if (modalName === "organization") {
                this.getOrganizationData(param);
            } else if (modalName === "supplier") {
                this.getSupplierData(param);
            } else if (modalName === "customer") {
                this.getCustomerData(param);
            } else if (modalName === "account") {
                this.getAccountData(param);
            } else if (modalName === "item") {
                this.getItemData(param);
            } else if (modalName === "batch") {
                this.getBatchData(param);
            } else if (!PF.isNull(param)) {
                PF.publicMessage("页面错误,请联系管理员!", "error");
                return false;
            }
        },
        getWareData(param) {
            IOT.getServerData(
                "/ware/infos/findSome",
                "get",
                { wareId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                            if (key === "wareName") {
                                list.push({
                                    label: "仓库名称",
                                    value: row[key],
                                });
                            } else if (key === "wareCode") {
                                list.push({
                                    label: "仓库编码",
                                    value: row[key],
                                });
                            } else if (key === "dutyUserName") {
                                list.push({ label: "联系人", value: row[key] });
                            } else if (key === "linkPhone") {
                                list.push({
                                    label: "联系电话",
                                    value: row[key],
                                });
                            } else if (key === "createTime") {
                                list.push({
                                    label: "创建时间",
                                    value: row[key],
                                });
                            } else if (key === "memo") {
                                list.push({ label: "备注", value: row[key] });
                            }
                        }
                        this.publicModalList = { title: "仓库", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getOrganizationData(param) {
            IOT.getServerData(
                "/organizations/list",
                "get",
                { organizationId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                            if (key === "organizationName") {
                                list.push({
                                    label: "货主名称",
                                    value: row[key],
                                });
                            } else if (key === "organizationCode") {
                                list.push({
                                    label: "货主编码",
                                    value: row[key],
                                });
                            } else if (key === "linkMan") {
                                list.push({ label: "联系人", value: row[key] });
                            } else if (key === "linkPhone") {
                                list.push({
                                    label: "联系电话",
                                    value: row[key],
                                });
                            } else if (key === "createTime") {
                                list.push({
                                    label: "创建时间",
                                    value: row[key],
                                });
                            } else if (key === "upperTacticName") {
                                list.push({
                                    label: "上架策略",
                                    value: row[key],
                                });
                            } else if (key === "batchTacticName") {
                                list.push({
                                    label: "批次策略",
                                    value: row[key],
                                });
                            } else if (key === "soMergeTacticName") {
                                list.push({
                                    label: "合并策略",
                                    value: row[key],
                                });
                            } else if (key === "qcTacticName") {
                                list.push({
                                    label: "质检策略",
                                    value: row[key],
                                });
                            } else if (key === "memo") {
                                list.push({ label: "备注", value: row[key] });
                            } else if (key === "organizationStatus") {
                                list.push({ label: "", value: row[key] });
                            }
                        }
                        this.publicModalList = { title: "货主", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getSupplierData(param) {
            IOT.getServerData(
                "/supplier/manages/list",
                "get",
                { supplierId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        console.log("------------供应商信息----------------");
                        console.log(row);
                        let list = [];
                        this.publicModalList={};
                        for (let key in row) {
                            if (key === "supplierName") {
                                list.push({
                                    label: "供应商名称",
                                    value: row[key],
                                });
                            } else if (key === "supplierCode") {
                                list.push({
                                    label: "供应商编码",
                                    value: row[key],
                                });
                            } else if (key === "linkMan") {
                                list.push({ label: "联系人", value: row[key] });
                            } else if (key === "linkPhone") {
                                list.push({
                                    label: "联系电话",
                                    value: row[key],
                                });
                            } else if (key === "createTime") {
                                list.push({
                                    label: "创建时间",
                                    value: row[key],
                                });
                            } else if (key === "memo") {
                                list.push({ label: "备注", value: row[key] });
                            }
                            
                        }
                        this.publicModalList = { title: "供应商", list: list };
                        this.dialogPublicVisible = true;
                        console.log(this.publicModalList);
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getCustomerData(param) {
            IOT.getServerData(
                "/customers/list",
                "get",
                { customerId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                            if (key === "customerName") {
                                list.push({
                                    label: "客户名称",
                                    value: row[key],
                                });
                            } else if (key === "customerCode") {
                                list.push({
                                    label: "客户编码",
                                    value: row[key],
                                });
                            } else if (key === "linkMan") {
                                list.push({ label: "联系人", value: row[key] });
                            } else if (key === "linkPhone") {
                                list.push({
                                    label: "联系电话",
                                    value: row[key],
                                });
                            } else if (key === "organizationName") {
                                list.push({ label: "货主", value: row[key] });
                            } else if (key === "carrierName") {
                                list.push({ label: "承运方", value: row[key] });
                            } else if (key === "address") {
                                list.push({ label: "地址", value: row[key] });
                            } else if (key === "memo") {
                                list.push({ label: "备注", value: row[key] });
                            }
                        }
                        this.publicModalList = { title: "客户", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getAccountData(param) {
            IOT.getServerData(
                "/users/userList",
                "get",
                { userId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                            if (key === "userName") {
                                list.push({
                                    label: "用户名称",
                                    value: row[key],
                                });
                            } else if (key === "jobNumber") {
                                list.push({ label: "工号", value: row[key] });
                            } else if (key === "mobile") {
                                list.push({
                                    label: "联系电话",
                                    value: row[key],
                                });
                            } else if (key === "createTime") {
                                list.push({
                                    label: "创建时间",
                                    value: row[key],
                                });
                            } else if (key === "deptName") {
                                list.push({ label: "部门", value: row[key] });
                            }
                        }
                        this.publicModalList = { title: "用户", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getItemData(param) {
            IOT.getServerData(
                "/item/infos/list",
                "get",
                { itemCode: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                             
                            if (key === "itemName") {
                                list.push({
                                    label: "SKU名称",
                                    value: row[key],
                                });
                            } else if (key === "itemCode") {
                                list.push({
                                    label: "SKU",
                                    value: row[key],
                                });
                            } else if (key === "itemTypeName") {
                                list.push({
                                    label: "商品种类",
                                    value: row[key],
                                });
                            } else if (key === "organizationName") {
                                list.push({ label: "货主", value: row[key] });
                            } else if (key === "supplierName") {
                                list.push({ label: "供应商", value: row[key] });
                            } else if (key === "packName") {
                                list.push({
                                    label: "包装名称",
                                    value: row[key],
                                });
                            } else if (key === "batchTacticName") {
                                list.push({
                                    label: "批次策略",
                                    value: row.batchTacticName,
                                });
                            } else if (key === "upperTacticName") {
                                list.push({
                                    label: "上架策略",
                                    value: row[key],
                                });
                            // } else if (key === "upperLimit") {
                            //     list.push({
                            //         label: "库存上限",
                            //         value: row[key],
                            //     });
                            // } else if (key === "floorLimit") {
                            //     list.push({
                            //         label: "库存下限",
                            //         value: row[key],
                            //     });
                            // } else if (key === "defaultPrice") {
                            //     list.push({
                            //         label: "默认单价",
                            //         value: row[key],
                            //     });
                            } else if (key === "weight") {
                                list.push({ label: "重量", value: row[key] });
                            } else if (key === "spec") {
                                list.push({ label: "体积", value: row[key] });
                            } else if (key === "model") {
                                list.push({ label: "型号", value: row[key] });
                            }  
                        }
                        this.publicModalList = { title: "商品", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getBatchData(param) {
            IOT.getServerData(
                "/item/batchs/list",
                "get",
                { batchId: param },
                (ret) => {
                    if (ret.code === 200) {
                        let row = PF.JSON(ret.rows[0]);
                        let list = [];
                        for (let key in row) {
                            if( row[key]!=null){
                                    if (key === "productDate") {
                                        list.push({
                                            label: "生产日期",
                                            value: row[key],
                                        });
                                    } else if (key === "exDate") {
                                        list.push({
                                            label: "失效日期",
                                            value: row[key],
                                        });
                                    } else if (key === "inDate") {
                                        list.push({
                                            label: "入库日期",
                                            value: row[key],
                                        });
                                    } else if (key === "supplierCode") {
                                        list.push({
                                            label: "供应商代码",
                                            value: row[key],
                                        });
                                    } else if (key === "supplierBatch") {
                                        list.push({
                                            label: "厂家批号",
                                            value: row[key],
                                        });
                                    } else if (key === "qcState") {
                                        list.push({
                                            label: "质量状态",
                                            value: row[key],
                                        });
                                    } else if (key === "costPrice") {
                                        list.push({
                                            label: "成本单价",
                                            value: row[key],
                                        });
                                    } else if (key === "salePrice") {
                                        list.push({
                                            label: "销售单价",
                                            value: row[key],
                                        });
                                    } else if (key === "detailNo") {
                                        list.push({
                                            label: "单据行编码",
                                            value: row[key],
                                        });
                                    } else if (key === "memo1") {
                                        list.push({ label: "MEMO1", value: row[key] });
                                    } else if (key === "memo2") {
                                        list.push({ label: "MEMO2", value: row[key] });
                                    } else if (key === "memo3") {
                                        list.push({ label: "MEMO3", value: row[key] });
                                    } else if (key === "batchAttribute1") {
                                        list.push({
                                            label: "BATCH_AT_1",
                                            value: row[key],
                                        });
                                    } else if (key === "batchAttribute2") {
                                        list.push({
                                            label: "BATCH_AT_2",
                                            value: row[key],
                                        });
                                    }
                            }
                            
                        }
                        this.publicModalList = { title: "批次", list: list };
                        this.dialogPublicVisible = true;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        toFullscreen() {
            let de = document.documentElement;
            if (de.requestFullscreen) {
                de.requestFullscreen();
            } else if (de.mozRequestFullScreen) {
                de.mozRequestFullScreen();
            } else if (de.webkitRequestFullScreen) {
                de.webkitRequestFullScreen();
            }
        },
        fullScreen(val) {
            if (val === false) {
                // this.isFullScreen = val;
                // document.querySelector('.rightBox').style.width = '100%';
                // PF.publicMessage('已开启全屏,退出请按 Esc', 'info', 2000);
                this.toFullscreen();
            }
        },
        offFullScreen() {
            // this.isFullScreen = true;
            // document.querySelector('.leftBox').style.width = '200px';
            // document.querySelector('.rightBox').style.width = 'calc(100% - 200px)';
            // PF.publicMessage('已退出全屏', 'info', 1000);
        },
    },
    created() {
        let that = this;
        if (!this.token) {
            PF.publicMessage("请重新登录!", "error");
            this.$router.push({ name: "login" });
        }
        window.addEventListener("resize", this.handleResize);
        this.getTimeOutTime();

        document.onkeydown = function (e) {
            let key = window.event.keyCode;
            if (key == 27) {
                that.offFullScreen();
            }
        };

        PF.getSysParamUdfData("language", [], (data, udfs) => {
            this.$i18n.locale = udfs[0].value;
        });
        this.getPrintIps();
        

       
    },
    beforeDestroy() {
        window.removeEventListener("resize", this.handleResize);
    },
    mounted() {
        let that = this;
        if (!this.token) {
            PF.publicMessage("请重新登录!", "error");
            this.$router.push({ name: "login" });
        }
        this.changeMenuHeight(); 
    },
};
</script>

<style lang="less">
.home {
    width: 100%;
    min-width: 970px;
    background: #f8f9ff;
    color: #08274c;
    font-size: 14px;
    height: 100%;
    padding-bottom: 30px;

    .leftBox {
        bottom: 30px;
        width: 160px;
        transition: width 0.3s;

        .logoBox {
            width: 100%;
            height: 70px;
            text-align: center;

            img {
                margin: 8px;
                max-width: 90%;
                height: auto;
                background-size: cover;
            }
        }

        .menuBox {
            width: 100%;
            background: #ffffff;
            border-radius: 0 20px 0 0;
            /*box-shadow: 0px 0px 20px 0px rgba(208, 231, 255, 0.48);*/
            box-shadow: 0px 0px 20px 0px rgba(193, 222, 255, 1);
            /*box-shadow: 0px 0px 20px 0px #66b1ffc7*/
            overflow-y: auto;
            overflow-x: hidden;
        }
    }

    .rightBox {
        width: calc(100% - 160px);
        transition: width 0.3s;

        > .header {
            height: 60px;
            line-height: 60px;
            border-radius: 0 0 0 20px;
            background: #ffffff;
            box-shadow: 0px 0px 20px 0px rgba(208, 231, 255, 0.48);
            margin-left: 10px;
        }

        .contentBox {
            height: 900px;
            margin: 10px 0px 0px 10px;
            overflow-x: hidden;
            overflow-y: scroll;
        }
    }
    .textBtn {
        color: #409eff;
        cursor: pointer;
    }
    .textBtn:hover {
        color: #66b1ff;
    }
    .textBtn:active {
        color: #3a8ee6;
    }

    .el-submenu .el-menu-item {
        min-width: 150px;
    }
    
    
}
.goog-te-banner-frame  {
              display: none;
    }
    .goog-te-banner{
        display: none;
    }
</style>
