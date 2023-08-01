package com.deer.wms.project.root.queue.config;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;

import com.deer.wms.project.root.util.QueueTool;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class QueueConfig implements QueueTool {
    private final RedissonClient redisson;

    @Override
    public <T> Queue<T> getQueue(String queueKey) {
        return redisson.getQueue(queueKey);
    }

    @Override
    public <T> BlockingQueue<T> getBlockingQueue(String queueKey) {
        return redisson.getBlockingQueue(queueKey);
    }
}
