package com.deer.wms.ware.task.model.Wave;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.deer.wms.ware.task.model.SO.SoMaster;

import lombok.ToString;

@ToString
public class WaveMasterDto extends WaveMaster {

    private String customerName;
    private String carrierName;
    private String soTyName;

    private List<SoMaster>  bills;

    public List<SoMaster> getBills() {
        return bills;
    }

    public void setBills(List<SoMaster> bills) {
        this.bills = bills;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getSoTyName() {
        return soTyName;
    }

    public void setSoTyName(String soTyName) {
        this.soTyName = soTyName;
    }

    @Column(name = "wave_tactic_code")
    private String waveTacticCode;

    @Column(name = "wave_tactic_name")
    private String waveTacticName;

    /**
     * 优先级  数字越大 等级越高
     */
    @Column(name = "priority_level")
    private Integer priorityLevel;

    /**
     * 0-未激活，1-激活
     */
    private Integer state;

    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 客户
     */
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 承运商
     */
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 订单类型
     */
    @Column(name = "so_type")
    private Integer soType;

    private String lastTime;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 订单结构  1：一单一品一件  2：一单一品多件 3：一单一品 4：一单多品
     */
    @Column(name = "so_structure")
    private Integer soStructure;

    /**
     * 订单发运截止时间
     */
    @Column(name = "so_deadline")
    private Date soDeadline;

    /**
     * 最大订单数
     */
    @Column(name = "so_count_limit")
    private Integer soCountLimit;

    /**
     * 最大SKU数
     */
    @Column(name = "item_count")
    private Integer itemCount;

    /**
     * 是否播种，1-是，0-否
     */
    @Column(name = "is_seeding")
    private Integer isSeeding;

    private Integer maxWaitTime;


    public String getWaveTacticName() {
        return waveTacticName;
    }

    public void setWaveTacticName(String waveTacticName) {
        this.waveTacticName = waveTacticName;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
    }

    public Integer getSoStructure() {
        return soStructure;
    }

    public void setSoStructure(Integer soStructure) {
        this.soStructure = soStructure;
    }

    public Date getSoDeadline() {
        return soDeadline;
    }

    public void setSoDeadline(Date soDeadline) {
        this.soDeadline = soDeadline;
    }

    public Integer getSoCountLimit() {
        return soCountLimit;
    }

    public void setSoCountLimit(Integer soCountLimit) {
        this.soCountLimit = soCountLimit;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }


    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }
}
