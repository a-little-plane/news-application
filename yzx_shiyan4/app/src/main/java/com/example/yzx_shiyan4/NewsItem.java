package com.example.yzx_shiyan4;

import java.io.Serializable;

//存储数据的容器类，每个页面的最小子类
public class NewsItem implements Serializable {
    private String title;
    private String time;
    private String href;
    private String imgSrc;

    public NewsItem(String title, String time, String href, String imgSrc) {
        this.title = title;
        this.time = time;
        this.href = href;
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        return String.format("Title=%s\nTime=%s\nHref=%s\nImageSrc=%s\n",title,time,href,imgSrc);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
