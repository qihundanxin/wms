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
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeCriteria;
import com.deer.wms.review.manage.model.ReviewNodeDto;
import com.deer.wms.review.manage.service.ReviewNodeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 审核节点信息api接口
 * 
 * Created by guo on 2018/10/24.
 */
@RestController
@RequestMapping("/review/nodes")
public class ReviewNodeController {

    @Autowired
    private ReviewNodeService reviewNodeService;

    @PostMapping("/add")
    @Authority("review_nodes_add")
    public Result add(@RequestBody ReviewNode reviewNode, @User CurrentUser currentUser) {
        reviewNodeService.save(reviewNode);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("review_nodes_delete")
    public Result delete(Integer reviewNodeId) {

        if (reviewNodeService.checkExistBill(reviewNodeId)) {
            ReviewNode reviewNode = reviewNodeService.findById(reviewNodeId);

            Integer reviewMasterId = reviewNode.getReviewMasterId();
            reviewNodeService.deleteById(reviewMasterId);
            return ResultGenerator.genSuccessResult();
        } else {
            throw new ServiceException(CommonCode.CANT_DELETE_REVIEW_NODE, "该节点有正在审核的单据，不可删除！");
        }

    }

    @PostMapping("/update")
    @Authority("review_nodes_update")
    public Result update(@RequestBody ReviewNode reviewNode) {
        reviewNodeService.update(reviewNode);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("review_nodes_get")
    public Result detail(@PathVariable Integer id) {
        ReviewNode reviewNode = reviewNodeService.findById(id);
        return ResultGenerator.genSuccessResult(reviewNode);
    }

    @GetMapping("/list")
    @Authority("review_nodes_list")
    public Result list(ReviewNodeCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReviewNodeDto> list = reviewNodeService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
