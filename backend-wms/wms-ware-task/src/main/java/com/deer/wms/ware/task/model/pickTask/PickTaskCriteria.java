package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;
import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import com.deer.wms.project.root.util.DateUtils;
import lombok.ToString;

/**
 * Created by guo on 2020/02/02.
 */
@ToString
public class PickTaskCriteria extends QueryCriteria {
    private Integer waveId;
    private String billNo;
    private Integer soDetailId;
    private Integer state;
    private List<Integer> ids;

    private Integer pickTaskId;
    private String toCellCode;
    private Double reviewQuantity;
    private String trackingNum;
    private String memo;
    private Integer wareId;
    private String wareName;

    private Integer organizationId;
    private String organizationName;
    private String itemCode;
    private String itemName;
    private Integer source;
    private String areaCode;
    private String shelfCode;
    private String cellCode;

    private String customerName;
    private Integer customerId;
    private Double pickQuantity;
    private String packCode;
    private Integer packDetailId;
    private String packDescribe;
    private Double transRatio;

    private List<Integer> states;
    private String boxCode;
    private String waveCode;
    private Integer tag;

    private List<String> serialNos;

    private Integer inventoryId;
    private Integer userId;

    private Integer findBySoMasterId;

    private String startCreateTime;

    private String endCreateTime;

    private String saleState;

    private List<String> billNos;

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
    }

    public String getSaleState() {
        return saleState;
    }

    public void setSaleState(String saleState) {
        this.saleState = saleState;
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

    public Integer getFindBySoMasterId() {
        return findBySoMasterId;
    }

    public void setFindBySoMasterId(Integer findBySoMasterId) {
        this.findBySoMasterId = findBySoMasterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public List<String> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<String> serialNos) {
        this.serialNos = serialNos;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Double getPickQuantity() {
        return pickQuantity;
    }

    public void setPickQuantity(Double pickQuantity) {
        this.pickQuantity = pickQuantity;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getPackDescribe() {
        return packDescribe;
    }

    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
    }

    public String getWaveCode() {
        return waveCode;
    }

    public void setWaveCode(String waveCode) {
        this.waveCode = waveCode;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public List<Integer> getStates() {
        return states;
    }

    public Double getReviewQuantity() {
        return reviewQuantity;
    }

    public void setReviewQuantity(Double reviewQuantity) {
        this.reviewQuantity = reviewQuantity;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public PickTaskCriteria() {
    }

    public PickTaskCriteria(Integer soDetailId, Integer state) {
        this.soDetailId = soDetailId;
        this.state = state;
    }

    public PickTaskCriteria(Integer pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

    public Integer getPickTaskId() {
        return pickTaskId;
    }

    public void setPickTaskId(Integer pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

    public String getToCellCode() {
        return toCellCode;
    }

    public void setToCellCode(String toCellCode) {
        this.toCellCode = toCellCode;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public Integer getWareId() {
        return wareId;
    }

    @Override
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    @Override
    public Integer getOrganizationId() {
        return organizationId;
    }

    @Override
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }
}
