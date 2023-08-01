package com.deer.wms.ASN.manage.model.upper;

import javax.persistence.Column;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/12/19.
 */
@ToString
public class UpperTaskCriteria extends QueryCriteria {

    private String cellCode;
    private String boxCode;
    private Integer packDetailId;
    private String itemCode;
    private Integer batchId;

    @Column(name = "upper_tactic_detail_id")
    private Integer upperTacticDetailId;
    /**
     * 上架规则编码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    public Integer getUpperTacticDetailId() {
        return upperTacticDetailId;
    }

    public void setUpperTacticDetailId(Integer upperTacticDetailId) {
        this.upperTacticDetailId = upperTacticDetailId;
    }

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }
}
