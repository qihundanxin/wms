package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.SelectProduct;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/12/19.
 */
public interface SelectProductService extends Service<SelectProduct, Integer> {
    void deleteAll();

    List<SelectProduct> selectBy(String where);

}
