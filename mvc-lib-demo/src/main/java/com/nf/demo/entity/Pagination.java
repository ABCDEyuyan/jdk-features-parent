package com.nf.demo.entity;

public class Pagination {
    private int pageSize ;
    private  int pageNo ;
    private Long recordCount;
    private int totalPages;

    public Pagination(int pageNo, Long recordCount) {
        this(2, pageNo, recordCount);
    }

    public Pagination(int pageSize, int pageNo, Long recordCount) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.recordCount = recordCount;
        this.totalPages = (int)Math.ceil(recordCount*1.0/pageSize);
    }

    public int getFirst(){
        return  1;
    }

    public int getLast(){
        return this.totalPages;
    }

    public int getNext(){
        return Math.min(this.pageNo+1,this.totalPages);
    }

    public int getPrev() {
        return Math.max(this.pageNo-1,1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public Long getRecordCount(){
        return  this.recordCount;
    }

    public int getTotalPages(){
        return this.totalPages;
    }


    public int getStart(){
        return (this.pageNo-1)*this.pageSize;
    }

    public int getCount(){
        return this.pageSize;
    }
}
