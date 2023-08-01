package com.deer.wms.busine.tactic.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2020/11/07.
 */
@ToString
public class WaveTacticDianshangCriteria extends QueryCriteria {
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
