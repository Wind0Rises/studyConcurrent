package com.liu.study.concurrent.custom.pool;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 自定义线程池。手写线程池。。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/24 13:10
 */
public class CustomThreadPool implements CustomExecutor {

    private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(8);

    @Override
    public void execute(Runnable runnable) {

    }
}