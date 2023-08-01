package com.deer.wms.ASN.manage.model.asn;

import lombok.ToString;

@ToString
public class AsnShipDetailDto extends AsnShipDetail {
    private String shipName;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
}