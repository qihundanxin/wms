package com.deer.wms.busine.tactic.model.Batch;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/29.
 */
@ToString
public class BatchTacticCriteria extends QueryCriteria {
    private Integer batchTacticId;
    private String batchTacticCode;
    private String batchTacticName;
    private Integer batchTacticDetailId;

    public Integer getBatchTacticId() {
        return batchTacticId;
    }

    public void setBatchTacticId(Integer batchTacticId) {
        this.batchTacticId = batchTacticId;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public String getBatchTacticName() {
        return batchTacticName;
    }

    public void setBatchTacticName(String batchTacticName) {
        this.batchTacticName = batchTacticName;
    }

    public Integer getBatchTacticDetailId() {
        return batchTacticDetailId;
    }

    public void setBatchTacticDetailId(Integer batchTacticDetailId) {
        this.batchTacticDetailId = batchTacticDetailId;
    }
}
