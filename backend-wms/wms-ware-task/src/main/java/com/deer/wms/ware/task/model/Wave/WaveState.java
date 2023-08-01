package com.deer.wms.ware.task.model.Wave;

import lombok.ToString;

@ToString
public class WaveState {
    private Integer waveId;
    private Integer state;
    private Integer count4;
    private Integer count5;
    private Integer count6;
    private Integer count7;
    private Integer count8;

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCount4() {
        return count4;
    }

    public void setCount4(Integer count4) {
        this.count4 = count4;
    }

    public Integer getCount5() {
        return count5;
    }

    public void setCount5(Integer count5) {
        this.count5 = count5;
    }

    public Integer getCount6() {
        return count6;
    }

    public void setCount6(Integer count6) {
        this.count6 = count6;
    }

    public Integer getCount7() {
        return count7;
    }

    public void setCount7(Integer count7) {
        this.count7 = count7;
    }

    public Integer getCount8() {
        return count8;
    }

    public void setCount8(Integer count8) {
        this.count8 = count8;
    }
}
