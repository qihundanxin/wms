<template>
  <div class="mixPlateAccept">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" :rules="rules" ref="searchData">
        <el-form-item label="追踪号" prop="toBoxCode" :label-width="formLabelWidth">
          <el-input v-model="searchData.toBoxCode" size="mini" placeholder="请输入追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="料号" :label-width="formLabelWidth">
          <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入料号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <!--<el-form-item label="关键字" :label-width="formLabelWidth">
          <el-input v-model="searchData.keyWords" size="mini" placeholder="关键字" clearable style="width: 90%;"></el-input>
        </el-form-item>-->
      </el-form>
      <el-button @click="searchData.pageSize = 10;asnDetails = [];getAsnDetailData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待验收ASN单</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in asnDetails" @click="commitAccept(item)">
          <!--noPaddingBottom-->
          <el-card>

            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{searchData.total}}</span>
                <span>{{item.itemName}}</span>
                <span>{{item.itemCode}}</span>
              </div>
              <div class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>单号:</span>
                    <span>&nbsp;{{item.billNo}}</span>
                  </li>
                  <li>
                    <span>明细号:</span>
                    <span>&nbsp;{{item.detailNo}}</span>
                  </li>
                  <li>
                    <span>备注:</span>
                    <span>&nbsp;{{item.memo}}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li v-for="item in asnUdfDs">
                    <span>{{item.value}}:</span>
                    <span :title="item[item.paramCode]">&nbsp;{{item[item.paramCode]}}</span>
                  </li>
                </ul>
              </div>
              <div class="mainData clearFloat">
                <!-- 重要信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{item.expectQuantity}}</span>
                    <span>预约:</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>&nbsp;{{item.packDescribe}}</span>
                    <span>单位:</span>
                  </li>
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                  </li>
                  <li>
                    <span>供应商:</span>
                    <span :title="item.supplierName">&nbsp;{{item.supplierName}}</span>
                  </li>
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="asnDetails.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "MixPlateAccept",
    data() {
      return {
        headerTitle: "混托盘验收",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        asnDetails: [],
        asnUdfDs: [],

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          toBoxCode: '',
          itemCode: '',
        },
        rules: {
          toBoxCode: [
            {required: true, message: '追踪号不得为空', trigger: 'blur'}
          ],
        },
      };
    },
    methods: {
      getAsnDetailData(){
        this.loading = true;
        let searchData = this.searchData;
        if(!PF.isNull(searchData.toBoxCode)){
          delete searchData.toBoxCode;
        }
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode;
        }
        IOT.getServerData('/asn/details/list','get',searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].toQuantity = list[i].expectQuantity;
            }
            this.asnDetails = list;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if(this.searchData.pageSize >= ret.total){
              this.noMore = true;
            }else{
              this.noMore = false;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      lazy(){
        if(this.asnDetails.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 10;
          this.getAsnDetailData();
        }
      },
      commitForm(formName,row){
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {

            let form = PF.JSON(row);
            form.toBoxCode = this.searchData.toBoxCode;

            if(!PF.isNull(form.toBoxCode)){
              IOT.tips('未填写追踪号！', 'error', 1000, () => {
                IOT.hideOverlay();
                return false
              });
            }
            if(!PF.isNull(form.toQuantity)){
              IOT.tips('未填写验收数量！', 'error', 1000, () => {
                IOT.hideOverlay();
                return false
              });
            }
            if(!PF.isNull(form.packDetailId)){
              IOT.tips('未选择单位！', 'error', 1000, () => {
                IOT.hideOverlay();
                return false
              });
            }

            IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: [form]}, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('验收成功！', 'success');
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
      commitAccept(row){
        this.$refs['searchData'].validate((valid) => {
          if (valid) {
            /*if(!PF.isNull(this.searchData.toBoxCode)){
              IOT.tips('请输入追踪号！', 'error');
              return false
            }*/
            row.toBoxCode = this.searchData.toBoxCode;
            this.$router.push({name: 'mixPlateAccept2',params: row})
          } else {
            console.error('error submit!!');
            return false;
          }
        });

      }
    },
    created() {
      PF.getSysParamUdfData('asnUdfDs', [], (data, udfs) => {
        this.asnUdfDs = udfs;
      });
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .mixPlateAccept {
  }
</style>
