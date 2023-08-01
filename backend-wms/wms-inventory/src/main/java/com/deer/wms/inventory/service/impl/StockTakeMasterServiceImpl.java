package com.deer.wms.inventory.service.impl;

import static com.deer.wms.project.root.util.FunctionUtil.nvl;

import java.util.List;
import java.util.Objects;

import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.project.root.util.Np;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.dao.StockTakeMasterMapper;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.stockTake.StockTakeData;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeMasterCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.StockTakeMasterService;
import com.deer.wms.inventory.service.StockTakeTaskService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;

/**
 * Created by 郭靖勋 on 2020/02/06.
 */
@Service
@Transactional
public class StockTakeMasterServiceImpl extends AbstractService<StockTakeMaster, Integer> implements StockTakeMasterService {

    @Autowired
    private StockTakeMasterMapper stockTakeMasterMapper;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private StockTakeMasterService stockTakeMasterService;

    @Autowired
    private StockTakeTaskService stockTakeTaskService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private CellInfoService cellInfoService;

    @Override
    public List<StockTakeMaster> findList(StockTakeMasterCriteria criteria) {
        return stockTakeMasterMapper.findList(criteria);
    }

    @Override
    public void createStockTakeMaster(StockTakeData stockTakeData, CurrentUser currentUser) {

        /**
         *  新建盘点单据
         */
        //      状态    2-等待审核(初始化)  1-审核通过  0-审核中  -1-审核退回
        Integer caseState = 2;

        StockTakeMaster stockTakeMaster = stockTakeData.getStockTakeMaster();
        String tableName = "stock_take_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "PD" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        caseState = caseMasterService.createCase(billNo, 5, currentUser.getUserId(), stockTakeMaster.getWareId(), stockTakeMaster.getOrganizationId());

        if (StringUtils.isNotBlank(stockTakeMaster.getItemCode())) {
            ItemInfo itemInfo = itemInfoService.findBySkuOrUpc(stockTakeMaster.getItemCode()).get(0);
            if (itemInfo != null) {
                stockTakeMaster.setItemName(itemInfo.getItemName());
            }
        }
        stockTakeMaster.setCreateUserId(createUserId);
        stockTakeMaster.setCreateUserName(createUserName);
        stockTakeMaster.setBillNo(billNo);
        stockTakeMaster.setCaseState(caseState);
        stockTakeMasterService.save(stockTakeMaster);
        //生成盘点任务
        if (caseState == 1) {
            int taskNumber = saveStockTakeTasks(stockTakeMaster, billNo, createUserId, createUserName, createTime);
            if (taskNumber <= 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "在拣货位，仓储位没有匹配到库存信息，无需盘点");
            }
        }
    }

    @Override
    public int saveStockTakeTasks(StockTakeMaster stockTakeMaster, String billNo, Integer createUserId, String createUserName, String createTime) {
        // 审核后生成盘点任务
        List<Inventory> list = inventoryService.getInventoryByFromInfo(stockTakeMaster);

        for (int i = 0; i < list.size(); i++) {
            StockTakeTask stockTakeTask = new StockTakeTask();
            stockTakeTask.setStockTakeMasterId(stockTakeMaster.getStockTakeMasterId());
            stockTakeTask.setCreateTime(createTime);
            stockTakeTask.setBillNo(billNo);
            stockTakeTask.setInventoryId(list.get(i).getInventoryId());
            stockTakeTask.setType(stockTakeMaster.getType());
            stockTakeTask.setState(1);
            stockTakeTaskService.save(stockTakeTask);
        }

        return list.size();
    }

    @Override
    public void updateStockTakeMasterState(Integer stockTakeMasterId) {
        List<StockTakeTaskDto> stockTakeTaskDtos = stockTakeTaskService.findList(new StockTakeTaskCriteria(stockTakeMasterId));
        int check = 0;
        int quantity = 0;
        if (stockTakeTaskDtos.size() > 0) {
            for (StockTakeTaskDto stockTakeTaskDto : stockTakeTaskDtos) {
                //如果有未完成的就累加1
                if (stockTakeTaskDto.getState().equals(1)) {
                    check++;
                }
                //如果复盘完成同时差异数量不为0的话累加1
                if (!Objects.equals(nvl(stockTakeTaskDto.getDifferQuantity()), 0.0d)
                        && stockTakeTaskDto.getState().equals(2)
                        && (stockTakeTaskDto.getType().equals(21) || stockTakeTaskDto.getType().equals(22))) {
                    quantity++;
                }
            }
            StockTakeMaster stockTakeMaster = findById(stockTakeMasterId);
            if (check > 0) {
                //有未完成的任务设置状态为盘点中
                stockTakeMaster.setState(3);
            } else {
                //如果复盘后累加数量还是有差异状态改为盘点完成(需调整)
                if (quantity > 0) {
                    stockTakeMaster.setState(5);
                }
                //复盘后累加数量小于等于0说明数量无差异状态改为盘点完成(无差异)
                else {
                    stockTakeMaster.setState(4);
                }
            }
            update(stockTakeMaster);
        }
    }
}
