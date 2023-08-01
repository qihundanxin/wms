package com.deer.wms.inventory.model.stockTake;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 盘点单
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "stock_take_master")
public class StockTakeMaster {
    // 兼容旧的String类型日期
    public String getDynamicFromTime() {
        return DateUtils.dateToStrNullable(this.dynamicFromTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setDynamicFromTime(String dynamicFromTime) {
        this.dynamicFromTime = DateUtils.strToDate(dynamicFromTime);
    }

    public String getDynamicToTime() {
        return DateUtils.dateToStrNullable(this.dynamicToTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setDynamicToTime(String dynamicToTime) {
        this.dynamicToTime = DateUtils.strToDate(dynamicToTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_take_master_id")
    private Integer stockTakeMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 1-普通盘点 2-动态盘点  3-随机盘点
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 0-初始化  -1-暂停  2-生成任务   3-盘点中  4-盘点完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 优先级 1-普通 2-优先 3-紧急
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库编码
     */
    @Column(name = "ware_code")
    private String wareCode;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主Id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 动态盘点开始时间
     */
    @Column(name = "dynamic_from_time")
    private Date dynamicFromTime;

    /**
     * 动态盘点结束时间
     */
    @Column(name = "dynamic_to_time")
    private Date dynamicToTime;

    /**
     * 产品编码
     */
    @Column(name = "item_keywords")
    private String itemKeywords;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 来源货区
     */
    @Column(name = "from_area_code")
    private String fromAreaCode;

    /**
     * 到货区
     */
    @Column(name = "to_area_code")
    private String toAreaCode;

    /**
     * 来源货架
     */
    @Column(name = "from_shelf_code")
    private String fromShelfCode;

    /**
     * 到货架
     */
    @Column(name = "to_shelf_code")
    private String toShelfCode;

    /**
     * 来源货位
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 到货位
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 自定义1
     */
    @Column(name = "udf_hs1")
    private String udfHs1;

    /**
     * 自定义2
     */
    @Column(name = "udf_hs2")
    private String udfHs2;

    /**
     * 自定义3
     */
    @Column(name = "udf_hs3")
    private String udfHs3;

    /**
     * 0-不随机  1-随机
     */
    @Column(name = "if_random")
    private Integer ifRandom;

    /**
     * 随机生成的任务数
     */
    @Column(name = "random_num")
    private Integer randomNum;

    /**
     * -1不通过   0-流转中  1-通过  2-初始化
     */
    @Column(name = "case_state")
    private Integer caseState;
}
