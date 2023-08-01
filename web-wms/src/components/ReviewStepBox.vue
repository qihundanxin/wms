<template>
<!--  rightFloat-->
  <div class="reviewStepBox">
    <!--<div class="title">
      <span>审核进程</span>
    </div>-->
    <div v-show="nodes.length > 0" class="bigReviewStep">
      <div class="reviewStep clearFloat" :style="{width: reviewStepWidth}">
        <div class="node leftFloat" v-for="(node, index) in nodes" :key="index">
          <div class="header clearFloat">
            <div class="yuan leftFloat" :class="node.taskResult === 0 ? '' : (node.taskResult === 0 ? 'primary' : (node.taskResult === 1 ? 'success' : (node.taskResult === -1 ? 'danger' : 'warning')))">
              <i class="iconfont" :class="node.taskResult === 0 ? '' : (node.taskResult === 0 ? 'icon-qita3' : (node.taskResult === 1 ? 'icon-dui' : (node.taskResult === -1 ? 'icon-icon1' : 'icon-gantanhao')))"></i>
              <i class="iconfont"></i>
            </div>
            <p class="xian leftFloat" v-if="index < nodes.length-1"></p>
          </div>
          <div class="footer">
            <p class="title" style="line-height: 30px;margin-top: 8px;">
              <span v-if="node.detailType === 1 && node.caseUsers.length>1" class="yuhuo">与</span>
              <span v-else-if="node.detailType === 0 && node.caseUsers.length>1" class="yuhuo">或</span>
              <span>{{node.reviewNodeName}}</span>
            </p>
             <p class="time" v-if="node.taskTime === '' || node.taskTime === null || node.taskTime === undefined"> </p>
            <p class="time" v-else>{{node.taskTime}}</p> 
            <div class="el-card clearFloat">
              <ul class="userBox leftFloat" v-for="(user, index) in node.caseUsers" :key="index" :class="user.reviewResult === 0 ? '' : (user.reviewResult === 0 ? 'primary' : (user.reviewResult === 1 ? 'success' : (user.reviewResult === -1 ? 'danger' : 'warning')))">
                <li>{{user.userName}}</li>
                <li v-if="user.reviewTime != null && user.reviewTime != undefined && user.reviewTime != ''">{{user.reviewTime}}</li>
                
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <p v-show="nodes.length <= 0" style="color: #909399;text-align: center;margin: 20px;">
      该单据未绑定审核流，已自动<span style="color: #67C23A;">审核通过</span>

      <br>
      如需绑定请移至
      <el-button type="text" @click="$router.push({name: 'organization'})">货主管理</el-button>
      页面进行设置
    </p>
  </div>
</template>

<script>
  export default{
    name: 'ReviewStepBox',
    props: [
      "masterData",
      "activeName"
    ],
    data(){
      return {
        reviewStepWidth: '',
        nodes: [],
      }
    },
    computed: {
      masterBillNo(){
        return this.masterData
      }
    },
    watch: {
      /*masterBillNo(val){
        if(PF.isNull(val)){
          this.getCaseDetailData()
        }
      },*/
      activeName(val){
        if(val === 'reviewStep'){
          if(PF.isNull(val)){
            this.getCaseDetailData()
          }else{
            IOT.tips('未获取到单号！', 'error');
          }
        }
      }
    },
    methods: {
      widthSum(){
        if(this.activeName !== 'second'){
          return false
        }
        setTimeout(() => {
          let node = document.querySelectorAll('.reviewStep > .node');
          if(node.length === 0 || node.length === null || node.length === undefined){
            this.widthSum();
            return false
          }else if(node.length > 0){
            let widthSum = 10;
            for(let i=0;i<node.length;i++){
              widthSum += node[i].clientWidth
            }
            if(this.reviewStepWidth !== widthSum + 'px'){
              this.widthSum();
            }
            this.reviewStepWidth = widthSum + 'px';
          }else {
            return false
          }
        },100);
      },
      getCaseDetailData(){
        IOT.getServerData('/case/details/nowDetails', 'get', {billNo: this.masterData}, (ret) => {
          if (ret.code === 200) {
            this.nodes = ret.rows;
            this.widthSum();
          } else if(ret.code !== 125) {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
    },
    mounted() {
      this.widthSum();
    }
  }
</script>

<style scoped lang="less">
  .yuhuo{
    border: 1px solid #A9B0C6;
    border-radius: 50%;
    color: #A9B0C6;
    display: inline-block;
    width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    font-size: 20px;
  }
</style>
