<template>
  <div class="soBill">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSoData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div>
        <div>
          <span>审核状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择审核状态" clearable>
            <el-option
                    v-for="item in reviewStates"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>货主</span>
          <el-select v-model="searchData.organizationId" size="mini" filterable placeholder="请选择货主" clearable>
            <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>供应商</span>
          <el-select v-model="searchData.supplierId" size="mini" filterable placeholder="请选择供应商" style="width: 70%;">
            <el-option
                    v-for="item in suppliers"
                    :key="item.supplierId"
                    :label="item.supplierName"
                    :value="item.supplierId">
            </el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
              highlight-current-row
              :data="soBills"
              @selection-change="handleSelectionChange"
              style="width: 100%">
        <el-table-column
                type="selection"
                width="55">
        </el-table-column>
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                fixed="left"
                sortable
                show-overflow-tooltip
                prop="billNo"
                label="单号"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'soBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
                fixed="left"
                sortable
                show-overflow-tooltip
                prop="mergeNo"
                label="合并单号"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'soBillDetail',params: {billNo: scope.row.mergeNo}})">{{scope.row.mergeNo}}</span>
            <span></span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="billSource"
                label="单据来源"
                width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.billSource === 0" type="info">采购单</el-tag>
            <el-tag v-else-if="scope.row.billSource === 1">新建</el-tag>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="state"
                label="审核状态"
                width="100">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in reviewStates" :key="index" v-if="scope.row.state === item.value" :type="item.type">{{item.label}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="soType"
                label="发运单类型"
                width="180">
          <template slot-scope="scope">
            <span v-for="(item,index) in soBillTypes" :key="index" v-if="scope.row.soType === item.dataDictDetailId">{{item.dictValue}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="organizationName"
                label="货主"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="customerName"
                label="客户"
                width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.customerId, 'customer')">{{scope.row.customerName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="linkMan"
                label="收货人"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="carrierName"
                label="承运方"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="deliveryTime"
                label="交货时间"
                width="180">
          <template slot-scope="scope">
            {{scope.row.deliveryTime.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="expectTimeFm"
                label="预发时间FM"
                width="180">
          <template slot-scope="scope">
            {{scope.row.expectTimeFm.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="expectTimeTo"
                label="预发时间TO"
                width="180">
          <template slot-scope="scope">
            {{scope.row.expectTimeTo.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="orderNo"
                label="优先级"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                v-for="(item,index) in soUdfHs"
                :key="index"
                :prop="item.paramCode"
                :label="item.value"
                width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="createTime"
                label="创建时间"
                width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="createUserName"
                label="创建人"
                width="180">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{scope.row.createUserName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="expectTime"
                label="预期到货时间"
                width="180">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="备注"
                width="180">
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              @current-change="handleCurrentChange"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              @size-change="handleSizeChange"
              :page-sizes="[15, 30, 50, 100]"
              layout="sizes,total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <div class="rightFloat">
        <el-button @click="commitSelect" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'ChooseSo',
    data(){
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          state: null,
          organizationId: null,
          supplierId: null,
        },

        soBills: [],

        formLabelWidth: '120px',

        soUdfHs: [],
        organizations: [],
        suppliers: [],
        soBillTypes: [],
        multipleSelection: [],
        waveMasterId: this.$route.params.waveMasterId,
      }
    },
    computed: {
      ...mapState([
        "reviewStates",
      ]),
      mergeFlg(){
        let list = PF.JSON(this.multipleSelection);
        let flg = false;
        if(list.length > 1){
          for(let i=0;i<list.length;i++){
            if(list[i].wareId !== list[0].wareId || list[i].organizationId !== list[0].organizationId || list[i].customerId !== list[0].customerId){
              flg = true;
            }
          }
          return flg;
        }else{
          return true;
        }
      }
    },
    methods: {

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getSoData()
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getSoData();
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.state = null;
        this.searchData.organizationId = null;
        this.searchData.supplierId = null;
      },
      getSoData(){
        IOT.getServerData('/so/masters/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.soBills = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      commitSelect() {
        IOT.showOverlay('保存中，请稍等...');
        let that = this;
        
        let list = PF.JSON(this.multipleSelection);
        let soIds = [];
        for(let i=0;i<list.length;i++){
          soIds.push(list[i].soMasterId)
        }
        IOT.getServerData('/wave/details/addFromSo','post',{soMasterIds: soIds, waveMasterId: that.waveMasterId},(ret) => {
          IOT.hideOverlay();

          this.multipleSelection = [];
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success');
            this.$router.push('WaveMaster')
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {

      this.getSoData();
      PF.getSysParamUdfData('soUdfHs', [], (data, udfs) => {
        this.soUdfHs = udfs;
      });
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getDataDictUdfData('soBillType', (rows) => {
        this.soBillTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .soBill{

  }
</style>
