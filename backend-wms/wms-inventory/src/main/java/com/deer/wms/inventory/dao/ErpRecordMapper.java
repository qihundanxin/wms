package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.ErpRecord;
import com.deer.wms.inventory.model.ErpRecordCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface ErpRecordMapper extends Mapper<ErpRecord> {

    List<ErpRecord> findList(ErpRecordCriteria criteria);
}