package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.model.DataDictDetail;
import com.deer.wms.system.manage.model.DataDictDetailCriteria;
import com.deer.wms.system.manage.service.DataDictDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 数据字典详细信息api接口
 * 
 * Created by on 2019/11/25.
 */
@RestController
@RequestMapping("/data/dict/details")
public class DataDictDetailController {

    @Autowired
    private DataDictDetailService dataDictDetailService;

    @Autowired
    private CacheTool cache;

    @PostMapping("/insert")
    @Authority("data_dict_details_insert")
    public Result add(@RequestBody DataDictDetail dataDictDetail) {
        dataDictDetailService.save(dataDictDetail);
        removeRedisCache();
        return ResultGenerator.genSuccessResult();
    }

    private void removeRedisCache() {
        List<DataDictDetail> dataDictDetails = dataDictDetailService.findAll();
        String[] codes = dataDictDetails.stream().map(x -> x.getDataDictCode()).toArray(String[]::new);
        cache.del(CacheKey.DATA_DICT_DETAIL, codes);
    }

    @GetMapping("/delete")
    @Authority("data_dict_details_delete")
    public Result delete(Integer id) {
        dataDictDetailService.deleteById(id);
        removeRedisCache();
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("data_dict_details_update")
    public Result update(@RequestBody DataDictDetail dataDictDetail) {
        dataDictDetailService.update(dataDictDetail);
        removeRedisCache();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("data_dict_details_detail")
    public Result detail(@PathVariable Integer id) {
        DataDictDetail dataDictDetail = dataDictDetailService.findById(id);
        return ResultGenerator.genSuccessResult(dataDictDetail);
    }

    @GetMapping("/list")
    @Authority("data_dict_details_list")
    public Result list(DataDictDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<DataDictDetail> list = dataDictDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findBydDataDictCode")
    @Authority("data_dict_details_findBydDataDictCode")
    public Result findBydDataDictCode(String dataDictCode) {
        List<DataDictDetail> list = cache.getObject(CacheKey.DATA_DICT_DETAIL, dataDictCode);
        if (list == null || list.size() == 0) {
            list = dataDictDetailService.findBydDataDictCode(dataDictCode);
            cache.setObject(CacheKey.DATA_DICT_DETAIL, dataDictCode, list, CacheTime.DATA_DICT_DETAIL);
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
