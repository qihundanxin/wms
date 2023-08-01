package com.deer.wms.inventory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.inventory.dao.FreezeDetailMapper;
import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.model.freeze.FreezeDetailCriteria;
import com.deer.wms.inventory.model.freeze.FreezeDetailDto;
import com.deer.wms.inventory.service.FreezeDetailService;
import com.deer.wms.project.root.core.service.AbstractService;

/**
 * Created by guo on 2019/12/25.
 */
@Service
@Transactional
public class FreezeDetailServiceImpl extends AbstractService<FreezeDetail, Integer> implements FreezeDetailService {

    @Autowired
    private FreezeDetailMapper freezeDetailMapper;

    @Override
    public List<FreezeDetailDto> findList(FreezeDetailCriteria criteria) {
        return freezeDetailMapper.findList(criteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        freezeDetailMapper.deleteByBillNo(billNo);
    }

    @Override
    public List<FreezeDetail> findFreezingByInventory(Integer inventoryId) {
        return freezeDetailMapper.findFreezingByInventory(inventoryId);
    }

    @Override
    public List<FreezeDetailDto> findListAndMaster(FreezeDetailCriteria criteria) {
        return freezeDetailMapper.findListAndMaster(criteria);
    }

}
