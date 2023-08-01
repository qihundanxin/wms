package com.deer.wms.base.system.model.Supplier;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 供应商信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "supplier_manage")
public class SupplierManage {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    /**
     * 供应商ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 供应商编码
     */
    @Column(name = "supplier_code")
    private String supplierCode;

    /**
     * 供应商名字
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 供应商类型
     */
    @Column(name = "supplier_type")
    private String supplierType;

    /**
     * 供应商联系人
     */
    @Column(name = "link_man")
    private String linkMan;

    /**
     * 供应商联系电话
     */
    @Column(name = "link_phone")
    private String linkPhone;

    /**
     * 供应商地址
     */
    @Column(name = "supplier_site")
    private String supplierSite;

    /**
     * 供应商邮箱
     */
    @Column(name = "supplier_email")
    private String supplierEmail;

    /**
     * 供应商传真
     */
    @Column(name = "supplier_fax")
    private String supplierFax;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 开户银行
     */
    @Column(name = "supplier_bank")
    private String supplierBank;

    /**
     * 生产厂家
     */
    @Column(name = "supplier_factory")
    private String supplierFactory;

    /**
     * 生产国家
     */
    @Column(name = "supplier_country")
    private String supplierCountry;

    /**
     * 生成区域
     */
    @Column(name = "supplier_area")
    private String supplierArea;

    /**
     * 银行账号
     */
    @Column(name = "supplier_bankAccount")
    private String supplierBankaccount;

    /**
     * 纳税号
     */
    @Column(name = "supplier_taxNumber")
    private String supplierTaxnumber;

    /**
     * 所属省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所属市
     */
    @Column(name = "city")
    private String city;

    /**
     * 所属区、县
     */
    @Column(name = "area")
    private String area;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;
}
