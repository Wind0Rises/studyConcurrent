package com.liu.study.concurrent.custom.queue;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/9/27 15:57
 */
public class BlockingDequeDemo {

    public static void main(String[] args) {
        // getElementThrowErrorTest();

        getElementReturnNullTest();
    }

    public static void getElementThrowErrorTest() {
        LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>();
        try {
            String element = deque.element();
        } catch (Exception e) {
            System.err.println("element---如果双端队列中没有元素，报错：" + e);
        }

        try {
            String first = deque.getFirst();
        } catch (Exception e) {
            System.err.println("getFirst---如果双端队列中没有元素，报错：" + e);
        }

        try {
            String element = deque.element();
        } catch (Exception e) {
            System.err.println("getLast---如果双端队列中没有元素，报错：" + e);
        }

        deque.add("p--");
        System.out.println(deque.element());
        System.out.println(deque);
    }

    public static void getElementReturnNullTest() {
        LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>();
        String peek = deque.peek();
        System.out.println("peek---如果双端队列没有元素，返回null：" + peek);
    }

}