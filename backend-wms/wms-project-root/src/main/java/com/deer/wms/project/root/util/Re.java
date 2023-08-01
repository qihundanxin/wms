package com.deer.wms.project.root.util;

/**
 * RE: REturn and Runtime Exception
 * 
 * <pre>
 * 编写一个逻辑方法，由外部决定是否开启异常时使用。
 * </pre>
 * 
 * @author xuesinuo
 */
public class Re<R> {
    private Re() {}

    private R r;

    private RuntimeException e;

    /**
     * 返回方式
     * 
     * @author xuesinuo
     */
    public enum Mode {
        /** Return */
        R,
        /** Exception */
        E;
    }

    public static <R> Re<R> i(R r, RuntimeException e) {
        Re<R> re = new Re<>();
        re.r = r;
        re.e = e;
        return re;
    }

    public static <R> Re<R> i(Class<R> rClass, RuntimeException e) {
        Re<R> re = new Re<>();
        re.r = null;
        re.e = e;
        return re;
    }

    public R o(Mode m) {
        if (m == Mode.E) {
            throw this.e;
        }
        return this.r;
    }
}
