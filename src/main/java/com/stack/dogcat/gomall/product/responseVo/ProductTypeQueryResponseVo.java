package com.stack.dogcat.gomall.product.responseVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/14 16:17
 * @Descrition TODO
 */
public class ProductTypeQueryResponseVo {

    private Integer id;

    private String name;

    private Integer parentId;

    private LocalDateTime gmtCreate;

    @Override
    public String toString() {
        return "ProductTypeQueryResponseVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", gmtCreate=" + gmtCreate +
                ", children=" + children +
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

    private List<ProductTypeChild> children;

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

    public List<ProductTypeChild> getChildren() {
        return children;
    }

    public void setChildren(List<ProductTypeChild> children) {
        this.children = children;
    }
}
