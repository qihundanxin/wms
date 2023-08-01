package com.deer.wms.inventory.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;
import org.checkerframework.common.reflection.qual.ClassBound;

/**
 * 库存master信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_adjust")
public class InventoryAdjust {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_adjust_id")
    private Integer inventoryAdjustId;

    /**
     * 库存调整单编号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 货主Id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 盘点单id
     */
    @Column(name = "stock_take_master_id")
    private Integer stockTakeMasterId;

    /**
     * 调整原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 调整原因描述
     */
    @Column(name = "reason_description")
    private String reasonDescription;

    /**
     * 仓库Id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库编码
     */
    @Column(name = "ware_code")
    private String wareCode;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 创建人id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 创建人
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 类型 1-盘点生成库存调整单 2-手动新增库存调整单 3-自动选择库存生成
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 类型
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 审核状态 -1不通过  0-流转中 1-已通过 2-待审核
     */
    @Column(name = "case_state")
    private Integer caseState;

    /**
     * 允许批量调整库存：0-不允许，1-允许：
     */
    @Column(name="allow_batch")
    private Integer allowBatch;
}
