package com.deer.wms.review.manage.model;

import java.util.List;

import lombok.ToString;

@ToString
public class ReviewMasterInsert {
    ReviewMaster reviewMaster;
    List<ReviewNodeInsert> reviewNodes;

    public ReviewMaster getReviewMaster() {
        return reviewMaster;
    }

    public void setReviewMaster(ReviewMaster reviewMaster) {
        this.reviewMaster = reviewMaster;
    }

    public List<ReviewNodeInsert> getReviewNodes() {
        return reviewNodes;
    }

    public void setReviewNodes(List<ReviewNodeInsert> reviewNodes) {
        this.reviewNodes = reviewNodes;
    }
}
