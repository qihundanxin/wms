package com.deer.wms.quartz.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.deer.wms.inventory.model.InventoryAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.lzb.erp.service.ErpRecordService;

import lombok.extern.slf4j.Slf4j;

@Component("inventoryAdjustSynErp")
@Slf4j
public class InventoryAdjustSynErp {

    @Resource
    private ErpRecordService erpRecordService;

    @Resource
    private InventoryAdjustDetailService inventoryAdjustDetailService;

    public void execute() {
        InventoryAdjustDetailCriteria criteria = new InventoryAdjustDetailCriteria();
        criteria.setState(2);
        criteria.setSynErp(0);
        List<InventoryAdjustDetailDto> adjustDetails = inventoryAdjustDetailService.findList(criteria);
        for (InventoryAdjustDetailDto adjustDetail : adjustDetails) {
            try {
                erpRecordService.inventoryAdjust(adjustDetail);
            } catch (Exception e) {
                log.error("adjustDetail同步erp失败，adjustDetailId:{}", adjustDetail.getAdjustDetailId(), e);
            }
        }
    }

}
