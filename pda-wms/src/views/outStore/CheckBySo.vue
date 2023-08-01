<template>
    <div class="boxingBySo">
        <FeaturesHeader :headerTitle="headerTitle"/>

        <div style="text-align: center;">
            <el-form class="noMargin BoxingPickTaskBox" :model="form" ref="form">

                <el-form-item label="订单号" :label-width="formLabelWidth">
                    <el-input v-model="searchData.billNo" size="mini" style="width: 60%;">
                    </el-input>
                    <el-button @click="getPickTaskData" type="primary" size="mini" round icon="iconfont icon-chaxun"
                               style="width: 27%;margin-top: 5px;margin-left: 10px;"> 查询
                    </el-button>
                </el-form-item>

                <el-form-item label="物资编码" :label-width="formLabelWidth">
                    <el-input v-model="form.itemCode" @blur="itemCodeScanned" size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>
                <el-form-item label="物资名称" :label-width="formLabelWidth">
                    <el-input v-model="form.itemName" disabled size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>
                <el-form-item label="数量" :label-width="formLabelWidth">
                    <el-input-number :min="0" :max="form.pickQuantity" v-model="form.quantity" size="mini" style="width: 90%;">
                    </el-input-number>
                </el-form-item>
                <el-form-item label="拣货数量" :label-width="formLabelWidth" style="width: 50%; display:inline-block;">
                    <el-input v-model="form.pickQuantity" disabled size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>
                <el-form-item label="单位" :label-width="formLabelWidth" style="width: 50%; display:inline-block;">
                    <el-input v-model="form.packDescribe" disabled size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>
                <el-form-item label="批次" :label-width="formLabelWidth">
                    <el-input v-model="form.batchName" disabled size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>
                <el-form-item label="备注" :label-width="formLabelWidth">
                    <el-input v-model="form.memo" size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>

            </el-form>

            <el-button @click="boxing" type="primary" size="mini" round icon="iconfont icon-dui"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 确认
            </el-button>
        </div>

        <el-divider content-position="center">拣货单</el-divider>

        <div class="baseAcceptBillBox noScrollbar">
            <ul v-infinite-scroll="lazy">
                <li v-for="(item,index) in pickTasks">
                    <el-card class="clearFloat">
                        <div class="cardBody">
                            <div class="topData clearFloat">
                                <!-- 头部信息 -->
                                <span>{{index+1}}/{{searchData.total}}</span>
                                <span>D3型卸扣</span>
                                <span>IM001</span>
                            </div>
                            <div class="minorData">
                                <ul>
                                    <li>
                                        <span>物资编码:</span>
                                        <span>&nbsp;{{item.itemCode}}</span>
                                    </li>
                                    <li>
                                        <span>物资名称:</span>
                                        <span>{{item.itemName}}</span>
                                    </li>
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
                                        <span style="width: 50px;">&nbsp;{{item.pickQuantity}}</span>
                                        <span>拣货数量</span>
                                    </li>
                                    <li>
                                        <span style="width: 50px;">{{item.reviewQuantity}}</span>
                                        <span>复核数量</span>
                                    </li>
                                </ul>
                                <ul class="leftFloat">
                                    <li>
                                        <span style="width: 50px;">&nbsp;{{(item.pickQuantity*100 - item.reviewQuantity*100)/100}}</span>
                                        <span>未复核数量</span>
                                    </li>
                                    <li>
                                    </li>
                                </ul>
                            </div>
                        </div>

                    </el-card>
                </li>
            </ul>
            <p v-show="pickTasks.length <= 0">暂无数据</p>
            <p v-show="loading">加载中...</p>
            <p v-show="noMore">没有更多了</p>
        </div>

    </div>
</template>
<script>
    import FeaturesHeader from "@/components/FeaturesHeader";

    export default {
        name: "CheckBySo",
        data() {
            return {
                loading: false,
                noMore: false,

                headerTitle: "按订单复核",
                formLabelWidth: '80px',
                searchData: {
                    pageNum: 1,
                    pageSize: 2,
                    total: null,
                    billNo: 'SO-44',
                    tag: 1
                },
                soMaster: {},
                pickTasks: [],
                form: {
                    soMasterId: null,
                    soDetailId: null,
                    billNo: '',
                    itemCode: '',
                    itemName: '',
                    batchs: '',
                    batchId: '',
                    batchName: '',
                    quantity: 0,
                    pickTaskId: null,
                    pickQuantity: 0,
                    packDetailId: null,
                    packDescribe: '',
                    memo: ''
                },


            };
        },
        methods: {

            getPickTaskData() {
                let that = this
                if (!PF.isNull(that.searchData.billNo)) {
                    IOT.tips('请输入单号！', 'error');
                    return false;
                }
                this.loading = true;
                IOT.getServerData('/pick/tasks/boxingList', 'get', that.searchData, (ret) => {
                    this.loading = false;
                    if (ret.code === 200) {
                        that.pickTasks = ret.rows;
                        that.searchData.total = ret.total;
                        that.searchData.pageNum = ret.pageNumber;
                        if(this.searchData.pageSize >= ret.total){
                            this.noMore = true;
                        }else{
                            this.noMore = false;
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            lazy() {
                if (this.pickTasks.length > 0 && !this.noMore && !this.loading) {
                    this.getPickTaskData();
                }
            },
            itemCodeScanned() {
                let form = this.form
                for (let pickTask of this.pickTasks) {
                    if (form.itemCode == pickTask.itemCode) {
                        form.itemName = pickTask.itemName
                        form.batchName = pickTask.batchName
                        form.batchId = pickTask.batchId
                        form.packDetailId = pickTask.packDetailId
                        form.packDescribe = pickTask.packDescribe
                        form.pickTaskId = pickTask.pickTaskId
                        form.pickQuantity = pickTask.pickQuantity
                        form.soMasterId = pickTask.soMasterId
                        form.soDetailId = pickTask.soDetailId
                    }
                }
            },
            boxing() {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                let form = PF.JSON(this.form)
                form.billNo = that.searchData.billNo
                IOT.getServerData('/pick/tasks/checkBySo', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getPickTaskData()
                            that.clearForm()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            newBox() {
                this.clearForm()
                this.form.boxNo = ''
            },
            clearForm() {
                this.form.itemCode = ''
                this.form.itemName = ''
                this.form.quantity = null
                this.form.pickQuantity = null
                this.form.packDescribe = ''
                this.form.batchName = ''
                this.form.memo = ''
            },
        },

        created() {
        },
        components: {
            FeaturesHeader,
        }
    };
</script>
<style lang="less" scoped>
    .baseAccept {
    }
</style>
