package com.deer.wms.base.system.model.Platform;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/19.
 */
@ToString
public class PlatformCriteria extends QueryCriteria {
    private Integer id;
    private String platformCode;
    private String wareName;
    private Boolean receiving;
    private Boolean delivery;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public Boolean getReceiving() {
        return receiving;
    }

    public void setReceiving(Boolean receiving) {
        this.receiving = receiving;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }
}
