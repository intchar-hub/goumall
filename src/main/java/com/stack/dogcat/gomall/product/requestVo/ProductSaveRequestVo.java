package com.stack.dogcat.gomall.product.requestVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Yang Jie
 * @Date 2021/7/15 14:52
 * @Descrition TODO
 */
public class ProductSaveRequestVo {

    @NotNull(message = "店铺不能为空")
    private Integer storeId;

    @NotNull(message = "分类不能为空")
    private Integer typeId;

    @NotEmpty(message = "商品名不能为空")
    private String name;

    @NotEmpty(message = "商品描述不能为空")
    private String description;

    @NotEmpty(message = "商品图片不能为空")
    private String imagePath;

    @NotNull(message = "属性集合不能为空")
    private Integer attributeCollectionId;

    @NotEmpty(message = "商品规格不能为空")
    private String skusString;

    @Override
    public String toString() {
        return "ProductSaveRequestVo{" +
                "storeId=" + storeId +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", attributeCollectionId=" + attributeCollectionId +
                ", skusString='" + skusString + '\'' +
                '}';
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getAttributeCollectionId() {
        return attributeCollectionId;
    }

    public void setAttributeCollectionId(Integer attributeCollectionId) {
        this.attributeCollectionId = attributeCollectionId;
    }

    public String getSkusString() {
        return skusString;
    }

    public void setSkusString(String skusString) {
        this.skusString = skusString;
    }
}
