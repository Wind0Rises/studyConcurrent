package com.liu.study.concurrent.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/25 17:28
 */
public class AQSShareDemo {

    private static class CustomSyn extends AbstractQueuedSynchronizer {

        private int tryAcquireSharedResult = -1;

        @Override
        protected int tryAcquireShared(int arg) {
            return tryAcquireSharedResult;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return true;
        }

        public void setTryAcquireSharedResult(int tryAcquireSharedResult) {
            this.tryAcquireSharedResult = tryAcquireSharedResult;
        }
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /**
         *
         */
        shareSingleThreadTest();
    }

    /**
     * {@link CustomSyn#tryReleaseShared(int)}返回-1。
     */
    private static void shareSingleThreadTest() throws Exception {
        CustomSyn customSyn = new CustomSyn();

        new Thread(() -> {
            customSyn.acquireShared(1);
            System.out.println(Thread.currentThread().getName() + "获取到共享锁");
        }, "线程一").start();

        TimeUnit.SECONDS.sleep(1);
    }

    /**
     *
     */
    private static void shareMultiThreadTest() throws Exception {
        CustomSyn customSyn = new CustomSyn();

        new Thread(() -> {
            customSyn.acquireShared(1);
            System.out.println(Thread.currentThread().getName() + "获取到共享锁");
        }, "线程一").start();

        TimeUnit.SECONDS.sleep(4);

        new Thread(() -> {
            customSyn.acquireShared(1);
            System.out.println(Thread.currentThread().getName() + "获取到共享锁");
        }, "线程二").start();

        TimeUnit.SECONDS.sleep(4);

        new Thread(() -> {
            customSyn.acquireShared(1);
            System.out.println(Thread.currentThread().getName() + "获取到共享锁");
        }, "线程三").start();


        TimeUnit.SECONDS.sleep(1);
    }
}
