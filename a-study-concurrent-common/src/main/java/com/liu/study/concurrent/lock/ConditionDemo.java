package com.liu.study.concurrent.lock;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Condition：条件，非常类似Object的await、notify()方法。
 * 可以与Synchronized与Object#await、notify方法进行比对；
 * 也可以与Guava的Monitor
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/26 13:04
 */
public class ConditionDemo {

    public static void main(String[] args) {
        /**
         *
         */
        consumerAndProducerModel();


    }


    /**
     * 使用Condition实现生产者与消费者模式。
     */
    public static void consumerAndProducerModel() {
        IntStream.range(0, 3).forEach(item -> {
            new Thread(new Producer()).start();
        });

        IntStream.range(0, 3).forEach(item -> {
            new Thread(new Consumer()).start();
        });
    }

    /**
     *
     */
    public static void conditionDetail() {

    }







    public static ReentrantLock lock = new ReentrantLock();

    public static Condition producerCondition = lock.newCondition();

    public static Condition consumerCondition = lock.newCondition();

    public static ArrayList<Integer> list = new ArrayList<>();

    /**
     * 生产者。
     */
    static class Producer implements Runnable {

        @Override
        public void run() {
            for(;;) {
                try {
                    lock.lock();
                    if (list.size() < 20) {
                        list.add(new Random().nextInt(100));
                        System.out.println("生产者生产数据后，容器中数据个数：" + list.size());
                        consumerCondition.signalAll();
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
                    } else {
                        System.out.println("生产者进入等待状态！！" );
                        producerCondition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 生产者。
     */
    static class Consumer implements Runnable {

        @Override
        public void run() {
            for(;;) {
                try {
                    lock.lock();
                    if (list.size() > 0) {
                        Integer consumer = list.remove(0);
                        System.out.println("消费者消费后，容器中数据个数：" + list.size() + "   消费的数据为：" + consumer);
                        producerCondition.signalAll();
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
                    } else {
                        System.out.println("消费者进入等待状态！！" );
                        consumerCondition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
