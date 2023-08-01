package com.deer.wms.ware.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.busine.tactic.service.WaveTacticDianshangService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.BillNoUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.dao.WaveMasterMapper;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.Wave.WaveMasterCriteria;
import com.deer.wms.ware.task.model.Wave.WaveMasterDto;
import com.deer.wms.ware.task.model.Wave.WaveState;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveDetailService;
import com.deer.wms.ware.task.service.WaveMasterService;

/**
 * Created by ll on 2020/02/02.
 */
@Service
@Transactional
public class WaveMasterServiceImpl extends AbstractService<WaveMaster, Integer> implements WaveMasterService {

    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private WaveDetailService waveDetailService;
    @Autowired
    private WareInfoService wareInfoService;
    @Autowired
    private WaveMasterMapper waveMasterMapper;
    @Autowired
    private WaveTacticDianshangService waveTacticDianshangService;
    @Autowired
    private PickTaskService pickTaskService;

    @Autowired
    private SoDetailService soDetailService;

    @Override
    public WaveState getWaveState(Integer waveId) {
        return waveMasterMapper.getWaveState(waveId);
    }

    @Override
    public boolean generateWaves(WaveTacticDianshangDto waveTactic) {

        // 根据每条策略生成波次

        String now = DateUtils.getNowDateTimeString();
        /**
         * 线程每时每刻都在刷新，同一个波次策略生成波次只能在设置的最大分钟数内，否则该策略的下一个波次 不同的仓库，波次策略不一样
         */
        Integer maxTime = waveTactic.getMaxWaitTime();

        SoMasterCriteria soMasterCriteria = generateSoMasterCriteria(waveTactic);
        Integer wareId = soMasterCriteria.getWareId();
        soMasterCriteria.setPageSize(500);

        soMasterCriteria.setWareId(wareId);
        List<SoMaster> soMasters = soMasterService.findSoByWave(soMasterCriteria);
//            if(soMasters.size()==0){
//                continue;
//            }
        WaveMaster last = waveMasterMapper.getLast(waveTactic.getWaveTacticCode(), wareId);
        String nowTime = DateUtils.getNowDateTimeString();
        String LastTime = last == null ? nowTime : last.getCreateTime();
        Long fadsf = DateUtils.getMin(LastTime, nowTime);
        if (last != null && fadsf >= maxTime) {
            // last.setOrganizationName("时间到");
            last.setEndType(1);
            last.setState(1);
            update(last);
            pickTaskService.mergeTask();
        }
        if (last != null && last.getTotalBillCount() >= waveTactic.getSoCountLimit()) {
            last.setEndType(2);
            // last.setOrganizationName("订单数");
            last.setState(1);
            update(last);
            pickTaskService.mergeTask();
        }
        if (last != null && last.getTotalSkuCount() >= waveTactic.getItemCount()) {
            // last.setOrganizationName("SKU数");
            last.setEndType(3);
            last.setState(1);
            update(last);
            pickTaskService.mergeTask();
        }
        if (soMasters.size() > 0) {

            // 该策略第一次生成 或者 该波次要生成下一个波次
            if (fadsf >= maxTime || last == null) {
                // 第一次生成
                WareInfo ware = wareInfoService.findById(waveTactic.getWareId());
                WaveMaster waveMaster = new WaveMaster();
                waveMaster.setCreateTime(now);
                waveMaster.setCode(BillNoUtils.generateWaveCode(waveTactic.getWareId()));
                waveMaster.setWareId(waveTactic.getWareId());
                waveMaster.setWareName(ware.getWareName());
                waveMaster.setWaveTacticCode(waveTactic.getWaveTacticCode());
                waveMaster.setTotalSkuCount(0);
                waveMaster.setTotalBillCount(0);
                waveMaster.setIsSeeding(waveTactic.getIsSeeding());

                soMasters: for (SoMaster soMaster : soMasters) {
                    Integer count = waveMasterMapper.countSku(waveMaster.getWaveId(), soMaster.getSoMasterId()).size();
                    if (waveMaster.getWaveId() != null && (count > waveTactic.getItemCount() || waveMaster.getTotalBillCount() >= waveTactic.getSoCountLimit())) {
                        waveMaster.setState(1);
                        waveMaster.setEndType(4);
                        update(waveMaster);
                        pickTaskService.mergeTask();
                        break soMasters;
                    } else {
                        waveMaster.setTotalSkuCount(count);
                        waveMaster.setTotalBillCount(waveMaster.getTotalBillCount() + 1);
                        if (waveMaster.getWaveId() == null) {
//                                waveTactic.setLastTime(nowTime);
//                                waveTacticDianshangService.update(waveTactic);
                            save(waveMaster);
                        } else {
                            update(waveMaster);
                        }
//                            WaveDetail waveDetail = new WaveDetail();
//                            waveDetail.setSoMasterId(soMaster.getSoMasterId());
//                            waveDetail.setWaveMasterId(waveMaster.getWaveId());
//                            waveDetailService.save(waveDetail);
                        soMaster.setWaveMasterId(waveMaster.getWaveId());
                        SoMaster soMaster1 = new SoMaster();
                        soMaster1.setSoMasterId(soMaster.getSoMasterId());
                        soMaster1.setState(soMaster.getState());// berwin 2022-04 modify error.log
                        soMaster1.setVersion(soMaster.getVersion());
                        soMaster1.setWaveMasterId(soMaster.getWaveMasterId());
                        soMasterService.update(soMaster1);

                    }
                }

            } else {
                // 同一波次
                // waveTactic.setLastTime(nowTime);
                List<WaveDetail> list = waveDetailService.getByMasterId(last.getWaveId());
                Integer soCount = list.size() - 1;
                soMasters: for (SoMaster soMaster : soMasters) {
                    soCount += 1;

                    // List<SoDetail> soDetails = soDetailService.findByBillNo(soMaster.getBillNo());
                    Integer count = waveMasterMapper.countSku(last.getWaveId(), soMaster.getSoMasterId()).size();
                    if (count > waveTactic.getItemCount()) {

                        // last.setOrganizationName("SKU数2");
                        last.setState(1);
                        last.setEndType(5);
                        update(last);
                        pickTaskService.mergeTask();

                    } else if (soCount >= waveTactic.getSoCountLimit()) {
                        // last.setOrganizationName("订单数2");
                        last.setState(1);
                        last.setEndType(7);
                        update(last);
                        pickTaskService.mergeTask();
                    } else {
//                            WaveDetail waveDetail = new WaveDetail();
//                            waveDetail.setSoMasterId(soMaster.getSoMasterId());
//                            waveDetail.setWaveMasterId(last.getWaveId());
//                            waveDetailService.save(waveDetail);
                        last.setTotalSkuCount(count);
                        last.setTotalBillCount(last.getTotalBillCount() + 1);
                        update(last);
                        soMaster.setWaveMasterId(last.getWaveId());
                        SoMaster soMaster1 = new SoMaster();
                        soMaster1.setSoMasterId(soMaster.getSoMasterId());
                        soMaster1.setState(soMaster.getState());// berwin 2022-04 modify error.log
                        soMaster1.setWaveMasterId(soMaster.getWaveMasterId());
                        soMaster1.setVersion(soMaster.getVersion());
                        soMasterService.update(soMaster1);
                    }

                }
            }
        }

        return true;
    }

