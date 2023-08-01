package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.ShipBoxDetailMapper;
import com.deer.wms.ware.task.model.ShipBoxDetail;
import com.deer.wms.ware.task.model.ShipBoxDetailCriteria;
import com.deer.wms.ware.task.service.ShipBoxDetailService;

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
public class ShipBoxDetailServiceImpl extends AbstractService<ShipBoxDetail, Integer> implements ShipBoxDetailService {

    @Autowired
    private ShipBoxDetailMapper shipBoxDetailMapper;

    @Override
    public List<ShipBoxDetail> findList(ShipBoxDetailCriteria criteria) {
        return shipBoxDetailMapper.findList(criteria);
    }
}
