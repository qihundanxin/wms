package com.deer.wms.project.root.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 
 * 
 * @ClassName BatchesInsertUtil
 * @Author luolin
 * @Date 2023/6/7 17:06
 * @Version 1.0
 **/
public class BatchesInsertUtil {
    /**
     * 拆分List并执行目标方法
     * 
     * @param <T>        List泛型
     * @param list       被拆分的List
     * @param insertFunc 要执行的方法
     */
    public static <T> void batchSplitInsert(List<T> list, Consumer insertFunc) {
        int maxInsertItemNumPerTime = 300;
        List<List<T>> all = new ArrayList<>();
        List<T> subList = new ArrayList<>();
        if (list.size() > maxInsertItemNumPerTime) {
            int i = 0;
            while (i < list.size()) {
                if (i + maxInsertItemNumPerTime > list.size()){
                    subList = list.subList(i, list.size());
                }else {
                    subList = list.subList(i, i + maxInsertItemNumPerTime);
                }
                i = i + maxInsertItemNumPerTime;
                all.add(subList);
            }
            all.parallelStream().forEach(insertFunc);
        } else {
            insertFunc.accept(list);
        }
    }
}
