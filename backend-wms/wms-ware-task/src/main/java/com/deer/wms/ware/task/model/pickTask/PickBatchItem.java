package com.deer.wms.ware.task.model.pickTask;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "pick_batch_item")
public class PickBatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 组编号 */
    private String pickBatchGroupNo;

    /** 波次编号 */
    private String pickBatchNo;

    /** 拣货波次详情ID */
    private Long pickBatchDetailId;

    /** 货位编码 */
    private String cellCode;

    /** sku */
    private String itemCode;

    /** sku名称 */
    private String itemName;

    /** 位置：L ;R */
    private String position;

    /** 状态：-1-取消；0-正常；1-完成; 2-异常 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 创建人 */
    private Integer createUserId;

    /** 创建人名称 */
    private String createUserName;

    /** 拣货二维码 */
    private String qrCode;

    /** 拣货人 */
    private Integer pickUserId;

    /** 对应的订单号 */
    private String soBillNo;

    /** 配送序号 */
    private Integer deliverIndex;

    /** 质量/重量（kg） */
    private BigDecimal weight;

    /** 磅数 */
    private BigDecimal pound;

    /** 拣货时间 */
    private Date pickTime;
}
