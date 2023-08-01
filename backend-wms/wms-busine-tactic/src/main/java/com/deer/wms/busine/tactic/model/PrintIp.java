package com.deer.wms.busine.tactic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 打印机参数
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "print_ip")
public class PrintIp {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getUpdateTime() {
        return DateUtils.dateToStrNullable(this.updateTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = DateUtils.strToDate(updateTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "print_ip_id")
    private Integer printIpId;

    /**
     * Ip
     */
    @Column(name = "ip")
    private String ip;

    @Column(name = "state")
    private Integer state;

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
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 修改人id
     */
    @Column(name = "update_user_id")
    private Integer updateUserId;

    /**
     * 修改人名称
     */
    @Column(name = "update_user_name")
    private String updateUserName;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
