package com.deer.wms.pdaApi.web;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.BoxInfoCriteria;
import com.deer.wms.base.system.service.BoxInfoService;
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
 * 容器接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/box")
@RequiredArgsConstructor
public class PdaBoxController {

    @Autowired
    private BoxInfoService boxInfoService;

    /**
     * 查询容器
     * @author luolin
     * @param criteria
     * @return java.util.List<com.deer.wms.base.system.model.BoxInfo>
     **/
    @PostMapping("/findAll")
    @Authority("pda_box_findAll")
    public Map<String, List<BoxInfo>> findAll(@RequestBody BoxInfoCriteria criteria){
        List<BoxInfo> list = boxInfoService.findList(criteria);
        Map map = new HashMap<>();
        map.put("box", list);
        return map;
    }
}
