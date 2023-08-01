<template>
  <!--
    打印拣货两件套
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
          <span>每单SKU只打印一次 / Print label once for each SKU</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.itemOnce" active-text="开 / ON" inactive-text="关 / OFF" />
        </el-col>

        <el-col :span="16">
          <span>每单结尾打印一张空白页 / Print a blank page at the end for each order</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-switch v-model="settings.nullPageEnd" active-text="开 / ON" inactive-text="关 / OFF" />
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
    <div v-for="(pickTask, iPickTask) in printData.pickTaskList" :key="'k4' + iPickTask">
      <!-- 汇总页 -->
      <PrintPage :width="width" :height="height" :version="version" :debug="debug">
        <template slot-scope="scope">
          <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
            <div class="height-6 font-size-5 line-height-6">
              <div v-if="pickTask.pickTaskInfo.serial != null" style="text-align: right;">
                <span class="height-5 font-size-4 line-height-5">
                  {{ titlesEnglish ? 'Deliver No.' : '配送顺序：' }}
                </span>
                <span style="font-weight: bolder;">
                  {{ pickTask.pickTaskInfo.serial }}
                </span>
              </div>
            </div>
            <div class="height-8 font-size-7 line-height-8">{{ titlesEnglish ? 'Order Picking List' : '拣货单' }}</div>
            <div class="height-6 font-size-5 line-height-6">{{ pickTask.pickTaskInfo.wareName }}</div>
            <div class="height-37 font-size-3 line-height-5">
              <table cellspacing="0" style="margin: auto;">
                <tr>
                  <td>{{ titlesEnglish ? 'Order#' : '订单号' }}</td>
                  <td colspan="3">{{ pickTask.pickTaskInfo.billNo }}</td>
                </tr>
                <tr>
                  <td>{{ titlesEnglish ? 'Driver' : '司机' }}</td>
                  <td colspan="3">{{ pickTask.pickTaskInfo.driverName }}</td>
                </tr>
                <tr>
                  <td class="width-19">{{ titlesEnglish ? 'Serial.' : '单序号' }}</td>
                  <td class="width-14">{{ pickTask.pickTaskInfo.shopDailCount }}</td>
                  <td class="width-19">{{ titlesEnglish ? 'Deliver No.' : '配送顺序' }}</td>
                  <td class="width-14">{{ pickTask.pickTaskInfo.serial }}</td>
                </tr>
                <tr>
                  <td>{{ titlesEnglish ? 'Number of SKUs' : 'SKU数' }}</td>
                  <td>{{ pickTask.pickTaskInfo.skuNum }}</td>
                  <td>{{ titlesEnglish ? 'Total number of Products' : '产品总数' }}</td>
                  <td>{{ pickTask.pickTaskInfo.itemNum }}</td>
                </tr>
              </table>
            </div>
            <div class="height-21">
              <div class="qr height-21 width-21" style="margin: auto;">{{ pickTask.pickTaskInfo.billNo }}</div>
            </div>
            <div class="height-4 font-size-3 line-height-4">
              {{ pickTask.pickTaskInfo.printDate ? pickTask.pickTaskInfo.printDate.substring(0, 11) : '' }}
            </div>
          </div>
        </template>
      </PrintPage>
      <!-- 表格页 -->
      <PrintPage :width="width" :height="height" :version="version" :debug="debug"
        v-for="(detailTable, detailTableIndex) in pickTask.detailTableList" :key="'k1' + detailTableIndex">
        <template slot-scope="scope">
          <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
            <div class="height-5 font-size-4 line-height-5">{{ pickTask.pickTaskInfo.billNo }}</div>
            <div class="font-size-3 line-height-4">
              <table cellspacing="0" style="margin: auto;">
                <tr>
                  <td>{{ titlesEnglish ? 'Cell Code' : '货位' }}</td>
                  <td>{{ titlesEnglish ? 'Item Name' : '产品名称' }}</td>
                  <td>{{ titlesEnglish ? 'Qty' : '数量' }}</td>
                </tr>
                <tr v-for="(detail, iDetail) in detailTable" :key="'k2' + iDetail">
                  <td class="height-12 width-19">
                    <div class="max-height-12 width-18" style="margin: auto;">
                      {{ detail.cellCode }}
                    </div>
                  </td>
                  <td class="height-12 width-38">
                    <div class="max-height-12 width-37" style="margin: auto;">
                      {{ tableItemEnglish ? getEnglishItemName(detail.itemName) : detail.itemName }}
                    </div>
                  </td>
                  <td class="height-12 width-9">
                    <div class="max-height-12 width-8" style="margin: auto;">
                      {{ detail.qty }}
                    </div>
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </template>
      </PrintPage>
      <!-- Label签页 -->
      <PrintPage :width="width" :height="height" :version="version" :debug="debug"
        v-for="(labelItem, iLabel) in showItemList(pickTask.itemList)" :key="'k3' + iLabel">
        <template slot-scope="scope">
          <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
            <div class="height-9 font-size-8 line-height-9" style="display: flex; justify-content: space-between;">
              <div class="max-width-48 font-size-5 line-height-5">{{ pickTask.pickTaskInfo.driverName }}</div>
              <div class="max-width-18" style="font-weight: bolder;">{{ pickTask.pickTaskInfo.serial }}</div>
            </div>
            <div class="height-6 font-size-5 line-height-6">
              <span>#{{ pickTask.pickTaskInfo.allotBillNo }}</span>
            </div>
            <div class="height-10" style="display: flex; justify-content: space-around">
              <div>
                <div class="height-4 font-size-3 line-height-4">
                  {{ titlesEnglish ? 'Serial' : '产品序号' }}
                </div>
                <div class="height-6 font-size-5 line-height-6">
                  {{ labelItem.detailIndex }}
                </div>
              </div>
              <div>
                <div class="height-4 font-size-3 line-height-4">
                  {{ titlesEnglish ? 'Cnt' : '数量' }}
                </div>
                <div class="height-6 font-size-5 line-height-6">
                  {{ labelItem.strIndex }}
                </div>
              </div>
              <div>
                <div class="height-4 font-size-3 line-height-4">
                  {{ titlesEnglish ? 'Total' : '总数' }}
                </div>
                <div class="height-6 font-size-5 line-height-6">
                  {{ labelItem.skuQuantity }}
                </div>
              </div>
              <div>
                <div class="height-4 font-size-3 line-height-4">
                  {{ titlesEnglish ? 'Order Qty' : '订单总数' }}
                </div>
                <div class="height-6 font-size-5 line-height-6">
                  {{ labelItem.orderAllQuantity }}
                </div>
              </div>
            </div>
            <div class="height-21" style="display: flex; align-items:center; justify-content: center;">
              <div class="width-21 font-size-5 line-height-6">
                {{ labelItem.hasWeight ? 'Weight' : '' }}
              </div>
              <div class="qr width-21">{{ labelItem.qrCode }}</div>
              <div class="width-21 font-size-8 line-height-9"></div>
            </div>
            <div class="height-7 font-size-6 line-height-7">{{ labelItem.cellCode }}</div>
            <div class="height-30 font-size-4 line-height-5">
              {{ labelItemEnglish ? getEnglishItemName(labelItem.itemName) : labelItem.itemName }}
            </div>
          </div>
        </template>
      </PrintPage>
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
      itemOnce: false,
      nullPageEnd: false,
      more: false,
      settings: {
        width: 76.2,
        height: 101.6,
        debug: false,
        tableItemEnglish: false,
        labelItemEnglish: false,
        titlesEnglish: true,
        itemOnce: false,
        nullPageEnd: false,
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
      if (isNaN(this.settings.width) || isNaN(this.settings.height)) {
        IOT.tips('参数填写错误 / Parameter error', 'error', 2000)
        return
      }
      this.width = Number(this.settings.width)
      this.height = Number(this.settings.height)
      this.debug = this.settings.debug
      this.tableItemEnglish = this.settings.tableItemEnglish
      this.labelItemEnglish = this.settings.labelItemEnglish
      this.titlesEnglish = this.settings.titlesEnglish
      this.itemOnce = this.settings.itemOnce
      this.nullPageEnd = this.settings.nullPageEnd
      this.$nextTick(() => {
        this.version = this.version + 1
      })
    },
    saveSettings() {
      PF.setLocal('PrintPickSuit-Settings', this.settings)
      IOT.tips('OK', 'success', 500)
    },
    forgetSettings() {
      PF.removeLocal('PrintPickSuit-Settings')
      IOT.tips('OK', 'success', 500)
    },
    print() {
      window.print()
    },
    getEnglishItemName(itemName) {
      const itemNameArr = itemName ? itemName.split('|') : ['']
      return itemNameArr[itemNameArr.length - 1]
    },
    showItemList(itemList) {
      if (!itemList) {
        return []
      }
      if (!this.itemOnce) {
        return itemList
      } else {
        const simpleItemList = PF.uniqueArrayObject(JSON.parse(JSON.stringify(itemList)), 'itemCode')
        for (const itemI in simpleItemList) {
          simpleItemList[itemI].strIndex = 'n'
        }
        return simpleItemList
      }
    }
  },
  created() { },
  mounted() {
    const printData = PF.getLocal('PrintPickSuit', 'json')
    if (!printData) {
      alert('请重新选择要打印的内容')
      window.close()
    }
    // 将detailList拆分成每N个一份 => detailTableList
    for (const pickTaskI in printData.pickTaskList) {
      const pickTask = printData.pickTaskList[pickTaskI]
      pickTask.detailTableList = []
      let detailList = []
      for (const detailI in pickTask.detailList) {
        detailList.push(pickTask.detailList[detailI])
        if ((detailI * 1 + 1) % 6 == 0) {// 满6个放一次
          pickTask.detailTableList.push(detailList)
          detailList = []
        }
      }
      if (detailList.length > 0) {// 剩下几个，循环结束后放入 => detailTableList
        pickTask.detailTableList.push(detailList)
      }
    }
    this.printData = printData
    this.$nextTick(() => {
      PF.removeLocal('PrintPickSuit')
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
