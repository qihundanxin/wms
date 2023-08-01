package com.deer.wms.inventory.model.move;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.Inventory;

import lombok.ToString;

@ToString
public class Move extends Inventory {

    private String toCellCode;
    private String toBoxCode;
    private String toLpn;
    private Double toQuantity;

    List<String> serialNos;

    public String getToCellCode() {
        return toCellCode;
    }

    public void setToCellCode(String toCellCode) {
        this.toCellCode = toCellCode;
    }

    public String getToBoxCode() {
        return toBoxCode;
    }

    public void setToBoxCode(String toBoxCode) {
        this.toBoxCode = toBoxCode;
    }

    public String getToLpn() {
        return toLpn;
    }

    public void setToLpn(String toLpn) {
        this.toLpn = toLpn;
    }

    public Double getToQuantity() {
        return toQuantity;
    }

    public void setToQuantity(Double toQuantity) {
        this.toQuantity = toQuantity;
    }

    public List<String> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<String> serialNos) {
        this.serialNos = serialNos;
    }

}
