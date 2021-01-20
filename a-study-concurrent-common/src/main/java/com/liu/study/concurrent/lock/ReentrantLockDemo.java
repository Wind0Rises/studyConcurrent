package com.liu.study.concurrent.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * 可重入锁的使用。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/12/27 14:27
 */
public class ReentrantLockDemo {

    /**
     * ReentrantLock：默认为非公平锁。
     */
    private static ReentrantLock noFairLock = new ReentrantLock();

    /**
     * ReentrantLock：默认为非公平锁。
     */
    private static ReentrantLock fairLock = new ReentrantLock(true);

    private static final String KEY = "KEY";

    private static Map<String, Integer> memoryOperatorObject = new HashMap<String, Integer>(){{put(KEY, 0);}};

    private static List<Integer> fairLockList = new ArrayList<>();

    private static List<Integer> noFairLockList = new ArrayList<>();

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /**
         * 基本使用，加锁，释放锁
         */
        // firstTestBasisUse();

        /**
         * 公平锁、非公平锁测试。
         */
        fairOrNoFairTest();
    }

    /**
     * Lock高并发场景下案例一。
     */
    public static void firstTestBasisUse() throws Exception {
        simulationConcurrent(false);
        memoryOperatorObject.put(KEY, 0);
        simulationConcurrent(true);
    }

    /**
     * 公平与非公平锁测试。
     *
     * @throws Exception
     */
    public static void fairOrNoFairTest() throws Exception {
        IntStream.range(0, 500).forEach(item -> {
            new Thread(() -> {
                lockNoFairOperator(true, () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    noFairLockList.add(Integer.valueOf(Thread.currentThread().getName()));
                });
            }, item + "").start();

            try {
                TimeUnit.MILLISECONDS.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.SECONDS.sleep(10L);
        final List<Integer> noFairLockList = ReentrantLockDemo.noFairLockList;
        for (int i = 0; i < noFairLockList.size(); i++) {
            if (i != noFairLockList.get(i)) {
                System.out.println("-------------------------" + i);
            }
        }
        System.out.println(noFairLockList);
    }


    /**
     * 模拟高并发场景。
     */
    private static void simulationConcurrent(Boolean needLock) throws Exception {
        int threadNum = 1000;

        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        CountDownLatch triggerCountDownLatch = new CountDownLatch(1);

        IntStream.range(0, threadNum).forEach(item -> {
            new Thread(() -> {
                countDownLatch.countDown();
                try {
                    triggerCountDownLatch.await();

                    lockNoFairOperator(needLock, () -> {
                        Integer oldValue = memoryOperatorObject.get(KEY);
                        Integer newValue = oldValue + 1;
                        memoryOperatorObject.put(KEY, newValue);
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }, "线程-" + item).start();
        });

        countDownLatch.await();
        triggerCountDownLatch.countDown();

        TimeUnit.SECONDS.sleep(5L);
        System.out.println("【" + (needLock ? "加锁" : "不加锁") + "】最后的结果为：" + memoryOperatorObject.get(KEY));
    }

    /**
     * 非公平锁。
     *
     * @param needLock
     * @param runnable
     */
    private static void lockNoFairOperator(Boolean needLock, Runnable runnable) {
        if (needLock) {
            try {
                noFairLock.lock();
                runnable.run();
            } catch (Exception e) {

            } finally {
                noFairLock.unlock();
            }
        } else {
            runnable.run();
        }
    }

    /**
     * 公平锁。
     *
     * @param needLock
     * @param runnable
     */
    private static void lockFairOperator(Boolean needLock, Runnable runnable) {
        if (needLock) {
            try {
                fairLock.lock();
                runnable.run();
            } catch (Exception e) {

            } finally {
                fairLock.unlock();
            }
        } else {
            runnable.run();
        }
    }


}
