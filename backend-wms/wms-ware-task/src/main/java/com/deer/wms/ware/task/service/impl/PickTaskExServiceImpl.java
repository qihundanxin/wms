package com.deer.wms.ware.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.ware.task.dao.PickTaskExMapper;
import com.deer.wms.ware.task.model.PickTaskEx;
import com.deer.wms.ware.task.model.PickTaskExCriteria;
import com.deer.wms.ware.task.service.PickTaskExService;

/**
 * Created by on 2021/03/20.
 */
@Service
@Transactional
public class PickTaskExServiceImpl extends AbstractService<PickTaskEx, Integer> implements PickTaskExService {

    @Autowired
    private PickTaskExMapper pickTaskExMapper;

    @Override
    public PickTaskEx findById(Integer id) {
        return pickTaskExMapper.findById(id);
    }

    @Override
    public int update(PickTaskEx pickTaskEx) {
        int rowCount = pickTaskExMapper.update(pickTaskEx);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.PICK_TASK_VERSION, "update pickTaskEx更新了0条数据：" + pickTaskEx.getPickTaskId());
        }
        return rowCount;
    }

    @Override
    public List<PickTaskEx> findList(PickTaskExCriteria criteria) {
        return pickTaskExMapper.findList(criteria);
    }
}
