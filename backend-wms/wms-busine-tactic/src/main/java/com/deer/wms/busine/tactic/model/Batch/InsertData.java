package com.deer.wms.busine.tactic.model.Batch;

import java.util.List;

import lombok.ToString;

@ToString
public class InsertData {
    private BatchTactic batchTactic;
    private List<BatchTacticDetail> batchTacticDetails;
    private List<BatchTacticDetailDetail> batchTacticDetailDetails;

    public BatchTactic getBatchTactic() {
        return batchTactic;
    }

    public void setBatchTactic(BatchTactic batchTactic) {
        this.batchTactic = batchTactic;
    }

    public List<BatchTacticDetail> getBatchTacticDetails() {
        return batchTacticDetails;
    }

    public void setBatchTacticDetails(List<BatchTacticDetail> batchTacticDetails) {
        this.batchTacticDetails = batchTacticDetails;
    }

    public List<BatchTacticDetailDetail> getBatchTacticDetailDetails() {
        return batchTacticDetailDetails;
    }

    public void setBatchTacticDetailDetails(List<BatchTacticDetailDetail> batchTacticDetailDetails) {
        this.batchTacticDetailDetails = batchTacticDetailDetails;
    }
}
