<template>
  <div class="ErpRecord">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
   
        
        <div>
          <span>接口</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="请选择接口" clearable>
            <el-option     label="入库成功"   value=1>   </el-option>
            <el-option     label="入库异常"   value=2>   </el-option>
            <el-option     label="出库成功"   value=3>   </el-option>
            <el-option     label="退件信息"   value=4>   </el-option>
          </el-select>
        </div>
        <div>
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择状态" clearable>
            <el-option     label="同步成功"   value=2>   </el-option>
            <el-option     label="同步失败"   value=1>   </el-option> 
          </el-select>
        </div>
        
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

     
 
    <!-- <div class="headerBtns">
      <el-button size="small" type="danger" round icon="iconfont icon-user-jinyong" @click="exportInventory()"  >导出</el-button>
    </div> -->
    <div class="tableBox rowHeight">
      <el-table
      v-loading="loading"
        highlight-current-row
        :data="inventoryBalances"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <!-- <el-table-column
          fixed="left"
          type="selection"
          width="55">
        </el-table-column>
        -->
       
 
     
       <el-table-column 
          show-overflow-tooltip
          prop="type"
          label="接口"
          min-width="100">
          <template slot-scope="scope">
            <span  v-if="scope.row.type==1" style="color:#409EFF">入库成功</span>
            <span  v-if="scope.row.type==2"  style="color:#E6A23C"  >入库异常</span>
             <span  v-if="scope.row.type==3"  style="color:#67C23A"  >出库成功</span>
              <span  v-if="scope.row.type==4"  style="color:#F56C6C"  >退件信息</span>
          </template>
        </el-table-column>
        <el-table-column 
          show-overflow-tooltip
          prop="state"
          label="状态"
          min-width="100">
          <template slot-scope="scope">
            <span  v-if="scope.row.state==1" style="color:#F56C6C">同步失败</span>
            <span  v-if="scope.row.state==2"  style="color:#67C23A"  >同步成功</span>
          </template>
        </el-table-column>
        <el-table-column 
          show-overflow-tooltip
          prop="createTime"
          label="请求时间"
          min-width="160">
         
        </el-table-column>
        <el-table-column 
          show-overflow-tooltip
          prop="returnTime"
          label="返回时间"
          min-width="160">
         
        </el-table-column>
           <el-table-column 
          show-overflow-tooltip
          prop="param"
          label="param"
          min-width="230">
         
        </el-table-column>

        
         <el-table-column
             show-overflow-tooltip
          prop="code"
          label="code"
          min-width="150">
          
        </el-table-column>
        <el-table-column 
              show-overflow-tooltip
          prop="msg"
          label="msg"
          width="150"> 
        </el-table-column>
        
      </el-table>
      <el-pagination
        class="pagination-bottom"
              style="text-align: right;"
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
  export default {
    name: 'ErpRecord',
    data() {
      return {
        loading:false,
        isMoreSearch: false,

        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
          type:'',
          state:''
           
        },

        inventoryBalances: [],
        organizations: [],
        multipleSelection: [],
        freezeReasons: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        dialogFreezeVisible: false,

        formLabelWidth: '120px',

        form: {
          freezeMaster: {
            freezeDescribe: '',
            memo: '',
            wareId: null,
            wareName: '',
            organizationId: null,
            organizationName: '',
          },
          freezeDetails: []
        },
        rules:{
          freezeDescribe: [
            { required: true, message: '请输入冻结原因', trigger: 'blur' }
          ],
          memo: [
            { required: true, message: '请输入备注', trigger: 'blur' }
          ],
        },

      }
    },
    computed: {
     
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryBalanceData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryBalanceData()
      },
      clearSearchData() { 
          this.searchData.type = null;
          this.searchData.state = null;
          
      },
      setPageNumFlush(){
          this.searchData.pageNum =1;
          this.getInventoryBalanceData();
      },
      getInventoryBalanceData() {
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
        
        searchData.pageSize = searchData.pageSize ;
        this.loading  =true;
        IOT.getServerData('/erp/records/list', 'get', searchData, (ret) => {
          this.loading  =false;
          if (ret.code === 200) {
            let list = ret.rows;
            
            this.inventoryBalances = list;
            this.searchData.total = ret.total ;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      exportInventory(){
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.itemCode)){
          delete searchData.itemCode
        }
        if(!PF.isNull(searchData.itemName)){
          delete searchData.itemName
        }
        
        searchData.pageSize = searchData.pageSize ;
        IOT.showOverlay('正在导出，请稍等...');
         

        IOT.getServerData('/erp/records/list', 'get', searchData, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            var path = ret.data;

            window.open(path)
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
     
        
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
     
       
      changePackDetailId(index){
        
        let detail = PF.JSON(this.inventoryBalances[index]);
        for (let i = 0; i < detail.packDetails.length; i++) {
          if (detail.packDetails[i].packDetailId1 === detail.packDetailId) {
            this.inventoryBalances[index].packDescribe = detail.packDetails[i].packDescribe1;
            this.inventoryBalances[index].transRatio = detail.packDetails[i].transRatio1;
          }
        }
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
  .ErpRecord {

  }
</style>
