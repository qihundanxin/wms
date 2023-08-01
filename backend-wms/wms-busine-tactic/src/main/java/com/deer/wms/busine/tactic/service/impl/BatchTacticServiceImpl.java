package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.BatchTacticMapper;
import com.deer.wms.busine.tactic.model.Batch.*;
import com.deer.wms.busine.tactic.service.BatchTacticDetailDetailService;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;
import com.deer.wms.busine.tactic.service.BatchTacticService;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/29.
 */
@Service
@Transactional
public class BatchTacticServiceImpl extends AbstractService<BatchTactic, Integer> implements BatchTacticService {

    @Autowired
    private BatchTacticMapper batchTacticMapper;

    @Autowired
    private BatchTacticDetailService batchTacticDetailService;

    @Autowired
    private BatchTacticDetailDetailService batchTacticDetailDetailService;

    @Override
    public List<BatchTactic> findList(BatchTacticCriteria criteria) {
        return batchTacticMapper.findList(criteria);
    }

    @Override
    public List<BatchTacticDto> findByState(Integer batchTacticId, Integer state) {
        return batchTacticMapper.findByState(batchTacticId, state);
    }

    @Override
    public List<BatchTacticDto> findByOrganizationOrItemInfo(Integer organizationId, String itemCode) {
        return batchTacticMapper.findByOrganizationOrItemInfo(organizationId, itemCode);
    }

    @Override
    public List<BatchTacticDto> findByOrganizationOrItemInfo2(Integer organizationId, String itemCode) {
        return batchTacticMapper.findByOrganizationOrItemInfo2(organizationId, itemCode);
    }

    @Override
    public void createBatchTactic(InsertData insertData, CurrentUser currentUser) {

        BatchTactic batchTactic = insertData.getBatchTactic();
        save(batchTactic);

        String batchTacticCode = batchTactic.getBatchTacticCode();

        List<BatchTacticDetail> batchTacticDetails = insertData.getBatchTacticDetails();
        for (int i = 0; i < batchTacticDetails.size(); i++) {
            BatchTacticDetail batchTacticDetail = batchTacticDetails.get(i);
            batchTacticDetail.setBatchTacticCode(batchTacticCode);
//            String detailRfName = GenerateSelfId.getSelfIdByUUId();
//            batchTacticDetail.setDetailRfName(detailRfName);
            batchTacticDetailService.save(batchTacticDetail);
            if (batchTacticDetail.getFormat() == 4) {
                List<BatchTacticDetailDetail> batchTacticDetailDetails = insertData.getBatchTacticDetailDetails();
                for (int y = 0; y < batchTacticDetailDetails.size(); y++) {
                    BatchTacticDetailDetail batchTacticDetailDetail = batchTacticDetailDetails.get(y);
                    if (batchTacticDetailDetail.getDetailCode().equals("batchAttribute1") && i == 12) {
                        batchTacticDetailDetail.setBatchTacticDetailId(batchTacticDetail.getBatchTacticDetailId());
                        String batchTacticDetailDetailValue = batchTacticDetailDetail.getBatchTacticDetailDetailValue();
                        batchTacticDetailDetail.setBatchTacticDetailDetailValue(batchTacticDetailDetailValue);
                        batchTacticDetailDetailService.save(batchTacticDetailDetail);
                    } else if (batchTacticDetailDetail.getDetailCode().equals("batchAttribute2") && i == 13) {
                        batchTacticDetailDetail.setBatchTacticDetailId(batchTacticDetail.getBatchTacticDetailId());
                        String batchTacticDetailDetailValue = batchTacticDetailDetail.getBatchTacticDetailDetailValue();
                        batchTacticDetailDetail.setBatchTacticDetailDetailValue(batchTacticDetailDetailValue);
                        batchTacticDetailDetailService.save(batchTacticDetailDetail);
                    }
                }
            }
        }
    }

    @Override
    public void updateBatchTactic(InsertData insertData, CurrentUser currentUser) {
        BatchTactic batchTactic = insertData.getBatchTactic();

        update(batchTactic);

        String batchTacticCode = batchTactic.getBatchTacticCode();

        List<BatchTacticDetail> batchTacticDetails = insertData.getBatchTacticDetails();
        for (int i = 0; i < batchTacticDetails.size(); i++) {
            BatchTacticDetail batchTacticDetail = batchTacticDetails.get(i);
            batchTacticDetail.setBatchTacticCode(batchTacticCode);
//            String detailRfName = GenerateSelfId.getSelfIdByUUId();
//            batchTacticDetail.setDetailRfName(detailRfName);
            batchTacticDetailService.update(batchTacticDetail);
            if (batchTacticDetail.getFormat() == 4) {
                batchTacticDetailDetailService.deleteByCodeAndId(batchTacticDetail.getDetailCode(), batchTacticDetail.getBatchTacticDetailId());
                List<BatchTacticDetailDetail> batchTacticDetailDetails = insertData.getBatchTacticDetailDetails();
                for (int y = 0; y < batchTacticDetailDetails.size(); y++) {
                    BatchTacticDetailDetail batchTacticDetailDetail = batchTacticDetailDetails.get(y);
                    if (batchTacticDetailDetail.getDetailCode().equals("batchAttribute1") && i == 12) {

                        batchTacticDetailDetail.setBatchTacticDetailId(batchTacticDetail.getBatchTacticDetailId());
                        String batchTacticDetailDetailValue = batchTacticDetailDetail.getBatchTacticDetailDetailValue();
                        batchTacticDetailDetail.setBatchTacticDetailDetailValue(batchTacticDetailDetailValue);
                        batchTacticDetailDetailService.save(batchTacticDetailDetail);
                    } else if (batchTacticDetailDetail.getDetailCode().equals("batchAttribute2") && i == 13) {

                        batchTacticDetailDetail.setBatchTacticDetailId(batchTacticDetail.getBatchTacticDetailId());
                        String batchTacticDetailDetailValue = batchTacticDetailDetail.getBatchTacticDetailDetailValue();
                        batchTacticDetailDetail.setBatchTacticDetailDetailValue(batchTacticDetailDetailValue);
                        batchTacticDetailDetailService.save(batchTacticDetailDetail);
                    }
                }
            }
        }
    }

    @Override
    public BatchTacticDto findByBatchTacticCode(String batchTacticCode) {
        return batchTacticMapper.findByBatchTacticCode(batchTacticCode);
    }

}
