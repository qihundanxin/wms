<template>
  <div class="mergeUpCell">
    <FeaturesHeader :headerTitle="headerTitle"/>

    <div style="text-align: center;">
      <el-form class="noMargin" :model="form" :rules="rules" ref="form">
        <el-form-item label="追踪号" prop="boxCode" :label-width="formLabelWidth">
          <el-input v-model="form.boxCode" size="mini" placeholder="请扫描追踪号" clearable
                    style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货位号" prop="cellCode" :label-width="formLabelWidth">
          <el-input v-model="form.cellCode" size="mini" placeholder="请扫描货位号" clearable
                    style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="commit('form')" type="primary" size="mini" round
                 icon="iconfont icon-shangjia" style="width: 200px;margin-top: 5px;"> 上架
      </el-button>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "MergeUpCell",
    data() {
      return {
        headerTitle: "合并上架",
        formLabelWidth: '120px',

        form: {
          boxCode: '',
          cellCode: ''
        },
        rules: {
          boxCode: [
            {required: true, message: '请扫描追踪号', trigger: 'blur'}
          ],
          cellCode: [
            {required: true, message: '请扫描货位号', trigger: 'blur'}
          ],
        },
      };
    },
    methods: {
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commit(formName){
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('', 'get', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('上架成功！', 'success');
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },

    },
    created() {

    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .mergeUpCell {
  }
</style>
