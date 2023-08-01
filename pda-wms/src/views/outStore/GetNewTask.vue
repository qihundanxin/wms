<template>
  <div class="GetNewTask">
    <FeaturesHeader  v-if="waveId==null"  :headerTitle="headerTitle" />
    <FeaturesHeader  v-if="waveId!=null"  headerTitle="拣货任务" />
      <!-- <el-button type="text" @click="dialogFormVisible = true">设置任务范围</el-button> -->

    <!-- <el-dialog title="设置任务范围" :visible.sync="dialogFormVisible" style="width: 100%;">
        <el-form class="noMargin" :model="taskSearch" ref="form">
     
        <el-form-item label="波次" :label-width="formLabelWidth">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="wave in waves"
                :key="wave.waveId"
                :label="wave.waveCode"
                :value="wave.waveId">
              </el-option>
            </el-select>
         
        </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="dialogFormVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog> -->
    <!-- <el-dialog title="拣货" :visible.sync="pickTaskVisible"  >
        <el-form class="noMargin" :model="taskSearch" ref="form">
        <el-form-item label="货区" :label-width="formLabelWidth">
          <el-input v-model="taskSearch.areaCode" size="mini" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货位组" :label-width="formLabelWidth">
          <el-input v-model="taskSearch.areaCode" size="mini" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="料号" :label-width="formLabelWidth">
          <el-input v-model="taskSearch.itemCode" size="mini" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="波次" :label-width="formLabelWidth">
          <el-input v-model="taskSearch.v" size="mini" clearable style="width: 100%;"></el-input>
        </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="pickTaskVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="pickTaskVisible = false">确 定</el-button>
      </div>
    </el-dialog> -->
    <div style=" ;">
       <div class="   ">
          <span style="color:#E6A23C;" v-if="waves.length == 0">暂无待拣波次</span>
          <ul v-if="waveId==null"  >
            <li v-for="(item,index) in waves"  @click="getPickTasks(item.waveId)"  >
              <el-card >  
                  <div>  
                        <span>波次号：</span>
                        <span>{{item.waveId}}</span>
                      </div>
                      <div> 
                        <span>订单结构：</span>
                              <span v-if="item.soStructure ==1"> 一单一品</span>
                              <span v-if="item.soStructure ==2"> 一单多品</span>
                              <span v-if="item.soStructure ==3"> 一单一品一件</span>
                              <span v-if="item.soStructure ==4"> 一单一品多件</span>
                    </div>
                    <div> 
                        <span>sku总数：</span>
                        <span>{{item.totalSkuCount}}</span> 
                  </div> 
              </el-card>
            </li>
        </ul>
        <ul   v-if="waveId!=null" >
          <li   v-for="(item,index) in pickTasks"  @click="$router.push({name: 'orderPickTaskOne',params: item})">
            <el-card class="clearFloat"> 
              <div class="cardBody">
                <div class="topData clearFloat">
                  <!-- 头部信息 -->
                  <!-- <span>{{item.pickTaskId}}</span> -->
                  <!-- <span>{{item.billNo}}</span> -->
                  <span>{{item.organizationName}}</span> 
                  
                </div>
                <div class="baseData clearFloat">
                  <!-- 基础信息 -->
                  <ul class="leftFloat">
                    <li>
                      <span>货位：</span>
                      <span>{{item.cellCode}}</span>
                      
                    </li>
                    <li>
                      <span>sku：</span>
                      <span>{{item.itemCode}}</span>
                    </li>
                  </ul>
                  <ul class="rightFloat">
                    <li>
                      <span>名称：</span>
                      <span>{{item.itemName}}</span>
                    </li>
                    
                    <!-- <li>
                      <span>批次:</span>
                      <span>{{item.batchName}}</span>
                      
                    </li> -->
                    <li>
                      <span>数量:</span>
                      <span>{{item.pickQuantity}}</span>
                    </li>
                    
                  </ul>
                </div>
                <div class="minorData">
                  <!-- 次要/整行信息 -->
                  <ul>
                    
                  </ul>
                </div>
                <div class="minorData">
                  <!-- 次要/整行信息 -->
                  <ul>
                    
                  </ul>
                </div>
              </div>

            </el-card>
          </li>
        </ul>
       
    </div>
    
      
       
    </div>


  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "GetNewTask",
    data() {
      return {
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
      async getWaves2(){
        let that = this; 
         if(that.waves.length==0 || that.waveId==null){ 
           that.getWaves();
         } 
      },
      async getPickTasks2(){
        let that = this;
          for(var i =0;i<that.pickTasks.length;i){ 
          } 
         if(that.pickTasks.length==0){ 
           that.getPickTasks();
         } 
      },
      getWaves(){
        let that = this
        IOT.getServerData('/wave/masters/allotWave','get', {state:1,wareId:this.wareId},(ret) => {
          if (ret.code === 200) { 
            that.waves = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPickTasks(waveId){ 
        let that = this;
        if(waveId==null || waveId==undefined || waveId==NaN || waveId==""){
            return;
        }
        that.taskSearch.waveId = waveId; 
        IOT.getServerData('/pick/tasks/allotTask','get', that.taskSearch,(ret) => {
          if (ret.code === 200) {  
            that.pickTasks = ret.rows;
            if(that.pickTasks.length>0){
              PF.setLocal("waveId",waveId);
              that.waveId = waveId;
            }else{
                for(var i =0;i<that.waves;i++){
                    if(that.waves[i].waveId ==waveId){
                        that.waves.splice(i,1);
                    }
                }
               PF.removeLocal("waveId");
               that.waveId = null;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }, 
    },
    created() {
      var that =this;
      that.getWaves2();
      that.timer1 =setInterval(that.getWaves2,10000); 
      that.getPickTasks(that.waveId);

       
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
