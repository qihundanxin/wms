import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const store = new Vuex.Store({   // 注意Store的S大写
  state: { // data
    productList: [
      {name: 'goods 1', price: 100},
      {name: 'goods 2', price: 200},
      {name: 'goods 3', price: 300},
      {name: 'goods 3', price: 400}
    ],
    shelfTypes: [
      {
        value: 1,
        label: '地堆'
      },
      {
        value: 2,
        label: '货架'
      },
      {
        value: 4,
        label: '上架车'
      },
    ],
    cellUseTypes: [ //  
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
        label: '快速'
      },
      {
        value: 2,
        label: '正常'
      },
      {
        value: 3,
        label: '慢速'
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
        label: '原始货位->目标货位'
      },
      {
        value: 2,
        label: '原始货位->目标库区'
      },
      {
        value: 3,
        label: '指定库区'
      },
      {
        value: 4,
        label: '目标库位'
      },
      {
        value: 30,
        label: '相同产品相邻储位'
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
      {label: '部分验收', value: 2, type: ''},
      {label: '全部验收', value: 3, type: 'success'},
      {label: '入库单完成', value: 4, type: 'success'},
      {label: '超额验收', value: 5, type: 'danger'},
      {label: '采购完成', value: 6, type: 'forcecompletion'},
    ],
    qcStates: [
      {label: '未检', value: -1, type: 'info'},
      {label: '待检', value: 0, type: ''},
      {label: '合格', value: 1, type: 'success'},
      {label: '不合格', value: 2, type: 'danger'},
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
      {label: '已取消', value: -1},
      {label: '待拣货', value: 0},
      {label: '部分拣货', value: 1},
      {label: '拣货完成', value: 2},
      {label: '取消拣货', value: 3},
      {label: '被合并', value: 4},
      {label: '已发运', value: 5},
      
    ],
    saleState: [
      {label: '未完成', value: 0}
      
    ],
    cpfrTaskTypes: [
      {label: '已取消', value: -1},
      {label: '待补货', value: 0},
      {label: '已补货', value: 1},
      
      
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
    stockTaskMasterType: [
      {label: '明盘', value: 1},
      {label: '暗盘', value: 2}, 
    ],
    stockTaskMasterState: [
      {label: '初始化', value: 0},
      {label: '暂停', value: -1},
      {label: '生成任务', value: 2},
      {label: '盘点中', value: 3},
      {label: '盘点完成(无差异)', value: 4},
      {label: '盘点完成(需调整)', value: 5},
      {label: '已生成调整任务', value: 6},
    ],
    stockTaskMasterOrder: [
      {label: '普通', value: 1},
      {label: '优先', value: 2},
      {label: '紧急', value: 3},
    ],
    stockTaskDetailType: [
      {label: ' (明盘)', value: 1},
      {label: ' (暗盘)', value: 2}, 
    ],
    stockTaskDetailState: [
      {label: '未盘', value: 1,color:'#909399'},
      
      {label: '复盘', value: 2,color:'#E6A23C'},
      {label: '已盘', value: 3,color:'#67C23A'},
    ],
    transactTypes: [
      {label: '验收', value: 0},
      {label: '快速验收', value: 1},
      {label: '上架', value: 2},
      {label: '盲收', value: 3},
      {label: '-', value: 4},
      {label: '移库', value: 5},
      {label: '采购入库', value: 6},
      {label: '扫描验收', value: 7},
      {label: '审核', value: 8},
      {label: '-', value: 9},
      {label: '库存调整', value: 10},
      {label: '货权转移', value: 11},
      {label: '库存冻结', value: 12},
      {label: '基础移库', value: 23},
      {label: '分配数迁移', value: 24},
    ],
    billTypes: [
      {label: '默认验收', value: 0},
      {label: '采购入库', value: 1},
      {label: '入库单', value: 2},
      {label: '盲收', value: 3},
      {label: '冻结', value: 4},
      {label: '扫描验收', value: 5},
    ],
    serialNoTypes: [
      {label: '验收', value: 1},
      {label: '移库', value: 2},
      {label: '发运', value: 3},
    ],
    reviewBillTypes: [
      {label: '采购', value: 1},
      {label: '入库单', value: 2},
      {label: '发运', value: 3},
      {label: '冻结', value: 4},
      {label: '盘点', value: 5},
      {label: '调整', value: 6},
      {label: '调拨', value: 7},
      {label: '销售', value: 8},
      {label: '退货', value: 9},
      {label: '货权转移', value: 10}
    ],
    inventoryAdjustType:[
      {label: '盘点单生成', value: 1},
      {label: '手动选择库存生成', value: 2},
      {label: '自动选择库存生成', value: 3},
    ],
    inventoryAdjustState:[
      {label: '库存未调整', value: 1},
      {label: '库存已调整', value: 2},
    ],
    recordTypes: [
      {label: '新增', value: [1,7,15,22,29,36,38,43,47]},
      {label: '草稿', value: [2,8,16,23,30,39,44,48,43]},
      {label: '审核', value: [3,9,17,24,31,37,40,45,49]},
      {label: '验收', value: [4]},
      {label: '修改', value: [5,14,20,27,33,50]},
      {label: '归档', value: [6,11,19,26,32]},
      {label: '入库单', value: [10,41]},
      {label: 'SO', value: [42,46]},
      {label: '合并', value: [28]},
      {label: '释放', value: [34,35]},
      {label: '质检', value: [21,12]},
      {label: '取消', value: [-1]},
      {label: '拣货异常退回订单', value: [-11]},
      {label: '创建invoice信息', value: [60]},
      {label: '删除invoice信息', value: [61]},
      {label: '更新invoice信息', value: [62]},
    ],
    sort: [
      {label: '升序', value: 1},
      {label: '降序', value: 2},
    ],
    soStates: [
      {label: '初始化', value: 0},
      {label: '部分预分配', value: 1},
      {label: '预分配完成', value: 2},
      {label: '部分分配', value: 3},
      {label: '分配完成', value: 4},
      {label: '部分拣货', value: 5},
      {label: '拣货完成', value: 6},
      {label: '部分发运', value: 7},
      {label: '完全发运', value: 8},
      {label: '取消', value: 9},
      {label: '完成', value: 10},
      {label: '已合并', value: 11},
    ],
    freezeDetailState :[
      {label: '正常', value: 0},
      {label: '已冻结', value: 1},
      {label: '已释放', value: 2},
    ],
    acceptAuditState :[
      {label: '审批不通过', value: -1},
      {label: '待审批', value: 0},
      {label: '审批通过', value: 1},
    ]
  },
  getters: { // computed
    xDataDictSysSetting() {
      IOT.getServerData('/data/dict/masters/list', 'get', {dataDictCode: 'sysSetting'}, (ret) => {
        if (ret.code === 200) {
          return ret.rows;
        } else {
          IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
        }
      });
    }
  }
});
