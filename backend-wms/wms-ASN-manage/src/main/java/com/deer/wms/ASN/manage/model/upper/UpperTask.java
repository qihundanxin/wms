package com.deer.wms.ASN.manage.model.upper;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 上架记录表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "upper_task")
public class UpperTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upper_task_id")
    private Integer upperTaskId;

    /**
     * 物料ID
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 物料类型ID
     */
    @Column(name = "item_type_id")
    private Integer itemTypeId;

    /**
     * 物料类型名称
     */
    @Column(name = "item_type_name")
    private String itemTypeName;

    /**
     * 上架人ID
     */
    @Column(name = "upper_user_id")
    private Integer upperUserId;

    /**
     * 上架人
     */
    @Column(name = "upper_user__name")
    private String upperUserName;

    /**
     * 上架策略编码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 上架策略名称
     */
    @Column(name = "upper_tactic_name")
    private String upperTacticName;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 包装详情ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    // 数据库类型为varbinary(20)二进制数据
    @Column(name = "pack_describe")
    private byte[] packDescribe;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
