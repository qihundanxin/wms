package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.RouteTactic;
import com.deer.wms.busine.tactic.model.RouteTacticCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 */
public interface RouteTacticService extends Service<RouteTactic, Integer> {

    List<RouteTactic> findList(RouteTacticCriteria criteria);
}
