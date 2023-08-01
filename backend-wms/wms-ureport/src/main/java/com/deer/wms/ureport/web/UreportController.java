package com.deer.wms.ureport.web;

import java.util.List;
import java.util.Map;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bstek.ureport.Utils;
import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ureport.model.LoadModel;
import com.deer.wms.ureport.model.UreportFile;
import com.deer.wms.ureport.service.UreportFileService;

/**
 * Ureport接口
 * 
 * Created by guo on 2019/11/21.
 *
 */
@RestController
@RequestMapping("/ureports")
public class UreportController {

    @Autowired
    private UreportFileService ureportFileService;

    @PostMapping("/loadReport")
    @Authority("ureports_loadReport")
    public Result loadReport(@RequestBody LoadModel loadModel) {
        ExportManager exportManager = (ExportManager) Utils.getApplicationContext().getBean(ExportManager.BEAN_ID);
        String fileName = "mysql:" + loadModel.getFileName();
        Map<String, Object> parameters = loadModel.getParameters();
        if(parameters.size()==0){
            return ResultGenerator.genSuccessResult("");

        }else {
            HtmlReport htmlReport = exportManager.exportHtml(fileName, "/wms", parameters);
            return ResultGenerator.genSuccessResult(htmlReport.getContent());
        }

    }

    @GetMapping("/findAll")
    @Authority("ureports_findAll")
    public Result findAll() {
        List<UreportFile> list = ureportFileService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/delete")
    @Authority("ureports_delete")
    public Result delete(Integer id) {
         ureportFileService.deleteById(id);
        return ResultGenerator.genSuccessResult( );
    }

}
