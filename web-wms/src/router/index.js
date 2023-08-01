import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home'
import Homepage from '../views/Homepage'
import NotFind from '../views/NotFind'

import PrintPickBatch from '../views/print/PrintPickBatch'
import PrintPickSuit from '../views/print/PrintPickSuit'
import PrintItemLabel from '../views/print/PrintItemLabel'

import About from '../views/About'

// demo
import ImportExcel from '../components/ImportExcel'

import SelectProduct from '../views/base/itemBase/SelectProduct'

// const Base = () => import('../views/Base')

// 测试登录页
import TestLogin from '../views/TestLogin.vue'
// work
import CellList from '../views/work/CellList'
// work-orderManage
// saleBill
import SaleBill from '../views/work/orderManage/saleBill/SaleBill'
import CreateSaleBill from '../views/work/orderManage/saleBill/CreateSaleBill'
import SaleBillDetail from '../views/work/orderManage/saleBill/SaleBillDetail'
import SaleBillDraft from '../views/work/orderManage/saleBill/SaleBillDraft'

// work-billInManage
// buyBillIn
import BuyBillIn from '../views/work/billInManage/buyBillIn/BuyBillIn'
import CreateBuyBillIn from '../views/work/billInManage/buyBillIn/CreateBuyBillIn'
import BuyBillInDetail from '../views/work/billInManage/buyBillIn/BuyBillInDetail'
import BuyBillInDraft from '../views/work/billInManage/buyBillIn/BuyBillInDraft'
import BuyBillInFile from '../views/work/billInManage/buyBillIn/BuyBillInFile'

///////ber
import AsnReturnBill from '../views/work/billInManage/asnReturnBill/AsnReturnBill'
import AsnReturnBillDraft from '../views/work/billInManage/asnReturnBill/AsnReturnBillDraft'
import CreateAsnReturnBill from '../views/work/billInManage/asnReturnBill/CreateAsnReturnBill'
import AsnReturnBillDetail from '../views/work/billInManage/asnReturnBill/AsnReturnBillDetail'
import AsnReturnBillFile from '../views/work/billInManage/asnReturnBill/AsnReturnBillFile'
///////import AsnReturnBillDetailsAccept from '../views/work/billInManage/AsnReturnBillDetailsAccept'

// asnBill
import AsnBill from '../views/work/billInManage/asnBill/AsnBill'
import AsnBillDraft from '../views/work/billInManage/asnBill/AsnBillDraft'
import CreateAsnBill from '../views/work/billInManage/asnBill/CreateAsnBill'
import AsnBillDetail from '../views/work/billInManage/asnBill/AsnBillDetail'
import AsnBillFile from '../views/work/billInManage/asnBill/AsnBillFile'
import AsnBillDetailsAccept from '../views/work/billInManage/AsnBillDetailsAccept'
import ExAccept from '../views/work/billInManage/ExAccept'
import inLog from '../views/work/billInManage/log/log'
import outLog from '../views/work/billOutManage/log/log'
import AsnDetails from '../views/work/billInManage/asnBill/AsnDetails'
// qc
import QcMaster from '../views/work/billInManage/qc/QcMaster'
import CreateQc from '../views/work/billInManage/qc/CreateQc'
import QcBillDetail from '../views/work/billInManage/qc/QcBillDetail'
import QcBillDraft from '../views/work/billInManage/qc/QcBillDraft'
import QcBillFile from '../views/work/billInManage/qc/QcBillFile'
// qcRecord
import QcRecord from '../views/work/billInManage/qc/QcRecord'
import QcRecordFile from '../views/work/billInManage/qc/QcRecordFile'

import AcceptRecord from '../views/work/billInManage/AcceptRecord'
import AcceptAuditRecord from '../views/work/billInManage/AcceptAuditRecord'
import ToCellInventory from '../views/work/billInManage/ToCellInventory'
import FastAccept from '../views/work/billInManage/FastAccept'

// inCellTask
import InCellTask from "../views/work/billInManage/InCellTask";
import UpperTask from "../views/work/billInManage/UpperTask";



