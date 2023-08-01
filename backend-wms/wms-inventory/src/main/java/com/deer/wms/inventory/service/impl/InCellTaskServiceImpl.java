package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.InCellTaskMapper;
import com.deer.wms.inventory.model.InCellTask;
import com.deer.wms.inventory.service.InCellTaskService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ll on 2020/03/05.
 */
@Service
@Transactional
public class InCellTaskServiceImpl extends AbstractService<InCellTask, Integer> implements InCellTaskService {

    @Autowired
    private InCellTaskMapper inCellTaskMapper;

}
