package com.deer.wms.ASN.manage.model.asn;

import lombok.ToString;

//入库异常解决方案
@ToString
public class InExResult {
    private String billNo;
    private String shipBillNo;
    private Integer exFangan;
    private String returnItemInfo;
    private String skuCode;
    private Double quantity;
    private String qcNo;
    private String memo;
    private ReturnInfo returnInfo;

    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ReturnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public static class ReturnInfo {
        private String expressBillUrl;//快递电子面单地址
        private String shipCode;//物流公司
        private String shipBillCode;//快递单号
        private String linkMan;
        private String linkPhone;
        private String country;
        private String province;
        private String city;
        private String area;
        private String detailAddress;

        public String getExpressBillUrl() {
            return expressBillUrl;
        }

        public void setExpressBillUrl(String expressBillUrl) {
            this.expressBillUrl = expressBillUrl;
        }

        public String getShipCode() {
            return shipCode;
        }

        public void setShipCode(String shipCode) {
            this.shipCode = shipCode;
        }

        public String getShipBillCode() {
            return shipBillCode;
        }

        public void setShipBillCode(String shipBillCode) {
            this.shipBillCode = shipBillCode;
        }

        public String getLinkMan() {
            return linkMan;
        }

        public void setLinkMan(String linkMan) {
            this.linkMan = linkMan;
        }

        public String getLinkPhone() {
            return linkPhone;
        }

        public void setLinkPhone(String linkPhone) {
            this.linkPhone = linkPhone;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getQcNo() {
        return qcNo;
    }

    public void setQcNo(String qcNo) {
        this.qcNo = qcNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getShipBillNo() {
        return shipBillNo;
    }

    public void setShipBillNo(String shipBillNo) {
        this.shipBillNo = shipBillNo;
    }

    public Integer getExFangan() {
        return exFangan;
    }

    public void setExFangan(Integer exFangan) {
        this.exFangan = exFangan;
    }

    public String getReturnItemInfo() {
        return returnItemInfo;
    }

    public void setReturnItemInfo(String returnItemInfo) {
        this.returnItemInfo = returnItemInfo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
