package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.PrintIpMapper;
import com.deer.wms.busine.tactic.model.PrintIp;
import com.deer.wms.busine.tactic.model.PrintIpCriteria;
import com.deer.wms.busine.tactic.model.PrintIpDto;
import com.deer.wms.busine.tactic.service.PrintIpService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/07/30.
 */
@Service
@Transactional
public class PrintIpServiceImpl extends AbstractService<PrintIp, Integer> implements PrintIpService {

    @Autowired
    private PrintIpMapper printIpMapper;

    @Override
    public List<PrintIpDto> findList(PrintIpCriteria criteria) {
        return printIpMapper.findList(criteria);
    }

    @Override
    public PrintIpDto findByIPId(Integer id) {
        return printIpMapper.findByIPId(id);
    }
}