// work-billOutManage
// soBill
import SoBill from '../views/work/billOutManage/soBill/SoBill'
import CreateSoBill from '../views/work/billOutManage/soBill/CreateSoBill'
import SoBillDetail from '../views/work/billOutManage/soBill/SoBillDetail'
import SoBillDraft from '../views/work/billOutManage/soBill/SoBillDraft'
import SoBillFile from '../views/work/billOutManage/soBill/SoBillFile'
import SoDetails from '../views/work/billOutManage/soBill/SoDetails'
//concentrateCellInventory
import ConcentrateCellInventory from '../views/work/billOutManage/concentrateCellInventory/ConcentrateCellInventory'
//tactic
import WaveMaster from '../views/work/Wave/WaveMaster'
import ChooseSo from '../views/work/Wave/ChooseSo'
import WaveTactic from '../views/base/tactic/WaveTactic'
import WaveTacticCreate from '../views/base/tactic/WaveTacticCreate'
import RouteTactic from "../views/base/tactic/RouteTactic";
//printConfig
import PrintConfig from "../views/base/user/PrintConfig";

//inventory-serialNo
import InventorySerialNo from "../views/work/inventory/serialNo/InventorySerialNo";
import SerialNoSelect from "../views/work/inventory/serialNo/SerialNoSelect";

//pickTask
import PickTask from '../views/work/billOutManage/pickTask/PickTask'
import PickTaskMove from '../views/work/billOutManage/pickTask/PickTaskMove'
import PickTaskReset from '../views/work/billOutManage/pickTask/PickTaskReset'
import PickTaskEx from '../views/work/billOutManage/pickTask/PickTaskEx'
import PickTaskReturn from '../views/work/billOutManage/pickTask/PickTaskReturn'
import PickTactic from '../views/base/tactic/PickTactic'
import TurnoverTactic from '../views/base/tactic/TurnoverTactic'
import CpfrTask from '../views/work/billOutManage/pickTask/CpfrTask'

//return
import ReturnBill from '../views/work/billOutManage/return/ReturnBill'
import CreateReturnBill from '../views/work/billOutManage/return/CreateReturnBill'
import ReturnBillDetail from '../views/work/billOutManage/return/ReturnBillDetail'
import ReturnBillDraft from '../views/work/billOutManage/return/ReturnBillDraft'
import ReturnBillFile from '../views/work/billOutManage/return/ReturnBillFile'

//
import LoadMaster from "../views/work/Load/LoadMaster";
import ChoosePickTask from "../views/work/Load/ChoosePickTask"

// work-inventory

import TotalInventory from '../views/work/inventory/TotalInventory'
import InventoryBalance from '../views/work/inventory/InventoryBalance'
import InventoryTransact from '../views/work/inventory/InventoryTransact'
import Freeze from "../views/work/inventory/Freeze";
import FreezeDetail from "../views/work/inventory/FreezeDetail";
import FreezeDraft from "../views/work/inventory/FreezeDraft";
import InventoryAdjust from "../views/work/inventory/adjust/InventoryAdjust"
import InventoryAdjustDetail from "../views/work/inventory/adjust/InventoryAdjustDetail"
import InventoryAdjustDraft from "../views/work/inventory/adjust/InventoryAdjustDraft"
import IvTransOrgMaster from "../views/work/inventory/IvTransOrgMaster";
import IvTransOrgDetail from "../views/work/inventory/IvTransOrgDetail";
import IvTransOrgCreate from "../views/work/inventory/IvTransOrgCreate";
import ChangeCapacity from "../views/work/inventory/ChangeCapacity";
import ErpRecord from "../views/work/inventory/ErpRecord";
import CanOut from "../views/work/inventory/CanOut";
import FreezeDetails from "../views/work/inventory/FreezeDetails";

//盘点任务
import StockTakeMaster from '../views/work/inventory/stockTakeManage/master'
import StockTakeTask from "../views/work/inventory/stockTakeManage/task";
import StockTakeDetail from "../views/work/inventory/stockTakeManage/detail";
import CreateStockMasterTask from "../views/work/inventory/stockTakeManage/CreateStockMasterTask";
import UpdateStockMasterTask from "../views/work/inventory/stockTakeManage/UpdateStockMasterTask";
import StockMasterTaskDetail from "../views/work/inventory/stockTakeManage/StockMasterTaskDetail";

//调拨单
import AllotBill from '../views/work/allot/AllotBill'
import CreateAllotBill from '../views/work/allot/CreateAllotBill'
import AllotBillDetail from '../views/work/allot/AllotBillDetail'
import AllotBillDraft from '../views/work/allot/AllotBillDraft'
import AllotBillFile from '../views/work/allot/AllotBillFile'


