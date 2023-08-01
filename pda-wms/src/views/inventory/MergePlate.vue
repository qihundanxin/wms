<template>
  <div class="mergePlate">
    <FeaturesHeader :headerTitle="headerTitle"/>

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="原托盘" prop="cellCode" :label-width="formLabelWidth">
          <el-input v-model="searchData.cellCode" size="mini" placeholder="原托盘" clearable
                    style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="目标托盘" prop="cellCode" :label-width="formLabelWidth">
          <el-input v-model="searchData.cellCode" size="mini" placeholder="目标托盘" clearable
                    style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <!--<el-button @click="searchData.pageSize = 2;inventoryBalances = [];getInventoryBalanceData()" type="primary" size="mini" round
                 icon="iconfont icon-chaxun" style="width: 100px;margin-top: 5px;left: auto"> 查询
      </el-button>-->
    </div>

    <div style="margin-top: 10px;">
      <div class="title clearFloat">
        <span>原托盘 : TPA01-001</span>
        <span class="rightFloat">1/10</span>
      </div>
      <div class="mpBox top">
        <div class="row clearFloat">
          <div class="clo"><el-checkbox v-model="checked1"></el-checkbox></div>
          <div class="clo">批次</div>
          <div class="clo">料号</div>
          <div class="clo">单价</div>
          <div class="clo">数量</div>
        </div>
      </div>
      <div class="mpBox bottom">
        <div class="row clearFloat" v-for="item in mpBoxData1">
          <div class="clo"><el-checkbox v-model="item.checked"></el-checkbox></div>
          <div class="clo">{{item.batch}}</div>
          <div class="clo">{{item.itemCode}}</div>
          <div class="clo">{{item.danwei}}</div>
          <div class="clo">{{item.quantity}}</div>
        </div>
      </div>
    </div>

    <div class="mpBoxCenter">
      <el-button type="primary" icon="iconfont icon-xia" circle @click="mpBoxMove('down')"></el-button>
      <el-button type="primary" icon="iconfont icon-shang" circle @click="mpBoxMove('up')"></el-button>
    </div>


    <div>
      <div class="title clearFloat">
        <span>目标托盘 : TPA01-002</span>
        <span class="rightFloat">1/10</span>
      </div>
      <div class="mpBox top">
        <div class="row clearFloat">
          <div class="clo"><el-checkbox v-model="checked2"></el-checkbox></div>
          <div class="clo">批次</div>
          <div class="clo">料号</div>
          <div class="clo">单价</div>
          <div class="clo">数量</div>
        </div>
      </div>
      <div class="mpBox bottom">
        <div class="row clearFloat" v-for="item in mpBoxData2">
          <div class="clo"><el-checkbox v-model="item.checked"></el-checkbox></div>
          <div class="clo">{{item.batch}}</div>
          <div class="clo">{{item.itemCode}}</div>
          <div class="clo">{{item.danwei}}</div>
          <div class="clo">{{item.quantity}}</div>
        </div>
      </div>
    </div>

    <div style="text-align: center;margin-top: 10px;">


      <el-button @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-hebing
"
                 style="width: 200px;margin-top: 5px;right: auto"> 合托
      </el-button>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "MergePlate",
    data() {
      return {
        headerTitle: "合并托盘",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        inventoryBalances: [],

        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          state: 1,
          wareId: PF.getLocal('wareId', 'number'),
          cellCode: ''
        },
        checked1: false,
        checked2: false,
        mpBoxData1: [
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
        ],
        mpBoxData2: [
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
          {
            checked: false,
            batch: "20190205",
            itemCode: "code",
            danwei: "0.5",
            quantity: "21",
          },
        ],
      };
    },
    computed:{
      mpBoxData1All(){
        for(let i=0;i<this.mpBoxData1.length;i++){
          if(!this.mpBoxData1[i].checked){
            return false
          }
        }
        return true
      },
      mpBoxData2All(){
        for(let i=0;i<this.mpBoxData2.length;i++){
          if(!this.mpBoxData2[i].checked){
            return false
          }
        }
        return true
      }
    },
    watch:{
      checked1(val,oval){
        if(val){
          this.forToData(val,1);
        }
      },
      checked2(val,oval){
        if(val){
          this.forToData(val,2);
        }
      },
      mpBoxData1All(val,oval){
        this.checked1 = val;
      },
      mpBoxData2All(val,oval){
        this.checked2 = val;
      },
    },
    methods: {
      mpBoxMove(type){
        let arr = [];
        let a = [];
        let b = [];
        if(type === 'down'){
          a = PF.JSON(this.mpBoxData1);
          b = PF.JSON(this.mpBoxData2);
        }else if(type === 'up'){
          a = PF.JSON(this.mpBoxData2);
          b = PF.JSON(this.mpBoxData1);
        }
        for(let i=0;i<a.length;i++){
          if(a[i].checked){
            a[i].checked = false;
            b.push(a[i]);
            a.splice(i,1);
            i--;
          }
        }
        if(type === 'down'){
          this.mpBoxData1 = a;
          this.mpBoxData2 = b;
        }else if(type === 'up'){
          this.mpBoxData2 = a;
          this.mpBoxData1 = b;
        }

      },
      forToData(val,index){
        if(index === 1){
          for(let i=0;i<this.mpBoxData1.length;i++){
            this.mpBoxData1[i].checked = val;
          }
        }else if(index === 2){
          for(let i=0;i<this.mpBoxData2.length;i++){
            this.mpBoxData2[i].checked = val;
          }
        }
      },
      getInventoryBalanceData() {
        this.loading = true;
        let searchData = this.searchData;
        if (!PF.isNull(searchData.cellCode)) {
          delete searchData.cellCode;
        }
        IOT.getServerData('/cell/infos/list', 'get', searchData, (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.inventoryBalances = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if (this.searchData.pageSize >= ret.total) {
              this.noMore = true;
            } else {
              this.noMore = false;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            for (let key in form.row) {
              form[key] = form.row[key]
            }
            delete form.row;
            form.fromCellCode = form.cellCode;
            IOT.getServerData('/inventorys/lockCell', 'get', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.searchData.pageSize = 2;
                  this.inventoryBalances = [];
                  this.getInventoryBalanceData();
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
    },
    created() {
      let cellCode = this.$route.params.cellCode;
      if (PF.isNull(cellCode)) {
        this.searchData.cellCode = cellCode;
      }

    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .mergePlate {
    .title{
      border: 1px solid #ebeef5;
      margin: 0 auto;
      width: calc(90% - 20px);
      overflow: auto;
      line-height: 30px;
      background: #f5f7fa;
      padding: 0 10px;
      span:first-child{
        color: #409EFF;
        font-weight: bold;
      }
      span:last-child{
        color: #909399;
        font-size: 16px;
      }
    }
    .mpBox{
      border: 1px solid #ebeef5;
      margin: 0 auto;
      width: 90%;
      overflow: auto;
      .row{
        border-bottom: 1px solid #ebeef5;
        .clo{
          float: left;
          margin-left: 5px;
          line-height: 30px;
          overflow: hidden;
          width: 20%;
        }
        .clo:nth-child(1){
          width: 5%;
        }
        .clo:nth-child(2){
          width: 35%;
        }
        .clo:nth-child(3){
          width: 25%;
        }
        .clo:nth-child(4){
          width: 15%;
        }
        .clo:nth-child(5){
          width: 10%;
        }
      }
      .row:last-child{
        border: 0;
      }
    }
    .mpBox.top{
    }
    .mpBox.bottom{
      max-height: 120px;
    }
    .mpBoxCenter{
      text-align: center;
      padding: 10px 0;
    }
  }
</style>
