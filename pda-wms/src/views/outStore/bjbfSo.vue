<template>
  <div class="BoxPickTaskList">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div v-if="pickTasks.length == 0" class="scanInput">
      <el-input placeholder="SO-拣货单" id="billNo" v-model="billNo"></el-input>
      <i class="el-icon-camera" @click="scan(1)"></i>
    </div>
    <div v-if="pickTasks.length > 0" style="text-align:center ;" class="   ">
      <div v-if="pickTask == null" class="scanInput">
        <el-button id="cellCodeShow" @click="cellCodeFocus()" style="width: 100%;text-align: left;color: #c0c4cc;">{{ cellCode ? cellCode : '请扫描货位码' }}</el-button>
        <!-- <i class="el-icon-camera" style="font-size:30px" @click="scan(1)"></i> -->
        <input id="cellCode" style="height: 0; border: 0; margin: 0;padding: 0;" v-model="cellCode" @blur="cellCoding(false)"/>
      </div>
      <div v-if="pickTask != null" class="scanInput">
        <el-input id="itemCode" v-model="itemCode" placeholder="商品编码（SKU）"></el-input>
        <i class="el-icon-camera" style="font-size:30px" @click="scan(2)"></i>
      </div>
    </div>
    
    <el-alert v-if="alertText" :title="alertText" type="info" :closable="false" center></el-alert>
    <div style="overflow: auto; height: 500px; margin-top:5px ">
      <!-- 可拣货item -->
      <div @click="commitBoxCode(item)" v-for="(item, index) in pickTasks"
        style="width:94%;margin-top:10px;margin-left:2%">
        <el-row>
          <el-col :span="7">
            <el-image style="width: 80px; height: 80px" :src="item.imgUrl"></el-image>
          </el-col>
          <el-col :span="17">
            <div style="padding-left:5px;padding-top:0px;font-size:15px">
              <div>
                <span>SKU:</span> {{ item.sjcUpperTaskId }}
                <span>{{ item.itemCode }}</span>
              </div>
              <div>
                <span>数量:</span>
                <span :style="item.reviewQuantity < item.pickQuantity ? 'color:#E6A23C' : 'color:#409EFF'">{{ item.reviewQuantity }}</span>
                <span style="color:#409EFF;">/{{ item.pickQuantity }}</span>
                <template v-if="pickTask != null && pickTask.pickTaskId == item.pickTaskId">
                  <el-button @click.stop="quantityDialog(item.pickTaskId)" style="margin-left:20px" size="mini"
                    type="primary" icon="el-icon-edit" round>填写</el-button>
                  <el-button @click.stop="ex(item)" style="margin-left:20px;display: none;" size="mini" type="warning" round>异常上报</el-button>
                </template>
              </div>
              <div>
                <span>单号:</span>
                <span>{{ item.billNo }} {{ item.shopDailyCount }}</span>
              </div>
              <div>
                <span>店铺订单日序号:</span>
                <span>{{ item.shopDailyCount }}</span>
              </div>
              <div>
                <span>货位:</span>
                <span>{{ item.cellCode }}</span>
              </div>
              <div>
                <span v-if="pickTask != null && item.pickTaskId == pickTask.pickTaskId" style="color:#67C23A; ">请拣货</span>
                <span v-else-if="item.state == 2" style="color:#67C23A; ">完成</span>
                <span v-else-if="item.reviewQuantity > 0.1 && item.state != 2" style="color:#E6A23C; ">拣货中</span>
                <span v-else-if="item.state == 0" style="color:#909399; ">待拣货</span>
              </div>
            </div>
          </el-col>
        </el-row>
        <div style="color:#FFFFFF;line-height:20px;padding:0 5px;width:100%;word-break:break-all;"
          :style="(pickTask != null && pickTask.pickTaskId == item.pickTaskId)?{'background-color': '#409EFF'} : {'background-color': '#909399'}">
          {{ item.itemName }}
        </div>
      </div>
      <!-- 缺货item -->
      <el-alert v-if="stockouts.length > 0" :closable="false" title="补货提醒" type="warning" center></el-alert>
      <div v-for="item in stockouts"
        style="width:94%;margin-top:10px;margin-left:2%">
        <el-row>
          <el-col :span="7">
            <el-image style="width: 80px; height: 80px" :src="item.imgUrl"></el-image>
          </el-col>
          <el-col :span="17">
            <div style="padding-left:5px;padding-top:0px;font-size:15px">
              <div>
                <span>SKU:</span> {{ item.sjcUpperTaskId }}
                <span>{{ item.itemCode }}</span>
              </div>
              <div>
                <span>数量:</span>
                <span style="color:#409EFF; padding-right: 6px;">{{ item.orderQuantity }}</span>
                <span style="color:#ed1c24;">缺货</span>
              </div>
              <div>
                <span style="padding-right: 6px;">拣货位:</span>
                <span v-if="item.type1List.length > 2" style="color:#909399;">...更多...</span>
                <span v-if="item.type1List.length == 0" style="color:#909399;">无</span>
              </div>
              <template v-for="(inv1,index) in item.type1List">
                <div v-if="index < 2">
                  <span style="padding-right: 6px;">{{ inv1.cellCode }}</span>
                  <span style="color:#409EFF;">{{ inv1.quantity }}</span>
                </div>
              </template>
              <div>
                <span style="padding-right: 6px;">仓储位:</span>
                <span v-if="item.type2List.length > 2" style="color:#909399;">...更多...</span>
                <span v-if="item.type2List.length == 0" style="color:#909399;">无</span>
              </div>
              <template v-for="(inv2,index) in item.type2List">
                <div v-if="index < 2">
                  <span style="padding-right: 6px;">{{ inv2.cellCode }}</span>
                  <span style="color:#409EFF;">{{ inv2.quantity }}</span>
                </div>
              </template>
            </div>
          </el-col>
        </el-row>
        <div style="color:#FFFFFF;line-height:20px;padding:0 5px;width:100%;word-break:break-all;"
          :style="(pickTask != null && pickTask.pickTaskId == item.pickTaskId)?{'background-color': '#409EFF'} : {'background-color': '#909399'}">
          {{ item.itemName }}
        </div>
      </div>
    </div>
    <el-dialog title="异常" :visible.sync="exDialogVisible" width="90%">
      <div style="padding-left:5px;padding-top:0px;font-size:15px">
        <div>
          <span>SKU:</span>
          <span>{{ exTask.itemCode }}</span>
        </div>
        <div>
          <span>需求数:</span>
          <span style="color:#409EFF; ">{{ exTask.pickQuantity }}</span>
        </div>
        <div>
          <span>拣货数:</span>
          <span style="color:#E6A23C; ">{{ exTask.reviewQuantity }}</span>
        </div>
        <div>
          <span>货位:</span>
          <span>{{ exTask.cellCode }}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="exDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="exCommit">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="数量" :visible.sync="quantityDialogVisible" width="90%">
      <div v-if="pickTask != null" style="padding-left:5px;padding-top:0px;font-size:15px">
        <div>
          <span>货位:</span>
          <span>{{ pickTask.cellCode }}</span>
        </div>
        <div>
          <span>SKU:</span>
          <span>{{ pickTask.itemCode }}</span>
        </div>
        <div>
          <span>需求数:</span>
          <span style="color:#409EFF; ">{{ pickTask.pickQuantity }}</span>
        </div>
        <div>
          <!-- <span>拣货数:</span> -->
          <el-input size="mini" v-model="quantityDialogNumber"></el-input>
          <!-- <span style="color:#E6A23C; ">{{pickTask.reviewQuantity}}</span> -->
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="quantityDialogClose()">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="容器号" :visible.sync="boxCodeVisible">
      <el-input id="boxCode" v-model="boxCode" autocomplete="off"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="boxCodeVisible = false">取 消</el-button>
        <el-button type="primary" @click="commit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "BoxPickTaskList",
  data() {
    return {
      billNo: '',
      boxCodeVisible: false,
      quantityDialogNumber: 0,
      quantityDialogVisible: false,
      waveMaster: {},
      exTask: {},
      exDialogVisible: false,
      pickTask: null,
      cellCode: '',
      itemCode: '',
      boxCode: '001',
      wareId: PF.getLocal('wareId', 'number'),
      waves: [],
      waveId: null,
      dialogFormVisible: false,
      pickTaskVisible: false,
      headerTitle: "边拣边分",
      formLabelWidth: '80px',
      pickTasks: [],
      stockouts: [],
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
    };
  },
  computed: {
    alertText(){
      if(this.pickTasks.length == 0){
        return '请扫描【拣货单二维码】或粘贴【SO-单号】';
      }else if(this.pickTasks.length > 0){
        if(this.pickTask == null){
          return '请扫描或粘贴【库位号】';
        }else{
          if(this.pickTask.pickQuantity !== this.pickTask.reviewQuantity){
            return '扫描或粘贴【SKU】，或手动修改数量';
          }else {
            return '请点击明细以完成SKU拣货'
          }
        }
      }
      return false;
    }
  },
  watch: {
    billNo: function (newVal, oldVal) {
      if (newVal != null && newVal.length > 1) {
        setTimeout(() => {
          if (newVal == this.billNo) {
            this.getPickTasks(newVal);
            this.billNo = '';
          }
        }, 400);
      }
    },
    cellCode: function (newVal, oldVal) {
      if (newVal != null && newVal.length > 0) {
        IOT.showOverlay('Loading');
        let flag = false;
        this.pickTasks.forEach(task => {
          if(flag){return}
          if (task.cellCode.trim() == newVal.trim() && task.state == 0) {// 遍历匹配到了货位
            flag = true;
            this.getTask();
            setTimeout(() => {
              var domFocus = document.getElementById('itemCode');
              domFocus.focus();
              // this.cellCode = '';
              IOT.hideOverlay();
            }, 100);
            // 称重提示
            if (task.weighting) {
              const itemNameArr = task.itemName ? task.itemName.split('|') : ['']
              const itemNameEnglish = itemNameArr[itemNameArr.length - 1]
              this.$alert('<h3>SKU: ' + task.itemCode + '</h3><h3>Item name: ' + itemNameEnglish + '</h3><p>This Product need to be photographed and weighed, please ensure that the photo is sent to customer service to modify the weight</p>', 'confirmation', {
                dangerouslyUseHTMLString: true,
                confirmButtonText: 'confirm',
                callback: () => {}
              });
            }
          }
        });
        if(!flag){// 遍历未匹配到货位
          setTimeout(() => {
            if(this.cellCode == newVal && this.cellCode != null && this.cellCode.length > 0){
              this.cellCode = '';
              IOT.hideOverlay();
            }
          },400);
        }
      }
    },
    itemCode: function (newVal, oldVal) {
      // 扫码日志
      const scanLog = {
        hasSuccess: null,// 是否成功
        scanValue: newVal,// 码内容
        scanType: 1,// 扫描方式 1=按任务拣货
      }
      const saveScanLog = () => {
        IOT.getServerData('/scanLog/save', 'post', scanLog)
      }
      // 二维码识别
      if(this.itemCode == newVal){
        let thisTask = this.pickTask;
        let qrObject = PF.simpleQrEncode(newVal);
        if(qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
          this.itemCode = qrObject.index2;
          return;
        }
        this.pickTasks.forEach(task => {
          if (thisTask.pickTaskId == task.pickTaskId
            && task.cellCode.trim() == this.cellCode.trim()
            &&
            (
              task.itemCode.trim() == newVal.trim() || this.mateUpc(newVal.trim(), task.upcs)
            )
            && task.state == 0) {
            // TODO 是否需要称重，暂时由前端判断
            const updateSkuData = () => {// SKU扫描有效时执行此方法更新页面数据
              if (task.pickQuantity == task.reviewQuantity) {
                this.$message({
                  message: '该SKU数量已满！',
                  type: 'warning'
                });
                scanLog.hasSuccess = false;
                saveScanLog();
              } else {
                task.reviewQuantity += 1;
                scanLog.hasSuccess = true;
                saveScanLog();
                if (task.pickQuantity == task.reviewQuantity) {
                  this.boxCodeVisible = true;
                  setTimeout(function () {
                    var domFocus = document.getElementById('boxCode');
                    domFocus.focus();
                  }, 100);
                }
              }
              this.itemCode = '';
            }
            // 称重提示
            if(task.weighting){
              const itemNameArr = task.itemName ? task.itemName.split('|') : ['']
              const itemNameEnglish = itemNameArr[itemNameArr.length - 1]
              this.$alert('<h3>SKU: '+task.itemCode+'</h3><h3>Item name: '+itemNameEnglish+'</h3><p>This Product need to be photographed and weighed, please ensure that the photo is sent to customer service to modify the weight</p>', 'confirmation', {
                dangerouslyUseHTMLString: true,
                confirmButtonText: 'confirm',
                callback: () => {
                  updateSkuData()
                }
              });
            }else{
              updateSkuData()
            }
          }
        });
      }
    }
  },
  methods: {
    mateUpc(upc, upcs) {
      if (upcs == null || upcs.length == 0) {
        return false;
      }
      for (var i = 0; i < upcs.length; i++) {
        if (upcs[i].upc == upc) {
          return true;
        }
      }
      return false;
    },
    cellCodeFocus(){
      let inp = document.getElementById('cellCode');
      inp.setAttribute('readonly', true);
      inp.focus();
      setTimeout(() => {
        inp.removeAttribute('readonly');
        this.cellCoding(true);
      },100)
    },
    cellCoding(bool){
      let dom = document.getElementById('cellCodeShow');
      dom.style.borderColor = bool ? "#3f94fa": "";
    },
    scan(scanType) {
      SCAN.startRecognize(
        (type, result) => {
          if (scanType == 1) {
            this.billNo = result;
          }
          else if (scanType == 2) {
            this.billNo = result;
          }
          SCAN.close();
        });
    },
    commitBoxCode(task) {
      var pickTask = this.pickTask;
      if (pickTask != null && pickTask.pickTaskId == task.pickTaskId && task.pickQuantity == task.reviewQuantity) {
        this.boxCodeVisible = true;
        setTimeout(function () {
          var domFocus = document.getElementById('boxCode');
          domFocus.focus();
        }, 100);
      }
    },
    async getWaves2() {
      let that = this;
      if (that.waves.length == 0 || that.waveId == null) {
        that.getWaves();
      }
    },
    getWaves() {
      let that = this
      that.loading = true;
      IOT.getServerData('/wave/masters/allotWave', 'get', { isSeeding: 0, state: 11, wareId: this.wareId }, (ret) => {
        that.loading = false;
        if (ret.code === 200) {
          that.waves = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    quantityDialog(pickTaskId) {
      if (this.pickTask != null && this.pickTask.pickTaskId == pickTaskId) {
        this.quantityDialogNumber = this.pickTask.reviewQuantity;
        if (this.quantityDialogNumber == 0) {
          this.quantityDialogNumber = '';
        }
        this.quantityDialogVisible = true;
      }
    },
    quantityDialogClose() {
      if (!(this.quantityDialogNumber * 1) && (this.quantityDialogNumber * 1) !== 0) {
        IOT.tips('请填写数字', 'error');
        return;
      }
      if (this.quantityDialogNumber * 1 % 1 !== 0) {
        IOT.tips('请填写整数', 'error');
        return;
      }
      if (this.quantityDialogNumber * 1 < 0) {
        IOT.tips('数量需大于0', 'error');
        return;
      }
      if (this.quantityDialogNumber * 1 > this.pickTask.pickQuantity) {
        IOT.tips('数量超出上限：' + this.pickTask.pickQuantity, 'error');
        return;
      }
      this.$set(this.pickTask, 'reviewQuantity', this.quantityDialogNumber * 1);
      this.quantityDialogNumber = 0;
      this.quantityDialogVisible = false;
      if(this.pickTask.reviewQuantity === this.pickTask.pickQuantity){
        this.commitBoxCode(this.pickTask);
      }
    },
    ex(task) {
      this.exTask = task;
      this.exDialogVisible = true;
    },
    exCommit() {
      var task = this.exTask;
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let param = {
        pickTaskId: task.pickTaskId,
        wareId: this.wareId,
        boxCode: this.boxCode,
        pickQuantity: task.reviewQuantity,
        exQuantity: task.pickQuantity - task.reviewQuantity,
        waveMasterId: task.waveMasterId

      }
      // String boxCode,Integer wareId,Integer pickTaskId,String toCellCode,Double pickQuantity,Double exQuantity
      IOT.getServerData('/pick/tasks/pickTaskEx', 'get', param, (ret) => {
        loading.close();
        if (ret.code === 200) {
          IOT.tips('成功！', 'success', 1000, () => {
          });
          this.exDialogVisible = false
          this.pickTasks.splice(0, 1)
          this.pickTask = null;
          this.cellCode = null;
          this.itemCode = null;
          this.exPickTask = {};
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    commit() {
      let that = this;
      var task = this.pickTasks[0];
      if (task.pickQuantity != task.reviewQuantity) {
        this.$message({
          message: '数量不符合',
          type: 'warning'
        });
        return;
      }
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let param = {
        pickTaskId: task.pickTaskId,
        wareId: this.wareId,
        boxCode: this.boxCode,
        reviewQuantity: task.reviewQuantity
      }
      IOT.getServerData('/pick/tasks/pickTaskBjbfAffirm', 'post', param, (ret) => {
        if (ret.code === 200) {
          IOT.tips('成功！', 'success', 1000, () => {
          });
          this.boxCodeVisible = false;
          this.pickTasks.splice(0, 1);
          if (this.pickTasks.length == 0) {
            this.pickTask = null;
            this.cellCode = null;
            this.itemCode = null;
            this.getWaves();
            loading.close();
            setTimeout(function () {
              var domFocus = document.getElementById('billNo');
              domFocus.focus();
            }, 100);
          } else {
            this.pickTask = null;
            this.cellCode = null;
            this.itemCode = null;
            setTimeout(function () {
              that.cellCodeFocus();
            }, 100);
            loading.close();
          }
        } else if (ret.code === 1234) {
          this.pickTasks.splice(0, 1)
          this.pickTask = null;
          this.cellCode = null;
          this.itemCode = null;
          if (this.pickTasks.length == 0) {
            setTimeout(function () {
              var domFocus = document.getElementById('billNo');
              domFocus.focus();
            }, 100);
          }
          else if (this.pickTasks.length > 0) {
            setTimeout(function () {
              that.cellCodeFocus();
            }, 100);
          }

          loading.close();
          IOT.tips(ret.message, 'error');
        }
        else if (ret.code === 12345) {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          this.pickTasks[0] = ret.data;
          this.pickTask = ret.data;
          loading.close();
          //    this.cellCode = null;
          //    this.itemCode=null;
          //    setTimeout(function () { 
          // 　　　 var domFocus = document.getElementById('cellCode'); 
          //       domFocus.focus();
          // 　　}, 100);
          //    loading.close();
          //     IOT.tips(ret.message  , 'error');
        }
        else {
          loading.close();
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true, null, () => {
        loading.close();
      });
    },
    clear() {
      // this.boxCode = '001';
      var domFocus = document.getElementById('boxCode');
      domFocus.focus();
    },
    clear2() {
      // this.boxCode = '001';
      var domFocus = document.getElementById('itemCode');
      domFocus.focus();
    },
    getTask() {
      var that = this;
      var tasks = this.pickTasks;
      for (let i = 0; i < tasks.length; i++) {
        if (tasks[i].cellCode.trim() == that.cellCode.trim()) {
          this.pickTask = tasks[i];
          this.pickTasks.unshift(this.pickTasks.splice(i, 1)[0]);
        }
      }
    },
    getPickTasks(billNo) {
      let that = this;
      that.pickTasks = [];
      if (billNo == null || billNo == undefined || billNo == NaN || billNo == '') {
        return;
      }
      IOT.showOverlay('Loading');
      that.taskSearch.billNo = billNo;
      IOT.getServerData('/pick/tasks/findRunningByBillNo', 'get', that.taskSearch, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          that.pickTasks = ret.rows;
          if (that.pickTasks.length > 0) {
            setTimeout(() => {
              that.cellCodeFocus();
            }, 100);
          }
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, null, null, () => {
        IOT.hideOverlay();
      });
      IOT.getServerData('/so/details/StockoutInfo', 'get', that.taskSearch, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          var result = [];
          var stockouts = ret.rows;
          // 计算补货信息
          for(var i in stockouts){
            if(stockouts[i].state != 1){
              continue;
            }
            if(stockouts[i]){
              if(stockouts[i].type1List && stockouts[i].type1List.length > 0){
                var type1Obj = {};
                for(var j in stockouts[i].type1List){
                  var itemT1 = stockouts[i].type1List[j];
                  if(!type1Obj[itemT1.cellCode]){
                    type1Obj[itemT1.cellCode] = 0;
                  }
                  type1Obj[itemT1.cellCode] = type1Obj[itemT1.cellCode] + itemT1.quantity - itemT1.allotQuantity - itemT1.freezeQuantity;
                }
                var type1Arr = [];
                for(var k in type1Obj){
                  if(type1Obj[k] > 0){
                    type1Arr.push({'cellCode': k,'quantity': type1Obj[k]});
                  }
                }
                stockouts[i].type1List = type1Arr;
              }
              if(stockouts[i].type2List && stockouts[i].type2List.length > 0){
                var type2Obj = {};
                for(var j in stockouts[i].type2List){
                  var itemT2 = stockouts[i].type2List[j];
                  if(!type2Obj[itemT2.cellCode]){
                    type2Obj[itemT2.cellCode] = 0;
                  }
                  type2Obj[itemT2.cellCode] = type2Obj[itemT2.cellCode] + itemT2.quantity - itemT2.allotQuantity - itemT2.freezeQuantity;
                }
                var type2Arr = [];
                for(var k in type2Obj){
                  if(type2Obj[k] > 0){
                    type2Arr.push({'cellCode': k,'quantity': type2Obj[k]});
                  }
                }
                stockouts[i].type2List = type2Arr;
              }
            }
            result = result.concat(stockouts[i]);
          }
          // 显示排序
          result.sort((a, b)=>{
            if(a.orderQuantity > b.orderQuantity) return -1;
            if(a.orderQuantity < b.orderQuantity) return 1;
            if(a.itemCode > b.itemCode) return 1;
            if(a.itemCode < b.itemCode) return -1;
            return 0;
          })
          // 完成
          that.stockouts = result;
        } else {
          IOT.tips(ret.message || '查询补货提醒失败！', 'error');
        }
      }, null,null,()=>{
        IOT.hideOverlay();
      });
      setTimeout(() => {
        IOT.hideOverlay();
      }, 2000);
    },
  },
  created() {
    setTimeout(function () {
      var domFocus = document.getElementById('billNo');
      domFocus.focus();
    }, 100);
  },
  destroyed() {
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
</style>
