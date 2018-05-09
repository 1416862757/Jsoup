package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
/**
 * Created by Administrator on 2018/04/03.
 */

public class UserController {

    public static void main(String[] args) {
//        String html = getHtml("https://www.35xs.com/book/184977/");
        try {
            Document document = Jsoup.parse(new URL("https://www.35xs.com/book/184977/"), 10000);
            Elements mulu_list = document.getElementsByClass("mulu_list");
            Element element = mulu_list.get(0);
            Elements li = element.getElementsByTag("a");

            FileWriter writer = new FileWriter("D:\\aaa.txt");

            for(int i = 0; i < li.size(); i++){
                String section = getArticleHtml("https://www.35xs.com" + li.get(i).attr("href").toString());
                writer.write(section);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getArticleHtml(String article){
        try {
            Document document = Jsoup.parse(new URL(article), 10000);
            Elements elements = document.getElementsByTag("p");
            Elements h1 = document.getElementsByTag("h1");

            StringBuilder sb = new StringBuilder();
            sb.append(h1.get(0).text() + "\r\n");
            for(int i = 1; i < elements.size(); i++){
                sb.append(elements.get(i).text()).append("\r\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return "文章获取失败！";
    }

}
