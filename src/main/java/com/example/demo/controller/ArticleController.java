package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Administrator on 2018/04/03.
 */

public class ArticleController {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            Document document = Jsoup.parse(new URL("https://www.biquge.cc/html/235/235098/"), 100000);
            Element list = document.getElementById("list");
            Elements li = list.getElementsByTag("a");

            new ArticleThread(li.subList(12, 512), "1", start).start();
            new ArticleThread(li.subList(512, 1012), "2", start).start();
            new ArticleThread(li.subList(1012, 1512), "3", start).start();
            new ArticleThread(li.subList(1512, li.size()), "4", start).start();

//            for(int i = 12; i < 22; i++){
//                String articleUrl = "https://www.biquge.cc/html/235/235098/" + li.get(i).attr("href").toString();
//                executorService1.execute(new ArticleThread(articleUrl));
//            }
//            executorService2.shutdown();
//            ArticleThread.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
