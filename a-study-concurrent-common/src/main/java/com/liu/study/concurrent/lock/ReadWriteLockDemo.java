package com.liu.study.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/26 16:42
 */
public class ReadWriteLockDemo {

    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    private static Lock readLock = readWriteLock.readLock();

    /**
     * 写锁。
     */
    private static Lock writeLock = readWriteLock.writeLock();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     *
     */
    public static void firstTest() {
    }

}
