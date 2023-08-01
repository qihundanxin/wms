package com.deer.wms.project.root.log.work.bean;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 代码工作日志
 * 
 * @author xuesinuo
 */
@Data
public class WorkLogBean {
    /**
     * 代码工作模式
     */
    public static interface WorkType {
        int UNKNOW = 0;
        int HTTP = 1;
        int CRON = 2;
        int TCP = 3;
    }

    /**
     * 开始时间
     */
    private Date begin;
    /**
     * 结束时间
     */
    private Date end;
    /**
     * 上级链路ID
     */
    private String traceId;
    /**
     * 本项目的工作ID
     */
    private String workId;
    /**
     * 代码工作模式
     */
    private Integer workType;
    /**
     * 工作队列
     */
    private List<WorkUnitLogBean> workList;
    /**
     * 来源信息（根据工作模式不同，记录内容也不一样）必须是可Json序列化的内容。
     */
    private Object fromInfo;
    /**
     * 来源是网络请求时，记录来源IP
     */
    private String ip;
    /**
     * 工作流成功完成
     */
    private Boolean success = true;
    /**
     * 如果有，记录用户ID
     */
    private Long userId;

    private Boolean resultTooLong = false;

    /**
     * @return 总耗时
     */
    public Long getTime() {
        if (begin != null && end != null) {
            return end.getTime() - begin.getTime();
        }
        return null;
    }
}
