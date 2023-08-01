package com.deer.wms.ware.task.model;

import java.util.List;

import lombok.ToString;

@ToString
public class RequestInsert {

    private RequestMaster requestMaster;

    private List<RequestDetail> requestDetailList;

    public RequestMaster getRequestMaster() {
        return requestMaster;
    }

    public void setRequestMaster(RequestMaster requestMaster) {
        this.requestMaster = requestMaster;
    }

    public List<RequestDetail> getRequestDetailList() {
        return requestDetailList;
    }

    public void setRequestDetailList(List<RequestDetail> requestDetailList) {
        this.requestDetailList = requestDetailList;
    }
}