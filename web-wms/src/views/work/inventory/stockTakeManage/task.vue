<template>
  <div class="task">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="盘点单号/SKU" clearable></el-input>
        </div>
        <div>
          <span>类型1</span>
          <el-select v-model="searchData.type" size="mini" filterable placeholder="请选择盘点类型" style="width: 70%;">
            <el-option
                    v-for="item in stockTaskDetailType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div>
          <span>状态</span>
          <el-select v-model="searchData.state" size="mini" filterable placeholder="请选择盘点状态" style="width: 70%;">
            <el-option
                    v-for="item in stockTaskDetailState"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <!-- <el-button size="small" type="primary" round icon="iconfont icon-piliangchuli" @click="commitCalculate()" :disabled="multipleSelection.length <= 0"> 批量盘点
      </el-button>
      <el-button size="small" type="warning" round icon="iconfont icon-xiazai" @click="downloadStockTakeTask()"> 导出
      </el-button>
      <el-button size="small" type="success" round icon="iconfont icon-shangchuan" @click="uploadStockTakeTask()"> 导入
      </el-button>
      <input type="file" id="file"></input> -->
    </div>
    <div class="tableBox">
      <el-table
              highlight-current-row
              @selection-change="handleSelectionChange"
              v-loading="loading.masterTable"
              :data="tasks"
              style="width: 100%">
                <!-- <el-table-column
                  type="selection"
                  width="55">
                </el-table-column> -->
        <el-table-column
                fixed="left"
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                fixed="left"
                show-overflow-tooltip
                prop="billNo"
                label="盘点单号"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="type"
                label="类型"
                width="80">
          <template slot-scope="scope">
            <span v-for="(item,index) in stockTaskDetailType" :key="index" v-if="scope.row.type === item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskQuantity"
                label="状态"
                width="80">
         <template slot-scope="scope">
            <span :style="'color:'+item.color" v-for="(item,index) in stockTaskDetailState" :key="index" v-if="scope.row.state === item.value">{{item.label}}</span>
          </template>
        </el-table-column>
     
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="初盘"
          width="300">
           <el-table-column
          show-overflow-tooltip
          prop="differQuantity"
          label="盘盈(+)/盘亏(-)"
          width="80">
           <template slot-scope="scope" v-if="scope.row.differQuantity !== null">
            <span style="color:#409EFF" v-if="scope.row.differQuantity>=0"><span  >{{scope.row.differQuantity}}</span></span>
            <span style="color:rgba(255,3,7,0.87)" v-else-if="scope.row.differQuantity<0"><span  >{{scope.row.differQuantity}}</span></span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="实盘数量"
          width="100">
          <template scope="scope">
            <span style="font-weight:bold">{{scope.row.actualQuantity}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskQuantity"
                label="快照数量"
                width="120">
        </el-table-column>
        </el-table-column>
         <el-table-column
          show-overflow-tooltip
          prop="actualQuantity"
          label="复盘"
          width="300">
           <el-table-column
          show-overflow-tooltip
          prop="differQuantity2"
          label="盘盈(+)/盘亏(-)"
          width="80">
           <template slot-scope="scope" v-if="scope.row.differQuantity2 !== null">
            <span style="color:#409EFF" v-if="scope.row.differQuantity2>=0"><span  >{{scope.row.differQuantity2}}</span></span>
            <span style="color:rgba(255,3,7,0.87)" v-else-if="scope.row.differQuantity2<0"><span  >{{scope.row.differQuantity2}}</span></span>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="actualQuantity2"
          label="实盘数量"
          width="100">
          <template scope="scope">
            <span style="font-weight:bold">{{scope.row.actualQuantity2}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskQuantity2"
                label="快照数量"
                width="120">
        </el-table-column>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskUserName"
                label="盘点人"
                width="100">
        </el-table-column>
        <!-- <el-table-column
                show-overflow-tooltip
                prop="wareName"
                label="仓库"
                width="150">
        </el-table-column> -->
        <el-table-column
                show-overflow-tooltip
                prop="organizationName"
                label="货主"
                width="150">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="itemCode"
                label="SKU"
                width="120">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="itemName"
                label="SKU名称"
                width="120">
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="createTime"
                label="任务创建时间"
                width="120">
          <template slot-scope="scope">
            {{scope.row.createTime.substr(0,19)}}
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                prop="taskTime"
                label="任务时间"
                width="120">
          <template slot-scope="scope" v-if="scope.row.taskTime !== null ">
            {{scope.row.taskTime.substr(0,19)}}
          </template>
        </el-table-column>
        <!-- <el-pagination
                style="text-align: right;"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                :page-sizes="[15,30,50,100]"
                :current-page.sync="searchData.pageNum"
                :page-size="searchData.pageSize"
                layout="sizes, total, prev, pager, next, jumper"
                :total="searchData.total">
        </el-pagination> -->
      </el-table>
       <el-pagination
                style="text-align: right"
                class="pagination-bottom"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                :page-sizes="[15, 30, 50, 100]"
                :current-page.sync="searchData.pageNum"
                :page-size="searchData.pageSize"
                layout="sizes, total, prev, pager, next, jumper"
                :total="searchData.total"
            >
            </el-pagination>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
    name: "StockTakeTask",
    data() {
        return {
            isMoreSearch: false,
            isMoreSearch2: false,

            searchData: {
                pageNum: 1,
                pageSize: 15,
                total: null,
                keyWords: "",
                type: "",
                state: "",
                wareId: PF.getLocal("wareId", "number"),
            },

            loading: {
                masterTable: true,
            },

            searchSelectInventory: {
                pageNum: 1,
                pageSize: 5,
                total: null,
                keyWords: "",
                wareId: null,
                organizationId: null,
            },

            wares: [],
            organizations: [],
            tasks: [],
            inventorys: [],
            multipleSelection: [],
            selectIds: [],

            dialogNewVisible: false,
            dialogUpdateVisible: false,
            innerVisibleInventory: false,

            formLabelWidth: "120px",

            form: {
                taskMaster: {
                    memo: "",
                    wareId: null,
                    wareName: "",
                    organizationId: null,
                    organizationName: "",
                },
                tasks: [],
            },
        };
    },
    computed: {
        ...mapState([
            "abcLevels",
            "stockTaskMasterType",
            "stockTaskMasterState",
            "stockTaskMasterOrder",
            "stockTaskDetailType",
            "stockTaskDetailState",
        ]),
        isFreezeBtn() {
            if (this.multipleSelection.length <= 0) {
                return true;
            }
            let organizationId = this.multipleSelection[0].organizationId;
            for (let i = 0; i < this.multipleSelection.length; i++) {
                if (
                    organizationId !== this.multipleSelection[i].organizationId
                ) {
                    return true;
                }
            }
            return false;
        },
    },
    methods: {
        downloadStockTakeTask() {
            let searchData = PF.JSON(this.searchData);
            let that = this;
            IOT.showOverlay("正在导出，请稍等...");

            IOT.getServerData(
                "/stock/take/tasks/exportStockTakeTask",
                "get",
                searchData,
                (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        var path = ret.data;
                        window.open(path);
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                },
                true
            );
        },
        uploadStockTakeTask() {
            var formData = new FormData();
            formData.append("model", document.querySelector("#file").files[0]); //上传一个files对象
            IOT.showOverlay("正在处理，请稍等...");
            IOT.upLoadFile(
                "/stock/take/tasks/importStockTakeTask",
                "post",
                formData,
                (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips("保存成功！", "success", 1000, () => {
                            var file = document.getElementById("file");
                            file.value = ""; //虽然file的value值不能设为有内容的字符，但是可以设置为空字符
                            file.outerHTML = file.outerHTML;
                            this.getFreezeData();
                        });
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                },
                true
            );
        },
        commitCalculate() {
            let that = this;
            IOT.showOverlay("保存中，请稍等...");
            let form = [];
            form = PF.JSON(this.multipleSelection);
            for (let i = 0; i < form.length; i++) {
                if (
                    form[i].taskQuantity === null ||
                    form[i].taskQuantity === ""
                ) {
                    IOT.tips("选中栏请填写盘点数量！", "error", 1000, () => {
                        IOT.hideOverlay();
                    });
                    return false;
                }
            }
            IOT.getServerData(
                "/stock/take/tasks/calculateStockTake",
                "post",
                { stockTakeTasks: form },
                (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips("取消成功！", "success", 1000, () => {
                            that.getFreezeData();
                        });
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        handleSizeChange(val) {
            this.searchData.pageSize = val;
            this.getFreezeData();
        },
        handleCurrentChange(val) {
            this.pageNum = val;
            this.getFreezeData();
        },
        handleCurrentChange2(val) {
            this.searchSelectInventory.pageNum = val;
            this.getInventoryData();
        },
        clearSearchData() {
            this.searchData.keyWords = "";
            this.searchData.state = "";
            this.searchData.type = "";
        },
        clearSearchSelectInventory() {
            this.searchSelectInventory.keyWords = "";
            this.searchSelectInventory.wareId = null;
            this.searchSelectInventory.organizationId = null;
        },
      setPageNumFlush(){
        this.searchData.pageNum =1;
        this.getFreezeData();
      },
        getFreezeData() {
            let that = this;
            that.loading.masterTable = true;
            IOT.getServerData(
                "/stock/take/tasks/list",
                "get",
                this.searchData,
                (ret) => {
                    if (ret.code === 200) {
                        this.tasks = ret.rows;
                        this.searchData.total = ret.total;
                        this.searchData.pageNum = ret.pageNumber;
                        that.loading.masterTable = false;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        getWareData() {
            IOT.getServerData("/ware/infos/findSome", "get", {}, (ret) => {
                if (ret.code === 200) {
                    this.wares = ret.rows;
                } else {
                    IOT.tips(
                        ret.message || "服务器请求失败，稍后再试！",
                        "error"
                    );
                }
            });
        },
        getInventoryData() {
            IOT.getServerData(
                "/inventorys/list",
                "get",
                this.searchSelectInventory,
                (ret) => {
                    if (ret.code === 200) {
                        this.inventorys = ret.rows;
                        this.searchSelectInventory.total = ret.total;
                        this.searchSelectInventory.pageNum = ret.pageNumber;
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
        resetForm(formName) {
            // 重置
            this.$refs[formName].resetFields();
        },
        handleClose(done) {
            this.resetForm("form.freezeMaster");
            this.dialogNewVisible = false;
        },
        showSelectInventoryModal() {
            this.selectIds = [];
            for (let i = 0; i < this.form.freezeDetails.length; i++) {
                this.selectIds.push(this.form.freezeDetails[i].inventoryId);
            }
            this.getInventoryData();
            this.innerVisibleInventory = true;
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        clickSelectInventory() {
            let list = PF.JSON(this.multipleSelection);
            this.form.freezeMaster.wareId = list[0].wareId;
            this.form.freezeMaster.wareName = list[0].wareName;
            this.form.freezeMaster.organizationId = list[0].organizationId;
            this.form.freezeMaster.organizationName = list[0].organizationName;

            let isRepeat = false;
            for (let i = 0; i < list.length; i++) {
                list[i].nowFreezeQuantity = list[i].freezeQuantity;
                list[i].freezeQuantity = 0;
                if (this.form.freezeDetails.length > 0) {
                    if (
                        this.selectIds.indexOf(list[i].inventoryId) === -1 &&
                        list[i].organizationId ===
                            this.form.freezeDetails[0].organizationId
                    ) {
                        this.form.freezeDetails.push(list[i]);
                    } else {
                        isRepeat = true;
                    }
                } else {
                    if (this.selectIds.indexOf(list[i].inventoryId) === -1) {
                        this.form.freezeDetails.push(list[i]);
                    } else {
                        isRepeat = true;
                    }
                }
            }
            if (isRepeat) {
                IOT.tips("不能重复添加或添加非此货主！", "error");
            }
            this.innerVisibleInventory = false;
        },
        deleteDetail(index) {
            this.form.freezeDetails.splice(index, 1);
        },
        commitForm(formName, insertType) {
            let that = this;
            IOT.showOverlay("保存中，请稍等...");
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let form = this.form;
                    if (form.freezeDetails.length <= 0) {
                        IOT.tips(
                            "未选择库存,请重新选择！",
                            "error",
                            1000,
                            () => {
                                IOT.hideOverlay();
                            }
                        );
                        return false;
                    }
                    for (let i = 0; i < form.freezeDetails.length; i++) {
                        if (form.freezeDetails[i].freezeQuantity === 0) {
                            IOT.tips(
                                "数量不得为零,请重新输入！",
                                "error",
                                1000,
                                () => {
                                    IOT.hideOverlay();
                                }
                            );
                            return false;
                        }
                    }
                    form.insertType = insertType;
                    IOT.getServerData(
                        "/freeze/masters/insert",
                        "post",
                        form,
                        (ret) => {
                            IOT.hideOverlay();
                            if (ret.code === 200) {
                                IOT.tips("保存成功！", "success", 1000, () => {
                                    that.getInventoryBalanceData();
                                });
                            } else {
                                IOT.tips(
                                    ret.message || "服务器请求失败，稍后再试！",
                                    "error"
                                );
                            }
                        }
                    );
                    this.resetForm(formName);
                    this.dialogNewVisible = false;
                } else {
                    IOT.hideOverlay();
                    console.error("error submit!!");
                    return false;
                }
            });
        },
    },
    created() {
        this.getFreezeData();
        PF.getDataDictUdfData("freeze_reason", (rows) => {
            this.freezeReasons = rows;
        });
        this.getWareData();
        PF.getOrganizations((rows) => {
            this.organizations = rows;
        });
    },
    mounted() {},
};
</script>

<style scoped lang="less">
.freeze {
}
</style>
