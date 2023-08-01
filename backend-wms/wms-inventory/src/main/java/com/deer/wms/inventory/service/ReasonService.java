package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.Reason;
import com.deer.wms.inventory.model.ReasonCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 */
public interface ReasonService extends Service<Reason, Integer> {
    List<Reason> findList(ReasonCriteria criteria);
}
