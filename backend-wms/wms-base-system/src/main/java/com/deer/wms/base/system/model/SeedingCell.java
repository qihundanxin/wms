package com.deer.wms.base.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 播种工位
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "seeding_cell")
public class SeedingCell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seeding_cell_id")
    private Integer seedingCellId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 播种工位编码
     */
    @Column(name = "seeding_cell_code")
    private String seedingCellCode;

    /**
     * 播种墙
     */
    @Column(name = "seeding_wall_code")
    private String seedingWallCode;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 修改用户id
     */
    @Column(name = "update_user_id")
    private Integer updateUserId;

    /**
     * 修改用户名
     */
    @Column(name = "update_user_name")
    private Date updateUserName;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    /**
     * 0-空 1-有billNo 2-完成复核 
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;
}
