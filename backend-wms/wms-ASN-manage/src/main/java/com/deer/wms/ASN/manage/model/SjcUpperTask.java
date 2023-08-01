package com.deer.wms.ASN.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 上架车上架任务
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sjc_upper_task")
public class SjcUpperTask {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getUpTime() {
        return DateUtils.dateToStrNullable(this.upTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setUpTime(String upTime) {
        this.upTime = DateUtils.strToDate(upTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sjc_upper_task_id")
    private Integer sjcUpperTaskId;

    /**
     * 验收库存id
     */
    @Column(name = "accept_inventory_id")
    private Integer acceptInventoryId;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "cc_inventory_id")
    private Integer ccInventoryId;

    /**
     *  仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 货主id
     */
    @Column(name = "org_id")
    private Integer orgId;

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
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 原始货位
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 上架车编码
     */
    @Column(name = "sjc_shlef_code")
    private String sjcShlefCode;

    /**
     * 上架车编码
     */
    @Column(name = "sjc_cell_code")
    private String sjcCellCode;

    /**
     * 推荐货位
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 实际上架货位
     */
    @Column(name = "to_cell_code2")
    private String toCellCode2;

    /**
     * 0-初始化 1-已经完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上架人ID
     */
    @Column(name = "up_user_id")
    private Integer upUserId;

    /**
     * 上架人
     */
    @Column(name = "up_user_name")
    private String upUserName;

    /**
     * 上架时间
     */
    @Column(name = "up_time")
    private Date upTime;

    /**
     * 0-上架车上架 1-普通上架
     */
    @Column(name = "type")
    private Integer type;
}
