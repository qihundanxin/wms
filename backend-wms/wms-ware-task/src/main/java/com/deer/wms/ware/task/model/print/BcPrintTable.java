package com.deer.wms.ware.task.model.print;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/5/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BcPrintTable {

    /**
     * 货位
     */
    private String cellCode;

    /**
     * sku 名称
     */
    private String itemName;

    /**
     * left数量
     */
    private Integer lQty;

    /**
     * right数量
     */
    private Integer rQty;
}
