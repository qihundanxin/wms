package com.deer.wms.pdaApi.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.ware.task.model.pickTask.MyBatchKanbanRespVO;
import com.deer.wms.ware.task.service.PickBatchService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 任务中心
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/taskCenter")
@RequiredArgsConstructor
public class PdaTaskCenterController {

    @Autowired
    private PickBatchService pickBatchService;

    @Data
    public static class GetCountParam{
        // 时间 (选填，默认今天)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date date;
    }

    /**
     * 我的任务统计
     * @author luolin
     * @param param
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getCount")
    @Authority("pda_taskCenter_getCount")
    public SimpleMap getCount(@RequestBody @Validated GetCountParam param, @PdaUser CurrentUser currentUser){
        if (param.getDate() == null) {// 默认今天
            param.setDate(DateUtils.now());
        }
        Date date = param.getDate();
        Date startDate = DateUtils.getDateStartTime(date);
        Date endDate = DateUtils.addDay(startDate, 1);
        // 返回数据源
        MyBatchKanbanRespVO result = pickBatchService.getBatchKanbanByDate(startDate, endDate, currentUser.getUserId());
        int myPickBatch = result.getMyTodaySum() - result.getMyTodayFinished();
        SimpleMap simpleMap = SimpleMap.init().append("datetime", date).append("myPickBatch", myPickBatch);
        return simpleMap;
    }
}
