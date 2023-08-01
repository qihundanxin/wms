<template>

  <div class="acceptance">
    <div id="inside">
      <div><span id="billin">发运方式装箱</span></div>
      <hr/>
      <div>
        <div style="width:80%">
          <el-card class="box-card">
            <el-form :inline="true" class="demo-form-inline">
              <el-form-item label="承运商">
                <el-select v-model="shipBox.shipCode" placeholder="请选择承运商">
                  <el-option
                      v-for="carrier in carriers"
                      :key="carrier.carrierCode"
                      :label="carrier.carrierName"
                      :value="carrier.carrierCode">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="数量">
                <el-input v-model="list.length" placeholder="数量"></el-input>
              </el-form-item>
              <el-form-item label="重量">
                <el-input v-model="shipBox.weight" placeholder="重量"></el-input>
              </el-form-item>
              <br/>
              <el-form-item label="长">
                <el-input v-model="shipBox.length" placeholder="长"></el-input>
              </el-form-item>
              <el-form-item label="宽">
                <el-input v-model="shipBox.width" placeholder="宽"></el-input>
              </el-form-item>
              <el-form-item label="高">
                <el-input v-model="shipBox.height" placeholder="高"></el-input>
              </el-form-item>
              <el-form-item label="寄送地址">
                <el-input v-model="shipBox.address" placeholder="高"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="save">提交</el-button>
              </el-form-item>
            </el-form>
          </el-card>
          <el-card class="box-card">
            <h2 style="text-align: center;margin-bottom: 20px;">装箱明细</h2>
            <div class="tableBox" style="height: 500px; overflow: auto">
              <el-tag
                  closable
                  style="margin-left:10px;margin-top:6px"
                  v-for="item in list"
                  :key="item"
                  @close="deleteBillCode(item)"
              >
                {{ item }}
              </el-tag>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- <div class="createFootBox clearFloat"
         :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
        <div class="leftFloat">
            <el-button type="info" @click="$router.go(-1)">返 回</el-button>
        </div>
        <div class="rightFloat">
            <el-button @click="commitForm()" type="primary">清空数据</el-button>
        </div>
    </div> -->

  </div>
</template>

<script>
import pako from 'pako';
import {mapState} from "vuex";

export default {
  name: 'seeding',
  data() {
    return {
      shipBillCode: '',
      list: [],
      shipBox: {
        shipCode: '',
        shipBoxCode: '',
        length: '',
        weight: '',
        height: '',
        width: '',
        address: '',
        wareId: PF.getLocal('wareId', 'number'),
        wareName: localStorage.getItem('wareName'),
      },
      carriers: [],
      seedingWallIndex: '',
      cellIndex: 0,
      itemCode: '请扫描',
      quantity: 0,
      billNo: '',
      htmlStr: '',
      formLabelWidth: '80px',
      formData: {
        waveCode: '',
        seedingWallNo: '',
        scanData: '',
        itemCode: '',
        inventoryId: ''
      },
      dialogHoleInfoVisible: false,
      dialogSoSelVisible: false,
      dialogQuantityVisible: false,
      holeInfo: {
        holeId: '',
        soNo: ''
      },
      soBills: [],
      seedingWalls: [],
      seedingWall: {},
      pickTasks: [],
      loading: false,
      cellIds: [],
      updateInfo: {
        soNo: '',
        itemCode: '',
        quantity: null,
        inventoryId: null
      }

    }
  },
  computed: {
    ...mapState([]),
    isCollapse() {
      return this.$parent.leftMenuIsCollapse;
    },
  },
  methods: {
    save() {
      var that = this;
      IOT.showOverlay('保存中，请稍等...');

      this.shipBox.packNo = this.list.length
      // 获取到快递名称
      let name = this.getShipName(this.carriers, this.shipBox.shipCode)
      this.shipBox.shipName = name
      console.log(this.shipBox)

      IOT.getServerData('/ship/boxs/insert', 'post', {list: this.list, shipBox: this.shipBox}, (ret) => {
        IOT.hideOverlay();
        IOT.tips('保存成功！', 'success');
        this.$router.push({name: 'LoadMaster'})
        if (ret.code === 200) {

        } else {
          IOT.tips(ret.message || 'server error', 'error')
        }
      })
    },
    deleteBillCode(data) {
      for (var i = 0; i < this.list.length; i++) {
        if (this.list[i] == data) {
          this.list.splice(i, 1);
        }
      }

    },

    scanCode(data) {
      for (var i = 0; i < this.list.length; i++) {
        if (this.list[i] == data) {
          this.$parent.playMusic("wms快递单号重复");
          this.$notify({
            title: '警告',
            message: '单号重复（' + data + '）',
            type: 'warning'
          });
          return;
        }
      }

      this.list.push(data);
    },
    jyCode(shipBillNo) {
      var that = this;
      IOT.getServerData('/ship/boxs/insert', 'post', {list: this.list, shipBox: this.shipBox}, (ret) => {
        IOT.hideOverlay();
        IOT.tips('保存成功！', 'success');
        this.$router.push({name: 'LoadMaster'})
        if (ret.code === 200) {

        } else {
          IOT.tips(ret.message || 'server error', 'error')
        }
      })
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

        if (e.key == "Enter") {
          lastCode = null;
          lastTime = null;
          that.scanCode(code);

        } else if (lastCode == null && lastTime == null) {
          code = "";
          code += e.key;
          lastCode = nextCode;
          lastTime = nextTime;

        } else if (lastCode != null && lastTime != null && nextTime - lastTime > 500) {
          code = "";
          lastCode = nextCode;
          lastTime = nextTime;

        } else if (lastCode != null && lastTime != null && nextTime - lastTime <= 500) {// 扫码枪
          code += e.key;
          lastCode = nextCode;
          lastTime = nextTime;
        }


      }
    },
    handleCurrentChange(val) {
    },
    commitForm(formName) {
    },
    resetForm(formName) { // 重置
      this.$refs[formName].resetFields();
    },
    handleCloseHoleInfo() {
      this.dialogHoleInfoVisible = false
    },
    showSoSelModal() {
      //  this.getSoData()
      this.dialogSoSelVisible = true
    },
    handleCloseSoSel() {
      this.dialogSoSelVisible = false
    },
    handleSoSelChange(soNo) {
      this.dialogSoSelVisible = false
      this.holeInfo.soNo = soNo
    },
    handleCloseQuantity() {
      this.dialogQuantityVisible = false
    },
    commitChangeHoleInfo() {
      PF.setLocal(this.holeInfo.holeId, this.holeInfo.soNo)
      this.dialogHoleInfoVisible = false

      this.displayHoleLabel()
    },
    getShipName(data, code) {
      let name = ''
      for (let i = 0; i < data.length; i++) {
        if (data[i].carrierCode == code) {
          name = data[i].carrierName
          break
        }
      }
      return name
    }
  },
  created() {
    PF.getCarriers((rows) => {
      this.carriers = rows;
    });

    this.getCode();

  },
  mounted() {

  },
  destoryed() {
    document.onkeypress = null;
  }
}
</script>

<style scoped lang="less">
.acceptance {
  .box-card {
    border-color: #c1deff;
    border-width: 1mm;
    margin-bottom: 10px;
  }

  padding-bottom: 100px;

  #billin {
    font-size: 30px;
    color: #4499FF;
  }

  .div-inline1 {
    margin-top: 5px;
    width: 35%;
  }

  .div-inline-right {
    float: left;
    margin-top: 5px;
    margin-left: 10px;
  }
}
</style>
