<template>
  <div class="serverCache">
    <div class="headerBtns">
      <el-button size="small" type="danger" round icon="iconfont icon-shanchu" @click="clearCache()">清理缓存</el-button>
    </div>

    <div class="tableBox">
      <el-table highlight-current-row v-loading="loading" :data="cacheMessages" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column prop="selection" type="selection" width="55" />
        <el-table-column show-overflow-tooltip prop="cacheKey" label="缓存Key" min-width="50" />
        <el-table-column show-overflow-tooltip prop="cacheName" label="缓存名称" min-width="100" />
        <el-table-column show-overflow-tooltip prop="count" label="已缓存数据个数" min-width="100" />
        <el-table-column show-overflow-tooltip label="特殊说明" min-width="200">
          <template slot-scope="scope">
            <span>{{cacheNote(scope.row.cacheKey)}}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'TimeTask',
    data() {
      return {
        loading: false,
        cacheMessages: [],
        cacheMessagesSelection: []
      }
    },
    methods: {
      handleSelectionChange(val){
        this.cacheMessagesSelection = val;
      },
      cacheNote(key){
        if(key === 'ck5:'){
          return '清除会导致全部WMS、PDA的登录失效！';
        }
        return '';
      },
      getCache(){
        IOT.getServerData('/sys/cache/keys','get',null,(ret) => {
          if (ret.code === 200) {
            if(ret.data.cacheMessageList && ret.data.cacheMessageList.length > 0){
              this.cacheMessages = ret.data.cacheMessageList;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      },
      clearCache(){
        let keys = [];
        for(let i in this.cacheMessagesSelection){
          keys = [...keys, this.cacheMessagesSelection[i].cacheKey];
        }
        if(keys.length <= 0){
          IOT.tips('请勾选要清理的缓存', 'warning');
          return;
        }
        IOT.getServerData('/sys/cache/clear','post',{'keys': keys},(ret) => {
          if (ret.code === 200) {
            IOT.tips('缓存清理成功', 'success');
            this.getCache();
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      }
    },
    created() {
      this.getCache();
    },
    mounted() {
    }
  }
</script>

<style lang="less">

</style>
