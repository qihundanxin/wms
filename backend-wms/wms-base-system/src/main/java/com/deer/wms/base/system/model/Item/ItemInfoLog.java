package com.deer.wms.base.system.model.Item;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 物料信息日志表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_info_log")
public class ItemInfoLog {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 物料id
     */
    // 数据库类型为String
    @Column(name = "item_info_id")
    private Integer itemInfoId;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 变更前的JSON数据
     */
    @Column(name = "before_data")
    private String beforeData;

    /**
     * JSON数据
     */
    @Column(name = "data")
    private String data;

    /**
     * 日志类型1.新增 2.修改 3.审核 4.删除 5.完成
     */
    @Column(name = "operation_type")
    private Integer operationType;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
