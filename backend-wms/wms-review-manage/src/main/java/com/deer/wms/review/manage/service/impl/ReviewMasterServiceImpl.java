package com.deer.wms.review.manage.service.impl;

import com.deer.wms.review.manage.dao.ReviewMasterMapper;
import com.deer.wms.review.manage.model.ReviewMaster;
import com.deer.wms.review.manage.model.ReviewMasterCriteria;
import com.deer.wms.review.manage.service.ReviewMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.deer.wms.review.manage.model.ReviewMasterDto;

/**
 * Created by guo on 2018/10/24.
 */
@Service
@Transactional
public class ReviewMasterServiceImpl extends AbstractService<ReviewMaster, Integer> implements ReviewMasterService {

    @Autowired
    private ReviewMasterMapper reviewMasterMapper;

    @Override
    public List<ReviewMasterDto> findList(ReviewMasterCriteria criteria) {
        return reviewMasterMapper.findList(criteria);
    }
}
