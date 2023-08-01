<template>
  <div class="qc2">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div class="baseAcceptBillBox">
      <el-card>

        <div class="cardBody">
          <div class="topData clearFloat">
            <!-- 头部信息 -->
            <span></span>
            <span>{{params.itemName}}</span>
            <span>{{params.itemCode}}</span>
          </div>
          <div class="baseData clearFloat">
            <!-- 基础信息 -->
            <ul class="leftFloat">
              <li>
                <span>单号:</span>
                <span>&nbsp;{{params.billNo}}</span>
              </li>
              <li>
                <span>明细号:</span>
                <span>&nbsp;{{params.detailNo}}</span>
              </li>
              <li>
                <span>备注:</span>
                <span>&nbsp;{{params.memo}}</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li v-for="item in qcUdfDs">
                <span>{{item.value}}:</span>
                <span :title="item[item.paramCode]">&nbsp;{{item[item.paramCode]}}</span>
              </li>
            </ul>
          </div>
          <div class="mainData clearFloat">
            <!-- 重要信息 -->
            <ul class="leftFloat">
              <li>
                <span v-if="params.expectQuantity">&nbsp;{{params.expectQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>预约:</span>
              </li>
              <li>
                <span v-if="params.qcQuantity">&nbsp;{{params.qcQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>质检数:</span>
              </li>
              <li>
                <span v-if="params.realQuantity">&nbsp;{{params.realQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>实检数:</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span v-if="params.packDescribe">&nbsp;{{params.packDescribe}}</span>
                <span v-else>&nbsp;-</span>
                <span>单位:</span>
              </li>
              <li>
                <span v-if="params.checkQuantity">&nbsp;{{params.checkQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>已检数:</span>
              </li>
              <li>
                <span v-if="params.qualifiedQuantity">&nbsp;{{params.qualifiedQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>合格数:</span>
              </li>
              <li>
                <span v-if="params.fiedQuantity">&nbsp;{{params.fiedQuantity}}</span>
                <span v-else>&nbsp;-</span>
                <span>不合格:</span>
              </li>
            </ul>
          </div>
        </div>

      </el-card>
    </div>

    <el-form class="noMargin" :model="form" :rules="rules"  ref="form">
      <el-form-item label="已检数" prop="checkQuantity" :label-width="formLabelWidth">
        <el-input-number v-model="form.checkQuantity" size="mini" placeholder="请输入已检数量" style="width: 90%;"></el-input-number>
      </el-form-item>
      <el-form-item label="实检数" prop="realQuantity" :label-width="formLabelWidth">
        <el-input-number v-model="form.realQuantity" size="mini" placeholder="请输入实检数量" style="width: 90%;"></el-input-number>
      </el-form-item>
      <el-form-item label="合格数" prop="qualifiedQuantity" :label-width="formLabelWidth">
        <el-input-number v-model="form.qualifiedQuantity" size="mini" placeholder="请输入合格数量" style="width: 90%;"></el-input-number>
      </el-form-item>
    </el-form>

    <FeaturesFooter @isCommit="isCommit" :footerBtn="footerBtn" />
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import FeaturesFooter from "@/components/FeaturesFooter";

  export default {
    name: "Qc2",
    data() {
      let validateQuantityNo0 = (rule, value, callback) => {
        if (value > 0) {
          callback();
        } else {
          callback(new Error('数量不得为0!'));
        }
      };
      return {
        headerTitle: "质量检验",
        footerBtn: "质检",
        formLabelWidth: '120px',
        params: this.$route.params,

        qcUdfDs: [],

        form: {},
        rules: {
          checkQuantity: [
            {required: true, message: '已检数量不得为空', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
          realQuantity: [
            {required: true, message: '实检数量不得为空', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
          qualifiedQuantity: [
            {required: true, message: '合格数量不得为空', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
        },

      };
    },
    methods: {
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName){
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            IOT.getServerData('/qc/records/qc', 'post', this.form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.$router.push({name: 'qc'})
                });
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
      isCommit(val){
        if(JSON.stringify(this.form) === '{}'){
          IOT.tips('请选择需要验收的物料！', 'error');
        }else if(val){
          this.commitForm('form');
        }
      }
    },
    created() {
      let params = PF.JSON(this.params);
      params.billType = params.billSource;
      params.qcBillNo = params.billNo;
      params.qcDetailNo = params.detailNo;
      params.realQuantity = 0;
      params.qualifiedQuantity = 0;
      this.form = params;
      PF.getSysParamUdfData('qcUdfDs', [], (data, udfs) => {
        this.qcUdfDs = udfs;
      });
    },
    components: {
      FeaturesHeader,
      FeaturesFooter
    }
  };
</script>
<style lang="less" scoped>
  .qc2 {
    padding-bottom: 90px;
  }
</style>
