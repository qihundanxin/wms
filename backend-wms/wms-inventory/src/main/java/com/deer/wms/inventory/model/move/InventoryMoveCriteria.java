package com.deer.wms.inventory.model.move;

import java.util.List;

import lombok.ToString;

/**
 * Created by guo on 2019/12/27.
 */
@ToString
public class InventoryMoveCriteria extends InventoryMove {
    private List<String> keyWordList;

    public List<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }
}
