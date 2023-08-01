package com.deer.wms.ASN.manage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;

public interface RequestBuyDetailMapper extends Mapper<RequestBuyDetail> {

    @Select("SELECT * FROM `request_buy_detail` LIMIT #{limit1},#{limit2}")
    List<RequestBuyDetail> getAllByPage(@Param("limit1") int limit1, @Param("limit2") int limit2);

    List<RequestBuyDetailDto> findList(RequestBuyDetailCriteria criteria);

    RequestBuyDetail findByDetailNo(@Param("detailNo") String detailNo);

    List<RequestBuyDetail> findByBillNo(@Param("billNo") String billNo);

    void deleteByBillNo(@Param("billNo") String billNo);

    int updateDetail(RequestBuyDetail detail);
}