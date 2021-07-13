package com.stack.dogcat.gomall.content.responseVo;

import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/13 11:26
 * @Descrition TODO
 */
public class ComplaintQueryResponseVo {

    private Integer id;

    private String storeName;

    private String content;

    private Integer storeId;

    private LocalDateTime gmtCreate;

    private Integer status;

    @Override
    public String toString() {
        return "ComplaintQueryResponseVo{" +
                "id=" + id +
                ", storeName='" + storeName + '\'' +
                ", content='" + content + '\'' +
                ", storeId=" + storeId +
                ", gmtCreate=" + gmtCreate +
                ", status=" + status +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
