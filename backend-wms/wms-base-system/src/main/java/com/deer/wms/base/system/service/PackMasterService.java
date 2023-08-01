package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Pack.PackMaster;
import com.deer.wms.base.system.model.Pack.PackMasterCriteria;
import com.deer.wms.base.system.model.Pack.PackMasterDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/18.
 */
public interface PackMasterService extends Service<PackMaster, Integer> {

    List<PackMasterDto> findList(PackMasterCriteria criteria);

    List<PackMasterDto> findByPackCode(String packCode);

    List<PackMasterDto> findByPackName(String packName);

    void deleteByCode(String packCode);
}
