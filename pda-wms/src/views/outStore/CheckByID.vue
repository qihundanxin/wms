<template>
  <div class="checkByID">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div style="text-align: center;">
      <el-form class="noMargin" :model="searchData" ref="form">
<!--        <el-form-item label="跟踪号" :label-width="formLabelWidth">-->
<!--          <el-input v-model="trackCode" size="mini" clearable style="width: 90%;"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="订单号" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.soNo" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="目标库位" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.toCell" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="数量" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.quantity" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="收货人" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.receiver" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="物资编码" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.itemCode" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="物资名称" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.itemName" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="单位" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.unit" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="自定义1" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.desc1" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="自定义2" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.desc2" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="自定义3" :label-width="formLabelWidth">-->
<!--          <el-input disabled v-model="searchSoData.desc3" size="mini" style="width: 90%;">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <el-button @click="searchSo" type="primary" size="mini" round icon="iconfont icon-chaxun" style="width: 200px;margin-top: 5px;"> 查询</el-button>
    </div>


  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";

  export default {
    name: "CheckByID",
    data() {
      return {
        headerTitle: "按 ID 复核",
        formLabelWidth: '120px',

        trackCode: '',
        searchSoData: {
          soNo: '',
          toCell: '',
          quantity: '',
          receiver: '',
          itemCode: '',
          itemName: '',
          unit: '',
          desc1: '',
          desc2: '',
          desc3: ''
        }

      };
    },
    methods: {
      searchSo(){
        let that = this
        IOT.getServerData('/so/masters/pdaSearchSo','get', {trackCode: that.trackCode},(ret) => {
          if (ret.code === 200) {
            that.searchSoData = ret.rows[0]
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
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
