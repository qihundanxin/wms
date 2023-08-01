<template>
  <div class="header">

    <el-drawer
        title="我的消息"
        :visible.sync="drawer"
        direction="rtl"
        :with-header="false">
      <h1>我的消息</h1>

    </el-drawer>

    <div class="wareSelectBox">
      <i class="iconfont icon-dizhi"></i>
      <span class="text">{{wareName}}</span>
      <el-button type="text" @click.stop="clickShowBigFloatBoxWare">[{{$t('m.head.change')}}]</el-button>
      <el-button type="text" icon="iconfont icon-xia" @click.stop="clickShowBigFloatBoxWare"
                 style="color: #A9B0C6;margin: 0;"></el-button>
      <div class="bigFloatBox" v-show="showBigFloatBoxWare && showBigFloatBoxWareHome" style="left: 50px;">
        <div class="floatBox halfScrollbar" style="max-height: 350px;overflow: auto;">
          <ul>
            <li v-for="(ware, index) in wares" :key="index" @click="changeWare(ware)">{{ware.wareName}}</li>
          </ul>
        </div>
        <span class="jiao"></span>
      </div>
<!--      <el-button icon="iconfont icon-shanchu" type="text" size="mini" @click.stop="clearWare" style="color: red"></el-button>-->
    </div>

    <div class="headerMenuBox">
      <div class="menuBox">
        <span v-if="menus1.length" class="menuBtn" @click="clickHeaderMenuBtn(0,true)">{{$t('m.head.ware')}}</span>
        <span v-if="menus2.length" class="menuBtn" @click="clickHeaderMenuBtn(1,true)">{{$t('m.head.base')}}</span>
        <span v-if="menus3.length" class="menuBtn" @click="clickHeaderMenuBtn(2,true)">{{$t('m.head.report')}}</span>
      </div>
    </div>

    <div class="userInfoBox">
