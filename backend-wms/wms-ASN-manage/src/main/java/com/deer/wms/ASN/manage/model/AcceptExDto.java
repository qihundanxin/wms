package com.deer.wms.ASN.manage.model;

import java.util.List;

import com.deer.wms.ASN.manage.model.buy.PackDetails;

import lombok.ToString;

@ToString
public class AcceptExDto extends AcceptEx {
    private List<AcceptExImg> imgUrls;

    private List<PackDetails> packDetails;

    public List<PackDetails> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetails> packDetails) {
        this.packDetails = packDetails;
    }

    public List<AcceptExImg> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<AcceptExImg> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
