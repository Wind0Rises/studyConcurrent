package com.liu.study.concurrent;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 *
 *
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/24 11:47
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        firstMethod();
    }

    private static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);

    public static void firstMethod() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6,
                30, TimeUnit.SECONDS, queue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("直接拒绝：" + executor);
                System.out.println("直接拒绝：" + r);
            }
        });

        System.out.println("开始前ActiveCount：" + executor.getActiveCount());
        IntStream.range(0, 8).forEach((item) -> {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        System.out.println("开始后ActiveCount：" + executor.getActiveCount());
        System.out.println("开始后QueueSize：" + executor.getQueue().size());

        while (true) {
            int activeCount = executor.getActiveCount();
            System.out.println("循环---ActiveCount：" + activeCount);
            // -- 这个线程
            System.out.println("循环---poolSize：" + executor.getPoolSize());
            System.out.println("循环---QueueSize：" + executor.getQueue().size());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}