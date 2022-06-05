package com.maozhua.utils;

import java.util.List;

/**
 * @author sryzzz
 * @create 2022/6/5 17:44
 * @description 用来返回分页Grid的数据格式
 */
public class PagedGridResult {

    /**
     * 当前页数
     */
    private int page;

    /**
     * 总页数
     */
    private long total;

    /**
     * 总记录数
     */
    private long records;

    /**
     * 每行显示的内容
     */
    private List<?> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
