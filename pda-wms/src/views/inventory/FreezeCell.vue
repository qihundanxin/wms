<template>
    <div class="freezeCell">
        <FeaturesHeader :headerTitle="headerTitle"/>

        <div style="text-align: center;">
            <el-form class="noMargin" :model="searchData" ref="form">
                <el-form-item label="货位号" :label-width="formLabelWidth">
                    <el-input v-model="searchData.cellCode" size="mini" placeholder="请输入货位号" clearable
                              style="width: 90%;"></el-input>
                </el-form-item>
            </el-form>
            <el-button @click="searchData.pageSize = 2;inventoryBalances = [];getInventoryBalanceData()" type="primary" size="mini" round
                       icon="iconfont icon-chaxun" style="width: 100px;margin-top: 5px;left: auto"> 查询
            </el-button>
            <el-button @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-quanxian1"
                       style="width: 100px;margin-top: 5px;right: auto"> 冻结
            </el-button>
        </div>

        <el-divider content-position="center">库存信息</el-divider>

        <div class="baseAcceptBillBox noScrollbar">
            <ul v-infinite-scroll="lazy">
                <li  v-for="(item,index) in inventoryBalances">
                    <el-card class="clearFloat">
                        <div class="cardBody">
                            <div class="topData clearFloat">
                                <!-- 头部信息 -->
                                <span>{{index+1}}/{{searchData.total}}</span>
                                <span>&nbsp;{{item.itemName}}</span>
                                <span>&nbsp;{{item.itemCode}}</span>
                            </div>
                            <div class="baseData clearFloat">
                                <!-- 基础信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>货主:</span>
                                        <span :title="item.organizationName">&nbsp;{{item.organizationName}}</span>
                                    </li>
                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>批次:</span>
                                        <span>&nbsp;{{item.batchName}}</span>
                                    </li>

                                </ul>
                            </div>
                            <div class="mainData clearFloat">
                                <!-- 重要信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>{{item.quantity}}</span>
                                        <span>库存</span>
                                    </li>

                                    <li>
                                        <span>&nbsp;{{item.allotQuantity}}</span>
                                        <span>分配</span>
                                    </li>

                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>&nbsp;{{item.packDescribe}}</span>
                                        <span>单位</span>
                                    </li>
                                    <li>

                                        <span>&nbsp;{{item.freezeQuantity}}</span>
                                        <span>冻结</span>
                                    </li>
                                </ul>
                            </div>
                            <div class="baseData clearFloat">
                                <!-- 基础信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>追踪号</span>
                                        <span>{{item.boxCode}}</span>
                                    </li>

                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>LPN</span>
                                        <span>{{item.lpn}}</span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="cardBottomBtn clearFloat">
                            <!-- 底部按钮 -->
                            <div class="leftFloat">
                                <ul>
                                    <li>
                                        <span>需冻数:</span>
                                        <span>
                                         <el-input-number v-model="item.freezeQuantity1" size="mini" controls-position="right" placeholder="需冻数" style="width: 200%;"></el-input-number>
                                        </span>
                                    </li>
                                </ul>

                            </div>
                            <div class="rightFloat" @click="commitFreeze(item)">冻结</div>
                        </div>
                    </el-card>
                </li>
            </ul>
            <p v-show="inventoryBalances.length <= 0">暂无数据</p>
            <p v-show="loading">加载中...</p>
            <p v-show="noMore">没有更多了</p>
        </div>

    </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "FreezeCell",
    data() {
      return {
        headerTitle: "货位冻结",
        formLabelWidth: '120px',

        loading: false,
        noMore: false,

        inventoryBalances: [],

        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          states: {
            0: 0,
            1: 1
          },
          wareId: PF.getLocal('wareId', 'number'),
          cellCode: ''
        },
      };
    },
    methods: {
      getInventoryBalanceData() {
        this.loading = true;
        let searchData = this.searchData;
        if (!PF.isNull(searchData.cellCode)) {
          delete searchData.cellCode;
        }
        IOT.getServerData('/inventorys/findByFreeze', 'get', searchData, (ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.inventoryBalances = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            if (this.searchData.pageSize >= ret.total) {
              this.noMore = true;
            } else {
              this.noMore = false;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            for (let key in form.row) {
              form[key] = form.row[key]
            }
            delete form.row;
            form.fromCellCode = form.cellCode;
            IOT.getServerData('/inventorys/freezeCells', 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                  IOT.tips('保存成功！', 'success', 1000, () => {
                      this.searchData.pageSize = 2;
                      this.inventoryBalances = [];
                      this.getInventoryBalanceData();
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      lazy() {
        if (this.inventoryBalances.length > 0 && !this.noMore && !this.loading) {
          this.searchData.pageSize += 2;
          this.getInventoryBalanceData();
        }
      },
      commitFreeze(row){
        if(!PF.isNull(row.freezeQuantity1) || row.freezeQuantity1 === 0){
          IOT.tips('请输入需冻数！', 'error');
          return false
        }
        IOT.showOverlay('保存中，请稍等...');
        IOT.getServerData('/inventorys/freezeCell', 'get', {inventoryId: row.inventoryId,freezeQuantity1: row.freezeQuantity1}, (ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 1000, () => {
              this.searchData.pageSize = 2;
              this.inventoryBalances = [];
              this.getInventoryBalanceData();
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      let cellCode = this.$route.params.cellCode;
      if (PF.isNull(cellCode)) {
        this.searchData.cellCode = cellCode;
      }

    },
    components: {
      FeaturesHeader,
    }
  };
</script>
<style lang="less" scoped>
    .freezeCell {
    }
</style>
