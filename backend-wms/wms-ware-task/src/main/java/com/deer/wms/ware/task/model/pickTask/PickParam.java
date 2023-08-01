package com.deer.wms.ware.task.model.pickTask;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 确认拣货的参数
 * 
 * @author xuesinuo
 *
 */
@Data
public class PickParam {
    /**
     * 拣货任务ID
     */
    @NotNull
    private Integer pickTaskId;
    /**
     * 拣货数量
     */
    @NotNull
    @Min(0)
    private Double reviewQuantity;
    /**
     * 拣货时扫描的二维码
     */
    private String qrCode;
    /**
     * 容器号
     */
    private String boxCode;
    /**
     * 拣出到：目标货位号
     */
    private String toCellCode;
    /**
     * 操作人ID
     */
    private Integer userId;
    /**
     * 操作人Name，最佳实践：获取当前登录人。（如果空，快照将不存储userName）
     */
    private String userName;
    /**
     * 备注
     */
    private String memo;
}
