<template>
  <div class="inventoryTransact">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>SKU</span>
          <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
        </div>
        <div>
          <span>SKU名称</span>
          <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
        </div>
        <div>
          <span>事务类型</span>
          <el-select v-model="searchData.transactType" size="mini" filterable placeholder="请选择事务类型" clearable>
            <el-option
              v-for="item in transactTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>单据类型</span>
          <el-select v-model="searchData.billType" size="mini" filterable placeholder="请选择单据类型" clearable>
            <el-option
              v-for="item in billTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <!-- <div>
          <span>原因</span>
          <el-input v-model="searchData.reason" size="mini" placeholder="原因" clearable></el-input>
        </div> -->
        <div>
          <span>单据号</span>
          <el-input v-model="searchData.detailNo" size="mini" placeholder="单据号" clearable></el-input>
        </div>
        <div>
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择状态" clearable>
            <el-option :key="1" label="完成" :value="1"></el-option>
            <el-option :key="-1" label="撤回" :value="-1"></el-option>
          </el-select>
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
          <span>来自批次名称</span>
          <el-input v-model="searchData.fromBatchName" size="mini" placeholder="来自批次名称" clearable></el-input>
        </div>
        <div>
          <span>来自追踪号</span>
          <el-input v-model="searchData.fromBoxCode" size="mini" placeholder="来自追踪号" clearable></el-input>
        </div>
        <div>
          <span>来自货位编码</span>
          <el-input v-model="searchData.fromCellCode" size="mini" placeholder="来自货位编码" clearable></el-input>
        </div>
        
        <div>
          <span>调入批次名称</span>
          <el-input v-model="searchData.toBatchName" size="mini" placeholder="调入批次名称" clearable></el-input>
        </div>
        <div>
          <span>调入追踪号</span>
          <el-input v-model="searchData.toBoxCode" size="mini" placeholder="调入追踪号" clearable></el-input>
        </div>
        <div>
          <span>调入货位编码</span>
          <el-input v-model="searchData.toCellCode" size="mini" placeholder="调入货位编码" clearable></el-input>
        </div>
       
        <div>
          <span>创建人</span>
          <el-input v-model="searchData.createUserName" size="mini" placeholder="创建人" clearable></el-input>
        </div>


      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
      v-loading  ="loading"
        highlight-current-row
        :data="inventoryTransacts"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          sortable
          prop="transactType"
          label="事务类型"
          width="100">
          <template slot-scope="scope">
            <span v-for="(item,index) in transactTypes" :key="index" v-if="scope.row.transactType === item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        
        
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemCode"
          label="SKU"
          width="130">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          sortable
          show-overflow-tooltip
          prop="wareName"
          label="仓库"
          width="150">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
          </template>
        </el-table-column> -->
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="fromOrganizationName"
                label="货主"
                width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.fromOrganizationName!=null"  >{{scope.row.fromOrganizationName}}</span>
           <br  v-if="scope.row.fromOrganizationName!=null" />
            <span  v-if="scope.row.toOrganizationName!=null"  >{{scope.row.toOrganizationName}}</span>
          
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="fromBatchName"
          label="批次"
          width="120">
           <template slot-scope="scope">
            <!-- <span   v-if="scope.row.fromBatchName!=null"   > {{scope.row.fromBatchName}}</span>
            <br  v-if="scope.row.fromBatchName!=null"  /> -->
            <span  > {{scope.row.toBatchName}}</span>
          </template>
           
          
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="fromBoxCode"
          label="追踪号"
          width="120">
          <template slot-scope="scope">
            <span     v-if="scope.row.fromBoxCode!=null" > {{scope.row.fromBoxCode}}</span>
            <br  v-if="scope.row.fromBoxCode!=null" />
            <span  > {{scope.row.toBoxCode}}</span>
          </template>
           
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="CellCode"
          label="货位编码"
          width="150">
          <template slot-scope="scope">
            <span     v-if="scope.row.fromCellCode!=null"  > {{scope.row.fromCellCode}}</span>
            <br   v-if="scope.row.fromCellCode!=null" />
            <span  > {{scope.row.toCellCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="fromPackDescribe"
          label="单位"
          width="120">
          <template slot-scope="scope">
            <!-- <span    v-if="scope.row.fromPackDescribe!=null"  > {{scope.row.fromPackDescribe}}</span>
            <br   v-if="scope.row.fromPackDescribe!=null"/> -->
            <span  > {{scope.row.toPackDescribe}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="fromQuantity"
          label="主单位数量"
          width="120">
          <template slot-scope="scope">
            <!-- <span    v-if="scope.row.fromQuantity!=null"   > {{scope.row.fromQuantity}}</span>
            <br  v-if="scope.row.fromQuantity!=null"  /> -->
            <span  > {{scope.row.toQuantity}}</span>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="createUserName"
          label="创建人"
          width="90">
          <template slot-scope="scope">
            <el-button @click="$parent.showPublicModal(scope.row.createUserId, 'account')" type="text">{{scope.row.createUserName}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="createTime"
          label="事务时间"
          width="160">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(0,16)}}
          </template>
        </el-table-column>

        <el-table-column
          sortable
          prop="billType"
          label="单据类型"
          width="100">
          <template slot-scope="scope">
            <span v-for="(item,index) in billTypes" :key="index" v-if="scope.row.billType === item.value">{{item.label}}</span>
          </template>
        </el-table-column>

        <el-table-column
          sortable
          show-overflow-tooltip
          prop="detailNo"
          label="单据号"
          width="120">
        </el-table-column>

        <!-- <el-table-column
          sortable
          show-overflow-tooltip
          prop="state"
          label="状态"
          width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.state === 1">完成</span>
            <span v-if="scope.row.state === -1">撤回</span>
          </template>
        </el-table-column>
        
        <el-table-column
          sortable
          show-overflow-tooltip
          prop="reason"
          label="原因"
          width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.reason === null">-</span>
            <span v-else>{{scope.row.reason}}</span>
          </template>
        </el-table-column> -->

        <el-table-column fixed="right" label="操作" min-width="120">
                <template slot-scope="scope">
                  <span v-show="scope.row.transactImgs" style="color:#409eff;cursor: pointer;" @click="showImgs(scope.row.transactImgs)">查看拍照记录</span>
                  </template>
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

      <!-- 图片预览的弹窗 -->

      <el-dialog
        title="在线预览"
        :visible.sync="imgsVisible"
         width="50%"
         center
        :before-close="handleClose">
        <span style="color:#409eff;float:right;;cursor: pointer;" @click="dowmLoadImg(showImsList)">下载全部附件</span>
       <!-- 轮播图片  -->
       <template>
        <el-carousel :autoplay=false indicator-position="none" arrow="always" style="width:100%;height:100%" class="imgs">
          <el-carousel-item v-for="item in showImsList" :key="item" class="carousel-item">
              <img :src="item" alt="" class="carousel-img">
          </el-carousel-item>
        </el-carousel>
      </template>
      <p style="font-weight: bold;margin-left:50px;margin-top: 20px;">备注：货位移动，拍照如上图所示</p>
        <span slot="footer" class="dialog-footer">
          <el-button @click="imgsVisible = false" type="primary">关 闭</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {mapState} from "vuex";
  import down from '../../../downloadAxios'

  export default {
    name: 'InventoryTransact',
    data() {
      return {
        isMoreSearch: false,
        imgsVisible:false,
        searchData: {
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          transactType:'',
          itemCode:'',
          itemName:'',
          billType:'',
          detailNo:'',
          state:'',
          reason:'',
          fromBatchName:'',
          fromBoxCode:'',
          fromCellCode:'',
          fromPackDescribe:'',
          fromQuantity:'',
          toBatchName:'',
          toBoxCode:'',
          toCellCode:'',
          toPackDescribe:'',
          toQuantity:'',
          createUserName:'',
          createTimeArr:[],
          startCreateTime:'',
          endCreateTime:'',

        },

        inventoryTransacts: [],

        dialogNewVisible: false,
        dialogUpdateVisible: false,
        loading:false,

        formLabelWidth: '120px',
        showImsList:[],
        originImgs:[]

      }
    },
    computed: {
      ...mapState([
        "transactTypes",
        "billTypes",
      ])
    },
    methods: {
      //下载全部图片
      dowmLoadImg(urls){
          urls.forEach((url) => {
              const pendingUrl = url.substring(url.lastIndexOf("/") + 1);
              const fileName = pendingUrl.split('?')[0];
              PF.download(url,fileName);
          });
      },
      handleClose(done) {
        done();
      },
      showImgs(transactImgs){
        //获取图片地址的全路径
        this.showImsList = [];
        let imgs = transactImgs.split(",");
        this.originImgs = imgs;
        for(var i=0;i<imgs.length;i++){
          this.openImageUrl(imgs[i])
        }
        this.imgsVisible = true;

      },
      //获取图片地址
      openImageUrl(fullPath) {
          IOT.getServerData('/file/getUrl', 'get', { fullPath }, (ret) => {
            this.showImsList.push(ret.data.url)
          },true);
        },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getInventoryTransactData();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getInventoryTransactData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
        this.searchData.itemCode = '';
        this.searchData.itemName = '';
        this.searchData.transactType = '';
        this.searchData.buyType = '';
        this.searchData.detailNo = '';
        this.searchData.state = '';
        this.searchData.fromBatchName = '';
        this.searchData.fromBoxCode = '';
        this.searchData.fromCellCode = '';
        this.searchData.fromPackDescribe = '';
        this.searchData.fromQuantity = '';
        this.searchData.toBatchName = '';
        this.searchData.toBoxCode = '';
        this.searchData.toCellCode = '';
        this.searchData.toPackDescribe = '';
        this.searchData.toQuantity = '';
        this.searchData.createUserName = '';
        this.searchData.createTimeArr = [];
        this.searchData.startCreateTime = '';
        this.searchData.endCreateTime = '';
        this.searchData.billType = ''
      },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getInventoryTransactData();
      },
      getInventoryTransactData() {
        let searchData = PF.JSON(this.searchData);
        if (searchData.createTimeArr !=null && searchData.createTimeArr.length > 0) {
          searchData.startCreateTime = searchData.createTimeArr[0];
          searchData.endCreateTime = searchData.createTimeArr[1];
        }
        this.loading  =true;
        IOT.getServerData('/inventory/transacts/list', 'get', searchData, (ret) => {
          this.loading  =false;
          if (ret.code === 200) {
            this.inventoryTransacts = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getInventoryTransactData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .inventoryTransact {
    .imgs{
      .carousel-item {
    width: 100%;
    height: 100%;
    background: white;
    display: flex;
    justify-content: center;
  }
  .carousel-img {
    max-width: 100%;
    max-height: 100%;
  }

    }
  }
</style>
