package com.deer.wms.base.system.event;

import org.springframework.context.ApplicationEvent;

import com.deer.wms.base.system.model.Item.ItemInfo;

/**
 * 修改商品时间
 * @author lixuehe
 * @date 2023/4/26
 */
public class UpdateItemInfoEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private ItemInfo itemInfo;

    public UpdateItemInfoEvent(ItemInfo itemInfo) {
        super(itemInfo);
        this.itemInfo = itemInfo;
    }

    /**
     * @return  更新后的结果
     */
    public ItemInfo getItemInfo() {
        return itemInfo;
    }
}
