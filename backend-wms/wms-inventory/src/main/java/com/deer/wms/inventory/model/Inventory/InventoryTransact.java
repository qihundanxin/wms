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
 * 库存事务表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_transact")
public class InventoryTransact {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_transact_id")
    private Integer inventoryTransactId;

    /**
     * 事务类型
     */
    @Column(name = "transact_type")
    private Integer transactType;

    /**
     * 单据类型
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 详情单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 1完成 -1撤回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

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
     * 来自库存id
     */
    @Column(name = "from_inventory_id")
    private Integer fromInventoryId;

    /**
     * 来自货主ID
     */
    @Column(name = "from_organization_id")
    private Integer fromOrganizationId;

    /**
     * 来自货主名称
     */
    @Column(name = "from_organization_name")
    private String fromOrganizationName;

    /**
     * 来自批次ID
     */
    @Column(name = "from_batch_id")
    private Integer fromBatchId;

    /**
     * 来自批次名称
     */
    @Column(name = "from_batch_name")
    private String fromBatchName;

    /**
     * 来自追踪号
     */
    @Column(name = "from_box_code")
    private String fromBoxCode;

    /**
     * 来自货位编码
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 包装描述
     */
    @Column(name = "from_pack_describe")
    private String fromPackDescribe;

    /**
     * 包装详情ID
     */
    @Column(name = "from_pack_detail_id")
    private Integer fromPackDetailId;

    /**
     * 来自转换系数
     */
    @Column(name = "from_trans_ratio")
    private Double fromTransRatio;

    /**
     * 主单位数量
     */
    @Column(name = "from_quantity")
    private Double fromQuantity;

    /**
     * 调入仓库id
     */
    @Column(name = "to_inventory_id")
    private Integer toInventoryId;

    /**
     * 调入货主ID
     */
    @Column(name = "to_organization_id")
    private Integer toOrganizationId;

    /**
     * 调入货主名称
     */
    @Column(name = "to_organization_name")
    private String toOrganizationName;

    /**
     * 调入批次ID
     */
    @Column(name = "to_batch_id")
    private Integer toBatchId;

    /**
     * 调入批次名称
     */
    @Column(name = "to_batch_name")
    private String toBatchName;

    /**
     * 调入追踪号
     */
    @Column(name = "to_box_code")
    private String toBoxCode;

    /**
     * 调入货位编码
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 调入包装ID
     */
    // 数据库类型为String
    @Column(name = "to_pack_detail_id")
    private Integer toPackDetailId;

    /**
     * 调入包装描述
     */
    @Column(name = "to_pack_describe")
    private String toPackDescribe;

    /**
     * 调入转换系数
     */
    @Column(name = "to_trans_ratio")
    private Double toTransRatio;

    /**
     * 调入数量
     */
    @Column(name = "to_quantity")
    private Double toQuantity;

    /**
     * 调入仓库id
     */
    @Column(name = "to_ware_id")
    private Integer toWareId;

    /**
     * 调入仓库名称
     */
    @Column(name = "to_ware_name")
    private String toWareName;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;


    /**
     * 事务执行图片
     */
    @Column(name = "transact_imgs")
    private String transactImgs;


}
