package com.deer.wms.system.manage.model;

import lombok.Data;

/**
 * @ClassName ValidatorCellInfoModel
 * @Description 货位唯一性验证实体类
 * @Author luolin
 * @Date 2023/6/26 10:14
 * @Version 1.0
 **/
@Data
public class ValidatorCellInfoModel {
    private Integer wareId;

    private Integer cellId;

    private String cellCode;

    private String cellName;
}
