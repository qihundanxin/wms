<template>
  <div class="baseMove2">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div class="baseAcceptBillBox">
      <el-card>

        <div class="cardBody">
          <div class="topData clearFloat">
            <!-- 头部信息 -->
            <span></span>
            <span>{{params.itemName}}</span>
            <span>{{params.itemCode}}</span>
          </div>
          <div class="baseData clearFloat">
            <!-- 基础信息 -->
            <ul class="leftFloat">
              <li>
                <span>货位号:</span>
                <span>&nbsp;{{params.cellCode}}</span>
              </li>
              <!-- <li>
                <span>LPN:</span>
                <span>&nbsp;{{params.lpn}}</span>
              </li> -->
            </ul>
            <ul class="rightFloat">
              <!-- <li>
                <span>追踪号:</span>
                <span>&nbsp;{{params.boxCode}}</span>
              </li> -->
            </ul>
          </div>
          <div class="mainData clearFloat">
            <!-- 重要信息 -->
            <ul class="leftFloat">
              <li>
                <span v-if="params.quantity">&nbsp;{{params.quantity}}</span>
                <span v-else-if="params.quantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>数量:</span>
              </li>
              <!-- <li>
                <span v-if="params.allotQuantity">&nbsp;{{params.allotQuantity}}</span>
                <span v-else-if="params.allotQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>分配数:</span>
              </li> -->
            </ul>
            <ul class="rightFloat">
              <li>
                <span v-if="params.packDescribe">&nbsp;{{params.packDescribe}}</span>
                <span v-else>&nbsp;-</span>
                <span>单位:</span>
              </li>
              <!-- <li>
                <span v-if="params.freezeQuantity">&nbsp;{{params.freezeQuantity}}</span>
                <span v-else-if="params.freezeQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>冻结数:</span>
              </li> -->
            </ul>
          </div>
          <div class="minorData">
            <!-- 次要/整行信息 -->
            <ul>
              <li>
                <span>仓库:</span>
                <span>&nbsp;{{params.wareName}}</span>
              </li>
              <li>
                <span>货主:</span>
                <span>&nbsp;{{params.organizationName}}</span>
              </li>
              <li>
                <span>批次:</span>
                <span>&nbsp;{{params.batchName}}</span>
              </li>
            </ul>
          </div>
        </div>

      </el-card>
    </div>

    <div style="text-align: center;">
      <el-form class="noMargin" :model="form" :rules="rules" ref="form">
        <el-form-item v-show="isFindUpperCell" label="推荐货位" :label-width="formLabelWidth">
          <span>{{form.findUpperCell}}</span>
        </el-form-item>
         <el-form-item  label="" :label-width="formLabelWidth">
       
           <el-select v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位" clearable style="width: 95%;">
            <el-option
              v-for="item in cells"
              :key="item.cellCode"
              :label="item.cellCode"
              :value="item.cellCode">
            </el-option>
          </el-select>
           </el-form-item>
            <el-form-item  label="" :label-width="formLabelWidth">
       
           
          <el-input id="toCellCode" style="top:-28px;left:-10%;width: 75%;"  v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位"  clearable  ></el-input>
         
           </el-form-item>
          <!-- <el-form-item label="目标追踪号" prop="toBoxCode" :label-width="formLabelWidth">
          <el-input v-model="form.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="目标LPN" prop="toLpn" :label-width="formLabelWidth">
          <el-input v-model="form.toLpn" size="mini" placeholder="请输入目标LPN" clearable style="width: 90%;"></el-input>
        </el-form-item> -->
           <el-input  placeholder="请输入数量"  v-model="form.toQuantity" size="mini" :min="0"   style="width: 95%;"></el-input>
<!--          <el-input v-model="form.toQuantity" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>-->
          <!--<el-form-item label="单位" prop="toCellCode" :label-width="formLabelWidth">
          <el-input v-model="form.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>-->
      </el-form>
      <el-button @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-shangjia" style="width: 200px;margin-top: 5px;"> 移库</el-button>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BaseMove2",
    data() {
      let validateQuantity = (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('数量不得为零'));
          callback();
        } else if (value > 0) {
          callback();
        } else {
          callback(new Error('请输入正确格式'));
        }
      };
      return {
        headerTitle: "主动补货",
        formLabelWidth: '0px',
        params: this.$route.params,

        isFindUpperCell: false,

        inventoryBalances: [],
        cells: [],

        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          toBoxCode: '',
          itemCode: '',
        },
         wareId: PF.getLocal('wareId', 'number'),
        form: {
          row: this.$route.params,
          findUpperCell: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: 0,
          toLpn:''
        },
        rules: {
          toCellCode: [
            {required: true, message: '请选择目标货位', trigger: 'blur'}
          ],
          // toBoxCode: [
          //   {required: true, message: '请选择目标追踪号', trigger: 'blur'}
          // ],
          toQuantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            {validator: validateQuantity, trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      findUpperCell() {
        IOT.getServerData('/upper/tasks/findUpperCell', 'get', this.params, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            this.isFindUpperCell = true;
            this.form.findUpperCell = ret.data;
            this.form.toCellCode = ret.data;
          } else if (ret.code === 122) {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'warning');
            this.isFindUpperCell = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      },
      getCellData(){
        IOT.getServerData('/cell/infos/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName) {
        let that = this;
       
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            if(form.toQuantity>0){
               
            }else{
                 IOT.tips( '请校验数量', 'error');
                return false;
            }
              IOT.showOverlay('保存中，请稍等...');
            IOT.getServerData('/inventorys/move', 'post', 
            {
              inventoryId:form.row.inventoryId,
              cellCode:form.toCellCode,
              quantity:form.toQuantity,
              wareId:that.wareId
            
            }, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                   this.resetForm(formName);
                  this.params = ret.data;
                  if(this.params==null){
                      this.$router.push({name: 'baseMove',params: {}});
                  }
                //  this.$router.push({name: '',params: {toBoxCode: form.fromBoxCode}})
                  //挑转到上一个页面
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            },true);
           
          //  this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
           // console.error('error submit!!');
            return false;
          }
        });
      },
    },
    created() {
      if(this.params.quantity==null ){
           this.$router.push({name: 'baseMove',params: {}});
      }
     // this.findUpperCell();
     setTimeout(function () { 
  　　　　var domFocus = document.getElementById("toCellCode"); 
         domFocus.focus();
  　　}, 100);

     this.form.toQuantity = this.params.quantity;
      this.getCellData();
    },
    mounted() {
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseUpCell {
  }
</style>
