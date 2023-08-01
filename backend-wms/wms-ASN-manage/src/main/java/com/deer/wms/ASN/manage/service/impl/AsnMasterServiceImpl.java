package com.deer.wms.ASN.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.dao.AsnMasterMapper;
import com.deer.wms.ASN.manage.model.asn.AsnData;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.asn.AsnMasterCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnMasterDto;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.asn.CreateASN.CreateASN;
import com.deer.wms.ASN.manage.model.asn.CreateASN.CreateASNSKUDetail;
import com.deer.wms.ASN.manage.model.asn.CreateASN.CreateASNShipDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.service.AsnDetailFileService;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterFileService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.ASN.manage.service.AsnShipDetailService;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.CarrierService;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.ShippingAddressService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.model.Allot.AllotData;
import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.service.AllotDetailService;
import com.deer.wms.ware.task.service.AllotMasterService;
import com.deer.wms.ware.task.service.SoMasterService;

/**
 * Created by guo on 2020/01/03.
 */
@Service
@Transactional
public class AsnMasterServiceImpl extends AbstractService<AsnMaster, Integer> implements AsnMasterService {

    @Autowired
    private AsnMasterMapper asnMasterMapper;

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private RequestBuyDetailService requestBuyDetailService;

    @Autowired
    private AsnMasterFileService asnMasterFileService;

