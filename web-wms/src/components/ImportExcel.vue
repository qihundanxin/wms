<template>
  <div class="importExcel">

    <input type="file" ref="upload" accept=".xls,.xlsx" @change="changeExcel">

    <p>{{outputs}}</p>

  </div>
</template>

<script>

  export default {
    name: 'ImportExcel',
    data(){
      return {
        outputs: []
      }
    },
    methods: {
      changeExcel(val){
        PF.readExcel(val, (data)=>{
          this.outputs = data;
        });
      }
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .importExcel {
  }
</style>
