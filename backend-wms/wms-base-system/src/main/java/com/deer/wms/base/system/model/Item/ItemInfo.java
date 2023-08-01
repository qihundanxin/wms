package com.deer.wms.base.system.model.Item;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 物料信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_info")
public class ItemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 条形码编码
     */
    @Column(name = "item_bar_code")
    private String itemBarCode;

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
     * 物料类别名称
     */
    @Column(name = "item_type_code")
    private String itemTypeCode;

    /**
     * 上限
     */
    @Column(name = "upper_limit")
    private Integer upperLimit;

    /**
     * 下限
     */
    @Column(name = "floor_limit")
    private Integer floorLimit;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 默认价格/单价
     */
    @Column(name = "default_price")
    private Double defaultPrice;

    /**
     * 质量/重量（KG）
     */
    @Column(name = "weight")
    private BigDecimal weight;

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
     * 货主ID -1通用种类 货主专属 ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 包装编码
     */
    @Column(name = "pack_code")
    private String packCode;

    /**
     * 物料类型ID
     */
    @Column(name = "item_type_id")
    private Integer itemTypeId;

    /**
     * 供应商id
     */
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 批次策略代码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 上架策略代码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 质检策略编码
     */
    @Column(name = "qc_tactic_code")
    private String qcTacticCode;

    /**
     * 1-A 2-B 3-C
     */
    @Column(name = "abc_level")
    private Integer abcLevel;

    /**
     * 物料状态：enable=启用；disable=停用；默认启用
     */
    @Column(name = "item_status")
    private String itemStatus;

    /**
     * 拣货策略
     */
    @Column(name = "pick_tactic_code")
    private String pickTacticCode;

    /**
     * 库存周转编码
     */
    @Column(name = "turnover_tactic_code")
    private String turnoverTacticCode;

    /**
     * 1688商品链接
     */
    @Column(name = "product_url_1688")
    private String productUrl1688;

    /**
     * 一级类目名
     */
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "category_id2")
    private String categoryId2;

    /**
     * 子级类目名
     */
    @Column(name = "en_category")
    private String enCategory;

    /**
     * 产品英文名
     */
    @Column(name = "en_product")
    private String enProduct;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 详细描述
     */
    @Column(name = "item_desc")
    private String itemDesc;

    /**
     * 简要描述
     */
    @Column(name = "short_desc")
    private String shortDesc;

    /**
     * 库存
     */
    @Column(name = "stock")
    private Long stock;

    @Column(name = "status")
    private Integer status;

    @Column(name = "url")
    private String url;
    /**
     * 长
     */
    @Column(name = "item_length")
    private BigDecimal itemLength;
    /***
     * 宽
     */
    @Column(name = "item_width")
    private BigDecimal itemWidth;
    /**
     * 高
     */
    @Column(name = "item_height")
    private BigDecimal itemHeight;

    /**
     * 磅数
     */
    @Column(name = "pound")
    private BigDecimal pound;

    /**
     * 是否要称重
     */
    @Column(name = "weighting")
    private Boolean weighting;

    /**
     * 是否是散件
     */
    @Column(name = "unpacking")
    private Boolean unpacking;
}
