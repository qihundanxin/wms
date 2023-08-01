<template>
  <div class="soDayReport">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSoDayReportData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="仓库/货主/物料/客户" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>实际发货时间</span>
          <el-date-picker
            v-model="searchData.actualDeliveryTimeArr"
            size="mini"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" plain round icon="iconfont icon-shangjia" @click="exportReport()">导出</el-button>
    </div>

    <div class="tableBox">
      <el-table
        v-loading="loading.masterTable"
        highlight-current-row
        :data="soDayReport"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column>
        <el-table-column
        
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
         
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="180">
        </el-table-column>
        <el-table-column
         
          show-overflow-tooltip
          prop="deliveryQuantity"
          label="发货数量"
          width="180">
        </el-table-column>
        <el-table-column
        
          show-overflow-tooltip
          prop="actualDeliveryTime"
          label="实际发货时间"
          width="180">
        </el-table-column>
      </el-table
         >
      <el-pagination
        class="pagination-bottom"
        style="text-align: right;"
        @current-change="handleCurrentChange"
        :current-page.sync="searchData.pageNum"
        :page-size="searchData.pageSize"
        layout="total, prev, pager, next, jumper"
        :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'SoDayReport',
    data() {
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          keyWords: '',
          item_code: '',
          actualDeliveryTimeArr: [],
          startActualDeliveryTime: '',
          endActualDeliveryTime: '',
        },

        soDayReport: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        multipleSelection: [],

        formLabelWidth: '120px',

      }
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getSoDayReportData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.itemCode = '',
        this.searchData.actualDeliveryTimeArr = [];
        this.searchData.startActualDeliveryTime = '';
        this.searchData.endActualDeliveryTime = '';
      },
      getSoDayReportData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(searchData.actualDeliveryTimeArr.length > 0){
          searchData.startActualDeliveryTime = searchData.actualDeliveryTimeArr[0];
          searchData.endActualDeliveryTime = searchData.actualDeliveryTimeArr[1];
        }
        IOT.getServerData('/so/details/findDayList', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.soDayReport = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      exportReport(){
        let searchData = PF.JSON(this.searchData);
        let that = this;
        IOT.showOverlay('正在导出，请稍等...');

        IOT.getServerData('/so/details/exportDayList', 'get', searchData, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            var path = ret.data;

            window.open(path)
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
    },
    created() {
      this.getSoDayReportData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .soDayReport {

  }
</style>
