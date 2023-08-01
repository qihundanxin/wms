<template>
  <div class="reportOperate">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSoDayReportData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <!-- <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="仓库/货主/物料/客户" clearable></el-input>
        </div> -->
        <div>
          <span>用户名</span>
          <el-input v-model="searchData.userName" size="mini" placeholder="用户名" clearable></el-input>
        </div>
        <div>
          <span>操作类型</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="操作类型" clearable> 
            
            <el-option key=1 value=1 style="color:#409EFF" label="验收"></el-option> 
            <el-option key=2 value=2 style="color:#543044" label="上架"></el-option>
            <el-option key=3 value=3 style="color:#E6A23C" label="拣货"></el-option>
            <!-- <el-option key=8 value=8 style="color:#67C23A" label="已发运"></el-option> -->
          </el-select>
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
      <!-- <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span> -->
    </div>

    <div class="headerBtns">
      <el-button size="small" type="info" round icon="iconfont icon-rizhi" @click="$router.push({name: 'reportOperateDetail'})">明细</el-button>
 
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
        <!-- <el-table-column
          
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn"
                  @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
        
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
          </template>
        </el-table-column> -->
        <el-table-column
          
          show-overflow-tooltip
          prop="userId"
          label="用户ID"
          min-width="150">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="userName"
          label="用户名"
          min-width="150">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="reportDate"
          label="日期"
          min-width="150">
          
        </el-table-column>
         <el-table-column
          
          show-overflow-tooltip
          prop="type"
          label="操作类型"
          min-width="80">
           <template slot-scope="scope">
             <span v-if="scope.row.type==1">验收</span>
             <span v-if="scope.row.type==2">上架</span>
             <span v-if="scope.row.type==3">拣货</span>
           </template>
         
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="quantity1"
          label="操作数"
          min-width="150">
          
        </el-table-column>
        <el-table-column
          
          show-overflow-tooltip
          prop="quantity2"
          label="完成单据数"
          min-width="150">
          
        </el-table-column>
         <!-- <el-table-column
          
          show-overflow-tooltip
          prop="quantity2"
          label="完成单据数"
          min-width="150">
         
          
        </el-table-column> -->
        <el-table-column
         show-overflow-tooltip
          prop="quantity2"
          label="明细"
          min-width="150">
           <template slot-scope="scope">
                        <span
                         class="el-icon-more"
                            
                            @click="
                                $router.push({
                                    name: 'reportOperateDetail',
                                    params: scope.row,
                                })
                            "
                        ></span
                        >
          </template>
          
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
          userName:'',
          type:'',
          pageNum: 1,
          pageSize: 10,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          keyWords: '',
          item_code: '',
          times: [],
          startTime: '',
          endTime: '',
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
        this.searchData.userName = '';
        this.searchData.type = '';
        this.searchData.startTime = '';
        this.searchData.endTime = '';
      },
      getSoDayReportData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(searchData.times.length > 0){
          searchData.startTime = searchData.times[0];
          searchData.endTime = searchData.times[1];
        }
        IOT.getServerData('/report/operates/list', 'get', searchData, (ret) => {
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
