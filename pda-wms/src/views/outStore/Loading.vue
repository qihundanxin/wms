<template>
    <div class="Loading">
        <FeaturesHeader :headerTitle="headerTitle"/>

        <div style="text-align: center;">
            <el-form class="noMargin BoxingPickTaskBox" :model="form" ref="form">
                <el-form-item label="装车单号" :label-width="formLabelWidth">
                    <el-input v-model="form.code" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="车牌号" :label-width="formLabelWidth">
                    <el-input v-model="form.licensePlateNo" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="车型" :label-width="formLabelWidth">
                    <el-select v-model="form.vehicleType" size="mini">
                        <el-option
                                v-for="item in vehicleTypes"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="司机" :label-width="formLabelWidth">
                    <el-input v-model="form.driverName" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="装车人员" :label-width="formLabelWidth">
                    <el-input v-model="form.loaderName" size="mini"></el-input>
                </el-form-item>

                <el-form-item label="仓库" :label-width="formLabelWidth">
                    <el-input disabled v-model="form.wareName" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="波次号" :label-width="formLabelWidth">
                    <el-input v-model="form.waveId" size="mini"></el-input>
                </el-form-item>
                <div style="float: left;width: 100%;">
                    <el-divider content-position="left"></el-divider>
                </div>
            </el-form>

            <el-button @click="getLoadMasterData" type="primary" size="mini" round icon="iconfont icon-dui"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 查询
            </el-button>
            <el-button @click="commitForm" type="primary" size="mini" round icon="iconfont icon-dui"
                       style="width: 30%;margin-top: 5px;margin-left: 10px;"> 提交
            </el-button>
        </div>

        <el-divider content-position="center">装车单</el-divider>
        <div class="baseAcceptBillBox noScrollbar">
            <ul>
                <li v-for="(item,index) in loadMasters">
                    <el-card class="clearFloat">
                        <div class="cardBody" @click="scanBox(item.id)">
                            <div class="mainData clearFloat">
                                <!-- 重要信息 -->
                                <ul class="leftFloat">
                                    <li>
                                        <span>&nbsp;{{item.code}}</span>
                                        <span>单号</span>
                                    </li>
                                    <li>
                                        <span>{{item.licensePlateNo}}</span>
                                        <span>车牌</span>
                                    </li>
                                </ul>
                                <ul class="rightFloat">
                                    <li>
                                        <span>&nbsp;{{item.driverName}}</span>
                                        <span>司机</span>
                                    </li>
                                    <li>
                                        <span>&nbsp;{{item.loaderName}} </span>
                                        <span>装卸人</span>
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
        name: "Loading",
        data() {
            return {
                headerTitle: "装车",
                formLabelWidth: '80px',
                searchData: {
                    pageNum: 1,
                    pageSize: 2,
                    total: null,
                },
                loadMasters: [],
                form: {
                    code: '',
                    licensePlateNo: '',
                    vehicleType: null,
                    driverName: '',
                    loaderName: '',
                    loadStart: '',
                    loadFinish: '',
                    sizeLimit: null,
                    weightLimit: null,
                    wareId: PF.getLocal('wareId'),
                    wareName: PF.getLocal('wareName'),
                    waveId: null,
                    desc1: '',
                    desc2: '',
                    desc3: '',
                    desc4: '',
                    loadDetails: []
                },
                vehicleTypes: [
                    {
                        value: 1,
                        label: 'type1'
                    },
                    {
                        value: 2,
                        label: 'type2'
                    },
                    {
                        value: 3,
                        label: 'type3'
                    },
                ],
            };
        },
        methods: {
            getLoadMasterData() {
                IOT.getServerData('/load/masters/list', 'get', {code: this.form.code}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        this.loadMasters = ret.rows
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            commitForm() {
                let form = PF.JSON(this.form)
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/load/masters', 'post', form, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000, () => {
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            scanBox(id) {
                this.$router.push({name:'scanBox', params: {loadMasterId: id}})
            }
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
