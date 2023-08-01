package com.deer.wms.review.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.dao.CaseMasterMapper;
import com.deer.wms.review.manage.model.CaseDetail;
import com.deer.wms.review.manage.model.CaseMaster;
import com.deer.wms.review.manage.model.CaseMasterCriteria;
import com.deer.wms.review.manage.model.CaseMasterDto;
import com.deer.wms.review.manage.model.CaseUser;
import com.deer.wms.review.manage.model.ReviewMasterCriteria;
import com.deer.wms.review.manage.model.ReviewMasterDto;
import com.deer.wms.review.manage.model.ReviewNode;
import com.deer.wms.review.manage.model.ReviewNodeCriteria;
import com.deer.wms.review.manage.model.ReviewNodeDto;
import com.deer.wms.review.manage.model.ReviewUser;
import com.deer.wms.review.manage.service.CaseDetailService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.review.manage.service.CaseUserService;
import com.deer.wms.review.manage.service.ReviewMasterService;
import com.deer.wms.review.manage.service.ReviewNodeService;
import com.deer.wms.review.manage.service.ReviewUserService;

/**
 * Created by guo on 2018/10/24.
 */
@Service
@Transactional
public class CaseMasterServiceImpl extends AbstractService<CaseMaster, Integer> implements CaseMasterService {

    @Autowired
    private CaseMasterMapper caseMasterMapper;

    @Autowired
    private ReviewMasterService reviewMasterService;

    @Autowired
    private ReviewNodeService reviewNodeService;

    @Autowired
    private CaseDetailService caseDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private CaseUserService caseUserService;

    @Autowired
    private ReviewUserService reviewUserService;

    @Override
    public List<CaseMasterDto> findList(CaseMasterCriteria criteria) {
        return caseMasterMapper.findList(criteria);
    }

    @Override
    //  @Async   调用者需要这个方法的返回值
    public Integer createCase(String billNo, Integer billTypeId, Integer userId, Integer wareId, Integer organizationId) {
        /*
         * 1.通过reivew Master Code查找reviewMaster以及node
         * 2.新建caseMaster以及caseNode
         * */
        ReviewMasterCriteria criteria = new ReviewMasterCriteria();
        criteria.setWareId(wareId);
        criteria.setOrganizationId(organizationId);
        criteria.setBillTypeId(billTypeId);
        ReviewMasterDto reviewMaster = null;
        List<ReviewMasterDto> reviewMasterDtos = reviewMasterService.findList(criteria);
        if (reviewMasterDtos.size() > 0) {
            reviewMaster = reviewMasterDtos.get(0);
        }
        if (reviewMaster == null || reviewMaster.getState() == 1) {
            return 1;
        }
        ReviewNodeCriteria reviewNodeCriteria = new ReviewNodeCriteria();
        reviewNodeCriteria.setReviewMasterId(reviewMaster.getReviewMasterId());
        List<ReviewNodeDto> reviewNodeDtos = reviewNodeService.findList(reviewNodeCriteria);

        //如果当前审核流没有审核节点,则返回1
        if (reviewNodeDtos == null || reviewNodeDtos.size() == 0) {
            return 1;
        }

        CaseMaster caseMaster = new CaseMaster();
        caseMaster.setBillNo(billNo);
        caseMaster.setCreateUserId(userId);
        //    caseMaster.setReviewMasterId(reviewMaster.getReviewMasterId());
        String nowDate = DateUtils.getNowDateTimeString();
        caseMaster.setCreateTime(nowDate);
        caseMasterService.save(caseMaster);

        for (int i = 1; i <= reviewNodeDtos.size(); i++) {
            ReviewNode reviewNode = reviewNodeDtos.get(i - 1);
            CaseDetail caseDetail = new CaseDetail();
            caseDetail.setCaseMasterId(caseMaster.getCaseMasterId());
            caseDetail.setDetailType(reviewNode.getType());
            caseDetail.setOrderNo(i);
            caseDetail.setReviewNodeId(reviewNode.getReviewNodeId());
            caseDetail.setReviewNodeName(reviewNode.getReviewNodeName());
            caseDetailService.save(caseDetail);

            if (i == 1) {
                caseMaster.setCaseNodeId(caseDetail.getCaseDetailId());
                caseMasterService.update(caseMaster);
            }
            List<ReviewUser> reviewUsers = reviewUserService.findByNodeId(reviewNode.getReviewNodeId());
            for (ReviewUser reviewUser : reviewUsers) {
                CaseUser caseUser = new CaseUser();
                caseUser.setCaseDetailId(caseDetail.getCaseDetailId());
                caseUser.setUserId(reviewUser.getUserId());
                caseUser.setUserName(reviewUser.getUserName());
                caseUserService.save(caseUser);
            }

        }
//        for(ReviewNode reviewNode:reviewNodeDtos){
//            CaseDetail caseDetail = new CaseDetail();
//     //       caseDetail.setReviewNodeId(reviewNode.getReviewNodeId());
//            caseDetail.setCaseMasterId(caseMaster.getCaseMasterId());
//            caseDetail.setDetailType(reviewNode.getType());
//            caseDetailService.save(caseDetail);
//        }
        return 0;

    }

