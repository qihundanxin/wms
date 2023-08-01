package com.deer.wms.base.system.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import com.deer.wms.base.system.annotation.ExcelImportValid;
import com.deer.wms.base.system.model.Cell.CellExcelModel;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CellListener
 * @Description 货位导入监听
 * @Author luolin
 * @Date 2023/6/27 19:39
 * @Version 1.0
 **/
@Slf4j
public class CellListener extends AnalysisEventListener<CellExcelModel> {

    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 20;
    @Getter
    List<CellExcelModel> list = new ArrayList<>(BATCH_COUNT);

    @Override
    public void invoke(CellExcelModel cellExcelModel, AnalysisContext analysisContext) {
        log.info("数据校验:"+cellExcelModel);
        ExcelImportValid.valid(cellExcelModel);
        list.add(cellExcelModel);
        if (list.size() >= BATCH_COUNT) {
            log.info("已经解析"+list.size()+"条数据");
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条表头数据:{}", JSON.toJSONString(headMap));
        List<String> voHead = new ArrayList<>();
        Field[] fields = CellExcelModel.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            ExcelProperty myField2 = fields[i].getAnnotation(ExcelProperty.class);
            voHead.add(myField2.value()[0]);
        }
        int i=0;
        for(Integer key : headMap.keySet()){
            log.info("表头数据校验："+headMap.get(key));
            if(headMap.get(key) == null ){
                throw new ServiceException(CommonCode.SERVICE_ERROR,"第"+(i+1)+"列表头格式不正确！");
            }
            if(i == voHead.size()){
                throw new ServiceException(CommonCode.SERVICE_ERROR,"上传文件表头超过模板列数，正确表头为："+voHead);
            }
            if(!headMap.get(key).equals(voHead.get(i))){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "第"+(i+1)+"列【"+headMap.get(key)+"】附近表头模板异常!\n正确表头顺序为：\n"+voHead+ "!");
            }
            i++;
        }
    }
}
