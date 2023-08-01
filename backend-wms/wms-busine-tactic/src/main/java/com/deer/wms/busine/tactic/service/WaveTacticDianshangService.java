package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangCriteria;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/11/07.
 */
public interface WaveTacticDianshangService extends Service<WaveTacticDianshang, Integer> {

    List<WaveTacticDianshangDto> findList(WaveTacticDianshangCriteria criteria);

}
