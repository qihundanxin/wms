package com.deer.wms.review.manage.service.impl;

import com.deer.wms.review.manage.dao.ReviewNodeMapper;
import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeCriteria;
import com.deer.wms.review.manage.service.ReviewNodeService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.deer.wms.review.manage.model.ReviewNodeDto;

/**
 * Created by guo on 2018/10/24.
 */
@Service
@Transactional
public class ReviewNodeServiceImpl extends AbstractService<ReviewNode, Integer> implements ReviewNodeService {

    @Autowired
    private ReviewNodeMapper reviewNodeMapper;

    @Override
    public List<ReviewNodeDto> findList(ReviewNodeCriteria criteria) {
        return reviewNodeMapper.findList(criteria);
    }

    public Boolean checkExistBill(Integer reviewNodeId) {
        Integer quantity = reviewNodeMapper.checkExistBill(reviewNodeId);
        if (quantity > 0) {
            return false;
        }
        return true;
    }
}
