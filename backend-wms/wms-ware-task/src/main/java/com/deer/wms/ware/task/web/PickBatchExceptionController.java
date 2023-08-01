package com.deer.wms.ware.task.web;

import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.ware.task.model.pickTask.PickBatchExcepitonResultVo;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchExceptionParam;
import com.deer.wms.ware.task.service.PickBatchExceptionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pickBatch/exception")
@RequiredArgsConstructor
public class PickBatchExceptionController {

    @Autowired
    private PickBatchExceptionService pickBatchExceptionService;
    @Autowired
    private FileS3Tool fileS3Tool;
    @Autowired
    private UserInfoService userInfoService;

    @Data
    public static class GetPickBatchExceptionParam{
        private Date date;
    }

    /**
     * 波次拣货异常处理列表
     * @author luolin
     * @param param
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getExceptionList")
    @Authority("pickBatch_exception_getExceptionList")
    public SimpleMap getExceptionList(@RequestBody @Validated GetPickBatchExceptionParam param){
        PickBatchExceptionParam searchParam = new PickBatchExceptionParam();
        if (param.getDate() != null) {
            Date date = param.getDate();
            Date startDate = DateUtils.getDateStartTime(date);
            Date endDate = DateUtils.addDay(startDate, 1);
            searchParam.setStartDate(startDate);
            searchParam.setEndDate(endDate);
        }
        List<PickBatchExcepitonResultVo> exceptionList = pickBatchExceptionService.getExceptionList(searchParam);
        return  SimpleMap.init().append("list", exceptionList);
    }

    @Data
    public static class GetCellAndItemParam{
        private Long id;
        @NotBlank
        private String cellCode;
        @NotBlank
        private String itemCode;
    }

    @Data
    public static class ExceptionResponse extends PickBatchException{
        private List<String> imgUrlList;
        private String userName;
    }

    /**
     * 根据货位和sku查询波次拣货异常信息
     * @author luolin
     * @param param
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getCellAndItemExceptionList")
    @Authority("pickBatch_exception_getCellAndItemExceptionList")
    public Object getCellAndItemExceptionList(@RequestBody GetCellAndItemParam param){
        PickBatchExceptionParam searchParam = new PickBatchExceptionParam();
        searchParam.setCellCode(param.getCellCode());
        searchParam.setItemCode(param.getItemCode());
        List<PickBatchExcepitonResultVo> exceptionList = pickBatchExceptionService.getExceptionList(searchParam);
        List<PickBatchException> list = pickBatchExceptionService.getAll(searchParam);
        List<ExceptionResponse> resList = new ArrayList<>();
        for (PickBatchException item : list){
            UserInfo userInfo = userInfoService.findById(item.getReportUserId());
            ExceptionResponse res = new ExceptionResponse();
            BeanUtils.copyProperties(item, res);
            List<String> urls = new ArrayList<>();
            if (item.getImgUrl() != null && item.getImgUrl().length() > 0){
                for (String str : item.getImgUrl().split(",")){
                    str = fileS3Tool.getUrl(str,7*60*60*24, 7*60*60*24);
                    urls.add(str);
                }
            }
            res.setImgUrlList(urls);
            res.setUserName(userInfo.getUserName());
            resList.add(res);
        }
        return SimpleMap.init().append("pickBatchException",exceptionList.get(0)).append("list", resList);
    }

    /**
     * 修改波次拣货异常列表处理状态
     * @author luolin
     * @param param
     **/
    @PostMapping("/handleExcepiton")
    @Authority("pickBatch_exception_handleExcepiton")
    public void handleBatchExcepiton(@RequestBody @Validated GetCellAndItemParam param, @User CurrentUser currentUser){
        PickBatchException pickBatchException = new PickBatchException();
        if (param.getId() != null){
            pickBatchException.setId(param.getId());
        }else {
            pickBatchException.setCellCode(param.getCellCode());
            pickBatchException.setItemCode(param.getItemCode());
        }
        pickBatchException.setHandleUserId(currentUser.getUserId());
        pickBatchException.setHandleTime(DateUtils.now());
        pickBatchException.setStatus(2);
        pickBatchExceptionService.handleExcepiton(pickBatchException);
    }
}
