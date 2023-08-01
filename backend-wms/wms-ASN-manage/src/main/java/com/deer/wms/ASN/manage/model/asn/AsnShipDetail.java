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
 * asn 快递信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "asn_ship_detail")
public class AsnShipDetail {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = DateUtils.strToDate(acceptTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asn_ship_detail_id")
    private Integer asnShipDetailId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 快递公司
     */
    @Column(name = "ship_code")
    private String shipCode;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_code")
    private String shipBillCode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 长度
     */
    @Column(name = "length")
    private Double length;

    /**
     * 宽度
     */
    @Column(name = "width")
    private Double width;

    /**
     * 高度
     */
    @Column(name = "height")
    private Double height;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 验收时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;
}
