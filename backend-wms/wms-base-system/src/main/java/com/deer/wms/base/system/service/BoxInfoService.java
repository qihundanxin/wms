package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.BoxInfoCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/03/17.
 */
public interface BoxInfoService extends Service<BoxInfo, Integer> {

    List<BoxInfo> findList(BoxInfoCriteria criteria);

    BoxInfo findByBoxCode(String boxCode,Integer type, Integer wareId);

    BoxInfo findByBoxCode2(String boxCode, Integer wareId);
}
