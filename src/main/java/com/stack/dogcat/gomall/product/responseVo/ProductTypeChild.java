package com.stack.dogcat.gomall.product.responseVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/14 16:17
 * @Descrition TODO
 */
public class ProductTypeChild {

    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "ProductTypeChild{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
