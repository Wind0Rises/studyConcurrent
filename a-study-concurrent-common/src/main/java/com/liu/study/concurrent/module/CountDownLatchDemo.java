package com.liu.study.concurrent.module;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch：有一组人进行百米比赛，有一个裁判（CountDownLatch）当运动员都准备好了，进行发令操作，进行比赛。
 *                 只有等每一个准备的好了，裁判才能发令，进行比赛。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/12/27 14:25
 */
public class CountDownLatchDemo {

    /**
     * 倒计时。
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(2);


    /**
     * 常用的方法：
     *      await()：
     *      countDown():
     *      getCount():
     *
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        commonUseMethodCountDownLatch();

    }

    /**
     * 通用的使用方法。
     */
    public static void commonUseMethodCountDownLatch() throws InterruptedException {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                System.out.println(Thread.currentThread().getName() + "---准备好了");

                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "运动员A").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10L);
                System.out.println(Thread.currentThread().getName() + "---准备好了");

                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "运动员B").start();


        countDownLatch.await();
        System.out.println("------   裁判发令了-----比赛开始   ---------");

    }

}
