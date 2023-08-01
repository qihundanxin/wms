package com.deer.wms.project.root.log.operation.config;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.log.operation.dao.LogDataOperationMapper;
import com.deer.wms.project.root.log.operation.model.LogDataOperation;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DataOperationLogTool;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据操作日志工具实现
 * 
 * @author xuesinuo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataOperationLogToolImpl implements DataOperationLogTool {

    private final LogDataOperationMapper mapper;

    @Override
    public <T> void save(DataOperationLog<T> doLog) {
        try {
            LogDataOperation logDo = new LogDataOperation();
            BeanUtils.copyBeanProp(logDo, doLog);
            logDo.setLogDataOperationId(null);// 自增ID
            if (StrUtil.isBlank(logDo.getModule())) {
                throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少Module");
            }
            if (StrUtil.isBlank(logDo.getOperation())) {
                throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少Operation");
            }
            if (logDo.getOperationUserId() == null) {
                throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少OperationUserId");
            }
            if (logDo.getOperationDatetime() == null) {
                logDo.setOperationDatetime(LocalDateTime.now());
            }
            if (doLog.getBeforeData() != null) {
                logDo.setBeforeData(JSON.toJSONString(doLog.getBeforeData()));
            }
            if (doLog.getAfterData() != null) {
                logDo.setAfterData(JSON.toJSONString(doLog.getAfterData()));
            }
            logDo.setUuid(IdUtil.fastSimpleUUID());
            mapper.save(logDo);
        } catch (Exception e) {
            log.error("DataOperationLogTool出现异常，导致数据库日志可能记录失败：\n" + doLog.toString(), e);
        }
    }
}
