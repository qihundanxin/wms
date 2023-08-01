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
          <span>每个标签打印几次 / Label repeat times</span>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-input v-model="settings.repeat">
            <template slot="append">次 / times</template>
          </el-input>
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
    <div v-for="itemInfo in printData" :key="'k1' + itemInfo.id">
      <PrintPage :width="width" :height="height" :debug="debug" :version="version" v-for="r in repeat" :key="'k2' + r">
        <template slot-scope="scope">
          <div :style="{ 'padding': scope.n * 8 + 'mm' + ' ' + scope.n * 4 + 'mm' }">
            <div class="height-54">
              <div class="font-size-7 line-height-10" style="text-align: left;font-weight: bolder;word-break: break-all;">
                {{ itemInfo.itemCode }}
              </div>
              <div class="font-size-4 line-height-5" style="text-align: left;">{{ itemInfo.itemName }}</div>
            </div>
            <div class="height-30" style="display: flex;flex-direction: row-reverse;">
              <div class="qr width-30">{{ itemInfo.itemCode }}</div>
            </div>
          </div>
        </template>
      </PrintPage>
    </div>
  </div>
</template>

<script>
import PrintPage from '@/components/PrintPage'

export default {
  name: "PrintItemLabel",
  data() {
    return {
      width: 76.2,
      height: 101.6,
      debug: false,
      version: 1,
      repeat: 1,
      more: false,
      settings: {
        width: 76.2,
        height: 101.6,
        debug: false,
        repeat: 1,
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
      if (isNaN(this.settings.width) || isNaN(this.settings.height) || isNaN(this.settings.repeat)) {
        IOT.tips('参数填写错误 / Parameter error', 'error', 2000)
        return
      }
      this.width = Number(this.settings.width)
      this.height = Number(this.settings.height)
      this.debug = this.settings.debug
      this.repeat = Number(this.settings.repeat)
      this.$nextTick(() => {
        this.version = this.version + 1
      })
    },
    saveSettings() {
      PF.setLocal('PrintItemLabel-Settings', this.settings)
      IOT.tips('OK', 'success', 500)
    },
    forgetSettings() {
      PF.removeLocal('PrintItemLabel-Settings')
      IOT.tips('OK', 'success', 500)
    },
    print() {
      window.print()
    }
  },
  created() { },
  mounted() {
    const printData = PF.getLocal('PrintItemLabel', 'json')
    if (!printData) {
      alert('请先选择要打印的内容')
      window.close()
    }
    this.printData = printData
    this.$nextTick(() => {
      PF.removeLocal('PrintItemLabel')
      const settings = PF.getLocal('PrintItemLabel-Settings', 'json')
      console.log(settings)
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
