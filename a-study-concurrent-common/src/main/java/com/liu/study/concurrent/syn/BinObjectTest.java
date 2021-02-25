package com.liu.study.concurrent.syn;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/2/24 17:46
 */
public class BinObjectTest {

    static PublicObject publicObject = new PublicObject();

    /**
     *      00  ：轻量级锁
     *      10  ：重量级锁
     *      11  ：GC
     *   0  01  ：无锁
     *   1  01  ：偏向锁
     */
    public static void main(String[] args) throws Exception {

        // test1();

        test2();
    }

    public static void test1() throws InterruptedException {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        /**
         * 如果没有调用hashCode，对象头就没有hashCode的记录
         */
        System.out.println(ClassLayout.parseInstance(publicObject).toPrintable());
        System.out.println(publicObject.hashCode());
        /**
         * 如果调用了hashCode，会把对象的hashCode加入到对象头中。
         */
        System.out.println(ClassLayout.parseInstance(publicObject).toPrintable());

        /**
         * 测试分为一个线程和两个线程。
         *      1、两个线程交替进入同步代码块
         *      2、两个线程同时进入代码块
         */
        TimeUnit.SECONDS.sleep(4L);
        new Thread(synchronizedTest).start();
        TimeUnit.SECONDS.sleep(10L);
        new Thread(synchronizedTest).start(); // 如果测试轻量级锁，注释本行。
    }

    /**
     * JVM启动时会进行一系列的复杂活动，比如装载配置，系统类初始化等等。
     * 在这个过程中会使用大量synchronized关键字对对象加锁，且这些锁大多数都不是偏向锁。
     * 为了减少初始化时间，JVM默认延时加载偏向锁。这个延时的时间大概为4S左右，具体时间因机器而异。
     *
     * 如果把这个sleep去掉，或者时间小于一个特定的时间（一般在4S左右），都是无锁状态。
     */
    public static void test2() {
        try {
            Thread.sleep(10000L);
            PublicObject publicObject = new PublicObject();
            System.out.println(ClassLayout.parseInstance(publicObject).toPrintable());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new BiasedLockTest()).start();
    }

    /**
     * 用于测试锁的变化，
     *      无线程竞争时，并且是第一次进入，是轻量级锁。
     *      有多线程竞争时，是重量级锁。
     */
    static class SynchronizedTest implements Runnable{
        @Override
        public void run() {
            System.out.println(ClassLayout.parseInstance(publicObject).toPrintable());
            System.out.println("############################################");
            synchronized(publicObject) {
                try {
                    System.out.println("线程id:" + Thread.currentThread().getId());
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(ClassLayout.parseInstance(publicObject).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class BiasedLockTest implements Runnable{
        final PublicObject biasedLock = new PublicObject();

        @Override
        public void run() {
            for (int i = 1; i < 4; i++) {
                testMethod(i);
            }
        }

        /**
         * 用于多次重复进入同步代码块，模拟一个线程多次获取同一把锁。
         */
        public void testMethod(int i) {
            synchronized(biasedLock) {
                System.out.println("##############  " + i + "   ###################");
                System.out.println(ClassLayout.parseInstance(biasedLock).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("asdfasdfa");
        }
    }

    // ###########################  下面是不相关的  #########################
    /**
     * 对象序列化。把java对象序列化为流。
     */
    public static void serializableTest() throws IOException {
        PublicObject publicObjectTest = new PublicObject();

        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(publicObjectTest);
        byte[] bytes = byteOutputStream.getBytes();

        synchronized (publicObjectTest) {
            StringBuilder sb = new StringBuilder();
            for (byte item : bytes) {
                String transResult = String.format("%8s", Integer.toBinaryString(item & 0xFF)).replace(' ', '0');
                sb.append(transResult);
            }
            System.out.println(sb.toString());
        }
    }

    static class PublicObject {

    }
}
