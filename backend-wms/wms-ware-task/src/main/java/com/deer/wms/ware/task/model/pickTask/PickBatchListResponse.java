package com.deer.wms.ware.task.model.pickTask;

import java.util.List;

import com.deer.wms.ware.task.model.print.SoSkuInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 豆芽接口返回的波次
 * 
 * @author lixuehe
 * @date 2023/5/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickBatchListResponse {
    private String pickBatchId;
    List<SoSkuInfo> itemList;
//    private Double item_qty;
//    private List<String> so_ids;
    private String container_type;
    private Boolean wholesale;// 是否为批发单
}
