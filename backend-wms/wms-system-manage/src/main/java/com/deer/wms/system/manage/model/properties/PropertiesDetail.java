package com.deer.wms.system.manage.model.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 配置文件详情
 * @author lixuehe
 * @date 2022/8/23
 */
@Data
public class PropertiesDetail {

    /*
      数据库连接
     */
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
}
