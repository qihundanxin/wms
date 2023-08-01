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
 * 物料批次表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_batch")
public class ItemBatch {

    // 兼容旧的String类型日期
    public String getProductDate() {
        return DateUtils.dateToStrNullable(this.productDate, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setProductDate(String productDate) {
        this.productDate = DateUtils.strToDate(productDate);
    }

    public String getExDate() {
        return DateUtils.dateToStrNullable(this.exDate, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExDate(String exDate) {
        this.exDate = DateUtils.strToDate(exDate);
    }

    public String getInDate() {
        return DateUtils.dateToStrNullable(this.inDate, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setInDate(String inDate) {
        this.inDate = DateUtils.strToDate(inDate);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

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
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 生产日期
     */
    @Column(name = "product_date")
    private Date productDate;

    /**
     * 失效日期
     */
    @Column(name = "ex_date")
    private Date exDate;

    /**
     * 入库日期
     */
    @Column(name = "in_date")
    private Date inDate;

    /**
     * 供应商代码
     */
    @Column(name = "supplier_code")
    private String supplierCode;

    /**
     * 厂家批号
     */
    @Column(name = "supplier_batch")
    private String supplierBatch;

    /**
     * 质量状态  0-合格  1-不合格
     */
    @Column(name = "qc_state")
    private Integer qcState;

    /**
     * 成本单价
     */
    @Column(name = "cost_price")
    private Double costPrice;

    /**
     * 销售单价
     */
    @Column(name = "sale_price")
    private Double salePrice;

    /**
     * 单据号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 用户自定义批次属性1
     */
    @Column(name = "batch_attribute_1")
    private String batchAttribute1;

    /**
     * 用户自定义批次属性2
     */
    @Column(name = "batch_attribute_2")
    private String batchAttribute2;

    /**
     * 单据扩展字段1
     */
    @Column(name = "memo1")
    private String memo1;

    /**
     * 单据扩展字段2
     */
    @Column(name = "memo2")
    private String memo2;

    /**
     * 单据扩展字段3
     */
    @Column(name = "memo3")
    private String memo3;

    /**
     * 订单类型  2-上架
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;
}
