package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Allot.AllotDetailCriteria;

import java.util.List;

/**
 * Created by ll on 2020/03/04.
 */
public interface AllotDetailService extends Service<AllotDetail, Integer> {

    List<AllotDetail> findList(AllotDetailCriteria criteria);
}
