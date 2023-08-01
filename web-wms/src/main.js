import Vue from 'vue'
import App from './App.vue'
import router from './router'
import {store} from './store/store'
import dateUtils from './utils/DateUtils'
import VueClipboard from 'vue-clipboard2'

// 引入公共文件
import dev from './assets/js/dev.js'

import './assets/iconfont/iconfont.js'
import './assets/iconfont/iconfont.css'
import './plugins/qrcodejs-master/jquery.qrcode.min'
import './assets/css/base.less'


// 安装
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/reset.css'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
// import VueAxios from 'vue-axios'
import VueI18n from 'vue-i18n'

import dataV from './assets/js/dataV/datav.map.vue.js'

import Echarts from 'echarts'

import Qrcode from './assets/js/qrcode.min.js'
//import GoogleTranslate from './assets/js/google/translate_a_element.js'
 

 


Vue.use(dataV);
Vue.use(Echarts);
Vue.use(VueClipboard);

Vue.config.productionTip = false;

Vue.prototype.dev = dev;
Vue.use(ElementUI);
Vue.use(Qrcode);
// Vue.use(VueAxios,axios);
Vue.use(VueI18n);
//Vue.use(GoogleTranslate);

const baseUrl =
  process.env.VUE_APP_CURRENTMODE == 'production' ||
  process.env.VUE_APP_CURRENTMODE == 'test'
    ? window.location.origin
    : '/local';

Vue.prototype.$http = axios.create({
  // baseURL: 'http://122.112.219.208:8089/wms/'
  // baseURL: 'http://192.168.1.102:8003/wms/',
  baseURL:'localhost:8003/wms/',
  headers: {
    "Accept": "*/*",
    "access-token": localStorage.getItem('token')
  },
   contentType: "application/json;charset=UTF-8",
  //contentType: "application/json",
  dataType: "json",
});

Vue.filter('timestamp2str', function (timestamp) {
  if (!timestamp) return '';
  return dateUtils.format(new Date(timestamp));
});


const i18n = new VueI18n({
  locale: 'en-US',
  messages: {
    'zh-CN': require('./common/lang/zh'),
    'en-US': require('./common/lang/en')
  }
});

new Vue({
  i18n,
  store,
  router,
  baseUrl,
  render: h => h(App)
}).$mount('#app');
