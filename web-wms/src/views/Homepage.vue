<template>
  <div id="homepage" class="homepage">
    <!-- 头部卡片 -->
    <el-row>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-top" style="color: green"></div>
          <div class="right">
            <div class="right1">SO-总数</div>
            <div class="right2">{{ stat.soCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-top" style="color: red"></div>
          <div class="right">
            <div class="right1">缺货单</div>
            <div class="right2">{{ stat.oosCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-top" style="color: orange"></div>
          <div class="right">
            <div class="right1">待拣货</div>
            <div class="right2">{{ stat.waitPickupTotal }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-top" style="color: blue"></div>
          <div class="right">
            <div class="right1">待发运</div>
            <div class="right2">{{ stat.waitShippedCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-bottom" style="color: green"></div>
          <div class="right">
            <div class="right1">ASN总数</div>
            <div class="right2">{{ stat.asnCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :md="8" :lg="4" :xl="4">
        <div class="box card">
          <div class="left_icon el-icon-bottom" style="color: orange"></div>
          <div class="right">
            <div class="right1">未验收</div>
            <div class="right2">{{ stat.unQcCount }}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    <!-- 图形 -->
    <el-row>
      <el-col :xs="24" :sm="24" :md="24" :lg="16" :xl="16">
        <div class="box">
          <div class="box3">
            <el-row>
              <el-col>
                <div style="text-align: left">
                  <el-radio-group v-model="timeSpan" size="medium" @change="fetchData">
                    <el-radio-button label="7">过去7天</el-radio-button>
                    <el-radio-button label="30">过去30天</el-radio-button>
                  </el-radio-group>
                </div>
              </el-col>
            </el-row>
            <div id="main" style="width: 98%; height: 280px; margin-top: 40px"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <div class="box">
          <el-row>
            <el-col :span="12">
              <div class="box3">
                <dv-water-level-pond :config="{ data: [stat.cellUsedRate.toFixed()], shape: 'roundRect' }"
                  style="width: 100px; height: 100px" />
                <div class="title">货位利用率</div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="box">
          <el-row>
            <el-col :span="24">
              <div class="box3">
                <div class="title2">当日任务进行</div>
                <el-row>
                  <el-col :span="3">
                    <div class="title3">入库</div>
                  </el-col>
                  <el-col :span="21">
                    <el-progress :stroke-width="20" :text-inside="true"
                      :percentage="stat.receiveTotal && stat.acceptedCount / stat.receiveTotal * 100"
                      :format="() => `${stat.acceptedCount}/${stat.receiveTotal}`">
                    </el-progress>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="3">
                    <div class="title3">退货</div>
                  </el-col>
                  <el-col :span="21">
                    <el-progress :stroke-width="20" :text-inside="true"
                      :percentage="stat.returnTotal && stat.returnedCount / stat.returnTotal * 100"
                      :format="() => `${stat.returnedCount}/${stat.returnTotal}`">
                    </el-progress>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="3">
                    <div class="title3">拣货</div>
                  </el-col>
                  <el-col :span="21">
                    <el-progress :stroke-width="20" :text-inside="true"
                      :percentage="stat.pickupTotal && stat.pickedCount / stat.pickupTotal * 100"
                      :format="() => `${stat.pickedCount}/${stat.pickupTotal}`">
                    </el-progress>
                  </el-col>
                </el-row>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <!-- 表格 -->
    <div class="box" style="padding :5px 10px 10px 20px">
      <el-tabs type="card">
        <el-tab-pane label="入库异常">
          <div class="tableBox none">
            <el-table :data="exList" style="width: 100%">
              <el-table-column type="index" width="30">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="itemName" label="缩略图/产品编码/产品名称" min-width="300">
                <template slot-scope="scope">
                  <el-row :gutter="20">
                    <el-col :span="8">
                      <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                        <el-image style="width: 80px; height: 80px" :src="scope.row.imgUrl"
                          :preview-src-list="[scope.row.imgUrl]">
                        </el-image>
                      </div>
                    </el-col>
                    <el-col :span="16">
                      <div style="display: inline-block;margin-left:5px;  ">
                        <span class="textBtn" style="color:red">{{ scope.row.itemCode }}</span>
                        <br />
                        <span>{{ scope.row.itemName }}</span>
                        <br />
                        <span v-if="scope.row.shipBillNo != null">{{ scope.row.shipBillNo }}</span>
                        <br />
                        <span>{{ scope.row.billNo }}</span>
                        <br />
                      </div>
                    </el-col>
                  </el-row>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="billNo" label="异常信息" min-width="230">
                <template slot-scope="scope">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <div style="display: inline-block;margin-left:5px;  width:80px;height:80px">
                        <el-image style="width: 80px; height: 80px" :src="scope.row.imgUrls[0]"
                          :preview-src-list="scope.row.imgUrls">
                        </el-image>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <span>类型: </span>
                      <span class="textBtn" v-if="scope.row.type == 1">包裹异常</span>
                      <span class="textBtn" v-if="scope.row.type == 2">商品异常</span>
                      <br>
                      <span>原因: </span>
                      <span class="textBtn" v-if="scope.row.reason == 1">多发</span>
                      <span class="textBtn" v-if="scope.row.reason == 2">少发</span>
                      <span class="textBtn" v-if="scope.row.reason == 3">错发</span>
                      <span class="textBtn" v-if="scope.row.reason == 4">破损</span>
                      <span class="textBtn" v-if="scope.row.reason == 5">其他</span>
                      <br>
                      <span>描述: </span>
                      <span class="textBtn">{{ scope.row.memo }}</span>
                      <br>
                    </el-col>
                  </el-row>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="billNo" label="处理方案" min-width="180">
                <template slot-scope="scope">
                  <el-tag type="warning" v-if="scope.row.fangan == 1">全部退件</el-tag>
                  <el-tag type="danger" v-if="scope.row.fangan == 2">全部弃件</el-tag>
                  <el-tag type="success" v-if="scope.row.fangan == 3">全部入库</el-tag>
                  <el-tag type="warning" v-if="scope.row.fangan == 4">部分入库部分退件</el-tag>
                  <el-tag type="danger" v-if="scope.row.fangan == 5">部分入库部分弃件</el-tag>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="expectQuantity" label="数量" min-width="130">
                <template slot-scope="scope">
                  订单数：<span style="color:#409EFF;margin-left:2px;font-size:20px">{{ scope.row.orderQuantity / 1 }}
                  </span>
                  <br>
                  已入库数：<span style="color:#409EFF;margin-left:2px;font-size:20px">{{ scope.row.inQuantity / 1 }} </span>
                  <br>
                  异常数：<span style="color:red;margin-left:2px;font-size:20px">{{ scope.row.quantity / 1 }} </span>
                  <br>
                  <br>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="toQuantity" label="验收信息/异常信息" min-width="200">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.state == 0" type="success">初始化 </el-tag>
                  <el-tag v-if="scope.row.state == 1" type="success">提交ERP </el-tag>
                  <el-tag v-if="scope.row.state == 2" type="danger">待处理 </el-tag>
                  <el-tag v-if="scope.row.state == 3" type="success">已经处理 </el-tag>
                  <div style="width:90%" v-if="scope.row.state == 2">
                    入库数量：<el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.toQuantity"
                      controls-position="right" min=0 placeholder="" label="请输入数量" size="small"
                      style="width: 90%;"></el-input-number>
                    <div style="height:5px"></div>
                    打印数量： <el-input-number :controls='controls' @focus="input0Set" v-model="scope.row.printQuantity"
                      controls-position="right" min=0 label="请输入数量" size="small" style="width: 90%;"></el-input-number>
                    <div style="height:5px"></div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="exCurrentChange"
              :current-page.sync="exSearchData.pageNum" :page-size="exSearchData.pageSize"
              layout="total, prev, pager, next, jumper" :total="exSearchData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="待上架库存">
          <div class="tableBox">
            <el-table highlight-current-row :data="upList" style="width: 100%">
              <el-table-column fixed="left" show-overflow-tooltip prop="inventoryId" label="库存ID" min-width="100">
              </el-table-column>
              <el-table-column fixed="left" show-overflow-tooltip prop="itemName" label="商品名称" min-width="150">
                <template slot-scope="scope">
                  <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                      scope.row.itemName
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="itemCode" label="商品编码" min-width="130">
                <template slot-scope="scope">
                  <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                      scope.row.itemCode
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="organizationName" label="货主" min-width="130">
                <template slot-scope="scope">
                  <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{
                      scope.row.organizationName
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="batchName" label="批次" min-width="130">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="quantity" label="数量" min-width="140">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="allotQuantity" label="分配数量" min-width="140">
                <template slot-scope="scope">
                  <span style="color:black;font-weight:900;">{{ scope.row.allotQuantity }} </span>
                  <span v-if="scope.row.allotQuantity > 0" style="font-size:20% ;color:red ">爆</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="cellCode" label="货位" min-width="150">
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="boxCode" label="追踪号" min-width="150">
              </el-table-column>
            </el-table>
            <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="upCurrentChange"
              @size-change="upSizeChange" :current-page.sync="upSearchData.pageNum" :page-sizes="[15, 30, 50, 100]"
              :page-size="upSearchData.pageSize" layout="sizes,total, prev, pager, next" :total="upSearchData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <!-- TODO 波次 -->
        <el-tab-pane label="待拣波次" v-if="false">
          <div class="tableBox  ">
            <el-table :data="waveMasters" style="width: 100%">
              <el-table-column sortable prop="code" label="波次编码">
                <template slot-scope="scope">
                  <div slot="header" class="clearfix">
                    <span>{{ scope.row.waveId }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="生成时间" min-width="150">
              </el-table-column>
              <el-table-column prop="totalBillCount" label="订单总数" min-width="150">
              </el-table-column>
              <el-table-column prop="totalSkuCount" label="sku总数" min-width="150">
              </el-table-column>
              <el-table-column prop="waveTacticName" label="波次策略" min-width="150">
                <template slot-scope="scope">
                  {{ scope.row.waveTacticName }}
                  <span v-if="scope.row.soStructure == 1">（一单一品）</span>
                  <span v-if="scope.row.soStructure == 2">（一单多品）</span>
                  <span v-if="scope.row.soStructure == 3">（一单一品一件）</span>
                  <span v-if="scope.row.soStructure == 4">（一单一品多件）</span>
                </template>
              </el-table-column>
              <el-table-column prop="isSeeding" label="分拣方式：" min-width="150">
                <template slot-scope="scope">
                  <span v-if="scope.row.isSeeding == 1">先拣后分</span>
                  <span v-if="scope.row.isSeeding == 0">边拣边分</span>
                </template>
              </el-table-column>
              <el-table-column prop="state" label="状态" min-width="150">
                <template slot-scope="scope">
                  <span style="color:#F56C6C" v-if="scope.row.state == 0">订单接收中</span>
                  <span style="color:#8B3A62" v-if="scope.row.state == 1">待拣货</span>
                  <span style="color:#8B3A62" v-if="scope.row.state == 11">拣货中</span>
                  <span style="color:#543044" v-if="scope.row.state == 2">拣货完成</span>
                  <span style="color:#E6A23C" v-if="scope.row.state == 3">播种完成</span>
                  <span style="color:#67C23A" v-if="scope.row.state == 4">发运完成</span>
                  <span style="color:#67C23A" v-if="scope.row.state == 5">结束</span>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination style="text-align: right;" class="pagination-bottom" @current-change="waveCurrentChange"
              :current-page.sync="waveSearchData.pageNum" :page-size="waveSearchData.pageSize"
              @size-change="waveSizeChange" :page-sizes="[15, 30, 50, 100]" layout="sizes,total, prev, pager, next  "
              :total="waveSearchData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <!-- TODO 波次END -->
        <!-- TODO 还货上架 -->
        <el-tab-pane label="还货上架" v-if="false">
          <div class="tableBox">
            <el-table :data="pickTaskReturns" style="width: 100%">
              <el-table-column fixed="left" show-overflow-tooltip prop="itemName" label="商品名称" min-width="150">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="itemCode" label="商品编码" min-width="130">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="quantity" label="数量" min-width="70">
                <template slot-scope="scope">
                  <span>{{ scope.row.quantity }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="boxCode" label="boxCode" min-width="70">
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="fromInventoryId" label="fromInventoryId" min-width="70">
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="toCellCode" label="推荐货位" min-width="100">
                <template slot-scope="scope">
                  <span>{{ scope.row.toCellCode }}</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="state" label="状态" min-width="80">
                <template slot-scope="scope">
                  <span v-if="scope.row.state == 0" style="color:#E6A23C">待上架</span>
                  <span v-if="scope.row.state == 1" style="color:#67C23A">已完成</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="type" label="类型" min-width="80">
                <template slot-scope="scope">
                  <span v-if="scope.row.type == 1" style="color:#909399">配货异常</span>
                  <span v-if="scope.row.type == 2" style="color:#F56C6C">订单修改（未发运）</span>
                  <span v-if="scope.row.type == 3" style="color:#F56C6C">订单修改（已发运）</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" min-width="120">
                <template slot-scope="scope">
                  <el-button @click="pickTaskAffirm(scope.row)" type="primary" size="small"
                    :disabled="scope.row.state === 2">&nbsp;拣货确认</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="returnCurrentChange"
              :current-page.sync="returnSearchData.pageNum" :page-size="returnSearchData.pageSize"
              @size-change="returnSizeChange" :page-sizes="[15, 30, 50, 100]"
              layout="sizes,total, prev, pager, next, jumper" :total="returnSearchData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <!-- TODO 还货上架END -->
        <!-- TODO 补货建议 -->
        <el-tab-pane label="补货建议" v-if="false">
          <div class="tableBox">
            <el-table highlight-current-row :data="cpfrTasks" style="width: 100%">
              <el-table-column fixed="left" show-overflow-tooltip prop="itemName" label="商品名称" min-width="150">
                <template slot-scope="scope">
                  <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                      scope.row.itemName
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="itemCode" label="商品编码" min-width="130">
                <template slot-scope="scope">
                  <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                      scope.row.itemCode
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="quantity" label="数量" min-width="70">
                <template slot-scope="scope">
                  <span>{{ scope.row.quantity }}</span>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="createTime" label="创建时间" min-width="100">
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="fromCellCode" label="FROM货位" min-width="100">
                <template slot-scope="scope">
                  <span>{{ scope.row.fromCellCode }}</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="toCellCode" label="TO货位" min-width="100">
                <template slot-scope="scope">
                  <span>{{ scope.row.toCellCode }}</span>
                </template>
              </el-table-column>
              <el-table-column sortable show-overflow-tooltip prop="type" label="来源" min-width="80">
                <template slot-scope="scope">
                  <span v-if="scope.row.type == 1">配置驱动</span>
                  <span v-else-if="scope.row.type == 2">订单驱动</span>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination class="pagination-bottom" style="text-align: right;" @current-change="cpfrCurrentChange"
              :current-page.sync="cpfrSearchData.pageNum" :page-size="cpfrSearchData.pageSize"
              @size-change="cpfrSizeChange" :page-sizes="[15, 30, 50, 100]"
              layout="sizes,total, prev, pager, next, jumper" :total="cpfrSearchData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <!-- TODO 补货建议END -->
      </el-tabs>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
var echarts = require("echarts");
export default {
  name: "Homepage",
  data() {
    let wareId = PF.getLocal('wareId', 'number');
    return {
      // 仓库
      wareId: wareId,
      // 日期选项
      timeSpan: 7,
      // 补货建议
      cpfrTasks: [],
      // 补货建议查询条件
      cpfrSearchData: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
        state: 0,
        wareId: wareId,
      },
      // 待上架库存
      upList: [],
      // 待上架库存查询条件
      upSearchData: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
        state: 0,
        cellUseType: 0,
        wareId: wareId,
      },
      // 异常入库
      exList: [],
      // 异常入库查询条件
      exSearchData: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
        state: 0,
        wareId: wareId,
      },
      // 还货上架
      pickTaskReturns: [],
      // 还货上架查询条件
      returnSearchData: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
        state: 0,
        wareId: wareId,
      },
      // 待拣波次
      waveMasters: [],
      // 待拣波次查询条件
      waveSearchData: {
        state: 1,
        pageNum: 1,
        pageSize: 10,
        total: null,
        wareId: wareId
      },
      stat: {
        // 标头数据
        soCount: 0,
        oosCount: 0,
        waitPickupTotal: 0,
        waitShippedCount: 0,
        asnCount: 0,
        unQcCount: 0,
        // 折线图
        statByDateList: [],
        // 右侧数据
        cellUsedRate: 0,
        receiveTotal: 0,
        acceptedCount: 0,
        returnTotal: 0,
        returnedCount: 0,
        pickupTotal: 0,
        pickedCount: 0,
      },
      // 折线图设置
      option: {
        color: [
          "#0090FF",
          "#36CE9E",
          "#FFC005",
          "#FF515A",
          "#8B5CFF",
          "#00CA69",
        ],
        title: {
          text: "货物数量统计",
          left: 'start',
          top: 0,
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["IN", "OUT"],
          top: 20,
          left: 'center',
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: [
          ],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            smooth: true,
            name: "IN",
            type: "line",
            data: [],
          },
          {
            smooth: true,
            name: "OUT",
            type: "line",
            data: [],
          },
        ],
      },
    };
  },
  computed: {
    ...mapState(["productList"]),
  },
  methods: {
    cpfrCurrentChange(val) {
      this.cpfrSearchData.pageNum = val;
      this.getPickTaskData()
    },
    cpfrSizeChange(val) {
      this.cpfrSearchData.pageSize = val;
      this.getPickTaskData();
    },
    getPickTaskData() {
      let that = this;
      IOT.getServerData('/cpfr/tasks/list', 'get', this.cpfrSearchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          this.cpfrTasks = list;
          this.cpfrSearchData.total = ret.total;
          this.cpfrSearchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    upCurrentChange(val) {
      this.upSearchData.pageNum = val;
      this.getUp()
    },
    upSizeChange(val) {
      this.upSearchData.pageSize = val;
      this.getUp();
    },
    getUp() {
      let that = this;
      let upSearchData = PF.JSON(this.upSearchData);
      IOT.getServerData('/inventorys/CellUseTypeList', 'get', upSearchData, (ret) => {
        if (ret.code === 200) {
          this.upList = ret.rows;
          this.upSearchData.total = ret.total;
          this.upSearchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    exCurrentChange(val) {
      this.upSearchData.pageNum = val;
      this.getExData()
    },
    getExData() {
      let that = this;
      this.exList = [];
      IOT.getServerData('/accept/exs/list', 'get', that.exSearchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          for (let i = 0; i < list.length; i++) {
            list[i].toQuantity = list[i].orderQuantity - list[i].quantity - list[i].inQuantity;
            list[i].printQuantity = list[i].orderQuantity - list[i] - list[i].inQuantity;
            list[i].toPackDescribe = list[i].packDescribe;
            list[i].toTransRatio = list[i].transRatio;
            list[i].toPackDetailId = list[i].packDetailId;
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
            list[i].imgUrls = []
            // async:{
            //   that.getImgs(list[i].acceptExId,i);
            // } 
          }
          this.exList = list;
          this.exSearchData.total = ret.total;
          this.exSearchData.pageNum = ret.pageNumber;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    // getImgs(exId,i){
    //     IOT.getServerData('/accept/ex/imgs/findByAcceptExId', 'get', {acceptExId:exId}, (ret) => {
    //     if (ret.code === 200) { 
    //       this.exList[i].imgUrls = ret.data;
    //     } else {
    //       IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
    //     }
    //   },true);
    // },
    returnCurrentChange(val) {
      this.returnSearchData.pageNum = val;
      this.getPickTaskReturn()
    },
    returnSizeChange(val) {
      this.returnSearchData.pageSize = val;
      this.getPickTaskReturn();
    },
    getPickTaskReturn() {
      let that = this;
      IOT.getServerData('/pick/task/returns/findList', 'get', this.returnSearchData, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          this.pickTaskReturns = list;
          this.returnSearchData.total = ret.total;
          this.returnSearchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    waveCurrentChange(val) {
      this.waveSearchData.pageNum = val;
      this.getPickTaskReturn()
    },
    waveSizeChange(val) {
      this.waveSearchData.pageSize = val;
      this.getPickTaskReturn();
    },
    getWaveMasterData() {
      IOT.getServerData('/wave/masters/list', 'get', this.waveSearchData, (ret) => {
        if (ret.code === 200) {
          this.waveMasters = ret.rows;
          this.waveSearchData.total = ret.total;
          this.waveSearchData.pageNum = ret.pageNumber
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    /**
     * 简单数据统计通查
     */
    fetchData() {
      let days = this.timeSpan || 7;
      IOT.getServerData("/home/statistics/topStatistics", "post",
        { days: Number(days), wareId: this.wareId }, (ret) => {
          if (ret.code === 200 && ret.data) {
            this.stat = Object.assign(this.stat, ret.data);
            this.updateEChartData();
          } else {
            IOT.tips(ret.message || "服务器请求失败，稍后再试！", "error");
          }
        }, true);
    },
    /**
     * 初始化折线图
     */
    initEcharts() {
      const myChart = echarts.init(document.getElementById("main"));
      // 指定图表的配置项和数据
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(this.option);
      window.addEventListener('resize', function () {
        myChart.resize();
      })
      this.myChart = myChart;
    },
    /**
     * 更新折线图
     */
    updateEChartData() {
      const oriStatByDateList = this.stat.statByDateList;
      if (!oriStatByDateList) {
        return;
      }
      const properties = [
        {
          name: 'IN',
          index: 'inboundCount',
        },
        {
          name: 'OUT',
          index: 'outboundCount',
        }
      ];
      const timeSpan = this.timeSpan;
      let startIndex = oriStatByDateList.length - timeSpan;
      if (startIndex < 0) {
        startIndex = 0;
      }
      const statByDateList = oriStatByDateList.slice(startIndex);
      const xAxisNames = [];
      const defaultItem = {
        smooth: true,
        type: 'line',
      };
      const series = [];
      for (const property of properties) {
        series[property.name] = { ...defaultItem, name: property.name, data: [] };
      }
      for (const stat of statByDateList) {
        for (const property of properties) {
          series[property.name].data.push(+stat[property.index]);
        }
        xAxisNames.push(stat.date);
      }
      this.option.series = Object.values(series);
      this.option.xAxis.data = xAxisNames;
      this.myChart.setOption(this.option);
    },
  },
  mounted() {
    this.initEcharts();
  },
  created() {
    this.fetchData();
    // this.getPickTaskData();
    this.getUp();
    this.getExData();
    // this.getPickTaskReturn();
    // this.getWaveMasterData();
  }
};
</script>
<style scoped lang="less">
.homepage {
  height: 1900px;

  .box {
    //   box-shadow: 0 2px 4px #c1deff, 0 0 6px #c1deff;
    border-radius: 10px;
    margin: 10px 15px 5px 8px;
    padding: 0px 20px 10px 0px;
    background-color: white;

    .left_icon {
      position: absolute;
      margin: 30px 20px;
    }

    .right {
      position: absolute;
      margin: 10px 10px;
      left: 45%;

      .right1 {
        font-size: 14px;
        margin: 0px 0px 10px 0px;
      }

      .right2 {
        font-size: 18px;
        font-weight: 700;
        margin: 5px 0px 10px 0px;
      }
    }

    .left_icon:before {
      font-size: 1cm;
    }
  }

  .card {
    height: 70px;
    position: relative;
  }

  .box2 {
    margin: 10px 15px 0px 0px;
    padding: 10px 0px 0px 15px;
    border-radius: 5px;
    height: 80px;
  }

  .box3 {
    margin: 10px 0px 0px 15px;
    padding: 2px 0px 0px 15px;
    border-radius: 5px;

    .title {
      font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
      margin: 12px 0px 0px 25px;
      font-weight: 700;
    }

    .title2 {
      font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
      margin: 0px 0px 5px 0px;
      font-weight: 700;
      font-size: 20px;
    }

    .el-table .warning-row {
      background: oldlace;
    }

    .el-table .success-row {
      background: #f0f9eb;
    }

    .title3 {
      font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
      margin: 8px 0px 4px 0px;
      font-weight: 550;
      font-size: 10px;
    }
  }

  .el-progress {
    width: 80%;
    line-height: 2;
    // display: inline;
  }

  .el-tabs--card {
    border: none;
  }
}
</style>
 