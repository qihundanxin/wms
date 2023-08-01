package com.deer.wms.inventory.model;

import com.deer.wms.project.root.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 批量库存调整detail表
 * @author lixuehe
 * @date 2023/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchAdjustDetail {


    /**
     * 货位
     */
    private String cellCode;


    /**
     * 物料编码
     */
    private String itemCode;


    /**
     * 库存调整数量
     */
    private Double adjustQuantity;

}

