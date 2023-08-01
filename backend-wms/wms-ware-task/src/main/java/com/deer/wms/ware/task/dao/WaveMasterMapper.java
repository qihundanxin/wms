package com.deer.wms.ware.task.dao;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.Wave.WaveMasterCriteria;
import com.deer.wms.ware.task.model.Wave.WaveMasterDto;
import com.deer.wms.ware.task.model.Wave.WaveState;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaveMasterMapper extends Mapper<WaveMaster> {

    void updateState(@Param("waveId")Integer waveId,@Param("state")Integer state);
    WaveState getWaveState(@Param("waveId")Integer waveId);
    void waveState(@Param("type") Integer type);
    WaveMaster findById(@Param("id") Integer id);
    WaveMasterDto findByBoxCode11(@Param("boxCode") String boxCode,@Param("isSeeding")Integer isSeeding,@Param("wareId")Integer wareId,@Param("userId")Integer userId);
    List<WaveMasterDto> findList(WaveMasterCriteria criteria);

    List<WaveMasterDto> allotWave(WaveMasterCriteria criteria);

    WaveMaster getLast(@Param("waveTacticCode") String waveTacticCode, @Param("wareId") Integer wareId);

    List<WaveMaster> findByState(@Param("state") Integer state);

    List<WaveMaster> findByStateNo0();

    List<Integer> countSku(@Param("waveMasterId") Integer waveMasterId, @Param("soMasterId") Integer soMasterId);

    WaveMaster findByBoxCode2(@Param("boxCode") String boxCode, @Param("wareId") Integer wareId);

    WaveMaster findByBoxCode(@Param("boxCode") String boxCode, @Param("wareId") Integer wareId);

    WaveMaster findByYdYpByBoxCode(@Param("boxCode") String boxCode, @Param("wareId") Integer wareId);

    WaveMaster findBySeedingWallCode(@Param("seedingWallCode") String seedingWallCode, @Param("wareId") Integer wareId);

    List<WaveMaster> findByWaveIds(@Param("list") List<Integer> waveIds);

    List<WaveMasterDto> findByUser(@Param("userId")Integer userId, @Param("wareId")Integer wareId);

}
