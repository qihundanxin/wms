package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendDetailParam;
import com.deer.wms.ware.task.service.SendDetailService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixuehe
 * @date 2023/3/3
 * 发运记录
 */

@RestController
@RequestMapping("/send")
public class sendDetailController {

    @Autowired
    private SendDetailService sendDetailService;

    @Autowired
    private SoDetailService soDetailService;


    /**
     * 本日发运记录
     * @param criteria
     * @param currentUser
     * @return
     */
    @GetMapping("/list/today")
    @Authority("send_list_today")
    public Result findList(SendDetailParam criteria,@User CurrentUser currentUser){
        criteria.setSendUserId(currentUser.getUserId());
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,+1);
        Date endTime = calendar.getTime();
        criteria.setStartSendTime(startTime);
        criteria.setEndSendTime(endTime);
        List<SendDetail> list = sendDetailService.findList(criteria);
        list = list.stream().collect(Collectors.toMap(SendDetail::getSoBillNo,s->s,(s1,s2)->{
            s1.setSendQuantity(s1.getSendQuantity()+s2.getSendQuantity());
            return s1;
        })).values().stream().collect(Collectors.toList());
        for(SendDetail send:list){
            //根据单号查询总数
            SoDetailCriteria soDetailCriteria = new SoDetailCriteria();
            soDetailCriteria.setBillNo(send.getSoBillNo());
            List<SoDetailDto> details = soDetailService.findList(soDetailCriteria);
            Double orderQuantity=0.0;
            for(SoDetailDto detail:details){
                orderQuantity += detail.getOrderQuantity();
            }
            send.setOrderQuantity(orderQuantity);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
