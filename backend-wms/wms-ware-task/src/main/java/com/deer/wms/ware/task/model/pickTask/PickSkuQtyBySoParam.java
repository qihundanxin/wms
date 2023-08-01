package com.deer.wms.ware.task.model.pickTask;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.deer.wms.project.root.validation.annotation.MethodValid4Type;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 2023-05-23新的拣货方式：
 * 
 * <pre>
 * 1. PDA层面的操作支持跨仓
 * 2. 支持对拣货任务进行部分完成
 * 3. 不以拣货任务为单位，以SO+SKU为单位。提交上来数量，通过SO+SKU确定若干拣货任务，进行依次提交完成，超出数量时提示，否则都是可以正常推进的。
 * </pre>
 * 
 * @author xuesinuo
 *
 */
@Data
@MethodValid4Type(message = "缺少必填信息：(soBillNo + itemCode) or qrCode", method = "checkKey")
public class PickSkuQtyBySoParam {
    /**
     * 出库单号
     */
    private String soBillNo;
    /**
     * SKU
     */
    private String itemCode;
    /**
     * 拣货扫描的二维码
     * 
     * 从二维码中解析数据，从而不再需要传单号、SKU
     */
    private String qrCode;
    /**
     * 拣货数量
     */
    @NotNull
    @Min(0)
    private Double reviewQuantity;
    /**
     * from货位号
     */
    @NotNull
    private String cellCode;
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

    /** 
     * 验证关键必填信息
     * @return 通过验证
     */
    public boolean checkKey(){
        return StrUtil.isNotBlank(this.soBillNo) && StrUtil.isNotBlank(this.itemCode) || StrUtil.isNotBlank(this.qrCode);
    }
}
