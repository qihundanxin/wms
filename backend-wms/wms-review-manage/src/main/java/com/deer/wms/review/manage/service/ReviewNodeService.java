package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeCriteria;

import com.deer.wms.project.root.core.service.Service;

import java.util.List;

import com.deer.wms.review.manage.model.ReviewNodeDto;

/**
 * Created by guo on 2018/10/24.
 */
public interface ReviewNodeService extends Service<ReviewNode, Integer> {

    List<ReviewNodeDto> findList(ReviewNodeCriteria criteria);

    Boolean checkExistBill(Integer reviewNodeId);

}
