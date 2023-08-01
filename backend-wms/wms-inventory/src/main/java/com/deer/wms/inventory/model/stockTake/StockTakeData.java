package com.deer.wms.inventory.model.stockTake;

import lombok.ToString;

@ToString
public class StockTakeData {
    private StockTakeMaster stockTakeMaster;

    public StockTakeMaster getStockTakeMaster() {
        return stockTakeMaster;
    }

    public void setStockTakeMaster(StockTakeMaster stockTakeMaster) {
        this.stockTakeMaster = stockTakeMaster;
    }

}
