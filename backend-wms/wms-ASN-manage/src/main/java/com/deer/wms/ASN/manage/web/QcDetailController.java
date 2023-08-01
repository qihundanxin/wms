package com.deer.wms.ASN.manage.web;

import java.util.ArrayList;
import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.qc.QcDetail;
import com.deer.wms.ASN.manage.model.qc.QcDetailCriteria;
import com.deer.wms.ASN.manage.model.qc.QcDetailDto;
import com.deer.wms.ASN.manage.model.qc.QcDetails;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.QcDetailService;
import com.deer.wms.ASN.manage.service.QcMasterService;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Pack.PackDetailCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.base.system.model.Pack.PackMaster;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.PackMasterService;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * QC详情api接口
 * 
 * Created by guo on 2020/01/15.
 */
@RestController
@RequestMapping("/qc/details")
public class QcDetailController {

    @Autowired
    private QcDetailService qcDetailService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private QcMasterService qcMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    @Autowired
    private PackMasterService packMasterService;

    @Autowired
    private PackDetailService packDetailService;

    @PostMapping("/insert")
    @Authority("qc_details_insert")
    public Result add(@RequestBody QcDetail qcDetail) {
        qcDetailService.save(qcDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_details_delete")
    public Result delete(Integer id) {
        qcDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_details_update")
    public Result update(@RequestBody QcDetail qcDetail) {
        qcDetailService.update(qcDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_details_detail")
    public Result detail(Integer id) {
        QcDetail qcDetail = qcDetailService.findById(id);
        return ResultGenerator.genSuccessResult(qcDetail);
    }

    @GetMapping("/list")
    @Authority("qc_details_list")
    public Result list(QcDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcDetailDto> list = qcDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/getCreateList")
    @Authority("qc_details_getCreateList")
    public Result getCreateList(String asnBillNo, Integer asnType) throws Exception {
        List<AsnDetail> asnDetails = asnDetailService.findByBillNo(asnBillNo);
        List<QcDetails> qcDetails = new ArrayList<>();
        ii:
        for (AsnDetail asnDetail : asnDetails) {
            List<QcTacticDetail> list = itemInfoService.findQcTacticDetails(asnDetail.getItemCode());
            ItemInfo itemInfo = itemInfoService.findBy("itemCode", asnDetail.getItemCode());
            PackMaster packMaster = packMasterService.findBy("packCode", itemInfo.getPackCode());
            PackDetailCriteria criteria = new PackDetailCriteria();
            criteria.setPackCode(packMaster.getPackCode());
            List<PackDetailDto> packDetailsList = packDetailService.findList(criteria);
            if (list.size() != 0) {
                jj:
                for (int j = 0; j < list.size(); j++) {
                    QcTacticDetail qcTacticDetail = list.get(j);
                    QcDetails qcDetail = new QcDetails();

                    //isType 单据类型 isQuantity 数量范围
                    Boolean isType = (qcTacticDetail.getAsnType().equals(asnType)) || (qcTacticDetail.getAsnType() == -1);
                    Boolean isQuantity = asnDetail.getExpectQuantity() <= qcTacticDetail.getEndQuantity() && asnDetail.getExpectQuantity() >= qcTacticDetail.getStartQuantity();
                    if (isType && isQuantity) {
                        Double qcQuantity = asnDetail.getExpectQuantity() * qcTacticDetail.getPercentage() * 0.01;
                        if (qcQuantity < qcTacticDetail.getMinQuantity()) {
                            qcDetail.setQcQuantity(qcTacticDetail.getMinQuantity());
                        } else {
                            qcDetail.setQcQuantity(qcQuantity);
                        }

                        qcDetail.setAsnDetailNo(asnDetail.getDetailNo());
                        qcDetail.setItemCode(asnDetail.getItemCode());
                        qcDetail.setItemName(asnDetail.getItemName());
                        qcDetail.setPackDetailId(asnDetail.getPackDetailId());
                        qcDetail.setPackDescribe(asnDetail.getPackDescribe());
                        qcDetail.setTransRatio(asnDetail.getTransRatio());
                        qcDetail.setExpectQuantity(asnDetail.getExpectQuantity());
                        qcDetail.setPackDetailsList(packDetailsList);
                        qcDetail.setMinPassRate(qcTacticDetail.getMinPassRate());
                        qcDetails.add(qcDetail);
                        continue ii;
                    } else {
                        if (j + 1 == list.size()) {
                            qcDetail.setAsnDetailNo(asnDetail.getDetailNo());
                            qcDetail.setItemCode(asnDetail.getItemCode());
                            qcDetail.setItemName(asnDetail.getItemName());
                            qcDetail.setPackDetailId(asnDetail.getPackDetailId());
                            qcDetail.setPackDescribe(asnDetail.getPackDescribe());
                            qcDetail.setTransRatio(asnDetail.getTransRatio());
                            qcDetail.setExpectQuantity(asnDetail.getExpectQuantity());
                            qcDetail.setPackDetailsList(packDetailsList);
                            qcDetail.setMinPassRate(qcTacticDetail.getMinPassRate());
                            qcDetail.setQcQuantity(null);
                            qcDetails.add(qcDetail);
                        }
                    }
                }
            } else {
                QcDetails qcDetail = new QcDetails();
                qcDetail.setAsnDetailNo(asnDetail.getDetailNo());
                qcDetail.setItemCode(asnDetail.getItemCode());
                qcDetail.setItemName(asnDetail.getItemName());
                qcDetail.setPackDetailId(asnDetail.getPackDetailId());
                qcDetail.setPackDescribe(asnDetail.getPackDescribe());
                qcDetail.setTransRatio(asnDetail.getTransRatio());
                qcDetail.setExpectQuantity(asnDetail.getExpectQuantity());
                qcDetail.setPackDetailsList(packDetailsList);
                qcDetail.setQcQuantity(null);
                qcDetails.add(qcDetail);
            }

        }

        return ResultGenerator.genSuccessResult(qcDetails);
    }

}