    @Override
    public WaveMasterDto findByBoxCode11(String boxCode, Integer isSeeding, Integer wareId, Integer userId) {
        return waveMasterMapper.findByBoxCode11(boxCode, isSeeding, wareId, userId);
    }

    @Override
    public void waveState(Integer type) {

        // waveMasterMapper.waveState(type);
        List<WaveMaster> list = findByStateNo0();
        waveMaster: for (WaveMaster waveMaster : list) {
            waveStateByWaveMaster(waveMaster);
        }
    }

    @Override
    public void updateState(Integer waveId, Integer state) {
        waveMasterMapper.updateState(waveId, state);
    }

    @Override
    public void waveStateBySo(Integer waveId) {

        WaveState waveState = getWaveState(waveId);
        if (waveState == null) {
            return;
        }
        // 正在拣货中
        Integer count4 = waveState.getCount4();
        // 部分拣货
        Integer count5 = waveState.getCount5();
        // 拣货完成的
        Integer count6 = waveState.getCount6();
        // 播种完成的
        Integer count7 = waveState.getCount7();
        // 发运完成的
        Integer count8 = waveState.getCount8();

        Integer state = waveState.getState();

        if (count4 == 0 && count7 == 0 && count6 > 0) {
            // 拣货完成
            updateState(waveId, 2);
        }

        if (count6 == 0 && count4 == 0 && count8 == 0 && count7 > 0) {
            // 播种完成
            updateState(waveId, 3);
        }

        if (count4 == 0 && count6 == 0 && count7 == 0 && count8 > 0) {
            // 发运完成
            updateState(waveId, 4);
        }

        if (count4 == 0 && count5 == 0 && count6 == 0 && count7 == 0 && count8 == 0) {
            // 结束（订单全部取消，或者应为拣货异常每一个能正常进行的）
            updateState(waveId, 5);
        }

        if (count5 > 0) {
            if (state > 1) {
                updateState(waveId, 11);
            }

        }

    }

