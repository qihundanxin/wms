package com.deer.wms.review.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2018/10/24.
 */
@ToString
public class ReviewNodeCriteria extends QueryCriteria {

    private Integer reviewMasterId;

    private Integer nodeUserId;

    public Integer getNodeUserId() {
        return nodeUserId;
    }

    public void setNodeUserId(Integer nodeUserId) {
        this.nodeUserId = nodeUserId;
    }

    public Integer getReviewMasterId() {
        return reviewMasterId;
    }

    public void setReviewMasterId(Integer reviewMasterId) {
        this.reviewMasterId = reviewMasterId;
    }
}
