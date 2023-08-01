package com.deer.wms.ASN.manage.model.asn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * ASN详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "asn_detail")
public class AsnDetail {

    // 兼容旧的String类型日期
    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String d) {
        this.acceptTime = DateUtils.strToDate(d);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asn_detail_id")
    private Integer asnDetailId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 详情单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 采购详情单号
     */
    @Column(name = "request_detail_detail_no")
    private String requestDetailDetailNo;

    /**
     * 状态 0-初始化 1-已码盘 2-部分收货 3-全部收货 4-ASN完成 5-超额收货
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 0-默认 1-异常待处理 2-异常已经处理
     */
    @Column(name = "ex_state")
    private Integer exState;

    /**
     * 质检状态 1- 不合格 0-待质检 1-合格
     */
    @Column(name = "qc_state")
    private Integer qcState;

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
     * 包装编码
     */
    @Column(name = "pack_code")
    private String packCode;

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
     * 商品统一代码
     */
    @Column(name = "upc")
    private String upc;

    /**
     * 预期到货数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 预期到货包装Id
     */
    @Column(name = "expect_pack_detail_id")
    private Integer expectPackDetailId;

    /**
     * 预期到货包装描述
     */
    @Column(name = "expect_pack_describe")
    private String expectPackDescribe;

    /**
     * 预期到货转换系数
     */
    @Column(name = "expect_trans_ratio")
    private Double expectTransRatio;

    /**
     * 验收数量
     */
    @Column(name = "accept_quantity")
    private Double acceptQuantity;

    /**
     * 验收包装Id
     */
    @Column(name = "accept_pack_detail_id")
    private Integer acceptPackDetailId;

    /**
     * 验收包装描述
     */
    @Column(name = "accept_pack_describe")
    private String acceptPackDescribe;

    /**
     * 验收转换系数
     */
    @Column(name = "accept_trans_ratio")
    private Double acceptTransRatio;

    /**
     * 收货时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    /**
     * 自定义1
     */
    @Column(name = "asn_udf_ds1")
    private String asnUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "asn_udf_ds2")
    private String asnUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "asn_udf_ds3")
    private String asnUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 上架数量
     */
    @Column(name = "upper_quantity")
    private Double upperQuantity;

    /**
     * 上架包装id
     */
    @Column(name = "upper_pack_detail_id")
    private Integer upperPackDetailId;

    /**
     * 上架包装描述
     */
    @Column(name = "upper_pack_describe")
    private String upperPackDescribe;

    /**
     * 上架转换系数
     */
    @Column(name = "upper_trans_ratio")
    private Double upperTransRatio;

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

    /**
     * 1688商品链接
     */
    @Column(name = "product_url_1688")
    private String productUrl1688;
}
