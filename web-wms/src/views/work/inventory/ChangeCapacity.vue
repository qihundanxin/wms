<template>
  <div class="changeCapacity">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getTableData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="创建人/备注" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="merge" :disabled="isMergeBtn">合并</el-button>
    </div>

    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="tableData"
          @selection-change="handleSelectionChange"
          style="width: 100%">
        <el-table-column
            type="selection"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            sortable
            prop="itemCode"
            label="SKU"
            width="200">
        </el-table-column>
        <el-table-column
            sortable
            prop="batchName"
            label="批次"
            width="260">
        </el-table-column>
        <el-table-column
            sortable
            prop="quantity"
            label="数量"
            width="180">
        </el-table-column>
        <el-table-column
            sortable
            prop="weight"
            label="重量"
            width="180">
        </el-table-column>
        <el-table-column
            sortable
            prop="boxCode"
            label="托盘号"
            width="180">
        </el-table-column>
        <el-table-column
            sortable
            prop="cellCode"
            label="货位编码"
            width="180">
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

  export default{
    name: 'ChangeCapacity',
    data(){
      return {
        loading:{
          masterTable:true,
        },
        isMoreSearch: false,
        isMoreSearch2: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
        },

        tableData: [],
        multipleSelection: [],

        formLabelWidth: '120px',

      }
    },
    computed: {
      isMergeBtn(){
        if(this.multipleSelection.length < 2){
          return true
        }
        return false
      }
    },
    methods: {

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getTableData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getTableData(){
        this.loading.masterTable = true;
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/inventorys/palletsToCombine','get',searchData,(ret) => {
          this.loading.masterTable = false;
          if (ret.code === 200) {
            this.tableData = ret.rows;
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
      merge(){
        let form = PF.JSON(this.multipleSelection);
        let itemCode = form[0].itemCode;
        let batchName = form[0].batchName;
        for(let i=0;i<form.length;i++){
          if(
            itemCode !== form[i].itemCode
            || batchName !== form[i].batchName
          ){
            IOT.tips('SKU或批次不同,请核对后合并！', 'error');
            return false
          }
        }
        IOT.showOverlay('保存中，请稍等...');
        IOT.getServerData('/combine/pallet/tasks/insert','post',{details: form},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000 , () => {
              that.getTableData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getTableData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .changeCapacity{
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
</style>
