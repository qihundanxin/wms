package com.deer.wms.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 货权转移
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "iv_trans_org_master")
public class IvTransOrgMaster {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 转移单号
     */
    @Column(name = "code")
    private String code;

    /**
     * 转出货主id
     */
    @Column(name = "from_org_id")
    private Integer fromOrgId;

    /**
     * 转出货主名称
     */
    @Column(name = "from_org_name")
    private String fromOrgName;

    /**
     * 转出货主id
     */
    @Column(name = "to_org_id")
    private Integer toOrgId;

    /**
     * 转出货主名称
     */
    @Column(name = "to_org_name")
    private String toOrgName;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 审核状态 0-审核中 1-审核通过 2-待审核 -1-审核退回
     */
    @Column(name = "case_state")
    private Integer caseState;

    /**
     * 0-初始，1-完成转移
     */
    @Column(name = "state")
    private Integer state;
}
