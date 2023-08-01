package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.WCS.WcsInfo;

/**
 * Created by  on 2020/06/14.
 *
 * TODO 暂时未增加权限标识
 */
@RestController
@RequestMapping("/wcs/infos")
public class WcsInfoController {

    private static WcsInfo wcsInfo;

    @GetMapping("/getWcsInfo1")
    @ResponseBody
    @Authority("wcs_infos_getWcsInfo1")
    public static WcsInfo getWcsInfo1() {
        if (wcsInfo == null) {
            return null;
        } else {
            /*Date now = DateUtils.getNowDate();
            long diff = now.getTime() - wcsInfo.getCreateTime().getTime();
            if(diff>5000){  //19895
                return null;
            }*/
            return wcsInfo;
        }

    }

    @PostMapping("/add")
    @ResponseBody
    @Authority("wcs_infos_add")
    public Result add(@RequestBody WcsInfo wcsInfo1) {
        wcsInfo = wcsInfo1;
        // wcsInfo.setCreateTime(DateUtils.getNowDateTimeString());
        // wcsInfoService.save(wcsInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/getWcsInfo")
    @ResponseBody
    @Authority("wcs_infos_getWcsInfo")
    public Result getWcsInfo() {
        return ResultGenerator.genSuccessResult(wcsInfo);
    }

}
