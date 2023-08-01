package com.deer.wms.ware.task.model.statistics;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 首页统计数据请求
 */
@Data
public class TopStatisticsRequest {
    /** 查询天数 */
    private Integer days;
    /** 开始时间（默认当前时间） */
    private LocalDateTime startTime;
    /** 结束时间（低优先级：有天数时，endTime无效） */
    private LocalDateTime endTime;
    /** 仓库ID */
    private Integer wareId;
}
