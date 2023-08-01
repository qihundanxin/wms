package com.deer.wms.ASN.manage.model.qc;

import java.util.List;

import com.deer.wms.base.system.model.Pack.PackDetailDto;

import lombok.ToString;

@ToString
public class QcDetails extends QcDetail {

    List<PackDetailDto> packDetailsList;

    public List<PackDetailDto> getPackDetailsList() {
        return packDetailsList;
    }

    public void setPackDetailsList(List<PackDetailDto> packDetailsList) {
        this.packDetailsList = packDetailsList;
    }
}
