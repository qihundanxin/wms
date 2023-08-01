package com.deer.wms.ware.task.model.Wave;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/02.
 */
@ToString
public class WaveDetailCriteria extends QueryCriteria {
    private String waveCode;
    private Integer soMasterId;
    private Integer waveId;

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }

    public String getWaveCode() {
        return waveCode;
    }

    public void setWaveCode(String waveCode) {
        this.waveCode = waveCode;
    }

    public WaveDetailCriteria() {
    }

    public WaveDetailCriteria(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }
}