    /*
     * 返回值说明：-1不通过  0-流转中 1-已通过
     * */
    @Override
    public Integer review(Integer caseDetailId, Integer userId, Integer reviewResult, String memo) {   //reviewResult -1-不通过 1-通过

        /*
         * 1.查找当前的审核节点
         * 2.录入当前审核结果  1-通过  2-拒绝
         * 3.如果拒绝 直接返回   如果通过，则判断当前节点是否是最后节点，如果是，则返回通过，如果不是则流向下一审核节点
         * */
        CaseDetail caseDetail = caseDetailService.findById(caseDetailId);
        Integer caseMasterId = caseDetail.getCaseMasterId();
        Integer orderNo = caseDetail.getOrderNo();
        CaseMaster caseMaster = super.findById(caseMasterId);

        CaseUser caseUser = caseUserService.findByDetailAndUserId(caseDetailId, userId);
        caseUser.setReviewResult(reviewResult == 1);
        caseUser.setMemo(memo);
        caseUserService.update(caseUser);

        List<CaseUser> caseUsers = caseUserService.findByCaseDetailId(caseDetailId);
        //与节点(会签) 全部通过才能通过  任何一个人拒绝就拒绝
        if (caseDetail.getDetailType() == 0) {
            /***
             * 1.先判断当前节点下所有人是否全部审核通过
             * 2.再流转到下一节点
             */
            //或节点下通过，判断其他人是否通过
            if (reviewResult == 1) {
                List<CaseUser> caseUserList = caseUserService.findNoREview(caseDetailId);
                //或节点还有其他人没有审核，则继续审核
                if (caseUserList.size() > 0) {
                    return 0;
                }
                //或节点下全部审核，则流向下一节点
                else {
                    caseDetail.setTaskResult(1);
                    caseDetail.setTaskTime(DateUtils.getNowDateTimeString());
                    caseDetailService.update(caseDetail);
                    return nextCaseDetail(caseMaster);

                }
            }
            //或节点下拒绝，则整个审核流程拒绝
            else if (reviewResult == -1) {
                caseDetail.setTaskResult(-1);
                caseDetail.setTaskTime(DateUtils.getNowDateTimeString());
                caseDetailService.update(caseDetail);
                caseMaster.setState(-1);
                super.update(caseMaster);
                return -1;
            }
        }
        //或节点（任何一个人通过就通过，任何一个人拒绝就拒绝）
        else if (caseDetail.getDetailType() == 1) {
            caseDetail.setTaskResult(reviewResult);
            caseDetail.setTaskTime(DateUtils.getNowDateTimeString());
            caseDetailService.update(caseDetail);
            caseMaster.setState(reviewResult);
            super.update(caseMaster);
            if (reviewResult == 1) {
                return nextCaseDetail(caseMaster);
            } else if (reviewResult == -1) {
                return -1;
            }
        }

        return null;
    }

    @Override
    public void deleteByBillNo(String billNo) {
        caseMasterMapper.deleteByBillNo(billNo);
    }

    /***
     * 通过之后流转到下一节点
     * @param caseMaster
     * @param
     */
    private Integer nextCaseDetail(CaseMaster caseMaster) {
        List<CaseDetail> caseDetails = caseDetailService.findNoReview(caseMaster.getCaseMasterId());
        if (caseDetails.size() > 0) {
            caseMaster.setCaseNodeId(caseDetails.get(0).getCaseDetailId());
            super.update(caseMaster);
            return 0;
        } else {
            caseMaster.setState(1);
            super.update(caseMaster);
            return 1;
        }

    }

    @Override
    public CaseMaster findByBillNo(String billNo) {
        return caseMasterMapper.findByBillNo(billNo);
    }
}
