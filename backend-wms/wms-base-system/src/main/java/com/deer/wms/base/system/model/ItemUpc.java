package com.deer.wms.base.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 物料upc
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_upc")
public class ItemUpc {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_upc_id")
    private Integer itemUpcId;

    /**
     * upc
     */
    @Column(name = "upc")
    private String upc;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料id
     */
    @Column(name = "item_id")
    private Integer itemId;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

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
    private Integer createUserName;

    /**
     * 0-不启用  1-启用
     */
    @Column(name = "state")
    private Integer state;
}
