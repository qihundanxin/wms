<template>
  <div class="reviewStepBox rightFloat">
    <div class="title">
      <span>快递信息</span>
    </div>
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
            <span class="title">{{node.caseDetailId}}</span>
            <p class="time">{{node.taskTime}}</p>
            <div class="el-card clearFloat">
              <ul class="userBox leftFloat" v-for="(user, index) in node.caseUsers" :key="index" :class="user.reviewResult === 0 ? '' : (user.reviewResult === 0 ? 'primary' : (user.reviewResult === 1 ? 'success' : (user.reviewResult === -1 ? 'danger' : 'warning')))">
                <li>{{user.userName}}</li>
                <li>2018-04-12 20:46:00</li>
                <li>
                  <span v-if="user.memo">{{user.memo}}</span>
                  <span v-else>-</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <p v-show="nodes.length <= 0" style="color: #909399;text-align: center;margin: 20px;">暂无审核流</p>
  </div>
</template>

<script>
  export default{
    name: 'DeliveryStatusBox',
    props: [
      "masterData"
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
      masterBillNo(val){
        if(PF.isNull(val)){
          this.getCaseDetailData()
        }
      }
    },
    methods: {
      widthSum(){
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
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
    },
    mounted() {
      this.widthSum()
    }
  }
</script>
