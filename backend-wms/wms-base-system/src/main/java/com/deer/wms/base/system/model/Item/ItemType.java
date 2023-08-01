package com.deer.wms.base.system.model.Item;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 物料类型表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_type")
public class ItemType {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    /**
     * 物料类型ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_type_id")
    private Integer itemTypeId;

    /**
     * 物料类型编码
     */
    @Column(name = "item_type_code")
    private String itemTypeCode;

    /**
     * 物料类型名称
     */
    @Column(name = "item_type_name")
    private String itemTypeName;

    /**
     * 物料类型状态
     */
    @Column(name = "item_type_state")
    private Integer itemTypeState;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上一级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 货主ID -1 通用种类  货主专属 ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 批次策略代码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 上架策略代码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 质检策略编码
     */
    @Column(name = "qc_tactic_code")
    private String qcTacticCode;

    /**
     * 拣货策略
     */
    @Column(name = "pick_tactic_code")
    private String pickTacticCode;
}
