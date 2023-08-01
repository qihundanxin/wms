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

                <el-form-item label="箱号" :label-width="formLabelWidth">
                    <el-input v-model="form.boxNo" size="mini" style="width: 60%;" @blur="getBoxDetailData">
                    </el-input>
                    <el-button @click="generateBoxCode" type="primary" size="mini" round icon="iconfont icon-xinzeng"
                               style="width: 27%;margin-top: 5px;margin-left: 10px;"> 生成
                    </el-button>
                </el-form-item>
                <el-form-item label="物资编码" :label-width="formLabelWidth">
                    <el-input v-model="form.itemCode" @blur="itemCodeScanned" size="mini" style="width: 90%;">
                    </el-input>
                </el-form-item>

                <el-form-item label="批次" :label-width="formLabelWidth">
                    <el-select v-model="inventoryId" size="mini" @change="batchChanged" style="width: 90%">
                        <el-option
                        v-for="item in inventories"
                        :key="item.inventoryId"
                        :label="item.batchName"
                        :value="item.inventoryId">
                        </el-option>
                    </el-select>
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

            <el-button @click="newBox" type="primary" size="mini" round icon="iconfont icon-jia1"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 新箱
            </el-button>
            <el-button @click="boxing" type="primary" size="mini" round icon="iconfont icon-dui"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 装箱
            </el-button>
        </div>

        <el-divider content-position="center"></el-divider>

        <el-tabs type="card">
            <el-tab-pane label="拣货任务">
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
            </el-tab-pane>
            <el-tab-pane label="装箱明细">
                <div class="baseAcceptBillBox noScrollbar">
                    <ul v-infinite-scroll="lazy">
                        <li v-for="(item,index) in boxDetails">
                            <el-card class="clearFloat">
                                <div class="cardBody">
                                    <div class="mainData clearFloat">
                                        <!-- 重要信息 -->
                                        <ul class="leftFloat">
                                            <li>
                                                <span>&nbsp;{{item.itemCode}}</span>
                                                <span>物料</span>
                                            </li>
                                            <li>
                                                <span>{{item.itemName}}</span>
                                                <span>名称</span>
                                            </li>
                                        </ul>
                                        <ul class="rightFloat">
                                            <li>
                                                <span>&nbsp;{{item.quantity}}</span>
                                                <span>数量</span>
                                            </li>
                                            <li>
                                                <span>&nbsp;{{item.pickTaskId}} </span>
                                                <span>任务号</span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="cardBottomBtn clearFloat">
                                    <div class="rightFloat" @click="withdraw(item.boxDetailId)">撤销装箱</div>
                                </div>
                            </el-card>
                        </li>
                    </ul>
                </div>
            </el-tab-pane>
        </el-tabs>


    </div>
</template>
<script>
    import FeaturesHeader from "@/components/FeaturesHeader";
    import WaveSeeding from "./WaveSeeding";

    export default {
        name: "BoxingBySo",
        data() {
            return {
                loading: false,
                noMore: false,

                headerTitle: "按订单装箱",
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
                inventories: [],
                inventoryId: null,
                form: {
                    soMasterId: null,
                    soDetailId: null,
                    billNo: '',
                    boxNo: '',
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
                boxDetails: []


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
            getBoxDetailData() {

                let that = this
                if (!PF.isNull(that.form.boxNo)) {
                    IOT.tips('箱号不能为空！', 'error')
                    return false
                }
                IOT.getServerData('/box/details/list', 'get', {boxNo: that.form.boxNo}, (ret) => {
                    if (ret.code === 200) {
                        that.boxDetails = ret.rows
                    } else {
                        IOT.tips(ret.message || 'server wrong', 'error')
                    }
                })
            },
            lazy(){
                if(this.pickTasks.length > 0 && !this.noMore && !this.loading){
                    this.getPickTaskData();
                }
            },
            itemCodeScanned() {
                this.getInventoryData()
            },
            getInventoryData() {
                IOT.getServerData('/inventorys/checkList', 'get', {itemCode: this.form.itemCode, billNo: this.searchData.billNo}, (ret) => {
                    if (ret.code === 200) {
                        this.inventories = ret.rows
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            batchChanged() {
                let form = this.form
                for (let pickTask of this.pickTasks) {
                    if (form.itemCode === pickTask.itemCode && this.inventoryId === pickTask.inventoryId) {
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
            generateBoxCode() {
                let no = 'box_no_' + Math.round(Math.random() * 100)
                this.form.boxNo = no
            },
            boxing() {
                let that = this;
                if (!PF.isNull(this.form.boxNo)) {
                    IOT.tips('未输入箱号！', 'error');
                    return false
                }
                IOT.showOverlay('保存中，请稍等...');
                let form = PF.JSON(this.form)
                IOT.getServerData('/box/details/insert', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getPickTaskData()
                            that.getBoxDetailData()
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
                this.boxDetails = []
            },
            clearForm() {
                this.form.itemCode = ''
                this.form.itemName = ''
                this.form.quantity = null
                this.form.pickQuantity = null
                this.form.packDescribe = ''
                this.form.batchName = ''
                this.form.memo = ''
                this.inventoryId = null
            },
            withdraw(id) {
                let that = this
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/box/details/withdraw', 'get', {boxDetailId: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                            that.getPickTaskData()
                            that.getBoxDetailData()
                            that.clearForm()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            }
        },

        created() {
        },
        components: {
            WaveSeeding,
            FeaturesHeader,
        }
    };
</script>
<style lang="less" scoped>
    .baseAccept {
    }
</style>
