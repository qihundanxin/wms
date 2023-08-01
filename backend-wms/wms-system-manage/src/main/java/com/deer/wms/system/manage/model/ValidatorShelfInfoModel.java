package com.deer.wms.system.manage.model;

import lombok.Data;

/**
 * @ClassName ValidatorShelfInfoModel
 * @Description 货位组唯一性验证实体类
 * @Author luolin
 * @Date 2023/6/25 14:28
 * @Version 1.0
 **/
@Data
public class ValidatorShelfInfoModel {

    private Integer wareId;

    private Integer shelfInfoId;

    private String shelfCode;

    private String shelfName;
}
