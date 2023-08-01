package com.deer.wms.base.system.model.Cell;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 货位信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "cell_info")
public class CellInfo {

    /**
     * 货位ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cell_id")
    private Integer cellId;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 货位名称
     */
    @Column(name = "cell_name")
    private String cellName;

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
     * 上架顺序
     */
    @Column(name = "ground_order")
    private String groundOrder;

    /**
     * 拣货顺序
     */
    @Column(name = "pick_order")
    private String pickOrder;

    /**
     * 货位类型 货架 地堆
     */
    @Column(name = "cell_type")
    private Integer cellType;

    /**
     * 货位使用类型  -1 垃圾货位 0-验收位 1-拣货位 2-仓储位 3-集货位 4-上架车
     */
    @Column(name = "cell_use_type")
    private Integer cellUseType;

    /**
     * 货位属性
     */
    @Column(name = "cell_attribute")
    private String cellAttribute;

    /**
     * 货位处理
     */
    @Column(name = "cell_handle")
    private String cellHandle;

    /**
     * 长
     */
    @Column(name = "length")
    private Double length;

    /**
     * 宽
     */
    @Column(name = "wide")
    private Double wide;

    /**
     * 高
     */
    @Column(name = "high")
    private Double high;

    /**
     * 周转需求 0-快速周转 1-正常周转 2-慢速周转
     */
    @Column(name = "cell_abc_level")
    private Integer cellAbcLevel;

    /**
     * 体积限制
     */
    @Column(name = "volume_limit")
    private Double volumeLimit;

    /**
     * 重量限制
     */
    @Column(name = "weight_limit")
    private Double weightLimit;

    /**
     * 箱数限制
     */
    @Column(name = "box_limit")
    private Double boxLimit;

    /**
     * sku种类限制
     */
    @Column(name = "sku_limit")
    private Double skuLimit;

    /**
     * 数量限制
     */
    @Column(name = "quantity_limit")
    private Double quantityLimit;

    /**
     * 托盘限制
     */
    @Column(name = "pallet_limit")
    private Double palletLimit;

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
     * z坐标
     */
    @Column(name = "z_coordinate")
    private Integer zCoordinate;

    /**
     * 货组第n组
     */
    @Column(name = "shelf_group")
    private Integer shelfGroup;

    /**
     * 货位组
     */
    @Column(name = "shelf_info_id")
    private Integer shelfInfoId;

    /**
     * 货位拣货方式 整箱拣货 拆零拣货 混合拣货
     */
    @Column(name = "cell_pick_type")
    private Integer cellPickType;

    /**
     * 启用状态 enable disable
     */
    @Column(name = "state")
    private String state;

    /**
     * 使用状态 0没有库存 1有库存
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 货架行
     */
    @Column(name = "s_row")
    private Integer sRow;

    /**
     * 货架列
     */
    @Column(name = "s_column")
    private Integer sColumn;

    /**
     * 1-系统内置
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
