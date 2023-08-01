<template>
  <div class="workbenchMenu">
    <h1>工作台导航</h1>
    <div class="btns">
      <el-button type="primary" round @click="$router.push({name: 'fastAccept'})">验收工作台</el-button>
     
      <el-button type="success" round @click="$router.push({name: 'ydyp'})">一单一品</el-button>
      <el-button type="success" round @click="$router.push({name: 'seeding'})">多品播种</el-button>
      <el-button type="success" round @click="$router.push({name: 'dpdb'})">多品打包</el-button>
    </div>
  </div>
</template>

<script>
  export default{
    name: 'WorkbenchMenu',
  }
</script>

<style scoped lang="less">
  .workbenchMenu{
    .btns{
      margin: 20px;
      >*{
        margin-right: 20px;
        height: 50px;
        width: 200px;
        font-size: 20px;
      }
      >.boldFont{
        font-weight: bold;
      }
    }
  }
</style>
