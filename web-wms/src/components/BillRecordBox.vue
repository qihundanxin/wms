<template>
  <div class="billRecordBox">

    <div style="height: 200px; overflow: auto;" >
      <el-table
          highlight-current-row
          :data="billRecords"
          style="width: 100%">
        <el-table-column
            fixed="left"
            type="index"
            width="50">
        </el-table-column>
        <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="recordType"
            label="节点名称"
            width="160">
          <template slot-scope="scope">
            <span v-for="(item, index) in recordTypes" :key="item.label + index" v-if="item.value.indexOf(scope.row.recordType) !== -1">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="memo"
            label="备注"
            width="190">

        </el-table-column>
        <el-table-column

            show-overflow-tooltip
            prop="createTime"
            label="操作时间"
            width="170">
          <template slot-scope="scope">
            <span v-if="scope.row.createTime">{{scope.row.createTime.substr(0,19)}}</span>
            <span v-else> - </span>
          </template>
        </el-table-column
>
        <el-table-column 
            show-overflow-tooltip
            prop="createUserName"
            label="操作人员"
            width="90">
          <template slot-scope="scope">
<!--            <span class="textBtn" @click="$parent.showPublicModal(scope.row.createUserId, 'account')">{{scope.row.createUserName}}</span>-->
<!--            <span class="textBtn" @click="this.showPublicModal(scope.row.createUserId,'account')">{{scope.row.createUserName}}</span>-->
            <span class="textBtn" @click="callGrandfatherMethod(scope.row.createUserId)">{{scope.row.createUserName}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
            sortable
            show-overflow-tooltip
            prop="modifyTime"
            label="结束时间"
            width="130">
          <template slot-scope="scope">
            <span v-if="scope.row.modifyTime">{{scope.row.modifyTime.substr(5,14)}}</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="modifyUserName"
            label="结束人"
            width="90">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.modifyUserId, 'account')">{{scope.row.modifyUserName}}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="time"
            label="时长(h)"
            width="100">
        </el-table-column> -->

      </el-table>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default{
    inject: ['showPublicModal'],
    name: 'BillRecordBox',
    props: [
      "masterData",
      "activeName"
    ],
    data(){
      return {
        billRecords: [],
      }
    },
    computed: {
      masterBillNo(){
        return this.masterData
      },
      ...mapState([
        "recordTypes"
      ])
    },
    watch: {
      activeName(val){
        if(val === 'billRecord'){
          if(PF.isNull(val)){
            this.getBillRecordData()
          }else{
            IOT.tips('未获取到单号！', 'error');
          }
        }
      }
    },
    methods: {
        callGrandfatherMethod(createUserId){
            const modalName = 'account'
            this.showPublicModal(createUserId,modalName)
        },
      getBillRecordData() {
        IOT.getServerData('/bill/records/list', 'get', {
          billNo: this.masterBillNo,
          wareId: PF.getLocal('wareId', 'number'),
        }, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].time = PF.calcTime( list[i].createTime, list[i].modifyTime );
            }
            this.billRecords = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

    },
    created() {
     
    },
    mounted() {
     //  this.getBillRecordData();
    }
  }
</script>
