package com.stack.dogcat.gomall.user.responseVo;

import java.time.LocalDateTime;

/**
 * <p>
 * 商店信息表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public class StoreInfoQueryResponseVo {

    private Integer id;

    private String userName;

    private String avatarPath;

    private String email;

    private String storeName;

    private String phoneNumber;

    /**
     * 发货地址
     */
    private String shipAddress;

    private Integer fansNum;

    private String  description;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    @Override
    public String toString() {
        return "StorePwdLoginResponseVo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", email='" + email + '\'' +
                ", storeName='" + storeName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", fansNum=" + fansNum +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
