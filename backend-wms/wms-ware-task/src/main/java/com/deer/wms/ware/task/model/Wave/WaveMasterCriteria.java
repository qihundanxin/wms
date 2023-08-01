package com.deer.wms.ware.task.model.Wave;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/02.
 */
@ToString
public class WaveMasterCriteria extends QueryCriteria {
    private Integer userId;
    private Integer state;
    private String boxCode;
    private Integer isSeeding;
    private Integer noAllot;

    public Integer getNoAllot() {
        return noAllot;
    }

    public void setNoAllot(Integer noAllot) {
        this.noAllot = noAllot;
    }

    public Integer getIsSeeding() {
        return isSeeding;
    }

    public void setIsSeeding(Integer isSeeding) {
        this.isSeeding = isSeeding;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
