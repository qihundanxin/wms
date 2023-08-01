<template>
    <div v-loading="loading">
        <FeaturesHeader :headerTitle="headerTitle"/>
        <div class="exceptionInfo">
            <div style="padding:3px 6px;">当前货位: <span style="color: #409EFF">{{pickBatchException.cellCode}}</span></div>
            <div style="padding:3px  6px;">SKU: <span style="color: #409EFF">{{pickBatchException.itemCode}}</span></div>
            <div style="padding:3px  6px;">商品名称: <span style="color: #409EFF">{{pickBatchException.itemName}}</span></div>
            <div style="padding:3px  6px;">异常总数量: <span style="color: #F56C6C">{{pickBatchException.exceptionQty}}</span></div>
            <div style="padding:3px  6px;">异常已处理: <span style="color: #409EFF">{{pickBatchException.handleQty}}</span></div>
        </div>
        <el-divider content-position="center"></el-divider>
        <div class="baseAcceptBillBox noScrollbar">
            <el-card v-for="(item,index) in exceptionList" style="padding: 3px">
                <div @click="goHandleException(item)">
                    <div style="display: flex;justify-content: space-between;align-items:center;">
                        <div v-for="(exception, index) in exceptionTypeList" v-if="exception.code == item.exceptionType" style="font-size: larger;font-weight: bold;">异常原因: {{exception.name}}</div>
                        <div style="border: solid 1px #606266;border-radius: 5px;padding: 3px 10px;color: #F56C6C;font-weight: bolder">
                            {{item.exceptionQty}}
                        </div>
                    </div>
                    <div style="display: flex;justify-content: space-between;align-items:center;margin-top: 3px">
                        <div>上报时间:{{item.reportTime}}</div>
                        <div v-if="item.status == 0" style="color: #FFF; border-radius: 5px;padding: 3px 10px; background: #E6A23C">未处理
                        </div>
                        <div v-else-if="item.status == 2"
                             style="color: #FFF; border-radius: 5px;padding: 3px 10px; background: #606266">处理完成
                        </div>
                    </div>
                </div>
            </el-card>
        </div>
        <div style="text-align: center;margin-top: 20px;" v-if="pickBatchException.exceptionQty > pickBatchException.handleQty">
            <el-button @click="handleException()" type="primary" size="mini" round style="width: 200px;margin-top: 5px;"> 批量处理异常
            </el-button>
        </div>
    </div>
</template>

<script>
    import FeaturesHeader from "@/components/FeaturesHeader";

    export default {
        name: "CellAndItemException",
        data() {
            return {
                headerTitle: "异常处理",
                formLabelWidth: '10px',
                loading: false,
                noMore: false,
                pickBatchException: {},
                searchData:{},
                exceptionList: [],
                exceptionTypeList: [
                    {"code": 0, "name": "其他"},
                    {"code": 1, "name": "有货损"},
                    {"code": 2, "name": "数量不够"},
                    {"code": 3, "name": "货物找不到"},
                    {"code": 4, "name": "货位找不到"},
                    {"code": 5, "name": "商品扫码不正确"},
                ]
            }
        },
        components: {
            FeaturesHeader
        },
        methods: {
            getCellAndItemException(){
                this.loading = true;
                IOT.getServerData('/pickBatch/exception/getCellAndItemExceptionList', 'post', this.searchData, (ret) => {
                    this.loading = false;
                    if (ret.code === 200) {
                        this.pickBatchException = ret.data.pickBatchException;
                        this.exceptionList = ret.data.list;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                }, true)
            },
            goHandleException(param){
                // 缓存本页查询参数
                if(this.searchData){
                    PF.setLocal('batchHandleExceptionParam',this.searchData);
                }
                param.itemName = this.pickBatchException.itemName;
                this.$router.push({ name: 'HandleException', params: param });
            },
            handleException(){
                this.loading = true;
                IOT.getServerData('/pickBatch/exception/handleExcepiton', 'post', this.searchData, (ret) => {
                    this.loading = false;
                    if (ret.code === 200) {
                        IOT.tips(ret.message);
                        this.$router.push({ name: "HandlePickBatchException",params: {} });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            }
        },
        created() {
            if (JSON.stringify(this.$route.params) == "{}"){
                this.searchData = PF.getLocal("batchHandleExceptionParam",'json');
            }else {
                this.searchData.cellCode = this.$route.params.cellCode;
                this.searchData.itemCode = this.$route.params.itemCode;
            }
            this.getCellAndItemException();
        },
    }
</script>

<style scoped>
    .exceptionInfo{
        font-size: larger;
        font-weight: bold;
        margin-left: 10px;
    }
</style>