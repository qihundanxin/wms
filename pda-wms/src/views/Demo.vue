<template>
  <div class="demo">
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
          </el-select>
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
        <li v-for="(item,index) in asnBills">
          <el-card class="clearFloat">
            <div class="cardBody">
              <div class="topData clearFloat">
                <!-- 头部信息 -->
                <span>{{index+1}}/{{searchData.total}}</span>
                <span>D3型卸扣</span>
                <span>IM001</span>
              </div>
              <div class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>单号:</span>
                    <span>&nbsp;{{item.billNo}}</span>
                  </li>
                  <li>
                    <span>来源:</span>
                    <span v-if="item.billSource === 0">采购单</span>
                    <span v-else-if="item.billSource === 1">新建</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                  </li>
                </ul>
              </div>
              <div class="mainData clearFloat">
                <!-- 重要信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>&nbsp;{{item.billNo}}</span>
                    <span>单号</span>
                  </li>
                  <li>
                    <span v-if="item.billSource === 0">采购单</span>
                    <span v-else-if="item.billSource === 1">新建</span>
                    <span>来源</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>&nbsp;{{item.wareName}}</span>
                    <span>仓库</span>
                  </li>
                  <li>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}{{item.organizationName}} </span>
                    <span>货主</span>
                  </li>
                </ul>
              </div>
              <div class="baseData clearFloat">
                <!-- 基础信息 -->
                <ul class="leftFloat">
                  <li>
                    <span>单号:</span>
                    <span>&nbsp;{{item.billNo}}</span>
                  </li>
                  <li>
                    <span>来源:</span>
                    <span v-if="item.billSource === 0">采购单</span>
                    <span v-else-if="item.billSource === 1">新建</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                  <li>
                    <span>货主:</span>
                    <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
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
                    <span>仓库:</span>
                    <span>&nbsp;{{item.wareName}}</span>
                  </li>
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>需冻数:</span>
                    <span>
                    <el-input-number v-model="item.freezeQuantity1" size="mini" controls-position="right" placeholder="需冻数" style="width: 100%;"></el-input-number>
                  </span>
                  </li>
                </ul>
              </div>
            </div>
            <div class="cardBottomBtn clearFloat">
              <!-- 底部按钮 -->
              <div class="leftFloat"></div>
              <div class="rightFloat" @click="commitFreeze(item)">冻结</div>
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
    name: "Demo",
    data() {
      return {
        headerTitle: "Demo",
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
          pageSize: 2,
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
        IOT.getServerData('/asn/masters/list','get',this.searchData,(ret) => {
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
  .demo {
  }
</style>
