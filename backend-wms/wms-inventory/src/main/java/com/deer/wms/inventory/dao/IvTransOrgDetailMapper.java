package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.IvTransOrgDetail;
import com.deer.wms.inventory.model.IvTransOrgDetailCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface IvTransOrgDetailMapper extends Mapper<IvTransOrgDetail> {
    List<IvTransOrgDetail> findList(IvTransOrgDetailCriteria criteria);
}