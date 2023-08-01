package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.PrinterInfo;
import com.deer.wms.base.system.model.PrinterInfoCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/08/25.
 */
public interface PrinterInfoService extends Service<PrinterInfo, Integer> {

    List<PrinterInfo> findList(PrinterInfoCriteria criteria);
    PrinterInfo findPdaDefault(Integer wareId);
    void changePda(PrinterInfo printerInfo);


}
