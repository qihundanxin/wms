package com.deer.wms.file.model;

import com.deer.wms.file.constant.FileUseType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lixuehe
 * @date 2023/5/15
 */
@Data
public class UplodaParam {

    /**
     * 允许文件后缀名
     */
    private String[] extNames;
    /**
     * 文件
     */
    private MultipartFile multipartFile;

    /**
     * 路径
     */
    private FileUseType path;
}
