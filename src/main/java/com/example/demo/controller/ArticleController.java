package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Date 11:14 2019/03/05
 * @Author Nika
 */

public class ArticleController {

//    https://www.biquge.biz/12_12453/    我的绝色总裁未婚妻
//    https://www.biquge.biz/22_22884/    女总裁的神级高手
//    https://www.biquge.biz/5_5164/    圣墟
//    https://www.biquge.biz/4_4369/    逆天透视眼
    // 小说URL
    private static final String BOOK_URL = "https://www.biquge.biz/22_22884/";

    // 小说名称
    private static final String BOOK_NAME = "女总裁的神级高手";

//    public static final String PLAT = "KY";
    public static final String PLAT = "";

    // 下载速度 100 - 500   值越小   速度越快  一个线程下载多少本
    private static final Integer SPEED = 100;

    // 下载线程控制
    private static CountDownLatch threadCount = null;

    // 线程数量
    private static Integer count = 1;

    // 下载临时书名
    private static List<String> bookName = new ArrayList();

    public static void main(String[] args) {
        System.out.println("开始时间：" + new Date(System.currentTimeMillis()));
        downloadNovel();
        threadCount = new CountDownLatch(count - 1);
        try {
            threadCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bookName.size(); i++){
            // 合并小说
            merging(bookName.get(i));
            // 删除临时文件
            delete(bookName.get(i));
        }
        System.out.println("结束时间：" + new Date(System.currentTimeMillis()));
    }

    /**
     * 合并小说
     * @param filePath
     */
    public static void merging(String filePath) {
        filePath = ArticleThread.FILE_PATH + filePath + ".txt";
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            // 获取源文件和目标文件的输入输出流
            in = new FileInputStream(filePath);
            out = new FileOutputStream(ArticleThread.FILE_PATH  + BOOK_NAME + ".txt", true);
            // 获取输入输出通道
            FileChannel fcIn = in.getChannel();
            FileChannel fcOut = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = fcIn.read(buffer);
                if (r == -1) {
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                fcOut.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null && out != null) {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除临时文件
     * @param filePath
     */
    private static void delete(String filePath){
        filePath = ArticleThread.FILE_PATH  + filePath + ".txt";
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filePath + "成功！");
            } else {
                System.out.println("删除单个文件" + filePath + "失败！");
            }
        } else {
            System.out.println("删除单个文件失败：" + filePath + "不存在！");
        }
    }

    /**
     * 利用线程下载小说
     */
    private static void downloadNovel() {
        long start = System.currentTimeMillis();
        try {
            Document document = Jsoup.parse(new URL(BOOK_URL), 100000);
            Elements li = null;
            if (PLAT.equals("KY")){
                Elements list = document.getElementsByClass("dirlist");
                li = list.get(0).getElementsByTag("a");
            } else {
                Element list = document.getElementById("list");
                li = list.getElementsByTag("a");
            }

            for (int i = 0; i < li.size(); i += SPEED) {
                int end = i + SPEED;
                if (i + SPEED > li.size()) {
                    end = li.size();
                }
                new ArticleThread(li.subList(i, end), "book" + count, start).start();
                bookName.add("book" + count++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void countDown() {
        threadCount.countDown();
    }

    @Test
    public void test(){
        String str = "3169.第3169章 玄仙术";
        System.out.println(str.substring(str.indexOf("第")));
    }

}
