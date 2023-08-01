package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Wave.WaveDetailCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetailDto;

import java.util.List;

/**
 * Created by ll on 2020/02/02.
 */
public interface WaveDetailService extends Service<WaveDetail, Integer> {
    List<WaveDetailDto> findList(WaveDetailCriteria criteria);

    List<WaveDetail> getByMasterId(Integer waveId);

    void deleteByWaveMasterId(Integer id);
}
