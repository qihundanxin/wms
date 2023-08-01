package com.deer.wms.busine.tactic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.busine.tactic.dao.PrintTempInfoMapper;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.model.PrintTempInfoCriteria;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.project.root.core.service.AbstractService;

/**
 * Created by  on 2020/11/13.
 */
@Service
@Transactional
public class PrintTempInfoServiceImpl extends AbstractService<PrintTempInfo, Integer> implements PrintTempInfoService {

    @Autowired
    private PrintTempInfoMapper printTempInfoMapper;

    public PrintTempInfo findByWare(Integer wareId, String type) {
        return printTempInfoMapper.findByWare(wareId, type);
    }

    @Override
    public PrintTempInfo findByWareAndShip(Integer wareId, String shipCode) {
        return printTempInfoMapper.findByWareAndShip(wareId, shipCode);
    }

    @Override
    public List<PrintTempInfo> findList(PrintTempInfoCriteria criteria) {
        return printTempInfoMapper.findList(criteria);
    }
}
