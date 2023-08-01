package com.deer.wms.ware.task.web;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingCellDto;
import com.deer.wms.base.system.model.SeedingWall.SeedingWall;
import com.deer.wms.base.system.service.BoxInfoService;
import com.deer.wms.base.system.service.SeedingCellService;
import com.deer.wms.base.system.service.SeedingWallService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Wave.*;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveDetailService;
import com.deer.wms.ware.task.service.WaveMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ll on 2020/02/02.
 *
 * 波次
 */
@RestController
@RequestMapping("/wave/masters")
public class WaveMasterController {

    @Autowired
    private WaveMasterService waveMasterService;
    @Autowired
    WaveDetailService waveDetailService;

    @PostMapping
    @Authority("wave_masters_add")
    public Result add(@RequestBody WaveMaster waveMaster) {
        waveMasterService.save(waveMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("wave_masters_delete")
    public Result delete(Integer id) {
        waveDetailService.deleteByWaveMasterId(id);
        waveMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/update")
    @Authority("wave_masters_update")
    public Result update(@RequestBody WaveMaster waveMaster) {
        UserInfo userInfo = userInfoService.findById(waveMaster.getAllotUserId());
        waveMaster.setAllotUserName(userInfo.getUserName());
        waveMaster.setAllotTime(DateUtils.getNowDateTimeString());
        waveMasterService.update(waveMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("wave_masters_get")
    public Result detail(@PathVariable Integer id) {
        WaveMaster waveMaster = waveMasterService.findById(id);
        return ResultGenerator.genSuccessResult(waveMaster);
    }

    @GetMapping("/list")
    @Authority("wave_masters_list")
    public Result list(WaveMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WaveMasterDto> list = waveMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/allotWave")
    @Authority("wave_masters_allotWave")
    public Result allotWave(WaveMasterCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setUserId(currentUser.getUserId());
        List<WaveMasterDto> list = waveMasterService.allotWave(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * 转移波次
     * @param waveDetailTransData
     * @return
     */
    @PostMapping("/transToWave")
    @Authority("wave_masters_transToWave")
    public Result transToWave(@RequestBody WaveDetailTransData waveDetailTransData) {
        WaveMaster wave = waveMasterService.findById(waveDetailTransData.getWaveId());
        for (Integer waveDetailId : waveDetailTransData.getWaveDetailIds()) {
            WaveDetail waveDetail = waveDetailService.findById(waveDetailId);
            waveDetail.setWaveMasterId(wave.getWaveId());
            waveDetailService.update(waveDetail);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 分配拣货人
     * @param waveMasterId
     * @param userId
     * @param userName
     * @return
     */
    @GetMapping("/allotPickUser")
    @Authority("wave_masters_allotPickUser")
    public Result allotPickUser(Integer waveMasterId, Integer userId, String userName) {
        WaveMaster wave = waveMasterService.findById(waveMasterId);
        wave.setAllotTime(DateUtils.getNowDateTimeString());
        wave.setAllotUserId(userId);
        wave.setAllotUserName(userName);
        waveMasterService.update(wave);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/allotPickUserAndBox")
    @Authority("wave_masters_allotPickUserAndBox")
    public Result allotPickUser(String gh, String boxCode, Integer waveId, Integer wareId,Integer isSeeding) {

        if(isSeeding!=null &&  isSeeding==1){
            BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode,0, wareId);
            if (boxInfo == null    ) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器号无效！");
            }
            WaveMaster waveMaster = waveMasterService.findByBoxCode2(boxCode, wareId);
            if (waveMaster != null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器上一波次未完成，请换一个容器再试！");
            }
        }

        WaveMaster wave = waveMasterService.findById(waveId);
        UserInfo userInfo = userInfoService.findUserInfoByAccount(gh);
        if (userInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此工号无效：" + gh);
        }
        wave.setAllotTime(DateUtils.getNowDateTimeString());
        wave.setAllotUserId(userInfo.getUserId());
        wave.setAllotUserName(userInfo.getUserName());
        wave.setBoxCode(boxCode);
        wave.setState(11);
        waveMasterService.update(wave);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/allotPickUserAndBoxPAD")
    @Authority("wave_masters_allotPickUserAndBoxPAD")
    public Result allotPickUserAndBoxPAD(Integer isSeeding, String boxCode, Integer waveId, Integer wareId
            , @User CurrentUser currentUser) {
        if(isSeeding!=null &&  isSeeding==1){
            BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode,0, wareId);
            if (boxInfo == null    ) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器号无效！");
            }
            WaveMaster waveMaster = waveMasterService.findByBoxCode2(boxCode, wareId);
            if (waveMaster != null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器上一波次未完成，请换一个容器再试！");
            }
        }
        WaveMaster wave = waveMasterService.findById(waveId);

        wave.setAllotTime(DateUtils.getNowDateTimeString());
        wave.setAllotUserId(currentUser.getUserId());
        wave.setAllotUserName(currentUser.getUserName());
        wave.setBoxCode(boxCode);
        wave.setState(11);
        waveMasterService.update(wave);
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private BoxInfoService boxInfoService;
    @Autowired
    private SeedingWallService seedingWallService;
    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private SeedingCellService seedingCellService;

    @GetMapping("/startSeeding")
    @Transactional
    @Authority("wave_masters_startSeeding")
    public Result startSeeding(String boxCode, String seedingWallCode, Integer wareId) {
        BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode,0, wareId);
        if (boxInfo == null) {
            throw new ServiceException(CommonCode.NO_WAVE, "该容器号无效！");
        }
//        if(boxInfo.getState()!=0){
//            throw  new  ServiceException(CommonCode.SERVICE_ERROR,"该容器号被占用");
//        }
        WaveMaster waveMaster = waveMasterService.findByBoxCode(boxCode, wareId);
        if (waveMaster == null) {
            throw new ServiceException(CommonCode.NO_WAVE, "无此波次！");
        }
        SeedingWall seedingWall = seedingWallService.findByCode(seedingWallCode, wareId);
        if (seedingWall == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该分拣墙编码无效！");
        }
//        if(seedingWall.getState()!=0 && !waveMaster.getSeedingWallCode().equals(seedingWallCode)){
//            throw  new  ServiceException(CommonCode.SERVICE_ERROR,"该分拣墙被占用");
//        }
        WaveMaster waveMaster2 = waveMasterService.findBySeedingWallCode(seedingWallCode, wareId);
        if (waveMaster2 != null && !waveMaster.getWaveId().equals(waveMaster2.getWaveId())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此分拣墙正在使用中！("+waveMaster2.getWaveId()+"）");
        }
        if (waveMaster.getSeedingWallCode() != null && !waveMaster.getSeedingWallCode().equals(seedingWallCode)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该波次已经在" + waveMaster.getSeedingWallCode() + "进行播种！");
        }

        waveMaster.setBoxCode(boxCode);
        waveMaster.setSeedingWallCode(seedingWallCode);

        waveMasterService.update(waveMaster);
        seedingWall.setState(1);
        seedingWallService.update(seedingWall);

        List<SoMasterDto> soMasters = soMasterService.findByWaveId(waveMaster.getWaveId(), wareId);
        List<SeedingCellDto> seedingCells = seedingCellService.findByWallCode(seedingWallCode, wareId);
        for (int i = 0; i < soMasters.size(); i++) {
            SoMasterDto soMasterDto = soMasters.get(i);
            SeedingCellDto seedingCell = seedingCells.get(i);
            seedingCell.setBillNo(soMasterDto.getBillNo());
            seedingCell.setSoState(soMasterDto.getState());
            seedingCell.setSoMasterId(soMasterDto.getSoMasterId());
            SoMaster soMaster1 = new SoMaster();
            soMaster1.setSoMasterId(soMasterDto.getSoMasterId());
            soMaster1.setVersion(soMasterDto.getVersion());
            soMaster1.setSeedingCellCode(seedingCell.getSeedingCellCode());
            soMasterService.update(soMaster1);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("seedingWall", seedingWall);
        data.put("seedingCells", seedingCells);
        data.put("waveMaster", waveMaster);

        return ResultGenerator.genSuccessResult(data);
    }

    @GetMapping("/startYdYpSeeding")
    @Transactional
    @Authority("wave_masters_startYdYpSeeding")
    public Result startYdYpSeeding(String boxCode, Integer wareId) {
        BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode,0, wareId);
        if (boxInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器号无效！");
        }

        WaveMaster waveMaster = waveMasterService.findByBoxCode(boxCode, wareId);
        if (waveMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无此波次！");
        }

        waveMaster.setBoxCode(boxCode);

        waveMasterService.update(waveMaster);

        List<SoMasterDto> soMasters = soMasterService.findByWaveId(waveMaster.getWaveId(), wareId);

        Map<String, Object> data = new HashMap<>();

        data.put("soMasters", soMasters);

        return ResultGenerator.genSuccessResult(data);
    }

    @GetMapping("/findByUser")
    @Transactional
    @Authority("wave_masters_findByUser")
    public Result findByUser(String userName, Integer wareId) {
        UserInfo userInfo = userInfoService.findUserInfoByAccount(userName);
        if (userInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "员工无效！（"+userName+"）");
        }

        List<WaveMasterDto> waveMasters = waveMasterService.findByUser(userInfo.getUserId(), wareId);
        if (waveMasters == null || waveMasters.size()==0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无此波次！");
        }
        return ResultGenerator.genSuccessResult(waveMasters);
    }

    @GetMapping("/findByCUser")
    @Transactional
    @Authority("wave_masters_findByCUser")
    public Result findByCUser( Integer wareId, @User CurrentUser currentUser) {
        List<WaveMasterDto> waveMasters = waveMasterService.findByUser(currentUser.getUserId(), wareId);
        if (waveMasters == null || waveMasters.size()==0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无此波次！");
        }
        return ResultGenerator.genSuccessResult(waveMasters);
    }
}
