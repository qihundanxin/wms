package com.deer.wms.inventory.model.Inventory;

import java.util.List;

import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.model.Pack.PackDetail2;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryDto extends Inventory {
    private Integer itemTypeId;
    private String itemTypeName;
    private String spec;
    private String model;
    private String packCode;
    private String packName;
    private Double sumQuantity;
    /**
     * 可拣库存合计：拣货位数量
     */
    private Double canPickQuantity;
    private List<ItemUpc> upcs;
    private List<PackDetail2> packDetails;
}
