package com.deer.wms.review.manage.model;

import java.util.List;

import lombok.ToString;

/**
 * Created by guo on 2018/10/24.
 */
@ToString
public class ReviewNodeDto extends ReviewNode {

    List<Integer> userIds;
    List<String> userNames;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }
}
