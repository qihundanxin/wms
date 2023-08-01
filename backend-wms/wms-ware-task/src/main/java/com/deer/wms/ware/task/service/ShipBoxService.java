package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.ShipBox;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.ShipBoxCriteria;
import com.deer.wms.ware.task.model.ShipBoxDto;

import java.util.List;

/**
 * Created by  on 2021/01/06.
 */
public interface ShipBoxService extends Service<ShipBox, Integer> {

    List<ShipBoxDto> findList(ShipBoxCriteria criteria);

}
