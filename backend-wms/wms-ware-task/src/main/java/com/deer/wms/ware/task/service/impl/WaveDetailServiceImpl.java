package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.WaveDetailMapper;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveDetailCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetailDto;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.service.WaveDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ware.task.service.WaveMasterService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/02.
 */
@Service
@Transactional
public class WaveDetailServiceImpl extends AbstractService<WaveDetail, Integer> implements WaveDetailService {

    @Autowired
    private WaveDetailMapper waveDetailMapper;

    @Override
    public List<WaveDetailDto> findList(WaveDetailCriteria criteria) {
        return waveDetailMapper.findList(criteria);
    }

    @Override
    public List<WaveDetail> getByMasterId(Integer waveId) {
        return waveDetailMapper.getByMasterId(waveId);
    }

    @Override
    public void deleteByWaveMasterId(Integer id) {

        WaveDetailCriteria criteria = new WaveDetailCriteria();
        criteria.setWaveId(id);
        waveDetailMapper.deleteByWaveMasterCode(criteria);
    }
}
