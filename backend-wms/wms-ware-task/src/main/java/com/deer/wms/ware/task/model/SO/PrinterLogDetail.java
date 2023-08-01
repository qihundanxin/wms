package com.deer.wms.ware.task.model.SO;

import java.time.Instant;

import lombok.Data;

@Data
public class PrinterLogDetail {
    private Long id;
    private String billNo;
    private Instant startTime;
    private Instant endTime;
    private Integer index;
    private String description;
}
