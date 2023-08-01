package com.deer.wms.ware.task.model.SO;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class PrinterLog {
    private Long id;
    private String type;
    private String driverName;
    private Instant startTime;
    private Instant endTime;
    private String description;
    private List<PrinterLogDetail> detailList;
}
