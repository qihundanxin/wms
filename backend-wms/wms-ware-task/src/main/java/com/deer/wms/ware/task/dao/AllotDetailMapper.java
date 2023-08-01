package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.ware.task.model.Allot.AllotDetailCriteria;

import java.util.List;

public interface AllotDetailMapper extends Mapper<AllotDetail> {

    List<AllotDetail> findList(AllotDetailCriteria criteria);
}