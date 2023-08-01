package com.deer.wms.webhook.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.webhook.model.WebHookLog;
import com.deer.wms.webhook.model.WebHookLogParam;

public interface WebHookLogMapper extends Mapper<WebHookLog> {
    List<WebHookLog> getList(@Param("param") WebHookLogParam param);
}
