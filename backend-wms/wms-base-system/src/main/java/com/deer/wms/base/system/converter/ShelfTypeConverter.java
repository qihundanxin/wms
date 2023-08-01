package com.deer.wms.base.system.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * EasyExcel 货位组读取
 * 
 * @ClassName ShelfTypeConverter
 * @Author luolin
 * @Date 2023/6/7 10:48
 * @Version 1.0
 **/
public class ShelfTypeConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        // 实体类中对象属性类型
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // Excel中对应的CellData属性类型
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData cellData, ExcelContentProperty excelContentProperty,
                                     GlobalConfiguration globalConfiguration) {
        // 从Cell中读取数据
        String shelfType = cellData.getStringValue();
        // 判断Excel中的值，将其转换为预期的数值
        switch(shelfType) {

            case "地堆":
                return 1;

            case "货架":
                return 2;

            case "上架车":
                return 4;

            default:
                return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer integer, ExcelContentProperty excelContentProperty,
                                               GlobalConfiguration globalConfiguration) {
        // 判断实体类中获取的值，转换为Excel预期的值，并封装为CellData对象
//        if (integer == null) {
//            return new CellData<>("");
//        } else if (integer == 0) {
//            return new CellData<>("男");
//        } else if (integer == 1) {
//            return new CellData<>("女");
//        }
        return new WriteCellData<>("");
    }
}
