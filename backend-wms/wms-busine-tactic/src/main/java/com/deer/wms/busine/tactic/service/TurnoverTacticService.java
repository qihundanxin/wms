package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.TurnoverTactic;
import com.deer.wms.busine.tactic.model.TurnoverTacticCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/03/15.
 */
public interface TurnoverTacticService extends Service<TurnoverTactic, Integer> {
    List<TurnoverTactic> findList(TurnoverTacticCriteria criteria);

    void deleteTurnoverDetailAndMaster(TurnoverTacticCriteria criteria);
}
