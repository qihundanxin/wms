<template>
  <div class="bjbf-batch">
    <FeaturesHeader headerTitle="扫码测试" />
    <div>停顿时长用于PDA处理扫码得到的字符，如果过短，会导无法正确扫码，请尝试合理的扫码时长。</div>
    <div style="text-align:center;">
      <span>扫码后停顿：</span>
      <el-button @click="setStop(-100)">-</el-button>
      <span>{{ stop / 100 }}00毫秒</span><!-- 200 毫秒比较合适-->
      <el-button @click="setStop(+100)">+</el-button>
    </div>
    请扫码：
    {{ log }}
  </div>
</template>
<script>
import FeaturesHeader from "@/components/FeaturesHeader";
export default {
  name: "bjbfBatchScanTest",
  data() {
    return {
      input: '',
      log: [],
      stop: 400,
    };
  },
  components: {
    FeaturesHeader,
  },
  computed: {

  },
  watch: {
    input(newValue, oldValue) {
      setTimeout(() => {
        if(newValue && newValue == this.input){
          this.input = ''
          this.scan(newValue)
        }
      }, this.stop)
    }
  },
  methods: {
    onkeydown(key) {
      this.input = this.input + key
    },
    scan(str) {
      this.log = [...this.log, str]
    },
    setStop(adder){
      let stop = this.stop + adder
      if(stop > 0){
        this.stop = stop
      }
    }
  },
  created() {
    document.onkeydown = (e) => {
      console.log(e.key)
      this.onkeydown(e.key)
      // if (e.key && e.key.length == 1) {
      //   this.onkeydown(e.key)
      // }
    }
  },
  destroyed() {
    document.onkeydown = () => {}
  }
};
</script>
<style lang="less" scoped>
</style>
