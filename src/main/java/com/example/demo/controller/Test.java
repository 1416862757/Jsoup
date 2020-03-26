package com.example.demo.controller;

import com.example.demo.controller.entity.Article;
import com.example.demo.controller.liuda.LiuDaUtil;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

/**
 * @Date 15:57 2020/3/26
 * @Author Nika
 */
public class Test {
    public static Article article = new Article("元尊", "http://www.liudatxt.com/so/8315/", "LIUDA");

    public static Map<Integer, String> map = new TreeMap<>();

    // 下载线程控制
    public static CountDownLatch threadCount = null;

    // 线程数量
    private static Integer count = 1;

    private static final Integer SPEED = 100;

    public static void main(String[] args) throws IOException {
        Long startTime = System.currentTimeMillis();
        Elements list = LiuDaUtil.getElementsListByLiuDa(article.getUrl());

        for (int i = 0; i < list.size(); i += SPEED) {
            int end = i + SPEED;
            if (i + SPEED > list.size()) {
                end = list.size();
            }
            new TestThread(list.subList(i, end), i).start();
            count++;
        }
        threadCount = new CountDownLatch(count - 1);
        try {
            threadCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = new FileOutputStream("E:\\" + article.getName() + ".txt");
        for (Integer key : map.keySet()) {
            fos.write((map.get(key)).getBytes());
        }
        fos.close();
        System.out.println((System.currentTimeMillis() - startTime) / 1000);
    }

}
