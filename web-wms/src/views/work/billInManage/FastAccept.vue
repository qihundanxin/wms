<template>
  <div class="fastAccept">

    <div class="publicSearch">
      <div class="searchBar" style="height:140px">

        <div>
          <span>自动打印</span>
          <el-switch
              v-model="autoPrint"
              active-color="#13ce66"
          >
          </el-switch>

        </div>
        <div>
          <span>爆款判断</span>
          <el-switch
              v-model="baoKuan"
              active-color="#13ce66"
          >
          </el-switch>

        </div>
        <div>
          <span>验收位置</span>
          <el-select v-model="acceptCellCode" placeholder="请选择" size="mini">
            <el-option
                v-for="cellInfo in cellInfos"
                :key="cellInfo.cellCode"
                :label="cellInfo.cellCode"
                :value="cellInfo.cellCode">
            </el-option>
          </el-select>

        </div>

        <div>
          <el-dropdown style="margin-left:10px;margin-right:10px" size="mini" type="primary"
                       icon="iconfont icon-print" @click="demoPrint()" split-button>
            打印
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button size="small" type="primary" @click="$parent.setPrinter">打印设置</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button style="margin-top:2px; " size="small" type="primary" @click="demoPrint(1)">打印预览1</el-button>
              </el-dropdown-item>

              <el-dropdown-item>
                <el-button style="margin-top:2px; " size="small" type="primary" @click="demoPrint(2)">打印预览2</el-button>
              </el-dropdown-item>

            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <br/>
        <div>
          <span>快递单号</span>
          <!-- <span style="font-size:20px;color:#409EFF;font-weight:bold;">{{shipInfo.shipBillCode}}</span> -->


          <el-input @keyup.enter.native="getAsnBillDetailData" v-model="shipInfo.shipBillCode" size="mini"
                    placeholder="快递单号" clearable></el-input>
        </div>

        <div>
          <span>包裹重量</span>
          <!-- <span style="font-size:20px;color:#409EFF;font-weight:bold;">{{shipInfo.weight}}</span>  -->
          <el-input style="width:100px" v-model="shipInfo.weight" size="mini" placeholder=" " clearable></el-input>

        </div>


        <div>
          <span>长</span>
          <!-- <el-input  style="width:100px"   v-model="shipInfo.length" size="mini" placeholder=" " clearable></el-input> -->
          <el-input :disabled="shipInfo.acceptTime !=null" style="width:80px" v-model="shipInfo.length" size="mini"
                    placeholder="长"></el-input>
        </div>


        <div>
          <span>宽</span>
          <!-- <el-input  style="width:100px"   v-model="shipInfo.width" size="mini" placeholder=" " clearable></el-input> -->
          <el-input :disabled="shipInfo.acceptTime !=null" style="width:80px" v-model="shipInfo.width" size="mini"
                    placeholder="宽"></el-input>
        </div>

        <div>
          <span>高</span>
          <!-- <el-input  style="width:100px"   v-model="shipInfo.height" size="mini" placeholder=" " clearable></el-input> -->
          <el-input :disabled="shipInfo.acceptTime !=null" style="width:80px" v-model="shipInfo.height" size="mini"
                    placeholder="高"></el-input>
        </div>


      </div>
    </div>

    <!--<div class="headerBtns">
      <el-button size="small" type="primary"    icon="iconfont icon-xinzeng" @click="showFreezeModal">冻结库存</el-button>
    </div>-->
    <div class="headerBtns">


      <span v-if="asnMaster.memo!=null"
            style="color:red;font-size:30px">采购备注：{{ asnMaster != null ? asnMaster.memo : '' }}</span>
    </div>

    <div class="tableBox none">
      <el-table
          border
          v-loading="loading.masterTable"
          highlight-current-row
          :data="asnDetails"
          @row-click="clickRow"
          @current-change="handleSelectionChange"
          style="width: 100%">
        <!--  <el-table-column
            type="selection"
            width="30">
          </el-table-column>
          <el-table-column
            fixed="left"
            type="index"
            width="40">
          </el-table-column> -->
        <!--  <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="billNo"
            label="单据信息"
            width="220">
            <template slot-scope="scope">
              <span>入库单号: </span>
              <span class="textBtn" @click="$router.push({name: 'asnBillDetail',params: scope.row})">{{scope.row.billNo}}</span>
              <br>
              <span>货主: </span>
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.supplierId, 'supplier')">{{scope.row.organizationName}}</span>
              <br>
              <span>供应商: </span>
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.supplierId, 'supplier')">{{scope.row.supplierName}}</span>
              <br>
              <span>仓库: </span>
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.wareId, 'ware')">{{scope.row.wareName}}</span>
            </template>
          </el-table-column> -->
        <!-- <el-table-column
           show-overflow-tooltip
           prop="detailNo"
           label="明细单号"
           width="120">
         </el-table-column> -->
        <el-table-column
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="itemName"
            label="缩略图/SKU/SKU名称"
            min-width="300">
          <template slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="8">
                <div style="display: inline-block;margin-left:5px;  width:120px;height:120px">
                  <el-image
                      style="width: 120px; height: 120px"
                      :src="scope.row.imgUrl"
                      :preview-src-list="[scope.row.imgUrl]">
                  </el-image>
                  <!-- <img v-bind:src="scope.row.imgUrl"  style="width:80px;height:86px"> -->
                </div>
              </el-col>
              <el-col :span="16">
                <div style="display: inline-block;margin-left:5px; height:86px">
                    <span class="textBtn" style="color:red"
                    >{{ scope.row.itemCode }}</span>
                  <br>
                  <span
                  >{{ scope.row.itemName }}</span>
                  <br>
                  <span>{{ scope.row.spec }}</span>
                  <br>
                  <span>{{ scope.row.model }}</span>
                </div>
              </el-col>

            </el-row>


          </template>
        </el-table-column>


        <el-table-column
            :render-header="renderHeader"
            show-overflow-tooltip
            prop="expectQuantity"
            label="已验收数/采购数量"
            min-width="130">
          <template slot-scope="scope">
         
            <!-- <span
                style="color:#67C23A;margin-left:2px;font-size:30px">{{
                scope.row.acceptQuantity / scope.row.transRatio
              }}  </span>
            /
            <span
                style="color:#409EFF;margin-left:2px;font-size:30px">  {{
                scope.row.expectQuantity / scope.row.transRatio
              }}</span> -->

               <span
                style="color:#67C23A;margin-left:2px;font-size:30px">{{
                scope.row.acceptQuantity  
              }}  </span>
            /
            <span
                style="color:#409EFF;margin-left:2px;font-size:30px">  {{
                scope.row.expectQuantity  
              }}</span>


            <br>

            <!-- <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index  )" filterable placeholder="请选择单位" size="small">
             <el-option
               v-for="item in scope.row.packDetails"
               :key="item.packDetailId"
               :label="item.packDescribe"
               :value="item.packDetailId">
             </el-option>
           </el-select> -->
          </template>
        </el-table-column>

        <el-table-column
            show-overflow-tooltip
            :render-header="renderHeader"
            prop="toQuantity"
            label="验收信息/异常信息"
            min-width="280">
          <template slot-scope="scope">

            <el-tag v-if="scope.row.state==2 && scope.row.exState==2  && !ex"
                    type="success">已验收
            </el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity == scope.row.expectQuantity && !ex"
                    type="success">已验收
            </el-tag>
            <el-tag
                v-else-if="!ex && scope.row.acceptQuantity > 0 && scope.row.acceptQuantity > scope.row.expectQuantity"
                type="danger">超额验收
            </el-tag>
            <div v-else-if="scope.row.exState==1">
              <el-tag type="danger">异常已经上报，请等待。。</el-tag>
            </div>
            <div style="width:90%" v-else-if="!ex && scope.row.expectQuantity>scope.row.acceptQuantity">
              入库数量：
              <el-input-number
                  :controls='controls'
                  @focus="input0Set"
                  v-model="scope.row.toQuantity"
                  @input="changeFunc(scope.row.toQuantity, scope.$index)"
                  controls-position="right" :min="0"
                  placeholder="" label="请输入数量" size="small"
                  style="width: 90%;"></el-input-number>

              <!-- <div style="height:5px"></div>
              退库数量：<el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.returnQuantity" controls-position="right" :min="0"
                              placeholder="" label="请输入数量" size="small"
                              style="width: 90%;"></el-input-number> -->

              <div style="height:5px"></div>
              打印数量：
              <el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.printQuantity"
                               controls-position="right" :min="0"
                               label="请输入数量" size="small"
                               style="width: 90%;"></el-input-number>
              <div style="height:5px"></div>


              <!-- <el-button  @click="itemWieht(scope.$index)"  size="small"  v-if="scope.row.weight==null && searchData.weightTableIndex!= scope.$index "  type="info">称重</el-button> -->
              <!-- <el-button   v-if="scope.row.weight==null && searchData.weightTableIndex == scope.$index"  size="small"  type="primary">称重</el-button>  -->
              <span v-if="scope.row.weight==null && searchData.weightTableIndex == scope.$index"
                    style="font-size:20px;color:#409EFF;font-weight:bold;">单位重量：请称重</span>

              <span v-if="scope.row.weight!=null"
                    style="font-size:20px;color:#67C23A;font-weight:bold;">单位重量：{{ scope.row.weight }}</span>


            </div>

            <div style="width:90%" v-else-if="ex">
              异常数量：
              <el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.exQuantity"
                               controls-position="right" :min="0"
                               label="请输入数量" size="small"
                               style="width: 90%;"></el-input-number>
              <div style="height:5px"></div>
              异常类型：
              <el-select size="small" style="width:90%" placeholder="异常类型" v-model="scope.row.exType">
                <el-option value=1 key=1 label="包裹异常"></el-option>
                <el-option value=2 key=2 label="商品异常"></el-option>
              </el-select>
              <div style="height:5px"></div>
              异常原因：
              <el-select size="small" style="width:90%" placeholder="异常原因" v-model="scope.row.exReason">
                <el-option :value="item.value" :key="item.value" :label="item.label" v-for="item in exReasons">
                </el-option>
              </el-select>
              <div style="height:5px"></div>
              异常描述：
              <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  placeholder="请输入异常描述"
                  v-model="scope.row.exMemo">
              </el-input>
              <div style="height:5px"></div>
              <!-- 异常图片：
              <el-upload
                  :id="'img'+scope.row.index"
                  style="display:inline"
                  class="upload-demo"
                  name="model"
                  :data=shipInfo
                  ref="upload"
                  :action="baseUrl+'/accept/ex/imgs/add'"

                  :file-list="scope.row.fileList"
                  :auto-upload="true">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
              </el-upload> -->


            </div>


          </template>
        </el-table-column>


        <!-- <el-table-column
           show-overflow-tooltip
           :render-header="renderHeader"
           label="批次信息"
           prop="batchInfo"
           width="200">
           <template slot-scope="scope">


                  <div :key="index" v-for="(item,index) in scope.row.batchInfo.batchTacticDetails">
                      <div  style="margin-bottom: 0px"   v-if="item.detailCode != 'inDate' &&item.detailCode != 'detailNo' && item.detailCode != 'supplierCode'"   :label="item.detailName" :prop="item.detailCode"
                               label-width="30">
                         <el-select size="small" v-if="item.detailCode === 'qcState'" v-model="scope.row[item.detailCode]" filterable
                                 :placeholder="'请选择'+item.detailName" style="width: 90%;">
                         <el-option
                           v-for="item2 in qcStates"
                           :key="item2.value"
                           :label="item2.label"
                           :value="item2.value">
                         </el-option>
                       </el-select>
                       <el-input size="small"  :placeholder="'请输入'+item.detailName" v-else-if="item.format === 1"    v-model.number="form[item.detailCode]"  > </el-input>

                       <el-date-picker size="small"  v-else-if="item.format === 2" v-model="scope.row[item.detailCode]" value-format="yyyy-MM-dd"
                                       type="date" :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-date-picker>
                       <el-time-picker size="small"  v-else-if="item.format === 3" v-model="scope.row[item.detailCode]" value-format="HH:mm:ss"
                                       :placeholder="'请选择'+item.detailName" style="width: 90%;"></el-time-picker>
                       <el-select size="small"  v-else-if="item.format === 4" v-model="scope.row[item.detailCode]" filterable
                                 :placeholder="'请选择'+item.detailName" style="width: 90%;">
                         <el-option
                           v-for="item2 in item.values"
                           :key="item2.batchTacticDetailDetailValue"
                           :label="item2.batchTacticDetailDetailValue"
                           :value="item2.batchTacticDetailDetailValue">
                         </el-option>
                       </el-select>
                 </div>
                  </div>


           </template>
         </el-table-column>   -->


        <!-- <el-table-column
                show-overflow-tooltip
                :render-header="renderHeader"
                prop="toCellCode"
                label="追踪号/收货货位"
                min-width="130">
          <template slot-scope="scope">
             <el-input v-model="scope.row.toBoxCode" placeholder="请输入追踪号" size="small"  ></el-input>
             <div style="height:5px"></div>
            <el-input v-model="scope.row.toCellCode" placeholder="请输入货位" size="small"   style="width: 90%"></el-input>
            <el-button @click="showSelectCellModal(scope.$index)" icon="iconfont icon-jia" type="text"
                       style="width: 10%;"></el-button>


            
          </template>
        </el-table-column> -->
        <!--  <el-table-column
            show-overflow-tooltip
            prop="toBoxCode"
            label="追踪号"
            width="130">
            <template slot-scope="scope">
              <el-input v-model="scope.row.toBoxCode" placeholder="请输入追踪号" size="small" clearable></el-input>
            </template>
          </el-table-column> -->
        <!-- <el-table-column
          show-overflow-tooltip
          prop="memo"
          label="备注"
          min-width="150">
          <template slot-scope="scope">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入备注"
              v-model="scope.row.memo">
            </el-input>
            
          </template>
        </el-table-column> -->
        <!--<el-table-column
          show-overflow-tooltip
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.acceptQuantity === 0" type="info">待验收</el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity > 0 && scope.row.acceptQuantity < scope.row.expectQuantity">
              部分验收
            </el-tag>
            <el-tag v-else-if="scope.row.acceptQuantity > 0 && scope.row.acceptQuantity >= scope.row.expectQuantity"
                    type="success">全部验收
            </el-tag>
          </template>
        </el-table-column>-->

        <!-- <el-table-column
          show-overflow-tooltip
          v-for="(item,index) in asnUdfDsArr"
          :key="index"
          :prop="item.code"
          :label="item.label"
          width="180">
        </el-table-column> -->
        <el-table-column
            fixed="right"
            label="操作"
            min-width="130">
          <template slot-scope="scope">
            <div v-if="scope.row.exState==1">
              <el-tag type="danger">异常已经上报，请等待。。</el-tag>
            </div>
            <div v-else-if=" searchData.weightTableIndex == scope.$index">
              <el-button v-if="!ex && scope.row.expectQuantity>scope.row.acceptQuantity" @click="ex=true" type="danger"
                         icon="   el-icon-warning"
                         size="small">异常
              </el-button>
              <div v-if="!ex" style="height:10px"></div>
              <el-button v-if="ex" @click="ex=false" type="info" icon="   el-icon-check"
                         size="small">正常
              </el-button>
              <div v-if="ex" style="height:10px"></div>
              <el-button v-if="!ex && scope.row.expectQuantity>scope.row.acceptQuantity" @click="accept(scope.row)"
                         icon="iconfont icon-shouhuo" type="primary"
                         size="small">入库
              </el-button>
              <div style="height:10px"></div>
              <el-button v-if="ex" @click="acceptEx(scope.row)" icon="iconfont icon-shouhuo" type="primary"
                         size="small">异常上传
              </el-button>
              <div style="height:10px"></div>

            </div>

          </template>
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


    <el-dialog
        title="爆款"
        :visible.sync="baokuanDialogVisible"
        width="30%"
    >
      <span>该商品一共{{ totalQuantity }}件，其中{{ inQuantity }}上架，{{ outQuantity }}件请放到爆款小推车中！</span>
      <span slot="footer" class="dialog-footer"> 
        <el-button type="primary" @click="baokuanDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {mapState} from "vuex";
