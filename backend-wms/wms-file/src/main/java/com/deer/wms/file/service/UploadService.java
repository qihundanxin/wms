package com.deer.wms.file.service;

import com.deer.wms.file.model.UplodaParam;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author lixuehe
 * @date 2023/5/12
 */
public interface UploadService  {

    public String upload(UplodaParam param) throws IOException;




}
