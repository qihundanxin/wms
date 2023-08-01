package com.deer.wms.ware.task.model.SO;

import java.util.List;

import com.deer.wms.base.system.enums.SendState;
import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/02/02.
 */
@ToString
public class SoMasterCriteria extends QueryCriteria {

    private Integer soStructure;
    private Integer soMasterId;
    private String billNo;
    private Integer state;
    private String sendState;
    private Integer gtState;

    private Integer customerId;
    private Integer createUserId;
    private Integer billSource;
    private Integer insertType;
    private Integer receivingAddressId;
    private Integer carrierId;
    private Integer soType;
    private String mergeNo;
    private List<String> billNos;

    private String startCreateTime;
    private String endCreateTime;
    private String startDeliveryTime;
    private String endDeliveryTime;
    private String startExpectTimeFm;
    private String endExpectTimeFm;
    private String startExpectTimeTo;
    private String endExpectTimeTo;

    private Integer wavePriority;
    private Integer waveDistributeTactic;
    private Integer waveDeliveryType;
    private Integer waveRoute;
    private Integer waveIsSingle;
    private String waveSoDeadLine;
    private Integer waveAcceptorCount;
    private Integer waveItemCount;
    private Double waveMinSkuQuantity;
    private Double waveSizeLimit;
    private Double waveWeightLimit;
    private Double waveQuantityLimit;
    private Integer waveWaveCount;
    private Integer waveCrossArea;
    private Integer waveWholeBox;
    private Integer waveSoSingleArea;
    private String waveExtraSql;
    private String allotBillNo;
    private String shipBillNo;

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState;
    }

    public Integer getGtState() {
        return gtState;
    }

    public void setGtState(Integer gtState) {
        this.gtState = gtState;
    }

    public String getShipBillNo() {
        return shipBillNo;
    }

    public void setShipBillNo(String shipBillNo) {
        this.shipBillNo = shipBillNo;
    }

    public Integer getSoStructure() {
        return soStructure;
    }

    public void setSoStructure(Integer soStructure) {
        this.soStructure = soStructure;
    }

    public Integer getWavePriority() {
        return wavePriority;
    }

    public void setWavePriority(Integer wavePriority) {
        this.wavePriority = wavePriority;
    }

    public Integer getWaveDistributeTactic() {
        return waveDistributeTactic;
    }

    public void setWaveDistributeTactic(Integer waveDistributeTactic) {
        this.waveDistributeTactic = waveDistributeTactic;
    }

    public Integer getWaveDeliveryType() {
        return waveDeliveryType;
    }

    public void setWaveDeliveryType(Integer waveDeliveryType) {
        this.waveDeliveryType = waveDeliveryType;
    }

    public Integer getWaveRoute() {
        return waveRoute;
    }

    public void setWaveRoute(Integer waveRoute) {
        this.waveRoute = waveRoute;
    }

    public Integer getWaveIsSingle() {
        return waveIsSingle;
    }

    public void setWaveIsSingle(Integer waveIsSingle) {
        this.waveIsSingle = waveIsSingle;
    }

    public String getWaveSoDeadLine() {
        return waveSoDeadLine;
    }

    public void setWaveSoDeadLine(String waveSoDeadLine) {
        this.waveSoDeadLine = waveSoDeadLine;
    }

    public Integer getWaveAcceptorCount() {
        return waveAcceptorCount;
    }

    public void setWaveAcceptorCount(Integer waveAcceptorCount) {
        this.waveAcceptorCount = waveAcceptorCount;
    }

    public Integer getWaveItemCount() {
        return waveItemCount;
    }

    public void setWaveItemCount(Integer waveItemCount) {
        this.waveItemCount = waveItemCount;
    }

    public Double getWaveMinSkuQuantity() {
        return waveMinSkuQuantity;
    }

    public void setWaveMinSkuQuantity(Double waveMinSkuQuantity) {
        this.waveMinSkuQuantity = waveMinSkuQuantity;
    }

    public Double getWaveSizeLimit() {
        return waveSizeLimit;
    }

    public void setWaveSizeLimit(Double waveSizeLimit) {
        this.waveSizeLimit = waveSizeLimit;
    }

    public Double getWaveWeightLimit() {
        return waveWeightLimit;
    }

    public void setWaveWeightLimit(Double waveWeightLimit) {
        this.waveWeightLimit = waveWeightLimit;
    }

    public Double getWaveQuantityLimit() {
        return waveQuantityLimit;
    }

    public void setWaveQuantityLimit(Double waveQuantityLimit) {
        this.waveQuantityLimit = waveQuantityLimit;
    }

    public Integer getWaveWaveCount() {
        return waveWaveCount;
    }

    public void setWaveWaveCount(Integer waveWaveCount) {
        this.waveWaveCount = waveWaveCount;
    }

    public Integer getWaveCrossArea() {
        return waveCrossArea;
    }

    public void setWaveCrossArea(Integer waveCrossArea) {
        this.waveCrossArea = waveCrossArea;
    }

    public Integer getWaveWholeBox() {
        return waveWholeBox;
    }

    public void setWaveWholeBox(Integer waveWholeBox) {
        this.waveWholeBox = waveWholeBox;
    }

    public Integer getWaveSoSingleArea() {
        return waveSoSingleArea;
    }

    public void setWaveSoSingleArea(Integer waveSoSingleArea) {
        this.waveSoSingleArea = waveSoSingleArea;
    }

    public String getWaveExtraSql() {
        return waveExtraSql;
    }

    public void setWaveExtraSql(String waveExtraSql) {
        this.waveExtraSql = waveExtraSql;
    }

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getReceivingAddressId() {
        return receivingAddressId;
    }

    public void setReceivingAddressId(Integer receivingAddressId) {
        this.receivingAddressId = receivingAddressId;
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

    public String getMergeNo() {
        return mergeNo;
    }

    public void setMergeNo(String mergeNo) {
        this.mergeNo = mergeNo;
    }

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
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

    public String getStartDeliveryTime() {
        return startDeliveryTime;
    }

    public void setStartDeliveryTime(String startDeliveryTime) {
        this.startDeliveryTime = startDeliveryTime;
    }

    public String getEndDeliveryTime() {
        return endDeliveryTime;
    }

    public void setEndDeliveryTime(String endDeliveryTime) {
        this.endDeliveryTime = endDeliveryTime;
    }

    public String getStartExpectTimeFm() {
        return startExpectTimeFm;
    }

    public void setStartExpectTimeFm(String startExpectTimeFm) {
        this.startExpectTimeFm = startExpectTimeFm;
    }

    public String getEndExpectTimeFm() {
        return endExpectTimeFm;
    }

    public void setEndExpectTimeFm(String endExpectTimeFm) {
        this.endExpectTimeFm = endExpectTimeFm;
    }

    public String getStartExpectTimeTo() {
        return startExpectTimeTo;
    }

    public void setStartExpectTimeTo(String startExpectTimeTo) {
        this.startExpectTimeTo = startExpectTimeTo;
    }

    public String getEndExpectTimeTo() {
        return endExpectTimeTo;
    }

    public void setEndExpectTimeTo(String endExpectTimeTo) {
        this.endExpectTimeTo = endExpectTimeTo;
    }

    public String getAllotBillNo() {
        return allotBillNo;
    }

    public void setAllotBillNo(String allotBillNo) {
        this.allotBillNo = allotBillNo;
    }
}
