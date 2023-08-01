package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.ShipBoxDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.ShipBoxDetailCriteria;

import java.util.List;

/**
 * Created by  on 2021/01/06.
 */
public interface ShipBoxDetailService extends Service<ShipBoxDetail, Integer> {

    List<ShipBoxDetail> findList(ShipBoxDetailCriteria criteria);

}
