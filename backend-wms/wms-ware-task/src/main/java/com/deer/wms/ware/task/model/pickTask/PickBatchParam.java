package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName PickBatchParam
 * @Description 波次拣货
 * @Author luolin
 * @Date 2023/7/11 16:22
 * @Version 1.0
 **/
@Data
public class PickBatchParam extends PickSkuQtyBySoParam {

    /**
     * 波次号
     */
    private String pickBatchNo;

    /**
     * 是否批发单
     */
    private Boolean wholesale;

    /**
     * 质量/重量（kg）
     **/
    private BigDecimal weight;

    /**
     * 磅数
     **/
    private BigDecimal pound;
}
