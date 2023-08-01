package com.deer.wms.ASN.manage.model.buy;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestBuyDto extends RequestBuy {
    private List<String> listInvoiceFilePath;

    private Boolean serviceChargeSetNull;

    private Boolean otherExpensesSetNull;

}
