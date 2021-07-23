package com.stack.dogcat.gomall.content.responseVo;

import lombok.Data;

import java.util.List;

@Data
public class StoreCollectionResponseVo {

    private Integer storeCollectionId;

    private String storeName;

    private Integer storeId;

    private String avatarPath;

    private String description;

    private List<String> imagePaths;

}
