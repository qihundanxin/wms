package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.statistics.TopStatisticsDto;
import com.deer.wms.ware.task.model.statistics.TopStatisticsRequest;

public interface StatisticsService {

    TopStatisticsDto topStatistics(TopStatisticsRequest request);
}
