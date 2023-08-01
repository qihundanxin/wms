package com.deer.wms.busine.tactic.model.Upper;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 上架策略详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "upper_tactic_detail")
public class UpperTacticDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upper_tactic_detail_id")
    private Integer upperTacticDetailId;

    /**
     * 顺序
     */
    @Column(name = "order_no")
    private Integer orderNo;

    /**
     * 上架策略id
     */
    @Column(name = "upper_tactic_id")
    private Integer upperTacticId;

    /**
     * 规则代码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 0-不启用  1-启用
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 上架规则类型
     */
    @Column(name = "upper_tactic_type")
    private Integer upperTacticType;

    /**
     * 原始货位
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 目标货位
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 目标货区
     */
    @Column(name = "to_area_code")
    private String toAreaCode;

    /**
     * 必须是空货位
     */
    @Column(name = "is_empty_cell")
    private Integer isEmptyCell;

    /**
     * 不允许混放产品
     */
    @Column(name = "no_confused_item")
    private Integer noConfusedItem;

    /**
     * 不许混批
     */
    @Column(name = "no_confused_batch")
    private Integer noConfusedBatch;

    /**
     * 必须同料号
     */
    @Column(name = "is_equal_item")
    private Integer isEqualItem;

    /**
     * 必须同批
     */
    @Column(name = "is_equal_batch")
    private Integer isEqualBatch;

    /**
     * 混放产品最大数
     */
    @Column(name = "can_item_max")
    private Integer canItemMax;

    /**
     * 混放最大批次数
     */
    @Column(name = "can_batch_max")
    private Integer canBatchMax;

    /**
     * 货位使用类型  -1 垃圾货位 0-验收位 1-拣货位 2-仓储位 3-集货位 4-上架车
     */
    @Column(name = "cell_use_type")
    private Integer cellUseType;

    /**
     *  货位类型  0-所有  货架  地堆
     */
    @Column(name = "cell_type")
    private Integer cellType;

    /**
     * 货位状态 0-所有
     */
    @Column(name = "cell_state")
    private Integer cellState;

    /**
     * 周转需求 0-所有 1-快速周转 2-正常周转 3-慢速周转
     */
    @Column(name = "cell_abc_level")
    private Integer cellAbcLevel;

    /**
     * 货位拣货方式 0-所有 整箱拣货  拆零拣货  混合拣货
     */
    @Column(name = "cell_pick_type")
    private Integer cellPickType;

    /**
     * 订单类型 0-所有  2-上架
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     *  包装级别 0-所有 
     */
    @Column(name = "pack_level")
    private Integer packLevel;

    /**
     * ABC循环级别  0-所有 1-A 2-B 3-C
     */
    @Column(name = "abc_level")
    private Integer abcLevel;

    /**
     * 货位组ID
     */
    @Column(name = "shelf_info_id")
    private Integer shelfInfoId;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;
}
