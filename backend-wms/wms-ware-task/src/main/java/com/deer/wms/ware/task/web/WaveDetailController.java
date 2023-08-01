package com.deer.wms.ware.task.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveDetailCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetailDto;
import com.deer.wms.ware.task.model.Wave.WaveDetailInsertData;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.service.WaveDetailService;
import com.deer.wms.ware.task.service.WaveMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/02/02.
 *
 * 波次明细
 */
@RestController
@RequestMapping("/wave/details")
public class WaveDetailController {

    @Autowired
    private WaveDetailService waveDetailService;
    @Autowired
    private WaveMasterService waveMasterService;

    @PostMapping
    @Authority("wave_details_add")
    public Result add(@RequestBody WaveDetail waveDetail) {
        waveDetailService.save(waveDetail);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/addFromSo")
    @Authority("wave_details_addFromSo")
    public Result addFromSo(@RequestBody WaveDetailInsertData insertData) {
        WaveMaster wave = waveMasterService.findById(insertData.getWaveMasterId());
        for (Integer soId : insertData.getSoMasterIds()) {
            List<WaveDetailDto> waveDetail1 = waveDetailService.findList(new WaveDetailCriteria(soId));
            if (waveDetail1.size() > 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "选中工单中有已添加批次");
            }
            WaveDetail waveDetail = new WaveDetail();
            waveDetail.setWaveMasterId(wave.getWaveId());
            waveDetail.setSoMasterId(soId);
            waveDetailService.save(waveDetail);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("wave_details_delete")
    public Result delete(Integer id) {
        waveDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("wave_details_update")
    public Result update(@RequestBody WaveDetail waveDetail) {
        waveDetailService.update(waveDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("wave_details_get")
    public Result detail(@PathVariable Integer id) {
        WaveDetail waveDetail = waveDetailService.findById(id);
        return ResultGenerator.genSuccessResult(waveDetail);
    }

    @GetMapping
    @Authority("wave_details_list")
    public Result list(WaveDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WaveDetailDto> list = waveDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
