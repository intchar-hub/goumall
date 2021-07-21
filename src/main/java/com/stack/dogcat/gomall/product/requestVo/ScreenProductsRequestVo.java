package com.stack.dogcat.gomall.product.requestVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Yang Jie
 * @Date 2021/7/16 16:33
 * @Descrition TODO
 */
public class ScreenProductsRequestVo {

    @NotNull(message = "商家id不能为空")
    private Integer storeId;

    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @NotNull(message = "页面大小不能为空")
    private Integer pageSize;

    private String name;

    private Integer typeId;

    private Integer stockNum;

    private String columnName;

    private String columnOrder;

    @Override
    public String toString() {
        return "ScreenProductsRequestVo{" +
                "storeId=" + storeId +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", stockNum=" + stockNum +
                ", columnName='" + columnName + '\'' +
                ", columnOrder='" + columnOrder + '\'' +
                '}';
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(String columnOrder) {
        this.columnOrder = columnOrder;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNums) {
        this.pageNum = pageNums;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }
}
