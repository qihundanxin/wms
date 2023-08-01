package com.deer.wms.project.root.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单Map，基于HashMap，String做键，并方便初始化
 * 
 * @author xuesinuo
 *
 */
public class SimpleMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 2887887422952950023L;

    public SimpleMap() {}

    public static SimpleMap init() {
        return new SimpleMap();
    }

    public static SimpleMap init(Map<?, ?> map) {
        SimpleMap result = new SimpleMap();
        if (map != null) {
            for (Object k : map.keySet()) {
                result.append(k.toString(), map.get(k));
            }
        }
        return result;
    }

    public static SimpleMap init(String k, Object v) {
        return new SimpleMap().append(k, v);
    }

    public SimpleMap append(String k, Object v) {
        this.put(k, v);
        return this;
    }
}
