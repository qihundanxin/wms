package com.deer.wms.ware.task.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnDetailCriteria;

import java.util.List;

/**
 * Created by guo on 2020/04/19.
 */
public interface ReturnDetailService extends Service<ReturnDetail, Integer> {

    List<ReturnDetail> findByBillNo(String billNo);

    List<ReturnDetail> findList(ReturnDetailCriteria criteria);
}
