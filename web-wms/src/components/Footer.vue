<template>
  <div class="footer">
    <span v-for="item in systemBottom" :key="item.value" v-if="item.paramCode === 'systemName'">{{item.value}}</span>
    <span v-for="item in systemBottom" :key="item.value" v-if="item.paramCode === 'companyName'">&copy;{{item.value}}</span>
    <a v-for="item in systemBottom" :key="item.value" v-if="item.paramCode === 'companyTal'" :href="'tel:' + item.value"><i class="iconfont icon-shouji"></i>{{item.value}}</a>
  </div>
</template>

<script>
    export default {
        name: 'Footer',
        data() {
            return {
              systemBottom: [],
            }
        },
        methods: {
        },
        created() {
          PF.getSysParamUdfData('systemBottom', [], (data, udfs) => {
            this.systemBottom = udfs;
          });
        }
    }
</script>

<style scoped lang="less">
  .footer {
    background: #333333;
    width: 100%;
    height: 30px;
    line-height: 30px;
    color: #dddddd;
    text-align: center;
    font-size: 14px;
    position: fixed;
    bottom: 0;
    z-index: 9999;
    >*{
      margin-left: 10px;
    }
    a{
      color: #dddddd;
    }
  }
</style>
