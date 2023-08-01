<template>
  <div class="BoxPickTaskList">
    <FeaturesHeader :headerTitle="headerTitle" />

    <el-dialog title="发运" :visible.sync="dialogVisible" width="80%">
      <h4 style="margin-bottom:15px">订单：{{ orderNo2 }}</h4>

      <!-- :before-close="handleClose" -->
      <h3 style="text-align:center;">是否确定发运？</h3>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="fy">确 定</el-button>
      </span>
    </el-dialog>


    <div style="text-align:center ;" class="   ">
      <el-alert :title="inputValue ? inputValue : '请扫描SKU标签'" center type="info" :closable="false" style="margin: 0 20px; width: calc(100% - 40px);" />
      </el-alert>
      <el-divider v-if="fyedList && fyedList.length > 0" content-position="left">发运记录：</el-divider>
      <div v-for="(fyed,fyedIndex) in fyedList" :key="fyedIndex">
        <div v-if="fyedIndex==fyedListOpenIndex" style="width: 100%;" @click="fyedListOpenIndex=null">
          <el-alert
            :title="'【'+fyed.qrIndex+'】'+fyed.soBillNo+' '+fyed.itemCode+(fyed.success?'成功':'失败')"
            :type="fyed.success?'success':'error'"
            effect="dark"
            :closable="false"
            style="margin: 2px 10px; width: auto;"
          />
        </div>
      </div>
      <div style="display: flex;flex-wrap: wrap;">
        <div
          v-for="(fyed,fyedIndex) in fyedList"
          :key="fyedIndex"
          :style="{'background-color':fyedIndex==fyedListOpenIndex? '#3f94fa':(fyed.success?'#62b93f':'#ef6464')}"
          style="color:#FFFFFF; padding: 3px;margin: 3px;width: 28px;height: 28px; line-height: 28px;border-radius: 4px;"
          @click="fyedListOpenIndex=fyedIndex==fyedListOpenIndex?null:fyedIndex"
        >
          {{ fyed.fyedIndex }}
        </div>
      </div>
    </div>
    <div v-if="soMaster && soMaster.allotBillNo" style=" margin-top:5px ">
      <el-divider content-position="left">当前订单剩余发运：</el-divider>
      <el-card class="clearFloat">

        <div class="cardBody">
          <!-- <div class="topData clearFloat">
                <span>{{soMaster.allotBillNo}}</span>
                 <span>{{soMaster.shopDailyCount}}</span> 
              </div> -->
          <div class="baseData clearFloat">
            <!-- 基础信息 -->
            <ul class="leftFloat">
              <li>
                <span>{{ soMaster.allotBillNo }}</span>
                <span>{{ soMaster.shopDailyCount }}</span>
              </li>
            </ul>
            <ul class="rightFloat">

              <li>
                <span v-if="soMaster.state == undefined">--</span>
                <span v-else>状态:</span>
                <span v-if="soMaster.state == 1" style="color:#909399">待处理</span>
                <span v-if="soMaster.state == 2" style="color:#909399">待处理</span>
                <span v-if="soMaster.state == 3" style="color:#F56C6C">缺货</span>
                <span v-if="soMaster.state == 4" style="color:#409EFF">已分配</span>
                <span v-if="soMaster.state == -1" style="color:#909399">取消</span>
                <span v-if="soMaster.state == 6" style="color:#543044">拣货完成</span>
                <span v-if="soMaster.state == 7" style="color:#E6A23C">已复核</span>
                <span v-if="soMaster.state == 8" style="color:#67C23A">全部发运</span>
                <span v-if="soMaster.state == 12" style="color:#67C23A">部分发运</span>
                <!-- <span>&nbsp;{{soMaster.state}}</span> -->
              </li>

            </ul>
          </div>
          <div class="minorData">
            <!-- 次要/整行信息 -->
            <ul>
              <li>
                <span v-if="soMaster.wareName == undefined">--</span>
                <span v-else>仓库:</span>

                <span>&nbsp;{{ soMaster.wareName }}</span>
              </li>
              <li>
                <span v-if="soMaster.organizationName == undefined">--</span>
                <span v-else>货主:</span>

                <span :title="soMaster.organizationName">&nbsp;{{ soMaster.organizationName }}</span>
              </li>

            </ul>
          </div>
          <div class="minorData">
            <!-- 次要/整行信息 -->
            <ul>
              <li>
                <span v-if="soMaster.createUserName == undefined">--</span>
                <span v-else>创建人:</span>

                <span :title="soMaster.createUserName">&nbsp;{{ soMaster.createUserName }}</span>
              </li>
              <li>
                <span v-if="soMaster.createTime == undefined">--</span>
                <span v-else>创建时间:</span>

                <span>&nbsp;{{ soMaster.createTime }}</span>
              </li>

            </ul>
          </div>
        </div>

      </el-card>

      <div @click="commitBoxCode(item)" v-for="(item, index) in soMaster.list"
        style="overflow: auto; height:280px; width:100%;   margin-top:10px ;margin-left:3%">
        <el-row style="width:90%">
          <el-col :span="7">
            <el-image style="width: 80px; height: 80px" :src="item.imgUrl"></el-image>

          </el-col>
          <el-col :span="17">
            <div style="padding-left:5px;padding-top:0px;font-size:15px">

              <div>

                <span>SKU：</span>
                <span>{{ item.itemCode }} </span>

              </div>
              <div>
                <span>数量：</span>
                <span style="color:#E6A23C; ">{{ item.orderQuantity }} </span>

              </div>
              <div>
                <span>单位：</span>
                <span>{{ item.packDescribe }} </span>

              </div>

            </div>

          </el-col>
        </el-row>

        <div style="color:#FFFFFF; line-height:20px;padding-left:20px;width:90%;height:20px;background-color: #409EFF;">
          {{ item.itemName }}
        </div>





      </div>
    </div>

    <!--订单发运完毕提示框 -->
    <el-dialog title="请核查" :visible.sync="skuSendEnd" width="80%" :show-close="false" :close-on-press-escape="false" :close-on-click-modal="false">
      <h4>订单：{{ soBillNo }}</h4>
      <h4 style="text-align: center;margin:10px 0 20px 0">此SKU已全部完成发运</h4>
      <p style="font-size:13px;padding:0 15px"><span>SKU:{{ itemCode }}</span><span style="float:right;">商品数量：{{
        sendEndQuantity }}</span></p>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size='mini' @click="skuSendEnd = false;">关 闭</el-button>
      </span>
    </el-dialog>

    <!--订单超过10个后可输入总数输入框 -->
    <el-dialog title="提示" :visible.sync="skuSendOver" width="80%" :before-close="closeSkuSendOver">
      <p>订单：{{ soBillNo }}</p>
      <h4 style="margin:10px 0 20px 0;padding:0 10px">此SKU商品数量超过10件，请确认此SKU产品总数量</h4>
      <el-input placeholder="请输入此SKU产品总数量" v-model="sendSumQuantity" clearable style="width:95%;padding-left:3%" @change="verfQuantity" type="number">
      </el-input>
      <p v-show="overTip" style="color:#D9001B;padding:5px 10px;font-size:13px ">{{tipContent}}</p>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="closeSkuSendOver()">取 消</el-button>
        <el-button type="primary" size="mini" @click="verfQuantity(1)" :disabled="isTrue">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 悬浮 -->
    <!-- <p style="color:#02A7F0;text-align: center;position:fixed;bottom:40px;width:100%" @click="goSendList">本日扫码记录</p> -->
    <!-- 不悬浮 -->
    <p style="color:#02A7F0;text-align: center;width:100%;padding: 10px 0;" @click="goSendList">本日扫码记录</p>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";

