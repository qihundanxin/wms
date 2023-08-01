import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home'
import Login from '@/views/Login.vue'
import ResInware from '@/views/createTask/ResInware.vue'
import ResInware2 from '@/views/createTask/ResInware2.vue'
import ResOutware from '@/views/createTask/ResOutware.vue'
import ResTuiware from '@/views/createTask/ResTuiware.vue'
import ResTuiitem from '@/views/createTask/ResTuiitem.vue'
import AddItem from '@/views/createTask/AddItem.vue'
import InventoryWater from '@/views/inventory/InventoryWater'
import InventoryWarning from '@/views/inventory/InventoryWarning'
import InventoryWarningDetail from '@/views/inventory/InventoryWarningDetail'
import FilterShelf from '@/views/inventory/FilterShelf' 
import InWareDetail from '@/views/report/InWareDetail'
import OutWareDetail from '@/views/report/OutWareDetail'
import Demo from '@/views/Demo'
import About from '@/views/About'

import Scan from '../components/Scan' 

// accept
import BaseAccept from '../views/accept/BaseAccept'
import UPCAccept from '../views/accept/UPCAccept'
import UPCAccept2 from '../views/accept/UPCAccept2'
import ExAccept from '../views/accept/ExAccept'
import BaseAccept2 from '../views/accept/BaseAccept2'
import MixPlateAccept from '../views/accept/MixPlateAccept'
import MixPlateAccept2 from '../views/accept/MixPlateAccept2'
import BlindAccept from '../views/accept/BlindAccept'
import Qc from '../views/accept/Qc'
import Qc2 from '../views/accept/Qc2'
// upCell
import BaseUpCell from '../views/upCell/BaseUpCell'
import BaseUpCell2 from '../views/upCell/BaseUpCell2'
import MergeUpCell from '../views/upCell/MergeUpCell'
import ShangJiaChe1 from '../views/upCell/ShangJiaChe1'
import ShangJiaChe2 from '../views/upCell/ShangJiaChe2'
import Zjsj from '../views/upCell/Zjsj'
import UPCsj from '../views/upCell/UPCsj'
//outStore
import SearchSo from '../views/outStore/SearchSo'
import GetNewTask from '../views/outStore/GetNewTask'
import BoxPickTask from '../views/outStore/BoxPickTask'
import BoxPickTaskList from '../views/outStore/BoxPickTaskList'
import PickTaskReturn from '../views/outStore/PickTaskReturn'
import PickTaskEx from '../views/outStore/PickTaskEx'
import fy from '../views/outStore/fy'
import fyRecord from '../views/outStore/fyRecord'
import PickTaskPrint from '../views/outStore/PickTaskPrint'
import PickTaskPrint2 from '../views/outStore/PickTaskPrint2'



//stockTake
import TaskNumberInventoryCheck from '../views/inventory/TaskNumberInventoryCheck'
import StockTakeTask from '../views/inventory/StockTakeTask'
import StockTakeTask2 from '../views/inventory/StockTakeTask2'
// 信息查询
import SkuInfoSearch from '../views/inventory/SkuInfoSearch'

//pickTask
import SelectFirstPickTask from '../views/pickTask/SelectFirstPickTask'
import OrderPickTask from '../views/pickTask/OrderPickTask'
import OrderPickTaskOne from '../views/pickTask/OrderPickTaskOne'
import LabelPickTask from '../views/pickTask/LabelPickTask'
import AffirmPickTask from '../views/pickTask/AffirmPickTask'
import HandlePickBatchException from '../views/pickTask/HandlePickBatchException'
import CellAndItemException from '../views/pickTask/CellAndItemException'
import HandleException from '../views/pickTask/HandleException'
import WarePickTask from '../views/pickTask/WarePickTask'
/*import WavePickTask from '../views/pickTask/WavePickTask'
import ShortcutPickTask from '../views/pickTask/ShortcutPickTask'
import ConcurrencePickTask from '../views/pickTask/ConcurrencePickTask'

import CrossPickTask from '../views/pickTask/CrossPickTask'
import StandardArtificialPickTask from '../views/pickTask/StandardArtificialPickTask'
import ArtificialPickTask from '../views/pickTask/ArtificialPickTask'
import ArtificialOppositePickTask from '../views/pickTask/ArtificialOppositePickTask'
import LogoutPickTask from '../views/pickTask/LogoutPickTask'
import StereoscopicPickTask from '../views/pickTask/StereoscopicPickTask'*/

