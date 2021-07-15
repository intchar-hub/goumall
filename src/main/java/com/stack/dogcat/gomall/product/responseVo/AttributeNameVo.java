package com.stack.dogcat.gomall.product.responseVo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Data
public class AttributeNameVo {

    private Integer attributeNameId;

    private String attributeName;

    private Integer attributeCollectionId;

    private Integer inputType;

    private LocalDateTime gmtCreate;

    private ArrayList<String> attributeValueList;


}
