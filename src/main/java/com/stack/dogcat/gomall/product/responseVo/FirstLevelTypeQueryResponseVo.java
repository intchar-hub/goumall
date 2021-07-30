package com.stack.dogcat.gomall.product.responseVo;

public class FirstLevelTypeQueryResponseVo {

    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "FirstLevelTypeQueryResponseVo{" +
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
