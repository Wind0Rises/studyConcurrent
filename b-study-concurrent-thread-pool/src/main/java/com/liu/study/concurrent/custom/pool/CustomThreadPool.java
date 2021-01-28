package com.liu.study.concurrent.custom.pool;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/24 13:10
 */
public class CustomThreadPool implements CustomExecutor {

    private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(8);

    @Override
    public void execut(Runnable runnable) {

    }
}