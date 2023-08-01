package com.deer.wms.ASN.manage.model.file;

import lombok.ToString;

@ToString
public class RequestBuyDetailFileDto extends RequestBuyDetailFile {
    private String batchTacticName;
    private String upperTacticName;
    private String packCode;
    private String packName;
    private Integer packDetailId;
    private String packDescribe;
    private Integer itemTypeId;
    private String itemTypeName;
    private String spec;
    private String model;

    public String getBatchTacticName() {
        return batchTacticName;
    }

    public void setBatchTacticName(String batchTacticName) {
        this.batchTacticName = batchTacticName;
    }

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    @Override
    public Integer getPackDetailId() {
        return packDetailId;
    }

    @Override
    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    @Override
    public String getPackDescribe() {
        return packDescribe;
    }

    @Override
    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
