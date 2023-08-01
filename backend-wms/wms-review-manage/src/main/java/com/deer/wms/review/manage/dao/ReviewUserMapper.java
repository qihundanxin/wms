package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.ReviewUser;

import java.util.List;

public interface ReviewUserMapper extends Mapper<ReviewUser> {
    List<ReviewUser> findByNodeId(Integer reviewNodeId);
}