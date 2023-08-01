package com.deer.wms.quartz.model;

import lombok.ToString;

@ToString
public class SysJobLogCriteria extends SysJobLog {
    private static final long serialVersionUID = 1L;

    private String keyWord;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页显示的条数
     */
    private Integer pageSize = 199999;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
