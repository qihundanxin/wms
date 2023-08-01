package com.deer.wms.review.manage.service.impl;

import com.deer.wms.review.manage.dao.ReviewUserMapper;
import com.deer.wms.review.manage.model.ReviewUser;
import com.deer.wms.review.manage.service.ReviewUserService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
@Service
@Transactional
public class ReviewUserServiceImpl extends AbstractService<ReviewUser, Integer> implements ReviewUserService {

    @Autowired
    private ReviewUserMapper reviewUserMapper;

    @Override
    public List<ReviewUser> findByNodeId(Integer reviewNodeId) {
        return reviewUserMapper.findByNodeId(reviewNodeId);
    }
}
