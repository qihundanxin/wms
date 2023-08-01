package com.deer.wms.project.root.util;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * 消息队列工具
 * 
 * @author xuesinuo
 */
public interface QueueTool {
    /**
     * 获取消息队列
     * 
     * @param <T>      消息数据类型
     * @param queueKey 队列Key
     * @return 队列
     */
    <T> Queue<T> getQueue(String queueKey);

    /**
     * 获取阻塞队列
     * 
     * @param <T>      消息数据类型
     * @param queueKey 队列Key
     * @return 队列
     */
    <T> BlockingQueue<T> getBlockingQueue(String queueKey);
}
