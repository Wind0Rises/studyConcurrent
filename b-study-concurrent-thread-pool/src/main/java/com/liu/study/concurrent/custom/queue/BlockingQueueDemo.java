package com.liu.study.concurrent.custom.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/9/3 12:55
 */
public class BlockingQueueDemo {

    static ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);

    /**
     * {@link ArrayBlockingQueue} 和 {@link java.util.concurrent.LinkedBlockingQueue}
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /**
         * 移除操作。
         */
        //removeFirstElementTest();


        /**
         * 添加元素。
         */
         //addFirstElementTest();

        /**
         * 获取元素。
         */
        // getFirstElement();

        /**
         * blockingQueue堵塞方法。
         */
        blockingMethod();
    }


    /**
     * 移除操作。
     */
    public static void removeFirstElementTest() throws InterruptedException {
        // poll方法：
        String poll = blockingQueue.poll();
        System.out.println("poll--获取【并】移除队列的第一个元素，如果没有返回null：" + poll);

        // remove方法：
        try {
            String remove = blockingQueue.remove();
        } catch (Exception e) {
            System.err.println("remove--获取【并】移除队列的第一个元素，如果没有抛出异常：" + e);
        }

        TimeUnit.SECONDS.sleep(2);

        /**
         * take：堵塞方法。
         */
        try {
            System.out.println("take--获取【并】移除队列的第一个元素，返回元素：【开始】" );
            String take = blockingQueue.take();
            System.out.println("take--获取【并】移除队列的第一个元素，返回元素：【开始】" );
        } catch (Exception e) {

        }

    }

    /**
     * 添加元素
     */
    public static void addFirstElementTest() {
        ArrayBlockingQueue blockingQueue =  new ArrayBlockingQueue(3);
        blockingQueue.add(1); blockingQueue.add(2); blockingQueue.add(3);

        try {
            boolean add = blockingQueue.add(4);
        } catch (Exception e) {
            System.err.println("add--向已经满的队列中添加元素，抛出异常：" + e);
        }

        boolean offer = blockingQueue.offer(4);
        System.out.println("offer--向已经满的队列中添加元素，返回元素：" + offer);

        /**
         * put会堵塞，直到队列有可用的位置。
         */
        try {
            System.out.println("put--向已经满的队列中添加元素，返回元素：【开始】" );
            blockingQueue.put(4);
            System.out.println("put--向已经满的队列中添加元素，返回元素：【结束】" );
        } catch (Exception e) {
            System.err.println("-------" + e);
        }
    }

    /**
     * 获取元素。
     */
    public static void getFirstElement() {

        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(3);
        String peek = queue.peek();
        System.out.println("poll--获取【并不】移除队列的第一个元素，如果没有返回null：" + peek);

        queue.add("1");
        String peekFirst = queue.peek();
        System.out.println(queue);

        ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue(3);
        try {
            String element = queue2.element();
        } catch (Exception e) {
            System.err.println("element--获取【并不】移除队列的第一个元素，如果没有抛出异常：" + e);
        }
    }

    /**
     * 堵塞方法。
     */
    public static void blockingMethod() {
        new Thread(() -> {
            ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(3);
            try {
                System.out.println("take--获取【并】移除队列的第一个元素，返回元素：【开始】" );
                String take = queue.take();
                System.out.println("take--获取【并】移除队列的第一个元素，返回元素：【开始】" );
            } catch (Exception e) {
                System.out.println("---------------" + e);
            }
        }).start();
        new Thread(() -> {
            ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(3);
            queue.add("1"); queue.add("2"); queue.add("3");
            try {
                System.out.println("put--向已经满的队列中添加元素，返回元素：【开始】" );
                queue.put("4");
                System.out.println("put--向已经满的队列中添加元素，返回元素：【结束】" );
            } catch (Exception e) {
                System.err.println("-------" + e);
            }
        }).start();
    }


    /**
     *
     */
    public static void otherMethod() {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(3);
    }
}