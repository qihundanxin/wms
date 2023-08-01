package com.deer.wms.inventory.model.freeze;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 库存冻结detail表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "freeze_detail")
public class FreezeDetail {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freeze_detail_id")
    private Integer freezeDetailId;

    /**
     * 入库单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 详情单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 库存ID
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

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
     * 物料类型ID
     */
    @Column(name = "item_type_id")
    private Integer itemTypeId;

    /**
     * 物料类型名称
     */
    @Column(name = "item_type_name")
    private String itemTypeName;

    /**
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 包装id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * LPN号码
     */
    @Column(name = "lpn")
    private Integer lpn;

    /**
     * 冻结数量
     */
    @Column(name = "freeze_quantity")
    private Double freezeQuantity;

    /**
     * 0-正常   1-冻结   2-已释放
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_use_id")
    private Integer createUseId;

    /**
     * 创建人
     */
    @Column(name = "create_use_name")
    private String createUseName;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;
}
