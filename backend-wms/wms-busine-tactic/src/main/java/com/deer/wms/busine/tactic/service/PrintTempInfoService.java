package com.deer.wms.busine.tactic.service;

import java.util.List;

import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.model.PrintTempInfoCriteria;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by  on 2020/11/13.
 */
public interface PrintTempInfoService extends Service<PrintTempInfo, Integer> {

    PrintTempInfo findByWare(Integer wareId, String type);

    PrintTempInfo findByWareAndShip(Integer wareId, String shipCode);

    List<PrintTempInfo> findList(PrintTempInfoCriteria criteria);

}
