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
    public static final String FILE_PATH = "D:\\";

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
            fos = new FileOutputStream(ArticleThread.FILE_PATH + name + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {

        for (int i = 0; i < articleList.size(); i++) {
            if (name.equals("book1")) {
                System.out.println("已完成：" + (i + 1) + "%");
            }
            String articleUrl = null;
            if (ArticleController.PLAT.equals("KY")) {
                articleUrl = "https://www.kuaiyankanshu.net" + articleList.get(i).attr("href");
            } else {
                articleUrl = "https://www.biquge.biz" + articleList.get(i).attr("href");
            }
            writeArticle(articleUrl);
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("TIME：" + (System.currentTimeMillis()-startTime)/1000.0/60.0);
        ArticleController.countDown();
    }

    public void writeArticle(String articleUrl) {
        try {
            Document document = Jsoup.parse(new URL(articleUrl), 1000000);
            Elements h1 = null;
            Element content = null;
            if (ArticleController.PLAT.equals("KY")) {
                Elements bookName = document.getElementsByClass("title");
                h1 = bookName.get(0).getElementsByTag("h1");
                content = document.getElementById("chaptercontent");
            } else {
                Elements bookName = document.getElementsByClass("bookname");
                h1 = bookName.get(0).getElementsByTag("h1");
                content = document.getElementById("content");

            }
            String title = h1.get(0).text();
            if (!title.startsWith("第") && title.contains("第")) {
                try {
                    title = title.substring(title.indexOf("第"));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(title);
                }
            } else {
                title = "第" + title;
            }
            fos.write((title + "\r\n").getBytes());
            fos.write((content.text() + "\r\n").getBytes());

//            System.out.println(this.name + "-----------" + h1.get(0).text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
