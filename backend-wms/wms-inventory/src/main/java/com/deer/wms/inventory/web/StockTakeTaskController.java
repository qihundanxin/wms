package com.deer.wms.inventory.web;

import static com.deer.wms.project.root.util.FunctionUtil.nvl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.ImportModel;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.request.ManualInventoryRequest;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.StockTakeMasterService;
import com.deer.wms.inventory.service.StockTakeTaskService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import lombok.Data;

/**
 * 盘点任务api接口
 * 
 * Created by 郭靖勋 on 2020/02/06.
 */
@RestController
@RequestMapping("/stock/take/tasks")
public class StockTakeTaskController {

    @Autowired
    private StockTakeTaskService stockTakeTaskService;
    @Autowired
    private FileSetting fileSetting;
    @Autowired
    private FileS3Tool fileS3Tool;
    @Autowired
    private StockTakeMasterService stockTakeMasterService;
    @Resource
    private InventoryService inventoryService;

    @PostMapping("/insert")
    @Authority("stock_take_tasks_insert")
    public Result add(@RequestBody StockTakeTask stockTakeTask) {
        stockTakeTaskService.save(stockTakeTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("stock_take_tasks_delete")
    public Result delete(Integer id) {
        stockTakeTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("stock_take_tasks_update")
    public Result update(@RequestBody StockTakeTask stockTakeTask) {
        stockTakeTaskService.update(stockTakeTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("stock_take_tasks_detail")
    public Result detail(Integer id) {
        StockTakeTask stockTakeTask = stockTakeTaskService.findById(id);
        return ResultGenerator.genSuccessResult(stockTakeTask);
    }

    /**
     * 复盘列表
     * @param criteria
     * @return
     */
    @GetMapping("/list")
    @Authority("stock_take_tasks_list")
    public Result list(StockTakeTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<StockTakeTaskDto> list = stockTakeTaskService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 待盘点列表
     * @param criteria
     * @return
     */
    @GetMapping("/findPDAList")
    @Authority("stock_take_tasks_findPDAList")
    public Result findPDAList(StockTakeTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<StockTakeTaskDto> list = stockTakeTaskService.findPDAList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Data
    public static class CalculateParam {
        private Integer id;
        private Double quantity;
    }

    /**
     * 盘点
     */
    @PostMapping("/calculateStockTake")
    @Authority("stock_take_tasks_calculateStockTake")
    public Result calculateStockTake(@RequestBody CalculateParam param, @User CurrentUser currentUser) {
        if (null == param.getQuantity()) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "缺少数量");
        }
        StockTakeTask task = stockTakeTaskService.findById(param.getId());
        Inventory inventory = inventoryService.findById(task.getInventoryId());
        if (task.getState() == 1) {
            task.setTaskQuantity(inventory.getQuantity());
            task.setDifferQuantity(param.getQuantity() - inventory.getQuantity());
            task.setActualQuantity(param.getQuantity());
            task.setTaskTime(DateUtils.getNowDateTimeString());
            task.setTaskUserId(currentUser.getUserId());
            task.setTaskUserName(currentUser.getUserName());
        } else if (task.getState() == 2) {
            task.setTaskQuantity2(inventory.getQuantity());
            task.setDifferQuantity2(param.getQuantity() - inventory.getQuantity());
            task.setActualQuantity2(param.getQuantity());
            task.setTaskTime2(DateUtils.getNowDateTimeString());
            task.setTaskUserId2(currentUser.getUserId());
            task.setTaskUserName2(currentUser.getUserName());
        }
        // 判断差异数量
        if (Objects.equals(nvl(task.getDifferQuantity()), 0.0d)) {
            // 无差异状态改为已盘
            task.setState(3);
            stockTakeTaskService.update(task);
        }
        // 有差异
        else {
            // 当类型是初盘时修改状态为复盘并生成任务
            if (task.getState() == 1) {
                task.setState(2);
                stockTakeTaskService.update(task);
            }
            // 当类型是复盘时修改状态为已盘
            else if (task.getState() == 2) {
                task.setState(3);
                // 修改盘点单状态
                stockTakeTaskService.update(task);
            }
        }
        StockTakeMaster stockTakeMaster = stockTakeMasterService.findById(task.getStockTakeMasterId());
        List<StockTakeTask> list = stockTakeTaskService.findByPdId(stockTakeMaster.getStockTakeMasterId());
        stockTakeMaster.setState(4); // 盘点完 无差异
        for (StockTakeTask taskTask : list) {
            if (taskTask.getState() == 1 || taskTask.getState() == 2) {
                stockTakeMaster.setState(3); // 盘点中
                break;
            }
            if (taskTask.getState() == 3 && taskTask.getDifferQuantity2() != null && !taskTask.getDifferQuantity2().equals(0.0)) {
                stockTakeMaster.setState(5); // 盘点完 需要调整
                continue;
            }
        }
        stockTakeMasterService.update(stockTakeMaster);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 导出盘点任务
     * 
     * @throws IOException
     * @throws BiffException
     * @throws WriteException
     * @throws RowsExceededException
     */
    @GetMapping("/exportStockTakeTask")
    @Authority("stock_take_tasks_exportStockTakeTask")
    public Result exportStockTakeTask(StockTakeTaskCriteria criteria, @User CurrentUser currentUser) throws BiffException, IOException, RowsExceededException, WriteException {
        criteria.setPageNum(1);
        criteria.setPageSize(100000);
        List<StockTakeTaskDto> list = stockTakeTaskService.findList(criteria);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/stockTakeTask.xls";
        String modelPath = fileSetting.getModelPath() + "/stockTakeTaskModel.xls";
        String modelFilePath = staticPath + modelPath;
        String filePath = staticPath + downloadPath;
        jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
        WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);
        WritableSheet wSheet = copy.getSheet(0);
        CellFormat format = wSheet.getCell(1, 1).getCellFormat();
        for (int i = 0; i < list.size(); i++) {
            wSheet.insertRow(i + 2);
            StockTakeTaskDto stockTakeTaskDto = list.get(i);
            String indexStr = Integer.toString(i + 1);
            String stockTakeTaskId = "-";
            if (stockTakeTaskDto.getStockTakeTaskId() != null) {
                stockTakeTaskId = stockTakeTaskDto.getStockTakeTaskId().toString();
            }
            String billNo = "-";
            if (stockTakeTaskDto.getBillNo() != null) {
                billNo = stockTakeTaskDto.getBillNo();
            }
            String type = "-";
            if (stockTakeTaskDto.getType() != null) {
                if (stockTakeTaskDto.getType() == 11) {
                    type = "初盘(明盘)";
                }
                if (stockTakeTaskDto.getType() == 12) {
                    type = "初盘(暗盘)";
                }
                if (stockTakeTaskDto.getType() == 21) {
                    type = "复盘(明盘)";
                }
                if (stockTakeTaskDto.getType() == 22) {
                    type = "复盘(暗盘)";
                }
            }
            String state = "-";
            if (stockTakeTaskDto.getState() != null) {
                if (stockTakeTaskDto.getState() == 1) {
                    state = "未盘";
                }
                if (stockTakeTaskDto.getState() == 2) {
                    state = "已盘";
                }
                if (stockTakeTaskDto.getState() == 3) {
                    state = "复盘";
                }
            }
            String wareName = "-";
            if (stockTakeTaskDto.getWareName() != null) {
                wareName = stockTakeTaskDto.getWareName();
            }
            String organizationName = "-";
            if (stockTakeTaskDto.getOrganizationName() != null) {
                organizationName = stockTakeTaskDto.getOrganizationName();
            }
            String itemCode = "-";
            if (stockTakeTaskDto.getItemCode() != null) {
                itemCode = stockTakeTaskDto.getItemCode();
            }
            String itemName = "-";
            if (stockTakeTaskDto.getItemName() != null) {
                itemName = stockTakeTaskDto.getItemName();
            }
            String differQuantity = "-";
            if (stockTakeTaskDto.getDifferQuantity() != null) {
                differQuantity = stockTakeTaskDto.getDifferQuantity().toString();
            }
            String taskQuantity = "-";
            if (stockTakeTaskDto.getTaskQuantity() != null) {
                taskQuantity = stockTakeTaskDto.getTaskQuantity().toString();
            }
            String quantity = "-";
            if (stockTakeTaskDto.getQuantity() != null) {
                quantity = stockTakeTaskDto.getQuantity().toString();
            }
            String createTime = "-";
            if (stockTakeTaskDto.getCreateTime() != null) {
                createTime = stockTakeTaskDto.getCreateTime().substring(0, 19);
            }
            String taskTime = "-";
            if (stockTakeTaskDto.getTaskTime() != null) {
                taskTime = stockTakeTaskDto.getTaskTime().substring(0, 19);
            }
            Label indexLabel = new Label(0, i + 2, indexStr);
            Label stockTakeTaskIdLabel = new Label(1, i + 2, stockTakeTaskId);
            Label billNoLabel = new Label(2, i + 2, billNo);
            Label typeLabel = new Label(3, i + 2, type);
            Label stateLabel = new Label(4, i + 2, state);
            Label wareNameLabel = new Label(5, i + 2, wareName);
            Label organizationNameLabel = new Label(6, i + 2, organizationName);
            Label itemCodeLabel = new Label(7, i + 2, itemCode);
            Label itemNameLabel = new Label(8, i + 2, itemName);
            Label differQuantityLabel = new Label(9, i + 2, differQuantity);
            Label taskQuantityLabel = new Label(10, i + 2, taskQuantity);
            Label quantityLabel = new Label(11, i + 2, quantity);
            Label createTimeLabel = new Label(12, i + 2, createTime);
            Label taskTimeLabel = new Label(13, i + 2, taskTime);
            indexLabel.setCellFormat(format);
            stockTakeTaskIdLabel.setCellFormat(format);
            billNoLabel.setCellFormat(format);
            typeLabel.setCellFormat(format);
            stateLabel.setCellFormat(format);
            wareNameLabel.setCellFormat(format);
            organizationNameLabel.setCellFormat(format);
            itemCodeLabel.setCellFormat(format);
            itemNameLabel.setCellFormat(format);
            differQuantityLabel.setCellFormat(format);
            taskQuantityLabel.setCellFormat(format);
            quantityLabel.setCellFormat(format);
            createTimeLabel.setCellFormat(format);
            taskTimeLabel.setCellFormat(format);
            wSheet.addCell(indexLabel);
            wSheet.addCell(stockTakeTaskIdLabel);
            wSheet.addCell(billNoLabel);
            wSheet.addCell(typeLabel);
            wSheet.addCell(stateLabel);
            wSheet.addCell(wareNameLabel);
            wSheet.addCell(organizationNameLabel);
            wSheet.addCell(itemCodeLabel);
            wSheet.addCell(itemNameLabel);
            wSheet.addCell(differQuantityLabel);
            wSheet.addCell(taskQuantityLabel);
            wSheet.addCell(quantityLabel);
            wSheet.addCell(createTimeLabel);
            wSheet.addCell(taskTimeLabel);
        }
        String nowStr = "打印日期：" + DateUtils.getNowDateString();
        Label dateLabel = new Label(0, list.size() + 3, nowStr);
        dateLabel.setCellFormat(format);
        wSheet.addCell(dateLabel);
        copy.write();
        copy.close();
        workbook.close();
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_STOCK_TAKE_TASK, "stockTakeTask.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    /**
     * 导入盘点任务
     */
    @PostMapping("/importStockTakeTask")
    @Transactional
    @Authority("stock_take_tasks_importStockTakeTask")
    public Result importStockTakeTask(ImportModel importmodel, @User CurrentUser currentUser) {


        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";

        try {
            billWorkbook = jxl.Workbook.getWorkbook(importmodel.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            Integer rowCount = 0;
            for (int i = 2; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 1, i);
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }

            for (int i = 2; i < rowCount + 2; i++) {
                Integer stockTakeMasterId = Integer.parseInt(ExcelUtils.getCellString(billSheet, 1, i).trim());
                Double taskQuantity = Double.valueOf(ExcelUtils.getCellString(billSheet, 10, i).trim());
                Double quantity = Double.valueOf(ExcelUtils.getCellString(billSheet, 11, i).trim());
                if (stockTakeMasterId == null) {
                    msg = "缺少任务id！";
                    throw new RuntimeException();
                }
                if (taskQuantity == null) {
                    msg = "缺少单位！" + taskQuantity;
                    throw new RuntimeException();
                }
                StockTakeTaskDto stockTakeTaskDto = stockTakeTaskService.findList(new StockTakeTaskCriteria(stockTakeMasterId)).get(0);
                stockTakeTaskDto.setDifferQuantity(taskQuantity - quantity);
                stockTakeTaskDto.setTaskUserId(currentUser.getUserId());
                stockTakeTaskDto.setTaskUserName(currentUser.getUserName());
                stockTakeTaskDto.setTaskTime(DateUtils.getNowDateTimeString());
                updateStockTakeTaskState(stockTakeTaskDto);
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
     * 手动盘点
     * 
     * 先扫商品，每扫一次 前端数量加1，当扫描到库位的时候直接上架
     */
    @PostMapping("/manualInventory")
    @Transactional
    @Authority("stock_take_tasks_manualInventory")
    public Result manualInventory(ManualInventoryRequest request) {
        String scanText = request.getScanText();
        // 先查询itemCode
        return ResultGenerator.genSuccessResult();

    }

    private void updateStockTakeTaskState(StockTakeTaskDto request) {

        StockTakeTask queryStockTakeTask = stockTakeTaskService.findById(request.getStockTakeTaskId());
        StockTakeTaskDto stockTakeTask = new StockTakeTaskDto();
        BeanUtils.copyProperties(queryStockTakeTask, stockTakeTask);
        stockTakeTask.setActualQuantity(request.getActualQuantity());
        Inventory inventory = inventoryService.findById(queryStockTakeTask.getInventoryId());
        stockTakeTask.setTaskQuantity(inventory.getQuantity());
        stockTakeTask.setDifferQuantity(stockTakeTask.getActualQuantity() - stockTakeTask.getTaskQuantity());
        stockTakeTask.setTaskTime(DateUtils.getNowDateTimeString());
        // 判断差异数量
        if (Objects.equals(nvl(stockTakeTask.getDifferQuantity()), 0.0d)) {
            // 无差异状态改为已盘
            stockTakeTask.setState(2);
            stockTakeTaskService.update(stockTakeTask);
            // 修改盘点单状态
            stockTakeMasterService.updateStockTakeMasterState(stockTakeTask.getStockTakeMasterId());
        }
        // 有差异
        else {
            // 当类型是初盘时修改状态为复盘并生成任务
            if (stockTakeTask.getType().equals(11) || stockTakeTask.getType().equals(12)) {
                stockTakeTask.setState(3);
                stockTakeTaskService.update(stockTakeTask);
            }
            // 当类型是复盘时修改状态为已盘
            if (stockTakeTask.getType().equals(21) || stockTakeTask.getType().equals(22)) {
                stockTakeTask.setState(2);
                // 修改盘点单状态
                stockTakeTaskService.update(stockTakeTask);
                stockTakeMasterService.updateStockTakeMasterState(stockTakeTask.getStockTakeMasterId());
            }
        }

        if (!Objects.equals(nvl(stockTakeTask.getDifferQuantity()), 0.0d) && (stockTakeTask.getType().equals(11)) || (stockTakeTask.getType().equals(12))) {
            stockTakeTask.setStockTakeTaskId(null);
            stockTakeTask.setState(1);
            stockTakeTask.setDifferQuantity(null);
            stockTakeTask.setTaskQuantity(null);
            stockTakeTask.setTaskUserName(null);
            stockTakeTask.setTaskUserId(null);
            stockTakeTask.setTaskTime(null);
            stockTakeTask.setActualQuantity(null);
            if (stockTakeTask.getType().equals(11)) {
                stockTakeTask.setType(21);
            }
            if (stockTakeTask.getType().equals(12)) {
                stockTakeTask.setType(22);
            }
            stockTakeTaskService.save(stockTakeTask);
        }
    }

    private void updateStockTakeTaskState2(StockTakeTask task, Double quantity) {

    }
}
