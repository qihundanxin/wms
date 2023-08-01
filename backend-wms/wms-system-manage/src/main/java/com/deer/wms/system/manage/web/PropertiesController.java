package com.deer.wms.system.manage.web;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

/**
 * 配置文件api接口
 * 
 * @author lixuehe
 * @date 2022/8/23
 */
@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    /**
     * 查询数据库连接前缀
     * @return
     */
    @GetMapping("/getDetail")
    @Authority("properties_getDetail")
    public Result getDetail(){
        String rdsUrl = "";
        int begin =datasourceUrl.indexOf("//");
        int end = datasourceUrl.indexOf(".");
        if(begin!=-1 && end!=-1){
            rdsUrl = datasourceUrl.substring(begin+2,end);
        }else{
            rdsUrl = datasourceUrl.substring(begin+2,begin+10)+"...";
        }
        return ResultGenerator.genSuccessResult(rdsUrl);
    }
}
