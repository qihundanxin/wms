package com.deer.wms.webhook.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.SqlUtil;
import com.deer.wms.webhook.dao.WebHookLogMapper;
import com.deer.wms.webhook.model.WebHookLog;
import com.deer.wms.webhook.model.WebHookLogParam;
import com.deer.wms.webhook.service.WebHookLogService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebHookLogServiceImpl implements WebHookLogService {

    private final WebHookLogMapper dao;

    @Override
    public WebHookLog add(WebHookLog entity) {
        dao.insert(entity);
        return entity;
    }

    @Override
    public List<WebHookLog> getList(WebHookLogParam param) {
        // 非法参数判断
        if (param.getSendDatetimeBegin() == null || param.getSendDatetimeEnd() == null) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "webhook日志查询必须指定开始、结束时间");
        }
        if (param.getSendDatetimeBegin().isAfter(param.getSendDatetimeEnd())) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "开始时间必须先于结束时间");
        }
        if (param.getSendDatetimeEnd().isAfter(param.getSendDatetimeBegin().plusDays(3L))) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "webhook日志查询时间跨度最大为3天");
        }
        if (param.getAbility() == null && param.getWebHookSrttingId() == null) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "webhook日志查询必须指定一项推送内容");
        }
        // 查询条件赋值
        if (StrUtil.isNotBlank(param.getKeyLike())) {
            param.setKeyLike(SqlUtil.likeLR(param.getKeyLike()));
        }
        return dao.getList(param);
    }

}
