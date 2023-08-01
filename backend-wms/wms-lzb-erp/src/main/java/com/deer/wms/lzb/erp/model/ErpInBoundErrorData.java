/**
 * Copyright (c) 2020 All Rights Reserved.
 */
package com.deer.wms.lzb.erp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.ToString;

/**
 * @author owen
 * @version $$Id: ErpInBoundErrorData, v 0.1 2021/3/22 上午12:00 owen Exp $$
 */
@ToString
public class ErpInBoundErrorData implements Serializable {

    private static final long serialVersionUID = 1L;

    // 必填 / 异常code
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exceptionCode;

    // 必填 // 异常描述
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exceptionDesc;

    //必填 // 异常图片
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] exceptionImg;

    // 必填 // int类型，1:包裹异常;2:商品异常 （目前QC应该都是商品异常）
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer type;

    // 异常商品编号
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String goodsNo;

    // 异常商品数量
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer goodsQuantity;

    // 异常编号
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qcNo;

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public String[] getExceptionImg() {
        return exceptionImg;
    }

    public void setExceptionImg(String[] exceptionImg) {
        this.exceptionImg = exceptionImg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Integer getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(Integer goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public String getQcNo() {
        return qcNo;
    }

    public void setQcNo(String qcNo) {
        this.qcNo = qcNo;
    }
}
