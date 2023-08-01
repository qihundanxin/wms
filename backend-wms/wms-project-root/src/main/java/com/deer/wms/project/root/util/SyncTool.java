package com.deer.wms.project.root.util;

import com.deer.wms.project.root.sync.pojo.SyncParam;

/**
 * 编程式分布式同步锁，效果等同声明式事务锁@Synchronized。
 * 
 * @author xuesinuo
 *
 */
public interface SyncTool {
    /**
     * 更多配置的实现方式：
     * 
     * <pre>
     * SyncParam lockParam = new SyncParam();
     * // setting lockParam...
     * try (Closer closer = syncTool.lock(lockParam)) {
     *     // do something...
     *     if (i_want_end_the_lock_early == true) {
     *         lockParam.close();
     *     }
     *     // do something else...
     * }
     * </pre>
     * 
     * @param param 上锁参数
     * @return 锁关闭器
     * @throws Exception
     */
    Closer lock(SyncParam param);

    /**
     * 简单常用的实现方式：
     * 
     * <pre>
     * try (Closer closer = syncTool.lock(new SyncParam(LockName.PICK_AFFIRM, param.getPickTaskId().toString()))) {
     *     // do something...
     * }
     * </pre>
     * 
     * @param lockName  锁的名称，一般为枚举、公共常量
     * @param lockParam 锁参数，一般为被将要操作的数据的Id、Key
     * @return 锁关闭器
     */
    Closer lock(String lockName, String lockParam);

    /**
     * 锁关闭器。使用JDK7的AutoCloseable语法，并且屏蔽掉关锁过程中的异常，因此使用抽象类。
     * 
     * @author xuesinuo
     *
     */
    public static abstract class Closer implements AutoCloseable {
        public abstract void close();
    }
}
