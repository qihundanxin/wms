package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.project.root.core.service.QueryCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验收审核记录
 *
 * @author lixuehe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptAuditRecordCriteria extends QueryCriteria {

    /**
     * 审批状态:-1-审批不通过 0-待审批 1-审批通过
     */
    private Integer auditState;
    /**
     * 物料编码
     */
    private String itemCode;
    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 货位编码
     */
    private String cellCode;
    /**
     * 验收人
     */
    private String acceptUserName;

    /**
     * 物料编码模糊查询
     */
    private String itemCodeLike;
    /**
     * 物料名称模糊查询
     */
    private String itemNameLike;
    /**
     * 货位编码模糊查询
     */
    private String cellCodeLike;
    /**
     * 验收人模糊查询
     */
    private String acceptUserNameLike;

    /**
     * 验收时间排序 ASC 时间正序，DESC 时间倒序
     */
    private String acceptTimeSort;




   }
