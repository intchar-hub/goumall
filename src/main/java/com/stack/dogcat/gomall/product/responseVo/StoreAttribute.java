package com.stack.dogcat.gomall.product.responseVo;

import lombok.Data;

import java.util.List;

@Data
public class StoreAttribute {

    private Integer id;

    private String name;

    private Integer inputType;

    private List<StoreValue> values;
}