import { MessageBox } from 'element-ui';

export default {
  name: 'fastAccept',
  data() {
    return {
      cfAccept:false,
      totalQuantity: "",
      inQuantity: '',
      outQuantity: '',
      baokuanDialogVisible: false,
      cellInfos: [],
       wareId: PF.getLocal('wareId', 'number'),
      wareName:PF.getLocal('wareName'),
      baseUrl: localStorage.getItem('baseUrl'),
      ex: false,
      fileList: [],
      asnMaster: {},
      chengzhong: false,
      ws: '',
      form: [],
      controls: false,

      multipleSelection: [],
      loading: {
        masterTable: false,
      },
      acceptExcess: null,
      autoPrint: true,
      baoKuan: false,
      autoBoxCode: true,
      acceptCellCode: PF.getLocal('acceptCellCode'),
      isMoreSearch: false,
      rows: [],
      spans: [],
      asnDetails: [],
      shipInfo: {
        weight: '',
        shipBillCode: '',
        length: '',
        width: '',
        height: ''
      },
      searchData: {
        weightTableIndex: null,
        pageNum: 1,
        pageSize: 20,
        total: 0,
        keyWords: '',

      },
      exReasons: [
        {
          value: '1',
          label: '多发'
        },
        {
          value: '2',
          label: '少发'
        },
        {
          value: '3',
          label: '错发'
        },
        {
          value: '4',
          label: '破损'
        },
        {
          value: '5',
          label: '其他'
        }
      ],

      inCellTasks: [],

      rules: {},
      asnUdfHsArr: [],
      asnUdfDsArr: [],

      batchTacticDetails: [],
      batchTactics: [],

    }
  },
  watch: {
    acceptCellCode: function (newVal, oldVal) {
      PF.setLocal('acceptCellCode', newVal);
    }
  },
  computed: {
    ...mapState([

      "qcStates",

    ]),

  },
  methods: {
    changeFunc(item, index) {
      if (this.asnDetails.length) {
        this.asnDetails[index].toQuantity = item;
        this.watchVal();
      }
    },
    watchVal() {
      for (let i = 0; i < this.asnDetails.length; i++) {
        let toQuantity = this.asnDetails[i].toQuantity;
        this.$set(this.asnDetails[i], 'printQuantity', toQuantity);
      }
    },
    getAcceptCell() {
      let param = {
        cellUseTypes: {0: 0},
        wareId: this.wareId
      };
      IOT.getServerData('/cell/infos/findByCellUserTypes', 'get', param, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {

          this.cellInfos = ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      })
    },
    print() {

    },

    clickRow(row, column, event) {
      this.itemWieht(row.index);
    },

    acceptEx(asnDetail) {
      if(this.cfAccept){
        return;
      }
      this.cfAccept  =true;
      const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      
      var that = this;
      if (that.shipInfo.acceptTime == null) {
        that.shipAccept();
      }
      let exData = {
        wareId: this.wareId,
        wareName: this.wareName,
        organizationId: this.asnMaster.organizationId,
        organizationName: this.asnMaster.organizationName,
        itemCode: asnDetail.itemCode,
        itemName: asnDetail.itemName,
        spec: asnDetail.spec,
        model: asnDetail.model,
        imgUrl: asnDetail.imgUrl,
        quantity: asnDetail.exQuantity,
        inQuantity: asnDetail.acceptQuantity,
        orderQuantity: asnDetail.expectQuantity,
        type: asnDetail.exType,
        reason: asnDetail.exReason,
        memo: asnDetail.exMemo,
        shipBillNo: this.shipInfo.shipBillCode,
        billNo: this.asnMaster.billNo,
        billDetailNo: asnDetail.asnDetailNo
      };

      if(asnDetail.exQuantity == null) {
        IOT.tips('异常数量不能为空！', 'error');
        this.cfAccept  =false;
        loading.close();
        return false
      }

      if(asnDetail.exQuantity != null && asnDetail.exQuantity <= 0) {
        IOT.tips('异常数量必须大于0！', 'error');
        this.cfAccept  =false;
        loading.close();
        return false
      }

      if(!asnDetail.exType) {
        IOT.tips('异常类型不能为空！', 'error');
        this.cfAccept  =false;
        loading.close();
        return false
      }

      if(!asnDetail.exReason) {
        IOT.tips('异常原因不能为空！', 'error');
        this.cfAccept  =false;
        loading.close();
        return false
      }

      IOT.showOverlay('保存中，请稍等...');
      IOT.getServerData('/accept/exs/add', 'post', exData, (ret) => {
        loading.close();
        this.cfAccept  =false;
        IOT.hideOverlay();
        if (ret.code === 200) {
          that.getAsnBillDetailData();
          that.getAsnMasterData();
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true)
    },
    accept(asnDetail) {
      if(this.cfAccept){
        return;
      }
      this.cfAccept  =true;
      const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
       
      var that = this;
      if (that.shipInfo.acceptTime == null) {
        that.shipAccept();
      }
      let acceptData = asnDetail;
      acceptData.wareId = that.wareId;
      acceptData.wareName = that.wareName;
      acceptData.organizationId = that.asnMaster.organizationId;
      acceptData.organizationName = that.asnMaster.organizationName;
      acceptData.autoPrint = that.autoPrint;
      acceptData.shipBillNo = that.shipInfo.shipBillCode;
      acceptData.baoKuan = that.baoKuan;
      acceptData.toCellCode = that.acceptCellCode;
      if(acceptData.version==null){
        acceptData.version=1;
      }
      IOT.showOverlay('保存中，请稍等...');
      IOT.getServerData('/accept/records/accept', 'post', acceptData, (ret) => {
        this.cfAccept  =false;
         loading.close();
        IOT.hideOverlay();
        if (ret.code === 200) {
          asnDetail.acceptQuantity += asnDetail.toQuantity;
          var printData = ret.data;
          if (printData.length == 2) {
            for (var i = 0; i < printData.length; i++) { 
              if (that.autoPrint   && printData[i].data != null) {
                var strHTML = printData[i].data;
                var quantity = printData[i].quantity;
                that.$parent.printHtml(strHTML, null, quantity); 
              }
            }
            this.totalQuantity = printData[0].quantity + printData[1].quantity;
            this.inQuantity = printData[0].quantity;
            this.outQuantity = printData[1].quantity;
            this.baokuanDialogVisible = true;

          } else {
            var strHTML = printData[0].data;
            var quantity = printData[0].quantity;
            if (that.autoPrint  ) {
              that.$parent.printHtml(strHTML, null, asnDetail.printQuantity);
            }
          }
          

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true)


    },
    shipAccept() {
      IOT.getServerData('/asn/ship/details/accept', 'get', this.shipInfo, (ret) => {
        if (ret.code === 200) {
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      })
    },

    itemWieht(index) {
      this.searchData.weightTableIndex = index;
      // if(this.shipInfo.weight=='' || this.shipInfo.weight==null){
      //      this.$message('请先称重包裹！');
      //       this.searchData.weightTableIndex = null;
      // }else{

      //     this.searchData.weightTableIndex = index;
      // }

    },
    getBatchInfo(batchTacticCode, index) {
      IOT.getServerData('/batch/tactics/findByBatchTacticCode', 'get', {
        batchTacticCode: batchTacticCode
      }, (ret) => {
        if (ret.code === 200) {

          let list2 = ret.data;
          this.asnDetails[index].batchInfo = list2;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true)
    },

    input0Set(e) {
      if (e.currentTarget.value == '0') {
        e.currentTarget.ariaValueNow = '';
        e.currentTarget.ariaValueText = '';
        e.currentTarget.value = '';
      }


    },
    getSysParamAsnBillData() {
      IOT.getServerData('/sys/params/findByModuleCode', 'get', {moduleCode: "asnBill"}, (ret) => {
        if (ret.code === 200) {
          let sysParams = ret.rows;
          for (let i in sysParams) {
            if (sysParams[i].paramCode === 'asnUdfHs1' || sysParams[i].paramCode === 'asnUdfHs2' || sysParams[i].paramCode === 'asnUdfHs3') {
              this.asnUdfHsArr.push({code: sysParams[i].paramCode, label: sysParams[i].value});
            }
            if (sysParams[i].paramCode === 'asnUdfDs1' || sysParams[i].paramCode === 'asnUdfDs2' || sysParams[i].paramCode === 'asnUdfDs3') {
              this.asnUdfDsArr.push({code: sysParams[i].paramCode, label: sysParams[i].value});
            }
          }
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    renderHeader(cerateElement, {column}) {
      return cerateElement("div", [
        cerateElement("span", {
          domProps: {
            innerHTML: "*"
          },
          style: {
            width: "10px",
            height: "5px",
            display: "inline-block",
            vertical_align: "middle",
            text_align: "center",
            color: "#f00",
          }
        }),
        cerateElement("span", column.label),
      ]);
    },
    changePackDetailId(index) {
      var that = this;
      let detail = PF.JSON(this.asnDetails[index]);
      detail.packDetails.forEach(packDetail => {
        if (detail.toPackDetailId == packDetail.packDetailId) {
          that.asnDetails[index].toPackDescribe = packDetail.packDescribe;
          that.asnDetails[index].toTransRatio = packDetail.transRatio;
          that.asnDetails[index].toPackDetailId = packDetail.packDetailId;
        }
      });

      // let expectQuantity1 = this.asnDetails[index].expectQuantity / this.asnDetails[index].transRatio;
      // let acceptQuantity1 = this.asnDetails[index].acceptQuantity / this.asnDetails[index].transRatio;
      // this.asnDetails[index].toQuantityMax = PF.subtraction( expectQuantity1, acceptQuantity1 );
      // this.asnDetails[index].toQuantity = this.asnDetails[index].toQuantityMax;
    },
    handleSelectionChange(val) {

      this.multipleSelection = val;
      this.itemWieht(val.index);
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getAsnBillDetailData();
    },
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getAsnBillDetailData();
    },
    clearSearchData() {
      this.searchData.keyWords = '';
    },
    getAsnBillDetailData() {
      let that = this;
      that.searchData.weightTableIndex = null;
      that.shipInfo.weight = '';
      this.asnDetails = [];
      that.loading.masterTable = true;
      let searchData = PF.JSON(this.searchData);
      IOT.getServerData('/asn/details/findByBillNo', 'get', {
        billNo: that.shipInfo.shipBillCode,
        pageSize: this.searchData.pageSize,
        pageNum: this.searchData.pageNum

      }, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {
          let list = ret.rows;
          if (list.length == 0) {
            // this.$alert('系统中无此快递单号111，请联系采购员进行处理', '提示', {
            //   confirmButtonText: '确定',
            //   callback: null,
            //
            // });
            MessageBox.confirm('系统中无此快递单号，请联系采购员进行处理', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
            }).then(() => {

            }).catch(() => {

            });
            //这里就可以自动关闭弹窗了
            setTimeout(() => {
              MessageBox.close(false)
            }, 1000);

            return;
          }
          let billNos = [];
          let spans = [];
          let rows = [];
          for (let i = 0; i < list.length; i++) {
            let index = billNos.indexOf(list[i].billNo);


            let expectQuantity1 = list[i].expectQuantity / list[i].transRatio;
            let acceptQuantity1 = list[i].acceptQuantity / list[i].transRatio;
            list[i].toQuantityMax = PF.subtraction(expectQuantity1, acceptQuantity1);
            list[i].toQuantity = list[i].expectQuantity - list[i].acceptQuantity;
            list[i].printQuantity = list[i].expectQuantity - list[i].acceptQuantity;


            list[i].toPackDescribe = list[i].packDescribe;
            list[i].toTransRatio = list[i].transRatio;
            list[i].toPackDetailId = list[i].packDetailId;
            list[i].isCommitBatch = true;
            list[i].billType = 2;
            list[i].toBoxCode = '';
            list[i].toCellCode = '';
            list[i].returnQuantity = 0;
            list[i].index = i;
            list[i].batchInfo = {};
            list[i].batchInfo.batchTacticDetails = [];
            list[i].weight = null;
            list[i].asnDetailNo = list[i].detailNo;
            list[i].asnBillNo = list[i].billNo;
            list[i].fileList = [];
            // async:{
            //   //that.getBatchInfo(list[i].batchTacticCode,i);
            //   PF.getPackDetails(
            //         list[i].packCode,
            //         (ret) => {
            //           that.asnDetails[i].packDetails=ret;
            //         }
            //   )
            // }
          }

          this.asnDetails = list;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;


        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    getAsnMasterData() {
      var that = this;
      that.asnMaster = {};

      IOT.getServerData('/asn/masters/findByBillNo', 'get', {billNo: that.shipInfo.shipBillCode}, (ret) => {

        if (ret.code === 200) {
          if (ret.data == null) {
            that.asnMaster = [];
            that.shipInfo.weight = '';
            that.shipInfo.length = '';
            that.shipInfo.width = '';
            that.shipInfo.height = '';
            that.shipInfo.acceptTime = null;
            that.shipInfo.createTime = null;
          } else {
            that.asnMaster = ret.data;
            that.shipInfo.weight = ret.data.weight == null ? '' : ret.data.weight;
            that.shipInfo.length = ret.data.length == null ? '' : ret.data.length;
            that.shipInfo.width = ret.data.width == null ? '' : ret.data.width;
            that.shipInfo.height = ret.data.height == null ? '' : ret.data.height;
            that.shipInfo.acceptTime = ret.data.acceptTime;
          }

        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      }, true);
    },

    ToggleConnectionClicked() {
      var that = this;
      try {
        this.ws = new WebSocket("ws://127.0.0.1:4649");


        this.ws.onmessage = function (evt) {
          that.getData(evt.data);

        };

        // when the connection is established, this method is called
        this.ws.onopen = function () {
          that.chengzhong = true;

        };

        // when the connection is closed, this method is called
        this.ws.onclose = function () {
          that.chengzhong = false;

        }
      } catch (ex) {

        alert(ex.message);
      }
      //document.getElementById("ToggleConnection").innerHTML = "断开";
    },

    SendData() {
      try {
        this.ws.send("hello");
      } catch (ex) {
        alert(ex.message);
      }
    },

    close() {
      try {
        this.ws.close();
        this.ws = null;
      } catch (ex) {
        alert(ex.message);
      }
    },

    getData(data) {
      this.shipInfo.shipBillCode = data;
      this.getAsnBillDetailData();
    },

    seestate() {
      alert(this.ws.readyState);
    },

    scanCode(code) {
      var that = this;
      var list = code.split('.');
      if (list.length == 2) {
        var index = that.searchData.weightTableIndex;
        if (index != null) {

          that.asnDetails[index].weight = code;
        } else if (that.shipInfo.acceptTime == null) {
          that.shipInfo.weight = code;
        }

      } else {
        that.shipInfo.shipBillCode = code;
        async:{
          that.getAsnBillDetailData();
          that.getAsnMasterData();

        }
      }
    },


    getCode() {
      var that = this;
      var code = "";
      var lastTime, nextTime;
      var lastCode, nextCode;
      document.onkeypress = function (e) {

        nextCode = e.which;
        nextTime = new Date().getTime();
        var now = new Date().getTime();

        if (lastCode == null && lastTime == null) {
          if (e.key == "Enter") {
            code = '';
            lastCode = null;
            lastTime = null;
            that.getAsnBillDetailData();
            that.getAsnMasterData();
          } else {
            code = e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }


        }
        //间隔大于500毫秒的是键盘输入的
        else if (lastCode != null && lastTime != null && nextTime - lastTime > 500) {
          if (e.key == "Enter") {
            code = '';
            lastCode = null;
            lastTime = null;
            that.getAsnBillDetailData();
            that.getAsnMasterData();
          } else {
            code = e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }
        }
        //间隔小于500毫秒的是扫码或者称重进来的
        else if (lastCode != null && lastTime != null && nextTime - lastTime <= 500) {// 扫码枪

          if (e.key == "Enter") {
            //这里监听扫码器或者称重设备的回车键
            that.scanCode(code);
            code = '';
            lastCode = null;
            lastTime = null;
          } else {
            code += e.key;
            lastCode = nextCode;
            lastTime = nextTime;
          }

        }


      }
    }

  },
  created() {
    this.getCode();
    this.getAcceptCell();
    //  this.ToggleConnectionClicked();
    this.getSysParamAsnBillData();


    // this.getAsnBillDetailData();
    PF.getSysParamUdfData('acceptExcess', [], (data, udfs) => {
      this.acceptExcess = 1 + (Number(udfs[0].value) / 100);
    });
  },
  mounted() {
  }
}


</script>

<style scoped lang="less">

</style>

  
