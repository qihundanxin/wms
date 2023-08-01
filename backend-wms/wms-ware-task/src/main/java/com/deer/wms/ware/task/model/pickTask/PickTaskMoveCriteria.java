package com.deer.wms.ware.task.model.pickTask;

import com.deer.wms.project.root.core.service.QueryCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 库存移动参数
 * @author lixuehe
 * @date 2023/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickTaskMoveCriteria extends QueryCriteria {

    /**
     * 货位编码
     */
    private String cellCode;

    /**
     * 拣货任务id
     */
    private List<Integer> pickTaskIds;
}
