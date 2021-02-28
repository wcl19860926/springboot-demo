package com.study.common.base.dto;



/**
 * vo 的分页数据格式
 *
 */





public class Page{

    private  static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_TOTAL_RECORD = 0;
    private static final int MIN_PAGE_INDEX = 1;
    private int pageSize;
    private int totalPage;
    private int totalRecord;
    private int pageIndex;

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Page(int pageSize, int pageIndex) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.totalRecord = 0;
    }

    public Page() {
        this.pageSize = MAX_PAGE_SIZE;
        this.pageIndex = MIN_PAGE_INDEX;
        this.totalRecord = DEFAULT_TOTAL_RECORD;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (this.totalRecord % this.pageSize == 0) {
            this.totalPage = this.totalRecord / this.pageSize;
        } else {
            this.totalPage = this.totalRecord / this.pageSize + 1;
        }
    }

    public int getTotalPage() {
        return totalPage;
    }


    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        if (this.totalRecord % this.pageSize == 0) {
            this.totalPage = this.totalRecord / this.pageSize;
        } else {
            this.totalPage = this.totalRecord / this.pageSize + 1;
        }
        this.totalRecord = totalRecord;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if (pageIndex <= 0) {
            pageIndex = MIN_PAGE_INDEX;
        }
        if (pageIndex >= this.getTotalPage()) {
            pageIndex = this.getTotalPage();
        }
        this.pageIndex = pageIndex;
    }

}