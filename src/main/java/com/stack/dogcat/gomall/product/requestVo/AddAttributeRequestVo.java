package com.stack.dogcat.gomall.product.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddAttributeRequestVo {

    //商家id
    private Integer storeId;

    //属性集合id
    @NotNull(message = "属性集合id不能为空")
    private Integer collectionId;

    //属性名
    @NotBlank(message = "属性名不能为空")
    private String attributeName;

    //属性录入方式：0->手工录入；1->从列表中选取
    @NotNull(message = "属性录入方式不能为空")
    private Integer inputType;

    //属性值之间通过‘-'分割
    private String attributeValuesString;
}
