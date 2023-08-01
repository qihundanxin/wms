package com.deer.wms.system.manage.model.ware;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/05.
 */
@ToString
public class WareUserCriteria extends QueryCriteria {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
