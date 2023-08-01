<template>
  <div class="orderPickTask">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="form" :rules="rules" ref="form">
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="form.organizationId" filterable placeholder="请选择货主" size="small" @change="getCustomerId()"
                     style="width: 70%;">
            <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收货人" prop="customerId" :label-width="formLabelWidth">
          <el-select v-model="form.customerId" filterable placeholder="请选择收货人" size="small" @change="changCustomer"
                     style="width: 70%;">
            <el-option
                    v-for="item in customers"
                    :key="item.customerId"
                    :label="item.customerName"
                    :value="item.customerId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户订单号" prop="billNo" :label-width="formLabelWidth">
          <el-input v-model="form.billNo" size="mini" placeholder="客户订单号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="库位" prop="cellCode" :label-width="formLabelWidth">
          <el-input v-model="form.cellCode" size="mini" placeholder="请扫描库位编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="物料编码" prop="itemCode" :label-width="formLabelWidth">
          <el-input v-model="form.itemCode" size="mini" placeholder="请扫描物料编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="拣货数量" prop="pickQuantity" :label-width="formLabelWidth">
          <el-input-number v-model="form.pickQuantity" size="mini" placeholder="请输入数量" clearable style="width: 90%;"></el-input-number>
        </el-form-item>
        <el-form-item label="目标货位" prop="toCellCode" :label-width="formLabelWidth">
          <el-select v-model="form.toCellCode" filterable placeholder="请选择目标货位" size="small"
                     style="width: 70%;">
            <el-option
                    v-for="item in cells"
                    :key="item.cellCode"
                    :label="item.cellCode"
                    :value="item.cellCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="包装编码" prop="packCode" :label-width="formLabelWidth">
          <el-input v-model="form.packCode" size="mini" @change="getPackDetailData(form.packCode)" placeholder="请输入包装编码" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="packDetailId" :label-width="formLabelWidth">
          <el-select v-model="form.packDetailId" filterable placeholder="请选择单位" size="small" @change="setPackDetail"
                     style="width: 70%;">
            <el-option
                    v-for="item in packDetails"
                    :key="item.packDetailId"
                    :label="item.packDescribe"
                    :value="item.packDetailId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="追踪号" prop="trackingNum" :label-width="formLabelWidth">
          <el-input v-model="form.trackingNum" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="commit()" size="mini" round style="width: 100px;margin-top: 5px;left: auto"> 确认</el-button>
    </div>

<!--    <el-divider content-position="center">拣货信息</el-divider>-->


  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "orderPickTask",
    data() {
      return {
        headerTitle: "仓库直拣",
        formLabelWidth: '120px',

        trackingNum:'',
        toCellCode:'',
        pickTasks:[],
        organizations:[],
        customers:[],
        cells:[],

        form:{
          wareId:PF.getLocal('wareId', 'number'),
          wareName:PF.getLocal('wareName'),
          organizationId:'',
          organizationName:'',
          customerName:'',
          customerId:'',
          billNo:'',
          cellCode:'',
          itemCode:'',
          pickQuantity:'',
          toCellCode:'',
          trackingNum:'',
          packCode:'',
          packDetailId:'',
          packDescribe:'',
          transRatio:'',
        },
        packDetails:[],

        rules: {
          organizationId: [{required: true, message: '请选择货主', trigger: 'blur'}],
          customerId: [{required: true, message: '请选择收货人', trigger: 'blur'}],
          billNo: [{required: true, message: '请录入单号', trigger: 'blur'}],
          cellCode: [{required: true, message: '请扫描库位', trigger: 'blur'}],
          itemCode: [{required: true, message: '请扫描物料编码', trigger: 'blur'}],
          pickQuantity: [{required: true, message: '请输入数量', trigger: 'blur'}],
          toCellCode: [{required: true, message: '请选择目标货位', trigger: 'blur'}],
          packCode: [{required: true, message: '请输入包装编码', trigger: 'blur'}],
          packDetailId: [{required: true, message: '请选择单位', trigger: 'blur'}],
          trackingNum: [{required: true, message: '请输入追踪号', trigger: 'blur'}],
        },
      };
    },
    methods: {
      commit(){
        IOT.showOverlay('保存中，请稍等...');
        let form = JSON.parse(JSON.stringify(this.form))

        IOT.getServerData('/pick/tasks/warePickTask', 'post', form, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              this.clearForm();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      clearForm(){
        this.form.organizationId='';
        this.form.organizationName='';
        this.form.customerName='';
        this.form.customerId='';
        this.form.billNo='';
        this.form.cellCode='';
        this.form.itemCode='';
        this.form.pickQuantity='';
        this.form.toCellCode='';
        this.form.this.form.trackingNum='';
        this.form.packCode='';
        this.form.packDetailId='';
        this.form.packDescribe='';
        this.form.transRatio='';
      },
      getCustomerId(){
        for(let i=0;i<this.organizations.length;i++){
          if(this.organizations[i].organizationId === this.form.organizationId){
            this.form.organizationName = this.organizations[i].organizationName;
            IOT.getServerData('/customers/list','get',{organizationId:this.form.organizationId},(ret) => {
              if (ret.code === 200) {
                this.customers = [];
                this.customers = ret.rows;
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
          }
        }
      },
      changCustomer(){
        for(let i=0;i<this.customers.length;i++){
          if(this.customers[i].customerId === this.form.customerId){
            this.form.customerName = this.customers[i].customerName;
          }
        }
      },
      setPackDetail(){
        for(let i=0;i<this.packDetails.length;i++){
          if(this.packDetails[i].packDetailId === this.form.packDetailId){
            this.form.packDescribe = this.packDetails[i].packDescribe;
            this.form.transRatio = this.packDetails[i].transRatio;
          }
        }
      },
      getPackDetailData(packCode) {
        IOT.getServerData('/pack/details/list', 'get', {packCode: packCode}, (ret) => {
          if (ret.code === 200) {
            this.packDetails = [];
            this.packDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData(){
        IOT.getServerData('/cell/infos/list','get',{cellUseType:5},(ret) => {
          if (ret.code === 200) {
            this.cells = [];
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getCellData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .orderPickTask {
  }
</style>
