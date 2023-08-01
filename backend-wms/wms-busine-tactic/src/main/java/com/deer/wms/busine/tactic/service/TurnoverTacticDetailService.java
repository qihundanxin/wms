package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetailCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/03/15.
 */
public interface TurnoverTacticDetailService extends Service<TurnoverTacticDetail, Integer> {
    List<TurnoverTacticDetail> findList(TurnoverTacticDetailCriteria criteria);

    void deleteByTurnoverCode(String turnoverCode);
}
