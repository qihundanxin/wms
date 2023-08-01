package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.ReviewMaster;
import com.deer.wms.review.manage.model.ReviewMasterCriteria;

import com.deer.wms.project.root.core.service.Service;

import java.util.List;

import com.deer.wms.review.manage.model.ReviewMasterDto;

import javax.jws.WebService;

/**
 * Created by guo on 2018/10/24.
 */
@WebService
public interface ReviewMasterService extends Service<ReviewMaster, Integer> {

    List<ReviewMasterDto> findList(ReviewMasterCriteria criteria);

}
