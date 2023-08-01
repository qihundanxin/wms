package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 波次拣货分组
 * @author lixuehe
 * @date 2023/6/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickBatchDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 组编号
     */
    @Column(name = "pick_batch_group_no")
    private String pickBatchGroupNo;

    /**
     * 波次编号
     */
    @Column(name = "pick_batch_no")
    private String pickBatchNo;

    /**
     * 货位
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * sku
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * sku 名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 位置：L ；R
     */
    @Column(name = "position")
    private String position;

    /**
     * 产品数量
     */
    @Column(name = "item_qty")
    private Double itemQty;

    /**
     * 已拣货数量
     */
    @Column(name = "review_qty")
    private Double reviewQty;

    /**
     * 状态：-1-取消；0-正常；1-部分完成；2-完成
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 异常数量
     */
    @Column(name = "exception_qty")
    private Double exceptionQty;

}
