package com.deer.wms.ASN.manage.service;

import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.ASN.manage.model.upper.UpperTask;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ASN.manage.model.upper.UpperTaskCriteria;
import com.deer.wms.ASN.manage.model.upper.UpperTaskInsert;

import java.util.List;

/**
 * Created by guo on 2019/12/19.
 */
public interface UpperTaskService extends Service<UpperTask, Integer> {

    List<UpperTask> findList(UpperTaskCriteria criteria);

    void upperTask(UpperTaskInsert upperTaskInsert);

    String findUpperCell(Inventory inventory);

    void generateInCellTask(List<Inventory> inventories);
}