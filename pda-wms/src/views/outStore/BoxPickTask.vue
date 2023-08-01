<template>
  <div class="GetNewTask">
    <FeaturesHeader  v-if="waveId==null"  :headerTitle="headerTitle" />
    <FeaturesHeader  v-if="waveId!=null"  headerTitle="拣货任务" />
   
    
       
  


  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "GetNewTask",
    data() {
      return {
        boxCode:'',
        waveId:PF.getLocal('waveId', 'number'),
        wareId:PF.getLocal('wareId', 'number'),
        waves:[],
        dialogFormVisible:false,
        pickTaskVisible:false,
        pickTask:{},
        taskSearch:{
          wareId:PF.getLocal('wareId', 'number'),
          orgId:null,
          areaCode:'',
          shelfCode:'',
          itemCode:'',
          state:0,
          waveId:'',

        },
        headerTitle: "获取任务",
        formLabelWidth: '80px',
        pickTasks:[],
        timer1:null,
      
      };
    },
    methods: {
       
    },
    created() {
      var that =this;
      
    },
    destroyed(){
       clearInterval(this.timer1); 
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseAccept {
  }
</style>
