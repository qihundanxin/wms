package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.IvTransOrgMaster;
import com.deer.wms.inventory.model.IvTransOrgMasterCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
public interface IvTransOrgMasterService extends Service<IvTransOrgMaster, Integer> {

    List<IvTransOrgMaster> findList(IvTransOrgMasterCriteria criteria);
}
