package com.deer.wms.ware.task.service;

import java.util.List;

import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;

/**
 * 拣货任务执行情况
 * 
 * @author xuesinuo
 *
 */
public interface PickTaskExecuteService {

    /**
     * 根据二维码查询（包含成功与失败）
     * 
     * @param qrCode 二维码
     * @return 匹配到的结果
     */
    List<PickTaskExecute> getByQrCode(String qrCode);

    /**
     * 非事务保存！即使在拣货任务失败时也能保存。
     * 
     * @param pickTaskExecute 执行记录
     * @return 执行记录
     */
    PickTaskExecute save(PickTaskExecute pickTaskExecute);
}
