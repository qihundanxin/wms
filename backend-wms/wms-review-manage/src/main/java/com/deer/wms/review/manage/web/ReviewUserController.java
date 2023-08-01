package com.deer.wms.review.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.review.manage.model.ReviewUser;
import com.deer.wms.review.manage.model.ReviewUserCriteria;
import com.deer.wms.review.manage.service.ReviewUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 审核人信息api接口
 * 
 * Created by guo on 2019/11/19.
 */
@RestController
@RequestMapping("/review/users")
public class ReviewUserController {

    @Autowired
    private ReviewUserService reviewUserService;

    @PostMapping
    @Authority("review_users_add")
    public Result add(@RequestBody ReviewUser reviewUser) {
        reviewUserService.save(reviewUser);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("review_users_delete")
    public Result delete(@PathVariable Integer id) {
        reviewUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("review_users_update")
    public Result update(@RequestBody ReviewUser reviewUser) {
        reviewUserService.update(reviewUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("review_users_get")
    public Result detail(@PathVariable Integer id) {
        ReviewUser reviewUser = reviewUserService.findById(id);
        return ResultGenerator.genSuccessResult(reviewUser);
    }

    @GetMapping
    @Authority("review_users_list")
    public Result list(ReviewUserCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReviewUser> list = reviewUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
