package com.deer.wms.pdaApi.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.system.manage.model.ware.WareInfoCriteria;
import com.deer.wms.system.manage.model.ware.WareInfoDto;
import com.deer.wms.system.manage.service.WareInfoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * PDA仓库接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/ware")
@RequiredArgsConstructor
public class PdaWareController {

    @Autowired
    private WareInfoService wareInfoService;

    @Data
    public static class WareResultVo{

        private Integer wareId;

        /**
         * 仓库编码
         **/
        private String wareCode;

        /**
         * 仓库名称
         **/
        private String wareName;

        /**
         * 创建时间
         **/
        private String addTime;
    }

    /**
     * 我可选的仓库
     * @author luolin
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getMy")
    @Authority("pda_ware_getMy")
    public SimpleMap getMy(@PdaUser CurrentUser currentUser) {
        WareInfoCriteria criteria = new WareInfoCriteria();
        criteria.setUserId(currentUser.getUserId());
        List<WareInfoDto> list = wareInfoService.findList(criteria);
        List<WareResultVo> resultList = new ArrayList<>();
        if (list != null){
            for (WareInfoDto dto : list){
                WareResultVo resultVo = new WareResultVo();
                BeanUtils.copyProperties(dto, resultVo);
                resultList.add(resultVo);
            }
        }
        SimpleMap map = SimpleMap.init().append("wareList", resultList);
        return map;
    }
}
