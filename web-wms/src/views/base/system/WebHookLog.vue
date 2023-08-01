<template>
  <div class="webhooklog">
    <h1>WebHook日志</h1>
    <div class="head">
      <el-form label-width="80px" size="mini" inline>
        <el-form-item label="关键词">
          <el-input v-model="searchParam.keyLike" placeholder="入库单号" clearable style="width: 100%;" />
        </el-form-item>
        <el-form-item label="推送内容">
          <el-select v-model="searchParam.ability" placeholder="请选择" clearable>
            <!-- WebHookAbility枚举 -->
            <el-option label="确认入库" value="CONFIRM_STORAGE_IN" />
          </el-select>
        </el-form-item>
        <el-form-item label="发送时间">
          <el-date-picker
            v-model="searchParam._sendDatetime"
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="~"
            start-placeholder="From.."
            end-placeholder="To..">
          </el-date-picker>
        </el-form-item>
        <el-button type="primary" size="mini" @click="search">搜索</el-button>
      </el-form>
    </div>
    <el-table class="body" :data="mainData">
      <el-table-column prop="url" label="URL" width="600" />
      <el-table-column prop="ability" label="推送内容">
        <template slot-scope="scope">
          <span v-if="scope.row.ability=='CONFIRM_STORAGE_IN'">确认入库</span>
          <span v-else>[未知错误]</span>
        </template>
      </el-table-column>
      <el-table-column prop="sendDatetime" label="发送时间" />
      <el-table-column prop="hasSuccess" label="是否成功">
        <template slot-scope="scope">
          <span :style="{color:scope.row.hasSuccess?'green':'red'}">{{ scope.row.hasSuccess?'成功':'失败' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sendMessage" label="发送数据" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button size="mini" @click="copyTo(scope.row.sendMessage)">复制</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="resultMessage" label="返回结果" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button size="mini" @click="copyTo(scope.row.resultMessage)">复制</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
export default {
  name: 'WebHookSetting',
  data() {
    return {
      searchParam: {},
      mainData: []
    }
  },
  methods: {
    search() {
      const success = (response) => {
        if (response.code && response.code == 200) {
          this.mainData = response.rows
        } else {
          this.$message.error(response.message || '查询失败')
        }
      }
      const error = () => { }
      if (this.searchParam._sendDatetime && this.searchParam._sendDatetime.length == 2) {
        this.searchParam.sendDatetimeBegin = this.searchParam._sendDatetime[0]
        this.searchParam.sendDatetimeEnd = this.searchParam._sendDatetime[1]
      } else {
        this.searchParam.sendDatetimeBegin = null
        this.searchParam.sendDatetimeEnd = null
      }
      IOT.getServerData('/webhook/log/get', 'get', this.searchParam, success, false, true, error)
    },
    copyTo(text) {
      this.$copyText(text).then(
        e => {
          this.$message.success('已复制到剪切板')
        },
        e => {
          this.$message.success('复制失败')
        }
      )
    }
  }
}
</script>
<style lang="less">
  .webhooklog{
    .head{
      background-color: #FFFFFF;
      border-radius: 4px;
      padding: 10px;
    }
    .body{
      width: auto;
      border-radius: 4px;
      margin: 10px;
    }
  }
</style>