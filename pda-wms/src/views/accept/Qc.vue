<template>
  <div class="qc">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" :rules="rules" ref="searchData">
        <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable style="width: 90%;">
            <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="质检单号" prop="billNo" :label-width="formLabelWidth">
          <el-input v-model="searchData.billNo" size="mini" placeholder="质检单号" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="追踪号" prop="boxCode" :label-width="formLabelWidth">
          <el-input v-model="searchData.boxCode" size="mini" placeholder="追踪号" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="searchData.pageSize = 2;qcBills = [];getQcData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待质检详情</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in qcBills" @click="$router.push({name: 'qc2',params: item})">
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
                    <span v-if="item.expectQuantity">&nbsp;{{item.expectQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>预约:</span>
                  </li>
                  <li>
                    <span v-if="item.qcQuantity">&nbsp;{{item.qcQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>质检数:</span>
                  </li>
                  <li>
                    <span v-if="item.realQuantity">&nbsp;{{item.realQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>实检数:</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span v-if="item.packDescribe">&nbsp;{{item.packDescribe}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>单位:</span>
                  </li>
                  <li>
                    <span v-if="item.checkQuantity">&nbsp;{{item.checkQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>已检数:</span>
                  </li>
                  <li>
                    <span v-if="item.qualifiedQuantity">&nbsp;{{item.qualifiedQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>合格数:</span>
                  </li>
                  <li>
                    <span v-if="item.fiedQuantity">&nbsp;{{item.fiedQuantity}}</span>
                    <span v-else>&nbsp;-</span>
                    <span>不合格:</span>
                  </li>
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="qcBills.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "Qc",
    data() {
      return {
        headerTitle: "质量检验",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        qcBills: [],
        organizations: [],
        qcUdfDs: [],
        qcTypes: [],

        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          billNo: 'QC-33',
          organizationId: null,
          boxCode: '',
        },
        rules: {
          billNo: [
            {required: true, message: '请扫描质检单', trigger: 'blur'}
          ],
        },
      };
    },
    methods: {
      getQcData(){
        this.$refs['searchData'].validate((valid) => {
          if (valid) {
            this.loading = true;
            IOT.getServerData('/qc/details/list','get',this.searchData,(ret) => {
              this.loading = false;
              if (ret.code === 200) {
                this.qcBills = ret.rows;
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
          } else {
            console.error('error submit!!');
            return false;
          }
        });

      },
      lazy(){
        if(this.qcBills.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getQcData();
        }
      },
    },
    created() {
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSysParamUdfData('qcUdfDs', [], (data, udfs) => {
        this.qcUdfDs = udfs;
      });
      PF.getDataDictUdfData('qcType', (rows) => {
        this.qcTypes = rows;
      });
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .qc {
  }
</style>
