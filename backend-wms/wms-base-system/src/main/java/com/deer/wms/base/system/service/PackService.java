package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Pack.Pack;
import com.deer.wms.base.system.model.Pack.PackCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/18.
 */
public interface PackService extends Service<Pack, Integer> {

    List<Pack> findList(PackCriteria criteria);

    List<Pack> findByPackData(String packData);

}
