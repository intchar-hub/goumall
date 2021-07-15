package com.stack.dogcat.gomall.product.responseVo;

import lombok.Data;

import java.util.List;

@Data
public class StoreAttributesListResponseVo {

    private Integer id;

    private String name;

    private List<StoreAttribute> attributes;
}
