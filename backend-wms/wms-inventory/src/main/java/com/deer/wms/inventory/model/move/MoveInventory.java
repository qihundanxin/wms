package com.deer.wms.inventory.model.move;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.InventoryTransact;

import lombok.ToString;

@ToString
public class MoveInventory extends InventoryTransact {

    private InventoryMove inventoryMove;
    private List<InventoryMoveDetail> inventoryMoveDetails;
    private Integer insertType;

    public InventoryMove getInventoryMove() {
        return inventoryMove;
    }

    public void setInventoryMove(InventoryMove inventoryMove) {
        this.inventoryMove = inventoryMove;
    }

    public List<InventoryMoveDetail> getInventoryMoveDetails() {
        return inventoryMoveDetails;
    }

    public void setInventoryMoveDetails(List<InventoryMoveDetail> inventoryMoveDetails) {
        this.inventoryMoveDetails = inventoryMoveDetails;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }
}
