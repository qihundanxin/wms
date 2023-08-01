package com.deer.wms.system.manage.model.resource;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.Data;
import lombok.ToString;

/**
 * Created by guo on 2018/8/11.
 */
@Data
public class ResourceCriteria extends QueryCriteria {
    private Integer parentId;

    private Integer resourceGroup;
}
