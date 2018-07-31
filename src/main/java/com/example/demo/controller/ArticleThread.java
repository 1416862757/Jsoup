package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ArticleThread implements Runnable {

    private Thread thread;
    private List<Element> articleList;
    private FileOutputStream fos = null;
    private String name;
    private Long startTime;

    public ArticleThread(List<Element> articleList, String name, Long startTime) {
        this.articleList = articleList;
        this.name = name;
        this.startTime = startTime;
        try {
            fos = new FileOutputStream("D:\\" + name + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {

        for(int i = 0; i < articleList.size(); i++){
            String articleUrl = "https://www.biquge.cc/html/235/235098/" + articleList.get(i).attr("href").toString();
            writeArticle(articleUrl);
        }
        System.out.println("TIME：" + (System.currentTimeMillis()-startTime)/1000.0/60.0);

    }

    public void writeArticle(String articleUrl){
        try {
            Document document = Jsoup.parse(new URL(articleUrl), 1000000);
            Elements bookName = document.getElementsByClass("bookname");
            Elements h1 = bookName.get(0).getElementsByTag("h1");
            Element content = document.getElementById("content");

            fos.write(new String(h1.get(0).text() + "\r\n").getBytes());
            fos.write(new String(content.text()).getBytes());

            System.out.println(this.name + h1.get(0).text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
