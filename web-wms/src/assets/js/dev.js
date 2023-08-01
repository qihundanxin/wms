import server from '../../server'

var getServerData = function (url, type, queryQaram) {
  queryQaram = JSON.stringify(queryQaram);
  var res = '';
  let token = localStorage.getItem('token');
  console.log(token)
  // let baseIp = localStorage.getItem('baseIp');
  let baseIp = server.baseUrl;
  // let token = null;
  // let baseUrl = localStorage.getItem('baseUrl');
  // let baseUrl = "http://192.168.11.142:8009/wms";
  let baseUrl = baseIp + "/wms";
  // let baseUrl = "http://localhost:8009/wms";
  /* if(token == '' || token == null) {
      window.location.href = '/'
  } */
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
      success: function (data) {
        res = data;
      },
      error: function () {
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
      success: function (data) {
        res = data;
      },
    });
  }
  return res
};


function shock() { // 震动
  navigator.vibrate(100);
}


function toTree(list = []) {
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
}

function validator(rule, value, callback, tableName, fieldName, fieldValue, type, idValue) {
  let form = {
    tableName: tableName, // 表名
    fieldName: fieldName, // 字段名
    fieldValue: fieldValue, // 字段值
    type: type, // 0-新增 1-更新
    idValue: idValue, // type为0时不传，type为1是必传，id值
  };
  IOT.getServerData(URI.BASE.VALIDATOR, form, (ret) => {
    if (ret.code === 100001 || ret.code === 100002) {
      callback(new Error(ret.message || '已重复'));
    } else if (ret.code === 100003) {
      callback();
    } else {
      IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
      callback();
    }
  });
}

let TC = {
  code: '',
  getRandom: function (min, max) { // 随机数
    return Math.floor(Math.random() * (max - min) + min);
  },
  getColor: function (min, max) { // 随机色
    var r = TC.getRandom(min, max);
    var g = TC.getRandom(min, max);
    var b = TC.getRandom(min, max);
    return 'rgb(' + r + ',' + g + ',' + b + ')';
  },
  createPoints: function (context) { // 点
    for (var i = 0; i <= 100; i++) {
      context.beginPath();//开始新路径
      context.fillStyle = TC.getColor(60, 180);
      context.arc(TC.getRandom(0, 120), TC.getRandom(0, 40), 1, 0, 2 * Math.PI);//画点
      context.fill();//实际绘制
      context.closePath();//闭合路径
    }
  },
  createLines: function (context) { // 线条
    for (var i = 0; i < 6; i++) {
      context.beginPath();
      context.moveTo(TC.getRandom(0, 120), TC.getRandom(0, 40));
      context.lineTo(TC.getRandom(0, 120), TC.getRandom(0, 40));
      context.strokeStyle = TC.getColor(100, 200);
      context.stroke();
      context.closePath();
    }
  },
  createText: function (context) {
    var str = 'abcdefghijklmnopqrstuvwxyz1234567890';
    TC.code = '';//变量提升
    for (var i = 1; i <= 6; i++) {//6个
      //取一个随机字符
      var char = str[TC.getRandom(0, str.length - 1)];
      TC.code += char;
      console.log(char);
      //第一个字符 20-23 20*1+(1,3)
      //第二个字符 40-45 20*2+(1,3)
      //第三个字符 60-65 20*3+(1,3)
      //第四个字符 80-83 20*4+(1,3)
      context.beginPath();
      var x = 15 * i + TC.getRandom(1, 3);
      var y = TC.getRandom(20, 32);
      context.font = '18px Arial';
      context.fillStyle = TC.getColor(10, 180);
      //添加一个旋转
      var deg = TC.getRandom(-8, 8);
      context.rotate(deg * Math.PI / 180);
      context.fillText(char, x, y);
      context.closePath();
      //恢复一下
      context.rotate(-deg * Math.PI / 180);
    }
  },
  init: function () { // 用户需求:综合函数
    //调用相应的功能模块
    //获取画布
    var canvas = document.getElementById('canvas');
    console.log(canvas);
    //定义图形类型:2d
    var context = canvas.getContext('2d');

    //添加一个矩形块,大小同画布一样
    context.fillStyle = 'lightyellow';
    context.fillRect(0, 0, 120, 40);

    //具体绘制形状
    //1)绘制点
    TC.createPoints(context);
    //2)绘制线条
    TC.createLines(context);
    //3)绘制文字
    TC.createText(context);
  }
};


export default {
  getServerData,
  shock,
  toTree,
  validator,
  TC,
}
