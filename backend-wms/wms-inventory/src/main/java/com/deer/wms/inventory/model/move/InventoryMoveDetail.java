package com.deer.wms.inventory.model.move;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 库存转移详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_move_detail")
public class InventoryMoveDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_move_detail_id")
    private Integer inventoryMoveDetailId;

    /**
     * 移库单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 状态  0-正常   1-转移完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 库存id
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
     * 从仓库ID
     */
    @Column(name = "from_ware_id")
    private Integer fromWareId;

    /**
     * 从仓库名称
     */
    @Column(name = "from_ware_name")
    private String fromWareName;

    /**
     * 从货主ID
     */
    @Column(name = "from_organization_id")
    private Integer fromOrganizationId;

    /**
     * 从货主名称
     */
    @Column(name = "from_organization_name")
    private String fromOrganizationName;

    /**
     * 从批次ID
     */
    @Column(name = "from_batch_id")
    private Integer fromBatchId;

    /**
     * 从批次名称
     */
    @Column(name = "from_batch_name")
    private String fromBatchName;

    /**
     * 从追踪号
     */
    @Column(name = "from_box_code")
    private String fromBoxCode;

    /**
     * 从货位
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 从包装
     */
    @Column(name = "from_pack_detail_id")
    private Integer fromPackDetailId;

    /**
     * 从包装描述
     */
    @Column(name = "from_pack_describe")
    private String fromPackDescribe;

    /**
     * 从数量
     */
    @Column(name = "from_quantity")
    private Double fromQuantity;

    /**
     * 从lpn
     */
    @Column(name = "from_lpn")
    private String fromLpn;

    /**
     * 到仓库ID
     */
    @Column(name = "to_ware_id")
    private Integer toWareId;

    /**
     * 到仓库名称
     */
    @Column(name = "to_ware_name")
    private String toWareName;

    /**
     * 到货主ID
     */
    @Column(name = "to_organization_id")
    private Integer toOrganizationId;

    /**
     * 到货主名称
     */
    @Column(name = "to_organization_name")
    private String toOrganizationName;

    /**
     * 到批次ID
     */
    @Column(name = "to_batch_id")
    private Integer toBatchId;

    /**
     * 到批次名称
     */
    @Column(name = "to_batch_name")
    private String toBatchName;

    /**
     * 搭配追踪号
     */
    @Column(name = "to_box_code")
    private String toBoxCode;

    /**
     * 到货位编码
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 到包装ID
     */
    @Column(name = "to_pack_detail_id")
    private Integer toPackDetailId;

    /**
     * 到包装描述
     */
    @Column(name = "to_pack_describe")
    private String toPackDescribe;

    /**
     * 到数量
     */
    @Column(name = "to_quantity")
    private Double toQuantity;

    /**
     * 到lpn
     */
    @Column(name = "to_lpn")
    private String toLpn;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
