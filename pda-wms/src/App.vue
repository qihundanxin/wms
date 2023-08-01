<template>
  <div v-loading.fullscreen.lock="fullscreenLoading" id="app">
    <div class="global-nav-bar">
      <div class="warning" v-show="warning">{{ warning + rdsUrl }}</div>
      <div class="error" v-show="error">{{ error + rdsUrl }}</div>
      <div class="blink" v-show="blink">{{ blink }}</div>
    </div>
    <div v-show="isLoading" class="loadingBox"></div>
    <router-view />
    <div class="bottomWare">{{ wareName }}</div>
    <el-dialog title="设置打印机" :visible.sync="dialogPrinterVisible" width="90%" style="margin-top: -10vh">
      <el-form class="clearFloat">
        <!--  <el-form-item prop="ip" label="IP地址(包括端口)" :label-width="formLabelWidth">
                        <el-input size="mini" v-model="printConfig.ip" @blur="demoSetClodopJS('')"></el-input>
                    </el-form-item>-->
        <!-- <el-form-item label=" " label-width="80px">
                    <el-link
                        href="http://demo.c-lodop.com:8000/Lodop6.226_Clodop4.127.zip"
                        type="primary"
                        >如果没有安装C-LODOP，请点击下载安装</el-link
                    >
                </el-form-item> -->
        <el-form-item label="IP" label-width="80px">
          <el-input v-model="printParam.ycIP" placeholder="请输入远程IP"></el-input>
          <el-button @click="openYc" type="primary">连接</el-button>
        </el-form-item>
        <el-form-item label="打印机" label-width="80px">
          <select id="Select011" @change="demoCreatePagSizeList()" v-model="printParam.printerIndex" style="
                            height: 28px;
                            width: 70%;
                            border: 1px solid rgba(220, 223, 230);
                        ">
            <option></option>
          </select>
        </el-form-item>
        <el-form-item label="纸张" label-width="80px">
          <select id="Select022" v-model="printParam.printSize" style="
                            height: 28px;
                            width: 70%;
                            border: 1px solid rgba(220, 223, 230);
                        ">
            <option></option>
          </select>
        </el-form-item>
        <!-- <el-form-item label="上边距" label-width="80px">
                    <el-input
                        v-model="printParam.printTop"
                        placeholder="请输入内容"
                    ></el-input>
                </el-form-item>
                <el-form-item label="左边距" label-width="80px">
                    <el-input
                        v-model="printParam.printLeft"
                        placeholder="请输入内容"
                    ></el-input>
                </el-form-item>
                <el-form-item label="打印方向" label-width="80px">
                    <template>
                        <el-radio v-model="printParam.printFangxiang" label="1"
                            >纵向</el-radio
                        >
                        <el-radio v-model="printParam.printFangxiang" label="2"
                            >横向</el-radio
                        >
                    </template>
                </el-form-item>
                 <el-form-item label="是否双面" label-width="80px">
                    <template>
                        <el-radio v-model="printParam.printDuplex" label="1"
                            >单面</el-radio
                        >
                        <el-radio v-model="printParam.printDuplex" label="2"
                            >双面</el-radio
                        >
                    </template>
                </el-form-item> -->
        <!--  <el-form-item   label="宽"  label-width="120px">
                        <el-input v-model="printWidth" placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item   label="高"  label-width="120px">
                        <el-input v-model="printHeight" placeholder="请输入内容"></el-input>
                    </el-form-item>   -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogPrinterVisible = false">关 闭</el-button>
        <el-button type="primary" @click="commitPrinter">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="打印预览" :visible.sync="printAreaPrinterVisible" width="80%" style="margin-top: -10vh">
      <div v-html="printAreaHtml" id="printArea"></div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="printUrl(1)">printUrl</el-button>
        <el-button @click="llqPrint()">打印</el-button>
        <el-button @click="printAreaPrinterVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import server from './server'
