<template>
  <div class="freezeDetails">

    <div class="publicSearch">
      <div class="searchBar" :style="{height:'auto'}">
        <div>
          <span>冻结单号</span>
          <el-input v-model="searchData.billNos" size="mini" placeholder="请使用,分割单号" clearable ></el-input>
        </div>
        <div>
          <span>sku</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="请输入sku" clearable ></el-input>
        </div>
        <div>
          <span>创建时间</span>
          <el-date-picker
                  v-model="searchData.createTimeArr"
                  size="mini"
                  type="daterange"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div>
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择状态" clearable>
            <el-option
                    v-for="item in freezeDetailState"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>冻结原因</span>
          <el-select v-model="searchData.freezeDescribe" size="mini" filterable placeholder="请选择冻结原因" clearable>
            <el-option
                    v-for="item in freezeReasons"
                    :key="item.dictKey"
                    :label="item.dictValue"
                    :value="item.dictKey">
            </el-option>
          </el-select>
        </div>
        
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
      </div>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="freezes"
        @sort-change="sortChange"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          prop="itemCode"
          label="sku" width="140">
        </el-table-column>
        <el-table-column
          prop="billNo"
          label="冻结单单号"
          width="140">
          <template slot-scope="scope">
            <span class="textBtn" @click="$router.push({name: 'freezeDetail',params: scope.row})">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>

        <el-table-column
          show-overflow-tooltip
          prop="state"
          label="状态">
          <template slot-scope="scope">
            <el-tag v-show="scope.row.state === 0">正常</el-tag>
            <el-tag v-show="scope.row.state === 1" type="danger">已冻结</el-tag>
            <el-tag v-show="scope.row.state === 2" type="success">已释放</el-tag>
          </template>
        </el-table-column>

        <el-table-column
          show-overflow-tooltip
          prop="itemTypeName"
          label="商品种类">
        </el-table-column>

        <el-table-column
          show-overflow-tooltip
          prop="createUseName"
          label="创建人"
          width="110">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUseId, 'account')" type="text">{{scope.row.createUseName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          sortable="custom"
          show-overflow-tooltip
          prop="createTime"
          label="创建时间"
          width="160">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(0,16)}}
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="freezeQuantity"
          label="冻结数量">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="cellCode"
          label="货位编码"
          width="150">
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="freezeDescribe"
          label="冻结原因"
          width="100">
          <template slot-scope="scope">
            <span v-for="(item,index) in freezeReasons" :key="index" v-if="scope.row.freezeDescribe === item.dictKey">{{item.dictValue}}</span>
          </template>
        </el-table-column>

        <el-table-column
          show-overflow-tooltip
          prop="memo"
          label="备注">
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
    name: 'freezeDetails',
    data(){
      return {

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          insertType: 0,
          wareId: PF.getLocal('wareId', 'number'),
          billNos:'',
          state:'',
          itemCode:'',
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',
          freezeDescribe:'',
          sort:null
        },
        searchSelectInventory:{
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
        },

        wares: [],
        freezes: [],
        inventorys: [],
        multipleSelection: [],
        selectIds: [],
        freezeReasons: [],

        dialogUpdateVisible: false,
        innerVisibleInventory: false,

        formLabelWidth: '120px',

        form: {
          freezeMaster: {
            wareId: PF.getLocal('wareId', 'number'),
            wareName: PF.getLocal('wareName'),
            freezeDescribe: '',
            memo: '',
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
      ...mapState([
        "freezeDetailState",
      ]),
      isFreezeBtn(){
        if(this.multipleSelection.length <= 0){
          return true
        }
        let organizationId = this.multipleSelection[0].organizationId;
        for(let i=0;i<this.multipleSelection.length;i++){
          if(organizationId !== this.multipleSelection[i].organizationId){
            return true
          }
        }
        return false
      }
    },
    methods: {
      sortChange(column){
        if(column.order=="ascending"){
          this.searchData.sort="ASC";
        }else{
          this.searchData.sort="DESC"
        }
        this.getFreezeData();
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getFreezeData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getFreezeData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
        this.searchData.createTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
        this.searchData.billNos = '';
        this.searchData.state = '';
        this.searchData.organizationId = null;
        this.searchData.freezeDescribe = '';
        this.searchData.itemCode = ''

      },
      clearSearchSelectInventory(){
        this.searchSelectInventory.keyWords = '';
        this.searchSelectInventory.wareId = null;
        this.searchSelectInventory.organizationId = null;
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getFreezeData();
      },
      getFreezeData(){
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) { 
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        IOT.getServerData('/freeze/details/findListAndMaster','get',searchData,(ret) => {
          if (ret.code === 200) {
            this.freezes = ret.rows;
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
      this.getFreezeData();
      PF.getDataDictUdfData('freeze_reason', (rows) => {
        this.freezeReasons = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .freezeDetails{
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
</style>