import BoxingBySo from "../views/outStore/BoxingBySo";
import BoxingByWave from "../views/outStore/BoxingByWave";
import Loading from "../views/outStore/Loading";
import ScanBox from "../views/outStore/ScanBox";
import WaveSeeding from "../views/outStore/WaveSeeding";
import WaveSeeding2 from "../views/outStore/WaveSeeding2";
import GetWaveTask from "../views/outStore/GetWaveTask";
import bjbf from "../views/outStore/bjbf";
import bjbfSo from "../views/outStore/bjbfSo";
import bjbfBatch from "../views/outStore/bjbfBatch";
import bjbfBatchScanTest from "../views/outStore/bjbfBatchScanTest";

import CheckBySo from "../views/outStore/CheckBySo";
import CheckByID from "../views/outStore/CheckByID";
import CheckByItem from "../views/outStore/CheckByItem";

//inventory
import BaseMove from '../views/inventory/BaseMove'
import BaseMove2 from '../views/inventory/BaseMove2'
import BaseSummaryMove from '../views/inventory/BaseSummaryMove'
import BaseSummaryMove2 from '../views/inventory/BaseSummaryMove2'
import FreezeCell from '../views/inventory/FreezeCell'
import FreezeCell2 from '../views/inventory/FreezeCell2'
import ReleaseCell from '../views/inventory/ReleaseCell'
import ReleaseCell2 from '../views/inventory/ReleaseCell2'
import MergePlate from '../views/inventory/MergePlate'

Vue.use(VueRouter)

