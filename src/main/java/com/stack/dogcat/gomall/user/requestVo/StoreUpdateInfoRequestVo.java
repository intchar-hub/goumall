package com.stack.dogcat.gomall.user.requestVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/12 19:59
 * @Descrition TODO
 */
public class StoreUpdateInfoRequestVo {

    private Integer id;

    private String shipAddress;

    private String avatarPath;

    private String password;

    private String description;

    @Override
    public String toString() {
        return "StoreUpdateInfoRequestVo{" +
                "id=" + id +
                ", shipAddress='" + shipAddress + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
