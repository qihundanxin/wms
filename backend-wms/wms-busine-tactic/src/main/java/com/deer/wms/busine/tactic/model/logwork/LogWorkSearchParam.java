package com.deer.wms.busine.tactic.model.logwork;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LogWorkSearchParam extends LogWork {
    private LocalDateTime beginFrom;
    private LocalDateTime beginTo;
    private Long timeMin;
    private Long timeMax;
}
