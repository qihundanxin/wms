<template>
  <div class="filterShelf">
    <div class="headerBox clearFloat">
      <!-- <span class="el-icon-arrow-left leftFloat clickColor" @click="backOut()"></span> -->
      <input type="text" class="headerInput" :placeholder="params.name" />
      <i class="el-icon-search clickColor" @click="qwe()"></i>
    </div>
    <div id="checkbox">
      <el-checkbox
        :indeterminate="isIndeterminate"
        v-model="checkAll"
        @change="handleCheckAllChange"
      >全选</el-checkbox>
      <!-- <div style="margin: 15px 0;"></div> -->
      <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
        <el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>
      </el-checkbox-group>
    </div>
    <FeaturesFooter :footerBtn="footerBtn" />
  </div>
</template>
<script>
import FeaturesFooter from "@/components/FeaturesFooter";
const cityOptions = ["电脑7", "罗技鼠标", "LG手机02", "秋裤", "LG手机01"];
export default {
  name: "filterShelf",
  data() {
    return {
      footerBtn: "确定",
      params: this.$route.params,
      checkAll: false,
      checkedCities: [],
      cities: cityOptions,
      isIndeterminate: false
    };
  },
  methods: {
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
    }
  },
  created() {},

  components: {
    FeaturesFooter
  }
};
</script>
<style lang="less" scoped>
.filterShelf {
  .headerBox {
    height: 50px;
    background: #f6f8f7;
    border-bottom: 2px solid #f0f0f0;
    font-size: 30px;
    display: flex;
    align-items: center;
    position: relative;
    .leftFloat {
      margin-left: 12px;
    }
    .headerInput {
      background: #e9e9eb;
      width: 60%;
      position: absolute;
      left: 50%;
      margin-left: -30%;
      border: 0;
      height: 32px;
      border-radius: 8px;
      text-align: center;
      outline: 0;
    }
    i {
      position: absolute;
      right: 0;
      margin-right: 12px;
    }
    .headerInput::-webkit-input-placeholder {
      color: #909399;
      font-size: 14px;
      text-align: center;
    }
  }
  #checkbox {
    width: 90%;
    margin: 0 5%;
    //   .el-checkbox-group{
    label {
      display: block;
      text-align: left;
      line-height: 40px;
      border-bottom: 1px solid #e4e7ed;
      margin-right: 0;
      span {
        font-size: 40px;
      }
    }
    //   }
  }
}
</style>
