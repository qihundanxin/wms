<template>
  <div class="baseAccept">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
        <el-form-item label="关键字" :label-width="formLabelWidth">
          <el-input v-model="searchData.keyWords" size="mini" placeholder="ASN单号等" clearable style="width: 90%;"></el-input>
        </el-form-item>
        <el-form-item label="货主" :label-width="formLabelWidth">
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable style="width: 90%;">
            <el-option
              v-for="item in organizations"
              :key="item.organizationId"
              :label="item.organizationName"
              :value="item.organizationId">
            </el-option>
          </el-select>z
        </el-form-item>
        <el-form-item label="供应商" :label-width="formLabelWidth">
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" clearable style="width: 90%;">
            <el-option
              v-for="item in suppliers"
              :key="item.supplierId"
              :label="item.supplierName"
              :value="item.supplierId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-button @click="searchData.pageSize = 2;asnBills = [];getAsnData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待验收ASN单</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in asnBills" @click="$router.push({name: 'baseAccept2',params: item})">
          <el-card class="clearFloat">

            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{asnBills.length}}</span>
                <span>{{item.billNo}}</span>
                <span v-if="item.billSource === 0">采购单</span>
                <span v-else-if="item.billSource === 1">手动创建</span>
              </div>
              <div class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>类型:</span>
                    <span v-for="item in asnTypes" v-if="item.asnType === item.dataDictDetailId">{{item.dictValue}}</span>
                  </li>
                  <li v-if="item.billSource === 0">
                    <span>采购单:</span>
                    <span :title="item.requestBuyBillNo">&nbsp;{{item.requestBuyBillNo}}</span>
                  </li>
                  <li v-for="item in asnUdfHs">
                    <span>{{item.value}}:</span>
                    <span :title="item[item.paramCode]">&nbsp;{{item[item.paramCode]}}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li v-if="item.insertType === 0">
                    <span>审核:</span>&nbsp;
                    <span v-if="item.state === -2">草稿</span>
                    <span v-else-if="item.state === 2">待审核</span>
                    <span v-else-if="item.state === 1">审核通过</span>
                    <span v-else-if="item.state === 0">审核中</span>
                    <span v-else-if="item.state === -1">审核退回</span>
                  </li>
                  <!-- <li>
                    <span>备注:</span>
                    <span>&nbsp;{{item.memo}}</span>
                  </li>
                  <li>
                    <span>发货地:</span>
                    <span>&nbsp;{{item.loadingPlace}}</span>
                  </li>
                  <li>
                    <span>交货地:</span>
                    <span>&nbsp;{{item.deliveryPlace}}</span>
                  </li>
                  <li>
                    <span>运输:</span>
                    <span>&nbsp;{{item.trafficDescribe}}</span>
                  </li> -->
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                  </li>
                  <!-- <li>
                    <span>供应商:</span>
                    <span :title="item.supplierName">&nbsp;{{item.supplierName}}</span>
                  </li> -->
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>创建人:</span>
                    <span :title="item.createUserName">&nbsp;{{item.createUserName}}</span>
                  </li>
                  <li>
                    <span>创建时间:</span>
                    <span>&nbsp;{{item.createTime}}</span>
                  </li>
                  <li>
                    <span>预到货时间:</span>
                    <span>&nbsp;{{item.expectTime}}</span>
                  </li>
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="asnBills.length <= 0">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">没有更多了</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BaseAccept",
    data() {
      return {
        headerTitle: "基础验收",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        asnBills: [],
        organizations: [],
        suppliers: [],
        asnUdfHs: [],
        asnTypes: [],

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          supplierId: null,
        },
      };
    },
    methods: {
      getAsnData(){
        this.loading = true;
        IOT.getServerData('/asn/masters/pdaAccept','get',this.searchData,(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.asnBills = ret.rows;
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
        if(this.asnBills.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getAsnData();
        }
      },
    },
    created() {
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
        this.asnUdfHs = udfs;
      });
      PF.getDataDictUdfData('asnType', (rows) => {
        this.asnTypes = rows;
      });
    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
  .baseAccept {
  }
</style>
