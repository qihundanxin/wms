package com.deer.wms.project.root.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import lombok.Data;

/**
 * 《超级遍历器》：一个对多次查询集合进行遍历的语法糖工具<br>
 * 
 * <pre>
 * 场景：对20000+数据进行数据库查询并遍历统计或操作时，一次从数据库查询全部数据可能会导致内存占用过高。
 * 
 * 这时，可能得解决方案：
 * jdbcTemplate.queryForRowSet(sql);// JDBC使用resultSet
 * resultSetType="FORWARD_ONLY"// MyBatis使用游标
 * 这些都是Dao层解决方案。
 * 
 * service层解决方案可以使用{@link SuperIterator}！对调用函数得到大List型结果进行多次调用拆分的封装。
 * 需要给定3个值：key（初始遍历标识）、searchFunction（查询函数）、nextKFunction（迭代Key的函数）。
 * 这样，最终得到一个“超级遍历器”，本质是双层遍历器嵌套。
 * 
 * eg:
 * int row = 100;// 每次查询100个
 * SuperIterator superIterator = SuperIterator.ba(0, idMin -&gt; server.getList(idMin, row), obj -&gt; obj.getId());
 * int i = 0;// 遍历标识，可以用其他方式代替，比如List...
 * for (Object item : superIterator) {
 *     // item操作：写入文件、发送Http请求，攒够一定数量进行一次操作。
 *     // 但不要将全部item存入集合变量，否则失去了分配查询的意义。
 *     if(++i == 100000){// 一共查询10万个
 *         break;
 *     }
 * }
 * </pre>
 * 
 * @author xuesinuo
 *
 * @param <K> key 查询的唯一标识，一般为ID
 * @param <R> result 遍历时得到的单个对象
 */
@Data
public class SuperIterator<K, R> implements Iterator<R>, Iterable<R> {

    private final Function<K, Iterator<R>> searchFunctionA;

    private final Function<K, Collection<R>> searchFunctionB;

    private final Function<R, K> nextKFunctionA;

    private final Function<K, K> nextKFunctionB;

    private Iterator<R> iterator;

    private K key;

    private R lastR;

    private SuperIterator(Function<K, Iterator<R>> searchFunctionA, Function<K, Collection<R>> searchFunctionB, Function<R, K> nextKFunctionA, Function<K, K> nextKFunctionB, K key) {
        this.searchFunctionA = searchFunctionA;
        this.searchFunctionB = searchFunctionB;
        this.nextKFunctionA = nextKFunctionA;
        this.nextKFunctionB = nextKFunctionB;
        if (this.searchFunctionA == null && this.searchFunctionB == null
                || this.nextKFunctionA == null && this.nextKFunctionB == null) {
            throw new RuntimeException("遍历器构建异常");
        }
        if (this.searchFunctionA != null) {
            this.iterator = this.searchFunctionA.apply(key);
        } else if (this.searchFunctionB != null) {
            this.iterator = this.searchFunctionB.apply(key).iterator();
        }
        this.key = key;
    }

    /**
     * 模式AA：以上一次的最后一个结果查询下一个Key。适用于自增ID或日期为初始条件的列表查询。
     * 
     * @param <K>             Key数据类型
     * @param <R>             Result数据类型
     * @param key             初始Key
     * @param searchFunctionA 查询函数
     * @param nextKFunctionA  下一个Key函数：以上一次的最后一个结果查询下一个Key
     * @return 多次查询结果的全局遍历器
     */
    public static <K, R> SuperIterator<K, R> aa(K key, Function<K, Iterator<R>> searchFunctionA, Function<R, K> nextKFunctionA) {
        return new SuperIterator<K, R>(searchFunctionA, null, nextKFunctionA, null, key);
    }

    /**
     * 模式BA：以上一次的最后一个结果查询下一个Key。适用于自增ID或日期为初始条件的列表查询。
     * 
     * <pre>
     * // eg: 
     * Iterator&lt;Bean&gt; iterator = ResultIterator.ba(0, idMin -&gt; server.getList(idMin, count), r -&gt; r.getId() + 1);
     * </pre>
     * 
     * @param <K>             Key数据类型
     * @param <R>             Result数据类型
     * @param key             初始Key
     * @param searchFunctionB 查询函数
     * @param nextKFunctionA  下一个Key函数：以上一次的最后一个结果查询下一个Key
     * @return 多次查询结果的全局遍历器
     */
    public static <K, R> SuperIterator<K, R> ba(K key, Function<K, Collection<R>> searchFunctionB, Function<R, K> nextKFunctionA) {
        return new SuperIterator<K, R>(null, searchFunctionB, nextKFunctionA, null, key);
    }

    /**
     * 模式AB：以上个Key查询下一个Key。适用于分页查询或二维数组遍历。
     * 
     * @param <K>             Key数据类型
     * @param <R>             Result数据类型
     * @param searchFunctionA 查询函数
     * @param nextKFunctionB  下一个Key函数：以上个Key查询下一个Key
     * @param key             初始Key
     * @return 多次查询结果的全局遍历器
     */
    public static <K, R> SuperIterator<K, R> ab(K key, Function<K, Iterator<R>> searchFunctionA, Function<K, K> nextKFunctionB) {
        return new SuperIterator<K, R>(searchFunctionA, null, null, nextKFunctionB, key);
    }

    /**
     * 模式BB：以上个Key查询下一个Key。适用于分页查询或二维数组遍历。
     * 
     * <pre>
     * // eg1: 注意数组越界
     * String[][] array = { { "1", "2" }, { "3", "4" }, { "5", "6" } };
     * Iterator&lt;String&gt; iterator = ResultIterator.bb(0, i -&gt; i &lt; array.length ? Arrays.asList(array[i]) : null, i -&gt; i + 1);
     * // eg2:
     * Iterator&lt;Bean&gt; iterator = ResultIterator.bb(0, page -&gt; server.getPage(param, page, count), page -&gt; page + 1);
     * </pre>
     * 
     * @param <K>             Key数据类型
     * @param <R>             Result数据类型
     * @param searchFunctionB 查询函数
     * @param nextKFunctionB  下一个Key函数：以上个Key查询下一个Key
     * @param key             初始Key
     * @return 多次查询结果的全局遍历器
     */
    public static <K, R> SuperIterator<K, R> bb(K key, Function<K, Collection<R>> searchFunctionB, Function<K, K> nextKFunctionB) {
        return new SuperIterator<K, R>(null, searchFunctionB, null, nextKFunctionB, key);
    }

    @Override
    public synchronized boolean hasNext() {
        if (iterator == null) {
            return false;
        }
        if (!iterator.hasNext()) {
            if (nextKFunctionA != null) {
                key = nextKFunctionA.apply(lastR);
            } else if (nextKFunctionB != null) {
                key = nextKFunctionB.apply(key);
            } else {
                return false;
            }
            if (key == null) {
                return false;
            }
            if (searchFunctionA != null) {
                iterator = searchFunctionA.apply(key);
            } else if (searchFunctionB != null) {
                Collection<R> cr = searchFunctionB.apply(key);
                if (cr == null || cr.isEmpty()) {
                    return false;
                }
                iterator = cr.iterator();
            } else {
                return false;
            }
            return iterator.hasNext();
        }
        return true;
    }

    @Override
    public synchronized R next() {
        if (!hasNext()) {
            return null;
        }
        lastR = iterator.next();
        return lastR;
    }

    @Override
    public Iterator<R> iterator() {
        return this;
    }
}
