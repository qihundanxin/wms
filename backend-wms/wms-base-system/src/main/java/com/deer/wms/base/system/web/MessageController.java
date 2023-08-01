package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Message.Message;
import com.deer.wms.base.system.model.Message.MessageCriteria;
import com.deer.wms.base.system.service.MessageService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 系统信息api接口
 * 
 * Created by guo on 2019/11/25.
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/insert")
    @Authority("messages_insert")
    public Result add(@RequestBody Message message) {
        messageService.save(message);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("messages_delete")
    public Result delete(Integer id) {
        messageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("messages_update")
    public Result update(@RequestBody Message message) {
        messageService.update(message);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("messages_detail")
    public Result detail(Integer id) {
        Message message = messageService.findById(id);
        return ResultGenerator.genSuccessResult(message);
    }

    @GetMapping("/list")
    @Authority("messages_list")
    public Result list(MessageCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<Message> list = messageService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
