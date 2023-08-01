<template>
  <div class="home">
    <el-dialog title="修改仓库" :visible.sync="dialogWareVisible" width="80%" style="margin-top: -10vh">
      <ul class="wareList">
        <li v-for="ware in wares" @click="changeWare(ware)">
          {{ ware.wareName }}
        </li>
      </ul>
    </el-dialog>

    <header class="clearFloat">
      <span tag="span" @click="isLogout" class="iconfont icon-chexiao tools leftFloat"></span>
      <span class="iconfont icon-cangkuguanli tools2 rightFloat" @click="showWareModal"></span>
      <h3 class="homeTitle">{{ wareName }}</h3>
    </header>

    <div class="content" v-for="menu in menus">
      <div v-if="permission(menu.code)">
        <p class="menuRowHeader">{{ menu.moduleName }}</p>
        <div class="menuRow clearFloat">
          <div v-for="moduleMenu in menu.moduleMenus">
            <div v-if="permission(moduleMenu.routeName)" class="menu clickColor"
              @click="toFeatures(moduleMenu.routeName)">
              <el-badge :value="moduleMenu.value" style="margin: auto">
                <span :class="moduleMenu.iconClass" :style="{ color: moduleMenu.iconColor }"></span>
              </el-badge>
              <span style="font-size:12px">{{ moduleMenu.title }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
// import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: "home",
  data() {
    return {
      wareId: PF.getLocal("wareId", "number"),
      wareName: PF.getLocal("wareName"),

      dialogWareVisible: false,

      wares: [],
      pda: PF.getLocal("menus4", "json"),

      menus: [
        {
          moduleName: "入库验收",
          code: 'in',
          moduleMenus: [
            {
              title: "UPC验收",
              iconClass: "iconfont icon-shouhuo",
              iconColor: "#67C23A",
              value: null,
              routeName: "UPCAccept",
            },
            {
              title: "异常验收",
              iconClass: "iconfont icon-qc",
              iconColor: "#67C23A",
              value: null,
              routeName: "ExAccept",
            },
            // {
            //     title: "基础验收",
            //     iconClass: "iconfont icon-shouhuo",
            //     iconColor: "#67C23A",
            //     value: null,
            //     routeName: "baseAccept",
            // },
            // {
            //     title: "混托盘收货",
            //     iconClass: "iconfont icon-tuopan",
            //     iconColor: "#E6A23C",
            //     value: null,
            //     routeName: "mixPlateAccept",
            // },
            {
              title: "盲收",
              iconClass: "iconfont icon-yanshou",
              iconColor: "#909399",
              value: null,
              routeName: "blindAccept",
            },
            // {
            //     title: "质量检验",
            //     iconClass: "iconfont icon-qc",
            //     iconColor: "#409EFF",
            //     value: null,
            //     routeName: "qc",
            // },

          ],
        },
        {
          moduleName: "上架",
          code: 'up',
          moduleMenus: [
            // {
            //   title: "上架车",
            //   iconClass: "iconfont icon-shangjia",
            //   iconColor: "#409EFF",
            //   value: null,
            //   routeName: "shangJiaChe1",
            // },
            // {
            //   title: "上架车上架",
            //   iconClass: "iconfont icon-shangjia",
            //   iconColor: "#409EFF",
            //   value: null,
            //   routeName: "shangJiaChe2",
            // },
            // {
            //   title: "直接上架",
            //   iconClass: "iconfont icon-shangjia",
            //   iconColor: "#409EFF",
            //   value: null,
            //   routeName: "Zjsj",
            // },
            {
              title: "UPC上架",
              iconClass: "iconfont icon-shangjia",
              iconColor: "#409EFF",
              value: null,
              routeName: "UPCsj",
            },
            // {
            //   title: "基础上架",
            //   iconClass: "iconfont icon-shangjia",
            //   iconColor: "#409EFF",
            //   value: null,
            //   routeName: "baseUpCell"
            // },
            // {
            //   title: "合并上架",
            //   iconClass: "iconfont icon-hebing",
            //   iconColor: "#E6A23C",
            //   value: null,
            //   routeName: "mergeUpCell"
            // },
          ],
        },

        {
          moduleName: "出库",
          code: 'out',
          moduleMenus: [
            // {
            //   title: "波次-标签打印",
            //   iconClass: "iconfont icon-chakan2",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "PickTaskPrint",
            // },
            // {
            //   title: "容器拣货",
            //   iconClass: "iconfont icon-chakan2",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "BoxPickTaskList",
            // },
            // {
            //   title: "还货上架",
            //   iconClass: "iconfont icon-chakan2",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "PickTaskReturn",
            // },
            // {
            //   title: "拣货异常",
            //   iconClass: "iconfont icon-chakan2",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "PickTaskEx",
            // },

            // {
            //   title: "波次播种",
            //   iconClass: "iconfont icon-seeding",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "waveSeeding"
            // },

            // {
            //   title: "任务领取",
            //   iconClass: "iconfont icon-tijiao",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "getWaveTask"
            // },

            // {
            //   title: "边拣边分",
            //   iconClass: "iconfont icon-yemianxianshi",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "bjbf"
            // },
            {
              title: "拣货单",
              iconClass: "iconfont icon-yemianxianshi",
              iconColor: "#08a4f7",
              value: null,
              routeName: "bjbfSo"
            },
            {
              title: "订单发运",
              iconClass: "iconfont icon-wuliu",
              iconColor: "#08a4f7",
              value: null,
              routeName: "fy"
            },
            {
              title: "波次拣货",
              iconClass: "iconfont icon-yemianxianshi",
              iconColor: "#08a4f7",
              value: null,
              routeName: "bjbfBatch"
            },
            {
              title: "扫码测试",
              iconClass: "iconfont icon-yemianxianshi",
              iconColor: "#08a4f7",
              value: null,
              routeName: "bjbfBatchScanTest"
            },


            // {
            //   title: "订单查询",
            //   iconClass: "iconfont icon-chakan2",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "searchSo"
            // },
            // {
            //   title: "拣货任务查询",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "selectFirstPickTask"
            // },
            // {
            //   title: "订单拣货",
            //   iconClass: "iconfont icon-pandian",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "orderPickTask"
            // },
            // {
            //   title: "标签拣货",
            //   iconClass: "iconfont icon-yemianxianshi",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "labelPickTask"
            // },
            // // {
            // //   title: "仓库直拣",
            // //   iconClass: "iconfont icon-kucunguanli",
            // //   iconColor: "#07f72a",
            // //   value: null,
            // //   routeName: "warePickTask"
            // // },
            // {
            //   title: "拣货确认-按订单",
            //   iconClass: "iconfont icon-tijiao",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "affirmPickTask"
            // },
            // //
            // /*{
            //   title: "波次合拣",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "wavePickTask"
            // },
            // {
            //   title: "快捷合拣",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "shortcutPickTask"
            // },
            // {
            //   title: "并发合拣",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "concurrencePickTask"
            // },

            // {
            //   title: "交叉拣货",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "warePickTask"
            // },
            // {
            //   title: "标准人工拣货",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "standardArtificialPickTask"
            // },
            // {
            //   title: "人工拣货",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "artificialPickTask"
            // },
            // {
            //   title: "人工反拣",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "artificialOppositePickTask"
            // },
            // {
            //   title: "注销拣选",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "logoutPickTask"
            // },
            // {
            //   title: "立库在线拣选",
            //   iconClass: "iconfont icon-chakan1",
            //   iconColor: "#07f72a",
            //   value: null,
            //   routeName: "stereoscopicPickTask"
            // },*/
            // {
            //   title: "按订单装箱",
            //   iconClass: "iconfont icon-soBoxing",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "boxingBySo"
            // },
            // {
            //   title: "按波次装箱",
            //   iconClass: "iconfont icon-waveBoxing",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "boxingByWave"
            // },
            // {
            //   title: "按订单复核",
            //   iconClass: "iconfont icon-tijiao",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "checkBySo"
            // },
            // {
            //   title: "装车",
            //   iconClass: "iconfont icon-wuliu",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "loading"
            // },
            // {
            //   title: "波次播种",
            //   iconClass: "iconfont icon-seeding",
            //   iconColor: "#08a4f7",
            //   value: null,
            //   routeName: "waveSeeding"
            // }
          ],
        },
        {
          moduleName: "库存管理",
          code: 'inventory',
          moduleMenus: [
            // {
            //     title: "合并托盘",
            //     iconClass: "iconfont icon-hebing",
            //     iconColor: "#E6A23C",
            //     value: null,
            //     routeName: "mergePlate",
            // },
            {
              title: "基础移库",
              iconClass: "iconfont icon-shangjia",
              iconColor: "#08a4f7",
              value: null,
              routeName: "baseMove",
            },
            {
              title: "汇总移库",
              iconClass: "iconfont icon-shangjia",
              iconColor: "#08a4f7",
              value: null,
              routeName: "baseSummaryMove",
            },
            // {
            //     title: "货位冻结",
            //     iconClass: "iconfont icon-quanxian1",
            //     iconColor: "#08a4f7",
            //     value: null,
            //     routeName: "freezeCell",
            // },
            // {
            //     title: "货位解冻",
            //     iconClass: "iconfont icon-quanxian",
            //     iconColor: "#08a4f7",
            //     value: null,
            //     routeName: "freezeCell2",
            // },
            // {
            //     title: "货位锁定",
            //     iconClass: "iconfont icon-user-jinyong",
            //     iconColor: "#08a4f7",
            //     value: null,
            //     routeName: "releaseCell",
            // },
            // {
            //     title: "货位解锁",
            //     iconClass: "iconfont icon-qiyong",
            //     iconColor: "#08a4f7",
            //     value: null,
            //     routeName: "releaseCell2",
            // },
            // {
            //     title: "任务号盘点",
            //     iconClass: "iconfont icon-kucunshiwu",
            //     iconColor: "#08a4f7",
            //     value: null,
            //     routeName: "taskNumberInventoryCheck",
            // },
            {
              title: "盘点（初盘）",
              iconClass: "iconfont icon-ziyuan",
              iconColor: "#08a4f7",
              value: null,
              routeName: "stockTakeTask",
            },
            {
              title: "盘点（复盘）",
              iconClass: "iconfont icon-ziyuan",
              iconColor: "#08a4f7",
              value: null,
              routeName: "stockTakeTask2",
            },
            {
              title: "信息查询",
              iconClass: "iconfont icon-chakan2",
              iconColor: "#08a4f7",
              value: null,
              routeName: "skuInfoSearch",
            },
            {
              title: "异常拣货处理",
              iconClass: "iconfont icon-qita",
              iconColor: "#08a4f7",
              value: null,
              routeName: "HandlePickBatchException",
            },
          ],
        },
      ],
    };
  },
  components: {
    // HelloWorld
  },
  methods: {
    permission(code) {
      // return true;
      for (var i = 0; i < this.pda.length; i++) {
        if (code == this.pda[i].code) {
          return true;
        }
      }
    },
    toFeatures(routeName) {
      this.$router.push({ name: routeName });
    },
    wareIsNull() {
      if (!PF.isNull(this.wareId)) {
        if (this.wares.length > 0) {
          PF.setLocal("wareId", this.wares[0].wareId);
          PF.setLocal("wareName", this.wares[0].wareName);
          this.$router.go(0);
        } else {
          IOT.tips("未获取到仓库信息！", "error");
        }
      } else {
        // this.getWareName();
      }
    },
    getWareData() {
      IOT.getServerData("/ware/infos/findSome", "get", {}, (ret) => {
        if (ret.code === 200) {
          this.wares = ret.rows;
          this.wareIsNull();
        } else {
          IOT.tips(
            ret.message || "服务器请求失败，稍后再试！",
            "error"
          );
        }
      });
    },
    changeWare(ware) {
      IOT.showOverlay("保存中，请稍等...");
      IOT.tips("仓库设置成功！", "success", 1000, () => {
        PF.setLocal("wareId", ware.wareId);
        PF.setLocal("wareName", ware.wareName);
        this.dialogWareVisible = false;
        IOT.hideOverlay();
        this.$router.go(0);
      });
    },
    showWareModal() {
      this.dialogWareVisible = true;
    },
    isLogout() {
      this.$confirm('是否确认退出登录？')
              .then(() => {
                this.logout()
              })
              .catch(() => {
              });
    },
    logout(){
      localStorage.removeItem("pda-token");
      localStorage.removeItem("userName");
      localStorage.removeItem("menus4");
      this.$router.push({ name: 'login' })
    }
  },
  created() {

    this.getWareData();
    IOT.initLodop();
  },
};
</script>
<style lang="less" scoped>
.home {
  .wareList {
    li {
      width: 80%;
      margin: 0 auto;
      font-size: 18px;
      line-height: 35px;
      text-align: center;
      border-bottom: 1px solid #dcdfe6;
    }

    li:active {
      background: #ecf5ff;
    }
  }
}

