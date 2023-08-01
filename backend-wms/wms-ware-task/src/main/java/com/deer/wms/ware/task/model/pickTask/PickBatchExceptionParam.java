package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;

import java.util.Date;

/**
 * @author luolin
 **/
@Data
public class PickBatchExceptionParam extends PickBatchException {
    private Date startDate;
    private Date endDate;
}
