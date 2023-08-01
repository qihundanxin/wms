package com.deer.wms.ware.task.service;

import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Wave.WaveMasterCriteria;
import com.deer.wms.ware.task.model.Wave.WaveMasterDto;
import com.deer.wms.ware.task.model.Wave.WaveState;

import java.util.List;

/**
 * Created by ll on 2020/02/02.
 */
public interface WaveMasterService extends Service<WaveMaster, Integer> {
    WaveState getWaveState(Integer waveId);
    boolean generateWaves(WaveTacticDianshangDto waveTacticDianshang);

    WaveMasterDto   findByBoxCode11(String boxCode,Integer isSeeding,Integer wareId,Integer userId);

    void updateState(Integer waveId,Integer state);
    void waveStateBySo(Integer waveId) ;
    void waveState(Integer type);

    public void waveStateByWaveId(Integer waveId);

    public void waveStateByWaveMaster(WaveMaster waveMaster);

    List<WaveMaster> findByState(Integer state);

    List<WaveMaster> findByStateNo0();

    List<WaveMasterDto> findList(WaveMasterCriteria criteria);
    List<WaveMasterDto> findByUser(Integer userId,Integer wareId);

    List<WaveMasterDto> allotWave(WaveMasterCriteria criteria);

    WaveMaster findByBoxCode(String boxCode, Integer wareId);

    WaveMaster findByBoxCode2(String boxCode, Integer wareId);

    WaveMaster findBySeedingWallCode(String seedingWallCode, Integer wareId);

    WaveMaster findByYdYpByBoxCode(String boxCode, Integer wareId);

    List<WaveMaster> findByWaveIds(List<Integer> waveIds);
}
