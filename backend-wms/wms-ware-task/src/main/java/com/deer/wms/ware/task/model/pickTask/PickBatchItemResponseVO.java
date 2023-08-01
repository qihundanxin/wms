package com.deer.wms.ware.task.model.pickTask;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PickBatchItemResponseVO {
    private Long id;
    // 波次组编号
    private String pickBatchGroupNo;
    // 波次编号
    private String pickBatchNo;
    // 0未开始 1部分拣货 2 拣货完成
    private Integer status;
    // 创建时间
    private String createTime;
    // 波次包含N种SKU
    private Integer skuNum;
    // 商品总数量
    private Double skuQty;
    // 未拣货数量
    private Double unPickedQty;
    // 已拣货数量
    private Double pickedQty;
    // 异常数量
    private Double exceptionQty;
    // 司机姓名
    private String driverName;
    // 拣货人ID，当前波次任务归属的人员ID
    private Integer pickUserId;
    // 本波次涉及的货货位数
    private Integer cellNum;
    // 任务分配、领取时间
    private Date taskTime;
    // 最后拣货时间
    private Date pickTime;
    // 干货数量
    private Double scbSkuQty;
    // 冷货数量
    private Double coldSkuQty;
    // 批发单
    private Boolean wholesale;
    //item 详情
    private List<PickBatchItemDto> batchItemList;
}
