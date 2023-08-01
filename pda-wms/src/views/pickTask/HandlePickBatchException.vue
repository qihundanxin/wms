<template>
    <div v-loading="loading">
        <FeaturesHeader :headerTitle="headerTitle"/>
        <div style="text-align: center;">
            <br/>
            <el-form label-width="10px" class="noMargin" :model="searchData" ref="form">
                <el-form-item :label-width="formLabelWidth">
                    <el-date-picker v-model="searchData.date" type="date" format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd 00:00:00" placeholder="请选择异常上报时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <el-button @click="getExceptionData()" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 搜索
            </el-button>
        </div>

        <el-divider content-position="center"></el-divider>

        <div class="baseAcceptBillBox noScrollbar">
            <el-card v-for="(item,index) in exceptionData" style="padding: 3px">
                <div @click="goCellAndItemException(item)">
                    <div style="display: flex;justify-content: space-between;align-items:center;">
                        <div>SKU: {{item.itemCode}}</div>
                        <div style="border: solid 1px #606266;border-radius: 5px;padding: 3px 10px;color: #F56C6C;font-weight: bolder">
                            {{item.exceptionQty}}
                        </div>
                    </div>
                    <div style="display: flex;justify-content: space-between; font-size: larger; font-weight: bolder;margin-top: 3px">
                        <div>货位: {{item.cellCode}}</div>
                    </div>
                    <div style="display: flex;justify-content: space-between;align-items:center;margin-top: 3px">
                        <div>{{item.reportTime}}</div>
                        <div v-if="item.noHandleQty > 0 && item.handleQty == 0"
                             style="color: #FFF; border-radius: 5px;padding: 3px 10px; background: #E6A23C">未处理
                        </div>
                        <div v-else-if="item.noHandleQty > 0 && item.handleQty > 0"
                             style="color: #FFF; border-radius: 5px;padding: 3px 10px; background: #67C23A">处理中
                        </div>
                        <div v-else-if="item.noHandleQty == 0 && item.handleQty > 0"
                             style="color: #FFF; border-radius: 5px;padding: 3px 10px; background: #606266">处理完成
                        </div>
                    </div>
                    <div style="background: #409EFF; color: #FFF;margin-top: 3px">{{item.itemName}}</div>
                </div>
            </el-card>
            <p v-show="exceptionData.length <= 0">暂无数据</p>
            <p v-show="loading">加载中...</p>
            <p v-show="noMore">没有更多了</p>
        </div>
    </div>
</template>

<script>
    import FeaturesHeader from "@/components/FeaturesHeader";

    export default {
        name: "HandlePickBatchException",
        data() {
            return {
                headerTitle: "异常拣货处理",
                formLabelWidth: '10px',
                loading: false,
                noMore: false,
                exceptionData: [],
                searchData: {
                    date: ""
                }
            }
        },
        components: {
            FeaturesHeader
        },
        methods: {
            getExceptionData() {
                this.loading = true;
                IOT.getServerData('/pickBatch/exception/getExceptionList', 'post', this.searchData, (ret) => {
                    this.loading = false;
                    if (ret.code === 200) {
                        this.exceptionData = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
            goCellAndItemException(param) {
                // 缓存本页查询参数
                if(this.searchData){
                    PF.setLocal('HandleExceptionListParam',this.searchData);
                }
                this.$router.push({ name: 'CellAndItemException', params: param })
            }
        },
        created() {
            const cacheSearchData = PF.getLocal("HandleExceptionListParam",'json');
            if (cacheSearchData){
                this.searchData = cacheSearchData;
            }
            this.getExceptionData();
            PF.removeLocal("HandleExceptionListParam");
        }
    }
</script>

<style scoped>

</style>