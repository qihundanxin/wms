<template>
  <div class="login">
    <div id="google_translate_element" style="position:absolute;top:10px;right:70px;z-index:100000;opacity:0.7"> </div>
    <header class="clearFloat">
      <!-- <span class="title leftFloat">账号登录</span> -->
      <span class="el-icon-s-tools tools rightFloat"></span>
    </header>
    <div class="logoBox">
      <div class="imgBox">
        <img src="../assets/images/foodLogoS.png" alt width="80%" />
      </div>
    </div>
    <div class="formBox">
      <div class="  item">
        <el-input
                             
                            size="small"
                            placeholder="请输入账号"
                            v-model="form.account"
                            prefix-icon="iconfont icon-ef-zhanghao"
                            clearable
                        >
                        </el-input>
         
      </div>
      <div class="  item">
        <el-input
                            
                            size="small"
                            placeholder="请输入密码"
                            v-model="form.password"
                            prefix-icon="iconfont icon-mima"
                            show-password
                            clearable
                        ></el-input>
        
      </div>
      <div class="item" style="justify-content:flex-end">
        <el-checkbox v-model="checked">记住账号</el-checkbox>
      </div>
      <div class="item">
        <el-button
          @click="login"
          round
          style="width: 50%;box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);"
        >登录</el-button>
      </div>
    </div>
    <div class="wareBox"></div>
  </div>
</template>
<script>
export default {
  name: "login",
  data() {
    return {
      checked: true,
      form: {
        systemType:1,
        account: '',
        password: ''
      },
    };
  },
  computed: {

    },
  methods: {
    login() {
      if(!this.form.account){
        PF.publicMessage('请输入<i class="iconfont icon-zhanghao"></i>账号!','error');
        return false
      }else if(!this.form.password){
        PF.publicMessage('请输入<i class="iconfont icon-mima"></i>密码!','error');
        return false
      }
      PF.publicLoadingShow('登录中,请稍后...');


      IOT.getServerData('/login/pdaLogin','post',this.form,(ret) => {
        PF.publicLoadingHide();
        if(ret.code === 200){
          PF.publicMessage('登录成功!','success',1000);
          localStorage.setItem("pda-token",ret.data.token);
          localStorage.setItem("userName",ret.data.userName);
          
           localStorage.setItem("menus4",JSON.stringify(ret.data.menus4));
          
           
            
          this.$router.push({name: 'home'})
        }else{
          PF.publicMessage(ret.message || '服务器请求错误!','error');
        }
      },true);
    },
   
    
  },
  mounted() {
    if (localStorage.getItem('pda-token')) {
      this.$router.push({ name: 'home' })
    }
  }
};
</script>
<style lang="less" scoped>
.login {
  // background: lightgoldenrodyellow;
  position: fixed;
  top:0px;
  bottom: 0;
  left: 0;
  right: 0;
  header {
    text-align: center;
    font-size: 18px;
    line-height: 50px;
    position: relative;
    // border-bottom: 2px solid #dcdfe6;
    //   box-shadow: 0 2px 12pxrgba(7, 4, 4, 0.1).1);
    // background: #fff;
    .title {
      width: 150px;
      position: absolute;
      left: 50%;
      margin-left: -75px;
      font-weight: bold;
    }
    .tools {
      font-size: 28px;
      line-height: 50px;
      margin-right: 10px;
    }
  }
  .logoBox {
    margin-top:30px;
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    .imgBox {
      width: 90px;
      height: 90px;
      background: #fff;
      // background: #2b7eb4;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      overflow: hidden;
      border: 1px solid #dcdfe6;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
  }
  .formBox {
    .item {
      width: 80%;
      margin: auto;
      height: 35px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 2px;
    }
    .inputBox {
      width: 80%;
      border-bottom: 1px solid #ccc;
      margin: auto;
      height: 35px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 2px;
      input {
        width: 96%;
        height: 30px;
        border: 0;
        outline: 0;
        font-size: 18px;
        // background: lightgoldenrodyellow;
      }
    }
  }
}
</style>
