package com.deer.wms.ware.task.model.Send;

import com.deer.wms.project.root.core.service.QueryCriteria;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 发运详情单
 *
 * @author lixuehe
 * @date 2023/02/22
 */
@Data
public class SendDetailParam  extends QueryCriteria {


    /**
     * 出库单号
     */
    private String soBillNo;

    private List<String> billNos;


    /**
     * 状态 状态 0新建 1部分发运 2 全部发运
     */
    private Integer state;

    /**
     * 物料编码
     */
    private String itemCode;

    /**
     * 物料名称
     */
    private String itemName;


    /**
     * 发运时间
     */
    private Date startSendTime;


    /**
     * 发运时间
     */
    private Date endSendTime;

    /**
     * 发运人ID
     */
    private Integer sendUserId;

    /**
     * 发运人
     */
    private String sendUserName;
}
