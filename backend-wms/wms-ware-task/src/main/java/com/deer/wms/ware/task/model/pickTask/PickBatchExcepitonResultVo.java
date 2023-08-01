package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;

/**
 * @author luolin
 **/
@Data
public class PickBatchExcepitonResultVo {
    private String itemCode;
    private String itemName;
    private String cellCode;
    private Double exceptionQty;
    private Double noHandleQty;
    private Double handleQty;
    private String reportTime;
}
