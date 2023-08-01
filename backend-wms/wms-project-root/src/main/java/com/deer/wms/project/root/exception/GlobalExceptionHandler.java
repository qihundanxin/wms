package com.deer.wms.project.root.exception;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.util.Np;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 * <p>
 * Created by guo on 2018/11/15.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(HttpServletRequest request, ServiceException ex) {
        if (ex.getCode() != null && ex.getCode() == CommonCode.SYSTEM_ERROR) {
            Result r = this.handleException(request, ex);
            r.setData(ex.getData());
            return r;
        } else {
            log.info("service exception : {}", ex.getMsg());
        }
        Result r = new Result();
        r.setCode(ex.getCode());
        r.setMessage(ex.getMsg());
        r.setData(ex.getData());
        if (StrUtil.isBlank(r.getMessage())) {
            r.setMessage(ex.getCode().getMessage());
        }
        return r;
    }

    /**
     * 处理文件上传异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    public Result handleMultipartException(HttpServletRequest request, MultipartException e) {
        log.error("upload file error : {}", e.getMessage(), e);
        Result r = new Result();
        r.setCode(CommonCode.UPLOAD_FILE_ERROR);
        if (e.getMessage().indexOf("the request was rejected because its size") != -1) {
            r.setMessage("上传文件过大");
        }
        return r;
    }

    /**
     * 捕获和处理 MethodArgumentNotValidException 方法参数无效的异常信息
     *
     * @param request 请求
     * @param e       异常
     * @return 返回异常信息状态码和异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleBindException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("method argument not valid exception : ", e.getMessage(), e);
        Map<String, String> data = e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        Result r = new Result();
        r.setCode(CommonCode.PARAMETER_ERROR);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            r.setMessage(Np.i(r.getMessage()).o("参数错误：") + key + " " + val + ".");
        }
        return r;
    }

    /**
     * 处理 Exception 异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServletRequest request, Exception ex) {
        String uuid = IdUtil.fastSimpleUUID();
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("[" + uuid + "] ");
        if (request != null) {
            logBuffer.append(" request method=" + request.getMethod());
            logBuffer.append(" url=" + request.getRequestURL());
            logBuffer.append(" params=" + request.getParameterMap());
        }
        logBuffer.append(" exception message:" + ex.getMessage());
        log.error(logBuffer.toString(), ex);
        Result r = new Result();
        r.setCode(CommonCode.SYSTEM_ERROR);
        r.setMessage("服务器出错啦，请联系管理员(" + uuid.substring(0, 8) + ")");
        return r;
    }
}
