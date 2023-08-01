package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.project.root.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 验收审核收录表
 * 
 * @author lixuehe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accept_audit_record")
public class AcceptAuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_audit_record_id")
    private Integer acceptAuditRecordId;

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
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

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
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 验收人ID
     */
    @Column(name = "accept_user_id")
    private Integer acceptUserId;

    /**
     * 验收人
     */
    @Column(name = "accept_user_name")
    private String acceptUserName;

    /**
     * 验收时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 包装ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装详细描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 验收数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 类型：3-盲收
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 审批状态:-1-审批不通过 0-待审批 1-审批通过
     */
    @Column(name = "audit_state")
    private Integer auditState;

    /**
     * 审批人Id
     */
    @Column(name = "audit_user_id")
    private Integer auditUserId;

    /**
     * 审批人
     */
    @Column(name = "audit_user_name")
    private String auditUserName;

    /**
     * 审批时间
     */
    @Column(name = "audit_time")
    private Date auditTime;
}
