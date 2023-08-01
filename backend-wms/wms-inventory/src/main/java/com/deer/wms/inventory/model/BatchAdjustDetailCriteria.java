package com.deer.wms.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量库存调整
 * @author lixuehe
 * @date 2023/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchAdjustDetailCriteria {

    /**
     * 仓库id
     */
    private Integer wareId;

    //库存调整列表
    private List<BatchAdjustDetail> adjustDetails;
}
