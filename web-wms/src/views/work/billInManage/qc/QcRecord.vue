<template>
  <div class="qcRecord">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getBuyBillInData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div>
        <div>
          <span>单据来源</span>
          <el-select v-model="searchData.billType" size="mini" filterable placeholder="请选择单据来源" clearable>
            <el-option :key="0" label="入库单" :value="0"></el-option>
            <el-option :key="1" label="新建" :value="1"></el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button class="rightFloat" size="small" type="info" plain    icon="iconfont icon-backup" @click="commitFile" :disabled="multipleSelection.length <= 0"> 归档</el-button>
    </div>

    <div class="tableBox none">
      <el-table
      v-loading="loading.masterTable"
        highlight-current-row
        :data="buyBillIns"
        :span-method="objectSpanMethod"
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
            prop="qcBillNo"
            label="单号"
            width="180">
          <template slot-scope="scope">
            <span>QC单号: </span>
            <span class="textBtn" @click="$router.push({name: 'qcBillDetail',params: {billNo: scope.row.qcBillNo}})">{{scope.row.qcBillNo}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="qcDetailNo"
            label="详情单号"
            width="120">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="billType"
            label="单据来源"
            width="100">
          <template slot-scope="scope">
              <el-tag v-if="scope.row.billType === 0" type="info">入库单</el-tag>
              <el-tag v-else-if="scope.row.billType === 1">新建</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qcQuantity"
          label="质检数量"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="packDescribe"
          label="单位"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="checkQuantity"
          label="已检数量"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="realQuantity"
          label="实检数量"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qualifiedQuantity"
          label="合格数量"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="fiedQuantity"
          label="不合格数量"
          width="120">
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="one"
          label="合格率"
          width="120">
          <template slot-scope="scope">
            <span>{{scope.row.one}}%</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="two"
          label="不合格率"
          width="120">
          <template slot-scope="scope">
            <span>{{scope.row.two}}%</span>
          </template>
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="two"
                label="最小合格率"
                width="120">
          <template slot-scope="scope">
            <span>{{scope.row.minPassRate}}%</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="qcUserName"
            label="质检人"
            width="180">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.qcUserId, 'account')" type="text">{{scope.row.qcUserName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="qcTime"
          label="质检时间"
          width="180">
          <template slot-scope="scope">
            {{scope.row.qcTime.substr(5,14)}}
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          v-for="(item,index) in qcUdfDs"
          :key="index"
          :prop="item.paramCode"
          :label="item.value"
          width="180">
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
  import {mapState} from "vuex";

  export default{
    name: 'QcRecord',
    data(){
      return {
        loading:{
            masterTable:true,
        },
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          billType: null,
        },

        buyBillIns: [],
        rows: [],
        spans: [],

        formLabelWidth: '120px',

        multipleSelection: [],
        qcUdfDs: [],

      }
    },
    computed: {
      ...mapState([
        "reviewStates",
        "asnStates"
      ])
    },
    methods: {

     handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getBuyBillInData()
      },
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getBuyBillInData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.billType = null;
      },
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        let index = this.rows.indexOf(rowIndex);
        if (column.property === 'qcBillNo') {
          if (index >= 0) {
            return this.spans[index];
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
      getBuyBillInData(){
         let that = this;
        that.loading.masterTable = true;
        IOT.getServerData('/qc/records/list','get',this.searchData,(ret) => {
          that.loading.masterTable = false;
          if (ret.code === 200) {
            let list = ret.rows;

            let billNos = [];
            let spans = [];
            let rows = [];
            for (let i = 0; i < list.length; i++) {
              let index = billNos.indexOf(list[i].qcBillNo);
              if (index === -1) {
                billNos.push(list[i].qcBillNo);
                rows.push(i);
                spans.push({rowspan: 1, colspan: 1});
              } else if (index >= 0) {
                spans[index].rowspan++
              }
            }
            this.rows = rows;
            this.spans = spans;

            this.buyBillIns = list;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            PF.getSysParamUdfData('qcUdfDs', [], (data, udfs) => {
              this.qcUdfDs = udfs;
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      commitFile(){

        IOT.showOverlay('保存中，请稍等...');
        let list = PF.JSON(this.multipleSelection);
        let detailNos = [];
        for(let i=0;i<list.length;i++){
          detailNos.push(list[i].qcDetailNo)
        }
        IOT.getServerData('/qc/records/file','post',{detailNos: detailNos},(ret) => {
          IOT.hideOverlay();
          this.multipleSelection = [];
          if (ret.code === 200) {
            IOT.tips('归档成功！', 'success');
            this.getQcBillData();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getBuyBillInData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .qcRecord{

  }
</style>
