package com.deer.wms.review.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.review.manage.model.ReviewMaster;
import com.deer.wms.review.manage.model.ReviewMasterCriteria;
import com.deer.wms.review.manage.model.ReviewMasterDto;
import com.deer.wms.review.manage.model.ReviewMasterInsert;
import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeInsert;
import com.deer.wms.review.manage.model.ReviewUser;
import com.deer.wms.review.manage.service.ReviewMasterService;
import com.deer.wms.review.manage.service.ReviewNodeService;
import com.deer.wms.review.manage.service.ReviewUserService;
import com.deer.wms.system.manage.model.user.UserInfoDetailVO;
import com.deer.wms.system.manage.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 审核信息api接口
 * 
 * Created by guo on 2018/10/24.
 */
@RestController
@RequestMapping("/review/masters")
public class ReviewMasterController {

    @Autowired
    private ReviewMasterService reviewMasterService;

    @Autowired
    private ReviewNodeService reviewNodeService;

    @Autowired
    private ReviewUserService reviewUserService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/add")
    @Authority("review_masters_add")
    public Result add(@RequestBody ReviewMasterInsert reviewMasterInsert, @User CurrentUser currentUser) {
        ReviewMaster reviewMaster = reviewMasterInsert.getReviewMaster();
        reviewMasterService.save(reviewMaster);

        List<ReviewNodeInsert> list = reviewMasterInsert.getReviewNodes();
        for (ReviewNodeInsert reviewNodeInsert : list) {
            ReviewNode reviewNode = reviewNodeInsert;
            reviewNode.setReviewMasterId(reviewMaster.getReviewMasterId());
            reviewNodeService.save(reviewNode);

            List<ReviewUser> reviewUsers = reviewNodeInsert.getReviewUsers();
            for (ReviewUser reviewUser : reviewUsers) {
                reviewUser.setReviewNodeId(reviewNode.getReviewNodeId());
                Integer userId = reviewUser.getUserId();
                UserInfoDetailVO vo = userInfoService.findUserInfoDetailByUserId(userId);
                reviewUser.setUserName(vo.getUserName());
                reviewUserService.save(reviewUser);
            }

        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("review_masters_delete")
    public Result delete(Integer reviewMasterId) {
        reviewMasterService.deleteById(reviewMasterId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("review_masters_update")
    public Result update(@RequestBody ReviewMasterInsert reviewMasterInsert) {

        ReviewMaster reviewMaster = reviewMasterInsert.getReviewMaster();
        reviewMasterService.deleteById(reviewMaster.getReviewMasterId());
        reviewMasterService.save(reviewMaster);

        List<ReviewNodeInsert> list = reviewMasterInsert.getReviewNodes();
        for (ReviewNodeInsert reviewNodeInsert : list) {
            ReviewNode reviewNode = reviewNodeInsert;
            reviewNode.setReviewMasterId(reviewMaster.getReviewMasterId());
            reviewNodeService.save(reviewNode);

            List<ReviewUser> reviewUsers = reviewNodeInsert.getReviewUsers();
            for (ReviewUser reviewUser : reviewUsers) {
                reviewUser.setReviewNodeId(reviewNode.getReviewNodeId());
                Integer userId = reviewUser.getUserId();
                UserInfoDetailVO vo = userInfoService.findUserInfoDetailByUserId(userId);
                reviewUser.setUserName(vo.getUserName());
                reviewUserService.save(reviewUser);
            }

        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("review_masters_get")
    public Result detail(@PathVariable Integer id) {
        ReviewMaster reviewMaster = reviewMasterService.findById(id);
        return ResultGenerator.genSuccessResult(reviewMaster);
    }

    @GetMapping("/list")
    @Authority("review_masters_list")
    public Result list(ReviewMasterCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReviewMasterDto> list = reviewMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
