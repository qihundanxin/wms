package com.deer.wms.ASN.manage.model.buy;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class RequestBuyDetailDto extends RequestBuyDetail {
    private String batchTacticName;
    private String upperTacticName;
    private String packCode;
    private String packName;
    private Integer itemTypeId;
    private String itemTypeName;
    private String spec;
    private String model;
    private List<PackDetails> packDetails;
    private Boolean priceInSetNull;
    private Boolean taxRateInSetNull;

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

    public List<PackDetails> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetails> packDetails) {
        this.packDetails = packDetails;
    }
}
