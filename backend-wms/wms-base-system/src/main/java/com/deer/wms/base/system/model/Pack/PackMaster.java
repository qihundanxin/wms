package com.deer.wms.base.system.model.Pack;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 包装信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pack_master")
public class PackMaster {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pack_master_id")
    private Integer packMasterId;

    /**
     * 包装编码
     */
    @Column(name = "pack_code")
    private String packCode;

    /**
     * 包装名称
     */
    @Column(name = "pack_name")
    private String packName;

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
     * 1-系统内置  0-正常
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
