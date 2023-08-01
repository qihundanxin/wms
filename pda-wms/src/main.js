import Vue from 'vue'
import App from './App.vue'
import router from './router'
import {store} from './store/store'

// 引入公共文件
import dev from './assets/js/dev.js'

import './assets/iconfont/iconfont.js'
import './assets/iconfont/iconfont.css'
import './plugins/qrcodejs-master/jquery.qrcode.min'
//import './assets/css/mui.css'

// 安装
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/reset.css'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
// import VueAxios from 'vue-axios'
import './assets/css/base.less'
Vue.config.productionTip = false;

Vue.prototype.dev = dev;
Vue.use(ElementUI);
// Vue.use(VueAxios,axios);

Vue.prototype.$http = axios.create({
  // baseURL: 'http://122.112.219.208:8089/wms/'
  baseURL: 'http://192.168.1.102:8003/wms/',
  headers: {
    "Accept": "*/*",
    "access-token": localStorage.getItem('pda-token')
  },
  // contentType: "application/json;charset=UTF-8",
  contentType: "application/json",
  dataType: "json",
});

window.EventBus = new Vue();
new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app');
