package com.deer.wms.pdaApi.web;

import com.deer.wms.base.system.model.Shelf.ShelfInfoCriteria;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.base.system.service.ShelfInfoService;
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
 * 货架接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/shelf")
@RequiredArgsConstructor
public class PdaShelfController {

    @Autowired
    private ShelfInfoService shelfInfoService;

    /**
     * 查询货架
     * @author luolin
     * @param criteria
     * @return java.util.List<com.deer.wms.base.system.model.Shelf.ShelfInfoDto>
     **/
    @PostMapping("/findAll")
    @Authority("pda_shelf_findAll")
    public Map<String, List<ShelfInfoDto>> findAll(@RequestBody ShelfInfoCriteria criteria){
        List<ShelfInfoDto> list = shelfInfoService.findList(criteria);
        Map map = new HashMap<>();
        map.put("shelf", list);
        return map;
    }
}
