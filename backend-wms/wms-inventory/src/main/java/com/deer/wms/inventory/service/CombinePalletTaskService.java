package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.combinePallet.CombinePalletTask;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/06/14.
 */
public interface CombinePalletTaskService extends Service<CombinePalletTask, Integer> {

    List<CombinePalletTask> taskWithBoxCode(String boxCode);
}
