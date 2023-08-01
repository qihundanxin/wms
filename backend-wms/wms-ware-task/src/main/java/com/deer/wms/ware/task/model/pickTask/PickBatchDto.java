package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PickBatchDto extends PickBatch {

    /**
     * 波次包含N种SKU
     */
    private Integer skuNum;
    /**
     * 商品总数量
     */
    private Double skuQty;
    /**
     * 未拣货数量
     */
    private Double unPickedQty;
    /**
     * 已拣货数量
     */
    private Double pickedQty;
    /**
     * 异常数量
     */
    private Double exceptionQty;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 本波次涉及的货位数
     */
    private Integer cellNum;
    /**
     * 干货数量
     */
    private Double scbSkuQty;
    /**
     * 冷货数量
     */
    private Double coldSkuQty;
}
