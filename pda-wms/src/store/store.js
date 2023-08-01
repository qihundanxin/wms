import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const store = new Vuex.Store({   // 注意Store的S大写
  state: { // data
    productList: [
      {name: 'goods 1',price: 100},
      {name: 'goods 2',price: 200},
      {name: 'goods 3',price: 300},
      {name: 'goods 3',price: 400}
    ],
    // 按照web对应修改了cellUseTypes
    cellUseTypes: [ 
      {
        value: -1,
        label: '垃圾货位'
      },
      {
        value: 0,
        label: '验收位'
      },
      {
        value: 1,
        label: '拣货位'
      },
      {
        value: 2,
        label: '仓储位'
      },
      {
        value: 3,
        label: '集货位'
      },
      {
        value: 4,
        label: '上架车'
      }
    ],
    cellAbcLevels: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '快速周转'
      },
      {
        value: 2,
        label: '正常周转'
      },
      {
        value: 3,
        label: '慢速周转'
      },
    ],
    cellPickTypes: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '整箱拣货'
      },
      {
        value: 2,
        label: '拆零拣货'
      },
      {
        value: 3,
        label: '混合拣货'
      },
    ],
    upperTacticTypes: [
      {
        value: 1,
        label: '01'
      },
      {
        value: 2,
        label: '02'
      },
      {
        value: 3,
        label: '03'
      },
    ],
    orderTypes: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '上架'
      },
      {
        value: 2,
        label: '下架'
      },
    ],
    packLevels: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '主单位'
      },
      {
        value: 2,
        label: '内包装'
      },
      {
        value: 3,
        label: '箱'
      },
      {
        value: 4,
        label: '栈板'
      },
      {
        value: 5,
        label: '其他'
      },
    ],
    abcLevels: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: 'A'
      },
      {
        value: 2,
        label: 'B'
      },
      {
        value: 3,
        label: 'C'
      },
    ],
    cellTypes: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '货架'
      },
      {
        value: 2,
        label: '地堆'
      },
    ],
    cellStates: [
      {
        value: 0,
        label: '不限制'
      },
      {
        value: 1,
        label: '无货'
      },
      {
        value: 2,
        label: '有货'
      },
      {
        value: 3,
        label: '冻结'
      },
    ],
    reviewStates: [
      {label: '待审核', value: 2, type: 'info'},
      {label: '审核通过', value: 1, type: 'success'},
      {label: '审核中', value: 0, type: ''},
      {label: '审核退回', value: -1, type: 'danger'},
    ],
    asnStates: [
      {label: '初始化', value: 0, type: 'info'},
      {label: '已码盘', value: 1, type: 'warning'},
      {label: '部分收货', value: 2, type: ''},
      {label: '全部收货', value: 3, type: 'success'},
    ],
    qcStates: [
      {label: '待检', value: 0},
      {label: '合格', value: 1},
      {label: '不合格', value: 2},
    ],
    sysParamTypes: [
      {
        dictKey: 0,
        dictValue: "开关"
      },
      {
        dictKey: 1,
        dictValue: "选择"
      },
      {
        dictKey: 2,
        dictValue: "数字"
      },
      {
        dictKey: 3,
        dictValue: "输入框"
      },
    ],
    pickTaskType: [
      {label: '待拣货', value: 0},
      {label: '部分拣货', value: 1},
      {label: '拣货完成', value: 2},
      {label: '取消拣货', value: 3},
    ],
    pickTacticType: [
      {label: '向上拆解', value: 1},
      {label: '向下分解', value: 2},
      {label: '是否拆包', value: 3},
      {label: '清仓优先', value: 4},
      {label: '整箱基于最优解的分配优化算法', value: 5},
      {label: '拣货位超量分配', value: 6},
      {label: '自动产生补货任务', value: 7},
      {label: '动态拣货位', value: 8},
      {label: '存储为拣选（EA）', value: 9},
      {label: '存储为拣选（CS）', value: 10},
    ],
    whether: [
      {label: '是', value: 1},
      {label: '否', value: 0},
    ],
      createSource: [
          {label: '手动创建', value: 1},
          {label: '自动创建', value: 2},
      ],
      stockTaskDetailType: [
          {label: '初盘(明盘)', value: 11},
          {label: '初盘(暗盘)', value: 12},
          {label: '复盘(明盘)', value: 21},
          {label: '复盘(暗盘)', value: 22},
      ],
  },
  getters:{ // computed
    xDataDictSysSetting(){
      IOT.getServerData('/data/dict/masters/list','get',{dataDictCode: 'sysSetting'},(ret) => {
        if (ret.code === 200) {
          return ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    }
  }
});
