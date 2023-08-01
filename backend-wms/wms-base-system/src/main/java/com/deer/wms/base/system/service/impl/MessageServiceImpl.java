package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.MessageMapper;
import com.deer.wms.base.system.model.Message.Message;
import com.deer.wms.base.system.service.MessageService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2019/11/25.
 */
@Service
@Transactional
public class MessageServiceImpl extends AbstractService<Message, Integer> implements MessageService {

    @Autowired
    private MessageMapper noticeMapper;

}