export default {
  name: "BoxPickTaskList",
  data() {
    return {
      sendSumQuantity:null,
      skuSend:'',
      tipContent:'',
      overTip:false,
      sendEndQuantity: '',
      skuSum:'',
      sendOverQuantity:'',
      skuSendEnd: false,
      skuSendOver: false,
      dialogVisible: false,
      inputValue: '',
      soMaster: {},
      boxCodeVisible: false,
      quantityDialogVisible: false,
      waveMaster: {},
      exTask: {},
      exDialogVisible: false,
      pickTask: null,
      cellCodeOK: false,
      cellCode: '',
      itemCode: '',
      boxCode: '',
      fyedList: [],
      fyedListOpenIndex: null,
      wareId: PF.getLocal('wareId', 'number'),
      waves: [],
      waveId: null,
      dialogFormVisible: false,
      pickTaskVisible: false,


      headerTitle: "订单发运",
      formLabelWidth: '80px',
      pickTasks: [],
      timer1: null,
      taskSearch: {
        wareId: PF.getLocal('wareId', 'number'),
        orgId: null,
        areaCode: '',
        shelfCode: '',
        itemCode: '',
        state: 0,
        waveId: '',

      },
      orderNo2: '',
      sendQuantity: 1,
      soBillNo: "",
      itemCode: '',
      sendDetail: {},
      isTrue:false

    };
  },
  watch: {
    inputValue: function (newVal, oldVal) {
      setTimeout(() => {
        if (newVal && this.inputValue == newVal) {// 半秒内输入框内容未更新
          let qrObject = PF.simpleQrEncode(newVal);
          if (qrObject && qrObject.index1 && qrObject.index2 && qrObject.index2.trim().length > 1) {
            this.soBillNo = qrObject.index1;
            this.itemCode = qrObject.index2;
            this.qrIndex = qrObject.index3 ? qrObject.index3 : 0;
            if (this.soBillNo && this.itemCode) {
              this.getSendQuantity(this.soBillNo, this.itemCode);
              this.getSoMasters(this.soBillNo);// 立刻刷新，这样首次扫码能更快弹出订单信息
            }else{
              IOT.tips("二维码识别失败", 'error');
              this.inputValue = '';
            }
          } else {
            IOT.tips("二维码识别失败", 'error');
            this.inputValue = '';
          }
        }
      }, 500);
    }
  },
  methods: {
    closeSkuSendOver(){
      this.skuSendOver = false;
      this.overTip = false;
      this.sendSumQuantity=null;
      this.bindKeyDown()
    },
    /** 绑定键盘按下事件：全局模式下增加键盘监听；非全局模式下输入框获取焦点 */
    bindKeyDown() {
      document.onkeydown = (e) => {
        if (e.key && e.key.length == 1) {
          this.inputValue = this.inputValue + e.key
        }
      }
    },
    /** 接下来需要使用键盘了，取消键盘绑定事件 */
    bindKeyDownOff(){
      document.onkeydown = () => { }
    },
    goSendList(){
      this.$router.push({ name: 'fyRecord' })
    },

    //验证输入的发运数量是否超过订单总数
    verfQuantity(param){
      this.overTip = false;
      if(this.sendSumQuantity>this.skuSum){
        this.tipContent = "此SKU输入产品数量超过下单数量";
        this.overTip = true;
      }else if(this.sendSumQuantity<=this.skuSend){
        this.tipContent = "此SKU输入产品数量需大于已扫码数量";
        this.overTip = true;
      }else if(param==1){
        this.isTrue = true;
        this.fy();
      }
    },
    scan(scanType) {
      SCAN.startRecognize(
        (type, result) => {
          if (scanType == 1) {
            this.inputValue = result;
          }

          SCAN.close();
        });
    },

    fy1() {
      if (this.soMaster.state != undefined) {
        this.dialogVisible = true;
      }

    },


    //SKU 发运
    fy() {
      console.log("fy");
      this.overTip = false;
      this.skuSendOver = false;
      let form = {
        soBillNo: this.soBillNo,
        itemCode: this.itemCode,
        qrIndex: this.qrIndex,
        sendQuantity: this.sendQuantity,
        sendSumQuantity:this.sendSumQuantity,
        wareId: this.wareId
      }
      IOT.getServerData('/so/masters/skuSendOut', 'post', form, (ret) => {
        // this.sendSumQuantity=null;
        if (ret.code === 200) {
          this.sendSumQuantity = null;
          // that.soMaster = {};false
          IOT.tips("发运成功！", 'success');
          this.getSoMasters(this.soBillNo); // 发运成功，刷新订单显示
          this.fyedList.push({success:true,...form})
        } else if (ret.code === 100070) {
          this.fyedList.push({success:false,...form})
          this.skuSendEnd = true;
          setTimeout(()=>{
            this.skuSendEnd = false;
          },3000)
        } else {
          this.fyedList.push({success:false,...form})
          // that.soMaster = {};
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true, null, () => {// error
        this.fyedList.push({success:false,...form})
      });

    },

    //查询已经发运数量
    getSendQuantity(soBillNo, itemCode) {
      IOT.getServerData('/so/masters/sendQuantity', 'get', { soBillNo: soBillNo, itemCode: itemCode }, (ret) => {
        this.inputValue = '';
        if (ret.code === 200) {
          if(ret.data!=null){
            this.skuSum = ret.data.orderQuantity;
            this.skuSend = ret.data.sendQuantity;
            //已经发运完毕
            if (this.skuSend == this.skuSum) {
              this.sendEndQuantity =  this.skuSum;
              this.skuSendEnd = true;
              setTimeout(() => {
                this.skuSendEnd = false;
              }, 3000)
              return;
            }
            //未完毕但是超过10个
            if (ret.data.sendQuantity >= 10) {
              this.sendSumQuantity = null;
              this.bindKeyDownOff()
              this.skuSendOver = true;
              this.overTip = false;
              this.isTrue = false;
              return;
            }
          }
          //发运流程
          this.fy();
          this.sendQuantity = 1;
          this.sendSumQuantity = null;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true, null ,()=>{
        this.inputValue = '';
      });
    },

    getSoMasters(orderNo) {
      let that = this
      that.soMaster = {};
      IOT.getServerData('/so/masters/findBillDetailByOrderNo', 'get', { orderNo: orderNo, wareId: this.wareId }, (ret) => {
        if (ret.code === 200) {
          that.soMaster = ret.data;
          that.inputValue = "";
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },

  },

  created() {
    this.bindKeyDown()
  },
  destroyed() {
    this.bindKeyDownOff()
  },
  components: {
    FeaturesHeader,
  }
};
</script>
<style lang="less" scoped>
.BoxPickTaskList {
  .el-card__body {
    padding: 10px;
  }
  .el-button--text {
    color: #409EFF;
    background: 0 0;
    padding: 0 0 0 0;
  }


  .el-dialog__body {
    padding: 10px 10px;
    color: #606266;
    font-size: 14px;
    word-break: break-all;
  }

  .el-dialog__body {
    padding: 10px 10px;
    color: #606266;
    font-size: 14px;
    word-break: break-all;
  }

}
/deep/ input::-webkit-outer-spin-button,
/deep/ input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}
/deep/ input[type='number'] {
  -moz-appearance: textfield;
}
</style>
