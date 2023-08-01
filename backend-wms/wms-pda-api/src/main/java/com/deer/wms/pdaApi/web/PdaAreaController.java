package com.deer.wms.pdaApi.web;

import com.deer.wms.base.system.model.Area.AreaInfoCriteria;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PDA货区接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/area")
@RequiredArgsConstructor
public class PdaAreaController {

    @Autowired
    private AreaInfoService areaInfoService;

    /**
     * 查询货区
     * @author luolin
     * @param criteria
     * @return java.util.List<com.deer.wms.base.system.model.Area.AreaInfoDto>
     **/
    @PostMapping("/findAll")
    @Authority("pda_area_findAll")
    public Map<String, List<AreaInfoDto>> findAll(@RequestBody AreaInfoCriteria criteria){
        List<AreaInfoDto> list = areaInfoService.findList(criteria);
        Map map = new HashMap<>();
        map.put("area", list);
        return map;
    }
}
