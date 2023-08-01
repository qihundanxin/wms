package com.deer.wms.ware.task.dao;

import java.util.List;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;

/**
 * 拣货任务执行记录
 * 
 * @author xuesinuo
 *
 */
public interface PickTaskExecuteMapper extends Mapper<PickTaskExecute> {

    /**
     * 根据二维码查询
     * 
     * @param qrCode 二维码
     * @return 结果
     */
    List<PickTaskExecute> selectByQrCode(String qrCode);
}
