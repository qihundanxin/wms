package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;
import java.util.List;

import com.deer.wms.base.system.model.ItemUpc;

import com.deer.wms.project.root.util.DateUtils;
import lombok.ToString;

/**
 * Created by guo on 2020/02/02.
 */
@ToString
public class PickTaskDto extends PickTask {
    private String billNo;
    private Double transRatio;
    private String packCode;
    private Integer customerId;
    private String customerName;
    private Integer waveId;
    private Integer invenId;

    private String allotTime;
    private Integer allotUserId;
    private String allotUserName;
    private Integer isSeeding;
    private Integer shopDailyCount;

    private List<ItemUpc> upcs;

    private Date soCreateTime;

    private String allotBillNo;

    private Boolean weighting;

    public Boolean getWeighting() {
        return weighting;
    }

    public void setWeighting(Boolean weighting) {
        this.weighting = weighting;
    }

    public String getAllotBillNo() {
        return allotBillNo;
    }

    public void setAllotBillNo(String allotBillNo) {
        this.allotBillNo = allotBillNo;
    }

    public String getSoCreateTime() {
        return DateUtils.dateToStrNullable(this.soCreateTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setSoCreateTime(String soCreateTime) {
        this.soCreateTime = DateUtils.strToDate(soCreateTime);
    }


    public List<ItemUpc> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<ItemUpc> upcs) {
        this.upcs = upcs;
    }

    public Integer getShopDailyCount() {
        return shopDailyCount;
    }

    public void setShopDailyCount(Integer shopDailyCount) {
        this.shopDailyCount = shopDailyCount;
    }

    public Integer getIsSeeding() {
        return isSeeding;
    }

    public void setIsSeeding(Integer isSeeding) {
        this.isSeeding = isSeeding;
    }

    public Integer getInvenId() {
        return invenId;
    }

    public void setInvenId(Integer invenId) {
        this.invenId = invenId;
    }

    public String getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(String allotTime) {
        this.allotTime = allotTime;
    }

    public Integer getAllotUserId() {
        return allotUserId;
    }

    public void setAllotUserId(Integer allotUserId) {
        this.allotUserId = allotUserId;
    }

    public String getAllotUserName() {
        return allotUserName;
    }

    public void setAllotUserName(String allotUserName) {
        this.allotUserName = allotUserName;
    }

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }
}
