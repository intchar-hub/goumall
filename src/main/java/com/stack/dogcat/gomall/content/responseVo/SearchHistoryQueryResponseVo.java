package com.stack.dogcat.gomall.content.responseVo;

import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/13 9:31
 * @Descrition TODO
 */
public class SearchHistoryQueryResponseVo {

    private Integer searchHistoryId;

    private String content;

    private LocalDateTime gmtCreate;

    @Override
    public String toString() {
        return "SearchHistoryQueryResponseVo{" +
                "searchHistoryId=" + searchHistoryId +
                ", content='" + content + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getSearchHistoryId() {
        return searchHistoryId;
    }

    public void setSearchHistoryId(Integer searchHistoryId) {
        this.searchHistoryId = searchHistoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
