package com.deer.wms.busine.tactic.model.Wave;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/03.
 */
@ToString
public class WaveTacticCriteria extends QueryCriteria {
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
