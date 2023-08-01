package com.deer.wms.ASN.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ASN.manage.model.upper.UpperTask;
import com.deer.wms.ASN.manage.model.upper.UpperTaskCriteria;

import java.util.List;

public interface UpperTaskMapper extends Mapper<UpperTask> {

    List<UpperTask> findList(UpperTaskCriteria criteria);

    UpperTask findSameUpperTask(UpperTaskCriteria inventoryCriteria);
}