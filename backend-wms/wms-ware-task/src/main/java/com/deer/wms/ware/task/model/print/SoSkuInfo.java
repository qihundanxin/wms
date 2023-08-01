package com.deer.wms.ware.task.model.print;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 豆芽接口返回的波次Item
 * 
 * @author lixuehe
 * @date 2023/5/23
 */
@Data
@AllArgsConstructor
public class SoSkuInfo {

    /**
     * 出库单号
     */
    @JsonProperty(value = "bill_no")
    private String billNo;

    /**
     * sku
     */
    @JsonProperty(value = "item_code")
    private String itemCode;

    /**
     * 名称
     */
    @JsonProperty(value = "item_name")
    private String itemName;

    /**
     * 位置
     */
    private String position;

    /**
     * 货位
     */
    @JsonProperty(value = "cell_code")
    private String cellCode;

    /**
     * sku数量
     */
    @JsonProperty(value = "order_quantity")
    private Integer orderQuantity;

    /**
     * sku拣货数量
     */
    @JsonProperty(value = "pick_quantity")
    private Integer pickQuantity;

    /**
     * 配送顺序
     */
    @JsonProperty(value = "index")
    private Integer orderIndex;
}
