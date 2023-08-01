package com.deer.wms.ware.task.web;

import javax.annotation.Resource;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.statistics.TopStatisticsDto;
import com.deer.wms.ware.task.model.statistics.TopStatisticsRequest;
import com.deer.wms.ware.task.service.StatisticsService;

@RestController
@RequestMapping("/home/statistics")
public class HomeStatisticsController {

    @Resource
    private StatisticsService statisticsService;

    @PostMapping("/topStatistics")
    @Authority("home_statistics_topStatistics")
    public Result topStatistics(@RequestBody TopStatisticsRequest request) {
        TopStatisticsDto topStatisticsDto = statisticsService.topStatistics(request);
        return ResultGenerator.genSuccessResult(topStatisticsDto);
    }

}
