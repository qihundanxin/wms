package com.deer.wms.api.configurer;

import org.springframework.context.annotation.Configuration;

import com.deer.wms.ASN.manage.constant.AsnManageConstant;
import com.deer.wms.base.system.constant.BaseSystemConstant;
import com.deer.wms.busine.tactic.constant.BusineConstant;
import com.deer.wms.file.constant.FileConstant;
import com.deer.wms.intercept.constant.InterceptConstant;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.lzb.erp.constant.LzbErpConstant;
import com.deer.wms.pdaApi.constant.PdaApiConstant;
import com.deer.wms.project.root.configurer.AbstractMybatisConfigurer;
import com.deer.wms.quartz.constant.QuartzConstant;
import com.deer.wms.review.manage.constant.ReviewManageConstant;
import com.deer.wms.system.manage.constant.SystemManageConstant;
import com.deer.wms.ureport.constant.UreportConstant;
import com.deer.wms.ware.task.constant.WareTaskConstant;
import com.deer.wms.webhook.constant.WebHookConstant;

/**
 * Created by guo on 2017/9/30.
 */
@Configuration
public class ApiMybatisConfigurer extends AbstractMybatisConfigurer {

    @Override
    public String getTypeAliasesPackage() {
        return SystemManageConstant.MODEL_PACKAGE;
    }

    @Override
    public String getMapperLocations() {
        return "classpath*:com/deer/wms/**/mapper/*.xml";
    }

    @Override
    public String getBasePackage() {
        return FileConstant.DAO_PACKAGE
                + ", " + SystemManageConstant.DAO_PACKAGE
                + ", " + ReviewManageConstant.DAO_PACKAGE
                + ", " + BaseSystemConstant.DAO_PACKAGE
                + ", " + QuartzConstant.DAO_PACKAGE
                + ", " + BusineConstant.DAO_PACKAGE
                + ", " + InventoryConstant.DAO_PACKAGE
                + ", " + WareTaskConstant.DAO_PACKAGE
                + ", " + AsnManageConstant.DAO_PACKAGE
                + ", " + UreportConstant.DAO_PACKAGE
                + ", " + InterceptConstant.DAO_PACKAGE
                + ", " + LzbErpConstant.DAO_PACKAGE
                + ", " + "com.deer.wms.project.root.log.operation.dao"
                + ", " + WebHookConstant.DAO_PACKAGE
                + ", " + PdaApiConstant.DAO_PACKAGE;
    }
}
