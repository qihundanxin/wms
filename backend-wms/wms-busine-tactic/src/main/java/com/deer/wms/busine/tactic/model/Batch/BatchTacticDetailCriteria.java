package com.deer.wms.busine.tactic.model.Batch;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/29.
 */
@ToString
public class BatchTacticDetailCriteria extends QueryCriteria {
    private Integer batchTacticDetailId;
    private String batchTacticCode;
    private Integer batchTacticDetailDetailId;

    public Integer getBatchTacticDetailId() {
        return batchTacticDetailId;
    }

    public void setBatchTacticDetailId(Integer batchTacticDetailId) {
        this.batchTacticDetailId = batchTacticDetailId;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public Integer getBatchTacticDetailDetailId() {
        return batchTacticDetailDetailId;
    }

    public void setBatchTacticDetailDetailId(Integer batchTacticDetailDetailId) {
        this.batchTacticDetailDetailId = batchTacticDetailDetailId;
    }
}
