package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveDetailCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetailDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaveDetailMapper extends Mapper<WaveDetail> {
    List<WaveDetailDto> findList(WaveDetailCriteria criteria);

    void deleteByWaveMasterCode(WaveDetailCriteria criteria);

    List<WaveDetail> getByMasterId(@Param("waveId") Integer waveId);
}