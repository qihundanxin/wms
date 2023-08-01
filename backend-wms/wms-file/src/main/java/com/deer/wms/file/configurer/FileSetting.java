package com.deer.wms.file.configurer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by guo on 2017/10/12.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileSetting {

    private String staticFileDir;

    private String downloadPath;

    private String tempPath;

    private String modelPath;

    private String pdfPath;
}