    @Autowired
    private AsnDetailFileService asnDetailFileService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private WareInfoService wareInfoService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AsnShipDetailService asnShipDetailService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Override
    public void deleteByBillNo(String billNo, Integer version) {
        int rowCount = asnMasterMapper.deleteByBillNo(billNo, version);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.ASN_VERSION, "请重试！");
        }
    }

    @Override
    public List<AsnMasterDto> findList(AsnMasterCriteria criteria) {
        return asnMasterMapper.findList(criteria);
    }

    @Override
    public List<AsnMasterDto> pdaAccept(AsnMasterCriteria criteria) {
        return asnMasterMapper.pdaAccept(criteria);
    }

    @Override
    public void createAsnMaster(AsnData asnData, CurrentUser currentUser) {
        /**
         *  新建ASN单据
         */
        AsnMaster asnMaster = asnData.getAsnMaster();
        String tableName = "asn_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "ASN" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        Integer state = 2;



        //如果是草稿，则保存草稿记录
        if (asnData.getInsertType() == -2) {
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(2);//ASN草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);
        }

        //如果是新增，则添加新增记录
        if (asnData.getInsertType() == 0) {
            //通过草稿提交，则需要先把草稿删掉
            String billNo1 = asnData.getAsnMaster().getBillNo();
            if (billNo1 != null) {
                billNo = billNo1;
                AsnMasterDto byBillNo = asnMasterService.findByBillNo(billNo1);
                asnMasterService.deleteByBillNo(billNo1, byBillNo.getVersion());
                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 2);//ASN草稿
                billRecordService.updateBillRecord(billRecord1, currentUser);
            }

            BillRecord billRecord3 = new BillRecord();
            billRecord3.setBillNo(billNo);
            billRecord3.setRecordType(1);//生成ASN
            billRecord3.setCreateTime(createTime);
            billRecord3.setCreateUserId(createUserId);
            billRecord3.setCreateUserName(createUserName);

            billRecordService.save(billRecord3);

            /**
             * 添加审核流
             */
            //        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 2, currentUser.getUserId(), asnMaster.getWareId(), asnMaster.getOrganizationId());

        }

        //新增Master
        asnMaster.setCreateUserId(createUserId);
        asnMaster.setCreateUserName(createUserName);
        asnMaster.setCreateTime(createTime);
        asnMaster.setBillNo(billNo);
        asnMaster.setState(state);    //初始化
        asnMaster.setInsertType(asnData.getInsertType());
        asnMaster.setBillSource(asnData.getBillSource());
		if (asnMaster.getAsnType() == null) {
			asnMaster.setAsnType(24);
		}
        asnMasterService.save(asnMaster);

        //新增detail
        List<AsnDetail> asnDetails = asnData.getAsnDetails();
        for (int i = 0; i < asnDetails.size(); i++) {
            String detailNo = billNo + "-" + (i + 1);
            AsnDetail asnDetail = asnDetails.get(i);
            asnDetail.setBillNo(billNo);
            asnDetail.setDetailNo(detailNo);
            asnDetail.setAcceptQuantity(0D);
            asnDetailService.save(asnDetail);
            //更新采购单的ASN数量 版本更新
            String detailNo1 = asnDetail.getRequestDetailDetailNo();
            RequestBuyDetail requestBuyDetail = requestBuyDetailService.findByDetailNo(detailNo1);
            if (requestBuyDetail != null) {
                requestBuyDetail.setAsnQuantity(asnDetail.getExpectQuantity() + requestBuyDetail.getAsnQuantity());
                requestBuyDetailService.update(requestBuyDetail);
            }
        }

        List<AsnShipDetail> asnShipDetails = asnData.getAsnShipDetails();
        if(asnShipDetails!=null){
            for (int i = 0; i < asnShipDetails.size(); i++) {
                AsnShipDetail asnShipDetail = asnShipDetails.get(i);
                asnShipDetail.setBillNo(billNo);
                asnShipDetail.setCreateTime(DateUtils.getNowDateTimeString());
                asnShipDetailService.save(asnShipDetail);
            }
        }


    }

    /**
     * 归档
     *
     * @param billNo
     * @param currentUser
     */
    @Override
    public void createFile(String billNo, CurrentUser currentUser) {
//        AsnMaster asnMaster = asnMasterService.findBy("billNo", billNo);
//        //状态为 ASN完成状态
//        if (asnMaster.getAsnState() != 3) {
//            throw new ServiceException(CommonCode.ASN_FILE_ERROR);
//        }
//        Integer fileUserId = currentUser.getUserId();
//        String fileUserName = currentUser.getUserName();
//        String fileTime = DateUtils.getNowDateTimeString();
//
//        BillRecord billRecord = new BillRecord();
//        billRecord.setBillNo(billNo);
//        billRecord.setRecordType(6);//ASN归档
//        billRecord.setCreateTime(fileTime);
//        billRecord.setCreateUserId(fileUserId);
//        billRecord.setCreateUserName(fileUserName);
//        billRecordService.save(billRecord);
//        AsnMasterFile asnMasterFile = new AsnMasterFile();
//        BeanUtils.copyProperties(asnMaster, asnMasterFile);
//
//        asnMasterFile.setFileUserId(fileUserId);
//        asnMasterFile.setFileUserName(fileUserName);
//        asnMasterFile.setFileTime(fileTime);
//        asnMasterFileService.save(asnMasterFile);
//        List<AsnDetail> asnDetails = asnDetailService.findByBillNo(billNo);
//
////        BeanUtils.copyProperties(asnDetails, asnDetailFile);
//        for (int i = 0; i < asnDetails.size(); i++) {
//            AsnDetailFile asnDetailFile = new AsnDetailFile();
//            asnDetailFile.setBillNo(asnDetails.get(i).getBillNo());
//            asnDetailFile.setRequestDetailDetailNo(asnDetails.get(i).getRequestDetailDetailNo());
//            asnDetailFile.setDetailNo(asnDetails.get(i).getDetailNo());
//            asnDetailFile.setItemCode(asnDetails.get(i).getItemCode());
//            asnDetailFile.setItemName(asnDetails.get(i).getItemName());
//            asnDetailFile.setPackDetailId(asnDetails.get(i).getPackDetailId());
//            asnDetailFile.setPackDescribe(asnDetails.get(i).getPackDescribe());
//            asnDetailFile.setAcceptQuantity(asnDetails.get(i).getAcceptQuantity());
//            asnDetailFile.setExpectQuantity(asnDetails.get(i).getExpectQuantity());
//            asnDetailFile.setMemo(asnDetails.get(i).getMemo());
//            asnDetailFile.setAcceptTime(asnDetails.get(i).getAcceptTime());
//            asnDetailFile.setState(asnDetails.get(i).getState());
//            asnDetailFile.setAsnUdfDs1(asnDetails.get(i).getAsnUdfDs1());
//            asnDetailFile.setAsnUdfDs2(asnDetails.get(i).getAsnUdfDs2());
//            asnDetailFile.setAsnUdfDs3(asnDetails.get(i).getAsnUdfDs3());
//
//            asnDetailFileService.save(asnDetailFile);
//
//
//        }
//        asnMasterService.deleteByBillNo(billNo);
//        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 6);//ASN归档
//        billRecord1.setModifyUserId(fileUserId);
//        billRecord1.setModifyUserName(fileUserName);
//        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
//        billRecordService.update(billRecord1);
    }

    @Override
    public void createASN(CreateASN createASN, CurrentUser currentUser) {
        AsnMaster asnMaster = new AsnMaster();
        String tableName = "asn_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = createASN.getBillNo();
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        String wareCode = createASN.getWareCode();
        String orgCode = createASN.getOrgCode();
        WareInfo wareInfo = wareInfoService.findBy("wareCode", wareCode);

        Organization organization = organizationService.findBy("organizationCode", orgCode);

        asnMaster.setAsnState(0);
        asnMaster.setBillNo(billNo);
        asnMaster.setCreateTime(createTime);
        asnMaster.setCreateUserId(createUserId);
        asnMaster.setCreateUserName(createUserName);
        asnMaster.setWareId(wareInfo.getWareId());
        asnMaster.setWareName(wareInfo.getWareName());
        asnMaster.setWareId(wareInfo.getWareId());
        asnMaster.setWareCode(wareInfo.getWareCode());
        asnMaster.setOrganizationCode(organization.getOrganizationCode());
        asnMaster.setOrganizationName(organization.getOrganizationName());
        asnMaster.setOrganizationId(organization.getOrganizationId());
        asnMaster.setRequestBuyBillNo(createASN.getBillNo());
        asnMaster.setInsertType(4);
        asnMasterService.save(asnMaster);
        List<CreateASNShipDetail> shipDetails = createASN.getShipList();
        if (shipDetails != null) {
            for (CreateASNShipDetail createASNShipDetail : shipDetails
            ) {
                AsnShipDetail asnShipDetail = asnShipDetailService.findBy("shipBillCode", createASNShipDetail.getShipBillCode());
                if (asnShipDetail == null) {
                    asnShipDetail = new AsnShipDetail();
                    asnShipDetail.setBillNo(asnMaster.getBillNo());
                    asnShipDetail.setShipBillCode(createASNShipDetail.getShipBillCode());
                    asnShipDetail.setShipCode(createASNShipDetail.getShipCode());
                    asnShipDetailService.save(asnShipDetail);
                } else {

                }
            }
        }
        List<CreateASNSKUDetail> asnskuDetails = createASN.getSkuList();
        for (int i = 0; i < asnskuDetails.size(); i++) {
            CreateASNSKUDetail createASNSKUDetail = asnskuDetails.get(i);

            ItemInfo itemInfo = itemInfoService.findBy("itemCode", createASNSKUDetail.getSkuCode());
            if (itemInfo == null) {
                itemInfo = new ItemInfo();
                itemInfo.setItemCode(createASNSKUDetail.getSkuCode());
                itemInfo.setItemName(createASNSKUDetail.getSkuName());
                itemInfo.setImgUrl(createASNSKUDetail.getImgUrl());
                itemInfo.setOrganizationId(organization.getOrganizationId());
                itemInfo.setPackCode("sys_pack");
                itemInfoService.save(itemInfo);
            } else if (itemInfo.getImgUrl() == null || itemInfo.getImgUrl().equals("")) {
                itemInfo.setImgUrl(createASNSKUDetail.getImgUrl());
                itemInfoService.updateItem(itemInfo);
            } else if (itemInfo.getItemName() == null || itemInfo.getItemName().equals("")) {
                itemInfo.setImgUrl(createASNSKUDetail.getSkuName());
                itemInfoService.updateItem(itemInfo);
            }

            AsnDetail asnDetail = new AsnDetail();
            asnDetail.setBillNo(asnMaster.getBillNo());
            asnDetail.setDetailNo(asnMaster.getBillNo() + (i + 1));
            asnDetail.setItemCode(createASNSKUDetail.getSkuCode());
            asnDetail.setItemName(createASNSKUDetail.getSkuName());
            asnDetail.setSpec(createASNSKUDetail.getSpec());
            asnDetail.setModel(createASNSKUDetail.getModel());
            asnDetail.setImgUrl(createASNSKUDetail.getImgUrl());
            asnDetail.setPackCode("sys_pack");
            asnDetail.setPackDetailId(166);
            asnDetail.setPackDescribe("each");
            asnDetail.setTransRatio(1.0);
            asnDetail.setExpectQuantity(createASNSKUDetail.getQuantity());
            asnDetailService.save(asnDetail);

        }

    }

    @Override
    public AsnMasterDto findByBillNo(String billNo) {
        return asnMasterMapper.findByBillNo(billNo);
    }

    @Override
    public AsnMaster findSoByBillNo(String billNo, String wareCode) {
        return asnMasterMapper.findSoByBillNo(billNo, wareCode);
    }

    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private AllotMasterService allotMasterService;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private AllotDetailService allotDetailService;


    @Override
    public void allot(AllotData allotData, CurrentUser currentUser) {
        //生成ASN单
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        BillRecord billRecord =new BillRecord();
        billRecord.setBillNo(allotData.getAllotMaster().getBillNo());
        billRecord.setRecordType(41);//生成ASN
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.createBillRecord(billRecord,currentUser);

        AsnData asnData = new AsnData();
        asnData.setBillSource(4);//调拨生成
        asnData.setInsertType(0);//提交
        AsnMaster asnMaster = new AsnMaster();
        BeanUtils.copyProperties(allotData.getAllotMaster(),asnMaster);

        asnMaster.setBillNo(null);
        asnMaster.setWareId(allotData.getAllotMaster().getToWareId());
        asnMaster.setWareName(allotData.getAllotMaster().getToWareName());
        asnMaster.setAllotBillNo(allotData.getAllotMaster().getBillNo());
        asnMaster.setAsnType(24);
        Carrier carrier = carrierService.findById(allotData.getAllotMaster().getCarrierId());
        if(carrier!=null){
            asnMaster.setCarrierCode(carrier.getCarrierCode());
        }

        ShippingAddress shippingAddress = shippingAddressService.findByThree(allotData.getAllotMaster().getOrganizationId(),allotData.getAllotMaster().getFromWareId(),1);
        //发货地
        if(shippingAddress==null){
            asnMaster.setLoadingPlace(null);
        }else {
            asnMaster.setLoadingPlace(shippingAddress.getProvince()+ shippingAddress.getCity()+shippingAddress.getArea()+shippingAddress.getDetailAddress());
        }
        ShippingAddress shippingAddress1 = shippingAddressService.findByThree(allotData.getAllotMaster().getOrganizationId(),allotData.getAllotMaster().getToWareId(),1);
        //交货地
        if(shippingAddress1==null){
            asnMaster.setDeliveryPlace(null);
        }else {
            asnMaster.setDeliveryPlace(shippingAddress1.getProvince()+ shippingAddress1.getCity()+shippingAddress1.getArea()+shippingAddress1.getDetailAddress());
        }
        asnData.setAsnMaster(asnMaster);
        List<AsnDetail> details = new ArrayList<>();
        for (int i=0;i<allotData.getAllotDetails().size();i++){
            AllotDetail allotDetail = allotData.getAllotDetails().get(i);
            AsnDetail asnDetail = new AsnDetail();
            BeanUtils.copyProperties(allotDetail,asnDetail);
            asnDetail.setAllotDetailNo(allotDetail.getDetailNo());
            asnDetail.setItemCode(allotDetail.getItemCode());
            asnDetail.setItemName(allotDetail.getItemName());
//            asnDetail.setImgUrl(allotDetail.getImgUrl());
            asnDetail.setPackDetailId(allotDetail.getPackDetailId());
            asnDetail.setPackDescribe(allotDetail.getPackDescribe());
            asnDetail.setPackCode("sys_pack");//刚看到，郭总采取强制设置。。。 同上边修改
            asnDetail.setTransRatio(allotDetail.getTransRatio());
            asnDetail.setQcState(1);//合格
            asnDetail.setBillNo(null);
            asnDetail.setDetailNo(null);
            details.add(asnDetail);
        }

        asnData.setAsnDetails(details);
        asnMasterService.createAsnMaster(asnData,currentUser);

        //生成SO单
        SoData soData = new SoData();
        soData.setBillSource(3);//调拨生成
        soData.setInsertType(0);//提交
        BillRecord billRecord1 =new BillRecord();
        billRecord1.setBillNo(allotData.getAllotMaster().getBillNo());
        billRecord1.setRecordType(42);//生成SO
        billRecord1.setCreateTime(createTime);
        billRecord1.setCreateUserId(createUserId);
        billRecord1.setCreateUserName(createUserName);
        billRecordService.createBillRecord(billRecord1,currentUser);

        SoMaster soMaster = new SoMaster();
       // BeanUtils.copyBeanProp(soMaster,allotData.getAllotMaster());

        soMaster.setBillNo(null);
        soMaster.setAllotBillNo(allotData.getAllotMaster().getBillNo());
        soMaster.setWareId(allotData.getAllotMaster().getFromWareId());
        soMaster.setWareName(allotData.getAllotMaster().getFromWareName());
        soMaster.setOrganizationId(allotData.getAllotMaster().getOrganizationId());
        soMaster.setOrganizationName(allotData.getAllotMaster().getOrganizationName());
        soMaster.setSoType(30);
        soMaster.setBillSource(3);
        soData.setSoMaster(soMaster);

        List<SoDetail>soDetails = new ArrayList<>();

        for(AllotDetail allotDetail :allotData.getAllotDetails()){
            SoDetail soDetail = new SoDetail();
            BeanUtils.copyProperties(allotDetail,soDetail);
            soDetail.setAllotDetailNo(allotDetail.getDetailNo());
            soDetail.setOrderQuantity(allotDetail.getExpectQuantity());
            soDetail.setBillNo(null);
            soDetail.setDetailNo(null);
            soDetails.add(soDetail);
        }
        soData.setSoDetails(soDetails);
        soMasterService.createSoMaster(soData,currentUser);

        allotMasterService.allot(allotData.getAllotMaster().getBillNo());


    }

    @Override
    public int update(AsnMaster asnMaster) {
        int rowCount = asnMasterMapper.update(asnMaster);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.ASN_VERSION, "请重试！");
        }
        return rowCount;
    }
    
    @Override
    public void forceCompletion(String billNO) {

    	List<AsnMaster> asnMasters = asnMasterService.findByRequestBuyBillNO(billNO);
    	if(null != asnMasters && asnMasters.size()!=0) {
            for(AsnMaster asnMaster:asnMasters){
                asnMaster.setAsnState(3);
                asnMasterService.update(asnMaster);
            }

    	}
    }

	@Override
	public List<AsnMaster> findByRequestBuyBillNO(String billNO) {
		return asnMasterMapper.findByRequestBuyBillNO(billNO);
	}

    @Override
    public AsnMaster findByOrderId(String soBillNo, Integer asnType, Integer wareId) {
        return asnMasterMapper.findBySoBillNo(soBillNo, asnType, wareId);
    }
}

