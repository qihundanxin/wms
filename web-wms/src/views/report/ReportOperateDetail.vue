<template>
  <div class="reportOperate">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSoDayReportData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>用户</span>
          <el-input v-model="searchData.userName" size="mini" placeholder="用户" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>日期</span>
          <el-date-picker
            v-model="searchData.times"
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
     <el-button size="small" type="primary" round icon="iconfont icon-zuo" @click="$router.push({name: 'reportOperate'})">返回</el-button>
    </div>

    <div class="tableBox">
      <el-table
        v-loading="loading.masterTable"
        highlight-current-row
        :data="operates"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        
          <el-table-column
          
          show-overflow-tooltip
          prop="createUserId"
          label="用户ID"
          min-width="80">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="createUserName"
          label="用户名"
          min-width="100">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="createTime"
          label="操作时间"
          min-width="150">
          
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="type"
          label="操作类型"
          min-width="120">
           <template slot-scope="scope">
             <span v-if="scope.row.type==1">验收</span>
             <span v-if="scope.row.type==2">上架</span>
             <span v-if="scope.row.type==3">拣货</span>
           </template>
         
          
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="billNo"
          label="单号"
          min-width="150">
          
        </el-table-column>
    
         <el-table-column
          
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          min-width="150">
          
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          min-width="150">
          
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="imgUrl"
          label="imgUrl"
          min-width="150">
          
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
    name: 'ReportOperate',
    data() {
      return {
        isMoreSearch: false,
        loading:{
          masterTable:true,

        },
        searchData: {
          reportOperateId:this.$route.params.reportOperateId,
          pageNum: 1,
          pageSize: 10,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          keyWords: '',
          itemCode: '',
          times: [],
          startTime: '',
          endTime: '',
          type: '',
          userName: '',
        },

        operates: [],

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
        this.searchData.times = [];
        this.searchData.startTime = '';
        this.searchData.endTime = '';
        this.searchData.type = '';
        this.searchData.userName = '';
        this.searchData.itemCode = '';
      },
      getSoDayReportData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(searchData.times.length > 0){
          searchData.startTime = searchData.times[0];
          searchData.endTime = searchData.times[1]+' 59:59:59';
        }
        IOT.getServerData('/report/operate/details/list', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.operates = ret.rows;
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

        IOT.getServerData('/report/operates/export', 'get', searchData, (ret) => {
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
  .reportOperate {

  }
</style>
