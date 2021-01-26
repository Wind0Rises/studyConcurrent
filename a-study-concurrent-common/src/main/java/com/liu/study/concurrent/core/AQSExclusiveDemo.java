package com.liu.study.concurrent.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 核心模块，AQS理解
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/25 13:12
 */
public class AQSExclusiveDemo {

    private static class CustomSyn extends AbstractQueuedSynchronizer {

        private Boolean tryAcquireResult = false;

        @Override
        protected boolean tryAcquire(int arg) {
            return tryAcquireResult;
        }

        @Override
        protected boolean tryRelease(int arg) {
            return true;
        }

        public void setTryAcquireResult(Boolean tryAcquireResult) {
            this.tryAcquireResult = tryAcquireResult;
        }
    }

    public static void main(String[] args) throws Exception{
        /**
         * 排他同步器。
         */
        // exclusiveSynSingleNodeTest();

        /**
         *
         */
        // exclusiveSynMultiNodeTest();

        /**
         *
         */
        exclusiveSynSingleReleaseTest();
    }

    /**
     * 把{@link CustomSyn#tryRelease(int)}返回值改为false；
     *
     * 排他模式——单个Node流程查看。
     */
    public static void exclusiveSynSingleNodeTest() throws Exception {

        CustomSyn customSyn = new CustomSyn();

        new Thread(() -> {
            customSyn.acquire(1);
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }, "线程一").start();



        Thread.sleep(1000);
        System.out.println("getExclusiveQueuedThreads：" + customSyn.getExclusiveQueuedThreads());
        System.out.println("getFirstQueuedThread：" + customSyn.getFirstQueuedThread());
        System.out.println("getQueuedThreads：" + customSyn.getQueuedThreads());
    }

    /**
     * 把{@link CustomSyn#tryRelease(int)}返回值改为false；
     *
     * 排他模式——多个Node流程查看。
     */
    public static void exclusiveSynMultiNodeTest() throws Exception {
        CustomSyn customSyn = new CustomSyn();

        new Thread(() -> {
            customSyn.acquire(1);
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }, "线程一").start();

        new Thread(() -> {
            customSyn.acquire(1);
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }, "线程二").start();

        new Thread(() -> {
            customSyn.acquire(1);
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }, "线程三").start();


        Thread.sleep(1000);
        System.out.println("getExclusiveQueuedThreads：" + customSyn.getExclusiveQueuedThreads());
        System.out.println("getFirstQueuedThread：" + customSyn.getFirstQueuedThread());
        System.out.println("getQueuedThreads：" + customSyn.getQueuedThreads());
    }

    /**
     * 把{@link CustomSyn#tryRelease(int)}返回值改为true；
     */
    public static void exclusiveSynSingleReleaseTest() throws Exception {
        CustomSyn customSyn = new CustomSyn();

        new Thread(() -> {
            customSyn.acquire(1);
            System.out.println(Thread.currentThread().getName() + "-- 加锁成功，执行同步代码");
        }, "线程一").start();

        TimeUnit.SECONDS.sleep(2);
        customSyn.setTryAcquireResult(true);

        new Thread(() -> {
            customSyn.acquire(1);

            System.out.println(Thread.currentThread().getName() + "-- 加锁成功，执行同步代码");

            customSyn.release(1);

            System.out.println(Thread.currentThread().getName() + "-- 释放成功，执行同步代码");
        }, "线程二").start();
    }

    /**
     *
     */
    private static void exclusiveSynInterruptible() throws Exception {
        CustomSyn customSyn = new CustomSyn();
    }

}
