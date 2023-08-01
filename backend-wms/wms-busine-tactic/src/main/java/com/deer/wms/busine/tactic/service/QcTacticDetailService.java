package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetailCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
public interface QcTacticDetailService extends Service<QcTacticDetail, Integer> {

    List<QcTacticDetail> findList(QcTacticDetailCriteria criteria);

    List<QcTacticDetail> findByQcTacticCode(String qcTacticCode);

}
