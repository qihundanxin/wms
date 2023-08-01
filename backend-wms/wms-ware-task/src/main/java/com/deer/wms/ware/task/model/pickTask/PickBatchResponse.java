package com.deer.wms.ware.task.model.pickTask;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 波次算法生成的结果（豆芽接口的返回值）
 * 
 * @author lixuehe
 * @date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickBatchResponse {

    @JsonProperty("pick_batch_group_no")
    private String pickBatchGroupNo;

    @JsonProperty("total_qty")
    private Double totalQty;

    @JsonProperty("pq_order_lst")
    private List<String> pqOrderLst;

    @JsonProperty("total_order_count")
    private Integer totalOrderCount;

    private List<PickBatchListResponse> pickBatchList;

}
