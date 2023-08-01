package com.deer.wms.inventory.model.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存回收
 *
 * @author lixuehe
 * @date 2022/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRecoveryCriteria {

    /**
     * 批量库存id
     */
    private List<Integer> inventoryIds;

    /**
     * 回收原因
     */
    private String recoveryReason;

}
