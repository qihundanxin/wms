<template>
  <div class="bjbf-batch"><!-- 本质是一码一拣 -->
    <FeaturesHeader headerTitle="波次拣货" />
    <el-input v-if="!useGlobalScan" id="input" v-model="input"
      :placeholder="input ? input : !cellCode ? '请扫描货位' : '请扫描SKU码（扫描后立刻提交）'"></el-input>
    <el-alert v-if="useGlobalScan" :title="input ? input : !cellCode ? '请扫描货位' : '请扫描SKU码（扫描后立刻提交）'" center type="info"
      :closable="false" style="margin: 0 20px; width: calc(100% - 40px);" />
    <div style="display: flex; margin: 0 20px; width: calc(100% - 40px); margin-top: 5px;">
      <el-alert :title="cellCode ? '当前货位：' + cellCode : '当前未选中货位'" center effect="dark"
        :type="cellCode ? 'info' : 'warning'" :closable="false" style="width: 100%;" />
      <el-button type="primary" @click="reCellCode" v-show="cellCode">切换</el-button>
    </div>
    <div class="sku-list noScrollbar">
      <div class="so-bill" v-for="scanned in scannedList" :key="scanned.soBillNo">
        <div class="bill-no">{{ scanned.soBillNo }}</div>
        <div class="sku-box" v-for="item in scanned.itemList" :key="item.itemCode">
          <div class="sku-img">
            <el-image style="width: 80px; height: 80px" :src="item.itemInfo.imgUrl"></el-image>
          </div>
          <div class="sku">
            <div>{{ item.itemCode }}</div>
            <div>货位：{{ item.cellCode }}</div>
            <div>已拣：{{ item.reviewQuantity }}</div>
          </div>
          <div class="sku-name">{{ item.itemInfo.itemName }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "BjbfBatch",
  data() {
    return {
      wareId: PF.getLocal('wareId', 'number'),
      useGlobalScan: true,// 是否使用全局扫码
      input: '',
      cellCode: '',
      scannedList: [],
    };
  },
  components: {
    FeaturesHeader,
  },
  computed: {

  },
  watch: {
    input(newValue, oldValue) {
      setTimeout(() => {
        if (newValue && newValue == this.input) {
          this.input = ''
          this.scan(newValue)
        }
      }, 200)
    }
  },
  methods: {
    /** 绑定键盘按下事件：全局模式下增加键盘监听；非全局模式下输入框获取焦点 */
    bindKeyDown() {
      if (this.useGlobalScan) {
        // 全局键盘事件绑定
        document.onkeydown = (e) => {
          if (e.key && e.key.length == 1) {
            this.onkeydown(e.key)
          }
        }
      } else {
        // 输入框方式输入
        document.getElementById('input').focus()
      }
    },
    onkeydown(key) {
      this.input = this.input + key
    },
    reCellCode() {
      if (this.cellCode) {
        this.$confirm('需要重新扫货位', '切换货位？', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(() => {
          this.cellCode = ''
          if (this.scannedList.length > 0) {
            this.$confirm('清空页面？', {
              confirmButtonText: '清空',
              cancelButtonText: '保留',
            }).then(() => {
              this.scannedList = []
            }).catch(() => { })
          }
        }).catch(() => { })
      }
    },
    /**
     * 页面核心操作：扫码得到了内容
     * @param {String} str 扫码得到的内容
     */
    scan(str) {
      // 扫码日志
      const scanLog = {
        hasSuccess: null,// 是否成功
        scanValue: str,// 码内容
        scanType: null,// 扫描方式
      }
      const saveScanLog = () => {
        IOT.getServerData('/scanLog/save', 'post', scanLog)
      }
      // 先扫描货位
      if (!this.cellCode) {
        IOT.getServerData('/cell/infos/byCellCodeOnly', 'get', { cellCode: str }, (ret) => {
          if (ret && ret.code == 200 && ret.data && ret.data.cellInfoList) {
            if (ret.data.cellInfoList.length == 1) {// 能够唯一确认货位
              if (ret.data.cellInfoList[0].wareId != this.wareId) {
                IOT.tips('已为您自动切换仓库' + (ret.data.cellInfoList[0].wareName ? '：' + ret.data.cellInfoList[0].wareName : ''), 'success', 5000)
                this.wareId = ret.data.cellInfoList[0].wareId
              }
              this.cellCode = ret.data.cellInfoList[0].cellCode
            } else if (ret.data.cellInfoList.length == 0) {
              IOT.tips('未找到您扫描的货位', 'error', 5000)
            } else {
              IOT.tips('WMS中存在重复的货位号，无法为您定位到仓库，请您手动切换仓库', 'error', 5000)
            }
          } else {
            IOT.tips('您扫描的不是货位码', 'error', 5000)
          }
        }, true)
        return
      }
      // 再扫描LabelItem
      let qrObject = PF.simpleQrEncode(str);
      if (qrObject && qrObject.index1 && qrObject.index2 && qrObject.index1.trim().length > 1 && qrObject.index2.trim().length > 1) {// 需要so和sku
        scanLog.scanType = 2 // 2=一码一拣
        const so = qrObject.index1.trim()
        const sku = qrObject.index2.trim()
        const cellCode = this.cellCode
        IOT.getServerData('/pick/tasks/pickTaskBjbfAffirmSo', 'post', {
          qrCode: str.trim(),
          soBillNo: so,
          itemCode: sku,
          cellCode: cellCode,
          reviewQuantity: 1
        }, (ret) => {
          if (ret.code === 200) {
            scanLog.hasSuccess = true
            let hasSo = false
            let hasItem = false
            let scannedList = JSON.parse(JSON.stringify(this.scannedList))
            for (const i in scannedList) {
              const scannedSo = scannedList[i];
              if (scannedSo.soBillNo == so) {// 匹到了订单
                hasSo = true
                for (const j in scannedSo.itemList) {
                  const item = scannedSo.itemList[j];
                  if (item.itemCode == sku) {// 匹配到了订单里的SKU
                    hasItem = true
                    item.reviewQuantity = item.reviewQuantity + 1
                  }
                }
                if (hasItem == false) {// 有订单没详情，新建详情
                  scannedSo.itemList = [...scannedSo.itemList, {
                    cellCode: cellCode,
                    itemCode: sku,
                    itemInfo: ret.data.itemInfo,
                    reviewQuantity: 1
                  }]
                }
              }
            }
            if (hasSo == false) {// 没有订单，新建订单
              scannedList = [...scannedList, {
                soBillNo: so,
                soMaster: ret.data.soMaster,
                itemList: [{
                  cellCode: cellCode,
                  itemCode: sku,
                  itemInfo: ret.data.itemInfo,
                  reviewQuantity: 1
                }]
              }]
            }
            this.scannedList = scannedList;
            // 称重提示
            const itemInfo = ret.data.itemInfo;
            if (itemInfo.weighting) {
              const itemNameArr = itemInfo.itemName ? itemInfo.itemName.split('|') : ['']
              const itemNameEnglish = itemNameArr[itemNameArr.length - 1]
              this.$alert('<h3>SKU: ' + itemInfo.itemCode + '</h3><h3>Item name: ' + itemNameEnglish + '</h3><p>This Product need to be photographed and weighed, please ensure that the photo is sent to customer service to modify the weight</p>', 'confirmation', {
                dangerouslyUseHTMLString: true,
                confirmButtonText: 'confirm',
                callback: () => { }
              });
            }
          } else {
            scanLog.hasSuccess = false
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error', 5000)
          }
          saveScanLog()
        }, true, null, () => {
          scanLog.hasSuccess = false
          saveScanLog()
        })
      } else {
        IOT.tips('此二维码无效，请扫描拣货标签上的码', 'error', 5000)
      }
      // 巩固扫码功能的可持续性
      setTimeout(() => {
        this.bindKeyDown()
      }, 500)
    }
  },
  mounted() {
    this.bindKeyDown()
  },
  destroyed() {
    document.onkeydown = () => { }
  }
};
</script>
<style lang="less" scoped>
.bjbf-batch {
  .sku-list {
    height: calc(100vh - 250px);
    overflow: scroll;

    .so-bill {
      width: 95%;
      margin: 10px auto 0 auto;
      box-shadow: 5px 5px 5px #CCC;
      border: 1px solid #ccc;

      .bill-no {
        font-size: large;
        font-weight: bold;
        margin: 5px 20px;
      }

      .sku-box {
        margin: 0 10px;
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        padding-bottom: 10px;

        .sku-img {
          width: 90px;
        }

        .sku {
          width: calc(100% - 90px);
        }

        .sku-name {
          font-size: small;
          background-color: #999;
          color: #FFF;
          width: 100%;
          word-break: break-all;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
    }
  }
}
</style>
