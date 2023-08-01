package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PickBatchSearchParam extends PickBatch {
    /**
     * 任务时间From
     */
    private Date taskTimeFrom;
    /**
     * 任务时间To
     */
    private Date taskTimeTo;
    /**
     * 创建时间From
     */
    private Date createTimeFrom;
    /**
     * 创建时间To
     */
    private Date createTimeTo;

    /** 排序规则 **/
    private List<String> sorts;

    /** 状态，0未开始 1部分拣货 2 拣货完成 **/
    private Integer status;

}
