package com.deer.wms.busine.tactic.model.Batch;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

@ToString
public class BatchTacticDetailDto extends BatchTacticDetail {
    private List<BatchTacticDetailDetail> batchTacticDetailDetails;

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
}
