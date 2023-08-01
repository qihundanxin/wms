package com.deer.wms.ureport.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ureport.model.ReportOperate;
import com.deer.wms.ureport.model.ReportOperateCriteria;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Created by  on 2021/01/24.
 */
public interface ReportOperateService extends Service<ReportOperate, Integer> {

    @Async
    void report(
            Integer wareId,
            Integer userId,
            Integer type,
            Integer quantity1,
            Integer quantity2,
            String billNo,
            String itemCode,
            String itemName,
            String imgUrl
                );

    ReportOperate findNow(Integer wareId,Integer userId,Integer type);

    List<ReportOperate> findList(ReportOperateCriteria criteria);
}
