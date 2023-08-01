<template>
  <div class="inCellTask">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getTaskData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="执行货位" clearable></el-input>
        </div>
        <div>
                    <span>状态</span>
                    <el-select v-model="searchData.state" size="mini" filterable placeholder="状态" clearable>
                        <el-option  key=0    value=0 style="color:#409EFF"  label="作业中"  ></el-option>
                     
                        <el-option  key=1    value=1  style="color:#67C23A"  label="完成" > </el-option>
                       


                           
                    </el-select>
          </div>

        <div>
                    <span>类型</span>
                    <el-select v-model="searchData.type" size="mini" filterable placeholder="类型" clearable>
                        <el-option  key=1    value=1 style="color:#409EFF"  label="直接上架"  ></el-option>
                     
                        <el-option  key=0    value=0  style="color:#67C23A"  label="上架车" > </el-option>
                       


                           
                    </el-select>
          </div>
      </div>
    </div>

    <!--<div class="headerBtns">
      <el-button size="small" type="primary"    icon="iconfont icon-xinzeng" @click="showFreezeModal">冻结库存</el-button>
    </div>-->

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="inCellTasks"
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
          label="SKU名称">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column

          show-overflow-tooltip
          prop="itemCode"
          label="SKU">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="quantity"
          label="数量">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="type"
          label="类型">
           <template slot-scope="scope">
              <span  v-if="scope.row.type === 0"    >上架车  </span> 
              <span   v-if="scope.row.type === 1"    >直接上架</span>  
          </template>
        </el-table-column>
       <el-table-column
                prop="fromCellCode"
                label="原始货位"
        ></el-table-column>
     <el-table-column
                prop="sjcCellCode"
                label="上架车位置"
        ></el-table-column>
         <el-table-column
                prop="toCellCode"
                label="推荐货位"
        ></el-table-column>
        <el-table-column
                prop="toCellCode2"
                label="执行货位"
        ></el-table-column>
        <el-table-column
                prop="createTime"
                label="创建时间"
                min-width="100"
        ><template slot-scope="scope">
               {{scope.row.createTime.substr(2,14)}}
          </template>
        </el-table-column>
       
       
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="状态">
           <template slot-scope="scope">
              <span   v-if="scope.row.state === 0"  style="color:#409EFF"  >作业中  </span> 
              <span   v-if="scope.row.state === 1"    style="color:#67C23A"  >完成</span>  
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
       class="pagination-bottom"
        style="text-align: right;"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"      
        :current-page.sync="searchData.pageNum"
        :page-sizes="[15, 30, 50, 100]"
        :page-size="searchData.pageSize"
        layout="sizes,total, prev, pager, next"
        :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'InCellTask',
    data() {
      return {
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          wareId: PF.getLocal('wareId', 'number'),
          pageSize: 15,
          total: 3,
          keyWords: ''
        },

        inCellTasks: [],

        rules: {
        }
      }
    },
    methods: {
      handleCurrentChange(val){
        this.searchData.pageNum = val;
        this.getTaskData();
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getTaskData();
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.type = null;
        this.searchData.state = '';
      },
      getTaskData() {
        IOT.getServerData('/sjc/upper/tasks/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.inCellTasks = ret.rows
            this.searchData.total = ret.total
            this.searchData.pageNum = ret.pageNum
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getTaskData()
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .toCellInventory {

  }
</style>
