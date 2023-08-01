package com.deer.wms.ware.task.model.statistics;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 统计参数
 * 
 * @author xuesinuo
 *
 */
@Data
public class StatisticsParam {
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 仓库ID */
    private Integer wareId;
}
