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
    public static final String FILE_PATH = "D:\\novel\\";

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
            fos = new FileOutputStream(ArticleThread.FILE_PATH  + name + ".txt");
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
            String articleUrl = "https://www.biquge.biz" + articleList.get(i).attr("href");
            writeArticle(articleUrl);
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TIME：" + (System.currentTimeMillis()-startTime)/1000.0/60.0);
        ArticleController.countDown();
    }

    public void writeArticle(String articleUrl){
        try {
            Document document = Jsoup.parse(new URL(articleUrl), 1000000);
            Elements bookName = document.getElementsByClass("bookname");
            Elements h1 = bookName.get(0).getElementsByTag("h1");
            Element content = document.getElementById("content");
            String title = h1.get(0).text();
//            if (!title.startsWith("第")) {
//                title = title.substring(title.indexOf("第"));
//            }
            fos.write(new String(title + "\r\n").getBytes());
            fos.write(new String(content.text() + "\r\n").getBytes());

//            System.out.println(this.name + "-----------" + h1.get(0).text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
