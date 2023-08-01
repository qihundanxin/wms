package com.deer.wms.inventory.model.freeze;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/12/25.
 */
@ToString
public class FreezeMasterCriteria extends QueryCriteria {

    private Integer freezeMasterId;
    private Integer createUseId;
    private String billNo;
    private Integer state;
    private String createTime;
    private Integer insertType;

    private String startCreateTime;
    private String endCreateTime;
    private String freezeDescribe;
    /**
     * 排序 ASC 时间正序，DESC 时间倒序
     */
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getFreezeMasterId() {
        return freezeMasterId;
    }

    public void setFreezeMasterId(Integer freezeMasterId) {
        this.freezeMasterId = freezeMasterId;
    }

    public Integer getCreateUseId() {
        return createUseId;
    }

    public void setCreateUseId(Integer createUseId) {
        this.createUseId = createUseId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getFreezeDescribe() {
        return freezeDescribe;
    }

    public void setFreezeDescribe(String freezeDescribe) {
        this.freezeDescribe = freezeDescribe;
    }


}

