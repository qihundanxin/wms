package com.deer.wms.project.root.log.work.bean;

import java.util.Date;

import lombok.Data;

/**
 * 工作单元记录
 * 
 * @author xuesinuo 2022-01-20
 */
@Data
public class WorkUnitLogBean {
    /**
     * 开始时间
     */
    private Date begin;
    /**
     * 结束时间
     */
    private Date end;
    /**
     * 执行方法全限定名
     */
    private String method;
    /**
     * 传入参数toString
     */
    private String[] args;
    /**
     * 返回结果toString
     */
    private String result;
    /**
     * 出现的异常，按照任意易懂的方式进行格式化
     */
    private String throwable;
    /**
     * 备注（500字符以内）
     */
    private String remark;

    /**
     * 设置异常信息，按照一定格式写入throwable
     * 
     * @param throwable 异常信息
     * @return this
     */
    public WorkUnitLogBean putThrowable(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        if (throwable != null) {
            sb.append(throwable.getClass().getName()).append("\n");
            int i = 0;
            for (StackTraceElement ste : throwable.getStackTrace()) {
                if (ste.getClassName().indexOf("com.deer.wms") >= 0) {
                    sb.append(ste).append("\n");
                    if (++i > 10) {
                        break;
                    }
                }
            }
            this.throwable = sb.toString();
        }
        return this;
    }

    /**
     * @return 耗时
     */
    public Long getTime() {
        if (begin != null && end != null) {
            return end.getTime() - begin.getTime();
        }
        return null;
    }
}
