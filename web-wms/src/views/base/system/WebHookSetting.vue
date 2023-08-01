<template>
  <div class="webhooksetting">
    <h1>WebHook设置</h1>
    <div class="head">
      <el-form label-width="80px" size="mini" inline>
        <el-form-item label="是否启用">
          <el-select v-model="searchParam.hasEnable" placeholder="请选择" clearable>
            <el-option label="启用" :value="true" />
            <el-option label="停用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="推送内容">
          <!-- WebHookAbility枚举 -->
          <el-select v-model="searchParam.ability" placeholder="请选择" clearable>
            <el-option label="确认入库" value="CONFIRM_STORAGE_IN" />
          </el-select>
        </el-form-item>
        <el-button type="primary" size="mini" @click="search">搜索</el-button>
        <el-button type="primary" size="mini" @click="addDialogShow = true">新增</el-button>
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
      <el-table-column prop="name" label="描述" />
      <el-table-column prop="hasEnable" label="是否启用">
        <template slot-scope="scope">
          <span>{{ scope.row.hasEnable ? '启用中' : '已停用' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" @click="enable(scope.row.webHookSettingId, !scope.row.hasEnable)">{{scope.row.hasEnable?'停':'启'}}用</el-button>
          <el-button size="mini" @click="del(scope.row.webHookSettingId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="新增" :visible.sync="addDialogShow" width="50%">
      <el-form label-width="80px" size="mini" :model="addParam" ref="addForm" :rules="addRules">
        <el-form-item label="URL" prop="url">
          <el-input v-model="addParam.url" placeholder="请填写" clearable />
        </el-form-item>
        <el-form-item label="推送内容" prop="ability">
          <!-- WebHookAbility枚举 -->
          <el-select v-model="addParam.ability" placeholder="请选择" clearable>
            <el-option label="确认入库" value="CONFIRM_STORAGE_IN" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="addParam.name" placeholder="请填写" clearable />
        </el-form-item>
        <el-form-item label="是否启用" prop="hasEnable">
          <el-select v-model="addParam.hasEnable" placeholder="请选择" clearable>
            <el-option label="启用" :value="true" />
            <el-option label="停用" :value="false" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogShow = false">取 消</el-button>
        <el-button type="primary" :loading="adding" @click="addSubmit">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="请注意" :visible.sync="addResultShow" width="50%">
      <div>以下为此WebHook的验签秘钥，此秘钥只展示一次，请注意保存:</div>
      <br>
      <div style="font-size: 25px; text-align: center;font-weight: 500;color: #000;">
        {{ addResult }}
      </div>
      <br>
      <div>如果忘记秘钥，可删除WebHook重新配置，或联系管理员查询。</div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="adding" @click="addResultShow=false">我已记下</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'WebHookSetting',
  data() {
    return {
      searchParam: {},
      mainData: [],
      addDialogShow: false,
      addParam: {},
      addRules: {
        url:[{ required: true, message: '必填', trigger: 'blur' }],
        ability:[{ required: true, message: '必填', trigger: 'change' }],
        hasEnable:[{ required: true, message: '必填', trigger: 'change' }]
      },
      adding: false,
      addResultShow: false,
      addResult: null
    }
  },
  mounted() {
    this.search()
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
      IOT.getServerData('/webhook/setting/list', 'get', this.searchParam, success, false, true, error)
    },
    addSubmit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.adding = true
          const success = (response) => {
            this.adding = false
            if (response.code && response.code == 200) {
              this.$message.success('新增成功')
              this.addDialogShow = false
              this.addParam = {}
              this.search()
              this.addResult = response.data
              this.addResultShow = true
            } else {
              this.$message.error(response.message || '新增失败')
            }
          }
          const error = () => {
            this.adding = false
          }
          IOT.getServerData('/webhook/setting/add', 'post', this.addParam, success, true, true, error)
        }
      })
    },
    enable(id, hasEnable){
      const success = (response) => {
        if (response.code && response.code == 200) {
          this.$message.success('状态修改成功')
          this.search()
        } else {
          this.$message.error(response.message || '状态修改失败')
        }
      }
      IOT.getServerData('/webhook/setting/enable', 'post', { webHookSettingId: id, hasEnable }, success, false, true)
    },
    del(id) {
      const success = (response) => {
        if (response.code && response.code == 200) {
          this.$message.success('删除成功')
          this.search()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      }
      IOT.getServerData('/webhook/setting/del', 'get', { id }, success, false, true)
    }
  }
}
</script>
<style lang="less">
  .webhooksetting{
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