<template>
  <div class="blindAccept">
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
        <el-form-item label="关键字" :label-width="formLabelWidth">
          <el-input v-model="searchData.keyWords" size="mini" placeholder="ASN单号等" clearable style="width: 90%;"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="searchData.pageSize = 2;items = [];getItemData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>

    <el-divider content-position="center">待验收ASN单</el-divider>

    <div class="baseAcceptBillBox noScrollbar">
      <ul v-infinite-scroll="lazy">
        <li v-for="(item,index) in items" @click="$router.push({name: 'blindAccept2',params: item})">
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
                    <span>规格:</span>
                    <span>&nbsp;{{item.spec}}</span>
                  </li>
                  <li>
                    <span>包装:</span>
                    <span>&nbsp;{{item.packName}}</span>
                  </li>
                  <li>
                    <span>重量:</span>
                    <span>&nbsp;{{item.weight}}</span>
                  </li>
                  <li>
                    <span>种类:</span>
                    <span>&nbsp;{{item.itemTypeName}}</span>
                  </li>
                </ul>
                <ul class="rightFloat">
                  <li>
                    <span>型号:</span>
                    <span>&nbsp;{{item.model}}</span>
                  </li>
                  <li>
                    <span>上限:</span>
                    <span>&nbsp;{{item.upperLimit}}</span>
                  </li>
                  <li>
                    <span>下限:</span>
                    <span>&nbsp;{{item.floorLimit}}</span>
                  </li>
                  <li>
                    <span>单价:</span>
                    <span>&nbsp;{{item.defaultPrice}}</span>
                  </li>
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>批次:</span>
                    <span>&nbsp;{{item.batchTacticName}}</span>
                  </li>
                </ul>
              </div>
              <div class="minorData">
                <!-- 次要/整行信息 -->
                <ul>
                  <li>
                    <span>货主:</span>
                    <span>&nbsp;{{item.organizationName}}</span>
                  </li>
                  <li>
                    <span>供应商:</span>
                    <span>&nbsp;{{item.supplierName}}</span>
                  </li>
                </ul>
              </div>
            </div>

          </el-card>
        </li>
      </ul>
      <p v-show="items.length <= 0 && !noMore">暂无数据</p>
      <p v-show="loading">加载中...</p>
      <p v-show="noMore">未查询到更多数据</p>
    </div>

  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "BlindAccept",
    data() {
      return {
        headerTitle: "盲收",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        items: [],
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
        rules: {
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '请输入物料编码', trigger: 'blur'},
            {validator: validateItemCode, trigger: 'blur'}
          ],
        },
      };
    },
    methods: {
      getItemData(){
        this.$refs['searchData'].validate((valid) => {
          if (valid) {
            this.loading = true;
            IOT.getServerData('/item/infos/list','get',this.searchData,(ret) => {
              this.loading = false;
              if (ret.code === 200) {
                this.items = ret.rows;
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
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });

      },
      lazy(){
        if(this.items.length > 0 && !this.noMore && !this.loading){
          this.searchData.pageSize += 2;
          this.getItemData();
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
  .blindAccept {
  }
</style>
