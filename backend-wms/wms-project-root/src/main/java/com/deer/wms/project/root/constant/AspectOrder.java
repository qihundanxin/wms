package com.deer.wms.project.root.constant;

/**
 * 切面顺序统一配置<br>
 * <br>
 * 数字越大，越早代理<br>
 * 数字越小，越早执行around<br>
 * 
 * @author xuesinuo
 */
public interface AspectOrder {
    /*
     * other
     */
    /**
     * 手动标注的日志记录
     */
    int ANNOTATION_LOGGER_CONTROLLER = 1;
//    /**
//     * 权限标识
//     */
//    int AUTH = 2;
    /*
     * controller
     */
    /**
     * 工作日志
     */
    int LOGGER_CONTROLLER = 3;
    /**
     * 接口权限
     */
    int AUTH_CONTROLLER = 4;

    /*
     * service
     */
    /**
     * 分布式定时任务，秒级同步任务，请务必先执行
     */
    int CRON_DISTRIBUTED = 5;
//    /**
//     * 定时任务控制切面
//     */
//    int SCHEDULED = 6;
    /**
     * 工作日志
     */
    int LOGGER_SERVICE = 7;
    /**
     * 同步代码块
     */
    int SYNC = 8;
}
