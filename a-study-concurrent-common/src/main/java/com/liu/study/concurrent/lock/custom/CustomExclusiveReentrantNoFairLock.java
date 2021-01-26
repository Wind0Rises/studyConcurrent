package com.liu.study.concurrent.lock.custom;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * 自定义实现锁——排他，可重入、非公平锁。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/25 13:01
 */
public class CustomExclusiveReentrantNoFairLock implements Lock, Serializable {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        testLock(false);

        testLock(true);
    }

    /**
     *
     * @throws Exception
     */
    public static void testLock(Boolean needLock) throws Exception {
        CustomExclusiveReentrantNoFairLock lock = new CustomExclusiveReentrantNoFairLock();
        ArrayList<Integer> container = new ArrayList<>();
        container.add(0);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    if (needLock) {
                        lock.lock();
                    }
                    Integer sourceValue = container.get(0);
                    sourceValue = sourceValue + 1;
                    container.set(0, sourceValue);
                } finally {
                    if (needLock) {
                        lock.unlock();
                    }
                }
            }, "线程" + i).start();
        }

        TimeUnit.SECONDS.sleep(5);
        System.out.println((needLock ? "需要加锁" : "不加锁") + container.get(0));
    }
    

    private Syn syn = new Syn();

    static class Syn extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            final Thread thread = Thread.currentThread();

            /**
             * 这个状态码的含义：
             *      0 - 标识没有加锁操作。
             */
            int state = getState();

            if (0 == state) {
                if (compareAndSetState(0, arg)) {
                    setExclusiveOwnerThread(thread);
                    return true;
                }
            } else if (thread == getExclusiveOwnerThread()){
                int next = state + arg;
                if (next < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(next);
                return false;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }

            int next = getState() - arg;
            boolean result = false;
            if (next == 0) {
                result = true;
                setExclusiveOwnerThread(null);
            }

            setState(next);
            return result;
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        syn.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        syn.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return syn.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return syn.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        syn.release(1);
    }

    @Override
    public Condition newCondition() {
        return syn.newCondition();
    }
}
