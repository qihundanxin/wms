package com.deer.wms.ASN.manage.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.dao.RequestBuyDetailMapper;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.util.Np;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by guo on 2020/01/02.
 */
@Slf4j
@Service
@Transactional
public class RequestBuyDetailServiceImpl extends AbstractService<RequestBuyDetail, Integer> implements RequestBuyDetailService {

    @Autowired
    private RequestBuyDetailMapper requestBuyDetailMapper;

    @Override
    public List<RequestBuyDetailDto> findList(RequestBuyDetailCriteria criteria) {
        return requestBuyDetailMapper.findList(criteria);
    }

    @Override
    public List<RequestBuyDetail> findByBillNo(String billNo) {
        return requestBuyDetailMapper.findByBillNo(billNo);
    }

    @Override
    public RequestBuyDetail findByDetailNo(String detailNo) {
        return requestBuyDetailMapper.findByDetailNo(detailNo);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        requestBuyDetailMapper.deleteByBillNo(billNo);
    }


    @Override
    public int updateDetail(RequestBuyDetailDto detail) {
        return requestBuyDetailMapper.updateDetail(detail);
    }


    /**
     * 用于刷新价格数据的临时方法
     * 
     * FIXME 不再使用时，可以删除
     */
    @WorkLogger
    public void refreshPrice() {
        int updateCount = 0;
        int limit1 = 0;
        int limit2 = 1000;
        for(;;) {
            List<RequestBuyDetail> list = requestBuyDetailMapper.getAllByPage(limit1, limit2);

            for (RequestBuyDetail requestBuyDetail : list) {
                try {
                    String strPrice = requestBuyDetail.getPoUdfDs1();
                    String strTaxRate = requestBuyDetail.getPoUdfDs2();
                    BigDecimal priceIn = requestBuyDetail.getPriceIn();
                    BigDecimal taxRateIn = requestBuyDetail.getTaxRateIn();

                    if (StrUtil.isNotBlank(strPrice) && priceIn == null) {
                        priceIn = this.strToDecimal(strPrice);
                        if (priceIn == null) {
                            log.error("价格解析失败。id：" + requestBuyDetail.getRequestBuyDetailId() + "；价格：" + strPrice);
                            continue;
                        }
                        if (priceIn.compareTo(new BigDecimal("999999")) == 1) {
                            log.error("价格过于离谱。id：" + requestBuyDetail.getRequestBuyDetailId() + "；价格：" + strPrice);
                            continue;
                        }
                    }
                    if (StrUtil.isNotBlank(strTaxRate) && taxRateIn == null) {
                        taxRateIn = this.strToDecimal(strTaxRate);
                        if (taxRateIn == null) {
                            log.error("税率解析失败。id：" + requestBuyDetail.getRequestBuyDetailId() + "；税率：" + strTaxRate);
                            continue;
                        }
                        if (priceIn.compareTo(new BigDecimal("999999")) == 1) {
                            log.error("税率过于离谱。id：" + requestBuyDetail.getRequestBuyDetailId() + "；价格：" + strPrice);
                            continue;
                        }
                    }
                    if (Np.i(priceIn).notEq(requestBuyDetail.getPriceIn()) ||
                            Np.i(taxRateIn).notEq(requestBuyDetail.getTaxRateIn())) {
                        requestBuyDetail.setPriceIn(priceIn);
                        requestBuyDetail.setTaxRateIn(taxRateIn);
                        requestBuyDetailMapper.updateByPrimaryKeySelective(requestBuyDetail);
                        updateCount++;
                    }
                } catch (Exception e) {
                    log.error("解析过程代码报错。id：" + requestBuyDetail.getRequestBuyDetailId(), e);
                }
            }

            if(list.size()<1000) {
                break;
            }else {
                limit1 = limit1 + 900;// 100的蠕动区间，不适合读写分离，更好的办法是使用自增ID
            }
        }
        log.info("完成，共计更新：" + updateCount);
    }

    private BigDecimal strToDecimal(String s) {
        char[] strPriceChars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean hasPoint = false;
        for (char c : strPriceChars) {
            if (c >= 48 && c <= 57) {
                sb.append(c);
            }
            if (c == 46 && hasPoint == false) {
                if (sb.length() == 0) {
                    sb.append('0');
                }
                sb.append(c);
                hasPoint = true;
            }
        }
        if (StrUtil.isNotBlank(sb)) {
            return new BigDecimal(sb.toString());
        } else {
            return null;
        }
    }
}
