package com.deer.wms.project.root.format.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.deer.wms.project.root.util.Np;

import lombok.Getter;

@Getter
@Configuration
public class FormatterConfig {
    private String datetimePattern;
    private String datePattern;
    private String timePattern;

    public FormatterConfig(Environment env) {
        datetimePattern = Np.i(env).x(x -> x.getProperty("deer.format.datetime")).o("yyyy-MM-dd HH:mm:ss");
        datePattern = Np.i(env).x(x -> x.getProperty("deer.format.date")).o("yyyy-MM-dd");
        timePattern = Np.i(env).x(x -> x.getProperty("deer.format.time")).o("HH:mm:ss");
    }
}
