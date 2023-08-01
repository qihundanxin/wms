package com.deer.wms.ware.task.service;

import java.io.IOException;
import java.util.List;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.PdaSearchSoData;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SO.SoMasterYdyp;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSo;
import com.deer.wms.ware.task.model.Send.SendOutCriteria;
import com.deer.wms.ware.task.model.xhcOrderInfo.Order;

/**
 * Created by guo on 2020/02/02.
 */
public interface SoMasterService extends Service<SoMaster, Integer> {

    void getOrder10(Order order);
    void getOrder11(Order order);

       void cancel(SoMaster soMaster,CurrentUser currentUser);

    void updateState(SoMaster soMaster);

    String getPdfUrl(SoMaster soMaster) throws IOException;
    String soPickTask(SoMaster soMaster,String driverName,String serialNumber) ;
    List<SoMasterDto> findList(SoMasterCriteria criteria);

    List<SoMasterDto> findByWaveId(Integer id, Integer wareId);

    SoMasterDto getSeedingByItemCode(String itemCode, Integer wareId, Integer waveMasterId);
    SoMasterDto findBillDetailByOrderNo(String orderNo,Integer wareId);

    void createSoMaster(SoData soData, CurrentUser currentUser);

    void deleteByBillNo(String billNo, Integer version);

    void createFile(String billNo, CurrentUser currentUser);

    List<SoMaster> findSoGroupInfo(SoMasterCriteria criteria);

    Integer findCountByWaveIdAndState(Integer waveMasterId, Integer state);

    List<SoMaster> findSoByWave(SoMasterCriteria criteria);

    List<SoMaster> findCanTongBu();

    void setCarriersWithRouteTactic();

    List<PdaSearchSoData> pdaSearchSo(String trackCode);

    SoMasterYdyp getYdyp(String itemCode, Integer wareId, Integer waveId);

    SoMasterYdyp getYdyp2(String billNo, Integer wareId, Integer waveId);

    void createSO(CreateSo createSo, CurrentUser currentUser);



    Integer getMaxPriority();

    List<SoMasterDto> getUnCompleteLog();

    SoMaster getByBoxOrCell(String code, Integer wareId);

    SoMasterDto getDtoByBoxOrCell(String boxCode, Integer wareId);
    
	void replayOrder(List<String> orderIDs);
	
    List<SoMaster> getReplayOrderIDByIDS(List<String> where_Order_idsList);
	
	List<SoMaster> getReplayOrderIDByID(String where_Order_id);
	
	SoMaster findByIdAndWareID(String orderID, String wareID, String orderIDLen);
	
    SoMaster findByBillNo(String billNo);

	//SoMaster findFororderIDLengthBy(String string, String orderID);

    Boolean skuSendOut(SendOutCriteria criteria ,CurrentUser currentUser);

    void updateMasterAndDetailState(String billNo, String sku, CurrentUser user);

}
