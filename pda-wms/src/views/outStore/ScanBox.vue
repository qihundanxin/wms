<template>
    <div class="scanBox">
        <FeaturesHeader :headerTitle="headerTitle"/>

        <div style="text-align: center;">
            <el-form class="noMargin BoxingPickTaskBox" ref="form">

                <el-form-item label="箱号" :label-width="formLabelWidth">
                    <el-input v-model="searchData.boxNo" size="mini" style="width: 60%;">
                    </el-input>
                    <el-button @click="getBoxDetailData" type="primary" size="mini" round icon="iconfont icon-chaxun"
                               style="width: 27%;margin-top: 5px;margin-left: 10px;"> 查询
                    </el-button>
                </el-form-item>


            </el-form>

            <el-button @click="loadBox" type="primary" size="mini" round icon="iconfont icon-dui"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 装车
            </el-button>
        </div>

        <el-divider content-position="center">装箱明细</el-divider>

        <div class="baseAcceptBillBox noScrollbar">
            <ul>
                <li v-for="(item,index) in boxDetails">
                    <el-card class="clearFloat">
                        <div class="cardBody">
                            <div class="mainData clearFloat">
                                <ul class="leftFloat">
                                    <li>
                                        <span>&nbsp;{{item.boxNo}}</span>
                                        <span>箱号</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.itemCode}}</span>
                                        <span>物料</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.pickTaskId}} </span>
                                        <span>任务</span>
                                    </li>
                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>&nbsp;{{item.quantity}} </span>
                                        <span>数量</span>
                                    </li>
                                    <li>
                                        <span>{{item.itemName}}</span>
                                        <span>名称</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.pickQuantity}}</span>
                                        <span>数量</span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </el-card>
                </li>
            </ul>
        </div>

    </div>
</template>
<script>
    import FeaturesHeader from "@/components/FeaturesHeader";

    export default {
        name: "ScanBox",
        data() {
            return {
                loading: false,
                noMore: false,

                headerTitle: "扫描箱号",
                formLabelWidth: '80px',
                searchData: {
                    pageNum: 1,
                    pageSize: 2,
                    total: null,
                    boxNo: ''
                },
                boxDetails: [],

                loadMasterId: this.$route.params.loadMasterId
            };
        },
        methods: {
            getBoxDetailData() {
                let that = this
                IOT.showOverlay('查询中~~~')
                IOT.getServerData('/box/details/list', 'get', {boxNo: that.searchData.boxNo, state: 0}, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        that.boxDetails = ret.rows
                    } else {
                        IOT.tips(ret.message || 'server wrong', 'error')
                    }
                })
            },
            loadBox() {
                IOT.showOverlay('保存中')
                IOT.getServerData('/load/masters/loadBox', 'post', {boxNo: this.searchData.boxNo, loadMasterId: this.loadMasterId}, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success')
                        this.getBoxDetailData()
                        this.boxDetails = []
                    } else {
                        IOT.tips('保存失败！', 'error')
                    }
                })
            },
            clearForm() {
            },

        },

        created() {
        },
        components: {
            FeaturesHeader,
        },
        mounted() {
        }
    };
</script>
<style lang="less" scoped>
    .baseAccept {
    }
</style>
