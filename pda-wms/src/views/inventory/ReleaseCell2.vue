<template>
    <div class="releaseCell2">
        <FeaturesHeader :headerTitle="headerTitle"/>

        <div style="text-align: center;">
            <el-form class="noMargin" :model="searchData" ref="form">
                <el-form-item label="货位号" prop="cellCode" :label-width="formLabelWidth">
                    <el-input v-model="searchData.cellCode" size="mini" placeholder="请输入货位号" clearable
                              style="width: 90%;"></el-input>
                </el-form-item>
            </el-form>
            <el-button @click="searchData.pageSize = 2;inventoryBalances = [];getInventoryBalanceData()" type="primary" size="mini" round
                       icon="iconfont icon-chaxun" style="width: 100px;margin-top: 5px;left: auto"> 查询
            </el-button>
            <el-button @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-qiyong
"
                       style="width: 100px;margin-top: 5px;right: auto"> 解锁
            </el-button>
        </div>

        <el-divider content-position="center">货位信息</el-divider>

        <div class="baseAcceptBillBox noScrollbar">
            <ul v-infinite-scroll="lazy">
                <li  v-for="(item,index) in inventoryBalances">
                    <el-card class="clearFloat">
                        <div class="cardBody">
                            <div class="topData clearFloat">
                                <!-- 头部信息 -->
                                <span>{{index+1}}/{{searchData.total}}</span>
                                <span>&nbsp;{{item.cellName}}</span>
                            </div>
                            <div class="baseData clearFloat">
                                <!-- 基础信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>货位组:</span>
                                        <span>&nbsp;{{item.shelfName}}</span>
                                    </li>
                                    <li>
                                        <span>属性:</span>
                                        <span>&nbsp;{{item.cellAttribute}}</span>
                                    </li>
                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>类型:</span>
                                        <span>&nbsp;{{item.cellUseType}}</span>
                                    </li>
                                    <li>
                                        <span>拣式:</span>
                                        <span>&nbsp;{{item.cellPickType}}</span>
                                    </li>

                                </ul>
                            </div>
                            <div class="mainData clearFloat">
                                <!-- 重要信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>&nbsp;{{item.length}}</span>
                                        <span>长:</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.wide}}</span>
                                        <span>宽:</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.high}}</span>
                                        <span>高:</span>
                                    </li>

                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>&nbsp;{{item.volumeLimit}}</span>
                                        <span>体限:</span>
                                        <template slot-scope="scope">
                                            <span>{{scope.row.volumeLimit.toFixed(2)}}</span>
                                        </template>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.weightLimit}}</span>
                                        <span>重限:</span>
                                        <template slot-scope="scope">
                                            <span>{{scope.row.weightLimit.toFixed(2)}}</span>
                                        </template>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.boxLimit}}</span>
                                        <span>箱限:</span>
                                        <template slot-scope="scope">
                                            <span>{{scope.row.boxLimit.toFixed(2)}}</span>
                                        </template>
                                    </li>
                                </ul>
                            </div>
                            <div class="baseData clearFloat">
                                <!-- 基础信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>上顺:</span>
                                        <span>&nbsp;{{item.groundOrder}}</span>
                                    </li>
                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>拣顺:</span>
                                        <span>&nbsp;{{item.pickOrder}}</span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <!--<div class="cardBottomBtn clearFloat">
                            &lt;!&ndash; 底部按钮 &ndash;&gt;
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
                        </div>-->
                    </el-card>
                </li>
            </ul>
           <!-- <ul v-infinite-scroll="lazy">
                <li v-for="item in inventoryBalances">
                    <el-card class="noPaddingBottom">
                        <div class="clearFloat">
                            <ul class="leftFloat">
                                <li>
                                    <span>名称:</span>
                                    <span>&nbsp;{{item.cellName}}</span>
                                </li>
                                <li>
                                    <span>货位:</span>
                                    <span>&nbsp;{{item.cellCode}}</span>
                                </li>
                                <li>
                                    <span>货位组:</span>
                                    <span>&nbsp;{{item.shelfName}}</span>
                                </li>
                                <li>
                                    <span>属性:</span>
                                    <span>&nbsp;{{item.cellAttribute}}</span>
                                </li>
                                <li>
                                    <span>长:</span>
                                    <span>&nbsp;{{item.length}}</span>
                                </li>
                                <li>
                                    <span>宽:</span>
                                    <span>&nbsp;{{item.wide}}</span>
                                </li>
                                <li>
                                    <span>高:</span>
                                    <span>&nbsp;{{item.high}}</span>
                                </li>


                            </ul>
                            <ul class="rightFloat">
                                <li>
                                    <span>类型:</span>
                                    <span v-for="cellUseType in cellUseTypes" v-if="cellUseType.value === item.cellUseType">{{cellUseType.label}}</span>
                                </li>
                                <li>
                                    <span>拣式:</span>
                                    <span>&nbsp;{{item.cellPickType}}</span>
                                </li>
                                <li>
                                    <span>上顺:</span>
                                    <span>&nbsp;{{item.groundOrder}}</span>
                                </li>
                                <li>
                                    <span>拣顺:</span>
                                    <span>&nbsp;{{item.pickOrder}}</span>
                                </li>
                                <li>
                                    <span>体限:</span>
                                    <span>&nbsp;{{item.volumeLimit}}</span>
                                    <template slot-scope="scope">
                                        <span>{{scope.row.volumeLimit.toFixed(2)}}</span>
                                    </template>
                                </li>
                                <li>
                                    <span>重限:</span>
                                    <span>&nbsp;{{item.weightLimit}}</span>
                                    <template slot-scope="scope">
                                        <span>{{scope.row.weightLimit.toFixed(2)}}</span>
                                    </template>
                                </li>
                                <li>
                                    <span>箱限:</span>
                                    <span>&nbsp;{{item.boxLimit}}</span>
                                    <template slot-scope="scope">
                                        <span>{{scope.row.boxLimit.toFixed(2)}}</span>
                                    </template>
                                </li>
                            </ul>
                        </div>
                    </el-card>
                </li>
            </ul>-->
            <p v-show="inventoryBalances.length <= 0">暂无数据</p>
            <p v-show="loading">加载中...</p>
            <p v-show="noMore">没有更多了</p>
        </div>

    </div>
