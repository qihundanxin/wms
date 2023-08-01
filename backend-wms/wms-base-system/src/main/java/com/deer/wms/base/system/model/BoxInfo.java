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
 * 箱子信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "box_info")
public class BoxInfo {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_id")
    private Integer boxId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 仓库Id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 类型 0-先拣后分 1-边拣边分 
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 编码
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

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
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;
}
