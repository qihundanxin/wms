package com.deer.wms.ASN.manage.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.AcceptExImg;
import com.deer.wms.ASN.manage.model.AcceptExImgCriteria;
import com.deer.wms.ASN.manage.model.AcceptExImgDto;
import com.deer.wms.ASN.manage.service.AcceptExImgService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2021/01/02.
 */
@RestController
@RequestMapping("/accept/ex/imgs")
@Deprecated
public class AcceptExImgController {

    @Autowired
    private AcceptExImgService acceptExImgService;

    private String uploadImgs = "";//

    private String imgFileDir = "";//


    @Value("${wms.host}")
    private String wmsHost;//

    @Deprecated
    @PostMapping("/add")
    public Result add(AcceptExImgDto acceptExImg) throws IOException {
        AcceptExImg acceptExImg1 = new AcceptExImg();
        acceptExImgService.save(acceptExImg1);
        String originalName = acceptExImg.getModel().getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        String name = acceptExImg1.getAcceptExImgId() + suffix;
        String destination = imgFileDir + uploadImgs + "/" + name;
        File file = new File(destination);
        acceptExImg.getModel().transferTo(file);
        acceptExImg1.setExImgUrl(wmsHost + "/uploadImgs/" + name);
        acceptExImg1.setShipBillCode(acceptExImg.getShipBillCode());
        acceptExImgService.update(acceptExImg1);
        return ResultGenerator.genSuccessResult();
    }

    @Deprecated
    @GetMapping("/delete")
    public Result delete(Integer id) {
        acceptExImgService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @Deprecated
    @PostMapping("/update")
    public Result update(@RequestBody AcceptExImg acceptExImg) {
        acceptExImgService.update(acceptExImg);
        return ResultGenerator.genSuccessResult();
    }

    @Deprecated
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        AcceptExImg acceptExImg = acceptExImgService.findById(id);
        return ResultGenerator.genSuccessResult(acceptExImg);
    }

    @Deprecated
    @GetMapping("/list")
    public Result list(AcceptExImgCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptExImg> list = acceptExImgService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Deprecated
    @GetMapping("/findByAcceptExId")
    public Result findByAcceptExId(Integer acceptExId) {
        List<String> list = acceptExImgService.findByAcceptExId(acceptExId);
        return ResultGenerator.genSuccessResult(list);
    }

}