</template>
<script>
    import FeaturesHeader from "@/components/FeaturesHeader";
    import {mapState} from "vuex";

    export default {
        name: "ReleaseCell2",
        data() {
            return {
                headerTitle: "货位解锁",
                formLabelWidth: '120px',

                loading: false,
                noMore: false,

                inventoryBalances: [],

                searchData: {
                    pageNum: 1,
                    pageSize: 2,
                    total: null,
                    keyWords: '',
                    state: 1,
                    wareId: PF.getLocal('wareId', 'number'),
                    cellCode: ''
                },
            };
        },
        computed: {
            ...mapState([
                "cellUseTypes",
            ]),
        },
        methods: {
            getInventoryBalanceData() {
                this.loading = true;
                let searchData = this.searchData;
                if (!PF.isNull(searchData.cellCode)) {
                    delete searchData.cellCode;
                }
                IOT.getServerData('/cell/infos/list', 'get', searchData, (ret) => {
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
                        IOT.getServerData('/inventorys/unlockCell', 'get', form, (ret) => {
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
            // commitFreeze(row){
            //     if(!PF.isNull(row.freezeQuantity1) || row.freezeQuantity1 === 0){
            //         IOT.tips('请输入需冻数！', 'error');
            //         return false
            //     }
            //     IOT.showOverlay('保存中，请稍等...');
            //     IOT.getServerData('/inventorys/freezeCell', 'get', {inventoryId: row.inventoryId,freezeQuantity1: row.freezeQuantity1}, (ret) => {
            //         IOT.hideOverlay();
            //         if (ret.code === 200) {
            //             IOT.tips('保存成功！', 'success', 1000, () => {
            //                 this.searchData.pageSize = 2;
            //                 this.inventoryBalances = [];
            //                 this.getInventoryBalanceData();
            //             });
            //         } else {
            //             IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            //         }
            //     });
            // },
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
    .releaseCell {
    }
</style>
