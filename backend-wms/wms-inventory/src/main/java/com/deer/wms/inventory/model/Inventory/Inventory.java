package com.deer.wms.inventory.model.Inventory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 库存余量表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory")
public class Inventory {

    // 兼容旧的String类型日期
    public String getModifyTime() {
        return DateUtils.dateToStrNullable(this.modifyTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = DateUtils.strToDate(modifyTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主ID
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
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

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
     * 商品统一代码
     */
    @Column(name = "upc")
    private String upc;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * TODO （冗余）需要被动更新
     * 
     * 库位类型 -1 垃圾货位 0-验收位 1-拣货位 2-仓储位 3-集货位 4-上架车
     */
    // 数据库类型为String
    @Column(name = "cell_use_type")
    private Integer cellUseType;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * LPN号码
     */
    @Column(name = "lpn")
    private String lpn;

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
     * 单位转换率
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 分配数量
     */
    @Column(name = "allot_quantity")
    private Double allotQuantity;

    /**
     * 分配包装id
     */
    @Column(name = "allot_pack_detail_id")
    private Integer allotPackDetailId;

    /**
     * 分配包装描述
     */
    @Column(name = "allot_pack_describe")
    private String allotPackDescribe;

    /**
     * 分配转换系数
     */
    @Column(name = "allot_trans_ratio")
    private Double allotTransRatio;

    /**
     * 冻结数量
     */
    @Column(name = "freeze_quantity")
    private Double freezeQuantity;

    /**
     * 冻结包装id
     */
    @Column(name = "freeze_pack_detail_id")
    private Integer freezePackDetailId;

    /**
     * 冻结包装描述
     */
    @Column(name = "freeze_pack_describe")
    private String freezePackDescribe;

    /**
     * 冻结转换系数
     */
    @Column(name = "freeze_trans_ratio")
    private Double freezeTransRatio;

    /**
     * -2 放入回收站 -1 发运 0-正常 1-部分冻结 2-全部冻结
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 1-合格 2-不合格
     */
    @Column(name = "qc_state")
    private Integer qcState;

    /**
     * 出库单 在集货位上面，属于哪个出库单
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 爆款
     */
    @Column(name = "bao_kuan")
    private Integer baoKuan;

    /**
     * 版本号，更新比较版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 是否是仓库直接放到货架的数据 0 否 1是
     */
    @Column(name = "ware_init")
    private Integer wareInit;

    /**
     * 边拣边分时使用
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * debug备注
     */
    @Column(name = "debug_memo")
    private String debugMemo;

    /**
     * 回收时间
     */
    @Column(name = "recovery_time")
    private Date recoveryTime;

    /**
     * 回收人id
     */
    @Column(name = "recovery_user_id")
    private Integer recoveryUserId;

    /**
     * 回收人
     */
    @Column(name = "recovery_user_name")
    private String recoveryUserName;

    /**
     * 回收原因
     */
    @Column(name = "recovery_reason")
    private String recoveryReason;

    /**
     *入库时间
     */
    @Column(name = "ware_entry_time")
    private Date wareEntryTime;
}
