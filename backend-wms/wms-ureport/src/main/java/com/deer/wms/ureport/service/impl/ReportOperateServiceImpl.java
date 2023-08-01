package com.deer.wms.ureport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ureport.dao.ReportOperateMapper;
import com.deer.wms.ureport.model.ReportOperate;
import com.deer.wms.ureport.model.ReportOperateCriteria;
import com.deer.wms.ureport.model.ReportOperateDetail;
import com.deer.wms.ureport.service.ReportOperateDetailService;
import com.deer.wms.ureport.service.ReportOperateService;

/**
 * Created by on 2021/01/24.
 */
@Service
@Transactional
public class ReportOperateServiceImpl extends AbstractService<ReportOperate, Integer> implements ReportOperateService {

    @Autowired
    private ReportOperateMapper reportOperateMapper;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ReportOperateDetailService reportOperateDetailService;

    /**
     * quantity1 quantity2 1-验收 操作数 入库单数 2-上架 操作数 3-拣货 操作数 订单数 4-复核 操作数 订单数 5-打包 操作数 订单数
     */
    @Override
    @Async
    public void report(Integer wareId,
            Integer userId,
            Integer type,
            Integer quantity1,
            Integer quantity2,
            String billNo,
            String itemCode,
            String itemName,
            String imgUrl) {
        WareInfo wareInfo = wareInfoService.findById(wareId);
        UserInfo userInfo = userInfoService.findById(userId);

        ReportOperate reportOperate = findNow(wareId, userId, type);
        if (quantity1 == null) {
            quantity1 = 0;
        }
        if (quantity2 == null) {
            quantity2 = 0;
        }
        if (reportOperate == null) {
            reportOperate = new ReportOperate();
            reportOperate.setWareId(wareId);
            reportOperate.setWareName(wareInfo.getWareName());
            reportOperate.setUserId(userId);
            reportOperate.setUserName(userInfo.getUserName());
            reportOperate.setReportDate(DateUtils.getCurrentDate("yyyy-MM-dd"));
            reportOperate.setCreateTime(DateUtils.now());
            reportOperate.setQuantity1(quantity1);
            reportOperate.setQuantity2(quantity2);
            reportOperate.setType(type);
            save(reportOperate);
        } else {
            reportOperate.setQuantity1(reportOperate.getQuantity1() + quantity1);
            reportOperate.setQuantity2(reportOperate.getQuantity2() + quantity2);
            reportOperate.setUpdateTime(DateUtils.now());
            update(reportOperate);
        }

        if (quantity1 == 1) {
            ReportOperateDetail reportOperateDetail = new ReportOperateDetail();
            reportOperateDetail.setReportOperateId(reportOperate.getReportOperateId());
            reportOperateDetail.setImgUrl(imgUrl);
            reportOperateDetail.setItemCode(itemCode);
            reportOperateDetail.setItemName(itemName);
            reportOperateDetail.setWareId(wareInfo.getWareId());
            reportOperateDetail.setWareName(wareInfo.getWareName());
            reportOperateDetail.setBillNo(billNo);
            reportOperateDetail.setType(type);
            reportOperateDetail.setCreateUserId(userId);
            reportOperateDetail.setCreateUserName(userInfo.getUserName());
            reportOperateDetail.setCreateTime(DateUtils.getNowDateTimeString());
            reportOperateDetailService.save(reportOperateDetail);
        }

    }

    @Override
    public ReportOperate findNow(Integer wareId, Integer userId, Integer type) {
        return reportOperateMapper.findNow(wareId, userId, type);
    }

    @Override
    public List<ReportOperate> findList(ReportOperateCriteria criteria) {
        return reportOperateMapper.findList(criteria);
    }

}
