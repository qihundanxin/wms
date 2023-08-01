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
 * 采购归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "request_buy_file")
public class RequestBuyFile {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getExpectTime() {
        return DateUtils.dateToStrNullable(this.expectTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = DateUtils.strToDate(expectTime);
    }

    public String getFileTime() {
        return DateUtils.dateToStrNullable(this.fileTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFileTime(String fileTime) {
        this.fileTime = DateUtils.strToDate(fileTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_buy_file_id")
    private Integer requestBuyFileId;

    /**
     * 采购单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 采购类型
     */
    @Column(name = "buy_type")
    private Integer buyType;

    /**
     * 状态 -2-保存为草稿 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 1-部分收货 0-全部收货 2-ASN完成
     */
    @Column(name = "accept_state")
    private Integer acceptState;

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
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 预期到货时间
     */
    @Column(name = "expect_time")
    private Date expectTime;

    /**
     * 自定义1
     */
    @Column(name = "po_udf_hs1")
    private String poUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "po_udf_hs2")
    private String poUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "po_udf_hs3")
    private String poUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 归档时间
     */
    @Column(name = "file_time")
    private Date fileTime;

    /**
     * 归档人ID
     */
    @Column(name = "file_user_id")
    private Integer fileUserId;

    /**
     * 归档人
     */
    @Column(name = "file_user_name")
    private String fileUserName;
}
