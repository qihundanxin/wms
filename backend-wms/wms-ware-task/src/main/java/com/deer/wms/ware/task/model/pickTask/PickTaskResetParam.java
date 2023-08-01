package com.deer.wms.ware.task.model.pickTask;

import java.util.List;

import lombok.Data;

/**
 * 拣货任务重新分配入参
 * 
 * @author xuesinuo
 *
 */
@Data
public class PickTaskResetParam {
    private Integer userId;
    private String username;
    private List<Integer> pickTaskIds;
}
