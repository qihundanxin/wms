package com.deer.wms.project.root.core.result;

import com.alibaba.fastjson2.JSON;

/**
 * 统一API响应结果封装
 * <p>
 * Created by guo on 2018/11/15.
 */
public class Result {
    /**
     * 业务处理的状态代码
     */
    private int code;

    /**
     * 业务处理的状态提示信息
     */
    private String message;

    /**
     * 业务处理的返回数据
     */
    private Object data;

    public Result setCode(Code code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        return this;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}