package com.deer.wms.inventory.model.Inventory;

import lombok.ToString;

@ToString
public class InventorySumListDto extends Inventory {
    private String packCode;
    private String packName;
    private Double one;
    private Double two;
    private Double three;

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

    public Double getThree() {
        return three;
    }

    public void setThree(Double three) {
        this.three = three;
    }
}
