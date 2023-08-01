package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.QC.QcTactic;
import com.deer.wms.busine.tactic.model.QC.QcTacticCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
public interface QcTacticService extends Service<QcTactic, Integer> {

    List<QcTactic> findList(QcTacticCriteria criteria);

}
