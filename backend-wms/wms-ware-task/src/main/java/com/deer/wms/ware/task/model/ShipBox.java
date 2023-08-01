package com.deer.wms.ware.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 装运箱信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "ship_box")
public class ShipBox {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_box_id")
    private Integer shipBoxId;

    /**
     * 快递公司
     */
    @Column(name = "ship_code")
    private String shipCode;

    /**
     * 箱号
     */
    @Column(name = "ship_box_code")
    private String shipBoxCode;

    /**
     * 承运商
     */
    @Column(name = "ship_name")
    private String shipName;

    /**
     * 装箱人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 装箱人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 装箱时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 长
     */
    @Column(name = "length")
    private Double length;

    /**
     * 宽
     */
    @Column(name = "width")
    private Double width;

    /**
     * 高
     */
    @Column(name = "height")
    private Double height;

    /**
     * 寄送地址
     */
    @Column(name = "address")
    private String address;

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
     * 包裹数量
     */
    @Column(name = "pack_no")
    private Integer packNo;
}
