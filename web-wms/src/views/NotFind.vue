<template>
  <div class="notFind">
    <div class="number">404</div>
    <div class="text">
      找不到此页面
      <a @click="$router.go(-1)">返回上一页</a>
      或
      <a @click="goHome()">返回首页（{{sec}}）</a>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
  import Footer from '../components/Footer'

  export default {
    name: 'NotFind',
    components: {
      Footer
    },
    data() {
      return {
        sec: 10,
        timer: true,
      };
    },
    mounted() {
      setInterval(()=>{
        if(this.timer && this.sec <= 1){
          this.timer = false;
          this.goHome();
        }else if(this.timer && this.sec > 1){
          this.sec = this.sec - 1;
        }
      },1000);
    },
    methods:{
      goHome(){
        // 以刷新的形式回到首页，这种方式回首页，防止当前页面缓存：部署的新页面404，一直404。
        var host = window.location.host;
        console.log(host);
        window.location.href = 'https://' + host;
      },
    }
  }
</script>

<style scoped lang="less">
  .notFind{
    position: fixed;
    top: 0;
    bottom: 30px;
    left: 0;
    right: 0;
    background: #909399;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    text-shadow: 10px 10px 5px #888888;
    .number{
      font-size: 100px;
      margin-right: 20px;
    }
    .text{
      font-size: 30px;
      a{
        text-decoration: underline;
        color: #fff;
      }
    }
  }
</style>