export default {
  name: 'App',
  data() {
    return {
      token: localStorage.getItem("pda-token"),
      baseIp: server.baseUrl,
      loading: null,
      isLoading: false,
      isRequest: true,
      wareName: '',
      fullscreenLoading: false,
      printAreaPrinterVisible: false,
      printAreaHtml: '',
      scan: null,
      dialogPrinterVisible: false,
      printParam: {
        ycIP: 'localhost',
        printerIndex: '',
        printSize: '',
        printTop: '0',
        printLeft: '0',
        printWidth: "100%",
        printHeight: "100%",
        printFangxiang: 1,
        printDuplex: 1,
      },
      error: '',
      warning: '',
      blink: '',
      rdsUrl: '',
      tipsMessageInfo: null
    }
  },
  methods: {
    getRds() {
      IOT.getServerData(
        "/properties/getDetail",
        "get",
        null,
        (ret) => {
          if (ret.code === 200) {
            this.rdsUrl = ret.data
            if (this.environment == 'test' && this.rdsUrl == "wms-db") {
              this.blink = '请不要使用！并尽快联系管理员！'
            }
            if ((this.environment == 'beta' || this.environment == 'prod') && this.rdsUrl != "wms-db") {
              this.blink = '当前系统不可用，请尽快联系管理员！'
            }
          } else {
            console.log("获取数据库地址失败");
          }
        },
        true,
        false
      );
    },
    printArea(html) {
      this.printAreaPrinterVisible = true;
      this.printAreaHtml = html;
    },
    qrPrint(html, ip, printIndex, size, fx, ds, quantity) {
      var that = this;
      that.demoCreateCLodopJSscript(ip);
      setTimeout(
        that.printHtml2(html, printIndex, size, fx, ds, quantity)
        , 10);
    },
    qrPrintUrl(url, ip, printIndex, size, fx, ds, quantity) {
      var that = this;
      that.demoCreateCLodopJSscript(ip);
      setTimeout(
        that.printUrl2(url, printIndex, size, fx, ds, quantity)
        , 10);
    },
    openYc() {
      this.userSetClodopJS(this.printParam.ycIP);
      PF.setLocal("ycIP", this.printParam.ycIP);
      setTimeout(this.setPrinter, 100);
    },
    setPrinter() {
      this.printParam.ycIP = PF.getLocal("ycIP");
      this.printParam.qzParam = PF.getLocal("qzParam");
      this.printParam.printerIndex = PF.getLocal("printerIndex", "number");
      this.printParam.printSize = PF.getLocal("printSize", "number");
      // this.printParam.printTop = PF.getLocal("printTop","number");
      // this.printParam.printLeft = PF.getLocal("printLeft","number");
      // this.printParam.printFangxiang = PF.getLocal("printFangxiang","number")==null?this.printParam.printFangxiang:PF.getLocal("printFangxiang","number")
      // this.printParam.printDuplex = PF.getLocal("printDuplex","number");
      this.dialogPrinterVisible = true;
      //   IOT.userSetClodopJS(this.ycIP+":8000");
      setTimeout(this.demoCreatePrinterList, 100);
    },
    //获取打印机列表
    demoCreatePrinterList() {
      var ele = document.getElementById("Select011");
      if (CLODOP == undefined || CLODOP == null) {
        PF.setLocal("ycIP", null);
      }
      CLODOP.Create_Printer_List(ele);
      this.printParam.printerIndex = PF.getLocal("printerIndex", "number");
      document.getElementById("Select011").value = this.printParam.printerIndex;
      this.demoCreatePagSizeList(true);
    },
    //获取打印机纸张大小
    demoCreatePagSizeList(noUpdate) {
      if (noUpdate) {
      }
      var iPrintIndex = document.getElementById("Select011").value;
      var oSelect = document.getElementById("Select022");
      CLODOP.Create_PageSize_List(oSelect, iPrintIndex);
      var printSize = localStorage.getItem("printSize");
      if (noUpdate && printSize != null && printSize != "null") {
        this.printParam.printSize = printSize;
        document.getElementById("Select022").value = printSize;
      }
    },
    userSetClodopJS: function (ip) {
      var that = this;
      var oscript = that.demoCreateCLodopJSscript(ip);
    },
    demoCreateCLodopJSscript: function (ip) {
      var that = this;
      var strSrc = "http://" + ip + ":8000/CLodopfuncs.js?priority=1";
      var ScriptSS = document.getElementsByTagName("script");
      for (var i in ScriptSS) {
        if (
          ScriptSS[i].src && ScriptSS[i].src.indexOf("CLodopfuncs.js") >= 0
        ) {
          if (ScriptSS[i].parentNode) {
            ScriptSS[i].parentNode.removeChild(ScriptSS[i]);
          }
        }
      }
      try {
        //加载双端口(8000和18000）避免其中某个端口被占用：
        var oscript2 = document.createElement("script");
        oscript2.src = "http://" + ip + ":18000/CLodopfuncs.js?priority=2";
        var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
        head.insertBefore(oscript2, head.firstChild);
      } catch (error) {
      }
      try {
        var oscript = document.createElement("script");
        if (strSrc.indexOf("src=") >= 0) {
          strSrc = strSrc.match(/=[\',\"][^\',^\"].*(?=[\',\"])/i);
          strSrc = strSrc[0].slice(2);
        }
        oscript.src = strSrc;
        var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
        head.insertBefore(oscript, head.firstChild);
      } catch (error) {
      }
      return oscript;
    },
    initLodop() {
      var that = this;
      var ycIP = localStorage.getItem("ycIP");
      if (ycIP != undefined && ycIP != null && ycIP != '') {
        that.printParam.ycIP = ycIP;
        that.userSetClodopJS(ycIP);
      } else {
        IOT.getServerData('/printer/infos/getPda', 'get', { wareId: localStorage.getItem("wareId") }, (ret) => {
          if (ret.code === 200) {
            var printer = ret.data;
            if (printer == null) {
              return;
            }
            that.printParam.ycIP = printer.ip;
            that.userSetClodopJS(that.printParam.ycIP);
            PF.setLocal("printerIndex", printer.printerIndex);
            that.printParam.printSize = printer.page;
            PF.setLocal("printSize", printer.page);
            that.printParam.printFangxiang = printer.fx;
            PF.setLocal("printFangxiang", printer.fx);
            that.printParam.printDuplex = printer.ds;
            PF.setLocal("printDuplex", printer.ds);
            setTimeout(that.demoCreatePrinterList, 1000);
          } else {
            IOT.tips(ret.message || '获取供应商信息失败！', 'error');
          }
        }, true);
      }
    },
    printHtml(strHTML, isRreview, quantity) {
      var cssFile =
        "<link rel='stylesheet' type='text/css' href='printTemp/css/deerPrint.css'>";
      strHTML = cssFile + "<body>" + strHTML + "</body>";
      // if (isRreview == 1) {
      //     this.strHTML = strHTML;
      //     this.printArea(strHTML);
      //     return;
      // }
      var printerIndex = PF.getLocal("printerIndex", "number");
      var printSize = PF.getLocal("printSize", "String");
      // var printTop = PF.getLocal("printTop", "number");
      // var printLeft = PF.getLocal("printLeft", "number");
      // var printWidth= PF.getLocal("printWidth","String");
      // var printHeight = PF.getLocal("printHeight","String");
      var printWidth = this.printParam.printWidth;
      var printHeight = this.printParam.printHeight;
      var printTop = this.printParam.printTop;
      var printLeft = this.printParam.printLeft;
      var printFangxiang = this.printParam.printFangxiang;
      var printDuplex = this.printParam.printDuplex;
      LODOP.SET_LICENSES("", "A099C4F906A9700980194E334A43D85A300", "", "");
      //LODOP.SET_LICENSES("","A4615E073C71151D8FC86D466B6E7F90929","23580B189D96F33252722697A57AC00506B","");
      // alert("SET_LICENSES执行了");
      LODOP.SET_PRINTER_INDEXA(printerIndex);
      LODOP.SET_PRINT_STYLEA(0, "HtmlWaitMilSecs", 1000);
      LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
      LODOP.SET_PRINT_MODE("PRINT_DUPLEX", printDuplex);//0-不控制 1-不双面 2-双面(长边装订) 3-小册子双面(短边装订_长边水平)
      LODOP.SET_PRINT_MODE("PRINT_DEFAULTSOURCE", 1);//1-纸盒 4-手动 7-自动 0-不控制
      LODOP.ADD_PRINT_HTM(
        printTop,
        printLeft,
        printWidth,
        printHeight,
        strHTML
      );
      if (quantity == undefined && quantity == null && quantity == NaN) {
      } else {
        LODOP.SET_PRINT_COPIES(quantity);
      }
      while (true) {
        if (LODOP.webskt && LODOP.webskt.readyState == 1) {
          if (isRreview == 2) {
            LODOP.PREVIEW();
          } else if (isRreview == undefined) {
            LODOP.PRINT();
          }
          return;
        }
      }
    },
    printHtml2(strHTML, printerIndex, printSize, printFangxiang, printDuplex, quantity) {
      var cssFile = "<link rel='stylesheet' type='text/css' href='printTemp/css/deerPrint.css'>";
      strHTML = cssFile + "<body>" + strHTML + "</body>";
      // if (true) {
      //     this.strHTML = strHTML;
      //     this.printArea(strHTML);
      //     return;
      // }
      // var printTop = PF.getLocal("printTop", "number");
      // var printLeft = PF.getLocal("printLeft", "number");
      // var printWidth= PF.getLocal("printWidth","String");
      // var printHeight = PF.getLocal("printHeight","String");
      var printWidth = this.printParam.printWidth;
      var printHeight = this.printParam.printHeight;
      var printTop = this.printParam.printTop;
      var printLeft = this.printParam.printLeft;
      var printFangxiang = this.printParam.printFangxiang;
      var printDuplex = this.printParam.printDuplex;
      LODOP.SET_LICENSES("", "A099C4F906A9700980194E334A43D85A300", "", "");
      //LODOP.SET_LICENSES("","A4615E073C71151D8FC86D466B6E7F90929","23580B189D96F33252722697A57AC00506B","");
      // alert("SET_LICENSES执行了");
      LODOP.SET_PRINTER_INDEXA(printerIndex);
      LODOP.SET_PRINT_STYLEA(0, "HtmlWaitMilSecs", 1000);
      LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
      LODOP.SET_PRINT_MODE("PRINT_DUPLEX", printDuplex);//0-不控制 1-不双面 2-双面(长边装订) 3-小册子双面(短边装订_长边水平)
      LODOP.SET_PRINT_MODE("PRINT_DEFAULTSOURCE", 1);//1-纸盒 4-手动 7-自动 0-不控制
      LODOP.ADD_PRINT_HTM(
        printTop,
        printLeft,
        printWidth,
        printHeight,
        strHTML
      );
      if (quantity == undefined && quantity == null && quantity == NaN) {
      } else {
        LODOP.SET_PRINT_COPIES(quantity);
      }
      while (true) {
        if (LODOP.webskt && LODOP.webskt.readyState == 1) {
          LODOP.PRINT();
          return;
        }
      }
    },
    printUrl2(strUrl, printerIndex, printSize, printFangxiang, printDuplex, quantity) {
      //  strUrl = "http://wms.facedrivesupply.com/public/pdf/11471331.pdf";
      //  LODOP.SET_PRINT_MODE("PRINT_DUPLEX",printDuplex);//0-不控制 1-不双面 2-双面(长边装订) 3-小册子双面(短边装订_长边水平)
      LODOP.SET_LICENSES("", "A099C4F906A9700980194E334A43D85A300", "", "");
      // LODOP.SET_LICENSES("","A4615E073C71151D8FC86D466B6E7F90929","23580B189D96F33252722697A57AC00506B","");
      LODOP.SET_PRINTER_INDEXA(printerIndex);
      LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
      LODOP.ADD_PRINT_PDF(
        '0',
        '0',
        '100%',
        '100%',
        strUrl
      );
      while (true) {
        if (LODOP.webskt && LODOP.webskt.readyState == 1) {
          LODOP.PRINT();
          return;
        }
      }
    },
    printUrl() {
      var strUrl = "http://15.222.240.207/pdf?ids=1424273";
      var printerIndex = PF.getLocal("printerIndex", "number");
      var printSize = PF.getLocal("printSize", "String");
      var printTop = PF.getLocal("printTop", "number");
      var printLeft = PF.getLocal("printLeft", "number");
      var printWidth = PF.getLocal("printWidth", "String");
      var printHeight = PF.getLocal("printHeight", "String");
      var printFangxiang = PF.getLocal("printFangxiang", "number");
      LODOP.SET_LICENSES("", "A099C4F906A9700980194E334A43D85A300", "", "");
      //   LODOP.SET_LICENSES("","A4615E073C71151D8FC86D466B6E7F90929","23580B189D96F33252722697A57AC00506B","");
      LODOP.SET_PRINTER_INDEXA(printerIndex);
      LODOP.SET_PRINT_PAGESIZE(printFangxiang, 0, 0, printSize);
      LODOP.ADD_PRINT_PDF(
        printTop,
        printLeft,
        printWidth,
        printHeight,
        strUrl
      );
      while (true) {
        if (LODOP.webskt && LODOP.webskt.readyState == 1) {
          LODOP.PRINT();
          return;
        }
      }
    },
    commitPrinter() {
      PF.setLocal("ycIP", this.printParam.ycIP);
      PF.setLocal("printerIndex", this.printParam.printerIndex);
      PF.setLocal("printSize", this.printParam.printSize);
      // PF.setLocal("printTop", this.printTop);
      // PF.setLocal("printLeft", this.printLeft);
      // PF.setLocal("printWidth", this.printWidth);
      // PF.setLocal("printHeight", this.printHeight);
      // PF.setLocal("printFangxiang", this.printFangxiang);
      // PF.setLocal("printDuplex", this.printDuplex);
      this.dialogPrinterVisible = false;
    },
    setPrintParam() {
      this.openYc();
      // this.dialogPrinterVisible  =true;
    }
  },
  computed: {
    environment() {
      if (window.location.origin.indexOf('//wms.') >= 0) {
        return 'prod';
      }
      if (window.location.origin.indexOf('//wms-beta.') >= 0) {
        return 'beta';
      }
      return 'test';
    },
  },
  created() {
    let that = this;
    window.SCAN = {
      startRecognize(success) {
        try {
          var filter;
          //自定义的扫描控件样式
          var styles = {
            top: '100px',
            left: '0px',
            width: '100%',
            height: '500px',
            position: 'static',
          }
          //扫描控件构造
          this.scan = plus.barcode.create('app', filter, styles);
          this.scan.onmarked = success;
          this.scan.onerror = this.onerror;
          plus.webview.currentWebview().append(this.scan);
          this.scan.start();
          //打开关闭闪光灯处理
          var flag = false;
          // document.getElementById("turnTheLight").addEventListener('tap', function() {
          //   if (flag == false) {
          //     scan.setFlash(true);
          //     flag = true;
          //   } else {
          //     scan.setFlash(false);
          //     flag = false;
          //   }
          // });
        } catch (e) {
          alert("出现错误啦:\n" + e);
        }
      },
      onerror(e) {
        // alert(e);
      },
      close() {
        //  plus.webview.currentWebview().removeItem(this.scan);
        this.scan.close();
      },
      onmarked(type, result) {
        var text = '';
        switch (type) {
          case plus.barcode.QR:
            text = 'QR: ';
            break;
          case plus.barcode.EAN13:
            text = 'EAN13: ';
            break;
          case plus.barcode.EAN8:
            text = 'EAN8: ';
            break;
        }
        //扫描成功之后的处理
        //alert(text + " : " + result);
        this.result = result;
        EventBus.$emit('scanResult', this.result)
        this.scan = null;
      },
    },
      window.IOT = {
        initLodop() {
          setTimeout(function () {
            that.initLodop();
          }, 1000);
        },
        qrPrint(html, ip, printIndex, size, fx, ds, quantity) {
          that.qrPrint(html, ip, printIndex, size, fx, ds, quantity);
        },
        qrPrintUrl(pdfUrl, ip, printIndex, size, fx, ds, quantity) {
          that.qrPrintUrl(pdfUrl, ip, printIndex, size, fx, ds, quantity);
        },
        setPrintParam() {
          that.setPrintParam();
        },
        printHtml(strHTML, isRreview, quantity) {
          that.printHtml(strHTML, isRreview, quantity);
        },
        getServerData(url, type, param, success, noToRet, isAsync, error) {
          this.fullscreenLoading = true;
          let params = {};
          let token = localStorage.getItem('pda-token');
          let baseUrl = that.baseIp + '/wms';
          type === 'get' ? params = PF.JSON(param) : params = JSON.stringify(param);
          let async = true;
          PF.isNull(isAsync) ? async = isAsync : async = true;
          console.log('pda-token:', token, ' url:', url, ' params:', JSON.stringify(params));
          $.ajax({
            type: type,
            dataType: "json",
            timeout: 1000 * 60 * 60,
            data: params,
            headers: {
              "Accept": "*/*",
              "access-token": token
            },
            contentType: "application/json",
            url: baseUrl + url,
            async: async,
            success: (res) => {
              this.fullscreenLoading = false;
              if (res.code === 10006 || res.code === 10007) {
                IOT.tips('访问令牌参数失效,请重新登录!', 'error', 2000, () => {
                  localStorage.removeItem('pda-token')
                  that.$router.push({ name: 'login' })
                });
                return false
              }
              if (noToRet) {
                success && success(res);
              } else {
                success && success(PF.toRet(res, true));
              }
            },
            error: (err) => {
              console.error(type, url, err);
              if(error){
                error(err);
              }
              let showInfo = '发生未知错误，请联系管理员！';
              if (err.status) {
                if (err.status == 504) {
                  showInfo = '系统链接超时，请稍后重试';
                }
              }else if(err.status === 0) {
                showInfo = '网络无响应，请重新加载';
              }
              IOT.tips(showInfo, "error", 5000, () => {
                IOT.hideOverlay();
              });
              error && error();
            },
          });
        },
        /**
          * 判断是否是数字
          *
          */
        isRealNum(val) {
          // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
          if (val === "" || val == null) {
            return false;
          }
          if (!isNaN(val) && typeof val === 'number') {
            //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,   //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
            return true;
          } else {
            return false;
          }
        },
        showOverlay(content) {
          let text = '';
          if (content) {
            text = content
          }
          that.loading = that.$loading({
            lock: true,
            text: text || '',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
        },
        hideOverlay() {
          if (that.loading) {
            that.loading.close();
          }
        },
        tips(message, type, time, end) {
          const messageInfo = that.tipsMessageInfo;
          if (messageInfo && messageInfo.expired > Date.now()) {
            that.tipsMessageInfo = { expired: Date.now() + 1000 };
            return;
          }
          that.tipsMessageInfo = { expired: Date.now() + 1000 };
          let duration = 2000;
          if (time) {
            duration = time
          }
          that.$message({
            showClose: true,
            dangerouslyUseHTMLString: true,
            message: '<div style="font-size: large;">' + message + '</div>',
            type: type,
            duration: duration,
            onClose: end
          });
        },
      };
    window.PF = {
      /** 精确加法 */
      accAdd(arg1, arg2) {
        let r1, r2, m
        try {
          r1 = arg1.toString().split('.')[1].length
        } catch (e) {
          r1 = 0
        }
        try {
          r2 = arg2.toString().split('.')[1].length
        } catch (e) {
          r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        return (arg1 * m + arg2 * m) / m
      },
      /** 精确减法 */
      accSub(arg1, arg2) {
        var r1, r2, m, n;
        try {
          r1 = arg1.toString().split(".")[1].length;
        } catch (e) {
          r1 = 0;
        }
        try {
          r2 = arg2.toString().split(".")[1].length;
        } catch (e) {
          r2 = 0;
        }
        m = Math.pow(10, Math.max(r1, r2));
        n = r1 >= r2 ? r1 : r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
      },
      /** 精确乘法 */
      accMul(arg1, arg2) {
        let m = 0
        let s1 = arg1.toString()
        let s2 = arg2.toString()
        try {
          m += s1.split('.')[1] ? s1.split('.')[1].length : ''
        } catch (e) { }
        try {
          m += s2.split('.')[1] ? s2.split('.')[1].length : ''
        } catch (e) { }
        return (Number(s1.replace('.', '')) * Number(s2.replace('.', ''))) / Math.pow(10, m)
      },
      /** 精确除法 */
      accDiv(arg1, arg2) {
        let t1 = 0
        let t2 = 0
        let r1
        let r2
        try {
          t1 = arg1.toString().split('.')[1].length
        } catch (e) { }
        try {
          t2 = arg2.toString().split('.')[1].length
        } catch (e) { }
        r1 = Number(arg1.toString().replace('.', ''))
        r2 = Number(arg2.toString().replace('.', ''))
        return (r1 / r2) * Math.pow(10, t2 - t1)
      },
      /**
       * 数字校验，使用方式：
       *
       * rules:{
       *   xxNumber : [
       *     {required: true, message: '必填', trigger: 'blur'}, // 其他验证条件，如必填
       *     PF.numberRule(0, true, 100, false, 0.01) // 本验证：0~99.99，最多2位小数
       *   ]
       * }
       *
       * @param {Number} min 最小值（任意数字）
       * @param {Boolean} coverMin （是否）包含最小值
       * @param {Number} max 最大值（任意数字）
       * @param {Boolean} coverMax （是否）包含最大值
       * @param {Number} accuracy 精度。例如0.25：精度就是1/4，即可填写0.25、0.5、0.75、1、1.25...
       */
      numberRule(min, coverMin, max, coverMax, accuracy) {
        const validator = (rule, value, callback) => {
          try {
            if (value || value === 0 || value === '0') {
              value = value.toString();
              const number = Number(value);
              if (!number) {
                callback(new Error('请填写数字'));
                return;
              }
              if (min) {// 最小值
                if (number < min || (number === min && !coverMin)) {
                  callback(new Error('最小值为' + min + (coverMin ? '（含）' : '（不含）')));
                  return;
                }
              }
              if (max) {// 最大值
                if (number > max || (number === max && !coverMax)) {
                  callback(new Error('最大值为' + max + (coverMax ? '（含）' : '（不含）')));
                  return;
                }
              }
              if (accuracy) {// 精度
                const tms = PF.accDiv(value, accuracy);
                if (tms !== Math.floor(tms)) {
                  if (accuracy == 1) {
                    callback(new Error('请填写整数'));
                    return;
                  }
                  for (let i = 1; i < 11; i++) {
                    if (PF.accMul(accuracy, Math.pow(10, i)) == 1) {
                      callback(new Error('最多' + i + '位小数'));
                      return;
                    }
                  }
                  callback(new Error('请填写' + accuracy + '的整倍数'));
                  return;
                }
              }
            }
          } catch (e) {
            console.error(e)
          }
          callback();
        };
        return { validator, trigger: ['blur', 'change'] }
      },
      queryUnit(queryString, cb, items) {
        var solList = []
        items.forEach(item => {
          solList.push({ 'label': item.cellCode, 'value': item.cellCode })
        })
        var results = queryString
          ? solList.filter(this.createFilterMethod(queryString))
          : solList;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      changePToChangeQ(data) { // 改变单位改变数量
        let detail = PF.JSON(data);
        let oldTransRatio = 0; // 改变前的系数
        let nowTransRatio = 0; // 改变后的系数
        for (let i = 0; i < detail.packDetails.length; i++) {
          if (detail.packDetailId === detail.oldP) { // 改变后的单位id 等于 改变前的单位id (单位未改变)
            nowTransRatio = oldTransRatio = 1;
          } else if (detail.packDetails[i].packDetailId1 === detail.packDetailId) { // (单位改变了) 找到改变后的单位
            nowTransRatio = detail.packDetails[i].transRatio // 拿到系数
          } else if (detail.packDetails[i].packDetailId1 === detail.oldP) { // (单位改变了) 找到改变前的单位
            oldTransRatio = detail.packDetails[i].transRatio // 拿到系数
          }
        }
        let quantity = (detail.oldQ / oldTransRatio) * nowTransRatio;
        return quantity;
      },
      /**
       * data: 加字段时使用
       */
      getSysParamUdfData(memo, data, callback) { // 自定义字段
        IOT.getServerData('/sys/params', 'get', { memo: memo }, (ret) => {
          if (ret.code === 200) {
            let udfs = ret.rows;
            let arr = PF.JSON(data);
            for (let i = 0; i < data.length; i++) {
              for (let j = 0; j < udfs.length; j++) {
                arr[i][udfs[j].paramCode] = '';
              }
            }
            callback(arr, udfs);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getDataDictUdfData(dataDictCode, callback) { // 数据字典自定义
        IOT.getServerData('/data/dict/details/list', 'get', { dataDictCode: dataDictCode }, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getOrganizations(callback) {
        IOT.getServerData('/organizations/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getSuppliers(callback) {
        IOT.getServerData('/supplier/manages/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCustomers(callback) {
        IOT.getServerData('/customers/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCarriers(callback) {
        IOT.getServerData('/carriers/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getNowFormatDate() {
        let date = new Date();
        let seperator1 = "-";
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let strDate = date.getDate();
        if (month >= 1 && month <= 9) {
          month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
          strDate = "0" + strDate;
        }
        let currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
      },
      setLocal(key, val) {
        if (typeof val === 'object') {
          localStorage.setItem(key, JSON.stringify(val))
        } else {
          localStorage.setItem(key, val)
        }
      },
      getLocal(key, type) {
        let value = localStorage.getItem(key);
        if (!PF.isNull(value)) {
          return value
        } else if (type && type === 'json') {
          return JSON.parse(value)
        } else if (type && type === 'number') {
          return Number(value)
        } else {
          return value
        }
      },
      removeLocal(key) {
        localStorage.removeItem(key)
      },
      getModule(path) {
        if (path.length <= 1) {
          return false
        }
        path += '/';
        let isHave = false;
        let moduleName = '';
        for (let i = 0; i < path.length; i++) {
          if (path[i] === '/') {
            if (!isHave) {
              isHave = true
            } else {
              return moduleName
            }
          } else {
            moduleName += path[i]
          }
        }
      },
      isNull(val, type) {
        if (type === '') {
          if (val === null || val === undefined) {
            return false
          } else {
            return true
          }
        } else {
          if (val === '' || val === null || val === undefined) {
            return false
          } else {
            return true
          }
        }
      },
      toRet(res, isAjax) {
        let ret = {};
        let data = {};
        if (isAjax) {
          data = res;
        } else {
          data = res.data;
        }
        if (PF.isNull(data.code, '')) {
          ret.code = data.code;
        }
        if (PF.isNull(data.message, '')) {
          ret.message = data.message;
        }
        if (PF.isNull(data.data, '') && typeof data.data === 'object') {
          if (data.data.length === undefined) {
            if (PF.isNull(data.data.pageNum, '')) {
              ret.pageNumber = data.data.pageNum;
            }
            if (PF.isNull(data.data.pageSize, '')) {
              ret.pageSize = data.data.pageSize;
            }
            if (PF.isNull(data.data.pages, '')) {
              ret.pages = data.data.pages;
            }
            if (PF.isNull(data.data.total, '')) {
              ret.total = data.data.total;
            }
            if (PF.isNull(data.data.list, '')) {
              ret.rows = data.data.list;
            }
          } else if (data.data.length >= 0) {
            if (PF.isNull(data.data, '')) {
              ret.rows = data.data;
            }
          }
        }
        return ret
      },
      getServerData: (url, type, queryQaram, loading, success2) => {
        loading = true;
        queryQaram = JSON.stringify(queryQaram);
        var res = '';
        let token = localStorage.getItem('pda-token');
        console.log(token);
        let baseUrl = that.baseIp + "/wms";
        if (loading) {
          if (type == 'post') {
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              data: queryQaram,
              headers: {
                "Accept": "*/*",
                "access-token": token
              },
              contentType: "application/json",
              url: baseUrl + url,
              async: false,
              beforeSend: () => {
                PF.publicLoadingShow(loading);
              },
              success: (data) => {
                PF.publicLoadingHide();
                success2(data);
              },
              error: () => {
                PF.publicLoadingHide();
                alert('请求失败,联系管理员!');
              }
            });
          } else if (type == 'get') {
            var param = '?';
            queryQaram = queryQaram.replace(/"/g, "");
            queryQaram = queryQaram.replace(/{/g, "");
            queryQaram = queryQaram.replace(/}/g, "");
            queryQaram = queryQaram.replace(/:/g, "=");
            queryQaram = queryQaram.replace(/,/g, "&");
            url = url + '?' + queryQaram;
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              headers: {
                "Accept": "*/*",
                "access-token": token
              },
              contentType: "application/json",
              url: baseUrl + url,
              async: false,
              beforeSend: () => {
                PF.publicLoadingShow(loading);
              },
              success: (data) => {
                PF.publicLoadingHide();
                res = data;
              },
              error: () => {
                PF.publicLoadingHide();
                alert('请求失败,联系管理员!');
              }
            });
          }
        } else {
          if (type == 'post') {
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              data: queryQaram,
              headers: {
                "Accept": "*/*",
                "access-token": token
              },
              contentType: "application/json",
              url: baseUrl + url,
              async: false,
              success: (data) => {
                PF.publicLoadingHide();
                res = data;
              },
              error: () => {
                PF.publicLoadingHide();
                alert('请求失败,联系管理员!');
              }
            });
          } else if (type == 'get') {
            var param = '?';
            queryQaram = queryQaram.replace(/"/g, "");
            queryQaram = queryQaram.replace(/{/g, "");
            queryQaram = queryQaram.replace(/}/g, "");
            queryQaram = queryQaram.replace(/:/g, "=");
            queryQaram = queryQaram.replace(/,/g, "&");
            url = url + '?' + queryQaram;
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              headers: {
                "Accept": "*/*",
                "access-token": token
              },
              contentType: "application/json",
              url: baseUrl + url,
              async: false,
              success: (data) => {
                PF.publicLoadingHide();
                res = data;
              },
              error: () => {
                PF.publicLoadingHide();
                alert('请求失败,联系管理员!');
              }
            });
          }
        }
        return res
      },
      validator: (rule, value, callback, tableName, fieldName, fieldValue, type, idValue) => {
        let form = {
          tableName: tableName, // 表名
          fieldName: fieldName, // 字段名
          fieldValue: fieldValue, // 字段值
          type: type, // 0-新增 1-更新
          idValue: idValue, // type为0时不传，type为1是必传，id值
        };
        IOT.getServerData('/validator', 'post', form, (ret) => {
          if (ret.code === 100001 || ret.code === 100002 || ret.code === 100004) {
            callback(new Error(ret.message || '已重复'));
          } else if (ret.code === 100003) {
            callback();
          } else {
            PF.publicMessage(ret.message || '服务器请求失败，稍后再试！', 'error');
            callback();
          }
        });
        /*that.$http.post('/validator', form).then(res=>{
            if(res.data.code === 100001 || res.data.code === 100002 || res.data.code === 100004){
                callback(new Error(data.message || '已重复'));
            } else if (res.data.code === 100003) {
                callback();
            }else{
                PF.publicMessage(res.data.message || '服务器请求失败，稍后再试！', 'error');
                callback();
            }
        }).catch(err=>{
            console.error(err || '未知的错误!','error');
        });*/
      },
      JSON: (data) => {
        if (typeof data === 'object') {
          return JSON.parse(JSON.stringify(data))
        } else if (typeof data === 'string') {
          return JSON.parse(data)
        }
      },
      /**
       * 判断是否为Object类型Json（复杂对象、不包含数组、以大括号包围的Json），如果是，返回Object。
       *
       * @param {string} str
       * @return {object} json转成的对象
       */
      isJsonObject: (str) => {
        if(typeof str !== 'string' || str.substring(0, 1) !== '{' || str.substring(str.length - 1) !== '}'){
          return null;
        }
        try{
          let obj = JSON.parse(str);
          if(typeof obj === 'object' && obj){
            return obj;
          }
        }catch(e) {}
        return null;
      },
      /**
       * 判断是否为Array类型Json（以中括号包围的Json），如果是，返回ArrayObject。
       *
       * @param {string} str
       * @return {object} json转成的数组
       */
       isJsonArray: (str) => {
        if(typeof str !== 'string' || str.substring(0, 1) !== '[' || str.substring(str.length - 1) !== ']'){
          return null;
        }
        try{
          let obj = JSON.parse(str);
          if(typeof obj === 'object' && obj){
            return obj;
          }
        }catch(e) {}
        return null;
      },
      /**
       * 参考com.deer.wms.project.root.model.QrEntity
       *
       * 1=so
       * 2=sku
       * 3=index
       * 4=sameSkuNumberInOrder
       *
       * 采用极简的扩展二维码格式，提升扫码速度并扩展二维码用途
       *
       * @param {*} str
       * @return {object} simpleQr转成的对象
       */
      simpleQrEncode: (str) => {
        if(!str){
          return {};
        }
        let result = {};
        // 【正序列举】每种已经被定义的特殊字
        let codeArray = ['`','~','!','@','#','$','%','^','&','*','+','|'];
        // 结束符
        let endIndex = str.indexOf(';');
        if (!(endIndex && endIndex > 0)) {
          return null;
        }
        // 【倒序解析】
        for(let i in codeArray){
          let beginIndex = str.indexOf(codeArray[codeArray.length - 1 - i]);
          if (beginIndex != null && beginIndex >= 0) {
            result['index'+(codeArray.length - i)] = str.substring(beginIndex + 1, endIndex);
            endIndex = beginIndex;
          }
        }
        return result;
      },
      toTree: (list = []) => {
        const data = JSON.parse(JSON.stringify(list)) // 浅拷贝不改变源数据
        let result = []
        if (!Array.isArray(data)) {
          return result
        }
        data.forEach(item => {
          delete item.children;
        });
        let map = {};
        data.forEach(item => {
          map[item.id] = item;
        });
        data.forEach(item => {
          let parent = map[item.parentId];
          if (parent) {
            (parent.children || (parent.children = [])).push(item);
          } else {
            result.push(item);
          }
        });
        return result;
      },
      publicLoadingShow: (content) => {
        // document.querySelector('.loadingBox').style.display = 'block';
        // that.isLoading = true;
        let text = '';
        if (content) {
          text = content
        }
        that.loading = that.$loading({
          lock: true,
          text: text,
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
          // background: 'rgba(255,255,255,0.9)'
        });
      },
      publicLoadingHide: () => {
        // document.querySelector('.loadingBox').style.display = 'none';
        // that.isLoading = false;
        if (that.loading) {
          that.loading.close();
        }
      },
      publicMessage(message, type, time, end) {
        let duration = 1500;
        if (time) {
          duration = time
        }
        that.$message({
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: message,
          type: type,
          duration: duration,
          onClose: end
        });
      },
      /**
       * 将字符串中的占位符转换为给定字符
       * @param str 目标字符串 形如：'/users/{0}?password={1}'，其中0\1代表占位符所对应的参数的顺序
       * @returns {*} 返回一个新的字符串
       */
      format: (str) => {
        if (!str || !str.length) {
          return str;
        }
        for (var tempStr = str, i = 0, len = arguments.length - 1; i < len; i++) {
          tempStr = tempStr.replace(new RegExp('\\{' + i + '\\}', 'g'), arguments[i + 1]);
        }
        return tempStr;
      },
      getSysParam(code) {
        var value = null;
        IOT.getServerData('/sys/params/getByCode', 'get', { code: code }, (ret) => {
          if (ret.code === 200) {
            value = ret.data;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true, false);
        return value;
      }
    }
  },
  mounted() {
    this.getRds();
    if (!this.token) {
      PF.publicMessage('请重新登录！', 'error')
      this.$router.push({ name: 'login' })
    }else{
      this.$router.push({ name: 'home' })
    }
    this.wareName = localStorage.getItem("wareName");
    if (this.environment == 'test') {
      this.warning = "当前为test环境。"
    }
    if (this.environment == 'beta') {
      this.error = "谨慎！beta环境！会影响生产数据。"
    }
    const timestamp=new Date().getTime();
    localStorage.setItem('isOpen', timestamp);
    this.timer = setInterval(() => {
      if (localStorage.getItem('isOpen') != timestamp){
        clearInterval(this.timer);
        this.$alert('网站重复打开即将关闭', '', {
          confirmButtonText: '确定',
          callback: () => {
            location.href = "about:blank";
          }
        });
      }
    }, 1000);
  }
}
</script>
<style lang="less">
#app {
  width: 100%;
  padding-bottom: 20px;

  .global-nav-bar {
    text-align: center;
    background-color: #ffe6e6;
    color: #cc0000;

    .warning{
      padding: 3px 0;
      font-size: 14px;
      background-color: #faecda;
      color: #E6A23C;
    }

    .error {
      padding: 3px 0;
      font-size: 14px;
    }
    .blink {
      font-weight: bold;
      font-size: 17px;
      animation: blink 1s linear infinite;
      /* 其它浏览器兼容性前缀 */
      -webkit-animation: blink 1s linear infinite;
      -moz-animation: blink 1s linear infinite;
      -ms-animation: blink 1s linear infinite;
      -o-animation: blink 1s linear infinite;
    }

    @keyframes blink {
      0% {
        opacity: 1;
      }

      100% {
        opacity: 0.1;
      }
    }
  }

  .bottomWare {
    z-index: 99;
    background: #aaaaaa;
    height: 20px;
    line-height: 20px;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
    color: #fff;
  }
}
</style>
