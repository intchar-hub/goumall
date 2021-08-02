package com.stack.dogcat.gomall.user.responseVo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商店信息（供顾客查询）
 */
public class StoreQueryResponseVo {

    private Integer id;

    private String avatarPath;

    private String storeName;

    private String shipAddress;

    private Integer fansNum;

    private String  description;

    private LocalDateTime gmtCreate;

    private List<String> imagePaths;

    @Override
    public String toString() {
        return "StoreQueryResponseVo{" +
                "id=" + id +
                ", avatarPath='" + avatarPath + '\'' +
                ", storeName='" + storeName + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", fansNum=" + fansNum +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", imagePaths=" + imagePaths +
                '}';
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
