package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.ReviewMaster;
import com.deer.wms.review.manage.model.ReviewMasterCriteria;
import com.deer.wms.review.manage.model.ReviewMasterDto;

import java.util.List;

public interface ReviewMasterMapper extends Mapper<ReviewMaster> {

    List<ReviewMasterDto> findList(ReviewMasterCriteria criteria);
}