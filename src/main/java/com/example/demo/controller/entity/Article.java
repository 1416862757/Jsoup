package com.example.demo.controller.entity;

/**
 * @Date 17:37 2020/3/26
 * @Author Nika
 */
public class Article {
    private String name;
    private String url;
    private String platform;

    public Article(String name, String url, String platform) {
        this.name = name;
        this.url = url;
        this.platform = platform;
    }

    public Article() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
