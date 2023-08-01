<template>
  <div class="ydyp">
    <!-- <audio id="myaudio" controls="controls" hidden src="../../../assets/mp3/wms称重成功.mp3" ref="audio"></audio> -->
    <h1>一单一品打包工位</h1>
    <div class="header">
      <el-card>
        <el-form :inline="true">
          <el-form-item label="CODE">
            <el-input v-model="code" placeholder="CODE"></el-input>
          </el-form-item>
          <el-form-item label="自动打印">
            <el-switch
                v-model="autoPrint"
                active-color="#13ce66"
            >
            </el-switch>
          </el-form-item>
          <el-form-item style="margin-left:50px" label="容器号/波次号">
            <el-input size="small" v-model="boxCode" placeholder="容器号/波次号"></el-input>
          </el-form-item>
          <el-form-item label=" ">
            <el-button size="small" @click="getYdYp();" type="primary">确定</el-button>
            <el-button size="small" v-if="wave.waveId!=null" @click="getPickTaskReturns();exTableVisible=true"
                       type="danger">异常上架
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div class="main">
      <div id="left" class="tableBox">
        <el-card>
          <el-table
              @current-change="handleSelChange"
              :data="bills"
              style="width: 100%">
            <el-table-column
                prop="billNo"
                label="订单号"
                min-width="300">
              <template slot-scope="scope">
                <el-row :gutter="24">
                  <el-col :span="6">
                    <div style="display: inline-block;   width:70px;height:70px" v-if="scope.row.imgUrl">
                      <img :src="scope.row.imgUrl" style="width:70px;height:70px">
                    </div>
                  </el-col>
                  <el-col :span="16">
                    <div  
                         style="display: inline-block;margin-left:8px; height:70px">
                      <span>{{ scope.row.billNo }}</span>
                      <br>
                      <span>{{ scope.row.itemCode }}</span>
                      <br>
                      <span>{{ scope.row.itemName }}</span>
                      <br>
                      <span style="color:#409EFF" v-if="scope.row.state==6">拣货完成</span>
                      <span style="color:#E6A23C" v-if="scope.row.state==7">复核完成</span>
                      <span style="color:#67C23A" v-if="scope.row.state==8">发运完成</span>
                      <span style="color:#909399" v-if="scope.row.state==-1">取消</span>
                    </div>
                  </el-col>
                </el-row>
                <!--<span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>-->
              </template>
            </el-table-column>
            <el-table-column
                prop="quantity"
                min-width="180"
                label="数量">
              <template slot-scope="scope">
                <span v-if="scope.row.state<8"
                      style="color:#E6A23C;font-size:30px">{{ scope.row.checkQuantity }} </span>
                <span v-if="scope.row.state==8"
                      style="color:#67C23A;font-size:30px">{{ scope.row.checkQuantity }} </span>
                <span style=" font-size:30px;margin-left:10px;margin-right:10px">/ </span>
                <span style="color:#409EFF;font-size:30px">{{ scope.row.orderQuantity }} </span>
              </template>
            </el-table-column>
            <el-table-column
                prop="quantity"
                min-width="180"
                label="操作">
              <template slot-scope="scope">
                <el-button size="mini" @click="printDzmd(scope.row.billNo)" type="primary">打印</el-button>
                <el-button size="mini" @click="cancelSo(scope.row.soMasterId)" type="danger">退回</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <div id="right" class="tableBox">
        <el-card>
          <el-form v-show="billInfo.billNo!=undefined" ref="form">
            <el-form-item label="单号" prop="billNo" :label-width="formLabelWidth">
              <span style="font-size:20px;color:#000000;">{{ billInfo.billNo }}</span>
              <!-- <el-input  disabled v-model="billInfo.billNo"  placeholder=" " clearable style="width: 90%;"></el-input> -->
            </el-form-item>
            <el-form-item label="运单号" prop="shipBillCode" :label-width="formLabelWidth">
              <span style="font-size:20px;color:#000000;">{{ billInfo.shipBillCode }}</span>
              <!-- <el-input  disabled v-model="billInfo.billNo"  placeholder=" " clearable style="width: 90%;"></el-input> -->
            </el-form-item>
            <el-form-item label="状态" prop="state" :label-width="formLabelWidth">
              <el-tag v-if="billInfo.state==6">拣货完成</el-tag>
              <el-tag v-if="billInfo.state==7" type="warning">成功复核</el-tag>
              <el-tag v-if="billInfo.state==8" type="success">成功发运</el-tag>
              <!-- <el-input  disabled v-model="billInfo.state"  placeholder=" " clearable style="width: 90%;"></el-input> -->
            </el-form-item>
            <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
              <span style="font-size:20px;color:#000000;">{{ billInfo.itemCode }}</span>
              <!-- <el-input disabled  v-model="billInfo.itemCode"  placeholder=" " clearable style="width: 90%;"></el-input> -->
            </el-form-item>
            <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
              <span style="font-size:20px;color:#000000;">{{ billInfo.itemName }}</span>
              <!-- <el-input v-model="billInfo.itemName" placeholder="请输入SKU名称" disabled clearable style="width: 90%;"></el-input> -->
            </el-form-item>
            <el-form-item label="图片" prop="batchId" :label-width="formLabelWidth" v-if="billInfo.imgUrl">
              <el-image
                  style="width: 100px; height: 100px"
                  :src="billInfo.imgUrl"
                  :preview-src-list="[billInfo.imgUrl]">
              </el-image>
            </el-form-item>
            <el-form-item label="订单数量" :label-width="formLabelWidth">

              <span v-if="billInfo.orderQuantity>billInfo.checkQuantity"
                    style="font-size:50px;color:#E6A23C;">{{ billInfo.orderQuantity }}</span>

              <span v-if="billInfo.orderQuantity==billInfo.checkQuantity"
                    style="font-size:50px;color:#67C23A;">{{ billInfo.orderQuantity }}</span>
              <el-progress :text-inside="true" :stroke-width="20"
                           :percentage="billInfo.checkQuantity/billInfo.orderQuantity*100"
                           status="success"></el-progress>
            </el-form-item>

            <el-form-item label="扫描数量" :label-width="formLabelWidth">
              <el-input-number @change="checkQuantityChange" @focus="input0Set" :max="billInfo.orderQuantity"
                               v-model="billInfo.checkQuantity" controls-position="right" :min="0"
                               size="small"
                               style="width: 90%;"></el-input-number>
            </el-form-item>

            <el-form-item label="重量" :label-width="formLabelWidth">
              <span style="font-size:50px;color:#67C23A;">{{ billInfo.weight }}</span>


            </el-form-item>

          </el-form>
        </el-card>
      </div>
    </div>
    <el-dialog title="还货任务" :visible.sync="exTableVisible">
      <el-dialog
          width="30%"
          title="新增"
          :visible.sync="addExTableVisible"
          append-to-body>
        <el-form :model="addReturn">
          <el-form-item label="SKU" :label-width="formLabelWidth">
            <el-input v-model="addReturn.itemCode" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="数量" :label-width="formLabelWidth">
            <el-input v-model="addReturn.quantity" autocomplete="off"></el-input>
          </el-form-item>


        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addReturnTask">确 定</el-button>
        </span>
      </el-dialog>

      <el-button type="primary" @click="addExTableVisible = true">新增</el-button>

      <el-table style="margin-top:10px" :data="pickTaskReturns">
        <el-table-column property="itemCode" label="SKU" width="150"></el-table-column>
        <el-table-column property="itemName" label="名字" width="200"></el-table-column>
        <el-table-column property="quantity" label="数量"></el-table-column>
        <el-table-column property="toCellCode" label="货位"></el-table-column>
        <el-table-column property="type" label="类型"></el-table-column>
      </el-table>

    </el-dialog>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'Check',
  data() {
    let validateQuantityNo0 = (rule, value, callback) => {
      if (value > 0) {
        callback();
      } else {
        callback(new Error('数量不得为0!'));
      }
    };
    return {
      wave: {},
      bills: [],
      autoPrint: true,
      pickTaskReturns: [],
      boxCode: '',
      addReturn: {},
      addExTableVisible: false,
      exTableVisible: false,
      code: '',
      wareId: PF.getLocal('wareId', 'number'),
      itemCode: '',
      waveId: '',
      quantity: 0,
      billInfo: {
        checkQuantity: 1,
        orderQuantity: 1
      },
      loading: {
        boxTable: false,
        pickTaskTable: false,
      },
      searchData: {
        pageNum: 1,
        pageSize: 5,
        total: null,
        wareId: PF.getLocal('wareId', 'number'),
        state: 4,
        billNo: 'SO-49',
      },
      searchData2: {
        pageNum: 1,
        pageSize: 5,
        total: null,
        wareId: PF.getLocal('wareId', 'number'),
        boxNo: '',
      },

      formLabelWidth: '150px',

      pickTasks: [],
      boxDetails: [],
      packDetails: [],

      form: {
        soMasterId: null,
        soDetailId: null,
        billNo: '',
        boxNo: '',
        itemCode: '',
        itemName: '',
        batchs: '',
        batchId: '',
        batchName: '',
        quantity: 0,
        pickTaskId: null,
        pickQuantity: 0,
        packDetailId: null,
        packDescribe: '',
        memo: '',
      },
      rules: {
        itemCode: [
          {required: true, message: '请输入SKU', trigger: 'blur'}
        ],
        batchId: [
          {required: true, message: '请选择批次', trigger: 'blur'}
        ],
        quantity: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {validator: validateQuantityNo0, trigger: 'blur'}
        ],
        packDetailId: [
          {required: true, message: '请选择单位', trigger: 'blur'}
        ],
      },
    }
  },
  computed: {
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
    isCheckBox() {
      return PF.isNull(this.form.boxNo)
    },
  },
  methods: {
    cancelSo(soMasterId) {
      //Integer waveMasterId, Integer soMasterId, Integer wareId
      var param = {
        wareId: this.wareId,
        waveMasterId: this.wave.waveId,
        soMasterId: soMasterId
      };
      IOT.showOverlay('保存中，请稍等...');
      IOT.getServerData('/pick/task/returns/cancelSo', 'get', param, (ret) => {
        IOT.hideOverlay();
        this.loading = false;
        this.soDetailsVisible = false
        if (ret.code === 200) {
          for (var i = 0; i < this.bills.length; i++) {
            if (this.bills[i].soMasterId == soMasterId) {
              this.bills[i].state = -1;
            }
          }
        } else {
          this.loading = false
          IOT.tips(ret.message || 'server error', 'error')
        }
      })

    },
    handleSelChange(rowData) {
      if (rowData != null && rowData.billNo != null) {
        this.billInfo = rowData;
      }
    },
    addBillQuantity(billNo) {
      this.bills.forEach(bill => {
        if (billNo == bill.billNo) {
          bill.checkQuantity += 1;
        }
      });
    },
    getYdYp() {
      var that = this;
      IOT.getServerData('/so/masters/getSeedingYdYpByBox', 'get', {
        boxCode: that.boxCode,
        wareId: that.wareId
      }, (ret) => {
        if (ret.code === 200) {
          that.bills = ret.data.bills;
          that.wave = ret.data.wave;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);

    },
    addReturnTask() {
      var that = this;
      var param = {
        waveMasterId: that.wave.waveId,
        boxCode: that.boxCode,
        wareId: that.wareId,
        itemCode: that.addReturn.itemCode,
        quantity: that.addReturn.quantity
      };
      IOT.showOverlay('保存中，请稍等...');
      //Integer waveMasterId,String boxCode,String itemCode,Integer wareId,Double quantity,
      IOT.getServerData('/pick/task/returns/create', 'get', param, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          that.addReturn = {};
          that.getPickTaskReturns();
          that.addExTableVisible = false;
          // that.pickTaskReturns= ret.data;

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);

    },
    getPickTaskReturns() {
      var that = this;
      IOT.getServerData('/pick/task/returns/getByBox', 'get', {boxCode: that.boxCode, wareId: that.wareId}, (ret) => {
        if (ret.code === 200) {
          that.pickTaskReturns = ret.data;

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    checkQuantityChange() {
      var that = this;
      if (that.billInfo.orderQuantity == that.billInfo.checkQuantity) {
        that.checkBill(that.billInfo.billNo);
      }
    },
    sendOut(billNo, wareId, weight) {
      IOT.showOverlay('保存中，请稍等...');
      IOT.getServerData('/so/masters/sendOut', 'get', {
        billNo: billNo,
        wareId: wareId,
        weight: weight,
        autoPrint: false
        // autoPrint:this.autoPrint

      }, (ret) => {
        IOT.hideOverlay();
        this.loading.pickTaskTable = false;
        if (ret.code === 200) {
          this.billInfo.state = 8;

          for (var i = 0; i < this.bills.length; i++) {
            if (this.bills[i].billNo == billNo) {
              this.bills[i].state = 8;
            }
          }
          var strHTML = ret.data;
          if (this.autoPrint) {
            // this.printDzmd(billNo);
          }

          this.$notify({
            title: '成功发运！',
            message: this.billInfo.billNo,
            type: 'success'
          });

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    printDzmd(billNo) {
      IOT.showOverlay('处理中，请稍等...');
      var that = this;
      IOT.getServerData('/so/masters/getDzmd', 'get', {billNo: billNo}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          var strHTML = ret.data;
          that.$parent.printHtml(strHTML, null,);

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },
    checkBill(billNo) {
      var that = this;
      IOT.getServerData('/so/masters/check', 'get', {billNo: billNo}, (ret) => {
        if (ret.code === 200) {
          that.billInfo.state = 7;
          /**
           * 这里应该要打标签
           */
          //  var urlStr = 'http://lazerp2.oss-cn-hangzhou.aliyuncs.com/label/ztoth/77130044426681.pdf';
          //  that.$parent.printUrl(urlStr);
          that.printDzmd(billNo);
          this.getYdYp();
          this.$notify({
            title: '订单复核',
            message: that.billInfo.billNo,
            type: 'success'
          });


        } else {
          IOT.tips(ret.message || 'server error', 'error')
        }
      })
    },
    input0Set(e) {
      if (e.currentTarget.value == '0') {
        e.currentTarget.ariaValueNow = '';
        e.currentTarget.ariaValueText = '';
        e.currentTarget.value = '';
      }
    },
    getBillInfo() {
      var that = this;
      var param = {
        itemCode: that.itemCode,
        wareId: this.wareId
      };
    },
    scanItemCode(itemCode, noCZ) {

      var that = this;
      that.code = '';

      var billNo = that.billInfo.billNo;
      var state = that.billInfo.state;
      var waveId = that.wave.waveId;
      var boxCode = that.boxCode;
      if (boxCode != '000' && (waveId == undefined || waveId == null || waveId == NaN)) {
        return
      }

      var list = itemCode.split('.');
      //称重
      if (billNo != undefined && state == 7 & !noCZ) {
        if (that.billInfo.orderQuantity > that.billInfo.checkQuantity) {
          that.itemCode = "";
          this.$notify({
            title: '警告',
            message: '请扫描完该订单全部商品之后再进行称重！',
            type: 'warning'
          });
        } else if (that.billInfo.state != 7) {
          that.itemCode = "";
          this.$notify({
            title: '警告',
            message: '该订单没有成功复核！',
            type: 'warning'
          });
        } else if (that.billInfo.orderQuantity = that.billInfo.checkQuantity) {
          that.billInfo.weight = itemCode;
          that.sendOut(that.billInfo.billNo, that.wareId, itemCode);
        }
      }

      //当前订单已复核，进行下一个
      else if ((that.itemCode != itemCode || state == 8)) {
        that.billInfo = {
          checkQuantity: 1,
          orderQuantity: 1
        };
        that.itemCode = itemCode;
        var param = {
          itemCode: itemCode,
          wareId: this.wareId,
          waveId: waveId
        };
        IOT.getServerData('/so/masters/getYdypByItemCode', 'get', param, (ret) => {
          if (ret.code === 200) {
            if (ret.data == null) {
              that.itemCode = "";
              that.$parent.playMusic("wms未匹配到订单");
              this.$notify({
                title: '警告',
                message: '没有匹配到订单',
                type: 'warning'
              });
            } else {

              if (ret.data.state == 7) {
                this.billInfo = ret.data;
                ret.data.checkQuantity = orderQuantity;
                that.billInfo.checkQuantity == that.billInfo.orderQuantity;
              } else {
                ret.data.checkQuantity = 1;
                this.addBillQuantity(ret.data.billNo);
                ret.data.weight = "";
                this.billInfo = ret.data;
                this.$notify({
                  title: '匹配订单',
                  message: this.billInfo.billNo,
                  type: 'success'
                });
                if (that.billInfo.checkQuantity == that.billInfo.orderQuantity) {
                  this.billInfo.state = 7;
                  that.checkBill(that.billInfo.billNo);
                }
              }

            }

          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      }
      //进行下一次扫描
      else if (billNo != undefined && that.billInfo.itemCode == itemCode && list.length != 2) {
        if (that.billInfo.checkQuantity == that.billInfo.orderQuantity) {
          that.itemCode = "";
          this.$notify({
            title: '警告',
            message: '该订单已复核！',
            type: 'warning'
          });
        } else {

          that.billInfo.checkQuantity += 1;
          this.addBillQuantity(billNo);
          if (that.billInfo.checkQuantity == that.billInfo.orderQuantity) {
            that.checkBill(that.billInfo.billNo);
          }

        }

      }
    },
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
          if (e.key == "Enter") {
            //这里监听正常的键盘回车键
            that.scanItemCode(that.code, true);
            code = '';
            lastCode = null;
            lastTime = null;
          } else {
            code = e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }
        }
        //间隔大于500毫秒的是键盘输入的
        else if (lastCode != null && lastTime != null && nextTime - lastTime > 500) {

          if (e.key == "Enter") {
            //这里监听正常的键盘回车键
            that.scanItemCode(that.code, true);
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
            that.scanItemCode(code, false);
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
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getPickTaskData()
    },
    handleCurrentChange2(val) {
      this.pageNum = val;
      this.getBoxDetailData()
    },
    clearSearchData() {
      this.searchData.billNo = '';
    },
    checkBoxNo() {
      this.form.boxNo = this.searchData2.boxNo;
    },
    clearBoxNo() {
      this.form.boxNo = '';
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
    },
  },
  created() {
    this.getCode();
  }
}
</script>

<style scoped lang="less">

.el-card__body {
  padding: 0;
}

.ydyp {
  padding-bottom: 100px;

  .header {
    height: auto;
  }

  .main {
    display: flex;
    justify-content: space-around;
    margin-top: 10px;
  }

  #left, #right {
    width: 50%;
    height: 600px;
    overflow: auto;
  }
}
</style>
