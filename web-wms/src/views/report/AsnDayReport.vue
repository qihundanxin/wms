<template>
  <div class="asnDayReport">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getAsnDayReportData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="仓库/货主/物料" clearable></el-input>
        </div>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>验收时间</span>
          <el-date-picker
            v-model="searchData.acceptTimeArr"
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
                    :data="asnDayReport"
                    highlight-current-row
                    style="width: 100%" >
      
       
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
                prop="wareName"
                label="仓库"
        ></el-table-column>
          <el-table-column
                prop="organizationName"
                label="货主"
        ></el-table-column>
        <el-table-column
         show-overflow-tooltip
                prop="itemName"
                label="SKU名称"
        ></el-table-column>
        <el-table-column
                prop="itemCode"
                label="SKU"
        ></el-table-column>
        
        
        <el-table-column
          
          prop="packDescribe"
          label="单位"
          >
        </el-table-column>
        <el-table-column
          
          prop="quantity"
          label="数量"
           >
        </el-table-column>
        <el-table-column
         
          prop="acceptTime"
          label="验收时间"
           >
        </el-table-column>
      </el-table>
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
    name: 'asnDayReport',
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
          itemCode:'',
          acceptTimeArr:[],
          startAcceptTime:'',
          endAcceptTime:'',
        },

        asnDayReport: [],
        dialogUpFileVisible: false,

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        multipleSelection: [],

        formLabelWidth: '120px',

      }
    },
    methods: {

      exportReport(){
        let searchData = PF.JSON(this.searchData);
        let that = this;
        IOT.showOverlay('正在导出，请稍等...');


        IOT.getServerData('/accept/records/exportDayList', 'get', searchData, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            var path = ret.data;

            window.open(path)
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);



      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getAsnDayReportData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.itemCode = '',
        this.searchData.acceptTimeArr = [];
        this.searchData.startAcceptTime = '';
        this.searchData.endAcceptTime = '';
      },
      getAsnDayReportData() {
        let that = this;
        that.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        if(searchData.acceptTimeArr.length > 0){
          searchData.startAcceptTime = searchData.acceptTimeArr[0];
          searchData.endAcceptTime = searchData.acceptTimeArr[1];
        }
        IOT.getServerData('/accept/records/findDayList', 'get', searchData, (ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            this.asnDayReport = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getAsnDayReportData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  
</style>
