package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.PrintIp;
import com.deer.wms.busine.tactic.model.PrintIpCriteria;
import com.deer.wms.busine.tactic.model.PrintIpDto;
import com.deer.wms.project.root.core.service.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by  on 2021/07/30.
 */
public interface PrintIpService extends Service<PrintIp, Integer> {
    List<PrintIpDto> findList(PrintIpCriteria criteria);

    PrintIpDto  findByIPId( Integer ip);

}
