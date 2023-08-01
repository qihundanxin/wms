package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.Upper.UpperTactic;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/28.
 */
public interface UpperTacticService extends Service<UpperTactic, Integer> {

    List<UpperTacticDto> findList(UpperTacticCriteria criteria);

    List<UpperTactic> findByWareId(Integer wareId);

    List<UpperTactic> findByUpperTacticCode(String upperTacticCode);

    List<UpperTactic> findByOrganizationAndWare(Integer organizationId, Integer wareId);

}
