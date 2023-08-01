<template>
    <div class="fyRecord">
        <FeaturesHeader :headerTitle="headerTitle" />

        <template>
            <el-table :data="tableData" style="margin:2%;width:96%"  border :header-cell-style="{background:'#F2F2F2'}">
                <el-table-column  type="index" label="序号" width="50">
                </el-table-column>
                <el-table-column prop="soBillNo" label="订单号">
                    <template slot-scope="scope">
                        <span :style="scope.row.orderQuantity>scope.row.sendQuantity?'color:#D9001B':''">{{scope.row.soBillNo}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="orderQuantity" label="商品数量">
                </el-table-column>
                <el-table-column prop="sendQuantity" label="实际扫码数量" >
                </el-table-column>
            </el-table>
        </template>

    </div>
</template>

<script>
import FeaturesHeader from "@/components/FeaturesHeader";

export default {
    name: "fyRecord",
    data() {
        return {
            headerTitle: "装车扫码记录",
            tableData:[]

        }
    },
    components: {
        FeaturesHeader,
    },
    methods:{

        getSendList(){
            IOT.getServerData('/send/list/today', 'get', {pageNum: 1,
            pageSize: 100,}, (ret) => {
                if(ret.code==200){
                    console.log(ret);
                    this.tableData = ret.rows;
                    console.log(this.tableData);
                }
            })

        }

    },
    created(){
        this.getSendList();
    }
}

</script>