<!--      <span v-show="isFullScreen" @click="offFullScreen" class="iconfont icon-tuichuquanping active" style="margin-left: 5px;"> </span>-->
      <i @click="fullScreen" class="iconfont icon-quanping active" title="全屏显示" style="margin-right: 50px;"></i>
      <!-- <div @click="showMessageModal" v-if="isShow">
        <el-badge :value="200" :max="99" type="primary" class="iconfont icon-xinfeng active" title="我的消息"
                  style="font-size: 20px;"></el-badge>
      </div> -->
      <div v-if="isShow">
        <i class="iconfont icon-gengduo active" title="更多设置" style="margin-right: 10px;" @click.stop="clickShowBigFloatBoxSys"></i>
        <div class="bigFloatBox" v-show="showBigFloatBoxSys && showBigFloatBoxSysHome" style="right: 70px;">
          <!-- <div class="floatBox">
            <ul>
              <li @click="playMp3">play</li>
              <li @click="stopMp3">stop</li>
              <li>
                <router-link :to="{name: 'workbenchMenu'}">{{$t('m.head.workbench')}}</router-link>
              </li>
              <li>
                <span @click="$parent.setPrinter">打印机设置</span>
              
              </li>
              <li class="split"></li>
              <li>
                <router-link :to="{name: 'sysSetting'}">系统设置</router-link>
              </li>
            </ul>
          </div> -->
          <!-- <span class="jiao"></span> -->
        </div>
      </div>
      <div class="userInfo">
        <img class="leftFloat" src="../assets/images/headerImg.jpg" alt="" height="30" style="border-radius: 50%;">
        <span class="textUser leftFloat active" @click.stop="clickShowBigFloatBoxUser">
          <span class="textUserBtn">{{$t('m.head.welcome')}}，{{userName}}</span>
          &nbsp;
          <i class="iconfont icon-xia"></i>
        </span>
        <div class="bigFloatBox" v-show="showBigFloatBoxUser && showBigFloatBoxUserHome" style="right: 0;">
          <div class="floatBox">
            <ul v-if="isShow">
              <li @click="passwordVisible=true">{{$t('m.head.personal')}}</li>
             
              <li @click="appVisible=!appVisible">{{$t('m.head.app_download')}}</li>
              <!--<li>{{$t('m.head.help_document')}}</li>  -->

                <!-- <li><a :href="'http://'+ $parent.$parent.baseIp +'/wms/swagger-ui.html'" target="_blank">swagger-ui</a></li> -->
              <!-- <li><a href="https://element.eleme.cn/#/zh-CN/component/installation" target="_blank">element-ui</a></li>  -->
              <li class="split"></li>
              <li @click="fullScreen">
               <!-- <span v-show="isFullScreen">取消全屏</span>-->
                <span v-show="!isFullScreen">全屏显示</span>
              </li> 
                <li @click="changeLangEvent">{{$t('m.head.language')}}</li>  
                <li class="split"></li>
              <li @click="isLogout">{{$t('m.head.logout')}}</li>
            </ul>
            <!-- <ul>
             
               
              
              <li class="split"></li>
              <li @click="isLogout">{{$t('m.head.logout')}}</li>
            </ul> -->
          </div>
          <span class="jiao"></span>
        </div>
      </div>

    </div>

    <el-dialog title="修改密码" :visible.sync="passwordVisible">
      <el-form style="text-align: center;  margin-left:50px" :model="modifyPassword" :rules="rules" ref="modifyPassword" >
        <el-form-item  prop="oldPassword" label="旧密码" :label-width="formLabelWidth">
          <el-input v-model="modifyPassword.oldPassword"></el-input>
        </el-form-item>
        <el-form-item  prop="password" label="新密码" :label-width="formLabelWidth">
          <el-input class="password" v-model="modifyPassword.password"  ></el-input>
        </el-form-item>
        <el-form-item  prop="confirmPassword"   label="确认密码" :label-width="formLabelWidth">
          <el-input class="password" v-model="modifyPassword.confirmPassword" ></el-input>
        </el-form-item>
       
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="passwordVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="APP" :visible.sync="appVisible">
      <div>
        <iframe v-if="PDA_URL!=null && PDA_URL.length>2" style="height:500px" :src="PDA_URL"></iframe>
        {{PDA_URL}}
        <div id="test"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="appVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm()">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
 
  export default {
    name: 'Header',
    data() {
       let validateCommitPassword = (rule, value, callback) => {
        if(value==this.modifyPassword.password){
           callback( );
        }else{
          callback(new Error(  "两次输入的密码不一致"));
        }
      };
      return {
        PDA_URL:IOT.getSysParam("PDA_URL"),
        appVisible:false,
        formLabelWidth:"80px",
        passwordVisible:false,
        modifyPassword:{
          oldPassword:'',
          password:'',
          confirmPassword:''
        },
        moduleName: '',
        wareId: PF.getLocal('wareId', 'number'),
        wareName: PF.getLocal("wareName"),
        userName: PF.getLocal("userName"),
        showBigFloatBoxUser: false,
        showBigFloatBoxWare: false,
        showBigFloatBoxSys: false,
        wares: [],
        isFullScreen: false,
        drawer: false,
        // 未开发完不展示
        isShow: true,
        menus1: PF.toTree(JSON.parse(localStorage.getItem("menus1"))),
        menus2: PF.toTree(JSON.parse(localStorage.getItem("menus2"))),
        menus3: PF.toTree(JSON.parse(localStorage.getItem("menus3"))),

        rules: {
          oldPassword: [{   required: true,   message: '请输入旧密码',   trigger: 'blur'  },  
             ],
          password: [{   required: true,   message: '请输入新密码',   trigger: 'blur'  },  ],
          confirmPassword: [{   required: true,   message: '请确认新密码',   trigger: 'blur'  }, {
              validator: validateCommitPassword,
              trigger: 'blur'
            } ]

        },
      }
    },
     
    props: [
      "showBigFloatBoxUserHome",
      "showBigFloatBoxWareHome",
      "showBigFloatBoxSysHome",
    ],
    computed: {
      pathChange() {
        return this.$route.path
      }
    },
    watch: {
      showBigFloatBoxUserHome(val) {
        this.showBigFloatBoxUser = val
      },
      showBigFloatBoxWareHome(val) {
        this.showBigFloatBoxWare = val
      },
      showBigFloatBoxSysHome(val) {
        this.showBigFloatBoxSys = val
      },
      appVisible(val){
        this.PDA_URL=IOT.getSysParam("PDA_URL");
        // var qrcode = new QRCode("test", {
        //     text: "http://www.runoob.com",
        //     width: 128,
        //     height: 128,
        //     colorDark : "#000000",
        //     colorLight : "#ffffff",
        //     correctLevel : QRCode.CorrectLevel.H
        // });
      },
      pathChange(val) {
        this.isModule()
        // let moduleName = PF.getModule(val);
        // if (moduleName === 'work'
        //   || moduleName === 'homepage'
        //   || moduleName === 'base'
        //   || moduleName === 'report') {
        //   return false
        // } else {
        //   this.isModule()
        // }

      },
    },
    methods: {
      commitForm(){
        var that = this;
          that.$refs['modifyPassword'].validate((valid) => {
            if (valid) {
               IOT.showOverlay('保存中，请稍等...');
               IOT.getServerData('/users/modifyPassword', 'post', that.modifyPassword, (ret) => {
                  IOT.hideOverlay();
                  if (ret.code === 200) {
                    IOT.tips('保存成功！', 'success', 1000, () => {
                       that.passwordVisible = false;
                    });
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });
            }
          })
      },
      playMp3(){
        PF.audioPlay('WCS未连接');
      },
      stopMp3(){
        PF.audioStop();
      },
      clickShowBigFloatBoxUser() {
        this.showBigFloatBoxUser = !this.showBigFloatBoxUser;
        this.showBigFloatBoxWare = false;
        this.showBigFloatBoxSys = false;
        this.$emit('changeShowBigFloatBoxUser', this.showBigFloatBoxUser);
      },
      clickShowBigFloatBoxWare() {
        this.showBigFloatBoxWare = !this.showBigFloatBoxWare;
        this.showBigFloatBoxUser = false;
        this.showBigFloatBoxSys = false;
        this.$emit('changeShowBigFloatBoxWare', this.showBigFloatBoxWare);
      },
      clickShowBigFloatBoxSys() {
        this.showBigFloatBoxSys = !this.showBigFloatBoxSys;
        this.showBigFloatBoxWare = false;
        this.showBigFloatBoxUser = false;
        this.$emit('changeShowBigFloatBoxSys', this.showBigFloatBoxSys);
      },
      clickHeaderMenuBtn(index, isToRouter) {
        let menuBtns = document.querySelectorAll('.menuBtn');
        for (let i = 0; i < menuBtns.length; i++) {
          menuBtns[i].classList.remove('current');
        }
        menuBtns[index].classList.add('current');
        this.$emit('changeLeftMenusIndex', index);
        if (isToRouter) {
          if (index === 0) {
            this.$router.push({name: 'homepage'})
          } else if (index === 1) {
            this.$router.push({name: 'ware'})
          } else if (index === 2) {
            this.$router.push({name: 'asnDayReport'})
          }
        } else {
        }

      },
      isLogout() {
        this.$confirm('是否确认退出登录？')
          .then(_ => {
            this.logout()
          })
          .catch(_ => {
          });
      },
      logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("menus1");
        localStorage.removeItem("menus2");
        localStorage.removeItem("menus3");
        const loginPage = this.$router.resolve({ name: 'login' })
        window.open(loginPage.href, '_self')
        location.reload()
      },
      isModule() {
        let path = this.$route.path;
        let moduleName = PF.getModule(path);
        if (moduleName === 'work' || moduleName === 'homepage') {
          this.clickHeaderMenuBtn(0);
        } else if (moduleName === 'base') {
          this.clickHeaderMenuBtn(1);
        } else if (moduleName === 'report') {
          this.clickHeaderMenuBtn(2);
        } else {
          this.clickHeaderMenuBtn(0);
        }
      },
      getWareData() {
        IOT.getServerData('/ware/infos/findSome', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
            this.wareIsNull();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changeWare(ware) {
        IOT.tips('仓库设置成功！', 'success', 1000, () => {
          PF.setLocal("wareId", ware.wareId);
          PF.setLocal("wareName", ware.wareName);
          this.$router.go(0);
        });
      },
      /*clearWare(){
        PF.removeLocal('wareId');
        PF.removeLocal('wareName');
        this.wareId = null;
        this.wareName = null;
      },*/
      wareIsNull() {
        if (!PF.isNull(this.wareId)) {
          if (this.wares.length > 0) {
            PF.setLocal("wareId", this.wares[0].wareId);
            PF.setLocal("wareName", this.wares[0].wareName);
            this.$router.go(0);
          } else {
            IOT.tips('未获取到仓库信息！', 'error');
          }
        } else {
          // this.getWareName();
        }
      },
      getWareName() {
        for (let i = 0; i < this.wares.length; i++) {
          if (this.wareId === this.wares[i].wareId) {
            this.wareName = this.wares[i].wareName;
          }
        }
      },
      fullScreen(){
        this.isFullScreen = true;
        this.$emit('fullScreen', false);
      },
      offFullScreen(){

      },
      changeLangEvent() {

        /*this.$confirm(this.$t('m.msg.switch_languages_title'))
          .then(_ => {
            if ( this.$i18n.locale === 'zh-CN' ) {
              this.$i18n.locale = 'en-US';
            }else if ( this.$i18n.locale === 'en-US' ) {
              this.$i18n.locale = 'zh-CN';
            }
          })
          .catch(_ => {});*/

        this.$confirm(this.$t('m.msg.switch_languages_title'), this.$t('m.msg.prompt'), {
          confirmButtonText: this.$t('m.msg.yes'),
          cancelButtonText: this.$t('m.msg.no'),
          type: 'warning'
        }).then(() => {
          if ( this.$i18n.locale === 'zh-CN' ) {
            this.$i18n.locale = 'en-US';
          }else if ( this.$i18n.locale === 'en-US' ) {
            this.$i18n.locale = 'zh-CN';
          }
        }).catch(() => {
          /*this.$message({
            type: 'info',
          });*/
        });

      },
      showMessageModal(){
        this.drawer = true;
      },

    },
    created() {
      this.getWareData();
    },
    mounted() {
      this.isModule();
    },
  }
</script>

<style scoped lang="less">
  .header {
    .password{
       text-security:disc;
      -webkit-text-security:disc;
      -moz-text-security:disc;
    }
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

    @media screen and (max-width: 870px) {
      .wareSelectBox {
        width: 160px;

        > .text {
          max-width: 100px;
        }
      }

      .headerMenuBox {
        width: 200px;
      }

      .userInfoBox {
        width: 320px;

        .textUserBtn {
          max-width: 130px;
        }
      }
    }
    @media screen and (min-width: 871px) and (max-width: 1300px) {
      .wareSelectBox {
        width: 160px;

        > .text {
          max-width: 100px;
        }
      }

      .headerMenuBox {
        width: 200px;
      }

      .userInfoBox {
        width: 320px;

        .textUserBtn {
          max-width: 130px;
        }
      }
    }
    @media screen and (min-width: 1301px) and (max-width: 1600px) {
      .wareSelectBox {
        width: 300px;

        > .text {
          max-width: 200px;
        }
      }

      .headerMenuBox {
        width: 300px;
      }

      .userInfoBox {
        width: 400px;

        .textUserBtn {
          max-width: 230px;
        }
      }
    }
    @media screen and (min-width: 1601px) {
      .wareSelectBox {
        width: 300px;

        > .text {
          max-width: 200px;
        }
      }

      .headerMenuBox {
        width: 300px;
      }

      .userInfoBox {
        width: 400px;

        .textUserBtn {
          max-width: 230px;
        }
      }
    }

    .wareSelectBox {
      padding-left: 40px;
      display: flex;
      align-items: center;
      position: relative;

      > .text {
        display: inline-block;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .headerMenuBox {
      .menuBox {
        width: 100%;
        margin: auto;
        display: flex;
        justify-content: space-between;

        .menuBtn {
          color: #A9B0C6;
          border-bottom: 2px solid transparent;
          cursor: pointer;
          font-size: 16px;
        }

        .menuBtn:hover {
          border-bottom: 2px solid #228ffd;
        }

        .menuBtn.current {
          color: #228ffd;
          border-bottom: 2px solid #228ffd;
        }
      }
    }

    .userInfoBox {
      padding-right: 20px;
      display: flex;
      align-items: center;
      flex-direction: row-reverse;

      > * {
        color: #A9B0C6;
      }

      .active {
        cursor: pointer;
      }

      .active:hover {
        color: #409EFF;
      }

      .userInfo {
        margin-right: 20px;
        display: flex;
        align-items: center;
        position: relative;

        .textUser {
          display: flex;
          align-items: center;

          .textUserBtn {
            display: inline-block;
            /*max-width: 130px;*/
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        > * {
          margin-left: 5px;
        }

      }
    }

    .bigFloatBox {
      position: absolute;
      top: 72px;
      z-index: 2019;

      .floatBox {
        /*width: 124px;*/
        padding: 0 34px;
        border: 1px solid #ECEEF5;
        border-radius: 4px;
        background: #ffffff;
        position: relative;

        > ul {
          > li {
            height: 35px;
            line-height: 35px;
            text-align: center;
            cursor: pointer;

            a {
              color: #A9B0C6;
            }
          }

          > li:hover {
            color: #409EFF;

            a {
              color: #409EFF;
            }
          }

          > li.split {
            height: 0;
            border-top: 1px solid #eef0f7;
            margin: 5px 0;
          }
        }
      }

      .jiao {
        position: absolute;
        top: -20px;
        right: 35px;
        display: inline-block;
        border: 10px solid transparent;
        border-bottom: 10px solid #ECEEF5;
      }

      .jiao::after {
        position: absolute;
        top: -8px;
        margin-left: -10px;
        content: ' ';
        border: 10px solid transparent;
        border-bottom: 10px solid #ffffff;
      }
    }

  }
</style>
