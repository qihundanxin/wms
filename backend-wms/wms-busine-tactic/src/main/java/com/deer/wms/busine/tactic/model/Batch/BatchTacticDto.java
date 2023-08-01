package com.deer.wms.busine.tactic.model.Batch;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

@ToString
public class BatchTacticDto extends BatchTactic {
    private List<BatchTacticDetailDto> batchTacticDetails;
    private List<BatchTacticDetailDetail> batchTacticDetailDetails;
//    private List<String> batchTacticDetailDetailValue;

    public List<BatchTacticDetailDto> getBatchTacticDetails() {
        return batchTacticDetails;
    }

    public void setBatchTacticDetails(List<BatchTacticDetailDto> batchTacticDetails) {
        this.batchTacticDetails = batchTacticDetails;
    }

    public List<BatchTacticDetailDetail> getBatchTacticDetailDetails() {
        return batchTacticDetailDetails;
    }

    public void setBatchTacticDetailDetails(List<BatchTacticDetailDetail> batchTacticDetailDetails) {
        List<BatchTacticDetailDetail> list = new ArrayList<>();
        for (BatchTacticDetailDetail ba : batchTacticDetailDetails) {
            if (ba.getBatchTacticDetailDetailValue() != null) {
                list.add(ba);
            }
        }
        this.batchTacticDetailDetails = list;

    }

//    public List<String> getBatchTacticDetailDetailValue() {
//        return batchTacticDetailDetailValue;
//    }
//
//    public void setBatchTacticDetailDetailValue(List<String> batchTacticDetailDetailValue) {
//        this.batchTacticDetailDetailValue = batchTacticDetailDetailValue;
//    }

    /*private Integer batchTacticDetailId;
    private String detailCode;
    private String detailName;
    private String detailRfName;
    private Integer state;
    private Integer isRequired;
    private String format;
    private String batchTacticDetailDetailValue;

    public Integer getBatchTacticDetailId() {
        return batchTacticDetailId;
    }

    public void setBatchTacticDetailId(Integer batchTacticDetailId) {
        this.batchTacticDetailId = batchTacticDetailId;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailRfName() {
        return detailRfName;
    }

    public void setDetailRfName(String detailRfName) {
        this.detailRfName = detailRfName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getBatchTacticDetailDetailValue() {
        return batchTacticDetailDetailValue;
    }

    public void setBatchTacticDetailDetailValue(String batchTacticDetailDetailValue) {
        this.batchTacticDetailDetailValue = batchTacticDetailDetailValue;
    }*/
}
