package com.mettl.profiler.crawler;

import java.io.IOException;

public class Runner {
    public static void main(String[] args) throws IOException {
         Crawler crawler = new Crawler();
        crawler.getStackOverflowProfile();

    }
}
