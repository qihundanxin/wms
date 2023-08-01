package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.PackMasterMapper;
import com.deer.wms.base.system.model.Pack.PackMaster;
import com.deer.wms.base.system.model.Pack.PackMasterCriteria;
import com.deer.wms.base.system.model.Pack.PackMasterDto;
import com.deer.wms.base.system.service.PackMasterService;

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
public class PackMasterServiceImpl extends AbstractService<PackMaster, Integer> implements PackMasterService {

    @Autowired
    private PackMasterMapper packMasterMapper;

    @Override
    public List<PackMasterDto> findList(PackMasterCriteria criteria) {
        return packMasterMapper.findList(criteria);
    }

    @Override
    public List<PackMasterDto> findByPackCode(String packCode) {
        return packMasterMapper.findByPackCode(packCode);
    }

    @Override
    public List<PackMasterDto> findByPackName(String packName) {
        return packMasterMapper.findByPackName(packName);
    }

    @Override
    public void deleteByCode(String packCode) {
        packMasterMapper.deleteByCode(packCode);
    }

}
