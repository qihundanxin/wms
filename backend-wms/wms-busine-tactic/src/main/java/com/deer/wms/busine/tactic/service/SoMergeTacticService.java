package com.deer.wms.busine.tactic.service;

import java.util.List;

import com.deer.wms.busine.tactic.model.SoMergeTactic;
import com.deer.wms.busine.tactic.model.SoMergeTacticCriteria;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by ll on 2020/02/04.
 */
public interface SoMergeTacticService extends Service<SoMergeTactic, Integer> {

    List<SoMergeTactic> findList(SoMergeTacticCriteria criteria);

}
