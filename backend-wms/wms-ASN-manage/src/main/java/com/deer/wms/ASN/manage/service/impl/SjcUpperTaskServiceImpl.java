package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.SjcUpperTaskMapper;
import com.deer.wms.ASN.manage.model.SjcUpperTask;
import com.deer.wms.ASN.manage.model.SjcUpperTaskCriteria;
import com.deer.wms.ASN.manage.service.SjcUpperTaskService;

import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/01/07.
 */
@Service
@Transactional
public class SjcUpperTaskServiceImpl extends AbstractService<SjcUpperTask, Integer> implements SjcUpperTaskService {

    @Autowired
    private SjcUpperTaskMapper sjcUpperTaskMapper;

    @Override
    public List<SjcUpperTask> findBySjcShellCode(SjcUpperTaskCriteria criteria) {
        return sjcUpperTaskMapper.findBySjcShellCode(criteria);
    }

    @Override
    public List<SjcUpperTask> findList(SjcUpperTaskCriteria criteria) {
        return sjcUpperTaskMapper.findList(criteria);
    }

    @Override
    public String findSameItemCell(String itemCode, Integer wareId) {
        return sjcUpperTaskMapper.findSameItemCell(itemCode, wareId);
    }

    @Override
    public SjcUpperTask find0ByCellCode(String cellCode, Integer wareId) {
        return sjcUpperTaskMapper.find0ByCellCode(cellCode, wareId);
    }

    @Override
    public SjcUpperTask find0ByToCellCode(String cellCode, Integer wareId) {
        return sjcUpperTaskMapper.find0ByToCellCode(cellCode, wareId);
    }

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CellInfoService cellInfoService;

    @Override
    public String gerCellCode(String itemCode, Integer wareId, Integer orgId) {

        String cellCode = inventoryService.findSameItemCell(itemCode, wareId, orgId);
        //说明没有如果相同的
        if (cellCode == null) {
            return null;
            // cellCode = cellInfoService.getNewInCell(wareId);
        }
        if (cellCode == null) {
            return null;
            //throw  new ServiceException(CommonCode.SERVICE_ERROR,"没有空货位了！");
        }

        return cellCode;
    }
}
