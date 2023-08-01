package com.deer.wms.inventory.model.freeze;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 库存冻结master表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "freeze_master")
public class FreezeMaster {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freeze_master_id")
    private Integer freezeMasterId;

    /**
     * 入库单号
     */
    @Column(name = "bill_no")
    private String billNo;

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
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * -1不通过   0-流转中  1-通过  2-初始化  -2保存为草稿
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_use_id")
    private Integer createUseId;

    /**
     * 创建人
     */
    @Column(name = "create_use_name")
    private String createUseName;

    /**
     * 冻结原因描述
     */
    @Column(name = "freeze_describe")
    private String freezeDescribe;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
