package com.deer.wms.file.controller;

import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.model.UplodaParam;
import com.deer.wms.file.service.UploadService;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.SimpleMap;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author lixuehe
 * @date 2023/5/15
 */
@RequestMapping("/file")
@RestController
@AllArgsConstructor
public class UploadController {

    private UploadService uploadService;

    private FileS3Tool fileS3Tool;

    /**
     * 基础移库上传图片
     * @return
     */
    @PostMapping("/move/uploadImg")
    @Authority("file_move_uploadImg")
    public Result uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        String[] extNames = {"jpg","png","jpeg"};
        UplodaParam param = new UplodaParam();
        param.setExtNames(extNames);
        param.setPath(FileUseType.MOVE_IMG);
        param.setMultipartFile(file);
        String fullPath = uploadService.upload(param);
        return ResultGenerator.genSuccessResult(SimpleMap.init().append("fullPath", fullPath));
    }


    /**
     * 获取地址
     */
    @GetMapping("/getUrl")
    @Authority("file_move_getUrl")
    public Result selectInvoice(@RequestParam("fullPath") String fullPath) throws IOException {
        String url = fileS3Tool.getUrl(fullPath, 1 * 24 * 60 * 60, null);
        return ResultGenerator.genSuccessResult(SimpleMap.init().append("url",url));
    }





}
