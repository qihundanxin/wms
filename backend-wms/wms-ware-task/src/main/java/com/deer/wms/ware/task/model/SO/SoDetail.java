package com.deer.wms.ware.task.model.SO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * SO发运单detail
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "so_detail")
public class SoDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_detail_id")
    private Integer soDetailId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * soMaster Id（目前废弃中）
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * 详情单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 详情顺序序号
     */
    @Column(name = "detail_index")
    private Integer detailIndex;

    /**
     * 状态 -1：取消 0：新建 1：缺货、部分分配 2：全部分配 3：部分拣货 4：全部拣货 5：复核完成 6：完全发运 7：部分发运
     */
    @Column(name = "state")
    private Integer state;

    /**a
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
     * 中文申报品名
     */
    @Column(name = "cn_declaration_name")
    private String cnDeclarationName;

    /**
     * 英文申报品名
     */
    @Column(name = "declaration_name")
    private String declarationName;

    /**
     * 申报重量
     */
    @Column(name = "declaration_weight")
    private Double declarationWeight;

    /**
     * 申报价格
     */
    @Column(name = "declaration_price")
    private Double declarationPrice;

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
     * 包装详情ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 订货数
     */
    @Column(name = "order_quantity")
    private Double orderQuantity;

    /**
     * 预分配数
     */
    @Column(name = "expect_allotted_quantity")
    private Double expectAllottedQuantity;

    /**
     * 分配数
     */
    @Column(name = "allotted_quantity")
    private Double allottedQuantity;

    /**
     * 拣货数量
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 复核数量
     */
    @Column(name = "check_quantity")
    private Double checkQuantity;

    /**
     * 发货数量
     */
    @Column(name = "delivery_quantity")
    private Double deliveryQuantity;

    /**
     * 自定义1
     */
    @Column(name = "so_udf_ds1")
    private String soUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "so_udf_ds2")
    private String soUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "so_udf_ds3")
    private String soUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 合并单号
     */
    @Column(name = "merge_no")
    private String mergeNo;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 大小
     */
    @Column(name = "size")
    private Double size;

    /**
     * 分配包装ID
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
     * 拣货包装ID
     */
    @Column(name = "pick_pack_detail_id")
    private Integer pickPackDetailId;

    /**
     * 拣货包装描述
     */
    @Column(name = "pick_pack_describe")
    private String pickPackDescribe;

    /**
     * 拣货转换系数
     */
    @Column(name = "pick_trans_ratio")
    private Double pickTransRatio;

    /**
     * 发货包装ID
     */
    @Column(name = "delivery_pack_detail_id")
    private Integer deliveryPackDetailId;

    /**
     * 发货包装描述
     */
    @Column(name = "delivery_pack_describe")
    private String deliveryPackDescribe;

    /**
     * 发货转换系数
     */
    @Column(name = "delivery_trans_ratio")
    private Double deliveryTransRatio;

    /**
     * bom 拆分
     */
    @Column(name = "bom_split")
    private Integer bomSplit;

    /**
     * 调拨详情单号
     */
    @Column(name = "allot_detail_no")
    private String allotDetailNo;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
