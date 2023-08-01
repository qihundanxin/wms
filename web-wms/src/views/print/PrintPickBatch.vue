<template>
  <!--
    打印波次拣货
    @auther: xuesinuo
  -->
  <div class="print-page">
    <div class="noprint print-setting-box">
      <el-row :gutter="20" v-show="more">
        <el-col :span="12">
          <el-input v-model="settings.width">
            <template slot="prepend">宽度 / Width</template>
            <template slot="append">mm</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input v-model="settings.height">
            <template slot="prepend">高度 / Height</template>
            <template slot="append">mm</template>
          </el-input>
        </el-col>

        <el-col :span="24" style="text-align: right;">
          <el-switch v-model="settings.debug" active-text="色彩调试 / Colorful debug" inactive-text="正常打印 / Normal" />
        </el-col>

        <el-col :span="16">
          <span>表格中商品名称的语言 / Language for Item Name in table</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.tableItemEnglish" active-text="英文 / English" inactive-text="中英混 / CH&EN Mix" />
        </el-col>

        <el-col :span="16">
          <span>标签中商品名称的语言 / Language for Item Name in label</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.labelItemEnglish" active-text="英文 / English" inactive-text="中英混 / CH&EN Mix" />
        </el-col>

        <el-col :span="16">
          <span>标题名称的语言 / Language for titles</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.titlesEnglish" active-text="英文 / English" inactive-text="中文 / Chinese" />
        </el-col>

        <el-col :span="16">
          <span>波次的首页与表格连续打印 / Print times for Home&Table each batch</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-input v-model="settings.homeTableTimes">
            <template slot="append">次 / times</template>
          </el-input>
        </el-col>

        <el-col :span="16">
          <span>每波次结尾打印一张空白页 / Print a blank page at the end for each batch</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.nullPageEnd" active-text="开 / ON" inactive-text="关 / OFF" />
        </el-col>

        <el-col :span="16">
          <span>不打印已拣货的SKU标签 / Do not print picked SKU labels</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.skipPicked" active-text="开 / ON" inactive-text="关 / OFF" />
        </el-col>


        <el-col :span="24" style="text-align: right;">
          <el-tooltip class="item" effect="dark" content="仅此电脑生效 / Only this computer" placement="bottom">
            <el-button @click="saveSettings">记住设置 / Keep settings</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="仅此电脑生效 / Only this computer" placement="bottom">
            <el-button @click="forgetSettings">忘掉设置 / Forget settings</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="数据多时较慢 / Slow, when so much data" placement="bottom">
            <el-button type="warning" @click="change">确认变更 / Confirm</el-button>
          </el-tooltip>
        </el-col>

        <el-col :span="24" style="text-align: right;">
          <span style="color: red;">注意：此页面请不要使用Google翻译功能。</span>
        </el-col>
        <el-col :span="24" style="text-align: right;">
          <span style="color: red;">Note: Do not use the Google Translate in this page.</span>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24" style="text-align: right;">
          <span></span>
          <el-button v-show="more == false" @click="more = true">更多 / More</el-button>
          <el-button v-show="more == true" @click="more = false">收起 / Less</el-button>
          <el-button type="primary" @click="print">打印 / Print</el-button>
        </el-col>
      </el-row>
    </div>
    <div v-for="(batch, batchIndex) in printData.batchList" :key="'k1' + batchIndex">
      <div v-for="p1p2 in homeTableTimes" :key="'k5' + p1p2">
        <!-- 波次汇总 -->
        <PrintPage :width="width" :height="height" :version="version" :debug="debug">
          <template slot-scope="scope">
            <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
              <div class="height-6 font-size-5 line-height-6" style="display: flex;justify-content:space-between">
                <div class="max-width-50">{{ batch.bcInfo.driverName }}</div>
                <div class="max-width-20">#{{ batch.bcInfo.bcNumber }}</div>
              </div>
              <div class="height-8 font-size-7 line-height-8">
                <span>{{ titlesEnglish ? 'Batch Picking List' : '波次拣货单' }}</span>
              </div>
              <div class="height-6 font-size-5 line-height-6">
                <span>{{ batch.bcInfo.wareName }}</span>
              </div>
              <div class="height-32 font-size-3 line-height-5">
                <table cellspacing="0" style="margin: auto;">
                  <tr>
                    <td>{{ titlesEnglish ? 'Batch Code' : '波次编码' }}</td>
                    <td colspan="3">{{ batch.bcInfo.pickBatchId }}</td>
                  </tr>
                  <tr>
                    <td class="width-20">{{ titlesEnglish ? 'Batch No.' : '波次号' }}</td>
                    <td class="width-15">
                      {{ batch.bcInfo.bcNumber + '/' + printData.batchList.length }}
                    </td>
                    <td class="width-20">{{ titlesEnglish ? 'Number of orders' : '订单数' }}</td>
                    <td class="width-15">{{ batch.bcInfo.bcOrder }}</td>
                  </tr>
                  <tr>
                    <td>{{ titlesEnglish ? 'Number of SKUs' : 'SKU数' }}</td>
                    <td>{{ batch.bcInfo.skuNum }}</td>
                    <td>{{ titlesEnglish ? 'Total number of Products' : '产品总数' }}</td>
                    <td>{{ batch.bcInfo.itemNum }}</td>
                  </tr>
                </table>
              </div>
              <div class="height-24" style="display: flex; align-items:center; justify-content: center;">
                <div class="qr width-24">{{ batch.bcInfo.pickBatchId }}</div>
              </div>
              <div class="height-4 font-size-3 line-height-4">
                <span>{{ batch.bcInfo.printDate ? batch.bcInfo.printDate.substring(0, 11) : '' }}</span>
              </div>
              <div class="height-5 font-size-4 line-height-5">
                <span>{{ batch.bcInfo.containerType }}</span>
              </div>
            </div>
          </template>
        </PrintPage>
        <!-- 波次表格 -->
        <PrintPage :width="width" :height="height" :version="version" :debug="debug"
          v-for="(bcDetailTable, bcDetailTableIndex) in batch.bcDetailTableList" :key="'k2' + bcDetailTableIndex">
          <template slot-scope="scope">
            <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
              <div class="height-6 font-size-5 line-height-6">
                <span>{{ titlesEnglish ? 'Batch No.' : '波次号：' }}{{ batch.bcInfo.bcNumber }}</span>
              </div>
              <div class="font-size-3 line-height-4">
                <table cellspacing="0" style="margin: auto;">
                  <tr>
                    <td>
                      {{ titlesEnglish ? 'Cell Code' : '货位' }}
                    </td>
                    <td>
                      {{ titlesEnglish ? 'Item Name' : '产品名称' }}
                    </td>
                    <td>
                      {{ titlesEnglish ? 'Qty' : '数量' }}
                    </td>
                  </tr>
                  <tr v-for="(bcDetail, dIndex) in bcDetailTable" :key="'k3' + dIndex">
                    <td class="height-12 width-19">
                      <div class="max-height-12 width-18" style="margin: auto;">
                        {{ bcDetail.cellCode }}
                      </div>
                    </td>
                    <td class="height-12 width-38">
                      <div class="max-height-12 width-37" style="margin: auto;">
                        {{ tableItemEnglish ? getEnglishItemName(bcDetail.itemName) : bcDetail.itemName }}
                      </div>
                    </td>
                    <td class="height-12 width-9">
                      <div class="max-height-12 width-8" style="margin: auto;">
                        {{ bcDetail.qty }}
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </template>
        </PrintPage>
      </div>
      <!-- SKU详情 -->
      <div v-for="(bcItem, iIndex) in batch.bcItemList" :key="'k4' + iIndex">
        <PrintPage :width="width" :height="height" :version="version" :debug="debug" v-if="skipPicked ? bcItem.status == 0 : true">
          <template slot-scope="scope">
            <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
              <div class="height-9 font-size-8 line-height-9" style="display: flex;justify-content: space-between;">
                <div class="max-width-48 font-size-5 line-height-6">
                  {{ batch.bcInfo.driverName }}
                </div>
                <div class="max-width-18" style="font-weight: bolder;">{{ bcItem.orderIndex }}</div>
              </div>
              <div class="height-6 font-size-5 line-height-6">
                <span>#{{ bcItem.allotBillNo }}</span>
              </div>
              <div class="height-10" style="display: flex; justify-content: space-around">
                <div>
                  <div class="height-4 font-size-3 line-height-4">
                    {{ titlesEnglish ? 'Serial' : '产品序号' }}
                  </div>
                  <div class="height-6 font-size-5 line-height-6">
                    {{ bcItem.detailIndex }}
                  </div>
                </div>
                <div>
                  <div class="height-4 font-size-3 line-height-4">
                    {{ titlesEnglish ? 'Cnt' : '数量' }}
                  </div>
                  <div class="height-6 font-size-5 line-height-6">
                    {{ bcItem.strIndex }}
                  </div>
                </div>
                <div>
                  <div class="height-4 font-size-3 line-height-4">
                    {{ titlesEnglish ? 'Total' : '总数' }}
                  </div>
                  <div class="height-6 font-size-5 line-height-6">
                    {{ bcItem.skuQuantity }}
                  </div>
                </div>
                <div>
                  <div class="height-4 font-size-3 line-height-4">
                    {{ titlesEnglish ? 'Order Qty' : '订单总数' }}
                  </div>
                  <div class="height-6 font-size-5 line-height-6">
                    {{ bcItem.orderAllQuantity }}
                  </div>
                </div>
              </div>
              <div class="height-21" style="display: flex; align-items:center; justify-content: center;">
                <div class="width-21 font-size-5 line-height-6" style="display: flex; flex-direction: column;">
                  <span>@{{ bcItem.loadIndex }}</span>
                  <span v-if="bcItem.hasWeight">{{ titlesEnglish ? 'Weight' : '称重' }}</span>
                </div>
                <div class="qr width-21">{{ bcItem.qrCode }}</div>
                <div class="width-21 font-size-5 line-height-6">
                  {{ titlesEnglish ? 'B #' : '波次' }} {{ batch.bcInfo.bcNumber }}
                </div>
              </div>
              <div class="height-7 font-size-6 line-height-7">{{ bcItem.cellCode }}</div>
              <div class="height-30 font-size-4 line-height-5">
                {{ labelItemEnglish ? getEnglishItemName(bcItem.itemName) : bcItem.itemName }}
              </div>
            </div>
          </template>
        </PrintPage>
      </div>
      <!-- 空白页 -->
      <PrintPage v-if="nullPageEnd" :width="width" :height="height" />
    </div>
  </div>
