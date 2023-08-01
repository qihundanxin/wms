package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeCriteria;
import com.deer.wms.review.manage.model.ReviewNodeDto;

import java.util.List;

public interface ReviewNodeMapper extends Mapper<ReviewNode> {
    List<ReviewNodeDto> findList(ReviewNodeCriteria criteria);

    Integer checkExistBill(Integer reviewNodeId);
}