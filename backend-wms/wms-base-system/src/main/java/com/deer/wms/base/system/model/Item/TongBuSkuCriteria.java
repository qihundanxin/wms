package com.deer.wms.base.system.model.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/2/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TongBuSkuCriteria {
    //货主编码
    private String orgCode;
    //商店id
    private String shopId;
}
