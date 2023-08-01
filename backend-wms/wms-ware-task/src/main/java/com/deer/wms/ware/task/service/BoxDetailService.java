package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Box.BoxDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Box.BoxDetailCriteria;

import java.util.List;

/**
 * Created by ll on 2020/02/15.
 */
public interface BoxDetailService extends Service<BoxDetail, Integer> {

    List<BoxDetail> findList(BoxDetailCriteria criteria);

}
