package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.PickTaskEx;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.PickTaskExCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;

import java.util.List;

/**
 * Created by  on 2021/03/20.
 */
public interface PickTaskExService extends Service<PickTaskEx, Integer> {

    List<PickTaskEx> findList(PickTaskExCriteria criteria);

}
