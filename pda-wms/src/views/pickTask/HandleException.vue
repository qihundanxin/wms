<template>
    <div>
        <FeaturesHeader :headerTitle="headerTitle"/>
        <div class="exceptionInfo">
            <div style="padding:3px 6px;">当前货位: <span style="color: #409EFF">{{exception.cellCode}}</span></div>
            <div style="padding:3px  6px;">SKU: <span style="color: #409EFF">{{exception.itemCode}}</span></div>
            <div style="padding:3px  6px;">商品名称: <span style="color: #409EFF">{{exception.itemName}}</span></div>
            <div style="padding:3px  6px;">异常上报数量: <span style="color: #F56C6C">{{exception.exceptionQty}}</span></div>
            <div style="padding:3px  6px;" v-for="(item, index) in exceptionTypeList" v-if="item.code == exception.exceptionType">异常原因: <span style="color: #409EFF">{{item.name}}</span></div>
            <div style="padding:3px  6px;">异常上报人: <span style="color: #409EFF">{{exception.userName}}</span></div>
            <div style="padding:3px  6px;">
                异常图片: <el-image v-for="url in exception.imgUrlList" :src="url"></el-image>
            </div>
            <div style="padding:3px  6px;">异常备注信息: <span style="color: #409EFF">{{exception.description}}</span></div>
        </div>
        <div style="text-align: center;margin-top: 20px;" v-if="exception.status == 0">
            <el-button @click="handleOnlyException()" type="primary" size="mini" round style="width: 200px;margin-top: 5px;"> 处理异常
            </el-button>
        </div>
    </div>
</template>

<script>
    import FeaturesHeader from "@/components/FeaturesHeader";
    export default {
        name: "HandleException",
        data(){
            return{
                headerTitle: "异常处理",
                exception: {},
                updateParam: {},
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
            handleOnlyException(){
                this.loading = true;
                IOT.getServerData('/pickBatch/exception/handleExcepiton', 'post', this.updateParam, (ret) => {
                    this.loading = false;
                    if (ret.code === 200) {
                        IOT.tips(ret.message);
                        this.$router.push({ name: "CellAndItemException",params: {} });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            }
        },
        created() {
            this.exception = this.$route.params;
            this.updateParam.id = this.exception.id;
            this.updateParam.cellCode = this.exception.cellCode;
            this.updateParam.itemCode = this.exception.itemCode;
        }
    }
</script>

<style scoped>
    .exceptionInfo{
        font-size: larger;
        font-weight: bold;
        margin-left: 10px;
    }
</style>