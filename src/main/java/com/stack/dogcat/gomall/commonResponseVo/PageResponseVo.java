package com.stack.dogcat.gomall.commonResponseVo;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/6/30 9:04
 * @Descrition TODO
 */
public class PageResponseVo<T> {

    /**
     * 当前页数
     */
    private Integer currentPage = 0;

    /**
     * 总页数
     */
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    private Integer totalCount = 0;

    /**
     * 一页有的记录数，不是data的大小
     */
    private Integer pageSize = 0;

    /**
     * 分页数据
     */
    private List<T> data = null;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageResponseVo(IPage<T> page) {
        this.currentPage = (int)page.getCurrent();
        this.totalPage = (int)page.getPages();
        this.totalCount = (int)page.getTotal();
        this.pageSize = (int)page.getSize();
        this.data = page.getRecords();
    }

    public PageResponseVo() {
    }

}
