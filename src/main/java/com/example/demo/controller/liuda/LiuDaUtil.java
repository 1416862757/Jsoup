package com.example.demo.controller.liuda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * @Date 17:33 2020/3/26
 * @Author Nika
 */
public class LiuDaUtil {
    public static Elements getElementsListByLiuDa(String url) {
        try {
            Document document = Jsoup.parse(new URL(url), 100000);
            Elements list = document.getElementById("readerlist").getElementsByTag("a");
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取章节类容-溜达小说
     *
     * @Date 17:27 2020/3/26
     * @Author Nika
     */
    public static String getTitleAndContentByLiuDa(String url) {
        try {
            Document item = Jsoup.parse(new URL(url), 100000);
            String title = item.getElementsByTag("h1").text();
            title = title.substring(title.indexOf("第") > 0 ? title.indexOf("第") : 0) + "\n\r";
            String content = item.getElementById("content").text() + "\n\r";
            return title + content;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
