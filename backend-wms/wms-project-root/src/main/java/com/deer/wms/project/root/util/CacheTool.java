package com.deer.wms.project.root.util;

import java.util.List;

/**
 * 缓存工具：
 * 
 * <pre>
 * 1. 一般缓存，存储一个完整的对象
 * 2. 列表缓存，存储若干个对象，按一定顺序查询（先进先出、后进先出）
 * 3. 计数器，不失效，只做数字的增减，默认即为0
 * </pre>
 * 
 * @author xuesinuo
 *
 */
public interface CacheTool {
    String SEPARATOR = ":";

    /**
     * 指定缓存失效时间
     * 
     * @param key 分组
     * @param id  键
     * @param sec 时间(秒)
     * @return 是否成功
     */
    Boolean expire(String key, String id, long sec);

    /**
     * 根据id 获取过期时间
     * 
     * @param key 分组
     * @param id  键 不能为null
     * @return 时间(秒) 返回-1代表为永久有效 失效时间为0，说明该主键未设置失效时间（失效时间默认为-1）
     */
    Long getExpire(String key, String id);

    /**
     * 判断id是否存在
     * 
     * @param key 分组
     * @param id  键
     * @return 是否存在
     */
    boolean has(String key, String id);

    /**
     * 根据分组，统计个数（或 判断分组是否存在）
     * 
     * @param key 分组
     * @return 个数
     */
    long countByKey(String key);

    /**
     * 删除缓存
     * 
     * @param key 分组
     * @param id  可以传0个、1个、N个。0个时，不会批量删除分组下全部缓存，而是本分组不区分ID。
     */
    void del(String key, String... id);

    /**
     * 根据Key删除整组的，也删除无ID的本分组缓存。具体参考{@link CacheTool.SEPARATOR}
     * 
     * <pre>
     * Ex1: key = "ck1:"; 删除 "ck1:" 与 "ck1:*"
     * Ex2: key = "ck2"; 删除 "ck2"
     * </pre>
     * 
     * @param key 分组
     */
    void delByKey(String key);

    /**
     * 普通缓存获取
     * 
     * @param <T> 值的数据类型
     * @param key 分组
     * @param id  键
     * @return 值
     */
    <T> T getObject(String key, String id);

    /**
     * 普通缓存放入并设置时间
     * 
     * @param <T>   值的数据类型
     * @param key   分组
     * @param id    键
     * @param value 值
     * @param sec   时间(秒) time要大于0
     */
    <T> void setObject(String key, String id, T value, long sec);

    /**
     * 【大List慎用！】获取List缓存<br>
     * 
     * 操作此List不影响缓存内容！
     * 
     * @param <T> 值的数据类型
     * @param key 分组
     * @param id  键
     * @return list
     */
    <T> List<T> getList(String key, String id);

    /**
     * List添加元素并刷新超时
     * 
     * @param <T>   值的数据类型
     * @param key   分组
     * @param id    键
     * @param value 值
     * @param sec   为List刷新超时秒，小于等于0时沿用以前的超时时间
     */
    <T> void addListItem(String key, String id, T value, long sec);

    /**
     * List添加元素并刷新超时
     * 
     * @param <T>   值的数据类型
     * @param key   分组
     * @param id    键
     * @param value 值
     * @param index 插入的位置
     * @param sec   为List刷新超时秒，小于等于0时沿用以前的超时时间
     */
    <T> void addListItem(String key, String id, T value, int index, long sec);

    /**
     * 移除并取出
     * 
     * @param <T>   值的数据类型
     * @param key   分组
     * @param id    键
     * @param index 取出的位置
     * @return 值
     */
    <T> T removeListItem(String key, String id, int index);

    /**
     * 查询List长度，未定义的List为0
     * 
     * @param key 分组
     * @param id  键
     * @return 长度
     */
    int getListSize(String key, String id);

    /**
     * 查询List的元素
     * 
     * @param <T>   值的数据类型
     * @param key   分组
     * @param id    键
     * @param index 查询的位置
     * @return 值
     */
    <T> T getListItem(String key, String id, int index);

    /**
     * （计数器）查询一个Long整数（查不到则为0）
     * 
     * @param key 分组
     * @param id  键
     * @return 整数值
     */
    long getCounter(String key, String id);

    /**
     * （计数器）在已有Long整数上进行加法运算，返回运算后的结果
     * 
     * @param key   分组
     * @param id    键
     * @param delta 变化量
     * @return 运算后的结果
     */
    long addCounter(String key, String id, long delta);

}
