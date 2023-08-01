package com.deer.wms.ware.task.model.Allot;

import java.util.List;

import com.deer.wms.ware.task.model.SO.PackDetails;

import lombok.ToString;

@ToString
public class AllotDetailDto extends AllotDetail {
    private Integer allotMasterId;
    private Integer fromWareId;
    private String fromWareName;
    private Integer toWareId;
    private String toWareName;
    private Integer organizationId;
    private String organizationName;
    private Double transRatios;

    private List<PackDetails> packDetails;

    public Integer getAllotMasterId() {
        return allotMasterId;
    }

    public void setAllotMasterId(Integer allotMasterId) {
        this.allotMasterId = allotMasterId;
    }

    public Integer getFromWareId() {
        return fromWareId;
    }

    public void setFromWareId(Integer fromWareId) {
        this.fromWareId = fromWareId;
    }

    public String getFromWareName() {
        return fromWareName;
    }

    public void setFromWareName(String fromWareName) {
        this.fromWareName = fromWareName;
    }

    public Integer getToWareId() {
        return toWareId;
    }

    public void setToWareId(Integer toWareId) {
        this.toWareId = toWareId;
    }

    public String getToWareName() {
        return toWareName;
    }

    public void setToWareName(String toWareName) {
        this.toWareName = toWareName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Double getTransRatios() {
        return transRatios;
    }

    public void setTransRatios(Double transRatios) {
        this.transRatios = transRatios;
    }

    public List<PackDetails> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetails> packDetails) {
        this.packDetails = packDetails;
    }
}
