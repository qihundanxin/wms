package com.deer.wms.inventory.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.dao.InventoryAdjustDetailMapper;
import com.deer.wms.inventory.model.*;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;

import com.deer.wms.inventory.service.InventoryAdjustService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.model.BillRecordTypeConstants;
import com.deer.wms.review.manage.service.BillRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/12.
 */
@Service
@Transactional
public class InventoryAdjustDetailServiceImpl extends AbstractService<InventoryAdjustDetail, Integer> implements InventoryAdjustDetailService {

    @Autowired
    private InventoryAdjustDetailMapper inventoryAdjustDetailMapper;

    @Autowired
    private InventoryAdjustService inventoryAdjustService;

    @Autowired
    private InventoryTransactService inventoryTransactService;
    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<InventoryAdjustDetailDto> findList(InventoryAdjustDetailCriteria inventoryAdjustDetailCriteria) {
        return inventoryAdjustDetailMapper.findList(inventoryAdjustDetailCriteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        inventoryAdjustDetailMapper.deleteByBillNo(billNo);
    }

    /**
     * 库存调整
     * @param criteria
     * @param currentUser
     */
    @Override
    public void adjustAffirm(InventoryAdjustDetailCriteria criteria, CurrentUser currentUser) {
        //如果是批量进行库存调整，需要确认允许进行批量调整
        if(criteria.getBillNo()!=null){
            InventoryAdjustCriteria inventoryAdjustCriteria = new InventoryAdjustCriteria();
            inventoryAdjustCriteria.setBillNo(criteria.getBillNo());
            List<InventoryAdjustDto> list =  inventoryAdjustService.findList(inventoryAdjustCriteria);
            if(list.size()>0){
                if(list.get(0).getAllowBatch()!=1){
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "该库存调整单不允许进行批量调整!");
                }
            }
        }
        List<InventoryAdjustDetailDto> inventoryAdjustDetailDtos = this.findList(criteria);
        for (InventoryAdjustDetailDto inventoryAdjustDetailDto : inventoryAdjustDetailDtos) {
            if (inventoryAdjustDetailDto.getState().equals(1)) {
                inventoryAdjustDetailDto.setState(2);
                inventoryAdjustDetailDto.setAdjustTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetailDto.setAdjustUserId(currentUser.getUserId());
                inventoryAdjustDetailDto.setAdjustUserName(currentUser.getUserName());
                this.update(inventoryAdjustDetailDto);
                Inventory inventory = inventoryService.findById(inventoryAdjustDetailDto.getInventoryId());
                inventory.setQuantity(inventoryAdjustDetailDto.getAdjustQuantity());
                if (inventory.getQuantity() < (inventory.getAllotQuantity() + inventory.getFreezeQuantity())) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "数量不能小于冻结数量与已分配数量之和！");
                }
                inventoryService.update(inventory);
                inventoryTransactService.run(
                        null, inventory, InventoryConstant.INVENTORY_ADJUST, inventoryAdjustDetailDto.getDetailNo(), currentUser.getUserId(), currentUser.getUserName());
                BillRecord billRecord = new BillRecord();
                billRecord.setBillNo(inventoryAdjustDetailDto.getBillNo());
                billRecord.setCreateTime(DateUtils.getNowDateTimeString());
                billRecord.setCreateUserId(currentUser.getUserId());
                billRecord.setCreateUserName(currentUser.getUserName());
                billRecord.setRecordType(BillRecordTypeConstants.INVENTORY_ADJUST);
                billRecord.setMemo("库存调整");
                billRecordService.save(billRecord);
            }
        }
    }

}
