package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.ReviewUser;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
public interface ReviewUserService extends Service<ReviewUser, Integer> {
    List<ReviewUser> findByNodeId(Integer reviewNodeId);
}
