package com.deer.wms.ware.task.model.Wave;

import java.util.List;

import lombok.ToString;

@ToString
public class WaveDetailInsertData {
    private List<Integer> soMasterIds;
    private Integer waveMasterId;

    public List<Integer> getSoMasterIds() {
        return soMasterIds;
    }

    public void setSoMasterIds(List<Integer> soMasterIds) {
        this.soMasterIds = soMasterIds;
    }

    public Integer getWaveMasterId() {
        return waveMasterId;
    }

    public void setWaveMasterId(Integer waveMasterId) {
        this.waveMasterId = waveMasterId;
    }
}
