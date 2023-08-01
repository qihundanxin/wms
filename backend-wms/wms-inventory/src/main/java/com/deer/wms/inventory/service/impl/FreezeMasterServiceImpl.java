package com.deer.wms.inventory.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.dao.FreezeMasterMapper;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.freeze.*;
import com.deer.wms.inventory.service.FreezeDetailService;
import com.deer.wms.inventory.service.FreezeMasterService;

import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/12/25.
 */
@Service
@Transactional
public class FreezeMasterServiceImpl extends AbstractService<FreezeMaster, Integer> implements FreezeMasterService {

    @Autowired
    private FreezeMasterMapper freezeMasterMapper;

    @Autowired
    private FreezeDetailService freezeDetailService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<FreezeMasterDto> findList(FreezeMasterCriteria criteria) {
        return freezeMasterMapper.findList(criteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        freezeMasterMapper.deleteByBillNo(billNo);
    }

    @Override
    @Transactional
    public void deleteByIdAndReleaseDetail(Integer id, CurrentUser currentUser) {
        FreezeMaster freezeMaster = this.findById(id);
        if(freezeMaster.getState()!=2){
            String billNo = freezeMaster.getBillNo();
            //释放库存
            BillRecord billRecord = new BillRecord();
            billRecord.setBillNo(billNo);
            billRecord.setRecordType(51);//整个释放
            billRecord.setCreateTime(DateUtils.getNowDateTimeString());
            billRecord.setCreateUserId(currentUser.getUserId());
            billRecord.setCreateUserName(currentUser.getUserName());
            billRecordService.createBillRecord(billRecord, currentUser);

            FreezeDetailCriteria criteria = new FreezeDetailCriteria();
            criteria.setBillNo(billNo);
            List<FreezeDetailDto> freezeDetailDtos = freezeDetailService.findList(criteria);
            for (FreezeDetail freezeDetail : freezeDetailDtos) {
                Double quantity = freezeDetail.getFreezeQuantity();
                Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
                inventory.setFreezeQuantity(inventory.getFreezeQuantity() - quantity);
                inventoryService.update(inventory);

                freezeDetail.setState(2);
                freezeDetail.setFreezeQuantity(freezeDetail.getFreezeQuantity() - quantity);
                freezeDetailService.update(freezeDetail);
            }
        }
        this.deleteById(freezeMaster.getFreezeMasterId());
    }
}
