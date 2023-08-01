/**
 * Copyright (c) 2020 All Rights Reserved.
 */
package com.deer.wms.lzb.erp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.ToString;

/**
 * @author owen
 * @version $$Id: ErpInBoundGoodsData, v 0.1 2021/3/22 上午12:00 owen Exp $$
 */
@ToString
public class ErpInBoundGoodsData implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String goodsNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int goodsQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int goodsBadQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qcNo;

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(int goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public int getGoodsBadQuantity() {
        return goodsBadQuantity;
    }

    public void setGoodsBadQuantity(int goodsBadQuantity) {
        this.goodsBadQuantity = goodsBadQuantity;
    }

    public String getQcNo() {
        return qcNo;
    }

    public void setQcNo(String qcNo) {
        this.qcNo = qcNo;
    }
}
