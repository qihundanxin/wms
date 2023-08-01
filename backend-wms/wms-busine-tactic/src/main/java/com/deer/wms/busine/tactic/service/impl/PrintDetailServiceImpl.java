package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.PrintDetailMapper;
import com.deer.wms.busine.tactic.model.PrintDetail;
import com.deer.wms.busine.tactic.service.PrintDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/07/30.
 */
@Service
@Transactional
public class PrintDetailServiceImpl extends AbstractService<PrintDetail, Integer> implements PrintDetailService {

    @Autowired
    private PrintDetailMapper printDetailMapper;

}
