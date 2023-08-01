package com.deer.wms.ware.task.model.Wave;

import java.util.List;

import lombok.ToString;

@ToString
public class WaveDetailTransData {
    private Integer waveId;
    private List<Integer> waveDetailIds;

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public List<Integer> getWaveDetailIds() {
        return waveDetailIds;
    }

    public void setWaveDetailIds(List<Integer> waveDetailIds) {
        this.waveDetailIds = waveDetailIds;
    }
}
