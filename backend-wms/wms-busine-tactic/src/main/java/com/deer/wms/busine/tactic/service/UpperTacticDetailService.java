package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/28.
 */
public interface UpperTacticDetailService extends Service<UpperTacticDetail, Integer> {

    List<UpperTacticDetailDto> findList(UpperTacticDetailCriteria criteria);

    List<UpperTacticDetail> findByAreaCode(String areaCode);

    List<UpperTacticDetailDto> findByUpperTacticCode(String upperTacticCode);

}
