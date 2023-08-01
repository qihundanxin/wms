package com.deer.wms.ware.task.model.WCS;

import lombok.ToString;

@ToString
public class CarInfo {
    private Integer carId;
    private Integer state;
    private Integer inState;
    private Integer outState;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getInState() {
        return inState;
    }

    public void setInState(Integer inState) {
        this.inState = inState;
    }

    public Integer getOutState() {
        return outState;
    }

    public void setOutState(Integer outState) {
        this.outState = outState;
    }
}
