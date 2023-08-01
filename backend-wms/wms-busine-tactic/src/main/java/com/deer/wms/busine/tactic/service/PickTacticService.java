package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.pickTactic.PickTactic;
import com.deer.wms.busine.tactic.model.pickTactic.PickTacticCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/02.
 */
public interface PickTacticService extends Service<PickTactic, Integer> {
    List<PickTactic> findList(PickTacticCriteria criteria);

    List<PickTactic> verify(PickTacticCriteria criteria);
}