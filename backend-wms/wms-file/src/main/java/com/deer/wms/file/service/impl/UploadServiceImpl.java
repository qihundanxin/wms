package com.deer.wms.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.model.UplodaParam;
import com.deer.wms.file.service.UploadService;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.project.root.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author lixuehe
 * @date 2023/5/12
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;

    @Override
    public String upload(UplodaParam param) throws IOException {

        //目前固定验证10兆
        if (param.getMultipartFile().getSize() > 10 * 1024 * 1024) {
            throw new ServiceException(CommonCode.UPLOAD_FILE_ERROR,"单一文件大小不超过10MB");
        }
        //获取文件名
        String fileName = param.getMultipartFile().getOriginalFilename();
        //检查文件拓展名
        String extName = FileNameUtil.extName(fileName);
        String[] extNames = param.getExtNames();
        String extNames2  = StringUtils.join(extNames,",");
        if(!Arrays.asList(extNames).contains(extName)){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "上传文件格式错误，请上传"+extNames2+"格式");
        }
        String fileTempPath = fileSetting.getStaticFileDir() + fileSetting.getTempPath() + "/" + fileName;
        File file = new File(fileTempPath);
        if (!FileUtil.isDirectory(fileSetting.getStaticFileDir() + fileSetting.getTempPath())) {
            FileUtil.mkdir(fileSetting.getStaticFileDir() + fileSetting.getTempPath());
        }
        param.getMultipartFile().transferTo(file);
        String fullPath = fileS3Tool.save(param.getPath(), fileName, file);
        //将Url返回回去
        return fullPath;
    }
}
