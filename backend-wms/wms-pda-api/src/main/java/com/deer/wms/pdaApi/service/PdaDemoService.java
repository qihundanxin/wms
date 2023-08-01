package com.deer.wms.pdaApi.service;

import com.deer.wms.pdaApi.model.PdaDemoEntity;

/**
 * Demo：Service不继承，用得到就写，用不到的不要写
 * 
 * @author xuesinuo
 *
 */
public interface PdaDemoService {
    PdaDemoEntity add(PdaDemoEntity entity);
}
