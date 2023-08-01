package com.deer.wms.system.manage.model.organization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 货主信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "organization")
public class Organization {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 联系人
     */
    @Column(name = "link_man")
    private String linkMan;

    /**
     * 联系方式
     */
    @Column(name = "link_phone")
    private String linkPhone;

    /**
     * 货主编码
     */
    @Column(name = "organization_code")
    private String organizationCode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 货主状态：enable=启用；disable=停用；默认启用
     */
    @Column(name = "organization_status")
    private String organizationStatus;

    /**
     * 信息状态：normal=正常的；invalid=无效的；deleted=已删除；默认正常的
     */
    @Column(name = "state")
    private String state;

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
     * SO合并策略编码
     */
    @Column(name = "so_merge_tactic_code")
    private String soMergeTacticCode;

    /**
     * 拣货策略编码
     */
    @Column(name = "pick_tactic_code")
    private String pickTacticCode;
}
