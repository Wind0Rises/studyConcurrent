package com.liu.study.concurrent.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Exchanger;

/**
 * Exchanger：两个线程进行数据交换的工具。线程A把自己需要传递的数据传递给线程B，同时，线程B把自己需要传递的数据传递给线程A，这个过程是同时进行的。
 *
 * <note>
 *     线程A把自己的数据传递给线程B，线程B获取到线程A传递的数据，这两个数据是同一对象，并不是copy的数据，这样的数据在两个线程中存在，
 *     就会存在线程安全的问题。
 * </note>
 *
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/12/27 14:53
 */
public class ExchangerDemo {

    public static void main(String[] args) {

        // commonUseMethod();

        oneThreadExchangeObjectIsNull();
    }

    /**
     * 通用的方法。
     */
    public static void commonUseMethod() {

        Exchanger<Student> exchanger = new Exchanger<>();

        /**
         * 线程A
         */
        new Thread(() -> {
            Student studentA = new Student("[线程A]中创建的数据。");
            try {
                Student exchange = exchanger.exchange(studentA);
                System.out.println("线程A获取的交换的数据：" + exchange);
                System.out.println("===========" + exchange.hashCode());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程A").start();

        /**
         * 线程B
         */
        new Thread(() -> {
            Student studentB = new Student("[线程B]中创建的数据。");
            System.out.println("===========" + studentB.hashCode());
            try {
                Student exchange = exchanger.exchange(studentB);
                System.out.println("线程B获取的交换的数据：" + exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程A").start();
    }

    /**
     * 其中一个线程exchange的对象是null。
     * 可以正常执行。
     */
    public static void oneThreadExchangeObjectIsNull() {
        Exchanger<Student> exchanger = new Exchanger<>();

        /**
         * 线程A
         */
        new Thread(() -> {
            Student studentA = new Student("[线程A]中创建的数据。");
            try {
                Student exchange = exchanger.exchange(studentA);
                System.out.println("线程A获取的交换的数据：" + exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程A").start();

        /**
         * 线程B
         */
        new Thread(() -> {
            try {
                Student exchange = exchanger.exchange(null);
                System.out.println("线程B获取的交换的数据：" + exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程A").start();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Student {
        String username;
    }

}
