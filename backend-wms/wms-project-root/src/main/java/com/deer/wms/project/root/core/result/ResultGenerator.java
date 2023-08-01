package com.deer.wms.project.root.core.result;

/**
 * 响应结果生成工具
 * <p>
 * Created by guo on 2018/11/15.
 */
public class ResultGenerator {

    public static Result genSuccessResult() {
        return genSuccessResult(CommonCode.SUCCESS);
    }

    public static Result genSuccessResult(Code code) {
        return new Result().setCode(code);
    }

    public static Result genSuccessResult(Object data) {
        return genSuccessResult().setData(data);
    }

    public static Result genSuccessResult(Code code, Object data) {
        return genSuccessResult(code).setData(data);
    }

    public static Result genSuccessResult(Code code, String message, Object data) {
        return genSuccessResult(code).setMessage(message).setData(data);
    }
}
