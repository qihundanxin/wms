package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.IvTransOrgDetail;
import com.deer.wms.inventory.model.IvTransOrgDetailCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
public interface IvTransOrgDetailService extends Service<IvTransOrgDetail, Integer> {

    List<IvTransOrgDetail> findList(IvTransOrgDetailCriteria criteria);
}
