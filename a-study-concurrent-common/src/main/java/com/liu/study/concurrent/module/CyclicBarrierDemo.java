package com.liu.study.concurrent.module;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * CyclicBarrier：比如一个小组有六个人，这六个做一件事，把这一件事拆分成六个任务，只有没有人的任务都完成以后，这件事
 *                才能算完成。这一件事情完成以后可能会有对应的事件触发。
 *                如果没有某一或者几个比较牛逼，提前把自己的任务做完了，那么她必须要等待，调用cyclicBarrier#await()等待其他
 *                做的比较慢的人。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/12/27 14:25
 */
public class CyclicBarrierDemo {

    /**
     * 没有回调的CyclicBarrier。
     */
    private static CyclicBarrier noCallback = new CyclicBarrier(2);

    /**
     * 有回调的CyclicBarrier。
     */
    private static CyclicBarrier hasCallback = new CyclicBarrier(2, () -> {
        System.out.println("事情做完了---【回调】");
    });


    /**
     * 常用方法：
     *      await()：
     *      reset():
     *      isBroken():
     *      getParties():
     *      getNumberWaiting():
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        // commonUseMethod();

        hasCallbackCyclicBarrier();
    }

    /**
     * 通用的测试。
     */
    public static void commonUseMethod() {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "，任务A做完了");

                noCallback.await();
                System.out.println("A事情做完了....");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "做任务A的人").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + "，任务B做完了");

                noCallback.await();
                System.out.println("B事情做完了....");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "做任务B的人").start();
    }

    /**
     * 有回调的函数的CyclicBarrier。
     *
     */
    public static void hasCallbackCyclicBarrier() {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "，任务A做完了");

                hasCallback.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "做任务A的人").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + "，任务B做完了");

                hasCallback.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "做任务B的人").start();
    }


    class TestRunnable implements Runnable {

        @Override
        public void run() {
            // cyclicBarrier.
        }
    }

}
