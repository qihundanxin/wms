package com.deer.wms.ware.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/6/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassQResponse {

    List<String> missBillNos;
    List<String> unFinishedNos;
}
