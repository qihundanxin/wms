<template>
    <div class="acceptAuditRecord">

        <div class="publicSearch">
            <div class="searchBar" :style="{ height: isMoreSearch ? 'auto' : '' }">
                <el-button-group>
                    <el-button @click="setPageNumFlush" size="mini" icon="el-icon-search">查询</el-button>
                    <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
                </el-button-group>
                <div>
                    <span>审批状态</span>
                    <el-select v-model="searchData.auditState" size="mini" filterable placeholder="请选择审批状态" clearable>
                        <el-option v-for="item in acceptAuditState" :key="item.value" :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div>
                    <span>SKU名称</span>
                    <el-input v-model="searchData.itemName" size="mini" placeholder="SKU名称" clearable></el-input>
                </div>
                <div>
                    <span>SKU</span>
                    <el-input v-model="searchData.itemCode" size="mini" placeholder="SKU" clearable></el-input>
                </div>
                <div>
                    <span>验收人</span>
                    <el-input v-model="searchData.acceptUserName" size="mini" placeholder="请输入验收人名称"
                        clearable></el-input>
                </div>
                <div>
                    <span>货位编码</span>
                    <el-input v-model="searchData.cellCode" size="mini" placeholder="请输入货位编码" clearable></el-input>
                </div>
            </div>
            <span class="moreSearchBtn" :class="isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
                @click="isMoreSearch = !isMoreSearch"></span>
        </div>

        <div class="headerBtns">
            <el-button size="small" type="primary" icon="iconfont icon-xinzeng" 
            :disabled="isCanAudit"
            @click="audit">盲收审批</el-button>
        </div>

        <div class="tableBox">
            <el-table v-loading="loading.table" highlight-current-row :data="acceptAuditRecords"
                @selection-change="handleSelectionChange" style="width: 100%"  @sort-change="sortChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column show-overflow-tooltip prop="cellUseType" label="状态" min-width="100" >
                    <template slot-scope="scope">
                        <span v-if="scope.row.auditState == 0" style="color:#909399">待审批</span>
                        <span v-if="scope.row.auditState == -1" style="color:#F56C6C">审批不通过</span>
                        <span v-if="scope.row.auditState == 1" style="color: #67c23a">审批通过</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="organizationName" label="货主" width="120">
                    <template slot-scope="scope">
                        <span class="textBtn"
                            @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{
                                scope.row.organizationName
                            }}</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="itemName" label="SKU名称" width="400">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                            scope.row.itemName
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="itemCode" label="SKU" width="160">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{
                            scope.row.itemCode
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="quantity" label="数量" width="80">
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="cellCode" label="货位编码" width="180">
                </el-table-column>

                <el-table-column show-overflow-tooltip prop="auditUserName" label="审批人" width="120">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.auditUserId, 'account')"
                            type="text">
                            {{ scope.row.auditUserName }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="auditTime" label="审批时间" width="180">
                    <template slot-scope="scope">
                        <span v-if="scope.row.auditTime">{{ scope.row.auditTime }}</span>
                        <span v-else> - </span>
                    </template>
                </el-table-column>

                <el-table-column show-overflow-tooltip prop="acceptUserName" label="验收人" width="120">
                    <template slot-scope="scope">
                        <span class="textBtn" @click="$parent.showPublicModal(scope.row.acceptUserId, 'account')"
                            type="text">
                            {{ scope.row.acceptUserName }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="acceptTime" label="收货时间" width="180" sortable="custom">
                    <template slot-scope="scope">
                        <span v-if="scope.row.acceptTime">{{ scope.row.acceptTime }}</span>
                        <span v-else> - </span>
                    </template>
                </el-table-column>

            </el-table>
            <el-pagination class="pagination-bottom" style="text-align: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page.sync="searchData.pageNum"
                :page-sizes="[15, 30, 50, 100]" :page-size="searchData.pageSize" layout="sizes,total, prev, pager, next"
                :total="searchData.total">
            </el-pagination>

        </div>

        <el-dialog title="盲收审批" :visible.sync="dialogVisible" :before-close="handleClose" width="900px"
            style="margin-top: -10vh">

            <div class="packMasterBox" >
                <div class="auditDetail" >
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <div class="grid-content bg-purple">
                                <span>SKU:</span>
                                <span :title="record.itemCode">&nbsp;{{record.itemCode}}</span>
                            </div>
                        </el-col>
                        <el-col :span="12">
                            <div class="grid-content bg-purple-light">
                                <span>盲收数量:</span>
                            <span :title="record.quantity">&nbsp;{{record.quantity}}</span>
                            </div>
                        </el-col>
                        </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <div class="grid-content bg-purple">
                                <span>SKU名称:</span>
                                <span :title="record.itemName">&nbsp;{{record.itemName}}</span>
                            </div>
                        </el-col>
                        <el-col :span="12">
                            <div class="grid-content bg-purple-light">
                                <span>验收货位:</span>
                                <span :title="record.cellCode">&nbsp;{{record.cellCode}}</span>
                            </div>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <div class="grid-content bg-purple">
                                <span>盲收提交人:</span>
                                <span :title="record.acceptUserName">&nbsp;{{record.acceptUserName}}</span>
                            </div>
                        </el-col>
                        <el-col :span="12">
                            <div class="grid-content bg-purple-light">
                                <span>盲收提交时间:</span>
                                <span :title="record.acceptTime">&nbsp;{{record.acceptTime}}</span>
                            </div>
                        </el-col>
                    </el-row>
                </div>

            </div>
            <div style="margin-top:70px;margin-bottom:20px">
                <span style="font-weight: bold; margin-left:40px;margin-right:40px">审批结果:</span>
                <template>
                    <el-radio v-model="auditRadio" label="1">审批通过</el-radio>
                    <el-radio v-model="auditRadio" label="-1">审批不通过</el-radio>
                </template>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="auditRecords">确 定</el-button>
            </div>
        </el-dialog>


    </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
    name: 'AcceptAuditRecord',
    data() {
        return {
            isMoreSearch: false,
            loading: {
                table: true,
            },
            searchData: {
                pageNum: 1,
                pageSize: 15,
                total: null,
                wareId: PF.getLocal('wareId', 'number'),
                itemCode: '',
                itemName: '',
                auditState: null,
                cellCode: '',
                acceptUserName: '',
                acceptTimeSort:''
            },
            acceptAuditRecords: [],
            multipleSelection: [],
            dialogVisible: false,
            auditRadio:'1',
            record:{}
        }
    },
    computed: {
        ...mapState([
            "acceptAuditState"
        ]),
        isCanAudit() {
            if (this.multipleSelection.length == 1) {
                return false
            }
            return true
        }
    },
    methods: {

        handleSizeChange(val) {
            this.searchData.pageSize = val;
            this.getAcceptAuditRecordData();
        },
        handleCurrentChange(val) {
            this.searchData.pageNum = val;
            this.getAcceptAuditRecordData()
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(this.multipleSelection.length>0){
                this.record = this.multipleSelection[0];
            }

        },

        handleClose(done) {
            this.dialogVisible = false;
        },
        resetForm(formName) { // 重置
            this.$refs[formName].resetFields();
        },

        //收货时间排序
        sortChange(column) {
            if (column.order == "ascending") {
                this.searchData.acceptTimeSort = "ASC";
            } else if(column.order=="descending"){
                this.searchData.acceptTimeSort = "DESC"
            }
            this.getAcceptAuditRecordData();
        },


        clearSearchData() {
            this.searchData.itemCode = '';
            this.searchData.itemName = '';
            this.searchData.auditState = '';
            this.searchData.cellCode = '';
            this.searchData.acceptUserName = '';
            this.searchData.acceptTimeSort = ''
        },

        setPageNumFlush(){
            this.searchData.pageNum =1;
            this.getAcceptAuditRecordData();
        },
        //列表查询
        getAcceptAuditRecordData() {
            this.loading.table = true;
            IOT.getServerData('/accept/audit/records/list', 'post', this.searchData, (ret) => {
                this.loading.table = false;
                if (ret.code === 200) {
                    this.acceptAuditRecords = ret.rows;
                    this.searchData.total = ret.total;
                    this.searchData.pageNum = ret.pageNumber
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
            });
        },
        audit(){
            if(this.record.auditState!=0){
                IOT.tips('该订单已审核！', 'error');
            }else{
                this.dialogVisible=true;
            }  
        },

        //盲收审批
        auditRecords() {
            IOT.showOverlay('提交审批中，请稍等...');
            //审批通过
            if (this.auditRadio == 1) {
                IOT.getServerData('/accept/audit/records/approved', 'post', { id: this.record.acceptAuditRecordId }, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('审批成功！', 'success');
                        this.dialogVisible = false;
                        this.getAcceptAuditRecordData();
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            //审批不通过
            } else {
                IOT.getServerData('/accept/audit/records/notApproved', 'post', { id: this.record.acceptAuditRecordId }, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('审批成功！', 'success');
                        this.dialogVisible = false;
                        this.getAcceptAuditRecordData();
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            }

        }
        
 

    },
    created() {
        this.getAcceptAuditRecordData();
    }
}
</script>

<style scoped lang='less'>
.acceptAuditRecord{
    el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .packMasterBox{
    margin-left:40px;
  }
}
</style>

