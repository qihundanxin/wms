package com.deer.wms.inventory.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.deer.wms.project.root.util.SimpleMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.BatchData;
import com.deer.wms.inventory.model.Inventory.GroupByWareOrgItem;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventoryImport;
import com.deer.wms.inventory.model.Inventory.InventoryRecoveryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.combinePallet.BoxResultInsertData;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTask;
import com.deer.wms.inventory.model.move.Move;
import com.deer.wms.inventory.service.CombinePalletTaskService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.inventory.service.SerialNoService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.ExcelUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存信息api接口
 * 
 * Created by 郭靖勋 on 2019/12/18.
 */
@RestController
@RequestMapping("/inventorys")
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PackDetailService packDetailService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;

    @Autowired
    private SerialNoService serialNoService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    private CombinePalletTaskService combinePalletTaskService;

    @PostMapping("/insert")
    @Authority("inventorys_insert")
    public Result add(@RequestBody Inventory inventory) {
        inventoryService.save(inventory);
        return ResultGenerator.genSuccessResult();
    }

    @Data
    public static class InventoryMoveRequestBean {
        private Integer inventoryId;
        private Double quantity;
        private String cellCode;
        private Integer wareId;
        /**
         * 事务执行图片
         */
        private String transactImgs;
    }

    @Data
    public static class InventorySummaryMoveRequestBean {
        private Double quantity;
        private String toCellCode;
        private Integer wareId;
        private String itemCode;
        private String fromCellCode;
    }

    /**
     * 基础移库
     * @param param
     * @param currentUser
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/move")
    @Authority("inventorys_move")
    public Result move(@RequestBody InventoryMoveRequestBean param, @User CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException {

        return ResultGenerator.genSuccessResult(inventoryService.move(param, currentUser));
    }

    /**
     * 汇总移库
     *  2023/6/30 PDA移库不区分批次号
     * @param param
     * @param currentUser
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/summaryMove")
    @Authority("inventorys_summary_move")
    public Result summaryMove(@RequestBody InventorySummaryMoveRequestBean param, @User CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException {
        return ResultGenerator.genSuccessResult(inventoryService.summaryMove(
                 param.getQuantity(), param.getToCellCode(), param.getWareId(),param.getItemCode(), param.getFromCellCode(),currentUser));
    }

    /**
     * 查询汇总移库的列表
     * 2023/6/30 PDA移库查询不区分批次号
     * @param criteria
     * @return
     */
    @PostMapping("/summaryMoveList")
    @Authority("inventorys_summary_moveList")
    public Result summaryMoveList(@RequestBody InventoryCriteria criteria) {
        //用户根据货位搜索时 如果是搜索条件为垃圾货位-则提示不可查询可移动库存
        if(criteria.getCellCode()!=null && criteria.getCellCode()!=""){
            Integer cellUseType = cellInfoService.getCellType(criteria.getCellCode(),criteria.getWareId());
            if(Np.i(cellUseType).eq(-1)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此货位不可进行基础移库操作");
            }
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());

        List<InventoryDto> list = inventoryService.findSummaryList(criteria);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

//    @GetMapping("/delete")
//    public Result delete(Integer id) {
//        inventoryService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }

    @PostMapping("/update")
    @Authority("inventorys_update")
    public Result update(@RequestBody Inventory inventory) {
        inventoryService.update(inventory);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("inventorys_detail")
    public Result detail(Integer id) {
        Inventory inventory = inventoryService.findById(id);
        return ResultGenerator.genSuccessResult(inventory);
    }

    @GetMapping("/getAccept")
    @Authority("inventorys_getAccept")
    public Result getAccept(Integer id, Integer wareId) {
        Inventory inventory = inventoryService.getAccept(id, wareId);
        return ResultGenerator.genSuccessResult(inventory);
    }

    @GetMapping("/list")
    @Authority("inventorys_list")
    public Result list(InventoryCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryDto> list = inventoryService.findListMoreInfo(criteria);
//        for (InventoryDto inventoryDto :list){
//            List<PackDetail2> packDetailList =packDetailService.findByCode2(inventoryDto.getPackCode());
//            inventoryDto.setPackDetails(packDetailList);
//        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据SKU + CellCode进行查询结果合并（主要是合并数量）
     */
    @Deprecated
    @GetMapping("/list/mergeQtyBySkuAndCell")
    @Authority("inventorys_list_mergeQtyBySkuAndCell")
    public Result getListMergeQtyBySkuAndCell(InventoryCriteria criteria) {
        List<InventoryDto> list = inventoryService.findList(criteria);
        Map<String, Map<String, InventoryDto>> sku2CellCode2Inv = new HashMap<>();
        for (InventoryDto inv : list) {
            if (!sku2CellCode2Inv.containsKey(inv.getItemCode())) {
                sku2CellCode2Inv.put(inv.getItemCode(), new HashMap<>());
            }
            Map<String, InventoryDto> cellCode2Inv = sku2CellCode2Inv.get(inv.getItemCode());
            if (!cellCode2Inv.containsKey(inv.getCellCode())) {
                // 求和时前初始化信息（通用属性）
                InventoryDto newInv = new InventoryDto();
                newInv.setItemCode(inv.getItemCode());
                newInv.setItemName(inv.getItemName());
                newInv.setCellCode(inv.getCellCode());
                newInv.setCellUseType(inv.getCellUseType());
                cellCode2Inv.put(inv.getCellCode(), newInv);
            }
            InventoryDto invMerged = cellCode2Inv.get(inv.getCellCode());
            invMerged.setQuantity(Np.i(invMerged.getQuantity()).o(0D) + Np.i(inv.getQuantity()).o(0D));
            invMerged.setAllotQuantity(Np.i(invMerged.getAllotQuantity()).o(0D) + Np.i(inv.getAllotQuantity()).o(0D));
            invMerged.setFreezeQuantity(Np.i(invMerged.getFreezeQuantity()).o(0D) + Np.i(inv.getFreezeQuantity()).o(0D));
        }
        Set<InventoryDto> resultSet = new HashSet<>();
        sku2CellCode2Inv.values().forEach(cellCode2Inv -> {
            resultSet.addAll(cellCode2Inv.values());
        });
        List<InventoryDto> resultList = resultSet.stream()
                .sorted((r1, r2) -> (int) (r2.getQuantity() - r2.getAllotQuantity() - r2.getFreezeQuantity() - r1.getQuantity() + r1.getAllotQuantity() + r1.getFreezeQuantity()))// 倒序
                .collect(Collectors.toList());
        return ResultGenerator.genSuccessResult(resultList);
    }

    /**
     * 根据SKU + CellCode进行查询结果合并（主要是合并数量）
     */
    @GetMapping("/list/mergeQtyBySkuAndCell2")
    @Authority("inventorys_list_mergeQtyBySkuAndCell2")
    public Result getListMergeQtyBySkuAndCell2(InventoryCriteria criteria) {
        List<InventoryDto> list = inventoryService.findList(criteria);
        Map<String, Map<String, InventoryDto>> sku2CellCode2Inv = new HashMap<>();
        for (InventoryDto inv : list) {
            if (!sku2CellCode2Inv.containsKey(inv.getItemCode())) {
                sku2CellCode2Inv.put(inv.getItemCode(), new HashMap<>());
            }
            Map<String, InventoryDto> cellCode2Inv = sku2CellCode2Inv.get(inv.getItemCode());
            if (!cellCode2Inv.containsKey(inv.getCellCode())) {
                // 求和时前初始化信息（通用属性）
                InventoryDto newInv = new InventoryDto();
                newInv.setItemCode(inv.getItemCode());
                newInv.setItemName(inv.getItemName());
                newInv.setCellCode(inv.getCellCode());
                newInv.setCellUseType(inv.getCellUseType());
                cellCode2Inv.put(inv.getCellCode(), newInv);
            }
            InventoryDto invMerged = cellCode2Inv.get(inv.getCellCode());
            invMerged.setQuantity(Np.i(invMerged.getQuantity()).o(0D) + Np.i(inv.getQuantity()).o(0D));
            invMerged.setAllotQuantity(Np.i(invMerged.getAllotQuantity()).o(0D) + Np.i(inv.getAllotQuantity()).o(0D));
            invMerged.setFreezeQuantity(Np.i(invMerged.getFreezeQuantity()).o(0D) + Np.i(inv.getFreezeQuantity()).o(0D));
        }
        Set<InventoryDto> resultSet = new HashSet<>();
        sku2CellCode2Inv.values().forEach(cellCode2Inv -> {
            resultSet.addAll(cellCode2Inv.values());
        });
        List<InventoryDto> resultList = resultSet.stream()
                .sorted((r1, r2) -> (int) (r2.getQuantity() - r2.getAllotQuantity() - r2.getFreezeQuantity() - r1.getQuantity() + r1.getAllotQuantity() + r1.getFreezeQuantity()))// 倒序
                .collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(resultList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 导出
     *
     * @param criteria
     * @param currentUser
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    @GetMapping("/exportList")
    @Authority("inventorys_exportList")
    public Result exportList(InventoryCriteria criteria, @User CurrentUser currentUser) throws IOException, RowsExceededException, WriteException {
        if(criteria.getWareId() == null) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "需要传入仓库ID");
        }
        List<InventoryDto> list = inventoryService.findList(criteria);
        if (list == null || list.isEmpty()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "查无库存信息");
        }

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/Balance" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/Balance.xls";
        try {
            jxl.Workbook modelWorkbook = jxl.Workbook.getWorkbook(new java.io.File(staticPath + modelPath));
            WritableWorkbook workbook = jxl.Workbook.createWorkbook(new java.io.File(staticPath + downloadPath), modelWorkbook);
            String sheetName = null;
            int sheetPageCount = 60000;
            for (int s = 0; s * sheetPageCount < list.size(); s++) {
                WritableSheet sheet = workbook.getSheet(s);
                if (sheetName == null) {// 初次读取sheet，记录名称，用于后面可能复制sheet
                    sheetName = sheet.getName();
                }
                if ((s + 1) * sheetPageCount < list.size()) {// 预判是否需要下一页，将本页复制一份到下一页
                    workbook.copySheet(s, sheetName + (s + 2), s + 1);
                }
                for (int i = 0; i < sheetPageCount && s * sheetPageCount + i < list.size(); i++) {
                    InventoryDto inv = list.get(s * sheetPageCount + i);
                    String cellUsetype = "";
                    if (inv.getCellUseType() != null) {
                        Integer type = inv.getCellUseType();
                        if (type == -1) {
                            cellUsetype = "垃圾货位";
                        } else if (type == 0) {
                            cellUsetype = "验收位";
                        } else if (type == 1) {
                            cellUsetype = "拣货位";
                        } else if (type == 2) {
                            cellUsetype = "仓储位";
                        } else if (type == 3) {
                            cellUsetype = "集货位";
                        } else if (type == 4) {
                            cellUsetype = "上架车";
                        }
                    }
                    sheet.addCell(new Label(0, i + 1, Np.i(inv).x(x -> x.getInventoryId()).toString("")));
                    sheet.addCell(new Label(1, i + 1, Np.i(inv).x(x -> x.getItemName()).toString("")));
                    sheet.addCell(new Label(2, i + 1, Np.i(inv).x(x -> x.getItemCode()).toString("")));
                    sheet.addCell(new Label(3, i + 1, Np.i(inv).x(x -> x.getUpc()).toString("")));
                    sheet.addCell(new Label(4, i + 1, Np.i(inv).x(x -> x.getCellCode()).toString("")));
                    sheet.addCell(new Label(5, i + 1, cellUsetype));
                    sheet.addCell(new Label(6, i + 1, Np.i(inv).x(x -> x.getQuantity()).toString("")));
                    sheet.addCell(new Label(7, i + 1, Np.i(inv).x(x -> x.getAllotQuantity()).toString("")));
                    sheet.addCell(new Label(8, i + 1, Np.i(inv).x(x -> x.getFreezeQuantity()).toString("")));
                    sheet.addCell(new Label(9, i + 1, Np.i(inv).x(x -> x.getBatchName()).toString("")));
                    sheet.addCell(new Label(10, i + 1, Np.i(inv).x(x -> x.getState()).x(x -> x == 0 ? "正常" : "回收站").toString("")));
                    sheet.addCell(new Label(11, i + 1, Np.i(inv).x(x -> x.getOrganizationName()).toString("")));
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            log.error("库存信息导出失败", e);
            throw new ServiceException(CommonCode.SYSTEM_ERROR);
        }

        String fullPath = fileS3Tool.save(FileUseType.EXPORT_BALANCE, "Balance.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    /**
     * 查询可以基础移库的列表
     * @param criteria
     * @return
     */
    // TODO 按编号拦截，为何不加库位使用类型的条件筛选？
    @PostMapping("/moveList")
    @Authority("inventorys_moveList")
    public Result moveList(@RequestBody InventoryCriteria criteria) {
        //用户根据货位搜索时 如果是搜索条件为垃圾货位-则提示不可查询可移动库存
        if(criteria.getCellCode()!=null && criteria.getCellCode()!=""){
            Integer cellUseType = cellInfoService.getCellType(criteria.getCellCode(),criteria.getWareId());
            if(Np.i(cellUseType).eq(-1)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此货位不可进行基础移库操作");
            }
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryDto> list = inventoryService.findList(criteria);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 待上架库存
     * @param criteria
     * @return
     */
    // TODO 支持UPC或SKU的精准匹配查询
    @Deprecated
    @GetMapping("/getUPCUpList")
    @Authority("inventorys_getUPCUpList")
    public Result getUPCUpList(InventoryCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setCellUseType(0);
        List<InventoryDto> list = inventoryService.findList(criteria);
        if (list == null || list.isEmpty()) {
            String sku = criteria.getItemCode();
            String upc = criteria.getUpc();
            criteria.setUpc(sku);
            criteria.setItemCode(upc);
            list = inventoryService.findList(criteria);
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * pda 商品信息查询
     * @param criteria
     * @return
     */
    @GetMapping("/pdaSearch")
    @Authority("inventorys_pdaSearch")
    public Result pdaSearch(InventoryCriteria criteria) {

        String itemCode = criteria.getItemCode();
        Integer wareId = criteria.getWareId();
        criteria.setItemCode(itemCode);
        criteria.setWareId(wareId);
        List<InventoryDto> list = inventoryService.findListMoreInfo(criteria);// 验收位，拣货位，仓储位
        list= list.stream().filter(x->x.getCellUseType()==0 || x.getCellUseType()==1 ||x.getCellUseType()==2).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            List<ItemInfo> itemInfos = itemInfoService.findBySkuOrUpc(itemCode);
            if (itemInfos.size() > 0) {
                WareInfo wareInfo = wareInfoService.findById(wareId);
                ItemInfo itemInfo = itemInfos.get(0);
                InventoryDto inventoryDto = new InventoryDto();
                inventoryDto.setItemCode(itemInfo.getItemCode());
                inventoryDto.setItemName(itemInfo.getItemName());
                inventoryDto.setImgUrl(itemInfo.getImgUrl());
                inventoryDto.setQuantity(0.0);
                inventoryDto.setWareId(wareInfo.getWareId());
                inventoryDto.setWareName(wareInfo.getWareName());
                list.add(inventoryDto);
            } else {
                list.add(new InventoryDto());
            }
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/historyPickCell")
    @Authority("inventorys_pdaSearch")
    public Result historyPickCell(@RequestBody InventoryCriteria criteria) {
        String sku = criteria.getKeyWords();
        Integer wareId = criteria.getWareId();

        if(sku == null || sku == "") {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "invalid sku");
        }

        if(wareId == 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "invalid wareId");
        }

        String cellName = inventoryTransactService.findHistoryPickCellBySku(sku, wareId);
        return ResultGenerator.genSuccessResult(SimpleMap.init("cellName", cellName));
    }

    /**
     * 待拣货库存
     * @param criteria
     * @return
     */
    @GetMapping("/checkList")
    @Authority("inventorys_checkList")
    public Result checkList(InventoryCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<Inventory> list = inventoryService.checkList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * sku分组查询库存信息
     * @param criteria
     * @return
     */
    @GetMapping("/SumList")
    @Authority("inventorys_SumList")
    public Result SumList(InventoryCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<Inventory> list = inventoryService.findSame(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据库位类型查询库存列表
     * @param criteria
     * @return
     */
    @Deprecated
    @GetMapping("/CellUseTypeList")
    @Authority("inventorys_CellUseTypeList")
    public Result CellUseTypeList(InventoryCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryDto> list = inventoryService.findList(criteria);
//        for (InventoryDto inventoryDto :list){
//
//            List<PackDetail2> packDetailList =packDetailService.findByCode2(inventoryDto.getPackCode());
//
//            inventoryDto.setPackDetails(packDetailList);
//        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 库存导入
     */
    @Transactional
    @PostMapping("/importInventory")
    @Authority("inventorys_importInventory")
    public Result importInventory(InventoryImport excel, @User CurrentUser currentUser) {

        jxl.Workbook billWorkbook = null;
        String msg = "Excel 内容有问题，无法导入！";

        try {
            billWorkbook = jxl.Workbook.getWorkbook(excel.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            Integer rowCount = 0;
            for (int i = 1; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 0, i).trim();
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }
            for (int i = 2; i < rowCount + 2; i++) {

                String organizationName = ExcelUtils.getCellString(billSheet, 0, 0).trim();
                Integer organizationId = null;
                Organization organization = organizationService.findBy("organizationName", organizationName);
                organizationId = organization.getOrganizationId();

                String itemName = ExcelUtils.getCellString(billSheet, 1, i).trim();
                String itemCode = null;
                ItemInfo itemInfo = itemInfoService.findBy("itemName", itemName);
                itemCode = itemInfo.getItemCode();

                String packDescribe = ExcelUtils.getCellString(billSheet, 2, i);
                if (packDescribe == null) {
                    msg = "缺少单位！" + packDescribe;
                    throw new Exception();
                }

                PackDetail packDetail = packDetailService.findBy("packDescribe", packDescribe);

                Double quantity = ExcelUtils.getCellDouble(billSheet, 3, i);

                String boxCode = ExcelUtils.getCellString(billSheet, 4, i).trim();
                String lpn = ExcelUtils.getCellString(billSheet, 5, i).trim();

                String cellCode = ExcelUtils.getCellString(billSheet, 6, i);
                if (cellCode == null) {
                    msg = "缺少货位！" + cellCode;
                    throw new Exception();
                }
//                CellInfo cellInfo = cellInfoService.findBy("cellName",cellName);

                Inventory inventory = new Inventory();
                inventory.setWareId(excel.getWareId());
                inventory.setWareName(excel.getWareName());
                inventory.setOrganizationId(organizationId);
                inventory.setOrganizationName(organizationName);
                inventory.setItemCode(itemCode);
                inventory.setItemName(itemName);
                inventory.setBatchName("CSKC");
                inventory.setCellCode(cellCode);
                inventory.setBoxCode(boxCode);
                inventory.setLpn(lpn);
                inventory.setPackDetailId(packDetail.getPackDetailId());
                inventory.setPackDescribe(packDescribe);
                inventory.setQuantity(quantity);
                inventory.setState(0);

                inventoryService.save(inventory);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 库存导出
     */
    @GetMapping("/exportInventory")
    @Authority("inventorys_exportInventory")
    public Result exportInboundExcel(InventoryCriteria criteria, @User CurrentUser currentUser) {
        criteria.setPageNum(1);
        criteria.setPageSize(10000);

        List<InventoryDto> list = inventoryService.findListMoreInfo(criteria);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/inventory" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/inventoryModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();
            DecimalFormat two = new DecimalFormat("#.##");

            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                InventoryDto inventory = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String wareName = "-";
                if (inventory.getWareName() != null) {
                    wareName = inventory.getWareName();
                }
                String organizationName = "-";
                if (inventory.getOrganizationName() != null) {
                    organizationName = inventory.getOrganizationName();
                }
                String itemName = "-";
                if (inventory.getItemName() != null) {
                    itemName = inventory.getItemName();
                }
                String itemCode = "-";
                if (inventory.getCellCode() != null) {
                    itemCode = inventory.getItemCode();
                }
                String batchName = "-";
                if (inventory.getBatchName() != null) {
                    batchName = inventory.getBatchName();
                }

                String quantity = "-";
                if (inventory.getQuantity() != null) {
                    quantity = Double.toString(inventory.getQuantity());
                }

                String packDescribe = "-";
                if (inventory.getPackDescribe() != null) {
                    packDescribe = inventory.getPackDescribe();
                }
                String allotQuantity = "-";
                if (inventory.getAllotQuantity() != null) {
                    allotQuantity = Double.toString(inventory.getAllotQuantity());
                }
                String freezeQuantity = "-";
                if (inventory.getFreezeQuantity() != null) {
                    freezeQuantity = Double.toString(inventory.getFreezeQuantity());
                }
                String cellCode = "-";
                if (inventory.getCellCode() != null) {
                    cellCode = inventory.getCellCode();
                }
                String boxCode = "-";
                if (inventory.getBoxCode() != null) {
                    boxCode = inventory.getBoxCode();
                }
                String lpn = "-";
                if (inventory.getLpn() != null) {
                    lpn = inventory.getLpn();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label wareNameLabel = new Label(1, i + 2, wareName);
                Label organizationNameLabel = new Label(2, i + 2, organizationName);
                Label itemNameLabel = new Label(3, i + 2, itemName);
                Label itemCodeLabel = new Label(4, i + 2, itemCode);
                Label batchNameLabel = new Label(5, i + 2, batchName);
                Label quantityLabel = new Label(6, i + 2, quantity);
                Label packDescribeLabel = new Label(7, i + 2, packDescribe);
                Label allotQuantityLabel = new Label(8, i + 2, allotQuantity);
                Label freezeQuantityLabel = new Label(9, i + 2, freezeQuantity);
                Label cellCodeLabel = new Label(10, i + 2, cellCode);
                Label boxCodeLabel = new Label(11, i + 2, boxCode);
                Label lpnLabel = new Label(12, i + 2, lpn);

                indexLabel.setCellFormat(format);
                wareNameLabel.setCellFormat(format);
                organizationNameLabel.setCellFormat(format);
                itemNameLabel.setCellFormat(format);
                itemCodeLabel.setCellFormat(format);
                batchNameLabel.setCellFormat(format);
                quantityLabel.setCellFormat(format);
                packDescribeLabel.setCellFormat(format);
                allotQuantityLabel.setCellFormat(format);
                freezeQuantityLabel.setCellFormat(format);
                cellCodeLabel.setCellFormat(format);
                boxCodeLabel.setCellFormat(format);
                lpnLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(wareNameLabel);
                wSheet.addCell(organizationNameLabel);
                wSheet.addCell(itemNameLabel);
                wSheet.addCell(itemCodeLabel);
                wSheet.addCell(batchNameLabel);
                wSheet.addCell(quantityLabel);
                wSheet.addCell(packDescribeLabel);
                wSheet.addCell(allotQuantityLabel);
                wSheet.addCell(freezeQuantityLabel);
                wSheet.addCell(cellCodeLabel);
                wSheet.addCell(boxCodeLabel);
                wSheet.addCell(lpnLabel);
            }

            String nowStr = "打印日期：" + DateUtils.getNowDateString();
            Label dateLabel = new Label(0, list.size() + 3, nowStr);
            dateLabel.setCellFormat(format);
            wSheet.addCell(dateLabel);

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {}

        String fullPath = fileS3Tool.save(FileUseType.EXPORT_INVENTORY, "inventory.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    /**
     * 库存移动
     */
    @PostMapping("/move/inventory")
    @Authority("inventorys_move_inventory")
    public Result moveInventory(@RequestBody Move move, @User CurrentUser currentUser) {

        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        InventoryTransact inventoryTransact = new InventoryTransact();
//        BeanUtils.copyProperties(moveInventory, inventoryTransact);
        inventoryTransact.setTransactType(InventoryConstant.TRANSACT_MOVE);
        inventoryTransact.setItemCode(move.getItemCode());
        inventoryTransact.setItemName(move.getItemName());
        inventoryTransact.setWareId(move.getWareId());
        inventoryTransact.setWareName(move.getWareName());
        inventoryTransact.setToWareId(move.getWareId());
        inventoryTransact.setToWareName(move.getWareName());
        inventoryTransact.setFromCellCode(move.getCellCode());
        inventoryTransact.setToCellCode(move.getToCellCode());
        inventoryTransact.setFromOrganizationId(move.getOrganizationId());
        inventoryTransact.setFromOrganizationName(move.getOrganizationName());
        inventoryTransact.setToOrganizationId(move.getOrganizationId());
        inventoryTransact.setToOrganizationName(move.getOrganizationName());
        inventoryTransact.setFromBatchId(move.getBatchId());
        inventoryTransact.setFromBatchName(move.getBatchName());
        inventoryTransact.setToBatchId(move.getBatchId());
        inventoryTransact.setToBatchName(move.getBatchName());
        inventoryTransact.setFromBoxCode(move.getBoxCode());
        inventoryTransact.setToBoxCode(move.getToBoxCode());
        inventoryTransact.setFromPackDetailId(move.getPackDetailId());
        inventoryTransact.setFromPackDescribe(move.getPackDescribe());
        inventoryTransact.setToPackDetailId(move.getPackDetailId());
        inventoryTransact.setToPackDescribe(move.getPackDescribe());
        inventoryTransact.setFromTransRatio(move.getTransRatio());
        inventoryTransact.setToTransRatio(move.getTransRatio());
        inventoryTransact.setCreateUserId(createUserId);
        inventoryTransact.setCreateUserName(createUserName);
        inventoryTransact.setCreateTime(createTime);
        inventoryTransact.setFromQuantity(move.getQuantity());
        inventoryTransact.setToQuantity(move.getToQuantity());
        int inventoryId = inventoryTransactService.run(inventoryTransact);
        List<String> serialNos = move.getSerialNos();
        if (serialNos != null && serialNos.size() != 0) {
            serialNoService.serialNoRecored(move.getSerialNos(), createUserId,
                    createUserName, null, inventoryId, InventoryConstant.SERIALNO_TRANSFER);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 查询冻结货位
     */
    @GetMapping("/findByFreeze")
    @Authority("inventorys_findByFreeze")
    public Result findByFreeze(InventoryCriteria criteria) {
        List<Inventory> list = inventoryService.findByCellCode(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查询解冻货位
     */
    @GetMapping("/findByRelease")
    @Authority("inventorys_findByRelease")
    public Result findByRelease(InventoryCriteria criteria) {
        List<Inventory> list = inventoryService.findByCellCodeAndState(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 货位冻结
     */
    @GetMapping("/freezeCell")
    @Authority("inventorys_freezeCell")
    public Result freezeCell(Integer inventoryId, Double freezeQuantity1, @User CurrentUser currentUser) {
        Inventory inventory = inventoryService.findById(inventoryId);
        inventory.setFreezeQuantity(inventory.getFreezeQuantity() + freezeQuantity1);
        inventory.setQuantity(inventory.getQuantity() - freezeQuantity1);

        if (inventory.getQuantity() == 0) {
            inventory.setState(2);
        } else {
            inventory.setState(1);
        }
        inventoryService.update(inventory);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 货位批量冻结
     */
    @GetMapping("/freezeCells")
    @Authority("inventorys_freezeCells")
    public Result freezeCells(String cellCode, Integer wareId, @User CurrentUser currentUser) {

        InventoryCriteria criteria = new InventoryCriteria();
        criteria.setCellCode(cellCode);
        criteria.setWareId(wareId);
        List<Inventory> inventorys = inventoryService.findByCellCode(criteria);
        for (Inventory inventory : inventorys) {
            inventory.setFreezeQuantity(inventory.getFreezeQuantity() + inventory.getQuantity());
            inventory.setQuantity(0.0);
            inventory.setState(2);
            inventoryService.update(inventory);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 货位解冻
     */
    @GetMapping("/releaseCell")
    @Authority("inventorys_releaseCell")
    public Result releaseCell(Integer inventoryId, Double freezeQuantity1, @User CurrentUser currentUser) {
        Inventory inventory = inventoryService.findById(inventoryId);
        inventory.setFreezeQuantity(inventory.getFreezeQuantity() - freezeQuantity1);
        inventory.setQuantity(inventory.getQuantity() + freezeQuantity1);
        if (inventory.getFreezeQuantity() == 0) {
            inventory.setState(0); // 正常
        }
        if (inventory.getQuantity() != 0) {
            inventory.setState(1); // 部分冻结
        }
        inventoryService.update(inventory);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 货位批量解冻
     */
    @GetMapping("/releaseCells")
    @Authority("inventorys_releaseCells")
    public Result releaseCells(String cellCode, Integer wareId, @User CurrentUser currentUser) {
        InventoryCriteria criteria = new InventoryCriteria();
        criteria.setCellCode(cellCode);
        criteria.setWareId(wareId);
        List<Inventory> inventorys = inventoryService.findByCellCodeAndState(criteria);
        for (Inventory inventory : inventorys) {
            inventory.setQuantity(inventory.getFreezeQuantity());
            inventory.setFreezeQuantity(0.0);
            inventory.setState(0);
            inventoryService.update(inventory);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 货位锁定
     */
    @GetMapping("/lockCell")
    @Authority("inventorys_lockCell")
    public Result lockCell(String cellCode) {

        InventoryCriteria criteria = new InventoryCriteria();
        criteria.setCellCode(cellCode);
        List<Inventory> inventorys = inventoryService.findByCellCode(criteria);
        if (inventorys.size() > 0) {
            throw new ServiceException(CommonCode.CELL_ERROR);
        }
        CellInfo cellInfo = cellInfoService.findBy("cellCode", cellCode);
        cellInfo.setState("disable");
        cellInfoService.update(cellInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 货位解锁
     */
    @GetMapping("/unlockCell")
    @Authority("inventorys_unlockCell")
    public Result unlockCell(String cellCode) {

        CellInfo cellInfo = cellInfoService.findBy("cellCode", cellCode);
        cellInfo.setState("enable");
        cellInfoService.update(cellInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 同步库存和批次的质检状态 传inventoryId
     */
    @PostMapping("/synchronization")
    @Authority("inventorys_synchronization")
    public Result synchronization(BatchData batchData) {
        for (int i = 0; i < batchData.getInventoryIds().size(); i++) {
            Inventory inventory = inventoryService.findById(i);
            ItemBatch itemBatch = itemBatchService.findById(inventory.getBatchId());
            itemBatch.setQcState(batchData.getQcState());
            itemBatchService.update(itemBatch);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 库存安全报表
     */
    @PostMapping("/inventorySafetyReport")
    @Authority("inventorys_inventorySafetyReport")
    public Result inventorySafetyReport(@RequestBody InventoryCriteria criteria, @User CurrentUser user) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        return ResultGenerator.genSuccessResult(new PageInfo(inventoryService.inventorySafetyList(criteria)));
    }

    /**
     * 查询待合并库存
     */
    @GetMapping("/palletsToCombine")
    @Authority("inventorys_palletsToCombine")
    public Result palletsToCombine(InventoryCriteria criteria, @User CurrentUser currentUser) {
//        if (currentUser == null) {
//            throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER);
//        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setMaxQuantity(1000.0);
        criteria.setMaxWeight(1000.0);
        List<Inventory> inventories = inventoryService.palletsToCombine(criteria);
        PageInfo pageInfo = new PageInfo(inventories);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据追踪号查询库存
     * @param boxCode
     * @return
     */
    @GetMapping("/inventoriesWithBoxCode")
    @Authority("inventorys_inventoriesWithBoxCode")
    public Result inventoriesWithBoxCode(String boxCode) {
        List<Inventory> inventories = inventoryService.inventoriesWithBoxCode(boxCode);
        PageInfo pageInfo = new PageInfo(inventories);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/boxResult")
    @Transactional
    @Authority("inventorys_boxResult")
    public Result boxResult(@RequestBody BoxResultInsertData insertData) {
        String boxCode = insertData.getBoxCode();
        String boxCode2 = insertData.getBoxCode2();
        List<Inventory> inventories = insertData.getInventoryList();
        List<Inventory> inventories2 = insertData.getInventoryList2();
        for (Inventory inventory : inventories) {
            inventory.setBoxCode(boxCode);
            inventoryService.update(inventory);
        }
        for (Inventory inventory : inventories2) {
            inventory.setBoxCode(boxCode2);
            inventoryService.update(inventory);
        }
        List<CombinePalletTask> tasks = combinePalletTaskService.taskWithBoxCode(boxCode);
        CombinePalletTask task = tasks.get(0);
        task.setCompleteTime(DateUtils.getNowDateTimeString());
        combinePalletTaskService.update(task);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 库存统计
     * @param criteria
     * @return
     */
    @GetMapping("/findGroupByWareOrgItem")
    @Transactional
    @Authority("inventorys_findGroupByWareOrgItem")
    public Result boxResult(InventoryCriteria criteria) {
        List<GroupByWareOrgItem> list = inventoryService.findGroupByWareOrgItem(criteria);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 可拣库存
     * @param criteria
     * @return
     */
    @GetMapping("/canOut")
    @Transactional
    @Authority("inventorys_canOut")
    public Result canOut(InventoryCriteria criteria) {
        List<InventoryDto> list = inventoryService.canOut(criteria);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    private static Integer exportCount = 1;

    @GetMapping("/exportFindGroupByWareOrgItem")
    @Transactional
    @Authority("inventorys_exportFindGroupByWareOrgItem")
    public Result exportFindGroupByWareOrgItem(InventoryCriteria criteria) {
        criteria.setPageNum(1);
        criteria.setPageSize(1000000);
        List<GroupByWareOrgItem> list = inventoryService.findGroupByWareOrgItem(criteria);
        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/InventoryB" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        exportCount += 1;
        String filePath = staticPath + downloadPath;

        try {
            Workbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("First");
            sheet.createRow(0);
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);

            String[] title = { "仓库", "货主", "名称", "SKU", "总数", "可用数", "验收位数", "仓储位数", "上架数", "分配数", "出货区数", "冻结数", "单位" };
            ExcelUtils excelUtils = new ExcelUtils(new File(filePath));
            excelUtils.writeArrayToExcel(0, true, 0, 0, title);

            for (int i = 0; i < list.size(); i++) {
                GroupByWareOrgItem box = list.get(i);
                String orgName = box.getOrganizationName() == null ? "" : box.getOrganizationName();
                String wareName = box.getWareName() == null ? "" : box.getWareName();
                String itemName = box.getItemName() == null ? "" : box.getItemName();
                String itemCode = box.getItemCode() == null ? "" : box.getItemCode();
                String totalQuantity = box.getTotalQuantity() == null ? "" : box.getTotalQuantity().toString();
                String totalAllotQuantity = box.getTotalAllotQuantity() == null ? "" : box.getTotalAllotQuantity().toString();
                String totalFreezeQuantity = box.getTotalFreezeQuantity() == null ? "" : box.getTotalFreezeQuantity().toString();
                String totalAcceptQuantity = box.getTotalAcceptQuantity() == null ? "" : box.getTotalAcceptQuantity().toString();
                String totalOutQuantity = box.getTotalOutQuantity() == null ? "" : box.getTotalOutQuantity().toString();
                String totalSjcQuantity = box.getTotalSjcQuantity() == null ? "" : box.getTotalSjcQuantity().toString();
                String totalCcwQuantity = box.getTotalCcwQuantity() == null ? "" : box.getTotalCcwQuantity().toString();
                String canAllotQuantity = box.getCanAllotQuantity() == null ? "" : box.getCanAllotQuantity().toString();
                // String packDescribe = box.getPackDescribe() == null ? "" : box.getPackDescribe();

                String[] row = {
                        wareName,
                        orgName,
                        itemName,
                        itemCode,
                        totalQuantity,
                        canAllotQuantity,
                        totalAcceptQuantity,
                        totalCcwQuantity,
                        totalSjcQuantity,
                        totalAllotQuantity,
                        totalOutQuantity,
                        totalFreezeQuantity,
                        "each" };
                excelUtils.insertRowWithFormat(0, i + 1);
                excelUtils.writeArrayToExcel(0, true, i + 1, 0, row);
            }
            excelUtils.writeAndClose();
            ((SXSSFWorkbook) workbook).dispose();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SKU_INVENTORY, "InventoryB.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

//    private void otherWareData(String itemCode, Integer wareId, List<InventoryDto> inventoryDtoList) {
//        List<Integer> wareIdList = wareInfoService.searchAllWareId();
//        List<Integer> newWareIdList = new ArrayList<>();
//        for (Integer id : wareIdList) {
//            if (id.equals(wareId)) {
//                continue;
//            }
//            newWareIdList.add(id);
//        }
//        for (Integer id : newWareIdList) {
//            InventoryCriteria criteria = new InventoryCriteria();
//            criteria.setItemCode(itemCode);
//            criteria.setWareId(id);
//            List<InventoryDto> list = inventoryService.findList(criteria);
//
//            inventoryDtoList.addAll(list);
//        }
//    }

    /**
     * TODO 暂时未增加权限标识
     * @return
     */
    @GetMapping("/fasdfasdf")
    @Transactional
    @Authority("inventorys_fasdfasdf")
    public Result fasdfasdf() {
        List<Inventory> list = inventoryService.findAll();
        for (Inventory inven : list) {
            Integer wareId = inven.getWareId();
            String cellCode = inven.getCellCode();
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
            if (cellInfo != null) {
                inven.setCellUseType(cellInfo.getCellUseType());
                inventoryService.update(inven);
            }
        }

        return ResultGenerator.genSuccessResult();

    }

    /**
     * 库存回收-放入回收站
     * 
     * @return
     */
    @PostMapping("/recovery")
    @Authority("inventorys_recovery")
    public Result inventoryRecovery(@RequestBody InventoryRecoveryCriteria criteria, @User CurrentUser currentUser) {
        inventoryService.inventoryRecovery(criteria, currentUser);
        return ResultGenerator.genSuccessResult();
    }
}