const routes = [
    
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login },
    { path: '/resInware', name: 'resInware', component: ResInware },
    { path: '/resInware2', name: 'resInware2', component: ResInware2 },
    { path: '/resOutware', name: 'resOutware', component: ResOutware },
    { path: '/resTuiware', name: 'resTuiware', component: ResTuiware },
    { path: '/resTuiitem', name: 'resTuiitem', component: ResTuiitem },
    { path: '/addItem', name: 'addItem', component: AddItem },
    { path: '/inventoryWater', name: 'inventoryWater', component: InventoryWater },
    { path: '/inventoryWarning', name: 'inventoryWarning', component: InventoryWarning },
    { path: '/inventoryWarningDetail', name: 'inventoryWarningDetail', component: InventoryWarningDetail },
    { path: '/filterShelf', name: 'filterShelf', component: FilterShelf },
    
    { path: '/inWareDetail', name: 'inWareDetail', component: InWareDetail },
    { path: '/outWareDetail', name: 'outWareDetail', component: OutWareDetail },
    { path: '/demo', name: 'demo', component: Demo },

    // accept-baseAccept
    { path: '/baseAccept', name: 'baseAccept', component: BaseAccept },
    { path: '/baseAccept2', name: 'baseAccept2', component: BaseAccept2 },
    { path: '/mixPlateAccept', name: 'mixPlateAccept', component: MixPlateAccept },
    { path: '/mixPlateAccept2', name: 'mixPlateAccept2', component: MixPlateAccept2 },
    { path: '/blindAccept', name: 'blindAccept', component: BlindAccept },
    { path: '/qc', name: 'qc', component: Qc },
    { path: '/qc2', name: 'qc2', component: Qc2 },
    { path: '/UPCAccept', name: 'UPCAccept', component: UPCAccept },
    { path: '/UPCAccept2', name: 'UPCAccept2', component: UPCAccept2 },
    { path: '/ExAccept', name: 'ExAccept', component: ExAccept },
    // upCell
    { path: '/baseUpCell', name: 'baseUpCell', component: BaseUpCell },
    { path: '/baseUpCell2', name: 'baseUpCell2', component: BaseUpCell2 },
    { path: '/mergeUpCell', name: 'mergeUpCell', component: MergeUpCell },
    { path: '/shangJiaChe1', name: 'shangJiaChe1', component: ShangJiaChe1 },
    { path: '/shangJiaChe2', name: 'shangJiaChe2', component: ShangJiaChe2 },
    { path: '/Zjsj', name: 'Zjsj', component: Zjsj },
    { path: '/UPCsj', name: 'UPCsj', component: UPCsj },
    //stockTake
    { path: '/taskNumberInventoryCheck', name: 'taskNumberInventoryCheck', component: TaskNumberInventoryCheck },
    { path: '/stockTakeTask', name: 'stockTakeTask', component: StockTakeTask },
    { path: '/stockTakeTask2', name: 'stockTakeTask2', component: StockTakeTask2 },
    // 信息查询
    { path: '/skuInfoSearch', name: 'skuInfoSearch', component: SkuInfoSearch },
    //pickTask
    { path: '/selectFirstPickTask', name: 'selectFirstPickTask', component: SelectFirstPickTask },
    { path: '/orderPickTask', name: 'orderPickTask', component: OrderPickTask },
    { path: '/orderPickTaskOne', name: 'orderPickTaskOne', component: OrderPickTaskOne },
    { path: '/labelPickTask', name: 'labelPickTask', component: LabelPickTask },
    { path: '/affirmPickTask', name: 'affirmPickTask', component: AffirmPickTask },
    { path: '/GetNewTask', name: 'GetNewTask', component: GetNewTask },
    { path: '/BoxPickTask', name: 'BoxPickTask', component: BoxPickTask },
    { path: '/BoxPickTaskList', name: 'BoxPickTaskList', component: BoxPickTaskList },
    { path: '/PickTaskReturn', name: 'PickTaskReturn', component: PickTaskReturn },
    { path: '/PickTaskEx', name: 'PickTaskEx', component: PickTaskEx },
    { path: '/fy', name: 'fy', component: fy },
    { path: '/fyRecord', name: 'fyRecord', component: fyRecord },
    { path: '/PickTaskPrint', name: 'PickTaskPrint', component: PickTaskPrint },
    { path: '/PickTaskPrint2', name: 'PickTaskPrint2', component: PickTaskPrint2 },
    

    
    // {path: '/warePickTask', name: 'warePickTask', component: WarePickTask},
    /*{path: '/wavePickTask', name: 'wavePickTask', component: WavePickTask},
    {path: '/shortcutPickTask', name: 'shortcutPickTask', component: ShortcutPickTask},
    {path: '/concurrencePickTask', name: 'concurrencePickTask', component: ConcurrencePickTask},

    {path: '/crossPickTask', name: 'crossPickTask', component: CrossPickTask},
    {path: '/standardArtificialPickTask', name: 'standardArtificialPickTask', component: StandardArtificialPickTask},
    {path: '/artificialPickTask', name: 'artificialPickTask', component: ArtificialPickTask},
    {path: '/artificialOppositePickTask', name: 'artificialOppositePickTask', component: ArtificialOppositePickTask},
    {path: '/logoutPickTask', name: 'logoutPickTask', component: LogoutPickTask},
    {path: '/stereoscopicPickTask', name: 'stereoscopicPickTask', component: StereoscopicPickTask},*/
    { path: '/freezeCell', name: 'freezeCell', component: FreezeCell },
    { path: '/freezeCell2', name: 'freezeCell2', component: FreezeCell2 },
    { path: '/releaseCell', name: 'releaseCell', component: ReleaseCell },
    { path: '/releaseCell2', name: 'releaseCell2', component: ReleaseCell2 },

    // outStore
    { path: '/searchSo', name: 'searchSo', component: SearchSo },

    { path: '/boxingBySo', name: 'boxingBySo', component: BoxingBySo },
    { path: '/boxingByWave', name: 'boxingByWave', component: BoxingByWave },
    { path: '/checkBySo', name: 'checkBySo', component: CheckBySo },
    { path: '/checkByID', name: 'checkByID', component: CheckByID },
    { path: '/checkByItem', name: 'checkByItem', component: CheckByItem },
    { path: '/loading', name: 'loading', component: Loading },
    { path: '/scanBox', name: 'scanBox', component: ScanBox },
    { path: '/waveSeeding', name: 'waveSeeding', component: WaveSeeding },
    { path: '/waveSeeding2', name: 'waveSeeding2', component: WaveSeeding2 },
    { path: '/getWaveTask', name: 'getWaveTask', component: GetWaveTask },
    { path: '/bjbf', name: 'bjbf', component: bjbf },
    { path: '/bjbfSo', name: 'bjbfSo', component: bjbfSo },
    { path: '/bjbfBatch', name: 'bjbfBatch', component: bjbfBatch },
    { path: '/bjbfBatchScanTest', name: 'bjbfBatchScanTest', component: bjbfBatchScanTest },
    
    //inventory
    { path: '/baseMove', name: 'baseMove', component: BaseMove },
    { path: '/baseMove2', name: 'baseMove2', component: BaseMove2 },
    { path: '/baseSummaryMove', name: 'baseSummaryMove', component: BaseSummaryMove },
    { path: '/baseSummaryMove2', name: 'baseSummaryMove2', component: BaseSummaryMove2 },
    { path: '/mergePlate', name: 'mergePlate', component: MergePlate },
    { path: '/HandlePickBatchException', name: 'HandlePickBatchException', component: HandlePickBatchException },
    { path: '/CellAndItemException', name: 'CellAndItemException', component: CellAndItemException },
    { path: '/HandleException', name: 'HandleException', component: HandleException },
    {
        path: '/about',
        name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    }
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
