package com.deer.wms.ware.task.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.StatisticsMapper;
import com.deer.wms.ware.task.model.statistics.StatByDateDto;
import com.deer.wms.ware.task.model.statistics.StatisticsParam;
import com.deer.wms.ware.task.model.statistics.TopStatisticsDto;
import com.deer.wms.ware.task.model.statistics.TopStatisticsRequest;
import com.deer.wms.ware.task.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public TopStatisticsDto topStatistics(TopStatisticsRequest request) {
        if (request.getWareId() == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "缺少【仓库ID】参数");
        }
        StatisticsParam param = new StatisticsParam();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).with(LocalTime.MIN);
        LocalDateTime endTime = Np.i(request).x(x -> x.getEndTime()).o(now);
        LocalDateTime startTime;
        if (Np.i(request).x(x -> x.getDays()).notNull()) {
            startTime = endTime.minusDays(request.getDays()).with(LocalTime.MIN);
        } else if (Np.i(request).x(x -> x.getStartTime()).notNull()) {
            startTime = request.getStartTime();
        } else {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "缺少【开始时间】参数");
        }

        TopStatisticsDto result = new TopStatisticsDto();

        param.setWareId(request.getWareId());
        param.setStartTime(yesterday);
        param.setEndTime(now);
        result.setSoCount(Np.i(statisticsMapper.getSoCount(param)).o(0));
        result.setOosCount(Np.i(statisticsMapper.getOosCount(param)).o(0));
        result.setWaitPickupTotal(Np.i(statisticsMapper.getWaitPickupTotal(param)).o(0));
        result.setWaitShippedCount(Np.i(statisticsMapper.getWaitShippedCount(param)).o(0));
        result.setAsnCount(Np.i(statisticsMapper.getAsnCount(param)).o(0));
        result.setUnQcCount(Np.i(statisticsMapper.getUnQcCount(param)).o(0));

        List<String> cellCodes = statisticsMapper.getCellCode(param);
        Integer cellCount = Np.i(cellCodes).x(x -> x.size()).o(0);
        Integer cellUsed = Np.i(statisticsMapper.getCellUsedCode(param, cellCodes)).x(x -> x.size()).o(0);
        result.setCellUsedRate(cellUsed * 100 / (cellCount == 0 ? 1 : cellCount));
        result.setReceiveTotal(Np.i(statisticsMapper.getReceiveTotal(param)).o(0));
        result.setAcceptedCount(Np.i(statisticsMapper.getAcceptedCount(param)).o(0));
        result.setReturnTotal(Np.i(statisticsMapper.getReturnTotal(param)).o(0));
        result.setReturnedCount(Np.i(statisticsMapper.getReturnedCount(param)).o(0));
        result.setPickupTotal(Np.i(statisticsMapper.getPickupTotal(param)).o(0));
        result.setPickedCount(Np.i(statisticsMapper.getPickedCount(param)).o(0));

        param.setStartTime(startTime);
        param.setEndTime(endTime);
        result.setStatByDateList(this.getStatByDateList(param));
        return result;
    }

    private List<StatByDateDto> getStatByDateList(StatisticsParam param) {
        List<StatByDateDto> result = new ArrayList<StatByDateDto>();
        LocalDateTime startTime = param.getStartTime();
        LocalDateTime endTime = param.getEndTime();
        for (LocalDateTime itemStartTime = startTime; itemStartTime.isBefore(endTime); itemStartTime = itemStartTime.plusDays(1)) {
            param.setStartTime(itemStartTime);
            param.setEndTime(itemStartTime.plusDays(1));
            StatByDateDto resultItem = new StatByDateDto();
            resultItem.setDate(itemStartTime.toLocalDate());
            resultItem.setInboundCount(Np.i(statisticsMapper.getAsnQty(param)).x(x -> x.intValue()).o(0));
            resultItem.setOutboundCount(Np.i(statisticsMapper.getSoQty(param)).x(x -> x.intValue()).o(0));
            result.add(resultItem);
        }
        return result;
    }

    private Double getCellUsedRate(TopStatisticsRequest request) {
        List<Map<String, Object>> cellStatList = statisticsMapper.getCellStat(request);
        int total = 0;
        int used = 0;
        for (Map<String, Object> cellStat : cellStatList) {
            Long cellCount = (Long) cellStat.get("cellCount");
            if (cellCount != 0) {
                used += 1;
            }
            total += 1;
        }
        if (total > 0) {
            return used * 100.0 / total;
        }
        return 0.0;
    }

    private void statByDate(TopStatisticsRequest request, TopStatisticsDto topStatisticsDto) {
        LocalDate currentDate = LocalDate.now();
        LocalDate date = currentDate.minusMonths(1);
        request.setStartTime(date.atStartOfDay());
        List<Field> fieldList = Arrays.asList(
                getDeclaredField("inboundCount"),
                getDeclaredField("outboundCount"));
        List<List<StatByDateDto>> dataList = Arrays.asList(
                statisticsMapper.getInboundCountByDate(request),
                statisticsMapper.getOutboundCountByDate(request));
        List<Map<LocalDate, StatByDateDto>> mapList = new ArrayList<>();
        for (List<StatByDateDto> data : dataList) {
            Map<LocalDate, StatByDateDto> map = data.stream()
                    .collect(Collectors.toMap(StatByDateDto::getDate, item -> item));
            mapList.add(map);
        }
        List<StatByDateDto> statList = new ArrayList<>();
        // 三个月每一天都填充入结果
        while (date.compareTo(currentDate) <= 0) {
            StatByDateDto dto = new StatByDateDto();
            dto.setDate(date);
            LocalDate finalDate = date;
            IntStream.range(0, mapList.size()).forEach(index -> {
                StatByDateDto tmpDto = mapList.get(index).get(finalDate);
                if (tmpDto != null) {
                    try {
                        Field field = fieldList.get(index);
                        Object result = field.get(tmpDto);
                        if (result != null) {
                            field.set(dto, result);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });

            statList.add(dto);
            date = date.plusDays(1);
        }
        topStatisticsDto.setStatByDateList(statList);
    }

    private Field getDeclaredField(String fieldName) {
        try {
            Field field = StatByDateDto.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

}
