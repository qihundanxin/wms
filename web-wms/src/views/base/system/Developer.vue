<template>
  <div class="sysSetting">

    <el-drawer class="sysMenuBox" title="开发者页面" :visible.sync="drawer" direction="ltr" :with-header="false"
      style="width: 1000px">
      <div class="headTitle">开发者页面</div>
    </el-drawer>

    <div class="headerBox">
      <i class="leftFloat iconfont icon-daohang" @click="drawer = true" style="margin: 0 20px;cursor: pointer;"></i>
      开发者页面
    </div>

    <div class="settingBox">
      <div class="card">
        <div class="row">
          <el-row :gutter="10" style="margin-left:7px">
            <el-col :span="7">
              <div class="grid-content bg-purple">同步 SKU</div>
            </el-col>
            <el-col :span="7">
              <div class="grid-content bg-purple-light">
                <el-input v-model="shopId" placeholder="请输入同步shopId"  size="small"></el-input>
              </div>
            </el-col>
            <el-col :span="7">
              <div class="grid-content bg-purple">
                <el-select v-model="orgCode" placeholder="请选择目标货主"  size="small">
                  <el-option v-for="item in organizations" :key="item.organizationCode" :label="item.organizationName"
                    :value="item.organizationCode">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="grid-content bg-purple-light">
                <el-button size="small"   v-loading="tongbuLoading" type="primary" round  @click="tongBu()">同步
                </el-button>
              </div>
            </el-col>
          </el-row>
          <div class="row">
            <p class="letRow" @click="rout('/base/system/timeTask')">定时任务</p>
          </div>
          <div class="row">
            <p class="letRow"  @click="rout('/base/system/resource')">页面配置</p>
          </div>
          <div class="row">
            <p class="letRow"  @click="rout('/base/system/dataDict')">数据字典</p>
          </div>
          <div class="row">
            <p class="letRow"  @click="rout('/base/system/webhook/setting')">WebHook配置</p>
          </div>
          <div class="row">
            <p class="letRow"  @click="rout('/base/system/webhook/log')">WebHook日志</p>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Developer',
  data() {
    return {
      drawer: false,
      organizations: [],
      orgCode: '',
      shopId: '',
      tongbuLoading:false,
    }
  },
  methods: {
    rout(path){
      this.$router.push(path)
    },
    getOrganizationData() {
      IOT.getServerData('/organizations/list', 'get', this.searchData, (ret) => {
        if (ret.code === 200) {
          this.organizations = ret.rows;
          console.log(this.organizations);
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    },
    //同步SKU
    tongBu(){
        this.tongbuLoading=true;
        let form = {
          orgCode:this.orgCode,
          shopId:this.shopId.replace(/\s*/g,"")
        }
          IOT.getServerData('/item/infos/xhcSku2', 'post', form, (ret) => {
            this.tongbuLoading=false;
              if (ret.code === 200) {
                IOT.tips('运行成功！', 'success');
                this.getItemData();
              } else {
                IOT.tips(ret.data +" " +ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
      },

  },
  created() {
  },
  mounted() {
    this.getOrganizationData();
  }
}
</script>

<style scoped lang="less">
.sysSetting {
  padding-bottom: 20px;

  .sysMenuBox {
    .headTitle {
      height: 60px;
      line-height: 60px;
      border-bottom: 1px solid #DCDFE6;
      padding-left: 20px;
      font-size: 18px;
      font-weight: bold;
    }

    a {
      width: 300px;
      height: 50px;
      line-height: 50px;
      display: block;
      color: #5f6368;
      cursor: pointer;
      padding-left: 20px;
    }

    a:hover {
      background: #f4f4f5;
    }

    a:active {
      color: #0c020f3f;
    }

    a.active {
      color: #0c020f3f;
    }
  }

  .headerBox {
    /*background: #409EFF;*/
    color: #ffffff;
    height: 50px;
    line-height: 50px;
    background-image: linear-gradient(#97999c, #c9cacc);
    font-size: 18px;
  }

  .settingBox {
    min-width: 600px;
    max-width: 50%;
    margin: 0 auto;

    .header {
      height: 35px;
      padding-top: 30px;
    }

    .card {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      border: 1px solid #ddd;
      background: #fff;
      height: 800px;

      .row {
        height: 48px;
        line-height: 48px;
        cursor: pointer;
        padding: 0 5px 0 10px;
        border-bottom: 1px solid #f0f0f0;

        .right {
          float: right;
          height: 48px;
        }

        .right.input {
          width: 200px;
        }

        .right.number {
          height: auto;
          margin-top: 10px;
        }
        .letRow{
          color:#409EFF;
        }
      }
    }
  }
}
</style>
