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
 * 序列号信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "serial_no")
public class SerialNo {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_no_id")
    private Integer serialNoId;

    /**
     * 序列号
     */
    @Column(name = "serial_no")
    private String serialNo;

    /**
     * 1-验收  2-移库  3-发运
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 验收记录ID  拣货记录ID  移库ID
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;
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
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;
}
