package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.SjcUpperTask;
import com.deer.wms.ASN.manage.model.SjcUpperTaskCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/01/07.
 */
public interface SjcUpperTaskService extends Service<SjcUpperTask, Integer> {

    List<SjcUpperTask> findBySjcShellCode(SjcUpperTaskCriteria criteria);

    List<SjcUpperTask> findList(SjcUpperTaskCriteria criteria);

    String findSameItemCell(String itemCode, Integer wareId);

    SjcUpperTask find0ByCellCode(String cellCode, Integer wareId);

    SjcUpperTask find0ByToCellCode(String cellCode, Integer wareId);

    String gerCellCode(String itemCode, Integer wareId, Integer orgId);

}
