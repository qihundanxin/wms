package com.deer.wms.ware.task.model.Send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/2/23
 *
 * 单个sku 发运
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendOutCriteria {

    /**
     * 出库单号
     */
    private String soBillNo;
    /**
     * sku
     */
    private String itemCode;
    /**
     * 仓库id
     */
    private Integer wareId;
    /**
     * 发运数量
     */
    private Double sendQuantity;

    /**
     * 发运总数
     */
    private Double sendSumQuantity;


}
