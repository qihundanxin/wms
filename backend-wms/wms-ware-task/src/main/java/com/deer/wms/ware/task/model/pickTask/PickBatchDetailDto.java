package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PickBatchDetailDto extends PickBatchDetail {
    // 商品图片
    private String imgUrl;
    // 需要称重
    private Boolean weighting;
    // 散件（需要拆包装）
    private Boolean unpacking;
}