</template>

<script>
import PrintPage from '@/components/PrintPage'

export default {
  name: "PrintPickBatch",
  data() {
    return {
      width: 76.2,
      height: 101.6,
      debug: false,
      version: 1,
      tableItemEnglish: false,
      labelItemEnglish: false,
      titlesEnglish: true,
      homeTableTimes: 3,
      nullPageEnd: true,
      more: false,
      skipPicked: true,
      settings: {
        width: 76.2,
        height: 101.6,
        debug: false,
        tableItemEnglish: false,
        labelItemEnglish: false,
        titlesEnglish: true,
        homeTableTimes: 3,
        nullPageEnd: true,
        skipPicked: true,
      },
      printData: {}
    };
  },
  components: {
    PrintPage
  },
  computed: {
  },
  watch: {
  },
  methods: {
    change() {
      if (isNaN(this.settings.width) || isNaN(this.settings.height) || isNaN(this.settings.homeTableTimes)) {
        IOT.tips('参数填写错误 / Parameter error', 'error', 2000)
        return
      }
      this.width = Number(this.settings.width)
      this.height = Number(this.settings.height)
      this.debug = this.settings.debug
      this.tableItemEnglish = this.settings.tableItemEnglish
      this.labelItemEnglish = this.settings.labelItemEnglish
      this.titlesEnglish = this.settings.titlesEnglish
      this.homeTableTimes = Number(this.settings.homeTableTimes)
      this.nullPageEnd = this.settings.nullPageEnd
      this.skipPicked = this.settings.skipPicked
      this.$nextTick(() => {
        this.version = this.version + 1
      })
    },
    saveSettings() {
      PF.setLocal('PrintPickBatch-Settings', this.settings)
      IOT.tips('OK', 'success', 500)
    },
    forgetSettings() {
      PF.removeLocal('PrintPickBatch-Settings')
      IOT.tips('OK', 'success', 500)
    },
    print() {
      window.print()
    },
    getEnglishItemName(itemName) {
      const itemNameArr = itemName ? itemName.split('|') : ['']
      return itemNameArr[itemNameArr.length - 1]
    }
  },
  created() { },
  mounted() {
    const printData = PF.getLocal('PrintPickBatch', 'json')
    if (!printData) {
      alert('请先选择要打印的内容')
      window.close()
    }
    // 将bcDetailList拆分成每N个一份 => bcDetailTableList
    for (const batchI in printData.batchList) {
      const batch = printData.batchList[batchI]
      batch.bcDetailTableList = []
      let bcDetailList = []
      for (const bcDetailI in batch.bcDetailList) {
        bcDetailList.push(batch.bcDetailList[bcDetailI])
        if ((bcDetailI * 1 + 1) % 6 == 0) {// 满6个放一次
          batch.bcDetailTableList.push(bcDetailList)
          bcDetailList = []
        }
      }
      if (bcDetailList.length > 0) {// 剩下几个，循环结束后放入 => bcDetailTableList
        batch.bcDetailTableList.push(bcDetailList)
      }
    }
    this.printData = printData
    this.$nextTick(() => {
      PF.removeLocal('PrintPickBatch')
      const settings = PF.getLocal('PrintPickSuit-Settings', 'json')
      if (settings) {
        this.settings = settings
        this.change()
      }
    })
  },
};
</script>

<style lang="less" scoped>
.print-page {
  min-height: 100vh;
  background-color: #EEE;
  font-family: 'SourceHanSansCN';

  .print-setting-box {
    margin: auto;
    max-width: 1080px;

    .el-col {
      margin-bottom: 10px;
    }
  }

  table {
    border-bottom: 1px solid;
    border-right: 1px solid;
  }

  td {
    border-top: 1px solid;
    border-left: 1px solid;
  }
}

@media print {
  .print-page {
    min-height: 0;
    background-color: #FFF;
  }
}
</style>
