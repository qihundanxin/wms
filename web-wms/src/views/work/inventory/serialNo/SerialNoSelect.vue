<template>
  <div class="serialNoSelect">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getSerialNoData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
              <span>关键字搜索</span>
              <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU/SKU名称" clearable></el-input>
        </div>
        <div>
          <span>类型</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="请选择类型" clearable>
            <el-option
                    v-for="item in serialNoTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
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
        v-loading="loading.masterTable"
        @selection-change="handleSelectionChange"
        :data="serialNos"
        style="width: 100%">
        <el-table-column
                label="序号"
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
          fixed="left"
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库名称"
          width="180">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="type"
                label="类型"
                width="180">
          <template slot-scope="scope">
            <el-tag v-for="(item,index) in serialNoTypes" :key="index" v-if="scope.row.type === item.value" >{{item.label}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="itemCode"
                label="SKU"
                width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="180">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="serialNo"
            label="序列号"
            width="100">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="organizationName"
          label="货主"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="batchName"
          label="批次"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="quantity"
          label="数量"
          width="180">
        </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="createTime"
                  label="创建时间"
                  width="180">
              <template slot-scope="scope" :v-if="scope.row.createTime !== null">
                  {{scope.row.createTime.substr(0,19)}}
              </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="createUserName"
                  label="创建人"
                  width="180">
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="inDate"
                  label="入库日期"
                  width="180">
              <template slot-scope="scope" :v-if="scope.row.inDate !== null">
                  {{scope.row.inDate.substr(0,10)}}
              </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="productDate"
                  label="生产日期"
                  width="180">
              <template slot-scope="scope" :v-if="scope.row.productDate !== null">
                  {{scope.row.productDate.substr(0,10)}}
              </template>
          </el-table-column>
          <el-table-column
                  sortable
                  show-overflow-tooltip
                  prop="exDate"
                  label="失效日期"
                  width="180">
              <template slot-scope="scope" :v-if="scope.row.exDate !== null">
                  {{scope.row.exDate.substr(0,10)}}
              </template>
          </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    name: 'serialNoSelect',
    data(){
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          insertType: -2,
          wareId: PF.getLocal('wareId', 'number'),
          type:'',
        },
        loading:{
          masterTable:true,

        },
        serialNos: [],
        organizations:[],
        formLabelWidth: '100px',

      }
    },
    computed: {
      ...mapState([
        "serialNoTypes",

      ])
    },
    methods: {
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getPickTaskData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getPickTaskData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      clearSearchData(){
          this.searchData.keyWords = '';
          this.searchData.type='';
      },
      getSerialNoData(){
        let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/serial/nos/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.serialNos = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            that.loading.masterTable = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

    },
    created() {
      this.getSerialNoData();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
      /*PF.getSysParamUdfData('poUdfHs', [], (data, udfs) => {
        this.poUdfHs = udfs;
      });
      PF.getDataDictUdfData('buyBillInType', (rows) => {
        this.buyBillInTypes = rows;
      });

      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });*/
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .buyBillInDraft{

  }
</style>
