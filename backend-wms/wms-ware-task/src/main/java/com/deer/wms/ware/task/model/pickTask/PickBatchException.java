package com.deer.wms.ware.task.model.pickTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author luolin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickBatchException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 波次号 **/
    @Column(name = "pick_batch_no")
    private String pickBatchNo;

    /** 货位编号 **/
    @Column(name = "cell_code")
    private String cellCode;

    /** sku **/
    @Column(name = "item_code")
    private String itemCode;

    /** 图片地址 **/
    @Column(name = "img_url")
    private String imgUrl;

    /** 异常数量 **/
    @Column(name = "exception_qty")
    private Double exceptionQty;

    /** 异常类型 0其他 1有货损 2数量不够 3货物找不到 4货位找不到 5商品扫码不正确 **/
    @Column(name = "exception_type")
    private Integer exceptionType;

    /** 描述 **/
    @Column(name = "description")
    private String description;

    /** 处理状态 0-未处理 1-处理中 2-处理完成 **/
    @Column(name = "status")
    private Integer status;

    /** 上报人 **/
    @Column(name = "report_user_id")
    private Integer reportUserId;

    /** 上报时间 **/
    @Column(name = "report_time")
    private Date reportTime;

    /** 处理人 **/
    @Column(name = "handle_user_id")
    private Integer handleUserId;

    /** 处理时间 **/
    @Column(name = "handle_time")
    private Date handleTime;
}