    // @Async
    @Override
    public void waveStateByWaveId(Integer waveId) {
        WaveMaster waveMaster = super.findById(waveId);
        waveStateByWaveMaster(waveMaster);
    }

    @Override
    public void waveStateByWaveMaster(WaveMaster waveMaster) {
        // 正在拣货中
        Integer count4 = soMasterService.findCountByWaveIdAndState(waveMaster.getWaveId(), 4);
        // 部分拣货
        Integer count5 = soMasterService.findCountByWaveIdAndState(waveMaster.getWaveId(), 5);
        // 拣货完成的
        Integer count6 = soMasterService.findCountByWaveIdAndState(waveMaster.getWaveId(), 6);
        // 播种完成的
        Integer count7 = soMasterService.findCountByWaveIdAndState(waveMaster.getWaveId(), 7);
        // 发运完成的
        Integer count8 = soMasterService.findCountByWaveIdAndState(waveMaster.getWaveId(), 8);

        if (count4 == 0 && count7 == 0 && count6 > 0) {
            // 拣货完成
            waveMaster.setState(2);
            update(waveMaster);
        }

        if (count6 == 0 && count4 == 0 && count8 == 0 && count7 > 0) {
            // 播种完成
            waveMaster.setState(3);
            update(waveMaster);
        }

        if (count4 == 0 && count6 == 0 && count7 == 0 && count8 > 0) {
            // 发运完成
            waveMaster.setState(4);
            update(waveMaster);
        }

        if (count4 == 0 && count5 == 0 && count6 == 0 && count7 == 0 && count8 == 0) {
            // 结束（订单全部取消，或者应为拣货异常每一个能正常进行的）
            waveMaster.setState(5);
            update(waveMaster);
        }

        if (count5 > 0) {
            if (waveMaster.getState() > 1) {
                waveMaster.setState(11);
                update(waveMaster);
            }

        }
    }

    @Override
    public List<WaveMaster> findByState(Integer state) {
        return waveMasterMapper.findByState(state);
    }

    @Override
    public List<WaveMaster> findByStateNo0() {
        return waveMasterMapper.findByStateNo0();
    }

    @Override
    public List<WaveMasterDto> findList(WaveMasterCriteria criteria) {
        return waveMasterMapper.findList(criteria);
    }

    @Override
    public List<WaveMasterDto> findByUser(Integer userId, Integer wareId) {
        return waveMasterMapper.findByUser(userId, wareId);
    }

    @Override
    public List<WaveMasterDto> allotWave(WaveMasterCriteria criteria) {
        return waveMasterMapper.allotWave(criteria);
    }

    @Override
    public WaveMaster findByBoxCode(String boxCode, Integer wareId) {
        return waveMasterMapper.findByBoxCode(boxCode, wareId);
    }

    @Override
    public WaveMaster findByBoxCode2(String boxCode, Integer wareId) {
        return waveMasterMapper.findByBoxCode2(boxCode, wareId);
    }

    @Override
    public WaveMaster findBySeedingWallCode(String seedingWallCode, Integer wareId) {
        return waveMasterMapper.findBySeedingWallCode(seedingWallCode, wareId);
    }

    @Override
    public WaveMaster findByYdYpByBoxCode(String boxCode, Integer wareId) {
        return waveMasterMapper.findByYdYpByBoxCode(boxCode, wareId);
    }

    @Override
    public List<WaveMaster> findByWaveIds(List<Integer> waveIds) {
        return waveMasterMapper.findByWaveIds(waveIds);
    }

    private SoMasterCriteria generateSoMasterCriteria(WaveTacticDianshang waveTactic) {
        SoMasterCriteria criteria = new SoMasterCriteria();
        if (waveTactic.getWareId() != null) {
            criteria.setWareId(waveTactic.getWareId());
        }
        if (waveTactic.getCustomerId() != null && waveTactic.getCustomerId() != 0) {
            criteria.setCustomerId(waveTactic.getCustomerId());
        }
        if (waveTactic.getCarrierId() != null && waveTactic.getCarrierId() != 0) {
            criteria.setCarrierId(waveTactic.getCarrierId());
        }

        if (waveTactic.getSoType() != null) {
            criteria.setSoType(waveTactic.getSoType());
        }
        if (waveTactic.getItemCount() != null) {
            criteria.setWaveItemCount(waveTactic.getItemCount());
        }
        if (waveTactic.getSoStructure() != null) {
            criteria.setSoStructure(waveTactic.getSoStructure());
        }
        return criteria;
    }
}
