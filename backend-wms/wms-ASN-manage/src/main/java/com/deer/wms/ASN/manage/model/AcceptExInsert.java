package com.deer.wms.ASN.manage.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString
public class AcceptExInsert {

    private String quantity;

    private String itemCode;

    private String itemName;

    private String spec;

    private String model;

    private String imgUrl;

    private Integer wareId;

    private Integer orgnizationId;

    private String type;

    private String yuanyin;

    private String memo;

    private List<MultipartFile> imgs;

    public List<MultipartFile> getImgs() {
        return imgs;
    }

    public void setImgs(List<MultipartFile> imgs) {
        this.imgs = imgs;
    }

    /**
     * @return quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @return item_code
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return item_name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return spec
     */
    public String getSpec() {
        return spec;
    }

    /**
     * @param spec
     */
    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return img_url
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return ware_id
     */
    public Integer getWareId() {
        return wareId;
    }

    /**
     * @param wareId
     */
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    /**
     * @return orgnization_id
     */
    public Integer getOrgnizationId() {
        return orgnizationId;
    }

    /**
     * @param orgnizationId
     */
    public void setOrgnizationId(Integer orgnizationId) {
        this.orgnizationId = orgnizationId;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return yuanyin
     */
    public String getYuanyin() {
        return yuanyin;
    }

    /**
     * @param yuanyin
     */
    public void setYuanyin(String yuanyin) {
        this.yuanyin = yuanyin;
    }

    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

}