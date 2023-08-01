package com.deer.wms.ware.task.model.statistics;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StatByDateDto {
    /** 入库量 */
    private Integer inboundCount;
    /** 出库量 */
    private Integer outboundCount;
    /** 日期 */
    private LocalDate date;
}
