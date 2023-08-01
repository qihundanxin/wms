package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.IvTransOrgMaster;
import com.deer.wms.inventory.model.IvTransOrgMasterCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface IvTransOrgMasterMapper extends Mapper<IvTransOrgMaster> {
    List<IvTransOrgMaster> findList(IvTransOrgMasterCriteria criteria);
}