package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangCriteria;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface WaveTacticDianshangMapper extends Mapper<WaveTacticDianshang> {

    List<WaveTacticDianshangDto> findList(WaveTacticDianshangCriteria criteria);
}