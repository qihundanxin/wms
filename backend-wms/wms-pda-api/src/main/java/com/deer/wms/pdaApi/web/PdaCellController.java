package com.deer.wms.pdaApi.web;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * PDA货架接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/cellInfo")
@RequiredArgsConstructor
public class PdaCellController {

    @Autowired
    private CellInfoService cellInfoService;

    @Data
    public static class CellFromVo{

        @NotNull
        private Integer wareId;

        private Integer id;

        /**
         * 货位编码
         **/
        private String cellCode;
    }

    @Data
    public static class CellResultVo{

        private Integer id;

        /**
         * 货位编码
         **/
        private String cellCode;

        /**
         * 货位名称
         **/
        private String cellName;

        /**
         * 仓库ID
         **/
        private Integer wareId;

        /**
         * 仓库名称
         **/
        private String wareName;

        /**
         * 货位使用类型 -1垃圾货位 0验收位 1拣货位 2仓储位 3集货位 4上架车
         **/
        private Integer cellUseType;
    }

    /**
     * 查询货位
     * @author luolin
     * @param cellFromVo
     * @return com.deer.wms.pdaApi.web.PdaCellController.CellResultVo
     **/
    @PostMapping("/getById")
    @Authority("pda_cellInfo_getById")
    public CellResultVo getById(@RequestBody CellFromVo cellFromVo){
        if (cellFromVo.getId() == null && cellFromVo.getCellCode() == null){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "ID和cellCode不能同时为空");
        }
        CellInfo cellInfo = new CellInfo();
        cellInfo.setWareId(cellFromVo.getWareId());
        cellInfo.setCellId(cellFromVo.getId());
        cellInfo.setCellCode(cellFromVo.getCellCode());
        if (cellFromVo.getId() != null && cellFromVo.getCellCode() != null){
            cellInfo.setCellCode(null);
        }
        CellInfo cell = cellInfoService.findCellInfo(cellInfo);
        CellResultVo result = new CellResultVo();
        if (cell != null){
            BeanUtils.copyProperties(cell, result);
        }
        return result;
}
}
