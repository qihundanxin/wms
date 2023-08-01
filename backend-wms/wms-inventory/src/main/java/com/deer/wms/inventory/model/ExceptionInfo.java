package com.deer.wms.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 异常信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "exception_info")
public class ExceptionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id")
    private Integer exceptionId;

    /**
     * 101-验收少件 102-验收多件 201-拣货少件 
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 异常信息
     */
    @Column(name = "message")
    private String message;

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
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 包装id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;
}
