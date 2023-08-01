package com.deer.wms.base.system.model.Shelf;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 货位组信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "shelf_info")
public class ShelfInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelf_info_id")
    private Integer shelfInfoId;

    /**
     * 货位组名称
     */
    @Column(name = "shelf_name")
    private String shelfName;

    /**
     * 货位组编码
     */
    @Column(name = "shelf_code")
    private String shelfCode;

    /**
     * 货位组描述
     */
    @Column(name = "shelf_describe")
    private String shelfDescribe;

    /**
     * 类型 货架  地堆
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 状态 
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 货区ID
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * x坐标
     */
    @Column(name = "x_coordinate")
    private Integer xCoordinate;

    /**
     * y坐标
     */
    @Column(name = "y_coordinate")
    private Integer yCoordinate;

    /**
     * 货架号
     */
    @Column(name = "shelf_no")
    private Integer shelfNo;

    /**
     * 货组中组数
     */
    @Column(name = "shelf_group")
    private Integer shelfGroup;

    /**
     * 货架行
     */
    @Column(name = "shelf_row")
    private Integer shelfRow;

    /**
     * 货架列
     */
    @Column(name = "shelf_column")
    private Integer shelfColumn;

    /**
     * 1-系统内置
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
