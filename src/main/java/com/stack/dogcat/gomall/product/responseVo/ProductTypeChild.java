package com.stack.dogcat.gomall.product.responseVo;

import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/14 16:17
 * @Descrition TODO
 */
public class ProductTypeChild {

    private Integer id;

    private String name;

    private Integer parentId;

    private LocalDateTime gmtCreate;

    @Override
    public String toString() {
        return "ProductTypeChild{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", gmtCreate=" + gmtCreate +
                '}';
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
