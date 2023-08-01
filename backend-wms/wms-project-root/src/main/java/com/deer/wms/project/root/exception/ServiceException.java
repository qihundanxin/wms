package com.deer.wms.project.root.exception;

import com.deer.wms.project.root.core.result.Code;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see GlobalExceptionHandler
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Code code;
    private String msg;
    private Object data;

    public ServiceException(Code code) {
        super(code.getMessage());
        this.code = code;
        this.msg = this.code.getMessage();
    }

    public ServiceException(Code code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(Code code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ServiceException(Code code, String msg, Object data, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 获取业务错误代码
     *
     * @return 业务错误代码
     */
    public Code getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
