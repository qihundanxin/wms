package com.deer.wms.inventory.service.impl;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.dao.InventoryAdjustMapper;
import com.deer.wms.inventory.model.*;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.service.*;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 */
@Service
@Transactional
public class InventoryAdjustServiceImpl extends AbstractService<InventoryAdjust, Integer> implements InventoryAdjustService {

    @Autowired
    private InventoryAdjustMapper inventoryAdjustMapper;
    @Autowired
    private InventoryAdjustDetailService inventoryAdjustDetailService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Override
    public List<InventoryAdjustDto> findList(InventoryAdjustCriteria inventoryAdjustCriteria) {
        return inventoryAdjustMapper.findList(inventoryAdjustCriteria);
    }

    @Override
    public void deleteById(Integer id) {
        inventoryAdjustDetailService.deleteByBillNo(findById(id).getBillNo());
        super.deleteById(id);
    }


    /**
     *批量创建库存调整单
     * @param criterias
     */
    @Override
    @Transactional
    public void batchCreateInventoryAdjust(List<BatchAdjustDetailCriteria> criterias) {
        String exceptionLast = "";
        String exception = "";
        String exception2 = "";
        String exception3 = "";
        String exception4 ="";
        String exception5 ="";
        Boolean isNUll = false;
        for(BatchAdjustDetailCriteria criteria:criterias){
            //循环验证最外层的仓库是否存在且正常
            Integer wareId = criteria.getWareId();
            WareInfo wareInfo = wareInfoService.findById(wareId);
            if(wareInfo==null){
                exception4 += criteria.getWareId()+";";
            }
            if(exception4!=""){
                exception4 = "以下仓库Id无效："+exception4;
                continue;
            }
            String adjustReason = "库存修正";
            String  ReasonDescription = "通过系统批量调整";
            Integer userId = 250;
            String userName = "Baikaiwang";
            Integer organizationId = 84;
            String organizationName = "小红车供应链";
            for(BatchAdjustDetail detail:criteria.getAdjustDetails()){
                CellInfo cellInfo = cellInfoService.findByCodeAndWareId(detail.getCellCode(),wareId);
                if(cellInfo==null){
                    exception5 +=wareId+"-"+detail.getCellCode()+";";
                    continue;
                }
                if(detail.getItemCode()==""){
                    isNUll = true;
                    continue;
                }
                InventoryCriteria inventoryCriteria  = new InventoryCriteria();
                inventoryCriteria.setItemCode(detail.getItemCode());
                inventoryCriteria.setCellCode(detail.getCellCode());
                inventoryCriteria.setFreezeQuantity(1.0);
                inventoryCriteria.setWareId(wareId);
                inventoryCriteria.setState(0);
                List<InventoryDto> inventorys = inventoryService.findList(inventoryCriteria);
                if(inventorys.size()==0){
                    exception3 +=detail.getItemCode()+';';
                    continue;
                }
                if(inventorys.size()>1){
                    exception +=detail.getItemCode()+';';
                    continue;
                }
                Inventory inventory = inventorys.get(0);
                if (detail.getAdjustQuantity() < inventory.getFreezeQuantity() + inventory.getAllotQuantity()) {
                    exception2 +=detail.getItemCode()+';';
                }
            }
            if(exception!="" || exception2!="" || exception3!="" || exception5!="" || exception4!="" || isNUll){
               continue;
            }
            //原因默认为库存修改
            List<Reason> reasons = reasonService.findList(new ReasonCriteria(adjustReason));
            if (reasons.size() <= 0) {
                Reason reason = new Reason();
                reason.setReason(adjustReason);
                reasonService.save(reason);
            }
            String tableName = "inventory_adjust";
            String nextId = validatorService.getNextIdString(tableName);
            String billNo = "IA" + "-" + nextId;
            Integer caseState = 2;
            //添加调整单跟踪信息
            BillRecord billRecord = new BillRecord();
            billRecord.setBillNo(billNo);
            billRecord.setRecordType(36);//调整新增
            billRecord.setCreateTime(DateUtils.getNowDateTimeString());
            billRecord.setCreateUserId(userId);
            billRecord.setCreateUserName(userName);
            billRecord.setMemo("系统批量库存调整");
            billRecordService.save(billRecord);
            caseState = caseMasterService.createCase(billNo, 6,userId, wareId, organizationId);
            InventoryAdjust inventoryAdjust = new InventoryAdjust();
            inventoryAdjust.setBillNo(billNo);
            inventoryAdjust.setOrganizationId(organizationId);
            inventoryAdjust.setOrganizationName(organizationName);
            inventoryAdjust.setCreateTime(DateUtils.getNowDateTimeString());
            inventoryAdjust.setReason(adjustReason);
            inventoryAdjust.setReasonDescription(ReasonDescription);
            inventoryAdjust.setWareId(wareId);
            inventoryAdjust.setWareCode(wareInfo.getWareCode());
            inventoryAdjust.setWareName(wareInfo.getWareName());
            inventoryAdjust.setUserId(userId);
            inventoryAdjust.setUserName(userName);
            inventoryAdjust.setAllowBatch(1);
            billRecord.setMemo("自动选择库存创建库存调整单");
            inventoryAdjust.setCaseState(caseState);
            inventoryAdjust.setType(3); //自动选怎库存创建
            inventoryAdjust.setInsertType(0);
            for (int j = 0; j < criteria.getAdjustDetails().size(); j++) {
                BatchAdjustDetail detail = criteria.getAdjustDetails().get(j);
                InventoryCriteria inventoryCriteria  = new InventoryCriteria();
                inventoryCriteria.setItemCode(detail.getItemCode());
                inventoryCriteria.setCellCode(detail.getCellCode());
                inventoryCriteria.setFreezeQuantity(1.0);
                List<InventoryDto> inventorys = inventoryService.findListMoreInfo(inventoryCriteria);
                Inventory inventory = inventorys.get(0);
                //创建库存事务
                String detailNo = billNo + "-" + (j + 1);
                inventoryTransactService.run(
                        null, inventory, InventoryConstant.INVENTORY_ADJUST, detailNo,userId, userName);
                InventoryAdjustDetail inventoryAdjustDetail = new InventoryAdjustDetail();
                inventoryAdjustDetail.setAdjustQuantity(detail.getAdjustQuantity());
                inventoryAdjustDetail.setBatchId(inventory.getBatchId());
                inventoryAdjustDetail.setBatchName(inventory.getBatchName());
                inventoryAdjustDetail.setBillNo(billNo);
                inventoryAdjustDetail.setCellCode(detail.getCellCode());
                inventoryAdjustDetail.setDetailNo(detailNo);
                inventoryAdjustDetail.setInventoryId(inventory.getInventoryId());
                inventoryAdjustDetail.setItemCode(detail.getItemCode());
                inventoryAdjustDetail.setItemName(inventory.getItemName());
                inventoryAdjustDetail.setPackDescribe(inventory.getPackDescribe());
                inventoryAdjustDetail.setQuantity(inventory.getQuantity());
                inventoryAdjustDetail.setAdjustTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetail.setDifferQuantity(inventoryAdjustDetail.getAdjustQuantity() - inventory.getQuantity());
                inventoryAdjustDetail.setState(2);//默认状态为已调整
                inventoryAdjustDetail.setCreateTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetail.setCreateUseId(userId);
                inventoryAdjustDetail.setCreateUseName(userName);
                inventoryAdjustDetail.setAdjustTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetail.setAdjustUserId(userId);
                inventoryAdjustDetail.setAdjustUserName(userName);
                inventoryAdjustDetailService.save(inventoryAdjustDetail);
                //更新库存中的数量
                inventory.setQuantity(detail.getAdjustQuantity());
                inventoryService.update(inventory);
            }
            this.save(inventoryAdjust);
        }
        if(exception5!=""){
            exception5 = "以下仓库Id与货位对应无效："+exception5;
        }
        if(exception!=""){
            exception = "以下SKU包含相同记录，无法确定具体库存："+exception;
        }
        if(exception2!=""){
            exception2 = "以下SKU包含库存调整数量小于分配数量+冻结数量："+exception2;
        }
        if(exception3!=""){
            exception3 = "以下SKU没有可调整的库存记录："+exception3;
        }
        if(isNUll){
            exceptionLast = "sku 不可传空字符；";
        }
        if(exception!="" || exception2!="" || exception3!="" || exception5!="" || exception4!=""){
            exceptionLast = exceptionLast+exception4+exception5+exception+exception2+exception3;
        }
        if(exceptionLast!=""){
            throw new ServiceException(CommonCode.SERVICE_ERROR, exceptionLast);
        }

    }

}
