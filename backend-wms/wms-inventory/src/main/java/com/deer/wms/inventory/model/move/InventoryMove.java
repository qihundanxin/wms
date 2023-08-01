package com.deer.wms.inventory.model.move;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 库存转移
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_move")
public class InventoryMove {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_move_id")
    private Integer inventoryMoveId;

    /**
     * 移库单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 审核状态  -1不通过   0-流转中  1-通过  2-初始化
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
