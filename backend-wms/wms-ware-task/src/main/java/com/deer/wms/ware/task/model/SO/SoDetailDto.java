package com.deer.wms.ware.task.model.SO;

import java.util.Date;
import java.util.List;

import lombok.ToString;

import javax.persistence.Column;

@ToString
public class SoDetailDto extends SoDetail {
    private Integer soMasterId;
    private Integer wareId;
    private String wareName;
    private Integer organizationId;
    private String organizationName;
    private Double transRatios;
    private String shop;//店铺名称
    private String shipBillCode;//快递单号

    private List<PackDetails> packDetails;

    private Integer upDismantle;

    private Integer downDismantle;

    private Integer whetherDismantle;

    private Integer clearancePriority;

    private Integer optimalAlgorithm;

    private Integer excessiveAllocation;

    private Integer autoReplenishment;

    private Integer dynamicPickCell;

    private Integer storageChooseEA;

    private Integer storageChooseCS;
    private String turnoverTacticCode;

    /**
     * sku 种类
     */
    private Integer soStructure;

    private String seedingCellCode;
    private Integer x;
    private Integer y;

    /**
     * 订单日序号
     */
    private Integer shopDailyCount;

    public Integer getShopDailyCount() {
        return shopDailyCount;
    }

    public void setShopDailyCount(Integer shopDailyCount) {
        this.shopDailyCount = shopDailyCount;
    }

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 0-初始化 1-部分预分配 2-预分配完成 3-部分分配 4-分配完成 5-部分拣货 6-拣货完成 7-复核完成 8-完全发运 9-取消 10-完成 11-已合并 12 部分发运
     */
    private Integer masterState;

    /**
     * 发运数量
     */
    private Double sendQuantity;

    /**
     * 发运时间
     */
    private Date sendTime;

    /**
     * 发运人
     */
    private String sendUserName;

    public Double getSendQuantity() {
        return sendQuantity;
    }

    public void setSendQuantity(Double sendQuantity) {
        this.sendQuantity = sendQuantity;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public Integer getMasterState() {
        return masterState;
    }

    public void setMasterState(Integer masterState) {
        this.masterState = masterState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }

    public String getSeedingCellCode() {
        return seedingCellCode;
    }

    public void setSeedingCellCode(String seedingCellCode) {
        this.seedingCellCode = seedingCellCode;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getSoStructure() {
        return soStructure;
    }

    public void setSoStructure(Integer soStructure) {
        this.soStructure = soStructure;
    }

    public String getTurnoverTacticCode() {
        return turnoverTacticCode;
    }

    public void setTurnoverTacticCode(String turnoverTacticCode) {
        this.turnoverTacticCode = turnoverTacticCode;
    }

    public Integer getDownDismantle() {
        return downDismantle;
    }

    public void setDownDismantle(Integer downDismantle) {
        this.downDismantle = downDismantle;
    }

    public Integer getWhetherDismantle() {
        return whetherDismantle;
    }

    public void setWhetherDismantle(Integer whetherDismantle) {
        this.whetherDismantle = whetherDismantle;
    }

    public Integer getClearancePriority() {
        return clearancePriority;
    }

    public void setClearancePriority(Integer clearancePriority) {
        this.clearancePriority = clearancePriority;
    }

    public Integer getOptimalAlgorithm() {
        return optimalAlgorithm;
    }

    public void setOptimalAlgorithm(Integer optimalAlgorithm) {
        this.optimalAlgorithm = optimalAlgorithm;
    }

    public Integer getExcessiveAllocation() {
        return excessiveAllocation;
    }

    public void setExcessiveAllocation(Integer excessiveAllocation) {
        this.excessiveAllocation = excessiveAllocation;
    }

    public Integer getAutoReplenishment() {
        return autoReplenishment;
    }

    public void setAutoReplenishment(Integer autoReplenishment) {
        this.autoReplenishment = autoReplenishment;
    }

    public Integer getDynamicPickCell() {
        return dynamicPickCell;
    }

    public void setDynamicPickCell(Integer dynamicPickCell) {
        this.dynamicPickCell = dynamicPickCell;
    }

    public Integer getStorageChooseEA() {
        return storageChooseEA;
    }

    public void setStorageChooseEA(Integer storageChooseEA) {
        this.storageChooseEA = storageChooseEA;
    }

    public Integer getStorageChooseCS() {
        return storageChooseCS;
    }

    public void setStorageChooseCS(Integer storageChooseCS) {
        this.storageChooseCS = storageChooseCS;
    }

    public Integer getUpDismantle() {
        return upDismantle;
    }

    public void setUpDismantle(Integer upDismantle) {
        this.upDismantle = upDismantle;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Double getTransRatios() {
        return transRatios;
    }

    public void setTransRatios(Double transRatios) {
        this.transRatios = transRatios;
    }

    public List<PackDetails> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetails> packDetails) {
        this.packDetails = packDetails;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}
