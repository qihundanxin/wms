<template>
  <div class="sysSetting">

    <el-drawer
      class="sysMenuBox"
      title="系统设置"
      :visible.sync="drawer"
      direction="ltr"
      :with-header="false"
      style="width: 1000px">
      <div class="headTitle">系统设置</div>
      <a :href="'#'+item1.dictKey" v-for="(item1,index) in arr" :key="index" class="menuA" @click="clickMenu(index)">
        <i v-if="item1.iconClass === ''" class="iconfont icon-qita2" style="margin-right: 20px;"></i>
        <i v-else :class="item1.iconClass" style="margin-right: 20px;"></i>
        <span>{{item1.dictValue}}</span>
      </a>
    </el-drawer>

    <div class="headerBox">
      <i class="leftFloat iconfont icon-daohang" @click="drawer = true" style="margin: 0 20px;cursor: pointer;"></i>
      系统设置
    </div>

    <div class="settingBox" v-for="(item1, index1) in arr" :key="index1">
      <div class="header" :id="item1.dictKey">
        <span>{{item1.dictValue}}</span>
      </div>
      <div class="card">
        <div class="row" v-for="(item2, index2) in item1.details" :key="index2">
          <span>{{item2.paramName}}</span>
          <el-switch
            @change="updateRowData(item2)"
            v-if="item2.type === 0"
            class="right"
            v-model="item2.value"
            active-value="1"
            inactive-value="0">
          </el-switch>
          <el-select
            @change="updateRowData(item2)"
            v-if="item2.type === 1"
            class="right input"
            size="mini"
            v-model="item2.value"
            filterable
            placeholder="请选择">
            <el-option
              v-for="item3 in item2.details"
              :key="item3.detailValue"
              :label="item3.detailName"
              :value="item3.detailValue">
            </el-option>
          </el-select>
          <el-input-number
            @change="updateRowData(item2)"
            v-if="item2.type === 2"
            class="right number"
            size="mini"
            v-model="item2.value"
            label="描述文字">
          </el-input-number>
          <el-input
            @change="updateRowData(item2)"
            v-if="item2.type === 3"
            class="right input"
            size="mini"
            placeholder="请输入内容"
            v-model="item2.value"
            clearable>
          </el-input>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'SysSetting',
    data(){
      return {
        drawer: false,
        sysParams: [],
        sysSettings: [],
        arr: [],
        // obj: {},
        class: {
          user: {
            title: '用户设置',
            arr: ['user']
          },
          default: {
            title: '默认设置',
            arr: ['cellValue', 'tactic']
          },
          bill: {
            title: '单据设置',
            arr: ['buyBillIn', 'asnBill', 'qcBill', 'soBill']
          },
        },
      }
    },
    methods: {

      clickMenu(index){
        let arr = document.querySelectorAll('.menuA');
        for(let i=0;i<arr.length;i++){
          arr[i].classList.remove("active");
        }
        arr[index].classList.add("active");
        this.drawer = false;
      },
      getSysParamMasterDetailData(){
        IOT.getServerData('/sys/params/detailList','get',{},(ret) => {
          if (ret.code === 200) {
            this.sysParams = ret.rows;
            let sysSettings = this.sysSettings;
            let sysParams = this.sysParams;
            let arr = [];
            // let obj = {};
            for(let i in sysSettings){
              arr.push(sysSettings[i]);
              arr[i].details = [];
              // arr.push(sysSettings[i].dictKey);
              for(let j in sysParams){
                if(sysSettings[i].dictKey === sysParams[j].moduleCode){
                  arr[i].details.push(sysParams[j]);
                  // obj[sysSettings[i].dictKey] = sysParams[j];
                }
              }
            }
            this.arr = arr;
            // this.obj = obj;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      updateRowData(row){
        let rowData = JSON.parse(JSON.stringify(row));
        IOT.getServerData('/sys/params/update','post',rowData,(ret) => {
          if (ret.code === 200) {
            IOT.tips('保存成功！', 'success', 500);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            PF.getDataDictUdfData('sysSetting', (rows) => {
                this.sysSettings = rows;
                this.getSysParamMasterDetailData();
            });
          }
        });
      }
    },
    created() {
      PF.getDataDictUdfData('sysSetting', (rows) => {
          this.sysSettings = rows;
          this.getSysParamMasterDetailData();
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .sysSetting{
    padding-bottom: 20px;
    .sysMenuBox{
      .headTitle{
        height: 60px;
        line-height: 60px;
        border-bottom: 1px solid #DCDFE6;
        padding-left: 20px;
        font-size: 18px;
        font-weight: bold;
      }
      a{
        width: 300px;
        height: 50px;
        line-height: 50px;
        display: block;
        color: #5f6368;
        cursor: pointer;
        padding-left: 20px;
      }
      a:hover{
        background: #f4f4f5;
      }
      a:active{
        color: #0075ff;
      }
      a.active{
        color: #0075ff;
      }
    }
    .headerBox{
      /*background: #409EFF;*/
      color: #ffffff;
      height: 50px;
      line-height: 50px;
      background-image: linear-gradient(#3367d6, #409EFF);
      font-size: 18px;
    }
    .settingBox{
      min-width: 600px;
      max-width: 50%;
      margin: 0 auto;
      .header{
        height: 35px;
        padding-top: 30px;
      }
      .card{
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
        border-radius: 5px;
        border: 1px solid #ddd;
        background: #fff;
        .row{
          height: 48px;
          line-height: 48px;
          cursor: pointer;
          padding: 0 20px;
          border-bottom: 1px solid #f0f0f0;
          .right{
            float: right;
            height: 48px;
          }
          .right.input{
            width: 200px;
          }
          .right.number{
            height: auto;
            margin-top: 10px;
          }
        }
        .row:last-child{
          border-bottom: 0;
        }
      }
    }
  }
</style>
