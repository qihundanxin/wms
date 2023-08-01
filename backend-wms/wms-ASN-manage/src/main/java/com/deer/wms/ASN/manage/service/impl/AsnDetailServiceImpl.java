package com.deer.wms.ASN.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.dao.AsnDetailMapper;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;

/**
 * Created by guo on 2020/01/03.
 */
@Service
@Transactional
public class AsnDetailServiceImpl extends AbstractService<AsnDetail, Integer> implements AsnDetailService {

    @Autowired
    private AsnDetailMapper asnDetailMapper;

    @Override
    public List<AsnDetailDto> findList(AsnDetailCriteria criteria) {
        return asnDetailMapper.findList(criteria);
    }

    @Override
    public List<AsnDetail> findByBillNo(String billNo) {
        return asnDetailMapper.findByBillNo(billNo);

    }

    @Override
    public List<AsnDetailDto> findByDetailNo(String detailNo) {
        return asnDetailMapper.findByDetailNo(detailNo);
    }

    @Override
    public AsnDetailDto findByDetailNoAndVersion(String detailNo, Integer version) {
        return asnDetailMapper.findByDetailNoAndVersion(detailNo, version);
    }

    @Override
    public int update(AsnDetail asnDetail) {
        int rowCount = asnDetailMapper.update(asnDetail);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.ASN_VERSION, "update asnDetail 更新了0条数据：" + asnDetail.getAsnDetailId() + "," + asnDetail.getVersion());
        }
        return rowCount;
    }
}
