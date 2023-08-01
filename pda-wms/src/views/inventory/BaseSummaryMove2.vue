<template>
  <div v-loading="loading" class="BaseSummaryMove2">
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
          <div class="minorData ">
            <!-- 基础信息 -->
            <ul class=" ">
              <li>
                <span style="width:60px" v-for="cellUseType in cellUseTypes" v-if="cellUseType.value === params.cellUseType">{{cellUseType.label}}</span>
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
              <li>
                <span v-if="params.allotQuantity">&nbsp;{{params.allotQuantity}}</span>
                <span v-else-if="params.allotQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>分配数:</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span v-if="params.packDescribe">&nbsp;{{params.packDescribe}}</span>
                <span v-else>&nbsp;-</span>
                <span>单位:</span>
              </li>
              <li>
                <span v-if="params.freezeQuantity">&nbsp;{{params.freezeQuantity}}</span>
                <span v-else-if="params.freezeQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>冻结数:</span>
              </li>
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
<!--              <li>
                <span>批次:</span>
                <span>&nbsp;{{params.batchName}}</span>
              </li>-->
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
         <!-- <el-form-item  label="" :label-width="formLabelWidth">
       
           <el-select v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位" clearable style="width: 95%;">
            <el-option
              v-for="item in cells"
              :key="item.cellCode"
              :label="item.cellCode"
              :value="item.cellCode">
            </el-option>
          </el-select>
           </el-form-item> -->
            <el-form-item  label="" :label-width="formLabelWidth">
       
           
          <!-- <el-input id="toCellCode" style="top:-28px;left:-10%;width: 75%;"  v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位"  clearable  ></el-input> -->
         <el-input id="toCellCode"   style="width: 95%;"  v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位"  clearable  ></el-input>
         
           </el-form-item>
          <!-- <el-form-item label="目标追踪号" prop="toBoxCode" :label-width="formLabelWidth">
          <el-input v-model="form.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="目标LPN" prop="toLpn" :label-width="formLabelWidth">
          <el-input v-model="form.toLpn" size="mini" placeholder="请输入目标LPN" clearable style="width: 90%;"></el-input>
        </el-form-item> -->
           <el-input  placeholder="请输入数量"  v-model="form.toQuantity" size="mini" :min="0"   style="width: 80%;"></el-input>
<!--          <el-input v-model="form.toQuantity" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>-->
          <!--<el-form-item label="单位" prop="toCellCode" :label-width="formLabelWidth">
          <el-input v-model="form.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>-->

        <el-button type="primary" size="mini" @click="showAll()">全部</el-button>
      </el-form>
    </div>
    <div style="height: 50px;"><!--留白，为底部悬浮按钮留空间，保证不遮挡上面文字--></div>
    <el-button v-show="params.cellUseType==3?false:true" @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-shangjia" style="position: fixed;bottom: 30px;left:14vw; height: 40px; width: 70%;">移库</el-button>
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import {mapState} from "vuex";

  export default {
    name: "BaseSummaryMove2",
    computed: {
            ...mapState([
                "cellUseTypes",
            ]),
        },
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
        headerTitle: "汇总移库",
        formLabelWidth: '0px',
        params: this.$route.params,

        isFindUpperCell: false,

        inventoryBalances: [],
        cells: [],
        loading:false,
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
          toQuantity: null,
          toLpn:''
        },
        nowQuantity:null,
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
        },
       
      };
    },
    methods: {
      showAll(){
        this.form.toQuantity = this.params.quantity;
      },
      findUpperCell() {
        this.loading =true;
        IOT.getServerData('/upper/tasks/findUpperCell', 'get', this.params, (ret) => {
           this.loading =false;
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
    //移库操作
    commitForm(formName) {
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = PF.JSON(this.form);
          if (form.toCellCode == this.params.cellCode) {
            IOT.tips('移库前后货位相同，请重新填写', 'error');
            return false;
          }
          if (form.toQuantity > 0) {

          } else {
            IOT.tips('请校验数量', 'error');
            return false;
          }
          if (Number(form.toQuantity) === form.row.quantity) {
            // 全部移库 不限制
          } else if (form.row.allotQuantity + form.row.freezeQuantity + Number(form.toQuantity) > form.row.quantity) {
            // 部分移库 限制只能移走可分配库存部分
            let tip = '最多可移库' + (form.row.quantity - form.row.freezeQuantity - form.row.allotQuantity);
            if (form.row.freezeQuantity + form.row.allotQuantity != 0) {
              tip = tip + '，或将' + form.row.quantity + '一次性全部移走';
            }
            IOT.tips(tip, 'error', 2000);
            return false;
          }
          this.$confirm('移库' + form.toQuantity + '件，到' + form.toCellCode, '确认移库', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(() => {
            this.loading = true;
            IOT.getServerData('/inventorys/summaryMove', 'post', {
              inventoryId: form.row.inventoryId,
              toCellCode: form.toCellCode,
              quantity: form.toQuantity,
              wareId: that.wareId,
              itemCode: form.row.itemCode,
              fromCellCode: form.row.cellCode
            }, (ret) => {
              if (ret.code === 200) {
                this.resetForm(formName);

                this.loading = false;
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.form.toCellCode= '';
                  this.form.toQuantity= null;
                  if(ret.data){
                      this.params = ret.data;
                      this.$set(this.form, 'row', this.params);
                      this.nowQuantity = this.params.quantity;
                    }else{
                      this.params.quantity = 0;
                      this.params.allotQuantity = 0;
                      this.params.freezeQuantity = 0;
                    }
                  // if (this.params == null) {
                  //   this.params.quantity = 0;
                    // this.$router.push({ name: 'baseMove', params: {} });
                  }
                  //  this.$router.push({name: '',params: {toBoxCode: form.fromBoxCode}})
                  //挑转到上一个页面
                // }
                );
              } else {
                this.loading = false;
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            }, true);
          });
        } else {
          IOT.hideOverlay();
          return false;
        }
      });
    },
      open(){
        if(this.params.cellUseType==3){
         IOT.tips('警告，集货位不可操作汇总移库！','warning',2000)
       }
      }
    },
    created() {
      this.nowQuantity = this.params.quantity;
      if(this.params.quantity==null ){
        this.PageParam_BaseMove_Search_Use = false;// 此种形式回退不携带查询条件
        this.$router.push({name: 'baseMove',params: {}});
      }
     // this.findUpperCell();
     setTimeout(function () { 
         var domFocus = document.getElementById("toCellCode"); 
         domFocus.focus();
      }, 100);

     // this.getCellData();
    },
  mounted() {
    this.open();
  },
  beforeDestroy() {
    if (PF.getLocal('PageParam_BaseMove_Search') && this.PageParam_BaseMove_Search_Use !== false) {
      PF.setLocal('PageParam_BaseMove_Search_Use', true);
      setTimeout(() => {
        PF.removeLocal('PageParam_BaseMove_Search_Use');
      }, 1000);// 1秒内返回到baseMove页面，则携带查询条件
    }
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
