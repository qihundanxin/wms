package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.CpfrTask;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by  on 2021/08/07.
 */
public interface CpfrTaskService extends Service<CpfrTask, Integer> {

    void create();


}
