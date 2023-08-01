package com.deer.wms.base.system.model.ItemCombined;

import java.util.List;

import lombok.ToString;

@ToString
public class ItemCombinedInsert {
    private ItemCombined itemCombined;
    private List<ItemComponent> itemComponents;

    public ItemCombined getItemCombined() {
        return itemCombined;
    }

    public void setItemCombined(ItemCombined itemCombined) {
        this.itemCombined = itemCombined;
    }

    public List<ItemComponent> getItemComponents() {
        return itemComponents;
    }

    public void setItemComponents(List<ItemComponent> itemComponents) {
        this.itemComponents = itemComponents;
    }
}
