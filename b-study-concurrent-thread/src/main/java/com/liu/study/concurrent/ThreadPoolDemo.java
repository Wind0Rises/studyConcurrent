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
        // firstMethod();
        // secondMethod();
        // threeMethod();
        fourthMethod();
    }

    public static void fourthMethod() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6,
                10, TimeUnit.SECONDS, queue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("直接拒绝：" + executor);
                System.out.println("直接拒绝：" + r);
            }
        });

        /**
         * 清除线程池中等待队里中的状态为删除状态的Future。
         */
        executor.purge();

        IntStream.range(0, 8).forEach(item -> executor.execute(() -> {
            long l = System.currentTimeMillis();
            System.out.println("---------------" + l);
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("###############" + l);
        }));

        /**
         * 设置核心线程数可以过期。
         * getPoolSize()：获取线程池中的线程数。（超过coreSize + queueSize的时候，会创建新的线程）
         * getActiveCount()：获取线程池中正在执行task的线程数。
         *
         *
         */
        executor.allowCoreThreadTimeOut(true);  // 如果在空闲的时候，并且空闲时间大于keepAliveTime时。

        /**
         * 启动所有的核心线程，使它们空闲地等待工作。
         *  这将覆盖仅在执行新任务时启动核心线程的默认策略。
         */
        executor.prestartAllCoreThreads();


        int i = 1;
        for (;;) {

            System.out.println("poolSize：" + executor.getPoolSize() + "；队列大小：" +
                    executor.getQueue().size() + "；活跃的总数：" + executor.getActiveCount() + ", i : " + i++);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

        /**
         * 加入：
         *      2个核心线程
         *      5个队列
         *      超过7个开始创建线程。
         *      最大6个 + 5个队列 超哥11个就走拒绝策略。
         */
        System.out.println("开始前ActiveCount：" + executor.getActiveCount());
        IntStream.range(0, 9).forEach((item) -> {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
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

    /**
     * 如果线程池是shutdown状态，添加任务不会报错，直接走拒绝策略。shutdown状态无法条件新的任务。
     */
    public static void secondMethod() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6,
                30, TimeUnit.SECONDS, queue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("直接拒绝：" + executor);
                System.out.println("直接拒绝：" + r);
            }
        });

        IntStream.range(0, 4).forEach(item -> {
            executor.submit(() -> {
                try {
                    System.out.println("开始执行-----------：" + item);
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("开始结束-----------：" + item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        System.out.println(executor.isShutdown());

        executor.shutdown();

        /**
         * 不会报错，直接走拒绝策略。shutdown状态无法条件新的任务。
         */
        executor.execute(() -> {
            System.out.println("liuweian");
        });

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(executor.isShutdown());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(executor.getPoolSize());
            System.out.println(executor.getQueue());
        }
    }

    /**
     * shutdownNow():调用之后，在添加任务时，直接拒绝，未完成的任务，会中断抛错。
     */
    public static void threeMethod() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6,
                30, TimeUnit.SECONDS, queue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("直接拒绝：" + executor);
                System.out.println("直接拒绝：" + r);
            }
        });

        IntStream.range(0, 4).forEach(item -> {
            executor.submit(() -> {
                try {
                    System.out.println("开始执行-----------：" + item);
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("开始结束-----------：" + item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });


        executor.shutdownNow();

        /**
         * 不会报错，直接走拒绝策略。shutdown状态无法条件新的任务。
         */
        executor.execute(() -> {
            System.out.println("liuweian");
        });

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(executor.isShutdown());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(executor.getPoolSize());
            System.out.println(executor.getQueue());
        }
    }


}