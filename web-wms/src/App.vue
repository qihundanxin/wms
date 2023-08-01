<template>
  <div id="app">
    <div class="global-nav-bar noprint">
      <div class="warning" v-show="warning">{{ warning + rdsUrl }}</div>
      <div class="error" v-show="error">{{ error + rdsUrl }}</div>
      <div class="blink" v-show="blink">{{ blink }}</div>
    </div>
    <div v-show="isLoading" class="loadingBox noprint"></div>
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
  </div>
</template>
<script>
import XLSX from "xlsx";
import server from './server'
let audio = "";
export default {
  name: "App",
  data() {
    return {
      baseIp: server.baseUrl,
      loading: null,
      isLoading: false,
      isRequest: true,
      printIP: "",
      printParam: null,
      sysParams: [],
      tipsMessageInfo: null,
      error: '',
      warning: '',
      blink: '',
      rdsUrl: '',
    };
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
  watch: {},
  methods: {
    getRds() {
      IOT.getServerData(
        "/properties/getDetail",
        "get",
        null,
        (ret) => {
          if (ret.code === 200) {
            this.rdsUrl = ret.data
            if (this.environment == 'test' && this.rdsUrl == 'wms-db') {
              this.blink = '请不要使用！并尽快联系管理员！'
            }
            if ((this.environment == 'beta' || this.environment == 'prod') && this.rdsUrl != 'wms-db') {
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
    getAllSysParam() {
      IOT.getServerData(
        "/sys/params/all",
        "get",
        null,
        (ret) => {
          if (ret.code === 200) {
            this.sysParams = ret.data;
          } else {
          }
        },
        true
      );
    },
    getPrintParam(type) {
      if (this.printParam == null) {
        return null;
      }
      var params = printParam.list;
      params.forEach((param) => {
        if (param.type == type) {
          return param;
        }
      });
    },
    setPrintParam(id, method) {
      if (id == undefined) {
        id = PF.getLocal("lodop_id");
      } else {
        PF.setLocal("lodop_id", id);
      }
      if (id == undefined) {
        return;
      } else {
        IOT.getServerData(
          "/print/ips/detail",
          "get",
          { id: id },
          (ret) => {
            if (ret.code === 200) {
              this.printParam = ret.data;
              this.userSetClodopJS(this.printParam.ip);
              this.$notify({
                title: "成功",
                message: "打印参数设置成功！" + PF.getLocal("lodop_id"),
                type: "success",
              });
              if (method != undefined) {
                method();
              }
            } else {
            }
          },
          true
        );
      }
    },
    //远程打印
    //  01     02
    userSetClodopJS: function (ip) {
      var that = this;
      if (ip == null) {
        ip = "localhost";
      }
      var strSrc =
        "https://" + ip + ".lodop.net:8443/CLodopfuncs.js?priority=1";
      var ScriptSS = document.getElementsByTagName("script");
      for (var i in ScriptSS) {
        if (ScriptSS[i].src && ScriptSS[i].src.indexOf("CLodopfuncs.js") >= 0) {
          if (ScriptSS[i].parentNode) {
            ScriptSS[i].parentNode.removeChild(ScriptSS[i]);
          }
        }
      }
      try {
        //加载双端口(8000和18000）避免其中某个端口被占用：
        var oscript2 = document.createElement("script");
        oscript2.src = "http://" + ip + ":18000/CLodopfuncs.js?priority=2";
        var head =
          document.head ||
          document.getElementsByTagName("head")[0] ||
          document.documentElement;
        head.insertBefore(oscript2, head.firstChild);
      } catch (error) { }
      try {
        var oscript = document.createElement("script");
        if (strSrc.indexOf("src=") >= 0) {
          strSrc = strSrc.match(/=[\',\"][^\',^\"].*(?=[\',\"])/i);
          strSrc = strSrc[0].slice(2);
        }
        oscript.src = strSrc;
        var head =
          document.head ||
          document.getElementsByTagName("head")[0] ||
          document.documentElement;
        head.insertBefore(oscript, head.firstChild);
      } catch (error) { }
      return oscript;
    },
    deleteLodop: function () {
      var ScriptSS = document.getElementsByTagName("script");
      for (var i in ScriptSS) {
        if (ScriptSS[i].src && ScriptSS[i].src.indexOf("CLodopfuncs.js") >= 0) {
          if (ScriptSS[i].parentNode) {
            ScriptSS[i].parentNode.removeChild(ScriptSS[i]);
          }
        }
      }
    },
    getSysParam(code) {
      var that = this;
      for (var i = 0; i < that.sysParams.length; i++) {
        var paramCode = that.sysParams[i].paramCode;
        if (code == paramCode) {
          return that.sysParams[i].value;
        }
      }
    },
    getSysParamByCode(code) {
      IOT.getServerData("/sys/params", "get", { paramCode: code }, (ret) => {
        if (ret.code === 200) {
          if (ret.rows.length > 0) {
            return ret.rows[0].value;
          } else {
            return "";
          }
        } else {
          IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
        }
      });
    },
  },
  created() {
    this.baseIp = server.baseUrl;
    let that = this;
    that.userSetClodopJS(localStorage.getItem("ycIP"));
    //  if(localStorage.getItem("qzParam")=="1"){
    //     that.userSetClodopJS(localStorage.getItem("ycIP"));
    //  }
    localStorage.setItem("baseIpPort", that.baseIp);
    window.IOT = {
      getSysParam(code) {
        if (that.sysParams.length > 0) {
          return that.getSysParam(code);
        } else {
          return that.getSysParamByCode(code);
        }
      },
      setPrintParam(id, method) {
        that.setPrintParam(id, method);
      },
      getPrintParam(type) {
        return that.getPrintParam(type);
      },
      userSetClodopJS(ip) {
        return that.userSetClodopJS(ip);
      },
      deleteLodop() {
        that.deleteLodop();
      },
      upLoadFile(url, param, success) {
        let token = localStorage.getItem("token");
        let baseUrl = that.baseIp + "/wms";
        localStorage.setItem("baseUrl", baseUrl);
        $.ajax({
          //jQuery方法，此处可以换成其它请求方式
          url: baseUrl + url,
          dataType: "json",
          type: "post",
          data: param,
          headers: {
            Accept: "*/*",
            "access-token": token,
          },
          processData: false, //不去处理发送的数据
          contentType: false, //不去设置Content-Type请求头
          error: function (res) {
            IOT.tips(
              err.statusText === "timeout" ? "请求超时!" : err || "未知的错误!",
              "error",
              1000,
              () => {
                IOT.hideOverlay();
              }
            );
            console.error(err.statusText, err || "未知的错误!");
          },
          success: function (res) {
            success(res);
          },
        });
      },
      getServerData(url, type, param, success, noToRet, isAsync, error) {
        let params = {};
        let token = localStorage.getItem("token");
        let baseUrl = that.baseIp + "/wms";
        localStorage.setItem("baseUrl", baseUrl);
        type === "get"
          ? (params = PF.JSON(param))
          : (params = JSON.stringify(param));
        let async = true;
        if (
          (token == undefined || token == null || token == "") &&
          url != "/login" &&
          url != "/sys/params/all" &&
          url != "/sys/params/findByMemo" &&
          url != "/properties/getDetail"
        ) {
          IOT.tips("访问令牌参数失效,请重新登录!", "error", 500, () => {
            // that.$router.push({ name: "login" });
            const loginPage = this.$router.resolve({ name: 'login' })
            window.open(loginPage.href, '_self')
            location.reload()
          });
          return false;
        }
        PF.isNull(isAsync) ? (async = isAsync) : (async = true);
        // console.log(
        //     "token:",
        //     token,
        //     " url:",
        //     url,
        //     " params:",
        //     JSON.stringify(params)
        // );
        $.ajax({
          type: type,
          dataType: "json",
          timeout: 1000 * 60 * 60,
          data: params,
          headers: {
            Accept: "application/json; charset=utf-8",
            "access-token": token,
          },
          contentType: "application/json",
          url: baseUrl + url,
          async: async,
          success: (res) => {
            if (res.code === 10006 || res.code === 10007) {
              localStorage.setItem("token", "");
              IOT.tips("访问令牌参数失效,请重新登录!", "error", 2000, () => {
                localStorage.removeItem('token');
                that.$router.push({ name: "login" });
              });
              return false;
            }
            if (noToRet) {
              success(res);
            } else {
              success(PF.toRet(res, true));
            }
          },
          error: (err) => {
            console.error(type, url, err);
            if(error){
              error(err);
            }
            let showInfo = '发生未知错误，请联系管理员！';
            let showDetail = true;
            if (err.status) {
              if (err.status == 504 || err.status == 504) {
                showInfo = '系统链接超时，请稍后重试！';
                showDetail = false;
              } else {
                showInfo = '发生系统错误，请联系管理员！';
              }
            }
            if (showDetail && err.responseText && typeof err.responseText === 'string'
              && err.responseText.trim().length > 0 && err.responseText.trim().substring(0, 1) != '<') {
              showInfo += '<br>详细信息：<br>' + err.responseText.substring(0, 500);
            }
            IOT.tips(showInfo, "error", 5000, () => {
              IOT.hideOverlay();
            });
          },
        });
      },
      showOverlay(content) {
        let text = "";
        if (content) {
          text = content;
        }
        that.loading = that.$loading({
          lock: true,
          text: text || "",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
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
        let duration = 1500;
        if (time) {
          duration = time;
        }
        that.$message({
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: message,
          type: type,
          duration: duration,
          onClose: end,
        });
      },
    };
    window.PF = {
      /** 根据对象属性去重 */
      uniqueArrayObject(arr = [], key = 'id') {
        if (arr.length === 0) return []
        let list = []
        const map = {}
        arr.forEach((item) => {
          if (!map[item[key]]) {
            map[item[key]] = item
          }
        })
        list = Object.values(map)
        return list
      },
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
       *     {required: true, message: '必填', trigger: 'blur'}, // 其他验证条件
       *     PF.numberRule(0, true, 100, false, 2) // 本验证：0~99.99，最多2位小数
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
              if ((number !== 0 && !number) || (number === 0 && value !== '0')) {
                callback(new Error('请填写数字'));
                return;
              }
              if (value.indexOf('0') === 0 && number >= 1) {
                callback(new Error('请去除首位不必要的0'));
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
      /**
       * 下载文件
       *
       * @param {String} url 文件的网络地址
       * @param {String} fileName 默认下载文件名（含扩展名）
       */
      download(url, fileName) {
        var xhr = new XMLHttpRequest();
          xhr.open('GET', url, true);
          xhr.responseType = 'blob';
          xhr.onload = function () {
              var blob = xhr.response;
              var link = document.createElement('a');
              link.href = window.URL.createObjectURL(blob);
              link.download = fileName;
              link.click();
          };
          xhr.send();
      },
      audioPlay(mp3Name) {
        audio = "/mp3/" + mp3Name + ".mp3";
        audio = new Audio(audio);
        //监听循环播放
        $(audio).bind("ended", function () {
          audio.play();
        });
        audio.play(); //播放 mp3这个音频对象
      },
      audioStop() {
        audio.pause();
      },
      calcTime(start, end) {
        if (PF.isNull(start) && PF.isNull(end)) {
          let a = new Date(start);
          let b = new Date(end);
          let c = Math.floor(((b - a) / 1000 / 60 / 60) * 10000) / 10000;
          return c;
        } else {
          return " - ";
        }
      },
      readExcel(e, callback) {
        //表格导入
        const files = e.target.files;
        // console.log(files);
        if (files.length <= 0) {
          //如果没有文件名
          return false;
        } else if (!/\.(xls|xlsx)$/.test(files[0].name.toLowerCase())) {
          IOT.tips("上传格式不正确，请上传xls或者xlsx格式！", "error");
          return false;
        }
        const fileReader = new FileReader();
        fileReader.onload = (ev) => {
          try {
            const data = ev.target.result;
            const workbook = XLSX.read(data, {
              type: "binary",
            });
            const wsname = workbook.SheetNames[0]; //取第一张表
            const ws = XLSX.utils.sheet_to_json(workbook.Sheets[wsname]); //生成json表格内容
            // console.log(ws);
            callback(ws);
            // this.$refs.upload.value = '';
          } catch (e) {
            return false;
          }
        };
        fileReader.readAsBinaryString(files[0]);
      },
      subtraction(x, y) {
        // 小数点减法
        let n = 10000000000;
        let z = (x * n - y * n) / n;
        return z;
      },
      codeProcess(code) {
        let data = "";
        for (let key in code) {
          if (key !== "code") {
            if (data !== "") {
              data += "&";
            }
            if (PF.isNull(code[key])) {
              data += key + ":" + code[key];
            } else {
              data += key + ":" + null;
            }
          }
        }
        return data;
      },
      codeToObj(data) {
        let arr = data.split("&");
        let obj = {};
        for (let i = 0; i < arr.length; i++) {
          let q = arr[i].split(":");
          if (q[1] === "null") {
            obj[q[0]] = null;
          } else {
            obj[q[0]] = q[1];
          }
        }
        return obj;
      },
      changePToChangeQ(data) {
        // 改变单位改变数量
        let detail = PF.JSON(data);
        let oldTransRatio = 0; // 改变前的系数
        let nowTransRatio = 0; // 改变后的系数
        for (let i = 0; i < detail.packDetails.length; i++) {
          if (detail.packDetailId === detail.oldP) {
            // 改变后的单位id 等于 改变前的单位id (单位未改变)
            nowTransRatio = oldTransRatio = 1;
          } else if (
            detail.packDetails[i].packDetailId1 === detail.packDetailId
          ) {
            // (单位改变了) 找到改变后的单位
            nowTransRatio = detail.packDetails[i].transRatio1; // 拿到系数
          } else if (detail.packDetails[i].packDetailId1 === detail.oldP) {
            // (单位改变了) 找到改变前的单位
            oldTransRatio = detail.packDetails[i].transRatio1; // 拿到系数
          }
        }
        let quantity = (detail.oldQ * oldTransRatio) / nowTransRatio;
        return quantity;
      },
      CPCQ(data, qArr) {
        let detail = PF.JSON(data);
        for (let i = 0; i < qArr.length; i++) {
          let quantity = detail[qArr[i]];
          for (let j = 0; j < detail.packDetails.length; j++) { }
        }
      },
      /**
       * 查询系统参数
       * data: 加字段时使用
       */
      getSysParamUdfData(memo, data, callback) {
        // 自定义字段
        IOT.getServerData(
          "/sys/params/findByMemo",
          "get",
          { memo: memo },
          (ret) => {
            if (ret.code === 200) {
              let udfs = ret.rows;
              let arr = PF.JSON(data);
              // 为data赋key
              for (let i = 0; i < data.length; i++) {
                for (let j = 0; j < udfs.length; j++) {
                  arr[i][udfs[j].paramCode] = "";
                }
              }
              callback(arr, udfs);
            } else {
              IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
            }
          }
        );
      },
      getDataDictUdfData(dataDictCode, callback) {
        // 数据字典自定义
        IOT.getServerData(
          "/data/dict/details/findBydDataDictCode",
          "get",
          { dataDictCode: dataDictCode },
          (ret) => {
            if (ret.code === 200) {
              let rows = ret.rows;
              callback(rows);
            } else {
              IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
            }
          }
        );
      },
      getOrganizations(callback) {
        IOT.getServerData("/organizations/list", "get", {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
      /**
       * 查询供应商信息
       */
      getSuppliers(callback) {
        IOT.getServerData("/supplier/manages/allList", "get", {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
      getCustomers(callback) {
        IOT.getServerData("/customers/allList", "get", {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
      getCarriers(callback) {
        IOT.getServerData("/carriers/allList", "get", {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
      getWares(callback) {
        IOT.getServerData("/ware/infos/list", "get", {}, (ret) => {
          if (ret.code === 200) {
            let rows = ret.rows;
            callback(rows);
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
      getPackDetails(packCode, callback) {
        IOT.getServerData(
          "/pack/details/list",
          "get",
          { packCode: packCode },
          (ret) => {
            if (ret.code === 200) {
              let rows = ret.rows;
              callback(rows);
            } else {
              IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
            }
          }
        );
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
        if (typeof val === "object") {
          localStorage.setItem(key, JSON.stringify(val));
        } else {
          localStorage.setItem(key, val);
        }
      },
      getLocal(key, type) {
        let value = localStorage.getItem(key);
        if (!PF.isNull(value)) {
          return value;
        } else if (type && type === "json") {
          return JSON.parse(value);
        } else if (type && type === "number") {
          return Number(value);
        } else {
          return value;
        }
      },
      removeLocal(key) {
        localStorage.removeItem(key);
      },
      getModule(path) {
        if (path.length <= 1) {
          return false;
        }
        path += "/";
        let isHave = false;
        let moduleName = "";
        for (let i = 0; i < path.length; i++) {
          if (path[i] === "/") {
            if (!isHave) {
              isHave = true;
            } else {
              return moduleName;
            }
          } else {
            moduleName += path[i];
          }
        }
      },
      isNull(val, type) {
        if (type === "") {
          if (val === null || val === undefined) {
            return false;
          } else {
            return true;
          }
        } else {
          if (val === "" || val === null || val === undefined) {
            return false;
          } else {
            return true;
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
        if (PF.isNull(data.code, "")) {
          ret.code = data.code;
        }
        if (PF.isNull(data.message, "")) {
          ret.message = data.message;
        }
        if (PF.isNull(data.data, "") && typeof data.data === "object") {
          if (data.data.length === undefined) {
            if (PF.isNull(data.data.pageNum, "")) {
              ret.pageNumber = data.data.pageNum;
            }
            if (PF.isNull(data.data.pageSize, "")) {
              ret.pageSize = data.data.pageSize;
            }
            if (PF.isNull(data.data.pages, "")) {
              ret.pages = data.data.pages;
            }
            if (PF.isNull(data.data.total, "")) {
              ret.total = data.data.total;
            }
            if (PF.isNull(data.data.list, "")) {
              ret.rows = data.data.list;
            }
          } else if (data.data.length >= 0) {
            if (PF.isNull(data.data, "")) {
              ret.rows = data.data;
            }
          }
        }
        return ret;
      },
      getServerData: (url, type, queryQaram, loading, success2) => {
        queryQaram = JSON.stringify(queryQaram);
        var res = "";
        let token = localStorage.getItem("token");
        // console.log(token);
        let baseUrl = that.baseIp + "/wms";
        if (loading) {
          if (type == "post") {
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              data: queryQaram,
              headers: {
                Accept: "*/*",
                "access-token": token,
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
                alert("请求失败,联系管理员!");
              },
            });
          } else if (type == "get") {
            var param = "?";
            queryQaram = queryQaram.replace(/"/g, "");
            queryQaram = queryQaram.replace(/{/g, "");
            queryQaram = queryQaram.replace(/}/g, "");
            queryQaram = queryQaram.replace(/:/g, "=");
            queryQaram = queryQaram.replace(/,/g, "&");
            url = url + "?" + queryQaram;
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              headers: {
                Accept: "*/*",
                "access-token": token,
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
                alert("请求失败,联系管理员!");
              },
            });
          }
        } else {
          if (type == "post") {
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              data: queryQaram,
              headers: {
                Accept: "*/*",
                "access-token": token,
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
                alert("请求失败,联系管理员!");
              },
            });
          } else if (type == "get") {
            var param = "?";
            queryQaram = queryQaram.replace(/"/g, "");
            queryQaram = queryQaram.replace(/{/g, "");
            queryQaram = queryQaram.replace(/}/g, "");
            queryQaram = queryQaram.replace(/:/g, "=");
            queryQaram = queryQaram.replace(/,/g, "&");
            url = url + "?" + queryQaram;
            $.ajax({
              type: type,
              dataType: "json",
              timeout: 30000,
              headers: {
                Accept: "*/*",
                "access-token": token,
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
                alert("请求失败,联系管理员!");
              },
            });
          }
        }
        return res;
      },
      validator: (
        rule,
        value,
        callback,
        tableName,
        fieldName,
        fieldValue,
        type,
        idValue
      ) => {
        let form = {
          tableName: tableName, // 表名
          fieldName: fieldName, // 字段名
          fieldValue: fieldValue, // 字段值
          type: type, // 0-新增 1-更新
          idValue: idValue, // type为0时不传，type为1是必传，id值
        };
        IOT.getServerData("/validator", "post", form, (ret) => {
          if (
            ret.code === 100001 ||
            ret.code === 100002 ||
            ret.code === 100004
          ) {
            callback(new Error(ret.message || "已重复"));
          } else if (ret.code === 100003) {
            callback();
          } else {
            PF.publicMessage(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
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
      validatorNew: (rule, value, callback, fieldName, wareId) => {
        let form = {
          [fieldName]: value, // 字段名
          wareId,
        };
        IOT.getServerData("/area/infos/validator", "post", form, (ret) => {
          if (
            ret.code === 100001 ||
            ret.code === 100002 ||
            ret.code === 100004
          ) {
            callback(new Error(ret.message || "已重复"));
          } else if (ret.code === 100003) {
            callback();
          } else {
            PF.publicMessage(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
            );
            callback();
          }
        });
      },
      JSON: (data) => {
        if (typeof data === "object") {
          return JSON.parse(JSON.stringify(data));
        } else if (typeof data === "string") {
          return JSON.parse(data);
        }
      },
      toTree: (list = [], disabled = false) => {
        const data = JSON.parse(JSON.stringify(list)); 
        let result = [];
        if (!Array.isArray(data)) {
          return result;
        }
        data.forEach((item) => {
          delete item.children;
        });
        let map = {};
        data.forEach((item) => {
          map[item.id] = item;
          if (disabled) {
            item.disabled = disabled;
          }
        });
        data.forEach((item) => {
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
        let text = "";
        if (content) {
          text = content;
        }
        that.loading = that.$loading({
          lock: true,
          text: text,
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
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
          duration = time;
        }
        that.$message({
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: message,
          type: type,
          duration: duration,
          onClose: end,
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
        for (
          var tempStr = str, i = 0, len = arguments.length - 1;
          i < len;
          i++
        ) {
          tempStr = tempStr.replace(
            new RegExp("\\{" + i + "\\}", "g"),
            arguments[i + 1]
          );
        }
        return tempStr;
      },
      getSysParam(param) {
        IOT.getServerData("/sys/params", "get", param, (ret) => {
          if (ret.code === 200) {
            return ret.rows[0].value;
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        });
      },
    };
    // that.setPrintParam();
    that.getAllSysParam();
  },
  mounted() {
    this.getRds();
    if (this.environment == 'test') {
      this.warning = "当前为test环境。"
    }
    if (this.environment == 'beta') {
      this.error = "当前为beta环境，操作将影响生产数据，请谨慎操作。"
    }
  }
  
};
</script>
<style lang="less">
#app {
  overflow: hidden;

  /* 导航栏 */
  .global-nav-bar {
    text-align: center;
    background-color: #ffe6e6;
    color: #cc0000;

    .warning {
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

}
</style>
