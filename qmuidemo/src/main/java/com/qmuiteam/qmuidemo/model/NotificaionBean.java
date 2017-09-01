package com.qmuiteam.qmuidemo.model;

/**
 * Created by andy on 2017/9/1.
 */

public class NotificaionBean {
    String title;
    String content;

    public NotificaionBean(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
