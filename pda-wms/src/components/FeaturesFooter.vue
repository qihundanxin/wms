<template>
  <div class="featuresFooter">
    <div v-if="footerBtn == 'inWareDetail'" class="footerBox bottomBackground">
      <b class="bottomHj">合计 : 614.0</b>
    </div>
    <div v-else-if="footerBtn == 'outWareDetail'" class="footerBox bottomBackground">
      <b class="bottomHj">合计 : 63.0</b>
    </div>
    <div v-else class="footerBox">
      <el-button type="primary" @click="commit">{{footerBtn}}</el-button>
    </div>
  </div>
</template>
<script>
export default {
  name: "featuresFooter",
  data(){
    return {
      returnCommitArr: [
        'baseAccept2',
        'mixPlateAccept2',
        'qc2',
        'blindAccept2',
      ]
    }
  },
  props: {
    footerBtn: String,
    batchItems: Array,
    itemMaster: Object
  },
  methods: {
    commit() {
      if (this.$router.currentRoute.name == "resInware2") {
        let itemMaster = this.itemMaster.itemData;
        console.log(itemMaster);
        let batchItems = this.batchItems;
        this.$router.push({
          name: this.itemMaster.fromRoute,
          params: { itemMaster: itemMaster, batchItems: batchItems }
        });
      }else if(this.$router.currentRoute.name == "filterShelf"){
        this.$router.push({name: 'inventoryWater',params: {fromRoute: 'filterShelf'}})
      }else if(this.returnCommitArr.indexOf(this.$router.currentRoute.name) !== -1){
        this.$emit('isCommit', true);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.featuresFooter {
  .footerBox {
    z-index: 99;
    background: #fff;
    position: fixed;
    bottom: 20px;
    left: 0;
    right: 0;
    text-align: right;
    border-top: 1px solid #e4e7ed;
    button {
      border-radius: 0;
    }
  }
  .bottomBackground{
    background: #e6e6e6;
    .bottomHj{
      height: 40px;
      line-height: 40px;
      margin-right: 25px;
    }
  }
}
</style>
