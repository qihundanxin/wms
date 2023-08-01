package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.PackMapper;
import com.deer.wms.base.system.model.Pack.Pack;
import com.deer.wms.base.system.model.Pack.PackCriteria;
import com.deer.wms.base.system.service.PackService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/18.
 */
@Service
@Transactional
public class PackServiceImpl extends AbstractService<Pack, Integer> implements PackService {

    @Autowired
    private PackMapper packMapper;

    @Override
    public List<Pack> findList(PackCriteria criteria) {
        return packMapper.findList(criteria);
    }

    @Override
    public List<Pack> findByPackData(String packData) {
        return packMapper.findByPackData(packData);
    }

}
