package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.ShipBoxMapper;
import com.deer.wms.ware.task.model.ShipBox;
import com.deer.wms.ware.task.model.ShipBoxCriteria;
import com.deer.wms.ware.task.model.ShipBoxDto;
import com.deer.wms.ware.task.service.ShipBoxService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/01/06.
 */
@Service
@Transactional
public class ShipBoxServiceImpl extends AbstractService<ShipBox, Integer> implements ShipBoxService {

    @Autowired
    private ShipBoxMapper shipBoxMapper;

    @Override
    public List<ShipBoxDto> findList(ShipBoxCriteria criteria) {
        return shipBoxMapper.findList(criteria);
    }
}
