package com.example.demo.controller;

import com.example.demo.controller.liuda.LiuDaUtil;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @Date 16:46 2020/3/26
 * @Author Nika
 */
public class TestThread implements Runnable {

    private Thread thread;
    private List<Element> articleList;
    private Integer key;

    public TestThread(List<Element> articleList, Integer key) {
        this.articleList = articleList;
        this.key = key;
    }

    @Override
    public void run() {
        articleList.forEach(article -> {
            String content = LiuDaUtil.getTitleAndContentByLiuDa("http://www.liudatxt.com" + article.attr("href"));
            Test.map.put(this.key, Test.map.get(this.key) == null ? content : (Test.map.get(this.key) + content));
        });
        Test.threadCount.countDown();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
}