// work-workbench
import Acceptance from '../views/work/workbench/Acceptance'
import WorkbenchMenu from "../views/work/workbench/WorkbenchMenu";
import Check from "../views/work/workbench/Check";
import Seeding from "../views/work/workbench/Seeding";
import Ydyp from "../views/work/workbench/Ydyp";
import ShipCode from "../views/work/workbench/ShipCode";
import Dpdb from "../views/work/workbench/Dpdb";
import OutPrint from "../views/work/workbench/OutPrint";

// base-warehouse
import Ware from '../views/base/warehouse/Ware'
import Platform from '../views/base/warehouse/Platform'
import Area from '../views/base/warehouse/Area'
import Shelf from '../views/base/warehouse/Shelf'
import Cell from '../views/base/warehouse/Cell'
import SeedingWall from "../views/base/warehouse/SeedingWall";
import Box from "../views/base/warehouse/Box";
import Printer from "../views/base/warehouse/Printer";
import WareShop from "../views/base/warehouse/WareShop";

// base-itemBase
import Item from '../views/base/itemBase/Item'
import ItemType from '../views/base/itemBase/ItemType'
import Pack from '../views/base/itemBase/Pack'
import PackData from '../views/base/itemBase/PackData'
import ItemCombined from "../views/base/itemBase/ItemCombined";
import ItemCombinedCreate from "../views/base/itemBase/ItemCombinedCreate";
import InventorySafety from "../views/base/itemBase/InventorySafety";
import Test from '../views/base/itemBase/Test'

// base-organizationManage
import Supplier from '../views/base/organizationManage/Supplier'
import Organization from '../views/base/organizationManage/Organization'
import Customer from '../views/base/organizationManage/Customer'
import Carrier from '../views/base/organizationManage/Carrier'

// base-user
import Department from '../views/base/user/Department'
import Account from '../views/base/user/Account'
import Isv from '../views/base/user/Isv'

// base-tactic
import UpperTactic from '../views/base/tactic/UpperTactic'
import BatchTactic from '../views/base/tactic/BatchTactic'
import QcTactic from '../views/base/tactic/QcTactic'
import SoMergeTactic from '../views/base/tactic/SoMergeTactic'
import PalletMergeTactic from '../views/base/tactic/PalletMergeTactic'
import PrintTemp from '../views/base/tactic/PrintTemp'
import PrintIp from '../views/base/tactic/PrintIp'

// base-system
import SysParam from '../views/base/system/SysParam'
import DataDict from '../views/base/system/DataDict'
import Message from '../views/base/system/Message'
import SysSetting from '../views/base/system/SysSetting'
import TimeTask from '../views/base/system/TimeTask'
import TimeTaskLog from '../views/base/system/TimeTaskLog'
import Resource from '../views/base/system/Resource'
import ServerCache from '../views/base/system/ServerCache'
import Developer from '../views/base/system/Developer'
import WebHookLog from '../views/base/system/WebHookLog'
import WebHookSetting from '../views/base/system/WebHookSetting'

// report
import AsnDayReport from '../views/report/AsnDayReport'
import SoDayReport from '../views/report/SoDayReport'
import InventorySafetyReport from "../views/report/InventorySafetyReport";
import UReport from "../views/report/UReport";
import ReportOperate from "../views/report/ReportOperate";
import ReportOperateDetail from "../views/report/ReportOperateDetail";



// manufacture
import ManufactureBill from "../views/work/manufacture/ManufactureBill";


// 领料
import RequestMaster from '../views/work/request/master/List';
import RequestMasterCreate from '../views/work/request/master/Create';
import RequestMasterDetail from '../views/work/request/master/Detail';


Vue.use(VueRouter)

