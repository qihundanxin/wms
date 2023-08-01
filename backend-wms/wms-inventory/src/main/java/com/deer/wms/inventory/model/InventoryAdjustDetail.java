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
 * 库存调整detail表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_adjust_detail")
public class InventoryAdjustDetail {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getAdjustTime() {
        return DateUtils.dateToStrNullable(this.adjustTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAdjustTime(String adjustTime) {
        this.adjustTime = DateUtils.strToDate(adjustTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjust_detail_id")
    private Integer adjustDetailId;

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
    private String lpn;

    /**
     * 差异数量
     */
    @Column(name = "differ_quantity")
    private Double differQuantity;

    /**
     * 库存数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 数量
     */
    @Column(name = "adjust_quantity")
    private Double adjustQuantity;

    /**
     * 1-未调整 2-已调整
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
     * 调整时间
     */
    @Column(name = "adjust_time")
    private Date adjustTime;

    /**
     * 调整人Id
     */
    @Column(name = "adjust_user_id")
    private Integer adjustUserId;

    /**
     * 调整人姓名
     */
    @Column(name = "adjust_user_name")
    private String adjustUserName;

    /**
     * 0未同步，1已同步
     */
    @Column(name = "syn_erp")
    private Integer synErp;
}

//    public InventoryAdjustDetail(String billNo, String detailNo, Integer inventoryId, String itemCode, String itemName, Integer batchId, String batchName, String cellCode, String boxCode, Integer packDetailId, String packDescribe, String lpn, Double differQuantity, Double adjustQuantity, Integer state, String createTime, Integer createUseId, String createUseName, String memo) {
//setbillNo() ;
//setdetailNo() ;
//setinventoryId() ;
//setitemCode() ;
//setitemName() ;
//setbatchId() ;
//setbatchName() ;
//setcellCode() ;
//setboxCode() ;
//setpackDetailId() ;
//setpackDescribe() ;
//setlpn()lpn;
//setdifferQuantity() ;
//setadjustQuantity() ;
//setstate()state;
//setcreateTime() ;
//setcreateUseId() ;
//setcreateUseName() ;
//setmemo() ;
//    }