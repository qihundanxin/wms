package com.deer.wms.review.manage.model;

import java.util.List;

import lombok.ToString;

@ToString
public class ReviewNodeInsert extends ReviewNode {
    List<ReviewUser> reviewUsers;

    public List<ReviewUser> getReviewUsers() {
        return reviewUsers;
    }

    public void setReviewUsers(List<ReviewUser> reviewUsers) {
        this.reviewUsers = reviewUsers;
    }
}