const routes = [
    { path: '*', redirect: '/notFind' },
    {
        path: '/', name: 'home', component: Home, redirect: '/homepage', children: [
            { path: '/homepage', name: 'homepage', component: Homepage },

            //test
            { path: '/base/selectProduct', name: 'SelectProduct', component: SelectProduct },

            // demo
            { path: '/importExcel', name: 'importExcel', component: ImportExcel },

            // work
            { path: '/work/cellList', name: 'cellList', component: CellList },

            // work-orderManage
            // saleBill
            { path: '/work/orderManage/saleBill', name: 'saleBill', component: SaleBill ,meta: {keepAlive: true}},
            { path: '/work/orderManage/saleBill/create', name: 'createSaleBill', component: CreateSaleBill },
            { path: '/work/orderManage/saleBill/detail/:billNo', name: 'saleBillDetail', component: SaleBillDetail },
            { path: '/work/orderManage/saleBill/draft', name: 'saleBillDraft', component: SaleBillDraft },

            // work-billInManage
            //buyBillIn
            { path: '/work/billInManage/buyBillIn', name: 'buyBillIn', component: BuyBillIn,meta: {keepAlive: true} },
            { path: '/work/billInManage/buyBillIn/create', name: 'createBuyBillIn', component: CreateBuyBillIn  },
            { path: '/work/billInManage/buyBillIn/detail/:billNo', name: 'buyBillInDetail', component: BuyBillInDetail },
            { path: '/work/billInManage/buyBillIn/draft', name: 'buyBillInDraft', component: BuyBillInDraft },
            { path: '/work/billInManage/buyBillIn/file', name: 'buyBillInFile', component: BuyBillInFile  },
            // ber
            { path: '/work/billInManage/asnReturnBill', name: 'asnReturnBill', component: AsnReturnBill ,meta: {keepAlive: true}},
            { path: '/work/billInManage/asnReturnBill/draft', name: 'asnReturnBillDraft', component: AsnReturnBillDraft },
            { path: '/work/billInManage/asnReturnBill/create', name: 'createAsnReturnBill', component: CreateAsnReturnBill },
            { path: '/work/billInManage/asnReturnBill/detail/:billNo', name: 'asnReturnBillDetail', component: AsnReturnBillDetail},
            { path: '/work/billInManage/asnReturnBill/file', name: 'asnReturnBillFile', component: AsnReturnBillFile },
            // {
            //     path: '/work/billInManage/asnReturnBill/details/accept',
            //     name: 'asnReturnBillDetailsAccept',
            //     component: AsnReturnBillDetailsAccept
            // },
            // asnBill
            { path: '/work/billInManage/asnBill', name: 'asnBill', component: AsnBill , meta: {keepAlive: true}},
            { path: '/work/billInManage/asnBill/draft', name: 'asnBillDraft', component: AsnBillDraft },
            { path: '/work/billInManage/asnBill/create', name: 'createAsnBill', component: CreateAsnBill },
            { path: '/work/billInManage/asnBill/detail/:billNo', name: 'asnBillDetail', component: AsnBillDetail},
            { path: '/work/billInManage/asnBill/file', name: 'asnBillFile', component: AsnBillFile },
            {
                path: '/work/billInManage/asnBill/details/accept',
                name: 'asnBillDetailsAccept',
                component: AsnBillDetailsAccept
            },
            { path: '/work/billInManage/asnBill/asnDetails', name: 'asnDetails', component: AsnDetails , meta: {keepAlive: true}},
            //ex
            { path: '/work/in/ex', name: 'exAccept', component: ExAccept },
            { path: '/work/in/log', name: 'inLog', component: inLog },
            { path: '/work/out/log', name: 'outLog', component: outLog },
            // qc
            { path: '/work/billInManage/qcBill', name: 'qcBill', component: QcMaster },
            { path: '/work/billInManage/qcBill/create', name: 'createQc', component: CreateQc },
            { path: '/work/billInManage/qcBill/detail/:billNo', name: 'qcBillDetail', component: QcBillDetail },
            { path: '/work/billInManage/qcBill/draft', name: 'qcBillDraft', component: QcBillDraft },
            { path: '/work/billInManage/qcBill/file', name: 'qcBillFile', component: QcBillFile },


            // qcRecord
            { path: '/work/billInManage/qcBill/record', name: 'qcRecord', component: QcRecord },
            { path: '/work/billInManage/qcBill/record/file', name: 'qcRecordFile', component: QcRecordFile },

            { path: '/work/billInManage/acceptRecord', name: 'acceptRecord', component: AcceptRecord ,meta: {keepAlive: true}},
            { path: '/work/billInManage/acceptAuditRecord', name: 'acceptAuditRecord', component: AcceptAuditRecord },
            { path: '/work/billInManage/toCellInventory', name: 'toCellInventory', component: ToCellInventory },
            { path: '/work/in/fastAccept', name: 'fastAccept', component: FastAccept },

            // inCellTask
            { path: '/work/billInManage/inCellTask', name: 'inCellManage', component: InCellTask },
            { path: '/work/in/upTasks', name: 'upperTask', component: UpperTask },

            // work-inventory
            { path: '/work/inventory/totalInventory', name: 'totalInventory', component: TotalInventory },
            { path: '/work/inventory/inventoryBalance', name: 'inventoryBalance', component: InventoryBalance },
            { path: '/work/inventory/inventoryTransact', name: 'inventoryTransact', component: InventoryTransact },
            { path: '/work/inventory/freeze', name: 'freeze', component: Freeze  ,meta: {keepAlive: true}},
            { path: '/work/inventory/freeze/detail/:billNo', name: 'freezeDetail', component: FreezeDetail},
            { path: '/work/inventory/freeze/draft', name: 'freezeDraft', component: FreezeDraft },
            { path: '/work/inventory/adjust/inventoryAdjust', name: 'inventoryAdjust', component: InventoryAdjust ,meta: {keepAlive: true} },
            { path: '/work/inventory/adjust/inventoryAdjustDetail/:billNo', name: 'inventoryAdjustDetail', component: InventoryAdjustDetail },
            { path: '/work/inventory/adjust/inventoryAdjustDraft', name: 'inventoryAdjustDraft', component: InventoryAdjustDraft },
            { path: '/work/inventory/ivTransOrgMaster', name: 'ivTransOrgMaster', component: IvTransOrgMaster },
            { path: '/work/inventory/ivTransOrgDetail', name: 'ivTransOrgDetail', component: IvTransOrgDetail },
            { path: '/work/inventory/ivTransOrgCreate', name: 'ivTransOrgCreate', component: IvTransOrgCreate },
            { path: '/work/inventory/changeCapacity', name: 'changeCapacity', component: ChangeCapacity },
            { path: '/work/inventory/erpRecord', name: 'ErpRecord', component: ErpRecord },
            { path: '/work/inventory/canOut', name: 'CanOut', component: CanOut },
            { path: '/work/inventory/freezeDetails', name: 'freeze', component: FreezeDetails  ,meta: {keepAlive: true}},
            //serialNo
            { path: '/work/inventory/serialNo/inventorySerialNo', name: 'inventorySerialNo', component: InventorySerialNo },
            { path: '/work/inventory/serialNo/serialNoSelect', name: 'serialNoSelect', component: SerialNoSelect },

            //盘点
            { path: '/work/inventory/stockTakeManage/master', name: 'stockTakeMaster', component: StockTakeMaster  ,meta: {keepAlive: true}},
            { path: '/work/inventory/stockTakeManage/task', name: 'stockTakeTask', component: StockTakeTask },
            {
                path: '/work/inventory/stockTakeManage/detail/:billNo',
                name: 'stockTakeDetail',
                component: StockTakeDetail
                
            },
            //新建盘点任务
            {
                path: '/work/inventory/stockTakeManage/UpdateStockMasterTask',
                name: 'UpdateStockMasterTask',
                component: UpdateStockMasterTask
            },
            {
                path: '/work/inventory/stockTakeManage/CreateStockMasterTask',
                name: 'CreateStockMasterTask',
                component: CreateStockMasterTask,
            },
            {
                path: '/work/inventory/stockTakeManage/StockMasterTaskDetail/:stockTakeMasterId',
                name: 'StockMasterTaskDetail',
                component: StockMasterTaskDetail,
            },


            // work-billOutManage
            // soBill
            { path: '/work/billOutManage/soBill', name: 'soBill', component: SoBill  ,meta: {keepAlive: true}},
            { path: '/work/billOutManage/soBill/create', name: 'createSoBill', component: CreateSoBill },
            { path: '/work/billOutManage/soBill/detail/:billNo', name: 'soBillDetail', component: SoBillDetail },
            { path: '/work/billOutManage/soBill/draft', name: 'soBillDraft', component: SoBillDraft },
            { path: '/work/billInManage/soBill/file', name: 'soBillFile', component: SoBillFile },
            { path: '/work/billOutManage/soBill/soDetails', name: 'SoDetails', component: SoDetails  ,meta: {keepAlive: true}},
            { path: '/work/out/seeding', name: 'Seeding', component: Seeding },
            { path: '/work/out/ydyp', name: 'Ydyp', component: Ydyp },
            { path: '/work/out/shipCode', name: 'ShipCode', component: ShipCode },
            { path: '/work/out/dpdb', name: 'Dpdb', component: Dpdb },
            { path: '/work/out/outPrint', name: 'OutPrint', component: OutPrint },

            //concentrateCellInventory
            { path: '/work/billOutManage/concentrateCellInventory/concentrateCellInventory', name: 'concentrateCellInventory', component: ConcentrateCellInventory },

            // return
            { path: '/work/billOutManage/return', name: 'ReturnBill', component: ReturnBill },
            { path: '/work/billOutManage/return/create', name: 'createReturnBill', component: CreateReturnBill },
            { path: '/work/billOutManage/return/detail/:billNo', name: 'returnBillDetail', component: ReturnBillDetail },
            { path: '/work/billOutManage/return/draft', name: 'returnBillDraft', component: ReturnBillDraft },
            { path: '/work/billInManage/return/file', name: 'returnBillFile', component: ReturnBillFile },

            //调拨单
            { path: '/work/allot/allotBill', name: 'allotBill', component: AllotBill ,meta: {keepAlive: true} },
            { path: '/work/allot/create', name: 'createAllotBill', component: CreateAllotBill },
            { path: '/work/allot/detail/:billNo', name: 'allotBillDetail', component: AllotBillDetail },
            { path: '/work/allot/draft', name: 'allotBillDraft', component: AllotBillDraft },
            { path: '/work/allot/file', name: 'allotBillFile', component: AllotBillFile },

            // tactic
            { path: '/work/Wave/WaveMaster', name: 'WaveMaster', component: WaveMaster },
            { path: '/work/Wave/ChooseSo', name: 'ChooseSo', component: ChooseSo },
            { path: '/base/tactic/WaveTactic', name: 'WaveTactic', component: WaveTactic },
            { path: '/base/tactic/WaveTacticCreate', name: 'WaveTacticCreate', component: WaveTacticCreate },
            { path: '/base/tactic/RouteTactic', name: 'RouteTactic', component: RouteTactic },
            { path: '/base/printTemp', name: 'PrintTemp', component: PrintTemp },
            { path: '/base/printIp', name: 'PrintIp', component: PrintIp },

            //
            { path: '/base/user/PrintConfig"', name: 'PrintConfig', component: PrintConfig },

            //pickTask
            { path: '/work/billOutManage/pickTask/pickTask', name: 'PickTask', component: PickTask },
            { path: '/work/billOutManage/pickTask/pickTaskMove', name: 'PickTaskMove', component: PickTaskMove },
            { path: '/work/billOutManage/pickTask/pickTaskReset', name: 'PickTaskReset', component: PickTaskReset },
            { path: '/work/out/pickTaskEx', name: 'PickTaskEx', component: PickTaskEx },
            { path: '/work/out/pickTaskReturn', name: 'PickTaskReturn', component: PickTaskReturn },
            { path: '/base/tactic/PickTactic', name: 'PickTactic', component: PickTactic },
            { path: '/base/tactic/turnoverTactic', name: 'turnoverTactic', component: TurnoverTactic },

            { path: '/work/billOutManage/pickTask/cpfrTask', name: 'CpfrTask', component: CpfrTask },

            { path: '/work/Load/LoadMaster', name: 'LoadMaster', component: LoadMaster },
            { path: '/work/Load/ChoosePickTask', name: 'ChoosePickTask', component: ChoosePickTask },

            // work-workbench
            { path: '/work/workbench/menu', name: 'workbenchMenu', component: WorkbenchMenu },
            { path: '/work/workbench/acceptance', name: 'acceptance', component: Acceptance },
            { path: '/work/workbench/check', name: 'check', component: Check },
            { path: '/work/workbench/seeding', name: 'seeding', component: Seeding },
           


            // base-warehouse
            { path: '/base/warehouse/ware', name: 'ware', component: Ware },
            { path: '/base/warehouse/platform', name: 'platform', component: Platform },
            { path: '/base/warehouse/area', name: 'area', component: Area },
            { path: '/base/warehouse/shelf', name: 'shelf', component: Shelf },
            { path: '/base/warehouse/cell', name: 'cell', component: Cell },
            { path: '/base/warehouse/SeedingWall', name: 'SeedingWall', component: SeedingWall },
            { path: '/base/warehouse/Box', name: 'Box', component: Box },
            { path: '/base/printer', name: 'Printer', component: Printer },
            { path: '/base/WareShop', name: 'WareShop', component: WareShop },
            // base-itemBase
            { path: '/base/itemBase/item', name: 'item', component: Item },
            { path: '/base/itemBase/itemType', name: 'itemType', component: ItemType },
            { path: '/base/itemBase/pack', name: 'pack', component: Pack },
            { path: '/base/itemBase/packData', name: 'packData', component: PackData },
            { path: '/base/itemBase/itemCombined', name: 'itemCombined', component: ItemCombined },
            { path: '/base/itemBase/itemCombinedCreate', name: 'itemCombinedCreate', component: ItemCombinedCreate },
            { path: '/base/itemBase/inventorySafety', name: 'inventorySafety', component: InventorySafety },
            { path: '/base/itemBase/Test', name: 'Test', component: Test },
            // base-organizationManage
            { path: '/base/organizationManage/supplier', name: 'supplier', component: Supplier },
            { path: '/base/organizationManage/organization', name: 'organization', component: Organization },
            { path: '/base/organizationManage/customer', name: 'customer', component: Customer },
            { path: '/base/organizationManage/carrier', name: 'carrier', component: Carrier },
            // base-user
            { path: '/base/user/department', name: 'department', component: Department },
            { path: '/base/user/account', name: 'account', component: Account },
            { path: '/base/user/isv', name: 'isv', component: Isv },
            // base-tactic
            { path: '/base/tactic/upperTactic', name: 'upperTactic', component: UpperTactic },
            { path: '/base/tactic/batchTactic', name: 'batchTactic', component: BatchTactic },
            { path: '/base/tactic/qcTactic', name: 'qcTactic', component: QcTactic },
            { path: '/base/tactic/soMergeTactic', name: 'soMergeTactic', component: SoMergeTactic },
            { path: '/base/tactic/palletMergeTactic', name: 'palletMergeTactic', component: PalletMergeTactic },
            // base-system
            { path: '/base/system/sysParam', name: 'sysParam', component: SysParam },
            { path: '/base/system/dataDict', name: 'dataDict', component: DataDict },
            { path: '/base/system/message', name: 'message', component: Message },
            { path: '/base/system/sysSetting', name: 'sysSetting', component: SysSetting },
            { path: '/base/system/timeTask', name: 'timeTask', component: TimeTask },
            { path: '/base/system/timeTask/log', name: 'timeTaskLog', component: TimeTaskLog },
            { path: '/base/system/resource', name: 'resource', component: Resource },
            { path: '/base/system/serverCache', name: 'serverCache', component: ServerCache },
            { path: '/base/system/developer', name: 'Developer', component: Developer },
            { path: '/base/system/webhook/log', name: 'WebHookLog', component: WebHookLog },
            { path: '/base/system/webhook/setting', name: 'WebHookSetting', component: WebHookSetting },
            // report
            { path: '/report/asnDayReport', name: 'asnDayReport', component: AsnDayReport },
            { path: '/report/soDayReport', name: 'soDayReport', component: SoDayReport },
            { path: '/report/inventorySafetyReport', name: 'inventorySafetyReport', component: InventorySafetyReport },
            { path: '/report/uReport', name: 'uReport', component: UReport },
            { path: '/report/reportOperate', name: 'reportOperate', component: ReportOperate },
            { path: '/report/reportOperateDetail', name: 'reportOperateDetail', component: ReportOperateDetail },

            // manufacture
            { path: '/work/manufacture/ManufactureBill', name: 'ManufactureBill', component: ManufactureBill },

            // 领料
            { path: '/work/request/requestMaster', name: 'requestMaster', component: RequestMaster },
            { path: '/work/request/requestMaster/create', name: 'requestMasterCreate', component: RequestMasterCreate },
            { path: '/work/request/requestMaster/detail', name: 'requestMasterDetail', component: RequestMasterDetail }



        ]
    },
    {
        // 当 /user/:id/profile 匹配成功，
        // UserProfile 会被渲染在 User 的 <router-view> 中
        path: '/login',
        name: 'login',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
    },
    { path: '/notFind', name: 'notFind', component: NotFind },
    // 打印
    { path: '/print/pick-batch', name: 'PrintPickBatch', component: PrintPickBatch},
    { path: '/print/pick-suit', name: 'PrintPickSuit', component: PrintPickSuit},
    { path: '/print/item-label', name: 'PrintItemLabel', component: PrintItemLabel},
    
    
];

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes
});


export default router