header {
  background-color: #409eff;
  line-height: 70px;
  position: relative;

  .homeTitle {
    color: #fff;
    font-weight: normal;
    text-align: center;
  }

  .tools {
    font-size: 28px;
    line-height: 70px;
    position: absolute;
    left: 20px;
    /*transform: rotate(-90deg);*/
    color: #fff;
  }

  .tools2 {
    font-size: 28px;
    line-height: 70px;
    position: absolute;
    right: 20px;
    color: #fff;
  }
}

.content {
  .menuRowHeader {
    background-color: #ecf0f1;
    line-height: 35px;
    padding-left: 20px;
    text-align: left;
    font-weight: 600;
  }

  .menuRow {
    // display: flex;
    // justify-content: space-around;
    margin-top: 8px;
    margin-bottom: 12px;

    .menu {
      display: flex;
      // flex: 1;
      text-align: center;
      flex-direction: column;
      float: left;
      width: 30%;

      span:first-child,
      img {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: auto;
        width: 40px;
        height: 40px;
        border-radius: 12px;
        // background-color: #f54514;
        font-size: 30px;
        color: #000;
        // margin-bottom: 4px;
      }

      span:last-child {
        white-space: nowrap;
      }
    }

    /* .menu:active {
  background: #dcdfe6;
} */
  }
}
</style>

