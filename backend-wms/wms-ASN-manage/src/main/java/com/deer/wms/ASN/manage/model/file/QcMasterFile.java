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
 * QC单归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "qc_master_file")
public class QcMasterFile {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getFileTime() {
        return DateUtils.dateToStrNullable(this.fileTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFileTime(String fileTime) {
        this.fileTime = DateUtils.strToDate(fileTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_master_file_id")
    private Integer qcMasterFileId;

    /**
     * 质检单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * ASN单号
     */
    @Column(name = "asn_bill_no")
    private String asnBillNo;

    /**
     * 质检类型
     */
    @Column(name = "qc_type")
    private Integer qcType;

    /**
     * 单据来源
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 状态  0- 未质检  1-已质检
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 创库名称
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
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 自定义1
     */
    @Column(name = "qc_udf_hs1")
    private String qcUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "qc_udf_hs2")
    private String qcUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "qc_udf_hs3")
    private String qcUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

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
