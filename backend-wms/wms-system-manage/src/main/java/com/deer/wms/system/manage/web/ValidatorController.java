package com.deer.wms.system.manage.web;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.StringUtil;
import com.deer.wms.project.root.util.WordUtils;
import com.deer.wms.system.manage.dao.ValidatorMapper;
import com.deer.wms.system.manage.model.ValidatorModel;
import com.deer.wms.system.manage.service.ValidatorService;

/**
 * 检验重复api接口
 * 
 * Created by guo on 2018/10/24.
 */
@RestController
@RequestMapping("/validator")
public class ValidatorController {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private ValidatorMapper validatorMapper;

    @Autowired
    private ValidatorService validatorService;

    @GetMapping()
    @Authority("validator_get_add")
    public Result add(String tableName) {
        if (tableName == null || !StringUtil.inAZaz09_(tableName)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的tableName：" + tableName);
        }
        String keyName = validatorMapper.findKey(tableName);
        return ResultGenerator.genSuccessResult(keyName);
    }

    @GetMapping("/getNextId")
    @Authority("validator_getNextId")
    public Result getNextId(String tableName) {
        if (tableName == null || !StringUtil.inAZaz09_(tableName)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的tableName：" + tableName);
        }
        String nextId = validatorService.getNextIdString(tableName);
        return ResultGenerator.genSuccessResult(nextId);
    }

    /**
     * 验证重复
     */
    @PostMapping()
    @Authority("validator_post_add")
    public Result add(@RequestBody ValidatorModel validatorModel, @User CurrentUser currentUser) {
        String tableName = validatorModel.getTableName();
        String fieldName = validatorModel.getFieldName();
        String idName = validatorModel.getIdName();
        if (tableName == null || !StringUtil.inAZaz09_(tableName)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的tableName：" + tableName);
        }
        if (fieldName == null || !StringUtil.inAZaz09_(fieldName)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的fieldName：" + fieldName);
        }
        if (idName != null && !StringUtil.inAZaz09_(idName)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的idName：" + idName);
        }
        String[] equal = fieldName.split("_");
        String nameOrCode = equal[equal.length - 1];
        // 新增时重复验证
        if (validatorModel.getType() == null || validatorModel.getType() == 0) {
            Integer hasChongfu = validatorMapper.findAddValidator(validatorModel);
            if (hasChongfu > 0) {
                if (nameOrCode.equals("name")) {
                    return ResultGenerator.genSuccessResult(CommonCode.NAME_EXISTS);
                } else if (nameOrCode.equals("code")) {
                    return ResultGenerator.genSuccessResult(CommonCode.CODE_EXISTS);
                } else {
                    return ResultGenerator.genSuccessResult(CommonCode.EQUALE_EXISTS);
                }
            } else {
                return ResultGenerator.genSuccessResult(CommonCode.NAME_CODE_NO_EXISTS);
            }
        }
        // 更新时重复验证
        else if (validatorModel.getType() == 1) {
            if (idName == null) {
                String tableSchema = WordUtils.getdbName(dbUrl);
                idName = validatorMapper.findPrimaryKey(new ValidatorModel(tableName, tableSchema));
                validatorModel.setIdName(idName);
            }
            Integer hasChongfu = validatorMapper.findUpdateValidator(validatorModel);
            if (hasChongfu > 0) {
                if (nameOrCode.equals("name")) {
                    return ResultGenerator.genSuccessResult(CommonCode.NAME_EXISTS);
                } else if (nameOrCode.equals("code")) {
                    return ResultGenerator.genSuccessResult(CommonCode.CODE_EXISTS);
                } else {
                    return ResultGenerator.genSuccessResult(CommonCode.EQUALE_EXISTS);
                }
            } else {
                return ResultGenerator.genSuccessResult(CommonCode.NAME_CODE_NO_EXISTS);
            }

        }
        return ResultGenerator.genSuccessResult();
    }
}
