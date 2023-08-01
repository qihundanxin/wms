package com.deer.wms.pdaApi.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.system.manage.model.organization.OrganizationCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationDto;
import com.deer.wms.system.manage.service.OrganizationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 货主查询
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/organization")
@RequiredArgsConstructor
public class PdaOrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Data
    public static class OrganizationResultVo{

        private Integer organizationId;

        /**
         * 货主名称
         **/
        private String organizationName;
    }

    /**
     * 我可选的货主
     * @author luolin
     * @param currentUser
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getMy")
    @Authority("pda_organization_getMy")
    public SimpleMap getMy(@PdaUser CurrentUser currentUser){
        OrganizationCriteria criteria = new OrganizationCriteria();
        if (currentUser.getIsAdmin() == 1) {
            criteria.setUserId(null);
        } else {
            criteria.setUserId(currentUser.getUserId());
        }
        List<OrganizationDto> list = organizationService.findList(criteria);
        List<OrganizationResultVo> resultList = new ArrayList<>();
        if (list != null){
            BeanUtils.copyProperties(list, resultList);
        }
        SimpleMap map = SimpleMap.init().append("organizationList", resultList);
        return map;
    }
}
