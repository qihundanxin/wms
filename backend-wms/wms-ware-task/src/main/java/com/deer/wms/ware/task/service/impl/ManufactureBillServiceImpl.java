package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.ManufactureBillMapper;
import com.deer.wms.ware.task.model.ManufactureBill;
import com.deer.wms.ware.task.service.ManufactureBillService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ll on 2020/03/06.
 */
@Service
@Transactional
public class ManufactureBillServiceImpl extends AbstractService<ManufactureBill, Integer> implements ManufactureBillService {

    @Autowired
    private ManufactureBillMapper manufactureBillMapper;

}
