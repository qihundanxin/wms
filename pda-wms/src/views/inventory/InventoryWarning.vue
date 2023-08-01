<template>
  <div class="inventoryWarning">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="items">
      <div class="row" v-for="item in items" @click="toInventoryWarningDetail">
        <p>{{item.title}}</p>
        <i class="el-icon-arrow-right"></i>
        <p>{{item.content}}</p>
      </div>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "inventoryWarning",
  data() {
    return {
      headerTitle: "预警提醒",
      items:[
        {
          title: '保质期预警',
          content: '客户:华润万家,货物:205g天鹰纯白薯粉丝,货架'
        },
        {
          title: '库龄预警',
          content: '暂时没有预警信息!'
        },
        {
          title: '库存过低预警',
          content: '暂时没有预警信息!'
        },
      ]
    };
  },
    methods:{
        toInventoryWarningDetail(){
            this.$router.push({name: 'inventoryWarningDetail'})
        }
    },
  components: {
    FeaturesHeader
  }
};
</script>
<style lang="less" scoped>
.inventoryWarning {
  .items {
    .row {
      width: 90%;
      margin: 0 5%;
      height: 60px;
      border-bottom: 1px solid #e4e7ed;
      padding: 0 8px;
      p {
        width: 85%;
        line-height: 30px;
        float: left;
        text-align: left;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
      p:first-child{
        color: #409EFF;
        // font-weight: 550;
        font-size: 18px;
      }
      i {
        width: 15%;
        font-size: 40px;
        line-height: 60px;
        float: right;
        color: #E6A23C;
      }
    }
  }
}
</style>
