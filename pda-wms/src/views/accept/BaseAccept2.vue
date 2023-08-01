<template>
  <div class="baseAccept2">
    <FeaturesHeader :headerTitle="headerTitle" />

    <el-tabs v-model="activeName" type="card" @tab-click="handleClick" style="margin: 10px 5px;">
      <el-tab-pane label="验收信息" name="first">
        <el-form class="noMargin" :model="form" :rules="rules"  ref="form">
          <el-form-item label="物料" :label-width="formLabelWidth">
            <el-select v-model="detailIndex" @change="selectItem" size="mini" filterable placeholder="请选择物料" clearable style="width: 90%;">
              <el-option
                v-for="(item,index) in asnDetails"
                :key="index"
                :label="item.itemName"
                :value="index">
              </el-option>
            </el-select>
          </el-form-item>

          <el-divider content-position="center">验收数据</el-divider>

          <div v-if="detailIndex !== null">
            <el-form-item label="仓库" prop="wareId" :label-width="formLabelWidth">
              <span>{{params.wareName}}</span>
            </el-form-item>
            <!--<el-form-item label="关键字" :label-width="formLabelWidth">
              <el-input v-model="form.keyWords" size="mini" placeholder="ASN单号等" clearable style="width: 90%;"></el-input>
            </el-form-item>-->
            <el-form-item label="已验收数" prop="organizationId" :label-width="formLabelWidth">
              <span>{{form.acceptQuantity}}</span>
            </el-form-item>
            <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
              <el-input v-model="form.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
            </el-form-item>
            <el-form-item label="验收数" prop="toQuantity" :label-width="formLabelWidth">
              <el-input-number v-model="form.toQuantity" size="mini" placeholder="请输入验收数量" style="width: 90%;"></el-input-number>
            </el-form-item>
            <el-form-item label="包装描述" prop="toPackDetailId" :label-width="formLabelWidth">
              <el-select v-model="form.toPackDetailId" size="mini" filterable placeholder="请选择单位" style="width: 90%;">
                <el-option
                  v-for="item in form.packDetails"
                  :key="item.packDetailId1"
                  :label="item.packDescribe1"
                  :value="item.packDetailId1">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="货位" prop="toCellCode" :label-width="formLabelWidth">
              <el-select v-model="form.toCellCode" size="mini" filterable placeholder="请选择货位" style="width: 90%;">
                <el-option
                  v-for="item in cells"
                  :key="item.cellCode"
                  :label="item.cellName"
                  :value="item.cellCode">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
              <span>{{form.organizationName}}</span>
            </el-form-item>
            <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
              <span>{{form.supplierName}}</span>
            </el-form-item>
          </div>
          <p class="messageP" v-else>请选择需要验收的物料</p>
        </el-form>

      </el-tab-pane>
      <el-tab-pane label="批次策略" name="second">
        <el-form class="noMargin" :model="form" :rules="rules"  ref="form">

          <el-form-item v-for="item in batchTactics.batchTacticDetails" :label="item.detailName" :prop="item.detailCode"
                        :label-width="formLabelWidth">
            <el-select v-if="item.detailCode === 'qcState'" v-model="form[item.detailCode]" filterable size="small"
                       :placeholder="'请选择'+item.detailName" style="width: 90%;">
              <el-option
                v-for="item2 in qcStates"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>

            <el-input v-else-if="item.format === 0" v-model="form[item.detailCode]" :placeholder="'请输入'+item.detailName"
                      :disabled="item.detailCode === 'detailNo' || item.detailCode === 'supplierCode'" size="small"
                      style="width: 90%;"></el-input>
            <el-input-number v-else-if="item.format === 1" v-model="form[item.detailCode]"
                             :min="0" label="请输入" size="small" style="width: 90%;"></el-input-number>
            <el-date-picker v-else-if="item.format === 2" v-model="form[item.detailCode]" value-format="yyyy-MM-dd"
                            type="date" :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-date-picker>
            <el-time-picker v-else-if="item.format === 3" v-model="form[item.detailCode]" value-format="HH:mm:ss"
                            :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-time-picker>
            <el-select v-else-if="item.format === 4" v-model="form[item.detailCode]" filterable
                       :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;">
              <el-option
                v-for="item2 in item.values"
                :key="item2.batchTacticDetailDetailValue"
                :label="item2.batchTacticDetailDetailValue"
                :value="item2.batchTacticDetailDetailValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <FeaturesFooter @isCommit="isCommit" :footerBtn="footerBtn" />
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import FeaturesFooter from "@/components/FeaturesFooter";
  import {mapState} from "vuex";

  export default {
    name: "BaseAccept2",
    data() {
      return {
        headerTitle: "基础验收",
        footerBtn: "验收",
        formLabelWidth: '120px',
        params: this.$route.params,

        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseType: 0,
        },
        cells: [],

        activeName: 'first',
        asnDetails: [],
        batchTactics: {},
        detailIndex: null,

        form: {},
        rules: {
          wareId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toQuantity: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          'productDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'exDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'inDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierCode': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierBatch': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'qcState': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'costPrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'salePrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'detailNo': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo3': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
        },

      };
    },
    computed: {
      ...mapState([
        "qcStates",
      ]),
    },
    methods: {
      handleClick(tab, event) {
        // console.log(tab, event);
        if(this.activeName === 'second'){
          if(this.form.organizationId && this.form.itemCode){
            this.getBatchTacticData(this.form.organizationId,this.form.itemCode);
          }else{
            IOT.tips('请先选择需要验收的物料！', 'error');
            this.activeName = 'first';
          }
        }
      },
      getAsnDetailData(){
        if(!PF.isNull(this.$route.params.billNo)){
          this.$router.go(-1);
        }
        IOT.getServerData('/asn/details/list','get',{billNo: this.$route.params.billNo},(ret) => {
          if (ret.code === 200) {
            this.asnDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      selectItem(val){
        let form = PF.JSON(this.asnDetails[val]);
        form.wareId = this.params.wareId;
        form.toCellCode = '';
        form.toQuantity = form.expectQuantity;
        form.toPackDetailId = form.packDetailId;
        this.form = form;
      },
      getBatchTacticData(organizationId,itemCode) {
        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: organizationId,
          itemCode: itemCode
        }, (ret) => {
          if (ret.code === 200) {
            let list = PF.JSON(ret.rows[0])
            let detailDetails = list.batchTacticDetailDetails;
            let testDetailDetails = [];
            list.batchTacticDetails[12].values = [];
            list.batchTacticDetails[13].values = [];
            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let i = 0; i < batchTacticDetails.length; i++) {
              if (batchTacticDetails[i].state === 1) {
                testBatchTacticDetails.push(batchTacticDetails[i])
                if (batchTacticDetails[i].isRequired === 1) {
                  this.rules[batchTacticDetails[i].detailCode][0].required = true;
                }
                if (batchTacticDetails[i].detailCode === 'inDate') {
                  this.form[batchTacticDetails[i].detailCode] = PF.getNowFormatDate();
                }
                if (batchTacticDetails[i].detailCode === 'detailNo') {
                  this.form.detailNo = this.form.detailNo;
                }else if (batchTacticDetails[i].detailCode === 'supplierCode') {
                  this.form.supplierCode = this.form.supplierCode;
                }
              }
            }

            list.batchTacticDetailDetails = testDetailDetails;
            list.batchTacticDetails = testBatchTacticDetails;
            // this.form.detailNo = this.theInput.asnDetailNo;
            // this.form.supplierBatch = this.theInput.batchNo;
            // this.form.supplierCode = this.theInput.supplierCode;
            this.batchTactics = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData() {
        IOT.getServerData('/cell/infos/list', 'get', this.searchSelectCell, (ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
            this.searchSelectCell.total = ret.total;
            this.searchSelectCell.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName){
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);

            // form.wareId = this.params.wareId;
            form.wareName = this.params.wareName;
            form.billType = 2;
            // form.toPackDetailId = form.packDetailId;
            // form.toPackDescribe = form.packDescribe;
            // form.toQuantity = form.expectQuantity;

            IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: [form]}, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('验收成功！', 'success', 1000, () => {
                  this.$router.push({name: 'baseAccept'})
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form');
            this.batchTactics = {};
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
        }else if(JSON.stringify(this.batchTactics) === '{}'){
          IOT.tips('请填写批次策略！', 'error');
        }else if(val){
          this.commitForm('form');
        }
      }
    },
    created() {
      this.getAsnDetailData();
      this.getCellData();
    },
    components: {
      FeaturesHeader,
      FeaturesFooter
    }
  };
</script>
<style lang="less" scoped>
  .baseAccept2 {
    padding-bottom: 90px;
  }
</style>
