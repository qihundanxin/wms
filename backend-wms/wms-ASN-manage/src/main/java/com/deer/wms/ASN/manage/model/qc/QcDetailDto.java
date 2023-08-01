package com.deer.wms.ASN.manage.model.qc;

import javax.persistence.Column;

import lombok.ToString;

@ToString
public class QcDetailDto extends QcDetail {
    /**
     * 实检数量
     */
    @Column(name = "real_quantity")
    private Double realQuantity;

    /**
     * 合格数量
     */
    @Column(name = "qualified_quantity")
    private Double qualifiedQuantity;

    /**
     * 不合格数量
     */
    @Column(name = "fied_quantity")
    private Double fiedQuantity;

    private Double one;
    private Double two;

    public Double getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Double realQuantity) {
        this.realQuantity = realQuantity;
    }

    public Double getQualifiedQuantity() {
        return qualifiedQuantity;
    }

    public void setQualifiedQuantity(Double qualifiedQuantity) {
        this.qualifiedQuantity = qualifiedQuantity;
    }

    public Double getFiedQuantity() {
        return fiedQuantity;
    }

    public void setFiedQuantity(Double fiedQuantity) {
        this.fiedQuantity = fiedQuantity;
    }

    public Double getOne() {
        return one;
    }

    public void setOne(Double one) {
        this.one = one;
    }

    public Double getTwo() {
        return two;
    }

    public void setTwo(Double two) {
        this.two = two;
    }
}