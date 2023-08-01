package com.deer.wms.project.root.util;

/**
 * 2020-07-29<br>
 * Java8 Optional 简化易用版空指针校验工具：<br>
 * 推荐在业务代码使用，不推荐在框架代码使用。<br>
 * 
 * @author xuesinuo
 */
public class Np<T> {
    private Np() {}

    private T t;

    /**
     * input a value
     */
    public static <T> Np<T> i(T t) {
        Np<T> np = new Np<T>();
        np.t = t;
        return np;
    }

    /**
     * do something, do not change the value
     */
    public Np<T> doo(java.util.function.Consumer<T> consumer) {
        if (this.t != null) {
            consumer.accept(this.t);
        }
        return this;
    }

    /**
     * do something many times
     */
    public Np<T> doo(java.util.function.Consumer<T> consumer, int repeat) {
        if (this.t != null) {
            for (int i = 0; i < repeat; i++) {
                consumer.accept(this.t);
            }
        }
        return this;
    }

    /**
     * do something and get the result into another Np
     */
    public <R> Np<R> x(java.util.function.Function<T, R> function) {
        Np<R> np = new Np<R>();
        if (this.t != null) {
            R r = function.apply(this.t);
            np.t = r;
        }
        return np;
    }

    /**
     * do a judge. If true, Continue. If false, set Np value null.
     */
    public Np<T> iff(java.util.function.Function<T, Boolean> function) {
        return this.els(function, null);
    }

    /**
     * do a judge. If true, Continue. If false, set another value.
     */
    public Np<T> els(java.util.function.Function<T, Boolean> function, T t) {
        if (this.t != null) {
            Boolean r = function.apply(this.t);
            if (r != null && r == true) {
                return this;
            }
        }
        Np<T> np = new Np<T>();
        np.t = t;
        return np;
    }

    /**
     * if value is null, set a default value.
     */
    public Np<T> ifNull(T t) {
        if (this.t == null) {
            Np<T> np = new Np<T>();
            np.t = t;
            return np;
        }
        return this;
    }

    /**
     * if value.toString() is blank, set a default value.
     */
    public Np<T> ifBlank(T t) {
        if (this.t == null || this.t.toString() == null || this.t.toString().trim().equals("")) {
            Np<T> np = new Np<T>();
            np.t = t;
            return np;
        }
        return this;
    }

    /**
     * output the value
     */
    public T o() {
        return t;
    }

    /**
     * output the value, if null, set a default value.
     */
    public T o(T def) {
        if (t == null) {
            return def;
        }
        return t;
    }

    /**
     * value is null?
     */
    public boolean isNull() {
        return t == null;
    }

    /**
     * value is not null?
     */
    public boolean notNull() {
        return t != null;
    }

    /**
     * value equals to ...?<br>
     * p.s. target value can be null (call isNull better)
     */
    public boolean eq(Object target) {
        return ObjectCompareUtil.eq(t, target);
    }

    /**
     * value not equals to ...?<br>
     * p.s. target value can be null (call notNull better)
     */
    public boolean notEq(Object target) {
        return !this.eq(target);
    }

    /**
     * value equals this, that, or that?<br>
     * p.s. target value can be null
     */
    public boolean in(Object... target) {
        return ObjectCompareUtil.in(t, target);
    }

    /**
     * value not equals this, that, or that?<br>
     * p.s. target value can be null
     */
    public boolean notIn(Object... target) {
        return !this.in(target);
    }

    /**
     * value is null? return null. not null? return toString().
     */
    public String toString() {
        if (t == null) {
            return null;
        }
        return t.toString();
    }

    /**
     * value is null? return the default. not null? return toString().
     */
    public String toString(String def) {
        if (t == null) {
            return def;
        }
        return t.toString();
    }
}
