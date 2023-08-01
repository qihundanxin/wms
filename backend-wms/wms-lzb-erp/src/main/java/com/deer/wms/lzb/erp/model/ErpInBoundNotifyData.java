/**
 * Copyright (c) 2020 All Rights Reserved.
 */
package com.deer.wms.lzb.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.ToString;

/**
 * @author owen
 * @version $$Id: ErpInBoundNotifyData, v 0.1 2021/3/21 下午11:58 owen Exp $$
 */
@ToString
public class ErpInBoundNotifyData implements Serializable {

    private static final long serialVersionUID = 1L;

    // 序列号 为了防止重复请求给ERP，同一个序列号 ERP只会入库一次
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serialNo;

    // 必填 // wms的入库单号 (创建入库单的时候wms生成)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wmsNo;

    //  面单条码，此处填中国快递单号 (创建入库单的时候ERP传给wms)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String labelBarcode;

    // 必填 // erp采购系统的订单号 (创建入库单的时候ERP传给wms)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderNo;

    // 货主编号
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orgCode;

    // 必填 // 入库重量(单位g, 最多两位小数)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal weight;

    // 长度(单位cm,最多两位小数)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal length;

    // 宽度(cm,最多两位小数)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal width;

    // 高度(cm,最多两位小数)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal height;

    // "2020-11-14 00:00:00", // 操作时间
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operationTime;

    // 操作人
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operationUser;

    // 操作是否成功
    private Boolean success;

    // 入库商品明细
    private List<ErpInBoundGoodsData> details;

    // 错误信息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErpInBoundErrorData> errors;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getWmsNo() {
        return wmsNo;
    }

    public void setWmsNo(String wmsNo) {
        this.wmsNo = wmsNo;
    }

    public String getLabelBarcode() {
        return labelBarcode;
    }

    public void setLabelBarcode(String labelBarcode) {
        this.labelBarcode = labelBarcode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ErpInBoundGoodsData> getDetails() {
        return details;
    }

    public void setDetails(List<ErpInBoundGoodsData> details) {
        this.details = details;
    }

    public List<ErpInBoundErrorData> getErrors() {
        return errors;
    }

    public void setErrors(List<ErpInBoundErrorData> errors) {
        this.errors = errors;
    }
}
