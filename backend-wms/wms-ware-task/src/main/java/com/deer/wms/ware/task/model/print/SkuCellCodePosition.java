package com.deer.wms.ware.task.model.print;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/5/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkuCellCodePosition {
    /**
     * sku
     */
    private String itemCode;

    /**
     * 货位
     */
    private String cellCode;

    /**
     * 位置
     */
    private String position;
}
