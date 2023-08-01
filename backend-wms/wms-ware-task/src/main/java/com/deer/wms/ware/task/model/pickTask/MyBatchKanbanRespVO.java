package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;

import java.util.Date;

@Data
public class MyBatchKanbanRespVO {
    // 查询条件中的时间
    private Date datetime;
    // 今日波次总数
    private Integer todaySum;
    // 当日未完成波次
    private Integer todayUnfinished;
    // 本人今日波次
    private Integer myTodaySum;
    // 本人今日已完成波次
    private Integer myTodayFinished;
    // 本人今日已经拣货数量
    private Double myTodayPickedQty;
}