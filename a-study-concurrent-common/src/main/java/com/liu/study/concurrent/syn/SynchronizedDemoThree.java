package com.liu.study.concurrent.syn;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/2/24 15:24
 */
public class SynchronizedDemoThree {

    /**
     * 001：无锁
     * 101：偏向锁
     *  00：轻量级锁
     *  10：重量级锁
     *  11：GC标记
     *
     * 无锁  ->  偏向锁   ->  轻量级锁  ->   重量级锁
     *
     *
     *
     * <note>
     *      101：偏向锁。        保存着线程ID.
     *      001：无锁。          保存着hashcode。
     *
     *      如果保存着HashCode，就不能保存线程ID，就不能从001 -> 101。
     *      只可能从 001 -> 00从无锁到轻量级锁。
     *      或者是从
     * </note>
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        /**
         * 无锁
         *
         * 需要计算一下HashCode。就能获取到无锁。
         */
        // noLock();

        /**
         * 偏向锁：
         */
        deviationLock();

        /**
         * 轻量级锁。
         */
        // lightLock();

        /**
         * 重量级锁
         */
        // heavyLock();
    }

    /**
     * 无锁对象。
     *
     * 是否是偏向锁     锁标准位
     *      0          01
     *
     *
     */
    public static void noLock() {
        ThreeObject threeObject = new ThreeObject();
        System.out.println(threeObject.hashCode());
        System.out.println(ClassLayout.parseInstance(threeObject).toPrintable());
    }


    /**
     * JVM启动时会进行一系列的复杂活动，比如装载配置，系统类初始化等等。
     * 在这个过程中会使用大量synchronized关键字对对象加锁，且这些锁大多数都不是偏向锁。
     * 为了减少初始化时间，JVM默认延时加载偏向锁。这个延时的时间大概为4S左右，具体时间因机器而异。
     *
     * 如果把这个sleep去掉，或者时间小于一个特定的时间（一般在4S左右），都是无锁状态。
     */
    /**
     * 偏向锁
     * 是否是偏向锁     锁标准位
     *      0
     *
     * -XX:+UseBiasedLocking
     *
     * java8:打开了偏向锁延迟开启，默认为4秒
     * -XX:BiasedLockingStartupDelay=0
     */
    public static void deviationLock() throws InterruptedException {
        ThreeObject threeObject = new ThreeObject();
        // System.out.println(threeObject.hashCode()); // 这个放开就有问题？就不是偏向锁，就是无锁状态了？？为什么？？？
        // 因为保存了hashcode就不能保存线程ID了.
        System.out.println(ClassLayout.parseInstance(threeObject).toPrintable());

    }

    /**
     * 轻量级锁：
     *
     */
    public static void lightLock() {
        ThreeObject threeObject = new ThreeObject();
        // 开启下一行，就是无锁 -> 轻量级锁。如果不开启，就是偏向锁 ->  轻量级锁。
        System.out.println("十六进制hashcode： " + Integer.toHexString(threeObject.hashCode()));
        System.out.println(ClassLayout.parseInstance(threeObject).toPrintable());
        System.out.println("线程ID：" + Thread.currentThread().getId());

        synchronized (threeObject) {

        }

        new Thread(() -> {
            synchronized (threeObject) {
                System.out.println(ClassLayout.parseInstance(threeObject).toPrintable());
            }
        }).start();
    }


    /**
     *
     */
    public static void heavyLock() {
        ThreeObject threeObject = new ThreeObject();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                synchronized (threeObject) {
                    try {
                        Thread.sleep(1000L);
                        System.out.println(threeObject.hashCode());
                        System.out.println(ClassLayout.parseInstance(threeObject).toPrintable());
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }






    static class ThreeObject {

    }
}
