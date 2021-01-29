package com.liu.study.concurrent.module;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Phaser：是一个可以重用的同步barrier，它的功能与 CountDownLatch、CyclicBarrier 相似，
 * 但是使用起来更加灵活。可以用来解决控制多个线程分阶段共同完成任务的情景问题。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/24 14:53
 */
public class PhaserDemo {

    /**
     * register()：单个注册。
     * bulkRegister()：批量注册。
     * arrive()：到达。
     * arriveAndAwaitAdvance()：到达并等待其他的达到。
     * arriveAndDeregister()：到达并取消注册。
     * onAdvance()：
     */
    public static void main(String[] args) throws Exception {

        /**
         * Phaser：基本的使用。
         */
        // phaserBasisUse();

        /**
         *
         */
        // exampleOfOtherPeople();

        /**
         *
         */
        unconventionalityMethod();
    }

    /**
     * Phaser基础使用。
     */
    public static void phaserBasisUse() {
        Phaser phaser = new Phaser();

        IntStream.rangeClosed(1, 3).forEach(item -> {
            new Thread(() -> {
                // 注册一个屏障。
                phaser.register();

                int awaitTime = new Random().nextInt(10);
                try {
                    TimeUnit.SECONDS.sleep(awaitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "等待时间为：" + awaitTime);
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + "第二阶段完成");
                phaser.arriveAndDeregister();
                System.out.println(Thread.currentThread().getName() + "完成");
            }, "线程" + item).start();
        });
    }


    /**
     * 其他的例子。
     */
    private static void exampleOfOtherPeople() {
        Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println(LocalDateTime.now() + ": onAdvance，registeredParties=" + getRegisteredParties() + ", phase=" + getPhase() + ", isTerminated=" + isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
                return super.onAdvance(phase, registeredParties);
            }
        };

        System.out.println(LocalDateTime.now() + ": 主线程开始执行异步任务，registeredParties=" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
        phaser.register();
        for (int i = 0; i < 5; i++) {
            phaser.register();
            System.out.println(LocalDateTime.now() + ": 注册一个屏障，registeredParties=" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
            int finalI = i;
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(finalI);
                    System.out.println(LocalDateTime.now() + ": 到达屏障，等待其他线程，" + finalI + ", registeredParties=" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
                    phaser.arriveAndAwaitAdvance();
                    TimeUnit.SECONDS.sleep(finalI);
                    System.out.println(LocalDateTime.now() + ": 屏障打开，开始执行剩下任务，" + finalI + ", registeredParties=" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
                    phaser.arriveAndDeregister();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        phaser.arriveAndDeregister();
        System.out.println(LocalDateTime.now() + ": 主线程执行完毕，registeredParties=" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated() + ", ThreadId=" + Thread.currentThread().getId());
    }

    /**
     * 非常规的方法:
     * parties、phase等字段的数量。
     */
    public static void unconventionalityMethod() {
        Phaser phaser = new Phaser();

        IntStream.rangeClosed(1, 3).forEach(item -> {
            new Thread(() -> {
                phaser.register();
                System.out.println("++++++++   registeredParties：" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated());


                if (item == 1) {
                    phaser.arriveAndDeregister();
                }
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------   registeredParties：" + phaser.getRegisteredParties() + ", phase=" + phaser.getPhase() + ", isTerminated=" + phaser.isTerminated());

            }).start();
        });
    }

}
