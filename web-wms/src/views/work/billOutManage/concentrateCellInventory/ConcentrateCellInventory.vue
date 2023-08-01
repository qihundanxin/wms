<template>
  <div class="toCellInventory">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getInventoryBalanceData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="货位/追踪号/LPN号" clearable></el-input>
        </div>
        <div>
          <span>SKU名称</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
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
      </div>
<!--      <span class="moreSearchBtn"-->
<!--            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"-->
<!--            @click="isMoreSearch = !isMoreSearch"></span>-->
    </div>

    <div class="tableBox">
      <el-table
      v-loading="loading.masterTable"
        highlight-current-row
        :data="inventoryBalances"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        
        <el-table-column
          fixed="left"
           show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column

          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        
        <el-table-column

          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="batchName"
          label="批次"
          width="150">
        </el-table-column>
         <el-table-column
          sortable
          show-overflow-tooltip
          prop="quantity"
          label="数量"
          width="120">
           <template slot-scope="scope">
            <span  >{{scope.row.quantity}}（主单位）</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="80">
        </el-table-column>
       
       
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="cellCode"
          label="货位"
          width="150">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="boxCode"
          label="追踪号"
          width="150">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="lpn"
          label="LPN码"
          width="150">
        </el-table-column>
      </el-table>
      <el-pagination
       class="pagination-bottom"
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

  </div>
</template>

<script>
  export default {
    name: 'ToCellInventory',
    data() {
      let validateQuantity = (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('数量不得为零'));
          callback();
        } else if (value > 0) {
          callback();
        } else {
          callback(new Error('请输入正确格式'));
        }
      };
      return {
        loading:{
            masterTable:true,
        },
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          cellUseType: 3,
          itemName:'',
          itemCode:''
        },

        inventoryBalances: [],
        organizations: [],
        cells: [],
        multipleSelection: [],

        serialNo1:[],
        scanner:'',
        select:true,

        isFindUpperCell: false,

        formLabelWidth: '120px',

      }
    },
    methods: {
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getInventoryBalanceData()
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryBalanceData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.organizationId = null;
        this.searchData.itemName = null;
        this.searchData.itemCode = null;
      },
      getInventoryBalanceData() {
         let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
        IOT.getServerData('/inventorys/CellUseTypeList', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.inventoryBalances = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
    },
    created() {
      this.getInventoryBalanceData();
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .toCellInventory {

  }
</style>
