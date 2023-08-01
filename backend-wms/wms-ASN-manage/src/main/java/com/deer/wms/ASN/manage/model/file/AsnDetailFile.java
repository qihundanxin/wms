package com.deer.wms.ASN.manage.model.file;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * ASN详情归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "asn_detail_file")
public class AsnDetailFile {

    // 兼容旧的String类型日期
    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = DateUtils.strToDate(acceptTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asn_detail_file_id")
    private Integer asnDetailFileId;

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
     * 状态 0-初始化 1-已码盘  2-部分收货  3-全部收货  4-ASN完成
     */
    @Column(name = "state")
    private Integer state;

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
     * 预期到货数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 验收数量
     */
    @Column(name = "accept_quantity")
    private Double acceptQuantity;

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
}
