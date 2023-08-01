<template>
  <div v-loading="loading" class="baseUpCell">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div style="text-align: center;margin-top:10px">
      <div v-if="inventory == null && inventorys.length > 0">请选择要上架的库存</div>
      <div v-else-if="inventorys.length == 0" class="scanInput">
        <el-input clearable placeholder="UPC" id="upc" v-model="upc"></el-input>
        <i class="el-icon-camera" style="font-size:30px" @click="scan(1)"></i>
      </div>
      <br />
      <div v-if="inventory != null" class="scanInput">
        <el-input placeholder="货位号" clearable id="cellCode" v-model="cellCode"></el-input>
        <i class="el-icon-camera" style="font-size:30px" @click="scan(2)"></i>
      </div>
      <br />
      <el-input v-if="inventory != null" placeholder="数量" clearable id="quantity" v-model="quantity"></el-input>
      <button @click="getInventory(inven)" v-for="(inven, index) in inventorys"
        style="  width:94%;   margin-top:10px ;margin-left:3%">
        <el-row>
          <el-col :span="9"> <el-image style="width: 80px; height: 80px" :src="inven.imgUrl"></el-image>
          </el-col>
          <el-col :span="15">
            <div style="text-align:left;padding-left:5px;padding-top:0px;font-size:15px">
              <div>
                <span>SKU：</span>
                <span>{{ inven.itemCode }} </span>
              </div>
              <div>
                <span>UPC：</span>
                <span>{{ inven.upc }} </span>
              </div>
              <div>
                <span>批次：</span>
                <span>{{ inven.batchName }} </span>
              </div>
              <div>
                <span>数量：</span>
                <span>{{ inven.quantity / inven.transRatio }} {{ inven.packDescribe }}</span>
              </div>
              <div>
                <span>跟踪号：</span>
                <span>{{ inven.boxCode }} </span>
              </div>
            </div>
          </el-col>
        </el-row>
        <br />
        <div v-if="inventory != null && inventory.inventoryId == inven.inventoryId">
          <el-button @click="add" style="width:200px;height:50px" type="primary">提交</el-button>
          <br />
          <br />
          <el-button @click="clear" style="width:200px;height:50px" type="info">清空</el-button>
        </div>
      </button>
      <br />
      <br />
    </div>
    <div style="text-align: center;margin-top:30px">
      <span v-if="memo.type == 3" style="font-size:20px;color:#67C23A;font-weight:bold;">{{ memo.msg }}</span>
      <span v-else-if="memo.type == -1" style="font-size:20px;color:red;font-weight:bold;">{{ memo.msg }}</span>
      <span v-else-if="memo.type != 3" style="font-size:20px;color:#409EFF;font-weight:bold;">{{ memo.msg }}</span>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "BaseUpCell",
  data() {
    return {
      scanResult: '',
      wareId: PF.getLocal('wareId', 'number'),
      quantity: '',
      inventory: null,
      headerTitle: "UPC上架",
      formLabelWidth: '150px',
      loading: false,
      noMore: false,
      upc: '',
      cellCode: '',
      inventoryId: '',
      memo: {
        code: 1,
        msg: '请扫描UPC码'
      },
      inventorys: [],
    };
  },
  watch: {
    inventoryId: function (newVal, oldVal) {
      if (newVal.trim().length >= 4) {
        this.getInventory();
      }
    },
    upc: function (newVal, oldVal) {
      let qrObject = PF.simpleQrEncode(newVal);
      if (qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
        this.upc = qrObject.index2;
        return;
      }
      setTimeout(() => {
        if(this.upc == newVal && newVal !=null && newVal !=""){
          this.getInventorys(newVal);
        }
      }, 200);
    },
  },
  methods: {
    scan(scanType) {
      SCAN.startRecognize(
        (type, result) => {
          if (scanType == 1) {
            this.upc = result;
          }
          else if (scanType == 2) {
            this.cellCode = result;
          }
          SCAN.close();
        });
    },
    getInventorys(newValue) {
      var that = this;
      var upc = that.upc;
      if (newValue != upc) {
        return;
      }
      this.loading = true;
      IOT.getServerData('/inventorys/getUPCUpList', 'get', { upc: upc, wareId: this.wareId }, (ret) => {
        this.loading = false;
        if (ret.code === 200) {
          this.inventorys = ret.data.list;
          for(let i=0;i<this.inventorys.length;i++){
            if(this.inventorys[i].upc==null && this.inventorys[i].upcs.length>0){
              this.inventorys[i].upc= this.inventorys[i].upcs[0].upc;
            }
          }
          console.log(ret.data.list.length);
          if (ret.data.list.length > 0) {
            this.upc = "";
            if (ret.data.list.length == 1) {
              this.getInventory(ret.data.list[0]);
            }
          } else {
            this.upc = "";
            this.$message.error('该UPC码无效！');
            that.memo = {
              code: 1,
              msg: '请扫描UPC码'
            }
          }
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true)
    },
    getInventory(inven) {
      var that = this;
      this.inventory = inven;
      for (var i = 0; i < this.inventorys.length; i++) {
        var element = this.inventorys[i];
        if (element.inventoryId == inven.inventoryId) {
          this.inventorys.unshift(this.inventorys.splice(i, 1)[0]);
        }
      }
      setTimeout(function () {
        var domFocus = document.getElementById("cellCode");
        domFocus.focus();
      }, 100);
    },
    clear() {
      this.cellCode ='';
      this.quantity = '';
    },
    siCcw() {
      var that = this;
      this.loading = true;
      IOT.getServerData('/sjc/upper/tasks/zjUp', 'get', data, (ret) => {
        this.loading = false;
        if (ret.code === 200) {
          that.upc = "";
          that.cellCode = "";
          that.quantity = "";
          that.inventory = null;
          that.memo = {
            type: 3,
            msg: '成功,请继续下一次操作，扫描商品标签库存码'
          }
          var domFocus = document.getElementById("upc");
          domFocus.focus();
        } else {
          that.memo = {
            type: -1,
            msg: ret.message
          }
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      })
    },
    add() {
      var that = this;
      var quantity = parseInt(that.quantity.trim());
      var inventoryId = parseInt(that.inventoryId.trim());
      //  if(that.inventoryId.length<1){
      //     this.$message.error('请扫描库存码！'); 
      //   return;
      // }
      if (that.cellCode.length < 1) {
        this.$message.error('请扫描货位号');
        return;
      }
      if (that.quantity.length < 1) {
        this.$message.error('请输入数量');
        return;
      }
      if (quantity > that.inventory.quantity) {
        this.$message.error('请校验数量！');
        return;
      }
      if (quantity > 0) {
      } else {
        this.quantity = "";
        this.$message.error('输入数量无效！');
        return;
      }
      let data = {
        wareId: PF.getLocal('wareId', 'number'),
        InventoryId: that.inventory.inventoryId,
        toCellCode: that.cellCode.trim(),
        quantity: quantity,
      }
      IOT.getServerData('/sjc/upper/tasks/zjUp', 'get', data, (ret) => {
        if (ret.code === 200) {
          that.inventoryId = "";
          that.cellCode = "";
          that.quantity = "";
          that.inventory = null;
          that.inventorys = [];
          that.memo = {
            type: 3,
            msg: '成功,请继续下一次操作，扫描商品标签库存码'
          }
          var domFocus = document.getElementById("upc");
          domFocus.focus();

        } else {
          that.memo = {
            type: -1,
            msg: ret.message
          }
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      })
    },
    //分析数据
    scanItemCode(code) {
      var that = this;
      if (that.inventoryId == '' || that.memo.type == 1) {
        that.inventoryId = code;
        that.memo = {
          type: 2,
          msg: '请扫描货位号'
        }
      } else if (that.cellCode == '' || that.memo.type == 2) {
        that.cellCode = code;
        that.add();
      } else if (that.memo.type == 3) {
        that.inventoryId = code;
        that.cellCode = '',
          that.memo = {
            type: 2,
            msg: '请扫描上架车货位号'
          }
      }
    },
    //得到数据
    getCode() {
      var that = this;
      var code = "";
      var lastTime, nextTime;
      var lastCode, nextCode;
      document.onkeypress = function (e) {
        nextCode = e.which;
        nextTime = new Date().getTime();
        var now = new Date().getTime();
        if (lastCode == null && lastTime == null) {
          //监听正常键盘回车事件
          if (e.key == "Enter") {
            code = '';
            lastCode = null;
            lastTime = null;
          }
          //
          else {
            code = e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }
        }
        else if (lastCode != null && lastTime != null && nextTime - lastTime > 500) {
          //监听正常键盘回车事件
          if (e.key == "Enter") {
            code = '';
            lastCode = null;
            lastTime = null;
          } else {
            code = e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }
        }
        //间隔小于500毫秒的是扫码或者称重进来的
        else if (lastCode != null && lastTime != null && nextTime - lastTime <= 500) {// 扫码枪
          if (e.key == "Enter") {
            //这里监听扫码器或者称重设备的回车键
            that.scanItemCode(code);
            code = '';
            lastCode = null;
            lastTime = null;
          } else {
            code += e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }
        }
      }
    }
  },
  created() {
    // this.getCode();
    setTimeout(function () {
      var domFocus = document.getElementById("upc");
      domFocus.focus();
    }, 100);
  },
  components: {
    FeaturesHeader,
  }
};
</script>
<style lang="less" scoped>

</style>
